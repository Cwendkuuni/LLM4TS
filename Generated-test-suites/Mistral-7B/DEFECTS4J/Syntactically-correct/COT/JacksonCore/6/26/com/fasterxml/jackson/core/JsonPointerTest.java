package com.fasterxml.jackson.core; // package imports for testing

import org.junit.Test;
import org.junit.Assert.*;

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
    public void testConstructorWithSegments() {
        String fullString = "/test/property1/2";
        String segment = "property1";
        JsonPointer nextJsonPointer = new JsonPointer("/test/3");
        JsonPointer jsonPointer = new JsonPointer(fullString, segment, nextJsonPointer);
        assertFalse(jsonPointer.matches());
        assertEquals(segment, jsonPointer.getMatchingProperty());
        assertEquals(0, jsonPointer.getMatchingIndex());
        assertEquals(fullString, jsonPointer.toString());
        assertEquals(nextJsonPointer, jsonPointer.tail());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidInputForConstructors() {
        String input = "test";
        JsonPointer.compile(input);
    }

    @Test
    public void testFactoryMethods() {
        String input = "/test/property1/2";
        JsonPointer jsonPointer = JsonPointer.compile(input);
        JsonPointer jsonPointer1 = JsonPointer.valueOf(input);
        assertEquals(jsonPointer, jsonPointer1);

        //TODO: Implement fromSegment factory method test case
    }

    @Test
    public void testGetters() {
        String fullString = "/test/property1/2";
        String segment = "property1";
        JsonPointer nextJsonPointer = new JsonPointer("/test/3");
        JsonPointer jsonPointer = new JsonPointer(fullString, segment, nextJsonPointer);
        assertTrue(jsonPointer.matches() ^ nextJsonPointer.matches());
        assertEquals(segment, jsonPointer.getMatchingProperty());
        assertEquals(0, jsonPointer.getMatchingIndex());
        assertFalse(jsonPointer.mayMatchProperty() ^ jsonPointer.mayMatchElement());
        JsonPointer propertyJsonPointer = jsonPointer.matchProperty(segment);
        assertEquals(jsonPointer, propertyJsonPointer);
        JsonPointer indexJsonPointer = jsonPointer.matchElement(0);
        assertNull(indexJsonPointer);
        indexJsonPointer = jsonPointer.matchElement(2);
        assertNull(indexJsonPointer);
        indexJsonPointer = jsonPointer.matchElement(2);
        assertEquals(jsonPointer, indexJsonPointer);
        assertEquals(jsonPointer.toString(), jsonPointer.tail().toString());
    }

    @Test
    public void testToString() {
        String fullString = "/test/property1/2";
        String segment = "property1";
        JsonPointer nextJsonPointer = new JsonPointer("/test/3");
        JsonPointer jsonPointer = new JsonPointer(fullString, segment, nextJsonPointer);
        assertEquals(fullString, jsonPointer.toString());
    }

    @Test
    public void testHashCodeAndEquals() {
        String fullString1 = "/test/property1/2";
        String segment1 = "property1";
        JsonPointer nextJsonPointer1 = new JsonPointer("/test/3");
        JsonPointer jsonPointer1 = new JsonPointer(fullString1, segment1, nextJsonPointer1);

        String fullString2 = "/test/property1/3";
        String segment2 = "property1";
        JsonPointer nextJsonPointer2 = new JsonPointer("/test/4");
        JsonPointer jsonPointer2 = new JsonPointer(fullString2, segment2, nextJsonPointer2);

        String fullString3 = "/test/property1/2";
        String segment3 = "property2";
        JsonPointer nextJsonPointer3 = new JsonPointer("/test/3");
        JsonPointer jsonPointer3 = new JsonPointer(fullString3, segment3, nextJsonPointer3);

        assertEquals(jsonPointer1.hashCode(), jsonPointer1.hashCode());
        assertEquals(jsonPointer1, jsonPointer1);
        assertEquals(jsonPointer1, jsonPointer1);
        assertNotEquals(jsonPointer1, jsonPointer2);
        assertNotEquals(jsonPointer1, jsonPointer3);
        assertNotEquals(jsonPointer1, new Object());
    }

    @Test
    public void testParseIndex() {
        String str1 = "";
        int index1 = JsonPointer._parseIndex(str1);
        assertEquals(-1, index1);

        String str2 = "/1";
        int index2 = JsonPointer._parseIndex(str2);
        assertEquals(0, index2);

        String str3 = "/10";
        int index3 = JsonPointer._parseIndex(str3);
        assertEquals(10, index3);

        String str4 = "/-1";
        int index4 = JsonPointer._parseIndex(str4);
        assertEquals(-1, index4);

        String str5 = "/1000000000";
        int index5 = JsonPointer._parseIndex(str5);
        assertEquals(1000000000, index5);

        String str6 = "/1a";
        int index6 = JsonPointer._parseIndex(str6);
        assertEquals(-1, index6);
    }

    @Test
    public void testParseTail() {
        String input1 = "/";
        JsonPointer jsonPointer1 = JsonPointer._parseTail(input1);
        assertTrue(jsonPointer1.matches());
        assertEquals("", jsonPointer1.toString());

        String input2 = "/test";
        JsonPointer jsonPointer2 = JsonPointer._parseTail(input2);
        assertFalse(jsonPointer2.matches());
        assertEquals("/test", jsonPointer2.toString());

        String input3 = "/test/property1";
        JsonPointer jsonPointer3 = JsonPointer._parseTail(input3);
        assertFalse(jsonPointer3.matches());
        assertEquals("/test/property1", jsonPointer3.toString());

        String input4 = "/test/property1/2";
        JsonPointer jsonPointer4 = JsonPointer._parseTail(input4);
        assertFalse(jsonPointer4.matches());
        assertEquals("/test/property1/2", jsonPointer4.toString());

        String input5 = "/test/property1/2/~1";
        JsonPointer jsonPointer5 = JsonPointer._parseTail(input5);
        assertFalse(jsonPointer5.matches());
        assertEquals("/test/property1/2/~1", jsonPointer5.toString());

        String input6 = "/test/property1/2/~1/~0";
        JsonPointer jsonPointer6 = JsonPointer._parseTail(input6);
        assertFalse(jsonPointer6.matches());
        assertEquals("/test/property1/2/~1/~0", jsonPointer6.toString());

        String input7 = "/test/property1/2/~1/~0/~2";
        JsonPointer jsonPointer7 = JsonPointer._parseTail(input7);
        assertFalse(jsonPointer7.matches());
        assertEquals("/test/property1/2/~1/~0/~2", jsonPointer7.toString());

        String input8 = "/test/property1/2/~1/~0/~2/";
        JsonPointer jsonPointer8 = JsonPointer._parseTail(input8);
        assertTrue(jsonPointer8.matches());
        assertEquals("/", jsonPointer8.toString());
    }
}