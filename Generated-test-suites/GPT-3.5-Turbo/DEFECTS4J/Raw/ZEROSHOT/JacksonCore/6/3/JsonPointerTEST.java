package com.fasterxml.jackson.core;

import org.junit.Assert;
import org.junit.Test;

public class JsonPointerTest {

    @Test
    public void testMatches() {
        JsonPointer pointer = new JsonPointer();
        Assert.assertTrue(pointer.matches());
    }

    @Test
    public void testGetMatchingProperty() {
        JsonPointer pointer = new JsonPointer();
        String matchingProperty = pointer.getMatchingProperty();
        Assert.assertEquals("", matchingProperty);
    }

    @Test
    public void testGetMatchingIndex() {
        JsonPointer pointer = new JsonPointer();
        int matchingIndex = pointer.getMatchingIndex();
        Assert.assertEquals(-1, matchingIndex);
    }

    @Test
    public void testMayMatchProperty() {
        JsonPointer pointer = new JsonPointer();
        boolean mayMatchProperty = pointer.mayMatchProperty();
        Assert.assertTrue(mayMatchProperty);
    }

    @Test
    public void testMayMatchElement() {
        JsonPointer pointer = new JsonPointer();
        boolean mayMatchElement = pointer.mayMatchElement();
        Assert.assertFalse(mayMatchElement);
    }

    @Test
    public void testMatchProperty() {
        JsonPointer pointer = new JsonPointer();
        JsonPointer result = pointer.matchProperty("name");
        Assert.assertNull(result);
    }

    @Test
    public void testMatchElement() {
        JsonPointer pointer = new JsonPointer();
        JsonPointer result = pointer.matchElement(2);
        Assert.assertNull(result);
    }

    @Test
    public void testTail() {
        JsonPointer pointer = new JsonPointer();
        JsonPointer result = pointer.tail();
        Assert.assertNull(result);
    }

    @Test
    public void testCompile() {
        String input = "/test";
        JsonPointer result = JsonPointer.compile(input);
        Assert.assertNotNull(result);
    }

    @Test
    public void testValueOf() {
        String input = "/test";
        JsonPointer result = JsonPointer.valueOf(input);
        Assert.assertNotNull(result);
    }

    @Test
    public void testToString() {
        JsonPointer pointer = new JsonPointer();
        String result = pointer.toString();
        Assert.assertEquals("", result);
    }

    @Test
    public void testHashCode() {
        JsonPointer pointer = new JsonPointer();
        int hashCode = pointer.hashCode();
        Assert.assertEquals(0, hashCode);
    }

    @Test
    public void testEquals() {
        JsonPointer pointer1 = new JsonPointer();
        JsonPointer pointer2 = new JsonPointer();
        boolean result = pointer1.equals(pointer2);
        Assert.assertTrue(result);
    }

}