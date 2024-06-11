package com.fasterxml.jackson.databind.type;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertNull;

import org.junit.Before;
import org.junit.Test;

public class TypeParserTest {
    
    private TypeParser typeParser;
    
    @Before
    public void setUp(){
        typeParser = new TypeParser(TypeFactory.defaultInstance());
    }
    
    @Test
    public void testParse() {
        JavaType javaType = typeParser.parse("java.util.List<java.lang.String>");

        assertEquals("java.util.List", javaType.getRawClass().getName());
        assertTrue(javaType.getBindings().isEmpty());
    }
    
    @Test
    public void testParse_EmptyString() {
        try {
            typeParser.parse("");
        } catch (IllegalArgumentException e) {
            assertEquals("Failed to parse type '' (remaining: ''): Unexpected end-of-string", e.getMessage());
        }
    }
    
    @Test
    public void testParse_EndOfString() {
        try {
            typeParser.parse("java.util.List<java.lang.String> unexpected");
        } catch (IllegalArgumentException e) {
            assertEquals("Failed to parse type 'java.util.List<java.lang.String> unexpected' (remaining: ' unexpected'): Unexpected tokens after complete type", e.getMessage());
        }
    }
    
    @Test
    public void testParseType() {
        TypeParser.MyTokenizer tokenizer = new TypeParser.MyTokenizer("java.util.List<java.lang.String>");
        JavaType javaType = typeParser.parseType(tokenizer);
        
        assertEquals("java.util.List", javaType.getRawClass().getName());
        assertTrue(javaType.getBindings().isEmpty());
    }
    
    @Test
    public void testParseType_UnexpectedEndOfString() {
        TypeParser.MyTokenizer tokenizer = new TypeParser.MyTokenizer("");
        
        try {
            typeParser.parseType(tokenizer);
        } catch (IllegalArgumentException e) {
            assertEquals("Failed to parse type '' (remaining: ''): Unexpected end-of-string", e.getMessage());
        }
    }
    
    @Test
    public void testParseType_Generic() {
        TypeParser.MyTokenizer tokenizer = new TypeParser.MyTokenizer("java.util.List");
        tokenizer.pushBack("<");
        tokenizer.pushBack("java.lang.String");
        
        JavaType javaType = typeParser.parseType(tokenizer);
        
        assertEquals("java.util.List", javaType.getRawClass().getName());
        assertTrue(javaType.getBindings().isEmpty());
    }
    
    @Test
    public void testParseTypes() {
        TypeParser.MyTokenizer tokenizer = new TypeParser.MyTokenizer("java.util.List<java.lang.String,java.lang.Integer>");
        
        try {
            typeParser.parseTypes(tokenizer);
        } catch (IllegalArgumentException e) {
            assertEquals("Failed to parse type 'java.util.List<java.lang.String,java.lang.Integer>' (remaining: ''): Unexpected end-of-string", e.getMessage());
        }
    }
    
    @Test
    public void testFindClass() {
        TypeParser.MyTokenizer tokenizer = new TypeParser.MyTokenizer("java.lang.String");
        Class<?> clazz = typeParser.findClass("java.lang.String", tokenizer);
        
        assertEquals("java.lang.String", clazz.getName());
    }
    
    @Test
    public void testFindClass_ClassNotFoundException() {
        TypeParser.MyTokenizer tokenizer = new TypeParser.MyTokenizer("java.lang.String");
        
        try {
            typeParser.findClass("com.example.NotFoundClass", tokenizer);
        } catch (IllegalArgumentException e) {
            assertEquals("Failed to parse type 'java.lang.String' (remaining: ''): Can not locate class 'com.example.NotFoundClass', problem: null", e.getMessage());
        }
    }
    
    @Test
    public void testFindClass_RuntimeException() {
        TypeParser.MyTokenizer tokenizer = new TypeParser.MyTokenizer("java.lang.String");
        
        try {
            typeParser.findClass("com.example.MyRuntimeException", tokenizer);
        } catch (RuntimeException e) {
            assertEquals("Failed to parse type 'java.lang.String' (remaining: ''): Can not locate class 'com.example.MyRuntimeException', problem: RuntimeException", e.getMessage());
        }
    }
    
    @Test
    public void testProblem() {
        TypeParser.MyTokenizer tokenizer = new TypeParser.MyTokenizer("java.lang.String");
        IllegalArgumentException exception = typeParser._problem(tokenizer, "Test problem");
        
        assertEquals("Failed to parse type 'java.lang.String' (remaining: ''): Test problem", exception.getMessage());
    }
    
    @Test
    public void testMyTokenizerHasMoreTokens() {
        TypeParser.MyTokenizer tokenizer = new TypeParser.MyTokenizer("java.util.List<java.lang.String>");
        
        assertTrue(tokenizer.hasMoreTokens());
    }
    
    @Test
    public void testMyTokenizerNextToken() {
        TypeParser.MyTokenizer tokenizer = new TypeParser.MyTokenizer("java.util.List<java.lang.String>");
        
        String token = tokenizer.nextToken();
        
        assertEquals("java.util.List", token);
        assertTrue(tokenizer.hasMoreTokens());
    }
    
    @Test
    public void testMyTokenizerPushBack() {
        TypeParser.MyTokenizer tokenizer = new TypeParser.MyTokenizer("java.util.List<java.lang.String>");
        
        String token = tokenizer.nextToken();
        tokenizer.pushBack(token);
        
        assertEquals("java.util.List", tokenizer.nextToken());
    }
    
    @Test
    public void testMyTokenizerEmptyString() {
        TypeParser.MyTokenizer tokenizer = new TypeParser.MyTokenizer("");
        
        assertTrue(tokenizer.hasMoreTokens());
        assertEquals("", tokenizer.nextToken());
        assertTrue(tokenizer.hasMoreTokens());
    }
    
    @Test
    public void testMyTokenizerStringWithDelimiter() {
        TypeParser.MyTokenizer tokenizer = new TypeParser.MyTokenizer("<,>");
        
        assertTrue(tokenizer.hasMoreTokens());
        assertEquals("<", tokenizer.nextToken());
        assertTrue(tokenizer.hasMoreTokens());
        assertEquals(",", tokenizer.nextToken());
        assertTrue(tokenizer.hasMoreTokens());
        assertEquals(">", tokenizer.nextToken());
        assertTrue(tokenizer.hasMoreTokens());
    }
    
    @Test
    public void testMyTokenizerGetAllInput() {
        TypeParser.MyTokenizer tokenizer = new TypeParser.MyTokenizer("java.util.List<java.lang.String>");
        
        assertEquals("java.util.List<java.lang.String>", tokenizer.getAllInput());
    }
    
    @Test
    public void testMyTokenizerGetRemainingInput() {
        TypeParser.MyTokenizer tokenizer = new TypeParser.MyTokenizer("java.util.List<java.lang.String>");
        
        tokenizer.nextToken();
        
        assertEquals("<java.lang.String>", tokenizer.getRemainingInput());
    }
}