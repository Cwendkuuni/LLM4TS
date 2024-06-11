package com.fasterxml.jackson.core;

import org.junit.Test;
import static org.junit.Assert.*;

public class JsonPointerTest {
    
    @Test
    public void testEmptyJsonPointerConstructor() {
        JsonPointer jsonPointer = new JsonPointer();
        
        assertEquals("", jsonPointer.getMatchingProperty());
        assertEquals(-1, jsonPointer.getMatchingIndex());
        assertEquals(true, jsonPointer.matches());
    }

    @Test
    public void testNonEmptyJsonPointerConstructor() {
        JsonPointer jsonPointer = new JsonPointer("test", "segment", null);
        
        assertEquals("segment", jsonPointer.getMatchingProperty());
        assertEquals(-1, jsonPointer.getMatchingIndex());
        assertEquals(false, jsonPointer.matches());
    }

    @Test
    public void testCompileValidJsonPointer() {
        JsonPointer jsonPointer = JsonPointer.compile("/test");
        
        assertEquals("test", jsonPointer.getMatchingProperty());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCompileInvalidJsonPointer() {
        JsonPointer jsonPointer = JsonPointer.compile("test");
    }

    @Test
    public void testMatchProperty() {
        JsonPointer jsonPointer = JsonPointer.compile("/test");
        JsonPointer nextPointer = jsonPointer.matchProperty("test");
        
        assertNotNull(nextPointer);
        assertEquals("", nextPointer.getMatchingProperty());
    }

    @Test
    public void testMatchElement() {
        JsonPointer jsonPointer = JsonPointer.compile("/1");
        JsonPointer nextPointer = jsonPointer.matchElement(1);
        
        assertNotNull(nextPointer);
        assertEquals("", nextPointer.getMatchingProperty());
    }

    @Test
    public void testTail() {
        JsonPointer jsonPointer = JsonPointer.compile("/test/next/test2");
        JsonPointer nextPointer = jsonPointer.tail();
        
        assertEquals("next/test2", nextPointer.toString());
    }

}