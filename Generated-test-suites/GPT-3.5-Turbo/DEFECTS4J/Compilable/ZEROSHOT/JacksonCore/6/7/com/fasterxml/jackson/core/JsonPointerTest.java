package com.fasterxml.jackson.core;
import static org.junit.Assert.*;
import org.junit.Test;

public class JsonPointerTest {

    @Test
    public void testEmptyConstructor() {
        JsonPointer pointer = new JsonPointer();
        assertTrue(pointer.matches());
        assertEquals("", pointer.getMatchingProperty());
        assertEquals(-1, pointer.getMatchingIndex());
        assertFalse(pointer.mayMatchProperty());
        assertFalse(pointer.mayMatchElement());
        assertNull(pointer.tail());
    }
    
    @Test
    public void testConstructorWithParameters() {
        JsonPointer pointer = new JsonPointer("fullString", "segment", new JsonPointer());
        assertFalse(pointer.matches());
        assertEquals("segment", pointer.getMatchingProperty());
        assertEquals(-1, pointer.getMatchingIndex());
        assertTrue(pointer.mayMatchProperty());
        assertFalse(pointer.mayMatchElement());
        assertNotNull(pointer.tail());
    }
    
    @Test
    public void testCompileWithEmptyInput() {
        JsonPointer pointer = JsonPointer.compile(null);
        assertTrue(pointer.matches());
        assertEquals("", pointer.getMatchingProperty());
        assertEquals(-1, pointer.getMatchingIndex());
        assertFalse(pointer.mayMatchProperty());
        assertFalse(pointer.mayMatchElement());
        assertNull(pointer.tail());
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testCompileWithInvalidInput() {
        JsonPointer pointer = JsonPointer.compile("invalid");
    }
    
    @Test
    public void testCompileWithValidInput() {
        JsonPointer pointer = JsonPointer.compile("/segment");
        assertFalse(pointer.matches());
        assertEquals("segment", pointer.getMatchingProperty());
        assertEquals(-1, pointer.getMatchingIndex());
        assertTrue(pointer.mayMatchProperty());
        assertFalse(pointer.mayMatchElement());
        assertNotNull(pointer.tail());
    }
    
    @Test
    public void testValueOf() {
        JsonPointer pointer = JsonPointer.valueOf("/segment");
        assertFalse(pointer.matches());
        assertEquals("segment", pointer.getMatchingProperty());
        assertEquals(-1, pointer.getMatchingIndex());
        assertTrue(pointer.mayMatchProperty());
        assertFalse(pointer.mayMatchElement());
        assertNotNull(pointer.tail());
    }
    
    @Test
    public void testToString() {
        JsonPointer pointer = new JsonPointer("fullString", "segment", new JsonPointer());
        assertEquals("fullString", pointer.toString());
    }
    
    @Test
    public void testHashCode() {
        JsonPointer pointer1 = new JsonPointer("fullString", "segment", new JsonPointer());
        JsonPointer pointer2 = new JsonPointer("fullString", "segment", new JsonPointer());
        assertTrue(pointer1.hashCode() == pointer2.hashCode());
    }
    
    @Test
    public void testEqualsWithSameObject() {
        JsonPointer pointer = new JsonPointer("fullString", "segment", new JsonPointer());
        assertTrue(pointer.equals(pointer));
    }
    
    @Test
    public void testEqualsWithDifferentObject() {
        JsonPointer pointer1 = new JsonPointer("fullString", "segment", new JsonPointer());
        JsonPointer pointer2 = new JsonPointer("fullString", "segment", new JsonPointer());
        assertTrue(pointer1.equals(pointer2));
    }
    
    @Test
    public void testEqualsWithNull() {
        JsonPointer pointer = new JsonPointer("fullString", "segment", new JsonPointer());
        assertFalse(pointer.equals(null));
    }
    
    @Test
    public void testMatchProperty() {
        JsonPointer pointer1 = new JsonPointer("fullString", "segment", new JsonPointer());
        JsonPointer pointer2 = pointer1.matchProperty("segment");
        assertNotNull(pointer2);
    }
    
    @Test
    public void testMatchElement() {
        JsonPointer pointer1 = new JsonPointer("fullString", "segment", new JsonPointer());
        JsonPointer pointer2 = pointer1.matchElement(0);
        assertNull(pointer2);
    }
}