package com.fasterxml.jackson.core;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class JsonPointerTest {

    @Test
    public void testEmptyPointer() {
        JsonPointer empty = JsonPointer.EMPTY;
        assertEquals("", empty.toString());
        assertTrue(empty.matches());
        assertNull(empty.getMatchingProperty());
        assertNull(empty.getMatchingIndex());
        assertNull(empty.tail());
    }

    @Test
    public void testSimplePointer() {
        JsonPointer pointer = JsonPointer.compile("/foo");
        assertEquals("/foo", pointer.toString());
        assertTrue(pointer.matches());
        assertNull(pointer.getMatchingProperty());
        assertNull(pointer.getMatchingIndex());
        assertNull(pointer.tail());
    }

    @Test
    public void testPropertyMatchingPointer() {
        JsonPointer pointer = JsonPointer.compile("/foo/bar");
        assertEquals("/foo/bar", pointer.toString());
        assertFalse(pointer.matches());
        assertEquals("bar", pointer.getMatchingProperty());
        assertNull(pointer.getMatchingIndex());
        assertEquals(JsonPointer.EMPTY, pointer.tail());
    }

    @Test
    public void testElementMatchingPointer() {
        JsonPointer pointer = JsonPointer.compile("/foo/0");
        assertEquals("/foo/0", pointer.toString());
        assertFalse(pointer.matches());
        assertNull(pointer.getMatchingProperty());
        assertEquals(0, pointer.getMatchingIndex());
        assertNull(pointer.tail());
    }

    @Test
    public void testComplexPointer() {
        JsonPointer pointer = JsonPointer.compile("/foo/bar/0");
        assertEquals("/foo/bar/0", pointer.toString());
        assertFalse(pointer.matches());
        assertEquals("bar", pointer.getMatchingProperty());
        assertEquals(0, pointer.getMatchingIndex());
        assertNotNull(pointer.tail());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidPointer() {
        JsonPointer.compile("~/foo");
    }

    @Test
    public void testMatchesProperty() {
        JsonPointer pointer = JsonPointer.compile("/foo/bar");
        JsonPointer matching = new JsonPointer("/foo", "bar", JsonPointer.EMPTY);
        assertTrue(pointer.matchesProperty(matching.getMatchingProperty()));
        assertFalse(pointer.matchesProperty("baz"));
    }

    @Test
    public void testMatchesElement() {
        JsonPointer pointer = JsonPointer.compile("/foo/0");
        JsonPointer matching = new JsonPointer("/foo", null, JsonPointer.EMPTY);
        JsonPointer matchingElement = new JsonPointer("/foo", null, JsonPointer.compile("/0"));
        assertTrue(pointer.matchesElement(matchingElement.getMatchingIndex()));
        assertFalse(pointer.matchesElement(1));
    }

    @Test
    public void testTail() {
        JsonPointer pointer = JsonPointer.compile("/foo/bar/0");
        JsonPointer tail = pointer.tail();
        assertEquals("/bar/0", tail.toString());
    }
}