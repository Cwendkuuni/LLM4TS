package com.fasterxml.jackson.databind.type;

import static org.junit.Assert.*;

import java.util.Arrays;

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

    @Test
    public void testConstruct() {
        Class<?> cls = String.class;
        SimpleType simpleType = SimpleType.construct(cls);
        assertEquals(cls, simpleType.getRawClass());
        assertNull(simpleType.getSuperClass());
        assertNull(simpleType.getSuperInterfaces());
        assertNull(simpleType.getValueHandler());
        assertNull(simpleType.getTypeHandler());
        assertFalse(simpleType.isStatic());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructWithMap() {
        Class<?> cls = HashMap.class;
        SimpleType simpleType = SimpleType.construct(cls);
        fail("IllegalArgumentException expected but not thrown");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructWithCollection() {
        Class<?> cls = ArrayList.class;
        SimpleType simpleType = SimpleType.construct(cls);
        fail("IllegalArgumentException expected but not thrown");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructWithArray() {
        Class<?> cls = String[].class;
        SimpleType simpleType = SimpleType.construct(cls);
        fail("IllegalArgumentException expected but not thrown");
    }

    @Test
    public void testNarrow() {
        Class<?> cls = String.class;
        SimpleType simpleType = new SimpleType(cls);
        Class<?> subclass = Integer.class;
        SimpleType narrowedType = simpleType._narrow(subclass);
        assertEquals(subclass, narrowedType.getRawClass());
        assertNull(narrowedType.getSuperClass());
        assertNull(narrowedType.getSuperInterfaces());
        assertNull(narrowedType.getValueHandler());
        assertNull(narrowedType.getTypeHandler());
        assertFalse(narrowedType.isStatic());
    }

    @Test
    public void testIsContainerType() {
        Class<?> cls = String.class;
        SimpleType simpleType = new SimpleType(cls);
        assertFalse(simpleType.isContainerType());
    }

    @Test
    public void testBuildCanonicalName() {
        Class<?> cls = List.class;
        TypeVariable<?>[] typeParameters = cls.getTypeParameters();
        SimpleType simpleType = new SimpleType(cls, Arrays.asList(TypeFactory.defaultInstance().constructType(Integer.class)));
        String canonicalName = simpleType.buildCanonicalName();
        String expectedCanonicalName = "java.util.List<java.lang.Integer>";
        assertEquals(expectedCanonicalName, canonicalName);
    }

    @Test
    public void testGetErasedSignature() {
        Class<?> cls = List.class;
        TypeVariable<?>[] typeParameters = cls.getTypeParameters();
        SimpleType simpleType = new SimpleType(cls, Arrays.asList(TypeFactory.defaultInstance().constructType(Integer.class)));
        StringBuilder expectedSignature = new StringBuilder("java.util.List");
        StringBuilder actualSignature = simpleType.getErasedSignature(new StringBuilder());
        assertEquals(expectedSignature.toString(), actualSignature.toString());
    }

    @Test
    public void testGetGenericSignature() {
        Class<?> cls = List.class;
        TypeVariable<?>[] typeParameters = cls.getTypeParameters();
        SimpleType simpleType = new SimpleType(cls, Arrays.asList(TypeFactory.defaultInstance().constructType(Integer.class)));
        StringBuilder expectedSignature = new StringBuilder("java.util.List<T>");
        StringBuilder actualSignature = new StringBuilder();
        simpleType.getGenericSignature(actualSignature);
        assertEquals(expectedSignature.toString(), actualSignature.toString());
    }

    @Test
    public void testEquals() {
        Class<?> cls1 = String.class;
        Class<?> cls2 = Integer.class;

        SimpleType simpleType1 = new SimpleType(cls1);
        SimpleType simpleType2 = new SimpleType(cls2);
        assertFalse(simpleType1.equals(simpleType2));

        SimpleType simpleType3 = new SimpleType(cls1);
        assertTrue(simpleType1.equals(simpleType3));
    }

    @Test
    public void testToString() {
        Class<?> cls = String.class;
        SimpleType simpleType = new SimpleType(cls);
        StringBuilder expectedToString = new StringBuilder("[simple type, class [Ljava.lang.String;]]");
        StringBuilder actualToString = new StringBuilder();
        simpleType.toString(actualToString);
        assertEquals(expectedToString.toString(), actualToString.toString());
    }
}