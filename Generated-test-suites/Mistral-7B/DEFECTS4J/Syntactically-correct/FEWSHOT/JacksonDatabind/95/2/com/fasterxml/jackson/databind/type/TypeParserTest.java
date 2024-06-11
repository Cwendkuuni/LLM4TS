package com.fasterxml.jackson.databind.type;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.Test;

public class TypeParserTest {

    private final TypeFactory _factory = new TypeFactory() {};
    private final TypeParser _parser = new TypeParser(_factory);

    @Test
    public void testParseType_SimpleClass() {
        JavaType type = _parser.parse("int");
        assertEquals(Integer.TYPE, type.getRawClass());
    }

    @Test
    public void testParseType_ClassWithGenericParameter() {
        JavaType type = _parser.parse("List<String>");
        assertEquals(List.class, type.getRawClass());
        assertEquals(String.class, type.getTypeParams().nextValue().getRawClass());
    }

    @Test
    public void testParseType_ClassWithMultipleGenericParameters() {
        JavaType type = _parser.parse("Map<String, List<Integer>>");
        assertEquals(Map.class, type.getRawClass());
        assertEquals(String.class, type.getTypeParams().nextValue().getRawClass());
        assertEquals(List.class, type.getTypeParams().nextValue().getRawClass());
        assertEquals(Integer.TYPE, type.getTypeParams().nextValue().getTypeParams().nextValue().getRawClass());
    }

    @Test
    public void testParseType_ClassWithArrayParameter() {
        JavaType type = _parser.parse("int[][]");
        assertEquals(int[].class, type.getRawClass());
        assertEquals(int[].class, type.getTypeParams().nextValue().getRawClass());
    }

    @Test
    public void testParseType_ClassWithCustomTypeName() {
        JavaType type = _parser.parse("MyCustomType");
        assertFalse(type.isRaw());
        assertTrue(type.getName().toString().startsWith("com.mypackage.MyCustomType"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseType_InvalidType() {
        _parser.parse("InvalidType");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseType_InvalidTypeWithSpaces() {
        _parser.parse("Invalid Type");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseType_MissingClosingBracket() {
        _parser.parse("List<String<");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseType_MissingClosingBracketWithSpaces() {
        _parser.parse("List<String <");
    }

    @Test
    public void testParseTypes_EmptyList() {
        List<JavaType> types = _parser.parseTypes("");
        assertTrue(types.isEmpty());
    }

    @Test
    public void testParseTypes_SingleType() {
        List<JavaType> types = _parser.parseTypes("int");
        assertEquals(1, types.size());
        assertEquals(Integer.TYPE, types.get(0).getRawClass());
    }

    @Test
    public void testParseTypes_MultipleTypesWithComma() {
        List<JavaType> types = _parser.parseTypes("int, List<String>");
        assertEquals(2, types.size());
        assertEquals(Integer.TYPE, types.get(0).getRawClass());
        assertEquals(List.class, types.get(1).getRawClass());
        assertEquals(String.class, types.get(1).getTypeParams().nextValue().getRawClass());
    }

    @Test
    public void testParseTypes_MultipleTypesWithClosingBracket() {
        List<JavaType> types = _parser.parseTypes("int> List<String>");
        assertEquals(2, types.size());
        assertEquals(Integer.TYPE, types.get(0).getRawClass());
        assertEquals(List.class, types.get(1).getRawClass());
        assertEquals(String.class, types.get(1).getTypeParams().nextValue().getRawClass());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseTypes_InvalidTypeWithComma() {
        _parser.parseTypes("int,");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseTypes_InvalidTypeWithClosingBracket() {
        _parser.parseTypes("int>");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseTypes_InvalidTypesWithSpaces() {
        _parser.parseTypes("int , List< String >");
    }
}