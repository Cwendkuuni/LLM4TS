package com.fasterxml.jackson.core;

import org.junit.Test;
import static org.junit.Assert.*;

public class JsonPointerTest {

    @Test
    public void testEmptyJsonPointer() {
        JsonPointer jsonPointer = JsonPointer.EMPTY;
        assertTrue(jsonPointer.matches());
        assertEquals("", jsonPointer.getMatchingProperty());
        assertEquals(-1, jsonPointer.getMatchingIndex());
    }

    @Test
    public void testJsonPointerConstruction() {
        JsonPointer jsonPointer = new JsonPointer("test", "segment1");
        assertFalse(jsonPointer.matches());
        assertEquals("segment1", jsonPointer.getMatchingProperty());
        assertEquals(-1, jsonPointer.getMatchingIndex());
        assertNull(jsonPointer.tail());

        JsonPointer nextSegment = new JsonPointer("test", "segment2", jsonPointer);
        assertFalse(nextSegment.matches());
        assertEquals("segment2", nextSegment.getMatchingProperty());
        assertEquals(-1, nextSegment.getMatchingIndex());
        assertSame(jsonPointer, nextSegment.tail());
    }

    @Test
    public void testCompileValidJsonPointer() {
        String jsonPointerStr = "/a/b/c";
        JsonPointer jsonPointer = JsonPointer.compile(jsonPointerStr);
        assertFalse(jsonPointer.matches());
        assertEquals("a", jsonPointer.getMatchingProperty());
        assertEquals(-1, jsonPointer.getMatchingIndex());
        assertFalse(jsonPointer.tail().matches());
        assertEquals("b", jsonPointer.tail().getMatchingProperty());
        assertEquals(-1, jsonPointer.tail().getMatchingIndex());
        assertFalse(jsonPointer.tail().tail().matches());
        assertEquals("c", jsonPointer.tail().tail().getMatchingProperty());
        assertEquals(-1, jsonPointer.tail().tail().getMatchingIndex());
    }

    @Test (expected = IllegalArgumentException.class)
    public void testCompileInvalidJsonPointer() {
        String jsonPointerStr = "a/b/c";
        JsonPointer.compile(jsonPointerStr);
    }

    @Test
    public void testValueOf() {
        String jsonPointerStr = "/a/b/c";
        JsonPointer jsonPointer = JsonPointer.valueOf(jsonPointerStr);
        assertFalse(jsonPointer.matches());
        assertEquals("a", jsonPointer.getMatchingProperty());
        assertEquals(-1, jsonPointer.getMatchingIndex());
        assertFalse(jsonPointer.tail().matches());
        assertEquals("b", jsonPointer.tail().getMatchingProperty());
        assertEquals(-1, jsonPointer.tail().getMatchingIndex());
        assertFalse(jsonPointer.tail().tail().matches());
        assertEquals("c", jsonPointer.tail().tail().getMatchingProperty());
        assertEquals(-1, jsonPointer.tail().tail().getMatchingIndex());
    }

    @Test
    public void testMatches() {
        JsonPointer jsonPointer1 = JsonPointer.valueOf("/a/b/c");
        assertFalse(jsonPointer1.matches());

        JsonPointer jsonPointer2 = JsonPointer.valueOf("/");
        assertTrue(jsonPointer2.matches());
    }

    @Test
    public void testGetMatchingProperty() {
        JsonPointer jsonPointer = JsonPointer.valueOf("/a/b/c");
        assertEquals("a", jsonPointer.getMatchingProperty());
        assertEquals("b", jsonPointer.tail().getMatchingProperty());
        assertEquals("c", jsonPointer.tail().tail().getMatchingProperty());
    }

    @Test
    public void testGetMatchingIndex() {
        JsonPointer jsonPointer = JsonPointer.valueOf("/0/1/2");
        assertEquals(0, jsonPointer.getMatchingIndex());
        assertEquals(1, jsonPointer.tail().getMatchingIndex());
        assertEquals(2, jsonPointer.tail().tail().getMatchingIndex());
    }

    @Test
    public void testMayMatchProperty() {
        JsonPointer jsonPointer1 = JsonPointer.valueOf("/a");
        assertTrue(jsonPointer1.mayMatchProperty());

        JsonPointer jsonPointer2 = JsonPointer.valueOf("/0");
        assertFalse(jsonPointer2.mayMatchProperty());
    }

    @Test
    public void testMayMatchElement() {
        JsonPointer jsonPointer1 = JsonPointer.valueOf("/0");
        assertTrue(jsonPointer1.mayMatchElement());

        JsonPointer jsonPointer2 = JsonPointer.valueOf("/a");
        assertFalse(jsonPointer2.mayMatchElement());
    }

    @Test
    public void testMatchProperty() {
        JsonPointer jsonPointer = JsonPointer.valueOf("/a/b/c");
        JsonPointer match1 = jsonPointer.matchProperty("a");
        assertFalse(match1.matches());
        assertEquals("b", match1.getMatchingProperty());
        assertEquals("c", match1.tail().getMatchingProperty());

        JsonPointer match2 = jsonPointer.matchProperty("x");
        assertNull(match2);
    }

    @Test
    public void testMatchElement() {
        JsonPointer jsonPointer = JsonPointer.valueOf("/0/1/2");
        JsonPointer match1 = jsonPointer.matchElement(0);
        assertFalse(match1.matches());
        assertEquals(1, match1.getMatchingIndex());
        assertEquals(2, match1.tail().getMatchingIndex());

        JsonPointer match2 = jsonPointer.matchElement(5);
        assertNull(match2);
    }

    @Test
    public void testTail() {
        JsonPointer jsonPointer = JsonPointer.valueOf("/a/b/c");
        assertFalse(jsonPointer.tail().matches());
        assertEquals("b", jsonPointer.tail().getMatchingProperty());

        assertFalse(jsonPointer.tail().tail().matches());
        assertEquals("c", jsonPointer.tail().tail().getMatchingProperty());

        assertNull(jsonPointer.tail().tail().tail());
    }

    @Test
    public void testToString() {
        JsonPointer jsonPointer = JsonPointer.valueOf("/a/b/c");
        assertEquals("/a/b/c", jsonPointer.toString());

        JsonPointer jsonPointer2 = new JsonPointer("/", "");
        assertEquals("/", jsonPointer2.toString());
    }

    @Test
    public void testHashCode() {
        JsonPointer jsonPointer1 = JsonPointer.valueOf("/a/b/c");
        JsonPointer jsonPointer2 = JsonPointer.valueOf("/a/b/c");
        assertEquals(jsonPointer1.hashCode(), jsonPointer2.hashCode());
    }

    @Test
    public void testEquals() {
        JsonPointer jsonPointer1 = JsonPointer.valueOf("/a/b/c");
        JsonPointer jsonPointer2 = JsonPointer.valueOf("/a/b/c");
        assertTrue(jsonPointer1.equals(jsonPointer2));

        JsonPointer jsonPointer3 = JsonPointer.valueOf("/a/b");
        assertFalse(jsonPointer1.equals(jsonPointer3));

        assertFalse(jsonPointer1.equals(null));
        assertFalse(jsonPointer1.equals("string"));
    }

    @Test
    public void testPrivateParseIndex() {
        int index = JsonPointer._parseIndex("-10");
        assertEquals(-1, index);

        index = JsonPointer._parseIndex("10");
        assertEquals(10, index);

        index = JsonPointer._parseIndex(String.valueOf(Integer.MAX_VALUE + 1L));
        assertEquals(-1, index);
    }

    @Test
    public void testParseQuotedTail() {
        JsonPointer jsonPointer = JsonPointer._parseQuotedTail("~2", 0);
        assertEquals("~", jsonPointer.toString());

        JsonPointer jsonPointer2 = JsonPointer._parseQuotedTail("~0~1~1", 0);
        assertEquals("~~/", jsonPointer2.toString());
    }

    @Test
    public void testAppendEscape() {
        StringBuilder stringBuilder = new StringBuilder();
        JsonPointer._appendEscape(stringBuilder, '0');
        assertEquals("~0", stringBuilder.toString());

        StringBuilder stringBuilder2 = new StringBuilder();
        JsonPointer._appendEscape(stringBuilder2, '1');
        assertEquals("~1", stringBuilder2.toString());
    }

}