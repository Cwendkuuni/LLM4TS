package com.fasterxml.jackson.core;

import org.junit.Test;
import static org.junit.Assert.*;

public class JsonPointerTest {

    @Test
    public void testMatches() {
        JsonPointer pointer = new JsonPointer();
        assertTrue(pointer.matches());
        
        pointer = new JsonPointer("test", "segment", null);
        assertFalse(pointer.matches());
    }

    @Test
    public void testGetMatchingProperty() {
        JsonPointer pointer = new JsonPointer();
        assertEquals("", pointer.getMatchingProperty());
        
        pointer = new JsonPointer("test", "segment", null);
        assertEquals("segment", pointer.getMatchingProperty());
    }

    @Test
    public void testGetMatchingIndex() {
        JsonPointer pointer = new JsonPointer();
        assertEquals(-1, pointer.getMatchingIndex());
        
        pointer = new JsonPointer("test", "0", null);
        assertEquals(0, pointer.getMatchingIndex());
    }

    @Test
    public void testMayMatchProperty() {
        JsonPointer pointer = new JsonPointer();
        assertFalse(pointer.mayMatchProperty());
        
        pointer = new JsonPointer("test", "segment", null);
        assertTrue(pointer.mayMatchProperty());
    }

    @Test
    public void testMayMatchElement() {
        JsonPointer pointer = new JsonPointer();
        assertFalse(pointer.mayMatchElement());
        
        pointer = new JsonPointer("test", "0", null);
        assertTrue(pointer.mayMatchElement());
    }

    @Test
    public void testMatchProperty() {
        JsonPointer pointer = new JsonPointer("test", "segment", new JsonPointer());

        assertNull(pointer.matchProperty("foo"));
        assertNotNull(pointer.matchProperty("segment"));
    }

    @Test
    public void testMatchElement() {
        JsonPointer pointer = new JsonPointer("test", "0", new JsonPointer());

        assertNull(pointer.matchElement(1));
        assertNotNull(pointer.matchElement(0));
    }

    @Test
    public void testTail() {
        JsonPointer pointer = new JsonPointer("test", "segment", new JsonPointer());

        assertNotNull(pointer.tail());
    }

    @Test
    public void testToString() {
        JsonPointer pointer = new JsonPointer("test", "segment", new JsonPointer());

        assertEquals("test", pointer.toString());
    }

    @Test
    public void testHashCode() {
        JsonPointer pointer1 = new JsonPointer();
        JsonPointer pointer2 = new JsonPointer();

        assertEquals(pointer1.hashCode(), pointer2.hashCode());
    }

    @Test
    public void testEquals() {
        JsonPointer pointer1 = new JsonPointer();
        JsonPointer pointer2 = new JsonPointer();

        assertTrue(pointer1.equals(pointer2));
    }
}