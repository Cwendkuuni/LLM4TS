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
        SimpleType simpleType = new SimpleType(String[].class, TypeBindings.emptyBindings(), null, null);
        StringBuilder expected = new StringBuilder("java.lang.String[]");
        StringBuilder actual = simpleType.buildCanonicalName();
        assertEquals(expected.toString(), actual.toString());
    }

    @Test
    public void testEquals() {
        SimpleType simpleType1 = new SimpleType(String.class, TypeBindings.emptyBindings(), null, null);
        SimpleType simpleType2 = new SimpleType(String.class, TypeBindings.emptyBindings(), null, null);
        assertTrue(simpleType1.equals(simpleType2));

        SimpleType simpleType3 = new SimpleType(Integer.class, TypeBindings.emptyBindings(), null, null);
        assertFalse(simpleType1.equals(simpleType3));

        SimpleType simpleType4 = new SimpleType(String.class, TypeBindings.emptyBindings(), null, null);
        SimpleType simpleType5 = new SimpleType(String.class, TypeBindings.emptyBindings(), null, null);
        TypeBindings bindings = new TypeBindings();
        bindings.put(String.class, new SimpleType(String.class, bindings, null, null));
        SimpleType simpleType6 = new SimpleType(String.class, bindings, null, null);
        SimpleType simpleType7 = new SimpleType(String.class, bindings, null, null);
        assertNotEquals(simpleType1, simpleType6);
        assertEquals(simpleType1, simpleType7);
    }

    @Test
    public void testToString() {
        SimpleType simpleType = new SimpleType(String.class, TypeBindings.emptyBindings(), null, null);
        StringBuilder expected = new StringBuilder("[simple type, class java.lang.String]");
        StringBuilder actual = new StringBuilder();
        actual.append(simpleType.toString());
        assertEquals(expected.toString(), actual.toString());
    }
}