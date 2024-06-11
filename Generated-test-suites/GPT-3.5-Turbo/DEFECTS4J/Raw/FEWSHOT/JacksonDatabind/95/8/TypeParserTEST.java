
package com.fasterxml.jackson.databind.type;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.fasterxml.jackson.databind.type.TypeParser;

public class TypeParserTest {
    private TypeParser parser;
    
    @Before
    public void setUp() {
        TypeFactory factory = TypeFactory.defaultInstance();
        parser = new TypeParser(factory);
    }
    
    @Test
    public void testParse() {
        // Test valid types
        JavaType type1 = parser.parse("java.lang.String");
        assertEquals(String.class, type1.getRawClass());
        
        JavaType type2 = parser.parse("java.util.List<java.lang.Integer>");
        assertEquals(List.class, type2.getRawClass());
        assertTrue(type2.getBindings().isEmpty());
        assertEquals(1, type2.containedTypeCount());
        assertEquals(Integer.class, type2.containedType(0).getRawClass());
        
        JavaType type3 = parser.parse("java.util.Map<java.lang.String, java.util.List<java.lang.Integer>>");
        assertEquals(java.util.Map.class, type3.getRawClass());
        assertTrue(type3.getBindings().isEmpty());
        assertEquals(2, type3.containedTypeCount());
        assertEquals(String.class, type3.containedType(0).getRawClass());
        assertEquals(List.class, type3.containedType(1).getRawClass());
        assertTrue(type3.containedType(1).getBindings().isEmpty());
        assertEquals(1, type3.containedType(1).containedTypeCount());
        assertEquals(Integer.class, type3.containedType(1).containedType(0).getRawClass());
        
        // Test invalid types
        try {
            parser.parse("");
        } catch (IllegalArgumentException e) {
            assertEquals("Failed to parse type '' (remaining: ''): Unexpected end-of-string", e.getMessage());
        }
        
        try {
            parser.parse("java.util.List");
        } catch (IllegalArgumentException e) {
            assertEquals("Failed to parse type 'java.util.List' (remaining: 'java.util.List'): Unexpected end-of-string", e.getMessage());
        }
        
        try {
            parser.parse("java.util.List<>"); // Empty type arguments are not allowed
        } catch (IllegalArgumentException e) {
            assertEquals("Failed to parse type 'java.util.List<>' (remaining: 'java.util.List<>'): Unexpected token '>', expected ',' or '>')", e.getMessage());
        }
        
        try {
            parser.parse("java.util.List<java.util.Map<>>"); // Empty type arguments are not allowed
        } catch (IllegalArgumentException e) {
            assertEquals("Failed to parse type 'java.util.List<java.util.Map<>>' (remaining: 'java.util.List<java.util.Map<>>'): Failed to parse type 'java.util.Map<>' (remaining: 'java.util.Map<>'): Unexpected token '>', expected ',' or '>')", e.getMessage());
        }
        
        try {
            parser.parse("java.util.List<java.lang.Integer"); // Missing closing '>'
        } catch (IllegalArgumentException e) {
            assertEquals("Failed to parse type 'java.util.List<java.lang.Integer' (remaining: 'java.util.List<java.lang.Integer'): Unexpected end-of-string", e.getMessage());
        }
        
        try {
            parser.parse("java.util.List<java.lang.Integer>");
            parser.parse("java.lang.NoSuchClass"); // Non-existent class
        } catch (IllegalArgumentException e) {
            assertTrue(e.getCause() instanceof ClassNotFoundException);
            assertEquals("Failed to parse type 'java.lang.NoSuchClass' (remaining: 'java.lang.NoSuchClass'): Can not locate class 'java.lang.NoSuchClass', problem: java.lang.ClassNotFoundException: java.lang.NoSuchClass", e.getMessage());
        }
    }
    
    @Test
    public void testParseType() {
        TypeParser.MyTokenizer tokens = new TypeParser.MyTokenizer("java.util.List<java.lang.Integer>");
        JavaType type = parser.parseType(tokens);
        
        assertEquals(List.class, type.getRawClass());
        assertTrue(type.getBindings().isEmpty());
        assertEquals(1, type.containedTypeCount());
        assertEquals(Integer.class, type.containedType(0).getRawClass());
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testParseTypeWithNoTokens() {
        TypeParser.MyTokenizer tokens = new TypeParser.MyTokenizer("");
        parser.parseType(tokens);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testParseTypeWithInvalidTokens() {
        TypeParser.MyTokenizer tokens = new TypeParser.MyTokenizer("java.util.List<>"); // Empty type arguments are not allowed
        parser.parseType(tokens);
    }
    
    @Test
    public void testParseTypes() {
        TypeParser.MyTokenizer tokens = new TypeParser.MyTokenizer("java.lang.String,java.lang.Integer");
        List<JavaType> types = parser.parseTypes(tokens);
        
        assertEquals(2, types.size());
        assertEquals(String.class, types.get(0).getRawClass());
        assertEquals(Integer.class, types.get(1).getRawClass());
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testParseTypesWithNoTokens() {
        TypeParser.MyTokenizer tokens = new TypeParser.MyTokenizer("");
        parser.parseTypes(tokens);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testParseTypesWithInvalidTokens() {
        TypeParser.MyTokenizer tokens = new TypeParser.MyTokenizer("java.lang.String>"); // Missing comma separator
        parser.parseTypes(tokens);
    }
    
    @Test
    public void testFindClass() {
        TypeParser.MyTokenizer tokens = new TypeParser.MyTokenizer("java.lang.String");
        Class<?> clazz = parser.findClass("java.lang.String", tokens);
        
        assertEquals(String.class, clazz);
    }
    
    @Test
    public void testFindClassWithNonExistentClass() {
        TypeParser.MyTokenizer tokens = new TypeParser.MyTokenizer("java.lang.NoSuchClass");
        IllegalArgumentException e = parser._problem(tokens, "Can not locate class 'java.lang.NoSuchClass', problem: java.lang.ClassNotFoundException: java.lang.NoSuchClass");
        
        assertTrue(e.getCause() instanceof ClassNotFoundException);
        assertEquals("Failed to parse type 'java.lang.NoSuchClass' (remaining: 'java.lang.NoSuchClass'): Can not locate class 'java.lang.NoSuchClass', problem: java.lang.ClassNotFoundException: java.lang.NoSuchClass", e.getMessage());
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testProblem() {
        TypeParser.MyTokenizer tokens = new TypeParser.MyTokenizer("");
        parser._problem(tokens, "Unexpected end-of-string");
    }
    
    @Test
    public void testMyTokenizer() {
        TypeParser.MyTokenizer tokenizer = new TypeParser.MyTokenizer("java.lang.String");
        
        assertTrue(tokenizer.hasMoreTokens());
        assertEquals("java.lang.String", tokenizer.nextToken());
        assertTrue(!tokenizer.hasMoreTokens());
        
        assertEquals("java.lang.String", tokenizer.getAllInput());
        assertEquals("", tokenizer.getRemainingInput());
    }
    
    @Test
    public void testMyTokenizerWithPushBack() {
        TypeParser.MyTokenizer tokenizer = new TypeParser.MyTokenizer("java.lang.String");
        
        tokenizer.pushBack("java");
        assertTrue(tokenizer.hasMoreTokens());
        assertEquals("java", tokenizer.nextToken());
        assertTrue(tokenizer.hasMoreTokens());
        assertEquals(".", tokenizer.nextToken());
        assertTrue(tokenizer.hasMoreTokens());
        assertEquals("lang", tokenizer.nextToken());
        assertTrue(tokenizer.hasMoreTokens());
        assertEquals(".", tokenizer.nextToken());
        assertTrue(tokenizer.hasMoreTokens());
        assertEquals("String", tokenizer.nextToken());
        assertTrue(!tokenizer.hasMoreTokens());
        
        assertEquals("java.lang.String", tokenizer.getAllInput());
        assertEquals("java.lang.String", tokenizer.getRemainingInput());
    }
}
