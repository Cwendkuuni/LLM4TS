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
        assertNull(bindings.findBoundType("test"));
        assertTrue(bindings.isEmpty());
    }

    @Test
    public void testCreateWithNoTypeArgs() {
        Class<?> clazz = List.class;
        TypeBindings bindings = TypeBindings.create(clazz, null);
        assertEquals(0, bindings.size());
        assertNull(bindings.findBoundType("List"));
        assertTrue(bindings.isEmpty());
    }

    @Test
    public void testCreateWithTypeArgs() {
        Class<?> clazz = List.class;
        List<JavaType> typeArgs = Arrays.asList(Integer.TYPE, String.class);
        TypeBindings bindings = TypeBindings.create(clazz, typeArgs);
        assertEquals(2, bindings.size());
        assertEquals("java.lang.Integer", bindings.getBoundName(0));
        assertEquals(Integer.TYPE, bindings.getBoundType(0));
        assertEquals("java.lang.String", bindings.getBoundName(1));
        assertEquals(String.class, bindings.getBoundType(1));
        assertNull(bindings.findBoundType("Unknown"));
    }

    @Test
    public void testCreateWithSingleTypeArg() {
        Class<?> clazz = List.class;
        JavaType typeArg = Integer.TYPE;
        TypeBindings bindings = TypeBindings.create(clazz, typeArg);
        assertEquals(1, bindings.size());
        assertEquals("java.lang.Integer", bindings.getBoundName(0));
        assertEquals(Integer.TYPE, bindings.getBoundType(0));
        assertNull(bindings.findBoundType("Unknown"));
    }

    @Test
    public void testCreateIfNeededWithNoTypeArgs() {
        Class<?> clazz = List.class;
        TypeBindings bindings = TypeBindings.createIfNeeded(clazz, null);
        assertEquals(0, bindings.size());
        assertTrue(bindings.isEmpty());
    }

    @Test
    public void testCreateIfNeededWithTypeArgs() {
        Class<?> clazz = List.class;
        List<JavaType> typeArgs = Arrays.asList(Integer.TYPE, String.class);
        TypeBindings bindings = TypeBindings.createIfNeeded(clazz, typeArgs);
        assertEquals(2, bindings.size());
        assertEquals("java.lang.Integer", bindings.getBoundName(0));
        assertEquals(Integer.TYPE, bindings.getBoundType(0));
        assertEquals("java.lang.String", bindings.getBoundName(1));
        assertEquals(String.class, bindings.getBoundType(1));
        assertNull(bindings.findBoundType("Unknown"));
    }

    @Test
    public void testCreateIfNeededWithSingleTypeArg() {
        Class<?> clazz = List.class;
        JavaType typeArg = Integer.TYPE;
        TypeBindings bindings = TypeBindings.createIfNeeded(clazz, typeArg);
        assertEquals(1, bindings.size());
        assertEquals("java.lang.Integer", bindings.getBoundName(0));
        assertEquals(Integer.TYPE, bindings.getBoundType(0));
        assertNull(bindings.findBoundType("Unknown"));
    }

    @Test
    public void testFindBoundType() {
        Class<?> clazz = List.class;
        List<JavaType> typeArgs = Arrays.asList(Integer.TYPE, String.class);
        TypeBindings bindings = TypeBindings.create(clazz, typeArgs);
        assertEquals("java.lang.Integer", bindings.getBoundName(0));
        assertEquals("java.lang.String", bindings.getBoundName(1));
        assertEquals(Integer.TYPE, bindings.findBoundType("java.lang.Integer"));
        assertEquals(String.class, bindings.findBoundType("java.lang.String"));
        assertNull(bindings.findBoundType("Unknown"));
    }

    @Test
    public void testHasUnbound() {
        Class<?> clazz = List.class;
        List<JavaType> typeArgs = Arrays.asList(Integer.TYPE, String.class);
        TypeBindings bindings = TypeBindings.create(clazz, typeArgs);
        assertFalse(bindings.hasUnbound("java.lang.Integer"));
        assertFalse(bindings.hasUnbound("java.lang.String"));
        assertFalse(bindings.hasUnbound("Unknown"));
        TypeBindings bindingsWithUnbound = bindings.withUnboundVariable("X");
        assertFalse(bindingsWithUnbound.hasUnbound("java.lang.Integer"));
        assertFalse(bindingsWithUnbound.hasUnbound("java.lang.String"));
        assertTrue(bindingsWithUnbound.hasUnbound("X"));
    }

}