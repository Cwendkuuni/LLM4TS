package com.example;

import static org.junit.Assert.*;
import com.fasterxml.jackson.core.JsonPointer;

import org.junit.Test;

public class JsonPointerTest {

    @Test
    public void testJsonPointerCompile() {
        String input = "/foo/0";
        JsonPointer jsonPointer = JsonPointer.compile(input);
        assertEquals("/foo/0", jsonPointer.toString());

        input = "/";
        jsonPointer = JsonPointer.compile(input);
        assertEquals("/", jsonPointer.toString());

        input = "/foo";
        jsonPointer = JsonPointer.compile(input);
        assertEquals("/foo", jsonPointer.toString());

        input = "/foo/bar/baz";
        jsonPointer = JsonPointer.compile(input);
        assertEquals("/foo/bar/baz", jsonPointer.toString());

        input = "/foo/-1";
        try {
            jsonPointer = JsonPointer.compile(input);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        input = "~/foo";
        jsonPointer = JsonPointer.compile(input);
        assertEquals("/foo", jsonPointer.toString());
    }

    @Test
    public void testJsonPointerMatches() {
        String input = "/foo/bar/baz";
        JsonPointer jsonPointer = JsonPointer.compile(input);

        assertTrue(jsonPointer.matches());
        JsonPointer emptyJsonPointer = JsonPointer.EMPTY;
        assertFalse(emptyJsonPointer.matches());
    }

    @Test
    public void testJsonPointerEmpty() {
        JsonPointer emptyJsonPointer = JsonPointer.EMPTY;
        assertTrue(emptyJsonPointer.matches());
        assertEquals("", emptyJsonPointer.getMatchingProperty());
        assertEquals(-1, emptyJsonPointer.getMatchingIndex());
        assertFalse(emptyJsonPointer.mayMatchProperty());
        assertFalse(emptyJsonPointer.mayMatchElement());
        assertNull(emptyJsonPointer.tail());
    }

    @Test
    public void testJsonPointerNullInput() {
        try {
            JsonPointer jsonPointer = JsonPointer.compile(null);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testJsonPointerEmptyInput() {
        try {
            JsonPointer jsonPointer = JsonPointer.compile("");
            jsonPointer.toString();
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testJsonPointerInvalidInput() {
        try {
            JsonPointer jsonPointer = JsonPointer.compile("invalid");
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }
}