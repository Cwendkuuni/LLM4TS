package com.google.common.base;

import org.junit.Test;

import static org.junit.Assert.*;

public class Utf8Test {
    @Test
    public void testEncodedLength() {
        // Test case 1
        assertEquals(0, Utf8.encodedLength(""));
        
        // Test case 2
        assertEquals(1, Utf8.encodedLength("a"));
        
        // Test case 3
        assertEquals(1, Utf8.encodedLength("\u0080"));
        
        // Test case 4
        assertEquals(1, Utf8.encodedLength("\u07FF"));
        
        // Test case 5
        assertEquals(2, Utf8.encodedLength("\u0800"));
        
        // Test case 6
        assertEquals(3, Utf8.encodedLength("\u1000"));
        
        // Test case 7
        assertEquals(3, Utf8.encodedLength("\uD7FF"));
        
        // Test case 8
        assertEquals(3, Utf8.encodedLength("\uE000"));
        
        // Test case 9
        assertEquals(1, Utf8.encodedLength("\uFFFD"));
        
        // Test case 10
        assertEquals(2, Utf8.encodedLength("\uD800\uDC00"));
        
        // Test case 11
        assertEquals(2, Utf8.encodedLength("\uD800\uDFFF"));
        
        // Test case 12
        assertEquals(3, Utf8.encodedLength("\uDBFF\uDFFF"));
        
        // Test case 13
        assertEquals(4, Utf8.encodedLength("\uDBC0\uDC01"));
    }
    
    @Test
    public void testIsWellFormed() {
        // Test case 1
        assertTrue(Utf8.isWellFormed(new byte[]{}));
        
        // Test case 2
        assertTrue(Utf8.isWellFormed(new byte[]{97}));
        
        // Test case 3
        assertTrue(Utf8.isWellFormed(new byte[]{(byte) 0xC2, (byte) 0x80}));
        
        // Test case 4
        assertTrue(Utf8.isWellFormed(new byte[]{(byte) 0xDF, (byte) 0xBF}));
        
        // Test case 5
        assertTrue(Utf8.isWellFormed(new byte[]{(byte) 0xE0, (byte) 0xA0, (byte) 0x80}));
        
        // Test case 6
        assertTrue(Utf8.isWellFormed(new byte[]{(byte) 0xE1, (byte) 0x80, (byte) 0x80}));
        
        // Test case 7
        assertTrue(Utf8.isWellFormed(new byte[]{(byte) 0xED, (byte) 0x9F, (byte) 0xBF}));
        
        // Test case 8
        assertTrue(Utf8.isWellFormed(new byte[]{(byte) 0xEE, (byte) 0x80, (byte) 0x80}));
        
        // Test case 9
        assertTrue(Utf8.isWellFormed(new byte[]{(byte) 0xEF, (byte) 0xBF, (byte) 0xBF}));
        
        // Test case 10
        assertTrue(Utf8.isWellFormed(new byte[]{(byte) 0xF0, (byte) 0x90, (byte) 0x80, (byte) 0x80}));
        
        // Test case 11
        assertTrue(Utf8.isWellFormed(new byte[]{(byte) 0xF4, (byte) 0x8F, (byte) 0xBF, (byte) 0xBF}));
        
        // Test case 12
        assertFalse(Utf8.isWellFormed(new byte[]{(byte) 0xFF}));
    }
}