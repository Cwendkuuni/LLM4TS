package com.fasterxml.jackson.databind.type;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;

public class TypeParserTest {

    private TypeFactory createTestFactory() {
        // Use a dummy TypeFactory for testing
        return new TypeFactory() {
            @Override
            public JavaType _fromClass(Class<?> rawClass, Class<?> resolvedClass, TypeBindings bindings) {
                return new SimpleImmutableJavaType(resolvedClass, bindings);
            }
        };
    }

    @Test
    public void testParseSimpleType() {
        TypeParser parser = new TypeParser(createTestFactory());
        assertEquals(String.class, parser.parse("String").getRawClass());
    }

    @Test
    public void testParseArrayType() {
        TypeParser parser = new TypeParser(createTestFactory());
        JavaType intArrayType = parser.parse("int[]");
        assertEquals(int[].class, intArrayType.getRawClass());
    }

    @Test
    public void testParseArrayTypeWithElementType() {
        TypeParser parser = new TypeParser(createTestFactory());
        JavaType listOfIntType = parser.parse("List<int>");
        assertEquals(java.util.List.class, listOfIntType.getRawClass());
        assertEquals(int.class, listOfIntType.getContentType().getRawClass());
    }

    @Test
    public void testParseListTypeWithGenericArguments() {
        TypeParser parser = new TypeParser(createTestFactory());
        JavaType listOfIntAndStringType = parser.parse("List<Integer, String>");
        assertEquals(java.util.List.class, listOfIntAndStringType.getRawClass());
        JavaType intType = listOfIntAndStringType.getContentType().elementType();
        assertEquals(Integer.class, intType.getRawClass());
        JavaType stringType = listOfIntAndStringType.getTypeBindings().get(1).getRawType();
        assertEquals(String.class, stringType.getRawClass());
    }

    @Test
    public void testParseMapType() {
        TypeParser parser = new TypeParser(createTestFactory());
        JavaType mapType = parser.parse("Map<String, Integer>");
        assertEquals(java.util.Map.class, mapType.getRawClass());
        JavaType keyType = mapType.getTypeBindings().get(0).getRawType();
        assertEquals(String.class, keyType.getRawClass());
        JavaType valueType = mapType.getTypeBindings().get(1).getRawType();
        assertEquals(Integer.class, valueType.getRawClass());
    }

    @Test
    public void testParseComplexTypeWithGenerics() {
        TypeParser parser = new TypeParser(createTestFactory());
        JavaType complexType = parser.parse("java.util.List<java.util.Map<java.lang.String, java.lang.Integer>>");
        assertEquals(java.util.List.class, complexType.getRawClass());
        JavaType listType = complexType.getTypeBindings().get(0);
        assertEquals(java.util.Map.class, listType.getRawClass());
        JavaType mapType = listType.getTypeBindings().get(0);
        assertEquals(String.class, mapType.getTypeBindings().get(0).getRawType().getRawClass());
        assertEquals(Integer.class, mapType.getTypeBindings().get(1).getRawType().getRawClass());
    }

    @Test
    public void testParseInvalidType() {
        TypeParser parser = new TypeParser(createTestFactory());
        try {
            parser.parse("InvalidType"); // Should throw an IllegalArgumentException
            fail("Expected an IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseMissingClosingBracket() {
        TypeParser parser = new TypeParser(createTestFactory());
        parser.parse("java.util.List<java.lang.String<"); // Should throw an IllegalArgumentException
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseMissingComma() {
        TypeParser parser = new TypeParser(createTestFactory());
        try {
            parser.parse("java.util.List<java.lang.String java.lang.Integer>"); // Should throw an IllegalArgumentException
            fail("Expected an IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseMissingEndParenthesis() {
        TypeParser parser = new TypeParser(createTestFactory());
        try {
            parser.parse("java.util.List<java.lang.String(java.lang.Integer)>"); // Should throw an IllegalArgumentException
            fail("Expected an IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testParseEmptyType() {
        TypeParser parser = new TypeParser(createTestFactory());
        JavaType emptyType = parser.parse("");
        assertNull(emptyType);
    }

    @Test
    public void testParseNullType() {
        TypeParser parser = new TypeParser(createTestFactory());
        try {
            parser.parse(null); // Should throw an IllegalArgumentException
            fail("Expected an IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }
}