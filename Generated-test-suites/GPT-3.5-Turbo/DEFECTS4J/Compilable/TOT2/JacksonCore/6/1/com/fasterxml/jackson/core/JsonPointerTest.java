package com.fasterxml.jackson.core;
import static org.junit.Assert.*;
import org.junit.Test;

public class JsonPointerTest {

    @Test
    public void testEmptyConstructor() {
        JsonPointer jsonPointer = new JsonPointer();
        assertTrue(jsonPointer.matches());
        assertEquals("", jsonPointer.getMatchingProperty());
        assertEquals(-1, jsonPointer.getMatchingIndex());
        assertFalse(jsonPointer.mayMatchProperty());
        assertFalse(jsonPointer.mayMatchElement());
    }

    @Test
    public void testCompileValidInput() {
        JsonPointer jsonPointer = JsonPointer.compile("/foo/bar");
        assertNotNull(jsonPointer);
        assertFalse(jsonPointer.matches());
        assertEquals("foo", jsonPointer.getMatchingProperty());
        assertEquals(-1, jsonPointer.getMatchingIndex());
        assertTrue(jsonPointer.mayMatchProperty());
        assertFalse(jsonPointer.mayMatchElement());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCompileInvalidInput() {
        JsonPointer.compile("foo/bar");
    }

    // Add more test cases for other methods

}