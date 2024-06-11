package com.fasterxml.jackson.core;

import static org.junit.Assert.*;

import org.junit.Test;

public class JsonPointerTest {
    
    @Test
    public void testCompile() {
        JsonPointer pointer = JsonPointer.compile("");
        assertTrue(pointer.matches());

        pointer = JsonPointer.compile("/");
        assertTrue(pointer.matches());
        
        pointer = JsonPointer.compile("/foo");
        assertFalse(pointer.matches());
        assertEquals("foo", pointer.getMatchingProperty());
        
        pointer = JsonPointer.compile("/0");
        assertFalse(pointer.matches());
        assertEquals(0, pointer.getMatchingIndex());
        
        pointer = JsonPointer.compile("/0/1");
        assertFalse(pointer.matches());
        assertEquals(0, pointer.getMatchingIndex());

        pointer = JsonPointer.compile("/a~1b");
        assertFalse(pointer.matches());
        assertEquals("a/b", pointer.getMatchingProperty());
        
        pointer = JsonPointer.compile("/c~0d");
        assertFalse(pointer.matches());
        assertEquals("c~d", pointer.getMatchingProperty());
    }
    
    @Test
    public void testValueOf() {
        JsonPointer pointer = JsonPointer.valueOf("");
        assertTrue(pointer.matches());

        pointer = JsonPointer.valueOf("/");
        assertTrue(pointer.matches());
        
        pointer = JsonPointer.valueOf("/foo");
        assertFalse(pointer.matches());
        assertEquals("foo", pointer.getMatchingProperty());
        
        pointer = JsonPointer.valueOf("/0");
        assertFalse(pointer.matches());
        assertEquals(0, pointer.getMatchingIndex());
        
        pointer = JsonPointer.valueOf("/0/1");
        assertFalse(pointer.matches());
        assertEquals(0, pointer.getMatchingIndex());

        pointer = JsonPointer.valueOf("/a~1b");
        assertFalse(pointer.matches());
        assertEquals("a/b", pointer.getMatchingProperty());
        
        pointer = JsonPointer.valueOf("/c~0d");
        assertFalse(pointer.matches());
        assertEquals("c~d", pointer.getMatchingProperty());
    }
    
    @Test
    public void testTail() {
        JsonPointer pointer = JsonPointer.compile("/foo/bar/baz").tail();
        assertEquals("/bar/baz", pointer.toString());
        
        pointer = JsonPointer.compile("").tail();
        assertNull(pointer);
    }
    
    @Test
    public void testToString() {
        JsonPointer pointer = JsonPointer.compile("/foo/bar/baz");
        assertEquals("/foo/bar/baz", pointer.toString());
        
        pointer = JsonPointer.compile("");
        assertEquals("", pointer.toString());
    }
    
    @Test
    public void testEquals() {
        JsonPointer pointer1 = JsonPointer.compile("/foo/bar/baz");
        JsonPointer pointer2 = JsonPointer.compile("/foo/bar/baz");
        JsonPointer pointer3 = JsonPointer.compile("/foo/bar");
        
        assertEquals(pointer1, pointer1);
        assertEquals(pointer1, pointer2);
        assertNotEquals(pointer1, pointer3);
        assertNotEquals(pointer1, null);
        assertNotEquals(pointer1, new Object());
    }
    
    @Test
    public void testHashCode() {
        JsonPointer pointer1 = JsonPointer.compile("/foo/bar/baz");
        JsonPointer pointer2 = JsonPointer.compile("/foo/bar/baz");
        JsonPointer pointer3 = JsonPointer.compile("/foo/bar");
        
        assertEquals(pointer1.hashCode(), pointer2.hashCode());
        assertNotEquals(pointer1.hashCode(), pointer3.hashCode());
    }
    
    @Test
    public void testMatchProperty() {
        JsonPointer pointer = JsonPointer.compile("/foo");
        
        JsonPointer matchingPointer = pointer.matchProperty("foo");
        assertNotNull(matchingPointer);
        assertTrue(matchingPointer.matches());
        
        matchingPointer = pointer.matchProperty("bar");
        assertNull(matchingPointer);
    }
    
    @Test
    public void testMatchElement() {
        JsonPointer pointer = JsonPointer.compile("/0");
        
        JsonPointer matchingPointer = pointer.matchElement(0);
        assertNotNull(matchingPointer);
        assertTrue(matchingPointer.matches());
        
        matchingPointer = pointer.matchElement(1);
        assertNull(matchingPointer);
    }
    
    @Test
    public void testMayMatchProperty() {
        JsonPointer pointer = JsonPointer.compile("/foo");
        assertTrue(pointer.mayMatchProperty());
        
        pointer = JsonPointer.compile("/0");
        assertFalse(pointer.mayMatchProperty());
    }
    
    @Test
    public void testMayMatchElement() {
        JsonPointer pointer = JsonPointer.compile("/foo");
        assertFalse(pointer.mayMatchElement());
        
        pointer = JsonPointer.compile("/0");
        assertTrue(pointer.mayMatchElement());
    }
    
    @Test
    public void testMatchingProperty() {
        JsonPointer pointer = JsonPointer.compile("/foo");
        assertEquals("foo", pointer.getMatchingProperty());
    }
    
    @Test
    public void testMatchingIndex() {
        JsonPointer pointer = JsonPointer.compile("/0");
        assertEquals(0, pointer.getMatchingIndex());
    }
    
    @Test
    public void testEmptyConstructor() {
        JsonPointer pointer = new JsonPointer();
        assertTrue(pointer.matches());
        assertEquals("", pointer.toString());
    }
    
    @Test
    public void testNonEmptyConstructor() {
        JsonPointer pointer = new JsonPointer("/foo/bar/baz", "foo", null);
        assertFalse(pointer.matches());
        assertEquals("foo", pointer.getMatchingProperty());
        assertEquals("/foo/bar/baz", pointer.toString());
    }
    
    @Test
    public void testPrivateConstructor() throws NoSuchMethodException, SecurityException {
        Constructor&lt;JsonPointer&gt; constructor = JsonPointer.class.getDeclaredConstructor();
        constructor.setAccessible(true);
        assertThrows(IllegalAccessException.class, () -> {
            constructor.newInstance();
        });
    }
}