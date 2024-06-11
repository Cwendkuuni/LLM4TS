package com.fasterxml.jackson.core;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class JsonPointerTest {
    @Test
    public void testMatches() {
        JsonPointer jsonPointer = JsonPointer.compile("/");
        assertEquals(true, jsonPointer.matches());
        
        jsonPointer = JsonPointer.compile("/segments");
        assertEquals(false, jsonPointer.matches());
    }
    
    @Test
    public void testGetMatchingProperty() {
        JsonPointer jsonPointer = JsonPointer.compile("/");
        assertEquals("", jsonPointer.getMatchingProperty());
        
        jsonPointer = JsonPointer.compile("/segments");
        assertEquals("segments", jsonPointer.getMatchingProperty());
    }
    
    @Test
    public void testGetMatchingIndex() {
        JsonPointer jsonPointer = JsonPointer.compile("/");
        assertEquals(-1, jsonPointer.getMatchingIndex());
        
        jsonPointer = JsonPointer.compile("/10");
        assertEquals(10, jsonPointer.getMatchingIndex());
    }
    
    @Test
    public void testMayMatchProperty() {
        JsonPointer jsonPointer = JsonPointer.compile("/");
        assertEquals(false, jsonPointer.mayMatchProperty());
        
        jsonPointer = JsonPointer.compile("/segments");
        assertEquals(true, jsonPointer.mayMatchProperty());
    }
    
    @Test
    public void testMayMatchElement() {
        JsonPointer jsonPointer = JsonPointer.compile("/");
        assertEquals(false, jsonPointer.mayMatchElement());
        
        jsonPointer = JsonPointer.compile("/1");
        assertEquals(true, jsonPointer.mayMatchElement());
    }
    
    @Test
    public void testMatchProperty() {
        JsonPointer jsonPointer = JsonPointer.compile("/segments");
        assertEquals(null, jsonPointer.matchProperty("invalid"));
        
        jsonPointer = JsonPointer.compile("/segments");
        assertEquals(jsonPointer.tail(), jsonPointer.matchProperty("segments"));
    }
    
    @Test
    public void testMatchElement() {
        JsonPointer jsonPointer = JsonPointer.compile("/1");
        assertEquals(null, jsonPointer.matchElement(0));
        
        jsonPointer = JsonPointer.compile("/1");
        assertEquals(jsonPointer.tail(), jsonPointer.matchElement(1));
    }
    
    @Test
    public void testTail() {
        JsonPointer jsonPointer = JsonPointer.compile("/");
        assertEquals(null, jsonPointer.tail());
        
        jsonPointer = JsonPointer.compile("/segments");
        assertEquals(jsonPointer, jsonPointer.tail());
    }
    
    @Test
    public void testToString() {
        JsonPointer jsonPointer = JsonPointer.compile("/");
        assertEquals("/", jsonPointer.toString());
        
        jsonPointer = JsonPointer.compile("/segments");
        assertEquals("/segments", jsonPointer.toString());
    }
    
    @Test
    public void testHashCode() {
        JsonPointer jsonPointer1 = JsonPointer.compile("/");
        JsonPointer jsonPointer2 = JsonPointer.compile("/");
        assertEquals(jsonPointer1.hashCode(), jsonPointer2.hashCode());
        
        jsonPointer1 = JsonPointer.compile("/segments");
        jsonPointer2 = JsonPointer.compile("/segments");
        assertEquals(jsonPointer1.hashCode(), jsonPointer2.hashCode());
    }
    
    @Test
    public void testEquals() {
        JsonPointer jsonPointer1 = JsonPointer.compile("/");
        JsonPointer jsonPointer2 = JsonPointer.compile("/");
        assertEquals(true, jsonPointer1.equals(jsonPointer2));
        
        jsonPointer1 = JsonPointer.compile("/segments");
        jsonPointer2 = JsonPointer.compile("/segments");
        assertEquals(true, jsonPointer1.equals(jsonPointer2));
        
        jsonPointer1 = JsonPointer.compile("/segments");
        jsonPointer2 = JsonPointer.compile("/other");
        assertEquals(false, jsonPointer1.equals(jsonPointer2));
    }
}