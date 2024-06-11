package com.fasterxml.jackson.databind.type;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.TypeBindings;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.fasterxml.jackson.databind.type.TypeParser.MyTokenizer;

public class TypeParserTest {

    private TypeParser parser;

    @Before
    public void setup() {
        TypeFactory factory = new TypeFactory();
        parser = new TypeParser(factory);
    }

    @Test
    public void testParse() {
        JavaType javaType = parser.parse("java.lang.String");
        assertNotNull(javaType);
        assertEquals("java.lang.String", javaType.toString());

        javaType = parser.parse("int");
        assertNotNull(javaType);
        assertEquals("int", javaType.toString());

        javaType = parser.parse("List<String>");
        assertNotNull(javaType);
        assertEquals("java.util.List<java.lang.String>", javaType.toString());

        javaType = parser.parse("Map<String, Object>");
        assertNotNull(javaType);
        assertEquals("java.util.Map<java.lang.String, java.lang.Object>", javaType.toString());

        try {
            javaType = parser.parse("UnknownClass");
        } catch (IllegalArgumentException e) {
            assertEquals("Failed to parse type 'UnknownClass' (remaining: 'UnknownClass'): Can not locate class 'UnknownClass', problem: null", e.getMessage());
        }
    }

    @Test
    public void testParseType() {
        MyTokenizer tokenizer = new MyTokenizer("java.lang.String");
        JavaType javaType = parser.parseType(tokenizer);
        assertNotNull(javaType);
        assertEquals("java.lang.String", javaType.toString());
        assertTrue(tokenizer.hasMoreTokens());

        tokenizer = new MyTokenizer("List<String>");
        javaType = parser.parseType(tokenizer);
        assertNotNull(javaType);
        assertEquals("java.util.List<java.lang.String>", javaType.toString());
        assertTrue(tokenizer.hasMoreTokens());

        tokenizer = new MyTokenizer("int");
        javaType = parser.parseType(tokenizer);
        assertNotNull(javaType);
        assertEquals("int", javaType.toString());
        assertTrue(tokenizer.hasMoreTokens());

        tokenizer = new MyTokenizer("UnknownClass");
        try {
            javaType = parser.parseType(tokenizer);
        } catch (IllegalArgumentException e) {
            assertEquals("Failed to parse type 'UnknownClass' (remaining: 'UnknownClass'): Can not locate class 'UnknownClass', problem: null", e.getMessage());
        }
        assertTrue(tokenizer.hasMoreTokens());
        
        // test empty string
        tokenizer = new MyTokenizer("");
        try {
        	javaType = parser.parseType(tokenizer);
        } catch (IllegalArgumentException e) {
        	assertEquals("Failed to parse type '' (remaining: ''): Unexpected end-of-string", e.getMessage());
        }
        assertTrue(tokenizer.hasMoreTokens());
    }

    @Test
    public void testParseTypes() {
        MyTokenizer tokenizer = new MyTokenizer("java.lang.String, Object, Integer");
        List<JavaType> javaTypes = parser.parseTypes(tokenizer);
        assertNotNull(javaTypes);
        assertEquals(3, javaTypes.size());
        assertEquals("java.lang.String", javaTypes.get(0).toString());
        assertEquals("java.lang.Object", javaTypes.get(1).toString());
        assertEquals("java.lang.Integer", javaTypes.get(2).toString());

        tokenizer = new MyTokenizer("java.lang.String");
        javaTypes = parser.parseTypes(tokenizer);
        assertNotNull(javaTypes);
        assertEquals(1, javaTypes.size());
        assertEquals("java.lang.String", javaTypes.get(0).toString());

        tokenizer = new MyTokenizer("");
        try {
        	javaTypes = parser.parseTypes(tokenizer);
        } catch (IllegalArgumentException e) {
        	assertEquals("Failed to parse type '' (remaining: ''): Unexpected end-of-string", e.getMessage());
        }
        
        tokenizer = new MyTokenizer(">");
        try {
        	javaTypes = parser.parseTypes(tokenizer);
        } catch (IllegalArgumentException e) {
        	assertEquals("Failed to parse type 'java.lang.String' (remaining: '>'): Unexpected token '>', expected ',' or '>')", e.getMessage());
        }
    }

    @Test
    public void testFindClass() {
        String className = "java.lang.String";
        Class<?> clazz = parser.findClass(className, null);
        assertNotNull(clazz);
        assertEquals(className, clazz.getName());

        className = "UnknownClass";
        try {
            clazz = parser.findClass(className, null);
        } catch (IllegalArgumentException e) {
            assertEquals("Failed to parse type 'UnknownClass' (remaining: 'UnknownClass'): Can not locate class 'UnknownClass', problem: null", e.getMessage());
        }
    }

    @Test
    public void testProblem() {
        MyTokenizer tokenizer = new MyTokenizer("java.lang.String");
        IllegalArgumentException exception = parser._problem(tokenizer, "Test exception");
        assertNotNull(exception);
        assertEquals("Failed to parse type 'java.lang.String' (remaining: 'java.lang.String'): Test exception", exception.getMessage());

        tokenizer = new MyTokenizer("java.lang.String");
        tokenizer.nextToken();
        exception = parser._problem(tokenizer, "Test exception");
        assertNotNull(exception);
        assertEquals("Failed to parse type 'java.lang.String' (remaining: ''): Test exception", exception.getMessage());
    }
}