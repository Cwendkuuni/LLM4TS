package com.fasterxml.jackson.core;

import static org.junit.Assert.*;

import org.junit.Test;

public class JsonPointerTest {
    @Test
    public void testMatches() {
        JsonPointer pointer = new JsonPointer();
        assertTrue(pointer.matches());
        
        pointer = new JsonPointer("", "segment");
        assertFalse(pointer.matches());
        
        pointer = new JsonPointer("", "", null);
        assertFalse(pointer.matches());
    }
    
    @Test
    public void testGetMatchingProperty() {
        JsonPointer pointer = new JsonPointer();
        assertEquals("", pointer.getMatchingProperty());
        
        pointer = new JsonPointer("", "segment");
        assertEquals("segment", pointer.getMatchingProperty());
        
        pointer = new JsonPointer("", "", null);
        assertEquals("", pointer.getMatchingProperty());
    }
    
    @Test
    public void testGetMatchingIndex() {
        JsonPointer pointer = new JsonPointer();
        assertEquals(-1, pointer.getMatchingIndex());
        
        pointer = new JsonPointer("", "segment");
        assertEquals(-1, pointer.getMatchingIndex());
        
        pointer = new JsonPointer("", "", null);
        assertEquals(-1, pointer.getMatchingIndex());
    }
    
    @Test
    public void testMayMatchProperty() {
        JsonPointer pointer = new JsonPointer();
        assertFalse(pointer.mayMatchProperty());
        
        pointer = new JsonPointer("", "segment");
        assertTrue(pointer.mayMatchProperty());
        
        pointer = new JsonPointer("", "", null);
        assertFalse(pointer.mayMatchProperty());
    }
    
    @Test
    public void testMayMatchElement() {
        JsonPointer pointer = new JsonPointer();
        assertFalse(pointer.mayMatchElement());
        
        pointer = new JsonPointer("", "segment");
        assertFalse(pointer.mayMatchElement());
        
        pointer = new JsonPointer("", "", null);
        assertFalse(pointer.mayMatchElement());
    }
    
    @Test
    public void testMatchProperty() {
        JsonPointer pointer = new JsonPointer("", "segment");
        assertNull(pointer.matchProperty(""));
        assertNull(pointer.matchProperty("wrong"));
        assertEquals(pointer, pointer.matchProperty("segment"));
        
        pointer = new JsonPointer("", "", null);
        assertNull(pointer.matchProperty("segment"));
    }
    
    @Test
    public void testMatchElement() {
        JsonPointer pointer = new JsonPointer("", "segment");
        assertNull(pointer.matchElement(0));
        assertNull(pointer.matchElement(1));
        assertEquals(pointer, pointer.matchElement(-1));
        
        pointer = new JsonPointer("", "", null);
        assertNull(pointer.matchElement(0));
    }
    
    @Test
    public void testTail() {
        JsonPointer pointer = new JsonPointer();
        assertNull(pointer.tail());
        
        pointer = new JsonPointer("", "segment");
        assertEquals(pointer, pointer.tail());
        
        pointer = new JsonPointer("", "", null);
        assertNull(pointer.tail());
    }
    
    @Test
    public void testToString() {
        JsonPointer pointer = new JsonPointer();
        assertEquals("", pointer.toString());
        
        pointer = new JsonPointer("", "segment");
        assertEquals("/segment", pointer.toString());
        
        pointer = new JsonPointer("", "", null);
        assertEquals("/", pointer.toString());
    }
    
    @Test
    public void testHashCode() {
        JsonPointer pointer1 = new JsonPointer();
        JsonPointer pointer2 = new JsonPointer();
        assertEquals(pointer1.hashCode(), pointer2.hashCode());
        
        pointer1 = new JsonPointer("", "segment");
        pointer2 = new JsonPointer("", "segment");
        assertEquals(pointer1.hashCode(), pointer2.hashCode());
        
        pointer1 = new JsonPointer("", "segment1");
        pointer2 = new JsonPointer("", "segment2");
        assertNotEquals(pointer1.hashCode(), pointer2.hashCode());
    }
    
    @Test
    public void testEquals() {
        JsonPointer pointer1 = new JsonPointer();
        JsonPointer pointer2 = new JsonPointer();
        assertTrue(pointer1.equals(pointer2));
        
        pointer1 = new JsonPointer("", "segment");
        pointer2 = new JsonPointer("", "segment");
        assertTrue(pointer1.equals(pointer2));
        
        pointer1 = new JsonPointer("", "segment1");
        pointer2 = new JsonPointer("", "segment2");
        assertFalse(pointer1.equals(pointer2));
        
        assertFalse(pointer1.equals(""));
        assertFalse(pointer1.equals(null));
    }
    
    @Test
    public void testCompile() {
        JsonPointer pointer = JsonPointer.compile("");
        assertEquals("", pointer.toString());
        
        pointer = JsonPointer.compile("/segment");
        assertEquals("/segment", pointer.toString());
        
        pointer = JsonPointer.compile("/segment~1");
        assertEquals("/segment/", pointer.toString());
        
        pointer = JsonPointer.compile("/~01");
        assertEquals("/~1", pointer.toString());
        
        pointer = JsonPointer.compile("/~0segment");
        assertEquals("/~segment", pointer.toString());
    }
    
    @Test
    public void testValueOf() {
        JsonPointer pointer = JsonPointer.valueOf("");
        assertEquals("", pointer.toString());
        
        pointer = JsonPointer.valueOf("/segment");
        assertEquals("/segment", pointer.toString());
        
        pointer = JsonPointer.valueOf("/segment~1");
        assertEquals("/segment/", pointer.toString());
        
        pointer = JsonPointer.valueOf("/~01");
        assertEquals("/~1", pointer.toString());
        
        pointer = JsonPointer.valueOf("/~0segment");
        assertEquals("/~segment", pointer.toString());
    }
    
    @Test
    public void test_parseIndex() {
        int index = JsonPointer._parseIndex("0");
        assertEquals(0, index);
        
        index = JsonPointer._parseIndex("1");
        assertEquals(1, index);
        
        index = JsonPointer._parseIndex("100");
        assertEquals(100, index);
        
        index = JsonPointer._parseIndex("-1");
        assertEquals(-1, index);
        
        index = JsonPointer._parseIndex("abc");
        assertEquals(-1, index);
        
        index = JsonPointer._parseIndex("10000000000");
        assertEquals(-1, index);
    }
    
    @Test
    public void test_parseTail() {
        JsonPointer pointer = JsonPointer._parseTail("/segment");
        assertEquals("/segment", pointer.toString());
        
        pointer = JsonPointer._parseTail("/segment/next");
        assertEquals("/segment", pointer.toString());
        pointer = pointer.tail();
        assertEquals("/next", pointer.toString());
        
        pointer = JsonPointer._parseTail("/segment~0next");
        assertEquals("/segment~next", pointer.toString());
    }
    
    @Test
    public void test_parseQuotedTail() {
        JsonPointer pointer = JsonPointer._parseQuotedTail("/segment/~01/next", 9);
        assertEquals("/segment/~1", pointer.toString());
        pointer = pointer.tail();
        assertEquals("/next", pointer.toString());
    }
    
    @Test
    public void test_appendEscape() {
        StringBuilder sb = new StringBuilder();
        JsonPointer._appendEscape(sb, '0');
        assertEquals("~0", sb.toString());
        
        sb = new StringBuilder();
        JsonPointer._appendEscape(sb, '1');
        assertEquals("~1", sb.toString());
        
        sb = new StringBuilder();
        JsonPointer._appendEscape(sb, 'a');
        assertEquals("~a", sb.toString());
    }
}