package com.fasterxml.jackson.databind.type;

import org.junit.Test;

import static org.junit.Assert.*;

import com.fasterxml.jackson.databind.JavaType;

import java.util.ArrayList;
import java.util.List;

public class TypeParserTest {
    
    private TypeParser typeParser;
    
    public TypeParserTest() {
        typeParser = new TypeParser(new TypeFactory());
    }
    
    @Test
    public void testParse_noTokens_throwsException() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            typeParser.parse("");
        });
        String expectedMessage = "Failed to parse type '' (remaining: ''): Unexpected end-of-string";
        String actualMessage = exception.getMessage();
        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    public void testParse_typeWithNoGenerics_successfullyParsed() {
        JavaType javaType = typeParser.parse("java.lang.String");
        assertEquals("class java.lang.String", javaType.toString());
    }

    @Test
    public void testParse_typeWithGenerics_successfullyParsed() {
        JavaType javaType = typeParser.parse("java.util.List<java.lang.String>");
        assertEquals("java.util.List<java.lang.String>", javaType.toString());
    }

    @Test
    public void testParse_invalidType_throwsException() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            typeParser.parse("java.lang.InvalidClass");
        });
        String expectedMessage = "Failed to parse type 'java.lang.InvalidClass' (remaining: 'java.lang.InvalidClass'): Can not locate class 'java.lang.InvalidClass', problem: null";
        String actualMessage = exception.getMessage();
        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    public void testParse_typeWithUnexpectedTokens_throwsException() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            typeParser.parse("java.util.HashMap<java.lang.String,java.lang.Integer>");
        });
        String expectedMessage = "Failed to parse type 'java.util.HashMap<java.lang.String,java.lang.Integer>' (remaining: 'java.util.HashMap<java.lang.String,java.lang.Integer>'): Unexpected tokens after complete type";
        String actualMessage = exception.getMessage();
        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    public void testParse_noClassForGenerics_throwsException() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            typeParser.parse("java.util.List<>");
        });
        String expectedMessage = "Failed to parse type 'java.util.List<>' (remaining: '<>'): Unexpected end-of-string";
        String actualMessage = exception.getMessage();
        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    public void testParse_hiddenGenericType_throwsException() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            typeParser.parse("java.util.Map<java.lang.String,java.util.List>");
        });
        String expectedMessage = "Failed to parse type 'java.util.Map<java.lang.String,java.util.List>' (remaining: '<java.lang.String,java.util.List>'): Unexpected token 'java.util.List', expected ',' or '>')";
        String actualMessage = exception.getMessage();
        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    public void testParse_multipleGenerics_successfullyParsed() {
        JavaType javaType = typeParser.parse("java.util.Map<java.lang.String,java.lang.Integer,java.lang.Boolean>");
        assertEquals("java.util.Map<java.lang.String,java.lang.Integer,java.lang.Boolean>", javaType.toString());
    }
    
    @Test
    public void testFindClass_existingClass_returnClass() {
        Class<?> clazz = typeParser.findClass("java.util.HashMap", null);
        assertEquals(HashMap.class, clazz);
    }

    @Test
    public void testFindClass_invalidClass_throwsException() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            typeParser.findClass("java.lang.InvalidClass", null);
        });
        String expectedMessage = "Failed to parse type '' (remaining: ''): Can not locate class 'java.lang.InvalidClass', problem: null";
        String actualMessage = exception.getMessage();
        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    public void testFindClass_nullClass_throwsException() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            typeParser.findClass(null, null);
        });
        String expectedMessage = "Failed to parse type '' (remaining: ''): Can not locate class '', problem: null";
        String actualMessage = exception.getMessage();
        assertTrue(actualMessage.contains(expectedMessage));
    }
}