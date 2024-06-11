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
    public void testIsContainerType() {
        Class<?> cls = String.class;
        SimpleType simpleType = new SimpleType(cls, TypeBindings.emptyBindings(), null, null);
        assertFalse(simpleType.isContainerType());
    }

    @Test
    public void testGetErasedSignature() {
        Class<?> cls = String.class;
        SimpleType simpleType = new SimpleType(cls, TypeBindings.emptyBindings(), null, null);
        StringBuilder expected = new StringBuilder("java.lang.String");
        assertEquals(expected.toString(), simpleType.getErasedSignature(new StringBuilder()).toString());
    }

    @Test
    public void testGetGenericSignature() {
        Class<?> cls = List.class;
        TypeParameterBound[] bounds = new TypeParameterBound[1];
        bounds[0] = new TypeParameterBound(Integer.class, null);
        JavaType javaType = TypeFactory.defaultInstance().constructType(List.class, bounds);
        SimpleType simpleType = (SimpleType) javaType;
        StringBuilder expected = new StringBuilder("java.util.List<java.lang.Integer>");
        assertEquals(expected.toString(), simpleType.getGenericSignature(new StringBuilder()).toString());
    }

    @Test
    public void testToString() {
        Class<?> cls = String.class;
        SimpleType simpleType = new SimpleType(cls, TypeBindings.emptyBindings(), null, null);
        StringBuilder expected = new StringBuilder("[simple type, class [Ljava.lang.String;]");
        assertEquals(expected.toString(), simpleType.toString());
    }

    @Test
    public void testEquals() {
        Class<?> cls1 = String.class;
        Class<?> cls2 = Integer.class;
        SimpleType simpleType1 = new SimpleType(cls1, TypeBindings.emptyBindings(), null, null);
        SimpleType simpleType2 = new SimpleType(cls2, TypeBindings.emptyBindings(), null, null);
        assertFalse(simpleType1.equals(simpleType2));

        SimpleType simpleType3 = new SimpleType(cls1, TypeBindings.emptyBindings(), null, null);
        assertTrue(simpleType1.equals(simpleType3));
    }
}