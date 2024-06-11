package com.fasterxml.jackson.databind.type;

import org.junit.Test;
import static org.junit.Assert.*;

public class TypeParserTest {

    @Test
    public void testParse() {
        TypeFactory factory = new TypeFactory();
        TypeParser parser = new TypeParser(factory);
        String canonical = "java.util.List<java.lang.String>";
        JavaType expected = factory._fromClass(null, List.class, TypeBindings.create(String.class));
        JavaType actual = parser.parse(canonical);
        assertEquals(expected, actual);
    }

    @Test
    public void testParseType() {
        TypeFactory factory = new TypeFactory();
        TypeParser parser = new TypeParser(factory);
        MyTokenizer tokens = new MyTokenizer("java.util.List<java.lang.String>");
        JavaType expected = factory._fromClass(null, List.class, TypeBindings.create(String.class));
        JavaType actual = parser.parseType(tokens);
        assertEquals(expected, actual);
    }

    @Test
    public void testParseTypes() {
        TypeFactory factory = new TypeFactory();
        TypeParser parser = new TypeParser(factory);
        MyTokenizer tokens = new MyTokenizer("java.lang.String, java.lang.Integer");
        List<JavaType> expected = new ArrayList<>();
        expected.add(factory._fromClass(null, String.class, null));
        expected.add(factory._fromClass(null, Integer.class, null));
        List<JavaType> actual = parser.parseTypes(tokens);
        assertEquals(expected, actual);
    }

    @Test
    public void testFindClass() {
        TypeFactory factory = new TypeFactory();
        TypeParser parser = new TypeParser(factory);
        String className = "java.util.List";
        MyTokenizer tokens = new MyTokenizer("java.util.List<java.lang.String>");
        Class<?> expected = List.class;
        Class<?> actual = parser.findClass(className, tokens);
        assertEquals(expected, actual);
    }

    @Test
    public void testProblem() {
        TypeFactory factory = new TypeFactory();
        TypeParser parser = new TypeParser(factory);
        MyTokenizer tokens = new MyTokenizer("java.util.List<java.lang.String>");
        String msg = "Invalid type format";
        IllegalArgumentException expected = new IllegalArgumentException("Failed to parse type 'java.util.List<java.lang.String>' (remaining: ''): Invalid type format");
        IllegalArgumentException actual = parser._problem(tokens, msg);
        assertEquals(expected.getMessage(), actual.getMessage());
    }

    @Test
    public void testMyTokenizer() {
        String input = "java.util.List<java.lang.String>";
        MyTokenizer tokenizer = new MyTokenizer(input);
        assertTrue(tokenizer.hasMoreTokens());
        assertEquals("java.util.List", tokenizer.nextToken());
        assertTrue(tokenizer.hasMoreTokens());
        assertEquals("<", tokenizer.nextToken());
        assertTrue(tokenizer.hasMoreTokens());
        assertEquals("java.lang.String", tokenizer.nextToken());
        assertTrue(tokenizer.hasMoreTokens());
        assertEquals(">", tokenizer.nextToken());
        assertFalse(tokenizer.hasMoreTokens());
        assertEquals("", tokenizer.getRemainingInput());
        assertEquals(input, tokenizer.getAllInput());
    }
}