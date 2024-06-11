package com.hf.sfm.crypt;
import static org.junit.Assert.*;
import org.junit.Test;

public class Base64Test {

    @Test
    public void testAltBase64ToByteArray() {
        // Typical Use Case
        assertEquals("Test AltBase64ToByteArray", new byte[] {48, 49, 50, 51, 52, 53, 54, 55, 56, 57}, Base64.altBase64ToByteArray("MDIzNDU2Nzg5"));
        
        // Edge Cases
        // Test empty string
        assertEquals("Test AltBase64ToByteArray Empty String", new byte[] {}, Base64.altBase64ToByteArray(""));
        
        // Test string with padding
        assertEquals("Test AltBase64ToByteArray Padding", new byte[] {48, 49, 50, 51}, Base64.altBase64ToByteArray("MDIz"));
        
        // Error Scenarios
        // Test illegal character
        try {
            Base64.altBase64ToByteArray("#$%");
            fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }
    
    @Test
    public void testBase64ToByteArray() {
        // Typical Use Case
        assertEquals("Test Base64ToByteArray", new byte[] {48, 49, 50, 51, 52, 53, 54, 55, 56, 57}, Base64.base64ToByteArray("MDEyMzQ1Njc4OQ=="));
        
        // Edge Cases
        // Test empty string
        assertEquals("Test Base64ToByteArray Empty String", new byte[] {}, Base64.base64ToByteArray(""));
        
        // Test string with no padding
        assertEquals("Test Base64ToByteArray No Padding", new byte[] {48, 49, 50, 51}, Base64.base64ToByteArray("MDEyMw=="));
        
        // Error Scenarios
        // Test illegal character
        try {
            Base64.base64ToByteArray("#$%");
            fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }
    
    @Test
    public void testByteArrayToAltBase64() {
        // Typical Use Case
        assertEquals("Test ByteArrayToAltBase64", "MDIzNDU2Nzg5", Base64.byteArrayToAltBase64(new byte[] {48, 49, 50, 51, 52, 53, 54, 55, 56, 57}));
        
        // Edge Cases
        // Test empty byte array
        assertEquals("Test ByteArrayToAltBase64 Empty Byte Array", "", Base64.byteArrayToAltBase64(new byte[] {}));
    }
    
    @Test
    public void testByteArrayToBase64() {
        // Typical Use Case
        assertEquals("Test ByteArrayToBase64", "MDEyMzQ1Njc4OQ==", Base64.byteArrayToBase64(new byte[] {48, 49, 50, 51, 52, 53, 54, 55, 56, 57}));
        
        // Edge Cases
        // Test empty byte array
        assertEquals("Test ByteArrayToBase64 Empty Byte Array", "", Base64.byteArrayToBase64(new byte[] {}));
    }

}