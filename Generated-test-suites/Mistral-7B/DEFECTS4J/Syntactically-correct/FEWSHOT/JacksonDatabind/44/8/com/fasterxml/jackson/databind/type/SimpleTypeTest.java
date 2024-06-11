package com.fasterxml.jackson.databind.type;

import static org.junit.Assert.*;

import org.junit.Test;

public class SimpleTypeTest {

    @Test
    public void testConstructUnsafe() {
        Class<?> cls = String.class;
        SimpleType simpleType = SimpleType.constructUnsafe(cls);
        assertNotNull(simpleType);
        assertEquals(cls, simpleType.getRawClass());
        assertNull(simpleType.getSuperClass());
        assertNull(simpleType.getSuperInterfaces());
        assertFalse(simpleType.isStatic());
    }

    @Test
    public void testConstruct() {
        Class<?> cls = String.class;
        SimpleType simpleType = SimpleType.construct(cls);
        assertNotNull(simpleType);
        assertEquals(cls, simpleType.getRawClass());
        assertNull(simpleType.getSuperClass());
        assertNull(simpleType.getSuperInterfaces());
        assertFalse(simpleType.isStatic());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructMap() {
        Class<?> cls = HashMap.class;
        SimpleType simpleType = SimpleType.construct(cls);
        fail("Should not reach this point");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructCollection() {
        Class<?> cls = ArrayList.class;
        SimpleType simpleType = SimpleType.construct(cls);
        fail("Should not reach this point");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructArray() {
        Class<?> cls = int[].class;
        SimpleType simpleType = SimpleType.construct(cls);
        fail("Should not reach this point");
    }

    @Test
    public void testNarrow() {
        Class<?> cls = String.class;
        Class<?> subClass = Object.class;
        SimpleType simpleType = new SimpleType(cls, null, null, null, null, null, false);
        SimpleType narrowType = simpleType.narrow(subClass);
        assertNotNull(narrowType);
        assertFalse(narrowType.equals(simpleType));
        assertEquals(subClass, narrowType.getRawClass());
    }

    @Test
    public void testNarrowSameClass() {
        Class<?> cls = String.class;
        SimpleType simpleType = new SimpleType(cls, null, null, null, null, null, false);
        SimpleType narrowType = simpleType.narrow(cls);
        assertTrue(simpleType.equals(narrowType));
    }

    @Test
    public void testIsContainerType() {
        Class<?> cls = String.class;
        SimpleType simpleType = new SimpleType(cls, null, null, null, null, null, false);
        assertFalse(simpleType.isContainerType());
    }

    @Test
    public void testGetErasedSignature() {
        Class<?> cls = String.class;
        SimpleType simpleType = new SimpleType(cls, null, null, null, null, null, false);
        StringBuilder sb = new StringBuilder();
        simpleType.getErasedSignature(sb);
        assertEquals("java.lang.String", sb.toString());
    }

    @Test
    public void testGetGenericSignature() {
        Class<?> cls = String.class;
        SimpleType simpleType = new SimpleType(cls, null, null, null, null, null, false);
        StringBuilder sb = new StringBuilder();
        simpleType.getGenericSignature(sb);
        assertEquals("java.lang.String", sb.toString());
    }

    @Test
    public void testGetCanonicalName() {
        Class<?> cls = String.class;
        SimpleType simpleType = new SimpleType(cls, null, null, null, null, null, false);
        assertEquals("java.lang.String", simpleType.getCanonicalName());
    }

    @Test
    public void testBuildCanonicalName() {
        Class<?> cls = String[].class;
        SimpleType simpleType = new SimpleType(cls, null, null, null, null, null, false);
        assertEquals("java.lang.String[]", simpleType.buildCanonicalName());
    }

    @Test
    public void testWithContentType() {
        Class<?> cls = String.class;
        SimpleType simpleType = new SimpleType(cls, null, null, null, null, null, false);
        JavaType contentType = new SimpleType(int.class, null, null, null, null, null, false);
        assertNull(simpleType.withContentType(contentType));
    }

    @Test
    public void testWithTypeHandler() {
        Class<?> cls = String.class;
        Object handler = new Object();
        SimpleType simpleType = new SimpleType(cls, null, null, null, null, null, false);
        SimpleType withTypeHandler = simpleType.withTypeHandler(handler);
        assertNotSame(simpleType, withTypeHandler);
        assertEquals(handler, withTypeHandler.getTypeHandler());
    }

    @Test
    public void testWithStaticTyping() {
        Class<?> cls = String.class;
        SimpleType simpleType = new SimpleType(cls, null, null, null, null, null, false);
        SimpleType withStaticTyping = simpleType.withStaticTyping();
        assertNotSame(simpleType, withStaticTyping);
        assertTrue(withStaticTyping.isStatic());
    }

    @Test
    public void testRefine() {
        Class<?> cls = String.class;
        Class<?> rawType = Object.class;
        TypeBindings bindings = TypeBindings.emptyBindings();
        JavaType[] superInterfaces = new JavaType[0];
        SimpleType simpleType = new SimpleType(cls, bindings, null, superInterfaces, null, null, false);
        JavaType refinedType = simpleType.refine(rawType, bindings, null, superInterfaces);
        assertNull(refinedType);
    }
}