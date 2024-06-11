package com.fasterxml.jackson.core.test;

import static org.junit.Assert.*;

import com.fasterxml.jackson.core.JsonPointer;

import org.junit.Test;

/**
 * Test class for JsonPointer class.
 */
public class JsonPointerTest {

    // Basic test cases for constructor methods
    @Test
    public void testEmptyJsonPointerConstruction() {
        JsonPointer emptyJsonPointer = new JsonPointer();
        assertTrue(emptyJsonPointer.matches());
        assertEquals("", emptyJsonPointer.getMatchingProperty());
        assertEquals(-1, emptyJsonPointer.getMatchingIndex());
        assertEquals("", emptyJsonPointer.toString());
    }

    @Test
    public void testJsonPointerConstructionWithFullStringAndSegment() {
        String fullString = "/user/name";
        String segment = "name";
        JsonPointer jsonPointer = new JsonPointer(fullString, segment, null);
        assertFalse(jsonPointer.matches());
        assertEquals(segment, jsonPointer.getMatchingProperty());
        assertEquals(-1, jsonPointer.getMatchingIndex());
        assertEquals(fullString, jsonPointer.toString());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIllegalArgumentExceptionForInvalidInput() {
        String input = "invalid_input";
        JsonPointer.compile(input);
    }

    // Test case for factory method compile
    @Test
    public void testJsonPointerCompile() {
        JsonPointer jsonPointer = JsonPointer.compile("/user/name");
        assertFalse(jsonPointer.matches());
        assertEquals("name", jsonPointer.getMatchingProperty());
        assertEquals(-1, jsonPointer.getMatchingIndex());
        assertEquals("/user/name", jsonPointer.toString());
    }

    // Test case for equals method
    @Test
    public void testJsonPointerEquals() {
        String input1 = "/user/name";
        String input2 = "/user/name";
        JsonPointer jsonPointer1 = JsonPointer.compile(input1);
        JsonPointer jsonPointer2 = JsonPointer.compile(input2);
        assertTrue(jsonPointer1.equals(jsonPointer2));
    }

    @Test
    public void testJsonPointerNotEquals() {
        String input1 = "/user/name";
        String input2 = "/user/age";
        JsonPointer jsonPointer1 = JsonPointer.compile(input1);
        JsonPointer jsonPointer2 = JsonPointer.compile(input2);
        assertFalse(jsonPointer1.equals(jsonPointer2));
    }

    // Test case for matchProperty method
    @Test
    public void testJsonPointerMatchProperty() {
        String fullString = "/user/name";
        String propertyName = "name";
        JsonPointer jsonPointer = JsonPointer.compile(fullString);
        JsonPointer matchedJsonPointer = jsonPointer.matchProperty(propertyName);
        assertEquals(propertyName, matchedJsonPointer.getMatchingProperty());
        assertTrue(matchedJsonPointer.matches());
        assertEquals(propertyName, matchedJsonPointer.toString());
    }

    @Test
    public void testJsonPointerMatchPropertyNull() {
        String fullString = "/user/name";
        JsonPointer jsonPointer = JsonPointer.compile(fullString);
        assertNull(jsonPointer.matchProperty(null));
    }

    // Test case for matchElement method
    @Test
    public void testJsonPointerMatchElement() {
        String fullString = "/user/0/name";
        JsonPointer jsonPointer = JsonPointer.compile(fullString);
        JsonPointer matchedJsonPointer = jsonPointer.matchElement(0);
        assertEquals("name", matchedJsonPointer.getMatchingProperty());
        assertTrue(matchedJsonPointer.matches());
        assertEquals("/user/0/name", matchedJsonPointer.toString());
    }

    @Test
    public void testJsonPointerMatchElementNegativeIndex() {
        String fullString = "/user/-1/name";
        JsonPointer jsonPointer = JsonPointer.compile(fullString);
        assertNull(jsonPointer.matchElement(-1));
    }

    // Test case for tail method
    @Test
    public void testJsonPointerTail() {
        String fullString = "/user/name";
        JsonPointer jsonPointer = JsonPointer.compile(fullString);
        JsonPointer tailJsonPointer = jsonPointer.tail();
        assertEquals("/", tailJsonPointer.toString());
    }

    // Test case for toString method
    @Test
    public void testJsonPointerToString() {
        String fullString = "/user/name";
        JsonPointer jsonPointer = JsonPointer.compile(fullString);
        assertEquals(fullString, jsonPointer.toString());
    }

    // Edge cases and exception handling test cases
    @Test(expected = NullPointerException.class)
    public void testJsonPointerNullPointerException() {
        JsonPointer jsonPointer = new JsonPointer(null, null, null);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testJsonPointerIndexOutOfBoundsException() {
        String fullString = "/user/-1/name";
        JsonPointer jsonPointer = JsonPointer.compile(fullString);
        jsonPointer.matchElement(-1);
    }
}