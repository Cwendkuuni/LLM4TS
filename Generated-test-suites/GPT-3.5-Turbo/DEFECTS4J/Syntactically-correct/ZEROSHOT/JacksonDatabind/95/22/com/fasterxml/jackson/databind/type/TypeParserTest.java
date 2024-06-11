package com.fasterxml.jackson.databind.type;

import org.junit.Test;
import org.junit.Before;
import static org.junit.Assert.*;

import com.fasterxml.jackson.databind.JavaType;

import java.util.*;

public class TypeParserTest {

    private TypeParser typeParser;

    @Before
    public void setUp() {
        TypeFactory typeFactory = new TypeFactory();
        typeParser = new TypeParser(typeFactory);
    }

    @Test
    public void testParse() {
        String canonical = "java.util.List";
        JavaType result = typeParser.parse(canonical);
        assertNotNull(result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseException() {
        String canonical = "";
        typeParser.parse(canonical);
    }

    @Test
    public void testParseType() {
        TypeParser.MyTokenizer tokens = new TypeParser.MyTokenizer("java.util.List");
        JavaType result = typeParser.parseType(tokens);
        assertNotNull(result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseTypeException() {
        TypeParser.MyTokenizer tokens = new TypeParser.MyTokenizer("");
        typeParser.parseType(tokens);
    }

    @Test
    public void testParseTypes() {
        TypeParser.MyTokenizer tokens = new TypeParser.MyTokenizer("java.util.List<Integer>");
        List<JavaType> result = typeParser.parseTypes(tokens);
        assertNotNull(result);
        assertEquals(1, result.size());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseTypesException() {
        TypeParser.MyTokenizer tokens = new TypeParser.MyTokenizer("");
        typeParser.parseTypes(tokens);
    }

    @Test
    public void testFindClass() {
        String className = "java.util.List";
        TypeParser.MyTokenizer tokens = new TypeParser.MyTokenizer("");
        Class<?> result = typeParser.findClass(className, tokens);
        assertNotNull(result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFindClassException() {
        String className = "invalid.class";
        TypeParser.MyTokenizer tokens = new TypeParser.MyTokenizer("");
        typeParser.findClass(className, tokens);
    }

    @Test
    public void testProblem() {
        TypeParser.MyTokenizer tokens = new TypeParser.MyTokenizer("");
        IllegalArgumentException result = typeParser._problem(tokens, "Test Problem");
        assertNotNull(result);
    }

    @Test
    public void testMyTokenizer() {
        String str = "java.util.List";
        TypeParser.MyTokenizer tokens = new TypeParser.MyTokenizer(str);
        assertNotNull(tokens);
    }

    @Test
    public void testHasMoreTokens() {
        TypeParser.MyTokenizer tokens = new TypeParser.MyTokenizer("");
        boolean result = tokens.hasMoreTokens();
        assertFalse(result);
    }

    @Test
    public void testNextToken() {
        TypeParser.MyTokenizer tokens = new TypeParser.MyTokenizer("List,Type");
        String result1 = tokens.nextToken();
        assertEquals("List", result1);
        String result2 = tokens.nextToken();
        assertEquals("Type", result2);
    }

    @Test
    public void testPushBack() {
        TypeParser.MyTokenizer tokens = new TypeParser.MyTokenizer("");
        tokens.pushBack("Test");
        String result = tokens.nextToken();
        assertEquals("Test", result);
    }

    @Test
    public void testGetAllInput() {
        TypeParser.MyTokenizer tokens = new TypeParser.MyTokenizer("java.util.List");
        String result = tokens.getAllInput();
        assertEquals("java.util.List", result);
    }

    @Test
    public void testGetRemainingInput() {
        TypeParser.MyTokenizer tokens = new TypeParser.MyTokenizer("java.util.List");
        tokens.nextToken();
        String result = tokens.getRemainingInput();
        assertEquals("", result);
    }
}