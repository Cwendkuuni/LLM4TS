package com.fasterxml.jackson.databind.type;

import static org.junit.Assert.*;

import org.junit.Test;

public class SimpleTypeTest {

    @Test
    public void testConstructUnsafe() {
        Class<?> cls = String.class;
        SimpleType simpleType = SimpleType.constructUnsafe(cls);
        assertEquals(cls, simpleType.getRawClass());
        assertNull(simpleType.getSuperClass());
        assertNull(simpleType.getSuperInterfaces());
        assertFalse(simpleType.isStatic());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructUnsafeWithArray() {
        Class<?> cls = int[].class;
        SimpleType.constructUnsafe(cls);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructUnsafeWithMap() {
        Class<?> cls = HashMap.class;
        SimpleType.constructUnsafe(cls);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructUnsafeWithCollection() {
        Class<?> cls = ArrayList.class;
        SimpleType.constructUnsafe(cls);
    }

    @Test
    public void testConstruct() {
        Class<?> cls = String.class;
        SimpleType simpleType = SimpleType.construct(cls);
        assertEquals(cls, simpleType.getRawClass());
        assertNull(simpleType.getSuperClass());
        assertNull(simpleType.getSuperInterfaces());
        assertFalse(simpleType.isStatic());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructWithArray() {
        Class<?> cls = int[].class;
        SimpleType.construct(cls);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructWithMap() {
        Class<?> cls = HashMap.class;
        SimpleType.construct(cls);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructWithCollection() {
        Class<?> cls = ArrayList.class;
        SimpleType.construct(cls);
    }

    @Test
    public void testIsContainerType() {
        SimpleType simpleType = new SimpleType(String.class, TypeBindings.emptyBindings(), null, null);
        assertFalse(simpleType.isContainerType());
    }

    @Test
    public void testGetErasedSignature() {
        SimpleType simpleType = new SimpleType(String.class, TypeBindings.emptyBindings(), null, null);
        StringBuilder sb = new StringBuilder();
        simpleType.getErasedSignature(sb);
        assertEquals("java.lang.String", sb.toString());
    }

    @Test
    public void testGetGenericSignature() {
        SimpleType simpleType = new SimpleType(String.class, TypeBindings.emptyBindings(), null, null);
        StringBuilder sb = new StringBuilder();
        simpleType.getGenericSignature(sb);
        assertEquals("java.lang.String", sb.toString());
    }

    @Test
    public void testGetCanonicalName() {
        SimpleType simpleType = new SimpleType(String.class, TypeBindings.emptyBindings(), null, null);
        assertEquals("java.lang.String", simpleType.getCanonicalName());
    }

    @Test
    public void testNarrow() {
        SimpleType simpleType1 = new SimpleType(String.class, TypeBindings.emptyBindings(), null, null);
        SimpleType simpleType2 = new SimpleType(String[].class, TypeBindings.emptyBindings(), null, null);
        SimpleType narrowedType = simpleType1.narrow(String[].class);
        assertNull(narrowedType);
    }

    @Test
    public void testWithStaticTyping() {
        SimpleType simpleType = new SimpleType(String.class, TypeBindings.emptyBindings(), null, null);
        SimpleType withStaticTyping = simpleType.withStaticTyping();
        assertTrue(withStaticTyping.isStatic());
    }
}