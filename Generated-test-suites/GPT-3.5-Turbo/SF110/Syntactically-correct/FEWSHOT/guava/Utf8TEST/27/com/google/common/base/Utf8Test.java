package com.google.common.base;

import static org.junit.Assert.*;

import org.junit.Test;

public class Utf8Test {

    @Test
    public void testEncodedLength() {
        assertEquals(4, Utf8.encodedLength("hello"));
        assertEquals(8, Utf8.encodedLength("こんにちは"));
        assertEquals(6, Utf8.encodedLength("Übung"));
        assertEquals(12, Utf8.encodedLength("😀😃😄😁"));
    }

    @Test
    public void testIsWellFormed() {
        assertTrue(Utf8.isWellFormed(new byte[]{0x68, 0x65, 0x6c, 0x6c, 0x6f}));
        assertTrue(Utf8.isWellFormed(new byte[]{0xe3, 0x81, 0x93, 0xe3, 0x82, 0x93}));
        assertTrue(Utf8.isWellFormed(new byte[]{0xc3, 0x9c, 0x62, 0x75, 0x6e, 0x67}));
        assertTrue(Utf8.isWellFormed(new byte[]{0xf0, 0x9f, 0x98, 0x80, 0xf0, 0x9f, 0x98, 0x83, 0xf0, 0x9f, 0x98, 0x84, 0xf0, 0x9f, 0x98, 0x81}));
    }
}