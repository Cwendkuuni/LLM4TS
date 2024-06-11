package com.fasterxml.jackson.core;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class JsonPointerTest {
    @Test
    public void testEmptyPointer() {
        JsonPointer emptyPointer = JsonPointer.EMPTY;
        assertTrue(emptyPointer.matches());
        assertNull(emptyPointer.getMatchingProperty());
        assertFalse(emptyPointer.mayMatchProperty());
        assertFalse(emptyPointer.mayMatchElement());
        assertNull(emptyPointer.tail());
        assertEquals("", emptyPointer.toString());
    }

    @Test
    public void testSingleSegmentPointer() {
        JsonPointer singleSegmentPointer = new JsonPointer("/foo", "foo", JsonPointer.EMPTY);
        assertEquals("/foo", singleSegmentPointer.toString());
        assertTrue(singleSegmentPointer.matches());
        assertEquals("foo", singleSegmentPointer.getMatchingProperty());
        assertTrue(singleSegmentPointer.mayMatchProperty());
        assertFalse(singleSegmentPointer.mayMatchElement());
        assertNull(singleSegmentPointer.tail());
    }

    @Test
    public void testMultiSegmentPointer() {
        JsonPointer multiSegmentPointer = new JsonPointer("/foo/bar/baz", "bar", new JsonPointer("/foo/baz", "baz", JsonPointer.EMPTY));
        assertEquals("/foo/bar/baz", multiSegmentPointer.toString());
        assertTrue(multiSegmentPointer.matches());
        assertEquals("bar", multiSegmentPointer.getMatchingProperty());
        assertTrue(multiSegmentPointer.mayMatchProperty());
        assertTrue(multiSegmentPointer.mayMatchElement());
        assertEquals("/baz", multiSegmentPointer.tail().toString());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidInput() {
        JsonPointer.compile("invalid");
    }

    @Test
    public void testMatchProperty() {
        JsonPointer pointer = new JsonPointer("/foo/bar/baz", "bar", new JsonPointer("/foo/baz", "baz", JsonPointer.EMPTY));
        JsonPointer matchedPointer = pointer.matchProperty("bar");
        assertEquals("/foo/baz", matchedPointer.toString());
        assertTrue(matchedPointer.matches());
        assertFalse(matchedPointer.mayMatchProperty());
        assertTrue(matchedPointer.mayMatchElement());
        assertNull(matchedPointer.tail());
    }

    @Test
    public void testMatchElement() {
        JsonPointer pointer = new JsonPointer("/foo/0/bar", "0", new JsonPointer("/foo/bar", "bar", JsonPointer.EMPTY));
        JsonPointer matchedPointer = pointer.matchElement(0);
        assertEquals("/foo/bar", matchedPointer.toString());
        assertTrue(matchedPointer.matches());
        assertFalse(matchedPointer.mayMatchProperty());
        assertTrue(matchedPointer.mayMatchElement());
        assertNull(matchedPointer.tail());
    }

    @Test
    public void testNullPointer() {
        JsonPointer nullPointer = new JsonPointer(null, null, JsonPointer.EMPTY);
        assertNull(nullPointer);
    }
}