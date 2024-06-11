
package com.hf.sfm.crypt;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class Base64Test {

    @Test
    public void testAltBase64ToByteArray() {
        byte[] expected = {};
        assertArrayEquals(expected, Base64.altBase64ToByteArray(""));
        
        expected = new byte[]{1, 2, 3, 4, 5, 6, 7};
        assertArrayEquals(expected, Base64.altBase64ToByteArray("AQIDBAUGBw=="));
        
        expected = new byte[]{10, 20, 30, 40, 50, 60, 70};
        assertArrayEquals(expected, Base64.altBase64ToByteArray("CgsMDhASFRYX"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAltBase64ToByteArrayInvalidLength() {
        Base64.altBase64ToByteArray("AQ===");
    }

    @Test
    public void testBase64ToByteArray() {
        byte[] expected = {};
        assertArrayEquals(expected, Base64.base64ToByteArray(""));

        expected = new byte[]{1, 2, 3, 4, 5, 6, 7};
        assertArrayEquals(expected, Base64.base64ToByteArray("AQIDBAUGBw=="));

        expected = new byte[]{10, 20, 30, 40, 50, 60, 70};
        assertArrayEquals(expected, Base64.base64ToByteArray("CgsMDhASFRYX"));
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testBase64ToByteArrayInvalidLength() {
        Base64.base64ToByteArray("AQ===");
    }

    @Test
    public void testByteArrayToAltBase64() {
        String expected = "";
        assertEquals(expected, Base64.byteArrayToAltBase64(new byte[]{}));
        
        expected = "AQIDBAUGBw==";
        assertEquals(expected, Base64.byteArrayToAltBase64(new byte[]{1, 2, 3, 4, 5, 6, 7}));
        
        expected = "CgsMDhASFRYX";
        assertEquals(expected, Base64.byteArrayToAltBase64(new byte[]{10, 20, 30, 40, 50, 60, 70}));
    }

    @Test
    public void testByteArrayToBase64() {
        String expected = "";
        assertEquals(expected, Base64.byteArrayToBase64(new byte[]{}));
        
        expected = "AQIDBAUGBw==";
        assertEquals(expected, Base64.byteArrayToBase64(new byte[]{1, 2, 3, 4, 5, 6, 7}));
        
        expected = "CgsMDhASFRYX";
        assertEquals(expected, Base64.byteArrayToBase64(new byte[]{10, 20, 30, 40, 50, 60, 70}));
    }

    @Test
    public void test_ExampleMain() {
        String s = "0123456789";
        byte[] b = s.getBytes();
        s = Base64.byteArrayToBase64(b);
        byte[] expected = b;
        assertArrayEquals(expected, Base64.base64ToByteArray(s));
    }
}
