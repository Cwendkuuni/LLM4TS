package com.fasterxml.jackson.databind.type;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

public class TypeParserTest {

    private TypeFactory _factory;
    private TypeParser _parser;

    @SuppressWarnings("unchecked")
    @Before
    public void setup() {
        _factory = new TypeFactory() {
            @Override
            public <T> JavaType _fromClass(String name, Class<?> clazz, TypeBindings bindings) {
                return (JavaType) (() -> clazz).getType();
            }
        };
        _parser = new TypeParser(_factory);
    }

    @Test
    public void testParse_simpleType() {
        JavaType type = _parser.parse("int");
        assertEquals(Integer.TYPE, type.getRawClass());
    }

    @Test
    public void testParse_fullyQualifiedType() {
        JavaType type = _parser.parse("java.util.List<java.lang.String>");
        assertEquals(List.class, type.getRawClass());
        assertEquals(String.class, type.getGenericTypes().get(0).getRawClass());
    }

    @Test
    public void testParse_typeWithArray() {
        JavaType type = _parser.parse("int[]");
        assertEquals(int[].class, type.getRawClass());
    }

    @Test
    public void testParse_typeWithGenericArray() {
        JavaType type = _parser.parse("java.util.List<int[]>");
        assertEquals(List.class, type.getRawClass());
        assertEquals(int[].class, type.getGenericTypes().get(0).getRawClass());
    }

    @Test
    public void testParse_typeWithMultipleGenerics() {
        JavaType type = _parser.parse("java.util.Map<java.lang.String, java.util.List<java.lang.Integer>>");
        assertEquals(Map.class, type.getRawClass());
        assertEquals(String.class, type.getGenericTypes().get(0).getRawClass());
        assertEquals(List.class, type.getGenericTypes().get(1).getRawClass());
        assertEquals(Integer.TYPE, type.getGenericTypes().get(1).getGenericTypes().get(0).getRawClass());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParse_invalidType() {
        _parser.parse("invalidType");
    }

    @Test
    public void testParse_typeWithMissingClosingBracket() {
        _parser.parse("java.util.Map<java.lang.String<java.lang.Integer>");
        fail("Expected IllegalArgumentException");
    }

    @Test
    public void testParse_typeWithMissingOpeningBracket() {
        _parser.parse("java.util.Map<java.lang.String java.lang.Integer>");
        fail("Expected IllegalArgumentException");
    }

    @Test
    public void testParse_typeWithExtraTokens() {
        _parser.parse("java.util.Map<java.lang.String java.lang.Integer> foo");
        fail("Expected IllegalArgumentException");
    }

    @Test
    public void testParse_typeWithRecursiveType() {
        JavaType listType = _parser.parse("java.util.List<MyClass>");
        JavaType myClassType = _parser.parse("MyClass");
        assertEquals(List.class, listType.getRawClass());
        assertEquals(myClassType, listType.getGenericTypes().get(0));
        assertEquals(myClassType, myClassType.getRawClass());
    }

    @Test
    public void testParse_typeWithNestedGenerics() {
        JavaType listType = _parser.parse("java.util.List<java.util.List<java.lang.Integer>>");
        JavaType innerListType = listType.getGenericTypes().get(0);
        assertEquals(List.class, listType.getRawClass());
        assertEquals(List.class, innerListType.getRawClass());
        JavaType integerType = innerListType.getGenericTypes().get(0);
        assertEquals(Integer.TYPE, integerType.getRawClass());
    }
}