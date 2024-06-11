package com.fasterxml.jackson.databind.type;

import static org.junit.Assert.*;
import org.junit.Test;

public class TypeParserTest {
    
    @Test
    public void testParse() {
        // Typical use case
        TypeFactory factory = new TypeFactory();
        TypeParser parser = new TypeParser(factory);
        JavaType result = parser.parse("java.util.List<java.lang.String>");
        assertNotNull(result);
        assertEquals(List.class, result.getRawClass());
        assertEquals(String.class, result.containedType(0).getRawClass());
        
        // Edge case: empty input
        try {
            result = parser.parse("");
            fail("Expected IllegalArgumentException for empty input");
        } catch (IllegalArgumentException e) {
            // expected
        }
        
        // Edge case: invalid type
        try {
            result = parser.parse("InvalidType");
            fail("Expected IllegalArgumentException for invalid type");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }
    
    @Test
    public void testParseType() {
        // Typical use case
        TypeFactory factory = new TypeFactory();
        TypeParser parser = new TypeParser(factory);
        MyTokenizer tokens = new MyTokenizer("java.lang.String");
        JavaType result = parser.parseType(tokens);
        assertNotNull(result);
        assertEquals(String.class, result.getRawClass());
        
        // Edge case: no tokens
        tokens = new MyTokenizer("");
        try {
            result = parser.parseType(tokens);
            fail("Expected IllegalArgumentException for no tokens");
        } catch (IllegalArgumentException e) {
            // expected
        }
        
        // Edge case: generics
        tokens = new MyTokenizer("java.util.List<java.lang.String>");
        result = parser.parseType(tokens);
        assertNotNull(result);
        assertEquals(List.class, result.getRawClass());
        assertEquals(String.class, result.containedType(0).getRawClass());
    }
    
    @Test
    public void testParseTypes() {
        // Typical use case
        TypeFactory factory = new TypeFactory();
        TypeParser parser = new TypeParser(factory);
        MyTokenizer tokens = new MyTokenizer("java.lang.String, java.lang.Integer");
        List<JavaType> result = parser.parseTypes(tokens);
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(String.class, result.get(0).getRawClass());
        assertEquals(Integer.class, result.get(1).getRawClass());
        
        // Edge case: missing closing '>'
        tokens = new MyTokenizer("java.lang.String, java.lang.Integer");
        try {
            result = parser.parseTypes(tokens);
            fail("Expected IllegalArgumentException for missing closing '>'");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    @Test
    public void testFindClass() {
        TypeFactory factory = new TypeFactory();
        TypeParser parser = new TypeParser(factory);
        MyTokenizer tokens = new MyTokenizer("");
        
        // Typical use case
        Class<?> result = parser.findClass("java.lang.String", tokens);
        assertEquals(String.class, result);
        
        // Edge case: invalid class
        try {
            result = parser.findClass("InvalidClass", tokens);
            fail("Expected IllegalArgumentException for invalid class");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

}