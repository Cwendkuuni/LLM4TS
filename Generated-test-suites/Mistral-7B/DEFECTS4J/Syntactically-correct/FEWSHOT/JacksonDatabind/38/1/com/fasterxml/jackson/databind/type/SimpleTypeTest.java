package com.fasterxml.jackson.databind.type;

import static org.junit.Assert.*;

import org.junit.Test;

public class SimpleTypeTest {

    @Test
    public void testConstructUnsafe() {
        Class<?> cls = String.class;
        SimpleType simpleType = SimpleType.constructUnsafe(cls);
        assertNotNull(simpleType);
        assertEquals(cls, simpleType.getRawClass());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructUnsafeWithArray() {
        Class<?> cls = String[].class;
        SimpleType simpleType = SimpleType.constructUnsafe(cls);
        fail("Should have thrown IllegalArgumentException");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructUnsafeWithMap() {
        Class<?> cls = HashMap.class;
        SimpleType simpleType = SimpleType.constructUnsafe(cls);
        fail("Should have thrown IllegalArgumentException");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructUnsafeWithCollection() {
        Class<?> cls = ArrayList.class;
        SimpleType simpleType = SimpleType.constructUnsafe(cls);
        fail("Should have thrown IllegalArgumentException");
    }

    @Test
    public void testConstruct() {
        Class<?> cls = String.class;
        SimpleType simpleType = SimpleType.construct(cls);
        assertNotNull(simpleType);
        assertEquals(cls, simpleType.getRawClass());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructWithArray() {
        Class<?> cls = String[].class;
        SimpleType simpleType = SimpleType.construct(cls);
        fail("Should have thrown IllegalArgumentException");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructWithMap() {
        Class<?> cls = HashMap.class;
        SimpleType simpleType = SimpleType.construct(cls);
        fail("Should have thrown IllegalArgumentException");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructWithCollection() {
        Class<?> cls = ArrayList.class;
        SimpleType simpleType = SimpleType.construct(cls);
        fail("Should have thrown IllegalArgumentException");
    }

    @Test
    public void testIsContainerType() {
        SimpleType simpleType = new SimpleType(String.class);
        assertFalse(simpleType.isContainerType());
    }

    @Test
    public void testBuildCanonicalName() {
        SimpleType simpleType = new SimpleType(String[].class);
        assertEquals("[Ljava.lang.String;", simpleType.buildCanonicalName());

        SimpleType simpleTypeWithGeneric = new SimpleType(List.class, new TypeBindings.Builder<>().build());
        assertEquals("java.util.List", simpleTypeWithGeneric.buildCanonicalName());
    }

    @Test
    public void testGetErasedSignature() {
        SimpleType simpleType = new SimpleType(String.class);
        assertEquals("[Ljava/lang/String;", simpleType.getErasedSignature(new StringBuilder()).toString());

        SimpleType simpleTypeWithGeneric = new SimpleType(List.class, new TypeBindings.Builder<>().build());
        assertEquals("java.util.List", simpleTypeWithGeneric.getErasedSignature(new StringBuilder()).toString());
    }

    @Test
    public void testGetGenericSignature() {
        SimpleType simpleType = new SimpleType(String.class);
        StringBuilder sb = new StringBuilder();
        simpleType.getGenericSignature(sb);
        assertEquals("[Ljava/lang/String;", sb.toString());

        SimpleType simpleTypeWithGeneric = new SimpleType(List.class, new TypeBindings.Builder<>().build());
        StringBuilder sb1 = new StringBuilder();
        simpleTypeWithGeneric.getGenericSignature(sb1);
        assertEquals("java.util.List<T>", sb1.toString());
    }
}