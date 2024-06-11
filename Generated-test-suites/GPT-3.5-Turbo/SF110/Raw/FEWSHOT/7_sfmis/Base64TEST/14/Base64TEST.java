
package com.hf.sfm.crypt;

import static org.junit.Assert.*;

import org.junit.Test;

public class Base64Test {

    @Test
    public void testAltBase64ToByteArray() {
        // Test case 1: empty string
        assertArrayEquals(new byte[] {}, Base64.altBase64ToByteArray(""));

        // Test case 2: valid input
        assertArrayEquals(new byte[] { 1, 23, 45, -67 }, Base64.altBase64ToByteArray("AXsR0w=="));

        // Test case 3: invalid input (string length not multiple of four)
        try {
            Base64.altBase64ToByteArray("ABCD");
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertEquals("String length must be a multiple of four.", e.getMessage());
        }
    }

    @Test
    public void testBase64ToByteArray() {
        // Test case 1: empty string
        assertArrayEquals(new byte[] {}, Base64.base64ToByteArray(""));

        // Test case 2: valid input
        assertArrayEquals(new byte[] { 1, 23, 45, -67 }, Base64.base64ToByteArray("Axce2A=="));

        // Test case 3: invalid input (string length not multiple of four)
        try {
            Base64.base64ToByteArray("ABCD");
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertEquals("String length must be a multiple of four.", e.getMessage());
        }
    }

    @Test
    public void testByteArrayToAltBase64() {
        // Test case 1: empty byte array
        assertEquals("", Base64.byteArrayToAltBase64(new byte[] {}));

        // Test case 2: valid byte array
        assertEquals("AXsR0w==", Base64.byteArrayToAltBase64(new byte[] { 1, 23, 45, -67 }));
    }

    @Test
    public void testByteArrayToBase64() {
        // Test case 1: empty byte array
        assertEquals("", Base64.byteArrayToBase64(new byte[] {}));

        // Test case 2: valid byte array
        assertEquals("Axce2A==", Base64.byteArrayToBase64(new byte[] { 1, 23, 45, -67 }));
    }
}
