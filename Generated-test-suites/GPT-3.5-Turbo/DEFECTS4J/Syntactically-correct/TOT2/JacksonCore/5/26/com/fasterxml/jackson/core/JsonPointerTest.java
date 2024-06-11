package com.fasterxml.jackson.core;

import org.junit.Test;
import static org.junit.Assert.*;

public class JsonPointerTest {

    @Test
    public void testEmptyConstructor() {
        JsonPointer jsonPointer = new JsonPointer();
        assertTrue(jsonPointer.matches());
        assertEquals("", jsonPointer.getMatchingProperty());
        assertEquals(-1, jsonPointer.getMatchingIndex());
    }

    @Test
    public void testNonEmptyConstructor() {
        JsonPointer jsonPointer = new JsonPointer("/test");
        assertFalse(jsonPointer.matches());
        assertEquals("test", jsonPointer.getMatchingProperty());
        assertEquals(-1, jsonPointer.getMatchingIndex());
    }

    @Test
    public void testCompileValidPointer() {
        JsonPointer jsonPointer = JsonPointer.compile("/test");
        assertFalse(jsonPointer.matches());
        assertEquals("test", jsonPointer.getMatchingProperty());
        assertEquals(-1, jsonPointer.getMatchingIndex());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCompileInvalidPointer() {
        JsonPointer.compile("test");
    }

    @Test
    public void testEqualsAndHashCode() {
        JsonPointer jsonPointer1 = new JsonPointer("/test");
        JsonPointer jsonPointer2 = new JsonPointer("/test");
        assertTrue(jsonPointer1.equals(jsonPointer2));
        assertEquals(jsonPointer1.hashCode(), jsonPointer2.hashCode());
    }

    @Test
    public void testMatchProperty() {
        JsonPointer parent = new JsonPointer("/parent");
        JsonPointer child = new JsonPointer("/child");
        parent._nextSegment = child;
        JsonPointer result = parent.matchProperty("child");
        assertEquals(child, result);
    }

    @Test
    public void testMatchElement() {
        JsonPointer parent = new JsonPointer("/parent/1");
        JsonPointer child = new JsonPointer("/1");
        parent._nextSegment = child;
        JsonPointer result = parent.matchElement(1);
        assertEquals(child, result);
    }

    // Add more test cases for other methods

}