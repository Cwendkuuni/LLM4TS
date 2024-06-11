package com.fasterxml.jackson.databind.type;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

public class TypeParserTest {

    private TypeParser parser;

    @Before
    public void setUp() {
        parser = new TypeParser(TypeFactory.defaultInstance());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseWithInvalidString() {
        parser.parse(null);
    }

    @Test
    public void testParseWithValidString() {
        String canonical = "java.util.List<java.lang.String>";
        JavaType type = parser.parse(canonical);
        assertNotNull(type);
        assertTrue(type.isCollectionLikeType());
        assertEquals("java.util.List", type.getRawClass().getName());
        assertEquals("java.lang.String", type.getContentType().getRawClass().getName());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseTypeWithEmptyTokenizer() {
        TypeParser.MyTokenizer tokens = new TypeParser.MyTokenizer("");
        parser.parseType(tokens);
    }

    @Test
    public void testParseTypeWithGenericType() {
        TypeParser.MyTokenizer tokens = new TypeParser.MyTokenizer("java.util.List<java.lang.String>");
        JavaType type = parser.parseType(tokens);
        assertNotNull(type);
        assertTrue(type.isCollectionLikeType());
        assertEquals("java.util.List", type.getRawClass().getName());
        assertEquals("java.lang.String", type.getContentType().getRawClass().getName());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseTypeWithUnexpectedEndOfString() {
        TypeParser.MyTokenizer tokens = new TypeParser.MyTokenizer("java.util.List<java.lang.String");
        parser.parseType(tokens);
    }

    @Test
    public void testParseTypesWithEmptyTokenizer() {
        TypeParser.MyTokenizer tokens = new TypeParser.MyTokenizer("");
        List<JavaType> types = parser.parseTypes(tokens);
        assertNotNull(types);
        assertTrue(types.isEmpty());
    }

    @Test
    public void testParseTypesWithSingleType() {
        TypeParser.MyTokenizer tokens = new TypeParser.MyTokenizer("java.util.List<String>");
        List<JavaType> types = parser.parseTypes(tokens);
        assertNotNull(types);
        assertEquals(1, types.size());
        assertTrue(types.get(0).isCollectionLikeType());
        assertEquals("java.util.List", types.get(0).getRawClass().getName());
        assertEquals("java.lang.String", types.get(0).getContentType().getRawClass().getName());
    }

    @Test
    public void testParseTypesWithMultipleTypes() {
        TypeParser.MyTokenizer tokens = new TypeParser.MyTokenizer("java.util.Map<String,Integer>");
        List<JavaType> types = parser.parseTypes(tokens);
        assertNotNull(types);
        assertEquals(2, types.size());
        assertTrue(types.get(0).isMapLikeType());
        assertEquals("java.util.Map", types.get(0).getRawClass().getName());
        assertTrue(types.get(1).isPrimitive());
        assertEquals("int", types.get(1).getRawClass().getName());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseTypesWithUnexpectedToken() {
        TypeParser.MyTokenizer tokens = new TypeParser.MyTokenizer("java.util.List<String, Integer>");
        parser.parseTypes(tokens);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFindClassWithNonExistentClass() {
        TypeParser.MyTokenizer tokens = new TypeParser.MyTokenizer("com.example.NonExistent");
        parser.findClass(tokens.nextToken(), tokens);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFindClassWithException() {
        TypeParser.MyTokenizer tokens = new TypeParser.MyTokenizer("com.example.ExceptionClass");
        parser.findClass(tokens.nextToken(), tokens);
    }

    @Test
    public void testFindClassWithValidClass() {
        TypeParser.MyTokenizer tokens = new TypeParser.MyTokenizer("java.util.List");
        Class<?> clazz = parser.findClass(tokens.nextToken(), tokens);
        assertNotNull(clazz);
        assertEquals("java.util.List", clazz.getName());
    }

    @Test
    public void testProblem() {
        TypeParser.MyTokenizer tokens = new TypeParser.MyTokenizer("java.util.List<String>");
        IllegalArgumentException e = parser._problem(tokens, "Unexpected error");
        assertNotNull(e);
        assertEquals("Failed to parse type 'java.util.List<String>' (remaining: ''): Unexpected error", e.getMessage());
    }

    @Test
    public void testMyTokenizerWithEmptyString() {
        TypeParser.MyTokenizer tokens = new TypeParser.MyTokenizer("");
        assertFalse(tokens.hasMoreTokens());
        assertNull(tokens.nextToken());
        assertNull(tokens.getRemainingInput());
    }

    @Test
    public void testMyTokenizerWithNonEmptyString() {
        TypeParser.MyTokenizer tokens = new TypeParser.MyTokenizer("java.util.List<String>");
        assertTrue(tokens.hasMoreTokens());
        assertEquals("java.util.List", tokens.nextToken());
        assertTrue(tokens.hasMoreTokens());
        assertEquals("String", tokens.nextToken());
        assertFalse(tokens.hasMoreTokens());
        assertNull(tokens.nextToken());
        assertTrue(tokens.hasMoreTokens());
        assertEquals("java.util.List<String>", tokens.getAllInput());
        assertEquals("", tokens.getRemainingInput());
    }

    @Test
    public void testMyTokenizerWithPushBack() {
        TypeParser.MyTokenizer tokens = new TypeParser.MyTokenizer("java.util.List");
        assertTrue(tokens.hasMoreTokens());
        assertEquals("java.util.List", tokens.nextToken());
        assertFalse(tokens.hasMoreTokens());
        tokens.pushBack("<");
        tokens.pushBack("String");
        assertTrue(tokens.hasMoreTokens());
        assertEquals("String", tokens.nextToken());
        assertTrue(tokens.hasMoreTokens());
        assertEquals("<", tokens.nextToken());
        assertFalse(tokens.hasMoreTokens());
        assertNull(tokens.nextToken());
        assertTrue(tokens.hasMoreTokens());
    }

}