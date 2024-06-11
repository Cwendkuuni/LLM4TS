package com.google.common.base;
import static org.junit.Assert.*;
import org.junit.Test;

public class Utf8Test {
    
    @Test
    public void testEncodedLength() {
        // Typical use case
        assertEquals(3, Utf8.encodedLength("abc"));
        
        // Edge case - empty string
        assertEquals(0, Utf8.encodedLength(""));
        
        // Error scenario - Unicode character that requires 2 bytes
        assertEquals(2, Utf8.encodedLength("€"));
    }
    
    @Test
    public void testIsWellFormed() {
        byte[] validBytes = {97, 98, 99}; // Valid UTF-8 encoding for "abc"
        byte[] invalidBytes = {97, -1, 99}; // Invalid UTF-8 encoding
        
        // Typical use case
        assertTrue(Utf8.isWellFormed(validBytes));
        
        // Edge case - empty byte array
        assertTrue(Utf8.isWellFormed(new byte[0]));
        
        // Error scenario - invalid UTF-8 encoding
        assertFalse(Utf8.isWellFormed(invalidBytes));
    }
}