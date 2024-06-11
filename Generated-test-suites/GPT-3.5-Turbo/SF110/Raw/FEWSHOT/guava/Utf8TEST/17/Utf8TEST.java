package com.google.common.base;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class Utf8Test {
    @Test
    public void testEncodedLength() {
        // Test empty string
        assertEquals(0, Utf8.encodedLength(""));

        // Test string with ASCII characters
        assertEquals(1, Utf8.encodedLength("a"));
        assertEquals(5, Utf8.encodedLength("hello"));
        assertEquals(11, Utf8.encodedLength("hello world"));

        // Test string with non-ASCII characters
        assertEquals(6, Utf8.encodedLength("hełło"));

        // Test string with surrogate pairs
        assertEquals(4, Utf8.encodedLength("\uD83D\uDE00")); // U+1F600
        assertEquals(8, Utf8.encodedLength("\uD83D\uDE00\uD83D\uDE01")); // U+1F600 U+1F601
        assertEquals(12, Utf8.encodedLength("\uD83D\uDE00\uD83D\uDE01\uD83D\uDE02")); // U+1F600 U+1F601 U+1F602
    }

    @Test
    public void testIsWellFormed() {
        // Test well-formed UTF-8 bytes
        assertTrue(Utf8.isWellFormed(new byte[]{(byte) 0x61}));
        assertTrue(Utf8.isWellFormed(new byte[]{(byte) 0xC4, (byte) 0x82}));
        assertTrue(Utf8.isWellFormed(new byte[]{(byte) 0xED, (byte) 0x9F, (byte) 0xBF})); // U+D7FF
        assertTrue(Utf8.isWellFormed(new byte[]{(byte) 0xEE, (byte) 0x80, (byte) 0x80})); // U+E000
        assertTrue(Utf8.isWellFormed(new byte[]{(byte) 0xEF, (byte) 0xBF, (byte) 0xBD})); // U+FFFD

        // Test malformed UTF-8 bytes
        assertFalse(Utf8.isWellFormed(new byte[]{(byte) 0x80}));
        assertFalse(Utf8.isWellFormed(new byte[]{(byte) 0xC0, (byte) 0x80}));
        assertFalse(Utf8.isWellFormed(new byte[]{(byte) 0xE0, (byte) 0x80, (byte) 0x80}));
        assertFalse(Utf8.isWellFormed(new byte[]{(byte) 0xE0, (byte) 0x90, (byte) 0x80}));
        assertFalse(Utf8.isWellFormed(new byte[]{(byte) 0xF0, (byte) 0x90, (byte) 0x80, (byte) 0x80}));

        // Test well-formed and malformed UTF-8 bytes together
        byte[] bytes = new byte[]{
            (byte) 0x61, // a
            (byte) 0xC4, (byte) 0x82, // ę
            (byte) 0xED, (byte) 0x9F, (byte) 0xBF, // U+D7FF
            (byte) 0xE0, (byte) 0x80, (byte) 0x80, // U+800
            (byte) 0xEF, (byte) 0xBF, (byte) 0xBD, // U+FFFD
            (byte) 0x80 // malformed
        };
        assertFalse(Utf8.isWellFormed(bytes));
    }

}