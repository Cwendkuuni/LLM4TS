package com.google.common.base;

import static org.junit.Assert.*;
import org.junit.Test;

public class Utf8Test {

    @Test
    public void testEncodedLength() {
        // Test with empty sequence
        assertEquals(0, Utf8.encodedLength(""));
        
        // Test with ASCII characters only
        assertEquals(3, Utf8.encodedLength("abc"));
        
        // Test with characters above ASCII range
        assertEquals(6, Utf8.encodedLength("𐐷"));
        
        // Test with unpaired surrogate
        try {
            Utf8.encodedLength("abc\uD800");
            fail("IllegalArgumentException expected");
        } catch (IllegalArgumentException e) {
            assertEquals("Unpaired surrogate at index 3", e.getMessage());
        }
    }
    
    @Test
    public void testEncodedLengthGeneral() {
        // Test with empty sequence
        assertEquals(0, Utf8.encodedLengthGeneral("", 0));
        
        // Test with ASCII characters only
        assertEquals(1, Utf8.encodedLengthGeneral("abc", 0));
        
        // Test with characters above ASCII range
        assertEquals(3, Utf8.encodedLengthGeneral("𐐷", 0));
        
        // Test with unpaired surrogate
        try {
            Utf8.encodedLengthGeneral("abc\uD800", 0);
            fail("IllegalArgumentException expected");
        } catch (IllegalArgumentException e) {
            assertEquals("Unpaired surrogate at index 3", e.getMessage());
        }
    }
    
    @Test
    public void testIsWellFormed() {
        // Test with well-formed bytes
        byte[] wellFormedBytes = {97, 98, 99};
        assertTrue(Utf8.isWellFormed(wellFormedBytes));
        
        // Test with ill-formed bytes
        byte[] illFormedBytes = {97, -100, 99};
        assertFalse(Utf8.isWellFormed(illFormedBytes));
        
        // Test with empty bytes
        byte[] emptyBytes = {};
        assertTrue(Utf8.isWellFormed(emptyBytes));
    }
    
    @Test
    public void testIsWellFormedWithOffsetAndLength() {
        // Test with well-formed bytes
        byte[] wellFormedBytes = {97, 98, 99};
        assertTrue(Utf8.isWellFormed(wellFormedBytes, 0, wellFormedBytes.length));
        
        // Test with ill-formed bytes
        byte[] illFormedBytes = {97, -100, 99};
        assertFalse(Utf8.isWellFormed(illFormedBytes, 0, illFormedBytes.length));
        
        // Test with empty bytes
        byte[] emptyBytes = {};
        assertTrue(Utf8.isWellFormed(emptyBytes, 0, emptyBytes.length));
    }
    
    @Test
    public void testIsWellFormedSlowPath() {
        // Test with well-formed bytes
        byte[] wellFormedBytes = {97, 98, 99};
        assertTrue(Utf8.isWellFormedSlowPath(wellFormedBytes, 0, wellFormedBytes.length));
        
        // Test with ill-formed bytes
        byte[] illFormedBytes = {97, -100, 99};
        assertFalse(Utf8.isWellFormedSlowPath(illFormedBytes, 0, illFormedBytes.length));
        
        // Test with empty bytes
        byte[] emptyBytes = {};
        assertTrue(Utf8.isWellFormedSlowPath(emptyBytes, 0, emptyBytes.length));
    }
}