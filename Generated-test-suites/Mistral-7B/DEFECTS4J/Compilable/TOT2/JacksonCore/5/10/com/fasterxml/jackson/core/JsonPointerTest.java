package com.fasterxml.jackson.core; // import statements here if needed

import org.junit.Assert;
import org.junit.Test;

public class JsonPointerTest {

    @Test
    public void testEmptyJsonPointer() {
        JsonPointer jsonPointer = JsonPointer.EMPTY;
        Assert.assertTrue(jsonPointer.matches());
        Assert.assertNull(jsonPointer.getMatchingProperty());
        Assert.assertEquals(-1, jsonPointer.getMatchingIndex());
        Assert.assertFalse(jsonPointer.mayMatchProperty());
        Assert.assertFalse(jsonPointer.mayMatchElement());
    }

    @Test
    public void testJsonPointerWithSingleSegment() {
        JsonPointer jsonPointer = new JsonPointer("/testProperty", null, JsonPointer.EMPTY);
        Assert.assertFalse(jsonPointer.matches());
        Assert.assertEquals("testProperty", jsonPointer.getMatchingProperty());
        Assert.assertEquals(-1, jsonPointer.getMatchingIndex());
        Assert.assertTrue(jsonPointer.mayMatchProperty());
        Assert.assertFalse(jsonPointer.mayMatchElement());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidJsonPointer() {
        String invalidJsonPointer = "testInvalid";
        JsonPointer jsonPointer = JsonPointer.compile(invalidJsonPointer);
    }

    // Add more test cases here for other methods in JsonPointer class
}