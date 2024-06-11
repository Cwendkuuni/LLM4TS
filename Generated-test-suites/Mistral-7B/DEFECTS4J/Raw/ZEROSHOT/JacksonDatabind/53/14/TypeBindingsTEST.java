package com.fasterxml.jackson.databind.type;

import static org.junit.Assert.*;

import java.lang.reflect.TypeVariable;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;

import com.fasterxml.jackson.databind.JavaType;

public class TypeBindingsTest {

    @Test
    public void testEmptyBindings() {
        TypeBindings bindings = TypeBindings.emptyBindings();
        assertEquals(0, bindings.size());
        assertNull(bindings.findBoundType("foo"));
    }

    @Test
    public void testCreateBindings() {
        Class<List<Integer>> listIntType = List.of(Integer.class).getType();
        JavaType[] types = new JavaType[] { JavaType.constructParametricType(List.class, Integer.class) };
        TypeBindings bindings = TypeBindings.create(listIntType, types);
        assertEquals(1, bindings.size());
        assertEquals("java.lang.Integer", bindings.getBoundName(0));
        assertEquals(types[0], bindings.getBoundType(0));
        assertNull(bindings.findBoundType("foo"));
    }

    @Test
    public void testCreateBindingsWithUnboundVariable() {
        Class<List<Integer>> listIntType = List.of(Integer.class).getType();
        JavaType[] types = new JavaType[] {};
        TypeBindings bindings = TypeBindings.create(listIntType, types);
        assertEquals(0, bindings.size());
        TypeBindings bindingsWithUnbound = bindings.withUnboundVariable("T");
        assertEquals(1, bindingsWithUnbound.size());
        assertEquals("T", bindingsWithUnbound.getBoundName(0));
        assertNull(bindingsWithUnbound.findBoundType("T"));
        assertNull(bindingsWithUnbound.findBoundType("java.lang.Integer"));
    }

    @Test
    public void testFindBoundType() {
        Class<List<Integer>> listIntType = List.of(Integer.class).getType();
        JavaType[] types = new JavaType[] { JavaType.constructParametricType(List.class, Integer.class) };
        TypeBindings bindings = TypeBindings.create(listIntType, types);
        assertEquals(1, bindings.size());
        JavaType boundType = bindings.findBoundType("java.lang.Integer");
        assertTrue(boundType instanceof ClassType);
        assertEquals(Integer.class, boundType.getRawClass());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateWithIncompatibleTypes() {
        Class<List<String>> listStringType = List.of(String.class).getType();
        JavaType[] types = new JavaType[] { JavaType.constructParametricType(List.class, Integer.class) };
        TypeBindings.create(listStringType, types);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateWithWrongNumberOfTypes() {
        Class<List<Integer>> listIntType = List.of(Integer.class).getType();
        JavaType[] types = new JavaType[] { JavaType.constructParametricType(List.class, Integer.class) };
        TypeBindings.create(listIntType, types, JavaType.constructParametricType(List.class, String.class));
    }

    @Test
    public void testCreateIfNeeded() {
        Class<List<Integer>> listIntType = List.of(Integer.class).getType();
        JavaType[] types = new JavaType[] {};
        TypeBindings bindings = TypeBindings.createIfNeeded(listIntType, JavaType.constructParametricType(List.class, Integer.class));
        assertEquals(1, bindings.size());
        assertEquals("java.lang.Integer", bindings.getBoundName(0));
        assertEquals(types[0], bindings.getBoundType(0));
        assertNull(bindings.findBoundType("foo"));
    }

    @Test
    public void testCreateIfNeededWithEmptyTypes() {
        Class<List<Integer>> listIntType = List.of(Integer.class).getType();
        TypeBindings bindings = TypeBindings.createIfNeeded(listIntType, null);
        assertEquals(0, bindings.size());
        assertNull(bindings.findBoundType("foo"));
    }

    @Test
    public void testToString() {
        Class<List<Integer>> listIntType = List.of(Integer.class).getType();
        JavaType[] types = new JavaType[] { JavaType.constructParametricType(List.class, Integer.class) };
        TypeBindings bindings = TypeBindings.create(listIntType, types);
        assertEquals("<java.lang.Integer>", bindings.toString());
    }

    @Test
    public void testHasUnbound() {
        Class<List<Integer>> listIntType = List.of(Integer.class).getType();
        JavaType[] types = new JavaType[] {};
        TypeBindings bindings = TypeBindings.createIfNeeded(listIntType, types);
        assertFalse(bindings.hasUnbound("java.lang.Integer"));
        assertTrue(bindings.hasUnbound("T"));
    }
}