package com.fasterxml.jackson.databind.type;

import static org.junit.Assert.*;

import org.junit.Test;

public class SimpleTypeTest {

    @Test
    public void testConstructUnsafe() {
        Class<?> cls = String.class;
        SimpleType simpleType = SimpleType.constructUnsafe(cls);
        assertEquals(cls, simpleType.getRawClass());
        assertNull(simpleType.getSuperClass());
        assertNull(simpleType.getSuperInterfaces());
        assertFalse(simpleType.isStatic());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructWithUnsupportedClass() {
        Class<?> cls = Map.class;
        SimpleType.construct(cls);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNarrowWithUnrelatedClass() {
        Class<?> cls = String.class;
        SimpleType simpleType = new SimpleType(int.class, null, null, null, null, null, false);
        assertNotSame(simpleType, simpleType._narrow(cls));
    }

    @Test
    public void testNarrowWithSubclass() {
        Class<?> superClass = Object.class;
        Class<?> subClass = String.class;
        SimpleType simpleType = new SimpleType(superClass, null, null, null, null, null, false);
        SimpleType narrowedType = simpleType._narrow(subClass);
        assertEquals(subClass, narrowedType.getRawClass());
    }

    @Test
    public void testBuildCanonicalName() {
        Class<?> listClass = List.class;
        Class<?> elementType = String.class;
        SimpleType simpleType = new SimpleType(listClass, null, null, null, null, null, false);
        String canonicalName = simpleType.buildCanonicalName();
        assertEquals("java.util.List<java.lang.String>", canonicalName);
    }
}