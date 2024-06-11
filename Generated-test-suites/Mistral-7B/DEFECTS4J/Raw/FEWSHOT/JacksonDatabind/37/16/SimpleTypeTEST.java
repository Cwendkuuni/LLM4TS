package com.fasterxml.jackson.databind.type;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;

import org.junit.Test;

public class SimpleTypeTest {

    @Test
    public void testConstructUnsafe() {
        Class<?> raw = String.class;
        SimpleType simpleType = SimpleType.constructUnsafe(raw);
        assertEquals(String.class, simpleType.getRawClass());
        assertFalse(simpleType.isContainerType());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstruct_InvalidClass() {
        Class<?> raw = Map.class;
        SimpleType.construct(raw);
    }

    @Test
    public void testConstruct_ValidClass() {
        Class<?> raw = Integer.class;
        SimpleType simpleType = SimpleType.construct(raw);
        assertEquals(Integer.class, simpleType.getRawClass());
        assertFalse(simpleType.isContainerType());
    }

    @Test
    public void testNarrow() {
        Class<?> raw = Integer.class;
        SimpleType intType = SimpleType.construct(raw);
        Class<?> intWrapperClass = Integer.TYPE.getClass();
        SimpleType intWrapperType = intType._narrow(intWrapperClass);
        assertEquals(intWrapperClass, intWrapperType.getRawClass());
        assertSame(intType, intType._narrow(Integer.TYPE));
    }

    @Test
    public void testIsContainerType() {
        Class<?> raw = ArrayList.class;
        SimpleType listType = SimpleType.construct(raw);
        assertTrue(listType.isContainerType());

        raw = Integer.class;
        SimpleType intType = SimpleType.construct(raw);
        assertFalse(intType.isContainerType());
    }

    @Test
    public void testGetErasedSignature() {
        Class<?> raw = Map.class;
        SimpleType mapType = SimpleType.construct(raw);
        StringBuilder expected = new StringBuilder("Map");
        StringBuilder sb = new StringBuilder();
        mapType.getErasedSignature(sb);
        assertEquals(expected.toString(), sb.toString());
    }

    @Test
    public void testGetGenericSignature() {
        Class<?> raw = Map.class;
        SimpleType mapType = SimpleType.construct(raw);
        StringBuilder expected = new StringBuilder("Map<K, V>");
        StringBuilder sb = new StringBuilder();
        mapType.getGenericSignature(sb);
        assertEquals(expected.toString(), sb.toString());
    }

    @Test
    public void testToString() {
        Class<?> raw = Map.class;
        SimpleType mapType = SimpleType.construct(raw);
        StringBuilder expected = new StringBuilder("[simple type, class [Ljava.util.Map;]");
        StringBuilder actual = new StringBuilder();
        actual = mapType.toString();
        assertEquals(expected.toString(), actual.toString());
    }

    @Test
    public void testEquals() {
        Class<?> raw1 = Integer.class;
        Class<?> raw2 = Integer.class;
        SimpleType intType1 = SimpleType.construct(raw1);
        SimpleType intType2 = SimpleType.construct(raw2);
        SimpleType intType3 = intType1;
        assertTrue(intType1.equals(intType1));
        assertTrue(intType1.equals(intType2));
        assertTrue(intType1.equals(intType3));

        SimpleType intWrapperType = new SimpleType(intType1);
        assertFalse(intType1.equals(intWrapperType));

        SimpleType listType = SimpleType.construct(ArrayList.class);
        SimpleType listType2 = new SimpleType(listType);
        assertFalse(listType.equals(listType2));
    }

    @Test
    public void testRefine() {
        Class<?> raw = List.class;
        TypeBindings bindings = new TypeBindings();
        bindings.put(0, new SimpleType(ArrayList.class));
        SimpleType listType = SimpleType.construct(raw);
        JavaType refinedType = listType.refine(ArrayList.class, bindings, null, null);
        assertNull(refinedType);
    }
}