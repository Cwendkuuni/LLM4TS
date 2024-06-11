package com.fasterxml.jackson.databind.type;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.databind.JavaType;

public class TypeParserTest {
    
    private TypeParser typeParser;

    @Before
    public void setUp() {
        // Instantiate the TypeParser with a mock TypeFactory for testing
        typeParser = new TypeParser(new MockTypeFactory());
    }

    @Test
    public void testParse_WithValidInput_ReturnsValidJavaType() {
        // Arrange
        String canonical = "java.util.List<java.lang.String>";

        // Act
        JavaType result = typeParser.parse(canonical);

        // Assert
        assertNotNull(result);
        assertEquals(List.class, result.getRawClass());
        assertEquals(String.class, result.getContentType().getRawClass());
    }

    @Test
    public void testParse_WithEmptyInput_ThrowsIllegalArgumentException() {
        // Arrange
        String canonical = "";

        // Act
        try {
            typeParser.parse(canonical);
            fail("Expected IllegalArgumentException was not thrown.");
        } catch (IllegalArgumentException e) {
            // Assert
            assertEquals("Failed to parse type '' (remaining: ''): Unexpected end-of-string", e.getMessage());
        }
    }

    @Test
    public void testParse_WithInvalidClass_ThrowsIllegalArgumentException() {
        // Arrange
        String canonical = "invalid.ClassName";

        // Act
        try {
            typeParser.parse(canonical);
            fail("Expected IllegalArgumentException was not thrown.");
        } catch (IllegalArgumentException e) {
            // Assert
            assertEquals("Failed to parse type 'invalid.ClassName' (remaining: 'invalid.ClassName'): Can not locate class 'invalid.ClassName', problem: null", e.getMessage());
        }
    }

    @Test
    public void testParse_WithUnexpectedTokens_ThrowsIllegalArgumentException() {
        // Arrange
        String canonical = "java.util.List<java.lang.String> unexpected";

        // Act
        try {
            typeParser.parse(canonical);
            fail("Expected IllegalArgumentException was not thrown.");
        } catch (IllegalArgumentException e) {
            // Assert
            assertEquals("Failed to parse type 'java.util.List<java.lang.String> unexpected' (remaining: 'unexpected'): Unexpected tokens after complete type", e.getMessage());
        }
    }

    @Test
    public void testParse_WithGenericTypeAndCommaMissingBetweenTypes_ThrowsIllegalArgumentException() {
        // Arrange
        String canonical = "java.util.Map<java.lang.String java.lang.Integer>";

        // Act
        try {
            typeParser.parse(canonical);
            fail("Expected IllegalArgumentException was not thrown.");
        } catch (IllegalArgumentException e) {
            // Assert
            assertEquals("Failed to parse type 'java.util.Map<java.lang.String java.lang.Integer>' (remaining: '<java.lang.String java.lang.Integer>'): Unexpected token 'java.lang.String java.lang.Integer>', expected ',' or '>')", e.getMessage());
        }
    }

    @Test
    public void testParse_WithInvalidGenericTypes_ThrowsIllegalArgumentException() {
        // Arrange
        String canonical = "java.util.List<invalid.ClassName>";

        // Act
        try {
            typeParser.parse(canonical);
            fail("Expected IllegalArgumentException was not thrown.");
        } catch (IllegalArgumentException e) {
            // Assert
            assertEquals("Failed to parse type 'java.util.List<invalid.ClassName>' (remaining: '<invalid.ClassName>'): Can not locate class 'invalid.ClassName', problem: null", e.getMessage());
        }
    }

    // Mock TypeFactory for testing purposes
    private class MockTypeFactory extends TypeFactory {
        @Override
        public Class<?> findClass(String className) throws ClassNotFoundException {
            if (className.equals("java.util.List")) {
                return List.class;
            } else if (className.equals("java.lang.String")) {
                return String.class;
            } else if (className.equals("java.util.Map")) {
                return List.class;
            } else if (className.equals("java.lang.Integer")) {
                return Integer.class;
            } else {
                throw new ClassNotFoundException();
            }
        }

        // Mock implementation of _fromClass and create methods
        // Not necessary for these tests
    }

}