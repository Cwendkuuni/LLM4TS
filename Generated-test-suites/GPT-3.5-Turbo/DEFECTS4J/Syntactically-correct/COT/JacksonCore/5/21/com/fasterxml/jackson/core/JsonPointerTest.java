package com.fasterxml.jackson.core;

import org.junit.Test;
import static org.junit.Assert.*;

public class JsonPointerTest {

    @Test
    public void testMatches() {
        JsonPointer jsonPointer = new JsonPointer();
        assertTrue(jsonPointer.matches());
    }

    @Test
    public void testGetMatchingProperty() {
        JsonPointer jsonPointer = new JsonPointer();
        assertNull(jsonPointer.getMatchingProperty());
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
        JsonPointer jsonPointer = new JsonPointer();
        assertNull(jsonPointer.matchProperty("name"));
    }

    @Test
    public void testMatchElement() {
        JsonPointer jsonPointer = new JsonPointer();
        assertNull(jsonPointer.matchElement(1));
    }

    @Test
    public void testTail() {
        JsonPointer jsonPointer = new JsonPointer();
        assertEquals(JsonPointer.EMPTY, jsonPointer.tail());
    }

    @Test
    public void testToString() {
        JsonPointer jsonPointer = new JsonPointer();
        assertEquals("", jsonPointer.toString());
    }

    @Test
    public void testHashCode() {
        JsonPointer jsonPointer = new JsonPointer();
        assertEquals(0, jsonPointer.hashCode());
    }

    @Test
    public void testEquals() {
        JsonPointer jsonPointer1 = new JsonPointer();
        JsonPointer jsonPointer2 = new JsonPointer();
        assertTrue(jsonPointer1.equals(jsonPointer1));
        assertTrue(jsonPointer1.equals(jsonPointer2));
        assertFalse(jsonPointer1.equals(null));
        assertFalse(jsonPointer1.equals(new Object()));
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testCompileNull() {
        JsonPointer.compile(null);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testCompileEmptyInput() {
        JsonPointer.compile("");
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testCompileInvalidInput() {
        JsonPointer.compile("test");
    }

    @Test
    public void testCompileValidInput() {
        JsonPointer jsonPointer = JsonPointer.compile("/test");
        assertEquals("test", jsonPointer.getMatchingProperty());
        assertNull(jsonPointer.tail());
    }

    @Test
    public void testValueOf() {
        JsonPointer jsonPointer = JsonPointer.valueOf("/test");
        assertEquals("test", jsonPointer.getMatchingProperty());
        assertNull(jsonPointer.tail());
    }
    
}