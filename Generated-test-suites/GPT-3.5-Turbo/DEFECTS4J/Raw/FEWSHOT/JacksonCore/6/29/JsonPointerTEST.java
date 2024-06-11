package com.fasterxml.jackson.core;

import static org.junit.Assert.*;

import org.junit.Test;

public class JsonPointerTest {
    @Test
    public void testMatches() {
        JsonPointer pointer = JsonPointer.EMPTY;
        assertTrue(pointer.matches());
        
        pointer = new JsonPointer("foo", "bar", JsonPointer.EMPTY);
        assertFalse(pointer.matches());
    }
    
    @Test
    public void testGetMatchingProperty() {
        JsonPointer pointer = JsonPointer.EMPTY;
        assertEquals("", pointer.getMatchingProperty());
        
        pointer = new JsonPointer("foo", "bar", JsonPointer.EMPTY);
        assertEquals("bar", pointer.getMatchingProperty());
    }
    
    @Test
    public void testGetMatchingIndex() {
        JsonPointer pointer = JsonPointer.EMPTY;
        assertEquals(-1, pointer.getMatchingIndex());
        
        pointer = new JsonPointer("foo", "0", JsonPointer.EMPTY);
        assertEquals(0, pointer.getMatchingIndex());
    }
    
    @Test
    public void testMayMatchProperty() {
        JsonPointer pointer = JsonPointer.EMPTY;
        assertTrue(pointer.mayMatchProperty());
        
        pointer = new JsonPointer("foo", "bar", JsonPointer.EMPTY);
        assertTrue(pointer.mayMatchProperty());
    }
    
    @Test
    public void testMayMatchElement() {
        JsonPointer pointer = JsonPointer.EMPTY;
        assertFalse(pointer.mayMatchElement());
        
        pointer = new JsonPointer("foo", "0", JsonPointer.EMPTY);
        assertTrue(pointer.mayMatchElement());
    }
    
    @Test
    public void testMatchProperty() {
        JsonPointer pointer = JsonPointer.EMPTY;
        assertNull(pointer.matchProperty("bar"));
        
        pointer = new JsonPointer("foo", "bar", JsonPointer.EMPTY);
        assertEquals(JsonPointer.EMPTY, pointer.matchProperty("bar"));
    }
    
    @Test
    public void testMatchElement() {
        JsonPointer pointer = JsonPointer.EMPTY;
        assertNull(pointer.matchElement(0));
        
        pointer = new JsonPointer("foo", "0", JsonPointer.EMPTY);
        assertEquals(JsonPointer.EMPTY, pointer.matchElement(0));
    }
    
    @Test
    public void testTail() {
       JsonPointer pointer = new JsonPointer("foo", "bar", JsonPointer.EMPTY);
       assertEquals(JsonPointer.EMPTY, pointer.tail());
    }
    
    @Test
    public void testToString() {
        JsonPointer pointer = JsonPointer.EMPTY;
        assertEquals("", pointer.toString());
        
        pointer = new JsonPointer("foo", "bar", JsonPointer.EMPTY);
        assertEquals("/bar", pointer.toString());
    }
    
    @Test
    public void testHashCode() {
        JsonPointer pointer1 = JsonPointer.EMPTY;
        JsonPointer pointer2 = new JsonPointer("foo", "bar", JsonPointer.EMPTY);
        
        assertEquals(pointer1.hashCode(), pointer1.hashCode());
        assertNotEquals(pointer1.hashCode(), pointer2.hashCode());
    }
    
    @Test
    public void testEquals() {
        JsonPointer pointer1 = JsonPointer.EMPTY;
        JsonPointer pointer2 = new JsonPointer("foo", "bar", JsonPointer.EMPTY);
        JsonPointer pointer3 = new JsonPointer("foo", "bar", JsonPointer.EMPTY);
        
        assertTrue(pointer1.equals(pointer1));
        assertFalse(pointer1.equals(pointer2));
        assertTrue(pointer2.equals(pointer3));
    }
}