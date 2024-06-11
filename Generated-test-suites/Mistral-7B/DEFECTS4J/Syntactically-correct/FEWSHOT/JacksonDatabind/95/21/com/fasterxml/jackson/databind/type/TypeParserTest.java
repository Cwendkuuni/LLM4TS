package com.fasterxml.jackson.databind.type;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

public class TypeParserTest {

    @Test
    public void testParseTypeWithNullInput() {
        TypeParser parser = new TypeParser(null);
        assertThrows(IllegalArgumentException.class, () -> parser.parse("null"));
    }

    @Test
    public void testParseTypeWithEmptyInput() {
        TypeParser parser = new TypeParser(null);
        assertNull(parser.parse(""));
    }

    @Test
    public void testParseTypeWithValidPrimitiveTypes() {
        TypeParser parser = new TypeParser(new TypeFactory() {
            @Override
            public JavaType _fromClass(JavaType primaryType, Class<?> clazz, TypeBindings typeBindings) {
                return null;
            }
        });
        JavaType intType = parser.parse("int");
        JavaType charType = parser.parse("char");
        JavaType booleanType = parser.parse("boolean");
        JavaType doubleType = parser.parse("double");
        JavaType floatType = parser.parse("float");
        JavaType longType = parser.parse("long");
        JavaType shortType = parser.parse("short");
        JavaType byteType = parser.parse("byte");

        assertTrue(intType.isPrimitive());
        assertTrue(charType.isPrimitive());
        assertTrue(booleanType.isPrimitive());
        assertTrue(doubleType.isPrimitive());
        assertTrue(floatType.isPrimitive());
        assertTrue(longType.isPrimitive());
        assertTrue(shortType.isPrimitive());
        assertTrue(byteType.isPrimitive());

        assertEquals(Integer.TYPE, intType.getRawClass());
        assertEquals(Character.TYPE, charType.getRawClass());
        assertEquals(Boolean.TYPE, booleanType.getRawClass());
        assertEquals(Double.TYPE, doubleType.getRawClass());
        assertEquals(Float.TYPE, floatType.getRawClass());
        assertEquals(Long.TYPE, longType.getRawClass());
        assertEquals(Short.TYPE, shortType.getRawClass());
        assertEquals(Byte.TYPE, byteType.getRawClass());
    }

    @Test
    public void testParseTypeWithValidClassWithNoGenerics() {
        TypeParser parser = new TypeParser(new TypeFactory() {
            @Override
            public JavaType _fromClass(JavaType primaryType, Class<?> clazz, TypeBindings typeBindings) {
                return null;
            }
        });
        JavaType stringType = parser.parse("String");
        JavaType listType = parser.parse("java.util.List");
        JavaType mapType = parser.parse("java.util.Map");

        assertTrue(stringType.isInstanceable());
        assertTrue(listType.isInstanceable());
        assertTrue(mapType.isInstanceable());

        assertEquals(String.class, stringType.getRawClass());
        assertEquals(List.class, listType.getRawClass());
        assertEquals(Map.class, mapType.getRawClass());
    }

    @Test
    public void testParseTypeWithValidClassWithGenericTypes() {
        TypeParser parser = new TypeParser(new TypeFactory() {
            @Override
            public JavaType _fromClass(JavaType primaryType, Class<?> clazz, TypeBindings typeBindings) {
                return null;
            }
        });
        JavaType listTypeOfInt = parser.parse("java.util.List<Integer>");
        JavaType listTypeOfStringAndInteger = parser.parse("java.util.List<String,Integer>");
        JavaType mapTypeOfStringToString = parser.parse("java.util.Map<String,String>");
        JavaType mapTypeOfMapOfStringToString = parser.parse("java.util.Map<java.util.Map<String,String>, String>");

        assertTrue(listTypeOfInt.isInstanceable());
        assertTrue(listTypeOfInt.getType() != null);
        assertTrue(listTypeOfInt.getType().isInstanceable());
        assertTrue(listTypeOfInt.getType().getType() == null);

        assertTrue(listTypeOfStringAndInteger.isInstanceable());
        assertTrue(listTypeOfStringAndInteger.getType().isInstanceable());
        assertTrue(listTypeOfStringAndInteger.getType().getType() != null);
        assertTrue(listTypeOfStringAndInteger.getType().getType().isInstanceable());

        assertTrue(mapTypeOfStringToString.isInstanceable());
        assertTrue(mapTypeOfStringToString.getType() != null);
        assertTrue(mapTypeOfStringToString.getType().isInstanceable());
        assertTrue(mapTypeOfStringToString.getType().getType() == null);

        assertTrue(mapTypeOfMapOfStringToString.isInstanceable());
        assertTrue(mapTypeOfMapOfStringToString.getType() != null);
        assertTrue(mapTypeOfMapOfStringToString.getType().isInstanceable());
        assertTrue(mapTypeOfMapOfStringToString.getType().getType() != null);
        assertTrue(mapTypeOfMapOfStringToString.getType().getType().isInstanceable());

        assertEquals(List.class, listTypeOfInt.getRawClass());
        assertEquals(Integer.class, listTypeOfInt.getType().getRawClass());

        assertEquals(List.class, listTypeOfStringAndInteger.getRawClass());
        assertEquals(String.class, listTypeOfStringAndInteger.getType().getRawClass());
        assertEquals(Integer.class, listTypeOfStringAndInteger.getType().getType().getRawClass());

        assertEquals(Map.class, mapTypeOfStringToString.getRawClass());
        assertEquals(String.class, mapTypeOfStringToString.getType().getRawClass());
        assertEquals(String.class, mapTypeOfStringToString.getType().getType().getRawClass());

        assertEquals(Map.class, mapTypeOfMapOfStringToString.getRawClass());
        assertEquals(Map.class, mapTypeOfMapOfStringToString.getType().getRawClass());
        assertEquals(String.class, mapTypeOfMapOfStringToString.getType().getType().getRawClass());
        assertEquals(String.class, mapTypeOfMapOfStringToString.getType().getType().getType().getRawClass());
    }

    @Test
    public void testParseTypeWithInvalidInput() {
        TypeParser parser = new TypeParser(null);
        assertThrows(IllegalArgumentException.class, () -> parser.parse("invalidType"));
    }

    @Test
    public void testParseTypeWithInvalidTokenOrder() {
        TypeParser parser = new TypeParser(null);
        assertThrows(IllegalArgumentException.class, () -> parser.parse("List<Integer>java.util.List<Integer>"));
    }

    @Test
    public void testParseTypeWithInvalidTokenType() {
        TypeParser parser = new TypeParser(null);
        assertThrows(IllegalArgumentException.class, () -> parser.parse("java.util.List<Integer>foo"));
    }

    @Test
    public void testParseTypeWithInvalidClass() {
        TypeParser parser = new TypeParser(new TypeFactory() {
            @Override
            public JavaType _fromClass(JavaType primaryType, Class<?> clazz, TypeBindings typeBindings) {
                return null;
            }
        });
        assertThrows(IllegalArgumentException.class, () -> parser.parse("java.util.NoSuchClass"));
    }

    @Test
    public void testParseTypeWithInvalidTokenCharacter() {
        TypeParser parser = new TypeParser(null);
        assertThrows(IllegalArgumentException.class, () -> parser.parse("java.util.List<Integer%"));
    }

    @Test
    public void testParseTypeWithInvalidCharacterAfterToken() {
        TypeParser parser = new TypeParser(null);
        assertThrows(IllegalArgumentException.class, () -> parser.parse("java.util.List<Integer> "));
    }

    @Test
    public void testParseTypeWithInvalidNumberOfGenerics() {
        TypeParser parser = new TypeParser(new TypeFactory() {
            @Override
            public JavaType _fromClass(JavaType primaryType, Class<?> clazz, TypeBindings typeBindings) {
                return null;
            }
        });
        assertThrows(IllegalArgumentException.class, () -> parser.parse("java.util.List<Integer, Integer, Integer>"));
    }

    @Test
    public void testParseTypeWithInvalidNumberOfLevelsOfGenerics() {
        TypeParser parser = new TypeParser(new TypeFactory() {
            @Override
            public JavaType _fromClass(JavaType primaryType, Class<?> clazz, TypeBindings typeBindings) {
                return null;
            }
        });
        assertThrows(IllegalArgumentException.class, () -> parser.parse("java.util.Map<java.util.Map<java.util.Map<String,String>, String>, String>"));
    }
}