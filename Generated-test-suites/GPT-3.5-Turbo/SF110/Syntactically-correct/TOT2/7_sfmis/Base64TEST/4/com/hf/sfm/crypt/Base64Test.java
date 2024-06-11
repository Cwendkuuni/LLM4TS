package com.hf.sfm.crypt;
import org.junit.Test;
import static org.junit.Assert.*;

public class Base64Test {
    
    @Test
    public void testAltBase64ToByteArray() {
        // Typical use case
        String input = "SGVsbG8=";
        byte[] expected = {72, 101, 108, 108, 111};
        assertArrayEquals(expected, Base64.altBase64ToByteArray(input));
        
        // Edge case - empty input
        String emptyInput = "";
        byte[] emptyExpected = {};
        assertArrayEquals(emptyExpected, Base64.altBase64ToByteArray(emptyInput));
        
        // Error scenario - invalid base64 string
        String invalidInput = "Invalid";
        assertThrows(IllegalArgumentException.class, () -> Base64.altBase64ToByteArray(invalidInput));
        
    }
    
    @Test
    public void testBase64ToByteArray() {
        // Typical use case
        String input = "SGVsbG8=";
        byte[] expected = {72, 101, 108, 108, 111};
        assertArrayEquals(expected, Base64.base64ToByteArray(input));
        
        // Edge case - empty input
        String emptyInput = "";
        byte[] emptyExpected = {};
        assertArrayEquals(emptyExpected, Base64.base64ToByteArray(emptyInput));
        
        // Error scenario - invalid base64 string
        String invalidInput = "Invalid";
        assertThrows(IllegalArgumentException.class, () -> Base64.base64ToByteArray(invalidInput));
    }
    
    @Test
    public void testByteArrayToAltBase64() {
        // Typical use case
        byte[] input = {72, 101, 108, 108, 111};
        String expected = "SGVsbG8=";
        assertEquals(expected, Base64.byteArrayToAltBase64(input));
        
        // Edge case - empty input
        byte[] emptyInput = {};
        String emptyExpected = "";
        assertEquals(emptyExpected, Base64.byteArrayToAltBase64(emptyInput));
        
    }
    
    @Test
    public void testByteArrayToBase64() {
        // Typical use case
        byte[] input = {72, 101, 108, 108, 111};
        String expected = "SGVsbG8=";
        assertEquals(expected, Base64.byteArrayToBase64(input));
        
        // Edge case - empty input
        byte[] emptyInput = {};
        String emptyExpected = "";
        assertEquals(emptyExpected, Base64.byteArrayToBase64(emptyInput));
        
    }
    
}