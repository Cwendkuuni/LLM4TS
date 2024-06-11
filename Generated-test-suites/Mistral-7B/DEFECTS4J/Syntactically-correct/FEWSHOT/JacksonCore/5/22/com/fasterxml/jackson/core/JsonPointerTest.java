package com.fasterxml.jackson.core;

import static org.junit.Assert.*;
import com.fasterxml.jackson.core.JsonPointer;

import org.junit.Test;

public class JsonPointerTest {

    @Test
    public void testEmptyJsonPointer() {
        JsonPointer jsonPointer = JsonPointer.EMPTY;
        assertTrue(jsonPointer.matches());
        assertNull(jsonPointer.getMatchingProperty());
        assertFalse(jsonPointer.mayMatchProperty());
        assertFalse(jsonPointer.mayMatchElement());
        assertEquals("", jsonPointer.toString());
    }

    @Test
    public void testJsonPointerWithSingleSegment() {
        String input = "/foo";
        JsonPointer jsonPointer = JsonPointer.compile(input);
        assertTrue(jsonPointer.matches());
        assertEquals("foo", jsonPointer.getMatchingProperty());
        assertFalse(jsonPointer.mayMatchProperty());
        assertFalse(jsonPointer.mayMatchElement());
        assertEquals(input, jsonPointer.toString());
    }

    @Test
    public void testJsonPointerWithSingleIndexedSegment() {
        String input = "/foo/0";
        JsonPointer jsonPointer = JsonPointer.compile(input);
        assertFalse(jsonPointer.matches());
        assertNull(jsonPointer.getMatchingProperty());
        assertTrue(jsonPointer.mayMatchElement());
        assertFalse(jsonPointer.mayMatchProperty());
        assertEquals(input, jsonPointer.toString());
    }

    @Test
    public void testJsonPointerWithMultipleSegments() {
        String input = "/foo/0/bar";
        JsonPointer jsonPointer = JsonPointer.compile(input);
        assertFalse(jsonPointer.matches());
        assertEquals("foo", jsonPointer.getMatchingProperty());
        assertTrue(jsonPointer.mayMatchElement());
        assertFalse(jsonPointer.mayMatchProperty());
        assertEquals(input, jsonPointer.toString());
    }

    @Test
    public void testJsonPointerWithMultipleIndexedSegments() {
        String input = "/foo/0/bar/1";
        JsonPointer jsonPointer = JsonPointer.compile(input);
        assertFalse(jsonPointer.matches());
        assertEquals("foo", jsonPointer.getMatchingProperty());
        assertTrue(jsonPointer.mayMatchElement());
        assertFalse(jsonPointer.mayMatchProperty());
        assertEquals(input, jsonPointer.toString());
    }

    @Test
    public void testJsonPointerWithPropertyMatch() {
        String input = "/foo/bar";
        JsonPointer jsonPointer = JsonPointer.compile(input);
        assertFalse(jsonPointer.matches());
        assertEquals("bar", jsonPointer.getMatchingProperty());
        assertFalse(jsonPointer.mayMatchElement());
        assertTrue(jsonPointer.mayMatchProperty());
        assertEquals(input, jsonPointer.toString());
    }

    @Test
    public void testJsonPointerWithElementMatch() {
        String input = "/foo/0";
        JsonPointer jsonPointer = JsonPointer.compile(input);
        assertFalse(jsonPointer.matches());
        assertNull(jsonPointer.getMatchingProperty());
        assertTrue(jsonPointer.mayMatchElement());
        assertFalse(jsonPointer.mayMatchProperty());
        assertEquals(input, jsonPointer.toString());

        JsonPointer elementJsonPointer = jsonPointer.matchElement(0);
        assertNotNull(elementJsonPointer);
        assertEquals("/foo", elementJsonPointer.toString());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testJsonPointerWithInvalidInput() {
        String input = "~foo";
        JsonPointer.compile(input);
    }
}