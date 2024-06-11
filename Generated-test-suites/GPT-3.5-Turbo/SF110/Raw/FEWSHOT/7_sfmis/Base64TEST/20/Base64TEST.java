package com.hf.sfm.crypt;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class Base64Test {
    @Test
    public void testAltBase64ToByteArray() {
        byte[] expected = {0, 1, 2, 3, 4, 5, 6, 7, 8, -1};
        byte[] result = Base64.altBase64ToByteArray("AAECAwQFBgc");
        assertNotNull(result);
        assertArrayEquals(expected, result);
        
        result = Base64.altBase64ToByteArray("AA==");
        assertNotNull(result);
        assertArrayEquals(new byte[]{0}, result);
        
        result = Base64.altBase64ToByteArray("AAA=");
        assertNotNull(result);
        assertArrayEquals(new byte[]{0, 0}, result);
        
        result = Base64.altBase64ToByteArray("");
        assertNotNull(result);
        assertArrayEquals(new byte[]{}, result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAltBase64ToByteArrayInvalidInput() {
        Base64.altBase64ToByteArray("AAECAwQFBgc=");
    }
    
    @Test
    public void testBase64ToByteArray() {
        byte[] expected = {0, 1, 2, 3, 4, 5, 6, 7, 8, -1};
        byte[] result = Base64.base64ToByteArray("AAECAwQFBgc=");
        assertNotNull(result);
        assertArrayEquals(expected, result);
        
        result = Base64.base64ToByteArray("AA==");
        assertNotNull(result);
        assertArrayEquals(new byte[]{0}, result);
        
        result = Base64.base64ToByteArray("AAA=");
        assertNotNull(result);
        assertArrayEquals(new byte[]{0, 0}, result);
        
        result = Base64.base64ToByteArray("");
        assertNotNull(result);
        assertArrayEquals(new byte[]{}, result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testBase64ToByteArrayInvalidInput() {
        Base64.base64ToByteArray("AAECAwQFBgc");
    }
    
    @Test
    public void testByteArrayToAltBase64() {
        byte[] input = {0, 1, 2, 3, 4, 5, 6, 7, 8, -1};
        String expected = "AAECAwQFBgc=";
        String result = Base64.byteArrayToAltBase64(input);
        assertNotNull(result);
        assertEquals(expected, result);
        
        input = new byte[]{0};
        expected = "AA==";
        result = Base64.byteArrayToAltBase64(input);
        assertNotNull(result);
        assertEquals(expected, result);
        
        input = new byte[]{0, 0};
        expected = "AAA=";
        result = Base64.byteArrayToAltBase64(input);
        assertNotNull(result);
        assertEquals(expected, result);
        
        input = new byte[]{};
        expected = "";
        result = Base64.byteArrayToAltBase64(input);
        assertNotNull(result);
        assertEquals(expected, result);
    }
    
    @Test
    public void testByteArrayToBase64() {
        byte[] input = {0, 1, 2, 3, 4, 5, 6, 7, 8, -1};
        String expected = "AAECAwQFBgc";
        String result = Base64.byteArrayToBase64(input);
        assertNotNull(result);
        assertEquals(expected, result);
        
        input = new byte[]{0};
        expected = "AA==";
        result = Base64.byteArrayToBase64(input);
        assertNotNull(result);
        assertEquals(expected, result);
        
        input = new byte[]{0, 0};
        expected = "AAA=";
        result = Base64.byteArrayToBase64(input);
        assertNotNull(result);
        assertEquals(expected, result);
        
        input = new byte[]{};
        expected = "";
        result = Base64.byteArrayToBase64(input);
        assertNotNull(result);
        assertEquals(expected, result);
    }   
}