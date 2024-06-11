package com.fasterxml.jackson.core;
import static org.junit.Assert.*;

import org.junit.Test;

public class JsonPointerTest {

    @Test
    public void testMatches() {
        JsonPointer pointer = new JsonPointer();
        assertTrue(pointer.matches());
    }
    
    @Test
    public void testGetMatchingProperty() {
        JsonPointer pointer = new JsonPointer();
        assertEquals("", pointer.getMatchingProperty());
    }
    
    @Test
    public void testGetMatchingIndex() {
        JsonPointer pointer = new JsonPointer();
        assertEquals(-1, pointer.getMatchingIndex());
    }
    
    @Test
    public void testMayMatchProperty() {
        JsonPointer pointer = new JsonPointer();
        assertTrue(pointer.mayMatchProperty());
    }
    
    @Test
    public void testMayMatchElement() {
        JsonPointer pointer = new JsonPointer();
        assertFalse(pointer.mayMatchElement());
    }
    
    @Test
    public void testMatchProperty() {
        JsonPointer pointer = new JsonPointer();
        assertNull(pointer.matchProperty("test"));
    }
    
    @Test
    public void testMatchElement() {
        JsonPointer pointer = new JsonPointer();
        assertNull(pointer.matchElement(0));
    }
    
    @Test
    public void testTail() {
        JsonPointer pointer = new JsonPointer();
        assertNull(pointer.tail());
    }
    
    @Test
    public void testCompile() {
        JsonPointer pointer = JsonPointer.compile("/test");
        assertEquals("/test", pointer.toString());
    }
    
    @Test
    public void testValueOf() {
        JsonPointer pointer = JsonPointer.valueOf("/test");
        assertEquals("/test", pointer.toString());
    }

}