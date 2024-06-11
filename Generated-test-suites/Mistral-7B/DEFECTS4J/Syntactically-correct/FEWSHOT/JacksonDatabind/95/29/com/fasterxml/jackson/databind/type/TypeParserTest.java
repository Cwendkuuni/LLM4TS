package com.fasterxml.jackson.databind.type;

import static org.junit.Assert.*;
import static org.junit.Assert.assertNotNull;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

public class TypeParserTest {

    private TypeFactory _factory;
    private TypeParser _parser;

    @Before
    public void setUp() {
        _factory = new TypeFactory();
        _parser = new TypeParser(_factory);
    }

    @Test
    public void testParseEmpty() {
        JavaType type = _parser.parse(" ");
        assertNull(type);
    }

    @Test
    public void testParsePrimitive() {
        JavaType intType = _parser.parse("int");
        JavaType booleanType = _parser.parse("boolean");
        JavaType charType = _parser.parse("char");
        JavaType doubleType = _parser.parse("double");
        JavaType floatType = _parser.parse("float");
        JavaType longType = _parser.parse("long");
        JavaType shortType = _parser.parse("short");
        JavaType byteType = _parser.parse("byte");
        JavaType voidType = _parser.parse("void");

        assertNotNull(intType);
        assertNotNull(booleanType);
        assertNotNull(charType);
        assertNotNull(doubleType);
        assertNotNull(floatType);
        assertNotNull(longType);
        assertNotNull(shortType);
        assertNotNull(byteType);
        assertNotNull(voidType);

        assertEquals(Integer.TYPE, intType.getRawClass());
        assertEquals(Boolean.TYPE, booleanType.getRawClass());
        assertEquals(Character.TYPE, charType.getRawClass());
        assertEquals(Double.TYPE, doubleType.getRawClass());
        assertEquals(Float.TYPE, floatType.getRawClass());
        assertEquals(Long.TYPE, longType.getRawClass());
        assertEquals(Short.TYPE, shortType.getRawClass());
        assertEquals(Byte.TYPE, byteType.getRawClass());
        assertEquals(Void.TYPE, voidType.getRawClass());
    }

    @Test
    public void testParseClass() {
        JavaType stringType = _parser.parse("String");
        JavaType arrayListType = _parser.parse("java.util.ArrayList<java.lang.Integer>");
        JavaType listType = _parser.parse("java.util.List<java.lang.String>");
        JavaType hashMapType = _parser.parse("java.util.HashMap<java.lang.String, java.lang.Integer>");

        assertNotNull(stringType);
        assertNotNull(arrayListType);
        assertNotNull(listType);
        assertNotNull(hashMapType);

        assertEquals(String.class, stringType.getRawClass());
        assertEquals(ArrayList.class, arrayListType.getRawClass());
        assertEquals(List.class, listType.getRawClass());
        assertEquals(HashMap.class, hashMapType.getRawClass());
    }

    @Test
    public void testParseGenericClass() {
        JavaType listType = _parser.parse("java.util.List<java.lang.Integer>");

        assertNotNull(listType);
        assertEquals(List.class, listType.getRawClass());
        assertEquals(Integer.class, listType.getContentType());
    }

    @Test
    public void testParseArray() {
        JavaType intArrayType = _parser.parse("int[]");
        JavaType stringArrayType = _parser.parse("String[]");

        assertNotNull(intArrayType);
        assertNotNull(stringArrayType);

        assertEquals(int[].class, intArrayType.getRawClass());
        assertEquals(String[].class, stringArrayType.getRawClass());
    }

    @Test
    public void testParseNestedTypes() {
        JavaType listOfListOfString = _parser.parse("java.util.List<java.util.List<java.lang.String>>");

        assertNotNull(listOfListOfString);
        assertEquals(List.class, listOfListOfString.getRawClass());
        List<JavaType> innerListTypes = (List<JavaType>) listOfListOfString.getContentType();
        assertEquals(List.class, innerListTypes.get(0).getRawClass());
        assertEquals(String.class, innerListTypes.get(0).getContentType().getRawClass());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseMissingCloseBracket() {
        _parser.parse("java.util.List<java.lang.String");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseMissingOpenBracket() {
        _parser.parse("java.util.List<java.lang.String> java.util.List<java.lang.Integer>");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseInvalidType() {
        _parser.parse("java.util.List<java.lang.String> foo");
    }
}