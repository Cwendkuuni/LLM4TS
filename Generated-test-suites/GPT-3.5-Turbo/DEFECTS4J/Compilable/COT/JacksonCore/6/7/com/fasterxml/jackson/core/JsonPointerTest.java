package com.fasterxml.jackson.core;

import static org.junit.Assert.*;
import org.junit.Test;

public class JsonPointerTest {

    @Test
    public void testMatches() {
        // create a new JsonPointer instance
        JsonPointer pointer = new JsonPointer();

        // test the matches() method
        assertTrue(pointer.matches());
    }

    @Test
    public void testGetMatchingProperty() {
        // create a new JsonPointer instance
        JsonPointer pointer = new JsonPointer();

        // test the getMatchingProperty() method
        assertEquals("", pointer.getMatchingProperty());
    }

    @Test
    public void testGetMatchingIndex() {
        // create a new JsonPointer instance
        JsonPointer pointer = new JsonPointer();

        // test the getMatchingIndex() method
        assertEquals(-1, pointer.getMatchingIndex());
    }

    @Test
    public void testMayMatchProperty() {
        // create a new JsonPointer instance
        JsonPointer pointer = new JsonPointer();

        // test the mayMatchProperty() method
        assertTrue(pointer.mayMatchProperty());
    }

    @Test
    public void testMayMatchElement() {
        // create a new JsonPointer instance
        JsonPointer pointer = new JsonPointer();

        // test the mayMatchElement() method
        assertTrue(pointer.mayMatchElement());
    }

    @Test
    public void testMatchProperty() {
        // create a new JsonPointer instance
        JsonPointer pointer = new JsonPointer();

        // test the matchProperty() method
        assertNull(pointer.matchProperty("test"));
    }

    @Test
    public void testMatchElement() {
        // create a new JsonPointer instance
        JsonPointer pointer = new JsonPointer();

        // test the matchElement() method
        assertNull(pointer.matchElement(0));
    }

    @Test
    public void testTail() {
        // create a new JsonPointer instance
        JsonPointer pointer = new JsonPointer();

        // test the tail() method
        assertNull(pointer.tail());
    }

    @Test
    public void testToString() {
        // create a new JsonPointer instance
        JsonPointer pointer = new JsonPointer();

        // test the toString() method
        assertEquals("", pointer.toString());
    }

    @Test
    public void testHashCode() {
        // create a new JsonPointer instance
        JsonPointer pointer = new JsonPointer();

        // test the hashCode() method
        assertNotNull(pointer.hashCode());
    }

    @Test
    public void testEquals() {
        // create a new JsonPointer instance
        JsonPointer pointer1 = new JsonPointer();
        JsonPointer pointer2 = new JsonPointer();

        // test the equals() method
        assertTrue(pointer1.equals(pointer2));
    }
    
}
