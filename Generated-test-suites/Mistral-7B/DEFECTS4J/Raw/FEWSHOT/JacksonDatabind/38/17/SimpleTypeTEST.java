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
        assertNull(type.getSuperInterfaces());
        assertFalse(type.isStatic());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructUnsafeWithArray() {
        SimpleType type = SimpleType.constructUnsafe(int[].class);
        fail("IllegalArgumentException expected");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructUnsafeWithMap() {
        SimpleType type = SimpleType.constructUnsafe(HashMap.class);
        fail("IllegalArgumentException expected");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructUnsafeWithCollection() {
        SimpleType type = SimpleType.constructUnsafe(ArrayList.class);
        fail("IllegalArgumentException expected");
    }

    @Test
    public void testConstruct() {
        SimpleType type = SimpleType.construct(String.class);
        assertNotNull(type);
        assertEquals(String.class, type.getRawClass());
        assertNull(type.getSuperClass());
        assertNull(type.getSuperInterfaces());
        assertFalse(type.isStatic());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructWithArray() {
        SimpleType type = SimpleType.construct(int[].class);
        fail("IllegalArgumentException expected");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructWithMap() {
        SimpleType type = SimpleType.construct(HashMap.class);
        fail("IllegalArgumentException expected");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructWithCollection() {
        SimpleType type = SimpleType.construct(ArrayList.class);
        fail("IllegalArgumentException expected");
    }

    @Test
    public void testIsContainerType() {
        SimpleType type1 = new SimpleType(String.class, TypeBindings.emptyBindings(), null, null);
        assertFalse(type1.isContainerType());

        SimpleType type2 = new SimpleType(ArrayList.class, TypeBindings.emptyBindings(), null, null);
        assertTrue(type2.isContainerType());
    }

    @Test
    public void testGetErasedSignature() {
        SimpleType type = new SimpleType(String.class, TypeBindings.emptyBindings(), null, null);
        StringBuilder expected = new StringBuilder("java.lang.String");
        StringBuilder actual = type.getErasedSignature(new StringBuilder());
        assertEquals(expected.toString(), actual.toString());
    }

    @Test
    public void testGetGenericSignature() {
        SimpleType type = new SimpleType(String.class, TypeBindings.emptyBindings(), null, null);
        StringBuilder expected = new StringBuilder("java.lang.String");
        StringBuilder actual = type.getGenericSignature(new StringBuilder());
        assertEquals(expected.toString(), actual.toString());

        SimpleType listType = new SimpleType(ArrayList.class, TypeBindings.emptyBindings(), null, null);
        expected = new StringBuilder("java.util.ArrayList<T>");
        actual = listType.getGenericSignature(new StringBuilder());
        assertEquals(expected.toString(), actual.toString());
    }

    @Test
    public void testToString() {
        SimpleType type = new SimpleType(String.class, TypeBindings.emptyBindings(), null, null);
        String expected = "[simple type, class [Ljava.lang.String;]]";
        String actual = type.toString();
        assertEquals(expected, actual);

        type = new SimpleType(ArrayList.class, TypeBindings.emptyBindings(), null, null);
        expected = "[simple type, class java.util.ArrayList]";
        actual = type.toString();
        assertEquals(expected, actual);
    }

    @Test
    public void testEquals() {
        SimpleType type1 = new SimpleType(String.class, TypeBindings.emptyBindings(), null, null);
        SimpleType type2 = new SimpleType(String.class, TypeBindings.emptyBindings(), null, null);
        assertTrue(type1.equals(type2));

        SimpleType type3 = new SimpleType(ArrayList.class, TypeBindings.emptyBindings(), null, null);
        SimpleType type4 = new SimpleType(ArrayList.class, TypeBindings.emptyBindings(), null, null);
        assertTrue(type3.equals(type4));

        SimpleType type5 = new SimpleType(String.class, TypeBindings.emptyBindings(), null, null);
        SimpleType type6 = new SimpleType(Object.class, TypeBindings.emptyBindings(), null, null);
        assertFalse(type5.equals(type6));

        SimpleType type7 = new SimpleType(String.class, TypeBindings.emptyBindings(), null, null);
        SimpleType type8 = new SimpleType(String[].class, TypeBindings.emptyBindings(), null, null);
        assertFalse(type7.equals(type8));
    }
}