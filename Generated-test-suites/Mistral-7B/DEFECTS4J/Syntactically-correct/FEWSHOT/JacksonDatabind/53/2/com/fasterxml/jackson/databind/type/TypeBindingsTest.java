package com.fasterxml.jackson.databind.type;

import static org.junit.Assert.*;

import java.lang.reflect.TypeVariable;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

public class TypeBindingsTest {

    @Test
    public void testCreateEmptyBindings() {
        TypeBindings bindings = TypeBindings.emptyBindings();
        assertFalse(bindings.hasUnboundVariables());
        assertEquals(0, bindings.size());
        assertTrue(bindings.isEmpty());
    }

    @Test
    public void testCreateBindingsForClassWithOneTypeParameter() {
        Class<List<Integer>> clazz = ArrayList::new;
        TypeBindings bindings = TypeBindings.create(clazz, null);

        assertEquals(1, bindings.size());
        String boundName = bindings.getBoundName(0);
        assertEquals("E", boundName);
        JavaType boundType = bindings.getBoundType(0);
        assertInstanceOf(ArrayList.class, boundType.getType());
        assertEquals(Integer.class, boundType.getRawType());
    }

    @Test
    public void testCreateBindingsForClassWithTwoTypeParameters() {
        Class<Map<Integer, List<String>>> clazz = HashMap::new;
        TypeBindings bindings = TypeBindings.create(clazz, null);

        assertEquals(2, bindings.size());
        String boundName1 = bindings.getBoundName(0);
        assertEquals("K", boundName1);
        JavaType boundType1 = bindings.getBoundType(0);
        assertInstanceOf(Integer.class, boundType1.getType());
        assertEquals(Integer.class, boundType1.getRawType());

        String boundName2 = bindings.getBoundName(1);
        assertEquals("V", boundName2);
        JavaType boundType2 = bindings.getBoundType(1);
        assertInstanceOf(List.class, boundType2.getType());
        assertEquals(String.class, boundType2.getRawType());
    }

    @Test
    public void testCreateBindingsForCollectionClassWithTypeArgument() {
        Class<List<Integer>> clazz = ArrayList.class;
        TypeBindings bindings = TypeBindings.createIfNeeded(clazz, Integer.TYPE);

        assertEquals(1, bindings.size());
        String boundName = bindings.getBoundName(0);
        assertEquals("E", boundName);
        JavaType boundType = bindings.getBoundType(0);
        assertInstanceOf(ArrayList.class, boundType.getType());
        assertEquals(Integer.class, boundType.getRawType());
    }

    @Test
    public void testCreateBindingsForMapClassWithTypeArguments() {
        Class<Map<Integer, List<String>>> clazz = HashMap.class;
        TypeBindings bindings = TypeBindings.createIfNeeded(clazz, Integer.TYPE, List.class);

        assertEquals(2, bindings.size());
        String boundName1 = bindings.getBoundName(0);
        assertEquals("K", boundName1);
        JavaType boundType1 = bindings.getBoundType(0);
        assertInstanceOf(Integer.class, boundType1.getType());
        assertEquals(Integer.class, boundType1.getRawType());

        String boundName2 = bindings.getBoundName(1);
        assertEquals("V", boundName2);
        JavaType boundType2 = bindings.getBoundType(1);
        assertInstanceOf(List.class, boundType2.getType());
        assertEquals(String.class, boundType2.getRawType());
    }

    @Test
    public void testFindBoundType() {
        Class<Map<Integer, List<String>>> clazz = HashMap.class;
        TypeBindings bindings = TypeBindings.create(clazz, null);

        JavaType boundType1 = bindings.findBoundType("K");
        assertNotNull(boundType1);
        assertEquals(Integer.class, boundType1.getType());
        assertEquals(Integer.class, boundType1.getRawType());

        JavaType boundType2 = bindings.findBoundType("V");
        assertNotNull(boundType2);
        assertInstanceOf(List.class, boundType2.getType());
        assertEquals(String.class, boundType2.getRawType());

        JavaType boundType3 = bindings.findBoundType("X");
        assertNull(boundType3);
    }

    @Test
    public void testWithUnboundVariable() {
        Class<Map<Integer, List<String>>> clazz = HashMap.class;
        TypeBindings bindings = TypeBindings.create(clazz, null);

        TypeBindings bindingsWithUnboundVariable = bindings.withUnboundVariable("Z");
        String[] unboundVariables = bindingsWithUnboundVariable.getUnboundVariables();
        assertEquals(1, unboundVariables.length);
        assertEquals("Z", unboundVariables[0]);
    }
}