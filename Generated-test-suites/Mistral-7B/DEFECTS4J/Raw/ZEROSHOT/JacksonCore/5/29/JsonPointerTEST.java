package com.fasterxml.jackson.core.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import com.fasterxml.jackson.core.JsonPointer;

import org.junit.Test;

/**
 * JUnit 4 test file for {@link JsonPointer} class
 */
public class JsonPointerTest {

    /**
     * Test construction of empty JsonPointer instance
     */
    @Test
    public void testEmptyJsonPointerConstruction() {
        JsonPointer emptyJsonPointer = JsonPointer.EMPTY;
        assertEquals("", emptyJsonPointer.toString());
        assertEquals(emptyJsonPointer, emptyJsonPointer.tail());
        assertTrue(emptyJsonPointer.matches());
    }

    /**
     * Test creation of a new JsonPointer instance
     */
    @Test
    public void testJsonPointerConstruction() {
        String fullString = "/address/street/0/name";
        String segment = "address/street";
        JsonPointer nextJsonPointer = new JsonPointer("/0/name", "/name", JsonPointer.EMPTY);

        JsonPointer jsonPointer = new JsonPointer(fullString, segment, nextJsonPointer);
        assertEquals(fullString, jsonPointer.toString());
        assertEquals(nextJsonPointer, jsonPointer.tail());
        assertFalse(jsonPointer.matches());
        assertEquals("address/street", jsonPointer.getMatchingProperty());
        assertEquals(0, jsonPointer.getMatchingIndex());

        assertEquals("/address/street/0/name", jsonPointer.toString());
        assertEquals("/name", jsonPointer.tail().toString());
    }

    /**
     * Test creation of JsonPointer instance from String input
     */
    @Test
    public void testJsonPointerFromStringConstruction() {
        String jsonPointerString = "/address/street/0/name";
        JsonPointer jsonPointer = JsonPointer.compile(jsonPointerString);

        assertEquals(jsonPointerString, jsonPointer.toString());
        assertEquals("/name", jsonPointer.tail().toString());
        assertFalse(jsonPointer.matches());
        assertEquals("address/street", jsonPointer.getMatchingProperty());
        assertEquals(0, jsonPointer.getMatchingIndex());
    }

    /**
     * Test creation of JsonPointer instance from an array of raw segments
     */
    @Test
    public void testJsonPointerFromSegmentsConstruction() {
        // TODO: Implement this test case when the 'fromSegment' method is available
    }

    /**
     * Test JsonPointer's public API
     */
    @Test
    public void testJsonPointerPublicApi() {
        String jsonPointerString = "/address/street/0/name";
        JsonPointer jsonPointer = JsonPointer.compile(jsonPointerString);

        assertEquals("address/street", jsonPointer.getMatchingProperty());
        assertEquals(0, jsonPointer.getMatchingIndex());
        assertTrue(jsonPointer.mayMatchProperty());
        assertTrue(jsonPointer.mayMatchElement());

        JsonPointer propertyJsonPointer = jsonPointer.matchProperty("address/street");
        assertEquals("/0/name", propertyJsonPointer.toString());
        JsonPointer nameJsonPointer = propertyJsonPointer.matchElement(0);
        assertEquals("/name", nameJsonPointer.toString());
        assertEquals(nameJsonPointer, jsonPointer.tail());

        assertNull(jsonPointer.matchProperty("invalidProperty"));
        assertNull(jsonPointer.matchElement(-1));
    }
}