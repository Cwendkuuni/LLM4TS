package com.fasterxml.jackson.core;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class JsonPointerTest {

    @Test
    public void testEmptyJsonPointer() {
        JsonPointer emptyJsonPointer = JsonPointer.EMPTY;
        assertTrue(emptyJsonPointer.matches());
        assertNull(emptyJsonPointer.getMatchingProperty());
        assertNull(emptyJsonPointer.getMatchingIndex());
        assertFalse(emptyJsonPointer.mayMatchProperty());
        assertFalse(emptyJsonPointer.mayMatchElement());
    }

    @Test
    public void testJsonPointerWithMatchingProperty() {
        String input = "/foo";
        JsonPointer jsonPointer = null;
        try {
            jsonPointer = JsonPointer.compile(input);
        } catch (IllegalArgumentException e) {
            fail("Unexpected IllegalArgumentException: " + e.getMessage());
        }
        assertEquals("/foo", jsonPointer.toString());
        assertTrue(jsonPointer.matches());
        assertEquals("foo", jsonPointer.getMatchingProperty());
        assertFalse(jsonPointer.mayMatchElement());
    }

    @Test
    public void testJsonPointerWithMatchingElement() {
        String input = "/list[0]";
        JsonPointer jsonPointer = null;
        try {
            jsonPointer = JsonPointer.compile(input);
        } catch (IllegalArgumentException e) {
            fail("Unexpected IllegalArgumentException: " + e.getMessage());
        }
        assertEquals("/list[0]", jsonPointer.toString());
        assertTrue(jsonPointer.matches());
        assertNull(jsonPointer.getMatchingProperty());
        assertEquals(0, jsonPointer.getMatchingIndex());
    }

    @Test
    public void testJsonPointerWithSubPointer() {
        String input = "/a/b";
        JsonPointer jsonPointer = null;
        try {
            jsonPointer = JsonPointer.compile(input);
        } catch (IllegalArgumentException e) {
            fail("Unexpected IllegalArgumentException: " + e.getMessage());
        }
        assertEquals("/a/b", jsonPointer.toString());
        assertFalse(jsonPointer.matches());
        assertEquals("b", jsonPointer.getMatchingProperty());
        assertFalse(jsonPointer.mayMatchElement());
        JsonPointer tail = jsonPointer.tail();
        assertEquals("/a", tail.toString());
        assertTrue(tail.matches());
        assertNull(tail.getMatchingProperty());
        assertFalse(tail.mayMatchElement());
    }

    @Test
    public void testJsonPointerWithInvalidInput() {
        String input = "invalid";
        try {
            JsonPointer.compile(input);
            fail("Unexpected result for invalid input: " + input);
        } catch (IllegalArgumentException e) {
            assertEquals("Invalid input: JSON Pointer expression must start with '/': \"invalid\"", e.getMessage());
        }
    }

    @Test
    public void testJsonPointerWithEmptyArray() {
        String input = "/list[]";
        JsonPointer jsonPointer = null;
        try {
            jsonPointer = JsonPointer.compile(input);
        } catch (IllegalArgumentException e) {
            fail("Unexpected IllegalArgumentException: " + e.getMessage());
        }
        assertEquals("/list[]", jsonPointer.toString());
        assertTrue(jsonPointer.matches());
        assertNull(jsonPointer.getMatchingProperty());
        assertFalse(jsonPointer.mayMatchElement());
    }

    @Test
    public void testJsonPointerWithArrayIndexOutOfBounds() {
        String input = "/list[-1]";
        try {
            JsonPointer.compile(input);
            fail("Unexpected result for invalid input: " + input);
        } catch (IllegalArgumentException e) {
            assertEquals("Invalid input: JSON Pointer expression must start with '/': \"/list[-1]\"", e.getMessage());
        }
    }
}