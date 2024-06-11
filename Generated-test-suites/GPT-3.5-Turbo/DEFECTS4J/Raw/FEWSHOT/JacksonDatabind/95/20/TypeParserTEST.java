package com.fasterxml.jackson.databind.type;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

public class TypeParserTest {
    
    private TypeParser parser = new TypeParser(new TypeFactory());
    
    @Test
    public void testParse() {
        JavaType javaType = parser.parse("java.lang.String");
        assertNotNull(javaType);
        assertEquals(String.class, javaType.getRawClass());
    }
    
    @Test
    public void testParseWithTypeArguments() {
        JavaType javaType = parser.parse("java.util.List<java.lang.String>");
        assertNotNull(javaType);
        assertEquals(List.class, javaType.getRawClass());
        assertEquals(String.class, javaType.getContentType().getRawClass());
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testParseInvalidType() {
        parser.parse("java.lang.InvalidType");
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testParseInvalidTypeArguments() {
        parser.parse("java.util.InvalidType<java.lang.String>");
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testParseInvalidSyntax() {
        parser.parse("java.util.List<java.lang.String,");
    }
    
    @Test
    public void testParseEmptyString() {
        JavaType javaType = parser.parse("");
        assertNull(javaType);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testParseInvalidEndOfString() {
        parser.parse("java.lang.String unexpectedToken");
    }
    
    @Test
    public void testParseSyntaxWithWhiteSpace() {
        JavaType javaType = parser.parse("  java.lang.String  ");
        assertNotNull(javaType);
        assertEquals(String.class, javaType.getRawClass());
    }
    
    @Test
    public void testParseTypeArgumentsWithWhiteSpace() {
        JavaType javaType = parser.parse("java.util.List<  java.lang.String  >");
        assertNotNull(javaType);
        assertEquals(List.class, javaType.getRawClass());
        assertEquals(String.class, javaType.getContentType().getRawClass());
    }
    
    @Test
    public void testWithFactory() {
        TypeFactory factory = new TypeFactory();
        TypeParser newParser = parser.withFactory(factory);
        assertNotNull(newParser);
        assertEquals(factory, newParser._factory);
    }
    
    @Test
    public void testFindClass() {
        Class<?> clazz = parser.findClass("java.lang.String", new TypeParser.MyTokenizer("java.lang.String"));
        assertNotNull(clazz);
        assertEquals(String.class, clazz);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testFindClassNotFound() {
        parser.findClass("java.lang.InvalidType", new TypeParser.MyTokenizer("java.lang.InvalidType"));
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testFindClassException() {
        parser.findClass("java.lang.Exception", new TypeParser.MyTokenizer("java.lang.Exception"));
    }
    
    @Test
    public void testProblem() {
        IllegalArgumentException exception = parser._problem(new TypeParser.MyTokenizer("input"), "message");
        assertNotNull(exception);
        assertEquals("Failed to parse type 'input' (remaining: 'input'): message", exception.getMessage());
    }
    
    @Test
    public void testMyTokenizer() {
        TypeParser.MyTokenizer tokenizer = new TypeParser.MyTokenizer("token1, token2");
        assertNotNull(tokenizer);
        assertEquals("token1, token2", tokenizer.getAllInput());
        assertEquals("token1", tokenizer.nextToken());
        assertEquals("token2", tokenizer.nextToken());
        assertEquals("", tokenizer.getRemainingInput());
    }
    
    @Test
    public void testMyTokenizerWithPushBack() {
        TypeParser.MyTokenizer tokenizer = new TypeParser.MyTokenizer("token1, token2");
        assertNotNull(tokenizer);
        assertEquals("token1, token2", tokenizer.getAllInput());
        assertEquals("token1", tokenizer.nextToken());
        tokenizer.pushBack("token3");
        assertEquals("token3", tokenizer.nextToken());
        assertEquals("token2", tokenizer.nextToken());
        assertEquals("", tokenizer.getRemainingInput());
    }
    
    @Test
    public void testMyTokenizerWithWhiteSpace() {
        TypeParser.MyTokenizer tokenizer = new TypeParser.MyTokenizer(" token1 , token2 ");
        assertNotNull(tokenizer);
        assertEquals(" token1 , token2 ", tokenizer.getAllInput());
        assertEquals("token1", tokenizer.nextToken());
        assertEquals("token2", tokenizer.nextToken());
        assertEquals("", tokenizer.getRemainingInput());
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testMyTokenizerInvalidRemainingInput() {
        TypeParser.MyTokenizer tokenizer = new TypeParser.MyTokenizer("token");
        assertNotNull(tokenizer);
        assertEquals("token", tokenizer.getAllInput());
        assertEquals("token", tokenizer.nextToken());
        assertEquals("", tokenizer.getRemainingInput());
        tokenizer.nextToken();
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testMyTokenizerUnexpectedComma() {
        TypeParser.MyTokenizer tokenizer = new TypeParser.MyTokenizer("token1, token2");
        assertNotNull(tokenizer);
        assertEquals("token1, token2", tokenizer.getAllInput());
        assertEquals("token1", tokenizer.nextToken());
        tokenizer.nextToken();
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testMyTokenizerUnexpectedToken() {
        TypeParser.MyTokenizer tokenizer = new TypeParser.MyTokenizer("token1, token2");
        assertNotNull(tokenizer);
        assertEquals("token1, token2", tokenizer.getAllInput());
        assertEquals("token1", tokenizer.nextToken());
        assertEquals("token2", tokenizer.nextToken());
        assertEquals("", tokenizer.getRemainingInput());
        tokenizer.nextToken();
    }
}