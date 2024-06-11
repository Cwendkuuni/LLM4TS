package com.fasterxml.jackson.core;

import org.junit.Test;
import static org.junit.Assert.*;

public class JsonPointerTest {

    @Test
    public void testMatches() {
        JsonPointer jsonPointer = new JsonPointer();
        assertTrue(jsonPointer.matches());
    }

    @Test
    public void testGetMatchingProperty() {
        JsonPointer jsonPointer = new JsonPointer();
        assertEquals("", jsonPointer.getMatchingProperty());
    }

    @Test
    public void testGetMatchingIndex() {
        JsonPointer jsonPointer = new JsonPointer();
        assertEquals(-1, jsonPointer.getMatchingIndex());
    }

    @Test
    public void testMayMatchProperty() {
        JsonPointer jsonPointer = new JsonPointer();
        assertTrue(jsonPointer.mayMatchProperty());
    }

    @Test
    public void testMayMatchElement() {
        JsonPointer jsonPointer = new JsonPointer();
        assertFalse(jsonPointer.mayMatchElement());
    }

    @Test
    public void testMatchProperty() {
        JsonPointer jsonPointer = new JsonPointer();
        assertNull(jsonPointer.matchProperty("test"));
    }

    @Test
    public void testMatchElement() {
        JsonPointer jsonPointer = new JsonPointer();
        assertNull(jsonPointer.matchElement(1));
    }

    @Test
    public void testTail() {
        JsonPointer jsonPointer = new JsonPointer();
        assertEquals(JsonPointer.EMPTY, jsonPointer.tail());
    }

    @Test
    public void testEquals() {
        JsonPointer jsonPointer1 = new JsonPointer();
        JsonPointer jsonPointer2 = new JsonPointer();
        assertTrue(jsonPointer1.equals(jsonPointer2));
    }

    @Test
    public void testHashCode() {
        JsonPointer jsonPointer = new JsonPointer();
        assertEquals(jsonPointer.toString().hashCode(), jsonPointer.hashCode());
    }

    @Test
    public void testCompile() {
        try {
            JsonPointer jsonPointer = JsonPointer.compile("/test");
            assertNotNull(jsonPointer);
        } catch (IllegalArgumentException e) {
            fail("IllegalArgumentException should not be thrown");
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidCompile() {
        JsonPointer.compile("test");
    }

    @Test
    public void testValueOf() {
        JsonPointer jsonPointer = JsonPointer.valueOf("/test");
        assertNotNull(jsonPointer);
    }

}