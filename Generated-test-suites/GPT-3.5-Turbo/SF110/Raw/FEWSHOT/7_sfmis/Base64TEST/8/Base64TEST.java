package com.hf.sfm.crypt;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class Base64Test {
    @Test
    public void testAltBase64ToByteArray() {
        assertNull(Base64.altBase64ToByteArray(null));
        assertArrayEquals(new byte[] { 65, 66, 67, 68, 69, 70 }, Base64.altBase64ToByteArray("QUJDREVG"));
        assertArrayEquals(new byte[] { 71, 72, 73, 74, 75 }, Base64.altBase64ToByteArray("R0hJSktM"));
        assertArrayEquals(new byte[] { 76, 77, 78, 79 }, Base64.altBase64ToByteArray("TExOTw=="));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAltBase64ToByteArrayWithInvalidString() {
        Base64.altBase64ToByteArray("invalid");
    }

    @Test
    public void testBase64ToByteArray() {
        assertArrayEquals(new byte[] { 65, 66, 67, 68, 69, 70 }, Base64.base64ToByteArray("QUJDREVG"));
        assertArrayEquals(new byte[] { 71, 72, 73, 74, 75 }, Base64.base64ToByteArray("R0hJSktM"));
        assertArrayEquals(new byte[] { 76, 77, 78, 79 }, Base64.base64ToByteArray("TExOTw=="));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testBase64ToByteArrayWithInvalidString() {
        Base64.base64ToByteArray("invalid");
    }

    @Test
    public void testByteArrayToAltBase64() {
        assertNull(Base64.byteArrayToAltBase64(null));
        assertEquals("QUJDREVG", Base64.byteArrayToAltBase64(new byte[] { 65, 66, 67, 68, 69, 70 }));
        assertEquals("R0hJSktM", Base64.byteArrayToAltBase64(new byte[] { 71, 72, 73, 74, 75 }));
        assertEquals("TExOTw==", Base64.byteArrayToAltBase64(new byte[] { 76, 77, 78, 79 }));
    }

    @Test
    public void testByteArrayToBase64() {
        assertNull(Base64.byteArrayToBase64(null));
        assertEquals("QUJDREVG", Base64.byteArrayToBase64(new byte[] { 65, 66, 67, 68, 69, 70 }));
        assertEquals("R0hJSktM", Base64.byteArrayToBase64(new byte[] { 71, 72, 73, 74, 75 }));
        assertEquals("TExOTw==", Base64.byteArrayToBase64(new byte[] { 76, 77, 78, 79 }));
    }
}