package com.fasterxml.jackson.core;

import org.junit.Test;
import static org.junit.Assert.*;

public class JsonPointerTest {

    @Test
    public void testEmptyConstructor() {
        JsonPointer pointer = new JsonPointer();
        assertEquals(null, pointer._nextSegment);
        assertEquals("", pointer._matchingPropertyName);
        assertEquals(-1, pointer._matchingElementIndex);
        assertEquals("", pointer._asString);
    }
    
    @Test
    public void testConstructor() {
        JsonPointer pointer = new JsonPointer("fullString", "segment", null);
        assertEquals("fullString", pointer._asString);
        assertEquals("segment", pointer._matchingPropertyName);
        assertEquals(-1, pointer._matchingElementIndex);
        assertEquals(null, pointer._nextSegment);
    }
    
    @Test
    public void testCompileWithNullInput() {
        try {
            JsonPointer pointer = JsonPointer.compile(null);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertEquals("Invalid input: JSON Pointer expression must start with '/': null", e.getMessage());
        }
    }
    
    @Test
    public void testCompileWithEmptyInput() {
        JsonPointer pointer = JsonPointer.compile("");
        assertEquals(JsonPointer.EMPTY, pointer);
    }
    
    @Test
    public void testCompileWithInvalidInput() {
        try {
            JsonPointer pointer = JsonPointer.compile("invalid");
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertEquals("Invalid input: JSON Pointer expression must start with '/': \"invalid\"", e.getMessage());
        }
    }
    
    @Test
    public void testValueOf() {
        JsonPointer pointer = JsonPointer.valueOf("/test");
        assertEquals("/test", pointer._asString);
        assertEquals("test", pointer._matchingPropertyName);
        assertEquals(-1, pointer._matchingElementIndex);
        assertEquals(null, pointer._nextSegment);
    }
    
    @Test
    public void testMatches() {
        JsonPointer pointer = new JsonPointer();
        assertTrue(pointer.matches());
    }
    
    @Test
    public void testGetMatchingProperty() {
        JsonPointer pointer = new JsonPointer("fullString", "segment", null);
        assertEquals("segment", pointer.getMatchingProperty());
    }
    
    @Test
    public void testGetMatchingIndex() {
        JsonPointer pointer = new JsonPointer("fullString", "segment", null);
        assertEquals(-1, pointer.getMatchingIndex());
    }
    
    @Test
    public void testMayMatchProperty() {
        JsonPointer pointer = new JsonPointer("fullString", "segment", null);
        assertTrue(pointer.mayMatchProperty());
    }
    
    @Test
    public void testMayMatchElement() {
        JsonPointer pointer = new JsonPointer("fullString", "segment", null);
        assertFalse(pointer.mayMatchElement());
    }
    
    @Test
    public void testMatchProperty() {
        JsonPointer pointer = new JsonPointer("fullString", "segment", null);
        JsonPointer result = pointer.matchProperty("segment");
        assertEquals(null, result);
    }
    
    @Test
    public void testMatchElement() {
        JsonPointer pointer = new JsonPointer("fullString", "segment", null);
        JsonPointer result = pointer.matchElement(0);
        assertEquals(null, result);
    }
    
    @Test
    public void testTail() {
        JsonPointer pointer = new JsonPointer("fullString", "segment", null);
        assertEquals(null, pointer.tail());
    }
    
    @Test
    public void testToString() {
        JsonPointer pointer = new JsonPointer("fullString", "segment", null);
        assertEquals("fullString", pointer.toString());
    }
    
    @Test
    public void testHashCode() {
        JsonPointer pointer = new JsonPointer("fullString", "segment", null);
        assertEquals("fullString".hashCode(), pointer.hashCode());
    }
    
    @Test
    public void testEqualsWithNull() {
        JsonPointer pointer = new JsonPointer("fullString", "segment", null);
        assertFalse(pointer.equals(null));
    }
    
    @Test
    public void testEqualsWithInvalidObject() {
        JsonPointer pointer = new JsonPointer("fullString", "segment", null);
        assertFalse(pointer.equals(new Object()));
    }
    
    @Test
    public void testEqualsWithSameObject() {
        JsonPointer pointer = new JsonPointer("fullString", "segment", null);
        assertTrue(pointer.equals(pointer));
    }
    
    @Test
    public void testEqualsWithDifferentObject() {
        JsonPointer pointer1 = new JsonPointer("fullString", "segment", null);
        JsonPointer pointer2 = new JsonPointer("differentString", "segment", null);
        assertFalse(pointer1.equals(pointer2));
    }

    @Test
    public void test_parseIndexWithEmptyString() {
        int result = JsonPointer._parseIndex("");
        assertEquals(-1, result);
    }
    
    @Test
    public void test_parseIndexWithInvalidStringTooLong() {
        int result = JsonPointer._parseIndex("12345678900");
        assertEquals(-1, result);
    }
    
    @Test
    public void test_parseIndexWithInvalidStringNonDigit() {
        int result = JsonPointer._parseIndex("12345a");
        assertEquals(-1, result);
    }
    
    @Test
    public void test_parseIndexWithValidString() {
        int result = JsonPointer._parseIndex("12345");
        assertEquals(12345, result);
    }
    
    @Test
    public void test_parseTailWithNoSegments() {
        JsonPointer pointer = JsonPointer._parseTail("/test");
        assertEquals("/test", pointer._asString);
        assertEquals("test", pointer._matchingPropertyName);
        assertEquals(-1, pointer._matchingElementIndex);
        assertEquals(null, pointer._nextSegment);
    }
    
    @Test
    public void test_parseTailWithSegments() {
        JsonPointer pointer = JsonPointer._parseTail("/test/next");
        assertEquals("/test", pointer._asString);
        assertEquals("test", pointer._matchingPropertyName);
        assertEquals(-1, pointer._matchingElementIndex);
        pointer = pointer._nextSegment;
        assertEquals("/next", pointer._asString);
        assertEquals("next", pointer._matchingPropertyName);
        assertEquals(-1, pointer._matchingElementIndex);
        assertEquals(null, pointer._nextSegment);
    }
    
    @Test
    public void test_parseQuotedTail() {
        JsonPointer pointer = JsonPointer._parseQuotedTail("/test/~0next");
        assertEquals("/test", pointer._asString);
        assertEquals("test", pointer._matchingPropertyName);
        assertEquals(-1, pointer._matchingElementIndex);
        pointer = pointer._nextSegment;
        assertEquals("/~next", pointer._asString);
        assertEquals("~next", pointer._matchingPropertyName);
        assertEquals(-1, pointer._matchingElementIndex);
        assertEquals(null, pointer._nextSegment);
    }
    
    @Test
    public void test_appendEscape() {
        StringBuilder sb = new StringBuilder();
        JsonPointer._appendEscape(sb, '0');
        assertEquals("~0", sb.toString());
        sb.setLength(0);
        JsonPointer._appendEscape(sb, '1');
        assertEquals("~1", sb.toString());
        sb.setLength(0);
        JsonPointer._appendEscape(sb, '2');
        assertEquals("~~2", sb.toString());
    }
}