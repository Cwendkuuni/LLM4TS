package com.fasterxml.jackson.databind.type;

import static org.junit.Assert.*;

import java.util.Arrays;

import org.junit.Test;

public class SimpleTypeTest {

    @Test
    public void testConstructUnsafe() {
        SimpleType type = SimpleType.constructUnsafe(String.class);
        assertNotNull(type);
        assertEquals(String.class, type.getRawClass());
        assertNull(type.getSuperClass());
        assertNull(type.getSuperInterfaces());
        assertNull(type.getValueHandler());
        assertNull(type.getTypeHandler());
        assertFalse(type.isStatic());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructUnsafeWithArray() {
        SimpleType type = SimpleType.constructUnsafe(int[].class);
        fail("IllegalArgumentException expected but not thrown");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructUnsafeWithCollection() {
        SimpleType type = SimpleType.constructUnsafe(ArrayList.class);
        fail("IllegalArgumentException expected but not thrown");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructUnsafeWithMap() {
        SimpleType type = SimpleType.constructUnsafe(HashMap.class);
        fail("IllegalArgumentException expected but not thrown");
    }

    @Test
    public void testConstruct() {
        SimpleType type = SimpleType.construct(String.class);
        assertNotNull(type);
        assertEquals(String.class, type.getRawClass());
        assertNull(type.getSuperClass());
        assertNull(type.getSuperInterfaces());
        assertNull(type.getValueHandler());
        assertNull(type.getTypeHandler());
        assertFalse(type.isStatic());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructWithArray() {
        SimpleType type = SimpleType.construct(int[].class);
        fail("IllegalArgumentException expected but not thrown");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructWithCollection() {
        SimpleType type = SimpleType.construct(ArrayList.class);
        fail("IllegalArgumentException expected but not thrown");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructWithMap() {
        SimpleType type = SimpleType.construct(HashMap.class);
        fail("IllegalArgumentException expected but not thrown");
    }

    @Test
    public void testNarrow() {
        Class<String> stringType = String.class;
        SimpleType stringSimpleType = new SimpleType(stringType);
        SimpleType stringSubClass = new SimpleType(StringBuilder.class);

        SimpleType narrowedType = stringSimpleType.narrow(StringBuilder.class);
        assertEquals(stringSubClass, narrowedType);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNarrowWithInvalidSubclass() {
        Class<String> stringType = String.class;
        SimpleType stringSimpleType = new SimpleType(stringType);
        SimpleType intType = new SimpleType(int.class);

        SimpleType narrowedType = stringSimpleType.narrow(int.class);
        fail("IllegalArgumentException expected but not thrown");
    }

    @Test
    public void testToString() {
        Class<String> stringType = String.class;
        SimpleType stringSimpleType = new SimpleType(stringType);

        StringBuilder expected = new StringBuilder("[simple type, class ");
        expected.append(stringType.getName()).append(']');

        StringBuilder actual = new StringBuilder(stringSimpleType.toString());

        assertEquals(expected.toString(), actual.toString());
    }

    @Test
    public void testEquals() {
        Class<String> stringType1 = String.class;
        Class<String> stringType2 = String.class;
        SimpleType stringSimpleType1 = new SimpleType(stringType1);
        SimpleType stringSimpleType2 = new SimpleType(stringType2);

        assertTrue(stringSimpleType1.equals(stringSimpleType2));
    }

    @Test
    public void testEqualsDifferentClass() {
        Class<String> stringType = String.class;
        Class<Integer> intType = Integer.class;
        SimpleType stringSimpleType = new SimpleType(stringType);
        SimpleType intSimpleType = new SimpleType(intType);

        assertFalse(stringSimpleType.equals(intSimpleType));
    }

    @Test
    public void testEqualsNull() {
        Class<String> stringType = String.class;
        SimpleType stringSimpleType = new SimpleType(stringType);

        assertFalse(stringSimpleType.equals(null));
    }
}