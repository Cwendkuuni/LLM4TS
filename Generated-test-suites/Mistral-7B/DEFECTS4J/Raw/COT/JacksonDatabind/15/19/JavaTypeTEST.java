package com.example.junit;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.lang.reflect.Modifier;
import java.util.Arrays;

import org.junit.Test;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.TypeFactory;

public class JavaTypeTest {
    @Test
    public void testJavaTypeConstructor() {
        Class<String> rawClass = String.class;
        Object valueHandler = new Object();
        Object typeHandler = new Object();
        boolean asStatic = false;

        JavaType javaType = new JavaType(rawClass, 0, valueHandler, typeHandler, asStatic);

        assertEquals(rawClass, javaType.getRawClass());
        assertEquals(valueHandler, javaType.getValueHandler());
        assertEquals(typeHandler, javaType.getTypeHandler());
        assertEquals(asStatic, javaType.useStaticType());
    }

    @Test
    public void testJavaTypeWithTypeHandler() {
        Class<String> rawClass = String.class;
        Object valueHandler = new Object();
        Object typeHandler = new Object();
        boolean asStatic = false;

        JavaType javaType = new JavaType(rawClass, 0, valueHandler, typeHandler, asStatic);

        JavaType withTypeHandler = javaType.withTypeHandler(new Object());

        assertEquals(javaType.getRawClass(), withTypeHandler.getRawClass());
        assertNotNull(withTypeHandler.getTypeHandler());
        assertNotSame(typeHandler, withTypeHandler.getTypeHandler());
    }

    @Test
    public void testJavaTypeWithContentTypeHandler() {
        Class<String> rawClass = String[].class;
        Object valueHandler = new Object();
        Object typeHandler = new Object();
        boolean asStatic = false;

        JavaType javaType = new JavaType(rawClass, 0, valueHandler, typeHandler, asStatic);

        JavaType withContentTypeHandler = javaType.withContentTypeHandler(new Object());

        assertEquals(javaType.getRawClass(), withContentTypeHandler.getRawClass());
        assertNotNull(withContentTypeHandler.getTypeHandler());
        assertNotSame(typeHandler, withContentTypeHandler.getTypeHandler());
    }

    @Test
    public void testJavaTypeWithValueHandler() {
        Class<String> rawClass = String.class;
        Object valueHandler = new Object();
        Object typeHandler = new Object();
        boolean asStatic = false;

        JavaType javaType = new JavaType(rawClass, 0, valueHandler, typeHandler, asStatic);

        JavaType withValueHandler = javaType.withValueHandler(new Object());

        assertEquals(javaType.getRawClass(), withValueHandler.getRawClass());
        assertNotNull(withValueHandler.getValueHandler());
        assertNotSame(valueHandler, withValueHandler.getValueHandler());
    }

    @Test
    public void testJavaTypeWithContentValueHandler() {
        Class<String[]> rawClass = String[].class;
        Object valueHandler = new Object();
        Object typeHandler = new Object();
        boolean asStatic = false;

        JavaType javaType = new JavaType(rawClass, 0, valueHandler, typeHandler, asStatic);

        JavaType withContentValueHandler = javaType.withContentValueHandler(new Object());

        assertEquals(javaType.getRawClass(), withContentValueHandler.getRawClass());
        assertNotNull(withContentValueHandler.getValueHandler());
        assertNotSame(valueHandler, withContentValueHandler.getValueHandler());
    }

    @Test
    public void testJavaTypeWithStaticTyping() {
        Class<String> rawClass = String.class;
        Object valueHandler = new Object();
        Object typeHandler = new Object();
        boolean asStatic = true;

        JavaType javaType = new JavaType(rawClass, 0, valueHandler, typeHandler, asStatic);

        JavaType withStaticTyping = javaType.withStaticTyping();

        assertEquals(javaType.getRawClass(), withStaticTyping.getRawClass());
        assertTrue(withStaticTyping.useStaticType());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testJavaTypeNarrowByException() {
        Class<String> rawClass = String.class;
        Object valueHandler = new Object();
        Object typeHandler = new Object();
        boolean asStatic = false;

        JavaType javaType = new JavaType(rawClass, 0, valueHandler, typeHandler, asStatic);

        javaType.narrowBy(Integer.class);
    }

    @Test
    public void testJavaTypeNarrowBy() {
        Class<String> rawClass = String.class;
        Object valueHandler = new Object();
        Object typeHandler = new Object();
        boolean asStatic = false;

        JavaType javaType = new JavaType(rawClass, 0, valueHandler, typeHandler, asStatic);

        JavaType narrowBy = javaType.narrowBy(String[].class);

        assertEquals(javaType.getRawClass(), narrowBy.getRawClass());
        assertTrue(narrowBy.isArrayType());
    }

    @Test
    public void testJavaTypeWidenBy() {
        Class<String> rawClass = String.class;
        Object valueHandler = new Object();
        Object typeHandler = new Object();
        boolean asStatic = false;

        JavaType javaType = new JavaType(rawClass, 0, valueHandler, typeHandler, asStatic);

        JavaType widenBy = javaType.widenBy(Object.class);

        assertEquals(javaType.getRawClass(), widenBy.getRawClass());
        assertTrue(widenBy.isAssignableFrom(TypeFactory.unknownType()));
    }

    @Test
    public void testJavaTypeNarrowContentsBy() {
        Class<String[]> rawClass = String[].class;
        Object valueHandler = new Object();
        Object typeHandler = new Object();
        boolean asStatic = false;

        JavaType javaType = new JavaType(rawClass, 0, valueHandler, typeHandler, asStatic);

        JavaType narrowContentsBy = javaType.narrowContentsBy(Integer[].class);

        assertEquals(javaType.getRawClass(), narrowContentsBy.getRawClass());
        assertTrue(narrowContentsBy.isArrayType());
        assertTrue(Arrays.equals(Integer.TYPE, narrowContentsBy.getContentType().getRawClass()));
    }

    @Test
    public void testJavaTypeWidenContentsBy() {
        Class<String[]> rawClass = String[].class;
        Object valueHandler = new Object();
        Object typeHandler = new Object();
        boolean asStatic = false;

        JavaType javaType = new JavaType(rawClass, 0, valueHandler, typeHandler, asStatic);

        JavaType widenContentsBy = javaType.widenContentsBy(Object[].class);

        assertEquals(javaType.getRawClass(), widenContentsBy.getRawClass());
        assertTrue(widenContentsBy.isArrayType());
        assertTrue(widenContentsBy.getContentType().isAssignableFrom(TypeFactory.unknownType()));
    }
}