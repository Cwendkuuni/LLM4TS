package com.hf.sfm.crypt;

import static org.junit.Assert.*;

import org.junit.Test;

public class Base64Test {

    @Test
    public void testAltBase64ToByteArray() {
        byte[] expected = {1, 2, 3, 4};
        assertArrayEquals(expected, Base64.altBase64ToByteArray("AQIDBA=="));
    }
    
    @Test(expected=IllegalArgumentException.class)
    public void testAltBase64ToByteArrayInvalidInput() {
        Base64.altBase64ToByteArray("AQQDQ=");
    }

    @Test
    public void testBase64ToByteArray() {
        byte[] expected = {1, 2, 3, 4};
        assertArrayEquals(expected, Base64.base64ToByteArray("AQIDBA=="));
    }
    
    @Test(expected=IllegalArgumentException.class)
    public void testBase64ToByteArrayInvalidInput() {
        Base64.base64ToByteArray("AQQDQ=");
    }

    @Test
    public void testByteArrayToAltBase64() {
        byte[] input = {1, 2, 3, 4};
        assertEquals("AQIDBA==", Base64.byteArrayToAltBase64(input));
    }

    @Test
    public void testByteArrayToBase64() {
        byte[] input = {1, 2, 3, 4};
        assertEquals("AQIDBA==", Base64.byteArrayToBase64(input));
    }

}