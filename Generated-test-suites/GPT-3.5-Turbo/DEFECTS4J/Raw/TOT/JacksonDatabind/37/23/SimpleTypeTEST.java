package com.fasterxml.jackson.databind.type;

import org.junit.Test;
import static org.junit.Assert.*;

import com.fasterxml.jackson.databind.JavaType;

public class SimpleTypeTest {

    @Test
    public void testConstructUnsafe() {
        SimpleType simpleType = SimpleType.constructUnsafe(Object.class);
        assertNotNull(simpleType);
        assertEquals(Object.class, simpleType.getRawClass());
    }

    @Test
    public void testConstruct() {
        SimpleType simpleType = SimpleType.construct(String.class);
        assertNotNull(simpleType);
        assertEquals(String.class, simpleType.getRawClass());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructWithMap() {
        SimpleType.construct(Map.class);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructWithCollection() {
        SimpleType.construct(Collection.class);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructWithArray() {
        SimpleType.construct(String[].class);
    }

    @Test
    public void testNarrow() {
        SimpleType simpleType = SimpleType.construct(Object.class);
        JavaType narrowedType = simpleType.narrow(String.class);
        assertNotNull(narrowedType);
        assertEquals(String.class, narrowedType.getRawClass());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidNarrow() {
        SimpleType simpleType = SimpleType.construct(Object.class);
        simpleType.narrow(Integer.class);
    }

    @Test
    public void testWithContentType() {
        SimpleType simpleType = SimpleType.construct(Object.class);
        try {
            simpleType.withContentType(Object.class);
            fail("Exception not thrown");
        } catch (IllegalArgumentException e) {
            assertEquals("Simple types have no content types; can not call withContentType()", e.getMessage());
        }
    }

    @Test
    public void testWithValueHandler() {
        SimpleType simpleType = SimpleType.construct(Object.class);
        Object valueHandler = new Object();

        SimpleType newType = simpleType.withValueHandler(valueHandler);
        assertNotNull(newType);
        assertEquals(simpleType.getRawClass(), newType.getRawClass());
        assertNotEquals(simpleType.getValueHandler(), newType.getValueHandler());
        assertEquals(valueHandler, newType.getValueHandler());
    }

    @Test
    public void testWithStaticTyping() {
        SimpleType simpleType = SimpleType.construct(Object.class);
        assertFalse(simpleType.isStaticTyping());

        SimpleType newType = simpleType.withStaticTyping();
        assertNotNull(newType);
        assertEquals(simpleType.getRawClass(), newType.getRawClass());
        assertTrue(newType.isStaticTyping());
    }

    @Test
    public void testIsContainerType() {
        SimpleType simpleType = SimpleType.construct(Object.class);
        assertFalse(simpleType.isContainerType());
    }

    @Test
    public void testGetErasedSignature() {
        SimpleType simpleType = SimpleType.construct(Object.class);
        StringBuilder sb = new StringBuilder();
        simpleType.getErasedSignature(sb);
        assertEquals("Ljava/lang/Object;", sb.toString());
    }

    @Test
    public void testGetGenericSignature() {
        SimpleType simpleType = SimpleType.construct(Object.class);
        StringBuilder sb = new StringBuilder();
        simpleType.getGenericSignature(sb);
        assertEquals("Ljava/lang/Object;", sb.toString());
    }

    @Test
    public void testToString() {
        SimpleType simpleType = SimpleType.construct(Object.class);
        String expected = "[simple type, class java.lang.Object]";
        assertEquals(expected, simpleType.toString());
    }

    @Test
    public void testEquals() {
        SimpleType simpleType1 = SimpleType.construct(Object.class);
        SimpleType simpleType2 = SimpleType.construct(Object.class);
        assertEquals(simpleType1, simpleType2);
    }

    @Test
    public void testRefine() {
        SimpleType simpleType = SimpleType.construct(Object.class);
        JavaType refinedType = simpleType.refine(String.class, TypeBindings.emptyBindings(), null, null);
        assertNull(refinedType);
    }
}