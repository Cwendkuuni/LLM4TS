package com.fasterxml.jackson.core;

import org.junit.Assert;
import org.junit.Test;

public class JsonPointerTest {

    @Test
    public void testMatches() {
        JsonPointer jsonPointer = new JsonPointer();
        Assert.assertTrue(jsonPointer.matches());
    }

    @Test
    public void testGetMatchingProperty() {
        JsonPointer jsonPointer = new JsonPointer();
        Assert.assertEquals("", jsonPointer.getMatchingProperty());
    }

    @Test
    public void testGetMatchingIndex() {
        JsonPointer jsonPointer = new JsonPointer();
        Assert.assertEquals(-1, jsonPointer.getMatchingIndex());
    }

    @Test
    public void testMayMatchProperty() {
        JsonPointer jsonPointer = new JsonPointer();
        Assert.assertTrue(jsonPointer.mayMatchProperty());
    }

    @Test
    public void testMayMatchElement() {
        JsonPointer jsonPointer = new JsonPointer();
        Assert.assertFalse(jsonPointer.mayMatchElement());
    }

    @Test
    public void testMatchProperty() {
        JsonPointer jsonPointer = new JsonPointer();
        JsonPointer result = jsonPointer.matchProperty("name");
        Assert.assertNull(result);
    }

    @Test
    public void testMatchElement() {
        JsonPointer jsonPointer = new JsonPointer();
        JsonPointer result = jsonPointer.matchElement(0);
        Assert.assertNull(result);
    }

    @Test
    public void testTail() {
        JsonPointer jsonPointer = new JsonPointer();
        JsonPointer result = jsonPointer.tail();
        Assert.assertNull(result);
    }

    @Test
    public void testToString() {
        JsonPointer jsonPointer = new JsonPointer();
        Assert.assertEquals("", jsonPointer.toString());
    }

    @Test
    public void testHashCode() {
        JsonPointer jsonPointer = new JsonPointer();
        Assert.assertEquals("".hashCode(), jsonPointer.hashCode());
    }

    @Test
    public void testEquals() {
        JsonPointer jsonPointer = new JsonPointer();
        Assert.assertTrue(jsonPointer.equals(jsonPointer));
    }

    @Test
    public void testCompile() {
        JsonPointer jsonPointer = JsonPointer.compile("/path");
        Assert.assertEquals("/path", jsonPointer.toString());
    }

    @Test
    public void testValueOf() {
        JsonPointer jsonPointer = JsonPointer.valueOf("/path");
        Assert.assertEquals("/path", jsonPointer.toString());
    }
}