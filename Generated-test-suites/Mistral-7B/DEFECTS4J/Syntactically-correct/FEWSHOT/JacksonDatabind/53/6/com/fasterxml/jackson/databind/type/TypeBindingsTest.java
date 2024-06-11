package com.fasterxml.jackson.databind.type;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertEquals;

import java.lang.reflect.TypeVariable;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.Test;

public class TypeBindingsTest {

    @Test
    public void testCreateEmptyBindings() {
        TypeBindings bindings = TypeBindings.emptyBindings();
        assertEquals(0, bindings.size());
        assertNull(bindings.findBoundType("T"));
    }

    @Test
    public void testCreateSimpleBindings() {
        Class<List<String>> type = List.class;
        TypeVariable<String> stringTypeVariable = type.getTypeParameters()[0];

        TypeBindings bindings = TypeBindings.create(type, Arrays.asList(String.class));
        assertEquals(1, bindings.size());
        assertEquals("T", bindings.getBoundName(0));
        assertEquals(String.class, bindings.getBoundType(0));

        assertNull(bindings.findBoundType(null));
        assertNull(bindings.findBoundType(""));
        assertNull(bindings.findBoundType("U"));

        JavaType boundType = bindings.findBoundType("T");
        assertSame(String.class, boundType);
    }

    @Test
    public void testCreateMultipleBindings() {
        Class<Map<String, List<Integer>>> mapType = Map.class;
        TypeVariable<String> keyTypeVariable = mapType.getTypeParameters()[0];
        TypeVariable<List<Integer>> valueTypeVariable = mapType.getTypeParameters()[1];

        TypeBindings bindings = TypeBindings.create(mapType, Arrays.asList(String.class, List.class, Integer.class));
        assertEquals(3, bindings.size());
        assertEquals("K", bindings.getBoundName(0));
        assertEquals(String.class, bindings.getBoundType(0));

        assertEquals("V", bindings.getBoundName(1));
        assertEquals(List.class, bindings.getBoundType(1));

        assertEquals("E", bindings.getBoundName(2));
        assertEquals(Integer.class, bindings.getBoundType(2));

        assertNull(bindings.findBoundType(null));
        assertNull(bindings.findBoundType(""));
        assertNull(bindings.findBoundType("W"));

        JavaType keyBoundType = bindings.findBoundType("K");
        assertEquals(String.class, keyBoundType);

        JavaType valueBoundType = bindings.findBoundType("V");
        assertEquals(List.class, valueBoundType);

        JavaType elementBoundType = bindings.findBoundType("E");
        assertEquals(Integer.class, elementBoundType);
    }

    @Test
    public void testCreateIfNeeded() {
        Class<List<Integer>> listType = List.class;

        TypeBindings bindings = TypeBindings.createIfNeeded(listType, null);
        assertEquals(0, bindings.size());

        bindings = TypeBindings.createIfNeeded(listType, Arrays.asList(Integer.class));
        assertEquals(1, bindings.size());
        assertEquals("T", bindings.getBoundName(0));
        assertEquals(Integer.class, bindings.getBoundType(0));
    }

    @Test
    public void testCreateIfNeededWithTypes() {
        Class<List<String>> listType = List.class;

        TypeBindings bindings = TypeBindings.createIfNeeded(listType, Arrays.asList(String.class));
        assertEquals(1, bindings.size());
        assertEquals("T", bindings.getBoundName(0));
        assertEquals(String.class, bindings.getBoundType(0));
    }

    @Test
    public void testCreateUnboundVariable() {
        Class<List<String>> listType = List.class;

        TypeBindings bindings = TypeBindings.create(listType, Arrays.asList(String.class));
        assertNull(bindings.hasUnbound("U"));

        TypeBindings bindingsWithUnbound = bindings.withUnboundVariable("U");
        assertTrue(bindingsWithUnbound.hasUnbound("U"));
    }

    @Test
    public void testFindBoundTypeWithUnboundVariable() {
        Class<List<String>> listType = List.class;

        String[] unboundVariableNames = {"U"};
        TypeBindings bindingsWithUnbound = new TypeBindings(new String[0], new JavaType[0], unboundVariableNames);

        JavaType boundType = bindingsWithUnbound.findBoundType("T");
        assertNull(boundType);

        boundType = bindingsWithUnbound.findBoundType("U");
        assertNull(boundType);

        bindingsWithUnbound = bindingsWithUnbound.withUnboundVariable("T");

        boundType = bindingsWithUnbound.findBoundType("T");
        assertNull(boundType);

        bindingsWithUnbound = bindingsWithUnbound.withUnboundVariable("T");

        boundType = bindingsWithUnbound.findBoundType("T");
        assertNull(boundType);

        bindingsWithUnbound = bindingsWithUnbound.withUnboundVariable("U");

        boundType = bindingsWithUnbound.findBoundType("T");
        assertNull(boundType);

        boundType = bindingsWithUnbound.findBoundType("U");
        assertNull(boundType);

        bindingsWithUnbound = bindingsWithUnbound.withUnboundVariable("T");

        boundType = bindingsWithUnbound.findBoundType("T");
        assertNull(boundType);

        boundType = bindingsWithUnbound.findBoundType("U");
        assertNull(boundType);

        bindingsWithUnbound = bindingsWithUnbound.withUnboundVariable("T");

        boundType = bindingsWithUnbound.findBoundType("T");
        assertNull(boundType);

        bindingsWithUnbound = bindingsWithUnbound.withUnboundVariable("U");

        boundType = bindingsWithUnbound.findBoundType("T");
        assertEquals(Object.class, boundType);
    }
}