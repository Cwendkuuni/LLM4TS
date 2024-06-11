package com.fasterxml.jackson.core;
import com.fasterxml.jackson.core.JsonPointer;
import org.junit.Test;
import static org.junit.Assert.*;

public class JsonPointerTest {

    @Test
    public void testCompileValidInput() {
        JsonPointer pointer = JsonPointer.compile("/test/property");
        assertNotNull(pointer);
        assertEquals("/test/property", pointer.toString());
    }

    @Test
    public void testCompileInvalidInput() {
        try {
            JsonPointer pointer = JsonPointer.compile("test/property");
            fail("IllegalArgumentException should have been thrown");
        } catch (IllegalArgumentException e) {
            // Exception thrown as expected
        }
    }

    @Test
    public void testMatchesEmptyPointer() {
        JsonPointer emptyPointer = JsonPointer.EMPTY;
        assertTrue(emptyPointer.matches());
    }

    @Test
    public void testGetMatchingProperty() {
        JsonPointer pointer = new JsonPointer("/test", "test", null);
        assertEquals("test", pointer.getMatchingProperty());
    }

    @Test
    public void testGetMatchingIndex() {
        JsonPointer pointer = new JsonPointer("/1", "1", null);
        assertEquals(1, pointer.getMatchingIndex());
    }

    @Test
    public void testMayMatchProperty() {
        JsonPointer pointer = new JsonPointer("/test", "test", null);
        assertTrue(pointer.mayMatchProperty());
    }

    @Test
    public void testMayMatchElement() {
        JsonPointer pointer = new JsonPointer("/1", "1", null);
        assertTrue(pointer.mayMatchElement());
    }

    @Test
    public void testMatchProperty() {
        JsonPointer pointer = new JsonPointer("/test", "test", null);
        JsonPointer next = pointer.matchProperty("test");
        assertNotNull(next);
        assertEquals("test", next.getMatchingProperty());
    }

    @Test
    public void testMatchElement() {
        JsonPointer pointer = new JsonPointer("/1", "1", null);
        JsonPointer next = pointer.matchElement(1);
        assertNotNull(next);
        assertEquals(1, next.getMatchingIndex());
    }

    @Test
    public void testTail() {
        JsonPointer pointer = new JsonPointer("/test", "test", null);
        JsonPointer next = pointer.tail();
        assertNull(next);
    }

    @Test
    public void testEquals() {
        JsonPointer pointer1 = new JsonPointer("/test/property", "test", null);
        JsonPointer pointer2 = new JsonPointer("/test/property", "test", null);
        assertEquals(pointer1, pointer2);
    }

    @Test
    public void testNotEquals() {
        JsonPointer pointer1 = new JsonPointer("/test/property1", "test", null);
        JsonPointer pointer2 = new JsonPointer("/test/property2", "test", null);
        assertNotEquals(pointer1, pointer2);
    }
}