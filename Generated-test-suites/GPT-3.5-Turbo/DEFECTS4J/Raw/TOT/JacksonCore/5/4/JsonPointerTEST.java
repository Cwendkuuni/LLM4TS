package com.fasterxml.jackson.core;

import org.junit.Test;
import static org.junit.Assert.*;

public class JsonPointerTest {

    @Test
    public void testMatches() {
        JsonPointer jsonPointer = new JsonPointer();
        assertTrue(jsonPointer.matches());
    }
    
    @Test
    public void testGetMatchingProperty() {
        JsonPointer jsonPointer = new JsonPointer();
        assertEquals("", jsonPointer.getMatchingProperty());
    }
    
    @Test
    public void testGetMatchingIndex() {
        JsonPointer jsonPointer = new JsonPointer();
        assertEquals(-1, jsonPointer.getMatchingIndex());
    }
    
    @Test
    public void testMayMatchProperty() {
        JsonPointer jsonPointer = new JsonPointer();
        assertTrue(jsonPointer.mayMatchProperty());
    }
    
    @Test
    public void testMayMatchElement() {
        JsonPointer jsonPointer = new JsonPointer();
        assertFalse(jsonPointer.mayMatchElement());
    }
    
    @Test
    public void testMatchPropertyValid() {
        JsonPointer jsonPointer = new JsonPointer("fullString", "segment", null);
        JsonPointer next = new JsonPointer();
        assertEquals(next, jsonPointer.matchProperty("segment"));
    }
    
    @Test
    public void testMatchPropertyInvalid() {
        JsonPointer jsonPointer = new JsonPointer("fullString", "segment", null);
        assertNull(jsonPointer.matchProperty("invalid"));
    }
    
    @Test
    public void testMatchElementValid() {
        JsonPointer jsonPointer = new JsonPointer("fullString", "", null);
        JsonPointer next = new JsonPointer();
        assertEquals(next, jsonPointer.matchElement(0));
    }
    
    @Test
    public void testMatchElementInvalid() {
        JsonPointer jsonPointer = new JsonPointer("fullString", "", null);
        assertNull(jsonPointer.matchElement(1));
    }
    
    @Test
    public void testTail() {
        JsonPointer jsonPointer = new JsonPointer("fullString", "", null);
        JsonPointer next = new JsonPointer();
        assertEquals(next, jsonPointer.tail());
    }
    
    @Test
    public void testToString() {
        JsonPointer jsonPointer = new JsonPointer("fullString", "", null);
        assertEquals("fullString", jsonPointer.toString());
    }
    
    @Test
    public void testHashCode() {
        JsonPointer jsonPointer1 = new JsonPointer("fullString", "", null);
        JsonPointer jsonPointer2 = new JsonPointer("fullString", "", null);
        assertEquals(jsonPointer1.hashCode(), jsonPointer2.hashCode());
    }
    
    @Test
    public void testEqualsTrue() {
        JsonPointer jsonPointer1 = new JsonPointer("fullString", "", null);
        JsonPointer jsonPointer2 = new JsonPointer("fullString", "", null);
        assertTrue(jsonPointer1.equals(jsonPointer2));
    }
    
    @Test
    public void testEqualsFalse() {
        JsonPointer jsonPointer1 = new JsonPointer("fullString", "", null);
        assertFalse(jsonPointer1.equals(new String("fullString")));
    }
}