package com.fasterxml.jackson.databind.type;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.TypeFactory;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class TypeParserTest {

    private TypeParser typeParser;

    @Before
    public void setUp() {
        TypeFactory typeFactory = new TypeFactory();
        typeParser = new TypeParser(typeFactory);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testEmptyInput() {
        typeParser.parse("");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testUnexpectedEndOfString() {
        typeParser.parse("java.util.List<");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testUnexpectedTokensAfterCompleteType() {
        typeParser.parse("java.util.List<> unexpectedToken");
    }

    @Test
    public void testNonGenericJavaType() {
        JavaType javaType = typeParser.parse("java.lang.String");
        
        assertEquals(String.class, javaType.getRawClass());
        assertEquals(null, javaType.getBindings());
    }

    @Test
    public void testGenericJavaType() {
        JavaType javaType = typeParser.parse("java.util.List<java.lang.String>");

        assertEquals(List.class, javaType.getRawClass());
        
        List<JavaType> parameterTypes = javaType.getBindings().getTypeParameters();
        assertEquals(1, parameterTypes.size());
        assertTrue(parameterTypes.get(0) instanceof JavaType);
        assertEquals(String.class, parameterTypes.get(0).getRawClass());
    }

    @Test
    public void testRecursiveGenericJavaType() {
        JavaType javaType = typeParser.parse("java.util.Map<java.lang.String, java.util.List<java.lang.Long>>");

        assertEquals(Map.class, javaType.getRawClass());

        List<JavaType> parameterTypes = javaType.getBindings().getTypeParameters();
        assertEquals(2, parameterTypes.size());

        assertTrue(parameterTypes.get(0) instanceof JavaType);
        assertEquals(String.class, parameterTypes.get(0).getRawClass());

        assertTrue(parameterTypes.get(1) instanceof JavaType);

        JavaType innerJavaType = parameterTypes.get(1);
        assertEquals(List.class, innerJavaType.getRawClass());

        List<JavaType> innerParameterTypes = innerJavaType.getBindings().getTypeParameters();
        assertEquals(1, innerParameterTypes.size());
        assertTrue(innerParameterTypes.get(0) instanceof JavaType);
        assertEquals(Long.class, innerParameterTypes.get(0).getRawClass());
    }

    @Test
    public void testMultipleGenericJavaTypes() {
        JavaType javaType = typeParser.parse("java.util.Map<java.lang.String, java.util.List<java.lang.Long>," +
                " java.util.Set<java.lang.String>>");

        assertEquals(Map.class, javaType.getRawClass());
        
        List<JavaType> parameterTypes = javaType.getBindings().getTypeParameters();
        assertEquals(3, parameterTypes.size());

        assertTrue(parameterTypes.get(0) instanceof JavaType);
        assertEquals(String.class, parameterTypes.get(0).getRawClass());

        assertTrue(parameterTypes.get(1) instanceof JavaType);

        JavaType innerJavaType1 = parameterTypes.get(1);
        assertEquals(List.class, innerJavaType1.getRawClass());

        List<JavaType> innerParameterTypes1 = innerJavaType1.getBindings().getTypeParameters();
        assertEquals(1, innerParameterTypes1.size());
        assertTrue(innerParameterTypes1.get(0) instanceof JavaType);
        assertEquals(Long.class, innerParameterTypes1.get(0).getRawClass());

        assertTrue(parameterTypes.get(2) instanceof JavaType);

        JavaType innerJavaType2 = parameterTypes.get(2);
        assertEquals(Set.class, innerJavaType2.getRawClass());

        List<JavaType> innerParameterTypes2 = innerJavaType2.getBindings().getTypeParameters();
        assertEquals(1, innerParameterTypes2.size());
        assertTrue(innerParameterTypes2.get(0) instanceof JavaType);
        assertEquals(String.class, innerParameterTypes2.get(0).getRawClass());
    }

    @Test
    public void testWildcardGenericJavaType() {
        JavaType javaType = typeParser.parse("java.util.Map<?, ?>");

        assertEquals(Map.class, javaType.getRawClass());

        List<JavaType> parameterTypes = javaType.getBindings().getTypeParameters();
        assertEquals(2, parameterTypes.size());

        assertTrue(parameterTypes.get(0) instanceof JavaType);
        assertEquals(Object.class, parameterTypes.get(0).getRawClass());

        assertTrue(parameterTypes.get(1) instanceof JavaType);
        assertEquals(Object.class, parameterTypes.get(1).getRawClass());
    }

    @Test
    public void testGenericJavaTypeWithMultipleTypeParameters() {
        JavaType javaType = typeParser.parse("java.util.Map<java.lang.Integer, java.lang.String," +
                " java.util.List<java.lang.Long>>");

        assertEquals(Map.class, javaType.getRawClass());

        List<JavaType> parameterTypes = javaType.getBindings().getTypeParameters();
        assertEquals(3, parameterTypes.size());

        assertTrue(parameterTypes.get(0) instanceof JavaType);
        assertEquals(Integer.class, parameterTypes.get(0).getRawClass());

        assertTrue(parameterTypes.get(1) instanceof JavaType);
        assertEquals(String.class, parameterTypes.get(1).getRawClass());

        assertTrue(parameterTypes.get(2) instanceof JavaType);

        JavaType innerJavaType = parameterTypes.get(2);
        assertEquals(List.class, innerJavaType.getRawClass());

        List<JavaType> innerParameterTypes = innerJavaType.getBindings().getTypeParameters();
        assertEquals(1, innerParameterTypes.size());
        assertTrue(innerParameterTypes.get(0) instanceof JavaType);
        assertEquals(Long.class, innerParameterTypes.get(0).getRawClass());
    }

    @Test
    public void testExceptionType() {
        JavaType javaType = typeParser.parse("java.lang.Exception");

        assertEquals(Exception.class, javaType.getRawClass());
        assertEquals(null, javaType.getBindings());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testClassNotFoundException() {
        typeParser.parse("com.example.NonExistentClass");
    }
}