package com.fasterxml.jackson.databind.type;

import static org.junit.Assert.*;

import org.junit.Test;

public class TypeParserTest {

    @Test
    public void testParse_validCanonicalString_returnsJavaType() {
        TypeParser parser = new TypeParser(new TypeFactory());
        String canonical = "java.util.List<com.fasterxml.jackson.databind.JavaType>";
        JavaType result = parser.parse(canonical);
        assertNotNull(result);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testParse_invalidCanonicalString_throwsIllegalArgumentException() {
        TypeParser parser = new TypeParser(new TypeFactory());
        String canonical = "invalid<com.fasterxml.jackson.databind.JavaType>";
        parser.parse(canonical);
    }
    
    @Test
    public void testParseType_endOfString_returnsJavaType() {
        TypeParser parser = new TypeParser(new TypeFactory());
        TypeParser.MyTokenizer tokens = parser.new MyTokenizer("com.fasterxml.jackson.databind.JavaType");
        JavaType result = parser.parseType(tokens);
        assertNotNull(result);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testParseType_invalidEndOfString_throwsIllegalArgumentException() {
        TypeParser parser = new TypeParser(new TypeFactory());
        TypeParser.MyTokenizer tokens = parser.new MyTokenizer("");
        parser.parseType(tokens);
    }
    
    @Test
    public void testParseType_withGenerics_returnsJavaType() {
        TypeParser parser = new TypeParser(new TypeFactory());
        TypeParser.MyTokenizer tokens = parser.new MyTokenizer("com.fasterxml.jackson.databind.JavaType");
        tokens.pushBack("<");
        tokens.pushBack("com.fasterxml.jackson.databind.JavaType");
        JavaType result = parser.parseType(tokens);
        assertNotNull(result);
    }
    
    @Test
    public void testParseTypes_multipleTypes_returnsListJavaType() {
        TypeParser parser = new TypeParser(new TypeFactory());
        TypeParser.MyTokenizer tokens = parser.new MyTokenizer("com.fasterxml.jackson.databind.JavaType, com.fasterxml.jackson.databind.JavaType");
        JavaType result = parser.parseTypes(tokens);
        assertNotNull(result);
        assertEquals(2, result.size());
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testParseTypes_invalidEndOfString_throwsIllegalArgumentException() {
        TypeParser parser = new TypeParser(new TypeFactory());
        TypeParser.MyTokenizer tokens = parser.new MyTokenizer("com.fasterxml.jackson.databind.JavaType");
        parser.parseTypes(tokens);
    }
    
    @Test
    public void testFindClass_validClassName_returnsClass() {
        TypeParser parser = new TypeParser(new TypeFactory());
        TypeParser.MyTokenizer tokens = parser.new MyTokenizer("java.util.List");
        Class<?> result = parser.findClass(tokens.nextToken(), tokens);
        assertNotNull(result);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testFindClass_invalidClassName_throwsIllegalArgumentException() {
        TypeParser parser = new TypeParser(new TypeFactory());
        TypeParser.MyTokenizer tokens = parser.new MyTokenizer("invalid.class.Name");
        parser.findClass(tokens.nextToken(), tokens);
    }
}