package com.fasterxml.jackson.core;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class JsonPointerTest {
   
    @Before
    public void setUp() throws Exception {
        // invoke before each test case
    }

    @Test
    public void testJsonPointerConstructorEmpty() {
        JsonPointer jsonPointer = new JsonPointer();
        assertEquals("", jsonPointer._matchingPropertyName);
        assertEquals(-1, jsonPointer._matchingElementIndex);
        assertEquals("", jsonPointer._asString);
    }

    @Test
    public void testJsonPointerConstructorNonEmpty() {
        JsonPointer jsonPointer = new JsonPointer("input", "segment", null);
        assertEquals("segment", jsonPointer._matchingPropertyName);
        assertEquals(-1, jsonPointer._matchingElementIndex);
        assertEquals("input", jsonPointer._asString);
    }

    @Test
    public void testCompileEmptyInput() {
        JsonPointer jsonPointer = JsonPointer.compile("");
        assertEquals("", jsonPointer._matchingPropertyName);
        assertEquals(-1, jsonPointer._matchingElementIndex);
        assertEquals("", jsonPointer._asString);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCompileInvalidInput() {
        JsonPointer jsonPointer = JsonPointer.compile("input");
    }

    @Test
    public void testCompileValidInput() {
        JsonPointer jsonPointer = JsonPointer.compile("/input");
        assertEquals("input", jsonPointer._matchingPropertyName);
        assertEquals(-1, jsonPointer._matchingElementIndex);
        assertEquals("/input", jsonPointer._asString);
    }

    @Test
    public void testValueOf() {
        JsonPointer jsonPointer = JsonPointer.valueOf("/input");
        assertEquals("input", jsonPointer._matchingPropertyName);
        assertEquals(-1, jsonPointer._matchingElementIndex);
        assertEquals("/input", jsonPointer._asString);
    }

    @Test
    public void testMatchPropertyMatch() {
        JsonPointer jsonPointer = new JsonPointer();
        JsonPointer nextSegment = jsonPointer.matchProperty("input");
        assertNull(nextSegment);
    }

    @Test
    public void testMatchPropertyNoMatch() {
        JsonPointer jsonPointer = new JsonPointer("input", "segment", null);
        JsonPointer nextSegment = jsonPointer.matchProperty("wrong");
        assertNull(nextSegment);
    }

    @Test
    public void testMatchElementMatch() {
        JsonPointer jsonPointer = new JsonPointer();
        JsonPointer nextSegment = jsonPointer.matchElement(0);
        assertNull(nextSegment);
    }

    @Test
    public void testMatchElementNoMatch() {
        JsonPointer jsonPointer = new JsonPointer("input", "segment", null);
        JsonPointer nextSegment = jsonPointer.matchElement(1);
        assertNull(nextSegment);
    }

    @Test
    public void testTail() {
        JsonPointer jsonPointer = new JsonPointer("input", "segment", null);
        JsonPointer nextSegment = jsonPointer.tail();
        assertNull(nextSegment);
    }

    @Test
    public void testToString() {
        JsonPointer jsonPointer = new JsonPointer("input", "segment", null);
        assertEquals("input", jsonPointer.toString());
    }

    @Test
    public void testHashCode() {
        JsonPointer jsonPointer1 = new JsonPointer("input", "segment", null);
        JsonPointer jsonPointer2 = new JsonPointer("input", "segment", null);
        assertEquals(jsonPointer1.hashCode(), jsonPointer2.hashCode());
    }

    @Test
    public void testEquals() {
        JsonPointer jsonPointer1 = new JsonPointer("input", "segment", null);
        JsonPointer jsonPointer2 = new JsonPointer("input", "segment", null);
        assertTrue(jsonPointer1.equals(jsonPointer2));
    }
}