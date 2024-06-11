package com.fasterxml.jackson.core;
import org.junit.Test;
import static org.junit.Assert.*;

public class JsonPointerTest {

    @Test
    public void testMatches() {
        // Test when pointer matches current node
        JsonPointer pointer1 = new JsonPointer();
        assertTrue(pointer1.matches());

        // Test when pointer does not match current node
        JsonPointer pointer2 = new JsonPointer("test", "property", null);
        assertFalse(pointer2.matches());
    }

    @Test
    public void testGetMatchingProperty() {
        // Test when there is a matching property
        JsonPointer pointer1 = new JsonPointer("test", "property", null);
        assertEquals("property", pointer1.getMatchingProperty());

        // Test when there is no matching property
        JsonPointer pointer2 = new JsonPointer();
        assertNull(pointer2.getMatchingProperty());
    }

    @Test
    public void testGetMatchingIndex() {
        // Test when there is a matching index
        JsonPointer pointer1 = new JsonPointer("test", "", null);
        assertEquals(0, pointer1.getMatchingIndex());

        // Test when there is no matching index
        JsonPointer pointer2 = new JsonPointer();
        assertEquals(-1, pointer2.getMatchingIndex());
    }

    @Test
    public void testMayMatchProperty() {
        // Test when there may be a matching property
        JsonPointer pointer1 = new JsonPointer("test", "property", null);
        assertTrue(pointer1.mayMatchProperty());

        // Test when there is no matching property
        JsonPointer pointer2 = new JsonPointer();
        assertFalse(pointer2.mayMatchProperty());
    }

    @Test
    public void testMayMatchElement() {
        // Test when there may be a matching element
        JsonPointer pointer1 = new JsonPointer("test", "", null);
        assertTrue(pointer1.mayMatchElement());

        // Test when there is no matching element
        JsonPointer pointer2 = new JsonPointer();
        assertFalse(pointer2.mayMatchElement());
    }

    @Test
    public void testMatchProperty() {
        // Test when there is a matching property
        JsonPointer pointer1 = new JsonPointer("test", "property", null);
        JsonPointer next1 = new JsonPointer("test", "", null);
        assertEquals(next1, pointer1.matchProperty("property"));

        // Test when there is no matching property
        JsonPointer pointer2 = new JsonPointer("test", "property", null);
        assertNull(pointer2.matchProperty("invalid"));

        // Test when pointer is empty
        JsonPointer pointer3 = new JsonPointer();
        assertNull(pointer3.matchProperty("property"));
    }

    @Test
    public void testMatchElement() {
        // Test when there is a matching element
        JsonPointer pointer1 = new JsonPointer("test", "", null);
        JsonPointer next1 = new JsonPointer("test", "", null);
        assertEquals(next1, pointer1.matchElement(0));

        // Test when there is no matching element
        JsonPointer pointer2 = new JsonPointer("test", "", null);
        assertNull(pointer2.matchElement(1));

        // Test when element index is negative
        JsonPointer pointer3 = new JsonPointer();
        assertNull(pointer3.matchElement(-1));
    }

    @Test
    public void testTail() {
        // Test when pointer has a tail
        JsonPointer pointer1 = new JsonPointer("test", "", null);
        JsonPointer next1 = new JsonPointer("test", "", null);
        assertEquals(next1, pointer1.tail());

        // Test when pointer is empty
        JsonPointer pointer2 = new JsonPointer();
        assertEquals(pointer2, pointer2.tail());
    }

    @Test
    public void testToString() {
        // Test when pointer is empty
        JsonPointer pointer1 = new JsonPointer();
        assertEquals("", pointer1.toString());

        // Test when pointer has segments
        JsonPointer pointer2 = new JsonPointer("test", "property", null);
        assertEquals("test/property", pointer2.toString());
    }

    @Test
    public void testHashCode() {
        // Test when pointers have the same hash code
        JsonPointer pointer1 = new JsonPointer("test", "property", null);
        JsonPointer pointer2 = new JsonPointer("test", "property", null);
        assertEquals(pointer1.hashCode(), pointer2.hashCode());

        // Test when pointers have different hash codes
        JsonPointer pointer3 = new JsonPointer("test", "", null);
        assertNotEquals(pointer1.hashCode(), pointer3.hashCode());
    }

    @Test
    public void testEquals() {
        // Test when pointers are the same
        JsonPointer pointer1 = new JsonPointer("test", "property", null);
        assertEquals(pointer1, pointer1);

        // Test when pointers are equal
        JsonPointer pointer2 = new JsonPointer("test", "property", null);
        assertEquals(pointer1, pointer2);

        // Test when pointers are not equal
        JsonPointer pointer3 = new JsonPointer("test", "", null);
        assertNotEquals(pointer1, pointer3);

        // Test when comparing with null
        assertNotEquals(pointer1, null);

        // Test when comparing with different object type
        assertNotEquals(pointer1, "test");
    }
}