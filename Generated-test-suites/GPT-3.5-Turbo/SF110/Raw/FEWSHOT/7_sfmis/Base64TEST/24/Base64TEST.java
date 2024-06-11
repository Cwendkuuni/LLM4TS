package com.hf.sfm.crypt;

import static org.junit.Assert.*;

import org.junit.Test;

public class Base64Test {
    @Test
    public void testAltBase64ToByteArray() {
        // Test case 1
        byte[] expectedResult1 = {
            49, 50, 51, 52, 53, 54, 55, 56, 57
        };
        byte[] actualResult1 = Base64.altBase64ToByteArray("MTIzNDU2Nzg5");
        assertArrayEquals(expectedResult1, actualResult1);
        
        // Test case 2
        byte[] expectedResult2 = {
            65, 66, 67, 68, 69, 70
        };
        byte[] actualResult2 = Base64.altBase64ToByteArray("QUJDREVG");
        assertArrayEquals(expectedResult2, actualResult2);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testAltBase64ToByteArrayInvalidInput() {
        // Test case - Invalid input (string length not a multiple of four)
        Base64.altBase64ToByteArray("QUJD");
    }
    
    @Test
    public void testBase64ToByteArray() {
        // Test case 1
        byte[] expectedResult1 = {
            49, 50, 51, 52, 53, 54, 55, 56, 57
        };
        byte[] actualResult1 = Base64.base64ToByteArray("MTIzNDU2Nzg5");
        assertArrayEquals(expectedResult1, actualResult1);
        
        // Test case 2
        byte[] expectedResult2 = {
            65, 66, 67, 68, 69, 70
        };
        byte[] actualResult2 = Base64.base64ToByteArray("QUJDREVG");
        assertArrayEquals(expectedResult2, actualResult2);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testBase64ToByteArrayInvalidInput() {
        // Test case - Invalid input (string length not a multiple of four)
        Base64.base64ToByteArray("QUJD");
    }
    
    @Test
    public void testByteArrayToAltBase64() {
        // Test case 1
        byte[] input1 = {
            49, 50, 51, 52, 53, 54, 55, 56, 57
        };
        String expectedResult1 = "MTIzNDU2Nzg5";
        String actualResult1 = Base64.byteArrayToAltBase64(input1);
        assertEquals(expectedResult1, actualResult1);
        
        // Test case 2
        byte[] input2 = {
            65, 66, 67, 68, 69, 70
        };
        String expectedResult2 = "QUJDREVG";
        String actualResult2 = Base64.byteArrayToAltBase64(input2);
        assertEquals(expectedResult2, actualResult2);
    }
    
    @Test
    public void testByteArrayToBase64() {
        // Test case 1
        byte[] input1 = {
            49, 50, 51, 52, 53, 54, 55, 56, 57
        };
        String expectedResult1 = "MTIzNDU2Nzg5";
        String actualResult1 = Base64.byteArrayToBase64(input1);
        assertEquals(expectedResult1, actualResult1);
        
        // Test case 2
        byte[] input2 = {
            65, 66, 67, 68, 69, 70
        };
        String expectedResult2 = "QUJDREVG";
        String actualResult2 = Base64.byteArrayToBase64(input2);
        assertEquals(expectedResult2, actualResult2);
    }
}