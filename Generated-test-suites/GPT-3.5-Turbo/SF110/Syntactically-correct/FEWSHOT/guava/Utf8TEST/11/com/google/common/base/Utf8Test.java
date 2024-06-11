
package com.google.common.base;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class Utf8Test {
    @Test
    public void testEncodedLength() {
        assertEquals(3, Utf8.encodedLength("abc"));
        assertEquals(5, Utf8.encodedLength("hello"));
        assertEquals(1, Utf8.encodedLength("!"));
        assertEquals(0, Utf8.encodedLength(""));
    }

    @Test
    public void testEncodedLength_generalCase() {
        assertEquals(4, Utf8.encodedLength("汉字"));
        assertEquals(6, Utf8.encodedLength("こんにちは"));
        assertEquals(8, Utf8.encodedLength("¡Hola!"));
    }

    @Test
    public void testEncodedLength_exceptionCase() {
        try {
            Utf8.encodedLength(new String(Character.toChars(0x110000)));
            // If the exception is not thrown, the test should fail.
            assertFalse(true);
        } catch (IllegalArgumentException e) {
            // The exception should be thrown, the test should pass.
            assertTrue(true);
        }
    }

    @Test
    public void testIsWellFormed() {
        assertTrue(Utf8.isWellFormed(new byte[]{97, 98, 99}));
        assertFalse(Utf8.isWellFormed(new byte[]{97, -97, 99}));
        assertTrue(Utf8.isWellFormed(new byte[]{97, -61, -125}));
        assertTrue(Utf8.isWellFormed(new byte[]{97, -30, -128, -128}));
    }
}
