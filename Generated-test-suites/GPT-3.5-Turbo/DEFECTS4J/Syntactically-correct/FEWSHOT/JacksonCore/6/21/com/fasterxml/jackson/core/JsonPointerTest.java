package com.fasterxml.jackson.core;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class JsonPointerTest {
    @Test
    public void testMatches() {
        JsonPointer pointer = new JsonPointer();
        assertEquals(true, pointer.matches());
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
        assertEquals(true, pointer.mayMatchProperty());
    }

    @Test
    public void testMayMatchElement() {
        JsonPointer pointer = new JsonPointer();
        assertEquals(false, pointer.mayMatchElement());
    }
    
    @Test
    public void testMatchProperty() {
        JsonPointer pointer = new JsonPointer();
        JsonPointer matchingPointer = pointer.matchProperty("name");
        assertNull(matchingPointer);
    }

    @Test
    public void testMatchElement() {
        JsonPointer pointer = new JsonPointer();
        JsonPointer matchingPointer = pointer.matchElement(0);
        assertNull(matchingPointer);
    }

    @Test
    public void testTail() {
        JsonPointer pointer = new JsonPointer();
        JsonPointer tailPointer = pointer.tail();
        assertNull(tailPointer);
    }

    @Test
    public void testCompile() {
        JsonPointer pointer = JsonPointer.compile("/path/to/node");
        assertEquals("/path/to/node", pointer.toString());
    }

    @Test
    public void testValueOf() {
        JsonPointer pointer = JsonPointer.valueOf("/path/to/node");
        assertEquals("/path/to/node", pointer.toString());
    }
}