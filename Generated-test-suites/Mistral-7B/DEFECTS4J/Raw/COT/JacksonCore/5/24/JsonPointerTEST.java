package com.fasterxml.jackson.core.test;

import static org.junit.Assert.*;

import com.fasterxml.jackson.core.JsonPointer;

import org.junit.Test;

/**
 * Test class for JsonPointer class
 */
public class JsonPointerTest {

    // Basic test cases for constructor methods

    @Test
    public void testEmptyConstructor() {
        JsonPointer jsonPointer = new JsonPointer();
        assertNull(jsonPointer._nextSegment);
        assertEquals("", jsonPointer._asString);
        assertEquals("", jsonPointer._matchingPropertyName);
        assertEquals(-1, jsonPointer._matchingElementIndex);
    }

    @Test
    public void testNonEmptyConstructor() {
        String fullString = "/test/property1/element1";
        String segment = "property1";
        JsonPointer next = new JsonPointer("/test/property2");
        JsonPointer jsonPointer = new JsonPointer(fullString, segment, next);

        assertEquals(fullString, jsonPointer._asString);
        assertEquals(next, jsonPointer._nextSegment);
        assertEquals("property1", jsonPointer._matchingPropertyName);
        assertEquals(0, jsonPointer._matchingElementIndex);
    }

    // Test case for compile factory method

    @Test
    public void testCompileFactoryMethod() {
        String jsonPointerString = "/test/property1/element1";
        JsonPointer jsonPointer = JsonPointer.compile(jsonPointerString);

        assertEquals(jsonPointerString, jsonPointer._asString);
        assertNull(jsonPointer._nextSegment);
        assertEquals("property1", jsonPointer._matchingPropertyName);
        assertEquals(0, jsonPointer._matchingElementIndex);
    }

    // Test case for matches method

    @Test
    public void testMatchesMethod() {
        JsonPointer jsonPointer = new JsonPointer("/test/property1");
        assertTrue(jsonPointer.matches());

        jsonPointer = new JsonPointer("/test/property1/element1");
        assertFalse(jsonPointer.matches());
    }

    // Test case for getMatchingProperty method

    @Test
    public void testGetMatchingPropertyMethod() {
        JsonPointer jsonPointer = new JsonPointer("/test/property1");
        assertEquals("property1", jsonPointer.getMatchingProperty());

        jsonPointer = new JsonPointer("/test/property1/element1");
        assertNull(jsonPointer.getMatchingProperty());
    }

    // Test case for getMatchingIndex method

    @Test
    public void testGetMatchingIndexMethod() {
        JsonPointer jsonPointer = new JsonPointer("/test/property1/element1");
        assertEquals(0, jsonPointer.getMatchingIndex());

        jsonPointer = new JsonPointer("/test/property1");
        assertEquals(-1, jsonPointer.getMatchingIndex());
    }

    // Test case for mayMatchProperty method

    @Test
    public void testMayMatchPropertyMethod() {
        JsonPointer jsonPointer = new JsonPointer("/test/property1");
        assertTrue(jsonPointer.mayMatchProperty());

        jsonPointer = new JsonPointer("/test/property1/element1");
        assertFalse(jsonPointer.mayMatchProperty());
    }

    // Test case for mayMatchElement method

    @Test
    public void testMayMatchElementMethod() {
        JsonPointer jsonPointer = new JsonPointer("/test/property1/element1");
        assertTrue(jsonPointer.mayMatchElement());

        jsonPointer = new JsonPointer("/test/property1");
        assertFalse(jsonPointer.mayMatchElement());
    }

    // Test case for matchProperty method

    @Test
    public void testMatchPropertyMethod() {
        String propertyName = "property1";
        JsonPointer jsonPointer = new JsonPointer("/test/property1");

        JsonPointer matchedJsonPointer = jsonPointer.matchProperty(propertyName);
        assertEquals(jsonPointer, matchedJsonPointer);

        jsonPointer = new JsonPointer("/test/property1/property2");
        assertNull(jsonPointer.matchProperty(propertyName));
    }

    // Test case for matchElement method

    @Test
    public void testMatchElementMethod() {
        int index = 0;
        JsonPointer jsonPointer = new JsonPointer("/test/property1/element0");

        JsonPointer matchedJsonPointer = jsonPointer.matchElement(index);
        assertEquals(jsonPointer, matchedJsonPointer);

        jsonPointer = new JsonPointer("/test/property1/element1");
        assertNull(jsonPointer.matchElement(index));

        jsonPointer = new JsonPointer("/test/property1/element0");
        assertNull(jsonPointer.matchElement(-1));
    }

    // Test case for tail method

    @Test
    public void testTailMethod() {
        JsonPointer jsonPointer = new JsonPointer("/test/property1");
        JsonPointer tailJsonPointer = jsonPointer.tail();
        assertEquals(jsonPointer._nextSegment, tailJsonPointer);

        jsonPointer = new JsonPointer("/test/property1/element1");
        tailJsonPointer = jsonPointer.tail();
        assertNull(tailJsonPointer);
    }

    // Test case for toString method

    @Test
    public void testToStringMethod() {
        JsonPointer jsonPointer = new JsonPointer("/test/property1");
        assertEquals("/test/property1", jsonPointer.toString());

        jsonPointer = new JsonPointer("/test/property1/element1");
        assertEquals("/test/property1/element1", jsonPointer.toString());
    }

    // Test case for hashCode and equals methods

    @Test
    public void testHashCodeAndEqualsMethods() {
        String jsonPointerString1 = "/test/property1";
        String jsonPointerString2 = "/test/property1";
        JsonPointer jsonPointer1 = new JsonPointer(jsonPointerString1, null, EMPTY);
        JsonPointer jsonPointer2 = new JsonPointer(jsonPointerString2, null, EMPTY);

        assertEquals(jsonPointer1.hashCode(), jsonPointer2.hashCode());
        assertTrue(jsonPointer1.equals(jsonPointer2));

        JsonPointer jsonPointer3 = new JsonPointer("/test/property1/element1");
        assertNotEquals(jsonPointer1.hashCode(), jsonPointer3.hashCode());
        assertFalse(jsonPointer1.equals(jsonPointer3));
    }

    // Edge cases and exception handling test cases

    @Test(expected = IllegalArgumentException.class)
    public void testCompileInvalidInput() {
        JsonPointer.compile("invalid");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCompileInputWithoutLeadingSlash() {
        JsonPointer.compile("property1");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCompileInputWithTrailingSlash() {
        JsonPointer.compile("/test/property1/");
    }

    @Test
    public void testParseTailWithQuotedSegment() {
        String input = "/test/property1/~0~/element1";
        JsonPointer jsonPointer = JsonPointer._parseTail(input);
        assertEquals("/test/property1/element1", jsonPointer._asString);
    }

    @Test
    public void testParseTailWithEscapedCharacter() {
        String input = "/test/property1/~0~\u002Felement1";
        JsonPointer jsonPointer = JsonPointer._parseTail(input);
        assertEquals("/test/property1/element1", jsonPointer._asString);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseTailWithInvalidCharacterAfterTilde() {
        String input = "/test/property1/~X";
        JsonPointer. _parseTail(input);
    }

    @Test
    public void testCompileAndMatchEmptyPointer() {
        JsonPointer jsonPointer = JsonPointer.compile("");
        assertTrue(jsonPointer.matches());
        assertEquals(jsonPointer, JsonPointer.EMPTY);
    }

    @Test
    public void testCompileAndMatchNullPointer() {
        JsonPointer jsonPointer = JsonPointer.compile(null);
        assertTrue(jsonPointer.matches());
        assertEquals(jsonPointer, JsonPointer.EMPTY);
    }

    @Test
    public void testCompileAndMatchEmptyStringProperty() {
        JsonPointer jsonPointer1 = JsonPointer.compile("/test/property1");
        JsonPointer jsonPointer2 = JsonPointer.compile("/test/property1/");
        assertTrue(jsonPointer1.matches());
        assertTrue(jsonPointer1.mayMatchProperty());
        assertFalse(jsonPointer1.mayMatchElement());
        assertNull(jsonPointer1.getMatchingProperty());
        assertEquals(jsonPointer2, jsonPointer1.tail());
        assertTrue(jsonPointer2.matches());
        assertTrue(jsonPointer2.mayMatchProperty());
        assertFalse(jsonPointer2.mayMatchElement());
        assertNull(jsonPointer2.getMatchingProperty());
        assertNull(jsonPointer2.getMatchingIndex());
        assertEquals(jsonPointer1, jsonPointer2.tail());
        assertEquals(jsonPointer1._asString, jsonPointer2._asString);
    }

    @Test
    public void testCompileAndMatchSingleProperty() {
        JsonPointer jsonPointer1 = JsonPointer.compile("/test/property1");
        JsonPointer jsonPointer2 = JsonPointer.compile("/test/property2");
        assertFalse(jsonPointer1.matches());
        assertFalse(jsonPointer1.mayMatchProperty());
        assertFalse(jsonPointer1.mayMatchElement());
        assertNull(jsonPointer1.getMatchingProperty());
        assertNull(jsonPointer1.getMatchingIndex());
        assertEquals(jsonPointer1._asString, jsonPointer1._asString);
        assertTrue(jsonPointer2.matches());
        assertFalse(jsonPointer2.mayMatchProperty());
        assertFalse(jsonPointer2.mayMatchElement());
        assertNull(jsonPointer2.getMatchingProperty());
        assertNull(jsonPointer2.getMatchingIndex());
        assertEquals(jsonPointer2._asString, jsonPointer2._asString);
        assertFalse(jsonPointer1.equals(jsonPointer2));
    }

    @Test
    public void testCompileAndMatchSingleElement() {
        JsonPointer jsonPointer1 = JsonPointer.compile("/test/property1/element1");
        JsonPointer jsonPointer2 = JsonPointer.compile("/test/property1/element2");
        assertFalse(jsonPointer1.matches());
        assertTrue(jsonPointer1.mayMatchProperty());
        assertTrue(jsonPointer1.mayMatchElement());
        assertEquals("property1", jsonPointer1.getMatchingProperty());
        assertEquals(0, jsonPointer1.getMatchingIndex());
        assertEquals(jsonPointer1._asString, jsonPointer1._asString);
        assertFalse(jsonPointer2.matches());
        assertTrue(jsonPointer2.mayMatchProperty());
        assertTrue(jsonPointer2.mayMatchElement());
        assertEquals("property1", jsonPointer2.getMatchingProperty());
        assertEquals(-1, jsonPointer2.getMatchingIndex());
        assertEquals(jsonPointer1._asString, jsonPointer2._asString);
        assertFalse(jsonPointer1.equals(jsonPointer2));
    }

    @Test
    public void testCompileAndMatchMultipleSegments() {
        JsonPointer jsonPointer1 = JsonPointer.compile("/test/property1/property2/element1");
        JsonPointer jsonPointer2 = JsonPointer.compile("/test/property1/property3/element1");
        JsonPointer jsonPointer3 = JsonPointer.compile("/test/property3/property2/element1");

        assertFalse(jsonPointer1.matches());
        assertTrue(jsonPointer1.mayMatchProperty());
        assertTrue(jsonPointer1.mayMatchElement());
        assertEquals("property1", jsonPointer1.getMatchingProperty());
        assertEquals(0, jsonPointer1.getMatchingIndex());
        assertEquals(jsonPointer1._asString, jsonPointer1._asString);
        assertFalse(jsonPointer2.matches());
        assertTrue(jsonPointer2.mayMatchProperty());
        assertTrue(jsonPointer2.mayMatchElement());
        assertEquals("property1", jsonPointer2.getMatchingProperty());
        assertEquals(-1, jsonPointer2.getMatchingIndex());
        assertEquals(jsonPointer1._asString, jsonPointer2._asString);
        assertFalse(jsonPointer3.matches());
        assertTrue(jsonPointer3.mayMatchProperty());
        assertTrue(jsonPointer3.mayMatchElement());
        assertEquals("property3", jsonPointer3.getMatchingProperty());
        assertEquals(-1, jsonPointer3.getMatchingIndex());
        assertEquals(jsonPointer1._asString, jsonPointer3._asString);
        assertFalse(jsonPointer1.equals(jsonPointer2));
        assertFalse(jsonPointer1.equals(jsonPointer3));
    }
}