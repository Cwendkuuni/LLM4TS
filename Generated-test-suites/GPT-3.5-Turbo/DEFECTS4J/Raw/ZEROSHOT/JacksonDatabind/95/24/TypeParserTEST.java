package com.fasterxml.jackson.databind.type;

import static org.junit.Assert.*;
import org.junit.Test;

public class TypeParserTest {
    
    @Test
    public void testParse() {
        // Test case 1
        TypeParser parser = new TypeParser(new TypeFactory());
        JavaType result = parser.parse("");
        assertNull(result);
        
        // Test case 2
        parser = new TypeParser(new TypeFactory());
        result = parser.parse("java.lang.String");
        assertNotNull(result);
        assertTrue(result instanceof JavaType);
        
        // Test case 3
        parser = new TypeParser(new TypeFactory());
        result = parser.parse("java.util.List<java.lang.String>");
        assertNotNull(result);
        assertTrue(result instanceof JavaType);
        
        // Test case 4
        parser = new TypeParser(new TypeFactory());
        try {
            result = parser.parse("java.util.List<java.lang.String");
            fail("IllegalArgumentException should be thrown");
        } catch (IllegalArgumentException e) {
            assertEquals("Failed to parse type 'java.util.List<java.lang.String' (remaining: ''): Unexpected end-of-string", e.getMessage());
        }
        
        // Test case 5
        parser = new TypeParser(new TypeFactory());
        try {
            result = parser.parse("java.util.List<<java.lang.String>");
            fail("IllegalArgumentException should be thrown");
        } catch (IllegalArgumentException e) {
            assertEquals("Failed to parse type 'java.util.List<<java.lang.String>' (remaining: ''): Unexpected token '<', expected ',' or '>')", e.getMessage());
        }
    }
    
    @Test
    public void testParseType() {
        // Test case 1
        TypeParser parser = new TypeParser(new TypeFactory());
        TypeParser.MyTokenizer tokens = parser.new MyTokenizer("");
        try {
            JavaType result = parser.parseType(tokens);
            fail("IllegalArgumentException should be thrown");
        } catch (IllegalArgumentException e) {
            assertEquals("Failed to parse type '' (remaining: ''): Unexpected end-of-string", e.getMessage());
        }
        
        // Test case 2
        parser = new TypeParser(new TypeFactory());
        tokens = parser.new MyTokenizer("java.lang.String");
        JavaType result = parser.parseType(tokens);
        assertNotNull(result);
        assertTrue(result instanceof JavaType);
        
        // Test case 3
        parser = new TypeParser(new TypeFactory());
        tokens = parser.new MyTokenizer("java.util.List<java.lang.String>");
        result = parser.parseType(tokens);
        assertNotNull(result);
        assertTrue(result instanceof JavaType);
        
        // Test case 4
        parser = new TypeParser(new TypeFactory());
        tokens = parser.new MyTokenizer("java.util.List<java.lang.String");
        try {
            result = parser.parseType(tokens);
            fail("IllegalArgumentException should be thrown");
        } catch (IllegalArgumentException e) {
            assertEquals("Failed to parse type 'java.util.List<java.lang.String' (remaining: ''): Unexpected end-of-string", e.getMessage());
        }
        
        // Test case 5
        parser = new TypeParser(new TypeFactory());
        tokens = parser.new MyTokenizer("java.util.List<java.lang.String>");
        tokens.pushBack(">");
        result = parser.parseType(tokens);
        assertNotNull(result);
        assertTrue(result instanceof JavaType);
    }
    
    @Test
    public void testParseTypes() {
        // Test case 1
        TypeParser parser = new TypeParser(new TypeFactory());
        TypeParser.MyTokenizer tokens = parser.new MyTokenizer("");
        try {
            parser.parseTypes(tokens);
            fail("IllegalArgumentException should be thrown");
        } catch (IllegalArgumentException e) {
            assertEquals("Failed to parse type '' (remaining: ''): Unexpected end-of-string", e.getMessage());
        }
        
        // Test case 2
        parser = new TypeParser(new TypeFactory());
        tokens = parser.new MyTokenizer("java.lang.String");
        try {
            parser.parseTypes(tokens);
            fail("IllegalArgumentException should be thrown");
        } catch (IllegalArgumentException e) {
            assertEquals("Failed to parse type 'java.lang.String' (remaining: ''): Unexpected end-of-string", e.getMessage());
        }
        
        // Test case 3
        parser = new TypeParser(new TypeFactory());
        tokens = parser.new MyTokenizer("java.util.List<java.lang.String>");
        try {
            parser.parseTypes(tokens);
            fail("IllegalArgumentException should be thrown");
        } catch (IllegalArgumentException e) {
            assertEquals("Failed to parse type 'java.util.List<java.lang.String>' (remaining: ''): Unexpected end-of-string", e.getMessage());
        }
        
        // Test case 4
        parser = new TypeParser(new TypeFactory());
        tokens = parser.new MyTokenizer("java.util.List<java.lang.String>");
        ArrayList<JavaType> result = parser.parseTypes(tokens);
        assertNotNull(result);
        assertTrue(result instanceof ArrayList<JavaType>);
        
        // Test case 5
        parser = new TypeParser(new TypeFactory());
        tokens = parser.new MyTokenizer("java.util.List<java.lang.String");
        try {
            parser.parseTypes(tokens);
            fail("IllegalArgumentException should be thrown");
        } catch (IllegalArgumentException e) {
            assertEquals("Failed to parse type 'java.util.List<java.lang.String' (remaining: ''): Unexpected end-of-string", e.getMessage());
        }
        
        // Test case 6
        parser = new TypeParser(new TypeFactory());
        tokens = parser.new MyTokenizer("java.util.List<java.lang.String>");
        tokens.pushBack(">");
        result = parser.parseTypes(tokens);
        assertNotNull(result);
        assertTrue(result instanceof ArrayList<JavaType>);
    }
    
    @Test
    public void testFindClass() {
        // Test case 1
        TypeParser parser = new TypeParser(new TypeFactory());
        TypeParser.MyTokenizer tokens = parser.new MyTokenizer("");
        Class<?> result = parser.findClass("", tokens);
        assertNull(result);
        
        // Test case 2
        parser = new TypeParser(new TypeFactory());
        tokens = parser.new MyTokenizer("java.lang.String");
        result = parser.findClass("java.lang.String", tokens);
        assertNotNull(result);
        assertEquals(String.class, result);
        
        // Test case 3
        parser = new TypeParser(new TypeFactory());
        tokens = parser.new MyTokenizer("java.lang.Integer");
        result = parser.findClass("java.lang.Integer", tokens);
        assertNotNull(result);
        assertEquals(Integer.class, result);
        
        // Test case 4
        parser = new TypeParser(new TypeFactory());
        tokens = parser.new MyTokenizer("javautilList");
        try {
            result = parser.findClass("javautilList", tokens);
            fail("IllegalArgumentException should be thrown");
        } catch (IllegalArgumentException e) {
            assertEquals("Failed to parse type 'javautilList' (remaining: ''): Can not locate class 'javautilList', problem: null", e.getMessage());
        }
    }
    
    @Test
    public void test_problem() {
        TypeParser parser = new TypeParser(new TypeFactory());
        TypeParser.MyTokenizer tokens = parser.new MyTokenizer("");
        String result = parser._problem(tokens, "Unexpected end-of-string");
        assertNotNull(result);
        assertEquals("Failed to parse type '' (remaining: ''): Unexpected end-of-string", result);
    }
}