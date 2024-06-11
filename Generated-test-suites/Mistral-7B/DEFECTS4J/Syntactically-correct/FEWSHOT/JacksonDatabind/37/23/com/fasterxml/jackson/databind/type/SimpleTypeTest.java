package com.fasterxml.jackson.databind.type;

import static org.junit.Assert.*;

import org.junit.Test;

public class SimpleTypeTest {

    @Test
    public void testConstructUnsafe() {
        SimpleType type = SimpleType.constructUnsafe(String.class);
        assertNotNull(type);
        assertEquals(String.class, type.getRawClass());
        assertNull(type.getSuperClass());
        assertNull(type.getSuperTypes());
        assertFalse(type.isContainerType());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructUnsafeWithArray() {
        SimpleType type = SimpleType.constructUnsafe(int[].class);
        fail("Should not reach this point");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructUnsafeWithMap() {
        SimpleType type = SimpleType.constructUnsafe(HashMap.class);
        fail("Should not reach this point");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructUnsafeWithCollection() {
        SimpleType type = SimpleType.constructUnsafe(ArrayList.class);
        fail("Should not reach this point");
    }

    @Test
    public void testConstruct() {
        SimpleType type = SimpleType.construct(String.class);
        assertNotNull(type);
        assertEquals(String.class, type.getRawClass());
        assertNull(type.getSuperClass());
        assertNull(type.getSuperTypes());
        assertFalse(type.isContainerType());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructWithArray() {
        SimpleType type = SimpleType.construct(int[].class);
        fail("Should not reach this point");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructWithMap() {
        SimpleType type = SimpleType.construct(HashMap.class);
        fail("Should not reach this point");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructWithCollection() {
        SimpleType type = SimpleType.construct(ArrayList.class);
        fail("Should not reach this point");
    }

    @Test
    public void testNarrow() {
        SimpleType stringType = SimpleType.construct(String.class);
        SimpleType stringSubType = SimpleType.construct(StringBuilder.class);
        SimpleType narrowed = stringType.narrow(StringBuilder.class);
        assertEquals(stringSubType, narrowed);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNarrowWithInvalidSubclass() {
        SimpleType intType = SimpleType.construct(int.class);
        SimpleType stringType = SimpleType.construct(String.class);
        intType.narrow(String.class);
        fail("Should not reach this point");
    }
}