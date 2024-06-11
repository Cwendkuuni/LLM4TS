package com.fasterxml.jackson.core;

import org.junit.Assert;
import org.junit.Test;

public class JsonPointerTest {

    @Test
    public void testEmptyPointerMatches() {
        JsonPointer pointer = JsonPointer.EMPTY;
        Assert.assertTrue(pointer.matches());
        Assert.assertFalse(pointer.mayMatchProperty());
        Assert.assertFalse(pointer.mayMatchElement());
    }

    @Test
    public void testEmptyPointerToString() {
        JsonPointer pointer = JsonPointer.EMPTY;
        Assert.assertEquals("", pointer.toString());
    }

    @Test
    public void testPointerMatchesProperty() {
        JsonPointer pointer = new JsonPointer("test", "property", JsonPointer.EMPTY);
        Assert.assertFalse(pointer.matches());
        Assert.assertTrue(pointer.mayMatchProperty());
        Assert.assertFalse(pointer.mayMatchElement());
        Assert.assertEquals("property", pointer.getMatchingProperty());
    }

    @Test
    public void testPointerMatchesElement() {
        JsonPointer pointer = new JsonPointer("test", "3", JsonPointer.EMPTY);
        Assert.assertFalse(pointer.matches());
        Assert.assertFalse(pointer.mayMatchProperty());
        Assert.assertTrue(pointer.mayMatchElement());
        Assert.assertEquals(3, pointer.getMatchingIndex());
    }

    @Test
    public void testTail() {
        JsonPointer pointer = new JsonPointer("test", "property", JsonPointer.EMPTY);
        Assert.assertEquals(JsonPointer.EMPTY, pointer.tail());
    }

    @Test
    public void testEquals() {
        JsonPointer pointer1 = new JsonPointer("test", "property", JsonPointer.EMPTY);
        JsonPointer pointer2 = new JsonPointer("test", "property", JsonPointer.EMPTY);
        Assert.assertEquals(pointer1, pointer2);
    }

    @Test
    public void testCompileWithValidExpression() {
        JsonPointer pointer = JsonPointer.compile("/test/property");
        Assert.assertEquals("test/property", pointer.toString());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCompileWithInvalidExpression() {
        JsonPointer pointer = JsonPointer.compile("test/property");
    }

}