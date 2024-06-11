package com.example.junit;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.TypeFactory;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class JavaTypeTest {

    @Test
    public void testJavaTypeConstruction() {
        JavaType javaType = TypeFactory.defaultInstance().constructType(String[].class);
        assertNotNull(javaType);
        assertEquals(String[].class, javaType.getRawClass());
        assertEquals(String[].class.getName(), javaType.toString());
    }

    @Test
    public void testIsAbstract() {
        JavaType javaType = TypeFactory.defaultInstance().constructType(String[].class);
        assertFalse(javaType.isAbstract());
    }

    @Test
    public void testIsConcrete() {
        JavaType javaType = TypeFactory.defaultInstance().constructType(String[].class);
        assertTrue(javaType.isConcrete());
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
    public void testIsFinal() {
        JavaType javaType = TypeFactory.defaultInstance().constructType(String[].class);
        assertFalse(javaType.isFinal());
    }

    @Test
    public void testIsEnumType() {
        JavaType javaType = TypeFactory.defaultInstance().constructType(Enum.class);
        assertFalse(javaType.isEnumType());
    }

    @Test
    public void testIsThrowable() {
        JavaType javaType = TypeFactory.defaultInstance().constructType(Throwable.class);
        assertTrue(javaType.isThrowable());
    }

    @Test
    public void testIsArrayType() {
        JavaType javaType = TypeFactory.defaultInstance().constructType(String[].class);
        assertTrue(javaType.isArrayType());
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
    public void testContainsTypeCount() {
        JavaType javaType = TypeFactory.defaultInstance().constructType(Map.class);
        assertEquals(2, javaType.containedTypeCount());
    }

    @Test
    public void testContainedType() {
        JavaType javaType = TypeFactory.defaultInstance().constructType(Map.class);
        JavaType keyType = javaType.containedType(0);
        assertEquals(Object.class, keyType.getRawClass());

        JavaType valueType = javaType.containedType(1);
        assertEquals(Object.class, valueType.getRawClass());
    }

    @Test
    public void testGetParameterSource() {
        JavaType javaType = TypeFactory.defaultInstance().constructType(List.class);
        assertEquals(Object.class, javaType.getParameterSource());
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
    public void testNarrowBy() {
        JavaType listType = TypeFactory.defaultInstance().constructType(List.class);
        JavaType stringListType = listType.narrowBy(String[].class);
        assertEquals(String[].class, stringListType.getRawClass());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNarrowByInvalidClass() {
        JavaType listType = TypeFactory.defaultInstance().constructType(List.class);
        JavaType intType = listType.narrowBy(int.class);
    }

    @Test
    public void testWidenBy() {
        JavaType stringListType = TypeFactory.defaultInstance().constructType(String[].class);
        JavaType listType = stringListType.widenBy(List.class);
        assertEquals(List.class, listType.getRawClass());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testWidenByInvalidClass() {
        JavaType stringListType = TypeFactory.defaultInstance().constructType(String[].class);
        JavaType mapType = stringListType.widenBy(Map.class);
    }

    @Test
    public void testNarrowContentsBy() {
        JavaType mapType = TypeFactory.defaultInstance().constructType(Map.class);
        JavaType stringMapType = mapType.narrowContentsBy(String.class);
        assertEquals(String.class, stringMapType.getParameterSource());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNarrowContentsByInvalidClass() {
        JavaType mapType = TypeFactory.defaultInstance().constructType(Map.class);
        JavaType intType = mapType.narrowContentsBy(int.class);
    }

    @Test
    public void testWidenContentsBy() {
        JavaType stringMapType = TypeFactory.defaultInstance().constructType(Map.class, String.class, Integer.class);
        JavaType mapType = stringMapType.widenContentsBy(Map.class);
        assertEquals(Map.class, mapType.getRawClass());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testWidenContentsByInvalidClass() {
        JavaType stringMapType = TypeFactory.defaultInstance().constructType(Map.class, String.class, Integer.class);
        JavaType listType = stringMapType.widenContentsBy(List.class);
    }

    @Test
    public void testUseStaticType() {
        JavaType javaType = TypeFactory.defaultInstance().constructType(String.class);
        assertTrue(javaType.useStaticType());

        JavaType javaType2 = TypeFactory.defaultInstance().constructType(Integer.class);
        assertFalse(javaType2.useStaticType());
    }
}