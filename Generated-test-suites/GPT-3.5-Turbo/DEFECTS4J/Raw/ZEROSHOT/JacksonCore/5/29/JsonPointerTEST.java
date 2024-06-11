package com.fasterxml.jackson.core;

import org.junit.Test;
import static org.junit.Assert.*;

public class JsonPointerTest {

    @Test
    public void testEmptyConstructor() {
        JsonPointer pointer = new JsonPointer();
        assertNotNull(pointer);
        assertNull(pointer._nextSegment);
        assertEquals("", pointer._matchingPropertyName);
        assertEquals(-1, pointer._matchingElementIndex);
        assertEquals("", pointer._asString);
    }
    
    @Test
    public void testConstructorWithParam() {
        JsonPointer pointer = new JsonPointer("fullString", "segment", null);
        assertNotNull(pointer);
        assertNull(pointer._nextSegment);
        assertEquals("segment", pointer._matchingPropertyName);
        assertEquals(-1, pointer._matchingElementIndex);
        assertEquals("fullString", pointer._asString);
    }
    
    @Test
    public void testCompileWithNull() {
        JsonPointer pointer = JsonPointer.compile(null);
        assertNotNull(pointer);
        assertNull(pointer._nextSegment);
        assertEquals("", pointer._matchingPropertyName);
        assertEquals(-1, pointer._matchingElementIndex);
        assertEquals("", pointer._asString);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testCompileWithInvalidInput() {
        JsonPointer pointer = JsonPointer.compile("invalid");
    }
    
    @Test
    public void testCompileWithEmptyInput() {
        JsonPointer pointer = JsonPointer.compile("");
        assertNotNull(pointer);
        assertNull(pointer._nextSegment);
        assertEquals("", pointer._matchingPropertyName);
        assertEquals(-1, pointer._matchingElementIndex);
        assertEquals("", pointer._asString);
    }
    
    @Test
    public void testMatchProperty() {
        JsonPointer pointer = new JsonPointer("fullString", "segment", null);
        JsonPointer result = pointer.matchProperty("segment");
        assertNotNull(result);
        assertNull(result._nextSegment);
        assertEquals("segment", result._matchingPropertyName);
        assertEquals(-1, result._matchingElementIndex);
        assertEquals("fullString", result._asString);
    }
    
    @Test
    public void testMatchPropertyNoMatch() {
        JsonPointer pointer = new JsonPointer("fullString", "segment", null);
        JsonPointer result = pointer.matchProperty("other");
        assertNull(result);
    }
    
    @Test
    public void testMatchElement() {
        JsonPointer pointer = new JsonPointer("fullString", null, null);
        JsonPointer result = pointer.matchElement(0);
        assertNotNull(result);
        assertNull(result._nextSegment);
        assertNull(result._matchingPropertyName);
        assertEquals(0, result._matchingElementIndex);
        assertEquals("fullString", result._asString);
    }
    
    @Test
    public void testMatchElementNoMatch() {
        JsonPointer pointer = new JsonPointer("fullString", null, null);
        JsonPointer result = pointer.matchElement(1);
        assertNull(result);
    }
    
    @Test
    public void testTail() {
        JsonPointer pointer = new JsonPointer("fullString", "segment", null);
        JsonPointer result = pointer.tail();
        assertNull(result._nextSegment);
        assertEquals("segment", result._matchingPropertyName);
        assertEquals(-1, result._matchingElementIndex);
        assertEquals("fullString", result._asString);
    }
    
    @Test
    public void testToString() {
        JsonPointer pointer = new JsonPointer("fullString", "segment", null);
        String result = pointer.toString();
        assertEquals("fullString", result);
    }
    
    @Test
    public void testEquals() {
        JsonPointer pointer1 = new JsonPointer("fullString", "segment", null);
        JsonPointer pointer2 = new JsonPointer("fullString", "segment", null);
        assertTrue(pointer1.equals(pointer2));
    }
    
    @Test
    public void testEqualsSameObject() {
        JsonPointer pointer = new JsonPointer("fullString", "segment", null);
        assertTrue(pointer.equals(pointer));
    }
    
    @Test
    public void testEqualsDifferentClass() {
        JsonPointer pointer = new JsonPointer("fullString", "segment", null);
        assertFalse(pointer.equals("string"));
    }
    
    @Test
    public void testHashCode() {
        JsonPointer pointer = new JsonPointer("fullString", "segment", null);
        int hashCode = pointer.hashCode();
        assertEquals(2319876, hashCode);
    }
}