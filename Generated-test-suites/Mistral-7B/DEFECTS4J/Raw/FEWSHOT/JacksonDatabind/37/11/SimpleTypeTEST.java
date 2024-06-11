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
        SimpleType simpleType = new SimpleType(List.class);
        StringBuilder sb = new StringBuilder();
        simpleType.getGenericSignature(sb);
        assertEquals("java.util.List<E>", sb.toString());
    }

    @Test
    public void testToString() {
        SimpleType simpleType = new SimpleType(String.class);
        assertEquals("[simple type, class [Ljava.lang.String;]", simpleType.toString());
    }

    @Test
    public void testEquals() {
        SimpleType simpleType1 = new SimpleType(String.class);
        SimpleType simpleType2 = new SimpleType(String.class);
        assertTrue(simpleType1.equals(simpleType2));

        SimpleType simpleType3 = new SimpleType(Integer.class);
        assertFalse(simpleType1.equals(simpleType3));
    }

    @Test
    public void testConstructUnsafe() {
        try {
            SimpleType simpleType = SimpleType.constructUnsafe(String.class);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test(expected = Deprecated.class)
    public void testConstructDeprecated() {
        SimpleType simpleType = SimpleType.construct(String.class);
    }

    @Test
    public void testNarrow() {
        Class<?> subclass = Integer.class;
        SimpleType simpleType = new SimpleType(String.class);
        assertEquals(simpleType, simpleType._narrow(String.class));
        assertEquals(null, simpleType._narrow(subclass));
    }

    @Test
    public void testWithContentType() {
        SimpleType simpleType = new SimpleType(String.class);
        try {
            simpleType.withContentType(new SimpleType(String.class));
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testWithTypeHandler() {
        SimpleType simpleType = new SimpleType(String.class);
        Object handler = new Object();
        SimpleType simpleTypeWithHandler = simpleType.withTypeHandler(handler);
        assertNotEquals(simpleType, simpleTypeWithHandler);
        assertEquals(handler, simpleTypeWithHandler._typeHandler);
    }

    @Test
    public void testWithValueHandler() {
        SimpleType simpleType = new SimpleType(String.class);
        Object handler = new Object();
        SimpleType simpleTypeWithHandler = simpleType.withValueHandler(handler);
        assertNotEquals(simpleType, simpleTypeWithHandler);
        assertEquals(handler, simpleTypeWithHandler._valueHandler);
    }

    @Test
    public void testWithStaticTyping() {
        SimpleType simpleType = new SimpleType(String.class);
        SimpleType simpleTypeWithStaticTyping = simpleType.withStaticTyping();
        assertNotEquals(simpleType, simpleTypeWithStaticTyping);
        assertTrue(simpleTypeWithStaticTyping._asStatic);
    }

    @Test
    public void testRefine() {
        SimpleType simpleType = new SimpleType(String.class);
        JavaType refinedType = simpleType.refine(Integer.class, null, null, null);
        assertNull(refinedType);
    }

    @Test
    public void testBuildCanonicalName() {
        SimpleType simpleType = new SimpleType(List.class);
        assertEquals("java.util.List", simpleType.buildCanonicalName());
    }
}