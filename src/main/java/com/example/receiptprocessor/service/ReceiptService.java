package com.example.receiptprocessor.service;

import com.example.receiptprocessor.model.Item;
import com.example.receiptprocessor.model.Receipt;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Service
public class ReceiptService {
    private final Map<String, Receipt> receipts = new HashMap<>();

    public String processReceipt(Receipt receipt) {
        String id = UUID.randomUUID().toString();
        receipt.setId(id);
        receipts.put(id, receipt);
        return id;
    }

    public Integer calculatePoints(String id) {
    	int points = 0;
    	
    	Receipt receipt = receipts.get(id);
    	

        // Rule 1: One point for every alphanumeric character in the retailer name.
        points += receipt.getRetailer().replaceAll("[^a-zA-Z0-9]", "").length();
        
        System.out.println(points);

        // Rule 2: 50 points if the total is a round dollar amount with no cents.
        if (Double.parseDouble(receipt.getTotal()) % 1 == 0) {
            points += 50;
        }
        
        System.out.println(points);

        // Rule 3: 25 points if the total is a multiple of 0.25.
        if (Double.parseDouble(receipt.getTotal()) % 0.25 == 0) {
            points += 25;
        }
        
        System.out.println(points);

        // Rule 4: 5 points for every two items on the receipt.
        points += (receipt.getItems().size() / 2) * 5;
        
        System.out.println(points);

        // Rule 5: If the trimmed length of the item description is a multiple of 3,
        // multiply the price by 0.2 and round up to the nearest integer.
        for (Item item : receipt.getItems()) {
            int trimmedLength = item.getShortDescription().trim().length();
            if (trimmedLength % 3 == 0) {
                points += Math.round(Double.parseDouble(item.getPrice()) * 0.2);
            }
        }
        
        System.out.println(points);

        // Rule 6: 6 points if the day in the purchase date is odd.
        if (Integer.parseInt(receipt.getPurchaseDate().substring(8, 10)) % 2 != 0) {
            points += 6;
        }
        
        System.out.println(points);

        // Rule 7: 10 points if the time of purchase is after 2:00 pm and before 4:00 pm.
        int purchaseHour = Integer.parseInt(receipt.getPurchaseTime().substring(0, 2));
        if (purchaseHour >= 14 && purchaseHour < 16) {
            points += 10;
        }
        
        System.out.println(points);

        return points;
    }

    public Receipt getReceiptById(String id) {
        return receipts.get(id);
    }
}
