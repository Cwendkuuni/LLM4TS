package com.hf.sfm.crypt;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class Base64Test {

    @Test
    public void testAltBase64ToByteArray_emptyString() {
        byte[] result = Base64.altBase64ToByteArray("");
        assertArrayEquals(new byte[0], result);
    }

    @Test
    public void testAltBase64ToByteArray_singleCharacter() {
        byte[] result = Base64.altBase64ToByteArray("B");
        assertArrayEquals(new byte[] {45}, result);
    }

    @Test
    public void testAltBase64ToByteArray_multipleCharacters() {
        byte[] result = Base64.altBase64ToByteArray("BCD");
        assertArrayEquals(new byte[] {45, 46, 47}, result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAltBase64ToByteArray_invalidStringLength() {
        Base64.altBase64ToByteArray("ABCDE");
    }

    @Test
    public void testBase64ToByteArray_emptyString() {
        byte[] result = Base64.base64ToByteArray("");
        assertArrayEquals(new byte[0], result);
    }

    @Test
    public void testBase64ToByteArray_singleCharacter() {
        byte[] result = Base64.base64ToByteArray("Q");
        assertArrayEquals(new byte[] {0}, result);
    }

    @Test
    public void testBase64ToByteArray_multipleCharacters() {
        byte[] result = Base64.base64ToByteArray("QUJD");
        assertArrayEquals(new byte[] {0, 1, 2}, result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testBase64ToByteArray_invalidStringLength() {
        Base64.base64ToByteArray("ABCDEF");
    }

    @Test
    public void testByteArrayToAltBase64_emptyArray() {
        String result = Base64.byteArrayToAltBase64(new byte[0]);
        assertEquals("", result);
    }

    @Test
    public void testByteArrayToAltBase64_singleElementArray() {
        String result = Base64.byteArrayToAltBase64(new byte[] {1});
        assertEquals("Jw==", result);
    }

    @Test
    public void testByteArrayToAltBase64_multipleElementArray() {
        String result = Base64.byteArrayToAltBase64(new byte[] {1, 2, 3});
        assertEquals("AQID", result);
    }

    @Test
    public void testByteArrayToBase64_emptyArray() {
        String result = Base64.byteArrayToBase64(new byte[0]);
        assertEquals("", result);
    }

    @Test
    public void testByteArrayToBase64_singleElementArray() {
        String result = Base64.byteArrayToBase64(new byte[] {1});
        assertEquals("AQ==", result);
    }

    @Test
    public void testByteArrayToBase64_multipleElementArray() {
        String result = Base64.byteArrayToBase64(new byte[] {1, 2, 3});
        assertEquals("AQID", result);
    }
}