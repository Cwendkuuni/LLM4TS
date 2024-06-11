package com.fasterxml.jackson.core;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class JsonPointerTest {

    @Test
    public void testEmptyPointer() {
        JsonPointer pointer = JsonPointer.EMPTY;
        assertTrue(pointer.matches());
        assertNull(pointer.getMatchingProperty());
        assertFalse(pointer.mayMatchProperty());
        assertFalse(pointer.mayMatchElement());
        assertNull(pointer.tail());
        assertEquals("/", pointer.toString());
    }

    @Test
    public void testSimplePointer() {
        String input = "/foo";
        JsonPointer pointer = JsonPointer.compile(input);
        assertTrue(pointer.matches());
        assertEquals("foo", pointer.getMatchingProperty());
        assertFalse(pointer.mayMatchElement());
        assertNull(pointer.tail());
        assertEquals(input, pointer.toString());
    }

    @Test
    public void testMultiSegmentPointer() {
        String input = "/foo/bar";
        JsonPointer pointer = JsonPointer.compile(input);
        assertFalse(pointer.matches());
        assertEquals("foo", pointer.getMatchingProperty());
        assertTrue(pointer.mayMatchProperty());
        assertFalse(pointer.mayMatchElement());
        JsonPointer tail = pointer.tail();
        assertEquals("/bar", tail.toString());
        assertEquals(1, pointer.toString().split("/").length);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidInput() {
        String input = "foo";
        JsonPointer.compile(input);
    }

    @Test
    public void testIndexedPointer() {
        String input = "/foo/0";
        JsonPointer pointer = JsonPointer.compile(input);
        assertFalse(pointer.matches());
        assertEquals("foo", pointer.getMatchingProperty());
        assertFalse(pointer.mayMatchProperty());
        assertTrue(pointer.mayMatchElement());
        assertEquals(0, pointer.getMatchingIndex());
        JsonPointer tail = pointer.tail();
        assertEquals("/", tail.toString());
        assertEquals(1, pointer.toString().split("/").length);
    }

    @Test
    public void testMultiLevelIndexedPointer() {
        String input = "/foo/1/bar/2";
        JsonPointer pointer = JsonPointer.compile(input);
        assertFalse(pointer.matches());
        assertEquals("foo", pointer.getMatchingProperty());
        assertTrue(pointer.mayMatchProperty());
        assertTrue(pointer.mayMatchElement());
        assertEquals(1, pointer.getMatchingIndex());
        JsonPointer tail = pointer.tail();
        assertEquals("/bar/2", tail.toString());
        assertEquals(3, pointer.toString().split("/").length);
    }

    @Test
    public void testMatchingProperty() {
        String input = "/foo/bar";
        JsonPointer pointer = JsonPointer.compile(input);
        String property = "bar";
        JsonPointer matchingPropertyPointer = pointer.matchProperty(property);
        assertTrue(matchingPropertyPointer.matches());
        assertEquals(property, matchingPropertyPointer.getMatchingProperty());
        assertNull(matchingPropertyPointer.tail());
    }

    @Test
    public void testMatchingElement() {
        String input = "/foo/0";
        JsonPointer pointer = JsonPointer.compile(input);
        JsonPointer matchingElementPointer = pointer.matchElement(0);
        assertTrue(matchingElementPointer.matches());
        assertEquals("", matchingElementPointer.getMatchingProperty());
        assertFalse(matchingElementPointer.mayMatchProperty());
        assertFalse(matchingElementPointer.mayMatchElement());
        assertNull(matchingElementPointer.tail());
    }

    @Test
    public void testMismatchedProperty() {
        String input = "/foo/bar";
        JsonPointer pointer = JsonPointer.compile(input);
        String property = "baz";
        JsonPointer matchingPropertyPointer = pointer.matchProperty(property);
        assertNull(matchingPropertyPointer);
    }

    @Test
    public void testMismatchedElement() {
        String input = "/foo/0";
        JsonPointer pointer = JsonPointer.compile(input);
        JsonPointer matchingElementPointer = pointer.matchElement(1);
        assertNull(matchingElementPointer);
    }
}