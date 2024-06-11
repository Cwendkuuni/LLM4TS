package com.fasterxml.jackson.databind.type;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class TypeParserTest {
    private TypeParser typeParser;

    @Before
    public void setUp() {
        typeParser = new TypeParser(new TypeFactory());
    }

    @Test
    public void testParse() {
        // Test case where canonical is empty
        try {
            typeParser.parse("");
        }  catch (IllegalArgumentException e) {
            assertEquals("Failed to parse type '' (remaining: ''): Unexpected end-of-string", e.getMessage());
        }

        // Test case where canonical is not empty
        try {
            typeParser.parse("java.lang.String");
        }  catch (IllegalArgumentException e) {
            assertEquals("Failed to parse type 'java.lang.String' (remaining: ''): Unexpected end-of-string", e.getMessage());
        }
    }

    @Test
    public void testParseType() {
        // Test case where tokens is empty
        MyTokenizer tokens = new MyTokenizer("");
        try {
            typeParser.parseType(tokens);
        }  catch (IllegalArgumentException e) {
            assertEquals("Failed to parse type '' (remaining: ''): Unexpected end-of-string", e.getMessage());
        }

        // Test case where tokens is not empty
        tokens = new MyTokenizer("java.util.List");
        try {
            typeParser.parseType(tokens);
        }  catch (IllegalArgumentException e) {
            assertEquals("Failed to parse type 'java.util.List' (remaining: ''): Unexpected end-of-string", e.getMessage());
        }
    }

    @Test
    public void testParseTypes() {
        // Test case where tokens is empty
        MyTokenizer tokens = new MyTokenizer("");
        try {
            typeParser.parseTypes(tokens);
        }  catch (IllegalArgumentException e) {
            assertEquals("Failed to parse type '' (remaining: ''): Unexpected end-of-string", e.getMessage());
        }

        // Test case where tokens is not empty
        tokens = new MyTokenizer("java.util.Map");
        try {
            typeParser.parseTypes(tokens);
        }  catch (IllegalArgumentException e) {
            assertEquals("Failed to parse type 'java.util.Map' (remaining: ''): Unexpected end-of-string", e.getMessage());
        }
    }

    @Test
    public void testFindClass() {
        // Test case where className is a valid class name
        MyTokenizer tokens = new MyTokenizer("java.util.List");
        Class<?> result = typeParser.findClass(tokens.nextToken(), tokens);
        assertEquals(List.class, result);

        // Test case where className is not a valid class name
        tokens = new MyTokenizer("invalid");
        try {
            typeParser.findClass(tokens.nextToken(), tokens);
        }  catch (IllegalArgumentException e) {
            assertEquals("Failed to parse type 'java.util.Map' (remaining: ''): Can not locate class 'invalid', problem: null", e.getMessage());
        }
    }

    @Test
    public void test_problem() {
        MyTokenizer tokens = new MyTokenizer("java.util.List");
        IllegalArgumentException exception = typeParser._problem(tokens, "Unexpected end-of-string");
        assertEquals("Failed to parse type 'java.util.List' (remaining: ''): Unexpected end-of-string", exception.getMessage());
    }

    @Test
    public void testMyTokenizer() {
        // Test case where str is empty
        MyTokenizer tokenizer = new MyTokenizer("");
        assertEquals("", tokenizer.getAllInput());
        assertFalse(tokenizer.hasMoreTokens());
        assertEquals("", tokenizer.getRemainingInput());
        
        // Test case where str is not empty
        tokenizer = new MyTokenizer("java.util.List");
        assertEquals("java.util.List", tokenizer.getAllInput());
        assertTrue(tokenizer.hasMoreTokens());
        assertEquals("java.util.List", tokenizer.getRemainingInput());
    }
}