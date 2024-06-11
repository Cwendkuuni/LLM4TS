package com.google.common.base;

import org.junit.Test;
import static org.junit.Assert.*;

public class Utf8Test {

    @Test
    public void testEncodedLength_basicCase() {
        CharSequence sequence = "Hello";
        int expectedLength = 5;
        int actualLength = Utf8.encodedLength(sequence);
        assertEquals(expectedLength, actualLength);
    }

    @Test
    public void testEncodedLength_edgeCase() {
        CharSequence sequence = "\uD800\uDFFF"; // Surrogate pair
        int expectedLength = 4;
        int actualLength = Utf8.encodedLength(sequence);
        assertEquals(expectedLength, actualLength);
    }

    @Test
    public void testIsWellFormed_basicCase() {
        byte[] bytes = {72, 101, 108, 108, 111}; // "Hello"
        assertTrue(Utf8.isWellFormed(bytes));
    }

    @Test
    public void testIsWellFormed_edgeCase() {
        byte[] bytes = {-50, -100, -50}; // Invalid UTF-8 bytes
        assertFalse(Utf8.isWellFormed(bytes));
    }

    @Test
    public void testIsWellFormed_exceptionCase() {
        byte[] bytes = {-50, -100, -50, -100}; // Invalid UTF-8 bytes
        assertFalse(Utf8.isWellFormed(bytes));
    }

}