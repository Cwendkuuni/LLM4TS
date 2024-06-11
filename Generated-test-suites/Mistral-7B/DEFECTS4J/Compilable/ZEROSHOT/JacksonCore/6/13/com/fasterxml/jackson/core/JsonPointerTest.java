package com.fasterxml.jackson.core;

import static org.junit.Assert.*;

import org.junit.Test;

import com.fasterxml.jackson.core.JsonPointer;

public class JsonPointerTest {

    @Test
    public void testEmptyPointer() {
        JsonPointer jsonPointer = JsonPointer.EMPTY;
        assertTrue(jsonPointer.matches());
        assertEquals("", jsonPointer.getMatchingProperty());
        assertEquals(-1, jsonPointer.getMatchingIndex());
        assertNull(jsonPointer.tail());
        assertEquals("", jsonPointer.toString());
    }

    @Test
    public void testConstructNonEmptySegment() {
        JsonPointer jsonPointer = new JsonPointer("/segment1", "segment1", null);
        assertFalse(jsonPointer.matches());
        assertEquals("segment1", jsonPointer.getMatchingProperty());
        assertEquals(-1, jsonPointer.getMatchingIndex());
        assertNull(jsonPointer.tail());
        assertEquals("/segment1", jsonPointer.toString());
    }

    @Test
    public void testConstructWithNextSegment() {
        String fullString = "/segment1/segment2";
        JsonPointer jsonPointer1 = new JsonPointer(fullString, "segment1",
                new JsonPointer(fullString, "segment2", JsonPointer.EMPTY));
        assertFalse(jsonPointer1.matches());
        assertEquals("segment1", jsonPointer1.getMatchingProperty());
        assertEquals(-1, jsonPointer1.getMatchingIndex());
        assertEquals("/segment2", jsonPointer1.tail().toString());
        assertEquals(fullString, jsonPointer1.toString());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidInput() {
        JsonPointer.compile("invalid");
    }

    @Test
    public void testCompile() {
        String input = "/segment1/segment2/segment3";
        JsonPointer jsonPointer = JsonPointer.compile(input);
        assertFalse(jsonPointer.matches());
        assertEquals("segment1", jsonPointer.getMatchingProperty());
        assertEquals(-1, jsonPointer.getMatchingIndex());
        assertEquals("/segment2", jsonPointer.tail().toString());
        assertEquals(input, jsonPointer.toString());
    }

    @Test
    public void testValueOf() {
        String input = "/segment1/segment2/segment3";
        JsonPointer jsonPointer = JsonPointer.valueOf(input);
        assertFalse(jsonPointer.matches());
        assertEquals("segment1", jsonPointer.getMatchingProperty());
        assertEquals(-1, jsonPointer.getMatchingIndex());
        assertEquals("/segment2", jsonPointer.tail().toString());
        assertEquals(input, jsonPointer.toString());
    }

    @Test
    public void testMatchProperty() {
        String input = "/segment1/segment2";
        JsonPointer jsonPointer = JsonPointer.compile(input);
        JsonPointer expected = new JsonPointer("/segment2", "segment2", JsonPointer.EMPTY);
        JsonPointer result = jsonPointer.matchProperty("segment2");
        assertEquals(expected, result);
    }

    @Test
    public void testMatchPropertyNull() {
        String input = "/segment1/segment2";
        JsonPointer jsonPointer = JsonPointer.compile(input);
        JsonPointer result = jsonPointer.matchProperty(null);
        assertNull(result);
    }

    @Test
    public void testMatchPropertyMismatch() {
        String input = "/segment1/segment2";
        JsonPointer jsonPointer = JsonPointer.compile(input);
        JsonPointer result = jsonPointer.matchProperty("segment3");
        assertNull(result);
    }

    @Test
    public void testMatchElement() {
        String input = "/segment1/0/segment3";
        JsonPointer jsonPointer = JsonPointer.compile(input);
        JsonPointer result = jsonPointer.matchElement(0);
        assertEquals(JsonPointer.EMPTY, result);
    }

    @Test
    public void testMatchElementNegative() {
        String input = "/segment1/-1/segment3";
        JsonPointer jsonPointer = JsonPointer.compile(input);
        JsonPointer result = jsonPointer.matchElement(-1);
        assertNull(result);
    }

    @Test
    public void testMatchElementMismatch() {
        String input = "/segment1/0/segment3";
        JsonPointer jsonPointer = JsonPointer.compile(input);
        JsonPointer result = jsonPointer.matchElement(1);
        assertNull(result);
    }

    @Test
    public void testMatchElementMatch() {
        String input = "/segment1/1/segment3";
        JsonPointer jsonPointer = JsonPointer.compile(input);
        JsonPointer result = jsonPointer.matchElement(1);
        assertEquals(JsonPointer.EMPTY, result);
    }

    @Test
    public void testToString() {
        String input = "/segment1/segment2/segment3";
        JsonPointer jsonPointer = JsonPointer.compile(input);
        assertEquals(input, jsonPointer.toString());
    }

    @Test
    public void testHashCode() {
        String input = "/segment1/segment2/segment3";
        JsonPointer jsonPointer1 = JsonPointer.compile(input);
        JsonPointer jsonPointer2 = JsonPointer.compile(input);
        assertEquals(jsonPointer1.hashCode(), jsonPointer2.hashCode());
    }

    @Test
    public void testEquals() {
        String input = "/segment1/segment2/segment3";
        JsonPointer jsonPointer1 = JsonPointer.compile(input);
        JsonPointer jsonPointer2 = JsonPointer.compile(input);
        JsonPointer jsonPointer3 = new JsonPointer("/segment1/segment2/segment4", "segment2", JsonPointer.EMPTY);
        assertTrue(jsonPointer1.equals(jsonPointer1));
        assertTrue(jsonPointer1.equals(jsonPointer2));
        assertFalse(jsonPointer1.equals(jsonPointer3));
    }
}