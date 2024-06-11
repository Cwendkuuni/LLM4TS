package com.fasterxml.jackson.core;

import org.junit.Assert;
import org.junit.Test;

public class JsonPointerTest {

    @Test
    public void testEmptyConstructor() {
        JsonPointer pointer = new JsonPointer();
        Assert.assertEquals("", pointer._asString);
        Assert.assertNull(pointer._nextSegment);
        Assert.assertEquals("", pointer._matchingPropertyName);
        Assert.assertEquals(-1, pointer._matchingElementIndex);
    }

    @Test
    public void testNonEmptyConstructor() {
        JsonPointer pointer = new JsonPointer("fullString", "segment", new JsonPointer());
        Assert.assertEquals("fullString", pointer._asString);
        Assert.assertNotNull(pointer._nextSegment);
        Assert.assertEquals("segment", pointer._matchingPropertyName);
        Assert.assertEquals(-1, pointer._matchingElementIndex);
    }

    @Test
    public void testCompileWithValidInput() {
        JsonPointer pointer = JsonPointer.compile("/path1/path2");
        Assert.assertEquals("/path1/path2", pointer.toString());
        Assert.assertNotNull(pointer._nextSegment);
        Assert.assertEquals("path1", pointer._matchingPropertyName);
        Assert.assertEquals(-1, pointer._matchingElementIndex);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCompileWithInvalidInput() {
        JsonPointer pointer = JsonPointer.compile("path1/path2");
    }

    @Test
    public void testValueOf() {
        JsonPointer pointer = JsonPointer.valueOf("/path1/path2");
        Assert.assertEquals("/path1/path2", pointer.toString());
        Assert.assertNotNull(pointer._nextSegment);
        Assert.assertEquals("path1", pointer._matchingPropertyName);
        Assert.assertEquals(-1, pointer._matchingElementIndex);
    }

    @Test
    public void testMatches() {
        JsonPointer pointer = JsonPointer.compile("/path1/path2");
        Assert.assertFalse(pointer.matches());
    }

    @Test
    public void testGetMatchingProperty() {
        JsonPointer pointer = JsonPointer.compile("/path1/path2");
        Assert.assertEquals("path1", pointer.getMatchingProperty());
    }

    @Test
    public void testGetMatchingIndex() {
        JsonPointer pointer = JsonPointer.compile("/path1/path2");
        Assert.assertEquals(-1, pointer.getMatchingIndex());
    }

    @Test
    public void testMayMatchProperty() {
        JsonPointer pointer = JsonPointer.compile("/path1/path2");
        Assert.assertTrue(pointer.mayMatchProperty());
    }

    @Test
    public void testMayMatchElement() {
        JsonPointer pointer = JsonPointer.compile("/path1/path2");
        Assert.assertFalse(pointer.mayMatchElement());
    }

    @Test
    public void testMatchProperty() {
        JsonPointer pointer = JsonPointer.compile("/path1/path2");
        JsonPointer nextSegment = pointer.matchProperty("path1");
        Assert.assertEquals("/path1/path2", nextSegment.toString());
    }

    @Test
    public void testMatchElement() {
        JsonPointer pointer = JsonPointer.compile("/path1/path2");
        JsonPointer nextSegment = pointer.matchElement(0);
        Assert.assertNull(nextSegment);
    }

    @Test
    public void testTail() {
        JsonPointer pointer = JsonPointer.compile("/path1/path2");
        JsonPointer nextSegment = pointer.tail();
        Assert.assertEquals("/path2", nextSegment.toString());
    }

    @Test
    public void testToString() {
        JsonPointer pointer = JsonPointer.compile("/path1/path2");
        Assert.assertEquals("/path1/path2", pointer.toString());
    }

    @Test
    public void testHashCode() {
        JsonPointer pointer = JsonPointer.compile("/path1/path2");
        Assert.assertTrue(pointer.hashCode() != 0);
    }

    @Test
    public void testEquals() {
        JsonPointer pointer1 = JsonPointer.compile("/path1/path2");
        JsonPointer pointer2 = JsonPointer.compile("/path1/path2");
        Assert.assertTrue(pointer1.equals(pointer2));
    }

    @Test
    public void test_parseIndexWithValidInput() {
        JsonPointer pointer = JsonPointer.compile("/path1/path2");
        int index = pointer._parseIndex("5");
        Assert.assertEquals(5, index);
    }

    @Test
    public void test_parseIndexWithMaxIntegerAsInput() {
        JsonPointer pointer = JsonPointer.compile("/path1/path2");
        int index = pointer._parseIndex("2147483647");
        Assert.assertEquals(2147483647, index);
    }

    @Test
    public void test_parseIndexWithInvalidInput() {
        JsonPointer pointer = JsonPointer.compile("/path1/path2");
        int index = pointer._parseIndex("abc");
        Assert.assertEquals(-1, index);
    }

    @Test
    public void test_parseIndexWithZeroAsInput() {
        JsonPointer pointer = JsonPointer.compile("/path1/path2");
        int index = pointer._parseIndex("0");
        Assert.assertEquals(0, index);
    }

    @Test
    public void test_parseTailWithValidInput() {
        JsonPointer pointer = JsonPointer.compile("/path1/path2");
        JsonPointer nextSegment = pointer._parseTail("/path2");
        Assert.assertEquals("/path2", nextSegment.toString());
    }

    @Test
    public void test_parseQuotedTailWithValidInput() {
        JsonPointer pointer = JsonPointer.compile("/path1/path2");
        JsonPointer nextSegment = pointer._parseQuotedTail("/path2", 1);
        Assert.assertEquals("/path2", nextSegment.toString());
    }

    @Test
    public void test__appendEscape() {
        StringBuilder sb = new StringBuilder();
        JsonPointer pointer = JsonPointer.compile("/path1/path2");
        pointer._appendEscape(sb, '0');
        Assert.assertEquals("~0", sb.toString());
    }
}