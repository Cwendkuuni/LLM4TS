package com.fasterxml.jackson.core;

import static org.junit.Assert.*;

import org.junit.Test;

public class JsonPointerTest {

    @Test
    public void testEmptyJsonPointer() {
        JsonPointer jsonPointer = JsonPointer.EMPTY;
        assertTrue(jsonPointer.matches());
        assertNull(jsonPointer.getMatchingProperty());
        assertEquals(-1, jsonPointer.getMatchingIndex());
        assertFalse(jsonPointer.mayMatchProperty());
        assertFalse(jsonPointer.mayMatchElement());
        assertNull(jsonPointer.tail());
        assertEquals("", jsonPointer.toString());
    }

    @Test
    public void testJsonPointerWithSingleSegment() {
        JsonPointer jsonPointer = new JsonPointer("/segment", "segment", JsonPointer.EMPTY);
        assertFalse(jsonPointer.matches());
        assertEquals("segment", jsonPointer.getMatchingProperty());
        assertEquals(-1, jsonPointer.getMatchingIndex());
        assertTrue(jsonPointer.mayMatchProperty());
        assertFalse(jsonPointer.mayMatchElement());
        assertNull(jsonPointer.tail());
        assertEquals("/segment", jsonPointer.toString());
    }

    @Test
    public void testJsonPointerWithMultipleSegments() {
        String input = "/segment1/segment2/segment3";
        JsonPointer jsonPointer = JsonPointer.compile(input);
        assertFalse(jsonPointer.matches());
        assertEquals("segment2", jsonPointer.getMatchingProperty());
        assertEquals(-1, jsonPointer.getMatchingIndex());
        assertTrue(jsonPointer.mayMatchProperty());
        assertFalse(jsonPointer.mayMatchElement());
        JsonPointer tail = jsonPointer.tail();
        assertEquals("/segment1/segment2", tail.toString());
        assertEquals(3, tail.getMatchingIndex());
        assertTrue(tail.mayMatchProperty());
        assertEquals("segment3", tail.getMatchingProperty());
        assertNull(tail.tail());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testJsonPointerWithInvalidInput() {
        JsonPointer.compile("invalid");
    }

    @Test
    public void testJsonPointerWithQuotedSegment() {
        String input = "/segment/segment~1";
        JsonPointer jsonPointer = JsonPointer.compile(input);
        assertFalse(jsonPointer.matches());
        assertEquals("segment", jsonPointer.getMatchingProperty());
        assertEquals(-1, jsonPointer.getMatchingIndex());
        assertTrue(jsonPointer.mayMatchProperty());
        assertFalse(jsonPointer.mayMatchElement());
        JsonPointer tail = jsonPointer.tail();
        assertEquals("/segment", tail.toString());
        assertEquals(1, tail.getMatchingIndex());
        assertTrue(tail.mayMatchProperty());
        assertNull(tail.tail());
    }

    @Test
    public void testJsonPointerWithQuotedSegments() {
        String input = "/segment/segment~1/segment~2";
        JsonPointer jsonPointer = JsonPointer.compile(input);
        assertFalse(jsonPointer.matches());
        assertEquals("segment", jsonPointer.getMatchingProperty());
        assertEquals(-1, jsonPointer.getMatchingIndex());
        assertTrue(jsonPointer.mayMatchProperty());
        assertFalse(jsonPointer.mayMatchElement());
        JsonPointer tail = jsonPointer.tail();
        assertEquals("/segment/segment~1", tail.toString());
        assertEquals(2, tail.getMatchingIndex());
        assertTrue(tail.mayMatchProperty());
        assertNull(tail.tail());
    }
}