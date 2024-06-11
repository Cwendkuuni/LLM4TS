
package com.fasterxml.jackson.databind.type;

import static org.junit.Assert.*;

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
        JavaType result = typeParser.parse(canonical);

        assertNotNull(result);
        assertEquals(ArrayList.class, result.getRawClass());
        assertEquals(1, result.getBindings().size());
        assertEquals(String.class, result.getBindings().getBoundType(0).getRawClass());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseWithInvalidCanonical() {
        String canonical = "java.lang.String}";
        typeParser.parse(canonical);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseWithUnexpectedTokens() {
        String canonical = "java.util.Map<java.lang.String, java.lang.Integer> extraTokens";
        typeParser.parse(canonical);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseWithUnexpectedEndOfString() {
        String canonical = "java.util.List<java.lang.String";
        typeParser.parse(canonical);
    }

    @Test
    public void testParseType() {
        MyTokenizer tokens = new MyTokenizer("java.util.List<java.lang.String>");
        JavaType result = typeParser.parseType(tokens);

        assertNotNull(result);
        assertEquals(ArrayList.class, result.getRawClass());
        assertEquals(1, result.getBindings().size());
        assertEquals(String.class, result.getBindings().getBoundType(0).getRawClass());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseTypeWithUnexpectedEndOfString() {
        MyTokenizer tokens = new MyTokenizer("");
        typeParser.parseType(tokens);
    }

    @Test
    public void testParseTypes() {
        MyTokenizer tokens = new MyTokenizer("java.lang.Integer, java.lang.String");
        List<JavaType> result = typeParser.parseTypes(tokens);

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(Integer.class, result.get(0).getRawClass());
        assertEquals(String.class, result.get(1).getRawClass());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseTypesWithUnexpectedEndOfString() {
        MyTokenizer tokens = new MyTokenizer("java.lang.Integer");
        typeParser.parseTypes(tokens);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseTypesWithUnexpectedToken() {
        MyTokenizer tokens = new MyTokenizer("java.lang.Integer ,");
        typeParser.parseTypes(tokens);
    }

    @Test
    public void testFindClass() {
        MyTokenizer tokens = new MyTokenizer("java.lang.String");
        Class<?> result = typeParser.findClass(tokens.nextToken(), tokens);

        assertEquals(String.class, result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFindClassWithClassNotFoundException() {
        MyTokenizer tokens = new MyTokenizer("invalid.class");
        typeParser.findClass(tokens.nextToken(), tokens);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFindClassWithRuntimeException() {
        MyTokenizer tokens = new MyTokenizer("invalid.class");
        typeParser.withFactory(null).findClass(tokens.nextToken(), tokens);
    }

    @Test
    public void testProblem() {
        MyTokenizer tokens = new MyTokenizer("java.util.List<java.lang.String>");
        IllegalArgumentException exception = typeParser._problem(tokens, "Invalid type");

        assertEquals("Failed to parse type 'java.util.List<java.lang.String>' (remaining: ''): Invalid type",
                exception.getMessage());
    }

    @Test
    public void testMyTokenizer() {
        MyTokenizer tokens = new MyTokenizer("java.util.List<java.lang.String>");

        assertTrue(tokens.hasMoreTokens());
        assertEquals("java.util.List", tokens.nextToken());
        assertEquals("<", tokens.nextToken());
        assertEquals("java.lang.String", tokens.nextToken());
        assertEquals(">", tokens.nextToken());
        assertFalse(tokens.hasMoreTokens());

        tokens.pushBack("unexpectedToken");
        assertTrue(tokens.hasMoreTokens());
        assertEquals("unexpectedToken", tokens.nextToken());
        assertFalse(tokens.hasMoreTokens());

        assertEquals("java.util.List<java.lang.String>", tokens.getAllInput());
        assertEquals("", tokens.getRemainingInput());
    }

}
