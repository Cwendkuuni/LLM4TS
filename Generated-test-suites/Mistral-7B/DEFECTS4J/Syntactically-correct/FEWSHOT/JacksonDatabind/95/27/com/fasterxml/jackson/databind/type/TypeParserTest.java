package com.fasterxml.jackson.databind.type;

import static org.junit.Assert.*;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

public class TypeParserTest {

    TypeFactory typeFactory = new TypeFactoryBuilder().build();
    TypeParser typeParser = new TypeParser(typeFactory);

    @Test
    public void testParseEmptyString() {
        try {
            JavaType javaType = typeParser.parse("");
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertEquals("Failed to parse type '' (remaining: ''): Unexpected end-of-string", e.getMessage());
        }
    }

    @Test
    public void testParseSimpleClass() {
        JavaType javaType = typeParser.parse("String");
        assertNotNull(javaType);
        assertEquals(String.class, javaType.getRawClass());
    }

    @Test
    public void testParseClassWithGenerics() {
        JavaType javaType = typeParser.parse("List<Integer>");
        assertNotNull(javaType);
        assertEquals(List.class, javaType.getRawClass());
        List<?> listType = javaType.getType();
        assertNotNull(listType.getGenericType());
        assertEquals(Integer.class, listType.getGenericType());
    }

    @Test
    public void testParseClassWithMultipeGenerics() {
        JavaType javaType = typeParser.parse("Map<String, List<Integer>>");
        assertNotNull(javaType);
        assertEquals(Map.class, javaType.getRawClass());
        Map<?> mapType = javaType.getType();
        assertNotNull(mapType.getGenericType());
        assertEquals(String.class, mapType.getGenericType().get(0));
        List<?> listType = (List<?>) mapType.getGenericType().get(1);
        assertNotNull(listType.getGenericType());
        assertEquals(Integer.class, listType.getGenericType());
    }

    @Test
    public void testParseInvalidType() {
        try {
            JavaType javaType = typeParser.parse("List<String>abcd");
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertEquals("Failed to parse type 'List<String>abcd' (remaining: 'abcd'): Unexpected token 'a'", e.getMessage());
        }
    }

    @Test
    public void testParseInvalidClass() {
        try {
            JavaType javaType = typeParser.parse("ArrayType<String>abcd");
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertEquals("Can not locate class 'ArrayType<String>abcd', problem: ArrayType is not a valid class name", e.getMessage());
        }
    }

    @Test
    public void testParseInvalidClassWithSpaces() {
        try {
            JavaType javaType = typeParser.parse("Array Type<String>abcd");
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertEquals("Can not locate class 'Array Type<String>abcd', problem: Array Type is not a valid class name", e.getMessage());
        }
    }

    @Test
    public void testParseEmptyList() {
        JavaType javaType = typeParser.parse("List<>");
        assertNotNull(javaType);
        List<?> listType = javaType.getType();
        assertEquals(ArrayList.class, listType.getRawType());
        assertEquals(0, listType.size());
    }

    @Test
    public void testParseListWithOneElement() {
        JavaType javaType = typeParser.parse("List<Integer>");
        assertNotNull(javaType);
        List<?> listType = javaType.getType();
        assertEquals(ArrayList.class, listType.getRawType());
        List<Integer> list = (List<Integer>) listType;
        assertEquals(1, list.size());
        assertEquals(Integer.class, list.get(0).getClass());
    }

    @Test
    public void testParseListWithMultipleElements() {
        JavaType javaType = typeParser.parse("List<Integer> intArray");
        assertNotNull(javaType);
        List<?> listType = javaType.getType();
        assertEquals(ArrayList.class, listType.getRawType());
        List<Integer> list = (List<Integer>) listType;
        assertEquals(2, list.size());
        assertEquals(Integer.class, list.get(0).getClass());
        assertEquals(Integer[].class, list.get(1).getClass());
    }
}