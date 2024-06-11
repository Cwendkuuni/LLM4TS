package com.fasterxml.jackson.core;

import org.junit.Test;
import static org.junit.Assert.*;

public class JsonPointerTest {

    @Test
    public void testMatches_EmptyPointer_ReturnsTrue() {
        JsonPointer pointer = JsonPointer.compile("");
        boolean result = pointer.matches();
        assertTrue(result);
    }

    @Test
    public void testMatches_NonEmptyPointer_ReturnsFalse() {
        JsonPointer pointer = JsonPointer.compile("/foo");
        boolean result = pointer.matches();
        assertFalse(result);
    }

    @Test
    public void testGetMatchingProperty_MatchingPointer_ReturnsPropertyName() {
        JsonPointer pointer = JsonPointer.compile("/foo/bar");
        String result = pointer.getMatchingProperty();
        assertEquals("foo", result);
    }

    @Test
    public void testGetMatchingIndex_MatchingPointer_ReturnsElementIndex() {
        JsonPointer pointer = JsonPointer.compile("/foo/0");
        int result = pointer.getMatchingIndex();
        assertEquals(0, result);
    }

    @Test
    public void testMayMatchProperty_MatchingPointer_ReturnsTrue() {
        JsonPointer pointer = JsonPointer.compile("/foo");
        boolean result = pointer.mayMatchProperty();
        assertTrue(result);
    }

    @Test
    public void testMayMatchProperty_NonMatchingPointer_ReturnsFalse() {
        JsonPointer pointer = JsonPointer.compile("/foo/bar");
        boolean result = pointer.mayMatchProperty();
        assertFalse(result);
    }

    @Test
    public void testMayMatchElement_MatchingPointer_ReturnsTrue() {
        JsonPointer pointer = JsonPointer.compile("/foo/0");
        boolean result = pointer.mayMatchElement();
        assertTrue(result);
    }

    @Test
    public void testMayMatchElement_NonMatchingPointer_ReturnsFalse() {
        JsonPointer pointer = JsonPointer.compile("/foo/bar");
        boolean result = pointer.mayMatchElement();
        assertFalse(result);
    }

    @Test
    public void testMatchProperty_MatchingProperty_ReturnsNextSegment() {
        JsonPointer pointer = JsonPointer.compile("/foo");
        JsonPointer result = pointer.matchProperty("foo");
        assertNotNull(result);
    }

    @Test
    public void testMatchProperty_NonMatchingProperty_ReturnsNull() {
        JsonPointer pointer = JsonPointer.compile("/foo");
        JsonPointer result = pointer.matchProperty("bar");
        assertNull(result);
    }

    @Test
    public void testMatchElement_MatchingIndex_ReturnsNextSegment() {
        JsonPointer pointer = JsonPointer.compile("/foo/0");
        JsonPointer result = pointer.matchElement(0);
        assertNotNull(result);
    }

    @Test
    public void testMatchElement_NonMatchingIndex_ReturnsNull() {
        JsonPointer pointer = JsonPointer.compile("/foo/0");
        JsonPointer result = pointer.matchElement(1);
        assertNull(result);
    }

    @Test
    public void testTail_ReturnsNextSegment() {
        JsonPointer pointer = JsonPointer.compile("/foo/bar");
        JsonPointer result = pointer.tail();
        assertNotNull(result);
    }

    @Test
    public void testToString_ReturnsAsExpected() {
        JsonPointer pointer = JsonPointer.compile("/foo/bar");
        String result = pointer.toString();
        assertEquals("/foo/bar", result);
    }

    @Test
    public void testHashCode_EqualsReturnsTrue() {
        JsonPointer pointer1 = JsonPointer.compile("/foo");
        JsonPointer pointer2 = JsonPointer.compile("/foo");
        assertEquals(pointer1.hashCode(), pointer2.hashCode());
    }

    @Test
    public void testEquals_SameInstance_ReturnsTrue() {
        JsonPointer pointer = JsonPointer.compile("/foo");
        assertTrue(pointer.equals(pointer));
    }

    @Test
    public void testEquals_Null_ReturnsFalse() {
        JsonPointer pointer = JsonPointer.compile("/foo");
        assertFalse(pointer.equals(null));
    }

    @Test
    public void testEquals_DifferentClass_ReturnsFalse() {
        JsonPointer pointer = JsonPointer.compile("/foo");
        assertFalse(pointer.equals("foo"));
    }

    @Test
    public void testEquals_EqualInstances_ReturnsTrue() {
        JsonPointer pointer1 = JsonPointer.compile("/foo");
        JsonPointer pointer2 = JsonPointer.compile("/foo");
        assertTrue(pointer1.equals(pointer2));
    }

    @Test
    public void testEquals_NonEqualInstances_ReturnsFalse() {
        JsonPointer pointer1 = JsonPointer.compile("/foo");
        JsonPointer pointer2 = JsonPointer.compile("/bar");
        assertFalse(pointer1.equals(pointer2));
    }
}

