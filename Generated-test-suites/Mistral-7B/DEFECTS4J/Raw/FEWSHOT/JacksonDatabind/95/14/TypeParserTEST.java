package com.fasterxml.jackson.databind.type;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;

public class TypeParserTest {

    private TypeFactory _factory;

    @org.junit.Before
    public void setUp() {
        _factory = new TypeFactoryBuilder()
                .build();
    }

    @Test
    public void testParseEmptyString() {
        JavaType type = new TypeParser(_factory).parse("");
        assertNull(type);
    }

    @Test
    public void testParseSingleClass() {
        JavaType type = new TypeParser(_factory).parse("java.lang.String");
        Type actualType = type.getType();
        assertTrue(actualType.getClass().isAssignableFrom(String.class));
        assertEquals("java.lang.String", type.toString());
    }

    @Test
    public void testParseClassWithGenerics() {
        JavaType type = new TypeParser(_factory).parse("java.util.ArrayList<java.lang.String>");
        Type actualType = type.getType();
        ArrayList listType = (ArrayList) actualType.getComponentType();
        assertTrue(listType.getClass().isAssignableFrom(ArrayList.class));
        assertTrue(listType.getComponentType().getClass().isAssignableFrom(String.class));
        assertEquals("java.util.ArrayList<java.lang.String>", type.toString());
    }

    @Test
    public void testParseClassWithMultipleGenerics() {
        JavaType type = new TypeParser(_factory).parse("java.util.Map<java.lang.String, java.util.List<java.lang.Integer>>");
        Type actualType = type.getType();
        Map mapType = (Map) actualType;
        assertTrue(mapType.getClass().isAssignableFrom(Map.class));
        Class<? extends Map.Entry> entryType = (Class<? extends Map.Entry>) mapType.getClass()
                .getComponentType();
        assertTrue(entryType.getClass().isAssignableFrom(Map.Entry.class));
        Class<?> keyType = entryType.getGenericComponentType();
        assertTrue(keyType.getClass().isAssignableFrom(String.class));
        Class<?> valueType = (Class<?>) entryType.getGenericTypeArguments()[1];
        assertTrue(valueType.getClass().isAssignableFrom(List.class));
        List listType = (List) valueType.getComponentType();
        assertTrue(listType.getClass().isAssignableFrom(List.class));
        assertTrue(listType.getComponentType().getClass().isAssignableFrom(Integer.class));
        assertEquals("java.util.Map<java.lang.String, java.util.List<java.lang.Integer>>", type.toString());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseInvalidType() {
        new TypeParser(_factory).parse("java.util.Map<java.lang.String, java.util.List<java.lang.Integer>");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseInvalidType2() {
        new TypeParser(_factory).parse("java.util.Map<java.lang.String, java.util.List<java.lang.Integer>");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseInvalidType3() {
        new TypeParser(_factory).parse("java.util.Map<java.lang.String, java.util.List<java.lang.Integer>");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseInvalidType4() {
        new TypeParser(_factory).parse("java.util.Map<java.lang.String, java.util.List<java.lang.Integer>");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseInvalidType5() {
        new TypeParser(_factory).parse("java.util.Map<java.lang.String, java.util.List<java.lang.Integer>");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseInvalidType6() {
        new TypeParser(_factory).parse("java.util.Map<java.lang.String, java.util.List<java.lang.Integer>");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseInvalidType7() {
        new TypeParser(_factory).parse("java.util.Map<java.lang.String, java.util.List<java.lang.Integer>");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseInvalidType8() {
        new TypeParser(_factory).parse("java.util.Map<java.lang.String, java.util.List<java.lang.Integer>");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseInvalidType9() {
        new TypeParser(_factory).parse("java.util.Map<java.lang.String, java.util.List<java.lang.Integer>");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseInvalidType10() {
        new TypeParser(_factory).parse("java.util.Map<java.lang.String, java.util.List<java.lang.Integer>");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseInvalidType11() {
        new TypeParser(_factory).parse("java.util.Map<java.lang.String, java.util.List<java.lang.Integer>");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseInvalidType12() {
        new TypeParser(_factory).parse("java.util.Map<java.lang.String, java.util.List<java.lang.Integer>");
    }

    @Test
    public void testParseEmptyListOfTypes() {
        JavaType type = new TypeParser(_factory).parse("[]");
        assertArrayEquals(new Class[0], type.getType().getComponentTypes());
        assertEquals("[]", type.toString());
    }

    @Test
    public void testParseSingleTypeInListOfTypes() {
        JavaType type = new TypeParser(_factory).parse("[java.lang.String]");
        assertArrayEquals(new Class[] { String.class }, type.getType().getComponentTypes());
        assertEquals("[java.lang.String]", type.toString());
    }

    @Test
    public void testParseListOfTypesWithGenerics() {
        JavaType type = new TypeParser(_factory).parse("[java.util.List<java.lang.Integer>]");
        List<JavaType> listTypes = (List<JavaType>) type.getType().getComponentType();
        assertTrue(listTypes.getClass().isAssignableFrom(List.class));
        JavaType listType = listTypes.get(0);
        assertTrue(listType.getType().getClass().isAssignableFrom(List.class));
        Class<?> listElementType = listType.getType().getComponentType();
        assertTrue(listElementType.getClass().isAssignableFrom(Integer.class));
        assertEquals("[java.util.List<java.lang.Integer>]", type.toString());
    }

    @Test
    public void testParseListOfTypesWithMultipleGenerics() {
        JavaType type = new TypeParser(_factory).parse("[java.util.Map<java.lang.String, java.util.List<java.lang.Integer>>]");
        List<JavaType> listTypes = (List<JavaType>) type.getType().getComponentType();
        assertTrue(listTypes.getClass().isAssignableFrom(List.class));
        JavaType listType = listTypes.get(0);
        assertTrue(listType.getType().getClass().isAssignableFrom(Map.class));
        Class<?> mapKeyType = ((Map) listType.getType()).getKeyType().getType();
        assertTrue(mapKeyType.getClass().isAssignableFrom(String.class));
        Class<?> mapValueType = ((Map) listType.getType()).getValueType().getType();
        assertTrue(mapValueType.getClass().isAssignableFrom(List.class));
        JavaType listElementType = ((List) mapValueType.getType()).getType();
        assertTrue(listElementType.getType().getClass().isAssignableFrom(Integer.class));
        assertEquals("[java.util.Map<java.lang.String, java.util.List<java.lang.Integer>>]", type.toString());
    }
}