package com.fasterxml.jackson.databind.test;

import com.fasterxml.jackson.databind.*;
import org.junit.Test;
import org.junit.Assert.*;

public class JavaTypeTest {

    @Test
    public void testJavaTypeConstruction() {
        Class<String> rawClass = String.class;
        JavaType javaType = new TypeFactory().constructType(rawClass);

        assertEquals(String.class, javaType.getRawClass());
        assertEquals(JavaType.class, javaType.getClass());
        assertNotNull(javaType._valueHandler);
        assertNotNull(javaType._typeHandler);
        assertEquals(false, javaType._asStatic);
    }

    @Test
    public void testJavaTypeWithHandlers() {
        Class<String> rawClass = String.class;
        JavaType javaType = new TypeFactory().constructType(rawClass);

        Object handler = new Object();

        JavaType javaTypeWithHandler = javaType.withValueHandler(handler);
        assertEquals(handler, javaTypeWithHandler.getValueHandler());

        javaTypeWithHandler = javaTypeWithHandler.withTypeHandler(handler);
        assertEquals(handler, javaTypeWithHandler.getTypeHandler());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidTypeHandlers() {
        Class<String> rawClass = String.class;
        JavaType javaType = new TypeFactory().constructType(rawClass);

        javaType.withValueHandler(new Integer(1));
    }

    @Test
    public void testJavaTypeNarrowing() {
        Class<String> rawClass = String[].class;
        JavaType javaType = new TypeFactory().constructType(rawClass);

        Class<String> subClass = String.class;
        JavaType narrowedJavaType = javaType.narrowBy(subClass);

        assertEquals(subClass, narrowedJavaType.getRawClass());
        assertEquals(JavaType.class, narrowedJavaType.getClass());
        assertEquals(javaType._valueHandler, narrowedJavaType._valueHandler);
        assertEquals(javaType._typeHandler, narrowedJavaType._typeHandler);
        assertEquals(false, narrowedJavaType._asStatic);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidNarrowing() {
        Class<String> rawClass = String.class;
        JavaType javaType = new TypeFactory().constructType(rawClass);

        Class<Integer> subClass = Integer.class;
        javaType.narrowBy(subClass);
    }

    @Test
    public void testJavaTypeWidening() {
        Class<String> rawClass = String[].class;
        JavaType javaType = new TypeFactory().constructType(rawClass);

        Class<Object[]> superclass = Object[].class;
        JavaType widenedJavaType = javaType.widenBy(superclass);

        assertEquals(superclass, widenedJavaType.getRawClass());
        assertEquals(JavaType.class, widenedJavaType.getClass());
        assertEquals(javaType._valueHandler, widenedJavaType._valueHandler);
        assertEquals(javaType._typeHandler, widenedJavaType._typeHandler);
        assertEquals(false, widenedJavaType._asStatic);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidWidening() {
        Class<String> rawClass = String.class;
        JavaType javaType = new TypeFactory().constructType(rawClass);

        Class<String[]> superclass = String[].class;
        javaType.widenBy(superclass);
    }

    @Test
    public void testJavaTypeIsContainerType() {
        Class<List<String>> rawClass = List.class;
        JavaType javaType = new TypeFactory().constructType(rawClass);

        assertTrue(javaType.isContainerType());
    }

    @Test
    public void testJavaTypeIsCollectionLikeType() {
        Class<List<String>> rawClass = List.class;
        JavaType javaType = new TypeFactory().constructType(rawClass);

        assertTrue(javaType.isCollectionLikeType());
    }

    @Test
    public void testJavaTypeIsMapLikeType() {
        Class<Map<String, String>> rawClass = Map.class;
        JavaType javaType = new TypeFactory().constructType(rawClass);

        assertTrue(javaType.isMapLikeType());
    }

    @Test
    public void testJavaTypeUseStaticType() {
        Class<String> rawClass = String.class;
        JavaType javaType = new TypeFactory().constructType(rawClass);

        JavaType staticType = javaType.withStaticTyping();
        assertTrue(staticType.useStaticType());
    }
}