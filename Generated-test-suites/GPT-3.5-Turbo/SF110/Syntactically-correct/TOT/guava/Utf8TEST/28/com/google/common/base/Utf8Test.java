package com.google.common.base;

import org.junit.Test;
import static org.junit.Assert.*;

public class Utf8Test {

    @Test
    public void testEncodedLength() {
        // Typical use case: Testing a sequence with ASCII characters
        assertEquals(5, Utf8.encodedLength("Hello"));

        // Edge case: Testing an empty sequence
        assertEquals(0, Utf8.encodedLength(""));

        // Error scenario: Testing a sequence with non-ASCII characters
        try {
            Utf8.encodedLength("こんにちは");
            fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException e){
            assertEquals("UTF-8 length does not fit in int: 4294967301", e.getMessage());
        }
    }

    @Test
    public void testEncodedLengthGeneral() {
        // Typical use case: Testing a sequence with characters within the range '\u0080' <= c < '\u0800'
        assertEquals(3, Utf8.encodedLengthGeneral("µùô"));

        // Edge case: Testing a sequence with a single character within the range '\u0080' <= c < '\u0800'
        assertEquals(2, Utf8.encodedLengthGeneral("ä"));

        // Error scenario: Testing a sequence with unpaired surrogates
        try {
            Utf8.encodedLengthGeneral("𐀁");
            fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException e) {
            assertEquals("Unpaired surrogate at index 0", e.getMessage());
        }
    }

    @Test
    public void testIsWellFormed() {
        byte[] validBytes = {65, 66, 67};
        byte[] invalidBytes = {65, 127, 67};

        // Typical use case: Testing a well-formed byte sequence
        assertTrue(Utf8.isWellFormed(validBytes));

        // Edge cases: Testing an empty byte sequence, sequence with a single byte
        assertTrue(Utf8.isWellFormed(new byte[0]));
        assertTrue(Utf8.isWellFormed(new byte[]{65}));

        // Error scenarios: Testing a byte sequence with an invalid byte
        assertFalse(Utf8.isWellFormed(invalidBytes));

        // Error scenario: Testing a byte sequence with multiple invalid bytes
        byte[] multipleInvalidBytes = {65, 127, 67, 128};
        assertFalse(Utf8.isWellFormed(multipleInvalidBytes));
    }

    @Test
    public void testIsWellFormedSlowPath() {
        byte[] validBytes = {65, 126, 66, 125};

        // Typical use case: Testing a well-formed byte sequence with multiple iterations
        assertTrue(Utf8.isWellFormedSlowPath(validBytes, 0, validBytes.length));

        // Edge case: Testing a byte sequence with a single byte
        assertFalse(Utf8.isWellFormedSlowPath(new byte[]{127}, 0, 1));

        // Error scenarios: Testing byte sequences with an invalid byte
        byte[] invalidBytes1 = {65, 160, 66, 125};
        assertFalse(Utf8.isWellFormedSlowPath(invalidBytes1, 0, invalidBytes1.length));

        byte[] invalidBytes2 = {65, 144, 66, 128};
        assertFalse(Utf8.isWellFormedSlowPath(invalidBytes2, 0, invalidBytes2.length));
    }
}