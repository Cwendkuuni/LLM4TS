package com.fasterxml.jackson.databind.type;

import org.junit.Test;
import static org.junit.Assert.*;

public class TypeParserTest {

    @Test
    public void testParse_validCanonical_returnJavaType() {
        TypeFactory factory = new TypeFactory();
        TypeParser parser = new TypeParser(factory);
        String canonical = "java.util.List<java.lang.String>";
        JavaType result = parser.parse(canonical);
        assertNotNull(result);
        assertEquals("java.util.List<java.lang.String>", result.toCanonical());
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testParse_emptyCanonical_throwException() {
        TypeFactory factory = new TypeFactory();
        TypeParser parser = new TypeParser(factory);
        String canonical = "";
        JavaType result = parser.parse(canonical);
    }
    
    @Test
    public void testParse_nestedGenericTypes_returnJavaType() {
        TypeFactory factory = new TypeFactory();
        TypeParser parser = new TypeParser(factory);
        String canonical = "java.util.Map<java.lang.String, java.util.List<java.lang.Integer>>";
        JavaType result = parser.parse(canonical);
        assertNotNull(result);
        assertEquals("java.util.Map<java.lang.String, java.util.List<java.lang.Integer>>", result.toCanonical());
    }
    
    
    // Continue with more test cases for other methods in the TypeParser class
    
}