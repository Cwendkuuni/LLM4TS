package com.fasterxml.jackson.databind.type;

import static org.junit.Assert.*;

import org.junit.Test;

public class SimpleTypeTest {

    @Test
    public void testConstructUnsafe() {
        Class<?> rawType = String.class;
        SimpleType simpleType = SimpleType.constructUnsafe(rawType);
        assertEquals(String.class, simpleType.getRawClass());
        assertNull(simpleType.getSuperClass());
        assertNull(simpleType.getSuperTypes());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructUnsafeWithArray() {
        Class<?> rawType = int[].class;
        SimpleType simpleType = SimpleType.constructUnsafe(rawType);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructUnsafeWithMap() {
        Class<?> rawType = HashMap.class;
        SimpleType simpleType = SimpleType.constructUnsafe(rawType);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructUnsafeWithCollection() {
        Class<?> rawType = ArrayList.class;
        SimpleType simpleType = SimpleType.constructUnsafe(rawType);
    }

    @Test
    public void testConstruct() {
        Class<?> rawType = String.class;
        SimpleType simpleType = SimpleType.construct(rawType);
        assertEquals(String.class, simpleType.getRawClass());
        assertNotNull(simpleType.getSuperClass());
        assertArrayEquals(new JavaType[0], simpleType.getSuperTypes());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructWithArray() {
        Class<?> rawType = int[].class;
        SimpleType simpleType = SimpleType.construct(rawType);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructWithMap() {
        Class<?> rawType = HashMap.class;
        SimpleType simpleType = SimpleType.construct(rawType);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructWithCollection() {
        Class<?> rawType = ArrayList.class;
        SimpleType simpleType = SimpleType.construct(rawType);
    }

    @Test
    public void testNarrow() {
        Class<?> rawType = String.class;
        SimpleType simpleType = new SimpleType(rawType);
        Class<?> subclass = StringBuffer.class;
        SimpleType narrow = simpleType._narrow(subclass);
        assertEquals(subclass, narrow.getRawClass());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNarrowWithInvalidSubclass() {
        Class<?> rawType = String.class;
        SimpleType simpleType = new SimpleType(rawType);
        Class<?> subclass = ArrayList.class;
        simpleType._narrow(subclass);
    }

    @Test
    public void testIsContainerType() {
        Class<?> rawType = String.class;
        SimpleType simpleType = new SimpleType(rawType);
        assertFalse(simpleType.isContainerType());
    }

    @Test
    public void testGetErasedSignature() {
        Class<?> rawType = String.class;
        SimpleType simpleType = new SimpleType(rawType);
        StringBuilder expected = new StringBuilder("java.lang.String");
        StringBuilder actual = simpleType.getErasedSignature(new StringBuilder());
        assertEquals(expected.toString(), actual.toString());
    }

    @Test
    public void testGetGenericSignature() {
        Class<?> rawType = List.class;
        Class<?> elementType = Integer.class;
        SimpleType simpleType = new SimpleType(rawType, null, null, new JavaType[]{new SimpleType(elementType)});
        StringBuilder expected = new StringBuilder("java.util.List<java.lang.Integer>");
        StringBuilder actual = simpleType.getGenericSignature(new StringBuilder());
        assertEquals(expected.toString(), actual.toString());
    }

    @Test
    public void testEquals() {
        Class<?> rawType1 = String.class;
        SimpleType simpleType1 = new SimpleType(rawType1);
        Class<?> rawType2 = String.class;
        SimpleType simpleType2 = new SimpleType(rawType2);
        assertTrue(simpleType1.equals(simpleType2));

        Class<?> rawType3 = Object.class;
        SimpleType simpleType3 = new SimpleType(rawType3);
        assertFalse(simpleType1.equals(simpleType3));

        SimpleType simpleType4 = new SimpleType(simpleType1);
        assertTrue(simpleType1.equals(simpleType4));
    }

    @Test
    public void testToString() {
        Class<?> rawType = String.class;
        SimpleType simpleType = new SimpleType(rawType);
        String expected = "[simple type, class java.lang.String]";
        String actual = simpleType.toString();
        assertEquals(expected, actual);
    }
}