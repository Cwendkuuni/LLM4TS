package com.fasterxml.jackson.databind.type;

import static org.junit.Assert.*;
import static org.junit.Assert.assertNotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;

public class TypeParserTest {

    private TypeFactory _factory;
    private TypeParser _parser;

    @SuppressWarnings("unchecked")
    @Before
    public void setUp() {
        _factory = new TypeFactory();
        _parser = new TypeParser(_factory);
    }

    @Test
    public void testParse_EmptyString() {
        JavaType type = _parser.parse("");
        assertNull(type);
    }

    @Test
    public void testParse_SingleClass() {
        JavaType type = _parser.parse("String");
        assertEquals(String.class, type.getRawClass());
    }

    @Test
    public void testParse_ArrayListOfStrings() {
        JavaType type = _parser.parse("java.util.ArrayList<java.lang.String>");
        assertArrayEquals(new Class[] { ArrayList.class, String.class }, type.getComponentTypes());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParse_UnexpectedToken() {
        _parser.parse("String@");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParse_MissingEndParenthesis() {
        _parser.parse("java.util.ArrayList<java.lang.String<");
    }

    @Test
    public void testParse_NestedTypes() {
        JavaType type = _parser.parse("java.util.ArrayList<java.util.ArrayList<java.lang.String>>");
        List<JavaType> components = Arrays.asList(ArrayList.class, ArrayList.class, String.class);
        assertArrayEquals(components.toArray(new JavaType[0]), type.getComponentTypes());
    }

    @Test
    public void testParse_GenericTypeWithWildcard() {
        JavaType type = _parser.parse("java.util.ArrayList<java.lang.String>?");
        assertNull(type.getComponentType());
    }

    @Test
    public void testParse_TypeWithTypeVariable() {
        JavaType type = _parser.parse("java.util.List<T>");
        assertEquals(List.class, type.getRawClass());
        TypeVariable<?> typeVariable = type.getTypeVariables().nextElement();
        assertEquals("T", typeVariable.getName());
    }

    @Test
    public void testParse_TypeWithMultipleTypeVariables() {
        JavaType type = _parser.parse("java.util.Map<K,V>");
        assertEquals(Map.class, type.getRawClass());
        List<TypeVariable<?>> typeVariables = Arrays.asList(type.getTypeVariables());
        assertEquals(2, typeVariables.size());
        assertEquals("K", typeVariables.get(0).getName());
        assertEquals("V", typeVariables.get(1).getName());
    }
}