package com.google.common.base;

import org.junit.Test;
import static org.junit.Assert.*;

public class Utf8Test {

    @Test
    public void testEncodedLength_typicalCase() {
        // Typical case: a sequence with all ASCII characters
        CharSequence sequence = "Hello World";
        
        int expectedLength = 11;
        int result = Utf8.encodedLength(sequence);
        
        assertEquals(expectedLength, result);
    }
    
    @Test
    public void testEncodedLength_edgeCase() {
        // Edge case: an empty sequence
        CharSequence sequence = "";
        
        int expectedLength = 0;
        int result = Utf8.encodedLength(sequence);
        
        assertEquals(expectedLength, result);
    }
    
    @Test
    public void testEncodedLength_errorScenario() {
        // Error scenario: sequence with a surrogate pair
        CharSequence sequence = "Hello \ud83d\ude01";
        
        try {
            Utf8.encodedLength(sequence);
            fail("IllegalArgumentException should have been thrown");
        } catch (IllegalArgumentException e) {
            // IllegalArgumentException expected
        }
    }
    
    @Test
    public void testIsWellFormed_typicalCase() {
        // Typical case: well-formed byte array
        byte[] bytes = { 72, 101, 108, 108, 111, 32, 87, 111, 114, 108, 100 };
        
        boolean result = Utf8.isWellFormed(bytes);
        
        assertTrue(result);
    }
    
    @Test
    public void testIsWellFormed_edgeCase() {
        // Edge case: empty byte array
        byte[] bytes = new byte[0];
        
        boolean result = Utf8.isWellFormed(bytes);
        
        assertTrue(result);
    }
    
    @Test
    public void testIsWellFormed_errorScenario() {
        // Error scenario: malformed byte array
        byte[] bytes = { 72, -50, 108, 108, 111 };
        
        boolean result = Utf8.isWellFormed(bytes);
        
        assertFalse(result);
    }
    
    @Test
    public void testIsWellFormedSlowPath_typicalCase() {
        // Typical case: well-formed byte array in slow path
        byte[] bytes = { 72, -61, -87, 108, 108, 111 };
        int off = 1;
        int len = 5;
        
        boolean result = Utf8.isWellFormedSlowPath(bytes, off, len);
        
        assertTrue(result);
    }
    
    @Test
    public void testIsWellFormedSlowPath_edgeCase() {
        // Edge case: empty byte array in slow path
        byte[] bytes = new byte[0];
        int off = 0;
        int len = 0;
        
        boolean result = Utf8.isWellFormedSlowPath(bytes, off, len);
        
        assertTrue(result);
    }
    
    @Test
    public void testIsWellFormedSlowPath_errorScenario() {
        // Error scenario: malformed byte array in slow path
        byte[] bytes = { 72, -50, -70, 108, 108, 111 };
        int off = 1;
        int len = 5;
        
        boolean result = Utf8.isWellFormedSlowPath(bytes, off, len);
        
        assertFalse(result);
    }
}