package com.fasterxml.jackson.core;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class JsonPointerTest {
    
    @Test
    public void testMatches() {
        JsonPointer pointer = JsonPointer.EMPTY;
        assertTrue(pointer.matches());
        
        pointer = new JsonPointer("/foo");
        assertFalse(pointer.matches());
    }
    
    @Test
    public void testGetMatchingProperty() {
        JsonPointer pointer = JsonPointer.EMPTY;
        assertEquals("", pointer.getMatchingProperty());
        
        pointer = new JsonPointer("/foo");
        assertEquals("foo", pointer.getMatchingProperty());
    }
    
    @Test
    public void testGetMatchingIndex() {
        JsonPointer pointer = JsonPointer.EMPTY;
        assertEquals(-1, pointer.getMatchingIndex());
        
        pointer = new JsonPointer("/0");
        assertEquals(0, pointer.getMatchingIndex());
    }
    
    @Test
    public void testMayMatchProperty() {
        JsonPointer pointer = JsonPointer.EMPTY;
        assertTrue(pointer.mayMatchProperty());
        
        pointer = new JsonPointer("/foo");
        assertTrue(pointer.mayMatchProperty());
        
        pointer = new JsonPointer("/0");
        assertFalse(pointer.mayMatchProperty());
    }
    
    @Test
    public void testMayMatchElement() {
        JsonPointer pointer = JsonPointer.EMPTY;
        assertFalse(pointer.mayMatchElement());
        
        pointer = new JsonPointer("/foo");
        assertFalse(pointer.mayMatchElement());
        
        pointer = new JsonPointer("/0");
        assertTrue(pointer.mayMatchElement());
    }
    
    @Test
    public void testMatchProperty() {
        JsonPointer pointer = JsonPointer.EMPTY;
        assertNull(pointer.matchProperty("foo"));
        
        pointer = new JsonPointer("/foo");
        JsonPointer next = pointer.matchProperty("foo");
        assertEquals("foo", next.getMatchingProperty());
    }
    
    @Test
    public void testMatchElement() {
        JsonPointer pointer = new JsonPointer("/0");
        JsonPointer next = pointer.matchElement(0);
        assertEquals(0, next.getMatchingIndex());
    }
    
    @Test
    public void testTail() {
        JsonPointer pointer = new JsonPointer("/foo/bar");
        JsonPointer tail = pointer.tail();
        assertEquals("bar", tail.getMatchingProperty());
    }
    
    @Test
    public void testToString() {
        JsonPointer pointer = JsonPointer.EMPTY;
        assertEquals("", pointer.toString());
        
        pointer = new JsonPointer("/foo/bar");
        assertEquals("/foo/bar", pointer.toString());
    }
    
    @Test
    public void testHashCode() {
        JsonPointer pointer1 = JsonPointer.EMPTY;
        JsonPointer pointer2 = JsonPointer.EMPTY;
        assertTrue(pointer1.hashCode() == pointer2.hashCode());
        
        pointer1 = new JsonPointer("/foo");
        pointer2 = new JsonPointer("/foo");
        assertTrue(pointer1.hashCode() == pointer2.hashCode());
    }
    
    @Test
    public void testEquals() {
        JsonPointer pointer1 = JsonPointer.EMPTY;
        JsonPointer pointer2 = JsonPointer.EMPTY;
        assertTrue(pointer1.equals(pointer2));
        
        pointer1 = new JsonPointer("/foo");
        pointer2 = new JsonPointer("/foo");
        assertTrue(pointer1.equals(pointer2));
    }
    
    @Test
    public void testCompile() {
        JsonPointer pointer = JsonPointer.compile("");
        assertEquals("", pointer.toString());
        
        pointer = JsonPointer.compile("/foo/bar");
        assertEquals("/foo/bar", pointer.toString());
        
        pointer = JsonPointer.compile("/foo~1bar");
        assertEquals("/foo~1bar", pointer.toString());
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testCompileInvalid() {
        JsonPointer.compile("foo");
    }
    
    @Test
    public void testValueOf() {
        JsonPointer pointer = JsonPointer.valueOf("");
        assertEquals("", pointer.toString());
        
        pointer = JsonPointer.valueOf("/foo/bar");
        assertEquals("/foo/bar", pointer.toString());
        
        pointer = JsonPointer.valueOf("/foo~1bar");
        assertEquals("/foo~1bar", pointer.toString());
    }
}