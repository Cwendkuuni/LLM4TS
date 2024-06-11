
package com.hf.sfm.crypt;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class Base64Test {
    
    @Test
    public void testAltBase64ToByteArray() {
        byte[] expected = {72, 101, 108, 108, 111};
        assertArrayEquals(expected, Base64.altBase64ToByteArray("SGVsbG8="));
        
        expected = new byte[] {-1, -2, -3, -4, -5};
        assertArrayEquals(expected, Base64.altBase64ToByteArray("/v7+8f4="));
        
        expected = new byte[] {9, 20, 127, -128, -126};
        assertArrayEquals(expected, Base64.altBase64ToByteArray("CRyf/g=="));
        
        expected = new byte[] {0, 0, 0, 0, 0};
        assertArrayEquals(expected, Base64.altBase64ToByteArray("AAAAAAAA"));
    }
    
    @Test(expected=IllegalArgumentException.class)
    public void testAltBase64ToByteArrayInvalidLength() {
        Base64.altBase64ToByteArray("ABC");
    }
    
    @Test
    public void testBase64ToByteArray() {
        byte[] expected = {72, 101, 108, 108, 111};
        assertArrayEquals(expected, Base64.base64ToByteArray("SGVsbG8="));
        
        expected = new byte[] {-1, -2, -3, -4, -5};
        assertArrayEquals(expected, Base64.base64ToByteArray("/v7+8f4="));
        
        expected = new byte[] {9, 20, 127, -128, -126};
        assertArrayEquals(expected, Base64.base64ToByteArray("CRyf/g=="));
        
        expected = new byte[] {0, 0, 0, 0, 0};
        assertArrayEquals(expected, Base64.base64ToByteArray("AAAAAAAA"));
    }
    
    @Test(expected=IllegalArgumentException.class)
    public void testBase64ToByteArrayInvalidLength() {
        Base64.base64ToByteArray("ABC");
    }
    
    @Test
    public void testByteArrayToAltBase64() {
        byte[] input = {72, 101, 108, 108, 111};
        assertEquals("SGVsbG8=", Base64.byteArrayToAltBase64(input));
        
        input = new byte[] {-1, -2, -3, -4, -5};
        assertEquals("/v7+8f4=", Base64.byteArrayToAltBase64(input));
        
        input = new byte[] {9, 20, 127, -128, -126};
        assertEquals("CRyf/g==", Base64.byteArrayToAltBase64(input));
        
        input = new byte[] {0, 0, 0, 0, 0};
        assertEquals("AAAAAAAA", Base64.byteArrayToAltBase64(input));
    }
    
    @Test
    public void testByteArrayToBase64() {
        byte[] input = {72, 101, 108, 108, 111};
        assertEquals("SGVsbG8=", Base64.byteArrayToBase64(input));
        
        input = new byte[] {-1, -2, -3, -4, -5};
        assertEquals("/v7+8f4=", Base64.byteArrayToBase64(input));
        
        input = new byte[] {9, 20, 127, -128, -126};
        assertEquals("CRyf/g==", Base64.byteArrayToBase64(input));
        
        input = new byte[] {0, 0, 0, 0, 0};
        assertEquals("AAAAAAAA", Base64.byteArrayToBase64(input));
    }
    
    @Test
    public void test_$23183() {
        byte[] bb = {0, 1, 2, 3, 4, 5};
        char[] cc = {'A', 'B', 'C', 'D', 'E', 'F'};
        
        assertEquals(0, Base64._$23183(cc[0], bb));
        assertEquals(1, Base64._$23183(cc[1], bb));
        assertEquals(2, Base64._$23183(cc[2], bb));
        assertEquals(3, Base64._$23183(cc[3], bb));
        assertEquals(4, Base64._$23183(cc[4], bb));
        assertEquals(5, Base64._$23183(cc[5], bb));
    }
}
