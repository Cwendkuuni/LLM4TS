package com.fasterxml.jackson.databind.type;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

public class TypeParserTest {

    private TypeFactory _factory;

    @Test
    public void testTypeParserInitialization() {
        _factory = new TypeFactory();
        TypeParser parser = new TypeParser(_factory);
        assertNotNull(parser);
    }

    @Test
    public void testParseEmptyString() {
        _factory = new TypeFactory();
        TypeParser parser = new TypeParser(_factory);
        assertNull(parser.parse(""));
    }

    @Test
    public void testParseSingleClass() {
        _factory = new TypeFactory();
        TypeParser parser = new TypeParser(_factory);
        assertEquals(_factory.constructType(String.class), parser.parse("String"));
    }

    @Test
    public void testParseSingleClassWithPackage() {
        _factory = new TypeFactory();
        TypeParser parser = new TypeParser(_factory);
        assertEquals(_factory.constructType(java.util.ArrayList.class), parser.parse("java.util.ArrayList"));
    }

    @Test
    public void testParseClassWithGenericArgument() {
        _factory = new TypeFactory();
        TypeParser parser = new TypeParser(_factory);
        List<JavaType> genericArguments = new ArrayList<>();
        genericArguments.add(parser.parse("Number"));
        TypeBindings typeBindings = new TypeBindings(java.util.List.class, genericArguments);
        assertEquals(_factory.constructType(List.class, JavaType.constructGeneric(List.class, genericArguments.toArray(new JavaType[0]))), parser.parse("java.util.List<Number>"));
    }

    @Test
    public void testParseClassWithMultipleGenericArguments() {
        _factory = new TypeFactory();
        TypeParser parser = new TypeParser(_factory);
        List<JavaType> genericArguments = new ArrayList<>();
        genericArguments.add(parser.parse("Number"));
        genericArguments.add(parser.parse("Boolean"));
        TypeBindings typeBindings = new TypeBindings(java.util.Map.class, genericArguments.toArray(new JavaType[0]));
        assertEquals(_factory.constructType(Map.class, JavaType.constructGeneric(Map.class, genericArguments.toArray(new JavaType[0]))), parser.parse("java.util.Map<Number, Boolean>"));
    }

    @Test
    public void testParseClassWithGenericArray() {
        _factory = new TypeFactory();
        TypeParser parser = new TypeParser(_factory);
        JavaType elementType = parser.parse("Number");
        TypeBindings typeBindings = new TypeBindings(java.lang.Object[].class, new JavaType[]{JavaType.constructArrayType(elementType)});
        assertEquals(_factory.constructType(Object[].class, new JavaType[]{elementType}), parser.parse("Number[]"));
    }

    @Test
    public void testParseMultipleTypesWithCommaSeparator() {
        _factory = new TypeFactory();
        TypeParser parser = new TypeParser(_factory);
        JavaType firstType = parser.parse("String");
        JavaType secondType = parser.parse("Integer");
        List<JavaType> types = new ArrayList<>();
        types.add(firstType);
        types.add(secondType);
        TypeBindings typeBindings = new TypeBindings(Object[].class, types.toArray(new JavaType[0]));
        assertEquals(_factory.constructType(Object[].class, new JavaType[]{firstType, secondType}), parser.parse("String, Integer"));
    }

    @Test
    public void testParseMultipleTypesWithTrailingComma() {
        _factory = new TypeFactory();
        TypeParser parser = new TypeParser(_factory);
        JavaType firstType = parser.parse("String");
        JavaType secondType = parser.parse("Integer");
        List<JavaType> types = new ArrayList<>();
        types.add(firstType);
        types.add(secondType);
        TypeBindings typeBindings = new TypeBindings(Object[].class, types.toArray(new JavaType[0]));
        assertEquals(_factory.constructType(Object[].class, new JavaType[]{firstType, secondType}), parser.parse("String, Integer,"));
    }

    @Test
    public void testParseInvalidType() {
        _factory = new TypeFactory();
        TypeParser parser = new TypeParser(_factory);
        assertThrows(IllegalArgumentException.class, () -> parser.parse("InvalidType"));
    }

    @Test
    public void testParseInvalidTypeWithUnexpectedToken() {
        _factory = new TypeFactory();
        TypeParser parser = new TypeParser(_factory);
        assertThrows(IllegalArgumentException.class, () -> parser.parse("InvalidType<"));
    }

    @Test
    public void testParseInvalidTypeWithUnexpectedCharacter() {
        _factory = new TypeFactory();
        TypeParser parser = new TypeParser(_factory);
        assertThrows(IllegalArgumentException.class, () -> parser.parse("InvalidType#"));
    }
}