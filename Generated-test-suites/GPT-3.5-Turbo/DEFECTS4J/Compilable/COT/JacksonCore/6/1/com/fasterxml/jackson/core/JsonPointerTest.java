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
        
        pointer = new JsonPointer("test", "segment", null);
        assertEquals(-1, pointer.getMatchingIndex());
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
        
        pointer = new JsonPointer("test", "segment", null);
        assertFalse(pointer.mayMatchElement());
    }
    
    @Test
    public void testMatchProperty() {
        JsonPointer pointer = new JsonPointer("test", "segment", null);
        assertNull(pointer.matchProperty("invalid"));
        
        JsonPointer next = pointer.matchProperty("segment");
        assertNotNull(next);
        assertEquals(pointer.tail(), next);
    }
    
    @Test
    public void testMatchElement() {
        JsonPointer pointer = new JsonPointer("test", "segment", null);
        assertNull(pointer.matchElement(1));
        
        JsonPointer next = pointer.matchElement(-1);
        assertNull(next);
        
        next = pointer.matchElement(0);
        assertNull(next);
    }
    
    @Test
    public void testTail() {
        JsonPointer pointer = new JsonPointer("test", "segment", null);
        assertNull(pointer.tail());
    }
    
    @Test
    public void testToString() {
        JsonPointer pointer = new JsonPointer();
        assertEquals("", pointer.toString());
        
        pointer = new JsonPointer("test", "segment", null);
        assertEquals("/segment", pointer.toString());
    }
    
    @Test
    public void testHashCode() {
        JsonPointer pointer1 = new JsonPointer();
        JsonPointer pointer2 = new JsonPointer();
        assertEquals(pointer1.hashCode(), pointer2.hashCode());
        
        pointer1 = new JsonPointer("test", "segment", null);
        pointer2 = new JsonPointer("test", "segment", null);
        assertEquals(pointer1.hashCode(), pointer2.hashCode());
    }
    
    @Test
    public void testEquals() {
        JsonPointer pointer1 = new JsonPointer();
        JsonPointer pointer2 = new JsonPointer();
        assertTrue(pointer1.equals(pointer2));
        assertTrue(pointer2.equals(pointer1));
        
        pointer1 = new JsonPointer("test", "segment", null);
        pointer2 = new JsonPointer("test", "segment", null);
        assertTrue(pointer1.equals(pointer2));
        assertTrue(pointer2.equals(pointer1));
        
        pointer1 = new JsonPointer("test", "segment", null);
        pointer2 = new JsonPointer("test", "othersegment", null);
        assertFalse(pointer1.equals(pointer2));
        assertFalse(pointer2.equals(pointer1));
        
        assertFalse(pointer1.equals(null));
        assertFalse(pointer1.equals("test"));
    }
    
}
