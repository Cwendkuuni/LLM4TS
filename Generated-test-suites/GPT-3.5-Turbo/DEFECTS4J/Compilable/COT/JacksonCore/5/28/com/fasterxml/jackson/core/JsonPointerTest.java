package com.fasterxml.jackson.core;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class JsonPointerTest {
    
    private JsonPointer jsonPointer;
    
    @Before
    public void setup() {
        jsonPointer = new JsonPointer();
    }
    
    @Test
    public void testMatches() {
        assertTrue(jsonPointer.matches());
    }
    
    @Test
    public void testGetMatchingProperty() {
        assertNull(jsonPointer.getMatchingProperty());
    }
    
    @Test
    public void testGetMatchingIndex() {
        assertEquals(-1, jsonPointer.getMatchingIndex());
    }
    
    @Test
    public void testMayMatchProperty() {
        assertFalse(jsonPointer.mayMatchProperty());
    }
    
    @Test
    public void testMayMatchElement() {
        assertFalse(jsonPointer.mayMatchElement());
    }
    
    @Test
    public void testMatchProperty() {
        assertNull(jsonPointer.matchProperty("name"));
    }
    
    @Test
    public void testMatchElement() {
        assertNull(jsonPointer.matchElement(0));
    }
    
    @Test
    public void testTail() {
        assertNull(jsonPointer.tail());
    }
    
    @Test
    public void testToString() {
        assertEquals("", jsonPointer.toString());
    }
    
    @Test
    public void testHashCode() {
        assertTrue(jsonPointer.hashCode() == jsonPointer.hashCode());
    }
    
    @Test
    public void testEquals() {
        assertTrue(jsonPointer.equals(jsonPointer));
        assertFalse(jsonPointer.equals(null));
        assertFalse(jsonPointer.equals("string"));
        assertTrue(jsonPointer.equals(new JsonPointer()));
        assertFalse(jsonPointer.equals(new JsonPointer("test", "", null)));
    }
}