package com.fasterxml.jackson.databind.type;

import static org.junit.Assert.*;

import java.util.Arrays;

import org.junit.Test;

public class SimpleTypeTest {
    @Test
    public void testSimpleType() {
        Class<?> cls = String.class;
        SimpleType simpleType = new SimpleType(cls);
        assertEquals(cls, simpleType.getRawClass());
        assertTrue(simpleType.isContainerType()); // should be false
        assertNull(simpleType.getSuperClass());
        assertNull(simpleType.getSuperInterfaces());
        assertArrayEquals(new Class[0], simpleType.getContentTypes());
        assertEquals("java.lang.String", simpleType.getCanonicalName());
        assertEquals("[simple type, class java.lang.String]", simpleType.toString());
    }

    @Test
    public void testSimpleTypeWithSuperClass() {
        Class<?> cls = Number.class;
        JavaType superClass = new SimpleType(Integer.class);
        SimpleType simpleType = new SimpleType(cls, null, superClass, null);
        assertEquals(cls, simpleType.getRawClass());
        assertNotNull(simpleType.getSuperClass());
        assertEquals(Integer.class, simpleType.getSuperClass().getRawClass());
        assertNull(simpleType.getSuperInterfaces());
        assertArrayEquals(new Class[0], simpleType.getContentTypes());
        assertEquals("java.lang.Number", simpleType.getCanonicalName());
        assertEquals("[simple type, class java.lang.Number]", simpleType.toString());

        assertEquals(superClass, simpleType.getSuperClass());
    }

    @Test
    public void testSimpleTypeWithSuperInterfaces() {
        Class<?> cls = List.class;
        JavaType superClass = new SimpleType(ArrayList.class);
        JavaType[] superInterfaces = new JavaType[] { new SimpleType(Collection.class), new SimpleType(Iterable.class) };
        SimpleType simpleType = new SimpleType(cls, null, null, superInterfaces);
        assertEquals(cls, simpleType.getRawClass());
        assertNull(simpleType.getSuperClass());
        assertArrayEquals(superInterfaces, simpleType.getSuperInterfaces());
        assertArrayEquals(new Class[0], simpleType.getContentTypes());
        assertEquals("java.util.List", simpleType.getCanonicalName());
        assertEquals("[simple type, class java.util.List]", simpleType.toString());
    }

    @Test
    public void testSimpleTypeWithValueHandler() {
        Class<?> cls = Integer.class;
        Object valueHandler = null;
        SimpleType simpleType = new SimpleType(cls, null, null, null, valueHandler, null, false);
        assertEquals(cls, simpleType.getRawClass());
        assertNull(simpleType.getValueHandler());
        simpleType = new SimpleType(cls, null, null, null, null, null, false, valueHandler);
        assertEquals(valueHandler, simpleType.getValueHandler());
    }

    @Test
    public void testSimpleTypeWithTypeHandler() {
        Class<?> cls = Integer.class;
        Object typeHandler = null;
        SimpleType simpleType = new SimpleType(cls, null, null, null, null, typeHandler, false);
        assertNull(simpleType.getTypeHandler());
        simpleType = new SimpleType(cls, null, null, null, null, null, false, null, typeHandler);
        assertEquals(typeHandler, simpleType.getTypeHandler());
    }

    @Test
    public void testSimpleTypeWithStaticTyping() {
        Class<?> cls = Integer.class;
        SimpleType simpleType = new SimpleType(cls, null, null, null, null, null, false);
        assertFalse(simpleType.isStaticTyping());
        simpleType = new SimpleType(cls, null, null, null, null, null, true);
        assertTrue(simpleType.isStaticTyping());
    }

    @Test
    public void testSimpleTypeWithBindings() {
        Class<?> cls = Map.class;
        JavaType keyType = new SimpleType(String.class);
        JavaType valueType = new SimpleType(Number.class);
        SimpleType simpleType = new SimpleType(cls, new SimpleType.TypeBindings(keyType, valueType));
        assertEquals(cls, simpleType.getRawClass());
        assertNotNull(simpleType.getBindings());
        assertEquals(2, simpleType.getBindings().size());
        assertEquals(keyType, simpleType.getBindings().getKeyType());
        assertEquals(valueType, simpleType.getBindings().getValueType());
    }
}