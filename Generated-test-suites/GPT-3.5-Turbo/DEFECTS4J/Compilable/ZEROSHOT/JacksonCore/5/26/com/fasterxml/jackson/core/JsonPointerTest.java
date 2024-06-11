package com.fasterxml.jackson.core;

import org.junit.Test;
import static org.junit.Assert.*;

public class JsonPointerTest {
    
    @Test
    public void testEmptyJsonPointer() {
        JsonPointer jsonPointer = new JsonPointer();
        assertTrue(jsonPointer.matches());
        assertEquals("", jsonPointer.getMatchingProperty());
        assertEquals(-1, jsonPointer.getMatchingIndex());
        assertFalse(jsonPointer.mayMatchProperty());
        assertFalse(jsonPointer.mayMatchElement());
        assertNull(jsonPointer.matchProperty("name"));
        assertNull(jsonPointer.matchElement(0));
        assertNull(jsonPointer.tail());
        assertEquals("", jsonPointer.toString());
        assertTrue(jsonPointer.equals(jsonPointer));
        assertFalse(jsonPointer.equals(null));
        assertFalse(jsonPointer.equals(new Object()));
        assertEquals(jsonPointer.hashCode(), jsonPointer.hashCode());
    }
    
    @Test
    public void testNonEmptyJsonPointer() {
        String input = "/test/test2";
        JsonPointer jsonPointer = JsonPointer.compile(input);

        assertFalse(jsonPointer.matches());
        assertEquals("test", jsonPointer.getMatchingProperty());
        assertEquals(-1, jsonPointer.getMatchingIndex());
        assertTrue(jsonPointer.mayMatchProperty());
        assertFalse(jsonPointer.mayMatchElement());
        assertNull(jsonPointer.matchProperty("name"));
        assertNull(jsonPointer.matchElement(0));
        assertNotNull(jsonPointer.tail());
        assertEquals("/test2", jsonPointer.tail().toString());
        assertEquals("/test/test2", jsonPointer.toString());
        assertTrue(jsonPointer.equals(jsonPointer));
        assertFalse(jsonPointer.equals(null));
        assertFalse(jsonPointer.equals(new Object()));
        assertEquals(jsonPointer.hashCode(), jsonPointer.hashCode());
    }
}