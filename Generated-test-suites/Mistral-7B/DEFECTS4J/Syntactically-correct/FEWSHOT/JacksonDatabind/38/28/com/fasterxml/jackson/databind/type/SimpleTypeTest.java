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
        assertFalse(simpleType.isContainerType());
    }

    @Test
    public void testConstruct() {
        // This test method is deprecated and should not be used.
        // However, for completeness, we test its behavior.

        Class<?> cls = String.class;
        SimpleType simpleType = SimpleType.construct(cls);
        assertEquals(cls, simpleType.getRawClass());
        assertNull(simpleType.getSuperClass());
        assertNull(simpleType.getSuperInterfaces());
        assertFalse(simpleType.isContainerType());

        // Check that calling this method throws an exception for collection classes
        try {
            SimpleType collectionType = SimpleType.construct(ArrayList.class);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertEquals("Can not construct SimpleType for a Collection (class: java.util.ArrayList)", e.getMessage());
        }
    }

    @Test
    public void testIsContainerType() {
        Class<?> cls = String.class;
        SimpleType simpleType = new SimpleType(cls, TypeBindings.emptyBindings(), null, null);
        assertFalse(simpleType.isContainerType());

        Class<?> arrayListClass = ArrayList.class;
        SimpleType arrayListType = new SimpleType(arrayListClass, TypeBindings.emptyBindings(), null, null);
        assertTrue(arrayListType.isContainerType());

        Class<?> listClass = List.class;
        SimpleType listType = new SimpleType(listClass, TypeBindings.emptyBindings(), null, null);
        assertTrue(listType.isContainerType());

        Class<?> setClass = Set.class;
        SimpleType setType = new SimpleType(setClass, TypeBindings.emptyBindings(), null, null);
        assertTrue(setType.isContainerType());

        Class<?> mapClass = Map.class;
        SimpleType mapType = new SimpleType(mapClass, TypeBindings.emptyBindings(), null, null);
        assertTrue(mapType.isContainerType());
    }

    @Test
    public void testGetErasedSignature() {
        Class<?> cls = String.class;
        SimpleType simpleType = new SimpleType(cls, TypeBindings.emptyBindings(), null, null);
        StringBuilder sb = new StringBuilder();
        simpleType.getErasedSignature(sb);
        assertEquals("java.lang.String", sb.toString());
    }

    @Test
    public void testGetGenericSignature() {
        Class<?> cls = String.class;
        SimpleType simpleType = new SimpleType(cls, TypeBindings.emptyBindings(), null, null);
        StringBuilder sb = new StringBuilder();
        simpleType.getGenericSignature(sb);
        assertEquals("java.lang.String", sb.toString());

        Class<?> listClass = List.class;
        TypeBindings bindings = new TypeBindings();
        bindings.put(0, String.class);
        SimpleType listType = new SimpleType(listClass, bindings, null, null);
        StringBuilder sb2 = new StringBuilder();
        listType.getGenericSignature(sb2);
        assertEquals("java.util.List<java.lang.String>", sb2.toString());
    }

    @Test
    public void testEquals() {
        Class<?> cls1 = String.class;
        Class<?> cls2 = Integer.class;
        SimpleType simpleType1 = new SimpleType(cls1, TypeBindings.emptyBindings(), null, null);
        SimpleType simpleType2 = new SimpleType(cls2, TypeBindings.emptyBindings(), null, null);
        assertFalse(simpleType1.equals(simpleType2));

        SimpleType simpleType3 = new SimpleType(cls1, TypeBindings.emptyBindings(), null, null);
        assertTrue(simpleType1.equals(simpleType3));

        SimpleType simpleType4 = new SimpleType(cls1, TypeBindings.emptyBindings(), null, null);
        TypeBindings bindings = new TypeBindings();
        bindings.put(0, Integer.class);
        SimpleType simpleType5 = new SimpleType(cls1, bindings, null, null);
        assertFalse(simpleType1.equals(simpleType5));
    }
}