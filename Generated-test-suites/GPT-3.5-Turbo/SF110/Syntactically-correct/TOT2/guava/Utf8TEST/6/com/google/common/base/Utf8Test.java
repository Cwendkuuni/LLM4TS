package com.google.common.base;
import static org.junit.Assert.*;
import org.junit.Test;

public class Utf8Test {

    @Test
    public void testEncodedLength_basic() {
        CharSequence sequence = "Hello";
        int result = Utf8.encodedLength(sequence);
        assertEquals(5, result);
    }

    @Test
    public void testEncodedLength_edgeCase() {
        CharSequence sequence = "\ud800\udfff";
        int result = Utf8.encodedLength(sequence);
        assertEquals(6, result);
    }

    @Test
    public void testEncodedLength_exception() {
        CharSequence sequence = "\ud800";
        try {
            Utf8.encodedLength(sequence);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // expected exception
        }
    }

    @Test
    public void testIsWellFormed_basic() {
        byte[] bytes = {72, 101, 108, 108, 111};
        boolean result = Utf8.isWellFormed(bytes);
        assertTrue(result);
    }

    @Test
    public void testIsWellFormed_edgeCase() {
        byte[] bytes = {-32, -128, -128};
        boolean result = Utf8.isWellFormed(bytes);
        assertFalse(result);
    }

    @Test
    public void testIsWellFormed_exception() {
        byte[] bytes = {-32, -128};
        try {
            Utf8.isWellFormed(bytes);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // expected exception
        }
    }

}