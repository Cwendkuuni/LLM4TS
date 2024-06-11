package com.fasterxml.jackson.databind.type;

import static org.junit.Assert.*;

import org.junit.Test;

public class SimpleTypeTest {

    @Test
    public void testIsContainerType() {
        SimpleType simpleType = new SimpleType(String.class, TypeBindings.emptyBindings(), null, null);
        assertFalse(simpleType.isContainerType());
    }

    @Test
    public void testGetErasedSignature() {
        SimpleType simpleType = new SimpleType(String.class, TypeBindings.emptyBindings(), null, null);
        StringBuilder expected = new StringBuilder("java.lang.String");
        StringBuilder actual = simpleType.getErasedSignature(new StringBuilder());
        assertEquals(expected.toString(), actual.toString());
    }

    @Test
    public void testGetGenericSignature() {
        SimpleType simpleType = new SimpleType(String[].class, TypeBindings.emptyBindings(), null, null);
        StringBuilder expected = new StringBuilder("java.lang.String[]");
        StringBuilder actual = simpleType.getGenericSignature(new StringBuilder());
        assertEquals(expected.toString(), actual.toString());
    }

    @Test
    public void testBuildCanonicalName() {
        SimpleType simpleType = new SimpleType(List.class, TypeBindings.emptyBindings(), null, null);
        StringBuilder expected = new StringBuilder("java.util.List");
        StringBuilder actual = simpleType.buildCanonicalName();
        assertEquals(expected.toString(), actual.toString());
    }

    @Test
    public void testToString() {
        SimpleType simpleType = new SimpleType(String.class, TypeBindings.emptyBindings(), null, null);
        StringBuilder expected = new StringBuilder("[simple type, class [Ljava.lang.String;]");
        StringBuilder actual = simpleType.toString();
        assertEquals(expected.toString(), actual.toString());
    }

    @Test
    public void testEquals() {
        SimpleType simpleType1 = new SimpleType(String.class, TypeBindings.emptyBindings(), null, null);
        SimpleType simpleType2 = new SimpleType(String.class, TypeBindings.emptyBindings(), null, null);
        assertTrue(simpleType1.equals(simpleType2));

        SimpleType simpleType3 = new SimpleType(Integer.class, TypeBindings.emptyBindings(), null, null);
        assertFalse(simpleType1.equals(simpleType3));
    }
}