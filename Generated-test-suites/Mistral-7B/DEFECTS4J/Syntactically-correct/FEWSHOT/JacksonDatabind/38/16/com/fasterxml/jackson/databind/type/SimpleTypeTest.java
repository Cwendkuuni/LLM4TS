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
        StringBuilder actualErasedSignature = simpleType.getErasedSignature(new StringBuilder());
        StringBuilder expectedErasedSignature = new StringBuilder("[simple type, class java.lang.String]");
        assertEquals(expectedErasedSignature.toString(), actualErasedSignature.toString());
    }

    @Test
    public void testGetGenericSignature() {
        SimpleType simpleType = new SimpleType(String[].class, TypeBindings.emptyBindings(), null, null);
        StringBuilder actualGenericSignature = simpleType.getGenericSignature(new StringBuilder());
        StringBuilder expectedGenericSignature = new StringBuilder("[simple type, class [Ljava.lang.String;]");
        assertEquals(expectedGenericSignature.toString(), actualGenericSignature.toString());
    }

    @Test
    public void testToString() {
        SimpleType simpleType = new SimpleType(String.class, TypeBindings.emptyBindings(), null, null);
        StringBuilder expectedString = new StringBuilder("[simple type, class java.lang.String]");
        StringBuilder actualString = new StringBuilder();
        actualString.append(simpleType.toString());
        assertEquals(expectedString.toString(), actualString.toString());
    }

    @Test
    public void testEquals() {
        SimpleType simpleType1 = new SimpleType(String.class, TypeBindings.emptyBindings(), null, null);
        SimpleType simpleType2 = new SimpleType(String.class, TypeBindings.emptyBindings(), null, null);
        assertTrue(simpleType1.equals(simpleType2));
    }

    @Test
    public void testEqualsWithDifferentClasses() {
        SimpleType simpleType1 = new SimpleType(String.class, TypeBindings.emptyBindings(), null, null);
        SimpleType simpleType2 = new SimpleType(Integer.class, TypeBindings.emptyBindings(), null, null);
        assertFalse(simpleType1.equals(simpleType2));
    }

    @Test
    public void testEqualsWithDifferentBindings() {
        SimpleType simpleType1 = new SimpleType(String.class, TypeBindings.emptyBindings(), null, null);
        SimpleType simpleType2 = new SimpleType(String.class, new TypeBindings(), null, null);
        assertFalse(simpleType1.equals(simpleType2));
    }
}