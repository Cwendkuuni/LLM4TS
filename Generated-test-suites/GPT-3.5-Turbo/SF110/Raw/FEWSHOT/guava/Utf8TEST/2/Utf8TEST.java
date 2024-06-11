
package com.google.common.base;

import org.junit.Test;

import static org.junit.Assert.*;

public class Utf8Test {

    @Test
    public void testEncodedLength() {
        assertEquals(0, Utf8.encodedLength(""));
        assertEquals(4, Utf8.encodedLength("test"));
        assertEquals(4, Utf8.encodedLength("测试"));
        assertEquals(4, Utf8.encodedLength("测试test"));
        assertEquals(4, Utf8.encodedLength("1234!@#"));
        assertEquals(15, Utf8.encodedLength("ABCDEFGHIJKLMNOPQRSTUVWXYZ"));
        assertEquals(15, Utf8.encodedLength("abcdefghijklmnopqrstuvwxyz"));
        assertEquals(15, Utf8.encodedLength("0123456789"));
        assertEquals(4, Utf8.encodedLength("!\"#$%&'()*+,-./"));
        assertEquals(4, Utf8.encodedLength(":;<=>?@"));
        assertEquals(4, Utf8.encodedLength("[\\]^_`"));
        assertEquals(4, Utf8.encodedLength("{|}~"));
    }

    @Test
    public void testIsWellFormed() {
        assertTrue(Utf8.isWellFormed(new byte[]{}));
        assertTrue(Utf8.isWellFormed(new byte[]{0x74, 0x65, 0x73, 0x74}));
        assertTrue(Utf8.isWellFormed(new byte[]{(byte)0xE6, (byte)0xB5, (byte)0x8B, (byte)0xE8, (byte)0xAF, (byte)0x95}));
        assertTrue(Utf8.isWellFormed(new byte[]{(byte)0xE6, (byte)0xB5, (byte)0x8B, (byte)0xE8, (byte)0xAF, (byte)0x95, 0x74, 0x65, 0x73, 0x74}));
        assertTrue(Utf8.isWellFormed(new byte[]{0x31, 0x32, 0x33, 0x34, 0x21, 0x40, 0x23}));
        assertTrue(Utf8.isWellFormed(new byte[]{0x41, 0x42, 0x43, 0x44, 0x45, 0x46, 0x47, 0x48, 0x49, 0x4A, 0x4B, 0x4C, 0x4D, 0x4E, 0x4F}));
        assertTrue(Utf8.isWellFormed(new byte[]{0x61, 0x62, 0x63, 0x64, 0x65, 0x66, 0x67, 0x68, 0x69, 0x6A, 0x6B, 0x6C, 0x6D, 0x6E, 0x6F}));
        assertTrue(Utf8.isWellFormed(new byte[]{0x30, 0x31, 0x32, 0x33, 0x34, 0x35, 0x36, 0x37, 0x38, 0x39}));
        assertTrue(Utf8.isWellFormed(new byte[]{0x21, 0x22, 0x23, 0x24, 0x25, 0x26, 0x27, 0x28, 0x29, 0x2A, 0x2B, 0x2C, 0x2D, 0x2E, 0x2F}));
        assertTrue(Utf8.isWellFormed(new byte[]{0x3A, 0x3B, 0x3C, 0x3D, 0x3E, 0x3F, 0x40}));
        assertTrue(Utf8.isWellFormed(new byte[]{0x5B, 0x5C, 0x5D, 0x5E, 0x5F, 0x60}));
        assertTrue(Utf8.isWellFormed(new byte[]{0x7B, 0x7C, 0x7D, 0x7E}));

        assertFalse(Utf8.isWellFormed(new byte[]{(byte)0xFF}));
        assertFalse(Utf8.isWellFormed(new byte[]{(byte)0xC3, (byte)0xFF, (byte)0xC2}));
    }
}
