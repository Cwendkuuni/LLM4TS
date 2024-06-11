package com.fasterxml.jackson.databind.type;

import static org.junit.Assert.*;
import static org.junit.Assert.assertNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;

public class TypeParserTest {

    private final TypeFactory _factory = new DefaultTypeFactory();
    private final TypeParser parser = new TypeParser(_factory);

    @Test
    public void testParse_emptyString() {
        JavaType type = parser.parse("");
        assertNull(type);
    }

    @Test
    public void testParse_singleClass() {
        JavaType type = parser.parse("String");
        assertEquals(String.class, type.getRawClass());
    }

    @Test
    public void testParse_singleClass_withGenerics() {
        JavaType type = parser.parse("java.util.List<String>");
        assertEquals(List.class, type.getRawClass());
        assertEquals(String.class, type.getTypeArgs()[0].getRawClass());
    }

    @Test
    public void testParse_multiClass() {
        JavaType type = parser.parse("java.util.List<String>[]");
        assertEquals(String[].class, type.getRawClass());
        assertEquals(List.class, type.getTypeComponentType());
        assertEquals(String.class, type.getTypeArgs()[0].getRawClass());
    }

    @Test
    public void testParse_invalidClass() {
        String errorMessage = "Failed to parse type '<invalid>' (remaining: '>')";
        try {
            parser.parse("<invalid>");
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertEquals(errorMessage, e.getMessage());
        }
    }

    @Test
    public void testParse_invalidClass_withExtraToken() {
        String errorMessage = "Failed to parse type 'java.util.List<String>foo' (remaining: 'foo')";
        try {
            parser.parse("java.util.List<String>foo");
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertEquals(errorMessage, e.getMessage());
        }
    }

    @Test
    public void testParse_invalidTypeName() {
        String errorMessage = "Can not locate class 'java.util.List<String>bar'";
        try {
            parser.parse("java.util.List<String>bar");
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertEquals(errorMessage, e.getMessage());
        }
    }

    @Test
    public void testParse_emptyList() {
        JavaType type = parser.parse("List<>()");
        assertEquals(List.class, type.getRawClass());
        assertArrayEquals(new Class[0], type.getTypeComponentType().getTypeArgs());
    }

    @Test
    public void testParse_listWithSingleTypeArgument() {
        JavaType type = parser.parse("List<String>");
        assertEquals(List.class, type.getRawClass());
        assertEquals(String.class, type.getTypeArgs()[0].getRawClass());
    }

    @Test
    public void testParse_listWithMultipleTypeArguments() {
        JavaType type = parser.parse("List<String, Integer>");
        assertEquals(List.class, type.getRawClass());
        assertEquals(String.class, type.getTypeArgs()[0].getRawClass());
        assertEquals(Integer.class, type.getTypeArgs()[1].getRawClass());
    }

    @Test
    public void testParse_complexTypeWithWildcard() {
        JavaType type = parser.parse("List<? extends Number>");
        assertEquals(List.class, type.getRawClass());
        assertEquals(Number.class, type.getTypeComponentType().getSuperClass());
    }

    @Test
    public void testParse_complexTypeWithWildcardAndTypeArguments() {
        JavaType type = parser.parse("List<? extends Number>[List<String>]");
        assertEquals(List.class, type.getRawClass());
        assertEquals(Number[].class, type.getTypeComponentType());
        assertEquals(List.class, type.getTypeArgs()[0].getRawClass());
        assertEquals(String.class, type.getTypeArgs()[1].getRawClass());
    }

    @Test
    public void testParse_complexTypeWithUnclosedAngleBracket() {
        String errorMessage = "Failed to parse type 'List<String>[' (remaining: '')";
        try {
            parser.parse("List<String>[");
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertEquals(errorMessage, e.getMessage());
        }
    }

    @Test
    public void testParse_complexTypeWithInvalidToken() {
        String errorMessage = "Failed to parse type 'List<String>//' (remaining: '//')";
        try {
            parser.parse("List<String>//");
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertEquals(errorMessage, e.getMessage());
        }
    }

    @Test
    public void testParse_complexTypeWithUnmatchedTokens() {
        String errorMessage = "Failed to parse type 'List<String>List<Integer>' (remaining: 'List<Integer>')";
        try {
            parser.parse("List<String>List<Integer>");
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertEquals(errorMessage, e.getMessage());
        }
    }

    private void assertArrayEquals(Object expected, Object actual) {
        if (!(expected instanceof Object[])) {
            assertEquals(expected, actual);
            return;
        }

        Object[] expectedArray = (Object[]) expected;
        Object[] actualArray = (Object[]) actual;

        if (expectedArray.length != actualArray.length) {
            fail("Array lengths mismatch: expected=" + expectedArray.length + ", actual=" + actualArray.length);
        }

        for (int i = 0; i < expectedArray.length; i++) {
            assertEquals(expectedArray[i], actualArray[i]);
        }
    }
}