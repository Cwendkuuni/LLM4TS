package com.fasterxml.jackson.core.test;

import static org.junit.Assert.*;

import com.fasterxml.jackson.core.JsonPointer;

import org.junit.Test;

/**
 * Test class for JsonPointer class
 */
public class JsonPointerTest {

    /**
     * Test constructor for empty instance
     */
    @Test
    public void testEmptyConstructor() {
        JsonPointer empty = JsonPointer.EMPTY;
        assertTrue(empty.matches());
        assertNull(empty.tail());
        assertEquals("", empty.toString());
    }

    /**
     * Test constructor for non-empty instance
     */
    @Test
    public void testNonEmptyConstructor() {
        JsonPointer pointer = new JsonPointer("/test/property", "property", null);
        assertFalse(pointer.matches());
        assertEquals("/test/property", pointer.toString());
        assertEquals("property", pointer.getMatchingProperty());
        assertEquals(-1, pointer.getMatchingIndex());
        assertEquals(pointer, pointer.tail());
    }

    /**
     * Test compile method with valid JSON Pointer expression
     */
    @Test
    public void testCompileValidJsonPointer() throws IllegalArgumentException {
        String input = "/test/property";
        JsonPointer pointer = JsonPointer.compile(input);
        assertEquals("/test/property", pointer.toString());
        assertEquals("property", pointer.getMatchingProperty());
        assertEquals(-1, pointer.getMatchingIndex());
        assertFalse(pointer.matches());
    }

    /**
     * Test compile method with invalid JSON Pointer expression
     */
    @Test(expected = IllegalArgumentException.class)
    public void testCompileInvalidJsonPointer() throws IllegalArgumentException {
        String input = "test/property";
        JsonPointer.compile(input);
    }

    /**
     * Test factory method fromSegment with valid segments
     */
    //TODO: Add test case for fromSegment method

    /**
     * Test matches method
     */
    @Test
    public void testMatches() {
        JsonPointer pointer = new JsonPointer("/test/property", "property", null);
        JsonPointer otherPointer = new JsonPointer("/test/otherProperty", "otherProperty", null);
        JsonPointer empty = JsonPointer.EMPTY;

        assertTrue(pointer.matches());
        assertFalse(otherPointer.matches());
        assertTrue(empty.matches());
    }

    /**
     * Test getMatchingProperty method
     */
    @Test
    public void testGetMatchingProperty() {
        JsonPointer pointer = new JsonPointer("/test/property/subProperty", "subProperty", null);
        assertEquals("subProperty", pointer.getMatchingProperty());
    }

    /**
     * Test getMatchingIndex method
     */
    @Test
    public void testGetMatchingIndex() {
        JsonPointer pointer = new JsonPointer("/test/0/subProperty", null, null);
        assertEquals(0, pointer.getMatchingIndex());
    }

    /**
     * Test mayMatchProperty method
     */
    @Test
    public void testMayMatchProperty() {
        JsonPointer pointer = new JsonPointer("/test/property", "property", null);
        assertTrue(pointer.mayMatchProperty());
        JsonPointer empty = JsonPointer.EMPTY;
        assertFalse(empty.mayMatchProperty());
    }

    /**
     * Test mayMatchElement method
     */
    @Test
    public void testMayMatchElement() {
        JsonPointer pointer = new JsonPointer("/test/0/subProperty", null, null);
        assertTrue(pointer.mayMatchElement());
        JsonPointer empty = JsonPointer.EMPTY;
        assertFalse(empty.mayMatchElement());
    }

    /**
     * Test matchProperty method
     */
    @Test
    public void testMatchProperty() {
        JsonPointer pointer = new JsonPointer("/test/property", "property", null);
        JsonPointer otherPointer = new JsonPointer("/test/otherProperty", "otherProperty", null);
        JsonPointer matchingProperty = new JsonPointer("/test/property", "property", null);

        assertEquals(matchingProperty, pointer.matchProperty("property"));
        assertNull(pointer.matchProperty("otherProperty"));
        assertNull(otherPointer.matchProperty("property"));
    }

    /**
     * Test matchElement method
     */
    @Test
    public void testMatchElement() {
        JsonPointer pointer = new JsonPointer("/test/0/subProperty", null, null);
        JsonPointer otherPointer = new JsonPointer("/test/1/subProperty", null, null);
        JsonPointer matchingElement = new JsonPointer("/test/0/subProperty", null, null);

        assertEquals(matchingElement, pointer.matchElement(0));
        assertNull(pointer.matchElement(-1));
        assertNull(otherPointer.matchElement(0));
    }

    /**
     * Test tail method
     */
    @Test
    public void testTail() {
        JsonPointer pointer = new JsonPointer("/test/property/subProperty", "subProperty", null);
        assertEquals(new JsonPointer("/test/property", "property", null), pointer.tail());
    }

    /**
     * Test toString method
     */
    @Test
    public void testToString() {
        JsonPointer pointer = new JsonPointer("/test/property/subProperty", "subProperty", null);
        assertEquals("/test/property/subProperty", pointer.toString());
    }

    /**
     * Test hashCode method
     */
    @Test
    public void testHashCode() {
        JsonPointer pointer1 = new JsonPointer("/test/property/subProperty", "subProperty", null);
        JsonPointer pointer2 = new JsonPointer("/test/property/subProperty", "subProperty", null);
        JsonPointer pointer3 = new JsonPointer("/test/otherProperty/subProperty", "subProperty", null);

        assertEquals(hashCode(pointer1), hashCode(pointer2));
        assertNotEquals(hashCode(pointer1), hashCode(pointer3));
    }

    /**
     * Test equals method
     */
    @Test
    public void testEquals() {
        JsonPointer pointer1 = new JsonPointer("/test/property/subProperty", "subProperty", null);
        JsonPointer pointer2 = new JsonPointer("/test/property/subProperty", "subProperty", null);
        JsonPointer pointer3 = new JsonPointer("/test/otherProperty/subProperty", "subProperty", null);

        assertTrue(pointer1.equals(pointer2));
        assertFalse(pointer1.equals(pointer3));
        assertFalse(pointer1.equals(new Object()));
    }

    /**
     * Test parseIndex method
     */
    @Test
    public void testParseIndex() {
        assertEquals(0, JsonPointer._parseIndex("0"));
        assertEquals(1, JsonPointer._parseIndex("1"));
        assertEquals(-1, JsonPointer._parseIndex(""));
        assertEquals(-1, JsonPointer._parseIndex("10"));
        assertEquals(-1, JsonPointer._parseIndex("abc"));
    }

    /**
     * Test parseTail method with valid JSON Pointer expression
     */
    @Test
    public void testParseTailValidJsonPointer() {
        String input = "/test/property/subProperty";
        JsonPointer pointer = JsonPointer._parseTail(input);
        assertEquals("/test/property", pointer._asString.substring(0, pointer._asString.lastIndexOf('/')));
        assertEquals("subProperty", pointer._matchingPropertyName);
        assertNull(pointer._nextSegment);
    }

    /**
     * Test parseTail method with escaped character
     */
    @Test
    public void testParseTailEscapedCharacter() {
        String input = "/test/property/subProperty~1";
        JsonPointer pointer = JsonPointer._parseTail(input);
        assertEquals("/test/property", pointer._asString.substring(0, pointer._asString.lastIndexOf('/')));
        assertEquals("/", pointer._matchingPropertyName.charAt(0));
        assertEquals("/", pointer._matchingPropertyName.charAt(1));
        assertEquals("/", pointer._matchingPropertyName.charAt(2));
        assertEquals("1", pointer._matchingPropertyName.substring(3));
        assertNull(pointer._nextSegment);
    }

    /**
     * Test parseTail method with multiple escaped characters
     */
    @Test
    public void testParseTailMultipleEscapedCharacters() {
        String input = "/test/property/subProperty~1/subProperty~0";
        JsonPointer pointer = JsonPointer._parseTail(input);
        assertEquals("/test/property/subProperty", pointer._asString.substring(0, pointer._asString.lastIndexOf('/')));
        assertEquals("/subProperty", pointer._matchingPropertyName);
        assertNull(pointer._nextSegment);
    }

    private int hashCode(JsonPointer pointer) {
        return pointer.toString().hashCode();
    }
}