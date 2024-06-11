package com.fasterxml.jackson.databind.type;

import static org.junit.Assert.*;

import org.junit.Test;

public class SimpleTypeTest {

    @Test
    public void testSimpleTypeConstruction() {
        Class<String> cls = String.class;
        SimpleType simpleType1 = new SimpleType(cls);
        SimpleType simpleType2 = new SimpleType(cls, TypeBindings.emptyBindings(), null, null);
        assertEquals(simpleType1, simpleType2);

        SimpleType simpleType3 = new SimpleType(cls, TypeBindings.emptyBindings(), null, null, null, null, false);
        assertEquals(simpleType1, simpleType3);

        SimpleType simpleType4 = new SimpleType(String[].class);
        assertFalse(simpleType1.equals(simpleType4));
    }

    @Test
    public void testNarrowMethod() {
        Class<String> cls = String.class;
        SimpleType simpleType = new SimpleType(cls);

        SimpleType narrowedType1 = simpleType._narrow(Integer.class);
        assertNull(narrowedType1);

        SimpleType narrowedType2 = new SimpleType(Number.class)._narrow(String.class);
        assertNotNull(narrowedType2);
        assertEquals(String.class, narrowedType2._class);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructMethod() {
        Class<String> cls = String.class;
        TypeFactory.constructType(cls);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructUnsafeMethod() {
        Class<String> cls = String.class;
        SimpleType.constructUnsafe(cls);
    }
}