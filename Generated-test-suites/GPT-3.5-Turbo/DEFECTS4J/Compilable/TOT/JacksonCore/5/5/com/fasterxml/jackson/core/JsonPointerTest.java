package com.fasterxml.jackson.core;
import static org.junit.Assert.*;
import org.junit.Test;

public class JsonPointerTest {

    @Test
    public void testJsonPointerEmpty() {
        JsonPointer empty = JsonPointer.EMPTY;
        
        assertTrue(empty.matches());
        assertEquals("", empty.getMatchingProperty());
        assertEquals(-1, empty.getMatchingIndex());
        assertFalse(empty.mayMatchProperty());
        assertFalse(empty.mayMatchElement());
    }
    
    @Test
    public void testJsonPointerCompileValid() {
        JsonPointer pointer = JsonPointer.compile("/user/address");
        
        assertFalse(pointer.matches());
        assertEquals("user", pointer.getMatchingProperty());
        assertEquals(-1, pointer.getMatchingIndex());
        assertTrue(pointer.mayMatchProperty());
        assertFalse(pointer.mayMatchElement());
        
        JsonPointer next = pointer.tail();
        assertNotNull(next);
        
        assertFalse(next.matches());
        assertEquals("address", next.getMatchingProperty());
        assertEquals(-1, next.getMatchingIndex());
        assertTrue(next.mayMatchProperty());
        assertFalse(next.mayMatchElement());
        
        assertNull(next.tail());
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testJsonPointerCompileInvalid() {
        JsonPointer.compile("user/address");
    }

    @Test
    public void testJsonPointerMatchProperty() {
        JsonPointer pointer = JsonPointer.compile("/user/address");
        JsonPointer match = pointer.matchProperty("user");
        
        assertNotNull(match);
        assertFalse(match.matches());
        assertEquals("address", match.getMatchingProperty());
        assertEquals(-1, match.getMatchingIndex());
        assertTrue(match.mayMatchProperty());
        assertFalse(match.mayMatchElement());
        
        assertNull(match.matchProperty("address"));
    }
    
    @Test
    public void testJsonPointerMatchElement() {
        JsonPointer pointer = JsonPointer.compile("/user/address/0");
        JsonPointer match = pointer.matchElement(0);
        
        assertNotNull(match);
        assertFalse(match.matches());
        assertEquals("", match.getMatchingProperty());
        assertEquals(0, match.getMatchingIndex());
        assertFalse(match.mayMatchProperty());
        assertTrue(match.mayMatchElement());
        
        assertNull(match.matchElement(1));
    }
}