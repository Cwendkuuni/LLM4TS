package com.google.common.base;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class Utf8Test {
    
    @Test
    public void testEncodedLength() {
        assertEquals(0, Utf8.encodedLength(""));
        assertEquals(1, Utf8.encodedLength("a"));
        assertEquals(4, Utf8.encodedLength("abcd"));
        assertEquals(7, Utf8.encodedLength("foobar"));
        assertEquals(13, Utf8.encodedLength("hello world!"));
    }
    
    @Test
    public void testEncodedLength_withSurrogatePair() {
        assertEquals(4, Utf8.encodedLength("🌟"));
        assertEquals(5, Utf8.encodedLength("🌟🌟"));
        assertEquals(8, Utf8.encodedLength("🌟🌟🌟"));
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testEncodedLength_invalidInput() {
        Utf8.encodedLength("𐀀");
    }
    
    @Test
    public void testIsWellFormed() {
        assertTrue(Utf8.isWellFormed(new byte[] {}));
        assertTrue(Utf8.isWellFormed(new byte[] {(byte) 0x61}));
        assertTrue(Utf8.isWellFormed(new byte[] {(byte) 0x65, (byte) 0x6c, (byte) 0x6c, (byte) 0x6f}));
        assertTrue(Utf8.isWellFormed(new byte[] {(byte) 0xed, (byte) 0x9f, (byte) 0x8c, (byte) 0x9f}));
    }
    
    @Test
    public void testIsWellFormed_invalidInput() {
        assertFalse(Utf8.isWellFormed(new byte[] {(byte) 0xf0, (byte) 0x90, (byte) 0x80}));
        assertFalse(Utf8.isWellFormed(new byte[] {(byte) 0xed, (byte) 0xa0, (byte) 0x80}));
        assertFalse(Utf8.isWellFormed(new byte[] {(byte) 0xc0}));
    }
    
    @Test
    public void testIsWellFormed_tailingBytes() {
        assertFalse(Utf8.isWellFormed(new byte[] {(byte) 0x61, (byte) 0x80}));
        assertFalse(Utf8.isWellFormed(new byte[] {(byte) 0x65, (byte) 0x6c, (byte) 0x6c, (byte) 0x6f, (byte) 0x80}));
        assertFalse(Utf8.isWellFormed(new byte[] {(byte) 0xed, (byte) 0x9f, (byte) 0x8c, (byte) 0x9f, (byte) 0x80}));
    }
    
    @Test
    public void testIsWellFormed_truncatedByteSequence() {
        assertFalse(Utf8.isWellFormed(new byte[] {(byte) 0xed, (byte) 0x9f, (byte) 0x8c}));
        assertFalse(Utf8.isWellFormed(new byte[] {(byte) 0xf0, (byte) 0x90, (byte) 0x80, (byte) 0x8b}));
    }
}