package com.fasterxml.jackson.core;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class JsonPointerTest {
    @Test
    public void testMatches() {
        JsonPointer pointer = new JsonPointer();
        assertEquals(true, pointer.matches());
        
        pointer = new JsonPointer("1", "segment", null);
        assertEquals(false, pointer.matches());
    }

    @Test
    public void testGetMatchingProperty() {
        JsonPointer pointer = new JsonPointer();
        assertEquals("", pointer.getMatchingProperty());
        
        pointer = new JsonPointer("1", "segment", null);
        assertEquals("segment", pointer.getMatchingProperty());
    }

    @Test
    public void testGetMatchingIndex() {
        JsonPointer pointer = new JsonPointer();
        assertEquals(-1, pointer.getMatchingIndex());
        
        pointer = new JsonPointer("1", "segment", null);
        assertEquals(1, pointer.getMatchingIndex());
    }

    @Test
    public void testMayMatchProperty() {
        JsonPointer pointer = new JsonPointer();
        assertEquals(true, pointer.mayMatchProperty());
        
        pointer = new JsonPointer("1", "segment", null);
        assertEquals(true, pointer.mayMatchProperty());
    }

    @Test
    public void testMayMatchElement() {
        JsonPointer pointer = new JsonPointer();
        assertEquals(false, pointer.mayMatchElement());
        
        pointer = new JsonPointer("1", "segment", null);
        assertEquals(false, pointer.mayMatchElement());
    }

    @Test
    public void testMatchProperty() {
        JsonPointer pointer = new JsonPointer();
        assertNull(pointer.matchProperty(""));

        pointer = new JsonPointer("1", "segment", null);
        assertEquals(null, pointer.matchProperty("property"));
        assertEquals(pointer, pointer.matchProperty("segment"));
    }

    @Test
    public void testMatchElement() {
        JsonPointer pointer = new JsonPointer();
        assertNull(pointer.matchElement(1));

        pointer = new JsonPointer("1", "segment", null);
        assertEquals(null, pointer.matchElement(0));
        assertEquals(pointer, pointer.matchElement(1));
    }

    @Test
    public void testTail() {
        JsonPointer pointer = new JsonPointer();
        assertEquals(null, pointer.tail());

        pointer = new JsonPointer("1", "segment", null);
        assertEquals(null, pointer.tail());
    }

    @Test
    public void testToString() {
        JsonPointer pointer = new JsonPointer();
        assertEquals("", pointer.toString());

        pointer = new JsonPointer("1", "segment", null);
        assertEquals("/", pointer.toString());
    }

    @Test
    public void testHashCode() {
        JsonPointer pointer = new JsonPointer();
        assertEquals("".hashCode(), pointer.hashCode());

        pointer = new JsonPointer("1", "segment", null);
        assertEquals("/".hashCode(), pointer.hashCode());
    }

    @Test
    public void testEquals() {
        JsonPointer pointer = new JsonPointer();
        assertEquals(true, pointer.equals(pointer));

        pointer = new JsonPointer("1", "segment", null);
        assertEquals(false, pointer.equals(null));
        assertEquals(false, pointer.equals("pointer"));
        assertEquals(true, pointer.equals(pointer));
        assertEquals(true, pointer.equals(new JsonPointer("1", "segment", null)));
    }
}