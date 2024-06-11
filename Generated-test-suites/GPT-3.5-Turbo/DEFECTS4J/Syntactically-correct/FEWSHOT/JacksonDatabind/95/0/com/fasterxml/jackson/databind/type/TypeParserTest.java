package com.fasterxml.jackson.databind.type;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.TypeFactory;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class TypeParserTest {
    private TypeParser parser;

    @Before
    public void setup() {
        parser = new TypeParser(TypeFactory.defaultInstance());
    }

    @Test
    public void testParse() {
        String canonical = "java.util.List<java.lang.String>";
        JavaType type = parser.parse(canonical);
        assertEquals(List.class, type.getRawClass());
        assertEquals(String.class, type.getContentType().getRawClass());
    }

    @Test
    public void testParse_Empty() {
        String canonical = "";
        try {
            parser.parse(canonical);
        } catch (IllegalArgumentException e) {
            assertEquals("Failed to parse type '' (remaining: ''): Unexpected end-of-string", e.getMessage());
        }
    }

    @Test
    public void testParseType() {
        TypeParser.MyTokenizer tokens = new TypeParser.MyTokenizer("java.util.List<java.lang.String>");
        JavaType type = parser.parseType(tokens);
        assertEquals(List.class, type.getRawClass());
        assertEquals(String.class, type.getContentType().getRawClass());
    }

    @Test
    public void testParseType_UnmatchedClosingBracket() {
        TypeParser.MyTokenizer tokens = new TypeParser.MyTokenizer("java.util.List<java.lang.String>>");
        try {
            parser.parseType(tokens);
        } catch (IllegalArgumentException e) {
            assertEquals("Failed to parse type 'java.util.List<java.lang.String>>' (remaining: '>'): Unexpected token '>', expected ',' or '>')", e.getMessage());
        }
    }

    @Test
    public void testParseType_NonGenericClass() {
        TypeParser.MyTokenizer tokens = new TypeParser.MyTokenizer("java.lang.String");
        JavaType type = parser.parseType(tokens);
        assertEquals(String.class, type.getRawClass());
        assertNull(type.getContentType());
    }

    @Test
    public void testParseTypes() {
        TypeParser.MyTokenizer tokens = new TypeParser.MyTokenizer("java.util.List<java.lang.String>, java.util.Map<java.lang.Integer, java.lang.String>");
        JavaType type1 = parser.parseType(tokens);
        JavaType type2 = parser.parseType(tokens);
        assertEquals(List.class, type1.getRawClass());
        assertEquals(String.class, type1.getContentType().getRawClass());
        assertEquals(Map.class, type2.getRawClass());
        assertEquals(Integer.class, type2.getKeyType().getRawClass());
        assertEquals(String.class, type2.getContentType().getRawClass());
    }

    @Test
    public void testParseTypes_UnexpectedToken() {
        TypeParser.MyTokenizer tokens = new TypeParser.MyTokenizer("java.util.List<java.lang.String>, java.util.Map<java.lang.Integer, java.lang.String>>");
        try {
            parser.parseTypes(tokens);
        } catch (IllegalArgumentException e) {
            assertEquals("Failed to parse type 'java.util.List<java.lang.String>, java.util.Map<java.lang.Integer, java.lang.String>>' (remaining: '>'): Unexpected token '>', expected ',' or '>')", e.getMessage());
        }
    }

    @Test
    public void testFindClass() {
        String className = "java.util.List";
        TypeParser.MyTokenizer tokens = new TypeParser.MyTokenizer("java.util.List");
        Class<?> clazz = parser.findClass(className, tokens);
        assertEquals(List.class, clazz);
    }

    @Test
    public void testFindClass_ClassNotFoundException() {
        String className = "non.existent.Class";
        TypeParser.MyTokenizer tokens = new TypeParser.MyTokenizer("non.existent.Class");
        try {
            parser.findClass(className, tokens);
        } catch (IllegalArgumentException e) {
            assertEquals("Failed to parse type 'non.existent.Class' (remaining: 'non.existent.Class'): Can not locate class 'non.existent.Class', problem: non.existent.Class", e.getMessage());
        }
    }
}