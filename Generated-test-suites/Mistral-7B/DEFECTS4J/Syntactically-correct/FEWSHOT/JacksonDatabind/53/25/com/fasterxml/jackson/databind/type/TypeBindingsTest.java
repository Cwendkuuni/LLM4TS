package com.fasterxml.jackson.databind.type;

import static org.junit.Assert.*;

import java.lang.reflect.TypeVariable;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.Test;

public class TypeBindingsTest {

    @Test
    public void testEmptyBindings() {
        TypeBindings bindings = TypeBindings.emptyBindings();
        assertTrue(bindings.isEmpty());
        assertEquals(0, bindings.size());
        assertNull(bindings.findBoundType(null));
    }

    @Test
    public void testCreateForSingleTypeParameter() {
        Class<?> erasedType = List.class;
        JavaType typeArg1 = Integer.TYPE;
        TypeBindings bindings = TypeBindings.create(erasedType, typeArg1);
        assertEquals(1, bindings.size());
        assertEquals("T", bindings.getBoundName(0));
        assertEquals(Integer.TYPE, bindings.getBoundType(0));
        assertNull(bindings.findBoundType(null));
    }

    @Test
    public void testCreateForTwoTypeParameters() {
        Class<?> erasedType = Map.class;
        JavaType typeArg1 = Integer.TYPE;
        JavaType typeArg2 = String.class;
        TypeBindings bindings = TypeBindings.create(erasedType, typeArg1, typeArg2);
        assertEquals(2, bindings.size());
        assertEquals("K", bindings.getBoundName(0));
        assertEquals(Integer.TYPE, bindings.getBoundType(0));
        assertEquals("V", bindings.getBoundName(1));
        assertEquals(String.class, bindings.getBoundType(1));
        assertNull(bindings.findBoundType(null));
    }

    @Test
    public void testCreateForMultipleUnboundVariables() {
        Class<?> erasedType = Map.class;
        List<JavaType> typeList = Arrays.asList(Integer.TYPE, String.class, Double.TYPE);
        TypeBindings bindings = TypeBindings.create(erasedType, typeList);
        assertEquals(3, bindings.size());
        assertEquals("K", bindings.getBoundName(0));
        assertEquals(Integer.TYPE, bindings.getBoundType(0));
        assertEquals("V", bindings.getBoundName(1));
        assertEquals(String.class, bindings.getBoundType(1));
        assertEquals("E", bindings.getBoundName(2));
        assertEquals(Double.TYPE, bindings.getBoundType(2));
        assertNull(bindings.findBoundType(null));
    }

    @Test
    public void testFindBoundType() {
        Class<?> erasedType = Map.class;
        JavaType typeArg1 = Integer.TYPE;
        JavaType typeArg2 = String.class;
        TypeBindings bindings = TypeBindings.create(erasedType, typeArg1, typeArg2);
        assertEquals(Integer.TYPE, bindings.findBoundType("K"));
        assertEquals(String.class, bindings.findBoundType("V"));
        assertNull(bindings.findBoundType("E"));
        assertNull(bindings.findBoundType("U"));
    }

    @Test
    public void testHasUnboundVariable() {
        Class<?> erasedType = Map.class;
        JavaType typeArg1 = Integer.TYPE;
        JavaType typeArg2 = String.class;
        TypeBindings bindings = TypeBindings.create(erasedType, typeArg1, typeArg2);
        assertFalse(bindings.hasUnboundVariable(null));
        assertFalse(bindings.hasUnboundVariable("K"));
        assertFalse(bindings.hasUnboundVariable("V"));
        assertTrue(bindings.hasUnboundVariable("E"));
    }

    @Test
    public void testCreateWithUnboundVariable() {
        Class<?> erasedType = Map.class;
        JavaType typeArg1 = Integer.TYPE;
        JavaType typeArg2 = String.class;
        String unboundVariable = "U";
        TypeBindings bindings = bindings.withUnboundVariable(unboundVariable);
        assertEquals(3, bindings.size());
        assertEquals("K", bindings.getBoundName(0));
        assertEquals(Integer.TYPE, bindings.getBoundType(0));
        assertEquals("V", bindings.getBoundName(1));
        assertEquals(String.class, bindings.getBoundType(1));
        assertEquals("E", bindings.getBoundName(2));
        assertEquals(Double.TYPE, bindings.getBoundType(2));
        assertEquals("U", bindings.getBoundName(3));
        assertNull(bindings.findBoundType(null));
    }
}