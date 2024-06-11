package com.fasterxml.jackson.databind.type;

import org.junit.Before;
import org.junit.Test;
import java.util.ArrayList;
import java.util.List;
import static org.junit.Assert.*;

public class TypeParserTest {

    private TypeFactory typeFactory;
    private TypeParser typeParser;

    @Before
    public void setUp() {
        typeFactory = new TypeFactory();
        typeParser = new TypeParser(typeFactory);
    }

    @Test
    public void testParse() {
        // Test case 1
        String canonical1 = "java.lang.String";
        JavaType javaType1 = typeParser.parse(canonical1);
        assertEquals("java.lang.String", javaType1.toString());
        
        // Test case 2
        String canonical2 = "java.util.List<java.lang.Integer>";
        JavaType javaType2 = typeParser.parse(canonical2);
        assertEquals("java.util.List", javaType2.getRawClass().getName());
        assertEquals("java.lang.Integer", javaType2.getContentType().getRawClass().getName());
    }

    @Test(expected=IllegalArgumentException.class)
    public void testParse_ExceptionCase() {
        String canonical = "UnknownClass";
        typeParser.parse(canonical);
    }

    @Test
    public void testParseType() {
        // Test case 1
        TypeParser.MyTokenizer tokenizer1 = new TypeParser.MyTokenizer("java.lang.String");
        JavaType javaType1 = typeParser.parseType(tokenizer1);
        assertEquals("java.lang.String", javaType1.toString());

        // Test case 2
        TypeParser.MyTokenizer tokenizer2 = new TypeParser.MyTokenizer("java.util.List<java.lang.Integer>");
        JavaType javaType2 = typeParser.parseType(tokenizer2);
        assertEquals("java.util.List", javaType2.getRawClass().getName());
        assertEquals("java.lang.Integer", javaType2.getContentType().getRawClass().getName());
    }

    @Test(expected=IllegalArgumentException.class)
    public void testParseType_ExceptionCase1() {
        TypeParser.MyTokenizer tokenizer = new TypeParser.MyTokenizer("");
        typeParser.parseType(tokenizer);
    }

    @Test(expected=IllegalArgumentException.class)
    public void testParseType_ExceptionCase2() {
        TypeParser.MyTokenizer tokenizer = new TypeParser.MyTokenizer("UnknownClass");
        typeParser.parseType(tokenizer);
    }

    @Test
    public void testParseTypes() {
        // Test case 1
        TypeParser.MyTokenizer tokenizer1 = new TypeParser.MyTokenizer("java.lang.String");
        List<JavaType> javaTypes1 = typeParser.parseTypes(tokenizer1);
        assertEquals(1, javaTypes1.size());
        assertEquals("java.lang.String", javaTypes1.get(0).toString());
        
        // Test case 2
        TypeParser.MyTokenizer tokenizer2 = new TypeParser.MyTokenizer("java.util.List<java.lang.Integer>");
        List<JavaType> javaTypes2 = typeParser.parseTypes(tokenizer2);
        assertEquals(1, javaTypes2.size());
        assertEquals("java.util.List", javaTypes2.get(0).getRawClass().getName());
        assertEquals("java.lang.Integer", javaTypes2.get(0).getContentType().getRawClass().getName());

        // Test case 3
        TypeParser.MyTokenizer tokenizer3 = new TypeParser.MyTokenizer("java.util.List<java.lang.Integer>,java.util.Map<java.lang.String,java.lang.Boolean>");
        List<JavaType> javaTypes3 = typeParser.parseTypes(tokenizer3);
        assertEquals(2, javaTypes3.size());
        assertEquals("java.util.List", javaTypes3.get(0).getRawClass().getName());
        assertEquals("java.util.Map", javaTypes3.get(1).getRawClass().getName());
        assertEquals("java.lang.Integer", javaTypes3.get(0).getContentType().getRawClass().getName());
        assertEquals("java.lang.String", javaTypes3.get(1).getKeyType().getRawClass().getName());
        assertEquals("java.lang.Boolean", javaTypes3.get(1).getContentType().getRawClass().getName());
    }

    @Test(expected=IllegalArgumentException.class)
    public void testParseTypes_ExceptionCase1() {
        TypeParser.MyTokenizer tokenizer = new TypeParser.MyTokenizer("");
        typeParser.parseTypes(tokenizer);
    }

    @Test(expected=IllegalArgumentException.class)
    public void testParseTypes_ExceptionCase2() {
        TypeParser.MyTokenizer tokenizer = new TypeParser.MyTokenizer("UnknownClass");
        typeParser.parseTypes(tokenizer);
    }

    @Test(expected=IllegalArgumentException.class)
    public void testParseTypes_ExceptionCase3() {
        TypeParser.MyTokenizer tokenizer = new TypeParser.MyTokenizer("java.util.List<java.lang.Integer>");
        tokenizer.nextToken(); // consume the first type
        typeParser.parseTypes(tokenizer);
    }

    @Test
    public void testFindClass() {
        // Test case 1
        TypeParser.MyTokenizer tokenizer1 = new TypeParser.MyTokenizer("java.lang.String");
        Class<?> clazz1 = typeParser.findClass(tokenizer1.nextToken(), tokenizer1);
        assertEquals("java.lang.String", clazz1.getName());

        // Test case 2
        TypeParser.MyTokenizer tokenizer2 = new TypeParser.MyTokenizer("UnknownClass");
        try {
            typeParser.findClass(tokenizer2.nextToken(), tokenizer2);
            fail("Expected IllegalArgumentException not thrown");
        } catch (IllegalArgumentException e) {
            assertEquals("Can not locate class 'UnknownClass', problem: null", e.getMessage());
        }
    }

    @Test(expected=IllegalArgumentException.class)
    public void test_problem() {
        TypeParser.MyTokenizer tokenizer = new TypeParser.MyTokenizer("java.lang.String");
        IllegalArgumentException exception = typeParser._problem(tokenizer, "Test exception");
        assertEquals("Failed to parse type 'java.lang.String' (remaining: ''): Test exception", exception.getMessage());
    }

    @Test
    public void test_MyTokenizer() {
        TypeParser.MyTokenizer tokenizer = new TypeParser.MyTokenizer("java.lang.String");
        assertEquals("java.lang.String", tokenizer.getToken());
        assertFalse(tokenizer.hasMoreTokens());
    }

}