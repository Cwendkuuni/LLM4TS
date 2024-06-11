package com.fasterxml.jackson.core;

import org.junit.Test;
import static org.junit.Assert.*;

public class JsonPointerTest {

    @Test
    public void testEmptyPointer() {
        JsonPointer pointer = JsonPointer.EMPTY;
        
        assertTrue(pointer.matches());
        assertEquals("", pointer.toString());
    }

    @Test
    public void testCompileValidInput() {
        JsonPointer pointer = JsonPointer.compile("/test/1");
        
        assertFalse(pointer.matches());
        assertEquals("/test/1", pointer.toString());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCompileInvalidInput() {
        JsonPointer.compile("test/1");
    }

    @Test
    public void testMatchProperty() {
        JsonPointer pointer = JsonPointer.compile("/test/1");
        
        JsonPointer matched = pointer.matchProperty("test");
        
        assertNotNull(matched);
        assertEquals("/1", matched.toString());
    }

    @Test
    public void testMatchElement() {
        JsonPointer pointer = JsonPointer.compile("/test/0");
        
        JsonPointer matched = pointer.matchElement(0);
        
        assertNotNull(matched);
        assertTrue(matched.matches());
    }

    @Test
    public void testTail() {
        JsonPointer pointer = JsonPointer.compile("/test/1");
        
        JsonPointer tail = pointer.tail();
        
        assertNotNull(tail);
        assertTrue(tail.matches());
    }
    
    @Test
    public void testEqualsAndHashCode() {
        JsonPointer pointer1 = JsonPointer.compile("/test/1");
        JsonPointer pointer2 = JsonPointer.compile("/test/1");
        
        assertTrue(pointer1.equals(pointer2));
        assertEquals(pointer1.hashCode(), pointer2.hashCode());
    }

    @Test
    public void testInvalidIndex() {
        int index = JsonPointer._parseIndex("invalid");
        
        assertEquals(-1, index);
    }

    @Test
    public void testLongIndex() {
        int index = JsonPointer._parseIndex("10000000000");
        
        assertEquals(-1, index);
    }

}