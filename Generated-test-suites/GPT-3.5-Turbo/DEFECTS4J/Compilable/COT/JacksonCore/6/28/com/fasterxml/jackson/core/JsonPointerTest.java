package com.fasterxml.jackson.core;

import com.fasterxml.jackson.core.JsonPointer;
import org.junit.Test;

import static org.junit.Assert.*;

public class JsonPointerTest {

    @Test
    public void testMatches_WithEmptyJsonPointer_ShouldReturnTrue() {
        JsonPointer jsonPointer = JsonPointer.EMPTY;
        assertTrue(jsonPointer.matches());
    }

    @Test
    public void testMatchProperty_WithMatchingProperty_ShouldReturnNextSegment() {
        JsonPointer jsonPointer = new JsonPointer("test", "matchingProperty", JsonPointer.EMPTY);
        JsonPointer nextSegment = jsonPointer.matchProperty("matchingProperty");
        assertNotNull(nextSegment);
        assertEquals(JsonPointer.EMPTY, nextSegment);
    }

    @Test
    public void testMatchProperty_WithNonMatchingProperty_ShouldReturnNull() {
        JsonPointer jsonPointer = new JsonPointer("test", "matchingProperty", JsonPointer.EMPTY);
        JsonPointer nextSegment = jsonPointer.matchProperty("nonMatchingProperty");
        assertNull(nextSegment);
    }

    @Test
    public void testMatchElement_WithMatchingIndex_ShouldReturnNextSegment() {
        JsonPointer jsonPointer = new JsonPointer("test", "0", JsonPointer.EMPTY);
        JsonPointer nextSegment = jsonPointer.matchElement(0);
        assertNotNull(nextSegment);
        assertEquals(JsonPointer.EMPTY, nextSegment);
    }

    @Test
    public void testMatchElement_WithNonMatchingIndex_ShouldReturnNull() {
        JsonPointer jsonPointer = new JsonPointer("test", "0", JsonPointer.EMPTY);
        JsonPointer nextSegment = jsonPointer.matchElement(1);
        assertNull(nextSegment);
    }

    @Test
    public void testTail_WithNonEmptyJsonPointer_ShouldReturnNextSegment() {
        JsonPointer jsonPointer = new JsonPointer("test", "next", JsonPointer.EMPTY);
        JsonPointer nextSegment = jsonPointer.tail();
        assertNotNull(nextSegment);
        assertEquals(JsonPointer.EMPTY, nextSegment);
    }

    @Test
    public void testTail_WithEmptyJsonPointer_ShouldReturnEmptyJsonPointer() {
        JsonPointer jsonPointer = JsonPointer.EMPTY;
        JsonPointer nextSegment = jsonPointer.tail();
        assertEquals(JsonPointer.EMPTY, nextSegment);
    }

    @Test
    public void testToString_WithNonEmptyJsonPointer_ShouldReturnAsString() {
        JsonPointer jsonPointer = new JsonPointer("test", "next", JsonPointer.EMPTY);
        assertEquals("test", jsonPointer.toString());
    }

    @Test
    public void testToString_WithEmptyJsonPointer_ShouldReturnEmptyString() {
        JsonPointer jsonPointer = JsonPointer.EMPTY;
        assertEquals("", jsonPointer.toString());
    }

    @Test
    public void testHashCode_WithEqualJsonPointers_ShouldReturnEqualHashCodes() {
        JsonPointer jsonPointer1 = new JsonPointer("test", "next", JsonPointer.EMPTY);
        JsonPointer jsonPointer2 = new JsonPointer("test", "next", JsonPointer.EMPTY);
        assertEquals(jsonPointer1.hashCode(), jsonPointer2.hashCode());
    }

    @Test
    public void testEquals_WithEqualJsonPointers_ShouldReturnTrue() {
        JsonPointer jsonPointer1 = new JsonPointer("test", "next", JsonPointer.EMPTY);
        JsonPointer jsonPointer2 = new JsonPointer("test", "next", JsonPointer.EMPTY);
        assertEquals(jsonPointer1, jsonPointer2);
    }

    @Test
    public void testEquals_WithDifferentJsonPointers_ShouldReturnFalse() {
        JsonPointer jsonPointer1 = new JsonPointer("test", "next", JsonPointer.EMPTY);
        JsonPointer jsonPointer2 = new JsonPointer("test", "nonMatching", JsonPointer.EMPTY);
        assertNotEquals(jsonPointer1, jsonPointer2);
    }

}
