package com.fasterxml.jackson.core;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class JsonPointerTest {
    private JsonPointer pointer;
    
    @Test
    public void testMatches() {
        pointer = new JsonPointer();
        assertTrue(pointer.matches());
        
        pointer = new JsonPointer("test", "test", null);
        assertFalse(pointer.matches());
    }
    
    @Test
    public void testGetMatchingProperty() {
        pointer = new JsonPointer();
        assertEquals("", pointer.getMatchingProperty());
        
        pointer = new JsonPointer("test", "test", null);
        assertEquals("test", pointer.getMatchingProperty());
    }
    
    @Test
    public void testGetMatchingIndex() {
        pointer = new JsonPointer();
        assertEquals(-1, pointer.getMatchingIndex());

        pointer = new JsonPointer("test", "test", null);
        assertEquals(-1, pointer.getMatchingIndex());
    }
    
    @Test
    public void testMayMatchProperty() {
        pointer = new JsonPointer();
        assertFalse(pointer.mayMatchProperty());

        pointer = new JsonPointer("test", "test", null);
        assertTrue(pointer.mayMatchProperty());
    }
    
    @Test
    public void testMayMatchElement() {
        pointer = new JsonPointer();
        assertFalse(pointer.mayMatchElement());

        pointer = new JsonPointer("test", "test", null);
        assertFalse(pointer.mayMatchElement());

        pointer = new JsonPointer("test", "1", null);
        assertTrue(pointer.mayMatchElement());
    }
    
    @Test
    public void testMatchProperty() {
        pointer = new JsonPointer("test", "test", null);
        JsonPointer result = pointer.matchProperty("test");
        assertEquals("test", result.toString());

        result = pointer.matchProperty("notMatching");
        assertNull(result);
    }
    
    @Test
    public void testMatchElement() {
        pointer = new JsonPointer("test", "1", null);
        JsonPointer result = pointer.matchElement(1);
        
        assertEquals("1", result.toString());
        
        result = pointer.matchElement(2);
        assertNull(result);

        result = pointer.matchElement(-1);
        assertNull(result);
    }
    
    @Test
    public void testTail() {
        pointer = new JsonPointer("test", "test", null);
        JsonPointer result = pointer.tail();
        
        assertNull(result);
        
        pointer = new JsonPointer("test", "1", null);
        result = pointer.tail();
        assertEquals("1", result.toString());
    }
    
    @Test
    public void testToString() {
        pointer = new JsonPointer();
        assertEquals("", pointer.toString());

        pointer = new JsonPointer("test", "test", null);
        assertEquals("test", pointer.toString());
    }
    
    @Test
    public void testHashCode() {
        pointer = new JsonPointer();
        int hashCode = pointer.hashCode();

        assertEquals(hashCode, pointer.hashCode());

        pointer = new JsonPointer("test", "test", null);
        hashCode = pointer.hashCode();

        assertEquals(hashCode, pointer.hashCode());
    }
    
    @Test
    public void testEquals() {
        pointer = new JsonPointer();
        assertTrue(pointer.equals(pointer));

        pointer = new JsonPointer("test", "test", null);
        assertFalse(pointer.equals(null));
        assertFalse(pointer.equals("test"));
        assertTrue(pointer.equals(pointer));

        JsonPointer pointer2 = new JsonPointer("test", "test", null);
        assertTrue(pointer.equals(pointer2));
    }
}