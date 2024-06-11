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
    public void testNonEmptyConstructor() {
        JsonPointer pointer = new JsonPointer("fullString", "segment", null);
        assertEquals(null, pointer._nextSegment);
        assertEquals("segment", pointer._matchingPropertyName);
        assertEquals(-1, pointer._matchingElementIndex);
        assertEquals("fullString", pointer._asString);
    }

    @Test
    public void testCompile() {
        JsonPointer pointer = JsonPointer.compile("/test");
        assertEquals(null, pointer._nextSegment);
        assertEquals("test", pointer._matchingPropertyName);
        assertEquals(-1, pointer._matchingElementIndex);
        assertEquals("/test", pointer._asString);
    }

    @Test
    public void testValueOf() {
        JsonPointer pointer = JsonPointer.valueOf("/test");
        assertEquals(null, pointer._nextSegment);
        assertEquals("test", pointer._matchingPropertyName);
        assertEquals(-1, pointer._matchingElementIndex);
        assertEquals("/test", pointer._asString);
    }

    @Test
    public void testMatchProperty() {
        JsonPointer pointer = new JsonPointer("fullString", "segment", null);
        JsonPointer result = pointer.matchProperty("segment");
        assertEquals(null, result._nextSegment);
        assertEquals("segment", result._matchingPropertyName);
        assertEquals(-1, result._matchingElementIndex);
        assertEquals("fullString", result._asString);
    }

    @Test
    public void testMatchElement() {
        JsonPointer pointer = new JsonPointer("fullString", "segment", null);
        JsonPointer result = pointer.matchElement(0);
        assertEquals(null, result._nextSegment);
        assertEquals("segment", result._matchingPropertyName);
        assertEquals(-1, result._matchingElementIndex);
        assertEquals("fullString", result._asString);
    }

    @Test
    public void testTail() {
        JsonPointer pointer = new JsonPointer("fullString", "segment", null);
        JsonPointer result = pointer.tail();
        assertEquals(null, result._nextSegment);
        assertEquals("segment", result._matchingPropertyName);
        assertEquals(-1, result._matchingElementIndex);
        assertEquals("fullString", result._asString);
    }

}