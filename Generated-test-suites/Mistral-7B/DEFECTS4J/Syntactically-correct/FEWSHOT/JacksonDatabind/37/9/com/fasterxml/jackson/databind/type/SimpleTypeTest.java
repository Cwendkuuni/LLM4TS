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
        StringBuilder expected = new StringBuilder("[Ljava/lang/String;");
        StringBuilder actual = simpleType.getErasedSignature(new StringBuilder());
        assertEquals(expected.toString(), actual.toString());
    }

    @Test
    public void testGetGenericSignature() {
        SimpleType simpleType = new SimpleType(List.class);
        StringBuilder expected = new StringBuilder("<Ljava/util/List;>");
        StringBuilder actual = simpleType.getGenericSignature(new StringBuilder());
        assertEquals(expected.toString(), actual.toString());
    }

    @Test
    public void testGetClass() {
        SimpleType simpleType = new SimpleType(String.class);
        assertEquals(String.class, simpleType.getClass());
    }

    @Test
    public void testEquals() {
        SimpleType simpleType1 = new SimpleType(String.class);
        SimpleType simpleType2 = new SimpleType(String.class);
        assertTrue(simpleType1.equals(simpleType2));

        TypeBindings bindings = new TypeBindings();
        SimpleType simpleType3 = new SimpleType(String[].class, bindings);
        assertFalse(simpleType1.equals(simpleType3));
    }

    @Test
    public void testConstructUnsafe() {
        Class<?> raw = String.class;
        SimpleType simpleType = SimpleType.constructUnsafe(raw);
        assertEquals(String.class, simpleType.getClass());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructDeprecated() {
        Class<?> cls = String.class;
        SimpleType simpleType = SimpleType.construct(cls);
    }

    @Test
    public void testNarrow() {
        Class<?> raw = String.class;
        SimpleType simpleType1 = new SimpleType(raw);
        Class<?> subclass = StringBuffer.class;
        SimpleType simpleType2 = simpleType1._narrow(subclass);
        assertEquals(StringBuffer.class, simpleType2.getClass());
    }

    @Test
    public void testWithTypeHandler() {
        Class<?> cls = String.class;
        SimpleType simpleType = new SimpleType(cls);
        Object typeHandler = new Object();
        SimpleType simpleTypeWithHandler = simpleType.withTypeHandler(typeHandler);
        assertEquals(simpleType.getClass(), simpleTypeWithHandler.getClass());
        assertNotEquals(simpleType.getTypeHandler(), simpleTypeWithHandler.getTypeHandler());
    }
}