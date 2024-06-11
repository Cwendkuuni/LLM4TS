package com.fasterxml.jackson.core;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class JsonPointerTest {
    @Test
    public void testMatches() {
        JsonPointer jp1 = JsonPointer.compile("");
        JsonPointer jp2 = JsonPointer.compile("/");
        JsonPointer jp3 = JsonPointer.compile("/foo");
        
        assertEquals(true, jp1.matches());
        assertEquals(false, jp2.matches());
        assertEquals(false, jp3.matches());
    }
    
    @Test
    public void testGetMatchingProperty() {
        JsonPointer jp1 = JsonPointer.compile("");
        JsonPointer jp2 = JsonPointer.compile("/foo");
        
        assertEquals("", jp1.getMatchingProperty());
        assertEquals("foo", jp2.getMatchingProperty());
    }

    @Test
    public void testGetMatchingIndex() {
        JsonPointer jp1 = JsonPointer.compile("");
        JsonPointer jp2 = JsonPointer.compile("/0");
        
        assertEquals(-1, jp1.getMatchingIndex());
        assertEquals(0, jp2.getMatchingIndex());
    }

    @Test
    public void testMayMatchProperty() {
        JsonPointer jp1 = JsonPointer.compile("");
        JsonPointer jp2 = JsonPointer.compile("/foo");
        
        assertEquals(true, jp1.mayMatchProperty());
        assertEquals(true, jp2.mayMatchProperty());
    }

    @Test
    public void testMayMatchElement() {
        JsonPointer jp1 = JsonPointer.compile("");
        JsonPointer jp2 = JsonPointer.compile("/0");
        
        assertEquals(false, jp1.mayMatchElement());
        assertEquals(true, jp2.mayMatchElement());
    }

    @Test
    public void testMatchProperty() {
        JsonPointer jp1 = JsonPointer.compile("");
        JsonPointer jp2 = JsonPointer.compile("/foo");
        JsonPointer jp3 = JsonPointer.compile("/bar");
        
        assertNull(jp1.matchProperty("foo"));
        assertEquals(jp3, jp2.matchProperty("bar"));
    }

    @Test
    public void testMatchElement() {
        JsonPointer jp1 = JsonPointer.compile("");
        JsonPointer jp2 = JsonPointer.compile("/0");
        JsonPointer jp3 = JsonPointer.compile("/1");
        
        assertNull(jp1.matchElement(0));
        assertEquals(jp3, jp2.matchElement(1));
    }

    @Test
    public void testTail() {
        JsonPointer jp1 = JsonPointer.compile("");
        JsonPointer jp2 = JsonPointer.compile("/foo");
        
        assertEquals(jp1, jp1.tail());
        assertEquals(jp1, jp2.tail());
    }
    
    @Test
    public void testToString() {
        JsonPointer jp1 = JsonPointer.compile("");
        JsonPointer jp2 = JsonPointer.compile("/foo");
        
        assertEquals("", jp1.toString());
        assertEquals("/foo", jp2.toString());
    }
    
    @Test
    public void testHashCode() {
        JsonPointer jp1 = JsonPointer.compile("");
        JsonPointer jp2 = JsonPointer.compile("/foo");
        
        assertEquals(jp1.toString().hashCode(), jp1.hashCode());
        assertEquals(jp2.toString().hashCode(), jp2.hashCode());
    }
    
    @Test
    public void testEquals() {
        JsonPointer jp1 = JsonPointer.compile("");
        JsonPointer jp2 = JsonPointer.compile("");
        JsonPointer jp3 = JsonPointer.compile("/foo");
        
        assertEquals(true, jp1.equals(jp1));
        assertEquals(true, jp1.equals(jp2));
        assertEquals(false, jp1.equals(jp3));
    }
}