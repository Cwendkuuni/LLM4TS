package com.fasterxml.jackson.databind.type;

import static org.junit.Assert.*;

import org.junit.Test;

public class SimpleTypeTest {

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
    public void testConstructUnsafe() {
        Class<?> raw = String[].class;
        SimpleType simpleType = SimpleType.constructUnsafe(raw);
        assertEquals(raw, simpleType.getRawClass());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructInvalidClass() {
        SimpleType.construct(Map.class);
    }

    @Test
    public void testNarrow() {
        Class<?> subclass = Integer.class;
        SimpleType simpleType = new SimpleType(String.class);
        SimpleType narrowedType = simpleType.narrow(subclass);
        assertNotNull(narrowedType);
        assertNotSame(simpleType, narrowedType);
        assertEquals(subclass, narrowedType.getRawClass());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNarrowInvalidClass() {
        Class<?> subclass = Map.class;
        SimpleType simpleType = new SimpleType(String.class);
        simpleType.narrow(subclass);
    }

    @Test
    public void testWithTypeHandler() {
        SimpleType simpleType = new SimpleType(String.class);
        Object typeHandler = new Object();
        SimpleType withTypeHandler = simpleType.withTypeHandler(typeHandler);
        assertNotSame(simpleType, withTypeHandler);
        assertEquals(typeHandler, withTypeHandler.getTypeHandler());
    }

    @Test
    public void testWithContentTypeHandler() {
        SimpleType simpleType = new SimpleType(String.class);
        Object contentTypeHandler = new Object();
        try {
            simpleType.withContentTypeHandler(contentTypeHandler);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testWithValueHandler() {
        SimpleType simpleType = new SimpleType(String.class);
        Object valueHandler = new Object();
        SimpleType withValueHandler = simpleType.withValueHandler(valueHandler);
        assertNotSame(simpleType, withValueHandler);
        assertEquals(valueHandler, withValueHandler.getValueHandler());
    }

    @Test
    public void testWithContentValueHandler() {
        SimpleType simpleType = new SimpleType(String.class);
        Object contentValueHandler = new Object();
        try {
            simpleType.withContentValueHandler(contentValueHandler);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testWithStaticTyping() {
        SimpleType simpleType = new SimpleType(String.class);
        SimpleType withStaticTyping = simpleType.withStaticTyping();
        assertNotSame(simpleType, withStaticTyping);
        assertTrue(withStaticTyping.isStatic());
    }

    @Test
    public void testRefine() {
        Class<?> raw = String[].class;
        SimpleType simpleType = new SimpleType(raw);
        SimpleType refinedType = simpleType.refine(String.class, null, null, null);
        assertNull(refinedType);
    }

    @Test
    public void testBuildCanonicalName() {
        SimpleType simpleType = new SimpleType(String[].class);
        String canonicalName = simpleType.buildCanonicalName();
        assertEquals("[Ljava.lang.String;", canonicalName);
    }
}