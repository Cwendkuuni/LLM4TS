package com.fasterxml.jackson.databind.type;

import static org.junit.Assert.*;
import org.junit.Test;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.TypeFactory;

public class TypeParserTest {
    
    @Test
    public void testParse() {
        TypeFactory typeFactory = new TypeFactory();
        TypeParser typeParser = new TypeParser(typeFactory);
        
        JavaType javaType = typeParser.parse("java.lang.String");
        assertEquals(typeFactory.constructType(String.class), javaType);
        
        javaType = typeParser.parse("java.util.List<java.lang.Integer>");
        assertEquals(typeFactory.constructParametricType(List.class, Integer.class), javaType);
        
        javaType = typeParser.parse("java.util.Map<java.lang.String, java.lang.Integer>");
        assertEquals(typeFactory.constructParametricType(Map.class, String.class, Integer.class), javaType);
        
        try {
            javaType = typeParser.parse("java.lang.");
            fail();
        } catch (IllegalArgumentException e) {
            assertEquals("Failed to parse type 'java.lang.' (remaining: ''): Unexpected end-of-string", e.getMessage());
        }
    }
    
    @Test
    public void testParseType() {
        TypeFactory typeFactory = new TypeFactory();
        TypeParser typeParser = new TypeParser(typeFactory);
        TypeParser.MyTokenizer tokenizer = typeParser.new MyTokenizer("java.lang.String");
        
        Class<?> base = typeParser.findClass("java.lang.String", tokenizer);
        assertEquals(String.class, base);
        
        tokenizer.pushBack(">");
        tokenizer.pushBack(",");
        
        JavaType javaType = typeParser.parseType(tokenizer);
        assertEquals(typeFactory.constructType(String.class), javaType);
        
        tokenizer = typeParser.new MyTokenizer("java.lang.List<java.lang.Integer>");
        tokenizer.pushBack("<");
        tokenizer.pushBack(",");
        tokenizer.pushBack("java.lang.Integer");
        
        javaType = typeParser.parseType(tokenizer);
        assertEquals(typeFactory.constructParametricType(List.class, Integer.class), javaType);
        
        tokenizer = typeParser.new MyTokenizer("java.lang.Map<java.lang.String, java.lang.Integer>");
        tokenizer.pushBack("<");
        tokenizer.pushBack(",");
        tokenizer.pushBack("java.lang.Integer");
        tokenizer.pushBack(",");
        tokenizer.pushBack("java.lang.String");
        
        javaType = typeParser.parseType(tokenizer);
        assertEquals(typeFactory.constructParametricType(Map.class, String.class, Integer.class), javaType);
        
        tokenizer = typeParser.new MyTokenizer("");
        try {
            javaType = typeParser.parseType(tokenizer);
            fail();
        } catch (IllegalArgumentException e) {
            assertEquals("Failed to parse type '' (remaining: ''): Unexpected end-of-string", e.getMessage());
        }
    }
    
    @Test
    public void testParseTypes() {
        TypeFactory typeFactory = new TypeFactory();
        TypeParser typeParser = new TypeParser(typeFactory);
        TypeParser.MyTokenizer tokenizer = typeParser.new MyTokenizer("java.lang.String");
        
        ArrayList<JavaType> types = new ArrayList<JavaType>();
        types.add(typeFactory.constructType(String.class));
        
        List<JavaType> javaTypes = typeParser.parseTypes(tokenizer);
        assertEquals(types, javaTypes);
        
        tokenizer = typeParser.new MyTokenizer("java.lang.List<java.lang.Integer>");
        tokenizer.pushBack(">");
        tokenizer.pushBack(",");
        
        types = new ArrayList<JavaType>();
        types.add(typeFactory.constructParametricType(List.class, Integer.class));
        
        javaTypes = typeParser.parseTypes(tokenizer);
        assertEquals(types, javaTypes);
        
        tokenizer = typeParser.new MyTokenizer("");
        try {
            javaTypes = typeParser.parseTypes(tokenizer);
            fail();
        } catch (IllegalArgumentException e) {
            assertEquals("Failed to parse type '' (remaining: ''): Unexpected end-of-string", e.getMessage());
        }
    }
    
    @Test
    public void testFindClass() {
        TypeFactory typeFactory = new TypeFactory();
        TypeParser typeParser = new TypeParser(typeFactory);
        TypeParser.MyTokenizer tokenizer = typeParser.new MyTokenizer("java.lang.String");
        
        Class<?> base = typeParser.findClass("java.lang.String", tokenizer);
        assertEquals(String.class, base);
        
        tokenizer = typeParser.new MyTokenizer("invalidClass");
        try {
            base = typeParser.findClass("invalidClass", tokenizer);
            fail();
        } catch (IllegalArgumentException e) {
            assertEquals("Can not locate class 'invalidClass', problem: null", e.getMessage());
        }
    }
    
    @Test
    public void testProblem() {
        TypeFactory typeFactory = new TypeFactory();
        TypeParser typeParser = new TypeParser(typeFactory);
        TypeParser.MyTokenizer tokenizer = typeParser.new MyTokenizer("java.lang.String");
        
        try {
            IllegalArgumentException exception = typeParser._problem(tokenizer, "Error Message");
            assertEquals("Failed to parse type 'java.lang.String' (remaining: ''): Error Message", exception.getMessage());
        } catch (IllegalArgumentException e) {
            fail();
        }
    }
    
    @Test
    public void testMyTokenizer_hasMoreTokens() {
        TypeFactory typeFactory = new TypeFactory();
        TypeParser typeParser = new TypeParser(typeFactory);
        TypeParser.MyTokenizer tokenizer = typeParser.new MyTokenizer("java.lang.String");
        
        assertTrue(tokenizer.hasMoreTokens());
        
        tokenizer.nextToken();
        assertTrue(tokenizer.hasMoreTokens());
        
        tokenizer.pushBack(">");
        assertTrue(tokenizer.hasMoreTokens());
        
        tokenizer.nextToken();
        assertFalse(tokenizer.hasMoreTokens());
    }
    
    @Test
    public void testMyTokenizer_nextToken() {
        TypeFactory typeFactory = new TypeFactory();
        TypeParser typeParser = new TypeParser(typeFactory);
        TypeParser.MyTokenizer tokenizer = typeParser.new MyTokenizer("java.lang.String");
        
        String token = tokenizer.nextToken();
        assertEquals("java.lang.String", token);
        
        token = tokenizer.nextToken();
        assertEquals("", token);
        
        tokenizer.pushBack(" ");
        token = tokenizer.nextToken();
        assertEquals("", token);
    }
    
    @Test
    public void testMyTokenizer_pushBack() {
        TypeFactory typeFactory = new TypeFactory();
        TypeParser typeParser = new TypeParser(typeFactory);
        TypeParser.MyTokenizer tokenizer = typeParser.new MyTokenizer("java.lang.String");
        
        String token = tokenizer.nextToken();
        assertEquals("java.lang.String", token);
        
        tokenizer.pushBack("pushedBackToken");
        token = tokenizer.nextToken();
        assertEquals("pushedBackToken", token);
    }
    
    @Test
    public void testMyTokenizer_getAllInput() {
        TypeFactory typeFactory = new TypeFactory();
        TypeParser typeParser = new TypeParser(typeFactory);
        TypeParser.MyTokenizer tokenizer = typeParser.new MyTokenizer("java.lang.String");
        
        String input = tokenizer.getAllInput();
        assertEquals("java.lang.String", input);
    }
    
    @Test
    public void testMyTokenizer_getRemainingInput() {
        TypeFactory typeFactory = new TypeFactory();
        TypeParser typeParser = new TypeParser(typeFactory);
        TypeParser.MyTokenizer tokenizer = typeParser.new MyTokenizer("java.lang.String");
        
        String input = tokenizer.getRemainingInput();
        assertEquals("java.lang.String", input);
        
        tokenizer.nextToken();
        
        input = tokenizer.getRemainingInput();
        assertEquals("", input);
    }
    
}