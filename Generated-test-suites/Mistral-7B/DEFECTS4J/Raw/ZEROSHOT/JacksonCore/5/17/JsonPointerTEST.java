package com.fasterxml.jackson.core.test;

import static org.junit.Assert.*;

import com.fasterxml.jackson.core.JsonPointer;

import org.junit.Test;

public class JsonPointerTest {

    @Test
    public void testEmptyJsonPointer() {
        JsonPointer jsonPointer = JsonPointer.EMPTY;
        assertTrue(jsonPointer.matches());
        assertEquals("", jsonPointer.getMatchingProperty());
        assertEquals(-1, jsonPointer.getMatchingIndex());
        assertNull(jsonPointer.tail());
        assertEquals("", jsonPointer.toString());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidInput() {
        JsonPointer.compile("invalid");
    }

    @Test
    public void testSimpleJsonPointer() {
        JsonPointer jsonPointer = JsonPointer.compile("/testProperty");
        assertTrue(jsonPointer.matches());
        assertEquals("testProperty", jsonPointer.getMatchingProperty());
        assertNull(jsonPointer.tail());
        assertEquals("/testProperty", jsonPointer.toString());
    }

    @Test
    public void testJsonPointerWithIndex() {
        JsonPointer jsonPointer = JsonPointer.compile("/list[0]");
        assertFalse(jsonPointer.matches());
        assertNull(jsonPointer.getMatchingProperty());
        assertEquals(0, jsonPointer.getMatchingIndex());
        assertNotNull(jsonPointer.tail());
        assertEquals("/list[0]", jsonPointer.toString());
    }

    @Test
    public void testJsonPointerWithProperty() {
        JsonPointer jsonPointerProperty = JsonPointer.compile("/testObject/testProperty");
        JsonPointer jsonPointerMatch = JsonPointer.compile("/testObject/testProperty");
        assertTrue(jsonPointerProperty.matches());
        assertEquals("testProperty", jsonPointerProperty.getMatchingProperty());
        assertNull(jsonPointerProperty.getMatchingIndex());
        assertNotNull(jsonPointerProperty.tail());
        assertEquals("/testObject/testProperty", jsonPointerProperty.toString());
        assertTrue(jsonPointerProperty.matches());
        assertEquals(jsonPointerProperty, jsonPointerMatch);
        assertEquals("/testObject/testProperty", jsonPointerMatch.toString());
    }

    @Test
    public void testJsonPointerWithElement() {
        String jsonString = "[1,2,3,4]";
        JsonPointer jsonPointerIndex = JsonPointer.compile("/" + jsonString + "/[1]");
        JsonPointer jsonPointerMatch = JsonPointer.compile(jsonString + "/[1]");
        assertTrue(jsonPointerIndex.matches());
        assertNull(jsonPointerIndex.getMatchingProperty());
        assertEquals(1, jsonPointerIndex.getMatchingIndex());
        assertNull(jsonPointerIndex.tail());
        assertEquals("/" + jsonString + "/[1]", jsonPointerIndex.toString());
        assertTrue(jsonPointerIndex.matches());
        assertEquals(jsonPointerIndex, jsonPointerMatch);
        assertEquals("/" + jsonString + "/[1]", jsonPointerMatch.toString());
    }

    @Test
    public void testJsonPointerWithMultipleSegments() {
        JsonPointer jsonPointer = JsonPointer.compile("/testObject/list/[1]/testProperty");
        String expected = "/testObject/list/[1]/testProperty";
        assertTrue(jsonPointer.matches());
        assertEquals("testProperty", jsonPointer.getMatchingProperty());
        assertEquals(1, jsonPointer.getMatchingIndex());
        assertNotNull(jsonPointer.tail());
        assertEquals(expected, jsonPointer.toString());
    }

    @Test
    public void testJsonPointerWithEscapedCharacter() {
        JsonPointer jsonPointer = JsonPointer.compile("/testObject/list/[0]/test~1Property");
        String expected = "/testObject/list/[0]/test/1Property";
        assertTrue(jsonPointer.matches());
        assertEquals("testProperty", jsonPointer.getMatchingProperty());
        assertEquals(0, jsonPointer.getMatchingIndex());
        assertNotNull(jsonPointer.tail());
        assertEquals(expected, jsonPointer.toString());
    }

}