package com.fasterxml.jackson.databind.type;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

public class TypeParserTest {

    private TypeFactory _factory;

    @Before
    public void setup() {
        _factory = new SimpleTypeFactory();
    }

    @Test
    public void testParseEmpty() {
        JavaType type = new TypeParser(_factory).parse(" ");
        assertEquals(Void.TYPE, type);
    }

    @Test
    public void testParseClass() {
        JavaType type = new TypeParser(_factory).parse("String");
        assertEquals(String.class, type.getRawClass());
    }

    @Test
    public void testParseClassWithGeneric() {
        JavaType type = new TypeParser(_factory).parse("List<String>");
        assertEquals(new TypeReference<List<String>>() {}.getType(), type);
    }

    @Test
    public void testParseClassWithGenericAndTypeArgument() {
        JavaType type = new TypeParser(_factory).parse("Map<String, List<Integer>>");
        assertEquals(new TypeReference<Map<String, List<Integer>>>() {}.getType(), type);
    }

    @Test
    public void testParseClassWithGenericAndTypeArguments() {
        JavaType type = new TypeParser(_factory).parse("List<String>[]");
        assertEquals(new TypeReference<JavaType[]>() {
            {
                getComponentType().set(new TypeReference<List<String>>() {}.getType());
            }
        }.getType(), type);
    }

    @Test
    public void testParseInvalidClass() {
        String input = "List<String> uno Two";
        assertThrows(IllegalArgumentException.class, () -> new TypeParser(_factory).parse(input));
    }

    @Test
    public void testParseInvalidClassWithMissingEndQuote() {
        String input = "List<String> uno";
        assertThrows(IllegalArgumentException.class, () -> new TypeParser(_factory).parse(input));
    }

    @Test
    public void testParseInvalidClassWithMissingOpenQuote() {
        String input = "List<String> uno";
        assertThrows(IllegalArgumentException.class, () -> new TypeParser(_factory).parse(input));
    }

    @Test
    public void testParseInvalidClassWithMissingOpenAngleBracket() {
        String input = "List<String> uno";
        assertThrows(IllegalArgumentException.class, () -> new TypeParser(_factory).parse(input));
    }

    @Test
    public void testParseInvalidClassWithMissingCloseAngleBracket() {
        String input = "List<String> uno<";
        assertThrows(IllegalArgumentException.class, () -> new TypeParser(_factory).parse(input));
    }

    @Test
    public void testParseInvalidClassWithExtraCloseAngleBracket() {
        String input = "List<String> uno>";
        assertThrows(IllegalArgumentException.class, () -> new TypeParser(_factory).parse(input));
    }

    @Test
    public void testParseInvalidClassWithExtraCloseQuote() {
        String input = "List<String> uno>";
        assertThrows(IllegalArgumentException.class, () -> new TypeParser(_factory).parse(input));
    }

    @Test
    public void testParseInvalidClassWithExtraComma() {
        String input = "List<String>, List<Integer>";
        assertThrows(IllegalArgumentException.class, () -> new TypeParser(_factory).parse(input));
    }

    @Test
    public void testParseInvalidClassWithExtraSpace() {
        String input = "List< String >";
        assertThrows(IllegalArgumentException.class, () -> new TypeParser(_factory).parse(input));
    }

    @Test
    public void testParseInvalidClassWithInvalidToken() {
        String input = "List<String> uno@";
        assertThrows(IllegalArgumentException.class, () -> new TypeParser(_factory).parse(input));
    }

    @Test
    public void testParseInvalidClassWithEmptyList() {
        String input = "List<>";
        assertThrows(IllegalArgumentException.class, () -> new TypeParser(_factory).parse(input));
    }

    @Test
    public void testParseValidListWithOneElement() {
        JavaType type = new TypeParser(_factory).parse("List<String>");
        assertEquals(List.class, type.getRawClass());
        assertEquals(String.class, type.getContentType());
    }

    @Test
    public void testParseValidListWithMultipleGenericTypes() {
        JavaType type = new TypeParser(_factory).parse("List<List<Map<String, Integer>>>");
        assertEquals(List.class, type.getRawClass());
        assertEquals(List.class, type.getContentType().getRawClass());
        assertEquals(Map.class, type.getContentType().getContentType().getRawClass());
        assertEquals(String.class, type.getContentType().getContentType().getContentType().getRawClass());
        assertEquals(Integer.class, type.getContentType().getContentType().getContentType().getContentType().getRawClass());
    }

    @Test
    public void testParseValidListWithRecursiveElementType() {
        JavaType type = new TypeParser(_factory).parse("List<List<String>>");
        assertEquals(List.class, type.getRawClass());
        assertEquals(List.class, type.getContentType().getRawClass());
        assertEquals(String.class, type.getContentType().getContentType().getRawClass());
    }

    @Test
    public void testParseValidMapWithOneKeyType() {
        JavaType type = new TypeParser(_factory).parse("Map<String, Integer>");
        assertEquals(Map.class, type.getRawClass());
        assertEquals(String.class, type.getContentType().getRawClass());
        assertEquals(Integer.class, type.getGenericType().getRawClass());
    }

    @Test
    public void testParseValidMapWithTwoKeyTypes() {
        JavaType type = new TypeParser(_factory).parse("Map<String, Map<Integer, List<String>>>");
        assertEquals(Map.class, type.getRawClass());
        assertEquals(String.class, type.getGenericType().get(0).getRawClass());
        assertEquals(Map.class, type.getGenericType().get(1).getRawClass());
        assertEquals(Integer.class, type.getGenericType().get(1).getGenericType().get(0).getRawClass());
        assertEquals(List.class, type.getGenericType().get(1).getGenericType().get(1).getRawClass());
        assertEquals(String.class, type.getGenericType().get(1).getGenericType().get(1).getContentType().getRawClass());
    }

    @Test
    public void testParseValidMapWithRecursiveKeyType() {
        JavaType type = new TypeParser(_factory).parse("Map<List<String>, Map<String, String>>");
        assertEquals(Map.class, type.getRawClass());
        assertEquals(List.class, type.getGenericType().get(0).getRawClass());
        assertEquals(Map.class, type.getGenericType().get(1).getRawClass());
        assertEquals(String.class, type.getGenericType().get(1).getGenericType().get(0).getRawClass());
        assertEquals(String.class, type.getGenericType().get(1).getGenericType().get(1).getRawClass());
    }

    @Test
    public void testParseValidMapWithMultipleValueTypes() {
        JavaType type = new TypeParser(_factory).parse("Map<String, List<Integer> & List<String>>");
        assertEquals(Map.class, type.getRawClass());
        assertEquals(String.class, type.getGenericType().get(0).getRawClass());
        assertEquals(List.class, type.getGenericType().get(1).getRawClass());
        assertEquals(Integer.class, type.getGenericType().get(1).getContentType().getRawClass());
        assertEquals(String.class, type.getGenericType().get(1).getContentType().getContentType().getRawClass());
    }

    @Test
    public void testParseValidArray() {
        JavaType type = new TypeParser(_factory).parse("int[][]");
        assertEquals(int[].class, type.getRawClass().getComponentType());
        assertEquals(int[].class, type.getContentType().getRawClass().getComponentType());
    }

    @Test
    public void testParseInvalidArray() {
        assertThrows(IllegalArgumentException.class, () -> new TypeParser(_factory).parse("int[]"));
    }
}