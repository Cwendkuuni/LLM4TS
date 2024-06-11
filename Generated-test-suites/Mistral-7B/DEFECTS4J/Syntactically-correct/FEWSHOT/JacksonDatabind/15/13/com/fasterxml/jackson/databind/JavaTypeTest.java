package com.fasterxml.jackson.databind.test;

import static org.junit.Assert.*;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.TypeFactory;

import java.lang.reflect.Modifier;

import org.junit.Test;

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
    public void testIsInterface() {
        JavaType javaType = TypeFactory.defaultInstance().constructType(String[].class);
        assertFalse(javaType.isInterface());
    }

    @Test
    public void testIsPrimitive() {
        JavaType javaType = TypeFactory.defaultInstance().constructType(boolean.class);
        assertTrue(javaType.isPrimitive());
    }

    @Test
    public void testIsFinal() {
        JavaType javaType = TypeFactory.defaultInstance().constructType(String[].class);
        assertFalse(javaType.isFinal());
    }

    @Test
    public void testIsContainerType() {
        JavaType javaType = TypeFactory.defaultInstance().constructType(String[].class);
        assertTrue(javaType.isContainerType());
    }

    @Test
    public void testIsCollectionLikeType() {
        JavaType javaType = TypeFactory.defaultInstance().constructType(java.util.List.class);
        assertTrue(javaType.isCollectionLikeType());
    }

    @Test
    public void testIsMapLikeType() {
        JavaType javaType = TypeFactory.defaultInstance().constructType(java.util.Map.class);
        assertTrue(javaType.isMapLikeType());
    }

    @Test
    public void testUseStaticType() {
        JavaType javaType = TypeFactory.defaultInstance().constructType(int[].class);
        assertFalse(javaType.useStaticType());
    }

    @Test
    public void testNarrowBy() {
        TypeFactory typeFactory = TypeFactory.defaultInstance();
        JavaType javaType = typeFactory.constructType(String[].class);
        JavaType narrowType = javaType.narrowBy(String[].class);
        assertEquals(javaType, narrowType);

        JavaType intType = typeFactory.constructType(int[].class);
        narrowType = javaType.narrowBy(intType);
        assertFalse(narrowType.isInstance(new String[]{}));
        assertTrue(narrowType.isInstance(new int[]{}));

        try {
            JavaType floatType = typeFactory.constructType(float[].class);
            javaType.narrowBy(floatType);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    @Test
    public void testWidenBy() {
        TypeFactory typeFactory = TypeFactory.defaultInstance();
        JavaType javaType = typeFactory.constructType(int[].class);
        JavaType widenType = javaType.widenBy(String[].class);
        assertTrue(widenType.isAssignableFrom(javaType));
        assertFalse(javaType.isAssignableFrom(widenType));

        try {
            JavaType floatType = typeFactory.constructType(float[].class);
            javaType.widenBy(floatType);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    @Test
    public void testNarrowContentsBy() {
        TypeFactory typeFactory = TypeFactory.defaultInstance();
        JavaType javaType = typeFactory.constructType(java.util.List.class);
        JavaType listOfInts = javaType.narrowContentsBy(int[].class);
        JavaType listOfStrings = javaType.narrowContentsBy(String[].class);

        assertFalse(listOfInts.isAssignableFrom(listOfStrings));
        assertFalse(listOfStrings.isAssignableFrom(listOfInts));

        JavaType listOfObject = javaType.narrowContentsBy(Object[].class);
        assertTrue(listOfInts.isAssignableFrom(listOfObject));
        assertTrue(listOfStrings.isAssignableFrom(listOfObject));
    }

    @Test
    public void testWidenContentsBy() {
        TypeFactory typeFactory = TypeFactory.defaultInstance();
        JavaType javaType = typeFactory.constructType(java.util.List.class);
        JavaType listOfInts = javaType.narrowContentsBy(int[].class);
        JavaType listOfObjects = javaType.widenContentsBy(Object[].class);

        assertTrue(listOfInts.isAssignableFrom(listOfObjects));
        assertFalse(listOfObjects.isAssignableFrom(listOfInts));
    }
}