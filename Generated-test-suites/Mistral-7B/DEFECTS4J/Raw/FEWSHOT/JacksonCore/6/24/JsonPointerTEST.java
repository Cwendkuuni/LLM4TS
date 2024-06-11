package com.fasterxml.jackson.core;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

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
        assertFalse(pointer.mayMatchElement());
        assertNull(pointer.tail());
        assertEquals("/foo", pointer.toString());
    }

    @Test
    public void testPointerWithMatchingElement() {
        String input = "/list/1";
        JsonPointer pointer = JsonPointer.compile(input);
        assertTrue(pointer.matches());
        assertNull(pointer.getMatchingProperty());
        assertTrue(pointer.mayMatchElement());
        assertEquals(1, pointer.getMatchingIndex());
        assertNull(pointer.tail());
        assertEquals("/list/1", pointer.toString());
    }

    @Test
    public void testPointerWithMatchingProperty() {
        String input = "/obj/prop";
        JsonPointer pointer = JsonPointer.compile(input);
        assertTrue(pointer.matches());
        assertEquals("prop", pointer.getMatchingProperty());
        assertFalse(pointer.mayMatchElement());
        assertNull(pointer.tail());
        assertEquals("/obj/prop", pointer.toString());
    }

    @Test
    public void testPointerWithMultipleSegments() {
        String input = "/obj/list/2";
        JsonPointer pointer = JsonPointer.compile(input);
        assertTrue(pointer.matches());
        assertEquals("list", pointer.getMatchingProperty());
        assertTrue(pointer.mayMatchElement());
        assertEquals(2, pointer.getMatchingIndex());
        JsonPointer tail = pointer.tail();
        assertFalse(tail.matches());
        assertEquals("2", tail.getMatchingProperty());
        assertFalse(tail.mayMatchElement());
        assertNull(tail.tail());
        assertEquals("/obj/list/2", pointer.toString());
        assertEquals("/obj/list", tail.toString());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidInput() {
        String input = "invalid";
        JsonPointer.compile(input);
    }

    @Test
    public void testFromSegment() {
        String[] segments = {"obj", "list", "2"};
        JsonPointer pointer = JsonPointer.fromSegment(segments);
        assertEquals("obj/list/2", pointer.toString());
        assertEquals("obj", pointer.getMatchingProperty());
        assertTrue(pointer.mayMatchElement());
        assertEquals(2, pointer.getMatchingIndex());
        JsonPointer tail = pointer.tail();
        assertEquals("list/2", tail.toString());
        assertEquals("list", tail.getMatchingProperty());
        assertTrue(tail.mayMatchElement());
        assertEquals(2, tail.getMatchingIndex());
    }
}