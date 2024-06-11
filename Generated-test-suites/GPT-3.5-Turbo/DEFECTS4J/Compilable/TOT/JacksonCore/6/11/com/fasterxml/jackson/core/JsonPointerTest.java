package com.fasterxml.jackson.core;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class JsonPointerTest {

    private JsonPointer jsonPointer;

    @Before
    public void setUp() {
        jsonPointer = JsonPointer.compile("/some/valid/pointer");
    }

    @Test
    public void testCompile_validInput_returnsJsonPointer() {
        JsonPointer result = JsonPointer.compile("/valid/pointer");
        assertNotNull(result);
        assertEquals("/valid/pointer", result.toString());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCompile_invalidInput_throwsIllegalArgumentException() {
        JsonPointer.compile("invalid/pointer");
    }

    @Test
    public void testValueOf_validInput_returnsJsonPointer() {
        JsonPointer result = JsonPointer.valueOf("/valid/pointer");
        assertNotNull(result);
        assertEquals("/valid/pointer", result.toString());
    }

    @Test
    public void testMatches_matchingPointer_returnsTrue() {
        assertTrue(jsonPointer.matches());
    }

    @Test
    public void testMatches_nonMatchingPointer_returnsFalse() {
        assertFalse(jsonPointer.tail().matches());
    }

    @Test
    public void testGetMatchingProperty_matchingProperty_returnsMatchingProperty() {
        String result = jsonPointer.getMatchingProperty();
        assertEquals("some", result);
    }

    @Test
    public void testGetMatchingProperty_nonMatchingProperty_returnsNull() {
        JsonPointer pointer = jsonPointer.tail().tail();
        assertNull(pointer.getMatchingProperty());
    }

    @Test
    public void testGetMatchingIndex_matchingIndex_returnsMatchingIndex() {
        int result = jsonPointer.getMatchingIndex();
        assertEquals(-1, result);
    }

    @Test
    public void testGetMatchingIndex_nonMatchingIndex_returnsNegativeOne() {
        JsonPointer pointer = JsonPointer.compile("/some/valid/0").tail().tail();
        assertEquals(-1, pointer.getMatchingIndex());
    }

    @Test
    public void testMayMatchProperty_matchingProperty_returnsTrue() {
        assertTrue(jsonPointer.mayMatchProperty());
    }

    @Test
    public void testMayMatchProperty_nonMatchingProperty_returnsFalse() {
        JsonPointer pointer = jsonPointer.tail().tail();
        assertFalse(pointer.mayMatchProperty());
    }

    @Test
    public void testMayMatchElement_matchingIndex_returnsTrue() {
        assertTrue(jsonPointer.mayMatchElement());
    }

    @Test
    public void testMayMatchElement_nonMatchingIndex_returnsFalse() {
        JsonPointer pointer = JsonPointer.compile("/some/valid/0").tail().tail();
        assertFalse(pointer.mayMatchElement());
    }

    @Test
    public void testMatchProperty_matchingProperty_returnsNextSegment() {
        JsonPointer result = jsonPointer.matchProperty("valid");
        assertNotNull(result);
        assertEquals("pointer", result.toString());
    }

    @Test
    public void testMatchProperty_nonMatchingProperty_returnsNull() {
        JsonPointer result = jsonPointer.matchProperty("nonMatching");
        assertNull(result);
    }

    @Test
    public void testMatchElement_matchingIndex_returnsNextSegment() {
        JsonPointer result = jsonPointer.matchElement(0);
        assertNotNull(result);
        assertEquals("valid/pointer", result.toString());
    }

    @Test
    public void testMatchElement_nonMatchingIndex_returnsNull() {
        JsonPointer result = jsonPointer.matchElement(1);
        assertNull(result);
    }

    @Test
    public void testTail_returnsNextSegment() {
        JsonPointer result = jsonPointer.tail();
        assertNotNull(result);
        assertEquals("valid/pointer", result.toString());
    }

    @Test
    public void testToString_returnsStringRepresentation() {
        assertEquals("/some/valid/pointer", jsonPointer.toString());
    }

    @Test
    public void testHashCode_sameJsonPointers_returnSameHashCode() {
        JsonPointer pointer1 = JsonPointer.compile("/some/valid/pointer");
        JsonPointer pointer2 = JsonPointer.compile("/some/valid/pointer");
        assertEquals(pointer1.hashCode(), pointer2.hashCode());
    }

    @Test
    public void testEquals_sameJsonPointers_returnsTrue() {
        JsonPointer pointer1 = JsonPointer.compile("/some/valid/pointer");
        JsonPointer pointer2 = JsonPointer.compile("/some/valid/pointer");
        assertTrue(pointer1.equals(pointer2));
    }

    @Test
    public void testEquals_differentJsonPointers_returnsFalse() {
        JsonPointer pointer1 = JsonPointer.compile("/some/valid/pointer");
        JsonPointer pointer2 = JsonPointer.compile("/another/valid/pointer");
        assertFalse(pointer1.equals(pointer2));
    }
}