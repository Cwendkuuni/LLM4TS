package com.fasterxml.jackson.core;
import static org.junit.Assert.*;
import org.junit.Test;

public class JsonPointerTest {

    @Test
    public void testEmptyInstance() {
        JsonPointer pointer = JsonPointer.EMPTY;
        
        assertNotNull(pointer);
        assertTrue(pointer.matches());
        assertEquals("", pointer.getMatchingProperty());
        assertEquals(-1, pointer.getMatchingIndex());
    }

    @Test
    public void testConstructorSegment() {
        JsonPointer pointer = new JsonPointer("/test", "test", JsonPointer.EMPTY);
        
        assertNotNull(pointer);
        assertFalse(pointer.matches());
        assertEquals("test", pointer.getMatchingProperty());
        assertEquals(-1, pointer.getMatchingIndex());
    }

    @Test
    public void testCompileValidInput() {
        JsonPointer pointer = JsonPointer.compile("/test");
        
        assertNotNull(pointer);
        assertFalse(pointer.matches());
        assertEquals("test", pointer.getMatchingProperty());
        assertEquals(-1, pointer.getMatchingIndex());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCompileInvalidInput() {
        JsonPointer.compile("test");
    }

    @Test
    public void testMatchProperty() {
        JsonPointer pointer = new JsonPointer("/test", "test", JsonPointer.EMPTY);
        
        JsonPointer result = pointer.matchProperty("test");
        
        assertNotNull(result);
        assertTrue(result.matches());
    }

    @Test
    public void testMatchElement() {
        JsonPointer pointer = new JsonPointer("/0", "0", JsonPointer.EMPTY);
        
        JsonPointer result = pointer.matchElement(0);
        
        assertNotNull(result);
        assertTrue(result.matches());
    }

    @Test
    public void testEquals() {
        JsonPointer pointer1 = JsonPointer.compile("/test");
        JsonPointer pointer2 = JsonPointer.compile("/test");
        
        assertTrue(pointer1.equals(pointer2));
    }

    @Test
    public void testNotEquals() {
        JsonPointer pointer1 = JsonPointer.compile("/test1");
        JsonPointer pointer2 = JsonPointer.compile("/test2");
        
        assertFalse(pointer1.equals(pointer2));
    }
}