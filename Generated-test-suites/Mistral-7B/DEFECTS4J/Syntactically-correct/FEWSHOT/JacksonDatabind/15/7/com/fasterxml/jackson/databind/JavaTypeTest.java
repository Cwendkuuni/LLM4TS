package com.fasterxml.jackson.databind.test;

import static org.junit.Assert.*;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.TypeFactory;

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
        assertTrue(javaType.isFinal());
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
        JavaType javaType = TypeFactory.defaultInstance().constructType(int.class);
        assertFalse(javaType.useStaticType());
    }

    @Test
    public void testNarrowBy() {
        JavaType javaTypeInt = TypeFactory.defaultInstance().constructType(int.class);
        JavaType javaTypeInteger = TypeFactory.defaultInstance().constructType(Integer.class);
        JavaType narrowed = javaTypeInt.narrowBy(Integer.class);
        assertEquals(javaTypeInteger, narrowed);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNarrowByFailure() {
        JavaType javaTypeInt = TypeFactory.defaultInstance().constructType(int.class);
        JavaType javaTypeString = TypeFactory.defaultInstance().constructType(String.class);
        javaTypeInt.narrowBy(String.class);
    }

    @Test
    public void testWidenBy() {
        JavaType javaTypeInt = TypeFactory.defaultInstance().constructType(int.class);
        JavaType javaTypeInteger = TypeFactory.defaultInstance().constructType(Integer.class);
        JavaType widened = javaTypeInteger.widenBy(int.class);
        assertEquals(javaTypeInt, widened);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testWidenByFailure() {
        JavaType javaTypeInt = TypeFactory.defaultInstance().constructType(int.class);
        JavaType javaTypeString = TypeFactory.defaultInstance().constructType(String.class);
        javaTypeInt.widenBy(String.class);
    }

    @Test
    public void testNarrowContentsBy() {
        JavaType javaTypeListInt = TypeFactory.defaultInstance().constructType(List.class, int.class);
        JavaType javaTypeListString = TypeFactory.defaultInstance().constructType(List.class, String.class);
        JavaType narrowed = javaTypeListInt.narrowContentsBy(String.class);
        assertEquals(javaTypeListString, narrowed);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNarrowContentsByFailure() {
        JavaType javaTypeListInt = TypeFactory.defaultInstance().constructType(List.class, int.class);
        JavaType javaTypeListString = TypeFactory.defaultInstance().constructType(List.class, String.class);
        javaTypeListInt.narrowContentsBy(int[].class);
    }

    @Test
    public void testWidenContentsBy() {
        JavaType javaTypeListInt = TypeFactory.defaultInstance().constructType(List.class, int.class);
        JavaType javaTypeListString = TypeFactory.defaultInstance().constructType(List.class, String.class);
        JavaType widened = javaTypeListString.widenContentsBy(int[].class);
        assertEquals(javaTypeListInt, widened);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testWidenContentsByFailure() {
        JavaType javaTypeListInt = TypeFactory.defaultInstance().constructType(List.class, int.class);
        JavaType javaTypeListString = TypeFactory.defaultInstance().constructType(List.class, String.class);
        javaTypeListString.widenContentsBy(String[].class);
    }
}