package com.fasterxml.jackson.core;

import org.junit.Test;

import static org.junit.Assert.*;

public class JsonPointerTest {

    // Test cases for constructor used for creating "empty" instance
    @Test
    public void testEmptyConstructor() {
        JsonPointer pointer = new JsonPointer();
        assertTrue(pointer.matches());
        assertEquals("", pointer.getMatchingProperty());
        assertEquals(-1, pointer.getMatchingIndex());
        assertFalse(pointer.mayMatchProperty());
        assertFalse(pointer.mayMatchElement());
        assertNull(pointer.tail());
    }

    // Test cases for constructor used for creating non-empty Segments
    @Test
    public void testNonEmptyConstructor() {
        JsonPointer pointer = new JsonPointer("fullString", "segment", null);
        assertFalse(pointer.matches());
        assertEquals("segment", pointer.getMatchingProperty());
        assertEquals(-1, pointer.getMatchingIndex());
        assertTrue(pointer.mayMatchProperty());
        assertFalse(pointer.mayMatchElement());
        assertNull(pointer.tail());
    }

    // Test cases for factory method that parses given input and construct matching pointer instance
    @Test
    public void testCompile() {
        // Test case for valid input
        JsonPointer pointer = JsonPointer.compile("/segment/1");
        assertFalse(pointer.matches());
        assertEquals("segment", pointer.getMatchingProperty());
        assertEquals(1, pointer.getMatchingIndex());
        assertTrue(pointer.mayMatchProperty());
        assertTrue(pointer.mayMatchElement());
        assertNotNull(pointer.tail());

        // Test case for invalid input
        try {
            JsonPointer invalidPointer = JsonPointer.compile("segment/1");
            fail("IllegalArgumentException should have been thrown.");
        } catch (IllegalArgumentException e) {
            assertEquals("Invalid input: JSON Pointer expression must start with '/': \"segment/1\"", e.getMessage());
        }
    }

    // Test cases for alias factory method
    @Test
    public void testValueOf() {
        JsonPointer pointer = JsonPointer.valueOf("/segment/1");
        assertFalse(pointer.matches());
        assertEquals("segment", pointer.getMatchingProperty());
        assertEquals(1, pointer.getMatchingIndex());
        assertTrue(pointer.mayMatchProperty());
        assertTrue(pointer.mayMatchElement());
        assertNotNull(pointer.tail());
    }

    // Test cases for matchProperty method
    @Test
    public void testMatchProperty() {
        JsonPointer pointer = new JsonPointer("fullString", "segment", null);
        
        // Test case for matching property
        JsonPointer nextSegment = pointer.matchProperty("segment");
        assertNotNull(nextSegment);
        assertFalse(nextSegment.matches());
        assertEquals("segment", nextSegment.getMatchingProperty());
        assertEquals(-1, nextSegment.getMatchingIndex());
        assertTrue(nextSegment.mayMatchProperty());
        assertFalse(nextSegment.mayMatchElement());
        assertNull(nextSegment.tail());

        // Test case for non-matching property
        nextSegment = pointer.matchProperty("otherSegment");
        assertNull(nextSegment);
    }

    // Test cases for matchElement method
    @Test
    public void testMatchElement() {
        JsonPointer pointer = new JsonPointer("fullString", "segment", null);
        
        // Test case for matching index
        JsonPointer nextSegment = pointer.matchElement(1);
        assertNotNull(nextSegment);
        assertFalse(nextSegment.matches());
        assertEquals("segment", nextSegment.getMatchingProperty());
        assertEquals(-1, nextSegment.getMatchingIndex());
        assertTrue(nextSegment.mayMatchProperty());
        assertFalse(nextSegment.mayMatchElement());
        assertNull(nextSegment.tail());

        // Test case for non-matching index
        nextSegment = pointer.matchElement(0);
        assertNull(nextSegment);
    }

    // Test case for tail method
    @Test
    public void testTail() {
        JsonPointer pointer = new JsonPointer("fullString", "segment", null);
        assertNull(pointer.tail());
    }

    // Test case for toString method
    @Test
    public void testToString() {
        JsonPointer pointer = new JsonPointer("fullString", "segment", null);
        assertEquals("fullString", pointer.toString());
    }

    // Test case for hashCode method
    @Test
    public void testHashCode() {
        JsonPointer pointer1 = new JsonPointer("fullString", "segment", null);
        JsonPointer pointer2 = new JsonPointer("fullString", "segment", null);
        assertEquals(pointer1.hashCode(), pointer2.hashCode());
    }

    // Test case for equals method
    @Test
    public void testEquals() {
        JsonPointer pointer1 = new JsonPointer("fullString", "segment", null);
        JsonPointer pointer2 = new JsonPointer("fullString", "segment", null);
        JsonPointer pointer3 = new JsonPointer("otherString", "segment", null);
        assertTrue(pointer1.equals(pointer2));
        assertFalse(pointer1.equals(pointer3));
    }

    // Additional test cases can be added for edge cases and error scenarios

}