package com.fasterxml.jackson.databind;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.lang.reflect.Modifier;

import org.junit.Test;

public class JavaTypeTest {

    @Test
    public void testIsAbstract() {
        JavaType javaType = TypeFactory.defaultInstance.constructType(String[].class);
        assertTrue(javaType.isAbstract());
    }

    @Test
    public void testIsConcrete() {
        JavaType javaType = TypeFactory.defaultInstance.constructType(String[].class);
        assertFalse(javaType.isConcrete());
    }

    @Test
    public void testIsThrowable() {
        JavaType javaType = TypeFactory.defaultInstance.constructType(Throwable.class);
        assertTrue(javaType.isThrowable());
    }

    @Test
    public void testIsArrayType() {
        JavaType javaType = TypeFactory.defaultInstance.constructType(String[].class);
        assertTrue(javaType.isArrayType());
    }

    @Test
    public void testIsEnumType() {
        JavaType javaType = TypeFactory.defaultInstance.constructType(Enum.class);
        assertTrue(javaType.isEnumType());
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
    public void testIsFinal() {
        JavaType javaType = TypeFactory.defaultInstance.constructType(String.class);
        assertFalse(javaType.isFinal());
    }

    @Test
    public void testIsContainerType() {
        JavaType javaType = TypeFactory.defaultInstance.constructType(List.class);
        assertTrue(javaType.isContainerType());
    }

    @Test
    public void testIsCollectionLikeType() {
        JavaType javaType = TypeFactory.defaultInstance.constructType(List.class);
        assertTrue(javaType.isCollectionLikeType());
    }

    @Test
    public void testIsMapLikeType() {
        JavaType javaType = TypeFactory.defaultInstance.constructType(Map.class);
        assertTrue(javaType.isMapLikeType());
    }

    @Test
    public void testUseStaticType() {
        JavaType javaType = TypeFactory.defaultInstance.constructType(String.class);
        assertFalse(javaType.useStaticType());
    }

    @Test
    public void testHasGenericTypes() {
        JavaType javaType = TypeFactory.defaultInstance.constructType(List.class);
        assertTrue(javaType.hasGenericTypes());
    }

    @Test
    public void testGetKeyType() {
        JavaType javaType = TypeFactory.defaultInstance.constructType(Map.class);
        assertNull(javaType.getKeyType());
    }

    @Test
    public void testGetContentType() {
        JavaType javaType = TypeFactory.defaultInstance.constructType(List.class);
        assertNull(javaType.getContentType());
    }

    @Test
    public void testContainedTypeCount() {
        JavaType javaType = TypeFactory.defaultInstance.constructType(Map.class);
        assertEquals(2, javaType.containedTypeCount());
    }

    @Test
    public void testContainedTypeOrUnknown() {
        JavaType javaType = TypeFactory.defaultInstance.constructType(Map.class);
        JavaType keyType = javaType.containedTypeOrUnknown(0);
        assertNotNull(keyType);
        JavaType valueType = javaType.containedTypeOrUnknown(1);
        assertNotNull(valueType);
    }

    @Test
    public void testGetGenericSignature() {
        JavaType javaType = TypeFactory.defaultInstance.constructType(Map.class);
        StringBuilder genericSignature = new StringBuilder();
        javaType.getGenericSignature(genericSignature);
        assertEquals("Map<K,V>", genericSignature.toString());
    }

    @Test
    public void testGetErasedSignature() {
        JavaType javaType = TypeFactory.defaultInstance.constructType(Map.class);
        StringBuilder erasedSignature = new StringBuilder();
        javaType.getErasedSignature(erasedSignature);
        assertEquals("java.util.Map", erasedSignature.toString());
    }
}