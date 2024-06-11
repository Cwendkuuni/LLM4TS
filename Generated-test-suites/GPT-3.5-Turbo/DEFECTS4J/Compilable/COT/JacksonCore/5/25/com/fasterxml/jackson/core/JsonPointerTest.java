package com.fasterxml.jackson.core;

import org.junit.Test;
import static org.junit.Assert.*;

public class JsonPointerTest {
    @Test
    public void testMatches() {
        JsonPointer pointer1 = new JsonPointer();
        assertTrue(pointer1.matches());
        
        JsonPointer pointer2 = new JsonPointer("test", "segment", null);
        assertFalse(pointer2.matches());
    }
    
    @Test
    public void testGetMatchingProperty() {
        JsonPointer pointer = new JsonPointer("test", "segment", null);
        assertEquals("segment", pointer.getMatchingProperty());
    }
    
    @Test
    public void testGetMatchingIndex() {
        JsonPointer pointer = new JsonPointer("test", "0", null);
        assertEquals(0, pointer.getMatchingIndex());
    }
    
    @Test
    public void testMayMatchProperty() {
        JsonPointer pointer1 = new JsonPointer("test", "segment", null);
        assertTrue(pointer1.mayMatchProperty());
        
        JsonPointer pointer2 = new JsonPointer("test", "0", null);
        assertFalse(pointer2.mayMatchProperty());
    }
    
    @Test
    public void testMayMatchElement() {
        JsonPointer pointer1 = new JsonPointer("test", "segment", null);
        assertFalse(pointer1.mayMatchElement());
        
        JsonPointer pointer2 = new JsonPointer("test", "0", null);
        assertTrue(pointer2.mayMatchElement());
    }
    
    @Test
    public void testMatchProperty() {
        JsonPointer pointer1 = new JsonPointer("test", "segment", null);
        assertNull(pointer1.matchProperty("otherSegment"));
        assertNotNull(pointer1.matchProperty("segment"));
        
        JsonPointer pointer2 = new JsonPointer("test", "1", null);
        assertNull(pointer2.matchProperty("0"));
    }
    
    @Test
    public void testMatchElement() {
        JsonPointer pointer1 = new JsonPointer("test", "segment", null);
        assertNull(pointer1.matchElement(1));
        assertNotNull(pointer1.matchElement(0));
        
        JsonPointer pointer2 = new JsonPointer("test", "0", null);
        assertNull(pointer2.matchElement(1));
        assertNotNull(pointer2.matchElement(0));
    }
    
    @Test
    public void testTail() {
        JsonPointer pointer1 = new JsonPointer("test", "segment", null);
        assertNull(pointer1.tail());
        
        JsonPointer pointer2 = new JsonPointer("test", "1", new JsonPointer());
        assertNotNull(pointer2.tail());
    }
    
    @Test
    public void testToString() {
        JsonPointer pointer = new JsonPointer("test", "segment", null);
        assertEquals("test", pointer.toString());
    }
    
    @Test
    public void testHashCode() {
        JsonPointer pointer = new JsonPointer("test", "segment", null);
        assertNotNull(pointer.hashCode());
    }
    
    @Test
    public void testEquals() {
        JsonPointer pointer1 = new JsonPointer("test", "segment", null);
        assertTrue(pointer1.equals(pointer1));
        
        JsonPointer pointer2 = new JsonPointer("test", "segment", null);
        assertTrue(pointer1.equals(pointer2) && pointer2.equals(pointer1));
        
        JsonPointer pointer3 = new JsonPointer("test", "otherSegment", null);
        assertFalse(pointer1.equals(pointer3));
    }
}
