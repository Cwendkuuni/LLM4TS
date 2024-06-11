
package com.hf.sfm.crypt;

import org.junit.Test;
import static org.junit.Assert.*;

public class Base64Test {

    @Test
    public void testAltBase64ToByteArray() {
        // Test basic functionality
        String input = "SGVsbG8gd29ybGQ=";
        byte[] expectedOutput = "Hello world".getBytes();
        byte[] actualOutput = Base64.altBase64ToByteArray(input);
        assertArrayEquals(expectedOutput, actualOutput);
        
        // Test exception handling
        try {
            String invalidInput = "SGVsbG8gd29ybGQ";
            Base64.altBase64ToByteArray(invalidInput);
            fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException e) {
            // Pass
        }
    }

    @Test
    public void testBase64ToByteArray() {
        // Test basic functionality
        String input = "SGVsbG8gd29ybGQ=";
        byte[] expectedOutput = "Hello world".getBytes();
        byte[] actualOutput = Base64.base64ToByteArray(input);
        assertArrayEquals(expectedOutput, actualOutput);
        
        // Test exception handling
        try {
            String invalidInput = "SGVsbG8gd29ybGQ";
            Base64.base64ToByteArray(invalidInput);
            fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException e) {
            // Pass
        }
    }

    @Test
    public void testByteArrayToAltBase64() {
        // Test basic functionality
        byte[] input = "Hello world".getBytes();
        String expectedOutput = "SGVsbG8gd29ybGQ=";
        String actualOutput = Base64.byteArrayToAltBase64(input);
        assertEquals(expectedOutput, actualOutput);
    }

    @Test
    public void testByteArrayToBase64() {
        // Test basic functionality
        byte[] input = "Hello world".getBytes();
        String expectedOutput = "SGVsbG8gd29ybGQ=";
        String actualOutput = Base64.byteArrayToBase64(input);
        assertEquals(expectedOutput, actualOutput);
    }

    // Additional test cases for edge cases and exceptions
    
    @Test
    public void testAltBase64ToByteArray_InvalidLength() {
        // Test exception handling for invalid length
        try {
            String input = "SGVsbG8gd29ybGQ";
            Base64.altBase64ToByteArray(input);
            fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException e) {
            // Pass
        }
    }

    @Test
    public void testAltBase64ToByteArray_InvalidCharacter() {
        // Test exception handling for invalid character
        try {
            String input = "SGVsbG8gd29ybG@=";
            Base64.altBase64ToByteArray(input);
            fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException e) {
            // Pass
        }
    }
    
    @Test
    public void testBase64ToByteArray_InvalidLength() {
        // Test exception handling for invalid length
        try {
            String input = "SGVsbG8gd29ybGQ";
            Base64.base64ToByteArray(input);
            fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException e) {
            // Pass
        }
    }

    @Test
    public void testBase64ToByteArray_InvalidCharacter() {
        // Test exception handling for invalid character
        try {
            String input = "SGVsbG8gd29ybG@=";
            Base64.base64ToByteArray(input);
            fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException e) {
            // Pass
        }
    }
    
    @Test
    public void testByteArrayToAltBase64_EmptyArray() {
        // Test edge case for empty array
        byte[] input = new byte[0];
        String expectedOutput = "";
        String actualOutput = Base64.byteArrayToAltBase64(input);
        assertEquals(expectedOutput, actualOutput);
    }

    // Add more test cases for other methods...

}

