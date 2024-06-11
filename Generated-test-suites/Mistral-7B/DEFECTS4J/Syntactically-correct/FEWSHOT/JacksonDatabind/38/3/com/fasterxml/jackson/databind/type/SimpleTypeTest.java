package com.fasterxml.jackson.databind.type;

import static org.junit.Assert.*;

import org.junit.Test;

public class SimpleTypeTest {

    @Test
    public void testConstructUnsafe() {
        Class<?> raw = String.class;
        SimpleType simpleType = SimpleType.constructUnsafe(raw);
        assertEquals(String.class, simpleType.getRawClass());
        assertNull(simpleType.getSuperClass());
        assertNull(simpleType.getSuperInterfaces());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructUnsafeWithArray() {
        Class<?> raw = int[].class;
        SimpleType simpleType = SimpleType.constructUnsafe(raw);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructUnsafeWithMap() {
        Class<?> raw = HashMap.class;
        SimpleType simpleType = SimpleType.constructUnsafe(raw);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructUnsafeWithCollection() {
        Class<?> raw = ArrayList.class;
        SimpleType simpleType = SimpleType.constructUnsafe(raw);
    }

    @Test
    public void testConstruct() {
        Class<?> raw = String.class;
        SimpleType simpleType = SimpleType.construct(raw);
        assertEquals(String.class, simpleType.getRawClass());
        assertNull(simpleType.getSuperClass());
        assertNull(simpleType.getSuperInterfaces());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructWithMap() {
        Class<?> raw = HashMap.class;
        SimpleType simpleType = SimpleType.construct(raw);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructWithCollection() {
        Class<?> raw = ArrayList.class;
        SimpleType simpleType = SimpleType.construct(raw);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructWithArray() {
        Class<?> raw = int[].class;
        SimpleType simpleType = SimpleType.construct(raw);
    }

    @Test
    public void testNarrow() {
        Class<?> raw = String.class;
        SimpleType original = new SimpleType(raw, null, null, null);
        SimpleType narrow = original._narrow(StringBuilder.class);
        assertEquals(String.class, narrow.getRawClass());
        assertNull(narrow.getSuperClass());
        assertNull(narrow.getSuperInterfaces());
    }

    @Test
    public void testNarrowWithSubclass() {
        Class<?> raw = String.class;
        SimpleType original = new SimpleType(raw, null, null, null);
        SimpleType narrow = original._narrow(StringBuilder.class);

        SimpleType narrow2 = new SimpleType(StringBuilder.class, null, null, null);
        SimpleType narrow3 = original._narrow(narrow2.getRawClass());

        assertEquals(StringBuilder.class, narrow3.getRawClass());
        assertNull(narrow3.getSuperClass());
        assertNull(narrow3.getSuperInterfaces());
    }

    @Test
    public void testIsContainerType() {
        Class<?> raw = String.class;
        SimpleType simpleType = new SimpleType(raw, null, null, null);
        assertFalse(simpleType.isContainerType());
    }

    @Test
    public void testGetErasedSignature() {
        Class<?> raw = String.class;
        SimpleType simpleType = new SimpleType(raw, null, null, null);

        StringBuilder sb = new StringBuilder();
        simpleType.getErasedSignature(sb);
        assertEquals("java.lang.String", sb.toString());
    }

    @Test
    public void testGetGenericSignature() {
        Class<?> raw = String.class;
        SimpleType simpleType = new SimpleType(raw, null, null, null);

        StringBuilder sb = new StringBuilder();
        simpleType.getGenericSignature(sb);
        assertEquals("java.lang.String", sb.toString());

        simpleType = new SimpleType(List.class, null, null, null);

        sb.setLength(0);
        simpleType.getGenericSignature(sb);
        assertEquals("java.util.List<T>", sb.toString());
    }
}