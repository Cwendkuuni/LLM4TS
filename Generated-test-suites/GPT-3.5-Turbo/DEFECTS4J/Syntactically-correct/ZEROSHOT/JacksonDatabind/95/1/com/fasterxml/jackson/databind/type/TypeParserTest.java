package com.fasterxml.jackson.databind.type;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class TypeParserTest {

    private TypeParser typeParser;

    @Before
    public void setUp() {
        // Initialize TypeParser
        typeParser = new TypeParser(TypeFactory.defaultInstance());
    }

    @Test
    public void testParse() {
        String canonical = "java.util.List<java.lang.String>";
        JavaType result = typeParser.parse(canonical);
        assertNotNull(result);
        assertEquals(List.class, result.getRawClass());
        assertEquals(String.class, result.getContentType().getRawClass());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParse_withInvalidCanonical() {
        String canonical = "InvalidCanonical";
        typeParser.parse(canonical);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParse_withUnexpectedTokens() {
        String canonical = "java.util.List<java.lang.String> unexpected";
        typeParser.parse(canonical);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParse_withUnexpectedEndOfString() {
        String canonical = "";
        typeParser.parse(canonical);
    }

    @Test
    public void testParseType() {
        TypeParser.MyTokenizer tokens = new TypeParser.MyTokenizer("java.util.List<java.lang.String>");
        JavaType result = typeParser.parseType(tokens);
        assertNotNull(result);
        assertEquals(List.class, result.getRawClass());
        assertEquals(String.class, result.getContentType().getRawClass());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseType_withUnexpectedEndOfString() {
        TypeParser.MyTokenizer tokens = new TypeParser.MyTokenizer("");
        typeParser.parseType(tokens);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseType_withUnexpectedEndOfTokens() {
        TypeParser.MyTokenizer tokens = new TypeParser.MyTokenizer("java.util.List");
        typeParser.parseType(tokens);
    }

    @Test
    public void testParseTypes() {
        TypeParser.MyTokenizer tokens = new TypeParser.MyTokenizer("java.lang.String, java.lang.Integer, java.lang.Boolean");
        List<JavaType> result = typeParser.parseTypes(tokens);
        assertNotNull(result);
        assertEquals(3, result.size());
        assertEquals(String.class, result.get(0).getRawClass());
        assertEquals(Integer.class, result.get(1).getRawClass());
        assertEquals(Boolean.class, result.get(2).getRawClass());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseTypes_withUnexpectedEndOfString() {
        TypeParser.MyTokenizer tokens = new TypeParser.MyTokenizer("");
        typeParser.parseTypes(tokens);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseTypes_withUnexpectedToken() {
        TypeParser.MyTokenizer tokens = new TypeParser.MyTokenizer("java.lang.String non");
        typeParser.parseTypes(tokens);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFindClass() {
        TypeParser.MyTokenizer tokens = new TypeParser.MyTokenizer("InvalidClass");
        typeParser.findClass(tokens.nextToken(), tokens);
    }

    @Test
    public void testFindClass_withValidClass() {
        TypeParser.MyTokenizer tokens = new TypeParser.MyTokenizer("java.lang.String");
        Class<?> result = typeParser.findClass(tokens.nextToken(), tokens);
        assertNotNull(result);
        assertEquals(String.class, result);
    }

    @Test
    public void testProblem() {
        TypeParser.MyTokenizer tokens = new TypeParser.MyTokenizer("java.util.List<java.lang.String>");
        String msg = "Unexpected tokens after complete type";
        IllegalArgumentException exception = typeParser._problem(tokens, msg);
        assertNotNull(exception);
        assertTrue(exception.getMessage().contains(msg));
    }

    @Test
    public void testMyTokenizer_hasMoreTokens() {
        TypeParser.MyTokenizer tokenizer = new TypeParser.MyTokenizer("abc");
        assertTrue(tokenizer.hasMoreTokens());
        tokenizer.nextToken();
        assertFalse(tokenizer.hasMoreTokens());
    }

    @Test
    public void testMyTokenizer_nextToken() {
        TypeParser.MyTokenizer tokenizer = new TypeParser.MyTokenizer("token1, token2");
        String token1 = tokenizer.nextToken();
        assertEquals("token1", token1);
        String token2 = tokenizer.nextToken();
        assertEquals("token2", token2);
    }

    @Test
    public void testMyTokenizer_pushBack() {
        TypeParser.MyTokenizer tokenizer = new TypeParser.MyTokenizer("token");
        tokenizer.pushBack("pushedToken");
        String token = tokenizer.nextToken();
        assertEquals("pushedToken", token);
    }

    @Test
    public void testMyTokenizer_getAllInput() {
        TypeParser.MyTokenizer tokenizer = new TypeParser.MyTokenizer("input");
        assertEquals("input", tokenizer.getAllInput());
    }

    @Test
    public void testMyTokenizer_getRemainingInput() {
        TypeParser.MyTokenizer tokenizer = new TypeParser.MyTokenizer("input");
        assertEquals("", tokenizer.getRemainingInput());
        tokenizer.nextToken();
        assertEquals("", tokenizer.getRemainingInput());
    }
}