package com.google.common.base;

import org.junit.Test;
import static org.junit.Assert.*;

public class Utf8Test {

    @Test
    public void testEncodedLength() {
        // Test with empty string
        assertEquals(0, Utf8.encodedLength(""));

        // Test with ASCII characters
        assertEquals(1, Utf8.encodedLength("a"));
        assertEquals(2, Utf8.encodedLength("ab"));
        assertEquals(3, Utf8.encodedLength("abc"));
        assertEquals(4, Utf8.encodedLength("abcd"));
        assertEquals(5, Utf8.encodedLength("abcde"));

        // Test with non-ASCII characters
        assertEquals(2, Utf8.encodedLength("é"));
        assertEquals(2, Utf8.encodedLength("ü"));
        assertEquals(4, Utf8.encodedLength("漢字"));

        // Test with characters that require more than 2 bytes in UTF-8 encoding
        assertEquals(3, Utf8.encodedLength("☺"));
        assertEquals(4, Utf8.encodedLength("😀"));
    }

    @Test
    public void testIsWellFormed() {
        // Test with well-formed bytes
        assertTrue(Utf8.isWellFormed(new byte[]{0x61}));

        // Test with ill-formed byte sequences
        assertFalse(Utf8.isWellFormed(new byte[]{(byte) 0xC0}));
        assertFalse(Utf8.isWellFormed(new byte[]{(byte) 0xC0, (byte) 0x80}));
        assertFalse(Utf8.isWellFormed(new byte[]{(byte) 0xE0, (byte) 0x80}));
        assertFalse(Utf8.isWellFormed(new byte[]{(byte) 0xE0, (byte) 0x80, (byte) 0x80}));
        assertFalse(Utf8.isWellFormed(new byte[]{(byte) 0xF0, (byte) 0x80, (byte) 0x80, (byte) 0x80}));
    }

    @Test
    public void testIsWellFormedWithOffsetAndLength() {
        byte[] bytes = {(byte) 0xE2, (byte) 0x82, (byte) 0xAC, (byte) 0xE2, (byte) 0x82, (byte) 0xAC, (byte) 0xE2, (byte) 0x82, (byte) 0xAC};
        int off = 2;
        int len = 6;
        assertTrue(Utf8.isWellFormed(bytes, off, len));

        bytes = new byte[]{(byte) 0xE2, (byte) 0x82, (byte) 0xAC, (byte) 0xE2, (byte) 0x82, (byte) 0xAC, (byte) 0xE2, (byte) 0x82, (byte) 0xAC};
        off = 0;
        len = 9;
        assertTrue(Utf8.isWellFormed(bytes, off, len));

        bytes = new byte[]{(byte) 0xE2, (byte) 0x82, (byte) 0xAC, (byte) 0xE2, (byte) 0x82};
        off = 0;
        len = 5;
        assertFalse(Utf8.isWellFormed(bytes, off, len));
    }

    @Test
    public void testEncodedLengthGeneral() {
        // Test with empty string
        assertEquals(0, Utf8.encodedLengthGeneral("", 0));

        // Test with ASCII characters
        assertEquals(1, Utf8.encodedLengthGeneral("a", 0));
        assertEquals(2, Utf8.encodedLengthGeneral("ab", 0));
        assertEquals(3, Utf8.encodedLengthGeneral("abc", 0));
        assertEquals(4, Utf8.encodedLengthGeneral("abcd", 0));
        assertEquals(5, Utf8.encodedLengthGeneral("abcde", 0));

        // Test with non-ASCII characters
        assertEquals(2, Utf8.encodedLengthGeneral("é", 0));
        assertEquals(2, Utf8.encodedLengthGeneral("ü", 0));
        assertEquals(4, Utf8.encodedLengthGeneral("漢字", 0));

        // Test with characters that require more than 2 bytes in UTF-8 encoding
        assertEquals(3, Utf8.encodedLengthGeneral("☺", 0));
        assertEquals(4, Utf8.encodedLengthGeneral("😀", 0));
    }

    @Test
    public void testIsWellFormedSlowPath() {
        byte[] bytes = {(byte) 0xE2, (byte) 0x82, (byte) 0xAC, (byte) 0xE2, (byte) 0x82, (byte) 0xAC, (byte) 0xE2, (byte) 0x82, (byte) 0xAC};
        int off = 2;
        int end = 8;
        assertTrue(Utf8.isWellFormedSlowPath(bytes, off, end));

        bytes = new byte[]{(byte) 0xE2, (byte) 0x82, (byte) 0xAC, (byte) 0xE2, (byte) 0x82, (byte) 0xAC, (byte) 0xE2, (byte) 0x82, (byte) 0xAC};
        off = 0;
        end = 9;
        assertTrue(Utf8.isWellFormedSlowPath(bytes, off, end));

        bytes = new byte[]{(byte) 0xE2, (byte) 0x82, (byte) 0xAC, (byte) 0xE2, (byte) 0x82};
        off = 0;
        end = 5;
        assertFalse(Utf8.isWellFormedSlowPath(bytes, off, end));
    }
}