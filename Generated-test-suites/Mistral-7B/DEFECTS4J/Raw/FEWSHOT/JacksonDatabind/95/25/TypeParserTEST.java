package com.fasterxml.jackson.databind.type;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

public class TypeParserTest {

    @Test
    public void testParseNull() {
        TypeParser parser = new TypeParser(TypeFactory.defaultInstance());
        try {
            parser.parse(null);
            fail();
        } catch (IllegalArgumentException e) {
            assertEquals("Failed to parse type '<null>' (remaining: ''): Unexpected end-of-string", e.getMessage());
        }
    }

    @Test
    public void testParseEmptyString() {
        TypeParser parser = new TypeParser(TypeFactory.defaultInstance());
        JavaType type = parser.parse("");
        assertNotNull(type);
        assertEquals(Object.class, type.getRawClass());
    }

    @Test
    public void testParseSingleClass() {
        TypeParser parser = new TypeParser(TypeFactory.defaultInstance());
        JavaType type = parser.parse("String");
        assertNotNull(type);
        assertEquals(String.class, type.getRawClass());
    }

    @Test
    public void testParseClassWithGenerics() {
        TypeParser parser = new TypeParser(TypeFactory.defaultInstance());
        JavaType type = parser.parse("List<String>");
        assertNotNull(type);
        java.util.List listType = type.getType();
        assertEquals(List.class, listType.getComponentType());
        assertEquals(String.class, listType.getType());
    }

    @Test
    public void testParseClassWithMultipleGenerics() {
        TypeParser parser = new TypeParser(TypeFactory.defaultInstance());
        JavaType type = parser.parse("Map<String, List<Integer>>");
        assertNotNull(type);
        java.util.Map mapType = (java.util.Map) type.getType();
        assertEquals(String.class, mapType.keyType());
        java.util.List listType = (java.util.List) mapType.valueType();
        assertEquals(Integer.class, listType.getComponentType());
        assertEquals(List.class, listType.getType());
    }

    @Test
    public void testParseInvalidString() {
        TypeParser parser = new TypeParser(TypeFactory.defaultInstance());
        try {
            parser.parse("List<String>XXX");
            fail();
        } catch (IllegalArgumentException e) {
            assertTrue(e.getMessage().contains("Unexpected token 'X'"));
        }
    }

    @Test
    public void testParseInvalidClass() {
        TypeParser parser = new TypeParser(TypeFactory.defaultInstance());
        try {
            parser.parse("Foo<String>");
            fail();
        } catch (IllegalArgumentException e) {
            assertTrue(e.getMessage().contains("Can not locate class 'Foo'"));
        }
    }

    @Test
    public void testParseInvalidLeftParenthesis() {
        TypeParser parser = new TypeParser(TypeFactory.defaultInstance());
        try {
            parser.parse("List<String>(");
            fail();
        } catch (IllegalArgumentException e) {
            assertTrue(e.getMessage().contains("Unexpected '<'"));
        }
    }

    @Test
    public void testParseInvalidRightParenthesis() {
        TypeParser parser = new TypeParser(TypeFactory.defaultInstance());
        try {
            parser.parse("List<String>)");
            fail();
        } catch (IllegalArgumentException e) {
            assertTrue(e.getMessage().contains("Unexpected ')'"));
        }
    }

    @Test
    public void testParseInvalidComma() {
        TypeParser parser = new TypeParser(TypeFactory.defaultInstance());
        try {
            parser.parse("List<String>,List<Integer>");
            fail();
        } catch (IllegalArgumentException e) {
            assertTrue(e.getMessage().contains("Unexpected ','"));
        }
    }

    @Test
    public void testParseInvalidTypeSequence1() {
        TypeParser parser = new TypeParser(TypeFactory.defaultInstance());
        try {
            parser.parse("List<String>List<Integer>");
            fail();
        } catch (IllegalArgumentException e) {
            assertTrue(e.getMessage().contains("Unexpected 'List<Integer>'"));
        }
    }

    @Test
    public void testParseInvalidTypeSequence2() {
        TypeParser parser = new TypeParser(TypeFactory.defaultInstance());
        try {
            parser.parse("List<String>List<String>");
            fail();
        } catch (IllegalArgumentException e) {
            assertTrue(e.getMessage().contains("Unexpected 'List<String>'"));
        }
    }
}