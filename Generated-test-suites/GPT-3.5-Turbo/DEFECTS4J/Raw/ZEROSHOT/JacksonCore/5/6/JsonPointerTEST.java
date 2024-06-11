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
    public void testParametrizedConstructor() {
        JsonPointer pointer = new JsonPointer("fullString", "segment", new JsonPointer());
        assertEquals("", pointer._matchingPropertyName);
        assertEquals(-1, pointer._matchingElementIndex);
        assertEquals("fullString", pointer._asString);
        assertNotNull(pointer._nextSegment);
    }

    @Test
    public void testCompile() {
        JsonPointer pointer = JsonPointer.compile("/test");
        assertNotNull(pointer);
        assertEquals("test", pointer.getMatchingProperty());

        pointer = JsonPointer.compile("/");
        assertNotNull(pointer);
        assertEquals("", pointer.getMatchingProperty());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCompileInvalidInput() {
        JsonPointer pointer = JsonPointer.compile("test");
    }

    @Test
    public void testValueOf() {
        JsonPointer pointer = JsonPointer.valueOf("/test");
        assertNotNull(pointer);
        assertEquals("test", pointer.getMatchingProperty());
    }

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
        assertTrue(pointer.mayMatchProperty());
    }

    @Test
    public void testMayMatchElement() {
        JsonPointer pointer = new JsonPointer();
        assertFalse(pointer.mayMatchElement());
    }

    @Test
    public void testMatchProperty() {
        JsonPointer pointer = new JsonPointer("/test", "test", new JsonPointer());
        JsonPointer result = pointer.matchProperty("test");
        assertNotNull(result);
        assertEquals("", result.getMatchingProperty());
    }

    @Test
    public void testMatchElement() {
        JsonPointer pointer = new JsonPointer("/0", "0", new JsonPointer());
        JsonPointer result = pointer.matchElement(0);
        assertNotNull(result);
        assertEquals("", result.getMatchingProperty());
    }

    @Test
    public void testTail() {
        JsonPointer pointer = new JsonPointer("fullString", "segment", new JsonPointer());
        assertNotNull(pointer.tail());
        assertEquals("", pointer.tail().getMatchingProperty());
    }

    @Test
    public void testToString() {
        JsonPointer pointer = new JsonPointer("fullString", "segment", new JsonPointer());
        assertEquals("fullString", pointer.toString());
    }

    @Test
    public void testHashCode() {
        JsonPointer pointer1 = new JsonPointer();
        JsonPointer pointer2 = new JsonPointer();
        assertEquals(pointer1.hashCode(), pointer2.hashCode());
    }

    @Test
    public void testEquals() {
        JsonPointer pointer1 = new JsonPointer();
        JsonPointer pointer2 = new JsonPointer();
        assertTrue(pointer1.equals(pointer2));
    }

    @Test
    public void testParseIndex() {
        int result = JsonPointer._parseIndex("0");
        assertEquals(0, result);

        result = JsonPointer._parseIndex("1");
        assertEquals(1, result);

        result = JsonPointer._parseIndex("100");
        assertEquals(100, result);

        result = JsonPointer._parseIndex("1000000000");
        assertEquals(1000000000, result);

        result = JsonPointer._parseIndex("10000000000");
        assertEquals(-1, result);
    }

    @Test
    public void testParseTail() {
        JsonPointer pointer = JsonPointer._parseTail("/test");
        assertNotNull(pointer);
        assertEquals("test", pointer.getMatchingProperty());

        pointer = JsonPointer._parseTail("/0");
        assertNotNull(pointer);
        assertEquals("0", pointer.getMatchingProperty());
    }

    @Test
    public void testParseQuotedTail() {
        JsonPointer pointer = JsonPointer._parseQuotedTail("/~1test", 2);
        assertNotNull(pointer);
        assertEquals("/test", pointer.getMatchingProperty());

        pointer = JsonPointer._parseQuotedTail("/~0test", 2);
        assertNotNull(pointer);
        assertEquals("/~test", pointer.getMatchingProperty());
    }

    @Test
    public void testAppendEscape() {
        StringBuilder sb = new StringBuilder();
        JsonPointer._appendEscape(sb, '0');
        assertEquals("~0", sb.toString());

        sb = new StringBuilder();
        JsonPointer._appendEscape(sb, '1');
        assertEquals("~1", sb.toString());

        sb = new StringBuilder();
        JsonPointer._appendEscape(sb, '2');
        assertEquals("~2", sb.toString());
    }
}