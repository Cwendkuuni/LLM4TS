package com.fasterxml.jackson.core;

import static org.junit.Assert.*;
import org.junit.Test;

public class JsonPointerTest {

    @Test
    public void testConstructor() {
        JsonPointer pointer = new JsonPointer();
        assertNotNull(pointer);
    }

    @Test
    public void testConstructorWithParameters() {
        JsonPointer pointer = new JsonPointer("fullString", "segment", null);
        assertNotNull(pointer);
    }

    @Test
    public void testCompile() {
        JsonPointer pointer = JsonPointer.compile("input");
        assertNotNull(pointer);
    }

    @Test
    public void testValueOf() {
        JsonPointer pointer = JsonPointer.valueOf("input");
        assertNotNull(pointer);
    }

    @Test
    public void testMatches() {
        JsonPointer pointer = new JsonPointer();
        boolean matches = pointer.matches();
        assertTrue(matches);
    }

    @Test
    public void testGetMatchingProperty() {
        JsonPointer pointer = new JsonPointer();
        String matchingProperty = pointer.getMatchingProperty();
        assertNull(matchingProperty);
    }

    @Test
    public void testGetMatchingIndex() {
        JsonPointer pointer = new JsonPointer();
        int matchingIndex = pointer.getMatchingIndex();
        assertEquals(-1, matchingIndex);
    }

    @Test
    public void testMayMatchProperty() {
        JsonPointer pointer = new JsonPointer();
        boolean mayMatchProperty = pointer.mayMatchProperty();
        assertTrue(mayMatchProperty);
    }

    @Test
    public void testMayMatchElement() {
        JsonPointer pointer = new JsonPointer();
        boolean mayMatchElement = pointer.mayMatchElement();
        assertFalse(mayMatchElement);
    }

    @Test
    public void testMatchProperty() {
        JsonPointer pointer = new JsonPointer();
        JsonPointer matchProperty = pointer.matchProperty("name");
        assertNull(matchProperty);
    }

    @Test
    public void testMatchElement() {
        JsonPointer pointer = new JsonPointer();
        JsonPointer matchElement = pointer.matchElement(0);
        assertNull(matchElement);
    }

    @Test
    public void testTail() {
        JsonPointer pointer = new JsonPointer();
        JsonPointer tail = pointer.tail();
        assertNull(tail);
    }

    @Test
    public void testToString() {
        JsonPointer pointer = new JsonPointer();
        String string = pointer.toString();
        assertEquals("", string);
    }

    @Test
    public void testHashCode() {
        JsonPointer pointer = new JsonPointer();
        int hashCode = pointer.hashCode();
        assertNotNull(hashCode);
    }

    @Test
    public void testEquals() {
        JsonPointer pointer = new JsonPointer();
        boolean equals = pointer.equals(pointer);
        assertTrue(equals);
    }

}