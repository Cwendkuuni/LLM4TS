package com.google.common.base;

import org.junit.Test;
import static org.junit.Assert.*;

public class Utf8Test {

    @Test
    public void testEncodedLength() {
        // Typical use case
        assertEquals(3, Utf8.encodedLength("abc"));
        
        // Edge case
        assertEquals(1, Utf8.encodedLength("\u0800")); // Check for encoding 3-byte character 
    }
    
    @Test
    public void testIsWellFormed() {
        // Typical use case
        assertTrue(Utf8.isWellFormed(new byte[]{97, 98, 99})); // ASCII characters
        
        // Edge case
        assertFalse(Utf8.isWellFormed(new byte[]{-33, -94, -127})); // Invalid UTF-8 byte sequence
    }

}