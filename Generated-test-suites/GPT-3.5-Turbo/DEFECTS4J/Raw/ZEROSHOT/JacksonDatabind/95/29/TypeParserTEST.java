package com.fasterxml.jackson.databind.type;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

public class TypeParserTest {

    @Test
    public void testParse() {
        // Test case 1: Empty string input
        TypeParser parser = new TypeParser(new TypeFactory());
        try {
            parser.parse("");
            fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException e) {
            assertEquals("Failed to parse type '' (remaining: ''): Unexpected end-of-string", e.getMessage());
        }

        // Test case 2: Valid input
        try {
            JavaType type = parser.parse("java.util.List<java.lang.String>");
            assertTrue(type instanceof ParameterizedType);
            ParameterizedType parameterizedType = (ParameterizedType) type;
            assertEquals(ArrayList.class, parameterizedType.getRawType());
            assertEquals(String.class, parameterizedType.getActualTypeArguments()[0]);
        } catch (IllegalArgumentException e) {
            fail("Unexpected IllegalArgumentException");
        }
    }

    @Test
    public void testParseType() {
        // Test case 1: Empty string input
        TypeParser parser = new TypeParser(new TypeFactory());
        MyTokenizer tokens = new MyTokenizer("");
        try {
            parser.parseType(tokens);
            fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException e) {
            assertEquals("Failed to parse type '' (remaining: ''): Unexpected end-of-string", e.getMessage());
        }

        // Test case 2: Valid input with generics
        tokens = new MyTokenizer("java.util.List<java.lang.String>");
        try {
            JavaType type = parser.parseType(tokens);
            assertTrue(type instanceof ParameterizedType);
            ParameterizedType parameterizedType = (ParameterizedType) type;
            assertEquals(ArrayList.class, parameterizedType.getRawType());
            assertEquals(String.class, parameterizedType.getActualTypeArguments()[0]);
        } catch (IllegalArgumentException e) {
            fail("Unexpected IllegalArgumentException");
        }

        // Test case 3: Valid input without generics
        tokens = new MyTokenizer("java.lang.String");
        try {
            JavaType type = parser.parseType(tokens);
            assertEquals(String.class, type.getRawType());
        } catch (IllegalArgumentException e) {
            fail("Unexpected IllegalArgumentException");
        }
    }

    @Test
    public void testParseTypes() {
        // Test case 1: Empty string input
        TypeParser parser = new TypeParser(new TypeFactory());
        MyTokenizer tokens = new MyTokenizer("");
        try {
            parser.parseTypes(tokens);
            fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException e) {
            assertEquals("Failed to parse type '' (remaining: ''): Unexpected end-of-string", e.getMessage());
        }

        // Test case 2: Valid input with multiple types
        tokens = new MyTokenizer("java.lang.String,java.lang.Integer");
        try {
            List<JavaType> types = parser.parseTypes(tokens);
            assertEquals(2, types.size());
            assertEquals(String.class, types.get(0).getRawType());
            assertEquals(Integer.class, types.get(1).getRawType());
        } catch (IllegalArgumentException e) {
            fail("Unexpected IllegalArgumentException");
        }

        // Test case 3: Valid input with single type
        tokens = new MyTokenizer("java.lang.String");
        try {
            List<JavaType> types = parser.parseTypes(tokens);
            assertEquals(1, types.size());
            assertEquals(String.class, types.get(0).getRawType());
        } catch (IllegalArgumentException e) {
            fail("Unexpected IllegalArgumentException");
        }
    }

    @Test
    public void testFindClass() {
        // Test case 1: Class not found
        TypeParser parser = new TypeParser(new TypeFactory());
        MyTokenizer tokens = new MyTokenizer("com.example.NonExistentClass");
        try {
            parser.findClass("com.example.NonExistentClass", tokens);
            fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException e) {
            assertEquals("Can not locate class 'com.example.NonExistentClass', problem: null", e.getMessage());
        }

        // Test case 2: Class found
        tokens = new MyTokenizer("java.lang.String");
        try {
            Class<?> clazz = parser.findClass("java.lang.String", tokens);
            assertEquals(String.class, clazz);
        } catch (IllegalArgumentException e) {
            fail("Unexpected IllegalArgumentException");
        }
    }

    @Test
    public void testProblem() {
        // Test case 1: Exception with empty input
        TypeParser parser = new TypeParser(new TypeFactory());
        MyTokenizer tokens = new MyTokenizer("");
        IllegalArgumentException exception = parser._problem(tokens, "Unexpected end-of-string");
        assertEquals("Failed to parse type '' (remaining: ''): Unexpected end-of-string", exception.getMessage());

        // Test case 2: Exception with non-empty input
        tokens = new MyTokenizer("java.lang.String");
        exception = parser._problem(tokens, "Unexpected token 'java.lang.String', expected ',' or '>')");
        assertEquals("Failed to parse type 'java.lang.String' (remaining: ''): Unexpected token 'java.lang.String', expected ',' or '>')", exception.getMessage());
    }
}