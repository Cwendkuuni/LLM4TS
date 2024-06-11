package com.fasterxml.jackson.core;

import com.fasterxml.jackson.core.io.NumberInput;
import org.junit.Test;

import static org.junit.Assert.*;

public class JsonPointerTest {

    @Test
    public void testMatches() {
        JsonPointer jsonPointer = JsonPointer.EMPTY;
        assertTrue(jsonPointer.matches());
        jsonPointer = JsonPointer.compile("/0");
        assertFalse(jsonPointer.matches());
    }

    @Test
    public void testGetMatchingProperty() {
        JsonPointer jsonPointer = JsonPointer.EMPTY;
        assertEquals("", jsonPointer.getMatchingProperty());
        jsonPointer = JsonPointer.compile("/property");
        assertEquals("property", jsonPointer.getMatchingProperty());
    }

    @Test
    public void testGetMatchingIndex() {
        JsonPointer jsonPointer = JsonPointer.EMPTY;
        assertEquals(-1, jsonPointer.getMatchingIndex());
        jsonPointer = JsonPointer.compile("/0");
        assertEquals(0, jsonPointer.getMatchingIndex());
    }

    @Test
    public void testMayMatchProperty() {
        JsonPointer jsonPointer = JsonPointer.EMPTY;
        assertTrue(jsonPointer.mayMatchProperty());
        jsonPointer = JsonPointer.compile("/property");
        assertTrue(jsonPointer.mayMatchProperty());
        jsonPointer = JsonPointer.compile("/0");
        assertFalse(jsonPointer.mayMatchProperty());
    }

    @Test
    public void testMayMatchElement() {
        JsonPointer jsonPointer = JsonPointer.EMPTY;
        assertFalse(jsonPointer.mayMatchElement());
        jsonPointer = JsonPointer.compile("/property");
        assertFalse(jsonPointer.mayMatchElement());
        jsonPointer = JsonPointer.compile("/0");
        assertTrue(jsonPointer.mayMatchElement());
    }

    @Test
    public void testMatchProperty() {
        JsonPointer jsonPointer = JsonPointer.EMPTY;
        JsonPointer result = jsonPointer.matchProperty("property");
        assertNull(result);
        jsonPointer = JsonPointer.compile("/property");
        result = jsonPointer.matchProperty("property");
        assertNull(result);
        result = jsonPointer.matchProperty("wrong");
        assertNull(result);
        jsonPointer = JsonPointer.compile("/0");
        result = jsonPointer.matchProperty("property");
        assertNull(result);
    }

    @Test
    public void testMatchElement() {
        JsonPointer jsonPointer = JsonPointer.EMPTY;
        JsonPointer result = jsonPointer.matchElement(0);
        assertNull(result);
        jsonPointer = JsonPointer.compile("/property");
        result = jsonPointer.matchElement(0);
        assertNull(result);
        result = jsonPointer.matchElement(1);
        assertNull(result);
        jsonPointer = JsonPointer.compile("/0");
        result = jsonPointer.matchElement(0);
        assertNotNull(result);
    }

    @Test
    public void testTail() {
        JsonPointer jsonPointer = JsonPointer.compile("/");
        JsonPointer result = jsonPointer.tail();
        assertNull(result);
    }

    @Test
    public void testToString() {
        JsonPointer jsonPointer = JsonPointer.compile("/");
        assertEquals("/", jsonPointer.toString());
    }

    @Test
    public void testHashCode() {
        JsonPointer jsonPointer = JsonPointer.compile("/");
        assertEquals("/".hashCode(), jsonPointer.hashCode());
    }

    @Test
    public void testEquals() {
        JsonPointer jsonPointer1 = JsonPointer.compile("/");
        JsonPointer jsonPointer2 = JsonPointer.compile("/");
        assertTrue(jsonPointer1.equals(jsonPointer2));
    }
    
    @Test
    public void testCompile() {
        JsonPointer jsonPointer = JsonPointer.compile("/");
        assertNotNull(jsonPointer);
    }

    @Test
    public void testValueOf() {
        JsonPointer jsonPointer = JsonPointer.valueOf("/");
        assertNotNull(jsonPointer);
    }

    @Test
    public void test_parseIndex() {
        int result = JsonPointer._parseIndex("0");
        assertEquals(0, result);
    }

    @Test
    public void test_parseTail() {
        JsonPointer jsonPointer = JsonPointer._parseTail("/");
        assertNotNull(jsonPointer);
    }

    @Test
    public void test_parseQuotedTail() {
        JsonPointer jsonPointer = JsonPointer._parseQuotedTail("~0");
        assertNotNull(jsonPointer);
    }

    @Test
    public void test_appendEscape() {
        StringBuilder sb = new StringBuilder();
        JsonPointer._appendEscape(sb, '0');
        assertEquals("~0", sb.toString());
    }
}