package com.fasterxml.jackson.core;

import org.junit.Test;
import static org.junit.Assert.*;

public class JsonPointerTest {

    @Test
    public void testEmptyPointer() {
        JsonPointer pointer = JsonPointer.EMPTY;
        
        // matches() should return true for an empty pointer
        assertTrue(pointer.matches());
        
        // getMatchingPropertyName() should return an empty string for an empty pointer
        assertEquals("", pointer.getMatchingProperty());
        
        // getMatchingIndex() should return -1 for an empty pointer
        assertEquals(-1, pointer.getMatchingIndex());
        
        // mayMatchProperty() should return false for an empty pointer
        assertFalse(pointer.mayMatchProperty());
        
        // mayMatchElement() should return false for an empty pointer
        assertFalse(pointer.mayMatchElement());
        
        // tail() should return null for an empty pointer
        assertNull(pointer.tail());
    }

    @Test
    public void testNonEmptyPointer() {
        String fullString = "/fullString";
        String segment = "fullString";
        JsonPointer next = JsonPointer.EMPTY;
        
        JsonPointer pointer = new JsonPointer(fullString, segment, next);
        
        // matches() should return false for a non-empty pointer
        assertFalse(pointer.matches());
        
        // getMatchingPropertyName() should return the matching property name
        assertEquals(segment, pointer.getMatchingProperty());
        
        // getMatchingIndex() should return -1 for a non-empty pointer
        assertEquals(-1, pointer.getMatchingIndex());
        
        // mayMatchProperty() should return true for a non-empty pointer
        assertTrue(pointer.mayMatchProperty());
        
        // mayMatchElement() should return false for a non-empty pointer
        assertFalse(pointer.mayMatchElement());
        
        // tail() should return the next segment
        assertEquals(next, pointer.tail());
    }

    @Test
    public void testMatchProperty() {
        String fullString = "/fullString/segment";
        
        JsonPointer pointer1 = JsonPointer.compile(fullString);
        JsonPointer pointer2 = pointer1.matchProperty("fullString");
        
        // matchProperty() should return null if the next segment does not match the given string
        assertNull(pointer2);
        
        pointer2 = pointer1.matchProperty("segment");
        
        // matchProperty() should return the next segment if the next segment matches the given string
        assertEquals("segment", pointer2.getMatchingProperty());
        
        // matches() should return false for the next segment
        assertFalse(pointer2.matches());
        
    }

    @Test
    public void testMatchElement() {
        String fullString = "/fullString/1";
        
        JsonPointer pointer1 = JsonPointer.compile(fullString);
        JsonPointer pointer2 = pointer1.matchElement(0);
        
        // matchElement() should return null if the next segment index is different from the given index
        assertNull(pointer2);
        
        pointer2 = pointer1.matchElement(1);
        
        // matchElement() should return the next segment if the next segment index matches the given index
        assertEquals(1, pointer2.getMatchingIndex());
        
        // matches() should return false for the next segment
        assertFalse(pointer2.matches());
    }

    @Test
    public void testToString() {
        String fullString = "/fullString";
        
        JsonPointer pointer = JsonPointer.compile(fullString);
        
        // toString() should return the fullString
        assertEquals(fullString, pointer.toString());
    }

    @Test
    public void testEquals() {
        String fullString1 = "/fullString";
        String fullString2 = "/fullString";
        
        JsonPointer pointer1 = JsonPointer.compile(fullString1);
        JsonPointer pointer2 = JsonPointer.compile(fullString2);
        
        // equals() should return true for two pointers with the same fullString
        assertTrue(pointer1.equals(pointer2));
        
        String fullString3 = "/differentString";
        
        JsonPointer pointer3 = JsonPointer.compile(fullString3);
        
        // equals() should return false for two pointers with different fullStrings
        assertFalse(pointer1.equals(pointer3));
    }

    @Test
    public void testCompile() {
        String input1 = "";
        String input2 = "/fullString";
        String input3 = "/full~String";
        
        JsonPointer pointer1 = JsonPointer.compile(input1);
        JsonPointer pointer2 = JsonPointer.compile(input2);
        JsonPointer pointer3 = JsonPointer.compile(input3);
        
        // compile() should return an empty pointer for an empty input
        assertEquals(JsonPointer.EMPTY, pointer1);
        
        // compile() should return a non-empty pointer for a non-empty input
        assertNotEquals(JsonPointer.EMPTY, pointer2);
        
        // compile() should handle escaped characters correctly
        assertNotEquals(JsonPointer.EMPTY, pointer3);
    }
}