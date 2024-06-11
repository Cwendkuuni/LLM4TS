package com.fasterxml.jackson.core;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertNotEquals;

import org.junit.Test;

public class JsonPointerTest {

    @Test
    public void testEmptyPointer() {
        JsonPointer empty = JsonPointer.EMPTY;
        assertEquals("/", empty.toString());
        assertEquals(empty, empty.tail());
        assertEquals(false, empty.matches());
        assertNull(empty.getMatchingProperty());
        assertNull(empty.getMatchingIndex());
        assertFalse(empty.mayMatchProperty());
        assertFalse(empty.mayMatchElement());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidInput() {
        JsonPointer.compile("invalid");
    }

    @Test
    public void testSimplePointer() {
        String input = "/foo";
        JsonPointer simple = JsonPointer.compile(input);
        assertEquals(input, simple.toString());
        assertEquals("/foo", simple.getMatchingProperty());
        assertEquals(0, simple.getMatchingIndex());
        assertEquals(false, simple.mayMatchProperty());
        assertEquals(false, simple.mayMatchElement());
        assertEquals(simple, simple.tail());
        assertEquals(false, simple.matches());
    }

    @Test
    public void testComplexPointer() {
        String input = "/foo/bar/baz/1";
        JsonPointer complex = JsonPointer.compile(input);
        assertEquals(input, complex.toString());
        assertEquals("/foo", complex.getMatchingProperty());
        assertEquals(1, complex.getMatchingIndex());
        assertEquals(false, complex.mayMatchProperty());
        assertEquals(true, complex.mayMatchElement());
        assertEquals(JsonPointer.EMPTY, complex.tail());
        assertFalse(complex.matches());
    }

    @Test
    public void testMatchProperty() {
        String input = "/foo/bar";
        JsonPointer pointer = JsonPointer.compile(input);
        String propertyName = "bar";
        JsonPointer matchProperty = pointer.matchProperty(propertyName);
        assertEquals("/bar", matchProperty.toString());
        assertEquals("/bar", matchProperty.getMatchingProperty());
        assertEquals(-1, matchProperty.getMatchingIndex());
        assertEquals(matchProperty, matchProperty.tail());
        assertTrue(matchProperty.matches());
        assertNotEquals(pointer, matchProperty);
    }

    @Test
    public void testMatchElement() {
        String input = "/foo/bar/1";
        JsonPointer pointer = JsonPointer.compile(input);
        int index = 1;
        JsonPointer matchElement = pointer.matchElement(index);
        assertEquals("/bar", matchElement.getMatchingProperty());
        assertEquals(1, matchElement.getMatchingIndex());
        assertEquals(JsonPointer.EMPTY, matchElement.tail());
        assertTrue(matchElement.matches());
        assertNotEquals(pointer, matchElement);
    }

    @Test
    public void testQuotedSegments() {
        String input = "/foo/bar/~1~/baz";
        JsonPointer pointer = JsonPointer.compile(input);
        assertEquals("/foo/bar/1/baz", pointer.toString());
        assertEquals("/foo/bar/1", pointer.getMatchingProperty());
        assertEquals(1, pointer.getMatchingIndex());
        assertEquals(JsonPointer.EMPTY, pointer.tail());
        assertTrue(pointer.matches());
    }

    @Test
    public void testFromSegment() {
        String input = "/foo/bar/baz";
        String[] segments = input.split("/");
        JsonPointer pointer = JsonPointer.fromSegment(segments);
        assertEquals("/foo/bar/baz", pointer.toString());
        assertEquals("/foo", pointer.getMatchingProperty());
        assertEquals(-1, pointer.getMatchingIndex());
        assertEquals(JsonPointer.EMPTY, pointer.tail());
        assertTrue(pointer.matches());
    }
}