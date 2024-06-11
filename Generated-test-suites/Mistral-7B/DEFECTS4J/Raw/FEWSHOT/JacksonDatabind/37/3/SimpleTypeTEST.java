package com.fasterxml.jackson.databind.type;

import static org.junit.Assert.*;

import org.junit.Test;

public class SimpleTypeTest {

    @Test
    public void testConstructUnsafe() {
        Class<?> cls = String.class;
        SimpleType type = SimpleType.constructUnsafe(cls);
        assertEquals(cls, type.getRawClass());
        assertNull(type.getSuperClass());
        assertNull(type.getSuperInterfaces());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstruct_withMap() {
        Class<?> cls = HashMap.class;
        SimpleType.construct(cls);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstruct_withCollection() {
        Class<?> cls = ArrayList.class;
        SimpleType.construct(cls);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstruct_withArray() {
        Class<?> cls = String[].class;
        SimpleType.construct(cls);
    }

    @Test
    public void testNarrow() {
        Class<?> superClass = Object.class;
        Class<?> subClass = String.class;
        SimpleType superType = new SimpleType(superClass);
        SimpleType subType = new SimpleType(subClass);
        SimpleType result = subType._narrow(superClass);
        assertEquals(subType, result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNarrow_withWrongSubclass() {
        Class<?> superClass = Object.class;
        Class<?> subClass = Integer.class;
        SimpleType superType = new SimpleType(superClass);
        SimpleType subType = new SimpleType(subClass);
        subType._narrow(String.class);
    }

    @Test
    public void testGetErasedSignature() {
        Class<?> cls = String.class;
        SimpleType type = new SimpleType(cls);
        StringBuilder expected = new StringBuilder("java.lang.String");
        StringBuilder result = type.getErasedSignature(new StringBuilder());
        assertEquals(expected.toString(), result.toString());
    }

    @Test
    public void testGetGenericSignature() {
        Class<?> cls = List.class;
        TypeFactory typeFactory = new TypeFactory();
        JavaType listType = typeFactory.constructCollectionType(Map.class, Integer.class);
        SimpleType type = new SimpleType(cls);
        StringBuilder expected = new StringBuilder("java.util.List<T>");
        StringBuilder result = type.getGenericSignature(new StringBuilder());
        assertEquals(expected.toString(), result.toString());
    }

    @Test
    public void testBuildCanonicalName() {
        Class<?> cls = List.class;
        TypeFactory typeFactory = new TypeFactory();
        JavaType listType = typeFactory.constructCollectionType(Map.class, Integer.class);
        SimpleType type = new SimpleType(cls);
        StringBuilder expected = new StringBuilder("java.util.List<java.lang.Integer>");
        StringBuilder result = type.buildCanonicalName();
        assertEquals(expected.toString(), result.toString());
    }

    @Test
    public void testIsContainerType() {
        Class<?> cls = String.class;
        SimpleType type = new SimpleType(cls);
        assertFalse(type.isContainerType());
    }

    @Test
    public void testToString() {
        Class<?> cls = String.class;
        SimpleType type = new SimpleType(cls);
        StringBuilder expected = new StringBuilder("[simple type, class [Ljava.lang.String;]");
        StringBuilder result = new StringBuilder();
        result.append(type.toString());
        assertEquals(expected.toString(), result.toString());
    }

    @Test
    public void testEquals() {
        Class<?> cls1 = String.class;
        Class<?> cls2 = Integer.class;
        SimpleType type1 = new SimpleType(cls1);
        SimpleType type2 = new SimpleType(cls2);
        assertFalse(type1.equals(type2));

        SimpleType type3 = new SimpleType(cls1);
        assertTrue(type1.equals(type3));
    }
}