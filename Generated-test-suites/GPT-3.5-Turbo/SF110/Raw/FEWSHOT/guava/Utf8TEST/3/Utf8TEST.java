package com.google.common.base;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class Utf8Test {
    @Test
    public void testEncodedLength() {
        // Test with empty string
        assertEquals(0, Utf8.encodedLength(""));

        // Test with ASCII characters
        assertEquals(1, Utf8.encodedLength("a"));
        assertEquals(2, Utf8.encodedLength("ab"));
        assertEquals(3, Utf8.encodedLength("abc"));

        // Test with non-ASCII characters
        assertEquals(2, Utf8.encodedLength("é"));
        assertEquals(3, Utf8.encodedLength("€"));
        assertEquals(4, Utf8.encodedLength("💡"));
        assertEquals(5, Utf8.encodedLength("𝄞"));

        // Test with surrogate pairs
        assertEquals(4, Utf8.encodedLength("𐐀"));

        // Test with long string
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 1000000; i++) {
            sb.append("a");
        }
        assertEquals(1000000, Utf8.encodedLength(sb.toString()));
    }

    @Test
    public void testIsWellFormed() {
        // Test with empty byte array
        assertTrue(Utf8.isWellFormed(new byte[0]));

        // Test with ASCII characters
        assertTrue(Utf8.isWellFormed(new byte[]{97}));
        assertTrue(Utf8.isWellFormed(new byte[]{97, 98}));
        assertTrue(Utf8.isWellFormed(new byte[]{97, 98, 99}));

        // Test with non-ASCII characters
        assertTrue(Utf8.isWellFormed(new byte[]{-61, -87}));
        assertTrue(Utf8.isWellFormed(new byte[]{-30, -126, -84}));
        assertTrue(Utf8.isWellFormed(new byte[]{-16, -97, -109, -121}));

        // Test with surrogate pairs
        assertFalse(Utf8.isWellFormed(new byte[]{-16, -124, -104, -115}));
        assertFalse(Utf8.isWellFormed(new byte[]{-64, -103}));
        
        // Test with invalid UTF-8
        assertFalse(Utf8.isWellFormed(new byte[]{127}));
        assertFalse(Utf8.isWellFormed(new byte[]{-65}));
        assertFalse(Utf8.isWellFormed(new byte[]{-17, -65}));
        assertFalse(Utf8.isWellFormed(new byte[]{-17, -65, -65}));
        assertFalse(Utf8.isWellFormed(new byte[]{-17, -65, -65, -65}));
    }
}