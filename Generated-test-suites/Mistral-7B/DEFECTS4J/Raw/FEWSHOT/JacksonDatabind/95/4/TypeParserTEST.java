package com.fasterxml.jackson.databind.type;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;

public class TypeParserTest {

    @Test
    public void testParseEmpty() {
        TypeParser tp = new TypeParser(TypeFactory.defaultInstance());
        try {
            JavaType type = tp.parse(" ");
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertEquals("Failed to parse type ' ' (remaining: ''): Unexpected end-of-string", e.getMessage());
        }
    }

    @Test
    public void testParseClass() {
        TypeParser tp = new TypeParser(TypeFactory.defaultInstance());
        JavaType type = tp.parse("int");
        assertEquals(Integer.TYPE, type.getRawClass());
    }

    @Test
    public void testParseClassWithGenerics1() {
        TypeParser tp = new TypeParser(TypeFactory.defaultInstance());
        JavaType type = tp.parse("List<String>");
        assertEquals(List.class, type.getRawClass());
        assertEquals(String.class, type.getTypeArguments()[0].getRawClass());
    }

    @Test
    public void testParseClassWithGenerics2() {
        TypeParser tp = new TypeParser(TypeFactory.defaultInstance());
        JavaType type = tp.parse("Map<String, List<Integer>>");
        assertEquals(Map.class, type.getRawClass());
        assertEquals(String.class, type.getTypeParameterNames()[0]);
        assertEquals(List.class, type.getTypeParameterNames()[1]);
        assertEquals(Integer.class, type.getTypeArguments()[1].get(0).getRawClass());
    }

    @Test
    public void testParseClassWithGenerics3() {
        TypeParser tp = new TypeParser(TypeFactory.defaultInstance());
        JavaType type = tp.parse("Map<String, List<Integer>>");
        assertEquals(String.class, type.getTypeArguments()[0].getTypeParameters()[0].getTypeArguments()[0].get(0).getRawClass());
        assertEquals(List.class, type.getTypeArguments()[1].getTypeArguments()[0].getRawClass());
        assertEquals(Integer.class, type.getTypeArguments()[1].getTypeArguments()[0].get(0).getRawClass());

        // verify nested generic types
        JavaType genericType1 = type.getTypeArguments()[1].getTypeArguments()[0].get(0);
        JavaType genericType2 = genericType1.getTypeArguments()[0];
        assertEquals(List.class, genericType1.getRawClass());
        assertEquals(Integer.class, genericType2.getRawClass());
    }

    @Test
    public void testParseClassWithGenerics4() {
        TypeParser tp = new TypeParser(TypeFactory.defaultInstance());
        JavaType type = tp.parse("List<Map<String, List<Integer>>>");
        assertEquals(List.class, type.getRawClass());
        assertEquals(Map.class, type.getTypeArguments()[0].getRawClass());
        assertEquals(String.class, type.getTypeParameterNames()[0]);
        assertEquals(List.class, type.getTypeArguments()[1].getRawClass());
        assertEquals(String.class, type.getTypeParameterNames()[1]);
        assertEquals(List.class, type.getTypeArguments()[1].getTypeArguments()[0].getRawClass());
        assertEquals(Map.class, type.getTypeArguments()[1].getTypeArguments()[0].getTypeRawType());
        assertEquals(String.class, type.getTypeArguments()[1].getTypeArguments()[0].getTypeParameterNames()[0]);
        assertEquals(List.class, type.getTypeArguments()[1].getTypeArguments()[0].getTypeArguments()[0].getRawClass());
        assertEquals(Integer.class, type.getTypeArguments()[1].getTypeArguments()[0].getTypeArguments()[0].get(0).getRawClass());

        // verify nested generic types
        JavaType genericType1 = type.getTypeArguments()[1].getTypeArguments()[0];
        JavaType genericType2 = genericType1.getTypeArguments()[0];
        JavaType genericType3 = genericType2.getTypeArguments()[0];
        assertEquals(List.class, genericType1.getRawClass());
        assertEquals(Map.class, genericType2.getRawClass());
        assertEquals(String.class, genericType2.getTypeParameterNames()[0]);
        assertEquals(List.class, genericType2.getTypeArguments()[0].getRawClass());
        assertEquals(Integer.class, genericType3.getRawClass());
    }

    @Test
    public void testParseClassWithWildcards1() {
        TypeParser tp = new TypeParser(TypeFactory.defaultInstance());
        JavaType type = tp.parse("List<?>");
        assertEquals(List.class, type.getRawClass());
        assertFalse(type.getTypeParameters().length > 0);
    }

    @Test
    public void testParseClassWithWildcards2() {
        TypeParser tp = new TypeParser(TypeFactory.defaultInstance());
        JavaType type = tp.parse("Map<?, List<?>>");
        assertEquals(Map.class, type.getRawClass());
        assertEquals(Object.class, type.getTypeParameterNames()[0]);
        assertEquals(Object.class, type.getTypeParameterNames()[1]);
        assertFalse(type.getTypeArguments().length > 0);
    }

    @Test
    public void testParseClassWithWildcards3() {
        TypeParser tp = new TypeParser(TypeFactory.defaultInstance());
        JavaType type = tp.parse("List<? extends Number>");
        assertEquals(List.class, type.getRawClass());
        assertFalse(type.getTypeParameters().length > 0);
        assertEquals(Number.class, type.getRawType().getComponentType());
    }

    @Test
    public void testParseClassWithWildcards4() {
        TypeParser tp = new TypeParser(TypeFactory.defaultInstance());
        JavaType type = tp.parse("Map<?, ? extends List<?>>");
        assertEquals(Map.class, type.getRawClass());
        assertEquals(Object.class, type.getTypeParameterNames()[0]);
        assertEquals(List.class, type.getTypeParameterNames()[1]);
        assertFalse(type.getTypeArguments().length > 0);
    }

    @Test
    public void testParseClassWithWildcards5() {
        TypeParser tp = new TypeParser(TypeFactory.defaultInstance());
        JavaType type = tp.parse("List<? extends Number>");
        assertEquals(List.class, type.getRawClass());
        assertEquals(Number.class, type.getRawType().getComponentType());
        JavaType listType = _factory.constructType(List.class, Number.class);
        assertEquals(listType, type);
    }

    @Test
    public void testParseClassWithWildcards6() {
        TypeParser tp = new TypeParser(TypeFactory.defaultInstance());
        JavaType type = tp.parse("Map<?, List<?>>");
        assertEquals(Map.class, type.getRawClass());
        assertEquals(Object.class, type.getTypeParameterNames()[0]);
        assertEquals(List.class, type.getTypeParameterNames()[1]);
        JavaType mapType = _factory.constructType(Map.class, Object.class, List.class);
        assertEquals(mapType, type);
    }

    @Test
    public void testParseClassWithInvalidToken() {
        TypeParser tp = new TypeParser(TypeFactory.defaultInstance());
        try {
            tp.parse("int #foo");
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertEquals("Failed to parse type 'int #foo' (remaining: ''): Unexpected token '#'", e.getMessage());
        }
    }

    @Test
    public void testParseClassWithMissingClosingBracket() {
        TypeParser tp = new TypeParser(TypeFactory.defaultInstance());
        try {
            tp.parse("List<String>");
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertEquals("Failed to parse type 'List<String>' (remaining: ''): Unexpected end-of-string", e.getMessage());
        }
    }

    @Test
    public void testParseClassWithExtraClosingBracket() {
        TypeParser tp = new TypeParser(TypeFactory.defaultInstance());
        try {
            tp.parse("List<String>");
            tp.parse(">");
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertEquals("Failed to parse type 'List<String>' (remaining: ''): Unexpected '>')", e.getMessage());
        }
    }

    @Test
    public void testParseClassWithMissingComma() {
        TypeParser tp = new TypeParser(TypeFactory.defaultInstance());
        try {
            tp.parse("List<String>List<Integer>");
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertEquals("Failed to parse type 'List<String>List<Integer>' (remaining: ''): Unexpected end-of-string", e.getMessage());
        }
    }

    @Test
    public void testParseClassWithTrailingComma() {
        TypeParser tp = new TypeParser(TypeFactory.defaultInstance());
        JavaType type = tp.parse("List<String>, ");
        assertNull(type);
    }

    @Test
    public void testParseClassWithExtraComma() {
        TypeParser tp = new TypeParser(TypeFactory.defaultInstance());
        JavaType type = tp.parse("List<String>, List<Integer>");
        assertEquals(List.class, type.getRawClass());
        assertEquals(String.class, type.getTypeArguments()[0].getRawClass());
        assertEquals(List.class, type.getTypeArguments()[1].getRawClass());
    }

    @Test
    public void testParseClassWithInvalidType1() {
        TypeParser tp = new TypeParser(TypeFactory.defaultInstance());
        try {
            tp.parse("int foo()");
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertEquals("Failed to parse type 'int foo()' (remaining: '()'): Unexpected '('", e.getMessage());
        }
    }

    @Test
    public void testParseClassWithInvalidType2() {
        TypeParser tp = new TypeParser(TypeFactory.defaultInstance());
        try {
            tp.parse("Map<int, String>");
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertEquals("Failed to parse type 'Map<int, String>' (remaining: ''): Unexpected end-of-string", e.getMessage());
        }
    }

    @Test
    public void testParseClassWithInvalidType3() {
        TypeParser tp = new TypeParser(TypeFactory.defaultInstance());
        try {
            tp.parse("List<Integer, String>");
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertEquals("Failed to parse type 'List<Integer, String>' (remaining: ''): Unexpected comma", e.getMessage());
        }
    }

    @Test
    public void testParseClassWithEmptyGenerics() {
        TypeParser tp = new TypeParser(TypeFactory.defaultInstance());
        JavaType type = tp.parse("Map<>");
        assertEquals(Map.class, type.getRawClass());
        assertEquals(Object.class, type.getTypeParameters()[0]);
    }

    @Test
    public void testParseClassWithEmptyGenerics2() {
        TypeParser tp = new TypeParser(TypeFactory.defaultInstance());
        JavaType type = tp.parse("List<>");
        assertEquals(List.class, type.getRawClass());
        assertEquals(Object.class, type.getTypeType());
    }

    @Test
    public void testParseClassWithInvalidTypeArguments() {
        TypeParser tp = new TypeParser(TypeFactory.defaultInstance());
        try {
            tp.parse("Map<Integer, int>");
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertEquals("Failed to parse type 'Map<Integer, int>' (remaining: ''): Mismatched type arguments: Unexpected 'int' (expected 'List'): Unexpected 'int'", e.getMessage());
        }
    }

    @Test
    public void testParseClassWithInvalidTypeArguments2() {
        TypeParser tp = new TypeParser(TypeFactory.defaultInstance());
        try {
            tp.parse("Map<String, Integer[]>");
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertEquals("Failed to parse type 'Map<String, Integer[]>' (remaining: ''): Mismatched type arguments: Unexpected 'Integer[]' (expected 'List'): Unexpected '[]'", e.getMessage());
        }
    }

    @Test
    public void testParseClassWithInvalidTypeArguments3() {
        TypeParser tp = new TypeParser(TypeFactory.defaultInstance());
        try {
            tp.parse("List<Map<String, Integer>>");
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertEquals("Failed to parse type 'List<Map<String, Integer>>' (remaining: ''): Mismatched type arguments: Unexpected 'Map<String, Integer>' (expected 'List'): Unexpected '<'", e.getMessage());
        }
    }

    @Test
    public void testParseClassWithInvalidTypeArguments4() {
        TypeParser tp = new TypeParser(TypeFactory.defaultInstance());
        try {
            tp.parse("Map<String, List<List<Integer>>>");
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertEquals("Failed to parse type 'Map<String, List<List<Integer>>>' (remaining: ''): Mismatched type arguments: Unexpected 'List<List<Integer>>' (expected 'List'): Unexpected '<'");
        }
    }

    @Test
    public void testParseClassWithCyclicDependency() {
        TypeParser tp = new TypeParser(TypeFactory.defaultInstance());
        try {
            JavaType cycleType1 = tp.parse("public static class A { int x; B b; }");
            JavaType cycleType2 = tp.parse("public static class B { int y; A a; }");
            JavaType cycleType = tp.parse("A b");
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertEquals("Failed to parse type 'A b' (remaining: ''): Cyclic type reference 'B' in type 'A': 'B.a' refers to 'A.b'", e.getMessage());
        }
    }

    private static TypeFactory _factory = TypeFactory.defaultInstance();

}