package com.fasterxml.jackson.core;

import static org.junit.Assert.*;
import org.junit.Test;

public class JsonPointerTest {

    @Test
    public void testEmptyJsonPointer() {
        JsonPointer empty = JsonPointer.EMPTY;
        assertEquals("", empty.toString());
        assertTrue(empty.matches());
    }

    @Test
    public void testMatchingProperty() {
        JsonPointer pointer = new JsonPointer("/property");
        assertTrue(pointer.mayMatchProperty());
        assertFalse(pointer.mayMatchElement());
        assertEquals("property", pointer.getMatchingProperty());
    }

    @Test
    public void testMatchingElement() {
        JsonPointer pointer = new JsonPointer("/0");
        assertTrue(pointer.mayMatchElement());
        assertFalse(pointer.mayMatchProperty());
        assertEquals(0, pointer.getMatchingIndex());
    }

    @Test
    public void testCompileValidPointer() {
        JsonPointer pointer = JsonPointer.compile("/test/0");
        assertFalse(pointer.matches());
        assertEquals("test", pointer.getMatchingProperty());
        assertEquals(0, pointer.getMatchingIndex());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCompileInvalidPointer() {
        JsonPointer.compile("test/0");
    }

    @Test
    public void testMatchProperty() {
        JsonPointer pointer = new JsonPointer("/test");
        JsonPointer nextPointer = pointer.matchProperty("test");
        assertNotNull(nextPointer);
        assertEquals("", nextPointer.toString());
    }

    @Test
    public void testMatchElement() {
        JsonPointer pointer = new JsonPointer("/0");
        JsonPointer nextPointer = pointer.matchElement(0);
        assertNotNull(nextPointer);
        assertEquals("", nextPointer.toString());
    }

    @Test
    public void testEquals() {
        JsonPointer ptr1 = new JsonPointer("/test");
        JsonPointer ptr2 = new JsonPointer("/test");
        assertEquals(ptr1, ptr2);
    }
}