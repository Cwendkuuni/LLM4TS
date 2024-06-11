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
        assertFalse(pointer.mayMatchProperty());
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
        assertEquals("bar", tail.getMatchingProperty());
        assertFalse(tail.matches());
        assertNull(tail.tail());
        assertEquals("/bar", tail.toString());
        assertEquals("/foo", pointer.toString());
    }

    @Test
    public void testTrailingSlashPointer() {
        String input = "/foo/";
        JsonPointer pointer = JsonPointer.compile(input);
        assertFalse(pointer.matches());
        assertNull(pointer.getMatchingProperty());
        assertTrue(pointer.mayMatchProperty());
        assertFalse(pointer.mayMatchElement());
        assertNull(pointer.tail());
        assertEquals(input, pointer.toString());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidInput() {
        String input = "~foo";
        JsonPointer.compile(input);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testEmptyInput() {
        JsonPointer.compile("");
    }

    @Test
    public void testMatchesProperty() {
        String input = "/foo/bar";
        JsonPointer pointer = JsonPointer.compile(input);
        JsonPointer matching = new JsonPointer("/foo", "bar", JsonPointer.EMPTY);
        assertTrue(pointer.matchesProperty(matching.getMatchingProperty()));
        assertEquals(matching, pointer.tail());
    }

    @Test
    public void testMatchesElement() {
        String input = "/list[0]/name";
        JsonPointer pointer = JsonPointer.compile(input);
        JsonPointer matching = new JsonPointer("/list", "name", JsonPointer.EMPTY);
        JsonPointer listPointer = new JsonPointer("/list", "", JsonPointer.EMPTY);
        JsonPointer namePointer = new JsonPointer("", "name", JsonPointer.EMPTY);
        assertEquals(namePointer, pointer.matchElement(0));
        assertEquals(matching, pointer.tail());
        assertEquals(listPointer, pointer.tail().tail());
        assertFalse(pointer.matchesElement(1));
        assertNull(pointer.matchElement(-1));
    }

    @Test
    public void testMatchesNull() {
        String input = "/foo";
        JsonPointer pointer = JsonPointer.compile(input);
        assertNull(pointer.matchProperty(null));
        assertNull(pointer.matchElement(0));
    }
}