package com.fasterxml.jackson.core;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertNotEquals;

import org.junit.Test;

public class JsonPointerTest {

    @Test
    public void testEmptyPointer() {
        JsonPointer emptyPointer = JsonPointer.EMPTY;
        assertEquals("/", emptyPointer.toString());
        assertEquals(emptyPointer, emptyPointer.tail());
        assertTrue(emptyPointer.matches());
        assertFalse(emptyPointer.mayMatchProperty());
        assertFalse(emptyPointer.mayMatchElement());
        assertNull(emptyPointer.getMatchingProperty());
        assertNull(emptyPointer.getMatchingIndex());
    }

    @Test
    public void testSingleSegmentPointer() {
        String input = "/foo";
        JsonPointer singleSegmentPointer = JsonPointer.compile(input);
        assertEquals(input, singleSegmentPointer.toString());
        assertEquals(singleSegmentPointer, singleSegmentPointer.tail());
        assertFalse(singleSegmentPointer.matches());
        assertTrue(singleSegmentPointer.mayMatchProperty());
        assertFalse(singleSegmentPointer.mayMatchElement());
        assertEquals("foo", singleSegmentPointer.getMatchingProperty());
        assertNull(singleSegmentPointer.getMatchingIndex());
    }

    @Test
    public void testMultiSegmentPointer() {
        String input = "/foo/bar/baz";
        JsonPointer multiSegmentPointer = JsonPointer.compile(input);
        assertEquals(input, multiSegmentPointer.toString());
        assertEquals(multiSegmentPointer, multiSegmentPointer.tail().tail());
        assertFalse(multiSegmentPointer.matches());
        assertTrue(multiSegmentPointer.mayMatchProperty());
        assertTrue(multiSegmentPointer.mayMatchElement());
        assertEquals("foo", multiSegmentPointer.getMatchingProperty());
        assertEquals(0, multiSegmentPointer.getMatchingIndex());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidPointerInput() {
        String input = "~invalid";
        JsonPointer.compile(input);
    }

    @Test
    public void testMatches() {
        String input = "/foo/bar/baz";
        JsonPointer pointer1 = JsonPointer.compile(input);
        JsonPointer pointer2 = JsonPointer.compile("/foo/bar");
        JsonPointer pointer3 = JsonPointer.compile("/foo");
        assertTrue(pointer1.matches());
        assertTrue(pointer2.matches());
        assertTrue(pointer3.matches());
        assertFalse(JsonPointer.EMPTY.matches());
    }

    @Test
    public void testGetMatchingProperty() {
        String input = "/foo/bar";
        JsonPointer pointer = JsonPointer.compile(input);
        assertEquals("bar", pointer.getMatchingProperty());
        JsonPointer emptyPointer = JsonPointer.EMPTY;
        assertNull(emptyPointer.getMatchingProperty());
    }

    @Test
    public void testGetMatchingIndex() {
        String input = "/foo/0";
        JsonPointer pointer = JsonPointer.compile(input);
        assertEquals(0, pointer.getMatchingIndex());
        JsonPointer emptyPointer = JsonPointer.EMPTY;
        assertNull(emptyPointer.getMatchingIndex());
    }

    @Test
    public void testMatchProperty() {
        String input = "/foo/bar";
        JsonPointer pointer = JsonPointer.compile(input);
        JsonPointer matchingProperty = new JsonPointer("/foo/bar");
        assertEquals(matchingProperty, pointer.matchProperty("bar"));
        assertEquals(null, pointer.matchProperty("baz"));
        JsonPointer emptyPointer = JsonPointer.EMPTY;
        assertNull(emptyPointer.matchProperty("bar"));
    }

    @Test
    public void testMatchElement() {
        String input = "/foo/0";
        JsonPointer pointer = JsonPointer.compile(input);
        assertEquals(0, pointer.getMatchingIndex());
        JsonPointer matchingElement = new JsonPointer("/foo/0");
        assertEquals(matchingElement, pointer.matchElement(0));
        assertNull(pointer.matchElement(-1));
        JsonPointer emptyPointer = JsonPointer.EMPTY;
        assertNull(emptyPointer.matchElement(0));
    }

    @Test
    public void testTail() {
        String input = "/foo/bar/baz";
        JsonPointer pointer = JsonPointer.compile(input);
        JsonPointer tail = pointer.tail();
        assertEquals("/foo/bar", tail.toString());
        assertFalse(tail.matches());
        assertTrue(tail.mayMatchProperty());
        assertFalse(tail.mayMatchElement());
        assertEquals("bar", tail.getMatchingProperty());
        assertNull(tail.getMatchingIndex());
    }

    @Test
    public void testToString() {
        String input = "/foo/bar/baz";
        JsonPointer pointer = JsonPointer.compile(input);
        assertEquals(input, pointer.toString());
        JsonPointer emptyPointer = JsonPointer.EMPTY;
        assertEquals("/", emptyPointer.toString());
    }

    @Test
    public void testHashCode() {
        String input = "/foo/bar";
        JsonPointer pointer1 = JsonPointer.compile(input);
        JsonPointer pointer2 = new JsonPointer(input, "bar", JsonPointer.EMPTY);
        assertEquals(pointer1.hashCode(), pointer2.hashCode());
        JsonPointer emptyPointer = JsonPointer.EMPTY;
        assertEquals(emptyPointer.hashCode(), emptyPointer.hashCode());
        assertNotEquals(pointer1.hashCode(), emptyPointer.hashCode());
    }

    @Test
    public void testEquals() {
        String input = "/foo/bar";
        JsonPointer pointer1 = JsonPointer.compile(input);
        JsonPointer pointer2 = new JsonPointer(input, "bar", JsonPointer.EMPTY);
        assertTrue(pointer1.equals(pointer2));
        JsonPointer emptyPointer = JsonPointer.EMPTY;
        assertFalse(pointer1.equals(emptyPointer));
        assertFalse(pointer1.equals("foo"));
        assertFalse(pointer1.equals(new Object()));
    }
}