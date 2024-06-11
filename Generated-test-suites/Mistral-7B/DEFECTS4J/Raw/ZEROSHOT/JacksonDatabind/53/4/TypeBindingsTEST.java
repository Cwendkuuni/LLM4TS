package com.fasterxml.jackson.databind.type;

import static org.junit.Assert.*;

import java.lang.reflect.TypeVariable;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.Test;

import com.fasterxml.jackson.databind.JavaType;

public class TypeBindingsTest {

    @Test
    public void testCreateEmpty() {
        TypeBindings bindings = TypeBindings.emptyBindings();
        assertEquals(0, bindings.size());
        assertTrue(bindings.isEmpty());
    }

    @Test
    public void testCreateWithNoArgs() {
        Class<?> erasedType = String[].class;
        TypeBindings bindings = TypeBindings.create(erasedType, null);
        assertEquals(0, bindings.size());
        assertTrue(bindings.isEmpty());
    }

    @Test
    public void testCreateWithOneArg() {
        Class<?> erasedType = List.class;
        JavaType javaType = JavaType.constructUnresolved(int[].class);
        TypeBindings bindings = TypeBindings.create(erasedType, javaType);
        assertEquals(1, bindings.size());
        assertEquals("java.lang.Integer", bindings.getBoundName(0));
        assertEquals(javaType, bindings.getBoundType(0));
        assertFalse(bindings.hasUnbound("java.lang.Integer"));
        assertFalse(bindings.hasUnbound("java.lang.String"));
    }

    @Test
    public void testCreateWithTwoArgs() {
        Class<?> erasedType = Map.class;
        JavaType javaType1 = JavaType.constructUnresolved(String.class);
        JavaType javaType2 = JavaType.constructUnresolved(Integer.class);
        TypeBindings bindings = TypeBindings.create(erasedType, javaType1, javaType2);
        assertEquals(2, bindings.size());
        assertEquals("java.lang.String", bindings.getBoundName(0));
        assertEquals(javaType1, bindings.getBoundType(0));
        assertEquals("java.lang.Integer", bindings.getBoundName(1));
        assertEquals(javaType2, bindings.getBoundType(1));
        assertFalse(bindings.hasUnbound("java.lang.String"));
        assertFalse(bindings.hasUnbound("java.lang.Integer"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateWithMismatchingArguments() {
        Class<?> erasedType = Map.class;
        JavaType javaType1 = JavaType.constructUnresolved(String[].class);
        JavaType javaType2 = JavaType.constructUnresolved(Integer.class);
        TypeBindings bindings = TypeBindings.create(erasedType, javaType1, javaType2);
        fail("Expected IllegalArgumentException");
    }

    @Test
    public void testCreateIfNeededWithOneArg() {
        Class<?> erasedType = List.class;
        JavaType javaType = JavaType.constructUnresolved(Integer.class);
        TypeBindings bindings = TypeBindings.createIfNeeded(erasedType, javaType);
        assertEquals(1, bindings.size());
        assertEquals("java.lang.Integer", bindings.getBoundName(0));
        assertEquals(javaType, bindings.getBoundType(0));
        assertFalse(bindings.hasUnbound("java.lang.Integer"));
        assertFalse(bindings.hasUnbound("java.lang.String"));
    }

    @Test
    public void testCreateIfNeededWithTwoArgs() {
        Class<?> erasedType = Map.class;
        JavaType javaType1 = JavaType.constructUnresolved(String.class);
        JavaType javaType2 = JavaType.constructUnresolved(Integer.class);
        TypeBindings bindings = TypeBindings.createIfNeeded(erasedType, javaType1, javaType2);
        assertEquals(2, bindings.size());
        assertEquals("java.lang.String", bindings.getBoundName(0));
        assertEquals(javaType1, bindings.getBoundType(0));
        assertEquals("java.lang.Integer", bindings.getBoundName(1));
        assertEquals(javaType2, bindings.getBoundType(1));
        assertFalse(bindings.hasUnbound("java.lang.String"));
        assertFalse(bindings.hasUnbound("java.lang.Integer"));
    }

    @Test
    public void testCreateIfNeededWithNoArgs() {
        Class<?> erasedType = String[].class;
        TypeBindings bindings = TypeBindings.createIfNeeded(erasedType, null);
        assertEquals(0, bindings.size());
        assertTrue(bindings.isEmpty());
    }

    @Test
    public void testFindBoundType() {
        Class<?> erasedType = Map.class;
        JavaType javaType1 = JavaType.constructUnresolved(String.class);
        JavaType javaType2 = JavaType.constructUnresolved(Integer.class);
        TypeBindings bindings = TypeBindings.create(erasedType, javaType1, javaType2);
        assertEquals(javaType1, bindings.findBoundType("java.lang.String"));
        assertEquals(javaType2, bindings.findBoundType("java.lang.Integer"));
        assertNull(bindings.findBoundType("java.lang.Object"));
        assertNull(bindings.findBoundType("java.util.List"));
    }

    @Test
    public void testIsEmpty() {
        TypeBindings bindings = TypeBindings.emptyBindings();
        assertTrue(bindings.isEmpty());

        Class<?> erasedType = String[].class;
        JavaType javaType = JavaType.constructUnresolved(Integer.class);
        TypeBindings bindings2 = TypeBindings.create(erasedType, javaType);
        assertFalse(bindings2.isEmpty());
    }

    @Test
    public void testSize() {
        TypeBindings bindings = TypeBindings.emptyBindings();
        assertEquals(0, bindings.size());

        Class<?> erasedType = String[].class;
        JavaType javaType = JavaType.constructUnresolved(Integer.class);
        TypeBindings bindings2 = TypeBindings.create(erasedType, javaType);
        assertEquals(1, bindings2.size());
    }

    @Test
    public void testGetBoundName() {
        Class<?> erasedType = Map.class;
        JavaType javaType1 = JavaType.constructUnresolved(String.class);
        JavaType javaType2 = JavaType.constructUnresolved(Integer.class);
        TypeBindings bindings = TypeBindings.create(erasedType, javaType1, javaType2);
        assertEquals("java.lang.String", bindings.getBoundName(0));
        assertEquals("java.lang.Integer", bindings.getBoundName(1));
        assertNull(bindings.getBoundName(2));
    }

    @Test
    public void testGetBoundType() {
        Class<?> erasedType = Map.class;
        JavaType javaType1 = JavaType.constructUnresolved(String.class);
        JavaType javaType2 = JavaType.constructUnresolved(Integer.class);
        TypeBindings bindings = TypeBindings.create(erasedType, javaType1, javaType2);
        assertEquals(javaType1, bindings.getBoundType(0));
        assertEquals(javaType2, bindings.getBoundType(1));
        assertNull(bindings.getBoundType(2));
    }

    @Test
    public void testGetTypeParameters() {
        Class<?> erasedType = Map.class;
        JavaType javaType1 = JavaType.constructUnresolved(String.class);
        JavaType javaType2 = JavaType.constructUnresolved(Integer.class);
        TypeBindings bindings = TypeBindings.create(erasedType, javaType1, javaType2);
        List<JavaType> javaTypes = bindings.getTypeParameters();
        assertEquals(2, javaTypes.size());
        assertEquals(javaType1, javaTypes.get(0));
        assertEquals(javaType2, javaTypes.get(1));
    }

    @Test
    public void testHasUnbound() {
        Class<?> erasedType = Map.class;
        JavaType javaType1 = JavaType.constructUnresolved(String.class);
        JavaType javaType2 = JavaType.constructUnresolved(Integer.class);
        TypeBindings bindings = TypeBindings.create(erasedType, javaType1, javaType2);
        assertFalse(bindings.hasUnbound("java.lang.String"));
        assertFalse(bindings.hasUnbound("java.lang.Integer"));

        TypeBindings bindingsWithUnbound = bindings.withUnboundVariable("java.lang.String");
        assertTrue(bindingsWithUnbound.hasUnbound("java.lang.String"));
        assertFalse(bindingsWithUnbound.hasUnbound("java.lang.Integer"));
    }

}