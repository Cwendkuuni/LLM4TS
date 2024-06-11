package com.example;

import org.junit.Test;
import static org.junit.Assert.*;
import com.fasterxml.jackson.core.JsonPointer;

public class JsonPointerTest {
    @Test
    public void testCompileValidJsonPointer() {
        String jsonPointer = "/foo/bar/1";
        JsonPointer expectedJsonPointer = new JsonPointer("/foo/bar/1");
        JsonPointer actualJsonPointer = JsonPointer.compile(jsonPointer);
        assertEquals(expectedJsonPointer, actualJsonPointer);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCompileInvalidJsonPointer() {
        String invalidJsonPointer = "baz";
        JsonPointer jsonPointer = JsonPointer.compile(invalidJsonPointer);
        // Should throw an IllegalArgumentException
    }

    @Test
    public void testValueOfValidJsonPointer() {
        String jsonPointer = "/foo/bar/1";
        JsonPointer expectedJsonPointer = new JsonPointer("/foo/bar/1");
        JsonPointer actualJsonPointer = JsonPointer.valueOf(jsonPointer);
        assertEquals(expectedJsonPointer, actualJsonPointer);
    }

    @Test
    public void testEmptyJsonPointer() {
        JsonPointer expectedJsonPointer = JsonPointer.EMPTY;
        JsonPointer actualJsonPointer = new JsonPointer();
        assertEquals(expectedJsonPointer, actualJsonPointer);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCompileEmptyJsonPointerInput() {
        String emptyJsonPointer = "";
        JsonPointer jsonPointer = JsonPointer.compile(emptyJsonPointer);
        // Should throw an IllegalArgumentException
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCompileOneSegmentJsonPointer() {
        String oneSegmentJsonPointer = "/foo";
        JsonPointer jsonPointer = JsonPointer.compile(oneSegmentJsonPointer);
        // Should throw an IllegalArgumentException
    }

    @Test
    public void testCompileMultipleSegmentsJsonPointer() {
        String multipleSegmentsJsonPointer = "/foo/bar/1";
        JsonPointer expectedJsonPointer = new JsonPointer("/foo/bar/1");
        JsonPointer actualJsonPointer = JsonPointer.compile(multipleSegmentsJsonPointer);
        assertEquals(expectedJsonPointer, actualJsonPointer);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCompileInvalidJsonPointerCharacters() {
        String invalidJsonPointer = "/foo/bar%";
        JsonPointer jsonPointer = JsonPointer.compile(invalidJsonPointer);
        // Should throw an IllegalArgumentException
    }

    @Test
    public void testValueOfEmptyJsonPointerInput() {
        String emptyJsonPointer = "";
        JsonPointer jsonPointer = JsonPointer.valueOf(emptyJsonPointer);
        assertEquals(JsonPointer.EMPTY, jsonPointer);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testValueOfInvalidJsonPointer() {
        String invalidJsonPointer = "baz";
        JsonPointer jsonPointer = JsonPointer.valueOf(invalidJsonPointer);
        // Should throw an IllegalArgumentException
    }

    @Test(expected = NullPointerException.class)
    public void testFromSegmentNullSegments() {
        String[] segments = null;
        JsonPointer expectedJsonPointer = new JsonPointer("/");
        JsonPointer jsonPointer = JsonPointer.fromSegment(segments);
        assertEquals(expectedJsonPointer, jsonPointer);
    }

    @Test
    public void testFromSegmentValidJsonPointerSegments() {
        String[] jsonPointerSegments = {"foo", "bar", "1"};
        JsonPointer expectedJsonPointer = new JsonPointer("/foo/bar/1");
        JsonPointer jsonPointer = JsonPointer.fromSegment(jsonPointerSegments);
        assertEquals(expectedJsonPointer, jsonPointer);
    }

    @Test
    public void testMatchesForEmptyJsonPointer() {
        JsonPointer jsonPointer = JsonPointer.EMPTY;
        assertTrue(jsonPointer.matches());
    }

    @Test
    public void testMatchesForNonEmptyJsonPointer() {
        JsonPointer jsonPointer = new JsonPointer("/foo/bar/1");
        assertFalse(jsonPointer.matches());
    }

    @Test
    public void testGetMatchingPropertyEmptyJsonPointer() {
        JsonPointer jsonPointer = JsonPointer.EMPTY;
        assertEquals("", jsonPointer.getMatchingProperty());
    }

    @Test(expected = IllegalStateException.class)
    public void testGetMatchingPropertyNonEmptyJsonPointer() {
        JsonPointer jsonPointer = new JsonPointer("/foo/bar/1");
        // Should throw an IllegalStateException
    }

    @Test
    public void testGetMatchingIndexForEmptyJsonPointer() {
        JsonPointer jsonPointer = JsonPointer.EMPTY;
        assertEquals(-1, jsonPointer.getMatchingIndex());
    }

    @Test
    public void testGetMatchingIndexForNonEmptyJsonPointer() {
        JsonPointer jsonPointer = new JsonPointer("/foo/bar/1");
        assertEquals(1, jsonPointer.getMatchingIndex());
    }

    @Test
    public void testMayMatchPropertyForEmptyJsonPointer() {
        JsonPointer jsonPointer = JsonPointer.EMPTY;
        assertFalse(jsonPointer.mayMatchProperty());
    }

    @Test
    public void testMayMatchPropertyForNonEmptyJsonPointer() {
        JsonPointer jsonPointer = new JsonPointer("/foo/bar/1");
        assertTrue(jsonPointer.mayMatchProperty());
    }

    @Test
    public void testMayMatchElementForEmptyJsonPointer() {
        JsonPointer jsonPointer = JsonPointer.EMPTY;
        assertFalse(jsonPointer.mayMatchElement());
    }

    @Test
    public void testMayMatchElementForNonEmptyJsonPointer() {
        JsonPointer jsonPointer = new JsonPointer("/foo/bar/1");
        assertTrue(jsonPointer.mayMatchElement());
    }

    @Test
    public void testMatchPropertyForEmptyJsonPointer() {
        JsonPointer jsonPointer = JsonPointer.EMPTY;
        JsonPointer expectedJsonPointer = JsonPointer.EMPTY;
        JsonPointer actualJsonPointer = jsonPointer.matchProperty("property");
        assertEquals(expectedJsonPointer, actualJsonPointer);
    }

    @Test
    public void testMatchPropertyForNonEmptyJsonPointer() {
        JsonPointer jsonPointer = new JsonPointer("/foo/bar/1");
        String property = "bar";
        JsonPointer expectedJsonPointer = new JsonPointer("/foo/bar");
        JsonPointer actualJsonPointer = jsonPointer.matchProperty(property);
        assertEquals(expectedJsonPointer, actualJsonPointer);
    }

    @Test
    public void testMatchElementForEmptyJsonPointer() {
        JsonPointer jsonPointer = JsonPointer.EMPTY;
        int index = 0;
        JsonPointer expectedJsonPointer = JsonPointer.EMPTY;
        JsonPointer actualJsonPointer = jsonPointer.matchElement(index);
        assertEquals(expectedJsonPointer, actualJsonPointer);
    }

    @Test
    public void testMatchElementForNonEmptyJsonPointer() {
        JsonPointer jsonPointer = new JsonPointer("/foo/bar/1");
        int index = 1;
        JsonPointer expectedJsonPointer = new JsonPointer("/bar");
        JsonPointer actualJsonPointer = jsonPointer.matchElement(index);
        assertEquals(expectedJsonPointer, actualJsonPointer);
    }

    @Test
    public void testTailForEmptyJsonPointer() {
        JsonPointer jsonPointer = JsonPointer.EMPTY;
        JsonPointer expectedJsonPointer = JsonPointer.EMPTY;
        JsonPointer actualJsonPointer = jsonPointer.tail();
        assertEquals(expectedJsonPointer, actualJsonPointer);
    }

    @Test
    public void testTailForNonEmptyJsonPointer() {
        JsonPointer jsonPointer = new JsonPointer("/foo/bar/1");
        JsonPointer expectedJsonPointer = new JsonPointer("/bar");
        JsonPointer actualJsonPointer = jsonPointer.tail();
        assertEquals(expectedJsonPointer, actualJsonPointer);
    }

    @Test
    public void testToString() {
        JsonPointer jsonPointer = new JsonPointer("/foo/bar/1");
        String expectedToString = "/foo/bar/1";
        String actualToString = jsonPointer.toString();
        assertEquals(expectedToString, actualToString);
    }

    @Test
    public void testHashCode() {
        JsonPointer jsonPointer1 = new JsonPointer("/foo/bar/1");
        JsonPointer jsonPointer2 = new JsonPointer("/foo/bar/1");
        int expectedHashCode = jsonPointer1.hashCode();
        int actualHashCode = jsonPointer2.hashCode();
        assertEquals(expectedHashCode, actualHashCode);
    }

    @Test
    public void testEqualsForSameObjects() {
        JsonPointer jsonPointer1 = new JsonPointer("/foo/bar/1");
        JsonPointer jsonPointer2 = new JsonPointer("/foo/bar/1");
        assertTrue(jsonPointer1.equals(jsonPointer2));
    }

    @Test
    public void testEqualsForDifferentObjects() {
        JsonPointer jsonPointer1 = new JsonPointer("/foo/bar/1");
        JsonPointer jsonPointer2 = new JsonPointer("/foo/bar/2");
        assertFalse(jsonPointer1.equals(jsonPointer2));
    }

    @Test
    public void testEqualsForNullObject() {
        JsonPointer jsonPointer = new JsonPointer("/foo/bar/1");
        assertFalse(jsonPointer.equals(null));
    }

    @Test
    public void testEqualsForNullJsonPointer() {
        JsonPointer jsonPointer = null;
        assertFalse(jsonPointer.equals(new JsonPointer("/foo/bar/1")));
    }

    @Test
    public void testEqualsForEqualJsonPointerStrings() {
        JsonPointer jsonPointer1 = new JsonPointer("/foo/bar/1");
        JsonPointer jsonPointer2 = new JsonPointer("/foo/bar/1");
        assertTrue(jsonPointer1.equals(jsonPointer2));
    }

    @Test
    public void testEqualsForDifferentJsonPointerStrings() {
        JsonPointer jsonPointer1 = new JsonPointer("/foo/bar/1");
        JsonPointer jsonPointer2 = new JsonPointer("/foo/baz/1");
        assertFalse(jsonPointer1.equals(jsonPointer2));
    }
}