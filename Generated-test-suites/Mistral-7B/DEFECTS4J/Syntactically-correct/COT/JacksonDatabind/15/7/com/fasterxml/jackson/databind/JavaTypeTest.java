package com.fasterxml.jackson.databind.test;

import static org.junit.Assert.*;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.TypeFactory;

import org.junit.Test;

/**
 * Test class for JavaType class.
 */
public class JavaTypeTest {

    @Test
    public void testConstructor() {
        Class<String> rawType = String.class;
        JavaType javaType = new JavaType(rawType, 0, null, null, false);
        assertEquals(rawType, javaType.getRawClass());
    }

    @Test
    public void testWithTypeHandler() {
        Class<String> rawType = String.class;
        Object valueHandler = new Object();
        JavaType javaType = new JavaType(rawType, 0, null, null, false);
        JavaType withTypeHandler = javaType.withTypeHandler(valueHandler);
        assertNotSame(javaType, withTypeHandler);
        assertEquals(javaType.getRawClass(), withTypeHandler.getRawClass());
        assertEquals(valueHandler, withTypeHandler.getTypeHandler());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testCopyMethodsWithUnsupportedType() {
        Class<String> rawType = String.class;
        JavaType javaType = new JavaType(rawType, 0, null, null, false);
        javaType.withTypeHandler(new Object()); // Passed for compilation, but this method returns an abstract JavaType
    }

    @Test
    public void testStaticTyping() {
        Class<String> rawType = String.class;
        JavaType javaType = new JavaType(rawType, 0, null, null, false);
        JavaType withStaticTyping = javaType.withStaticTyping();
        assertTrue(withStaticTyping.useStaticType());
        assertEquals(javaType.getRawClass(), withStaticTyping.getRawClass());
    }

    @Test
    public void testNarrowBy() {
        Class<String> rawType = String.class;
        Class<StringBuilder> subclass = StringBuilder.class;
        JavaType javaType = new JavaType(rawType, 0, null, null, false);
        JavaType narrowBy = javaType.narrowBy(subclass);
        assertEquals(subclass, narrowBy.getRawClass());
        assertEquals(javaType.getValueHandler(), narrowBy.getValueHandler());
        assertEquals(javaType.getTypeHandler(), narrowBy.getTypeHandler());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidNarrowBy() {
        Class<String> rawType = String.class;
        Class<Integer> subclass = Integer.class;
        JavaType javaType = new JavaType(rawType, 0, null, null, false);
        javaType.narrowBy(subclass); // Compiles, but this method throws an IllegalArgumentException
    }

    @Test
    public void testWidenBy() {
        Class<String> rawType = String.class;
        Class<Object> superclass = Object.class;
        JavaType javaType = new JavaType(rawType, 0, null, null, false);
        JavaType widenBy = javaType.widenBy(superclass);
        assertEquals(superclass, widenBy.getRawClass());
        assertEquals(javaType.getValueHandler(), widenBy.getValueHandler());
        assertEquals(javaType.getTypeHandler(), widenBy.getTypeHandler());
    }

    @Test
    public void testIsContainerType() {
        Class<String[]> arrayType = String[].class;
        JavaType javaType = new JavaType(arrayType, 0, null, null, false);
        assertTrue(javaType.isContainerType());
    }

    @Test
    public void testIsCollectionLikeType() {
        Class<java.util.ArrayList<String>> collectionType = java.util.ArrayList.class;
        JavaType javaType = new JavaType(collectionType, 0, null, null, false);
        assertFalse(javaType.isCollectionLikeType());
    }

    @Test
    public void testGetGenericSignature() {
        Class<java.util.Map<String, Integer>> mapType = java.util.Map.class;
        TypeFactory typeFactory = new TypeFactory();
        JavaType javaType = typeFactory.constructType(mapType);
        StringBuilder sb = new StringBuilder();
        javaType.getGenericSignature(sb);
        String genericSignature = sb.toString();
        assertTrue(genericSignature.contains("<"));
        assertTrue(genericSignature.contains("Class"));
        assertTrue(genericSignature.contains("java.lang.Integer"));
        assertTrue(genericSignature.contains("java.lang.String"));
    }

    @Test
    public void testGetErasedSignature() {
        Class<java.util.Map<String, Integer>> mapType = java.util.Map.class;
        TypeFactory typeFactory = new TypeFactory();
        JavaType javaType = typeFactory.constructType(mapType);
        StringBuilder sb = new StringBuilder();
        javaType.getErasedSignature(sb);
        String erasedSignature = sb.toString();
        assertTrue(erasedSignature.contains("Map"));
        assertTrue(erasedSignature.contains("java.lang.String"));
        assertTrue(erasedSignature.contains("java.lang.Object"));
    }
}