package com.fasterxml.jackson.core;

import org.junit.Assert;
import org.junit.Test;

public class JsonPointerTest {

    @Test
    public void testMatches() {
        JsonPointer pointer = JsonPointer.EMPTY;
        Assert.assertTrue(pointer.matches());
    }

    @Test
    public void testGetMatchingProperty() {
        JsonPointer pointer = JsonPointer.EMPTY;
        String matchingProperty = pointer.getMatchingProperty();
        Assert.assertEquals("", matchingProperty);
    }

    @Test
    public void testGetMatchingIndex() {
        JsonPointer pointer = JsonPointer.EMPTY;
        int matchingIndex = pointer.getMatchingIndex();
        Assert.assertEquals(-1, matchingIndex);
    }

    @Test
    public void testMayMatchProperty() {
        JsonPointer pointer = JsonPointer.EMPTY;
        boolean mayMatchProperty = pointer.mayMatchProperty();
        Assert.assertTrue(mayMatchProperty);
    }

    @Test
    public void testMayMatchElement() {
        JsonPointer pointer = JsonPointer.EMPTY;
        boolean mayMatchElement = pointer.mayMatchElement();
        Assert.assertFalse(mayMatchElement);
    }

    @Test
    public void testMatchProperty() {
        JsonPointer pointer = JsonPointer.EMPTY;
        String name = "test";
        JsonPointer matchingProperty = pointer.matchProperty(name);
        Assert.assertNull(matchingProperty);
    }

    @Test
    public void testMatchElement() {
        JsonPointer pointer = JsonPointer.EMPTY;
        int index = 0;
        JsonPointer matchingElement = pointer.matchElement(index);
        Assert.assertNull(matchingElement);
    }

    @Test
    public void testTail() {
        JsonPointer pointer = JsonPointer.EMPTY;
        JsonPointer tail = pointer.tail();
        Assert.assertNull(tail);
    }

    @Test
    public void testToString() {
        JsonPointer pointer = JsonPointer.EMPTY;
        String toString = pointer.toString();
        Assert.assertEquals("", toString);
    }

    @Test
    public void testHashCode() {
        JsonPointer pointer1 = JsonPointer.EMPTY;
        JsonPointer pointer2 = JsonPointer.EMPTY;
        int hashCode1 = pointer1.hashCode();
        int hashCode2 = pointer2.hashCode();
        Assert.assertEquals(hashCode1, hashCode2);
    }

    @Test
    public void testEquals() {
        JsonPointer pointer1 = JsonPointer.EMPTY;
        JsonPointer pointer2 = JsonPointer.EMPTY;
        boolean equals = pointer1.equals(pointer2);
        Assert.assertTrue(equals);
    }

    @Test
    public void testCompile() {
        String input = "";
        try {
            JsonPointer jsonPointer = JsonPointer.compile(input);
            Assert.assertNotNull(jsonPointer);
        } catch (IllegalArgumentException e) {
            Assert.fail(e.getMessage());
        }
    }

    @Test
    public void testValueOf() {
        String input = "";
        JsonPointer jsonPointer = JsonPointer.valueOf(input);
        Assert.assertNotNull(jsonPointer);
    }
}