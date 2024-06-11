
package com.google.common.base;

import org.junit.Test;
import static org.junit.Assert.*;

public class Utf8Test {
  
    @Test
    public void testEncodedLength() {
        // Test with empty sequence
        assertEquals(0, Utf8.encodedLength(""));
        
        // Test with single character sequence
        assertEquals(1, Utf8.encodedLength("a"));
        assertEquals(1, Utf8.encodedLength("1"));
        assertEquals(1, Utf8.encodedLength("!"));

        // Test with multi-character sequence
        assertEquals(5, Utf8.encodedLength("hello"));
        assertEquals(15, Utf8.encodedLength("こんにちは"));
        
        // Test with invalid input
        try {
            Utf8.encodedLength("hello\uD801");
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Exception caught as expected
        }
    }
    
    @Test
    public void testIsWellFormed() {
        // Test with well-formed input
        assertTrue(Utf8.isWellFormed(new byte[]{65, 66, 67}));
        assertTrue(Utf8.isWellFormed(new byte[]{-27, -91, -67, -27, -113, -116}));

        // Test with ill-formed input
        assertFalse(Utf8.isWellFormed(new byte[]{65, -27, -67, -65, 67}));
        assertFalse(Utf8.isWellFormed(new byte[]{-27, -91, -67, -27, -113}));
    }
    
    @Test
    public void testIsWellFormedWithOffsetAndLength() {
        // Test with well-formed input
        assertTrue(Utf8.isWellFormed(new byte[]{65, 66, 67, -27, -91, -67, -27, -113, -116, 65, 66, 67}, 3, 6));

        // Test with ill-formed input
        assertFalse(Utf8.isWellFormed(new byte[]{65, -27, -67, -65, 67, -27, -91, -67, -27, -113, -116, 65, 66, 67}, 3, 9));
    }
  
}
