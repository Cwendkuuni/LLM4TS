package com.fasterxml.jackson.core;

import org.junit.Test;
import static org.junit.Assert.*;

public class JsonPointerTest {

    // Test cases for the `compile` method

    @Test
    public void testCompile_EmptyInput_ReturnsEmptyJsonPointer() {
        JsonPointer pointer = JsonPointer.compile("");
        
        assertEquals("", pointer.toString);
        assertNull(pointer.tail());
    }

    @Test
    public void testCompile_ValidInput_ReturnsJsonPointer() {
        JsonPointer pointer = JsonPointer.compile("/foo/bar");
        
        assertEquals("/foo/bar", pointer.toString());
        assertEquals("foo", pointer.getMatchingProperty());
        assertEquals(2, pointer.getMatchingIndex());
        assertNull(pointer.tail());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCompile_InvalidInput_ThrowsIllegalArgumentException() {
        JsonPointer pointer = JsonPointer.compile("foo/bar");
    }

    // Test cases for the `matches` method

    @Test
    public void testMatches_EmptyPointer_ReturnsTrue() {
        JsonPointer pointer = JsonPointer.compile("");
        
        assertTrue(pointer.matches());
    }

    @Test
    public void testMatches_NonEmptyPointer_ReturnsFalse() {
        JsonPointer pointer = JsonPointer.compile("/foo/bar");
        
        assertFalse(pointer.matches());
    }

    // Test cases for the `getMatchingProperty` method

    @Test
    public void testGetMatchingProperty_MatchingProperty_ReturnsProperty() {
        JsonPointer pointer = JsonPointer.compile("/foo/bar");
        
        assertEquals("foo", pointer.getMatchingProperty());
    }

    @Test
    public void testGetMatchingProperty_NoMatchingProperty_ReturnsNull() {
        JsonPointer pointer = JsonPointer.compile("/0/bar");
        
        assertNull(pointer.getMatchingProperty());
    }

    // Test cases for the `getMatchingIndex` method

    @Test
    public void testGetMatchingIndex_MatchingIndex_ReturnsIndex() {
        JsonPointer pointer = JsonPointer.compile("/0/bar");
        
        assertEquals(0, pointer.getMatchingIndex());
    }

    @Test
    public void testGetMatchingIndex_NoMatchingIndex_ReturnsNegativeOne() {
        JsonPointer pointer = JsonPointer.compile("/foo/bar");
        
        assertEquals(-1, pointer.getMatchingIndex());
    }

    // Test cases for the `mayMatchProperty` method

    @Test
    public void testMayMatchProperty_MatchingProperty_ReturnsTrue() {
        JsonPointer pointer = JsonPointer.compile("/foo/bar");
        
        assertTrue(pointer.mayMatchProperty());
    }

    @Test
    public void testMayMatchProperty_NoMatchingProperty_ReturnsFalse() {
        JsonPointer pointer = JsonPointer.compile("/0/bar");
        
        assertFalse(pointer.mayMatchProperty());
    }

    // Test cases for the `mayMatchElement` method

    @Test
    public void testMayMatchElement_MatchingIndex_ReturnsTrue() {
        JsonPointer pointer = JsonPointer.compile("/0/bar");
        
        assertTrue(pointer.mayMatchElement());
    }

    @Test
    public void testMayMatchElement_NoMatchingIndex_ReturnsFalse() {
        JsonPointer pointer = JsonPointer.compile("/foo/bar");
        
        assertFalse(pointer.mayMatchElement());
    }

    // Test cases for the `matchProperty` method

    @Test
    public void testMatchProperty_MatchingProperty_ReturnsNextSegment() {
        JsonPointer pointer = JsonPointer.compile("/foo/bar");
        
        JsonPointer nextSegment = pointer.matchProperty("foo");
        
        assertEquals("/bar", nextSegment.toString());
    }

    @Test
    public void testMatchProperty_NoMatchingProperty_ReturnsNull() {
        JsonPointer pointer = JsonPointer.compile("/foo/bar");
        
        JsonPointer nextSegment = pointer.matchProperty("baz");
        
        assertNull(nextSegment);
    }

    // Test cases for the `matchElement` method

    @Test
    public void testMatchElement_MatchingIndex_ReturnsNextSegment() {
        JsonPointer pointer = JsonPointer.compile("/0/bar");
        
        JsonPointer nextSegment = pointer.matchElement(0);
        
        assertEquals("/bar", nextSegment.toString());
    }

    @Test
    public void testMatchElement_NoMatchingIndex_ReturnsNull() {
        JsonPointer pointer = JsonPointer.compile("/0/bar");
        
        JsonPointer nextSegment = pointer.matchElement(1);
        
        assertNull(nextSegment);
    }

    // Test cases for the `tail` method

    @Test
    public void testTail_NonEmptyPointer_ReturnsNextSegment() {
        JsonPointer pointer = JsonPointer.compile("/foo/bar");
        
        JsonPointer tail = pointer.tail();
        
        assertEquals("/bar", tail.toString());
    }

    @Test
    public void testTail_EmptyPointer_ReturnsNull() {
        JsonPointer pointer = JsonPointer.compile("");
        
        assertNull(pointer.tail());
    }

    // Test cases for the `toString` method

    @Test
    public void testToString_NonEmptyPointer_ReturnsPointerAsString() {
        JsonPointer pointer = JsonPointer.compile("/foo/bar");
        
        assertEquals("/foo/bar", pointer.toString());
    }

    @Test
    public void testToString_EmptyPointer_ReturnsEmptyString() {
        JsonPointer pointer = JsonPointer.compile("");
        
        assertEquals("", pointer.toString());
    }

    // Test cases for the `hashCode` method

    @Test
    public void testHashCode_EqualPointers_ReturnsSameHashCode() {
        JsonPointer pointer1 = JsonPointer.compile("/foo/bar");
        JsonPointer pointer2 = JsonPointer.compile("/foo/bar");
        
        assertEquals(pointer1.hashCode(), pointer2.hashCode());
    }

    // Test cases for the `equals` method

    @Test
    public void testEquals_SamePointer_ReturnsTrue() {
        JsonPointer pointer1 = JsonPointer.compile("/foo/bar");
        JsonPointer pointer2 = JsonPointer.compile("/foo/bar");
        
        assertTrue(pointer1.equals(pointer2));
    }

    @Test
    public void testEquals_DifferentPointer_ReturnsFalse() {
        JsonPointer pointer1 = JsonPointer.compile("/foo/bar");
        JsonPointer pointer2 = JsonPointer.compile("/baz/qux");
        
        assertFalse(pointer1.equals(pointer2));
    }

    // Test cases for the private `_parseIndex` method

    @Test
    public void testParseIndex_ValidIndex_ReturnsIndex() {
        int result = JsonPointer._parseIndex("42");
        
        assertEquals(42, result);
    }

    @Test
    public void testParseIndex_InvalidIndex_ReturnsNegativeOne() {
        int result = JsonPointer._parseIndex("abc");
        
        assertEquals(-1, result);
    }

    // Test cases for the private `_parseTail` method

    @Test
    public void testParseTail_NonEscapedPointer_ReturnsJsonPointer() {
        JsonPointer pointer = JsonPointer._parseTail("/foo/bar");
        
        assertEquals("/foo/bar", pointer.toString());
        assertEquals("foo", pointer.getMatchingProperty());
        assertEquals(2, pointer.getMatchingIndex());
        assertNull(pointer.tail());
    }

    @Test
    public void testParseTail_EscapedPointer_ReturnsJsonPointer() {
        JsonPointer pointer = JsonPointer._parseTail("/~0/~1");
        
        assertEquals("/~0/~1", pointer.toString());
        assertNull(pointer.getMatchingProperty());
        assertEquals(2, pointer.getMatchingIndex());
        assertNull(pointer.tail());
    }

    // Test cases for the private `_parseQuotedTail` method

    @Test
    public void testParseQuotedTail_EscapedPointer_ReturnsJsonPointer() {
        JsonPointer pointer = JsonPointer._parseQuotedTail("/~0/foo/~1", 3);
        
        assertEquals("/~0/foo/~1", pointer.toString());
        assertEquals("~0", pointer.getMatchingProperty());
        assertNull(pointer.tail());
    }

    // Test cases for the private `_appendEscape` method

    @Test
    public void testAppendEscape_EscapeCharacter_ReplacesCharacter() {
        StringBuilder sb = new StringBuilder();
        
        JsonPointer._appendEscape(sb, '0');
        
        assertEquals("~0", sb.toString());
    }

    @Test
    public void testAppendEscape_NoEscapeCharacter_KeepsCharacter() {
        StringBuilder sb = new StringBuilder();
        
        JsonPointer._appendEscape(sb, 'a');
        
        assertEquals("a", sb.toString());
    }

}