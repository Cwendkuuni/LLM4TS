package com.fasterxml.jackson.core;

import org.junit.Test;
import static org.junit.Assert.*;

public class JsonPointerTest {

   @Test
    public void testMatches() {
        JsonPointer empty = new JsonPointer();
        assertTrue(empty.matches());
    }
    
   @Test
    public void testGetMatchingProperty() {
        JsonPointer empty = new JsonPointer();
        assertEquals("", empty.getMatchingProperty());
    }
    
   @Test
    public void testGetMatchingIndex() {
        JsonPointer empty = new JsonPointer();
        assertEquals(-1, empty.getMatchingIndex());
    }
    
   @Test
    public void testMayMatchProperty() {
        JsonPointer empty = new JsonPointer();
        assertTrue(empty.mayMatchProperty());
    }
    
   @Test
    public void testMayMatchElement() {
        JsonPointer empty = new JsonPointer();
        assertFalse(empty.mayMatchElement());
    }
    
   @Test
    public void testMatchProperty() {
        JsonPointer empty = new JsonPointer();
        assertNull(empty.matchProperty("test"));
    }
    
   @Test
    public void testMatchElement() {
        JsonPointer empty = new JsonPointer();
        assertNull(empty.matchElement(0));
    }
    
   @Test
    public void testTail() {
        JsonPointer empty = new JsonPointer();
        assertSame(empty, empty.tail());
    }
    
   @Test
    public void testToString() {
        JsonPointer empty = new JsonPointer();
        assertEquals("", empty.toString());
    }
    
   @Test
    public void testHashCode() {
        JsonPointer empty = new JsonPointer();
        assertEquals(0, empty.hashCode());
    }
    
   @Test
    public void testEquals() {
        JsonPointer empty = new JsonPointer();
        assertTrue(empty.equals(empty));
        assertFalse(empty.equals(null));
        assertFalse(empty.equals(""));
        assertFalse(empty.equals(new JsonPointer()));
    }

}