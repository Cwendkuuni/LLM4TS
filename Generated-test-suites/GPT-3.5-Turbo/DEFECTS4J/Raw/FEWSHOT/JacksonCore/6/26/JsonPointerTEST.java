package com.fasterxml.jackson.core;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class JsonPointerTest {
    @Test
    public void testMatches() {
        JsonPointer pointer = JsonPointer.EMPTY;
        assertEquals(true, pointer.matches());

        pointer = new JsonPointer("foo", "foo", null);
        assertEquals(false, pointer.matches());
    }

    @Test
    public void testGetMatchingProperty() {
        JsonPointer pointer = JsonPointer.EMPTY;
        assertEquals("", pointer.getMatchingProperty());

        pointer = new JsonPointer("foo", "foo", null);
        assertEquals("foo", pointer.getMatchingProperty());
    }

    @Test
    public void testGetMatchingIndex() {
        JsonPointer pointer = JsonPointer.EMPTY;
        assertEquals(-1, pointer.getMatchingIndex());

        pointer = new JsonPointer("foo", "foo", null);
        assertEquals(-1, pointer.getMatchingIndex());
    }

    @Test
    public void testMayMatchProperty() {
        JsonPointer pointer = JsonPointer.EMPTY;
        assertEquals(true, pointer.mayMatchProperty());

        pointer = new JsonPointer("foo", "foo", null);
        assertEquals(true, pointer.mayMatchProperty());
    }

    @Test
    public void testMayMatchElement() {
        JsonPointer pointer = JsonPointer.EMPTY;
        assertEquals(false, pointer.mayMatchElement());

        pointer = new JsonPointer("foo", "foo", null);
        assertEquals(false, pointer.mayMatchElement());
    }

    @Test
    public void testMatchProperty() {
        JsonPointer pointer = JsonPointer.EMPTY;
        assertNull(pointer.matchProperty("foo"));

        pointer = new JsonPointer("foo", "foo", null);
        assertNull(pointer.matchProperty("bar"));

        pointer = new JsonPointer("foo", "foo", null);
        assertEquals(pointer, pointer.matchProperty("foo"));
    }

    @Test
    public void testMatchElement() {
        JsonPointer pointer = JsonPointer.EMPTY;
        assertNull(pointer.matchElement(0));

        pointer = new JsonPointer("foo", "foo", null);
        assertNull(pointer.matchElement(1));

        pointer = new JsonPointer("foo", "foo", null);
        assertEquals(pointer, pointer.matchElement(-1));
    }

    @Test
    public void testTail() {
        JsonPointer pointer = JsonPointer.EMPTY;
        assertEquals(JsonPointer.EMPTY, pointer.tail());

        pointer = new JsonPointer("foo", "foo", null);
        assertNull(pointer.tail());
    }

    @Test
    public void testCompile() {
        assertEquals(JsonPointer.EMPTY, JsonPointer.compile(null));
        assertEquals(JsonPointer.EMPTY, JsonPointer.compile(""));

        JsonPointer pointer = JsonPointer.compile("/foo");
        assertEquals("foo", pointer.getMatchingProperty());
        assertEquals(-1, pointer.getMatchingIndex());
        assertEquals(JsonPointer.EMPTY, pointer.tail());

        pointer = JsonPointer.compile("/foo/bar");
        assertEquals("foo", pointer.getMatchingProperty());
        assertEquals(-1, pointer.getMatchingIndex());

        JsonPointer tail = pointer.tail();
        assertEquals("bar", tail.getMatchingProperty());
        assertEquals(-1, tail.getMatchingIndex());
        assertEquals(JsonPointer.EMPTY, tail.tail());
    }

    @Test
    public void testValueOf() {
        assertEquals(JsonPointer.compile(null), JsonPointer.valueOf(null));
        assertEquals(JsonPointer.compile(""), JsonPointer.valueOf(""));

        JsonPointer pointer = JsonPointer.valueOf("/foo");
        assertEquals("foo", pointer.getMatchingProperty());
        assertEquals(-1, pointer.getMatchingIndex());
        assertEquals(JsonPointer.EMPTY, pointer.tail());

        pointer = JsonPointer.valueOf("/foo/bar");
        assertEquals("foo", pointer.getMatchingProperty());
        assertEquals(-1, pointer.getMatchingIndex());

        JsonPointer tail = pointer.tail();
        assertEquals("bar", tail.getMatchingProperty());
        assertEquals(-1, tail.getMatchingIndex());
        assertEquals(JsonPointer.EMPTY, tail.tail());
    }
}