package com.fasterxml.jackson.databind.type.test;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.fasterxml.jackson.databind.type.TypeParser;

public class TypeParserTest {
    
    @Test
    public void testParse() {
        String canonical = "java.util.List<String>";
        TypeFactory factory = TypeFactory.defaultInstance();
        TypeParser parser = new TypeParser(factory);
        JavaType javaType = parser.parse(canonical);
        assertEquals("java.util.List", javaType.getRawClass().getName());
        assertEquals("java.lang.String", javaType.getContentType().getRawClass().getName());
    }

    @Test
    public void testParseType() {
        String canonical1 = "java.util.List<String>";
        String canonical2 = "int";
        TypeFactory factory = TypeFactory.defaultInstance();
        TypeParser parser = new TypeParser(factory);
        MyTokenizer tokens1 = new MyTokenizer(canonical1);
        MyTokenizer tokens2 = new MyTokenizer(canonical2);
        JavaType javaType1 = parser.parseType(tokens1);
        JavaType javaType2 = parser.parseType(tokens2);
        assertEquals("java.util.List", javaType1.getRawClass().getName());
        assertEquals("java.lang.String", javaType1.getContentType().getRawClass().getName());
        assertEquals("int", javaType2.getRawClass().getName());
    }

    @Test
    public void testParseTypes() {
        String canonical = "java.util.List<String>, java.util.Map<String, Integer>";
        TypeFactory factory = TypeFactory.defaultInstance();
        TypeParser parser = new TypeParser(factory);
        MyTokenizer tokens = new MyTokenizer(canonical);
        List<JavaType> javaTypes = parser.parseTypes(tokens);
        assertEquals(2, javaTypes.size());
        assertEquals("java.util.List", javaTypes.get(0).getRawClass().getName());
        assertEquals("java.lang.String", javaTypes.get(0).getContentType().getRawClass().getName());
        assertEquals("java.util.Map", javaTypes.get(1).getRawClass().getName());
        assertEquals("java.lang.String", javaTypes.get(1).getKeyType().getRawClass().getName());
        assertEquals("java.lang.Integer", javaTypes.get(1).getContentType().getRawClass().getName());
    }

    @Test
    public void testFindClass() {
        String className = "java.util.List";
        TypeFactory factory = TypeFactory.defaultInstance();
        TypeParser parser = new TypeParser(factory);
        MyTokenizer tokens = new MyTokenizer("");
        Class<?> clazz = parser.findClass(className, tokens);
        assertEquals("java.util.List", clazz.getName());
    }

    @Test
    public void testProblem() {
        String msg = "Failed to parse type 'java.util.List<String>, java.util.Map<String, Integer>' (remaining: '>') Unexpected end-of-string";
        TypeFactory factory = TypeFactory.defaultInstance();
        TypeParser parser = new TypeParser(factory);
        MyTokenizer tokens = new MyTokenizer(">");
        IllegalArgumentException exception = parser._problem(tokens, "Unexpected end-of-string");
        assertEquals(msg, exception.getMessage());
    }

    @Test
    public void testMyTokenizer() {
        String str = "java.util.List";
        MyTokenizer tokenizer = new MyTokenizer(str);
        assertEquals(str, tokenizer.getAllInput());
        assertEquals("java.util.List", tokenizer.nextToken());
        assertEquals("", tokenizer.getRemainingInput());
    }
}