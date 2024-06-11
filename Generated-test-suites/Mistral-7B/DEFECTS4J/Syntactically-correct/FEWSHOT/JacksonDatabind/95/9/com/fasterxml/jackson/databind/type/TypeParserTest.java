package com.fasterxml.jackson.databind.type;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;

public class TypeParserTest {

    @Test
    public void testParseNull() {
        assertThrows(IllegalArgumentException.class, () -> new TypeParser(null).parse(""));
    }

    @Test
    public void testParseEmpty() {
        TypeParser parser = new TypeParser(TypeFactory.defaultInstance());
        JavaType result = parser.parse("");
        assertNull(result);
    }

    @Test
    public void testParseSimpleClass() {
        TypeParser parser = new TypeParser(TypeFactory.defaultInstance());
        JavaType result = parser.parse("String");
        JavaType expected = TypeFactory.defaultInstance().constructType(String.class);
        assertEquals(expected, result);
    }

    @Test
    public void testParseClassWithGenerics() {
        TypeParser parser = new TypeParser(TypeFactory.defaultInstance());
        JavaType result = parser.parse("List<String>");
        JavaType expected = TypeFactory.defaultInstance().constructType(List.class, String.class);
        assertEquals(expected, result);
    }

    @Test
    public void testParseClassWithMultipleGenerics() {
        TypeParser parser = new TypeParser(TypeFactory.defaultInstance());
        JavaType result = parser.parse("Map<String, List<Integer>>");
        JavaType expected = TypeFactory.defaultInstance()
                .constructType(Map.class, String.class, List.class, Integer.class);
        assertEquals(expected, result);
    }

    @Test
    public void testParseClassWithArray() {
        TypeParser parser = new TypeParser(TypeFactory.defaultInstance());
        JavaType result = parser.parse("int[][]");
        JavaType expected = TypeFactory.defaultInstance()
                .constructArrayType(int[].class);
        assertEquals(expected, result);

        JavaType expected2 = TypeFactory.defaultInstance()
                .constructArrayType(int[].class, int[].class);
        assertEquals(expected2, parser.parse("int[][]"));
    }

    @Test
    public void testParseClassWithArrayAndGenerics() {
        TypeParser parser = new TypeParser(TypeFactory.defaultInstance());
        JavaType result = parser.parse("List<int[]>");
        JavaType expected = TypeFactory.defaultInstance()
                .constructType(List.class, int[].class);
        assertEquals(expected, result);
    }

    @Test
    public void testParseInvalidType() {
        TypeParser parser = new TypeParser(TypeFactory.defaultInstance());
        assertThrows(IllegalArgumentException.class, () -> parser.parse("invalidType"));
    }

    @Test
    public void testParseInvalidClassToken() {
        TypeParser parser = new TypeParser(TypeFactory.defaultInstance());
        assertThrows(IllegalArgumentException.class, () -> parser.parse("List<invalidClass>"));
    }

    @Test
    public void testParseInvalidClassHierarchy() {
        TypeParser parser = new TypeParser(TypeFactory.defaultInstance());
        assertThrows(IllegalArgumentException.class, () -> parser.parse("List<String extends Integer>"));
    }

    @Test
    public void testParseInvalidTypeWithGenerics() {
        TypeParser parser = new TypeParser(TypeFactory.defaultInstance());
        assertThrows(IllegalArgumentException.class, () -> parser.parse("List<String> int"));
    }

    @Test
    public void testParseInvalidTokenOrder() {
        TypeParser parser = new TypeParser(TypeFactory.defaultInstance());
        assertThrows(IllegalArgumentException.class, () -> parser.parse("Integer<List>"));
    }

    @Test
    public void testParseInvalidArrayType() {
        TypeParser parser = new TypeParser(TypeFactory.defaultInstance());
        assertThrows(IllegalArgumentException.class, () -> parser.parse("int[]"));
    }

    @Test
    public void testParseEmptyListOfTypes() {
        TypeParser parser = new TypeParser(TypeFactory.defaultInstance());
        JavaType result = parser.parse("List<></List>");
        JavaType expected = TypeFactory.defaultInstance()
                .constructType(List.class, JavaType.VALUE_TYPE);
        assertEquals(expected, result);
    }

    @Test
    public void testParseListWithSingleType() {
        TypeParser parser = new TypeParser(TypeFactory.defaultInstance());
        JavaType result = parser.parse("List<String>");
        JavaType expected = TypeFactory.defaultInstance()
                .constructType(List.class, String.class);
        assertEquals(expected, result);
    }

    @Test
    public void testParseListWithMultipleTypes() {
        TypeParser parser = new TypeParser(TypeFactory.defaultInstance());
        JavaType result = parser.parse("List<String, Integer>");
        JavaType expected = TypeFactory.defaultInstance()
                .constructType(List.class, String.class, Integer.class);
        assertEquals(expected, result);
    }

    @Test
    public void testParseListWithEmptyListOfTypes() {
        TypeParser parser = new TypeParser(TypeFactory.defaultInstance());
        JavaType result = parser.parse("List<List<>>");
        JavaType expected = TypeFactory.defaultInstance()
                .constructType(List.class, List.class);
        assertEquals(expected, result);
    }

    @Test
    public void testParseListWithMultipleLevelsOfNestedLists() {
        TypeParser parser = new TypeParser(TypeFactory.defaultInstance());
        JavaType result = parser.parse("List<List<List<String>>>");
        JavaType expected = TypeFactory.defaultInstance()
                .constructType(List.class, List.class, List.class, String.class);
        assertEquals(expected, result);
    }

    @Test
    public void testParseListWithMixedTypes() {
        TypeParser parser = new TypeParser(TypeFactory.defaultInstance());
        JavaType result = parser.parse("List<String, Integer, List<Float>>");
        JavaType expected = TypeFactory.defaultInstance()
                .constructType(List.class, String.class, Integer.class, List.class, Float.class);
        assertEquals(expected, result);
    }

    @Test
    public void testParseListWithEmptyTypes() {
        TypeParser parser = new TypeParser(TypeFactory.defaultInstance());
        JavaType result = parser.parse("List<></List<,>>");
        JavaType expected = TypeFactory.defaultInstance()
                .constructType(List.class, JavaType.VALUE_TYPE, JavaType.VALUE_TYPE);
        assertEquals(expected, result);
    }

    @Test
    public void testParseInvalidListWithEmptyTypes() {
        TypeParser parser = new TypeParser(TypeFactory.defaultInstance());
        assertThrows(IllegalArgumentException.class, () -> parser.parse("List<></List<,>>"));
    }

    @Test
    public void testParseListWithInvalidTypes() {
        TypeParser parser = new TypeParser(TypeFactory.defaultInstance());
        assertThrows(IllegalArgumentException.class, () -> parser.parse("List<String, invalidType>"));
    }

    @Test
    public void testParseListWithInvalidTokenOrder() {
        TypeParser parser = new TypeParser(TypeFactory.defaultInstance());
        assertThrows(IllegalArgumentException.class, () -> parser.parse("List<Integer, String>"));
    }

    @Test
    public void testParseMapWithSingleKeyAndValue() {
        TypeParser parser = new TypeParser(TypeFactory.defaultInstance());
        JavaType result = parser.parse("Map<String, Integer>");
        JavaType expected = TypeFactory.defaultInstance()
                .constructType(Map.class, String.class, Integer.class);
        assertEquals(expected, result);
    }

    @Test
    public void testParseMapWithMultipleKeyAndValue() {
        TypeParser parser = new TypeParser(TypeFactory.defaultInstance());
        JavaType result = parser.parse("Map<String, List<Integer>>");
        JavaType expected = TypeFactory.defaultInstance()
                .constructType(Map.class, String.class, List.class, Integer.class);
        assertEquals(expected, result);
    }

    @Test
    public void testParseMapWithEmptyKeyListAndValue() {
        TypeParser parser = new TypeParser(TypeFactory.defaultInstance());
        JavaType result = parser.parse("Map<> Map<></Map<,>>");
        JavaType expected = TypeFactory.defaultInstance()
                .constructType(Map.class, JavaType.VALUE_TYPE, JavaType.VALUE_TYPE);
        assertEquals(expected, result);
    }

    @Test
    public void testParseMapWithMixedKeyAndValueTypes() {
        TypeParser parser = new TypeParser(TypeFactory.defaultInstance());
        JavaType result = parser.parse("Map<String, List<Float, Integer>>");
        JavaType expected = TypeFactory.defaultInstance()
                .constructType(Map.class, String.class, List.class, Float.class, Integer.class);
        assertEquals(expected, result);
    }

    @Test
    public void testParseMapWithEmptyValue() {
        TypeParser parser = new TypeParser(TypeFactory.defaultInstance());
        JavaType result = parser.parse("Map<String, > Map<String, List<>>");
        JavaType expected = TypeFactory.defaultInstance()
                .constructType(Map.class, String.class, JavaType.VALUE_TYPE);
        assertEquals(expected, result);
    }

    @Test
    public void testParseMapWithMixedKeyAndValueTypesAndEmptyValues() {
        TypeParser parser = new TypeParser(TypeFactory.defaultInstance());
        JavaType result = parser.parse("Map<String, List<Float, Integer>> Map<String, >");
        JavaType expected = TypeFactory.defaultInstance()
                .constructType(Map.class, String.class, List.class, Float.class, Integer.class, JavaType.VALUE_TYPE);
        assertEquals(expected, result);
    }

    @Test
    public void testParseMapWithEmptyKeyListAndValueList() {
        TypeParser parser = new TypeParser(TypeFactory.defaultInstance());
        JavaType result = parser.parse("Map<></Map<,>>");
        JavaType expected = TypeFactory.defaultInstance()
                .constructType(Map.class, JavaType.VALUE_TYPE, JavaType.VALUE_TYPE);
        assertEquals(expected, result);
    }

    @Test
    public void testParseMapWithInvalidKeyAndValueTypes() {
        TypeParser parser = new TypeParser(TypeFactory.defaultInstance());
        assertThrows(IllegalArgumentException.class, () -> parser.parse("Map<String, invalidType>"));
    }

    @Test
    public void testParseMapWithInvalidTokenOrder() {
        TypeParser parser = new TypeParser(TypeFactory.defaultInstance());
        assertThrows(IllegalArgumentException.class, () -> parser.parse("Map<Integer, String>"));
    }

    @Test
    public void testParseArrayWithMultipleLevels() {
        TypeParser parser = new TypeParser(TypeFactory.defaultInstance());
        JavaType result = parser.parse("int[][][]");
        JavaType expected = TypeFactory.defaultInstance()
                .constructArrayType(int[].class, int[].class, int[].class);
        assertEquals(expected, result);
    }

    @Test
    public void testParseComplexType() {
        TypeParser parser = new TypeParser(TypeFactory.defaultInstance());
        JavaType result = parser.parse("Map<List<String>, List<List<Map<String, List<String>>>>>");
        JavaType expected = TypeFactory.defaultInstance()
                .constructType(Map.class, List.class, List.class, Map.class, String.class, List.class, String.class);
        assertEquals(expected, result);
    }

    @Test
    public void testParseComplexTypeWithNullTypes() {
        TypeParser parser = new TypeParser(TypeFactory.defaultInstance());
        JavaType result = parser.parse("Map<List<Null>, List<List<Map<String, List<Null>>>>>");
        JavaType expected = TypeFactory.defaultInstance()
                .constructType(Map.class, List.class, List.class, Map.class, String.class, List.class, null);
        assertEquals(expected, result);
    }

    @Test
    public void testParseComplexTypeWithEmptyTypes() {
        TypeParser parser = new TypeParser(TypeFactory.defaultInstance());
        JavaType result = parser.parse("Map<List<>, List<Map<>, List<>>>");
        JavaType expected = TypeFactory.defaultInstance()
                .constructType(Map.class, List.class, Map.class, List.class);
        assertEquals(expected, result);
    }

    @Test
    public void testParseComplexTypeWithMixedTypes() {
        TypeParser parser = new TypeParser(TypeFactory.defaultInstance());
        JavaType result = parser.parse("Map<List<String>, List<Map<Integer, List<Float>>>>");
        JavaType expected = TypeFactory.defaultInstance()
                .constructType(Map.class, List.class, List.class, Map.class, Integer.class, List.class, Float.class);
        assertEquals(expected, result);
    }
}