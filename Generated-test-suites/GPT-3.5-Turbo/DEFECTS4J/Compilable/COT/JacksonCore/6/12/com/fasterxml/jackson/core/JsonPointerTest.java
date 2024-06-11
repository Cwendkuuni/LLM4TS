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
        assertEquals("", jsonPointer.getMatchingProperty());
    }

    @Test
    public void testGetMatchingIndex() {
        assertEquals(-1, jsonPointer.getMatchingIndex());
    }

    @Test
    public void testMayMatchProperty() {
        assertTrue(jsonPointer.mayMatchProperty());
    }

    @Test
    public void testMayMatchElement() {
        assertFalse(jsonPointer.mayMatchElement());
    }

    @Test
    public void testMatchProperty() {
        assertNull(jsonPointer.matchProperty("test"));
    }

    @Test
    public void testMatchElement() {
        assertNull(jsonPointer.matchElement(0));
    }

    @Test
    public void testTail() {
        assertNull(jsonPointer.tail());
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
        assertTrue(jsonPointer.equals(new JsonPointer()));
        assertFalse(jsonPointer.equals(null));
        assertFalse(jsonPointer.equals(new Object()));
    }
}
