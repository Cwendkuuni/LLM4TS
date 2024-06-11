package com.fasterxml.jackson.core;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import com.fasterxml.jackson.core.JsonPointer;

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
    public void testJsonPointerWithSingleSegment() {
        String jsonPointerString = "/foo";
        JsonPointer jsonPointer = JsonPointer.compile(jsonPointerString);

        assertEquals(jsonPointerString, jsonPointer.toString());
        assertTrue(jsonPointer.matches());
        assertEquals("foo", jsonPointer.getMatchingProperty());
        assertFalse(jsonPointer.mayMatchElement());
        assertFalse(jsonPointer.mayMatchProperty());
    }

    @Test
    public void testJsonPointerWithMultipleSegmentsAndProperties() {
        String jsonPointerString = "/foo/bar/baz";
        JsonPointer jsonPointer = JsonPointer.compile(jsonPointerString);

        assertEquals(jsonPointerString, jsonPointer.toString());
        assertTrue(jsonPointer.matches());
        assertEquals("bar", jsonPointer.getMatchingProperty());
        assertFalse(jsonPointer.mayMatchElement());
        assertTrue(jsonPointer.mayMatchProperty());

        JsonPointer propertyJsonPointer = jsonPointer.matchProperty("bar");
        assertEquals("/foo/bar/baz", propertyJsonPointer.toString());
        assertTrue(propertyJsonPointer.matches());
        assertEquals("baz", propertyJsonPointer.getMatchingProperty());
        assertFalse(propertyJsonPointer.mayMatchElement());
        assertTrue(propertyJsonPointer.mayMatchProperty());
    }

    @Test
    public void testJsonPointerWithMultipleSegmentsAndElements() {
        String jsonPointerString = "/foo/0/bar/1";
        JsonPointer jsonPointer = JsonPointer.compile(jsonPointerString);

        assertEquals(jsonPointerString, jsonPointer.toString());
        assertTrue(jsonPointer.matches());
        assertEquals("bar", jsonPointer.getMatchingProperty());
        assertTrue(jsonPointer.getMatchingIndex() == 1);
        assertFalse(jsonPointer.mayMatchProperty());
        assertTrue(jsonPointer.mayMatchElement());

        JsonPointer elementJsonPointer = jsonPointer.matchElement(1);
        assertEquals("/foo/0/bar", elementJsonPointer.toString());
        assertTrue(elementJsonPointer.matches());
        assertEquals(null, elementJsonPointer.getMatchingProperty());
        assertFalse(elementJsonPointer.mayMatchProperty());
        assertTrue(elementJsonPointer.mayMatchElement());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidJsonPointer() {
        String invalidJsonPointerString = "~invalidJsonPointer";
        JsonPointer.compile(invalidJsonPointerString);
    }

    @Test
    public void testJsonPointerWithTildeEscapes() {
        String jsonPointerStringWithTildeEscapes = "/foo/~0/bar/~1";
        JsonPointer jsonPointer = JsonPointer.compile(jsonPointerStringWithTildeEscapes);

        assertEquals(jsonPointerStringWithTildeEscapes, jsonPointer.toString());
        assertTrue(jsonPointer.matches());
        assertEquals("bar", jsonPointer.getMatchingProperty());
        assertTrue(jsonPointer.getMatchingIndex() == 0);
        assertFalse(jsonPointer.mayMatchProperty());
        assertTrue(jsonPointer.mayMatchElement());

        JsonPointer elementJsonPointer = jsonPointer.matchElement(0);
        assertEquals("/foo/~0/bar", elementJsonPointer.toString());
        assertTrue(elementJsonPointer.matches());
        assertEquals(null, elementJsonPointer.getMatchingProperty());
        assertFalse(elementJsonPointer.mayMatchProperty());
        assertTrue(elementJsonPointer.mayMatchElement());
    }
}