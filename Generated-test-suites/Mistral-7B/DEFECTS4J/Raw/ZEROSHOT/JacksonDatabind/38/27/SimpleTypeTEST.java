package com.fasterxml.jackson.databind.type;

import org.junit.Test;
import static org.junit.Assert.*;

import java.util.Arrays;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.SimpleType;

public class SimpleTypeTest {

    @Test
    public void testSimpleTypeConstruction() {
        Class<?> cls = String.class;
        SimpleType simpleType = new SimpleType(cls);
        JavaType javaType = simpleType.getType();
        assertEquals(cls, javaType.getRawClass());
        assertEquals(SimpleType.class, javaType.getType());
    }

    @Test
    public void testSimpleTypeWithSuperclass() {
        Class<?> parentClass = Object.class;
        Class<?> cls = String.class;
        SimpleType simpleType = new SimpleType(cls, null, parentClass, null);
        JavaType javaType = simpleType.getType();
        assertEquals(cls, javaType.getRawClass());
        assertEquals(SimpleType.class, javaType.getType());
        assertEquals(parentClass, simpleType.getSuperclass());
        assertEquals(Arrays.asList(JavaType.class), simpleType.getSuperTypes());
    }

    @Test
    public void testSimpleTypeWithBindings() {
        Class<?> cls = List.class;
        Class<?> elementType = Integer.class;
        SimpleType simpleType = new SimpleType(cls, null, null, null, null, null, false);
        JavaType javaType = simpleType.getType();
        assertEquals(Collections.singletonList(elementType).getType(), simpleType.getContentTypes().get(0));
        assertEquals(SimpleType.class, javaType.getType());
        assertEquals(List.class, javaType.getRawClass());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIllegalArgumentExceptionForInvalidType() {
        SimpleType simpleType = new SimpleType(Map.class);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIllegalArgumentExceptionForInvalidSuperclass() {
        Class<?> parentClass = String[].class;
        SimpleType simpleType = new SimpleType(String.class, null, null, null, null, null, false);
        simpleType.setSuperclass(parentClass);
    }

    @Test
    public void testSimpleTypeWithValueHandler() {
        Class<?> cls = String.class;
        SimpleType simpleType = new SimpleType(cls, null, null, null, null, null, false);
        JavaType javaType = simpleType.withValueHandler(new Object());
        assertEquals(SimpleType.class, javaType.getType());
        assertEquals(cls, javaType.getRawClass());
        assertNotEquals(simpleType, javaType);
    }

    @Test
    public void testSimpleTypeWithTypeHandler() {
        Class<?> cls = String.class;
        SimpleType simpleType = new SimpleType(cls, null, null, null, null, null, false);
        JavaType javaType = simpleType.withTypeHandler(new Object());
        assertEquals(SimpleType.class, javaType.getType());
        assertEquals(cls, javaType.getRawClass());
        assertNotEquals(simpleType, javaType);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIllegalArgumentExceptionForInvalidWithTypeHandler() {
        SimpleType simpleType = new SimpleType(String.class);
        simpleType.withTypeHandler(null);
    }

    @Test
    public void testSimpleTypeWithStaticTyping() {
        Class<?> cls = String.class;
        SimpleType simpleType = new SimpleType(cls, null, null, null, null, null, false);
        JavaType javaType = simpleType.withStaticTyping();
        assertEquals(SimpleType.class, javaType.getType());
        assertEquals(cls, javaType.getRawClass());
        assertTrue(javaType.isStatic());
        assertFalse(simpleType.isStatic());
    }
}