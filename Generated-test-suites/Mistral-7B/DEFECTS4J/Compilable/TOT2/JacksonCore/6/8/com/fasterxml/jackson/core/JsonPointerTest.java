package com.fasterxml.jackson.core;
import org.junit.Assert;
import org.junit.Test;
import com.fasterxml.jackson.core.JsonPointer;

public class JsonPointerTest {

    @Test
    public void testEmptyPointer() {
        JsonPointer emptyPointer = JsonPointer.EMPTY;
        Assert.assertTrue(emptyPointer.matches());
        Assert.assertNull(emptyPointer.getMatchingProperty());
        Assert.assertEquals(-1, emptyPointer.getMatchingIndex());
        Assert.assertFalse(emptyPointer.mayMatchProperty());
        Assert.assertFalse(emptyPointer.mayMatchElement());
    }

    @Test
    public void testSimplePointer() {
        String pointerString = "/testProperty";
        JsonPointer pointer = JsonPointer.compile(pointerString);
        Assert.assertFalse(pointer.matches());
        Assert.assertEquals("testProperty", pointer.getMatchingProperty());
        Assert.assertEquals(-1, pointer.getMatchingIndex());
        Assert.assertTrue(pointer.mayMatchProperty());
        Assert.assertFalse(pointer.mayMatchElement());
        Assert.assertEquals(pointerString, pointer.toString());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidPointer() {
        String pointerString = "invalidPointer";
        JsonPointer pointer = JsonPointer.compile(pointerString);
    }

    // Add test cases for other methods and edge cases here
}