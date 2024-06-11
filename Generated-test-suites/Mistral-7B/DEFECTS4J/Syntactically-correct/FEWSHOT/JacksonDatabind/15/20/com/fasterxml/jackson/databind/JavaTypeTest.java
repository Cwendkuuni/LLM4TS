package com.fasterxml.jackson.databind;

import static org.junit.Assert.*;

import java.lang.reflect.Modifier;

import org.junit.Test;

public class JavaTypeTest {

    @Test
    public void testIsAbstract() {
        JavaType javaType = TypeFactory.defaultInstance.constructType(String[].class);
        assertTrue(Modifier.isAbstract(javaType.getRawClass().getModifiers()));
    }

    @Test
    public void testIsConcrete() {
        JavaType javaType = TypeFactory.defaultInstance.constructType(String[].class);
        assertFalse(javaType.isConcrete());
    }

    @Test
    public void testIsFinal() {
        JavaType javaType = TypeFactory.defaultInstance.constructType(String[].class);
        assertFalse(javaType.isFinal());
    }

    @Test
    public void testIsInterface() {
        JavaType javaType = TypeFactory.defaultInstance.constructType(List.class);
        assertTrue(javaType.isInterface());
    }

    @Test
    public void testIsPrimitive() {
        JavaType javaType = TypeFactory.defaultInstance.constructType(int.class);
        assertTrue(javaType.isPrimitive());
    }

    @Test
    public void testIsEnumType() {
        JavaType javaType = TypeFactory.defaultInstance.constructType(Enum[].class);
        assertTrue(javaType.isEnumType());
    }

    @Test
    public void testIsMapLikeType() {
        JavaType javaType = TypeFactory.defaultInstance.constructType(Map.class);
        assertFalse(javaType.isMapLikeType());
    }

    @Test
    public void testIsCollectionLikeType() {
        JavaType javaType = TypeFactory.defaultInstance.constructType(List.class);
        assertTrue(javaType.isCollectionLikeType());
    }

    @Test
    public void testIsContainerType() {
        JavaType javaType = TypeFactory.defaultInstance.constructType(List.class);
        assertTrue(javaType.isContainerType());
    }

    @Test
    public void testHasRawClass() {
        JavaType javaType = TypeFactory.defaultInstance.constructType(String[].class);
        assertTrue(javaType.hasRawClass(String[].class));
    }

    @Test
    public void testHasRawClassFalse() {
        JavaType javaType = TypeFactory.defaultInstance.constructType(List.class);
        assertFalse(javaType.hasRawClass(String[].class));
    }

    @Test
    public void testIsThrowable() {
        JavaType javaType = TypeFactory.defaultInstance.constructType(Exception.class);
        assertTrue(javaType.isThrowable());
    }

    @Test
    public void testIsArrayType() {
        JavaType javaType = TypeFactory.defaultInstance.constructType(String[].class);
        assertTrue(javaType.isArrayType());
    }

    @Test(expected = AssertionError.class)
    public void testGetKeyTypeNull() {
        JavaType javaType = TypeFactory.defaultInstance.constructType(Map.class);
        javaType.getKeyType();
    }

    @Test
    public void testGetContentType() {
        JavaType javaType = TypeFactory.defaultInstance.constructType(Map.class);
        JavaType contentType = TypeFactory.defaultInstance.constructType(String.class);
        javaType.setContentType(contentType);
        assertEquals(contentType, javaType.getContentType());
    }

    @Test(expected = AssertionError.class)
    public void testGetContentTypeNull() {
        JavaType javaType = TypeFactory.defaultInstance.constructType(Map.class);
        javaType.getContentType();
    }

    @Test
    public void testContainedTypeCount() {
        JavaType javaType = TypeFactory.defaultInstance.constructType(Map.class);
        javaType.setContentType(TypeFactory.defaultInstance.constructType(List.class));
        javaType.setContentType(TypeFactory.defaultInstance.constructType(String.class));
        assertEquals(2, javaType.containedTypeCount());
    }

    @Test
    public void testContainedType() {
        JavaType javaType = TypeFactory.defaultInstance.constructType(Map.class);
        javaType.setContentType(TypeFactory.defaultInstance.constructType(List.class));
        javaType.setContentType(TypeFactory.defaultInstance.constructType(String.class));
        JavaType containedType = javaType.containedType(1);
        assertEquals(String.class, containedType.getRawClass());
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testContainedTypeInvalidIndex() {
        JavaType javaType = TypeFactory.defaultInstance.constructType(Map.class);
        javaType.containedType(1);
    }

    @Test
    public void testContainedTypeOrUnknown() {
        JavaType javaType = TypeFactory.defaultInstance.constructType(Map.class);
        javaType.setContentType(TypeFactory.defaultInstance.constructType(List.class));
        javaType.setContentType(TypeFactory.defaultInstance.constructType(String.class));
        JavaType containedType = javaType.containedTypeOrUnknown(1);
        assertNotNull(containedType);
        assertEquals(String.class, containedType.getRawClass());
    }

    @Test
    public void testContainedTypeOrUnknownNull() {
        JavaType javaType = TypeFactory.defaultInstance.constructType(Map.class);
        JavaType containedType = javaType.containedTypeOrUnknown(1);
        assertNull(containedType);
    }

    @Test
    public void testGetGenericSignature() {
        JavaType javaType = TypeFactory.defaultInstance.constructType(Map.class);
        javaType.setKeyType(TypeFactory.defaultInstance.constructType(String.class));
        javaType.setContentType(TypeFactory.defaultInstance.constructType(List.class));
        javaType.setContentType(TypeFactory.defaultInstance.constructType(String.class));
        StringBuilder sb = new StringBuilder(100);
        javaType.getGenericSignature(sb);
        String signature = sb.toString();
        assertEquals("java.util.Map<java.lang.String,java.util.List<java.lang.String>>", signature);
    }

    @Test
    public void testGetErasedSignature() {
        JavaType javaType = TypeFactory.defaultInstance.constructType(Map.class);
        javaType.setKeyType(TypeFactory.defaultInstance.constructType(String.class));
        javaType.setContentType(TypeFactory.defaultInstance.constructType(List.class));
        javaType.setContentType(TypeFactory.defaultInstance.constructType(String.class));
        StringBuilder sb = new StringBuilder(100);
        javaType.getErasedSignature(sb);
        String signature = sb.toString();
        assertEquals("java.util.Map", signature);
    }
}