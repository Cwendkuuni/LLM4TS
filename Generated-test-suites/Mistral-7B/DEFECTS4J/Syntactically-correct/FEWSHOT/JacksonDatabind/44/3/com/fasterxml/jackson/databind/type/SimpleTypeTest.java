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
    public void testConstructUnsafeWithArray() {
        Class<?> cls = int[].class;
        SimpleType.constructUnsafe(cls);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructUnsafeWithMap() {
        Class<?> cls = HashMap.class;
        SimpleType.constructUnsafe(cls);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructUnsafeWithCollection() {
        Class<?> cls = List.class;
        SimpleType.constructUnsafe(cls);
    }

    @Test
    public void testConstruct() {
        Class<?> cls = String.class;
        SimpleType simpleType = SimpleType.construct(cls);
        assertEquals(cls, simpleType.getRawClass());
        assertNull(simpleType.getSuperClass());
        assertNull(simpleType.getSuperInterfaces());
        assertFalse(simpleType.isStatic());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructWithMap() {
        Class<?> cls = HashMap.class;
        SimpleType.construct(cls);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructWithCollection() {
        Class<?> cls = ArrayList.class;
        SimpleType.construct(cls);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructWithArray() {
        Class<?> cls = int[].class;
        SimpleType.construct(cls);
    }

    @Test
    public void testNarrow() {
        Class<?> baseClass = String.class;
        Class<?> subClass = Object.class;
        SimpleType baseType = new SimpleType(baseClass, TypeBindings.emptyBindings(), null, null);
        SimpleType subType = new SimpleType(subClass, TypeBindings.emptyBindings(), null, null);
        assertEquals(baseType, baseType.narrow(subClass));
        assertEquals(subType, subType.narrow(baseClass));

        Class<?> baseClass2 = Integer.class;
        Class<?> subClass2 = String.class;
        SimpleType baseType2 = new SimpleType(baseClass2, TypeBindings.emptyBindings(), null, null);
        SimpleType subType2 = new SimpleType(subClass2, TypeBindings.emptyBindings(), null, null);
        assertNotSame(baseType2, subType2.narrow(baseClass2));
        assertNotSame(subType2, baseType2.narrow(subClass2));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNarrowInvalidSubclass() {
        Class<?> baseClass = String.class;
        Class<?> subClass = Integer.class;
        SimpleType baseType = new SimpleType(baseClass, TypeBindings.emptyBindings(), null, null);
        SimpleType subType = new SimpleType(subClass, TypeBindings.emptyBindings(), null, null);
        subType.narrow(baseClass);
    }
}