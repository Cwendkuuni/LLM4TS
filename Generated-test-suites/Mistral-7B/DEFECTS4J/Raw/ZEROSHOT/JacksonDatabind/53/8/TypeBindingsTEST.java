package com.fasterxml.jackson.databind.type;

import static org.junit.Assert.*;

import java.lang.reflect.TypeVariable;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.TypeBindings;

public class TypeBindingsTest {

    @Test
    public void testEmptyBindings() {
        TypeBindings bindings = TypeBindings.emptyBindings();
        assertEquals(0, bindings.size());
        assertNull(bindings.findBoundType(""));
        assertTrue(bindings.isEmpty());
    }

    @Test
    public void testCreateEmptyListBindings() {
        Class<?> emptyListClass = List.class;
        TypeBindings bindings = TypeBindings.create(emptyListClass, null);
        assertEquals(0, bindings.size());
        assertNull(bindings.findBoundType(""));
        assertTrue(bindings.isEmpty());
    }

    @Test
    public void testCreateListBindings() {
        Class<?> listClass = List.class;
        TypeVariable<?>[] typeParameters = listClass.getTypeParameters();
        JavaType listType = TypeFactory.defaultInstance().constructType(List.class, Integer.class);
        TypeBindings bindings = TypeBindings.create(listClass, listType);
        assertEquals(1, bindings.size());
        assertEquals("E", bindings.getBoundName(0));
        assertEquals(Integer.class, bindings.getBoundType(0).getRawClass());
        assertEquals(Arrays.asList(new JavaType[] { listType }), bindings.getTypeParameters());
        assertFalse(bindings.hasUnbound(""));
    }

    @Test
    public void testCreateMapBindings() {
        Class<?> mapClass = Map.class;
        TypeVariable<?>[] typeParameters = mapClass.getTypeParameters();
        JavaType mapType = TypeFactory.defaultInstance().constructType(Map.class, String.class, Integer.class);
        TypeBindings bindings = TypeBindings.create(mapClass, mapType);
        assertEquals(2, bindings.size());
        assertEquals("K", bindings.getBoundName(0));
        assertEquals(String.class, bindings.getBoundType(0).getRawClass());
        assertEquals("V", bindings.getBoundName(1));
        assertEquals(Integer.class, bindings.getBoundType(1).getRawClass());
        assertEquals(Arrays.asList(new JavaType[] { mapType }), bindings.getTypeParameters());
        assertFalse(bindings.hasUnbound(""));
    }

    @Test
    public void testCreateBindingsWithUnboundVariable() {
        Class<?> listClass = List.class;
        TypeVariable<?>[] typeParameters = listClass.getTypeParameters();
        JavaType listType = TypeFactory.defaultInstance().constructType(List.class, Integer.class);
        TypeBindings bindings = TypeBindings.create(listClass, listType);
        TypeBindings unboundBindings = bindings.withUnboundVariable("X");
        assertEquals(1, unboundBindings.size());
        assertEquals("E", unboundBindings.getBoundName(0));
        assertEquals(Integer.class, unboundBindings.getBoundType(0).getRawClass());
        assertEquals(1, unboundBindings.size() + bindings.size());
        assertTrue(unboundBindings.hasUnbound("X"));
        assertFalse(unboundBindings.hasUnbound(""));
    }
}