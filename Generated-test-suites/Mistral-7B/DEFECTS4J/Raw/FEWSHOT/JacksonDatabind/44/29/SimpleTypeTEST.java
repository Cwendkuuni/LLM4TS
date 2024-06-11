package com.fasterxml.jackson.databind.type;

import static org.junit.Assert.*;

import java.util.Arrays;

import org.junit.Test;

public class SimpleTypeTest {

    @Test
    public void testConstructUnsafe() {
        Class<?> cls = String.class;
        SimpleType simpleType = SimpleType.constructUnsafe(cls);
        assertEquals(cls, simpleType.getType());
        assertNull(simpleType.getSuperClass());
        assertNull(simpleType.getSuperInterfaces());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructFailedForMap() {
        Class<?> cls = HashMap.class;
        SimpleType.construct(cls);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructFailedForCollection() {
        Class<?> cls = ArrayList.class;
        SimpleType.construct(cls);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructFailedForArray() {
        Class<?> cls = int[].class;
        SimpleType.construct(cls);
    }

    @Test
    public void testIsContainerType() {
        SimpleType simpleType1 = SimpleType.construct(String.class);
        assertFalse(simpleType1.isContainerType());

        SimpleType simpleType2 = SimpleType.construct(ArrayList.class);
        assertTrue(simpleType2.isContainerType());
    }

    @Test
    public void testGetErasedSignature() {
        SimpleType simpleType = SimpleType.construct(String.class);
        StringBuilder sb = new StringBuilder();
        simpleType.getErasedSignature(sb);
        assertEquals("java.lang.String", sb.toString());
    }

    @Test
    public void testGetGenericSignature() {
        SimpleType simpleType = SimpleType.construct(String.class);
        StringBuilder sb = new StringBuilder();
        simpleType.getGenericSignature(sb);
        assertEquals("java.lang.String", sb.toString());

        SimpleType listType = SimpleType.construct(ArrayList.class);
        StringBuilder sbList = new StringBuilder();
        listType.getGenericSignature(sbList);
        assertEquals("java.util.List<E>", sbList.toString());
    }

    @Test
    public void testWithTypeHandler() {
        SimpleType simpleType = SimpleType.construct(String.class);
        Object handler = new Object() {};
        SimpleType simpleTypeWithHandler = simpleType.withTypeHandler(handler);
        assertEquals(simpleType.getType(), simpleTypeWithHandler.getType());
        assertEquals(handler, simpleTypeWithHandler._typeHandler);
    }

    @Test
    public void testWithValueHandler() {
        SimpleType simpleType = SimpleType.construct(String.class);
        Object handler = new Object() {};
        SimpleType simpleTypeWithHandler = simpleType.withValueHandler(handler);
        assertEquals(simpleType.getType(), simpleTypeWithHandler.getType());
        assertEquals(handler, simpleTypeWithHandler._valueHandler);
    }

    @Test
    public void testWithStaticTyping() {
        SimpleType simpleType = SimpleType.construct(String.class);
        SimpleType simpleTypeStatic = simpleType.withStaticTyping();
        assertFalse(simpleTypeStatic._asStatic);
        assertTrue(simpleType._asStatic);
    }

    @Test
    public void testRefine() {
        SimpleType simpleType = SimpleType.construct(String.class);
        assertNull(simpleType.refine(String[].class, null, null, null));
    }

    @Test
    public void testBuildCanonicalName() {
        SimpleType simpleType1 = SimpleType.construct(String.class);
        assertEquals("java.lang.String", simpleType1.buildCanonicalName());

        SimpleType listType1 = SimpleType.construct(ArrayList.class);
        assertEquals("java.util.List<E>", listType1.buildCanonicalName());

        SimpleType listType2 = SimpleType.construct(ArrayList.class, Arrays.asList(Integer.TYPE));
        assertEquals("java.util.List<java.lang.Integer>", listType2.buildCanonicalName());
    }
}