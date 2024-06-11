package com.fasterxml.jackson.core;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class JsonPointerTest {

    private JsonPointer jsonPointer;

    @Before
    public void setUp() {
        jsonPointer = new JsonPointer();
    }

    @Test
    public void testMatches() {
        assertTrue(jsonPointer.matches());
    }

    @Test
    public void testGetMatchingProperty() {
        assertNull(jsonPointer.getMatchingProperty());
    }

    @Test
    public void testGetMatchingIndex() {
        assertEquals(-1, jsonPointer.getMatchingIndex());
    }

    @Test
    public void testMayMatchProperty() {
        assertFalse(jsonPointer.mayMatchProperty());
    }

    @Test
    public void testMayMatchElement() {
        assertFalse(jsonPointer.mayMatchElement());
    }

    @Test
    public void testMatchProperty() {
        JsonPointer result = jsonPointer.matchProperty("test");
        assertNull(result);
    }

    @Test
    public void testMatchElement() {
        JsonPointer result = jsonPointer.matchElement(0);
        assertNull(result);
    }

    @Test
    public void testTail() {
        JsonPointer result = jsonPointer.tail();
        assertNull(result);
    }

    @Test
    public void testToString() {
        assertEquals("", jsonPointer.toString());
    }

    @Test
    public void testHashCode() {
        assertEquals(0, jsonPointer.hashCode());
    }

    @Test
    public void testEquals() {
        assertTrue(jsonPointer.equals(null));
        assertTrue(jsonPointer.equals(new JsonPointer()));
        assertFalse(jsonPointer.equals(new Object()));
    }
}