package com.fasterxml.jackson.databind.test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.lang.reflect.Modifier;

import com.fasterxml.jackson.core.type.ResolvedType;
import com.fasterxml.jackson.databind.type.TypeFactory;

import com.fasterxml.jackson.databind.JavaType;

public class JavaTypeTest {

    @Test
    public void testIsAbstract() {
        JavaType javaType = TypeFactory.defaultInstance().constructType(String[].class);
        assertTrue(Modifier.isAbstract(javaType.getRawClass().getModifiers()));
    }

    @Test
    public void testIsConcrete() {
        JavaType javaType = TypeFactory.defaultInstance().constructType(String[].class);
        assertFalse(javaType.isConcrete());
    }

    @Test
    public void testIsEnumType() {
        JavaType javaType = TypeFactory.defaultInstance().constructType(Enum.class);
        assertTrue(javaType.isEnumType());
    }

    @Test
    public void testIsInterface() {
        JavaType javaType = TypeFactory.defaultInstance().constructType(List.class);
        assertTrue(javaType.isInterface());
    }

    @Test
    public void testIsPrimitive() {
        JavaType javaType = TypeFactory.defaultInstance().constructType(int.class);
        assertTrue(javaType.isPrimitive());
    }

    @Test
    public void testIsFinal() {
        JavaType javaType = TypeFactory.defaultInstance().constructType(String.class);
        assertTrue(Modifier.isFinal(javaType.getRawClass().getModifiers()));
    }

    @Test
    public void testIsContainerType() {
        JavaType javaType = TypeFactory.defaultInstance().constructType(Map.class);
        assertTrue(javaType.isContainerType());
    }

    @Test
    public void testIsCollectionLikeType() {
        JavaType javaType = TypeFactory.defaultInstance().constructType(List.class);
        assertTrue(javaType.isCollectionLikeType());
    }

    @Test
    public void testIsMapLikeType() {
        JavaType javaType = TypeFactory.defaultInstance().constructType(Map.class);
        assertTrue(javaType.isMapLikeType());
    }

    @Test
    public void testUseStaticType() {
        JavaType javaType = TypeFactory.defaultInstance().constructType(int[].class);
        assertFalse(javaType.useStaticType());

        javaType = TypeFactory.defaultInstance().constructType(String.class);
        assertTrue(javaType.useStaticType());
    }

    @Test
    public void testHasGenericTypes() {
        JavaType javaType = TypeFactory.defaultInstance().constructType(List.class);
        assertTrue(javaType.hasGenericTypes());

        javaType = TypeFactory.defaultInstance().constructType(String.class);
        assertFalse(javaType.hasGenericTypes());
    }

    @Test
    public void testGetKeyType() {
        JavaType javaType = TypeFactory.defaultInstance().constructType(Map.class);
        assertNull(javaType.getKeyType());

        javaType = TypeFactory.defaultInstance().constructType(Map.class, String.class, Integer.class);
        assertNotNull(javaType.getKeyType());
    }

    @Test
    public void testGetContentType() {
        JavaType javaType = TypeFactory.defaultInstance().constructType(List.class);
        assertNull(javaType.getContentType());

        javaType = TypeFactory.defaultInstance().constructType(List.class, Integer.class);
        assertNotNull(javaType.getContentType());
    }

    @Test
    public void testContainedTypeCount() {
        JavaType javaType = TypeFactory.defaultInstance().constructType(Map.class, String.class, Integer.class);
        assertEquals(2, javaType.containedTypeCount());

        javaType = TypeFactory.defaultInstance().constructType(String.class);
        assertEquals(0, javaType.containedTypeCount());
    }

    @Test
    public void testContainedType() {
        JavaType javaType = TypeFactory.defaultInstance().constructType(Map.class, String.class, Integer.class);
        JavaType containedType = javaType.containedType(0);
        assertEquals(Integer.class, containedType.getRawClass());

        containedType = javaType.containedType(1);
        assertEquals(String.class, containedType.getRawClass());

        javaType = TypeFactory.defaultInstance().constructType(String.class);
        containedType = javaType.containedType(0);
        assertNull(containedType);
    }

    @Test
    public void testContainedTypeOrUnknown() {
        JavaType javaType = TypeFactory.defaultInstance().constructType(Map.class, String.class, Integer.class);
        JavaType containedType = javaType.containedTypeOrUnknown(0);
        assertEquals(Integer.class, containedType.getRawClass());

        containedType = javaType.containedTypeOrUnknown(2);
        assertNull(containedType);
    }

    @Test
    public void testGetParameterSource() {
        JavaType javaType = TypeFactory.defaultInstance().constructType(Map.class, String[].class, Integer[].class);
        assertEquals(Object.class, javaType.getParameterSource().getRawClass());

        javaType = TypeFactory.defaultInstance().constructType(Map.class, String[].class, Integer[].class, String.class, Integer.class);
        assertEquals(Map.class, javaType.getParameterSource().getRawClass());
    }

    @Test
    public void testGetGenericSignature() {
        JavaType javaType = TypeFactory.defaultInstance().constructType(List.class, Integer.class);
        StringBuilder sb = new StringBuilder();
        javaType.getGenericSignature(sb);
        assertEquals("java.util.List<java.lang.Integer>", sb.toString());

        javaType = TypeFactory.defaultInstance().constructType(Map.class, String.class, Integer.class);
        sb = new StringBuilder();
        javaType.getGenericSignature(sb);
        assertEquals("java.util.Map<java.lang.String,java.lang.Integer>", sb.toString());

        javaType = TypeFactory.defaultInstance().constructType(String.class);
        sb = new StringBuilder();
        javaType.getGenericSignature(sb);
        assertEquals("java.lang.String", sb.toString());
    }

    @Test
    public void testGetErasedSignature() {
        JavaType javaType = TypeFactory.defaultInstance().constructType(List.class, Integer.class);
        StringBuilder sb = new StringBuilder();
        javaType.getErasedSignature(sb);
        assertEquals("java.util.List", sb.toString());

        javaType = TypeFactory.defaultInstance().constructType(Map.class, String.class, Integer.class);
        sb = new StringBuilder();
        javaType.getErasedSignature(sb);
        assertEquals("java.util.Map", sb.toString());

        javaType = TypeFactory.defaultInstance().constructType(String.class);
        sb = new StringBuilder();
        javaType.getErasedSignature(sb);
        assertEquals("java.lang.String", sb.toString());
    }
}