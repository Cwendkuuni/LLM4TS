package com.fasterxml.jackson.databind.type;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.TypeFactory;

public class TypeParserTest {
    
    private TypeParser parser;
    
    @Before
    public void setup() {
        parser = new TypeParser(TypeFactory.defaultInstance());
    }
    
    @Test
    public void testParse() throws IllegalArgumentException {
        String canonical = "java.util.List<java.lang.String>";
        JavaType result = parser.parse(canonical);
        assertTrue(result.isCollectionLikeType());
        assertEquals(List.class, result.getRawClass());
        assertEquals(String.class, result.getContentType().getRawClass());
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testParseWithUnexpectedTokens() throws IllegalArgumentException {
        String canonical = "java.util.List<java.lang.String>,java.util.Map<java.lang.String,java.lang.Integer>";
        parser.parse(canonical);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testParseWithUnexpectedEndOfString() throws IllegalArgumentException {
        String canonical = "java.util.List<java.lang.String";
        parser.parse(canonical);
    }
    
    @Test
    public void testParseType() throws IllegalArgumentException {
        TypeParser.MyTokenizer tokens = new TypeParser.MyTokenizer("java.util.List<java.lang.String>");
        JavaType result = parser.parseType(tokens);
        assertTrue(result.isCollectionLikeType());
        assertEquals(List.class, result.getRawClass());
        assertEquals(String.class, result.getContentType().getRawClass());
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testParseTypeWithUnexpectedEndOfString() throws IllegalArgumentException {
        TypeParser.MyTokenizer tokens = new TypeParser.MyTokenizer("java.util.List<java.lang.String");
        parser.parseType(tokens);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testParseTypeWithUnexpectedToken() throws IllegalArgumentException {
        TypeParser.MyTokenizer tokens = new TypeParser.MyTokenizer("java.util.List<java.lang.String,");
        parser.parseType(tokens);
    }
    
    @Test
    public void testParseTypes() throws IllegalArgumentException {
        TypeParser.MyTokenizer tokens = new TypeParser.MyTokenizer("java.util.List<java.lang.String>,java.util.Map<java.lang.String,java.lang.Integer>");
        List<JavaType> result = parser.parseTypes(tokens);
        assertEquals(2, result.size());
        assertTrue(result.get(0).isCollectionLikeType());
        assertEquals(List.class, result.get(0).getRawClass());
        assertEquals(String.class, result.get(0).getContentType().getRawClass());
        assertFalse(result.get(1).isCollectionLikeType());
        assertEquals(java.util.Map.class, result.get(1).getRawClass());
        assertEquals(String.class, result.get(1).getKeyType().getRawClass());
        assertEquals(Integer.class, result.get(1).getContentType().getRawClass());
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testParseTypesWithUnexpectedEndOfString() throws IllegalArgumentException {
        TypeParser.MyTokenizer tokens = new TypeParser.MyTokenizer("java.util.List<java.lang.String>,java.util.Map<java.lang.String,java.lang.Integer");
        parser.parseTypes(tokens);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testParseTypesWithUnexpectedToken() throws IllegalArgumentException {
        TypeParser.MyTokenizer tokens = new TypeParser.MyTokenizer("java.util.List<java.lang.String>$java.util.Map<java.lang.String,java.lang.Integer>");
        parser.parseTypes(tokens);
    }
    
    @Test
    public void testFindClass() {
        TypeParser.MyTokenizer tokens = new TypeParser.MyTokenizer("java.util.List");
        Class<?> result = parser.findClass("java.util.List", tokens);
        assertEquals(List.class, result);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testFindClassWithError() {
        TypeParser.MyTokenizer tokens = new TypeParser.MyTokenizer("java.util.List");
        parser.findClass("java.util.List", tokens);
    }
    
    @Test
    public void testProblem() {
        TypeParser.MyTokenizer tokens = new TypeParser.MyTokenizer("java.util.List<java.lang.String>");
        IllegalArgumentException result = parser._problem(tokens, "Unexpected tokens after complete type");
        assertTrue(result.getMessage().startsWith("Failed to parse type 'java.util.List<java.lang.String>'"));
    }

    @Test
    public void testMyTokenizerHasMoreTokens() {
        TypeParser.MyTokenizer tokens = new TypeParser.MyTokenizer("java.util.List<java.lang.String>");
        assertTrue(tokens.hasMoreTokens());
        tokens.nextToken();
        assertFalse(tokens.hasMoreTokens());
    }
    
    @Test
    public void testMyTokenizerNextToken() {
        TypeParser.MyTokenizer tokens = new TypeParser.MyTokenizer("java.util.List<java.lang.String>");
        assertEquals("java.util.List", tokens.nextToken());
        assertEquals("java.lang.String", tokens.nextToken());
    }
    
    @Test
    public void testMyTokenizerPushBack() {
        TypeParser.MyTokenizer tokens = new TypeParser.MyTokenizer("java.util.List<java.lang.String>");
        tokens.pushBack("java.util.Map");
        assertEquals("java.util.Map", tokens.nextToken());
    }
    
    @Test
    public void testMyTokenizerGetAllInput() {
        TypeParser.MyTokenizer tokens = new TypeParser.MyTokenizer("java.util.List<java.lang.String>");
        assertEquals("java.util.List<java.lang.String>", tokens.getAllInput());
    }
    
    @Test
    public void testMyTokenizerGetRemainingInput() {
        TypeParser.MyTokenizer tokens = new TypeParser.MyTokenizer("java.util.List<java.lang.String>");
        tokens.nextToken();
        assertEquals("<java.lang.String>", tokens.getRemainingInput());
    }
}