package com.fasterxml.jackson.databind.type;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class TypeParserTest {
    
    private TypeParser typeParser;
    
    @Before
    public void setup() {
        typeParser = new TypeParser(new TypeFactory());
    }
    
    @Test
    public void testParse_SingleNonGenericType_ReturnsJavaType() {
        String input = "java.lang.String";
        JavaType result = typeParser.parse(input);
        assertEquals(JavaType.class, result.getClass());
        assertEquals(String.class, result.getRawClass());
    }
    
    @Test
    public void testParse_TopLevelGenericClass_ReturnsJavaTypeWithParameterizedType() {
        String input = "java.util.List<java.lang.String>";
        JavaType result = typeParser.parse(input);
        assertEquals(JavaType.class, result.getClass());
        assertEquals(List.class, result.getRawClass());
        
        JavaType[] typeArguments = result.getBindings().getTypeParameters();
        assertEquals(1, typeArguments.length);
        assertEquals(String.class, typeArguments[0].getRawClass());
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testParse_InvalidEnd_ReturnsIllegalArgumentException() {
        String input = "java.util.List<java.lang.String>;";
        typeParser.parse(input);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testParse_InvalidTypeName_ReturnsIllegalArgumentException() {
        String input = "java.lnag.String";
        typeParser.parse(input);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testParse_TrailingTokens_ReturnsIllegalArgumentException() {
        String input = "java.util.List<java.lang.String> extra";
        typeParser.parse(input);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testParse_InvalidTypeSeparator_ReturnsIllegalArgumentException() {
        String input = "java.util.List<java.lang.String>; java.util.Set<java.lang.Integer>";
        typeParser.parse(input);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testParse_InvalidParameterSeparator_ReturnsIllegalArgumentException() {
        String input = "java.util.List<java.lang.String, java.util.Set<java.lang.Integer>>";
        typeParser.parse(input);
    }
}