package com.fasterxml.jackson.core;

import org.junit.Test;
import static org.junit.Assert.*;

public class JsonPointerTest {

    @Test
    public void testEmptyJsonPointer() {
        JsonPointer emptyJsonPointer = new JsonPointer();
        assertTrue(emptyJsonPointer.matches());
        assertEquals("", emptyJsonPointer.getMatchingProperty());
        assertEquals(-1, emptyJsonPointer.getMatchingIndex());
        assertEquals("", emptyJsonPointer.toString());
    }

    @Test
    public void testJsonPointerConstruction() {
        String fullString = "/a/b/c";
        String segment = "b";
        JsonPointer next = new JsonPointer("/a/d");
        JsonPointer jsonPointer = new JsonPointer(fullString, segment, next);
        assertEquals(fullString, jsonPointer.toString());
        assertTrue(jsonPointer.matchesProperty("b"));
        assertEquals(next, jsonPointer.tail());
    }

    @Test
    public void testJsonPointerCompile() {
        String jsonPointerString = "/a/b/2";
        JsonPointer jsonPointer = JsonPointer.compile(jsonPointerString);
        assertEquals(jsonPointerString, jsonPointer.toString());
        assertTrue(jsonPointer.matchesElement(2));
    }

    @Test
    public void testJsonPointerParseTail() {
        String jsonPointerString = "/a/b/~1/c";
        JsonPointer jsonPointer = JsonPointer._parseTail(jsonPointerString);
        assertEquals("/c", jsonPointer.toString());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testJsonPointerNullInput() {
        JsonPointer.compile(null);
    }

    @Test
    public void testJsonPointerEmptyPointer() {
        JsonPointer emptyJsonPointer = JsonPointer.compile("");
        assertTrue(emptyJsonPointer.matches());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testJsonPointerInvalidCharacter() {
        String jsonPointerString = "/a/b/~invalid_character/c";
        JsonPointer.compile(jsonPointerString);
    }

    @Test
    public void testJsonPointerEmptySegment() {
        String jsonPointerString = "/a//c";
        JsonPointer jsonPointer = JsonPointer.compile(jsonPointerString);
        assertEquals(jsonPointerString, jsonPointer.toString());
    }

    @Test
    public void testJsonPointerSingleSegment() {
        String jsonPointerString = "/a";
        JsonPointer jsonPointer = JsonPointer.compile(jsonPointerString);
        assertEquals(jsonPointerString, jsonPointer.toString());
        assertTrue(jsonPointer.matchesProperty("a"));
    }

    @Test
    public void testJsonPointerMultipleSegments() {
        String jsonPointerString = "/a/b/c";
        JsonPointer jsonPointer = JsonPointer.compile(jsonPointerString);
        assertEquals(jsonPointerString, jsonPointer.toString());
        assertTrue(jsonPointer.matchesProperty("b"));
        assertTrue(jsonPointer.matchesElement(2));
        assertEquals("/c", jsonPointer.tail().toString());
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testJsonPointerInvalidIndex() {
        String jsonPointerString = "/a/0";
        JsonPointer jsonPointer = JsonPointer.compile(jsonPointerString);
        assertTrue(jsonPointer.matchesElement(0));
        jsonPointer.matchElement(-1); // This should throw an IndexOutOfBoundsException
    }
}