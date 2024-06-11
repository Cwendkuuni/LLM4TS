package com.fasterxml.jackson.core;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class JsonPointerTest {
    
    @Test
    public void testMatches() {
        JsonPointer jsonPointer = new JsonPointer();
        assertTrue(jsonPointer.matches());
    }

    @Test
    public void testGetMatchingProperty() {
        JsonPointer jsonPointer = new JsonPointer();
        assertEquals("", jsonPointer.getMatchingProperty());
    }

    @Test
    public void testGetMatchingIndex() {
        JsonPointer jsonPointer = new JsonPointer();
        assertEquals(-1, jsonPointer.getMatchingIndex());
    }

    @Test
    public void testMayMatchProperty() {
        JsonPointer jsonPointer = new JsonPointer();
        assertTrue(jsonPointer.mayMatchProperty());
    }

    @Test
    public void testMayMatchElement() {
        JsonPointer jsonPointer = new JsonPointer();
        assertFalse(jsonPointer.mayMatchElement());
    }

    @Test
    public void testMatchProperty() {
        JsonPointer jsonPointer = new JsonPointer("property", "segment", null);
        assertNull(jsonPointer.matchProperty("name"));
        assertNull(jsonPointer.matchProperty(null));
    }

    @Test
    public void testMatchElement() {
        JsonPointer jsonPointer = new JsonPointer("element", "segment", null);
        assertNull(jsonPointer.matchElement(1));
        assertNull(jsonPointer.matchElement(-1));
    }

    @Test
    public void testTail() {
        JsonPointer jsonPointer = new JsonPointer("tail", "segment", null);
        assertNull(jsonPointer.tail());
    }

    @Test
    public void testToString() {
        JsonPointer jsonPointer = new JsonPointer();
        assertEquals("", jsonPointer.toString());
    }

    @Test
    public void testHashCode() {
        JsonPointer jsonPointer = new JsonPointer();
        assertEquals(jsonPointer.toString().hashCode(), jsonPointer.hashCode());
    }

    @Test
    public void testEquals() {
        JsonPointer jsonPointer = new JsonPointer();
        assertTrue(jsonPointer.equals(jsonPointer));
        assertFalse(jsonPointer.equals(null));
        assertFalse(jsonPointer.equals(""));
    }

    @Test
    public void testCompileWithNull() {
        JsonPointer jsonPointer = JsonPointer.compile(null);
        assertTrue(jsonPointer.matches());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCompileWithInvalidInput() {
        JsonPointer jsonPointer = JsonPointer.compile("invalid");
    }

    @Test
    public void testValueOf() {
        JsonPointer jsonPointer = JsonPointer.valueOf("");
        assertTrue(jsonPointer.matches());
    }
}