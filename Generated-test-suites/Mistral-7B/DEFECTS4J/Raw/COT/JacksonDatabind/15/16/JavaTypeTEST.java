package com.fasterxml.jackson.databind.test;

import static org.junit.Assert.*;

import java.lang.reflect.Modifier;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.core.type.TypeFactory;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.TypeFactory.DefaultTypeFactory;

public class JavaTypeTest {

    private TypeFactory typeFactory;

    @Before
    public void setUp() {
        typeFactory = new DefaultTypeFactory();
    }

    @Test
    public void testConstructor() {
        JavaType javaType = typeFactory.constructType(String.class);
        assertNotNull(javaType);
        assertEquals(String.class, javaType.getRawClass());
    }

    @Test
    public void testIsAbstract() {
        JavaType javaType = typeFactory.constructType(String.class);
        assertFalse(javaType.isAbstract());
    }

    @Test
    public void testIsConcrete() {
        JavaType javaType = typeFactory.constructType(String.class);
        assertTrue(javaType.isConcrete());
    }

    @Test
    public void testIsEnumType() {
        JavaType javaType = typeFactory.constructType(Enum.class);
        assertTrue(javaType.isEnumType());
    }

    @Test
    public void testIsInterface() {
        JavaType javaType = typeFactory.constructType(List.class);
        assertTrue(javaType.isInterface());
    }

    @Test
    public void testIsPrimitive() {
        JavaType javaType = typeFactory.constructType(int.class);
        assertTrue(javaType.isPrimitive());
    }

    @Test
    public void testIsFinal() {
        JavaType javaType = typeFactory.constructType(String.class);
        assertFalse(javaType.isFinal());
    }

    @Test
    public void testIsContainerType() {
        JavaType javaType = typeFactory.constructType(List.class);
        assertTrue(javaType.isContainerType());
    }

    @Test
    public void testIsCollectionLikeType() {
        JavaType javaType = typeFactory.constructType(List.class);
        assertTrue(javaType.isCollectionLikeType());
    }

    @Test
    public void testIsMapLikeType() {
        JavaType javaType = typeFactory.constructType(Map.class);
        assertTrue(javaType.isMapLikeType());
    }

    @Test
    public void testUseStaticType() {
        JavaType javaType = typeFactory.constructType(String.class);
        assertFalse(javaType.useStaticType());

        javaType = typeFactory.constructType(String[].class);
        assertTrue(javaType.useStaticType());
    }

    @Test
    public void testNarrow() {
        JavaType listType = typeFactory.constructType(List.class);
        JavaType stringListType = listType.narrowBy(String[].class);
        assertEquals(String[].class, stringListType.getRawClass());

        JavaType unsupportedType = typeFactory.constructType(Object.class);
        try {
            JavaType unsupportedNarrow = listType.narrowBy(unsupportedType.getClass());
            fail();
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    @Test
    public void testWiden() {
        JavaType stringType = typeFactory.constructType(String.class);
        JavaType objectType = stringType.widenBy(Object.class);
        assertEquals(Object.class, objectType.getRawClass());

        JavaType unsupportedType = typeFactory.constructType(int.class);
        try {
            JavaType unsupportedWiden = stringType.widenBy(unsupportedType.getClass());
            fail();
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    @Test
    public void testNarrowContentsBy() {
        JavaType listType = typeFactory.constructType(List.class);
        JavaType stringListType = listType.narrowContentsBy(String.class);
        assertEquals(String.class, stringListType.getRawClass());

        JavaType unsupportedType = typeFactory.constructType(Map.class);
        try {
            JavaType unsupportedNarrow = listType.narrowContentsBy(unsupportedType.getClass());
            fail();
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    @Test
    public void testWidenContentsBy() {
        JavaType listType = typeFactory.constructType(List.class);
        JavaType objectListType = listType.widenContentsBy(Object[].class);
        assertEquals(Object[].class, objectListType.getRawClass());

        JavaType unsupportedType = typeFactory.constructType(Map.class);
        try {
            JavaType unsupportedWiden = listType.widenContentsBy(unsupportedType.getClass());
            fail();
        } catch (IllegalArgumentException e) {
            // expected
        }
    }
}