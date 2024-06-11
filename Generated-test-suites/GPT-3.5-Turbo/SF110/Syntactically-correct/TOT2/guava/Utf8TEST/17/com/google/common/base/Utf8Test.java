package com.google.common.base;
import org.junit.Test;
import static org.junit.Assert.*;

public class Utf8Test {

    @Test
    public void testEncodedLength() {
        assertEquals(5, Utf8.encodedLength("hello")); // Typical use case
        assertEquals(7, Utf8.encodedLength("こんにちは")); // Typical use case
        assertEquals(0, Utf8.encodedLength("")); // Edge case
        try {
            Utf8.encodedLength(null); // Error scenario
            fail("Expected an IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException e) {
            assertNotNull(e.getMessage());
        }
    }

    @Test
    public void testIsWellFormed() {
        byte[] validBytes = {(byte)0x48, (byte)0x65, (byte)0x6C, (byte)0x6C, (byte)0x6F};
        assertTrue(Utf8.isWellFormed(validBytes)); // Typical use case

        byte[] invalidBytes = {(byte)0x80, (byte)0x30, (byte)0x00};
        assertFalse(Utf8.isWellFormed(invalidBytes)); // Edge case

        try {
            Utf8.isWellFormed(null); // Error scenario
            fail("Expected an IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException e) {
            assertNotNull(e.getMessage());
        }
    }

}