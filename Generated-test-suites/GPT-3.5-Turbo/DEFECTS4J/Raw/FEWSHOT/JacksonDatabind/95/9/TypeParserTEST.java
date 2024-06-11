package com.fasterxml.jackson.databind.type;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.TypeParser.MyTokenizer;

public class TypeParserTest {
    
    private TypeParser typeParser;

    @Before
    public void setUp() {
        typeParser = new TypeParser(new TypeFactory());
    }
    
    @Test
    public void testParse() {
        String canonical = "java.util.List<java.lang.String>";
        JavaType javaType = typeParser.parse(canonical);
        assertNotNull(javaType);
        assertEquals(ArrayList.class, javaType.getRawClass());
        assertEquals(1, javaType.containedTypeCount());
    }

    @Test(expected=IllegalArgumentException.class)
    public void testParseWithInvalidType() {
        String canonical = "java.util.List<java.lang.String";
        typeParser.parse(canonical);
    }

    @Test
    public void testParseType() {
        String canonical = "java.util.List<java.lang.String>";
        MyTokenizer tokens = new MyTokenizer(canonical.trim());
        JavaType javaType = typeParser.parseType(tokens);
        assertNotNull(javaType);
        assertEquals(ArrayList.class, javaType.getRawClass());
        assertEquals(1, javaType.containedTypeCount());
    }

    @Test(expected=IllegalArgumentException.class)
    public void testParseTypeWithInvalidEndOfString() {
        String canonical = "java.util.List";
        MyTokenizer tokens = new MyTokenizer(canonical.trim());
        typeParser.parseType(tokens);
    }

    @Test(expected=IllegalArgumentException.class)
    public void testParseTypeWithInvalidToken() {
        String canonical = "java.util.List<java.lang.String";
        MyTokenizer tokens = new MyTokenizer(canonical.trim());
        typeParser.parseType(tokens);
    }

    @Test
    public void testParseTypes() {
        String canonical = "java.util.List<java.lang.String, java.util.Map<java.lang.String, java.lang.Integer>>";
        MyTokenizer tokens = new MyTokenizer(canonical.trim());
        List<JavaType> javaTypes = typeParser.parseTypes(tokens);
        assertNotNull(javaTypes);
        assertEquals(2, javaTypes.size());
        assertEquals(ArrayList.class, javaTypes.get(0).getRawClass());
        assertEquals(HashMap.class, javaTypes.get(1).getRawClass());
        assertEquals(1, javaTypes.get(0).containedTypeCount());
        assertEquals(2, javaTypes.get(1).containedTypeCount());
    }

    @Test(expected=IllegalArgumentException.class)
    public void testParseTypesWithInvalidEndOfString() {
        String canonical = "java.util.List<java.lang.String";
        MyTokenizer tokens = new MyTokenizer(canonical.trim());
        typeParser.parseTypes(tokens);
    }

    @Test(expected=IllegalArgumentException.class)
    public void testParseTypesWithInvalidToken() {
        String canonical = "java.util.List<java.lang.String,, java.util.Map<java.lang.String, java.lang.Integer>>";
        MyTokenizer tokens = new MyTokenizer(canonical.trim());
        typeParser.parseTypes(tokens);
    }

    @Test
    public void testFindClass() {
        String className = "java.util.List";
        MyTokenizer tokens = new MyTokenizer("");
        Class<?> clazz = typeParser.findClass(className, tokens);
        assertNotNull(clazz);
        assertEquals(ArrayList.class, clazz);
    }

    @Test(expected=IllegalArgumentException.class)
    public void testFindClassWithInvalidClassName() {
        String className = "com.example.InvalidClass";
        MyTokenizer tokens = new MyTokenizer("");
        typeParser.findClass(className, tokens);
    }

    @Test
    public void testProblem() {
        MyTokenizer tokens = new MyTokenizer("");
        IllegalArgumentException exception = typeParser._problem(tokens, "Invalid input");
        assertEquals("Failed to parse type '' (remaining: ''): Invalid input", exception.getMessage());
    }
}