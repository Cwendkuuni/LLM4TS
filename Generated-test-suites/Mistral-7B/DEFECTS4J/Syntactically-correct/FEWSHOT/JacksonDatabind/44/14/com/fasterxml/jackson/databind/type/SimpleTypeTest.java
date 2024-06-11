package com.fasterxml.jackson.databind.type;

import static org.junit.Assert.*;

import org.junit.Test;

public class SimpleTypeTest {

    @Test
    public void testConstructUnsafe() {
        Class<?> cls = String.class;
        SimpleType simpleType = SimpleType.constructUnsafe(cls);
        assertEquals(cls, simpleType.getClass());
        assertNull(simpleType.getSuperClass());
        assertNull(simpleType.getSuperInterfaces());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructWithInvalidClass() {
        Class<?> cls = new ArrayList<>().getClass(); // this is a Collection
        SimpleType.construct(cls);
    }

    @Test
    public void testWithContentTypeHandler() {
        // Simple types have no content types
        SimpleType simpleType = new SimpleType(String.class, TypeBindings.emptyBindings(), null, null);
        assertNull(simpleType.withContentTypeHandler(null));
    }

    @Test
    public void testWithTypeHandler() {
        SimpleType simpleType = new SimpleType(String.class, TypeBindings.emptyBindings(), null, null);
        JavaType javaType = new SimpleType(Integer.TYPE, TypeBindings.emptyBindings(), null, null);
        SimpleType newSimpleType = simpleType.withTypeHandler(javaType);
        assertEquals(simpleType.getClass(), newSimpleType.getClass());
        assertNotSame(simpleType.getTypeHandler(), newSimpleType.getTypeHandler());
    }

    @Test
    public void testIsContainerType() {
        SimpleType simpleType = new SimpleType(String.class, TypeBindings.emptyBindings(), null, null);
        assertFalse(simpleType.isContainerType());
    }

    @Test
    public void testBuildCanonicalName() {
        SimpleType simpleType = new SimpleType(String[].class, TypeBindings.emptyBindings(), null, null);
        assertEquals("[class [Ljava.lang.String;]", simpleType.buildCanonicalName());

        SimpleType simpleType2 = new SimpleType(List.class, TypeBindings.emptyBindings(), null, null);
        assertEquals("[class java.util.List]", simpleType2.buildCanonicalName());
    }

    @Test
    public void testGetErasedSignature() {
        SimpleType simpleType = new SimpleType(String.class, TypeBindings.emptyBindings(), null, null);
        StringBuilder sb = new StringBuilder(10);
        simpleType.getErasedSignature(sb);
        assertEquals("[class java.lang.String]", sb.toString());
    }

    @Test
    public void testGetGenericSignature() {
        SimpleType simpleType = new SimpleType(String.class, TypeBindings.emptyBindings(), null, null);
        StringBuilder sb = new StringBuilder(20);
        simpleType.getGenericSignature(sb);
        assertEquals("[class java.lang.String]", sb.toString());

        SimpleType listType = new SimpleType(List.class, TypeBindings.emptyBindings(), null, null);
        StringBuilder sb2 = new StringBuilder(30);
        listType.getGenericSignature(sb2);
        assertEquals("[class java.util.List<T>]", sb2.toString());
    }
}