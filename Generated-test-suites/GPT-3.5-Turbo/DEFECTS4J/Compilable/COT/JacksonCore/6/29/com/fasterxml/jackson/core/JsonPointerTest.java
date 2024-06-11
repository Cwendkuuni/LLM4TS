package com.fasterxml.jackson.core;

import com.fasterxml.jackson.core.JsonPointer;
import org.junit.Test;
import static org.junit.Assert.*;

public class JsonPointerTest {

    @Test
    public void testMatches() {
        // Create a JsonPointer instance
        JsonPointer jsonPointer = new JsonPointer();

        // Test case: check if matches() returns true for an empty JsonPointer
        assertTrue(jsonPointer.matches());
    }

    @Test
    public void testGetMatchingProperty() {
        // Create a JsonPointer instance
        JsonPointer jsonPointer = new JsonPointer();

        // Test case: check if getMatchingProperty() returns an empty string
        assertEquals("", jsonPointer.getMatchingProperty());
    }

    @Test
    public void testGetMatchingIndex() {
        // Create a JsonPointer instance
        JsonPointer jsonPointer = new JsonPointer();

        // Test case: check if getMatchingIndex() returns -1
        assertEquals(-1, jsonPointer.getMatchingIndex());
    }

    @Test
    public void testMayMatchProperty() {
        // Create a JsonPointer instance
        JsonPointer jsonPointer = new JsonPointer();

        // Test case: check if mayMatchProperty() returns true for an empty JsonPointer
        assertTrue(jsonPointer.mayMatchProperty());
    }

    @Test
    public void testMayMatchElement() {
        // Create a JsonPointer instance
        JsonPointer jsonPointer = new JsonPointer();

        // Test case: check if mayMatchElement() returns false for an empty JsonPointer
        assertFalse(jsonPointer.mayMatchElement());
    }

    @Test
    public void testMatchProperty() {
        // Create a JsonPointer instance
        JsonPointer jsonPointer = new JsonPointer();

        // Test case: check if matchProperty() returns null for an empty JsonPointer
        assertNull(jsonPointer.matchProperty("name"));
    }

    @Test
    public void testMatchElement() {
        // Create a JsonPointer instance
        JsonPointer jsonPointer = new JsonPointer();

        // Test case: check if matchElement() returns null for an empty JsonPointer
        assertNull(jsonPointer.matchElement(1));
    }

    @Test
    public void testTail() {
        // Create a JsonPointer instance
        JsonPointer jsonPointer = new JsonPointer();

        // Test case: check if tail() returns null for an empty JsonPointer
        assertNull(jsonPointer.tail());
    }

}
