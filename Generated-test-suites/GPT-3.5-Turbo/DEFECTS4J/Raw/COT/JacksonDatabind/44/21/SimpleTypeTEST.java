package com.fasterxml.jackson.databind.type;

import org.junit.Test;

import static org.junit.Assert.*;

public class SimpleTypeTest {

    // Test constructor
    @Test
    public void testConstructor() {
        Class<?> cls = Object.class;
        SimpleType simpleType = new SimpleType(cls);
        assertEquals(cls, simpleType.getRawClass());
    }

    // Test constructUnsafe method
    @Test
    public void testConstructUnsafe() {
        Class<?> cls = Object.class;
        SimpleType simpleType = SimpleType.constructUnsafe(cls);
        assertEquals(cls, simpleType.getRawClass());
    }

    // Test withTypeHandler method
    @Test
    public void testWithTypeHandler() {
        Object typeHandler = new Object();
        SimpleType simpleType = new SimpleType(Object.class);
        SimpleType newSimpleType = simpleType.withTypeHandler(typeHandler);
        assertEquals(typeHandler, newSimpleType.getTypeHandler());
    }

    // Test withValueHandler method
    @Test
    public void testWithValueHandler() {
        Object valueHandler = new Object();
        SimpleType simpleType = new SimpleType(Object.class);
        SimpleType newSimpleType = simpleType.withValueHandler(valueHandler);
        assertEquals(valueHandler, newSimpleType.getValueHandler());
    }

    // Test withStaticTyping method
    @Test
    public void testWithStaticTyping() {
        SimpleType simpleType = new SimpleType(Object.class);
        SimpleType newSimpleType = simpleType.withStaticTyping();
        assertTrue(newSimpleType.isStatic());
    }

    // Test refine method
    @Test
    public void testRefine() {
        SimpleType simpleType = new SimpleType(Object.class);
        JavaType refinedType = simpleType.refine(Object.class, TypeBindings.emptyBindings(), null, null);
        assertNull(refinedType);
    }

    // Test isContainerType method
    @Test
    public void testIsContainerType() {
        SimpleType simpleType = new SimpleType(Object.class);
        assertFalse(simpleType.isContainerType());
    }

    // Test getErasedSignature method
    @Test
    public void testGetErasedSignature() {
        StringBuilder sb = new StringBuilder();
        SimpleType simpleType = new SimpleType(Object.class);
        StringBuilder signature = simpleType.getErasedSignature(sb);
        assertEquals("Ljava/lang/Object;", signature.toString());
    }

    // Test getGenericSignature method
    @Test
    public void testGetGenericSignature() {
        StringBuilder sb = new StringBuilder();
        SimpleType simpleType = new SimpleType(Object.class);
        StringBuilder signature = simpleType.getGenericSignature(sb);
        assertEquals("Ljava/lang/Object;;", signature.toString());
    }

    // Test toString method
    @Test
    public void testToString() {
        SimpleType simpleType = new SimpleType(Object.class);
        String toString = simpleType.toString();
        assertEquals("[simple type, class java.lang.Object]", toString);
    }

    // Test equals method
    @Test
    public void testEquals() {
        SimpleType simpleType1 = new SimpleType(Object.class);
        SimpleType simpleType2 = new SimpleType(Object.class);
        assertTrue(simpleType1.equals(simpleType2));
    }
}