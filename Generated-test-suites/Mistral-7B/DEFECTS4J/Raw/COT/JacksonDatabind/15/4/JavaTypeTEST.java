package com.example; // replace with your test package name

import static org.junit.Assert.*;

import com.fasterxml.jackson.databind.TypeFactory;

import org.junit.Test;

public class JavaTypeTest {
    

    @Test
    public void testJavaTypeConstructor() {
        Class<String> rawClass = String.class;
        JavaType javaType = new JavaType(rawClass, 0, null, null, false);
        assertEquals(String.class, javaType.getRawClass());
        assertEquals(0, javaType.hashCode());
        assertNull(javaType.getValueHandler());
        assertNull(javaType.getTypeHandler());
        assertFalse(javaType.isAsStatic());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testJavaTypeInvalidSubclass() {
        Class<String> rawClass = String.class;
        JavaType javaType = new JavaType(rawClass, 0, null, null, false);
        JavaType invalidSubclass = javaType.narrowBy(int.class);
    }

    @Test
    public void testJavaTypeNarrowBy() {
        Class<String> rawClass = String.class;
        JavaType javaType = new JavaType(rawClass, 0, null, null, false);
        JavaType stringArrayType = javaType.narrowBy(String[].class);
        assertTrue(stringArrayType instanceof JavaTypeArray);
        assertEquals(String[].class, stringArrayType.getRawClass());
        assertFalse(stringArrayType.isAsStatic());
    }

    @Test
    public void testJavaTypeWidenBy() {
        Class<String> rawClass = String.class;
        JavaType javaType = new JavaType(rawClass, 0, null, null, false);
        JavaType objectType = javaType.widenBy(Object.class);
        assertEquals(Object.class, objectType.getRawClass());
        assertFalse(objectType.isAsStatic());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testJavaTypeInvalidWidenBy() {
        Class<String> rawClass = String.class;
        JavaType javaType = new JavaType(rawClass, 0, null, null, false);
        JavaType intType = javaType.widenBy(int.class);
    }

    @Test
    public void testJavaTypeIsContainerType() {
        Class<List<String>> listClass = List.class.getGenericType();
        JavaType javaType = new JavaType(listClass, 0, null, null, false);
        assertTrue(javaType.isContainerType());
    }

    @Test
    public void testJavaTypeIsCollectionLikeType() {
        Class<List<String>> listClass = List.class.getGenericType();
        JavaType javaType = new JavaType(listClass, 0, null, null, false);
        assertTrue(javaType.isCollectionLikeType());
    }

    @Test
    public void testJavaTypeIsMapLikeType() {
        Class<Map<String, Integer>> mapClass = Map.class.getGenericType();
        JavaType javaType = new JavaType(mapClass, 0, null, null, false);
        assertTrue(javaType.isMapLikeType());
    }

    @Test
    public void testJavaTypeUseStaticType() {
        Class<List<String>> listClass = List.class.getGenericType();
        JavaType javaType = new JavaType(listType, 0, null, null, true);
        assertTrue(javaType.useStaticType());
    }

    @Test
    public void testJavaTypeGetErasedSignature() {
        Class<List<String>> listClass = List.class.getGenericType();
        StringBuilder sb = new StringBuilder();
        JavaType javaType = new JavaType(listClass, 0, null, null, false);
        javaType.getErasedSignature(sb);
        String expected = "Ljava/util/List;";
        assertEquals(expected, sb.toString());
    }

    @Test
    public void testJavaTypeGetGenericSignature() {
        Class<List<String>> listClass = List.class.getGenericType();
        StringBuilder sb = new StringBuilder();
        JavaType javaType = new JavaType(listClass, 0, null, null, false);
        javaType.getGenericSignature(sb);
        String expected = "<Ljava/lang/String;>Ljava/util/List;>";
        assertEquals(expected, sb.toString());
    }

    @Test
    public void testJavaTypeIsAbstract() {
        Class<List<String>> listClass = List.class.getGenericType();
        JavaType javaType = new JavaType(listClass, 0, null, null, false);
        assertTrue(javaType.isAbstract());
    }

    @Test
    public void testJavaTypeIsInterface() {
        Class<List<String>> listClass = List.class.getGenericType();
        JavaType javaType = new JavaType(listClass, 0, null, null, false);
        assertTrue(javaType.isInterface());
    }

    @Test
    public void testJavaTypeIsPrimitive() {
        Class<boolean[]> booleanArrayClass = boolean[].class;
        JavaType javaType = new JavaType(booleanArrayClass, 0, null, null, false);
        assertTrue(javaType.isPrimitive());
    }

    @Test
    public void testJavaTypeIsFinal() {
        Class<List<String>> listClass = List.class.getGenericType();
        JavaType javaType = new JavaType(listClass, 0, null, null, false);
        assertTrue(javaType.isFinal());
    }

    @Test
    public void testJavaTypeWithTypeHandler() {
        Class<String> rawClass = String.class;
        JavaType javaType = new JavaType(rawClass, 0, null, null, false);
        Object handler = new Object();
        JavaType javaTypeWithHandler = javaType.withTypeHandler(handler);
        assertSame(javaTypeWithHandler, javaType.withTypeHandler(handler));
        assertEquals(handler, javaTypeWithHandler.getTypeHandler());
    }

    @Test
    public void testJavaTypeWithContentTypeHandler() {
        Class<List<String>> listClass = List.class.getGenericType();
        JavaType javaType = new JavaType(listClass, 0, null, null, false);
        Object handler = new Object();
        JavaType javaTypeWithHandler = javaType.withContentTypeHandler(handler);
        assertSame(javaTypeWithHandler, javaType.withContentTypeHandler(handler));
        assertEquals(handler, javaTypeWithHandler.getTypeHandler());
    }

    @Test
    public void testJavaTypeWithValueHandler() {
        Class<List<String>> listClass = List.class.getGenericType();
        JavaType javaType = new JavaType(listClass, 0, null, null, false);
        Object handler = new Object();
        JavaType javaTypeWithHandler = javaType.withValueHandler(handler);
        assertSame(javaTypeWithHandler, javaType.withValueHandler(handler));
        assertEquals(handler, javaTypeWithHandler.getValueHandler());
    }

    @Test
    public void testJavaTypeWithContentValueHandler() {
        Class<List<String>> listClass = List.class.getGenericType();
        JavaType javaType = new JavaType(listClass, 0, null, null, false);
        Object handler = new Object();
        JavaType javaTypeWithHandler = javaType.withContentValueHandler(handler);
        assertSame(javaTypeWithHandler, javaType.withContentValueHandler(handler));
        assertEquals(handler, javaTypeWithHandler.getValueHandler());
    }

    @Test
    public void testJavaTypeWithStaticTyping() {
        Class<List<String>> listClass = List.class.getGenericType();
        JavaType javaType = new JavaType(listClass, 0, null, null, false);
        JavaType javaTypeWithStaticTyping = javaType.withStaticTyping();
        assertTrue(javaTypeWithStaticTyping.useStaticType());
    }

    
}