package com.fasterxml.jackson.databind.type;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class TypeParserTest {

    private TypeParser typeParser;

    @Before
    public void setUp() {
        typeParser = new TypeParser(new TypeFactory());
    }

    @Test
    public void testParse() {
        try {
            JavaType result = typeParser.parse("java.util.List<java.lang.String>");
            assertNotNull(result);
            assertEquals("java.util.List<java.lang.String>", result.toCanonical());
        } catch (IllegalArgumentException e) {
            fail("Test failed with exception: " + e.getMessage());
        }
    }

    @Test
    public void testParseType() {
        TypeParser.MyTokenizer tokens = new TypeParser.MyTokenizer("java.util.List<java.lang.String>");
        try {
            JavaType result = typeParser.parseType(tokens);
            assertNotNull(result);
            assertEquals("java.util.List<java.lang.String>", result.toCanonical());
        } catch (IllegalArgumentException e) {
            fail("Test failed with exception: " + e.getMessage());
        }
    }

    @Test
    public void testParseTypes() {
        TypeParser.MyTokenizer tokens = new TypeParser.MyTokenizer("java.util.List<java.lang.String>");
        try {
            typeParser.parseType(tokens);
            JavaType result = typeParser.parseTypes(tokens);
            assertNotNull(result);
            assertEquals("java.lang.String", result.toCanonical());
        } catch (IllegalArgumentException e) {
            fail("Test failed with exception: " + e.getMessage());
        }
    }

    @Test
    public void testFindClass() {
        TypeParser.MyTokenizer tokens = new TypeParser.MyTokenizer("java.util.List<java.lang.String>");
        Class<?> result = typeParser.findClass("java.util.List", tokens);
        assertNotNull(result);
        assertEquals(java.util.List.class, result);
    }

    @Test
    public void testProblem() {
        TypeParser.MyTokenizer tokens = new TypeParser.MyTokenizer("java.util.List<java.lang.String>");
        IllegalArgumentException result = typeParser._problem(tokens, "Test Message");
        assertNotNull(result);
        assertEquals("Failed to parse type 'java.util.List<java.lang.String>' (remaining: ''): Test Message", result.getMessage());
    }

    @Test
    public void testMyTokenizer() {
        String str = "java.util.List<java.lang.String>";
        TypeParser.MyTokenizer tokenizer = new TypeParser.MyTokenizer(str);
        assertTrue(tokenizer.hasMoreTokens());
        assertEquals("java.util.List", tokenizer.nextToken());
        assertTrue(tokenizer.hasMoreTokens());
        assertEquals("<", tokenizer.nextToken());
        assertTrue(tokenizer.hasMoreTokens());
        assertEquals("java.lang.String", tokenizer.nextToken());
        assertTrue(tokenizer.hasMoreTokens());
        assertEquals(">", tokenizer.nextToken());
        assertFalse(tokenizer.hasMoreTokens());
        assertEquals(str, tokenizer.getAllInput());
        assertEquals("", tokenizer.getRemainingInput());
    }

    @Test
    public void testMyTokenizerWithPushBack() {
        String str = "java.util.List<java.lang.String>";
        TypeParser.MyTokenizer tokenizer = new TypeParser.MyTokenizer(str);
        assertTrue(tokenizer.hasMoreTokens());
        assertEquals("java.util.List", tokenizer.nextToken());
        tokenizer.pushBack("pushedBackToken");
        assertEquals("pushedBackToken", tokenizer.nextToken());
    }
}