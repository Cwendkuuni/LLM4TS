package com.fasterxml.jackson.databind.type;

import static org.junit.Assert.*;

import org.junit.Test;

public class SimpleTypeTest {

    @Test
    public void testIsContainerType() {
        SimpleType simpleType = new SimpleType(String.class, null, null, null);
        assertFalse(simpleType.isContainerType());
    }

    @Test
    public void testBuildCanonicalName() {
        SimpleType simpleType = new SimpleType(String.class, null, null, null);
        assertEquals("java.lang.String", simpleType.buildCanonicalName());
    }

    @Test
    public void testGetErasedSignature() {
        SimpleType simpleType = new SimpleType(String[].class, null, null, null);
        StringBuilder sb = new StringBuilder();
        simpleType.getErasedSignature(sb);
        assertEquals("[Ljava.lang.String;", sb.toString());
    }

    @Test
    public void testGetGenericSignature() {
        SimpleType simpleType = new SimpleType(List.class, null, null, null);
        StringBuilder sb = new StringBuilder();
        simpleType.getGenericSignature(sb);
        assertEquals("java.util.List<T>", sb.toString());
    }

    @Test
    public void testToString() {
        SimpleType simpleType = new SimpleType(String.class, null, null, null);
        assertEquals("[simple type, class java.lang.String]", simpleType.toString());
    }

    @Test
    public void testEquals() {
        SimpleType simpleType1 = new SimpleType(String.class, null, null, null);
        SimpleType simpleType2 = new SimpleType(String.class, null, null, null);
        assertTrue(simpleType1.equals(simpleType2));

        SimpleType simpleType3 = new SimpleType(Integer.class, null, null, null);
        assertFalse(simpleType1.equals(simpleType3));
    }
}