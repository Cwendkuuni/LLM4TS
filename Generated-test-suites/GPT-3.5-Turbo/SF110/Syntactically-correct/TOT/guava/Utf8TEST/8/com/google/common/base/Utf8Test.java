
package com.google.common.base;

import org.junit.Test;
import static org.junit.Assert.*;

public class Utf8Test {
    
    // Test case for the encodedLength() method
    @Test
    public void testEncodedLength() {
        // Typical use case
        assertEquals(10, Utf8.encodedLength("hello world"));
        
        // Edge case: empty string
        assertEquals(0, Utf8.encodedLength(""));
        
        // Edge case: single character string
        assertEquals(1, Utf8.encodedLength("a"));
        
        // Error scenario: string with surrogate pair
        try {
            Utf8.encodedLength("ab\uD83D\uDE00");
            fail("IllegalArgumentException not thrown");
        } catch (IllegalArgumentException e) {
            assertEquals("Unpaired surrogate at index 2", e.getMessage());
        }
    }
    
    // Test case for the isWellFormed() method
    @Test
    public void testIsWellFormed() {
        // Typical use case
        byte[] bytes1 = {-30, -128, -100, 65, 66, 67};
        assertTrue(Utf8.isWellFormed(bytes1));
        
        // Edge case: empty array
        byte[] bytes2 = {};
        assertTrue(Utf8.isWellFormed(bytes2));
        
        // Edge case: array with a single well-formed character
        byte[] bytes3 = {97};
        assertTrue(Utf8.isWellFormed(bytes3));
        
        // Error scenario: array with an ill-formed character
        byte[] bytes4 = {97, (byte) 0xC0, 65};
        assertFalse(Utf8.isWellFormed(bytes4));
        
        // Error scenario: array with an incomplete sequence
        byte[] bytes5 = {97, (byte) 0xC0};
        assertFalse(Utf8.isWellFormed(bytes5));
    }
    
    // Test case for the isWellFormed(byte[] bytes, int off, int len) method
    @Test
    public void testIsWellFormedWithOffsets() {
        // Typical use case
        byte[] bytes1 = {97, (byte) 0xC0, 65};
        assertTrue(Utf8.isWellFormed(bytes1, 1, 2));
        
        // Edge case: empty array with offsets
        byte[] bytes2 = {};
        assertTrue(Utf8.isWellFormed(bytes2, 0, 0));
        
        // Edge case: array with offsets but still well-formed
        byte[] bytes3 = {97, (byte) 0xC0, 65};
        assertTrue(Utf8.isWellFormed(bytes3, 0, 3));
        
        // Error scenario: incomplete sequence with offsets
        byte[] bytes4 = {97, (byte) 0xC0};
        assertFalse(Utf8.isWellFormed(bytes4, 0, 2));
        
        // Error scenario: ill-formed character with offsets
        byte[] bytes5 = {97, (byte) 0xC0, 65};
        assertFalse(Utf8.isWellFormed(bytes5, 1, 3));
    }
    
    // Test case for the isWellFormedSlowPath() method
    @Test
    public void testIsWellFormedSlowPath() {
        // Typical use case
        byte[] bytes1 = {97, (byte) 0xC0, 65};
        assertFalse(Utf8.isWellFormedSlowPath(bytes1, 1, 3));
        
        // Edge case: empty array
        byte[] bytes2 = {};
        assertFalse(Utf8.isWellFormedSlowPath(bytes2, 0, 0));
        
        // Error scenario: incomplete sequence
        byte[] bytes3 = {97, (byte) 0xC0};
        assertFalse(Utf8.isWellFormedSlowPath(bytes3, 0, 2));
        
        // Error scenario: ill-formed character
        byte[] bytes4 = {97, (byte) 0xC0, 65};
        assertFalse(Utf8.isWellFormedSlowPath(bytes4, 1, 3));
    }
}

