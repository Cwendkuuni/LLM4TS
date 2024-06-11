package com.fasterxml.jackson.databind.type;

import static org.junit.Assert.*;

import org.junit.*;

public class TypeParserTest {

    private TypeParser parser;

    @Before
    public void setup() {
        parser = new TypeParser(new TypeFactory());
    }

    @Test
    public void testParse_singleGenericType() {
        String canonical = "java.util.List<java.lang.String>";
        JavaType result = parser.parse(canonical);
        JavaType expected = new SimpleTypeFactory().constructCollectionType(List.class, String.class);
        assertEquals(expected, result);
    }

    @Test
    public void testParse_singleNonGenericType() {
        String canonical = "java.lang.Integer";
        JavaType result = parser.parse(canonical);
        JavaType expected = new SimpleTypeFactory().constructType(Integer.class);
        assertEquals(expected, result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParse_unexpectedTokensAfterCompleteType() {
        String canonical = "java.util.Map<java.lang.String, java.lang.Integer> extra";
        parser.parse(canonical);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParse_unexpectedEndOfString() {
        String canonical = "";
        parser.parse(canonical);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParse_unexpectedToken() {
        String canonical = "java.util.List<java.lang.String";
        parser.parse(canonical);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParse_unexpectedEndOfStringInParseTypes() {
        String canonical = "java.util.List<java.lang.String, java.lang.Integer>";
        JavaType result = parser.parse(canonical);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParse_unexpectedTokenInParseTypes() {
        String canonical = "java.util.List<java.lang.String>>";
        parser.parse(canonical);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParse_findClassException() {
        String canonical = "com.example.DoesNotExist";
        parser.parse(canonical);
    }
}