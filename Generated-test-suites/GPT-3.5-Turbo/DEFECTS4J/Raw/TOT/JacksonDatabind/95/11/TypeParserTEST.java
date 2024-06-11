
package com.fasterxml.jackson.databind.type;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class TypeParserTest {

    private TypeParser typeParser;

    @Before
    public void setup() {
        TypeFactory factory = new TypeFactory();
        typeParser = new TypeParser(factory);
    }

    @Test
    public void testParse_validCanonicalString_returnsJavaTypeObject() {
        // Arrange
        String canonical = "java.util.List<java.lang.String>";

        // Act
        JavaType result = typeParser.parse(canonical);

        // Assert
        assertNotNull(result);
        assertTrue(result instanceof JavaType);
        assertEquals(canonical, result.getCanonicalName());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParse_invalidCanonicalString_throwsIllegalArgumentException() {
        // Arrange
        String canonical = "java.util.@#$List";

        // Act
        typeParser.parse(canonical);

        // Assert
        // Expected exception
    }

    @Test
    public void testParse_emptyCanonicalString_throwsIllegalArgumentException() {
        // Arrange
        String canonical = "";

        // Act
        try {
            typeParser.parse(canonical);
            
            // Assert
            fail("Exception not thrown");
        } catch (IllegalArgumentException e) {
            assertEquals("Failed to parse type '', remaining: '': Unexpected end-of-string", e.getMessage());
        }
    }

    @Test
    public void testParse_validMultipleGenerics_returnsJavaTypeObjectWithParameterTypes() {
        // Arrange
        String canonical = "java.util.Map<java.lang.String, java.util.List<java.lang.Integer>>";

        // Act
        JavaType result = typeParser.parse(canonical);

        // Assert
        assertNotNull(result);
        assertTrue(result instanceof JavaType);
        assertEquals(canonical, result.getCanonicalName());
        assertNotNull(result.getBindings());
        assertEquals(2, result.getBindings().size());
        assertEquals("java.lang.String", result.getBindings().getBoundTypeName(0));
        assertEquals("java.util.List<java.lang.Integer>", result.getBindings().getBoundTypeName(1));
    }

    @Test
    public void testFindClass_validClassName_returnsClassObject() {
        // Arrange
        String className = "java.lang.String";
        MyTokenizer tokens = new MyTokenizer(className);

        // Act
        Class<?> result = typeParser.findClass(className, tokens);

        // Assert
        assertNotNull(result);
        assertEquals(String.class, result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFindClass_invalidClassName_throwsIllegalArgumentException() {
        // Arrange
        String className = "unknown.class.name";
        MyTokenizer tokens = new MyTokenizer(className);

        // Act
        typeParser.findClass(className, tokens);

        // Assert
        // Expected exception
    }

    @Test
    public void testFindClass_runtimeExceptionClass_throwsRuntimeException() {
        // Arrange
        String className = "java.lang.RuntimeException";
        MyTokenizer tokens = new MyTokenizer(className);

        // Act
        try {
            typeParser.findClass(className, tokens);

            // Assert
            fail("Exception not thrown");
        } catch (RuntimeException e) {
            assertEquals("Can not locate class 'java.lang.RuntimeException', problem: null", e.getMessage());
        }
    }

    @Test
    public void testParseType_validBaseType_returnsJavaTypeObject() {
        // Arrange
        String canonical = "java.lang.String";
        MyTokenizer tokens = new MyTokenizer(canonical);

        // Act
        JavaType result = typeParser.parseType(tokens);

        // Assert
        assertNotNull(result);
        assertTrue(result instanceof JavaType);
        assertEquals(canonical, result.getCanonicalName());
        assertNull(result.getBindings());
    }

    @Test
    public void testParseType_validGenericType_returnsJavaTypeObjectWithBindings() {
        // Arrange
        String canonical = "java.util.List<java.lang.String>";
        MyTokenizer tokens = new MyTokenizer(canonical);

        // Act
        JavaType result = typeParser.parseType(tokens);

        // Assert
        assertNotNull(result);
        assertTrue(result instanceof JavaType);
        assertEquals(canonical, result.getCanonicalName());
        assertNotNull(result.getBindings());
        assertEquals(1, result.getBindings().size());
        assertEquals("java.lang.String", result.getBindings().getBoundTypeName(0));
    }

    @Test
    public void testParseType_emptyCanonicalString_throwsIllegalArgumentException() {
        // Arrange
        String canonical = "";
        MyTokenizer tokens = new MyTokenizer(canonical);

        // Act
        try {
            typeParser.parseType(tokens);

            // Assert
            fail("Exception not thrown");
        } catch (IllegalArgumentException e) {
            assertEquals("Failed to parse type '', remaining: '': Unexpected end-of-string", e.getMessage());
        }
    }

    @Test
    public void testParseType_invalidTokenString_returnsJavaTypeObjectWithoutBindings() {
        // Arrange
        String canonical = "java.lang.*";
        MyTokenizer tokens = new MyTokenizer(canonical);

        // Act
        JavaType result = typeParser.parseType(tokens);

        // Assert
        assertNotNull(result);
        assertTrue(result instanceof JavaType);
        assertEquals(canonical, result.getCanonicalName());
        assertNull(result.getBindings());
    }

    @Test
    public void testParseTypes_validMultipleGenericTypes_returnsListOfJavaTypeObjects() {
        // Arrange
        String canonical = "java.lang.String, java.util.List<java.lang.Integer>, java.util.Map<java.lang.Character, java.lang.Boolean>";
        MyTokenizer tokens = new MyTokenizer(canonical);

        // Act
        List<JavaType> result = typeParser.parseTypes(tokens);

        // Assert
        assertNotNull(result);
        assertEquals(3, result.size());
        assertEquals("java.lang.String", result.get(0).getCanonicalName());
        assertEquals("java.util.List<java.lang.Integer>", result.get(1).getCanonicalName());
        assertEquals("java.util.Map<java.lang.Character, java.lang.Boolean>", result.get(2).getCanonicalName());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseTypes_invalidTokenString_throwsIllegalArgumentException() {
        // Arrange
        String canonical = "java.lang.String, , java.util.List<java.lang.Integer>";
        MyTokenizer tokens = new MyTokenizer(canonical);

        // Act
        typeParser.parseTypes(tokens);

        // Assert
        // Expected exception
    }

    @Test
    public void testParseTypes_emptyCanonicalString_throwsIllegalArgumentException() {
        // Arrange
        String canonical = "";
        MyTokenizer tokens = new MyTokenizer(canonical);

        // Act
        try {
            typeParser.parseTypes(tokens);

            // Assert
            fail("Exception not thrown");
        } catch (IllegalArgumentException e) {
            assertEquals("Failed to parse type '', remaining: '': Unexpected end-of-string", e.getMessage());
        }
    }
}

class MyTokenizerTest {

    @Test
    public void testHasMoreTokens_withPushbackToken_returnsTrue() {
        // Arrange
        MyTokenizer tokenizer = new MyTokenizer("java.util.List<java.lang.String>");
        tokenizer.pushBack("<");

        // Act
        boolean result = tokenizer.hasMoreTokens();

        // Assert
        assertTrue(result);
    }

    @Test
    public void testHasMoreTokens_withoutPushbackToken_returnsTrue() {
        // Arrange
        MyTokenizer tokenizer = new MyTokenizer("java.util.List<java.lang.String>");

        // Act
        boolean result = tokenizer.hasMoreTokens();

        // Assert
        assertTrue(result);
    }

    @Test
    public void testNextToken_withoutPushbackToken_returnsNextToken() {
        // Arrange
        MyTokenizer tokenizer = new MyTokenizer("java.util.List<java.lang.String>");

        // Act
        String result = tokenizer.nextToken();

        // Assert
        assertEquals("java.util.List", result);
    }

    @Test
    public void testNextToken_withPushbackToken_returnsPushbackToken() {
        // Arrange
        MyTokenizer tokenizer = new MyTokenizer("java.util.List<java.lang.String>");
        tokenizer.pushBack("<");

        // Act
        String result = tokenizer.nextToken();

        // Assert
        assertEquals("<", result);
    }

    @Test
    public void testPushBack_withToken_pushesTokenToStack() {
        // Arrange
        MyTokenizer tokenizer = new MyTokenizer("java.util.List<java.lang.String>");

        // Act
        tokenizer.pushBack("<");

        // Assert
        assertTrue(tokenizer.hasMoreTokens());
        assertEquals("<", tokenizer.nextToken());
    }

    @Test
    public void testGetAllInput_returnsInputString() {
        // Arrange
        MyTokenizer tokenizer = new MyTokenizer("java.util.List<java.lang.String>");

        // Act
        String result = tokenizer.getAllInput();

        // Assert
        assertEquals("java.util.List<java.lang.String>", result);
    }

    @Test
    public void testGetRemainingInput_returnsRemainingInputString() {
        // Arrange
        MyTokenizer tokenizer = new MyTokenizer("java.util.List<java.lang.String>");

        // Act
        tokenizer.nextToken();
        String result = tokenizer.getRemainingInput();

        // Assert
        assertEquals("<java.lang.String>", result);
    }
}
