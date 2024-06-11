package com.fasterxml.jackson.databind.type;

import org.junit.Test;
import static org.junit.Assert.*;

public class TypeParserTest {

    @Test
    public void testParse() {
        TypeFactory factory = new TypeFactory();
        TypeParser parser = new TypeParser(factory);
        
        // Test case 1
        String canonical1 = "java.util.List<java.lang.String>";
        JavaType result1 = parser.parse(canonical1);
        JavaType expected1 = factory._fromClass(null, List.class, TypeBindings.create(String.class));
        assertEquals(expected1, result1);
        
        // Test case 2
        String canonical2 = "java.util.Map<java.lang.Integer, java.lang.String>";
        JavaType result2 = parser.parse(canonical2);
        JavaType expected2 = factory._fromClass(null, Map.class, TypeBindings.create(Integer.class, String.class));
        assertEquals(expected2, result2);
        
        // Test case 3
        String canonical3 = "com.example.MyClass";
        JavaType result3 = parser.parse(canonical3);
        JavaType expected3 = factory._fromClass(null, com.example.MyClass.class, null);
        assertEquals(expected3, result3);
        
        // Test case 4
        String canonical4 = "java.util.Set<java.util.List<java.lang.String>>";
        JavaType result4 = parser.parse(canonical4);
        JavaType expected4 = factory._fromClass(null, Set.class, TypeBindings.create(factory._fromClass(null, List.class, TypeBindings.create(String.class))));
        assertEquals(expected4, result4);
    }
    
    @Test
    public void testParseType() {
        TypeFactory factory = new TypeFactory();
        TypeParser parser = new TypeParser(factory);
        
        TypeParser.MyTokenizer tokenizer = parser.new MyTokenizer("java.util.List<java.lang.String>");
        
        // Test case 1
        Class<?> result1 = parser.parseType(tokenizer);
        Class<?> expected1 = List.class;
        assertEquals(expected1, result1);
        
        tokenizer = parser.new MyTokenizer("java.lang.Integer");
        
        // Test case 2
        Class<?> result2 = parser.parseType(tokenizer);
        Class<?> expected2 = Integer.class;
        assertEquals(expected2, result2);
    }
    
    @Test
    public void testFindClass() {
        TypeFactory factory = new TypeFactory();
        TypeParser parser = new TypeParser(factory);
        
        parser.findClass("java.util.List", parser.new MyTokenizer("java.util.List"));
        // Class for List is found successfully
        
        try {
            parser.findClass("com.example.MyClass", parser.new MyTokenizer("com.example.MyClass"));
        } catch (IllegalArgumentException e) {
            assertEquals("Can not locate class 'com.example.MyClass', problem: null", e.getMessage());
        }
        // Class for com.example.MyClass is not found
    }
}