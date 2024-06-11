package com.fasterxml.jackson.databind.type;

import org.junit.Test;
import static org.junit.Assert.*;

public class TypeParserTest {
    
    @Test
    public void testParse() {
        TypeFactory factory = new TypeFactory();
        TypeParser parser = new TypeParser(factory);
        
        try {
            JavaType type = parser.parse("java.util.List<java.lang.String>");
            assertNotNull(type);
        } catch (IllegalArgumentException e) {
            fail("Failed to parse canonical type");
        }
    }
    
    @Test
    public void testParseType() {
        TypeFactory factory = new TypeFactory();
        TypeParser parser = new TypeParser(factory);
        
        MyTokenizer tokens = new MyTokenizer("java.lang.String");
        try {
            JavaType type = parser.parseType(tokens);
            assertNotNull(type);
        } catch (IllegalArgumentException e) {
            fail("Failed to parse type");
        }
    }
    
    @Test
    public void testParseTypes() {
        TypeFactory factory = new TypeFactory();
        TypeParser parser = new TypeParser(factory);
        
        MyTokenizer tokens = new MyTokenizer("java.lang.String, java.lang.Integer");
        try {
            List<JavaType> types = parser.parseTypes(tokens);
            assertNotNull(types);
            assertTrue(types.size() == 2);
        } catch (IllegalArgumentException e) {
            fail("Failed to parse types");
        }
    }
    
    @Test
    public void testFindClass() {
        TypeFactory factory = new TypeFactory();
        TypeParser parser = new TypeParser(factory);
        
        try {
            Class<?> clazz = parser.findClass("java.lang.String", new MyTokenizer(""));
            assertEquals("java.lang.String", clazz.getName());
        } catch (IllegalArgumentException e) {
            fail("Failed to find class");
        }
    }
    
    @Test
    public void testProblem() {
        TypeFactory factory = new TypeFactory();
        TypeParser parser = new TypeParser(factory);
        
        MyTokenizer tokens = new MyTokenizer("java.lang.String");
        IllegalArgumentException e = parser._problem(tokens, "Test error message");
        
        assertNotNull(e);
        assertEquals("Failed to parse type 'java.lang.String' (remaining: ''): Test error message", e.getMessage());
    }
}