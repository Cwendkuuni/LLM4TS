package com.fasterxml.jackson.databind.type;

//imports

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

public class TypeParserTest {
    
    @Test
    public void testWithTypeFactory() {
        TypeFactory factory = new TypeFactory();
        TypeParser parser = new TypeParser(factory);
        
        TypeParser newParser = parser.withFactory(factory);
        
        assertEquals(parser, newParser);
    }
    
    @Test
    public void testParse() {
        TypeFactory factory = new TypeFactory();
        TypeParser parser = new TypeParser(factory);
        
        // Test cases for valid input strings
        assertEquals(factory._fromClass(null, Integer.class, null), parser.parse("java.lang.Integer"));
        assertEquals(factory._fromClass(null, List.class, TypeBindings.create(ArrayList.class, factory._fromClass(null, String.class, null))), 
                     parser.parse("java.util.List<java.util.ArrayList<java.lang.String>>"));
        
        // Test cases for invalid input strings
        try {
            parser.parse("");
        } catch (IllegalArgumentException e) {
            assertEquals("Failed to parse type '' (remaining: ''): Unexpected end-of-string", e.getMessage());
        }
        
        try {
            parser.parse("java.");
        } catch (IllegalArgumentException e) {
            assertEquals("Failed to parse type 'java.' (remaining: ''): Unexpected end-of-string", e.getMessage());
        }
        
        try {
            parser.parse("java.lang");
        } catch (IllegalArgumentException e) {
            assertEquals("Failed to parse type 'java.lang' (remaining: ''): Unexpected end-of-string", e.getMessage());
        }
        
        try {
            parser.parse("<>");
        } catch (IllegalArgumentException e) {
            assertEquals("Failed to parse type '<>' (remaining: ''): Unexpected end-of-string", e.getMessage());
        }
        
        try {
            parser.parse("java.util.<>");
        } catch (IllegalArgumentException e) {
            assertEquals("Failed to parse type 'java.util.<>' (remaining: ''): Unexpected end-of-string", e.getMessage());
        }
        
    }
    
    @Test
    public void testParseType() {
        TypeFactory factory = new TypeFactory();
        TypeParser parser = new TypeParser(factory);
        
        // Test cases for valid input strings
        assertEquals(factory._fromClass(null, Integer.class, null), parser.parseType(new MyTokenizer("java.lang.Integer")));
        
        assertEquals(factory._fromClass(null, List.class, TypeBindings.create(ArrayList.class, factory._fromClass(null, String.class, null))), 
                     parser.parseType(new MyTokenizer("java.util.List<java.util.ArrayList<java.lang.String>>")));
        
        // Test cases for invalid input strings
        try {
            parser.parseType(new MyTokenizer(""));
        } catch (IllegalArgumentException e) {
            assertEquals("Failed to parse type '' (remaining: ''): Unexpected end-of-string", e.getMessage());
        }
        
        try {
            parser.parseType(new MyTokenizer("java."));
        } catch (IllegalArgumentException e) {
            assertEquals("Failed to parse type 'java.' (remaining: ''): Unexpected end-of-string", e.getMessage());
        }
        
        try {
            parser.parseType(new MyTokenizer("java.lang"));
        } catch (IllegalArgumentException e) {
            assertEquals("Failed to parse type 'java.lang' (remaining: ''): Unexpected end-of-string", e.getMessage());
        }
        
        try {
            parser.parseType(new MyTokenizer("<>"));
        } catch (IllegalArgumentException e) {
            assertEquals("Failed to parse type '<>' (remaining: ''): Unexpected end-of-string", e.getMessage());
        }
        
        try {
            parser.parseType(new MyTokenizer("java.util.<>"));
        } catch (IllegalArgumentException e) {
            assertEquals("Failed to parse type 'java.util.<>' (remaining: ''): Unexpected end-of-string", e.getMessage());
        }
    }
    
    @Test
    public void testParseTypes() {
        TypeFactory factory = new TypeFactory();
        TypeParser parser = new TypeParser(factory);
        
        // Test cases for valid input strings
        ArrayList<JavaType> types = new ArrayList<JavaType>();
        
        types.add(factory._fromClass(null, Integer.class, null));
        types.add(factory._fromClass(null, String.class, null));
        types.add(factory._fromClass(null, List.class, null));
        
        assertEquals(types, parser.parseTypes(new MyTokenizer("java.lang.Integer,java.lang.String,java.util.List")));
        
        types.clear();
        types.add(factory._fromClass(null, Integer.class, TypeBindings.create(List.class, factory._fromClass(null, String.class, null))));
        types.add(factory._fromClass(null, String.class, null));
        
        assertEquals(types, parser.parseTypes(new MyTokenizer("java.lang.Integer<java.util.List<java.lang.String>>,java.lang.String")));
        
        // Test cases for invalid input strings
        try {
            parser.parseTypes(new MyTokenizer(""));
        } catch (IllegalArgumentException e) {
            assertEquals("Failed to parse type '' (remaining: ''): Unexpected end-of-string", e.getMessage());
        }
        
        try {
            parser.parseTypes(new MyTokenizer("java."));
        } catch (IllegalArgumentException e) {
            assertEquals("Failed to parse type 'java.' (remaining: ''): Unexpected end-of-string", e.getMessage());
        }
        
        try {
            parser.parseTypes(new MyTokenizer("java.lang"));
        } catch (IllegalArgumentException e) {
            assertEquals("Failed to parse type 'java.lang' (remaining: ''): Unexpected end-of-string", e.getMessage());
        }
        
        try {
            parser.parseTypes(new MyTokenizer("java.lang.Integer<java.util.List<java.lang.String>>,"));
        } catch (IllegalArgumentException e) {
            assertEquals("Failed to parse type 'java.lang.Integer<java.util.List<java.lang.String>>,' (remaining: ''): Unexpected end-of-string", e.getMessage());
        }
        
        try {
            parser.parseTypes(new MyTokenizer("java.lang.Integer,"));
        } catch (IllegalArgumentException e) {
            assertEquals("Failed to parse type 'java.lang.Integer,' (remaining: ''): Unexpected end-of-string", e.getMessage());
        }
    }
    
    @Test
    public void testFindClass() {
        TypeFactory factory = new TypeFactory();
        TypeParser parser = new TypeParser(factory);
        
        // Test cases for valid class names
        assertEquals(Integer.class, parser.findClass("java.lang.Integer", new MyTokenizer("")));
        
        assertEquals(List.class, parser.findClass("java.util.List", new MyTokenizer("")));
        
        // Test cases for invalid class names
        try {
            parser.findClass("", new MyTokenizer(""));
        } catch (IllegalArgumentException e) {
            assertEquals("Can not locate class '', problem: null", e.getMessage());
        }
        
        try {
            parser.findClass("java.", new MyTokenizer("java."));
        } catch (IllegalArgumentException e) {
            assertEquals("Can not locate class 'java.', problem: null", e.getMessage());
        }
        
        try {
            parser.findClass("java.lang", new MyTokenizer("java.lang"));
        } catch (IllegalArgumentException e) {
            assertEquals("Can not locate class 'java.lang', problem: null", e.getMessage());
        }
    }
    
    @Test
    public void testProblem() {
        TypeFactory factory = new TypeFactory();
        TypeParser parser = new TypeParser(factory);
        
        MyTokenizer tokenizer = new MyTokenizer("java.lang.");
        
        IllegalArgumentException exception = parser._problem(tokenizer, "Can not locate class 'java.lang.', problem: null");
        
        assertEquals("Failed to parse type 'java.lang.' (remaining: ''): Can not locate class 'java.lang.', problem: null", exception.getMessage());
    }
    
    @Test
    public void testMyTokenizer() {
        MyTokenizer tokenizer = new MyTokenizer("java.lang.Integer");
        
        // Test case for hasMoreTokens() method
        assertTrue(tokenizer.hasMoreTokens());
        tokenizer.nextToken();
        assertFalse(tokenizer.hasMoreTokens());
        
        // Test case for nextToken() method
        tokenizer = new MyTokenizer("java.lang.Integer");
        assertEquals("java.lang.Integer", tokenizer.nextToken());
        assertFalse(tokenizer.hasMoreTokens());
        
        tokenizer = new MyTokenizer("java.lang.");
        assertEquals("java.lang.", tokenizer.nextToken());
        assertFalse(tokenizer.hasMoreTokens());
        
        // Test case for pushBack() method
        tokenizer = new MyTokenizer("java.lang.Integer");
        assertEquals("java.lang.Integer", tokenizer.nextToken());
        tokenizer.pushBack("java");
        assertEquals("java", tokenizer.nextToken());
        assertFalse(tokenizer.hasMoreTokens());
        
        // Test case for getAllInput() method
        tokenizer = new MyTokenizer("java.lang.Integer");
        assertEquals("java.lang.Integer", tokenizer.getAllInput());
    }
    
}