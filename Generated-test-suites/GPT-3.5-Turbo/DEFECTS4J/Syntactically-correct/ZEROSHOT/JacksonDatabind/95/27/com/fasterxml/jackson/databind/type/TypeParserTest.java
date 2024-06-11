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
            // Test case 1: Empty string as input
            String canonical1 = "";
            JavaType result1 = typeParser.parse(canonical1);
            assertNull(result1);

            // Test case 2: Non-empty string as input
            String canonical2 = "java.util.List";
            JavaType result2 = typeParser.parse(canonical2);
            assertNotNull(result2);
            assertTrue(result2 instanceof SimpleType);
            assertEquals("java.util.List<java.lang.Object>", result2.toCanonical());
        } catch (IllegalArgumentException e) {
            fail("Unexpected IllegalArgumentException");
        }
    }

    @Test
    public void testParseType() {
        try {
            // Test case 1: Empty input
            TypeParser.MyTokenizer tokens1 = new TypeParser.MyTokenizer("");
            IllegalArgumentException e1 = assertThrows(IllegalArgumentException.class, () -> typeParser.parseType(tokens1));
            assertEquals("Failed to parse type '' (remaining: ''): Unexpected end-of-string", e1.getMessage());
            assertFalse(tokens1.hasMoreTokens());

            // Test case 2: End-of-string flag not set
            String input2 = "java.lang.String";
            TypeParser.MyTokenizer tokens2 = new TypeParser.MyTokenizer(input2);
            JavaType result2 = typeParser.parseType(tokens2);
            assertNotNull(result2);
            assertTrue(result2 instanceof SimpleType);
            assertEquals("java.lang.String", result2.toCanonical());
            assertEquals("", tokens2.getRemainingInput());

            // Test case 3: End-of-string flag set after class name
            String input3 = "java.util.List<java.lang.Integer";
            TypeParser.MyTokenizer tokens3 = new TypeParser.MyTokenizer(input3);
            IllegalArgumentException e3 = assertThrows(IllegalArgumentException.class, () -> typeParser.parseType(tokens3));
            assertEquals("Failed to parse type 'java.util.List<java.lang.Integer' (remaining: ''): Unexpected end-of-string", e3.getMessage());
            assertEquals("java.util.List<java.lang.Integer", tokens3.getAllInput());
            assertEquals("", tokens3.getRemainingInput());

            // Test case 4: End-of-string flag set after opening '<'
            String input4 = "java.util.List<";
            TypeParser.MyTokenizer tokens4 = new TypeParser.MyTokenizer(input4);
            List<JavaType> parameterTypes4 = typeParser.parseTypes(tokens4);
            assertNotNull(parameterTypes4);
            assertTrue(parameterTypes4.isEmpty());
            assertEquals(">", tokens4.getRemainingInput());

            // Test case 5: Valid input
            String input5 = "java.util.Map<java.lang.String, java.util.List<java.lang.Integer>>";
            TypeParser.MyTokenizer tokens5 = new TypeParser.MyTokenizer(input5);
            JavaType result5 = typeParser.parseType(tokens5);
            assertNotNull(result5);
            assertTrue(result5 instanceof SimpleType);
            assertEquals("java.util.Map<java.lang.String, java.util.List<java.lang.Integer>>", result5.toCanonical());
            assertEquals("", tokens5.getRemainingInput());

        } catch (IllegalArgumentException e) {
            fail("Unexpected IllegalArgumentException");
        }
    }

    @Test
    public void testParseTypes() {
        try {
            // Test case 1: Empty string as input
            TypeParser.MyTokenizer tokens1 = new TypeParser.MyTokenizer("");
            IllegalArgumentException e1 = assertThrows(IllegalArgumentException.class, () -> typeParser.parseTypes(tokens1));
            assertEquals("Failed to parse type '' (remaining: ''): Unexpected end-of-string", e1.getMessage());
            assertFalse(tokens1.hasMoreTokens());

            // Test case 2: Single type as input
            String input2 = "java.lang.Integer";
            TypeParser.MyTokenizer tokens2 = new TypeParser.MyTokenizer(input2);
            List<JavaType> types2 = typeParser.parseTypes(tokens2);
            assertNotNull(types2);
            assertEquals(1, types2.size());
            JavaType type2 = types2.get(0);
            assertNotNull(type2);
            assertTrue(type2 instanceof SimpleType);
            assertEquals("java.lang.Integer", type2.toCanonical());
            assertEquals("", tokens2.getRemainingInput());

            // Test case 3: Multiple types as input
            String input3 = "java.lang.Integer, java.lang.String, java.util.Date";
            TypeParser.MyTokenizer tokens3 = new TypeParser.MyTokenizer(input3);
            List<JavaType> types3 = typeParser.parseTypes(tokens3);
            assertNotNull(types3);
            assertEquals(3, types3.size());
            JavaType type3a = types3.get(0);
            assertNotNull(type3a);
            assertTrue(type3a instanceof SimpleType);
            assertEquals("java.lang.Integer", type3a.toCanonical());
            JavaType type3b = types3.get(1);
            assertNotNull(type3b);
            assertTrue(type3b instanceof SimpleType);
            assertEquals("java.lang.String", type3b.toCanonical());
            JavaType type3c = types3.get(2);
            assertNotNull(type3c);
            assertTrue(type3c instanceof SimpleType);
            assertEquals("java.util.Date", type3c.toCanonical());
            assertEquals("", tokens3.getRemainingInput());

            // Test case 4: Invalid input
            String input4 = "java.lang.Integer, java.lang.String, java.util.Date,";
            TypeParser.MyTokenizer tokens4 = new TypeParser.MyTokenizer(input4);
            IllegalArgumentException e4 = assertThrows(IllegalArgumentException.class, () -> typeParser.parseTypes(tokens4));
            assertEquals("Failed to parse type 'java.lang.Integer, java.lang.String, java.util.Date,' (remaining: ''): Unexpected end-of-string", e4.getMessage());
            assertEquals(">", tokens4.getAllInput());
            assertEquals("", tokens4.getRemainingInput());

        } catch (IllegalArgumentException e) {
            fail("Unexpected IllegalArgumentException");
        }
    }

    @Test
    public void testFindClass() {
        try {
            // Test case 1: Class not found
            String className1 = "com.fasterxml.jackson.NonexistentClass";
            TypeParser.MyTokenizer tokens1 = new TypeParser.MyTokenizer("");
            IllegalArgumentException e1 = assertThrows(IllegalArgumentException.class, () -> typeParser.findClass(className1, tokens1));
            assertEquals("Can not locate class 'com.fasterxml.jackson.NonexistentClass', problem: null", e1.getMessage());
            assertEquals("", tokens1.getRemainingInput());

            // Test case 2: Class found
            String className2 = "java.util.List";
            TypeParser.MyTokenizer tokens2 = new TypeParser.MyTokenizer("");
            Class<?> result2 = typeParser.findClass(className2, tokens2);
            assertNotNull(result2);
            assertEquals(ArrayList.class, result2);
            assertEquals("", tokens2.getRemainingInput());
        } catch (IllegalArgumentException e) {
            fail("Unexpected IllegalArgumentException");
        }
    }

    @Test
    public void testProblem() {
        try {
            // Test case 1: Empty input
            TypeParser.MyTokenizer tokens1 = new TypeParser.MyTokenizer("");
            IllegalArgumentException e1 = typeParser._problem(tokens1, "Custom message");
            assertEquals("Failed to parse type '' (remaining: ''): Custom message", e1.getMessage());

            // Test case 2: Non-empty input
            String input2 = "java.lang.String";
            TypeParser.MyTokenizer tokens2 = new TypeParser.MyTokenizer(input2);
            IllegalArgumentException e2 = typeParser._problem(tokens2, "Custom message");
            assertEquals("Failed to parse type 'java.lang.String' (remaining: ''): Custom message", e2.getMessage());
        } catch (IllegalArgumentException e) {
            fail("Unexpected IllegalArgumentException");
        }
    }
}