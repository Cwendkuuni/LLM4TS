package com.hf.sfm.crypt;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class Base64Test {
    
    @Test
    public void testAltBase64ToByteArray() {
        String s1 = "";
        byte[] b1 = {};
        assertTrue(UtilTest.equals(b1, Base64.altBase64ToByteArray(s1)));
        
        String s2 = "YQ";
        byte[] b2 = {97};
        assertTrue(UtilTest.equals(b2, Base64.altBase64ToByteArray(s2)));
        
        String s3 = "YWI";
        byte[] b3 = {97, 98};
        assertTrue(UtilTest.equals(b3, Base64.altBase64ToByteArray(s3)));
        
        String s4 = "YWJj";
        byte[] b4 = {97, 98, 99};
        assertTrue(UtilTest.equals(b4, Base64.altBase64ToByteArray(s4)));
        
        String s5 = "YWJjZA";
        byte[] b5 = {97, 98, 99, 100};
        assertTrue(UtilTest.equals(b5, Base64.altBase64ToByteArray(s5)));
        
        String s6 = "YWJjZGQ";
        byte[] b6 = {97, 98, 99, 100, 101};
        assertTrue(UtilTest.equals(b6, Base64.altBase64ToByteArray(s6)));
    }
    
    @Test(expected=IllegalArgumentException.class)
    public void testAltBase64ToByteArrayInvalidStringLength() {
        String s = "YWJjZA=";
        Base64.altBase64ToByteArray(s);
    }
    
    @Test
    public void testBase64ToByteArray() {
        String s1 = "";
        byte[] b1 = {};
        assertTrue(UtilTest.equals(b1, Base64.base64ToByteArray(s1)));
        
        String s2 = "AA==";
        byte[] b2 = {0};
        assertTrue(UtilTest.equals(b2, Base64.base64ToByteArray(s2)));
        
        String s3 = "AQ==";
        byte[] b3 = {1};
        assertTrue(UtilTest.equals(b3, Base64.base64ToByteArray(s3)));
        
        String s4 = "Ag==";
        byte[] b4 = {2};
        assertTrue(UtilTest.equals(b4, Base64.base64ToByteArray(s4)));
        
        String s5 = "Aw==";
        byte[] b5 = {3};
        assertTrue(UtilTest.equals(b5, Base64.base64ToByteArray(s5)));
        
        String s6 = "BAAA";
        byte[] b6 = {0, 0};
        assertTrue(UtilTest.equals(b6, Base64.base64ToByteArray(s6)));
    }
    
    @Test(expected=IllegalArgumentException.class)
    public void testBase64ToByteArrayInvalidStringLength() {
        String s = "AA====";
        Base64.base64ToByteArray(s);
    }
    
    @Test
    public void testByteArrayToAltBase64() {
        byte[] b1 = {};
        String s1 = "";
        assertEquals(s1, Base64.byteArrayToAltBase64(b1));
        
        byte[] b2 = {97};
        String s2 = "YQ==";
        assertEquals(s2, Base64.byteArrayToAltBase64(b2));
        
        byte[] b3 = {97, 98};
        String s3 = "YWI=";
        assertEquals(s3, Base64.byteArrayToAltBase64(b3));
        
        byte[] b4 = {97, 98, 99};
        String s4 = "YWJj";
        assertEquals(s4, Base64.byteArrayToAltBase64(b4));
        
        byte[] b5 = {97, 98, 99, 100};
        String s5 = "YWJjZA==";
        assertEquals(s5, Base64.byteArrayToAltBase64(b5));
        
        byte[] b6 = {97, 98, 99, 100, 101};
        String s6 = "YWJjZGQ=";
        assertEquals(s6, Base64.byteArrayToAltBase64(b6));
    }
    
    @Test
    public void testByteArrayToBase64() {
        byte[] b1 = {};
        String s1 = "";
        assertEquals(s1, Base64.byteArrayToBase64(b1));
        
        byte[] b2 = {0};
        String s2 = "AA==";
        assertEquals(s2, Base64.byteArrayToBase64(b2));
        
        byte[] b3 = {1};
        String s3 = "AQ==";
        assertEquals(s3, Base64.byteArrayToBase64(b3));
        
        byte[] b4 = {2};
        String s4 = "Ag==";
        assertEquals(s4, Base64.byteArrayToBase64(b4));
        
        byte[] b5 = {3};
        String s5 = "Aw==";
        assertEquals(s5, Base64.byteArrayToBase64(b5));
        
        byte[] b6 = {0, 0};
        String s6 = "BAAA";
        assertEquals(s6, Base64.byteArrayToBase64(b6));
    }
    
    @Test
    public void testMain() {
        String s = "0123456789";
        byte[] b = s.getBytes();
        String s1 = Base64.byteArrayToBase64(b);
        String expected1 = "MDEyMzQ1Njc4OQ==";
        assertEquals(expected1, s1);
        
        byte[] b2 = Base64.base64ToByteArray(s1);
        String s2 = new String(b2);
        assertEquals(s, s2);
    }
    
    //Helper method to compare byte arrays
    private static boolean equals(byte[] b1, byte[] b2) {
        if (b1.length != b2.length) {
            return false;
        }

        for (int i = 0; i < b1.length; i++) {
            if (b1[i] != b2[i]) {
                return false;
            }
        }
        
        return true;
    }
}