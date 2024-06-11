package com.fasterxml.jackson.core;

import static org.junit.Assert.*;
import org.junit.Test;

public class JsonPointerTest {
    @Test
    public void testEmptyConstructor() {
        JsonPointer pointer = new JsonPointer();
        assertTrue(pointer.matches());
        assertEquals("", pointer.getMatchingProperty());
        assertEquals(-1, pointer.getMatchingIndex());
        assertNull(pointer.matchProperty("name"));
        assertNull(pointer.matchElement(0));
        assertNull(pointer.tail());
        assertEquals("", pointer.toString());
        assertEquals(pointer.hashCode(), pointer.toString().hashCode());
        assertTrue(pointer.equals(pointer));
        assertFalse(pointer.equals(null));
        assertFalse(pointer.equals(""));

    }

    @Test
    public void testNonEmptyConstructor() {
        JsonPointer pointer = new JsonPointer("input", "segment", new JsonPointer());
        assertFalse(pointer.matches());
        assertEquals("segment", pointer.getMatchingProperty());
        assertEquals(-1, pointer.getMatchingIndex());
        assertNull(pointer.matchProperty("name"));
        assertNull(pointer.matchElement(0));
        assertNotNull(pointer.tail());
        assertEquals("segment", pointer.tail().toString());
        assertEquals("input", pointer.toString());
    }

    @Test
    public void testCompile() {
        JsonPointer pointer = JsonPointer.compile("");
        assertTrue(pointer.matches());
        assertEquals("", pointer.getMatchingProperty());
        assertEquals(-1, pointer.getMatchingIndex());
        assertNull(pointer.matchProperty("name"));
        assertNull(pointer.matchElement(0));

        pointer = JsonPointer.compile("/");
        assertFalse(pointer.matches());
        assertEquals("", pointer.getMatchingProperty());
        assertEquals(-1, pointer.getMatchingIndex());
        assertNull(pointer.matchProperty("name"));
        assertNull(pointer.matchElement(0));
        assertNull(pointer.tail());

        pointer = JsonPointer.compile("/segment");
        assertFalse(pointer.matches());
        assertEquals("segment", pointer.getMatchingProperty());
        assertEquals(-1, pointer.getMatchingIndex());
        assertNull(pointer.matchProperty("name"));
        assertNull(pointer.matchElement(0));
        assertEquals("segment", pointer.tail().toString());

        pointer = JsonPointer.compile("/segment/subsegment");
        assertFalse(pointer.matches());
        assertEquals("segment", pointer.getMatchingProperty());
        assertEquals(-1, pointer.getMatchingIndex());
        assertNull(pointer.matchProperty("name"));
        assertNull(pointer.matchElement(0));
        assertFalse(pointer.tail().matches());
        assertEquals("subsegment", pointer.tail().getMatchingProperty());
        assertEquals(-1, pointer.tail().getMatchingIndex());
        assertNull(pointer.tail().matchProperty("name"));
        assertNull(pointer.tail().matchElement(0));
        assertEquals("subsegment", pointer.tail().toString());

        try {
            pointer = JsonPointer.compile("invalid");
            fail("compile() method should throw IllegalArgumentException for invalid input");
        } catch (IllegalArgumentException e) {
            // expected exception
        }
    }

    @Test
    public void testValueOf() {
        JsonPointer pointer = JsonPointer.valueOf("");
        assertTrue(pointer.matches());
        assertEquals("", pointer.getMatchingProperty());
        assertEquals(-1, pointer.getMatchingIndex());
        assertNull(pointer.matchProperty("name"));
        assertNull(pointer.matchElement(0));

        pointer = JsonPointer.valueOf("/");
        assertFalse(pointer.matches());
        assertEquals("", pointer.getMatchingProperty());
        assertEquals(-1, pointer.getMatchingIndex());
        assertNull(pointer.matchProperty("name"));
        assertNull(pointer.matchElement(0));
        assertNull(pointer.tail());

        pointer = JsonPointer.valueOf("/segment");
        assertFalse(pointer.matches());
        assertEquals("segment", pointer.getMatchingProperty());
        assertEquals(-1, pointer.getMatchingIndex());
        assertNull(pointer.matchProperty("name"));
        assertNull(pointer.matchElement(0));
        assertEquals("segment", pointer.tail().toString());

        pointer = JsonPointer.valueOf("/segment/subsegment");
        assertFalse(pointer.matches());
        assertEquals("segment", pointer.getMatchingProperty());
        assertEquals(-1, pointer.getMatchingIndex());
        assertNull(pointer.matchProperty("name"));
        assertNull(pointer.matchElement(0));
        assertFalse(pointer.tail().matches());
        assertEquals("subsegment", pointer.tail().getMatchingProperty());
        assertEquals(-1, pointer.tail().getMatchingIndex());
        assertNull(pointer.tail().matchProperty("name"));
        assertNull(pointer.tail().matchElement(0));
        assertEquals("subsegment", pointer.tail().toString());
    }
}