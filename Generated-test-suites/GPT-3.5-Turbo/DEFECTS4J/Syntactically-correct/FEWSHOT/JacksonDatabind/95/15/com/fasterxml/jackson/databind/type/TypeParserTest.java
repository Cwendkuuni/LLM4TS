package com.fasterxml.jackson.databind.type;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import com.fasterxml.jackson.databind.JavaType;
import org.junit.BeforeClass;
import org.junit.Test;

public class TypeParserTest {
    private static TypeParser typeParser;

    @BeforeClass
    public static void setUp() {
        typeParser = new TypeParser(new TypeFactory());
    }

    @Test
    public void testParse() {
        // Test case 1: Empty input
        try {
            typeParser.parse("");
        } catch (IllegalArgumentException e) {
            assertEquals("Failed to parse type '' (remaining: ''): Unexpected end-of-string", e.getMessage());
        }

        // Test case 2: Non-generic type
        JavaType javaType = typeParser.parse("java.lang.String");
        assertNotNull(javaType);

        // Test case 3: Generic type
        javaType = typeParser.parse("java.util.List<java.lang.String>");
        assertNotNull(javaType);

        // Test case 4: Generic type with multiple parameters
        javaType = typeParser.parse("java.util.Map<java.lang.String, java.lang.Integer>");
        assertNotNull(javaType);

        // Test case 5: Unexpected tokens after complete type
        try {
            typeParser.parse("java.lang.String unexpected");
        } catch (IllegalArgumentException e) {
            assertEquals("Failed to parse type 'java.lang.String unexpected' (remaining: ' unexpected'): Unexpected tokens after complete type", e.getMessage());
        }
    }

    @Test
    public void testParseType() {
        // Test case 1: Unexpected end-of-string
        MyTokenizer tokens = new TypeParser.MyTokenizer("");
        try {
            typeParser.parseType(tokens);
        } catch (IllegalArgumentException e) {
            assertEquals("Failed to parse type '' (remaining: ''): Unexpected end-of-string", e.getMessage());
        }

        // Test case 2: Non-generic type
        tokens = new TypeParser.MyTokenizer("java.lang.String");
        JavaType javaType = typeParser.parseType(tokens);
        assertNotNull(javaType);

        // Test case 3: Generic type with parameter
        tokens = new TypeParser.MyTokenizer("java.util.List<java.lang.String>");
        javaType = typeParser.parseType(tokens);
        assertNotNull(javaType);

        // Test case 4: Generic type without parameters
        tokens = new TypeParser.MyTokenizer("java.util.List<>");
        javaType = typeParser.parseType(tokens);
        assertNotNull(javaType);

        // Test case 5: Unexpected token
        tokens = new TypeParser.MyTokenizer("java.lang.String unexpected");
        try {
            typeParser.parseType(tokens);
        } catch (IllegalArgumentException e) {
            assertEquals("Failed to parse type 'java.lang.String unexpected' (remaining: ' unexpected'): Unexpected token 'unexpected', expected ',' or '>')", e.getMessage());
        }
    }

    @Test
    public void testParseTypes() {
        // Test case 1: Unexpected end-of-string
        MyTokenizer tokens = new TypeParser.MyTokenizer("");
        try {
            typeParser.parseTypes(tokens);
        } catch (IllegalArgumentException e) {
            assertEquals("Failed to parse type '' (remaining: ''): Unexpected end-of-string", e.getMessage());
        }

        // Test case 2: Single type in generic type
        tokens = new TypeParser.MyTokenizer("java.lang.String");
        JavaType javaType = typeParser.parseTypes(tokens);
        assertNotNull(javaType);

        // Test case 3: Multiple types in generic type
        tokens = new TypeParser.MyTokenizer("java.lang.String, java.lang.Integer");
        javaType = typeParser.parseTypes(tokens);
        assertNotNull(javaType);

        // Test case 4: Generic type without parameters
        tokens = new TypeParser.MyTokenizer("<>");
        try {
            typeParser.parseTypes(tokens);
        } catch (IllegalArgumentException e) {
            assertEquals("Failed to parse type '<>' (remaining: ''): Unexpected end-of-string", e.getMessage());
        }

        // Test case 5: Unexpected token
        tokens = new TypeParser.MyTokenizer("java.lang.String unexpected");
        try {
            typeParser.parseTypes(tokens);
        } catch (IllegalArgumentException e) {
            assertEquals("Failed to parse type 'java.lang.String unexpected' (remaining: ' unexpected'): Unexpected token 'unexpected', expected ',' or '>')", e.getMessage());
        }
    }

    @Test
    public void testFindClass() {
        // Test case 1: Class not found
        MyTokenizer tokens = new TypeParser.MyTokenizer("unknownClass");
        try {
            typeParser.findClass("unknownClass", tokens);
        } catch (IllegalArgumentException e) {
            assertEquals("Failed to parse type 'unknownClass' (remaining: ''): Can not locate class 'unknownClass', problem: null", e.getMessage());
        }

        // Test case 2: Class found
        Class<?> clazz = typeParser.findClass("java.lang.String", null);
        assertNotNull(clazz);
    }

    @Test
    public void testProblem() {
        MyTokenizer tokens = new TypeParser.MyTokenizer("java.lang.String");
        IllegalArgumentException exception = typeParser._problem(tokens, "Unexpected end-of-string");
        assertEquals("Failed to parse type 'java.lang.String' (remaining: ''): Unexpected end-of-string", exception.getMessage());
    }
}