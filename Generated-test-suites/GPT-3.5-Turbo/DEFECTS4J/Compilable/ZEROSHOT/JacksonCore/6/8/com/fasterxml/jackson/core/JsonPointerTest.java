package com.fasterxml.jackson.core;
import static org.junit.Assert.*;
import org.junit.*;

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
        assertNull(jsonPointer.matchElement(1));
        assertNull(jsonPointer.tail());
    }

    @Test
    public void testNonEmptyJsonPointer() {
        JsonPointer jsonPointer = new JsonPointer("fullString", "segment", null);
        assertFalse(jsonPointer.matches());
        assertEquals("segment", jsonPointer.getMatchingProperty());
        assertEquals(-1, jsonPointer.getMatchingIndex());
        assertTrue(jsonPointer.mayMatchProperty());
        assertFalse(jsonPointer.mayMatchElement());
        assertNull(jsonPointer.matchProperty("name"));
        assertNull(jsonPointer.matchElement(1));
        assertNull(jsonPointer.tail());
    }

    @Test
    public void testCompileValidInput() {
        JsonPointer jsonPointer = JsonPointer.compile("/foo/bar");
        assertFalse(jsonPointer.matches());
        assertEquals("foo", jsonPointer.getMatchingProperty());
        assertEquals(-1, jsonPointer.getMatchingIndex());
        assertTrue(jsonPointer.mayMatchProperty());
        assertFalse(jsonPointer.mayMatchElement());
        assertNotNull(jsonPointer.matchProperty("foo"));
        assertNull(jsonPointer.matchProperty("bar"));
        assertNull(jsonPointer.matchElement(1));
        assertNotNull(jsonPointer.tail());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCompileInvalidInput() {
        JsonPointer.compile("foo/bar");
    }

    @Test
    public void testValueOf() {
        JsonPointer jsonPointer = JsonPointer.valueOf("/foo");
        assertFalse(jsonPointer.matches());
        assertEquals("foo", jsonPointer.getMatchingProperty());
        assertEquals(-1, jsonPointer.getMatchingIndex());
        assertTrue(jsonPointer.mayMatchProperty());
        assertFalse(jsonPointer.mayMatchElement());
        assertNotNull(jsonPointer.matchProperty("foo"));
        assertNull(jsonPointer.matchProperty("bar"));
        assertNull(jsonPointer.matchElement(1));
        assertNotNull(jsonPointer.tail());
    }

    @Test
    public void testToString() {
        JsonPointer jsonPointer = new JsonPointer("fullString", "segment", null);
        assertEquals("fullString", jsonPointer.toString());
    }

    @Test
    public void testHashCode() {
        JsonPointer jsonPointer1 = new JsonPointer("fullString", "segment", null);
        JsonPointer jsonPointer2 = new JsonPointer("fullString", "segment", null);
        assertEquals(jsonPointer1.hashCode(), jsonPointer2.hashCode());
    }

    @Test
    public void testEquals() {
        JsonPointer jsonPointer1 = new JsonPointer("fullString", "segment", null);
        JsonPointer jsonPointer2 = new JsonPointer("fullString", "segment", null);
        JsonPointer jsonPointer3 = new JsonPointer("differentString", "segment", null);
        assertTrue(jsonPointer1.equals(jsonPointer2));
        assertFalse(jsonPointer1.equals(jsonPointer3));
    }

}