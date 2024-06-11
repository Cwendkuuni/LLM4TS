package com.fasterxml.jackson.core;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class JsonPointerTest {
    @Test
    public void testMatches() {
        JsonPointer pointer = new JsonPointer();
        assertTrue(pointer.matches());
        
        pointer = new JsonPointer("foo", "", null);
        assertFalse(pointer.matches());
    }
    
    @Test
    public void testGetMatchingProperty() {
        JsonPointer pointer = new JsonPointer();
        assertEquals("", pointer.getMatchingProperty());
        
        pointer = new JsonPointer("foo", "bar", null);
        assertEquals("bar", pointer.getMatchingProperty());
    }
    
    @Test
    public void testGetMatchingIndex() {
        JsonPointer pointer = new JsonPointer();
        assertEquals(-1, pointer.getMatchingIndex());
        
        pointer = new JsonPointer("foo", "5", null);
        assertEquals(5, pointer.getMatchingIndex());
    }
    
    @Test
    public void testMayMatchProperty() {
        JsonPointer pointer = new JsonPointer();
        assertTrue(pointer.mayMatchProperty());
        
        pointer = new JsonPointer("foo", "bar", null);
        assertTrue(pointer.mayMatchProperty());
    }
    
    @Test
    public void testMayMatchElement() {
        JsonPointer pointer = new JsonPointer();
        assertFalse(pointer.mayMatchElement());
        
        pointer = new JsonPointer("foo", "5", null);
        assertTrue(pointer.mayMatchElement());
    }
    
    @Test
    public void testMatchProperty() {
        JsonPointer pointer = new JsonPointer();
        assertNull(pointer.matchProperty("foo"));
        
        JsonPointer next = new JsonPointer("foo", "bar", null);
        pointer = new JsonPointer("foo", "", next);
        assertEquals(next, pointer.matchProperty("bar"));
        assertNull(pointer.matchProperty("baz"));
    }
    
    @Test
    public void testMatchElement() {
        JsonPointer pointer = new JsonPointer();
        assertNull(pointer.matchElement(1));
        
        JsonPointer next = new JsonPointer("foo", "5", null);
        pointer = new JsonPointer("foo", "", next);
        assertEquals(next, pointer.matchElement(5));
        assertNull(pointer.matchElement(10));
    }
    
    @Test
    public void testTail() {
        JsonPointer pointer = new JsonPointer();
        assertNull(pointer.tail());
        
        JsonPointer next = new JsonPointer("foo", "bar", null);
        pointer = new JsonPointer("foo", "", next);
        assertEquals(next, pointer.tail());
    }
    
    @Test
    public void testToString() {
        JsonPointer pointer = new JsonPointer();
        assertEquals("", pointer.toString());
        
        pointer = new JsonPointer("foo", "bar", null);
        assertEquals("/bar", pointer.toString());
    }
    
    @Test
    public void testHashCode() {
        JsonPointer pointer1 = new JsonPointer();
        JsonPointer pointer2 = new JsonPointer();
        assertEquals(pointer1.hashCode(), pointer2.hashCode());
        
        pointer1 = new JsonPointer("foo", "bar", null);
        pointer2 = new JsonPointer("foo", "bar", null);
        assertEquals(pointer1.hashCode(), pointer2.hashCode());
        
        pointer2 = new JsonPointer("foo", "baz", null);
        assertNotEquals(pointer1.hashCode(), pointer2.hashCode());
    }
    
    @Test
    public void testEquals() {
        JsonPointer pointer1 = new JsonPointer();
        JsonPointer pointer2 = new JsonPointer();
        assertTrue(pointer1.equals(pointer2));
        
        pointer1 = new JsonPointer("foo", "bar", null);
        pointer2 = new JsonPointer("foo", "bar", null);
        assertTrue(pointer1.equals(pointer2));
        
        pointer2 = new JsonPointer("foo", "baz", null);
        assertFalse(pointer1.equals(pointer2));
        
        assertFalse(pointer1.equals(null));
        assertFalse(pointer1.equals(""));
    }
}