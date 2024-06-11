package com.fasterxml.jackson.core;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

public class JsonPointerTest {

    @Test
    public void testEmptyPointer() {
        JsonPointer pointer = JsonPointer.EMPTY;
        assertTrue(pointer.matches());
        assertNull(pointer.getMatchingProperty());
        assertNull(pointer.getMatchingIndex());
        assertFalse(pointer.mayMatchProperty());
        assertFalse(pointer.mayMatchElement());
        assertEquals("/", pointer.toString());
    }

    @Test
    public void testSegmentedPointer() {
        JsonPointer pointer = JsonPointer.compile("/a/b/c");
        assertTrue(pointer.matches());
        assertEquals("a", pointer.getMatchingProperty());
        assertEquals(-1, pointer.getMatchingIndex());
        assertTrue(pointer.mayMatchProperty());
        assertFalse(pointer.mayMatchElement());
        assertEquals("/a/b/c", pointer.toString());

        JsonPointer tail = pointer.tail();
        assertEquals("/b/c", tail.toString());
        assertEquals("b", tail.getMatchingProperty());
        assertFalse(tail.mayMatchProperty());
        assertTrue(tail.mayMatchElement());
        assertEquals(-1, tail.getMatchingIndex());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidPointer() {
        JsonPointer.compile("invalid");
    }

    @Test
    public void testPropertyMatching() {
        JsonPointer pointer = JsonPointer.compile("/a/b/c");
        JsonPointer propertyPointer = JsonPointer.compile("/a/b");
        JsonPointer tail = pointer.tail();

        assertNull(pointer.matchProperty("d"));
        assertNull(propertyPointer.matchProperty("d"));
        assertNull(tail.matchProperty("d"));

        JsonPointer matchedPointer = pointer.matchProperty("b");
        assertEquals("/c", matchedPointer.toString());
        assertEquals("c", matchedPointer.getMatchingProperty());
        assertFalse(matchedPointer.mayMatchProperty());
        assertFalse(matchedPointer.mayMatchElement());
        assertEquals(-1, matchedPointer.getMatchingIndex());
    }

    @Test
    public void testElementMatching() {
        JsonPointer pointer = JsonPointer.compile("/a/0/c");
        JsonPointer tail = pointer.tail();

        assertNull(pointer.matchElement(1));
        assertNull(tail.matchElement(1));
        assertEquals(0, pointer.getMatchingIndex());
        assertEquals("/c", pointer.toString());
    }

    @Test
    public void testMultipleSegments() {
        JsonPointer pointer = JsonPointer.compile("/a/b/1/c/d/2");
        JsonPointer tail = pointer.tail();
        JsonPointer tail2 = tail.tail();

        assertEquals("/c/d/2", tail2.toString());
        assertEquals("d", tail2.getMatchingProperty());
        assertEquals(2, tail2.getMatchingIndex());
        assertFalse(tail2.mayMatchProperty());
        assertTrue(tail2.mayMatchElement());

        assertEquals("/c/d", tail.toString());
        assertEquals("d", tail.getMatchingProperty());
        assertEquals(-1, tail.getMatchingIndex());
        assertTrue(tail.mayMatchProperty());
        assertFalse(tail.mayMatchElement());

        assertEquals("/a/b/1/c/d", pointer.toString());
        assertEquals("b", pointer.getMatchingProperty());
        assertEquals(1, pointer.getMatchingIndex());
        assertTrue(pointer.mayMatchProperty());
        assertFalse(pointer.mayMatchElement());
    }

    @Test
    public void testQuotedSegment() {
        JsonPointer pointer = JsonPointer.compile("/a/~1o/b");
        JsonPointer tail = pointer.tail();

        assertEquals("/b", tail.toString());
        assertEquals("b", tail.getMatchingProperty());
        assertFalse(tail.mayMatchProperty());
        assertTrue(tail.mayMatchElement());
        assertEquals(-1, tail.getMatchingIndex());

        assertEquals("/a/~1o/b", pointer.toString());
        assertEquals("1o", pointer.getMatchingProperty());
        assertEquals(-1, pointer.getMatchingIndex());
        assertTrue(pointer.mayMatchProperty());
        assertFalse(pointer.mayMatchElement());
    }

    @Test
    public void testListOfPointers() {
        List<JsonPointer> pointers = new ArrayList<>();
        pointers.add(JsonPointer.compile("/a"));
        pointers.add(JsonPointer.compile("/b"));
        pointers.add(JsonPointer.compile("/c"));

        for (JsonPointer pointer : pointers) {
            assertTrue(pointer.matches());
            assertTrue(pointer.mayMatchProperty());
            assertFalse(pointer.mayMatchElement());
        }

        JsonPointer pointer = JsonPointer.compile("/a/0");
        JsonPointer matchedPointer = null;
        for (JsonPointer p : pointers) {
            JsonPointer match = p.matchProperty("0");
            if (match != null) {
                matchedPointer = match;
                break;
            }
        }
        assertEquals("/c", matchedPointer.toString());
    }
}