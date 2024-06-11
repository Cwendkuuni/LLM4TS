package com.fasterxml.jackson.databind.type;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

public class TypeParserTest {

    private TypeFactory typeFactory = new BasicTypeFactory();
    private TypeParser typeParser = new TypeParser(typeFactory);

    @Test
    public void testParseBasic() throws IllegalArgumentException {
        String canonical = "String";
        JavaType javaType = typeParser.parse(canonical);
        assertEquals("java.lang.String", javaType.getClassName());
    }

    @Test
    public void testParseClassWithGenerics() throws IllegalArgumentException {
        String canonical = "List<Integer>";
        JavaType javaType = typeParser.parse(canonical);
        assertEquals("java.util.List", javaType.getClassName());
        assertTrue(javaType.hasGenericTypes());
        List<JavaType> typeArguments = javaType.getGenerics();
        assertEquals(java.lang.Integer.class, typeArguments.get(0).getRawClass());
    }

    @Test
    public void testParseComplexClassWithGenerics() throws IllegalArgumentException {
        String canonical = "Map<String, List<Integer>>";
        JavaType javaType = typeParser.parse(canonical);
        assertEquals("java.util.Map", javaType.getClassName());
        assertTrue(javaType.hasGenericTypes());
        TypeBindings typeBindings = javaType.getTypeBindings();
        assertEquals(java.lang.String.class, typeBindings.getKeyType().getRawClass());
        List<JavaType> valueTypes = javaType.getGenerics();
        assertEquals(valueTypeSize(2), valueTypes.size());
        assertEquals(java.lang.Integer.class, valueTypes.get(0).getRawClass());
        assertEquals(java.util.List.class, valueTypes.get(1).getRawClass());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseInvalidCanonical() throws IllegalArgumentException {
        String canonical = "List<Integer>abc";
        typeParser.parse(canonical);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseInvalidCanonicalWithExtraTokens() throws IllegalArgumentException {
        String canonical = "List<Integer>abc , xyz";
        typeParser.parse(canonical);
    }

    private int valueTypeSize(int n) {
        if (n == 0) {
            return 0;
        } else {
            return valueTypeSize(n - 1) + 1;
        }
    }
}