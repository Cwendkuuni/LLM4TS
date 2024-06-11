package com.fasterxml.jackson.databind.type;

import static org.junit.Assert.*;

import org.junit.Test;

public class SimpleTypeTest {

    @Test
    public void testConstructUnsafe() {
        Class<?> raw = String.class;
        SimpleType simpleType = SimpleType.constructUnsafe(raw);
        assertEquals(raw, simpleType.getType());
        assertNull(simpleType.getSuperClass());
        assertNull(simpleType.getSuperInterfaces());
        assertFalse(simpleType.isStatic());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstruct_Map() {
        Class<?> cls = HashSet.class;
        SimpleType.construct(cls);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstruct_Collection() {
        Class<?> cls = ArrayList.class;
        SimpleType.construct(cls);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstruct_Array() {
        Class<?> cls = int[].class;
        SimpleType.construct(cls);
    }

    @Test
    public void testNarrow() {
        Class<?> raw = String.class;
        SimpleType simpleType = SimpleType.constructUnsafe(raw);
        Class<?> subclass = Object.class;
        SimpleType narrowType = simpleType._narrow(subclass);
        assertNotSame(simpleType, narrowType);
        assertEquals(subclass, narrowType.getType());
        assertNull(narrowType.getSuperClass());
        assertNull(narrowType.getSuperInterfaces());
        assertFalse(narrowType.isStatic());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNarrow_NotSubclass() {
        Class<?> raw = String.class;
        SimpleType simpleType = SimpleType.constructUnsafe(raw);
        Class<?> subclass = Integer.class;
        simpleType._narrow(subclass);
    }

    @Test
    public void testWithTypeHandler() {
        Class<?> raw = String.class;
        SimpleType simpleType = SimpleType.constructUnsafe(raw);
        Object handler = new Object() {};
        SimpleType withTypeHandler = simpleType.withTypeHandler(handler);
        assertNotSame(simpleType, withTypeHandler);
        assertEquals(raw, withTypeHandler.getType());
        assertEquals(handler, withTypeHandler.getTypeHandler());
        assertNull(withTypeHandler.getSuperClass());
        assertNull(withTypeHandler.getSuperInterfaces());
        assertFalse(withTypeHandler.isStatic());
    }

    @Test
    public void testBuildCanonicalName() {
        Class<?> raw = List.class;
        TypeParameter<?> typeParameter = new TypeParameter<>();
        List<String> list = new ArrayList<>();
        list.add("foo");
        SimpleType simpleType = new SimpleType(raw, null, null, null, null, null, false);
        simpleType._bindings = new TypeBindings(new SimpleTypeBinding(typeParameter, simpleType));
        String result = simpleType.buildCanonicalName();
        String expected = "[simple type, class java.util.List<java.lang.String>]";
        assertEquals(expected, result);
    }

    @Test
    public void testIsContainerType() {
        Class<?> raw = List.class;
        SimpleType simpleType = SimpleType.constructUnsafe(raw);
        assertFalse(simpleType.isContainerType());
    }

    @Test
    public void testGetErasedSignature() {
        Class<?> raw = List.class;
        SimpleType simpleType = SimpleType.constructUnsafe(raw);
        StringBuilder expected = new StringBuilder("java.util.List");
        StringBuilder result = simpleType.getErasedSignature(new StringBuilder());
        assertEquals(expected.toString(), result.toString());
    }

    @Test
    public void testGetGenericSignature() {
        Class<?> raw = List.class;
        SimpleType simpleType = SimpleType.constructUnsafe(raw);
        StringBuilder expected = new StringBuilder("java.util.List<T>");
        StringBuilder result = simpleType.getGenericSignature(new StringBuilder());
        assertEquals(expected.toString(), result.toString());
    }
}