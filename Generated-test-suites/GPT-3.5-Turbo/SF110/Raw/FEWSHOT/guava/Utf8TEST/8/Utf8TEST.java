package com.google.common.base;

import static org.junit.Assert.*;

import org.junit.Test;

public class Utf8Test {

    @Test
    public void testEncodedLength() {
        // Test for empty string
        assertEquals(0, Utf8.encodedLength(""));

        // Test for single character strings
        assertEquals(1, Utf8.encodedLength("a"));
        assertEquals(1, Utf8.encodedLength("A"));
        assertEquals(1, Utf8.encodedLength("1"));
        assertEquals(1, Utf8.encodedLength(" "));

        // Test for strings with characters < 128
        assertEquals(3, Utf8.encodedLength("abc"));
        assertEquals(2, Utf8.encodedLength("ü"));
        assertEquals(6, Utf8.encodedLength("こんにちは"));
        assertEquals(7, Utf8.encodedLength("你好世界"));

        // Test for strings with characters >= 128
        assertEquals(4, Utf8.encodedLength("€"));
        assertEquals(4, Utf8.encodedLength("€€€€€"));
    }

    @Test
    public void testIsWellFormed() {
        // Test for empty byte array
        assertTrue(Utf8.isWellFormed(new byte[] {}));

        // Test for single byte arrays
        assertTrue(Utf8.isWellFormed(new byte[] { 0 }));
        assertTrue(Utf8.isWellFormed(new byte[] { 64 }));
        assertTrue(Utf8.isWellFormed(new byte[] { (byte) 128 }));
        assertFalse(Utf8.isWellFormed(new byte[] { (byte) 255 }));

        // Test for well-formed byte arrays
        assertTrue(Utf8.isWellFormed(new byte[] { 97, 98, 99 }));
        assertTrue(Utf8.isWellFormed(new byte[] { -62, -64 }));
        assertTrue(Utf8.isWellFormed(new byte[] { -26, -106, -121 }));
        assertTrue(Utf8.isWellFormed(new byte[] { -28, -72, -83, -26, -96, -83 }));

        // Test for ill-formed byte arrays
        assertFalse(Utf8.isWellFormed(new byte[] { -124, -65 }));
        assertFalse(Utf8.isWellFormed(new byte[] { -32, -96, -65 }));
        assertFalse(Utf8.isWellFormed(new byte[] { -19, -97, -64, -64 }));
    }

}