package com.fasterxml.jackson.core;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class JsonPointerTest {

    @Test
    public void testEmptyJsonPointer() {
        JsonPointer empty = JsonPointer.EMPTY;
        assertTrue(empty.matches());
        assertNull(empty.getMatchingProperty());
        assertFalse(empty.mayMatchProperty());
        assertFalse(empty.mayMatchElement());
    }

    @Test
    public void testJsonPointerCreation() {
        String pointerString = "/foo/bar/1";
        JsonPointer pointer = JsonPointer.compile(pointerString);
        assertEquals(pointerString, pointer.toString());
        assertEquals("foo", pointer.getMatchingProperty());
        assertEquals(1, pointer.getMatchingIndex());
        assertFalse(pointer.matches());
        assertNotNull(pointer.tail());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidJsonPointerInput() {
        String invalidPointerString = "foo";
        JsonPointer.compile(invalidPointerString);
    }

    @Test
    public void testJsonPointerMatches() {
        String pointerString = "/foo/bar/1";
        JsonPointer pointer = JsonPointer.compile(pointerString);
        String subString = "/foo";

        JsonPointer matchingPointer = new JsonPointer(pointerString, subString, null);
        JsonPointer notMatchingPointer = new JsonPointer(pointerString, "baz", null);

        assertTrue(matchingPointer.matches());
        assertSame(matchingPointer, pointer);
        assertFalse(notMatchingPointer.matches());
    }

    @Test
    public void testJsonPointerTail() {
        String pointerString = "/foo/bar/1";
        JsonPointer pointer = JsonPointer.compile(pointerString);

        JsonPointer tail = pointer.tail();
        assertEquals("/bar/1", tail.toString());
    }

    @Test
    public void testJsonPointerMatchProperty() {
        String pointerString = "/foo/bar/1";
        JsonPointer pointer = JsonPointer.compile(pointerString);

        String matchingProperty = "bar";
        JsonPointer matchingPointer = new JsonPointer(pointerString, matchingProperty, null);
        assertEquals(matchingProperty, pointer.matchProperty(matchingProperty).getMatchingProperty());
        assertSame(matchingPointer, pointer.matchProperty(matchingProperty));

        String notMatchingProperty = "baz";
        JsonPointer notMatchingPointer = new JsonPointer(pointerString, notMatchingProperty, null);
        assertNull(pointer.matchProperty(notMatchingProperty));
    }

    @Test
    public void testJsonPointerMatchElement() {
        String pointerString = "/foo/bar[1]/baz";
        JsonPointer pointer = JsonPointer.compile(pointerString);

        JsonPointer matchingElementPointer = new JsonPointer(pointerString, null, new JsonPointer("/foo/bar[2]/baz", null, null));
        JsonPointer notMatchingElementPointer = new JsonPointer(pointerString, null, new JsonPointer("/foo/bar[0]/baz", null, null));

        assertTrue(pointer.mayMatchElement());
        assertTrue(matchingElementPointer.matches());
        assertSame(matchingElementPointer, pointer.matchElement(1));
        assertNull(pointer.matchElement(0));
    }
}