package com.fasterxml.jackson.core;

import org.junit.Test;

import static org.junit.Assert.*;

public class JsonPointerTest {

    @Test
    public void testEmptyJsonPointer() {
        JsonPointer jsonPointer = JsonPointer.EMPTY;
        
        assertTrue(jsonPointer.matches());
        assertEquals("", jsonPointer.toString());
    }

    @Test
    public void testJsonPointerConstruction() {
        JsonPointer jsonPointer = new JsonPointer("/foo/bar", "bar", JsonPointer.EMPTY);
        
        assertFalse(jsonPointer.matches());
        assertEquals("/foo/bar", jsonPointer.toString());
        assertEquals("bar", jsonPointer.getMatchingProperty());
        assertEquals(-1, jsonPointer.getMatchingIndex());
        assertTrue(jsonPointer.mayMatchProperty());
        assertFalse(jsonPointer.mayMatchElement());
    }

    @Test
    public void testJsonPointerCompile() {
        JsonPointer jsonPointer = JsonPointer.compile("/foo/bar");
        
        assertFalse(jsonPointer.matches());
        assertEquals("/foo/bar", jsonPointer.toString());
        assertEquals("bar", jsonPointer.getMatchingProperty());
        assertEquals(-1, jsonPointer.getMatchingIndex());
        assertTrue(jsonPointer.mayMatchProperty());
        assertFalse(jsonPointer.mayMatchElement());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidJsonPointerCompile() {
        JsonPointer.compile("foo/bar");
    }

    @Test
    public void testJsonPointerEquality() {
        JsonPointer jsonPointer1 = JsonPointer.compile("/foo");
        JsonPointer jsonPointer2 = new JsonPointer("/foo", "foo", JsonPointer.EMPTY);
        
        assertEquals(jsonPointer1, jsonPointer2);
        assertEquals(jsonPointer1.hashCode(), jsonPointer2.hashCode());
    }

    @Test
    public void testJsonPointerIndexParsing() {
        assertEquals(42, JsonPointer._parseIndex("42"));
        assertEquals(-1, JsonPointer._parseIndex("abc"));
        assertEquals(-1, JsonPointer._parseIndex("01234567890"));
        assertEquals(-1, JsonPointer._parseIndex(""));
    }

    @Test
    public void testJsonPointerQuotedTail() {
        JsonPointer jsonPointer = new JsonPointer("/foo%2Fbar", "foo/bar", JsonPointer.EMPTY);
        
        assertFalse(jsonPointer.matches());
        assertEquals("/foo~1bar", jsonPointer.toString());
        assertEquals("foo/bar", jsonPointer.getMatchingProperty());
        assertEquals(-1, jsonPointer.getMatchingIndex());
        assertTrue(jsonPointer.mayMatchProperty());
        assertFalse(jsonPointer.mayMatchElement());
    }

}