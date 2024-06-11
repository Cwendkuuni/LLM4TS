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

    @Test
    public void testConstruct() {
        Class<?> cls = String.class;
        try {
            SimpleType simpleType = SimpleType.construct(cls);
            fail("Method should not be called as it is deprecated.");
        } catch (IllegalArgumentException e) {
            assertEquals("Can not construct SimpleType for a String (class: " + cls.getName() + ")", e.getMessage());
        }
    }

    @Test
    public void testIsContainerType() {
        SimpleType simpleType = new SimpleType(String.class);
        assertFalse(simpleType.isContainerType());
    }

    @Test
    public void testGetErasedSignature() {
        SimpleType simpleType = new SimpleType(String.class);
        StringBuilder sb = new StringBuilder();
        simpleType.getErasedSignature(sb);
        assertEquals("java.lang.String", sb.toString());
    }

    @Test
    public void testGetGenericSignature() {
        SimpleType simpleType = new SimpleType(String[].class);
        StringBuilder sb = new StringBuilder();
        simpleType.getGenericSignature(sb);
        assertEquals("[Ljava.lang.String;", sb.toString());
    }

    @Test
    public void testWithContentType() {
        SimpleType simpleType = new SimpleType(String.class);
        JavaType javaType = new SimpleType(String[].class);
        assertNull(simpleType.withContentType(javaType));
    }

    @Test
    public void testWithTypeHandler() {
        SimpleType simpleType = new SimpleType(String.class);
        Object handler = new Object();
        SimpleType newSimpleType = simpleType.withTypeHandler(handler);
        assertNotSame(simpleType, newSimpleType);
        assertEquals(handler, newSimpleType.getTypeHandler());
    }

    @Test
    public void testWithContentTypeHandler() {
        SimpleType simpleType = new SimpleType(String.class);
        Object handler = new Object();
        assertNull(simpleType.withContentTypeHandler(handler));
    }

    @Test
    public void testWithValueHandler() {
        SimpleType simpleType = new SimpleType(String.class);
        Object handler = new Object();
        SimpleType newSimpleType = simpleType.withValueHandler(handler);
        assertNotSame(simpleType, newSimpleType);
        assertEquals(handler, newSimpleType.getValueHandler());
    }

    @Test
    public void testWithContentValueHandler() {
        SimpleType simpleType = new SimpleType(String.class);
        Object handler = new Object();
        assertNull(simpleType.withContentValueHandler(handler));
    }

    @Test
    public void testWithStaticTyping() {
        SimpleType simpleType = new SimpleType(String.class);
        SimpleType newSimpleType = simpleType.withStaticTyping();
        assertTrue(newSimpleType.isStatic());
    }

    @Test
    public void testRefine() {
        SimpleType simpleType = new SimpleType(String.class);
        Class<?> refinedType = Integer.class;
        JavaType refinedJavaType = new SimpleType(refinedType);
        assertNull(simpleType.refine(refinedType, null, null, null));
    }

    @Test
    public void testBuildCanonicalName() {
        SimpleType simpleType = new SimpleType(String[].class);
        assertEquals("[Ljava.lang.String;", simpleType.buildCanonicalName());
    }
}