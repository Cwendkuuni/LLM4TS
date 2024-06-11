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
        StringBuilder expected = new StringBuilder("java.lang.String");
        StringBuilder actual = simpleType.getErasedSignature(new StringBuilder());
        assertEquals(expected.toString(), actual.toString());
    }

    @Test
    public void testGetGenericSignature() {
        SimpleType simpleType = new SimpleType(List.class, TypeBindings.emptyBindings(), null, null);
        StringBuilder expected = new StringBuilder("java.util.List<");
        StringBuilder actual = simpleType.getGenericSignature(new StringBuilder());
        assertEquals(expected.toString(), actual.toString());
    }

    @Test
    public void testBuildCanonicalName() {
        SimpleType simpleType = new SimpleType(Map.class, TypeBindings.emptyBindings(), null, null);
        StringBuilder expected = new StringBuilder("java.util.Map<java.lang.Object, java.lang.Object>");
        StringBuilder actual = simpleType.buildCanonicalName();
        assertEquals(expected.toString(), actual.toString());
    }

    @Test
    public void testToString() {
        SimpleType simpleType = new SimpleType(String[].class);
        StringBuilder expected = new StringBuilder("[simple type, class [Ljava.lang.String;]]");
        StringBuilder actual = new StringBuilder();
        actual = simpleType.toString();
        assertEquals(expected.toString(), actual.toString());
    }

    @Test
    public void testEquals() {
        SimpleType simpleType1 = new SimpleType(String.class);
        SimpleType simpleType2 = new SimpleType(String.class);
        SimpleType simpleType3 = new SimpleType(Integer.class);
        assertEquals(simpleType1, simpleType1);
        assertEquals(simpleType1, simpleType2);
        assertNotEquals(simpleType1, simpleType3);
    }
}