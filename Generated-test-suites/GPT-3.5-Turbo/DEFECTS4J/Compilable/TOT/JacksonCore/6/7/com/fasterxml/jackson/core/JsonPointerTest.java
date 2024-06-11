package com.fasterxml.jackson.core;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class JsonPointerTest {
    
    private JsonPointer jsonPointer;
    
    @Before
    public void setUp() {
        jsonPointer = new JsonPointer();
    }
    
    @Test
    public void testMatches() {
        assertTrue(jsonPointer.matches());
    }
    
    @Test
    public void testGetMatchingProperty() {
        assertEquals("", jsonPointer.getMatchingProperty());
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
        JsonPointer nextSegment = jsonPointer.matchProperty("name");
        assertNull(nextSegment);
    }
    
    @Test
    public void testMatchElement() {
        JsonPointer nextSegment = jsonPointer.matchElement(0);
        assertNull(nextSegment);
    }
    
    @Test
    public void testTail() {
        JsonPointer nextSegment = jsonPointer.tail();
        assertNull(nextSegment);
    }
    
    @Test
    public void testToString() {
        assertEquals("", jsonPointer.toString());
    }
    
    @Test
    public void testHashCode() {
        JsonPointer jp = new JsonPointer();
        assertEquals(jp.hashCode(), jsonPointer.hashCode());
    }
    
    @Test
    public void testEquals() {
        JsonPointer jp = new JsonPointer();
        assertTrue(jsonPointer.equals(jp));
    }
    
    @Test
    public void testCompile_ValidInput() {
        JsonPointer jp = JsonPointer.compile("/root");
        assertNotNull(jp);
        assertTrue(jp.matches());
        assertEquals("root", jp.getMatchingProperty());
        assertEquals(-1, jp.getMatchingIndex());
        assertFalse(jp.mayMatchProperty());
        assertFalse(jp.mayMatchElement());
        assertNull(jp.matchProperty("name"));
        assertNull(jp.matchElement(0));
        assertNull(jp.tail());
        assertEquals("/root", jp.toString());
        assertEquals(jp.hashCode(), jp.hashCode());
        assertTrue(jp.equals(jp));
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testCompile_InvalidInput() {
        JsonPointer.compile("root");
    }
    
    @Test
    public void testValueOf() {
        JsonPointer jp = JsonPointer.valueOf("/root");
        assertNotNull(jp);
        assertTrue(jp.matches());
        assertEquals("root", jp.getMatchingProperty());
        assertEquals(-1, jp.getMatchingIndex());
        assertFalse(jp.mayMatchProperty());
        assertFalse(jp.mayMatchElement());
        assertNull(jp.matchProperty("name"));
        assertNull(jp.matchElement(0));
        assertNull(jp.tail());
        assertEquals("/root", jp.toString());
        assertEquals(jp.hashCode(), jp.hashCode());
        assertTrue(jp.equals(jp));
    }
}