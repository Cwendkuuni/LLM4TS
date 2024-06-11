package com.fasterxml.jackson.core;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class JsonPointerTest {

    // Test empty constructor
    @Test
    public void testEmptyConstructor() {
        JsonPointer jsonPointer = new JsonPointer();
        assertTrue(jsonPointer.matches());
        assertEquals("", jsonPointer.getMatchingProperty());
        assertEquals(-1, jsonPointer.getMatchingIndex());
        assertFalse(jsonPointer.mayMatchProperty());
        assertFalse(jsonPointer.mayMatchElement());
        assertNull(jsonPointer.matchProperty("name"));
        assertNull(jsonPointer.matchElement(0));
        assertNull(jsonPointer.tail());
    }
    
    // Test constructor with parameters
    @Test
    public void testConstructorWithParameters() {
        JsonPointer jsonPointer = new JsonPointer("fullString", "segment", null);
        assertFalse(jsonPointer.matches());
        assertEquals("segment", jsonPointer.getMatchingProperty());
        assertEquals(-1, jsonPointer.getMatchingIndex());
        assertTrue(jsonPointer.mayMatchProperty());
        assertFalse(jsonPointer.mayMatchElement());
        assertNull(jsonPointer.matchProperty("name"));
        assertNull(jsonPointer.matchElement(0));
        assertNull(jsonPointer.tail());
    }
    
    // Test compile method
    @Test
    public void testCompile() {
        JsonPointer jsonPointer = JsonPointer.compile("/test");
        assertFalse(jsonPointer.matches());
        assertEquals("test", jsonPointer.getMatchingProperty());
        assertEquals(-1, jsonPointer.getMatchingIndex());
        assertTrue(jsonPointer.mayMatchProperty());
        assertFalse(jsonPointer.mayMatchElement());
        assertNull(jsonPointer.matchProperty("name"));
        assertNull(jsonPointer.matchElement(0));
        assertNull(jsonPointer.tail());
    }
    
    // Test valueOf method
    @Test
    public void testValueOf() {
        JsonPointer jsonPointer = JsonPointer.valueOf("/test");
        assertFalse(jsonPointer.matches());
        assertEquals("test", jsonPointer.getMatchingProperty());
        assertEquals(-1, jsonPointer.getMatchingIndex());
        assertTrue(jsonPointer.mayMatchProperty());
        assertFalse(jsonPointer.mayMatchElement());
        assertNull(jsonPointer.matchProperty("name"));
        assertNull(jsonPointer.matchElement(0));
        assertNull(jsonPointer.tail());
    }

    // Test toString method
    @Test
    public void testToString() {
        JsonPointer jsonPointer = JsonPointer.compile("/test");
        assertEquals("/test", jsonPointer.toString());
    }

    // Test hashCode method
    @Test
    public void testHashCode() {
        JsonPointer jsonPointer1 = JsonPointer.compile("/test");
        JsonPointer jsonPointer2 = JsonPointer.compile("/test");
        assertEquals(jsonPointer1.hashCode(), jsonPointer2.hashCode());
    }

    // Test equals method
    @Test
    public void testEquals() {
        JsonPointer jsonPointer1 = JsonPointer.compile("/test");
        JsonPointer jsonPointer2 = JsonPointer.compile("/test");
        JsonPointer jsonPointer3 = JsonPointer.compile("/name");
        assertTrue(jsonPointer1.equals(jsonPointer2));
        assertFalse(jsonPointer1.equals(jsonPointer3));
    }
}