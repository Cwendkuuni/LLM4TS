package com.google.common.base;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class Utf8Test {
    @Test
    public void testEncodedLength() {
        assertEquals(3, Utf8.encodedLength("abc")); // Test string with ASCII characters
        assertEquals(7, Utf8.encodedLength("αβγδ")); // Test string with non-ASCII characters
        assertEquals(3, Utf8.encodedLength("abc\uD834\uDF06def")); // Test string with surrogate pairs
        assertEquals(Integer.MAX_VALUE, Utf8.encodedLength("")); // Test empty string
    }
    
    @Test
    public void testEncodedLengthGeneral() {
        assertEquals(3, Utf8.encodedLengthGeneral("abc", 0)); // Test string with ASCII characters
        assertEquals(8, Utf8.encodedLengthGeneral("αβγδ", 0)); // Test string with non-ASCII characters
        assertEquals(4, Utf8.encodedLengthGeneral("abc\uD834\uDF06def", 3)); // Test string with surrogate pairs
    }
    
    @Test
    public void testIsWellFormed() {
        byte[] bytes1 = {(byte)0x61, (byte)0x62, (byte)0x63}; // Valid UTF-8 encoded bytes for "abc"
        assertTrue(Utf8.isWellFormed(bytes1));
        
        byte[] bytes2 = {(byte)0xC2, (byte)0xA9}; // Valid UTF-8 encoded bytes for "©"
        assertTrue(Utf8.isWellFormed(bytes2));
        
        byte[] bytes3 = {(byte)0xC3, (byte)0x28, (byte)0xC3, (byte)0xA9}; // Invalid UTF-8 encoded bytes
        assertFalse(Utf8.isWellFormed(bytes3));
    }
    
    @Test
    public void testIsWellFormedSlowPath() {
        byte[] bytes1 = {(byte)0xC2, (byte)0xA9}; // Valid UTF-8 encoded bytes for "©"
        assertTrue(Utf8.isWellFormedSlowPath(bytes1, 0, 2));
        
        byte[] bytes2 = {(byte)0xC3, (byte)0x28, (byte)0xC3, (byte)0xA9}; // Invalid UTF-8 encoded bytes
        assertFalse(Utf8.isWellFormedSlowPath(bytes2, 0, 4));
    }
}