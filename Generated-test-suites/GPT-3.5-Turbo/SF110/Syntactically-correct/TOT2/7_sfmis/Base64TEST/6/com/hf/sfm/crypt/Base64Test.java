package com.hf.sfm.crypt;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.junit.Test;

public class Base64Test {

    @Test
    public void testAltBase64ToByteArray() {
        String input = "SGVsbG8=";
        byte[] expected = {72, 101, 108, 108, 111};
        assertArrayEquals(expected, Base64.altBase64ToByteArray(input));
    }

    @Test
    public void testBase64ToByteArray() {
        String input = "SGVsbG8=";
        byte[] expected = {72, 101, 108, 108, 111};
        assertArrayEquals(expected, Base64.base64ToByteArray(input));
    }

    @Test
    public void testByteArrayToAltBase64() {
        byte[] input = {72, 101, 108, 108, 111};
        String expected = "SGVsbG8=";
        assertEquals(expected, Base64.byteArrayToAltBase64(input));
    }

    @Test
    public void testByteArrayToBase64() {
        byte[] input = {72, 101, 108, 108, 111};
        String expected = "SGVsbG8=";
        assertEquals(expected, Base64.byteArrayToBase64(input));
    }

    @Test
    public void testEdgeCaseAltBase64ToByteArray() {
        String input = "SGVsbG8==";
        byte[] expected = {72, 101, 108, 108, 111};
        assertArrayEquals(expected, Base64.altBase64ToByteArray(input));
    }

    @Test
    public void testExceptionHandlingBase64ToByteArray() {
        String input = "SGVsbG8==";
        try {
            Base64.base64ToByteArray(input);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertTrue(true);
        }
    }
}