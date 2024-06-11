package com.fasterxml.jackson.core;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.fasterxml.jackson.core.JsonPointer;

public class JsonPointerTest {

    @Test
    public void testEmptyJsonPointer() {
        JsonPointer jsonPointer = JsonPointer.EMPTY;
        assertTrue(jsonPointer.matches());
        assertNull(jsonPointer.getMatchingProperty());
        assertNull(jsonPointer.getMatchingIndex());
        assertNull(jsonPointer.tail());
        assertEquals("", jsonPointer.toString());
    }

    @Test
    public void testJsonPointerWithSingleSegment() {
        String input = "/foo";
        JsonPointer jsonPointer = JsonPointer.compile(input);
        assertTrue(jsonPointer.matches());
        assertEquals("foo", jsonPointer.getMatchingProperty());
        assertNull(jsonPointer.getMatchingIndex());
        assertNull(jsonPointer.tail());
        assertEquals(input, jsonPointer.toString());
    }

    @Test
    public void testJsonPointerWithMultipleSegments() {
        String input = "/foo/bar/baz";
        JsonPointer jsonPointer = JsonPointer.compile(input);
        assertFalse(jsonPointer.matches());
        assertEquals("foo", jsonPointer.getMatchingProperty());
        assertNull(jsonPointer.getMatchingIndex());
        assertEquals("/bar/baz", jsonPointer.toString());
        JsonPointer tail = jsonPointer.tail();
        assertEquals("bar/baz", tail.toString());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testJsonPointerWithInvalidInput() {
        JsonPointer.compile("~invalid");
    }

    @Test
    public void testJsonPointerMatchProperty() {
        String input = "/foo/bar";
        JsonPointer jsonPointer = JsonPointer.compile(input);
        String propertyName = "bar";
        JsonPointer matchedJsonPointer = jsonPointer.matchProperty(propertyName);
        assertTrue(matchedJsonPointer != null && matchedJsonPointer.matches());
        assertEquals(propertyName, matchedJsonPointer.getMatchingProperty());
        assertNull(matchedJsonPointer.getMatchingIndex());
        assertNull(matchedJsonPointer.tail());
        assertEquals(input.substring(0, input.length() - propertyName.length()), matchedJsonPointer.toString());
    }

    @Test
    public void testJsonPointerMatchElement() {
        String input = "/foo/0";
        JsonPointer jsonPointer = JsonPointer.compile(input);
        int index = 0;
        JsonPointer matchedJsonPointer = jsonPointer.matchElement(index);
        assertTrue(matchedJsonPointer != null && matchedJsonPointer.matches());
        assertEquals("", matchedJsonPointer.getMatchingProperty());
        assertEquals(index, matchedJsonPointer.getMatchingIndex());
        assertNull(matchedJsonPointer.tail());
        assertEquals(input.substring(0, input.length() - String.valueOf(index).length()), matchedJsonPointer.toString());
    }

    @Test
    public void testJsonPointerMatchElementNegativeIndex() {
        String input = "/foo/-1";
        JsonPointer jsonPointer = JsonPointer.compile(input);
        int index = -1;
        JsonPointer matchedJsonPointer = jsonPointer.matchElement(index);
        assertNull(matchedJsonPointer);
    }
}