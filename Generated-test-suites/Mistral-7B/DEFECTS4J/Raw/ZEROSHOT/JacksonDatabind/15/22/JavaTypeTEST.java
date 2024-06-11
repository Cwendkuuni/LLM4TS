package com.example.junit;

import static org.junit.Assert.*;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.TypeFactory;

public class JavaTypeTest {

    @org.junit.Test
    public void testJavaTypeConstruction() {
        Class<Integer> intType = Integer.class;
        JavaType javaType = TypeFactory.defaultInstance().constructType(intType);

        assertEquals(Integer.class, javaType.getRawClass());
        assertTrue(javaType.isConcrete());
        assertFalse(javaType.isAbstract());
        assertFalse(javaType.isInterface());
        assertFalse(javaType.isPrimitive());
        assertFalse(javaType.isEnumType());
        assertFalse(javaType.isFinal());
        assertFalse(javaType.isThrowable());
        assertFalse(javaType.isArrayType());
        assertFalse(javaType.isContainerType());
        assertFalse(javaType.isCollectionLikeType());
        assertFalse(javaType.isMapLikeType());
        assertFalse(javaType.hasGenericTypes());
        assertNull(javaType.getKeyType());
        assertNull(javaType.getContentType());
        assertEquals(0, javaType.containedTypeCount());

        // Test useStaticType()
        assertTrue(javaType.useStaticType());

        // Test narrowing and widening
        JavaType intArrayType = javaType.narrowBy(int[].class);
        assertEquals(int[].class, intArrayType.getRawClass());
        JavaType objectType = javaType.widenBy(Object.class);
        assertEquals(Object.class, objectType.getRawClass());

        // Test narrowing and widening contents
        JavaType listIntType = javaType.narrowContentsBy(java.util.List.class);
        assertEquals(List.class, listIntType.getRawClass());
        JavaType mapStringIntType = javaType.widenContentsBy(java.util.Map.class);
        assertEquals(Map.class, mapStringIntType.getRawClass());
    }

    @org.junit.Test(expected = IllegalArgumentException.class)
    public void testInvalidNarrowing() {
        Class<Integer> intType = Integer.class;
        JavaType javaType = TypeFactory.defaultInstance().constructType(intType);
        javaType.narrowBy(String.class);
    }

    @org.junit.Test(expected = IllegalArgumentException.class)
    public void testInvalidWidening() {
        Class<Integer> intType = Integer.class;
        JavaType javaType = TypeFactory.defaultInstance().constructType(intType);
        javaType.widenBy(String[].class);
    }

    @org.junit.Test
    public void testHashCode() {
        Class<Integer> intType = Integer.class;
        JavaType javaType1 = TypeFactory.defaultInstance().constructType(intType);
        JavaType javaType2 = TypeFactory.defaultInstance().constructType(intType);

        assertEquals(javaType1.hashCode(), javaType2.hashCode());
    }

    @org.junit.Test
    public void testEquals() {
        Class<Integer> intType = Integer.class;
        JavaType javaType1 = TypeFactory.defaultInstance().constructType(intType);
        JavaType javaType2 = TypeFactory.defaultInstance().constructType(intType);

        assertTrue(javaType1.equals(javaType2));
        assertFalse(javaType1.equals(new Object()));
    }
}