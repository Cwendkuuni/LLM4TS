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
        assertFalse(simpleType.isStatic());
    }

    @Test
    public void testConstruct() {
        Class<?> cls = String.class;
        SimpleType simpleType = SimpleType.construct(cls);
        assertEquals(cls, simpleType.getRawClass());
        assertNotNull(simpleType.getSuperClass());
        assertNull(simpleType.getSuperInterfaces());
        assertFalse(simpleType.isStatic());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructInvalidClass() {
        Class<?> cls = Map.class;
        SimpleType.construct(cls);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructArrayClass() {
        Class<?> cls = int[].class;
        SimpleType.construct(cls);
    }

    @Test
    public void testIsContainerType() {
        Class<?> cls = String.class;
        SimpleType simpleType = new SimpleType(cls, TypeBindings.emptyBindings(), null, null, null, null, false);
        assertFalse(simpleType.isContainerType());
    }

    @Test
    public void testGetErasedSignature() {
        Class<?> cls = String.class;
        SimpleType simpleType = new SimpleType(cls, TypeBindings.emptyBindings(), null, null, null, null, false);
        StringBuilder sb = new StringBuilder();
        simpleType.getErasedSignature(sb);
        assertEquals("java.lang.String", sb.toString());
    }

    @Test
    public void testGetGenericSignature() {
        Class<?> cls = List.class;
        TypeFactory typeFactory = new TypeFactory();
        JavaType javaTypeList = typeFactory.constructParametrizedType(List.class, String.class);
        SimpleType simpleType = new SimpleType(cls, TypeBindings.emptyBindings(), null, null, null, null, false);
        StringBuilder sb = new StringBuilder();
        simpleType.getGenericSignature(sb);
        assertEquals("[Ljava.util.List;<Ljava.lang.String;>", sb.toString());
    }

    @Test
    public void testBuildCanonicalName() {
        Class<?> cls = List.class;
        TypeFactory typeFactory = new TypeFactory();
        JavaType javaTypeList = typeFactory.constructParametrizedType(List.class, String.class);
        SimpleType simpleType = new SimpleType(cls, TypeBindings.emptyBindings(), null, null, null, null, false);
        assertEquals("java.util.List<java.lang.String>", simpleType.buildCanonicalName());
    }

    @Test
    public void testNarrow() {
        Class<?> cls = String.class;
        SimpleType simpleType = new SimpleType(cls, TypeBindings.emptyBindings(), null, null, null, null, false);
        SimpleType narrowType = simpleType.narrow(StringBuilder.class);
        assertSame(simpleType, narrowType);

        Class<?> subclass = StringBuilder.class;
        SimpleType subclassType = new SimpleType(subclass, TypeBindings.emptyBindings(), null, null, null, null, false);
        narrowType = simpleType.narrow(subclassType.getRawClass());
        assertSame(subclassType, narrowType);
    }
}