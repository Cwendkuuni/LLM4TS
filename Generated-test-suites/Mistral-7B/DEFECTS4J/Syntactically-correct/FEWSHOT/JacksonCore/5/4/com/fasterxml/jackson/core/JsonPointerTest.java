package com.fasterxml.jackson.core;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class JsonPointerTest {

    @Test
    public void testEmptyPointer() {
        JsonPointer emptyPointer = JsonPointer.EMPTY;
        assertTrue(emptyPointer.matches());
        assertNull(emptyPointer.getMatchingProperty());
        assertNull(emptyPointer.getMatchingIndex());
        assertNull(emptyPointer.tail());
        assertEquals("", emptyPointer.toString());
    }

    @Test
    public void testBasicSegmentPointer() {
        String input = "/segment1";
        JsonPointer pointer = JsonPointer.compile(input);
        assertEquals("/segment1", pointer.toString());
        assertTrue(pointer.matches());
        assertNull(pointer.getMatchingProperty());
        assertNull(pointer.getMatchingIndex());
        assertNull(pointer.tail());
    }

    @Test
    public void testPropertyMatchingPointer() {
        String input = "/segment1/property";
        JsonPointer pointer = JsonPointer.compile(input);
        assertEquals("/segment1/property", pointer.toString());
        assertFalse(pointer.matches());
        assertEquals("property", pointer.getMatchingProperty());
        assertNull(pointer.getMatchingIndex());
        assertNull(pointer.tail());
    }

    @Test
    public void testElementMatchingPointer() {
        String input = "/segment1/0";
        JsonPointer pointer = JsonPointer.compile(input);
        assertEquals("/segment1/0", pointer.toString());
        assertFalse(pointer.matches());
        assertNull(pointer.getMatchingProperty());
        assertEquals(0, pointer.getMatchingIndex());
        assertNull(pointer.tail());
    }

    @Test
    public void testMultipleSegmentPointer() {
        String input = "/segment1/property1/property2";
        JsonPointer pointer = JsonPointer.compile(input);
        assertEquals("/segment1/property1/property2", pointer.toString());
        assertFalse(pointer.matches());
        assertEquals("property1", pointer.getMatchingProperty());
        assertNull(pointer.getMatchingIndex());
        JsonPointer tail = pointer.tail();
        assertEquals("/property2", tail.toString());
        assertFalse(tail.matches());
        assertNull(tail.getMatchingProperty());
        assertNull(tail.getMatchingIndex());
        assertNull(tail.tail());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidInputPointer() {
        String input = "invalid";
        JsonPointer.compile(input);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidInputWithSlashPointer() {
        String input = "/invalid";
        JsonPointer.compile(input);
    }
}