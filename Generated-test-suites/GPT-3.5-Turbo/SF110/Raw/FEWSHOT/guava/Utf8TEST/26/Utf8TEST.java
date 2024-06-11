package com.google.common.base;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class Utf8Test {

    @Test
    public void testEncodedLength() {
        // Test with empty string
        assertEquals(0, Utf8.encodedLength(""));

        // Test with ASCII characters
        assertEquals(3, Utf8.encodedLength("abc"));
        assertEquals(5, Utf8.encodedLength("hello"));

        // Test with non-ASCII characters
        assertEquals(9, Utf8.encodedLength("¢€𐍈"));
        assertEquals(25, Utf8.encodedLength("𠜎⼎"));

        // Test with special characters
        assertEquals(4, Utf8.encodedLength("\"'\\"));
        assertEquals(11, Utf8.encodedLength("foo\"bar\"baz"));
    }

    @Test
    public void testEncodedLengthGeneral() {
        // Test with empty string
        assertEquals(0, Utf8.encodedLengthGeneral("", 0));

        // Test with ASCII characters
        assertEquals(0, Utf8.encodedLengthGeneral("abc", 0));
        assertEquals(2, Utf8.encodedLengthGeneral("hello", 1));

        // Test with non-ASCII characters
        assertEquals(3, Utf8.encodedLengthGeneral("¢€𐍈", 1));
        assertEquals(5, Utf8.encodedLengthGeneral("𠜎⼎", 0));

        // Test with special characters
        assertEquals(0, Utf8.encodedLengthGeneral("\"'\\", 1));
        assertEquals(3, Utf8.encodedLengthGeneral("foo\"bar\"baz", 4));
    }

    @Test
    public void testIsWellFormed() {
        // Test with well-formed UTF-8 bytes
        assertTrue(Utf8.isWellFormed(new byte[] { 0x41, 0x42, 0x43 }));
        assertTrue(Utf8.isWellFormed(new byte[] { (byte) 0xC2, (byte) 0xA2 }));
        assertTrue(Utf8.isWellFormed(new byte[] { (byte) 0xE2, (byte) 0x82, (byte) 0xAC }));
        assertTrue(Utf8.isWellFormed(new byte[] { (byte) 0xF0, (byte) 0xA0, (byte) 0x8D, (byte) 0x88 }));

        // Test with malformed UTF-8 bytes
        assertFalse(Utf8.isWellFormed(new byte[] { (byte) 0x80 }));
        assertFalse(Utf8.isWellFormed(new byte[] { (byte) 0xC2 }));
        assertFalse(Utf8.isWellFormed(new byte[] { (byte) 0xE2, (byte) 0x82 }));
        assertFalse(Utf8.isWellFormed(new byte[] { (byte) 0xF0, (byte) 0xA0, (byte) 0x8D }));

        // Test with mixed well-formed and malformed UTF-8 bytes
        assertFalse(Utf8.isWellFormed(new byte[] { 0x41, 0x80, 0x42, (byte)0xC2, 0x43, (byte)0xE2, (byte)0x82 }));
        assertFalse(Utf8.isWellFormed(new byte[] { (byte)0xAC, 0x44, (byte)0xF0, (byte)0xA0 }));
    }

    @Test
    public void testIsWellFormedSlowPath() {
        // Test with well-formed UTF-8 bytes
        assertTrue(Utf8.isWellFormedSlowPath(new byte[] { 0x41, 0x42, 0x43 }, 0, 3));
        assertTrue(Utf8.isWellFormedSlowPath(new byte[] { (byte) 0xC2, (byte) 0xA2 }, 0, 2));
        assertTrue(Utf8.isWellFormedSlowPath(new byte[] { (byte) 0xE2, (byte) 0x82, (byte) 0xAC }, 0, 3));
        assertTrue(Utf8.isWellFormedSlowPath(new byte[] { (byte) 0xF0, (byte) 0xA0, (byte) 0x8D, (byte) 0x88 }, 0, 4));

        // Test with malformed UTF-8 bytes
        assertFalse(Utf8.isWellFormedSlowPath(new byte[] { (byte) 0x80 }, 0, 1));
        assertFalse(Utf8.isWellFormedSlowPath(new byte[] { (byte) 0xC2 }, 0, 1));
        assertFalse(Utf8.isWellFormedSlowPath(new byte[] { (byte) 0xE2, (byte) 0x82 }, 0, 2));
        assertFalse(Utf8.isWellFormedSlowPath(new byte[] { (byte) 0xF0, (byte) 0xA0, (byte) 0x8D }, 0, 3));

        // Test with mixed well-formed and malformed UTF-8 bytes
        assertFalse(Utf8.isWellFormedSlowPath(new byte[] { 0x41, 0x80, 0x42, (byte)0xC2, 0x43, (byte)0xE2, (byte)0x82 }, 0, 7));
        assertFalse(Utf8.isWellFormedSlowPath(new byte[] { (byte)0xAC, 0x44, (byte)0xF0, (byte)0xA0 }, 0, 4));
    }
}