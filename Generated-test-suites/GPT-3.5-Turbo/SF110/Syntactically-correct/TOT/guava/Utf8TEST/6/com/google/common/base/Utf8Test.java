package com.google.common.base;

import org.junit.Test;
import static org.junit.Assert.*;

public class Utf8Test {

    @Test
    public void testEncodedLength_typicalCase() {
        String input = "Test";
        int expected = 4;
        int actual = Utf8.encodedLength(input);
        assertEquals(expected, actual);
    }

    @Test
    public void testEncodedLength_edgeCase_singleChar() {
        String input = "A";
        int expected = 1;
        int actual = Utf8.encodedLength(input);
        assertEquals(expected, actual);
    }

    @Test
    public void testEncodedLength_edgeCase_emptyString() {
        String input = "";
        int expected = 0;
        int actual = Utf8.encodedLength(input);
        assertEquals(expected, actual);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testEncodedLength_errorCase_utf8LengthOverflow() {
        String input = "\uD83D\uDE00"; // Surrogate Pair for emoji "😀"
        Utf8.encodedLength(input);
    }

    @Test
    public void testIsWellFormed_typicalCase() {
        byte[] input = {65, 66, 67}; // ASCII characters 'A', 'B', 'C'
        boolean expected = true;
        boolean actual = Utf8.isWellFormed(input);
        assertEquals(expected, actual);
    }

    @Test
    public void testIsWellFormed_edgeCase_emptyArray() {
        byte[] input = {};
        boolean expected = true;
        boolean actual = Utf8.isWellFormed(input);
        assertEquals(expected, actual);
    }

    @Test
    public void testIsWellFormed_edgeCase_singleByte() {
        byte[] input = {65}; // ASCII character 'A'
        boolean expected = true;
        boolean actual = Utf8.isWellFormed(input);
        assertEquals(expected, actual);
    }

    @Test
    public void testIsWellFormed_errorCase_invalidUtf8Sequence() {
        byte[] input = {-50, 65}; // Invalid UTF-8 sequence: -50
        boolean expected = false;
        boolean actual = Utf8.isWellFormed(input);
        assertEquals(expected, actual);
    }

    @Test
    public void testIsWellFormed_errorCase_unfinishedUtf8Sequence() {
        byte[] input = {-61}; // Unfinished UTF-8 sequence: -61
        boolean expected = false;
        boolean actual = Utf8.isWellFormed(input);
        assertEquals(expected, actual);
    }

    @Test
    public void testIsWellFormedSlowPath_typicalCase() {
        byte[] input = {-61, -92}; // UTF-8 sequence for 'é'
        assertFalse(Utf8.isWellFormedSlowPath(input, 0, input.length));
    }

    @Test
    public void testIsWellFormedSlowPath_edgeCase_incompleteSequence() {
        byte[] input = {65}; // ASCII character 'A'
        assertTrue(Utf8.isWellFormedSlowPath(input, 0, input.length));
    }

    @Test
    public void testIsWellFormedSlowPath_errorCase_invalidUtf8Sequence() {
        byte[] input = {-61, -1}; // Invalid UTF-8 sequence: -1
        assertFalse(Utf8.isWellFormedSlowPath(input, 0, input.length));
    }

}