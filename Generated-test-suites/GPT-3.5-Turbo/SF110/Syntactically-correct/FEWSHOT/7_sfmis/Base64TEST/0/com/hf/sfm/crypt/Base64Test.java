
package com.hf.sfm.crypt;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class Base64Test {
    
    @Test
    public void testAltBase64ToByteArray() {
        assertArrayEquals(new byte[] {1, 18, -73, 103, -103}, Base64.altBase64ToByteArray("AB40amo="));
        assertArrayEquals(new byte[] {55, -99, 119, -66, 0, 13, -112, -43}, Base64.altBase64ToByteArray("NcvXdC0E0tQ="));
        assertArrayEquals(new byte[] {-68, -101, -49, 42, -90, -72, -54, 34, 80, 58}, Base64.altBase64ToByteArray("/IPtKquMzCJQCjo="));
    }
    
    @Test
    public void testBase64ToByteArray() {
        assertArrayEquals(new byte[] {65, 1, -73, 103, -103}, Base64.base64ToByteArray("QQFShow="));
        assertArrayEquals(new byte[] {51, -3, 116, -1, 13, -112, -43}, Base64.base64ToByteArray("M/MaxQtA088="));
        assertArrayEquals(new byte[] {-53, 127, -99, 60, -8, -59, -12, 110, 80, 58}, Base64.base64ToByteArray("1TfPzDzqnFBQO6Y="));
    }
    
    @Test
    public void testByteArrayToAltBase64() {
        assertEquals("AB40amo=", Base64.byteArrayToAltBase64(new byte[] {1, 18, -73, 103, -103}));
        assertEquals("NcvXdC0E0tQ=", Base64.byteArrayToAltBase64(new byte[] {55, -99, 119, -66, 0, 13, -112, -43}));
        assertEquals("/IPtKquMzCJQCjo=", Base64.byteArrayToAltBase64(new byte[] {-68, -101, -49, 42, -90, -72, -54, 34, 80, 58}));
    }
    
    @Test
    public void testByteArrayToBase64() {
        assertEquals("QQFShow=", Base64.byteArrayToBase64(new byte[] {65, 1, -73, 103, -103}));
        assertEquals("M/MaxQtA088=", Base64.byteArrayToBase64(new byte[] {51, -3, 116, -1, 13, -112, -43}));
        assertEquals("1TfPzDzqnFBQO6Y=", Base64.byteArrayToBase64(new byte[] {-53, 127, -99, 60, -8, -59, -12, 110, 80, 58}));
    }
}
