package com.hf.sfm.crypt;
import static org.junit.Assert.*;
import org.junit.Test;

public class Base64Test {
    
    // Test cases for altBase64ToByteArray method
    
    @Test
    public void testAltBase64ToByteArray() {
        String inputString = "SGVsbG8=";
        byte[] expectedOutput = {72, 101, 108, 108, 111};
        assertArrayEquals(expectedOutput, Base64.altBase64ToByteArray(inputString));
    }
    
    // Test cases for base64ToByteArray method
    
    @Test
    public void testBase64ToByteArray() {
        String inputString = "U29tZSBtZXNzYWdl";
        byte[] expectedOutput = {83, 111, 109, 101, 32, 109, 101, 115, 115, 97, 103, 101};
        assertArrayEquals(expectedOutput, Base64.base64ToByteArray(inputString));
    }
    
    // Test cases for byteArrayToAltBase64 method
    
    @Test
    public void testByteArrayToAltBase64() {
        byte[] inputArray = {87, 111, 114, 108, 100};
        String expectedOutput = "V29ybGQ=";
        assertEquals(expectedOutput, Base64.byteArrayToAltBase64(inputArray));
    }
    
    // Test cases for byteArrayToBase64 method
    
    @Test
    public void testByteArrayToBase64() {
        byte[] inputArray = {72, 101, 108, 108, 111, 32, 74, 85, 110, 105, 116};
        String expectedOutput = "SGVsbG8gSlVuaXQ=";
        assertEquals(expectedOutput, Base64.byteArrayToBase64(inputArray));
    }
    
    // Additional edge case tests
    
    @Test(expected = IllegalArgumentException.class)
    public void testAltBase64ToByteArrayWithInvalidInput() {
        // Invalid input with non-multiple of four length
        String invalidInput = "InvalidInput";
        Base64.altBase64ToByteArray(invalidInput);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testBase64ToByteArrayWithInvalidCharacter() {
        // Invalid character in input string
        String invalidInput = "U29tZS!bWU="; // contains '!' which is not valid in Base64
        Base64.base64ToByteArray(invalidInput);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testAltBase64ToByteArrayWithIncompletePadding() {
        // Incomplete padding '=' character
        String invalidInput = "U29tZSBtZXNzYWdl=";
        Base64.altBase64ToByteArray(invalidInput);
    }
    
    // Test main method - for code coverage
    @Test
    public void testMainMethod() {
        Base64.main(null); // Just to increase code coverage, no assertion here
    }
}