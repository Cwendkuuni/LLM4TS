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
        JsonPointer pointer = new JsonPointer();
        
        assertNull(pointer.matchProperty(""));
    }
    
    @Test
    public void testMatchElement() {
        JsonPointer pointer = new JsonPointer();
        
        assertNull(pointer.matchElement(0));
    }
    
    @Test
    public void testTail() {
        JsonPointer pointer = new JsonPointer();
        
        assertEquals(null, pointer.tail());
    }
    
    @Test
    public void testCompile() {
        JsonPointer pointer = new JsonPointer();
        
        assertEquals(JsonPointer.EMPTY, pointer.compile(""));
    }
    
    @Test
    public void testValueOf() {
        JsonPointer pointer = new JsonPointer();
        
        assertEquals(JsonPointer.EMPTY, pointer.valueOf(""));
    }
    
    @Test
    public void testToString() {
        JsonPointer pointer = new JsonPointer();
        
        assertEquals("", pointer.toString());
    }
    
    @Test
    public void testHashCode() {
        JsonPointer pointer = new JsonPointer();
        
        assertEquals(0, pointer.hashCode());
    }
    
    @Test
    public void testEquals() {
        JsonPointer pointer1 = new JsonPointer();
        JsonPointer pointer2 = new JsonPointer();
        
        assertEquals(true, pointer1.equals(pointer2));
    }
}