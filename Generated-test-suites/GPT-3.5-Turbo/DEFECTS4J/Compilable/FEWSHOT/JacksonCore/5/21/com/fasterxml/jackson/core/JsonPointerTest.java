
package com.fasterxml.jackson.core;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class JsonPointerTest {
    
    @Test
    public void testCompile() {
        JsonPointer pointer = JsonPointer.compile("");
        assertEquals("", pointer.toString());
        
        pointer = JsonPointer.compile("/foo");
        assertEquals("/foo", pointer.toString());
        
        pointer = JsonPointer.compile("/foo/bar");
        assertEquals("/foo/bar", pointer.toString());
        
        pointer = JsonPointer.compile("/foo/0");
        assertEquals("/foo/0", pointer.toString());
        
        pointer = JsonPointer.compile("/foo/~1bar");
        assertEquals("/foo/~1bar", pointer.toString());
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testCompileInvalidInput() {
        JsonPointer.compile("foo");
    }
    
    @Test
    public void testEmptyPointer() {
        JsonPointer pointer = JsonPointer.EMPTY;
        assertTrue(pointer.matches());
        assertNull(pointer.tail());
    }
    
    @Test
    public void testNonEmptyPointer() {
        JsonPointer pointer = new JsonPointer("/foo/bar", "foo", null);
        assertFalse(pointer.matches());
        assertNotNull(pointer.tail());
    }
    
    @Test
    public void testMatchProperty() {
        JsonPointer pointer = new JsonPointer("/foo/bar", "foo", null);
        JsonPointer nextPointer = pointer.matchProperty("foo");
        assertNotNull(nextPointer);
        assertEquals("/bar", nextPointer.toString());
        
        nextPointer = pointer.matchProperty("bar");
        assertNull(nextPointer);
    }
    
    @Test
    public void testMatchElement() {
        JsonPointer pointer = new JsonPointer("/foo/bar", "foo", null);
        JsonPointer nextPointer = pointer.matchElement(0);
        assertNull(nextPointer);
        
        pointer = new JsonPointer("/foo/0", "foo", null);
        nextPointer = pointer.matchElement(0);
        assertNotNull(nextPointer);
        assertEquals("/", nextPointer.toString());
    }
    
    @Test
    public void testToString() {
        JsonPointer pointer = new JsonPointer("/foo/bar", "foo", null);
        assertEquals("/foo/bar", pointer.toString());
    }
    
    @Test
    public void testHashCode() {
        JsonPointer pointer1 = new JsonPointer("/foo/bar", "foo", null);
        JsonPointer pointer2 = new JsonPointer("/foo/bar", "foo", null);
        assertEquals(pointer1.hashCode(), pointer2.hashCode());
    }
    
    @Test
    public void testEquals() {
        JsonPointer pointer1 = new JsonPointer("/foo/bar", "foo", null);
        JsonPointer pointer2 = new JsonPointer("/foo/bar", "foo", null);
        assertTrue(pointer1.equals(pointer2));
        
        pointer1 = new JsonPointer("/foo/bar", "foo", null);
        assertFalse(pointer1.equals(null));
        
        pointer1 = new JsonPointer("/foo/bar", "foo", null);
        assertFalse(pointer1.equals(""));
    }
    
    @Test
    public void testMatchingProperty() {
        JsonPointer pointer = new JsonPointer("/foo/bar", "foo", null);
        assertEquals("foo", pointer.getMatchingProperty());
        assertTrue(pointer.mayMatchProperty());
        assertFalse(pointer.mayMatchElement());
    }
    
    @Test
    public void testMatchingIndex() {
        JsonPointer pointer = new JsonPointer("/foo/0", "foo", null);
        assertEquals(0, pointer.getMatchingIndex());
        assertFalse(pointer.mayMatchProperty());
        assertTrue(pointer.mayMatchElement());
    }
}

