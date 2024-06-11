package com.fasterxml.jackson.databind.type;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class TypeParserTest {

    private TypeParser typeParser;

    @Before
    public void setUp() {
        typeParser = new TypeParser(new TypeFactory());
    }

    @After
    public void tearDown() {
        typeParser = null;
    }

    @Test
    public void testParse_ValidCanonicalType_ReturnsJavaType() {
        // Arrange
        String canonicalType = "java.util.List<java.lang.String>";

        // Act
        JavaType result = typeParser.parse(canonicalType);

        // Assert
        assertNotNull(result);
        assertEquals("java.util.List", result.getRawClass().getName());
        assertEquals("java.lang.String", result.getContentType().getRawClass().getName());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParse_InvalidCanonicalType_ThrowsIllegalArgumentException() {
        // Arrange
        String invalidCanonicalType = "java.util.List<java.lang.String";

        // Act
        typeParser.parse(invalidCanonicalType);

        // Assert
        // Exception thrown
    }

    @Test
    public void testParse_EmptyCanonicalType_ThrowsIllegalArgumentException() {
        // Arrange
        String emptyCanonicalType = "";

        // Act
        try {
            typeParser.parse(emptyCanonicalType);
            // Assert
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Assert
            assertEquals("Failed to parse type '', remaining: '': Unexpected end-of-string", e.getMessage());
        }
    }

    @Test
    public void testParse_UnexpectedTokensAfterCompleteType_ThrowsIllegalArgumentException() {
        // Arrange
        String canonicalType = "java.util.List<java.lang.String> extraTokens";

        // Act
        try {
            typeParser.parse(canonicalType);
            // Assert
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Assert
            assertEquals("Failed to parse type 'java.util.List<java.lang.String> extraTokens' (remaining: ' extraTokens'): Unexpected tokens after complete type", e.getMessage());
        }
    }

    @Test
    public void testParseTypes_ValidCanonicalTypes_ReturnsListOfJavaTypes() {
        // Arrange
        String canonicalTypes = "java.util.List<java.lang.String>,java.util.Map<java.lang.Integer,java.lang.String>";

        // Act
        List<JavaType> result = typeParser.parseTypes(new TypeParser.MyTokenizer(canonicalTypes));

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("java.util.List", result.get(0).getRawClass().getName());
        assertEquals("java.lang.String", result.get(0).getContentType().getRawClass().getName());
        assertEquals("java.util.Map", result.get(1).getRawClass().getName());
        assertEquals("java.lang.Integer", result.get(1).getKeyType().getRawClass().getName());
        assertEquals("java.lang.String", result.get(1).getContentType().getRawClass().getName());
    }

    @Test
    public void testParseTypes_UnexpectedToken_ThrowsIllegalArgumentException() {
        // Arrange
        String canonicalTypes = "java.util.List<java.lang.String>,";

        // Act
        try {
            typeParser.parseTypes(new TypeParser.MyTokenizer(canonicalTypes));
            // Assert
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Assert
            assertEquals("Failed to parse type 'java.util.List<java.lang.String>,' (remaining: ','): Unexpected token ',', expected ',' or '>')", e.getMessage());
        }
    }

    @Test
    public void testParseTypes_UnexpectedEndOfString_ThrowsIllegalArgumentException() {
        // Arrange
        String canonicalTypes = "java.util.List<java.lang.String>,java.util.Map";

        // Act
        try {
            typeParser.parseTypes(new TypeParser.MyTokenizer(canonicalTypes));
            // Assert
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Assert
            assertEquals("Failed to parse type 'java.util.List<java.lang.String>,java.util.Map' (remaining: ''): Unexpected end-of-string", e.getMessage());
        }
    }

    @Test
    public void testFindClass_ValidClassName_ReturnsClass() {
        // Arrange
        String className = "java.util.List";

        // Act
        Class<?> result = typeParser.findClass(className, new TypeParser.MyTokenizer(""));

        // Assert
        assertNotNull(result);
        assertEquals("java.util.List", result.getName());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFindClass_InvalidClassName_ThrowsIllegalArgumentException() {
        // Arrange
        String invalidClassName = "invalid.class.name";

        // Act
        typeParser.findClass(invalidClassName, new TypeParser.MyTokenizer(""));

        // Assert
        // Exception thrown
    }

}