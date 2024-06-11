package com.fasterxml.jackson.core;

import static org.junit.Assert.*;

import org.junit.Test;

public class JsonPointerTest {

    @Test
    public void testEmptyPointer() {
        JsonPointer empty = JsonPointer.EMPTY;
        assertTrue(empty.matches());
        assertNull(empty.getMatchingProperty());
        assertFalse(empty.mayMatchProperty());
        assertFalse(empty.mayMatchElement());
        assertNull(empty.tail());
        assertEquals("", empty.toString());
    }

    @Test
    public void testSimplePointer() {
        String input = "/foo";
        JsonPointer pointer = JsonPointer.compile(input);
        assertTrue(pointer.matches());
        assertEquals("foo", pointer.getMatchingProperty());
        assertFalse(pointer.mayMatchProperty());
        assertFalse(pointer.mayMatchElement());
        assertNull(pointer.tail());
        assertEquals("/foo", pointer.toString());
    }

    @Test
    public void testPointerWithIndex() {
        String input = "/foo/0";
        JsonPointer pointer = JsonPointer.compile(input);
        assertFalse(pointer.matches());
        assertEquals("foo", pointer.getMatchingProperty());
        assertTrue(pointer.mayMatchElement());
        assertFalse(pointer.mayMatchProperty());
        assertEquals(0, pointer.getMatchingIndex());
        assertNull(pointer.tail());
        assertEquals("/foo/0", pointer.toString());
    }

    @Test
    public void testPointerWithProperty() {
        String input = "/foo/bar";
        JsonPointer pointer = JsonPointer.compile(input);
        assertFalse(pointer.matches());
        assertEquals("bar", pointer.getMatchingProperty());
        assertTrue(pointer.mayMatchProperty());
        assertFalse(pointer.mayMatchElement());
        assertNull(pointer.tail());
        assertEquals("/foo/bar", pointer.toString());
    }

    @Test
    public void testPointerWithMultipleSegments() {
        String input = "/foo/bar/baz";
        JsonPointer pointer = JsonPointer.compile(input);
        assertFalse(pointer.matches());
        assertEquals("baz", pointer.getMatchingProperty());
        assertFalse(pointer.mayMatchProperty());
        assertFalse(pointer.mayMatchElement());
        assertNull(pointer.tail());
        assertEquals("/foo/bar/baz", pointer.toString());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidPointer() {
        String input = "foo";
        JsonPointer.compile(input);
    }

    @Test
    public void testTailMethod() {
        String input = "/foo/bar/baz";
        JsonPointer pointer = JsonPointer.compile(input);
        assertEquals("/bar/baz", pointer.tail().toString());
    }

    @Test
    public void testMatchPropertyMethod() {
        String input1 = "/foo/bar";
        JsonPointer pointer1 = JsonPointer.compile(input1);
        String input2 = "/foo";
        JsonPointer pointer2 = JsonPointer.compile(input2);
        assertEquals(pointer2, pointer1.matchProperty("bar"));
        assertNull(pointer1.matchProperty("baz"));
    }

    @Test
    public void testMatchElementMethod() {
        String input1 = "/foo/0";
        JsonPointer pointer1 = JsonPointer.compile(input1);
        String input2 = "/foo/bar";
        JsonPointer pointer2 = JsonPointer.compile(input2);
        assertEquals(pointer1, pointer2.matchElement(0));
        assertNull(pointer2.matchElement(1));
    }

    @Test
    public void testHashCodeAndEquals() {
        String input1 = "/foo/bar";
        JsonPointer pointer1 = JsonPointer.compile(input1);
        String input2 = "/foo/bar";
        JsonPointer pointer2 = JsonPointer.compile(input2);
        assertEquals(pointer1, pointer2);
        String input3 = "/foo/baz";
        JsonPointer pointer3 = JsonPointer.compile(input3);
        assertNotEquals(pointer1, pointer3);
    }
}