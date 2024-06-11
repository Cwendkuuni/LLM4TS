package com.google.common.base;
import org.junit.Test;
import static org.junit.Assert.*;

public class Utf8Test {
    
    @Test
    public void testEncodedLength() {
        // Typical use case
        assertEquals(6, Utf8.encodedLength("Hello!"));
        
        // Edge case - empty sequence
        assertEquals(0, Utf8.encodedLength(""));
        
        // Error scenario - invalid Unicode character
        try {
            Utf8.encodedLength("Hello\uD801");
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected exception
        }
    }
    
    @Test
    public void testIsWellFormed() {
        // Typical use case
        byte[] validBytes = {(byte) 0x41, (byte) 0x42, (byte) 0x43};
        assertTrue(Utf8.isWellFormed(validBytes));
        
        // Edge case - empty byte array
        byte[] emptyBytes = {};
        assertTrue(Utf8.isWellFormed(emptyBytes));
        
        // Error scenario - invalid UTF-8 byte
        byte[] invalidBytes = {(byte) 0x41, (byte) 0xC0, (byte) 0x42};
        assertFalse(Utf8.isWellFormed(invalidBytes));
    }
}