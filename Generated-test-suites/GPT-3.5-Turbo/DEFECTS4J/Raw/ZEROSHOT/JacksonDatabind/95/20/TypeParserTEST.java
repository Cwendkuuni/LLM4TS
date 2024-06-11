package com.fasterxml.jackson.databind.type;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

public class TypeParserTest {

    private TypeFactory factory;
    private TypeParser parser;
    
    @Before
    public void setUp() {
        factory = new TypeFactory();
        parser = new TypeParser(factory);
    }
    
    @Test
    public void testParse() {
        String canonical = "java.util.List<java.lang.Integer>";
        JavaType type = parser.parse(canonical);
        
        assertNotNull(type);
        assertEquals(List.class, type.getRawClass());
        assertEquals(1, type.containedTypeCount());
        assertEquals(Integer.class, type.containedType(0).getRawClass());
    }
    
    @Test(expected=IllegalArgumentException.class)
    public void testParseWithInvalidType() {
        String canonical = "java.lang.String";
        parser.parse(canonical);
    }
    
    @Test(expected=IllegalArgumentException.class)
    public void testParseWithUnexpectedTokens() {
        String canonical = "java.util.List<java.lang.Integer> otherTokens";
        parser.parse(canonical);
    }
    
    @Test(expected=IllegalArgumentException.class)
    public void testParseWithUnexpectedEndOfString() {
        String canonical = "java.util.List<java.lang.Integer";
        parser.parse(canonical);
    }
    
    @Test
    public void testParseType() {
        TypeParser.MyTokenizer tokens = parser.new MyTokenizer("java.util.List<java.lang.Integer>");
        JavaType type = parser.parseType(tokens);
        
        assertNotNull(type);
        assertEquals(List.class, type.getRawClass());
        assertEquals(1, type.containedTypeCount());
        assertEquals(Integer.class, type.containedType(0).getRawClass());
    }
    
    @Test(expected=IllegalArgumentException.class)
    public void testParseTypeWithBaseClassNotFound() {
        TypeParser.MyTokenizer tokens = parser.new MyTokenizer("java.lang.Unknown");
        parser.parseType(tokens);
    }
    
    @Test
    public void testParseTypes() {
        TypeParser.MyTokenizer tokens = parser.new MyTokenizer("java.lang.String,java.util.List<java.lang.Integer>");
        List<JavaType> types = parser.parseTypes(tokens);
        
        assertNotNull(types);
        assertEquals(2, types.size());
        assertEquals(String.class, types.get(0).getRawClass());
        assertEquals(List.class, types.get(1).getRawClass());
        assertEquals(1, types.get(1).containedTypeCount());
        assertEquals(Integer.class, types.get(1).containedType(0).getRawClass());
    }
    
    @Test(expected=IllegalArgumentException.class)
    public void testParseTypesWithInvalidToken() {
        TypeParser.MyTokenizer tokens = parser.new MyTokenizer("java.lang.String,java.util.List<java.lang.Integer,java.lang.Boolean>");
        parser.parseTypes(tokens);
    }
    
    @Test(expected=IllegalArgumentException.class)
    public void testParseTypesWithUnexpectedEndOfString() {
        TypeParser.MyTokenizer tokens = parser.new MyTokenizer("java.lang.String,java.util.List<java.lang.Integer");
        parser.parseTypes(tokens);
    }
    
    @Test
    public void testFindClass() {
        TypeParser.MyTokenizer tokens = parser.new MyTokenizer("java.lang.String");
        Class<?> clazz = parser.findClass(tokens.nextToken(), tokens);
        
        assertNotNull(clazz);
        assertEquals(String.class, clazz);
    }
    
    @Test(expected=IllegalArgumentException.class)
    public void testFindClassWithClassNotFound() {
        TypeParser.MyTokenizer tokens = parser.new MyTokenizer("java.lang.Unknown");
        parser.findClass(tokens.nextToken(), tokens);
    }
    
    @Test
    public void testProblem() {
        TypeParser.MyTokenizer tokens = parser.new MyTokenizer("input remaining");
        IllegalArgumentException e = parser._problem(tokens, "error message");
        
        assertNotNull(e);
        assertEquals("Failed to parse type 'input remaining' (remaining: ' remaining'): error message", e.getMessage());
    }
    
}