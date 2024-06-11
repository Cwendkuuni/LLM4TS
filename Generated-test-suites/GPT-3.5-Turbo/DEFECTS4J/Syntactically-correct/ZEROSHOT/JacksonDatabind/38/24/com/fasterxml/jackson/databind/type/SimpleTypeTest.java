package com.fasterxml.jackson.databind.type;

import org.junit.Test;
import static org.junit.Assert.*;

public class SimpleTypeTest {

    @Test
    public void testConstructUnsafe() {
        SimpleType simpleType = SimpleType.constructUnsafe(Object.class);
        assertNotNull(simpleType);
        assertEquals(Object.class, simpleType.getRawClass());
        assertFalse(simpleType.isContainerType());
    }

    @Test
    public void testConstruct() {
        try {
            SimpleType.construct(Map.class);
            fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException e) {
            assertEquals("Can not construct SimpleType for a Map (class: java.util.Map)", e.getMessage());
        }

        try {
            SimpleType.construct(Collection.class);
            fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException e) {
            assertEquals("Can not construct SimpleType for a Collection (class: java.util.Collection)", e.getMessage());
        }

        try {
            SimpleType.construct(Object[].class);
            fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException e) {
            assertEquals("Can not construct SimpleType for an array (class: [Ljava.lang.Object;)", e.getMessage());
        }

        SimpleType simpleType = SimpleType.construct(Object.class);
        assertNotNull(simpleType);
        assertEquals(Object.class, simpleType.getRawClass());
        assertFalse(simpleType.isContainerType());
    }

    @Test
    public void testNarrow() {
        SimpleType simpleType = new SimpleType(Object.class);
        assertEquals(simpleType, simpleType._narrow(Object.class));
        assertNotEquals(simpleType, simpleType._narrow(Number.class));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testWithContentType() {
        SimpleType simpleType = new SimpleType(Object.class);
        simpleType.withContentType(SimpleType.constructUnsafe(String.class));
    }

    @Test
    public void testWithTypeHandler() {
        Object handler = new Object();
        SimpleType simpleType = new SimpleType(Object.class);
        SimpleType newType = simpleType.withTypeHandler(handler);
        assertNotNull(newType);
        assertEquals(Object.class, newType.getRawClass());
        assertEquals(handler, newType.getTypeHandler());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testWithContentTypeHandler() {
        SimpleType simpleType = new SimpleType(Object.class);
        simpleType.withContentTypeHandler(new Object());
    }

    @Test
    public void testWithValueHandler() {
        Object handler = new Object();
        SimpleType simpleType = new SimpleType(Object.class);
        SimpleType newType = simpleType.withValueHandler(handler);
        assertNotNull(newType);
        assertEquals(Object.class, newType.getRawClass());
        assertEquals(handler, newType.getValueHandler());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testWithContentValueHandler() {
        SimpleType simpleType = new SimpleType(Object.class);
        simpleType.withContentValueHandler(new Object());
    }

    @Test
    public void testWithStaticTyping() {
        SimpleType simpleType = new SimpleType(Object.class);
        assertTrue(simpleType.withStaticTyping().isStatic());
    }

    @Test
    public void testRefine() {
        SimpleType simpleType = new SimpleType(Object.class);
        assertNull(simpleType.refine(Object.class, null, null, null));
    }

    @Test
    public void testGetErasedSignature() {
        StringBuilder sb = new StringBuilder();
        SimpleType simpleType = new SimpleType(Object.class);
        simpleType.getErasedSignature(sb);
        assertEquals("Ljava/lang/Object;", sb.toString());
    }

    @Test
    public void testGetGenericSignature() {
        StringBuilder sb = new StringBuilder();
        SimpleType simpleType = new SimpleType(Object.class);
        simpleType.getGenericSignature(sb);
        assertEquals("Ljava/lang/Object;", sb.toString());
    }

    @Test
    public void testBuildCanonicalName() {
        SimpleType simpleType = new SimpleType(Object.class);
        assertEquals("java.lang.Object", simpleType.buildCanonicalName());
    }

    @Test
    public void testToString() {
        SimpleType simpleType = new SimpleType(Object.class);
        assertEquals("[simple type, class java.lang.Object]", simpleType.toString());
    }

    @Test
    public void testEquals() {
        SimpleType simpleType1 = new SimpleType(Object.class);
        SimpleType simpleType2 = new SimpleType(Object.class);
        assertEquals(simpleType1, simpleType2);
    }
}