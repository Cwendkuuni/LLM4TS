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
        assertFalse(pointer.mayMatchProperty());
        assertFalse(pointer.mayMatchElement());
        assertNull(pointer.matchProperty("name"));
        assertNull(pointer.matchElement(0));
        assertNull(pointer.tail());
        assertEquals("", pointer.toString());
        assertEquals(0, pointer.hashCode());
        assertTrue(pointer.equals(pointer));
        assertFalse(pointer.equals(null));
        assertFalse(pointer.equals("name"));
    }
    
    @Test
    public void testNonEmptyConstructor() {
        JsonPointer pointer = new JsonPointer("fullString", "segment", null);
        
        assertFalse(pointer.matches());
        assertEquals("segment", pointer.getMatchingProperty());
        assertEquals(-1, pointer.getMatchingIndex());
        assertTrue(pointer.mayMatchProperty());
        assertFalse(pointer.mayMatchElement());
        assertNull(pointer.matchProperty("name"));
        assertNull(pointer.matchElement(0));
        assertNull(pointer.tail());
        assertEquals("segment", pointer.toString());
        assertEquals(530050492, pointer.hashCode());
        assertTrue(pointer.equals(pointer));
        assertFalse(pointer.equals(null));
        assertFalse(pointer.equals("name"));
    }
    
    @Test
    public void testCompile() {
        JsonPointer pointer = JsonPointer.compile("");
        
        assertTrue(pointer.matches());
        assertEquals("", pointer.getMatchingProperty());
        assertEquals(-1, pointer.getMatchingIndex());
        assertFalse(pointer.mayMatchProperty());
        assertFalse(pointer.mayMatchElement());
        assertNull(pointer.matchProperty("name"));
        assertNull(pointer.matchElement(0));
        assertNull(pointer.tail());
        assertEquals("", pointer.toString());
        assertEquals(0, pointer.hashCode());
        assertTrue(pointer.equals(pointer));
        assertFalse(pointer.equals(null));
        assertFalse(pointer.equals("name"));
    }
    
    @Test(expected=IllegalArgumentException.class)
    public void testCompileInvalidInput() {
        JsonPointer.compile("invalid");
    }
    
    @Test
    public void testCompileValidInput() {
        JsonPointer pointer = JsonPointer.compile("/segment");
        
        assertFalse(pointer.matches());
        assertEquals("segment", pointer.getMatchingProperty());
        assertEquals(-1, pointer.getMatchingIndex());
        assertTrue(pointer.mayMatchProperty());
        assertFalse(pointer.mayMatchElement());
        assertNull(pointer.matchProperty("name"));
        assertNull(pointer.matchElement(0));
        assertNull(pointer.tail());
        assertEquals("segment", pointer.toString());
        assertEquals(530050492, pointer.hashCode());
        assertTrue(pointer.equals(pointer));
        assertFalse(pointer.equals(null));
        assertFalse(pointer.equals("name"));
    }
    
    @Test
    public void testValueOf() {
        JsonPointer pointer = JsonPointer.valueOf("");
        
        assertTrue(pointer.matches());
        assertEquals("", pointer.getMatchingProperty());
        assertEquals(-1, pointer.getMatchingIndex());
        assertFalse(pointer.mayMatchProperty());
        assertFalse(pointer.mayMatchElement());
        assertNull(pointer.matchProperty("name"));
        assertNull(pointer.matchElement(0));
        assertNull(pointer.tail());
        assertEquals("", pointer.toString());
        assertEquals(0, pointer.hashCode());
        assertTrue(pointer.equals(pointer));
        assertFalse(pointer.equals(null));
        assertFalse(pointer.equals("name"));
    }
    
    @Test
    public void testMatchProperty() {
        JsonPointer pointer = new JsonPointer("fullString", "segment", null);
        
        assertNull(pointer.matchProperty("name"));
        
        JsonPointer next = new JsonPointer(null, null, null);
        pointer = new JsonPointer("fullString", "segment", next);
        
        assertEquals(next, pointer.matchProperty("segment"));
    }
    
    @Test
    public void testMatchElement() {
        JsonPointer pointer = new JsonPointer("fullString", "segment", null);
        
        assertNull(pointer.matchElement(0));
        
        JsonPointer next = new JsonPointer(null, null, null);
        pointer = new JsonPointer("fullString", "segment", next);
        
        assertNull(pointer.matchElement(1));
        assertEquals(next, pointer.matchElement(0));
    }
    
    @Test
    public void testTail() {
        JsonPointer pointer = new JsonPointer("fullString", "segment", null);
        
        assertNull(pointer.tail());
        
        JsonPointer next = new JsonPointer(null, null, null);
        pointer = new JsonPointer("fullString", "segment", next);
        
        assertEquals(next, pointer.tail());
    }
}