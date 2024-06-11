package com.fasterxml.jackson.core;

import org.junit.Test;
import static org.junit.Assert.*;

public class JsonPointerTest {

    @Test
    public void testMatches() {
        JsonPointer pointer = new JsonPointer();
        assertTrue(pointer.matches());
    }
    
    @Test
    public void testGetMatchingProperty() {
        JsonPointer pointer = new JsonPointer();
        assertEquals("", pointer.getMatchingProperty());
    }
    
    @Test
    public void testGetMatchingIndex() {
        JsonPointer pointer = new JsonPointer();
        assertEquals(-1, pointer.getMatchingIndex());
    }
    
    @Test
    public void testMayMatchProperty() {
        JsonPointer pointer = new JsonPointer();
        assertFalse(pointer.mayMatchProperty());
    }
    
    @Test
    public void testMayMatchElement() {
        JsonPointer pointer = new JsonPointer();
        assertFalse(pointer.mayMatchElement());
    }
    
    @Test
    public void testMatchProperty() {
        JsonPointer pointer1 = new JsonPointer();
        assertNull(pointer1.matchProperty("name"));
        
        JsonPointer pointer2 = new JsonPointer("fullString", "segment", pointer1);
        assertNull(pointer2.matchProperty("name"));
        
        JsonPointer pointer3 = new JsonPointer("fullString", "", pointer1);
        assertNull(pointer3.matchProperty("name"));
        
        JsonPointer pointer4 = new JsonPointer("fullString", "name", pointer1);
        assertNull(pointer4.matchProperty("name"));
        
        JsonPointer pointer5 = new JsonPointer("fullString", "name", pointer1);
        assertEquals(pointer1, pointer5.matchProperty(""));
    }
    
    @Test
    public void testMatchElement() {
        JsonPointer pointer1 = new JsonPointer();
        assertNull(pointer1.matchElement(0));
        
        JsonPointer pointer2 = new JsonPointer("fullString", "segment", pointer1);
        assertNull(pointer2.matchElement(0));
        
        JsonPointer pointer3 = new JsonPointer("fullString", "", pointer1);
        assertNull(pointer3.matchElement(0));
        
        JsonPointer pointer4 = new JsonPointer("fullString", "name", pointer1);
        assertNull(pointer4.matchElement(0));
        
        JsonPointer pointer5 = new JsonPointer("fullString", "name", pointer1);
        assertNull(pointer5.matchElement(-1));
        
        JsonPointer pointer6 = new JsonPointer("fullString", "name", pointer1);
        assertEquals(pointer1, pointer6.matchElement(0));
    }
    
    @Test
    public void testTail() {
        JsonPointer pointer1 = new JsonPointer();
        assertNull(pointer1.tail());
        
        JsonPointer pointer2 = new JsonPointer("fullString", "segment", pointer1);
        assertNull(pointer2.tail());
        
        JsonPointer pointer3 = new JsonPointer("fullString", "", pointer1);
        assertEquals(pointer1, pointer3.tail());
        
        JsonPointer pointer4 = new JsonPointer("fullString", "name", pointer1);
        assertEquals(pointer1, pointer4.tail());
    }
    
    @Test
    public void testToString() {
        JsonPointer pointer = new JsonPointer();
        assertEquals("", pointer.toString());
        
        JsonPointer pointer1 = new JsonPointer("fullString", "segment", pointer);
        assertEquals("fullString", pointer1.toString());
        
        JsonPointer pointer2 = new JsonPointer("fullString", "", pointer);
        assertEquals("fullString", pointer2.toString());
        
        JsonPointer pointer3 = new JsonPointer("fullString", "name", pointer);
        assertEquals("fullString", pointer3.toString());
    }
    
    @Test
    public void testHashCode() {
        JsonPointer pointer1 = new JsonPointer();
        JsonPointer pointer2 = new JsonPointer("fullString", "segment", pointer1);
        
        assertEquals(pointer1.hashCode(), pointer1.hashCode());
        assertNotEquals(pointer1.hashCode(), pointer2.hashCode());
    }
    
    @Test
    public void testEquals() {
        JsonPointer pointer1 = new JsonPointer();
        JsonPointer pointer2 = new JsonPointer();
        JsonPointer pointer3 = new JsonPointer("fullString", "segment", pointer1);
        JsonPointer pointer4 = new JsonPointer("fullString", "segment", pointer2);
        
        assertTrue(pointer1.equals(pointer1));
        assertTrue(pointer1.equals(pointer2));
        assertFalse(pointer1.equals(pointer3));
        assertFalse(pointer3.equals(pointer4));
    }
    
    @Test
    public void testCompile() {
        JsonPointer pointer1 = JsonPointer.compile("");
        assertTrue(pointer1.matches());
        
        JsonPointer pointer2 = JsonPointer.compile("fullString");
        assertFalse(pointer2.matches());
        
        JsonPointer pointer3 = JsonPointer.compile("/fullString");
        assertFalse(pointer3.matches());
    }
    
    @Test
    public void testValueOf() {
        JsonPointer pointer1 = JsonPointer.valueOf("");
        assertTrue(pointer1.matches());
        
        JsonPointer pointer2 = JsonPointer.valueOf("fullString");
        assertFalse(pointer2.matches());
        
        JsonPointer pointer3 = JsonPointer.valueOf("/fullString");
        assertFalse(pointer3.matches());
    }

    @Test
    public void test_parseIndex() {
        JsonPointer pointer = new JsonPointer();
        assertEquals(-1, pointer._parseIndex(""));
        assertEquals(0, pointer._parseIndex("0"));
        assertEquals(9, pointer._parseIndex("9"));
        assertEquals(10, pointer._parseIndex("10"));
        assertEquals(-1, pointer._parseIndex("0000000000"));
        assertEquals(-1, pointer._parseIndex("10000000000"));
    }

    @Test
    public void test_parseTail() {
        JsonPointer pointer1 = new JsonPointer();
        assertEquals(pointer1, JsonPointer._parseTail("/"));
        
        JsonPointer pointer2 = new JsonPointer("fullString", "segment", pointer1);
        assertEquals(pointer2, JsonPointer._parseTail("/fullString/segment"));
        
        JsonPointer pointer3 = new JsonPointer("fullString", "", pointer1);
        assertEquals(pointer3, JsonPointer._parseTail("/fullString/"));
        
        JsonPointer pointer4 = new JsonPointer("fullString", "name", pointer1);
        assertEquals(pointer4, JsonPointer._parseTail("/fullString/name"));
    }

    @Test
    public void test_parseQuotedTail() {
        JsonPointer pointer1 = new JsonPointer();
        assertEquals(pointer1, JsonPointer._parseQuotedTail("/~0/~1", 1));
        
        JsonPointer pointer2 = new JsonPointer("fullString", "seg~/ment", pointer1);
        assertEquals(pointer2, JsonPointer._parseQuotedTail("/fullString/seg~0/ment", 1));
        
        JsonPointer pointer3 = new JsonPointer("fullString", "~1", pointer1);
        assertEquals(pointer3, JsonPointer._parseQuotedTail("/fullString/~01", 1));
    }
    
    private static void _appendEscape(StringBuilder sb, char c) {
        if (c == '0') {
            c = '~';
        } else if (c == '1') {
            c = '/';
        } else {
            sb.append('~');
        }
        sb.append(c);
    }
}