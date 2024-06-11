package com.hf.sfm.crypt;

import static org.junit.Assert.*;
import org.junit.Test;

public class Base64Test {
    @Test
    public void testAltBase64ToByteArray() {
        assertArrayEquals(new byte[] {0, 1, 2, 3, 4, 5, 6, 7, 8, 9}, Base64.altBase64ToByteArray("AAECAwQFBgcI"));
        assertArrayEquals(new byte[] {1, 2, 3, 4, 5, 6, 7, 8, 9, 10}, Base64.altBase64ToByteArray("AQIDBAUGBwgJ"));
        assertArrayEquals(new byte[] {2, 3, 4, 5, 6, 7, 8, 9, 10, 11}, Base64.altBase64ToByteArray("AgMEBQYHCAkK"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAltBase64ToByteArrayInvalidLength() {
        Base64.altBase64ToByteArray("AAECAwQFB");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAltBase64ToByteArrayInvalidCharacter() {
        Base64.altBase64ToByteArray("AAECAwQFB!c");
    }

    @Test
    public void testBase64ToByteArray() {
        assertArrayEquals(new byte[] {0, 1, 2, 3, 4, 5, 6, 7, 8, 9}, Base64.base64ToByteArray("AAECAwQFBgcI"));
        assertArrayEquals(new byte[] {1, 2, 3, 4, 5, 6, 7, 8, 9, 10}, Base64.base64ToByteArray("AQIDBAUGBwgJ"));
        assertArrayEquals(new byte[] {2, 3, 4, 5, 6, 7, 8, 9, 10, 11}, Base64.base64ToByteArray("AgMEBQYHCAkK"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testBase64ToByteArrayInvalidLength() {
        Base64.base64ToByteArray("AAECAwQFB");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testBase64ToByteArrayInvalidCharacter() {
        Base64.base64ToByteArray("AAECAwQFB!c");
    }

    @Test
    public void testByteArrayToAltBase64() {
        assertEquals("AAECAwQFBgcI", Base64.byteArrayToAltBase64(new byte[] {0, 1, 2, 3, 4, 5, 6, 7, 8, 9}));
        assertEquals("AQIDBAUGBwgJ", Base64.byteArrayToAltBase64(new byte[] {1, 2, 3, 4, 5, 6, 7, 8, 9, 10}));
        assertEquals("AgMEBQYHCAkK", Base64.byteArrayToAltBase64(new byte[] {2, 3, 4, 5, 6, 7, 8, 9, 10, 11}));
    }

    @Test
    public void testByteArrayToBase64() {
        assertEquals("AAECAwQFBgcI", Base64.byteArrayToBase64(new byte[] {0, 1, 2, 3, 4, 5, 6, 7, 8, 9}));
        assertEquals("AQIDBAUGBwgJ", Base64.byteArrayToBase64(new byte[] {1, 2, 3, 4, 5, 6, 7, 8, 9, 10}));
        assertEquals("AgMEBQYHCAkK", Base64.byteArrayToBase64(new byte[] {2, 3, 4, 5, 6, 7, 8, 9, 10, 11}));
    }

    @Test
    public void testMain() {
        // No meaningful test can be done on a main method
        Base64.main(new String[]{});
    }
}