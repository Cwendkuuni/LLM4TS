package com.fasterxml.jackson.databind.type;

import static org.junit.Assert.*;
import org.junit.*;

public class TypeParserTest {

    @Test
    public void testParse() {
        TypeParser parser = new TypeParser(new TypeFactory());
        JavaType type = parser.parse("java.util.List<java.lang.String>");
        assertNotNull(type);
        assertEquals("java.util.List<java.lang.String>", type.toCanonical());
    }

    @Test
    public void testParseType() {
        TypeParser parser = new TypeParser(new TypeFactory());
        MyTokenizer tokens = new MyTokenizer("java.util.List<String>");
        JavaType type = parser.parseType(tokens);
        assertNotNull(type);
        assertEquals("java.util.List<java.lang.String>", type.toCanonical());
    }

    @Test
    public void testParseTypes() {
        TypeParser parser = new TypeParser(new TypeFactory());
        MyTokenizer tokens = new MyTokenizer("<java.util.List<String>, java.util.Map<Integer, String>>");
        List<JavaType> types = parser.parseTypes(tokens);
        assertEquals(2, types.size());
        assertEquals("java.util.List<java.lang.String>", types.get(0).toCanonical());
        assertEquals("java.util.Map<java.lang.Integer,java.lang.String>", types.get(1).toCanonical());
    }

    @Test
    public void testFindClass() {
        TypeParser parser = new TypeParser(new TypeFactory());
        Class<?> clazz = parser.findClass("java.util.List", new MyTokenizer(""));
        assertEquals(java.util.List.class, clazz);
    }

    @Test
    public void testProblem() {
        TypeParser parser = new TypeParser(new TypeFactory());
        MyTokenizer tokens = new MyTokenizer("java.util.List<String>");
        IllegalArgumentException ex = parser._problem(tokens, "Test");
        assertEquals("Failed to parse type 'java.util.List<String>' (remaining: ''): Test", ex.getMessage());
    }
}