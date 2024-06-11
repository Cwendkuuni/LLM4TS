package com.fasterxml.jackson.core.test;

import com.fasterxml.jackson.core.JsonPointer;
import org.junit.Test;
import static org.junit.Assert.*;

public class JsonPointerTest {

    @Test
    public void testEmptyConstructor() {
        JsonPointer jsonPointer = new JsonPointer();
        assertTrue(jsonPointer.matches());
        assertEquals("", jsonPointer.getMatchingProperty());
        assertEquals(-1, jsonPointer.getMatchingIndex());
        assertEquals("", jsonPointer.toString());
    }

    @Test
    public void testConstructorWithFullStringAndSegment() {
        String fullString = "/level1/level2";
        String segment = "level2";
        JsonPointer jsonPointer = new JsonPointer(fullString, segment, null);
        assertFalse(jsonPointer.matches());
        assertEquals("level2", jsonPointer.getMatchingProperty());
        assertEquals(-1, jsonPointer.getMatchingIndex());
        assertEquals(fullString, jsonPointer.toString());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructorWithInvalidInput() {
        String input = "invalid";
        JsonPointer.compile(input);
    }

    @Test
    public void testCompileMethod() throws IllegalArgumentException {
        String input = "/level1/level2";
        JsonPointer jsonPointer = JsonPointer.compile(input);
        assertFalse(jsonPointer.matches());
        assertEquals("level2", jsonPointer.getMatchingProperty());
        assertEquals(-1, jsonPointer.getMatchingIndex());
        assertEquals(input, jsonPointer.toString());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCompileMethodWithEmptyInput() throws IllegalArgumentException {
        String input = "";
        JsonPointer.compile(input);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCompileMethodWithInputStartingWithSlash() throws IllegalArgumentException {
        String input = "/";
        JsonPointer.compile(input);
    }

    @Test
    public void testMatchPropertyMethod() {
        String fullString = "/level1/level2";
        String matchingProperty = "level2";
        JsonPointer jsonPointer = new JsonPointer(fullString, matchingProperty, null);
        String testProperty = "level2";
        JsonPointer expectedResult = new JsonPointer(fullString, "", null);
        JsonPointer result = jsonPointer.matchProperty(testProperty);
        assertEquals(expectedResult, result);

        jsonPointer = new JsonPointer();
        assertNull(jsonPointer.matchProperty(null));
        assertNull(jsonPointer.matchProperty(""));
    }

    @Test
    public void testMatchElementMethod() {
        String fullString = "/level1/0/level3";
        int matchingIndex = 0;
        JsonPointer jsonPointer = new JsonPointer(fullString, "", null);
        int testIndex = matchingIndex;
        JsonPointer result = jsonPointer.matchElement(testIndex);
        assertEquals(new JsonPointer(fullString, "", null), result);

        jsonPointer = new JsonPointer();
        assertNull(jsonPointer.matchElement(-1));
        assertNull(jsonPointer.matchElement(1));
    }

    @Test
    public void testTailMethod() {
        String fullString = "/level1/level2";
        JsonPointer jsonPointer = new JsonPointer(fullString, "", new JsonPointer(fullString, "", null));
        JsonPointer expectedResult = new JsonPointer(fullString, "", null);
        JsonPointer result = jsonPointer.tail();
        assertEquals(expectedResult, result);
    }
}