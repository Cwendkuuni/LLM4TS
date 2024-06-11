package com.fasterxml.jackson.core;
import static org.junit.Assert.*;
import org.junit.Test;
import com.fasterxml.jackson.core.JsonPointer;

public class JsonPointerTest {

    // Test for empty JSON Pointer
    @Test
    public void testEmptyPointer() {
        JsonPointer pointer = JsonPointer.compile("");
        assertTrue(pointer.matches());
        assertEquals("", pointer.getMatchingProperty());
        assertEquals(-1, pointer.getMatchingIndex());
        assertNull(pointer.tail());
    }

    // Test for JSON Pointer with one segment
    @Test
    public void testSingleSegmentPointer() {
        JsonPointer pointer = JsonPointer.compile("/foo");
        assertFalse(pointer.matches());
        assertEquals("foo", pointer.getMatchingProperty());
        assertEquals(-1, pointer.getMatchingIndex());
        assertNull(pointer.tail());
    }

    // Test for JSON Pointer with multiple segments
    @Test
    public void testMultiSegmentPointer() {
        JsonPointer pointer = JsonPointer.compile("/foo/bar/baz");
        assertFalse(pointer.matches());
        assertEquals("foo", pointer.getMatchingProperty());
        assertFalse(pointer.mayMatchElement());
        assertNull(pointer.matchElement(0));
        
        pointer = pointer.tail();
        assertEquals("bar", pointer.getMatchingProperty());
        assertFalse(pointer.mayMatchElement());
        assertNull(pointer.matchElement(1));
        
        pointer = pointer.tail();
        assertEquals("baz", pointer.getMatchingProperty());
        assertTrue(pointer.mayMatchElement());
        assertNull(pointer.matchElement(2));
        
        pointer = pointer.tail();
        assertTrue(pointer.matches());
        assertNull(pointer.getMatchingProperty());
        assertEquals(-1, pointer.getMatchingIndex());
        assertNull(pointer.tail());
    }

    // Test for JSON Pointer with a numeric segment
    @Test
    public void testNumericSegmentPointer() {
        JsonPointer pointer = JsonPointer.compile("/0");
        assertFalse(pointer.matches());
        assertNull(pointer.getMatchingProperty());
        assertEquals(0, pointer.getMatchingIndex());
        assertNull(pointer.tail());
    }

    // Test for JSON Pointer with an escape sequence
    @Test
    public void testEscapedSegmentPointer() {
        JsonPointer pointer = JsonPointer.compile("/~0");
        assertFalse(pointer.matches());
        assertNull(pointer.getMatchingProperty());
        assertNull(pointer.getMatchingIndex());
        assertNull(pointer.tail());
        
        pointer = JsonPointer.compile("/~1");
        assertFalse(pointer.matches());
        assertNull(pointer.getMatchingProperty());
        assertNull(pointer.getMatchingIndex());
        assertNull(pointer.tail());
    }

    // Test for JSON Pointer with a mix of segments
    @Test
    public void testMixedSegmentPointer() {
        JsonPointer pointer = JsonPointer.compile("/foo/0/~1");
        assertFalse(pointer.matches());
        assertEquals("foo", pointer.getMatchingProperty());
        assertFalse(pointer.mayMatchElement());
        assertNull(pointer.matchElement(0));
        
        pointer = pointer.tail();
        assertNull(pointer.getMatchingProperty());
        assertEquals(0, pointer.getMatchingIndex());
        assertFalse(pointer.mayMatchProperty());
        assertNull(pointer.matchProperty("1"));
        
        pointer = pointer.tail();
        assertNull(pointer.getMatchingProperty());
        assertNull(pointer.getMatchingIndex());
        assertNull(pointer.tail());
    }
}