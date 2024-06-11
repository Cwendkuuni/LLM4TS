package com.google.common.base;

import org.junit.Test;
import static org.junit.Assert.*;

public class Utf8Test {

    @Test
    public void testEncodedLength_typicalCase() {
        CharSequence sequence = "Hello World";
        int expected = sequence.length();
        int actual = Utf8.encodedLength(sequence);
        assertEquals(expected, actual);
    }

    @Test
    public void testEncodedLength_specialCase() {
        CharSequence sequence = "你好, 世界!";
        int expected = 15;
        int actual = Utf8.encodedLength(sequence);
        assertEquals(expected, actual);
    }

    @Test
    public void testEncodedLength_emptyCase() {
        CharSequence sequence = "";
        int expected = 0;
        int actual = Utf8.encodedLength(sequence);
        assertEquals(expected, actual);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testEncodedLength_overflowCase() {
        CharSequence sequence = "a" + new String(new char[Integer.MAX_VALUE]);
        Utf8.encodedLength(sequence);
    }

    @Test
    public void testIsWellFormed_typicalCase() {
        byte[] bytes = { 72, 101, 108, 108, 111, 32, 87, 111, 114, 108, 100 };
        assertTrue(Utf8.isWellFormed(bytes));
    }

    @Test
    public void testIsWellFormed_invalidCase() {
        byte[] bytes = { -61, -91, 32, -28, -72, -83, 33 };
        assertFalse(Utf8.isWellFormed(bytes));
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testIsWellFormed_invalidIndexCase() {
        byte[] bytes = { 72, 101, 108, 108, 111, 32, 87, 111, 114, 108, 100 };
        Utf8.isWellFormed(bytes, -1, bytes.length);
    }

    @Test
    public void testIsWellFormed_emptyCase() {
        byte[] bytes = new byte[0];
        assertTrue(Utf8.isWellFormed(bytes));
    }

    @Test
    public void testIsWellFormed_slowPathCase() {
        byte[] bytes = { -27, -91, -67, -27, -113, -92, -27, -91, -67 };
        assertFalse(Utf8.isWellFormed(bytes));
    }

    @Test
    public void testIsWellFormedSlowPath_typicalCase() {
        byte[] bytes = { -27, -91, -67, -27, -113, -92, -27, -91, -67 };
        assertFalse(Utf8.isWellFormedSlowPath(bytes, 0, bytes.length));
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testIsWellFormedSlowPath_invalidIndexCase() {
        byte[] bytes = { -27, -91, -67, -27, -113, -92, -27, -91, -67 };
        Utf8.isWellFormedSlowPath(bytes, -1, bytes.length);
    }
}