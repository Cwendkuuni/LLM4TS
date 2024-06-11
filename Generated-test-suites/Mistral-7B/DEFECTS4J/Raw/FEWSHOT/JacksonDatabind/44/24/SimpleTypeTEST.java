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
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructIllegalArgumentException() {
        Class<?> cls = Map.class;
        SimpleType.construct(cls);
    }

    @Test
    public void testNarrow() {
        Class<?> cls1 = Integer.class;
        Class<?> cls2 = Integer[].class;
        SimpleType simpleType1 = new SimpleType(cls1, null, null, null, null, null, false);
        SimpleType simpleType2 = new SimpleType(cls2, null, null, null, null, null, false);
        SimpleType result = simpleType1._narrow(cls2);
        assertEquals(simpleType2, result);

        simpleType1 = new SimpleType(String.class, null, null, null, null, null, false);
        assertNull(simpleType1._narrow(Integer.class));
    }

    @Test
    public void testIsContainerType() {
        assertFalse(new SimpleType(String.class, null, null, null, null, null, false).isContainerType());
    }

    @Test
    public void testBuildCanonicalName() {
        SimpleType simpleType = new SimpleType(String[].class, null, null, null, null, null, false);
        assertEquals("[Ljava.lang.String;", simpleType.buildCanonicalName());
    }

    @Test
    public void testGetErasedSignature() {
        SimpleType simpleType = new SimpleType(String[].class, null, null, null, null, null, false);
        assertEquals("[Ljava.lang.String;", simpleType.getErasedSignature(new StringBuilder()).toString());
    }

    @Test
    public void testGetGenericSignature() {
        SimpleType simpleType = new SimpleType(List.class, null, null, null, null, null, false);
        StringBuilder sb = new StringBuilder();
        simpleType.getGenericSignature(sb);
        assertEquals("java.util.List<T>", sb.toString());
    }
}