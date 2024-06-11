package com.fasterxml.jackson.core;

import static org.junit.Assert.*;

import org.junit.Test;

public class JsonPointerTest {

    @Test
    public void testEmptyConstructor() {
        JsonPointer pointer = new JsonPointer();
        
        assertNull(pointer.tail());
        assertFalse(pointer.matches());
        assertNull(pointer.getMatchingProperty());
        assertEquals(-1, pointer.getMatchingIndex());
        assertFalse(pointer.mayMatchProperty());
        assertFalse(pointer.mayMatchElement());
    }
    
    @Test
    public void testNonEmptyConstructor() {
        JsonPointer pointer = new JsonPointer("json/pointer", "pointer", null);
        
        assertNull(pointer.tail());
        assertFalse(pointer.matches());
        assertEquals("pointer", pointer.getMatchingProperty());
        assertEquals(-1, pointer.getMatchingIndex());
        assertTrue(pointer.mayMatchProperty());
        assertFalse(pointer.mayMatchElement());
    }
    
    @Test
    public void testCompileEmptyInput() {
        JsonPointer pointer = JsonPointer.compile(null);
        
        assertNull(pointer.tail());
        assertTrue(pointer.matches());
        assertNull(pointer.getMatchingProperty());
        assertEquals(-1, pointer.getMatchingIndex());
        assertFalse(pointer.mayMatchProperty());
        assertFalse(pointer.mayMatchElement());
    }
    
    @Test
    public void testCompileInvalidInput() {
        try {
            JsonPointer pointer = JsonPointer.compile("json.pointer");
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertEquals("Invalid input: JSON Pointer expression must start with '/': \"json.pointer\"", e.getMessage());
        }
    }
    
    @Test
    public void testCompileValidInput() {
        JsonPointer pointer = JsonPointer.compile("/json/pointer");

        assertNull(pointer.tail());
        assertFalse(pointer.matches());
        assertEquals("json", pointer.getMatchingProperty());
        assertEquals(-1, pointer.getMatchingIndex());
        assertTrue(pointer.mayMatchProperty());
        assertFalse(pointer.mayMatchElement());
    }
    
    @Test
    public void testValueOf() {
        JsonPointer pointer = JsonPointer.valueOf("/json/pointer");

        assertNull(pointer.tail());
        assertFalse(pointer.matches());
        assertEquals("json", pointer.getMatchingProperty());
        assertEquals(-1, pointer.getMatchingIndex());
        assertTrue(pointer.mayMatchProperty());
        assertFalse(pointer.mayMatchElement());
    }
    
    @Test
    public void testMatchProperty() {
        JsonPointer pointer = new JsonPointer("json/pointer", "pointer", null);
        JsonPointer result = pointer.matchProperty("pointer");
        
        assertNull(result.tail());
        assertTrue(result.matches());
    }
    
    @Test
    public void testMatchElement() {
        JsonPointer pointer = new JsonPointer("json/pointer", "", null);
        JsonPointer result = pointer.matchElement(0);
        
        assertNull(result.tail());
        assertTrue(result.matches());
    }
    
    @Test
    public void testToString() {
        JsonPointer pointer = new JsonPointer("json/pointer", "pointer", null);
        
        assertEquals("json/pointer", pointer.toString());
    }
    
    @Test
    public void testHashCode() {
        JsonPointer pointer1 = new JsonPointer("json/pointer", "pointer", null);
        JsonPointer pointer2 = new JsonPointer("json/pointer", "pointer", null);
        
        assertEquals(pointer1.hashCode(), pointer2.hashCode());
    }
    
    @Test
    public void testEquals() {
        JsonPointer pointer1 = new JsonPointer("json/pointer", "pointer", null);
        JsonPointer pointer2 = new JsonPointer("json/pointer", "pointer", null);
        
        assertTrue(pointer1.equals(pointer2));
    }
    
    @Test
    public void testParseIndex() {
        int index = JsonPointer._parseIndex("5");
        
        assertEquals(5, index);
    }
    
    @Test
    public void testParseTail() {
        JsonPointer pointer = JsonPointer._parseTail("/json/pointer");
        
        assertNull(pointer.tail());
        assertFalse(pointer.matches());
        assertEquals("json", pointer.getMatchingProperty());
        assertEquals(-1, pointer.getMatchingIndex());
        assertTrue(pointer.mayMatchProperty());
        assertFalse(pointer.mayMatchElement());
    }
    
    @Test
    public void testParseQuotedTail() {
        JsonPointer pointer = JsonPointer._parseQuotedTail("/json/~0pointer");
        
        assertNull(pointer.tail());
        assertFalse(pointer.matches());
        assertEquals("json", pointer.getMatchingProperty());
        assertEquals(-1, pointer.getMatchingIndex());
        assertTrue(pointer.mayMatchProperty());
        assertFalse(pointer.mayMatchElement());
    }
    
    @Test
    public void testAppendEscape() {
        StringBuilder sb = new StringBuilder();
        JsonPointer._appendEscape(sb, '0');
        
        assertEquals("~0", sb.toString());
    }

}