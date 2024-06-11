package com.fasterxml.jackson.databind.type;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Before;
import org.junit.Test;

public class TypeParserTest {

    private TypeParser parser;

    @Before
    public void setUp() {
        parser = new TypeParser(new TypeFactory());
    }

    @Test
    public void testParse() {
        String canonical = "java.util.List";
        JavaType type = parser.parse(canonical);
        assertEquals("java.util.List", type.getRawClass().getName());
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testParseInvalidCanonical() {
        String canonical = "InvalidCanonical";
        parser.parse(canonical);
    }
    
    @Test
    public void testParseType() {
        String typeStr = "java.util.List<String>";
        TypeParser.MyTokenizer tokens = parser.new MyTokenizer(typeStr);
        JavaType type = parser.parseType(tokens);
        assertEquals("java.util.List", type.getRawClass().getName());
        assertNull(tokens.nextToken());
    }
    
    @Test
    public void testParseTypeWithNoGenerics() {
        String typeStr = "java.lang.String";
        TypeParser.MyTokenizer tokens = parser.new MyTokenizer(typeStr);
        JavaType type = parser.parseType(tokens);
        assertEquals("java.lang.String", type.getRawClass().getName());
        assertNull(tokens.nextToken());
    }
    
    @Test
    public void testParseTypeWithMultipleGenerics() {
        String typeStr = "java.util.Map<java.lang.String, java.lang.Integer>";
        TypeParser.MyTokenizer tokens = parser.new MyTokenizer(typeStr);
        JavaType type = parser.parseType(tokens);
        assertEquals("java.util.Map", type.getRawClass().getName());
        assertNull(tokens.nextToken());
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testParseTypeInvalidToken() {
        String typeStr = "java.lang.String<>";
        TypeParser.MyTokenizer tokens = parser.new MyTokenizer(typeStr);
        parser.parseType(tokens);
    }
    
    @Test
    public void testParseTypes() {
        String typesStr = "java.util.List<java.lang.String>, java.util.Map<java.lang.String, java.lang.Integer>";
        TypeParser.MyTokenizer tokens = parser.new MyTokenizer(typesStr);
        parser.parseTypes(tokens);
        assertNull(tokens.nextToken());
    }
    
    @Test
    public void testParseTypesWithNoGenerics() {
        String typesStr = "java.lang.String";
        TypeParser.MyTokenizer tokens = parser.new MyTokenizer(typesStr);
        parser.parseTypes(tokens);
        assertNull(tokens.nextToken());
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testParseTypesInvalidToken() {
        String typesStr = "java.util.List<String, Integer>";
        TypeParser.MyTokenizer tokens = parser.new MyTokenizer(typesStr);
        parser.parseTypes(tokens);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testFindClass() {
        String className = "InvalidClass";
        TypeParser.MyTokenizer tokens = parser.new MyTokenizer("");
        parser.findClass(className, tokens);
    }
    
    @Test
    public void testProblem() {
        String msg = "Invalid type";
        TypeParser.MyTokenizer tokens = parser.new MyTokenizer("");
        IllegalArgumentException exception = parser._problem(tokens, msg);
        assertEquals(String.format("Failed to parse type '', (remaining: ''): %s", msg), exception.getMessage());
    }

}