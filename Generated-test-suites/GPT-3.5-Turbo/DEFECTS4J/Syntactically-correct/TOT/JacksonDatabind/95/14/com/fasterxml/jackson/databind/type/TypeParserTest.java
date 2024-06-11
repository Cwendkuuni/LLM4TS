package com.fasterxml.jackson.databind.type;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class TypeParserTest {

    private TypeParser typeParser;

    @Before
    public void setup() {
        typeParser = new TypeParser(new TypeFactory());
    }

    @Test
    public void testParse_withNullCanonical_throwsIllegalArgumentException() {
        try {
            typeParser.parse(null);
            fail("Expected IllegalArgumentException was not thrown");
        } catch (IllegalArgumentException e) {
            assertEquals("Failed to parse type 'null' (remaining: 'null'): Unexpected end-of-string", e.getMessage());
        }
    }

    @Test
    public void testParse_withEmptyCanonical_throwsIllegalArgumentException() {
        try {
            typeParser.parse("");
            fail("Expected IllegalArgumentException was not thrown");
        } catch (IllegalArgumentException e) {
            assertEquals("Failed to parse type '' (remaining: ''): Unexpected end-of-string", e.getMessage());
        }
    }

    @Test
    public void testParse_withInvalidCanonical_throwsIllegalArgumentException() {
        try {
            typeParser.parse("com.fasterxml.jackson.databind.type");
            fail("Expected IllegalArgumentException was not thrown");
        } catch (IllegalArgumentException e) {
            assertEquals("Failed to parse type 'com.fasterxml.jackson.databind.type' (remaining: 'com.fasterxml.jackson.databind.type'): Unexpected end-of-string", e.getMessage());
        }
    }

    @Test
    public void testParse_withValidCanonical_returnsJavaType() {
        try {
            JavaType javaType = typeParser.parse("java.util.List<java.lang.String>");
            assertNotNull(javaType);
            assertEquals("java.util.List<java.lang.String>", javaType.getGenericSignature());
            assertEquals("java.util.List", javaType.getRawClass().getName());
            assertEquals("java.lang.String", javaType.getBindings().getBoundType(0).getRawClass().getName());
        } catch (IllegalArgumentException e) {
            fail("Unexpected IllegalArgumentException was thrown");
        }
    }

    @Test
    public void testParse_withInvalidTypeArguments_throwsIllegalArgumentException() {
        try {
            typeParser.parse("java.util.List<java.lang.Unknown>");
            fail("Expected IllegalArgumentException was not thrown");
        } catch (IllegalArgumentException e) {
            assertEquals("Failed to parse type 'java.util.List<java.lang.Unknown>' (remaining: 'java.lang.Unknown>'): Can not locate class 'java.lang.Unknown', problem: null", e.getMessage());
        }
    }

    @Test
    public void testParse_withAdditionalTokensAfterType_throwsIllegalArgumentException() {
        try {
            typeParser.parse("java.util.List<java.lang.String> extraTokens");
            fail("Expected IllegalArgumentException was not thrown");
        } catch (IllegalArgumentException e) {
            assertEquals("Failed to parse type 'java.util.List<java.lang.String> extraTokens' (remaining: 'extraTokens'): Unexpected tokens after complete type", e.getMessage());
        }
    }

    @Test
    public void testParse_withIncompleteType_throwsIllegalArgumentException() {
        try {
            typeParser.parse("java.util.List<java.lang.String");
            fail("Expected IllegalArgumentException was not thrown");
        } catch (IllegalArgumentException e) {
            assertEquals("Failed to parse type 'java.util.List<java.lang.String' (remaining: 'java.util.List<java.lang.String'): Unexpected end-of-string", e.getMessage());
        }
    }

    @Test
    public void testFindClass_withExistingClass_returnsClass() {
        try {
            Class<?> clazz = typeParser.findClass("java.util.List", new TypeParser.MyTokenizer("java.util.List"));
            assertNotNull(clazz);
            assertEquals("java.util.List", clazz.getName());
        } catch (IllegalArgumentException e) {
            fail("Unexpected IllegalArgumentException was thrown");
        }
    }

    @Test
    public void testFindClass_withNonExistingClass_throwsIllegalArgumentException() {
        try {
            typeParser.findClass("com.unknown.Class", new TypeParser.MyTokenizer("com.unknown.Class"));
            fail("Expected IllegalArgumentException was not thrown");
        } catch (IllegalArgumentException e) {
            assertEquals("Failed to parse type 'null' (remaining: 'null'): Can not locate class 'com.unknown.Class', problem: null", e.getMessage());
        }
    }

    @Test
    public void testParseTypes_withEmptyCanonical_throwsIllegalArgumentException() {
        try {
            typeParser.parseTypes(new TypeParser.MyTokenizer(""));
            fail("Expected IllegalArgumentException was not thrown");
        } catch (IllegalArgumentException e) {
            assertEquals("Failed to parse type '' (remaining: ''): Unexpected end-of-string", e.getMessage());
        }
    }

    @Test
    public void testParseTypes_withValidCanonical_returnsJavaTypeList() {
        try {
            TypeParser.MyTokenizer tokenizer = new TypeParser.MyTokenizer("java.lang.String, java.lang.Integer");
            JavaType javaType1 = typeParser.parseTypes(tokenizer).get(0);
            assertNotNull(javaType1);
            assertEquals("java.lang.String", javaType1.getRawClass().getName());

            JavaType javaType2 = typeParser.parseTypes(tokenizer).get(1);
            assertNotNull(javaType2);
            assertEquals("java.lang.Integer", javaType2.getRawClass().getName());
        } catch (IllegalArgumentException e) {
            fail("Unexpected IllegalArgumentException was thrown");
        }
    }

    @Test
    public void testParseTypes_withInvalidTypeArguments_throwsIllegalArgumentException() {
        try {
            TypeParser.MyTokenizer tokenizer = new TypeParser.MyTokenizer("java.lang.String, java.lang.Unknown");
            typeParser.parseTypes(tokenizer);
            fail("Expected IllegalArgumentException was not thrown");
        } catch (IllegalArgumentException e) {
            assertEquals("Failed to parse type 'java.lang.Unknown' (remaining: 'java.lang.Unknown'): Can not locate class 'java.lang.Unknown', problem: null", e.getMessage());
        }
    }

    @Test
    public void testParseTypes_withAdditionalTokensAfterTypes_throwsIllegalArgumentException() {
        try {
            TypeParser.MyTokenizer tokenizer = new TypeParser.MyTokenizer("java.lang.String, java.lang.Integer extraTokens");
            typeParser.parseTypes(tokenizer);
            fail("Expected IllegalArgumentException was not thrown");
        } catch (IllegalArgumentException e) {
            assertEquals("Failed to parse type 'java.lang.Integer extraTokens' (remaining: 'extraTokens'): Unexpected tokens after complete type", e.getMessage());
        }
    }

    @Test
    public void testParseTypes_withIncompleteType_throwsIllegalArgumentException() {
        try {
            TypeParser.MyTokenizer tokenizer = new TypeParser.MyTokenizer("java.lang.String, java.lang.IntegerextraTokens");
            typeParser.parseTypes(tokenizer);
            fail("Expected IllegalArgumentException was not thrown");
        } catch (IllegalArgumentException e) {
            assertEquals("Failed to parse type 'java.lang.IntegerextraTokens' (remaining: 'java.lang.IntegerextraTokens'): Unexpected end-of-string", e.getMessage());
        }
    }
}