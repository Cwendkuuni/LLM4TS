package com.google.common.base;

import org.junit.Test;
import static org.junit.Assert.*;

public class Utf8Test {
    @Test
    public void testEncodedLength() {
        // Test empty sequence
        assertEquals(0, Utf8.encodedLength(""));

        // Test sequence with only ASCII characters
        assertEquals(5, Utf8.encodedLength("abcd"));
        assertEquals(11, Utf8.encodedLength("!@#$%^&*()"));

        // Test sequence with non-ASCII characters
        assertEquals(8, Utf8.encodedLength("ümlaut"));
        assertEquals(10, Utf8.encodedLength("한글"));

        // Test sequence with surrogate pairs
        assertEquals(8, Utf8.encodedLength("𝄞𝄞𝄞𝄞"));
        assertEquals(8, Utf8.encodedLength("𝄞𝄞𝄞𝄞𝅘𝅥𝅯"));

        // Test sequence with mixed ASCII and non-ASCII characters
        assertEquals(6, Utf8.encodedLength("aümlaut"));
        assertEquals(11, Utf8.encodedLength("!한글"));
        assertEquals(8, Utf8.encodedLength("𝄞a𝄞𝄞"));
    }

    @Test
    public void testIsWellFormed() {
        // Test well-formed bytes
        assertTrue(Utf8.isWellFormed(new byte[] {(byte)0x61, (byte)0x62, (byte)0x63}));
        assertTrue(Utf8.isWellFormed(new byte[] {(byte)0xE2, (byte)0x9C, (byte)0x93}));

        // Test ill-formed bytes
        assertFalse(Utf8.isWellFormed(new byte[] {(byte)0xC3, (byte)0x28}));
        assertFalse(Utf8.isWellFormed(new byte[] {(byte)0xF0, (byte)0x28, (byte)0x8C, (byte)0xBC}));
    }

    @Test
    public void testIsWellFormedWithOffsetAndLength() {
        // Test well-formed bytes
        assertTrue(Utf8.isWellFormed(new byte[] {(byte)0x61, (byte)0x62, (byte)0x63, (byte)0x64, (byte)0x65}, 2, 2));
        assertTrue(Utf8.isWellFormed(new byte[] {(byte)0xE2, (byte)0x9C, (byte)0x93, (byte)0x00}, 0, 3));

        // Test ill-formed bytes
        assertFalse(Utf8.isWellFormed(new byte[] {(byte)0xC3, (byte)0x28, (byte)0x65}, 0, 3));
        assertFalse(Utf8.isWellFormed(new byte[] {(byte)0xF0, (byte)0x28, (byte)0x8C, (byte)0xBC, (byte)0x10}, 1, 3));
    }
}