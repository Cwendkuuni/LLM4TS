package com.fasterxml.jackson.databind.type;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class TypeParserTest {
    @Test
    public void testParse() {
        TypeFactory factory = new TypeFactory();
        TypeParser parser = new TypeParser(factory);
        
        // Test cases for valid input
        JavaType type1 = parser.parse("java.lang.String");
        assertNotNull(type1);
        assertEquals("java.lang.String", type1.toString());
        
        JavaType type2 = parser.parse("java.util.List<java.lang.Integer>");
        assertNotNull(type2);
        assertEquals("java.util.List<java.lang.Integer>", type2.toString());
        
        JavaType type3 = parser.parse("java.util.Map<java.lang.String,java.lang.Integer>");
        assertNotNull(type3);
        assertEquals("java.util.Map<java.lang.String,java.lang.Integer>", type3.toString());
        
        // Test cases for invalid input
        try {
            parser.parse("");
        } catch (IllegalArgumentException e) {
            assertEquals("Failed to parse type '' (remaining: ''): Unexpected end-of-string", e.getMessage());
        }
        
        try {
            parser.parse("java.lang.String,,");
        } catch (IllegalArgumentException e) {
            assertEquals("Failed to parse type 'java.lang.String,' (remaining: ','): Unexpected token ',', expected ',' or '>')", e.getMessage());
        }
        
        try {
            parser.parse("java.util.List java.lang.Integer>");
        } catch (IllegalArgumentException e) {
            assertEquals("Failed to parse type 'java.util.List java.lang.Integer>' (remaining: ' java.lang.Integer>'): Unexpected token ' java.lang.Integer>', expected ',' or '>')", e.getMessage());
        }
    }

    @Test
    public void testParseType() {
        TypeFactory factory = new TypeFactory();
        TypeParser parser = new TypeParser(factory);
        TypeParser.MyTokenizer tokenizer = parser.new MyTokenizer("java.util.List<java.lang.Integer>");
        
        JavaType type = parser.parseType(tokenizer);
        assertNotNull(type);
        assertEquals("java.util.List<java.lang.Integer>", type.toString());
        
        tokenizer = parser.new MyTokenizer("java.lang.String");
        type = parser.parseType(tokenizer);
        assertNotNull(type);
        assertEquals("java.lang.String", type.toString());
        
        tokenizer = parser.new MyTokenizer("java.util.Map<java.lang.String,java.lang.Integer>");
        type = parser.parseType(tokenizer);
        assertNotNull(type);
        assertEquals("java.util.Map<java.lang.String,java.lang.Integer>", type.toString());
        
        tokenizer = parser.new MyTokenizer("");
        try {
            parser.parseType(tokenizer);
        } catch (IllegalArgumentException e) {
            assertEquals("Failed to parse type '' (remaining: ''): Unexpected end-of-string", e.getMessage());
        }
        
        tokenizer = parser.new MyTokenizer("java.util.List<java.lang.Integer");
        try {
            parser.parseType(tokenizer);
        } catch (IllegalArgumentException e) {
            assertEquals("Failed to parse type 'java.util.List<java.lang.Integer' (remaining: ' java.lang.Integer'): Unexpected end-of-string", e.getMessage());
        }
        
        tokenizer = parser.new MyTokenizer("java.util.Map<java.lang.String,java.lang.Integer");
        try {
            parser.parseType(tokenizer);
        } catch (IllegalArgumentException e) {
            assertEquals("Failed to parse type 'java.util.Map<java.lang.String,java.lang.Integer' (remaining: ' java.lang.Integer'): Unexpected end-of-string", e.getMessage());
        }
    }

    @Test
    public void testParseTypes() {
        TypeFactory factory = new TypeFactory();
        TypeParser parser = new TypeParser(factory);
        TypeParser.MyTokenizer tokenizer = parser.new MyTokenizer("java.lang.String,java.lang.Integer");
        
        JavaType type = parser.parseType(tokenizer);
        assertNotNull(type);
        assertEquals("java.lang.String", type.toString());
        
        tokenizer = parser.new MyTokenizer("java.util.Map<java.lang.String,java.lang.Integer><java.util.List<java.lang.String>,java.util.Map<java.lang.Integer,java.lang.String>>");
        type = parser.parseType(tokenizer);
        assertNotNull(type);
        assertEquals("java.util.Map<java.lang.String,java.lang.Integer>", type.toString());
        
        tokenizer = parser.new MyTokenizer("");
        try {
            parser.parseTypes(tokenizer);
        } catch (IllegalArgumentException e) {
            assertEquals("Failed to parse type '' (remaining: ''): Unexpected end-of-string", e.getMessage());
        }
        
        tokenizer = parser.new MyTokenizer("java.lang.String,java.lang.Integer > java.util.List<java.lang.String>,java.util.Map<java.lang.Integer,java.lang.String");
        try {
            parser.parseTypes(tokenizer);
        } catch (IllegalArgumentException e) {
            assertEquals("Failed to parse type 'java.lang.String,java.lang.Integer > java.util.List<java.lang.String>,java.util.Map<java.lang.Integer,java.lang.String' (remaining: ' <java.lang.String>,java.util.Map<java.lang.Integer,java.lang.String'): Unexpected token ' <java.lang.String>,java.util.Map<java.lang.Integer,java.lang.String>', expected ',' or '>')", e.getMessage());
        }
    }

    @Test
    public void testFindClass() {
        TypeFactory factory = new TypeFactory();
        TypeParser parser = new TypeParser(factory);
        
        Class<?> clazz = parser.findClass("java.lang.String", null);
        assertEquals(String.class, clazz);
        
        try {
            parser.findClass("invalid.class", null);
        } catch (IllegalArgumentException e) {
            assertEquals("Can not locate class 'invalid.class', problem: null", e.getMessage());
        }
    }

    @Test
    public void testProblem() {
        TypeFactory factory = new TypeFactory();
        TypeParser parser = new TypeParser(factory);
        TypeParser.MyTokenizer tokenizer = parser.new MyTokenizer("java.lang.String,java.util.List");
        
        try {
            parser._problem(tokenizer, "Unexpected end-of-string");
        } catch (IllegalArgumentException e) {
            assertEquals("Failed to parse type 'java.lang.String,java.util.List' (remaining: ''): Unexpected end-of-string", e.getMessage());
        }
    }

    @Test
    public void testMyTokenizer() {
        TypeParser parser = new TypeParser(new TypeFactory());
        TypeParser.MyTokenizer tokenizer = parser.new MyTokenizer("java.lang.String,java.util.List");
        
        assertEquals("java.lang.String,java.util.List", tokenizer.getAllInput());
        assertEquals("java.lang.String", tokenizer.nextToken());
        assertEquals("java.lang.String", tokenizer.getRemainingInput());
        assertEquals("java.util.List", tokenizer.nextToken());
        
        tokenizer.pushBack("java.util.Map");
        assertEquals("java.util.Map", tokenizer.nextToken());
        assertEquals("java.lang.String", tokenizer.getRemainingInput());
        assertEquals("java.util.List", tokenizer.nextToken());
        assertEquals("", tokenizer.getRemainingInput());
    }
}