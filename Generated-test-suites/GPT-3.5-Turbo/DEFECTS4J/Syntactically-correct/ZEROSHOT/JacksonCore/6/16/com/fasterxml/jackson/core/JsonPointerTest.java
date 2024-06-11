package com.fasterxml.jackson.core;

import org.junit.Test;
import static org.junit.Assert.*;

public class JsonPointerTest {

    @Test
    public void testEmptyPointer() {
        JsonPointer pointer = JsonPointer.EMPTY;
        
        assertTrue(pointer.matches());
        assertEquals("", pointer.getMatchingProperty());
        assertEquals(-1, pointer.getMatchingIndex());
        assertNull(pointer.tail());
    }
    
    @Test
    public void testCompile() {
        JsonPointer pointer = JsonPointer.compile("/foo/bar");
        
        assertFalse(pointer.matches());
        assertEquals("foo", pointer.getMatchingProperty());
        assertEquals(-1, pointer.getMatchingIndex());
        
        JsonPointer tail = pointer.tail();
        assertNotNull(tail);
        assertFalse(tail.matches());
        assertEquals("bar", tail.getMatchingProperty());
        assertEquals(-1, tail.getMatchingIndex());
        assertNull(tail.tail());
    }
    
    @Test
    public void testValueOf() {
        JsonPointer pointer = JsonPointer.valueOf("/baz/qux");
        
        assertFalse(pointer.matches());
        assertEquals("baz", pointer.getMatchingProperty());
        assertEquals(-1, pointer.getMatchingIndex());
        
        JsonPointer tail = pointer.tail();
        assertNotNull(tail);
        assertFalse(tail.matches());
        assertEquals("qux", tail.getMatchingProperty());
        assertEquals(-1, tail.getMatchingIndex());
        assertNull(tail.tail());
    }
    
    @Test
    public void testMatchProperty() {
        JsonPointer pointer = JsonPointer.compile("/foo");
        
        assertFalse(pointer.matches());
        assertEquals("foo", pointer.getMatchingProperty());
        assertEquals(-1, pointer.getMatchingIndex());
        
        JsonPointer match = pointer.matchProperty("foo");
        assertNotNull(match);
        assertTrue(match.matches());
        assertEquals("", match.getMatchingProperty());
        assertEquals(-1, match.getMatchingIndex());
    }
    
    @Test
    public void testMatchElement() {
        JsonPointer pointer = JsonPointer.compile("/3");
        
        assertFalse(pointer.matches());
        assertEquals("", pointer.getMatchingProperty());
        assertEquals(3, pointer.getMatchingIndex());
        
        JsonPointer match = pointer.matchElement(3);
        assertNotNull(match);
        assertTrue(match.matches());
        assertEquals("", match.getMatchingProperty());
        assertEquals(-1, match.getMatchingIndex());
    }
    
    @Test
    public void testToString() {
        JsonPointer pointer = JsonPointer.compile("/foo/bar/baz");
        
        assertEquals("/foo/bar/baz", pointer.toString());
    }
    
    @Test
    public void testHashCode() {
        JsonPointer pointer1 = JsonPointer.compile("/foo");
        JsonPointer pointer2 = JsonPointer.compile("/foo");
        
        assertEquals(pointer1.hashCode(), pointer2.hashCode());
    }
    
    @Test
    public void testEquals() {
        JsonPointer pointer1 = JsonPointer.compile("/foo");
        JsonPointer pointer2 = JsonPointer.compile("/foo");
        JsonPointer pointer3 = JsonPointer.compile("/bar");
        
        assertEquals(pointer1, pointer2);
        assertNotEquals(pointer1, pointer3);
    }
}