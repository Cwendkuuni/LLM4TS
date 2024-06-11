package com.fasterxml.jackson.databind.type;

import static org.junit.Assert.*;
import org.junit.Test;

public class TypeParserTest {

    @Test
    public void testParse_NullInput_ShouldThrowIllegalArgumentException() {
        TypeParser parser = new TypeParser(new TypeFactory());
        try {
            parser.parse(null);
            fail("Expected IllegalArgumentException was not thrown");
        } catch (IllegalArgumentException e) {
            assertEquals("Failed to parse type 'null' (remaining: ''): Unexpected end-of-string", e.getMessage());
        }
    }

    @Test
    public void testParse_EmptyInput_ShouldThrowIllegalArgumentException() {
        TypeParser parser = new TypeParser(new TypeFactory());
        try {
            parser.parse("");
            fail("Expected IllegalArgumentException was not thrown");
        } catch (IllegalArgumentException e) {
            assertEquals("Failed to parse type '' (remaining: ''): Unexpected end-of-string", e.getMessage());
        }
    }

    @Test
    public void testParse_InvalidInput_ShouldThrowIllegalArgumentException() {
        TypeParser parser = new TypeParser(new TypeFactory());
        try {
            parser.parse("class");
            fail("Expected IllegalArgumentException was not thrown");
        } catch (IllegalArgumentException e) {
            assertEquals("Failed to parse type 'class' (remaining: 'class'): Can not locate class 'class', problem: class", e.getMessage());
        }
    }

    @Test
    public void testParse_SingleNonGenericType_ShouldReturnJavaType() {
        TypeParser parser = new TypeParser(new TypeFactory());
        JavaType result = parser.parse("java.lang.String");
        assertNotNull(result);
        assertEquals("class java.lang.String", result.toString());
    }

    @Test
    public void testParse_GenericType_ShouldReturnJavaType() {
        TypeParser parser = new TypeParser(new TypeFactory());
        JavaType result = parser.parse("java.util.List<java.lang.String>");
        assertNotNull(result);
        assertEquals("java.util.List<java.lang.String>", result.toString());
    }

    @Test
    public void testParse_GenericTypeWithMultipleParameters_ShouldReturnJavaType() {
        TypeParser parser = new TypeParser(new TypeFactory());
        JavaType result = parser.parse("java.util.Map<java.lang.String, java.lang.Integer>");
        assertNotNull(result);
        assertEquals("java.util.Map<java.lang.String, java.lang.Integer>", result.toString());
    }

    @Test
    public void testParse_TrailingGarbageAfterCompleteType_ShouldThrowIllegalArgumentException() {
        TypeParser parser = new TypeParser(new TypeFactory());
        try {
            parser.parse("java.lang.String garbage");
            fail("Expected IllegalArgumentException was not thrown");
        } catch (IllegalArgumentException e) {
            assertEquals("Failed to parse type 'java.lang.String garbage' (remaining: 'garbage'): Unexpected tokens after complete type", e.getMessage());
        }
    }

    @Test
    public void testParse_InvalidGenericType_ShouldThrowIllegalArgumentException() {
        TypeParser parser = new TypeParser(new TypeFactory());
        try {
            parser.parse("java.util.List<java.lang.String");
            fail("Expected IllegalArgumentException was not thrown");
        } catch (IllegalArgumentException e) {
            assertEquals("Failed to parse type 'java.util.List<java.lang.String' (remaining: '<java.lang.String'): Unexpected end-of-string", e.getMessage());
        }
    }

    @Test
    public void testParse_InvalidGenericTypeWithTrailingGarbage_ShouldThrowIllegalArgumentException() {
        TypeParser parser = new TypeParser(new TypeFactory());
        try {
            parser.parse("java.util.List<java.lang.String garbage");
            fail("Expected IllegalArgumentException was not thrown");
        } catch (IllegalArgumentException e) {
            assertEquals("Failed to parse type 'java.util.List<java.lang.String garbage' (remaining: '<java.lang.String garbage'): Unexpected end-of-string", e.getMessage());
        }
    }

    @Test
    public void testParse_GenericsSeparatedByComma_ShouldReturnJavaType() {
        TypeParser parser = new TypeParser(new TypeFactory());
        JavaType result = parser.parse("java.util.Map<java.lang.String, java.lang.Integer, java.util.List<java.lang.Character>>");
        assertNotNull(result);
        assertEquals("java.util.Map<java.lang.String, java.lang.Integer, java.util.List<java.lang.Character>>", result.toString());
    }
}