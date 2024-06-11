package com.fasterxml.jackson.databind.type;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class TypeParserTest {

    private TypeFactory factory;
    private TypeParser parser;

    @Before
    public void setUp() {
        factory = new TypeFactory();
        parser = new TypeParser(factory);
    }

    @Test
    public void testParse_Success() {
        try {
            JavaType javaType = parser.parse("java.util.List<com.example.User>");
            assertNotNull(javaType);
            assertEquals(List.class, javaType.getRawClass());
            assertEquals(1, javaType.containedTypeCount());
            assertEquals(User.class, javaType.containedType(0).getRawClass());
        } catch (Exception e) {
            fail("Exception should not be thrown");
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParse_InvalidInput() {
        parser.parse("invalidInput");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParse_UnexpectedTokens() {
        parser.parse("java.util.List<com.example.User> unexpectedTokens");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParse_EmptyInput() {
        parser.parse("");
    }

    @Test
    public void testParseType_Success() {
        TypeParser.MyTokenizer tokens = new TypeParser.MyTokenizer("java.util.List");
        try {
            JavaType javaType = parser.parseType(tokens);
            assertNotNull(javaType);
            assertEquals(List.class, javaType.getRawClass());
            assertNull(javaType.getBindings());
        } catch (Exception e) {
            fail("Exception should not be thrown");
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseType_EmptyInput() {
        TypeParser.MyTokenizer tokens = new TypeParser.MyTokenizer("");
        parser.parseType(tokens);
    }

    @Test
    public void testParseType_GenericType() {
        TypeParser.MyTokenizer tokens = new TypeParser.MyTokenizer("java.util.List<com.example.User>");
        try {
            JavaType javaType = parser.parseType(tokens);
            assertNotNull(javaType);
            assertEquals(List.class, javaType.getRawClass());
            assertEquals(1, javaType.containedTypeCount());
            assertEquals(User.class, javaType.containedType(0).getRawClass());
        } catch (Exception e) {
            fail("Exception should not be thrown");
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseTypes_UnexpectedToken() {
        TypeParser.MyTokenizer tokens = new TypeParser.MyTokenizer("com.example.User unexpectedToken");
        parser.parseTypes(tokens);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseTypes_EmptyInput() {
        TypeParser.MyTokenizer tokens = new TypeParser.MyTokenizer("");
        parser.parseTypes(tokens);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFindClass_ClassNotFound() {
        TypeParser.MyTokenizer tokens = new TypeParser.MyTokenizer("com.example.UnknownClass");
        parser.findClass("com.example.UnknownClass", tokens);
    }
}