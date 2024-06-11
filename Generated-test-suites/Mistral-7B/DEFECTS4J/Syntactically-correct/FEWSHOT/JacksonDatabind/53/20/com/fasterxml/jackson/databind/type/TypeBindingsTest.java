package com.fasterxml.jackson.databind.type;

import static org.junit.Assert.*;

import java.lang.reflect.TypeVariable;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;

public class TypeBindingsTest {

    @Test
    public void testCreateEmptyBindings() {
        TypeBindings bindings = TypeBindings.emptyBindings();
        assertEquals(0, bindings.size());
        assertNull(bindings.findBoundType(""));
    }

    @Test
    public void testCreateBindingsWithOneTypeParameter() {
        Class<List<Integer>> clazz = List.of(Integer.class).getClass();
        TypeBindings bindings = TypeBindings.create(clazz, null);
        assertEquals(1, bindings.size());
        assertEquals("E", bindings.getBoundName(0));
        assertEquals(Integer.class, bindings.getBoundType(0));
        assertNull(bindings.findBoundType(""));
    }

    @Test
    public void testCreateBindingsWithTwoTypeParameters() {
        Class<Map<String, List<Integer>>> clazz = Map.class.cast(
                Map.of(String.class, List.class).getClass());
        TypeBindings bindings = TypeBindings.create(clazz, null);
        assertEquals(2, bindings.size());
        assertEquals("K", bindings.getBoundName(0));
        assertEquals(String.class, bindings.getBoundType(0));
        assertEquals("V", bindings.getBoundName(1));
        assertEquals(List.class, bindings.getBoundType(1));
        assertNull(bindings.findBoundType(""));
    }

    @Test
    public void testCreateBindingsWithSpecifiedTypeParameters() {
        Class<Map<String, List<Integer>>> clazz = Map.class.cast(
                Map.of(String.class, List.class).getClass());
        JavaType[] typeParameters = new JavaType[] {
                TypeFactory.defaultInstance().constructType(String.class),
                TypeFactory.defaultInstance().constructType(List.class)
        };
        TypeBindings bindings = TypeBindings.create(clazz, typeParameters);
        assertEquals(2, bindings.size());
        assertEquals("K", bindings.getBoundName(0));
        assertEquals(String.class, bindings.getBoundType(0));
        assertEquals("V", bindings.getBoundName(1));
        assertEquals(List.class, bindings.getBoundType(1));
        assertNull(bindings.findBoundType(""));
    }

    @Test
    public void testFindBoundType() {
        Class<Map<String, List<Integer>>> clazz = Map.class.cast(
                Map.of(String.class, List.class).getClass());
        JavaType[] typeParameters = new JavaType[] {
                TypeFactory.defaultInstance().constructType(String.class),
                TypeFactory.defaultInstance().constructType(List.class)
        };
        TypeBindings bindings = TypeBindings.create(clazz, typeParameters);
        assertEquals(String.class, bindings.findBoundType("K"));
        assertEquals(List.class, bindings.findBoundType("V"));
        assertNull(bindings.findBoundType(""));
    }

    @Test
    public void testCreateBindingsWithUnresolvedTypeParameters() {
        Class<Map<String, List<Integer>>> clazz = Map.class.cast(
                Map.of(String.class, List.class).getClass());
        JavaType[] typeParameters = new JavaType[] {
                null,
                TypeFactory.defaultInstance().constructType(List.class)
        };
        TypeBindings bindings = TypeBindings.create(clazz, typeParameters);
        assertEquals(1, bindings.size());
        assertEquals(null, bindings.getBoundName(0));
        assertEquals(null, bindings.getBoundType(0));
        assertEquals(1, bindings.size() + bindings.getUnboundVariables().length);
        assertEquals("V", bindings.getUnboundVariables()[0]);
    }

    @Test
    public void testCreateBindingsWithUnresolvedTypeParametersAndBoundVariable() {
        Class<Map<String, List<Integer>>> clazz = Map.class.cast(
                Map.of(String.class, List.class).getClass());
        JavaType[] typeParameters = new JavaType[] {
                TypeFactory.defaultInstance().constructTypeVariable(String.class, "X"),
                TypeFactory.defaultInstance().constructType(List.class)
        };
        TypeBindings bindings = TypeBindings.create(clazz, typeParameters);
        assertEquals(1, bindings.size());
        assertEquals("X", bindings.getBoundName(0));
        assertEquals(null, bindings.getBoundType(0));
        assertEquals(1, bindings.size() + bindings.getUnboundVariables().length);
        assertEquals("V", bindings.getUnboundVariables()[0]);
    }
}