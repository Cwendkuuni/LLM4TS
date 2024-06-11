package com.example.test;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.SimpleType;
import com.fasterxml.jackson.databind.TypeFactory;
import com.fasterxml.jackson.databind.TypeReference;
import org.junit.Test;

import static org.junit.Assert.*;

public class TypeParserTest {

    @Test
    public void testParse() throws Exception {
        TypeParser parser = new TypeParser(new TypeFactory());
        String canonical = "java.lang.String";
        JavaType actual = parser.parse(canonical);
        JavaType expected = new SimpleType("java.lang.String");
        assertEquals(expected, actual);
    }

    @Test
    public void testParseType() throws Exception {
        MyTokenizer tokens = new MyTokenizer("java.lang.String");
        TypeParser parser = new TypeParser(new TypeFactory());
        JavaType actual = parser.parseType(tokens);
        JavaType expected = new SimpleType("java.lang.String");
        assertEquals(expected, actual);
    }

    @Test
    public void testParseTypes() throws Exception {
        MyTokenizer tokens = new MyTokenizer("java.lang.String, java.util.List<Integer>");
        TypeParser parser = new TypeParser(new TypeFactory());
        List<JavaType> actual = parser.parseTypes(tokens);
        List<JavaType> expected = Arrays.asList(new SimpleType("java.lang.String"), new TypeReference<List<Integer>>(){}.getType());
        assertEquals(expected, actual);
    }

    @Test
    public void testFindClass() throws Exception {
        MyTokenizer tokens = new MyTokenizer("");
        TypeParser parser = new TypeParser(new TypeFactory());
        String className = "java.lang.String";
        Class<?> actual = parser.findClass(className, tokens);
        Class<?> expected = String.class;
        assertEquals(expected, actual);
    }

    @Test
    public void testProblem() throws Exception {
        MyTokenizer tokens = new MyTokenizer("");
        TypeParser parser = new TypeParser(new TypeFactory());
        parser._problem(tokens, "test message");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testEmptyString() throws Exception {
        TypeParser parser = new TypeParser(new TypeFactory());
        parser.parse("");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidClassName() throws Exception {
        MyTokenizer tokens = new MyTokenizer("myClass$#^%");
        TypeParser parser = new TypeParser(new TypeFactory());
        parser._problem(tokens, "Can not locate class 'myClass$#^%'");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidTokenOrder() throws Exception {
        MyTokenizer tokens = new MyTokenizer("java.lang.String<,>");
        TypeParser parser = new TypeParser(new TypeFactory());
        parser._problem(tokens, "Unexpected token ',' at position 7, expected '>'");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testMissingGreaterThan() throws Exception {
        MyTokenizer tokens = new MyTokenizer("java.util.List<Integer>");
        TypeParser parser = new TypeParser(new TypeFactory());
        parser.parseTypes(tokens);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testMissingComma() throws Exception {
        MyTokenizer tokens = new MyTokenizer("java.util.List<IntegerJava.util.List<String>");
        TypeParser parser = new TypeParser(new TypeFactory());
        parser._problem(tokens, "Unexpected token 'Java.util.List<String>' at position 21, expected ',' or '>')");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testUnexpectedEndOfString() throws Exception {
        MyTokenizer tokens = new MyTokenizer("");
        TypeParser parser = new TypeParser(new TypeFactory());
        parser.parseTypes(tokens);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testMissingClassOrPackage() throws Exception {
        MyTokenizer tokens = new MyTokenizer("<missingPackage>.MissingClass");
        TypeParser parser = new TypeParser(new TypeFactory());
        parser._problem(tokens, "Can not locate class 'MissingClass' in package 'missingPackage'");
    }
}