package com.fasterxml.jackson.databind.type;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JavaType.TypeBindings;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.fasterxml.jackson.databind.type.TypeParser.MyTokenizer;

public class TypeParserTest {

    @Test
    public void testParse() {
        TypeFactory factory = new TypeFactory();
        TypeParser parser = new TypeParser(factory);
        
        JavaType type = parser.parse("java.util.List<java.lang.String>");
        assertEquals(ArrayList.class, type.getRawClass());
        assertEquals(String.class, type.getBindings().getTypeParameters().get(0).getRawClass());
        
        type = parser.parse("java.util.Map<java.lang.String, java.lang.Integer>");
        assertEquals(java.util.Map.class, type.getRawClass());
        assertEquals(String.class, type.getBindings().getTypeParameters().get(0).getRawClass());
        assertEquals(Integer.class, type.getBindings().getTypeParameters().get(1).getRawClass());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParse_InvalidType() {
        TypeFactory factory = new TypeFactory();
        TypeParser parser = new TypeParser(factory);

        JavaType type = parser.parse("java.util.UnknownType");
    }

    @Test
    public void testParseType() {
        TypeFactory factory = new TypeFactory();
        TypeParser parser = new TypeParser(factory);

        MyTokenizer tokens = parser.new MyTokenizer("java.lang.String");
        JavaType type = parser.parseType(tokens);
        assertEquals(String.class, type.getRawClass());
        assertFalse(tokens.hasMoreTokens());
        
        tokens = parser.new MyTokenizer("java.util.List<java.lang.String>");
        type = parser.parseType(tokens);
        assertEquals(ArrayList.class, type.getRawClass());
        assertEquals(String.class, type.getBindings().getTypeParameters().get(0).getRawClass());
        assertFalse(tokens.hasMoreTokens());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseType_UnexpectedEndOfString() {
        TypeFactory factory = new TypeFactory();
        TypeParser parser = new TypeParser(factory);

        MyTokenizer tokens = parser.new MyTokenizer("java.util.UnknownType<java.lang.String");
        JavaType type = parser.parseType(tokens);
    }

    @Test
    public void testParseTypes() {
        TypeFactory factory = new TypeFactory();
        TypeParser parser = new TypeParser(factory);

        MyTokenizer tokens = parser.new MyTokenizer("java.lang.String, java.lang.Integer");
        List<JavaType> types = parser.parseTypes(tokens);
        assertEquals(2, types.size());
        assertEquals(String.class, types.get(0).getRawClass());
        assertEquals(Integer.class, types.get(1).getRawClass());
        assertFalse(tokens.hasMoreTokens());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseTypes_UnexpectedEndOfString() {
        TypeFactory factory = new TypeFactory();
        TypeParser parser = new TypeParser(factory);

        MyTokenizer tokens = parser.new MyTokenizer("java.lang.String, java.lang.Integer>");
        List<JavaType> types = parser.parseTypes(tokens);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseTypes_UnexpectedToken() {
        TypeFactory factory = new TypeFactory();
        TypeParser parser = new TypeParser(factory);

        MyTokenizer tokens = parser.new MyTokenizer("java.lang.String, java.lang.Integer~");
        List<JavaType> types = parser.parseTypes(tokens);
    }

    @Test
    public void testFindClass() {
        TypeFactory factory = new TypeFactory();
        TypeParser parser = new TypeParser(factory);

        MyTokenizer tokens = parser.new MyTokenizer("java.lang.String");
        Class<?> clazz = parser.findClass("java.lang.String", tokens);
        assertEquals(String.class, clazz);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFindClass_ClassNotFound() {
        TypeFactory factory = new TypeFactory();
        TypeParser parser = new TypeParser(factory);

        MyTokenizer tokens = parser.new MyTokenizer("java.lang.UnknownClass");
        Class<?> clazz = parser.findClass("java.lang.UnknownClass", tokens);
    }

    @Test
    public void testProblem() {
        TypeFactory factory = new TypeFactory();
        TypeParser parser = new TypeParser(factory);

        MyTokenizer tokens = parser.new MyTokenizer("java.util.Map<java.lang.String, java.lang.Integer>");
        IllegalArgumentException exception = parser._problem(tokens, "Unexpected token");
        assertEquals("Failed to parse type 'java.util.Map<java.lang.String, java.lang.Integer>' (remaining: ''): Unexpected token", exception.getMessage());
    }

    @Test
    public void testMyTokenizer() {
        TypeFactory factory = new TypeFactory();
        TypeParser parser = new TypeParser(factory);

        MyTokenizer tokenizer = parser.new MyTokenizer("java.util.List<java.lang.String>");
        assertTrue(tokenizer.hasMoreTokens());
        assertEquals("java.util.List", tokenizer.nextToken());
        assertEquals("<", tokenizer.nextToken());
        assertEquals("java.lang.String", tokenizer.nextToken());
        assertEquals(">", tokenizer.nextToken());
        assertFalse(tokenizer.hasMoreTokens());
        assertEquals("java.util.List<java.lang.String>", tokenizer.getAllInput());
        assertEquals("", tokenizer.getRemainingInput());
        
        tokenizer.pushBack(">");
        assertEquals(">", tokenizer.nextToken());
        assertFalse(tokenizer.hasMoreTokens());
        assertEquals("<java.lang.String>", tokenizer.getRemainingInput());
    }
}