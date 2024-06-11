package com.fasterxml.jackson.databind.type;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;
import com.fasterxml.jackson.databind.JavaType;

public class TypeParserTest {

    private TypeParser typeParser;
    
    @Before
    public void setUp() {
        typeParser = new TypeParser(new TypeFactory());
    }
    
    @Test
    public void testParse_NoGeneric() {
        String canonical = "java.util.List";
        JavaType javaType = typeParser.parse(canonical);
        assertEquals("List", javaType.getRawClass().getSimpleName());
        assertNull(javaType.containedTypeCount());
    }
    
    @Test
    public void testParse_GenericWithOneParameter() {
        String canonical = "java.util.Map<java.lang.String>";
        JavaType javaType = typeParser.parse(canonical);
        assertEquals("Map", javaType.getRawClass().getSimpleName());
        assertEquals(1, javaType.containedTypeCount());
        assertEquals("String", javaType.containedType(0).getRawClass().getSimpleName());
    }
    
    @Test
    public void testParse_GenericWithMultipleParameters() {
        String canonical = "java.util.Map<java.lang.String, java.lang.Integer>";
        JavaType javaType = typeParser.parse(canonical);
        assertEquals("Map", javaType.getRawClass().getSimpleName());
        assertEquals(2, javaType.containedTypeCount());
        assertEquals("String", javaType.containedType(0).getRawClass().getSimpleName());
        assertEquals("Integer", javaType.containedType(1).getRawClass().getSimpleName());
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testParse_InvalidTokens() {
        String canonical = "java.util.Map<java.lang.String,, java.lang.Integer>";
        typeParser.parse(canonical);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testParse_UnexpectedEndOfString() {
        String canonical = "java.util.Map<java.lang.String, java.lang.Integer";
        typeParser.parse(canonical);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testParse_ClassNotFound() {
        String canonical = "com.example.NonExistentClass";
        typeParser.parse(canonical);
    }
}