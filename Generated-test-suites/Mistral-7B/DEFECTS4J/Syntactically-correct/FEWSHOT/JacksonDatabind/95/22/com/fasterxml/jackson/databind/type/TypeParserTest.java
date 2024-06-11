package com.fasterxml.jackson.databind.type;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertInstanceOf;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertThrows;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

public class TypeParserTest {

    private TypeFactory _factory;

    @Before
    public void setup() {
        _factory = new TypeFactory() {
            @Override
            public JavaType _fromClass(JavaType primaryType, Class<?> clazz, TypeBindings typeBindings) {
                return new SimpleModule().addTypeHandler(clazz).handleType(primaryType);
            }
        };
    }

    @Test
    public void testParseNull() {
        assertThrows(IllegalArgumentException.class, () -> new TypeParser(_factory).parse(null));
    }

    @Test
    public void testParseEmptyString() {
        JavaType type = new TypeParser(_factory).parse("");
        assertNull(type);
    }

    @Test
    public void testParseClass() {
        JavaType type = new TypeParser(_factory).parse("String");
        assertEquals(String.class, type.getRawClass());
    }

    @Test
    public void testParseWrapperClass() {
        JavaType type = new TypeParser(_factory).parse("Integer");
        assertEquals(Integer.class, type.getRawClass());
        assertEquals(java.lang.Integer.class, type.getRawClass().getComponentType());
    }

    @Test
    public void testParseArray() {
        JavaType intType = new TypeParser(_factory).parse("int[]");
        assertEquals(int[].class, intType.getRawClass());
        JavaType intArrayType = new TypeParser(_factory).parse("int[][]");
        assertEquals(int[][].class, intArrayType.getRawClass());
    }

    @Test
    public void testParseGenericClass() {
        JavaType listType = new TypeParser(_factory).parse("java.util.List<java.lang.String>");
        assertEquals(java.util.List.class, listType.getRawClass());
        assertEquals(java.lang.String.class, listType.getType().getRawClass());
    }

    @Test
    public void testParseGenericClassWithWildcard() {
        JavaType listType = new TypeParser(_factory).parse("java.util.List<? extends java.lang.Number>");
        assertEquals(java.util.List.class, listType.getRawClass());
        Class<?> elementType = listType.getType().getRawClass();
        assertEquals(java.lang.Number.class, elementType.getSuperclass());
        assertSame(Number.class, elementType.getInterfaces()[0]);
    }

    @Test
    public void testParseGenericClassWithMultipleTypeArguments() {
        JavaType mapType = new TypeParser(_factory).parse("java.util.Map<java.lang.String, java.util.List<java.lang.Integer>>");
        assertEquals(java.util.Map.class, mapType.getRawClass());
        assertEquals(java.lang.String.class, mapType.getType().getKeyType().getRawClass());
        assertEquals(java.util.List.class, mapType.getType().getValueType().getRawClass());
        assertEquals(java.lang.Integer.class, mapType.getType().getValueType().getType().getRawClass());
    }

    @Test
    public void testParseInvalidType() {
        assertThrows(IllegalArgumentException.class, () -> new TypeParser(_factory).parse("java.util.List<java.lang.String>x"));
    }

    @Test
    public void testParseInvalidTypeWithMissingClosingBracket() {
        assertThrows(IllegalArgumentException.class, () -> new TypeParser(_factory).parse("java.util.List<java.lang.String>"));
    }

    @Test
    public void testParseInvalidTypeWithExtraToken() {
        assertThrows(IllegalArgumentException.class, () -> new TypeParser(_factory).parse("java.util.List<java.lang.String> x"));
    }

    @Test
    public void testParseInvalidTypeWithMalformedToken() {
        assertThrows(IllegalArgumentException.class, () -> new TypeParser(_factory).parse("java.util.List<java.lang.String> x<"));
    }

    @Test
    public void testParseInvalidTypeWithMissingComma() {
        assertThrows(IllegalArgumentException.class, () -> new TypeParser(_factory).parse("java.util.List<java.lang.String, java.util.List<java.lang.Integer>>"));
    }

    @Test
    public void testParseInvalidTypeWithExtraComma() {
        assertThrows(IllegalArgumentException.class, () -> new TypeParser(_factory).parse("java.util.List<java.lang.String, java.util.List<java.lang.Integer>, java.lang.String>"));
    }

    @Test
    public void testParseInvalidTypeWithInvalidTypeArgument() {
        assertThrows(IllegalArgumentException.class, () -> new TypeParser(_factory).parse("java.util.List<java.lang.String, java.lang.Integer>"));
    }

    @Test
    public void testParseInvalidTypeWithInvalidToken() {
        assertThrows(IllegalArgumentException.class, () -> new TypeParser(_factory).parse("java.util.List<java.lang.String> x<x>"));
    }

    @Test
    public void testParseTypes() {
        List<JavaType> types = new TypeParser(_factory).parseTypes("int, long, java.lang.String[]");
        assertEquals(3, types.size());
        assertEquals(int.class, types.get(0).getRawClass());
        assertEquals(long.class, types.get(1).getRawClass());
        assertEquals(String[].class, types.get(2).getRawClass());
    }

}