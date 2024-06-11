package com.fasterxml.jackson.core;

import org.junit.Assert;
import org.junit.Test;

public class JsonPointerTest {
    
    @Test
    public void testCompile_ValidInput_ReturnsJsonPointer() {
        String input = "/foo/bar";
        JsonPointer pointer = JsonPointer.compile(input);
        Assert.assertNotNull(pointer);
        Assert.assertEquals(input, pointer.toString());
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testCompile_InvalidInput_ThrowsException() {
        String input = "foo/bar";
        JsonPointer.compile(input);
    }
    
    @Test
    public void testValueOf_ValidInput_ReturnsJsonPointer() {
        String input = "/foo/bar";
        JsonPointer pointer = JsonPointer.valueOf(input);
        Assert.assertNotNull(pointer);
        Assert.assertEquals(input, pointer.toString());
    }
    
    @Test
    public void testMatches_MatchingSegment_ReturnsTrue() {
        String input = "/foo";
        JsonPointer pointer = JsonPointer.compile(input);
        Assert.assertTrue(pointer.matches());
        Assert.assertEquals("", pointer.getMatchingProperty());
        Assert.assertFalse(pointer.mayMatchElement());
    }
    
    @Test
    public void testMatches_NonMatchingSegment_ReturnsFalse() {
        String input = "/bar";
        JsonPointer pointer = JsonPointer.compile(input);
        Assert.assertFalse(pointer.matches());
        Assert.assertNull(pointer.getMatchingProperty());
        Assert.assertFalse(pointer.mayMatchElement());
    }
    
    @Test
    public void testGetMatchingProperty() {
        String input = "/foo";
        JsonPointer pointer = JsonPointer.compile(input);
        Assert.assertEquals("foo", pointer.getMatchingProperty());
    }
    
    @Test
    public void testGetMatchingIndex() {
        String input = "/5";
        JsonPointer pointer = JsonPointer.compile(input);
        Assert.assertEquals(5, pointer.getMatchingIndex());
    }

    @Test
    public void testMatchProperty_MatchingProperty_ReturnsNextSegment() {
        String input = "/foo";
        JsonPointer pointer = JsonPointer.compile(input);
        JsonPointer nextSegment = pointer.matchProperty("foo");
        Assert.assertNotNull(nextSegment);
    }
    
    @Test
    public void testMatchProperty_NonMatchingProperty_ReturnsNull() {
        String input = "/foo";
        JsonPointer pointer = JsonPointer.compile(input);
        JsonPointer nextSegment = pointer.matchProperty("bar");
        Assert.assertNull(nextSegment);
    }
    
    @Test
    public void testMatchElement_MatchingElement_ReturnsNextSegment() {
        String input = "/5";
        JsonPointer pointer = JsonPointer.compile(input);
        JsonPointer nextSegment = pointer.matchElement(5);
        Assert.assertNotNull(nextSegment);
    }
    
    @Test
    public void testMatchElement_NonMatchingElement_ReturnsNull() {
        String input = "/5";
        JsonPointer pointer = JsonPointer.compile(input);
        JsonPointer nextSegment = pointer.matchElement(10);
        Assert.assertNull(nextSegment);
    }
    
    @Test
    public void testTail_ReturnsNextSegment() {
        String input = "/foo/bar";
        JsonPointer pointer = JsonPointer.compile(input);
        JsonPointer nextSegment = pointer.tail();
        Assert.assertNotNull(nextSegment);
        Assert.assertEquals("/bar", nextSegment.toString());
    }
    
    @Test
    public void testToString_ReturnsStringRepresentation() {
        String input = "/foo/bar";
        JsonPointer pointer = JsonPointer.compile(input);
        String pointerString = pointer.toString();
        Assert.assertEquals(input, pointerString);
    }
    
    @Test
    public void testHashCode_EqualsHashCodesOfEqualPointers() {
        String input1 = "/foo/bar";
        JsonPointer pointer1 = JsonPointer.compile(input1);
        String input2 = "/foo/bar";
        JsonPointer pointer2 = JsonPointer.compile(input2);
        Assert.assertEquals(pointer1.hashCode(), pointer2.hashCode());
    }
    
    @Test
    public void testEquals_EqualsSameObject_ReturnsTrue() {
        String input = "/foo/bar";
        JsonPointer pointer = JsonPointer.compile(input);
        Assert.assertTrue(pointer.equals(pointer));
    }
    
    @Test
    public void testEquals_EqualsEqualPointers_ReturnsTrue() {
        String input1 = "/foo/bar";
        JsonPointer pointer1 = JsonPointer.compile(input1);
        String input2 = "/foo/bar";
        JsonPointer pointer2 = JsonPointer.compile(input2);
        Assert.assertTrue(pointer1.equals(pointer2));
    }
    
    @Test
    public void testEquals_NotEqualPointers_ReturnsFalse() {
        String input1 = "/foo/bar";
        JsonPointer pointer1 = JsonPointer.compile(input1);
        String input2 = "/bar";
        JsonPointer pointer2 = JsonPointer.compile(input2);
        Assert.assertFalse(pointer1.equals(pointer2));
    }
    
    @Test
    public void testEquals_NotEqualObject_ReturnsFalse() {
        String input = "/foo/bar";
        JsonPointer pointer = JsonPointer.compile(input);
        Assert.assertFalse(pointer.equals(null));
        Assert.assertFalse(pointer.equals(new Object()));
    }
    
    @Test
    public void test_parseTail_ValidInput_ReturnsJsonPointer() {
        String input = "/foo";
        JsonPointer pointer = JsonPointer._parseTail(input);
        Assert.assertNotNull(pointer);
        Assert.assertEquals(input, pointer.toString());
    }
    
    @Test
    public void test_parseQuotedTail_ValidInput_ReturnsJsonPointer() {
        String input = "/~1foo";
        JsonPointer pointer = JsonPointer._parseQuotedTail(input, 0);
        Assert.assertNotNull(pointer);
        Assert.assertEquals(input, pointer.toString());
    }
    
    @Test
    public void test_appendEscape_SpecialCharacter_ReturnsEscapedString() {
        StringBuilder sb = new StringBuilder();
        JsonPointer._appendEscape(sb, '/');
        Assert.assertEquals("~1", sb.toString());
    }
    
    
}