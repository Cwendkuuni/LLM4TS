
package com.hf.sfm.crypt;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import org.junit.Test;

public class Base64Test {
    
    @Test
    public void testAltBase64ToByteArray() {
        // Test with valid Base64 string
        String base64String = "SGVsbG8gd29ybGQ=";
        byte[] expectedByteArray = "Hello world".getBytes();
        assertArrayEquals(expectedByteArray, Base64.altBase64ToByteArray(base64String));
        
        // Test with invalid Base64 string
        base64String = "SGVsbG8gd29ybGQ";
        try {
            Base64.altBase64ToByteArray(base64String);
            fail("Should have thrown IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertEquals("String length must be a multiple of four.", e.getMessage());
        }
    }
    
    @Test
    public void testBase64ToByteArray() {
        // Test with valid Base64 string
        String base64String = "SGVsbG8gd29ybGQ=";
        byte[] expectedByteArray = "Hello world".getBytes();
        assertArrayEquals(expectedByteArray, Base64.base64ToByteArray(base64String));
        
        // Test with invalid Base64 string
        base64String = "SGVsbG8gd29ybGQ";
        try {
            Base64.base64ToByteArray(base64String);
            fail("Should have thrown IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertEquals("String length must be a multiple of four.", e.getMessage());
        }
    }
    
    @Test
    public void testByteArrayToAltBase64() {
        byte[] byteArray = "Hello world".getBytes();
        String expectedBase64String = "SGVsbG8gd29ybGQ=";
        assertEquals(expectedBase64String, Base64.byteArrayToAltBase64(byteArray));
    }
    
    @Test
    public void testByteArrayToBase64() {
        byte[] byteArray = "Hello world".getBytes();
        String expectedBase64String = "SGVsbG8gd29ybGQ=";
        assertEquals(expectedBase64String, Base64.byteArrayToBase64(byteArray));
    }
    
}
