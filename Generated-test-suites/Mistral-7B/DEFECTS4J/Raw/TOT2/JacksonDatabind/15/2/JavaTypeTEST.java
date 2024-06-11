package com.fasterxml.jackson.databind.test;

import com.fasterxml.jackson.core.type.TypeFactory;
import com.fasterxml.jackson.databind.JavaType;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

public class JavaTypeTest {

    @Test
    public void testJavaTypeCreation() {
        Class<String> rawClass = String.class;
        JavaType javaType = TypeFactory.defaultInstance().constructType(rawClass);
        assertNotNull(javaType);
        assertEquals(rawClass, javaType.getRawClass());
    }

    @Test
    public void testJavaTypeWithValueHandler() {
        Class<String> rawClass = String.class;
        JavaType javaType = TypeFactory.defaultInstance().constructType(rawClass);
        Object valueHandler = new Object() {};
        JavaType javaTypeWithHandler = javaType.withValueHandler(valueHandler);
        assertEquals(javaType.getRawClass(), javaTypeWithHandler.getRawClass());
        assertNotNull(javaTypeWithHandler.getValueHandler());
        assertEquals(valueHandler, javaTypeWithHandler.getValueHandler());
    }

    @Test
    public void testJavaTypeNarrowing() {
        Class<String> rawClass = String[].class;
        JavaType javaType = TypeFactory.defaultInstance().constructType(rawClass);
        Class<String> subclass = String.class;
        JavaType narrowedType = javaType.narrowBy(subclass);
        assertEquals(subclass, narrowedType.getRawClass());
        assertEquals(javaType.getRawClass(), narrowedType.getSuperClass());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testJavaTypeNarrowingWithIncompatibleType() {
        Class<String> rawClass = String.class;
        JavaType javaType = TypeFactory.defaultInstance().constructType(rawClass);
        Class<Integer> incompatibleType = Integer.class;
        javaType.narrowBy(incompatibleType);
    }

    @Test
    public void testJavaTypeWidening() {
        Class<String> rawClass = String.class;
        JavaType javaType = TypeFactory.defaultInstance().constructType(rawClass);
        Class<Object[]> superclass = Object[].class;
        JavaType widenedType = javaType.widenBy(superclass);
        assertEquals(superclass, widenedType.getRawClass());
        assertEquals(javaType.getRawClass(), widenedType.getSuperClass());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testJavaTypeWideningWithIncompatibleType() {
        Class<String> rawClass = String.class;
        JavaType javaType = TypeFactory.defaultInstance().constructType(rawClass);
        Class<String[]> incompatibleType = String[].class;
        javaType.widenBy(incompatibleType);
    }

    @Test
    public void testJavaTypeIsContainerType() {
        Class<List<String>> rawClass = List.class;
        JavaType javaType = TypeFactory.defaultInstance().constructType(rawClass);
        assertTrue(javaType.isContainerType());
    }

    @Test
    public void testJavaTypeIsMapLikeType() {
        Class<Map<String, Integer>> rawClass = Map.class;
        JavaType javaType = TypeFactory.defaultInstance().constructType(rawClass);
        assertTrue(javaType.isMapLikeType());
    }

    @Test
    public void testJavaTypeIsCollectionLikeType() {
        Class<List<String>> rawClass = List.class;
        JavaType javaType = TypeFactory.defaultInstance().constructType(rawClass);
        assertFalse(javaType.isCollectionLikeType());
    }

    @Test
    public void testJavaTypeUseStaticType() {
        Class<List<String>> rawClass = List.class;
        JavaType javaType = TypeFactory.defaultInstance().constructType(rawClass);
        assertFalse(javaType.useStaticType());

        JavaType javaTypeWithStaticTyping = javaType.withStaticTyping();
        assertTrue(javaTypeWithStaticTyping.useStaticType());
    }

    @Test
    public void testJavaTypeGetErasedSignature() {
        Class<List<String>> rawClass = List.class;
        JavaType javaType = TypeFactory.defaultInstance().constructType(rawClass);
        StringBuilder signature = new StringBuilder();
        javaType.getErasedSignature(signature);
        assertEquals("java.util.List", signature.toString());
    }

    @Test
    public void testJavaTypeGetGenericSignature() {
        Class<List<String>> rawClass = List.class;
        JavaType javaType = TypeFactory.defaultInstance().constructType(rawClass);
        StringBuilder signature = new StringBuilder();
        javaType.getGenericSignature(signature);
        assertEquals("<java.lang.String>", signature.toString());
    }
}