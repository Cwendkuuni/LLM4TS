package com.fasterxml.jackson.core.test;

import static org.junit.Assert.*;

import com.fasterxml.jackson.core.JsonPointer;

import org.junit.Test;

public class JsonPointerTest {

    @Test
    public void testEmptyPointer() {
        JsonPointer emptyPointer = JsonPointer.EMPTY;
        assertTrue(emptyPointer.matches());
        assertNull(emptyPointer.tail());
        assertNull(emptyPointer.getMatchingProperty());
        assertFalse(emptyPointer.mayMatchProperty());
        assertFalse(emptyPointer.mayMatchElement());
    }

    @Test
    public void testBasicPointerConstruction() {
        String fullString = "/foo/bar/baz";
        JsonPointer pointer = new JsonPointer(fullString, "foo", null);
        assertEquals("/foo", pointer.toString());
        assertFalse(pointer.matches());
        assertEquals("foo", pointer.getMatchingProperty());
        assertTrue(pointer.mayMatchProperty());
        assertFalse(pointer.mayMatchElement());
        assertEquals(JsonPointer.EMPTY, pointer.tail());
    }

    @Test
    public void testFactoryMethodConstruction() {
        JsonPointer pointer = JsonPointer.compile("/foo/bar/baz");
        assertEquals("/foo/bar/baz", pointer.toString());
        assertFalse(pointer.matches());
        assertEquals("bar", pointer.getMatchingProperty());
        assertTrue(pointer.mayMatchProperty());
        assertFalse(pointer.mayMatchElement());
        assertEquals(JsonPointer.EMPTY, pointer.tail());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidInputForCompile() {
        JsonPointer.compile("invalid");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidInputForValueOf() {
        JsonPointer.valueOf("invalid");
    }

    @Test
    public void testMatchingProperties() {
        JsonPointer pointer = JsonPointer.compile("/foo/bar/baz");
        assertEquals("/foo", pointer.matchProperty("foo").toString());
        assertEquals("/bar", pointer.matchProperty("foo/bar").toString());
        assertEquals("/baz", pointer.matchProperty("foo/bar/baz").toString());
        assertNull(pointer.matchProperty("baz/qux"));
        assertNull(pointer.matchProperty(""));
    }

    @Test
    public void testMatchingElements() {
        JsonPointer pointer = JsonPointer.compile("[1,2,3]/1");
        assertEquals("[1]", pointer.matchElement(0).toString());
        assertNull(pointer.matchElement(1));
        assertNull(pointer.matchElement(-1));
        assertNull(pointer.matchElement(2));
    }

    @Test
    public void testTailMethod() {
        JsonPointer pointer = JsonPointer.compile("/foo/bar/baz");
        assertEquals("/bar/baz", pointer.tail().toString());
        assertEquals("/bar/baz", pointer.tail().tail().toString());
        assertNull(JsonPointer.EMPTY.tail());
    }

    @Test
    public void testToStringMethod() {
        JsonPointer pointer = JsonPointer.compile("/foo/bar/baz");
        assertEquals("/foo/bar/baz", pointer.toString());
    }

    @Test
    public void testHashCodeAndEquals() {
        JsonPointer pointer1 = JsonPointer.compile("/foo/bar/baz");
        JsonPointer pointer2 = JsonPointer.compile("/foo/bar/baz");
        JsonPointer pointer3 = JsonPointer.compile("/foo/qux/baz");
        JsonPointer pointer4 = JsonPointer.EMPTY;

        assertEquals(pointer1.hashCode(), pointer2.hashCode());
        assertTrue(pointer1.equals(pointer2));
        assertFalse(pointer1.equals(pointer3));
        assertFalse(pointer1.equals(pointer4));
    }
}