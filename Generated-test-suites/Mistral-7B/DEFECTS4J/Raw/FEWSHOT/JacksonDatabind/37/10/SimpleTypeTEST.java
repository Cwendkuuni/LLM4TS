package com.fasterxml.jackson.databind.type;

import static org.junit.Assert.*;

import java.util.Arrays;

import org.junit.Test;

public class SimpleTypeTest {

    @Test
    public void testConstructUnsafe() {
        Class<?> cls = String.class;
        SimpleType type = SimpleType.constructUnsafe(cls);
        assertEquals(cls, type.getRawClass());
        assertNull(type.getSuperClass());
        assertNull(type.getSuperTypes());
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

    @Test(expected = IllegalArgumentException.class)
    public void testConstructWithArray() {
        Class<?> cls = String[].class;
        SimpleType.construct(cls);
    }

    @Test
    public void testNarrow() {
        Class<?> cls = String.class;
        SimpleType type1 = new SimpleType(cls);
        SimpleType type2 = new SimpleType(Integer.class);
        SimpleType narrowed = type1.narrow(Integer.class);
        assertEquals(type2, narrowed);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNarrowWithInvalidSubclass() {
        Class<?> cls = String.class;
        SimpleType type = new SimpleType(cls);
        type.narrow(ArrayList.class);
    }

    @Test
    public void testToString() {
        Class<?> cls = String.class;
        SimpleType type = new SimpleType(cls);
        assertEquals("[simple type, class " + cls.getName() + "]", type.toString());
    }

    @Test
    public void testEquals() {
        Class<?> cls1 = String.class;
        Class<?> cls2 = Integer.class;
        SimpleType type1 = new SimpleType(cls1);
        SimpleType type2 = new SimpleType(cls2);
        SimpleType type3 = new SimpleType(cls1);
        assertTrue(type1.equals(type3));
        assertFalse(type1.equals(type2));
    }

    @Test
    public void testWithTypeHandler() {
        Class<?> cls = String.class;
        Object handler = new Object() {};
        SimpleType type = new SimpleType(cls);
        SimpleType withHandler = type.withTypeHandler(handler);
        assertEquals(handler, withHandler.getTypeHandler());
    }

    @Test
    public void testWithStaticTyping() {
        Class<?> cls = String.class;
        SimpleType type = new SimpleType(cls, null, null, null, null, null, false);
        SimpleType withStaticTyping = type.withStaticTyping();
        assertTrue(withStaticTyping.isStatic());
    }

    @Test
    public void testRefine() {
        Class<?> raw = String.class;
        JavaType superClass = Object.class;
        JavaType[] superInterfaces = new JavaType[0];
        SimpleType type = new SimpleType(raw, null, superClass, superInterfaces, null, null, false);
        JavaType refined = type.refine(String[].class, null, null, null);
        assertNull(refined);
    }

    @Test
    public void testBuildCanonicalName() {
        Class<?> cls = List.class;
        SimpleType type = new SimpleType(cls, null, null, null, null, null, false);
        assertEquals("java.util.List", type.getCanonicalName());
    }

    @Test
    public void testGetErasedSignature() {
        Class<?> cls = List.class;
        SimpleType type = new SimpleType(cls, null, null, null, null, null, false);
        StringBuilder expected = new StringBuilder("java.util.List");
        StringBuilder actual = type.getErasedSignature(new StringBuilder());
        assertEquals(expected.toString(), actual.toString());
    }

    @Test
    public void testGetGenericSignature() {
        Class<?> cls = List.class;
        SimpleType type = new SimpleType(cls, null, null, null, null, null, false);
        StringBuilder expected = new StringBuilder("java.util.List<T>");
        StringBuilder actual = new StringBuilder();
        type.getGenericSignature(actual);
        assertEquals(expected.toString(), actual.toString());
    }
}