package com.fasterxml.jackson.core;
import static org.junit.Assert.*;
import org.junit.Test;

public class JsonPointerTest {

    @Test
    public void testMatches() {
        JsonPointer jsonPointer = new JsonPointer();
        assertTrue(jsonPointer.matches());
    }

    @Test
    public void testGetMatchingProperty() {
        JsonPointer jsonPointer = new JsonPointer("testString", "testSegment", null);
        assertEquals("testSegment", jsonPointer.getMatchingProperty());
    }

    @Test
    public void testGetMatchingIndex() {
        JsonPointer jsonPointer = new JsonPointer("testString", "testSegment", null);
        assertEquals(-1, jsonPointer.getMatchingIndex());
    }

    @Test
    public void testMayMatchProperty() {
        JsonPointer jsonPointer = new JsonPointer("testString", "testSegment", null);
        assertTrue(jsonPointer.mayMatchProperty());
    }

    @Test
    public void testMayMatchElement() {
        JsonPointer jsonPointer = new JsonPointer("testString", "testSegment", null);
        assertFalse(jsonPointer.mayMatchElement());
    }

    @Test
    public void testMatchProperty() {
        JsonPointer jsonPointer = new JsonPointer("testString", "testSegment", null);
        assertNull(jsonPointer.matchProperty("differentSegment"));
    }

    @Test
    public void testMatchElement() {
        JsonPointer jsonPointer = new JsonPointer("testString", "testSegment", null);
        assertNull(jsonPointer.matchElement(5));
    }

    @Test
    public void testTail() {
        JsonPointer jsonPointer = new JsonPointer("testString", "testSegment", new JsonPointer());
        assertNotNull(jsonPointer.tail());
    }

    @Test
    public void testEquals() {
        JsonPointer jsonPointer1 = new JsonPointer("testString1", "testSegment", null);
        JsonPointer jsonPointer2 = new JsonPointer("testString2", "testSegment", null);
        assertFalse(jsonPointer1.equals(jsonPointer2));
    }

    @Test
    public void testToString() {
        JsonPointer jsonPointer = new JsonPointer("testString", "testSegment", null);
        assertEquals("testString", jsonPointer.toString());
    }

    @Test
    public void testCompileValidInput() {
        JsonPointer jsonPointer = JsonPointer.compile("/test/path");
        assertNotNull(jsonPointer);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCompileInvalidInput() {
        JsonPointer.compile("test/path");
    }

    @Test
    public void testValueOf() {
        JsonPointer jsonPointer = JsonPointer.valueOf("/test/value");
        assertNotNull(jsonPointer);
    }
}