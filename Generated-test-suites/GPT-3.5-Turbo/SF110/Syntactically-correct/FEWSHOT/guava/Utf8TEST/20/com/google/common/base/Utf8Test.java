package com.google.common.base;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class Utf8Test {
    
    @Test
    public void testEncodedLength() {
        assertEquals(6, Utf8.encodedLength("hello"));
        assertEquals(12, Utf8.encodedLength("你好"));
        assertEquals(18, Utf8.encodedLength("こんにちは"));
        assertEquals(24, Utf8.encodedLength("안녕하세요"));
        assertEquals(0, Utf8.encodedLength(""));
    }
    
    @Test
    public void testEncodedLengthGeneral() {
        assertEquals(12, Utf8.encodedLengthGeneral("你好", 0));
        assertEquals(18, Utf8.encodedLengthGeneral("こんにちは", 0));
        assertEquals(18, Utf8.encodedLengthGeneral("こんにちは", 5));
        assertEquals(24, Utf8.encodedLengthGeneral("안녕하세요", 0));
        assertEquals(24, Utf8.encodedLengthGeneral("안녕하세요", 5));
    }
    
    @Test
    public void testIsWellFormed() {
        assertTrue(Utf8.isWellFormed(new byte[] {(byte) 0x61, (byte) 0x62, (byte) 0x63}));
        assertTrue(Utf8.isWellFormed(new byte[] {(byte) 0xE4, (byte) 0xBD, (byte) 0xA0, (byte) 0xE5, (byte) 0xA5, (byte) 0xBD}));
        assertTrue(Utf8.isWellFormed(new byte[] {(byte) 0xE3, (byte) 0x81, (byte) 0x93, (byte) 0xE3, (byte) 0x82, (byte) 0x93, (byte) 0xE3, (byte) 0x81, (byte) 0xAB}));
        assertTrue(Utf8.isWellFormed(new byte[] {(byte) 0xEC, (byte) 0x95, (byte) 0x88, (byte) 0xEB, (byte) 0x85, (byte) 0x95, (byte) 0xED, (byte) 0x95, (byte) 0x98, (byte) 0xEC, (byte) 0x84, (byte) 0xB8, (byte) 0xEC, (byte) 0x9A, (byte) 0xA9}));
        assertFalse(Utf8.isWellFormed(new byte[] {(byte) 0xC2, (byte) 0xFE}));
        assertFalse(Utf8.isWellFormed(new byte[] {(byte) 0xC2}));
        assertFalse(Utf8.isWellFormed(new byte[] {(byte) 0xC2, (byte) 0x01}));
    }
    
    @Test
    public void testIsWellFormedSlowPath() {
        assertTrue(Utf8.isWellFormedSlowPath(new byte[] {(byte) 0x61, (byte) 0x62, (byte) 0x63}, 0, 3));
        assertTrue(Utf8.isWellFormedSlowPath(new byte[] {(byte) 0xE4, (byte) 0xBD, (byte) 0xA0, (byte) 0xE5, (byte) 0xA5, (byte) 0xBD}, 0, 6));
        assertTrue(Utf8.isWellFormedSlowPath(new byte[] {(byte) 0xE3, (byte) 0x81, (byte) 0x93, (byte) 0xE3, (byte) 0x82, (byte) 0x93, (byte) 0xE3, (byte) 0x81, (byte) 0xAB}, 0, 9));
        assertTrue(Utf8.isWellFormedSlowPath(new byte[] {(byte) 0xEC, (byte) 0x95, (byte) 0x88, (byte) 0xEB, (byte) 0x85, (byte) 0x95, (byte) 0xED, (byte) 0x95, (byte) 0x98, (byte) 0xEC, (byte) 0x84, (byte) 0xB8, (byte) 0xEC, (byte) 0x9A, (byte) 0xA9}, 0, 15));
        assertFalse(Utf8.isWellFormedSlowPath(new byte[] {(byte) 0xC2, (byte) 0xFE}, 0, 2));
        assertFalse(Utf8.isWellFormedSlowPath(new byte[] {(byte) 0xC2}, 0, 1));
        assertFalse(Utf8.isWellFormedSlowPath(new byte[] {(byte) 0xC2, (byte) 0x01}, 0, 2));
    }
}