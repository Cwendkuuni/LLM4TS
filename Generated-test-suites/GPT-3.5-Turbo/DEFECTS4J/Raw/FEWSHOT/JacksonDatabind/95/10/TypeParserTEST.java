
package com.fasterxml.jackson.databind.type;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.fasterxml.jackson.databind.type.TypeParser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

public class TypeParserTest {
    private TypeParser typeParser;
    
    @Before
    public void setup() {
        TypeFactory typeFactory = new TypeFactory();
        typeParser = new TypeParser(typeFactory);
    }
    
    @Test
    public void testParse() {
        JavaType javaType = typeParser.parse("java.lang.String");
        assertEquals("java.lang.String", javaType.toString());
        
        javaType = typeParser.parse("java.util.List<java.lang.String>");
        assertEquals("java.util.List<java.lang.String>", javaType.toString());
        
        javaType = typeParser.parse("java.util.List<com.fasterxml.jackson.databind.JavaType>");
        assertEquals("java.util.List<com.fasterxml.jackson.databind.JavaType>", javaType.toString());
        
        javaType = typeParser.parse("com.fasterxml.jackson.databind.JavaType<?>[]");
        assertEquals("com.fasterxml.jackson.databind.JavaType<?>[]", javaType.toString());
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testParseWithInvalidType() {
        typeParser.parse("InvalidType");
    }
    
    @Test
    public void testWithFactory() {
        TypeParser newTypeParser = typeParser.withFactory(new TypeFactory());
        assertFalse(newTypeParser == typeParser);
    }
    
    @Test
    public void testParseType() {
        TypeParser.MyTokenizer tokenizer = new TypeParser.MyTokenizer("java.lang.String");
        JavaType javaType = typeParser.parseType(tokenizer);
        assertEquals("java.lang.String", javaType.toString());
        
        tokenizer = new TypeParser.MyTokenizer("java.util.List<java.lang.String>");
        javaType = typeParser.parseType(tokenizer);
        assertEquals("java.util.List<java.lang.String>", javaType.toString());
        
        tokenizer = new TypeParser.MyTokenizer("java.util.List<com.fasterxml.jackson.databind.JavaType>");
        javaType = typeParser.parseType(tokenizer);
        assertEquals("java.util.List<com.fasterxml.jackson.databind.JavaType>", javaType.toString());
        
        tokenizer = new TypeParser.MyTokenizer("com.fasterxml.jackson.databind.JavaType<?>[]");
        javaType = typeParser.parseType(tokenizer);
        assertEquals("com.fasterxml.jackson.databind.JavaType<?>[]", javaType.toString());
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testParseTypeWithInvalidToken() {
        TypeParser.MyTokenizer tokenizer = new TypeParser.MyTokenizer("java.lang.String invalidToken");
        typeParser.parseType(tokenizer);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testParseTypeWithEndOfStringInput() {
        TypeParser.MyTokenizer tokenizer = new TypeParser.MyTokenizer("");
        typeParser.parseType(tokenizer);
    }
    
    @Test
    public void testParseTypes() {
        TypeParser.MyTokenizer tokenizer = new TypeParser.MyTokenizer("java.lang.String,java.util.List<java.lang.Integer>");
        List<JavaType> types = typeParser.parseTypes(tokenizer);
        assertEquals(2, types.size());
        assertEquals("java.lang.String", types.get(0).toString());
        assertEquals("java.util.List<java.lang.Integer>", types.get(1).toString());
        
        tokenizer = new TypeParser.MyTokenizer("java.lang.String,java.util.List<com.fasterxml.jackson.databind.JavaType>");
        types = typeParser.parseTypes(tokenizer);
        assertEquals(2, types.size());
        assertEquals("java.lang.String", types.get(0).toString());
        assertEquals("java.util.List<com.fasterxml.jackson.databind.JavaType>", types.get(1).toString());
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testParseTypesWithInvalidToken() {
        TypeParser.MyTokenizer tokenizer = new TypeParser.MyTokenizer("java.lang.String,java.util.List invalidToken");
        typeParser.parseTypes(tokenizer);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testParseTypesWithEndOfStringInput() {
        TypeParser.MyTokenizer tokenizer = new TypeParser.MyTokenizer("");
        typeParser.parseTypes(tokenizer);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testParseTypesWithUnexpectedToken() {
        TypeParser.MyTokenizer tokenizer = new TypeParser.MyTokenizer("java.lang.String,java.util.List<java.lang.Integer> invalidToken");
        typeParser.parseTypes(tokenizer);
    }
    
    @Test
    public void testFindClass() {
        TypeParser.MyTokenizer tokenizer = new TypeParser.MyTokenizer("java.lang.String");
        Class<?> javaClass = typeParser.findClass("java.lang.String", tokenizer);
        assertEquals(String.class, javaClass);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testFindClassWithInvalidClassName() {
        TypeParser.MyTokenizer tokenizer = new TypeParser.MyTokenizer("invalidClassName");
        typeParser.findClass("invalidClassName", tokenizer);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testFindClassWithException() {
        TypeParser.MyTokenizer tokenizer = new TypeParser.MyTokenizer("exception");
        typeParser.findClass("exception", tokenizer);
    }
    
    @Test
    public void testProblem() {
        TypeParser.MyTokenizer tokenizer = new TypeParser.MyTokenizer("java.lang.String");
        IllegalArgumentException exception = typeParser._problem(tokenizer, "Problem message");
        assertEquals("Failed to parse type 'java.lang.String' (remaining: ''): Problem message", exception.getMessage());
    }

    @Test
    public void testExtendsObject() {
        JavaType javaType = typeParser.parse("?");
        assertTrue(javaType.isTypeOrSubTypeOf(Object.class));
    }
}
