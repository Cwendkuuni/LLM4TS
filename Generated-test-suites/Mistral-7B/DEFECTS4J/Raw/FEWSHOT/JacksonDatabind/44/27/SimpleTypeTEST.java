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
        Class<?> cls = HashSet.class;
        SimpleType.construct(cls);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructWithCollection() {
        Class<?> cls = ArrayList.class;
        SimpleType.construct(cls);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructWithArray() {
        Class<?> cls = int[].class;
        SimpleType.construct(cls);
    }

    @Test
    public void testNarrow() {
        Class<?> superClass = Object.class;
        Class<?> subclass = String.class;
        SimpleType simpleType = new SimpleType(superClass, null, null, null);
        SimpleType narrowType = simpleType.narrow(subclass);
        assertEquals(subclass, narrowType.getRawClass());
        assertNotSame(simpleType, narrowType);
    }

    @Test
    public void testWithContentTypeHandler() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            SimpleType simpleType = new SimpleType(String.class, null, null, null, null, null, false);
            simpleType.withContentTypeHandler(null);
        });
        assertEquals("Simple types have no content types; can not call withContentTypeHandler()", exception.getMessage());
    }

    @Test
    public void testBuildCanonicalName() {
        Class<?> cls = List.class;
        SimpleType simpleType = new SimpleType(cls, null, null, null);
        assertEquals("java.util.List", simpleType.buildCanonicalName());
    }

    @Test
    public void testIsContainerType() {
        assertFalse(new SimpleType(String.class, null, null, null).isContainerType());
    }

    @Test
    public void testGetErasedSignature() {
        Class<?> cls = List.class;
        StringBuilder expected = new StringBuilder("java.util.List");
        SimpleType simpleType = new SimpleType(cls, null, null, null);
        StringBuilder result = simpleType.getErasedSignature(new StringBuilder());
        assertEquals(expected.toString(), result.toString());
    }

    @Test
    public void testGetGenericSignature() {
        Class<?> cls = List.class;
        StringBuilder expected = new StringBuilder("<T>");
        SimpleType simpleType = new SimpleType(cls, null, null, null);
        StringBuilder result = simpleType.getGenericSignature(new StringBuilder());
        assertEquals(expected.toString(), result.toString());
    }
}