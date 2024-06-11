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
        assertNull(simpleType.getValueHandler());
        assertNull(simpleType.getTypeHandler());
        assertFalse(simpleType.isStatic());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstruct_Map() {
        Class<?> cls = HashMap.class;
        SimpleType.construct(cls);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstruct_Collection() {
        Class<?> cls = ArrayList.class;
        SimpleType.construct(cls);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstruct_Array() {
        Class<?> cls = String[].class;
        SimpleType.construct(cls);
    }

    @Test
    public void testNarrow() {
        Class<?> cls1 = Integer.class;
        Class<?> cls2 = Integer.TYPE;
        SimpleType simpleType1 = new SimpleType(cls1);
        SimpleType simpleType2 = new SimpleType(cls2);
        SimpleType narrowType1 = simpleType1.narrow(cls2);
        SimpleType narrowType2 = simpleType2.narrow(cls1);
        assertEquals(simpleType2, narrowType1);
        assertEquals(simpleType1, narrowType2);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNarrow_NotSubType() {
        Class<?> cls1 = Integer.class;
        Class<?> cls2 = String.class;
        SimpleType simpleType1 = new SimpleType(cls1);
        SimpleType simpleType2 = new SimpleType(cls2);
        simpleType1.narrow(cls2);
    }

    @Test
    public void testWithTypeHandler() {
        Class<?> cls = String.class;
        Object handler = new Object();
        SimpleType simpleType = new SimpleType(cls);
        SimpleType typeWithHandler = simpleType.withTypeHandler(handler);
        assertEquals(cls, typeWithHandler.getRawClass());
        assertEquals(handler, typeWithHandler.getTypeHandler());
        assertFalse(typeWithHandler.isStatic());
    }

    @Test
    public void testWithStaticTyping() {
        Class<?> cls = String.class;
        SimpleType simpleType = new SimpleType(cls);
        SimpleType staticType = simpleType.withStaticTyping();
        assertTrue(staticType.isStatic());
    }

    @Test
    public void testBuildCanonicalName() {
        Class<?> cls = new ArrayList<Integer>().getClass();
        SimpleType simpleType = new SimpleType(cls);
        assertEquals("java.util.ArrayList<java.lang.Integer>", simpleType.buildCanonicalName());
    }

    @Test
    public void testIsContainerType() {
        Class<?> cls = Integer.class;
        SimpleType simpleType = new SimpleType(cls);
        assertFalse(simpleType.isContainerType());
    }

    @Test
    public void testGetErasedSignature() {
        Class<?> cls = Map.class;
        SimpleType simpleType = new SimpleType(cls);
        StringBuilder expected = new StringBuilder("java.util.Map");
        StringBuilder actual = simpleType.getErasedSignature(new StringBuilder());
        assertEquals(expected.toString(), actual.toString());
    }

    @Test
    public void testGetGenericSignature() {
        Class<?> cls = Map.class;
        SimpleType simpleType = new SimpleType(cls);
        StringBuilder expected = new StringBuilder("java.util.Map<K,V>");
        StringBuilder actual = simpleType.getGenericSignature(new StringBuilder());
        assertEquals(expected.toString(), actual.toString());
    }

    @Test
    public void testToString() {
        Class<?> cls = Integer.class;
        SimpleType simpleType = new SimpleType(cls);
        assertEquals("[simple type, class [I]", simpleType.toString());
    }

    @Test
    public void testEquals() {
        Class<?> cls1 = Integer.class;
        Class<?> cls2 = Integer.TYPE;
        SimpleType simpleType1 = new SimpleType(cls1);
        SimpleType simpleType2 = new SimpleType(cls2);
        SimpleType simpleType3 = new SimpleType(cls1);
        assertTrue(simpleType1.equals(simpleType1));
        assertTrue(simpleType1.equals(simpleType3));
        assertTrue(simpleType1.equals(simpleType2));
        assertFalse(simpleType1.equals(null));
        assertFalse(simpleType1.equals(""));
        assertFalse(simpleType1.equals(new Object()));
    }
}