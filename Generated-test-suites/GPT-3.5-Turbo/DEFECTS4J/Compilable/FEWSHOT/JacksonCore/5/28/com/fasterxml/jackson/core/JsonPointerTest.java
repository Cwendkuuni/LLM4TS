package com.fasterxml.jackson.core;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class JsonPointerTest {
    
    @Test
    public void testEmptyConstructor() {
        JsonPointer pointer = new JsonPointer();
        
        assertTrue(pointer.matches());
        assertEquals("", pointer.getMatchingProperty());
        assertEquals(-1, pointer.getMatchingIndex());
        assertNull(pointer.tail());
    }
    
    @Test
    public void testConstructorWithArgs() {
        String fullString = "/foo/bar";
        String segment = "foo";
        JsonPointer next = new JsonPointer();
        
        JsonPointer pointer = new JsonPointer(fullString, segment, next);
        
        assertFalse(pointer.matches());
        assertEquals(segment, pointer.getMatchingProperty());
        assertEquals(-1, pointer.getMatchingIndex());
        assertEquals(next, pointer.tail());
    }
    
    @Test
    public void testCompile() {
        String input = "/foo/bar";
        
        JsonPointer pointer = JsonPointer.compile(input);
        
        assertFalse(pointer.matches());
        assertEquals("foo", pointer.getMatchingProperty());
        assertEquals(-1, pointer.getMatchingIndex());
        assertEquals("/bar", pointer.tail().toString());
    }
    
    @Test(expected=IllegalArgumentException.class)
    public void testCompileWithInvalidInput() {
        String input = "foo/bar";
        
        JsonPointer.compile(input);
    }
    
    @Test
    public void testValueOf() {
        String input = "/foo/bar";
        
        JsonPointer pointer = JsonPointer.valueOf(input);
        
        assertFalse(pointer.matches());
        assertEquals("foo", pointer.getMatchingProperty());
        assertEquals(-1, pointer.getMatchingIndex());
        assertEquals("/bar", pointer.tail().toString());
    }
    
    @Test
    public void testMatches() {
        JsonPointer pointer = new JsonPointer();
        
        assertTrue(pointer.matches());
        
        pointer = new JsonPointer("/foo/bar", "foo", new JsonPointer());
        
        assertFalse(pointer.matches());
    }
    
    @Test
    public void testGetMatchingProperty() {
        JsonPointer pointer = new JsonPointer();
        
        assertEquals("", pointer.getMatchingProperty());
        
        pointer = new JsonPointer("/foo/bar", "foo", new JsonPointer());
        
        assertEquals("foo", pointer.getMatchingProperty());
    }
    
    @Test
    public void testGetMatchingIndex() {
        JsonPointer pointer = new JsonPointer();
        
        assertEquals(-1, pointer.getMatchingIndex());
        
        pointer = new JsonPointer("/foo/1/bar", "1", new JsonPointer());
        
        assertEquals(1, pointer.getMatchingIndex());
    }
    
    @Test
    public void testMayMatchProperty() {
        JsonPointer pointer = new JsonPointer();
        
        assertTrue(pointer.mayMatchProperty());
        
        pointer = new JsonPointer("/foo/1/bar", "1", new JsonPointer());
        
        assertFalse(pointer.mayMatchProperty());
    }
    
    @Test
    public void testMayMatchElement() {
        JsonPointer pointer = new JsonPointer();
        
        assertFalse(pointer.mayMatchElement());
        
        pointer = new JsonPointer("/foo/1/bar", "1", new JsonPointer());
        
        assertTrue(pointer.mayMatchElement());
    }
    
    @Test
    public void testMatchProperty() {
        JsonPointer pointer = new JsonPointer("/foo/bar", "foo", new JsonPointer());
        
        JsonPointer result = pointer.matchProperty("foo");
        
        assertEquals("/bar", result.toString());
        
        result = pointer.matchProperty("bar");
        
        assertNull(result);
    }
    
    @Test
    public void testMatchElement() {
        JsonPointer pointer = new JsonPointer("/foo/1/bar", "1", new JsonPointer());
        
        JsonPointer result = pointer.matchElement(1);
        
        assertEquals("/bar", result.toString());
        
        result = pointer.matchElement(0);
        
        assertNull(result);
    }
    
    @Test
    public void testTail() {
        JsonPointer pointer = new JsonPointer("/foo/bar", "foo", new JsonPointer("/bar", "bar", new JsonPointer()));
        
        JsonPointer result = pointer.tail();
        
        assertEquals("/bar", result.toString());
    }
    
    @Test
    public void testToString() {
        JsonPointer pointer = new JsonPointer("/foo/bar", "foo", new JsonPointer());
        
        assertEquals("/foo/bar", pointer.toString());
    }
    
    @Test
    public void testHashCode() {
        JsonPointer pointer1 = new JsonPointer("/foo/bar", "foo", new JsonPointer());
        JsonPointer pointer2 = new JsonPointer("/foo/bar", "foo", new JsonPointer());
        
        assertEquals(pointer1.hashCode(), pointer2.hashCode());
    }
    
    @Test
    public void testEquals() {
        JsonPointer pointer1 = new JsonPointer("/foo/bar", "foo", new JsonPointer());
        JsonPointer pointer2 = new JsonPointer("/foo/bar", "foo", new JsonPointer());
        
        assertTrue(pointer1.equals(pointer2));
        
        pointer1 = new JsonPointer("/foo/bar", "foo", new JsonPointer());
        pointer2 = new JsonPointer("/foo/bar", "foo", new JsonPointer("/bar", "bar", new JsonPointer()));
        
        assertFalse(pointer1.equals(pointer2));
        
        pointer1 = new JsonPointer("/foo/bar", "foo", new JsonPointer());
        pointer2 = new JsonPointer("/foo/bar", "bar", new JsonPointer());
        
        assertFalse(pointer1.equals(pointer2));
        
        pointer1 = new JsonPointer("/foo/bar", "foo", new JsonPointer());
        pointer2 = null;
        
        assertFalse(pointer1.equals(pointer2));
        
        pointer1 = new JsonPointer("/foo/bar", "foo", new JsonPointer());
        Object pointer2Obj = new Object();
        
        assertFalse(pointer1.equals(pointer2Obj));
    }
}