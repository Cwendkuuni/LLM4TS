package com.fasterxml.jackson.core;

import static org.junit.Assert.*;

import org.junit.Test;

public class JsonPointerTest {

    @Test
    public void testCompile() {
        JsonPointer pointer = JsonPointer.compile("");
        assertEquals(JsonPointer.EMPTY, pointer);

        pointer = JsonPointer.compile("/foo");
        assertEquals("/foo", pointer.toString());
        assertFalse(pointer.matches());
        assertTrue(pointer.mayMatchProperty());
        assertFalse(pointer.mayMatchElement());
        assertNull(pointer.getMatchingProperty());
        assertEquals(-1, pointer.getMatchingIndex());

        pointer = JsonPointer.compile("/foo/0/bar");
        assertEquals("/foo/0/bar", pointer.toString());
        assertFalse(pointer.matches());
        assertTrue(pointer.mayMatchProperty());
        assertFalse(pointer.mayMatchElement());
        assertNull(pointer.getMatchingProperty());
        assertEquals(-1, pointer.getMatchingIndex());

        pointer = JsonPointer.compile("/foo/0/bar/1");
        assertEquals("/foo/0/bar/1", pointer.toString());
        assertFalse(pointer.matches());
        assertTrue(pointer.mayMatchProperty());
        assertFalse(pointer.mayMatchElement());
        assertNull(pointer.getMatchingProperty());
        assertEquals(-1, pointer.getMatchingIndex());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCompileInvalidInput() {
        JsonPointer.compile("foo");
    }

    @Test
    public void testMatchProperty() {
        JsonPointer pointer = JsonPointer.compile("/foo");
        assertNull(pointer.matchProperty("bar"));

        pointer = JsonPointer.compile("/foo");
        assertEquals(JsonPointer.EMPTY, pointer.matchProperty("foo"));

        pointer = JsonPointer.compile("/foo/0/bar");
        assertNull(pointer.matchProperty("baz"));

        pointer = JsonPointer.compile("/foo/0/bar");
        assertNull(pointer.matchProperty("bar"));

        pointer = JsonPointer.compile("/foo/0/bar");
        assertEquals(JsonPointer.EMPTY, pointer.matchProperty("0"));
    }

    @Test
    public void testMatchElement() {
        JsonPointer pointer = JsonPointer.compile("/foo/0");
        assertNull(pointer.matchElement(1));

        pointer = JsonPointer.compile("/foo/0");
        assertEquals(JsonPointer.EMPTY, pointer.matchElement(0));

        pointer = JsonPointer.compile("/foo/0/bar/1");
        assertNull(pointer.matchElement(2));

        pointer = JsonPointer.compile("/foo/0/bar/1");
        assertNull(pointer.matchElement(1));

        pointer = JsonPointer.compile("/foo/0/bar/1");
        assertEquals(JsonPointer.EMPTY, pointer.matchElement(0));
    }

    @Test
    public void testTail() {
        JsonPointer pointer = JsonPointer.compile("/foo");
        assertEquals(JsonPointer.EMPTY, pointer.tail());

        pointer = JsonPointer.compile("/foo/0");
        assertEquals("/0", pointer.tail().toString());

        pointer = JsonPointer.compile("/foo/0/bar/1");
        assertEquals("/0/bar/1", pointer.tail().toString());
    }

    @Test
    public void testToString() {
        JsonPointer pointer = JsonPointer.compile("");
        assertEquals("", pointer.toString());

        pointer = JsonPointer.compile("/foo");
        assertEquals("/foo", pointer.toString());

        pointer = JsonPointer.compile("/foo/0/bar");
        assertEquals("/foo/0/bar", pointer.toString());

        pointer = JsonPointer.compile("/foo/0/bar/1");
        assertEquals("/foo/0/bar/1", pointer.toString());
    }

    @Test
    public void testHashCode() {
        JsonPointer pointer1 = JsonPointer.compile("/foo");
        JsonPointer pointer2 = JsonPointer.compile("/foo");
        JsonPointer pointer3 = JsonPointer.compile("/bar");

        assertEquals(pointer1.hashCode(), pointer2.hashCode());
        assertNotEquals(pointer1.hashCode(), pointer3.hashCode());
    }

    @Test
    public void testEquals() {
        JsonPointer pointer1 = JsonPointer.compile("/foo");
        JsonPointer pointer2 = JsonPointer.compile("/foo");
        JsonPointer pointer3 = JsonPointer.compile("/bar");

        assertEquals(pointer1, pointer2);
        assertNotEquals(pointer1, pointer3);
        assertNotEquals(pointer1, null);
        assertNotEquals(pointer1, "foo");
        assertEquals(pointer1, pointer1);
    }

}