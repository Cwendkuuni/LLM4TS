
package com.fasterxml.jackson.core;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class JsonPointerTest {

    @Test
    public void testEmptyJsonPointer() {
        JsonPointer jsonPointer = JsonPointer.compile("");
        assertEquals("", jsonPointer.toString());
        assertEquals("", jsonPointer.getMatchingProperty());
        assertEquals(-1, jsonPointer.getMatchingIndex());
        assertNull(jsonPointer.tail());
    }

    @Test
    public void testJsonPointerWithMatchingSegment() {
        JsonPointer jsonPointer = JsonPointer.compile("/segment");
        assertEquals("/segment", jsonPointer.toString());
        assertEquals("segment", jsonPointer.getMatchingProperty());
        assertEquals(-1, jsonPointer.getMatchingIndex());
        assertNull(jsonPointer.tail());
    }

    @Test
    public void testJsonPointerWithMatchingIndex() {
        JsonPointer jsonPointer = JsonPointer.compile("/5");
        assertEquals("/5", jsonPointer.toString());
        assertEquals(null, jsonPointer.getMatchingProperty());
        assertEquals(5, jsonPointer.getMatchingIndex());
        assertNull(jsonPointer.tail());
    }

    @Test
    public void testJsonPointerWithNestedSegments() {
        JsonPointer jsonPointer = JsonPointer.compile("/foo/bar");
        assertEquals("/foo/bar", jsonPointer.toString());
        assertEquals("foo", jsonPointer.getMatchingProperty());
        assertEquals(-1, jsonPointer.getMatchingIndex());
        assertEquals("/bar", jsonPointer.tail().toString());
    }

    @Test
    public void testJsonPointerWithEscapedSegments() {
        JsonPointer jsonPointer = JsonPointer.compile("/~0~0/~1");
        assertEquals("/~0~0/~1", jsonPointer.toString());
        assertEquals("~0~0", jsonPointer.getMatchingProperty());
        assertEquals(-1, jsonPointer.getMatchingIndex());
        assertEquals("/~1", jsonPointer.tail().toString());
    }
}
