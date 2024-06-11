
package com.fasterxml.jackson.core;

import org.junit.Test;
import static org.junit.Assert.*;

public class JsonPointerTest {
  
    @Test
    public void testMatches() {
        JsonPointer jsonPointer1 = JsonPointer.EMPTY;
        assertTrue(jsonPointer1.matches());
        
        JsonPointer jsonPointer2 = new JsonPointer("foo", "bar", JsonPointer.EMPTY);
        assertFalse(jsonPointer2.matches());
    }
    
    @Test
    public void testGetMatchingProperty() {
        JsonPointer jsonPointer = new JsonPointer("foo", "bar", JsonPointer.EMPTY);
        assertEquals("bar", jsonPointer.getMatchingProperty());
    }
    
    @Test
    public void testGetMatchingIndex() {
        JsonPointer jsonPointer = new JsonPointer("foo", "4", JsonPointer.EMPTY);
        assertEquals(4, jsonPointer.getMatchingIndex());
    }
    
    @Test
    public void testMayMatchProperty() {
        JsonPointer jsonPointer1 = new JsonPointer("foo", "bar", JsonPointer.EMPTY);
        assertTrue(jsonPointer1.mayMatchProperty());
        
        JsonPointer jsonPointer2 = new JsonPointer("foo", "4", JsonPointer.EMPTY);
        assertFalse(jsonPointer2.mayMatchProperty());
    }
    
    @Test
    public void testMayMatchElement() {
        JsonPointer jsonPointer1 = new JsonPointer("foo", "bar", JsonPointer.EMPTY);
        assertFalse(jsonPointer1.mayMatchElement());
        
        JsonPointer jsonPointer2 = new JsonPointer("foo", "4", JsonPointer.EMPTY);
        assertTrue(jsonPointer2.mayMatchElement());
    }
    
    @Test
    public void testMatchProperty() {
        JsonPointer jsonPointer = new JsonPointer("foo", "bar", JsonPointer.EMPTY);
        JsonPointer result = jsonPointer.matchProperty("bar");
        assertEquals(JsonPointer.EMPTY, result);
    }
    
    @Test
    public void testMatchElement() {
        JsonPointer jsonPointer = new JsonPointer("foo", "4", JsonPointer.EMPTY);
        JsonPointer result = jsonPointer.matchElement(4);
        assertEquals(JsonPointer.EMPTY, result);
    }
    
    @Test
    public void testTail() {
        JsonPointer jsonPointer = new JsonPointer("foo", "bar", JsonPointer.EMPTY);
        JsonPointer result = jsonPointer.tail();
        assertEquals(JsonPointer.EMPTY, result);
    }
    
    @Test
    public void testToString() {
        JsonPointer jsonPointer = new JsonPointer("foo", "bar", JsonPointer.EMPTY);
        String result = jsonPointer.toString();
        assertEquals("foo", result);
    }
    
    @Test
    public void testHashCode() {
        JsonPointer jsonPointer1 = new JsonPointer("foo", "bar", JsonPointer.EMPTY);
        JsonPointer jsonPointer2 = new JsonPointer("foo", "bar", JsonPointer.EMPTY);
        assertEquals(jsonPointer1.hashCode(), jsonPointer2.hashCode());
    }
    
    @Test
    public void testEquals() {
        JsonPointer jsonPointer1 = new JsonPointer("foo", "bar", JsonPointer.EMPTY);
        JsonPointer jsonPointer2 = new JsonPointer("foo", "bar", JsonPointer.EMPTY);
        assertTrue(jsonPointer1.equals(jsonPointer2));
    }
}
