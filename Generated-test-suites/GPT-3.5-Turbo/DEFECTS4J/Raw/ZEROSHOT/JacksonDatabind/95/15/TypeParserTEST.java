package com.fasterxml.jackson.databind.type;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.TypeParser.MyTokenizer;

public class TypeParserTest {
    private TypeParser typeParser;
    
    @Before
    public void setUp() {
        typeParser = new TypeParser(null);
    }
    
    @Test
    public void testParse() throws IllegalArgumentException {
        String canonical = "java.util.List<java.lang.String>";
        JavaType result = typeParser.parse(canonical);
        
        assertNotNull(result);
        assertEquals("java.util.List", result.getRawClass().getName());
        assertEquals(1, result.containedTypeCount());
        assertEquals("java.lang.String", result.containedType(0).getRawClass().getName());
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testParse_InvalidSyntax() throws IllegalArgumentException {
        String canonical = "java.util.List<java.lang.String";
        typeParser.parse(canonical);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testParse_UnexpectedTokensAfterCompleteType() throws IllegalArgumentException {
        String canonical = "java.util.List<java.lang.String> extraTokens";
        typeParser.parse(canonical);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testParseType_UnexpectedEndOfString() throws IllegalArgumentException {
        MyTokenizer tokens = typeParser.new MyTokenizer("");
        typeParser.parseType(tokens);
    }
    
    @Test
    public void testParseType_NonGenericType() throws IllegalArgumentException {
        MyTokenizer tokens = typeParser.new MyTokenizer("java.util.List");
        JavaType result = typeParser.parseType(tokens);
        
        assertNotNull(result);
        assertEquals("java.util.List", result.getRawClass().getName());
        assertEquals(0, result.containedTypeCount());
    }
    
    @Test
    public void testParseType_GenericType() throws IllegalArgumentException {
        MyTokenizer tokens = typeParser.new MyTokenizer("java.util.List<java.lang.String>");
        JavaType result = typeParser.parseType(tokens);
        
        assertNotNull(result);
        assertEquals("java.util.List", result.getRawClass().getName());
        assertEquals(1, result.containedTypeCount());
        assertEquals("java.lang.String", result.containedType(0).getRawClass().getName());
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testParseType_UnexpectedTokensAfterCompleteType() throws IllegalArgumentException {
        MyTokenizer tokens = typeParser.new MyTokenizer("java.util.List<java.lang.String>>");
        typeParser.parseType(tokens);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testParseType_UnexpectedEndOfString() throws IllegalArgumentException {
        MyTokenizer tokens = typeParser.new MyTokenizer("java.util.List<java.lang.String");
        typeParser.parseType(tokens);
    }
    
    @Test
    public void testParseTypes() throws IllegalArgumentException {
        MyTokenizer tokens = typeParser.new MyTokenizer("java.util.List<java.lang.String>,java.util.Map<java.lang.Integer,java.lang.String>");
        List<JavaType> result = typeParser.parseTypes(tokens);
        
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("java.util.List", result.get(0).getRawClass().getName());
        assertEquals("java.lang.String", result.get(0).containedType(0).getRawClass().getName());
        assertEquals("java.util.Map", result.get(1).getRawClass().getName());
        assertEquals("java.lang.Integer", result.get(1).containedType(0).getRawClass().getName());
        assertEquals("java.lang.String", result.get(1).containedType(1).getRawClass().getName());
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testParseTypes_UnexpectedToken() throws IllegalArgumentException {
        MyTokenizer tokens = typeParser.new MyTokenizer("java.util.List<java.lang.String,java.util.Map>");
        typeParser.parseTypes(tokens);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testParseTypes_UnexpectedEndOfString() throws IllegalArgumentException {
        MyTokenizer tokens = typeParser.new MyTokenizer("java.util.List<java.lang.String");
        typeParser.parseTypes(tokens);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testFindClass_ClassNotFoundException() {
        MyTokenizer tokens = typeParser.new MyTokenizer("com.example.InvalidClass");
        typeParser.findClass("com.example.InvalidClass", tokens);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void test_findClass_CannotLocateClass() {
        MyTokenizer tokens = typeParser.new MyTokenizer("com.example.InvalidClass");
        typeParser.findClass("com.example.InvalidClass", tokens);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testFindClass_RuntimeException() {
        MyTokenizer tokens = typeParser.new MyTokenizer("com.example.RuntimeExceptionClass");
        typeParser.findClass("com.example.RuntimeExceptionClass", tokens);
    }
    
    @Test
    public void test_problem() {
        MyTokenizer tokens = typeParser.new MyTokenizer("input", "remaining");
        IllegalArgumentException exception = typeParser._problem(tokens, "failed");
        
        assertNotNull(exception);
        assertEquals("Failed to parse type 'input' (remaining: 'remaining'): failed", exception.getMessage());
    }
    
    @Test
    public void testMyTokenizer_HasMoreTokens() {
        MyTokenizer tokens = typeParser.new MyTokenizer("a, b, c");
        assertTrue(tokens.hasMoreTokens());
        tokens.nextToken();
        assertTrue(tokens.hasMoreTokens());
        tokens.nextToken();
        assertTrue(tokens.hasMoreTokens());
        tokens.nextToken();
        assertFalse(tokens.hasMoreTokens());
    }
    
    @Test
    public void testMyTokenizer_NextToken() {
        MyTokenizer tokens = typeParser.new MyTokenizer("a, b, c");
        assertEquals("a", tokens.nextToken());
        assertEquals("b", tokens.nextToken());
        assertEquals("c", tokens.nextToken());
    }
    
    @Test
    public void testMyTokenizer_PushBack() {
        MyTokenizer tokens = typeParser.new MyTokenizer("a, b, c");
        tokens.pushBack("pushback");
        
        assertEquals("pushback", tokens.nextToken());
        assertEquals("b", tokens.nextToken());
        assertEquals("c", tokens.nextToken());
    }
    
    @Test
    public void testMyTokenizer_getAllInput() {
        MyTokenizer tokens = typeParser.new MyTokenizer("input");
        assertEquals("input", tokens.getAllInput());
    }
    
    @Test
    public void testMyTokenizer_getRemainingInput() {
        MyTokenizer tokens = typeParser.new MyTokenizer("input", "remaining");
        assertEquals("remaining", tokens.getRemainingInput());
    }
}