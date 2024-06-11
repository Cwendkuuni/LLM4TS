package com.fasterxml.jackson.databind.type;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class TypeParserTest {
    
    private TypeParser parser;
    
    @Before
    public void setUp() {
        parser = new TypeParser(new TypeFactory());
    }
    
    @Test
    public void testParse_validInput_returnsJavaType() {
        String canonical = "java.util.List<java.lang.String>";
        try {
            JavaType type = parser.parse(canonical);
            assertNotNull(type);
            assertSame(JavaType.class, type.getClass());
        } catch (IllegalArgumentException e) {
            fail("Unexpected IllegalArgumentException");
        }
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testParse_invalidInput_throwsIllegalArgumentException() {
        String canonical = "java.util.<java.lang.String>";
        JavaType type = parser.parse(canonical);
    }
    
    @Test
    public void testParseType_nonGenericInput_returnsJavaType() {
        TypeParser.MyTokenizer tokens = parser.new MyTokenizer("java.util.List");
        try {
            JavaType type = parser.parseType(tokens);
            assertNotNull(type);
            assertSame(JavaType.class, type.getClass());
        } catch (IllegalArgumentException e) {
            fail("Unexpected IllegalArgumentException");
        }
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testParseType_invalidInput_throwsIllegalArgumentException() {
        TypeParser.MyTokenizer tokens = parser.new MyTokenizer("");
        JavaType type = parser.parseType(tokens);
    }
    
    @Test
    public void testParseType_genericInput_returnsJavaType() {
        TypeParser.MyTokenizer tokens = parser.new MyTokenizer("java.util.List<java.lang.String>");
        try {
            JavaType type = parser.parseType(tokens);
            assertNotNull(type);
            assertSame(JavaType.class, type.getClass());
        } catch (IllegalArgumentException e) {
            fail("Unexpected IllegalArgumentException");
        }
    }
    
    @Test
    public void testParseTypes_validInput_returnsList() {
        TypeParser.MyTokenizer tokens = parser.new MyTokenizer("java.lang.String, java.lang.Integer, java.lang.Boolean");
        try {
            List<JavaType> types = parser.parseTypes(tokens);
            assertNotNull(types);
            assertEquals(3, types.size());
            for (JavaType type : types) {
                assertSame(JavaType.class, type.getClass());
            }
        } catch (IllegalArgumentException e) {
            fail("Unexpected IllegalArgumentException");
        }
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testParseTypes_invalidInput_throwsIllegalArgumentException() {
        TypeParser.MyTokenizer tokens = parser.new MyTokenizer("");
        List<JavaType> types = parser.parseTypes(tokens);
    }
    
    @Test
    public void testFindClass_validClassName_returnsClass() {
        String className = "java.util.List";
        TypeParser.MyTokenizer tokens = parser.new MyTokenizer("");
        Class<?> cls = parser.findClass(className, tokens);
        assertNotNull(cls);
        assertSame(Class.class, cls.getClass());
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testFindClass_invalidClassName_throwsIllegalArgumentException() {
        String className = "java.util...";
        TypeParser.MyTokenizer tokens = parser.new MyTokenizer("");
        Class<?> cls = parser.findClass(className, tokens);
    }
    
    @Test
    public void testProblem_validTokens_returnsIllegalArgumentException() {
        TypeParser.MyTokenizer tokens = parser.new MyTokenizer("java.util.List");
        String msg = "Unexpected end-of-string";
        IllegalArgumentException exception = parser._problem(tokens, msg);
        assertNotNull(exception);
        assertEquals(String.format("Failed to parse type '%s' (remaining: '%s'): %s",
                tokens.getAllInput(), tokens.getRemainingInput(), msg), exception.getMessage());
    }
}