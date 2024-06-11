
package com.fasterxml.jackson.databind.type;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

public class TypeParserTest {

    @Test
    public void testParse() {
        TypeFactory factory = new TypeFactory();
        TypeParser parser = new TypeParser(factory);

        JavaType type = parser.parse("java.lang.String");
        assertTrue(type instanceof JavaType);
        assertEquals("java.lang.String", type.toString());

        type = parser.parse("java.util.List<java.lang.String>");
        assertTrue(type instanceof JavaType);
        assertEquals("java.util.List<java.lang.String>", type.toString());

        type = parser.parse("java.util.Map<java.lang.String, java.lang.Integer>");
        assertTrue(type instanceof JavaType);
        assertEquals("java.util.Map<java.lang.String, java.lang.Integer>", type.toString());

        type = parser.parse("com.example.Person");
        assertTrue(type instanceof JavaType);
        assertEquals("com.example.Person", type.toString());

        type = parser.parse("com.example.GenericType<java.lang.String>");
        assertTrue(type instanceof JavaType);
        assertEquals("com.example.GenericType<java.lang.String>", type.toString());
    }

    @Test
    public void testParseType() {
        TypeFactory factory = new TypeFactory();
        TypeParser parser = new TypeParser(factory);
        MyTokenizer tokenizer = new MyTokenizer("java.lang.String");

        JavaType type = parser.parseType(tokenizer);
        assertTrue(type instanceof JavaType);
        assertEquals("java.lang.String", type.toString());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseTypeWithInvalidToken() {
        TypeFactory factory = new TypeFactory();
        TypeParser parser = new TypeParser(factory);
        MyTokenizer tokenizer = new MyTokenizer("invalid token");

        JavaType type = parser.parseType(tokenizer);
    }

    @Test
    public void testParseTypes() {
        TypeFactory factory = new TypeFactory();
        TypeParser parser = new TypeParser(factory);
        MyTokenizer tokenizer = new MyTokenizer("java.lang.String, java.util.List<java.lang.Integer>");

        List<JavaType> types = parser.parseTypes(tokenizer);
        assertTrue(types instanceof ArrayList);
        assertEquals(2, types.size());
        assertEquals("java.lang.String", types.get(0).toString());
        assertEquals("java.util.List<java.lang.Integer>", types.get(1).toString());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseTypesWithInvalidToken() {
        TypeFactory factory = new TypeFactory();
        TypeParser parser = new TypeParser(factory);
        MyTokenizer tokenizer = new MyTokenizer("java.lang.String, invalid token");

        List<JavaType> types = parser.parseTypes(tokenizer);
    }

    @Test
    public void testFindClass() {
        TypeFactory factory = new TypeFactory();
        TypeParser parser = new TypeParser(factory);
        MyTokenizer tokenizer = new MyTokenizer("java.lang.String");

        Class<?> clazz = parser.findClass("java.lang.String", tokenizer);
        assertEquals(String.class, clazz);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFindClassWithInvalidClassName() {
        TypeFactory factory = new TypeFactory();
        TypeParser parser = new TypeParser(factory);
        MyTokenizer tokenizer = new MyTokenizer("java.lang.String");

        Class<?> clazz = parser.findClass("com.example.InvalidClass", tokenizer);
    }

    @Test
    public void testProblem() {
        TypeFactory factory = new TypeFactory();
        TypeParser parser = new TypeParser(factory);
        MyTokenizer tokenizer = new MyTokenizer("java.lang.String");

        IllegalArgumentException exception = parser._problem(tokenizer, "Unexpected end-of-string");

        assertEquals("Failed to parse type 'java.lang.String' (remaining: ''): Unexpected end-of-string", exception.getMessage());
    }

    @Test
    public void testMyTokenizer() {
        MyTokenizer tokenizer = new MyTokenizer("java.lang.String");

        assertEquals("java.lang.String", tokenizer.getAllInput());
        assertEquals("", tokenizer.getRemainingInput());

        assertTrue(tokenizer.hasMoreTokens());
        assertEquals("java.lang.String", tokenizer.nextToken());

        assertFalse(tokenizer.hasMoreTokens());
    }

    @Test
    public void testMyTokenizerWithTokens() {
        MyTokenizer tokenizer = new MyTokenizer("java.lang.String, java.util.List<java.lang.Integer>");

        assertEquals("java.lang.String, java.util.List<java.lang.Integer>", tokenizer.getAllInput());
        assertEquals("", tokenizer.getRemainingInput());

        assertTrue(tokenizer.hasMoreTokens());
        assertEquals("java.lang.String", tokenizer.nextToken());

        assertTrue(tokenizer.hasMoreTokens());
        assertEquals(",", tokenizer.nextToken());

        assertTrue(tokenizer.hasMoreTokens());
        assertEquals("java.util.List<java.lang.Integer>", tokenizer.nextToken());

        assertFalse(tokenizer.hasMoreTokens());
    }

    @Test
    public void testMyTokenizerWithPushback() {
        MyTokenizer tokenizer = new MyTokenizer("java.lang.String, java.util.List<java.lang.Integer>");

        assertEquals("java.lang.String, java.util.List<java.lang.Integer>", tokenizer.getAllInput());
        assertEquals("", tokenizer.getRemainingInput());

        assertTrue(tokenizer.hasMoreTokens());
        assertEquals("java.lang.String", tokenizer.nextToken());

        assertTrue(tokenizer.hasMoreTokens());
        tokenizer.pushBack(",");

        assertTrue(tokenizer.hasMoreTokens());
        assertEquals(",", tokenizer.nextToken());

        assertTrue(tokenizer.hasMoreTokens());
        assertEquals("java.util.List<java.lang.Integer>", tokenizer.nextToken());

        assertFalse(tokenizer.hasMoreTokens());
    }
}
