package com.fasterxml.jackson.databind.type;

import java.lang.reflect.TypeVariable;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

import org.junit.Test;

public class TypeBindingsTest {

    @Test
    public void testCreateEmptyBindings() {
        TypeBindings bindings = TypeBindings.emptyBindings();
        assertNull(bindings._names);
        assertArrayEquals(new JavaType[0], bindings._types);
        assertNull(bindings._unboundVariables);
        assertEquals(0, bindings._hashCode);
    }

    @Test
    public void testCreateWithOneType() {
        JavaType type = new org.apache.commons.math3.complex.ComplexType();
        TypeBindings bindings = TypeBindings.create(Integer.TYPE, type);
        assertArrayEquals(new String[]{"T"}, bindings._names);
        assertArrayEquals(new JavaType[]{type}, bindings._types);
        assertNull(bindings._unboundVariables);
        assertEquals(1 + type.hashCode(), bindings._hashCode);
        assertSame(type, bindings.findBoundType("T"));
    }

    @Test
    public void testCreateWithTwoTypes() {
        JavaType type1 = new java.util.ArrayList<java.util.ArrayList<java.lang.String>>().getType();
        JavaType type2 = new java.util.HashMap<java.util.ArrayList<java.lang.String>, java.util.ArrayList<java.lang.String>>().getType();
        TypeBindings bindings = TypeBindings.create(String[].class, type1, type2);
        assertArrayEquals(new String[]{"T1", "T2"}, bindings._names);
        assertArrayEquals(new JavaType[]{type1, type2}, bindings._types);
        assertNull(bindings._unboundVariables);
        assertEquals(1 + type1.hashCode() + type2.hashCode(), bindings._hashCode);
        assertSame(type1, bindings.findBoundType("T1"));
        assertSame(type2, bindings.findBoundType("T2"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateWithMismatchedNamesAndTypes() {
        JavaType type1 = new java.util.ArrayList<java.util.ArrayList<java.lang.String>>().getType();
        String[] names = new String[]{"T1", "T2", "T3"};
        TypeBindings bindings = new TypeBindings(names, new JavaType[]{type1}, null);
        throw new AssertionError("Should not reach this point");
    }

    @Test
    public void testCreateWithUnboundVariable() {
        JavaType type = new java.util.ArrayList<java.lang.String>().getType();
        TypeBindings bindings = TypeBindings.create(String[].class, type.getTypeParameters()[0]);
        assertArrayEquals(new String[]{"T", "UV"}, bindings._names);
        assertArrayEquals(new JavaType[]{type.getTypeParameters()[0], type}, bindings._types);
        assertArrayEquals(new String[]{"UV"}, bindings._unboundVariables);
        assertEquals(1 + type.getTypeParameters()[0].hashCode() + type.hashCode(), bindings._hashCode);
    }

    @Test
    public void testFindBoundType() {
        JavaType type1 = new java.util.ArrayList<java.util.ArrayList<java.lang.String>>().getType();
        JavaType type2 = new java.util.HashMap<java.util.ArrayList<java.lang.String>, java.util.ArrayList<java.lang.String>>().getType();
        TypeBindings bindings = TypeBindings.create(String[].class, type1, type2);
        assertSame(type1, bindings.findBoundType("T1"));
        assertSame(type2, bindings.findBoundType("T2"));
        assertNull(bindings.findBoundType("Unknown"));
    }

    @Test
    public void testIsEmpty() {
        TypeBindings bindings = TypeBindings.emptyBindings();
        assertTrue(bindings.isEmpty());
        TypeBindings nonEmpty = TypeBindings.create(String[].class, new Object().getClass().getTypeParameters()[0]);
        assertFalse(bindings.isEmpty());
    }

    @Test
    public void testSize() {
        TypeBindings bindings = TypeBindings.create(String[].class, new Object().getClass().getTypeParameters()[0]);
        assertEquals(1, bindings.size());
    }

    @Test
    public void testGetBoundName() {
        JavaType type = new java.util.ArrayList<java.util.ArrayList<java.lang.String>>().getType();
        TypeBindings bindings = TypeBindings.create(String[].class, type);
        assertEquals("T1", bindings.getBoundName(0));
        assertNull(bindings.getBoundName(1));
    }

    @Test
    public void testGetBoundType() {
        JavaType type1 = new java.util.ArrayList<java.util.ArrayList<java.lang.String>>().getType();
        JavaType type2 = new java.util.HashMap<java.util.ArrayList<java.lang.String>, java.util.ArrayList<java.lang.String>>().getType();
        TypeBindings bindings = TypeBindings.create(String[].class, type1, type2);
        assertSame(type1, bindings.getBoundType(0));
        assertSame(type2, bindings.getBoundType(1));
        assertNull(bindings.getBoundType(2));
    }

    @Test
    public void testGetTypeParameters() {
        JavaType type = new java.util.ArrayList<java.util.ArrayList<java.lang.String>>().getType();
        TypeBindings bindings = TypeBindings.create(String[].class, type);
        List<JavaType> typeParameters = bindings.getTypeParameters();
        assertSame(type, typeParameters.get(0));
        assertEquals(0, typeParameters.size() - 1);
    }

    @Test
    public void testHasUnbound() {
        JavaType type = new java.util.ArrayList<java.util.ArrayList<java.lang.String>>().getType();
        TypeBindings bindings = TypeBindings.create(String[].class, type);
        assertTrue(bindings.hasUnbound("T"));
        assertFalse(bindings.hasUnbound("Unknown"));
    }
}