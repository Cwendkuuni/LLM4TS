package com.google.common.base;

import org.junit.Test;
import static org.junit.Assert.*;

public class Utf8Test {

    @Test
    public void testEncodedLength_RegularSequence() {
        CharSequence sequence = "Hello World!";
        int expected = 12;
        int actual = Utf8.encodedLength(sequence);
        assertEquals(expected, actual);
    }

    @Test
    public void testEncodedLength_EmptySequence() {
        CharSequence sequence = "";
        int expected = 0;
        int actual = Utf8.encodedLength(sequence);
        assertEquals(expected, actual);
    }

    @Test
    public void testEncodedLength_SpecialCharacters() {
        CharSequence sequence = "😀 some special characters: Ω ç é";
        int expected = 29;
        int actual = Utf8.encodedLength(sequence);
        assertEquals(expected, actual);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testEncodedLength_InvalidLength() {
        CharSequence sequence = "𠜎";
        Utf8.encodedLength(sequence);
    }

    @Test
    public void testIsWellFormed_ValidBytes() {
        byte[] bytes = {72, 101, 108, 108, 111};
        boolean expected = true;
        boolean actual = Utf8.isWellFormed(bytes);
        assertEquals(expected, actual);
    }

    @Test
    public void testIsWellFormed_InvalidBytes() {
        byte[] bytes = {-96, -90, -100, 72, 101, 108, 108, 111};
        boolean expected = false;
        boolean actual = Utf8.isWellFormed(bytes);
        assertEquals(expected, actual);
    }

    @Test
    public void testIsWellFormed_InvalidBytes2() {
        byte[] bytes = {-32, -96, -90};
        boolean expected = false;
        boolean actual = Utf8.isWellFormed(bytes);
        assertEquals(expected, actual);
    }

    @Test
    public void testIsWellFormedSlowPath_ValidBytes() {
        byte[] bytes = {72, 101, 108, 108, 111};
        int off = 0;
        int len = 5;
        boolean expected = true;
        boolean actual = Utf8.isWellFormedSlowPath(bytes, off, len);
        assertEquals(expected, actual);
    }

    @Test
    public void testIsWellFormedSlowPath_ValidBytes2() {
        byte[] bytes = {-32, -96, -91};
        int off = 0;
        int len = 3;
        boolean expected = true;
        boolean actual = Utf8.isWellFormedSlowPath(bytes, off, len);
        assertEquals(expected, actual);
    }

    @Test
    public void testIsWellFormedSlowPath_InvalidBytes() {
        byte[] bytes = {-32, -96};
        int off = 0;
        int len = 2;
        boolean expected = false;
        boolean actual = Utf8.isWellFormedSlowPath(bytes, off, len);
        assertEquals(expected, actual);
    }

}