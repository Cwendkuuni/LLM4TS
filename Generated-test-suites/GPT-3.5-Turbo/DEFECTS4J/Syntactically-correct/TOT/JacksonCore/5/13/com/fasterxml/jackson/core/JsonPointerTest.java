package com.fasterxml.jackson.core;

import org.junit.Test;

import static org.junit.Assert.*;

public class JsonPointerTest {

    @Test
    public void testEmptyJsonPointerMatches() {
        JsonPointer jsonPointer = JsonPointer.EMPTY;

        assertTrue(jsonPointer.matches());
    }

    @Test
    public void testEmptyJsonPointerGetMatchingProperty() {
        JsonPointer jsonPointer = JsonPointer.EMPTY;

        assertNull(jsonPointer.getMatchingProperty());
    }

    @Test
    public void testEmptyJsonPointerGetMatchingIndex() {
        JsonPointer jsonPointer = JsonPointer.EMPTY;

        assertEquals(-1, jsonPointer.getMatchingIndex());
    }

    @Test
    public void testEmptyJsonPointerMayMatchProperty() {
        JsonPointer jsonPointer = JsonPointer.EMPTY;

        assertFalse(jsonPointer.mayMatchProperty());
    }

    @Test
    public void testEmptyJsonPointerMayMatchElement() {
        JsonPointer jsonPointer = JsonPointer.EMPTY;

        assertFalse(jsonPointer.mayMatchElement());
    }

    @Test
    public void testEmptyJsonPointerMatchProperty() {
        JsonPointer jsonPointer = JsonPointer.EMPTY;

        assertNull(jsonPointer.matchProperty("property"));
    }

    @Test
    public void testEmptyJsonPointerMatchElement() {
        JsonPointer jsonPointer = JsonPointer.EMPTY;

        assertNull(jsonPointer.matchElement(1));
    }

    @Test
    public void testEmptyJsonPointerTail() {
        JsonPointer jsonPointer = JsonPointer.EMPTY;

        assertNull(jsonPointer.tail());
    }

    @Test
    public void testCompileWithEmptyString() {
        JsonPointer jsonPointer = JsonPointer.compile("");
        
        assertTrue(jsonPointer.matches());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCompileWithInvalidInput() {
        JsonPointer jsonPointer = JsonPointer.compile("invalidInput");
    }

    @Test
    public void testCompileWithMatchingProperty() {
        JsonPointer jsonPointer = JsonPointer.compile("/property");
        
        assertFalse(jsonPointer.matches());
        assertEquals("property", jsonPointer.getMatchingProperty());
    }

    @Test
    public void testCompileWithMatchingIndex() {
        JsonPointer jsonPointer = JsonPointer.compile("/1");
        
        assertFalse(jsonPointer.matches());
        assertEquals(1, jsonPointer.getMatchingIndex());
    }

    @Test
    public void testCompileWithMultipleSegments() {
        JsonPointer jsonPointer = JsonPointer.compile("/property1/2/property3");

        assertFalse(jsonPointer.matches());
        assertEquals("property1", jsonPointer.getMatchingProperty());
        assertEquals(2, jsonPointer.getMatchingIndex());
        assertFalse(jsonPointer.tail().matches());
        assertEquals("property3", jsonPointer.tail().getMatchingProperty());
    }

    @Test
    public void testEquals() {
        JsonPointer jsonPointer1 = JsonPointer.compile("/property");
        JsonPointer jsonPointer2 = JsonPointer.compile("/property");
        JsonPointer jsonPointer3 = JsonPointer.compile("/property1");

        assertTrue(jsonPointer1.equals(jsonPointer2));
        assertFalse(jsonPointer1.equals(jsonPointer3));
    }

    @Test
    public void testToString() {
        JsonPointer jsonPointer = JsonPointer.compile("/property");

        assertEquals("/property", jsonPointer.toString());
    }

}