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
    public void testConstruct_InvalidType() {
        SimpleType.construct(Map.class);
    }

    @Test
    public void testConstruct_ValidType() {
        Class<?> cls = Integer.class;
        SimpleType simpleType = SimpleType.construct(cls);
        assertNotNull(simpleType);
        assertEquals(cls, simpleType.getRawClass());
    }

    @Test
    public void testGetCanonicalName() {
        Class<?> cls = Integer[].class;
        SimpleType simpleType = SimpleType.construct(cls);
        assertEquals("[Ljava.lang.Integer;", simpleType.getCanonicalName());
    }

    @Test
    public void testGetErasedSignature() {
        Class<?> cls = Map.class;
        SimpleType simpleType = SimpleType.construct(cls);
        StringBuilder sb = new StringBuilder();
        simpleType.getErasedSignature(sb);
        assertEquals("Map", sb.toString());
    }

    @Test
    public void testGetGenericSignature() {
        Class<?> klass = Map.class;
        SimpleType simpleType = SimpleType.construct(klass);
        StringBuilder sb = new StringBuilder();
        simpleType.getGenericSignature(sb);
        assertEquals("Map<K, V>", sb.toString());
    }

    @Test
    public void testIsContainerType() {
        Class<?> cls = Map.class;
        SimpleType simpleType = SimpleType.construct(cls);
        assertFalse(simpleType.isContainerType());
    }

    @Test
    public void testEquals() {
        Class<?> cls = Integer.class;
        SimpleType simpleType1 = SimpleType.construct(cls);
        SimpleType simpleType2 = SimpleType.construct(cls);
        assertTrue(simpleType1.equals(simpleType2));
    }

    @Test
    public void testNotEquals() {
        Class<?> cls1 = Integer.class;
        Class<?> cls2 = String.class;
        SimpleType simpleType1 = SimpleType.construct(cls1);
        SimpleType simpleType2 = SimpleType.construct(cls2);
        assertFalse(simpleType1.equals(simpleType2));
    }

    @Test
    public void testHashCode() {
        Class<?> cls = Integer.class;
        SimpleType simpleType1 = SimpleType.construct(cls);
        SimpleType simpleType2 = SimpleType.construct(cls);
        assertEquals(simpleType1.hashCode(), simpleType2.hashCode());

        Class<?> cls2 = String.class;
        SimpleType simpleType3 = SimpleType.construct(cls2);
        assertNotEquals(simpleType1.hashCode(), simpleType3.hashCode());
    }

    @Test
    public void testToString() {
        Class<?> cls = Integer.class;
        SimpleType simpleType = SimpleType.construct(cls);
        assertEquals("[simple type, class " + cls.getName() + ']', simpleType.toString());
    }
}