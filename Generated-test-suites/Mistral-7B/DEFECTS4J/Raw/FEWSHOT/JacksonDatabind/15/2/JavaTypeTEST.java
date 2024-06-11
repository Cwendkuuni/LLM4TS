package com.fasterxml.jackson.databind;

import static org.junit.Assert.*;

import org.junit.Test;

public class JavaTypeTest {

    @Test
    public void testIsAbstract() {
        JavaType javaType = TypeFactory.defaultInstance().constructType(String.class);
        assertTrue(javaType.isAbstract());
    }

    @Test
    public void testIsConcrete() {
        JavaType javaType = TypeFactory.defaultInstance().constructType(String.class);
        assertFalse(javaType.isConcrete());
    }

    @Test
    public void testIsInterface() {
        JavaType javaType = TypeFactory.defaultInstance().constructType(String.class);
        assertFalse(javaType.isInterface());
    }

    @Test
    public void testIsPrimitive() {
        JavaType javaType = TypeFactory.defaultInstance().constructType(byte.class);
        assertTrue(javaType.isPrimitive());
    }

    @Test
    public void testIsFinal() {
        JavaType javaType = TypeFactory.defaultInstance().constructType(String.class);
        assertFalse(javaType.isFinal());
    }

    @Test
    public void testIsContainerType() {
        JavaType javaType = TypeFactory.defaultInstance().constructCollectionType(List.class, String.class);
        assertTrue(javaType.isContainerType());
    }

    @Test
    public void testIsCollectionLikeType() {
        JavaType javaType = TypeFactory.defaultInstance().constructCollectionType(List.class, String.class);
        assertTrue(javaType.isCollectionLikeType());
    }

    @Test
    public void testIsMapLikeType() {
        JavaType javaType = TypeFactory.defaultInstance().constructMapType(Map.class, String.class, Integer.class);
        assertTrue(javaType.isMapLikeType());
    }

    @Test
    public void testUseStaticType() {
        JavaType javaType = TypeFactory.defaultInstance().constructType(String.class);
        assertFalse(javaType.useStaticType());

        JavaType javaTypeWithStaticTyping = javaType.withStaticTyping();
        assertTrue(javaTypeWithStaticTyping.useStaticType());
    }

    @Test
    public void testNarrowBy() {
        JavaType javaType = TypeFactory.defaultInstance().constructType(String.class);
        JavaType stringListType = TypeFactory.defaultInstance().constructCollectionType(List.class, String.class);

        JavaType stringListTypeNarrowed = javaType.narrowBy(stringListType.getRawClass());
        assertEquals(stringListType, stringListTypeNarrowed);

        JavaType stringArrayType = TypeFactory.defaultInstance().constructType(String[].class);
        JavaType stringListTypeWidened = javaType.widenBy(stringArrayType.getRawClass());
        assertEquals(stringListType, stringListTypeWidened);

        assertNull(javaType.narrowBy(Integer.class));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNarrowByWithInvalidSubclass() {
        JavaType javaType = TypeFactory.defaultInstance().constructType(String.class);
        javaType.narrowBy(Integer.class);
    }

    @Test
    public void testWidenBy() {
        JavaType javaType = TypeFactory.defaultInstance().constructType(String[].class);
        JavaType stringType = TypeFactory.defaultInstance().constructType(String.class);

        JavaType stringTypeWidened = javaType.widenBy(stringType.getRawClass());
        assertEquals(stringType, stringTypeWidened);

        JavaType intType = TypeFactory.defaultInstance().constructType(int.class);
        JavaType stringTypeNarrowed = javaType.narrowBy(intType.getRawClass());
        assertNull(stringTypeNarrowed);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testWidenByWithInvalidSuperclass() {
        JavaType javaType = TypeFactory.defaultInstance().constructType(String[].class);
        javaType.widenBy(Integer.class);
    }

    @Test
    public void testNarrowContentsBy() {
        JavaType javaType = TypeFactory.defaultInstance().constructMapType(Map.class, String.class, Integer.class);
        JavaType stringIntegerMapType = TypeFactory.defaultInstance().constructMapType(Map.class, String.class, String.class);

        JavaType stringIntegerMapTypeNarrowed = javaType.narrowContentsBy(String.class);
        assertEquals(stringIntegerMapType, stringIntegerMapTypeNarrowed);

        JavaType stringStringMapType = TypeFactory.defaultInstance().constructMapType(Map.class, String.class, String.class);
        JavaType intStringMapType = TypeFactory.defaultInstance().constructMapType(Map.class, Integer.class, String.class);
        JavaType intStringMapTypeWidened = javaType.widenContentsBy(intStringType.getRawClass());
        assertEquals(intStringMapType, intStringMapTypeWidened);

        assertNull(javaType.narrowContentsBy(Integer[].class));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNarrowContentsByWithInvalidContentClass() {
        JavaType javaType = TypeFactory.defaultInstance().constructMapType(Map.class, String.class, Integer.class);
        javaType.narrowContentsBy(Integer[].class);
    }

    @Test
    public void testWidenContentsBy() {
        JavaType javaType = TypeFactory.defaultInstance().constructMapType(Map.class, String.class, Integer.class);
        JavaType stringStringMapType = TypeFactory.defaultInstance().constructMapType(Map.class, String.class, String.class);

        JavaType stringStringMapTypeWidened = javaType.widenContentsBy(stringStringMapType.getRawClass());
        assertEquals(stringStringMapType, stringStringMapTypeWidened);

        JavaType stringIntegerMapType = TypeFactory.defaultInstance().constructMapType(Map.class, String.class, Integer.class);
        JavaType stringStringMapTypeNarrowed = javaType.narrowContentsBy(String.class);
        assertNull(stringStringMapTypeNarrowed);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testWidenContentsByWithInvalidContentClass() {
        JavaType javaType = TypeFactory.defaultInstance().constructMapType(Map.class, String.class, Integer.class);
        javaType.widenContentsBy(Integer[].class);
    }

    @Test
    public void testGetGenericSignature() {
        JavaType javaType = TypeFactory.defaultInstance().constructMapType(Map.class, String.class, Integer.class);

        StringBuilder signature = new StringBuilder(100);
        javaType.getGenericSignature(signature);

        assertEquals("java.util.Map<java.lang.String,java.lang.Integer>", signature.toString());
    }

    @Test
    public void testGetErasedSignature() {
        JavaType javaType = TypeFactory.defaultInstance().constructMapType(Map.class, String.class, Integer.class);

        StringBuilder signature = new StringBuilder(50);
        javaType.getErasedSignature(signature);

        assertEquals("java.util.Map", signature.toString());
    }
}