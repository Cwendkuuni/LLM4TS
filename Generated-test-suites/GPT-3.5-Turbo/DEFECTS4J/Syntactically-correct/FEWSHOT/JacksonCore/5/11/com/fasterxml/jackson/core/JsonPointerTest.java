package com.fasterxml.jackson.core;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class JsonPointerTest {
    
    @Test
    public void testMatches() {
        JsonPointer pointer = JsonPointer.compile("/foo");
        assertEquals(false, pointer.matches());
        
        pointer = JsonPointer.compile("/");
        assertEquals(true, pointer.matches());
    }
    
    @Test
    public void testGetMatchingProperty() {
        JsonPointer pointer = JsonPointer.compile("/foo");
        assertEquals("foo", pointer.getMatchingProperty());
        
        pointer = JsonPointer.compile("/");
        assertNull(pointer.getMatchingProperty());
    }
    
    @Test
    public void testGetMatchingIndex() {
        JsonPointer pointer = JsonPointer.compile("/0");
        assertEquals(0, pointer.getMatchingIndex());
        
        pointer = JsonPointer.compile("/");
        assertEquals(-1, pointer.getMatchingIndex());
    }
    
    @Test
    public void testMayMatchProperty() {
        JsonPointer pointer = JsonPointer.compile("/foo");
        assertEquals(true, pointer.mayMatchProperty());
        
        pointer = JsonPointer.compile("/0");
        assertEquals(false, pointer.mayMatchProperty());
    }
    
    @Test
    public void testMayMatchElement() {
        JsonPointer pointer = JsonPointer.compile("/foo");
        assertEquals(false, pointer.mayMatchElement());
        
        pointer = JsonPointer.compile("/0");
        assertEquals(true, pointer.mayMatchElement());
    }
    
    @Test
    public void testMatchProperty() {
        JsonPointer pointer = JsonPointer.compile("/foo");
        assertEquals(null, pointer.matchProperty("bar"));
        
        pointer = JsonPointer.compile("/foo");
        assertEquals(pointer.tail(), pointer.matchProperty("foo"));
    }
    
    @Test
    public void testMatchElement() {
        JsonPointer pointer = JsonPointer.compile("/0");
        assertEquals(null, pointer.matchElement(1));
        
        pointer = JsonPointer.compile("/0");
        assertEquals(pointer.tail(), pointer.matchElement(0));
    }
    
    @Test
    public void testTail() {
        JsonPointer pointer = JsonPointer.compile("/foo/bar");
        assertEquals(JsonPointer.compile("/bar"), pointer.tail());
        
        pointer = JsonPointer.compile("/foo");
        assertEquals(JsonPointer.EMPTY, pointer.tail());
    }
    
}