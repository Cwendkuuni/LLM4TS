package com.fasterxml.jackson.core;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class JsonPointerTest {
    @Test
    public void testMatches() {
        JsonPointer pointer = new JsonPointer();
        assertEquals(true, pointer.matches());
    }
    
    @Test
    public void testGetMatchingProperty() {
        JsonPointer pointer = new JsonPointer();
        assertEquals("", pointer.getMatchingProperty());
    }
    
    @Test
    public void testGetMatchingIndex() {
        JsonPointer pointer = new JsonPointer();
        assertEquals(-1, pointer.getMatchingIndex());
    }
    
    @Test
    public void testMayMatchProperty() {
        JsonPointer pointer = new JsonPointer();
        assertEquals(true, pointer.mayMatchProperty());
    }
    
    @Test
    public void testMayMatchElement() {
        JsonPointer pointer = new JsonPointer();
        assertEquals(false, pointer.mayMatchElement());
    }
    
    @Test
    public void testMatchProperty() {
        String name = "name";
        JsonPointer pointer = new JsonPointer();
        assertNull(pointer.matchProperty(name));
    }
    
    @Test
    public void testMatchElement() {
        int index = 5;
        JsonPointer pointer = new JsonPointer();
        assertNull(pointer.matchElement(index));
    }
    
    @Test
    public void testTail() {
        JsonPointer pointer = new JsonPointer();
        assertEquals(pointer, pointer.tail());
    }
}