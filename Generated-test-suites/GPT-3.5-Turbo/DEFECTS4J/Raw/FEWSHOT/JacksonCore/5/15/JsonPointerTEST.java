package com.fasterxml.jackson.core;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class JsonPointerTest {
  
    @Test
    public void testMatches() {
        JsonPointer emptyPointer = new JsonPointer();
        JsonPointer nonEmptyPointer = new JsonPointer("/", "test", emptyPointer);
        
        assertTrue(emptyPointer.matches());
        assertFalse(nonEmptyPointer.matches());
    }
    
    @Test
    public void testGetMatchingProperty() {
        JsonPointer emptyPointer = new JsonPointer();
        JsonPointer nonEmptyPointer = new JsonPointer("/", "test", emptyPointer);
        
        assertNull(emptyPointer.getMatchingProperty());
        assertEquals("test", nonEmptyPointer.getMatchingProperty());
    }
    
    @Test
    public void testGetMatchingIndex() {
        JsonPointer emptyPointer = new JsonPointer();
        JsonPointer nonEmptyPointer = new JsonPointer("/", "2", emptyPointer);
        
        assertEquals(-1, emptyPointer.getMatchingIndex());
        assertEquals(2, nonEmptyPointer.getMatchingIndex());
    }
    
    @Test
    public void testMayMatchProperty() {
        JsonPointer emptyPointer = new JsonPointer();
        JsonPointer nonEmptyPointerWithProperty = new JsonPointer("/", "test", emptyPointer);
        JsonPointer nonEmptyPointerWithoutProperty = new JsonPointer("/", "", emptyPointer);
        
        assertFalse(emptyPointer.mayMatchProperty());
        assertTrue(nonEmptyPointerWithProperty.mayMatchProperty());
        assertFalse(nonEmptyPointerWithoutProperty.mayMatchProperty());
    }
    
    @Test
    public void testMayMatchElement() {
        JsonPointer emptyPointer = new JsonPointer();
        JsonPointer nonEmptyPointerWithElement = new JsonPointer("/", "1", emptyPointer);
        JsonPointer nonEmptyPointerWithoutElement = new JsonPointer("/", "", emptyPointer);
        
        assertFalse(emptyPointer.mayMatchElement());
        assertTrue(nonEmptyPointerWithElement.mayMatchElement());
        assertFalse(nonEmptyPointerWithoutElement.mayMatchElement());
    }
    
    @Test
    public void testMatchProperty() {
        JsonPointer emptyPointer = new JsonPointer();
        JsonPointer nonEmptyPointer = new JsonPointer("/", "test", emptyPointer);
        
        assertNull(emptyPointer.matchProperty("test"));
        assertEquals(emptyPointer, nonEmptyPointer.matchProperty("other"));
    }
    
    @Test
    public void testMatchElement() {
        JsonPointer emptyPointer = new JsonPointer();
        JsonPointer nonEmptyPointer = new JsonPointer("/", "2", emptyPointer);
        
        assertNull(emptyPointer.matchElement(2));
        assertEquals(emptyPointer, nonEmptyPointer.matchElement(1));
    }
    
    @Test
    public void testTail() {
        JsonPointer emptyPointer = new JsonPointer();
        JsonPointer nonEmptyPointer = new JsonPointer("/", "test", emptyPointer);
        
        assertEquals(emptyPointer, emptyPointer.tail());
        assertEquals(emptyPointer, nonEmptyPointer.tail());
    }
    
    @Test
    public void testToString() {
        JsonPointer emptyPointer = new JsonPointer();
        JsonPointer nonEmptyPointer = new JsonPointer("/", "test", emptyPointer);
        
        assertEquals("", emptyPointer.toString());
        assertEquals("/test", nonEmptyPointer.toString());
    }
    
    @Test
    public void testHashCode() {
        JsonPointer emptyPointer1 = new JsonPointer();
        JsonPointer emptyPointer2 = new JsonPointer();
        JsonPointer nonEmptyPointer1 = new JsonPointer("/", "test", emptyPointer1);
        JsonPointer nonEmptyPointer2 = new JsonPointer("/", "test", emptyPointer2);
        
        assertEquals(emptyPointer1.hashCode(), emptyPointer2.hashCode());
        assertEquals(nonEmptyPointer1.hashCode(), nonEmptyPointer2.hashCode());
    }
    
    @Test
    public void testEquals() {
        JsonPointer emptyPointer1 = new JsonPointer();
        JsonPointer emptyPointer2 = new JsonPointer();
        JsonPointer nonEmptyPointer1 = new JsonPointer("/", "test", emptyPointer1);
        JsonPointer nonEmptyPointer2 = new JsonPointer("/", "test", emptyPointer2);
        JsonPointer nonEmptyPointer3 = new JsonPointer("/", "other", emptyPointer1);
        
        assertTrue(emptyPointer1.equals(emptyPointer1));
        assertFalse(emptyPointer1.equals(null));
        assertFalse(emptyPointer1.equals(""));
        assertTrue(emptyPointer1.equals(emptyPointer2));
        assertTrue(nonEmptyPointer1.equals(nonEmptyPointer2));
        assertFalse(nonEmptyPointer1.equals(nonEmptyPointer3));
    }

}