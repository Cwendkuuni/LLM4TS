package com.fasterxml.jackson.databind;

import static org.junit.Assert.*;

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
    public void testIsInterface() {
        JavaType javaType = TypeFactory.defaultInstance().constructType(String[].class);
        assertFalse(javaType.isInterface());
    }

    @Test
    public void testIsPrimitive() {
        JavaType javaType = TypeFactory.defaultInstance().constructType(int.class);
        assertTrue(javaType.isPrimitive());
    }

    @Test
    public void testIsContainerType() {
        JavaType javaType = TypeFactory.defaultInstance().constructType(String[].class);
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
        JavaType javaType = TypeFactory.defaultInstance().constructType(String.class);
        assertFalse(javaType.useStaticType());
    }

    @Test
    public void testHasGenericTypes() {
        JavaType javaType = TypeFactory.defaultInstance().constructType(List.class);
        assertTrue(javaType.hasGenericTypes());
    }

    @Test
    public void testKeyType() {
        JavaType javaType = TypeFactory.defaultInstance().constructType(Map.class);
        assertEquals(Object.class, javaType.getKeyType());
    }

    @Test
    public void testContentType() {
        JavaType javaType = TypeFactory.defaultInstance().constructType(Map.class);
        assertEquals(Object.class, javaType.getContentType());
    }

    @Test
    public void testContainedTypeCount() {
        JavaType javaType = TypeFactory.defaultInstance().constructType(Map.class);
        assertEquals(2, javaType.containedTypeCount());
    }

    @Test
    public void testContainedType() {
        JavaType javaType = TypeFactory.defaultInstance().constructType(Map.class);
        JavaType containedType = javaType.containedType(0);
        assertEquals(Object.class, containedType.getRawClass());

        containedType = javaType.containedType(1);
        assertEquals(Object.class, containedType.getRawClass());
    }

    @Test
    public void testContainedTypeName() {
        JavaType javaType = TypeFactory.defaultInstance().constructType(Map.class);
        assertEquals("K", javaType.containedTypeName(0));
        assertEquals("V", javaType.containedTypeName(1));
    }

    @Test
    public void testGetParameterSource() {
        JavaType javaType = TypeFactory.defaultInstance().constructType(Map.class);
        assertEquals(Map.class, javaType.getParameterSource());
    }

    @Test
    public void testGetRawClass() {
        JavaType javaType = TypeFactory.defaultInstance().constructType(String[].class);
        assertEquals(String[].class, javaType.getRawClass());
    }

    @Test
    public void testHasRawClass() {
        JavaType javaType = TypeFactory.defaultInstance().constructType(String[].class);
        assertTrue(javaType.hasRawClass(String[].class));
    }

    @Test
    public void testIsEnumType() {
        JavaType javaType = TypeFactory.defaultInstance().constructType(Boolean.class);
        assertTrue(javaType.isEnumType());
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

    @Test(expected = UnsupportedOperationException.class)
    public void testToString() {
        JavaType javaType = TypeFactory.defaultInstance().constructType(String[].class);
        javaType.toString();
    }

    @Test
    public void testEquals() {
        JavaType javaType1 = TypeFactory.defaultInstance().constructType(String[].class);
        JavaType javaType2 = TypeFactory.defaultInstance().constructType(String[].class);
        assertTrue(javaType1.equals(javaType2));
    }

    @Test
    public void testHashCode() {
        JavaType javaType1 = TypeFactory.defaultInstance().constructType(String[].class);
        JavaType javaType2 = TypeFactory.defaultInstance().constructType(String[].class);
        assertEquals(javaType1.hashCode(), javaType2.hashCode());
    }
}