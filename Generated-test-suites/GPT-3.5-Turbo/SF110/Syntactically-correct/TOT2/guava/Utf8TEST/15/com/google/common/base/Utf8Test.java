package com.google.common.base;
import org.junit.Test;
import static org.junit.Assert.*;

public class Utf8Test {

    @Test
    public void testEncodedLength() {
        // Typical use case
        assertEquals(3, Utf8.encodedLength("abc"));

        // Edge case
        assertEquals(1, Utf8.encodedLength("\u0000")); // Single byte UTF-8 character

        // Exception handling
        try {
            Utf8.encodedLength("\uD800"); // Surrogate pair 
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testIsWellFormed() {
        // Typical use case
        assertTrue(Utf8.isWellFormed(new byte[]{65, 66, 67}));

        // Edge case
        assertFalse(Utf8.isWellFormed(new byte[]{-1, 65, 66})); // Invalid byte at the beginning

        // Exception handling
        try {
            Utf8.isWellFormed(new byte[]{-65, 65, 66}); // Invalid bytes sequence
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }
}