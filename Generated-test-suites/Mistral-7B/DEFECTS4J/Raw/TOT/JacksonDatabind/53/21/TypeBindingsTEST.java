package com.example.test;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.TypeBindings;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Tests for {@link TypeBindings} class.
 */
public class TypeBindingsTest {

    @Test
    public void testEmptyBindings() {
        TypeBindings bindings = TypeBindings.emptyBindings();
        assertTrue(bindings.isEmpty());
        assertEquals(0, bindings.size());
    }

    @Test
    public void testCreateWithNoTypeParameters() {
        Class<?> erasedType = String.class;
        TypeBindings bindings = TypeBindings.create(erasedType, null);
        assertFalse(bindings.isEmpty());
        assertEquals(0, bindings.size());
    }

    @Test
    public void testCreateWithOneTypeParameter() {
        Class<?> erasedType = ArrayList.class;
        List<JavaType> typeList = Arrays.asList(Integer.TYPE);
        TypeBindings bindings = TypeBindings.create(erasedType, typeList);
        assertFalse(bindings.isEmpty());
        assertEquals(1, bindings.size());
        assertEquals("E", bindings.getBoundName(0));
        assertEquals(Integer.TYPE, bindings.getBoundType(0));
    }

    @Test
    public void testCreateWithTwoTypeParameters() {
        Class<?> erasedType = Map.class;
        List<JavaType> typeList = Arrays.asList(List.class, Integer.TYPE);
        TypeBindings bindings = TypeBindings.create(erasedType, typeList);
        assertFalse(bindings.isEmpty());
        assertEquals(2, bindings.size());
        assertEquals("K", bindings.getBoundName(0));
        assertEquals(List.class, bindings.getBoundType(0));
        assertEquals("V", bindings.getBoundName(1));
        assertEquals(Integer.TYPE, bindings.getBoundType(1));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateWithMoreThanTwoTypeParameters() {
        Class<?> erasedType = Map.class;
        List<JavaType> typeList = Arrays.asList(List.class, Integer.TYPE, Boolean.TYPE);
        TypeBindings.create(erasedType, typeList);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateWithMismatchedNumberOfTypeParametersAndNames() {
        Class<?> erasedType = Map.class;
        List<JavaType> typeList = Arrays.asList(List.class, Integer.TYPE, Boolean.TYPE);
        TypeBindings.create(erasedType, new String[]{"K", "V", "U"});
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateWithMismatchedTypes() {
        Class<?> erasedType = Map.class;
        List<JavaType> typeList = Arrays.asList(List.class, Boolean.TYPE, Integer.TYPE);
        TypeBindings.create(erasedType, typeList);
    }

    @Test
    public void testWithUnboundVariable() {
        Class<?> erasedType = Map.class;
        List<JavaType> typeList = Arrays.asList(List.class, Integer.TYPE);
        TypeBindings bindings = TypeBindings.create(erasedType, typeList);
        TypeBindings bindingsWithUnboundVariable = bindings.withUnboundVariable("U");
        assertTrue(bindingsWithUnboundVariable.hasUnbound("U"));
    }

    @Test
    public void testCreateIfNeededWithNoTypeParameters() {
        Class<?> erasedType = String.class;
        TypeBindings bindings = TypeBindings.createIfNeeded(erasedType, null);
        assertTrue(bindings.isEmpty());
    }

    @Test
    public void testCreateIfNeededWithOneTypeParameter() {
        Class<?> erasedType = ArrayList.class;
        JavaType typeArg1 = Integer.TYPE;
        TypeBindings bindings = TypeBindings.createIfNeeded(erasedType, typeArg1);
        assertFalse(bindings.isEmpty());
        assertEquals(1, bindings.size());
        assertEquals("E", bindings.getBoundName(0));
        assertEquals(Integer.TYPE, bindings.getBoundType(0));
    }

    @Test
    public void testCreateIfNeededWithTwoTypeParameters() {
        Class<?> erasedType = Map.class;
        JavaType typeArg1 = List.class;
        JavaType typeArg2 = Integer.TYPE;
        TypeBindings bindings = TypeBindings.createIfNeeded(erasedType, new JavaType[]{typeArg1, typeArg2});
        assertFalse(bindings.isEmpty());
        assertEquals(2, bindings.size());
        assertEquals("K", bindings.getBoundName(0));
        assertEquals(List.class, bindings.getBoundType(0));
        assertEquals("V", bindings.getBoundName(1));
        assertEquals(Integer.TYPE, bindings.getBoundType(1));
    }
}