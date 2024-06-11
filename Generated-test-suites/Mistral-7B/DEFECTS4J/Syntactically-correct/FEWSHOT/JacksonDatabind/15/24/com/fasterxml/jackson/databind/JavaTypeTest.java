package com.fasterxml.jackson.databind.test;

import static org.junit.Assert.*;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.TypeFactory;

import org.junit.Test;

public class JavaTypeTest {

    @Test
    public void testIsAbstract() {
        JavaType javaType = TypeFactory.defaultInstance().constructType(String[].class);
        assertTrue(javaType.isAbstract());
    }

    @Test
    public void testIsConcrete() {
        JavaType javaType = TypeFactory.defaultInstance().constructType(String[].class);
        assertFalse(javaType.isConcrete());
    }

    @Test
    public void testIsFinal() {
        JavaType javaType = TypeFactory.defaultInstance().constructType(String[].class);
        assertFalse(javaType.isFinal());
    }

    @Test
    public void testIsThrowable() {
        JavaType javaType = TypeFactory.defaultInstance().constructType(Exception.class);
        assertTrue(javaType.isThrowable());
    }

    @Test
    public void testIsArrayType() {
        JavaType javaType = TypeFactory.defaultInstance().constructType(String[].class);
        assertTrue(javaType.isArrayType());
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
    public void testIsContainerType() {
        JavaType javaType = TypeFactory.defaultInstance().constructType(List.class);
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
        JavaType javaType = TypeFactory.defaultInstance().constructType(String[].class);
        assertFalse(javaType.useStaticType());

        javaType = TypeFactory.defaultInstance().constructType(String.class);
        assertTrue(javaType.useStaticType());
    }

    @Test
    public void testHasGenericTypes() {
        JavaType javaType = TypeFactory.defaultInstance().constructType(List.class);
        assertTrue(javaType.hasGenericTypes());
    }

    @Test
    public void testGetKeyType() {
        JavaType javaType = TypeFactory.defaultInstance().constructType(Map.class);
        assertNull(javaType.getKeyType());
    }

    @Test
    public void testGetContentType() {
        JavaType javaType = TypeFactory.defaultInstance().constructType(List.class);
        assertNull(javaType.getContentType());
    }

    @Test
    public void testContainedTypeCount() {
        JavaType javaType = TypeFactory.defaultInstance().constructType(List.class);
        assertEquals(1, javaType.containedTypeCount());
    }

    @Test
    public void testContainedType() {
        JavaType javaType = TypeFactory.defaultInstance().constructType(List.class);
        JavaType expected = TypeFactory.defaultInstance().constructType(String.class);
        assertEquals(expected, javaType.containedType(0));
    }

    @Test
    public void testContainedTypeName() {
        JavaType javaType = TypeFactory.defaultInstance().constructType(List.class);
        String expected = "java.lang.String";
        assertEquals(expected, javaType.containedTypeName(0));
    }

    @Test
    public void testGetParameterSource() {
        JavaType javaType = TypeFactory.defaultInstance().constructType(List.class);
        assertNull(javaType.getParameterSource());
    }

    @Test
    public void testGetErasedSignature() {
        JavaType javaType = TypeFactory.defaultInstance().constructType(List.class);
        String expected = "java.util.List";
        assertEquals(expected, javaType.getErasedSignature());
    }

    @Test
    public void testGetGenericSignature() {
        JavaType javaType = TypeFactory.defaultInstance().constructType(List.class);
        String expected = "<E>";
        assertEquals(expected, javaType.getGenericSignature().toString());
    }
}