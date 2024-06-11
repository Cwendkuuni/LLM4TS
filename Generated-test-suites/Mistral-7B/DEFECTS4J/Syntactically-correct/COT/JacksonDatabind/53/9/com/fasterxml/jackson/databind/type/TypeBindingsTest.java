package com.fasterxml.jackson.databind.type;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.lang.reflect.TypeVariable;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.Test;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.TypeBindings;

public class TypeBindingsTest {

    @Test
    public void testEmptyBindings() {
        TypeBindings bindings = TypeBindings.emptyBindings();
        assertTrue(bindings.isEmpty());
        assertEquals(0, bindings.size());
    }

    @Test
    public void testCreateEmptyType() {
        Class<?> clazz = Object.class;
        TypeBindings bindings = TypeBindings.create(clazz, null);
        assertTrue(bindings.isEmpty());
        assertEquals(0, bindings.size());
    }

    @Test
    public void testCreateTypeWithOneTypeParameter() {
        Class<List<Integer>> clazz = Collections.class.asSubclass(List.class);
        TypeVariable<?> typeVariable = clazz.getTypeParameters()[0];
        JavaType javaType = typeVariable.getGenericType();
        TypeBindings bindings = TypeBindings.create(clazz, javaType);
        assertFalse(bindings.isEmpty());
        assertEquals(1, bindings.size());
        assertSame(javaType, bindings.getBoundType(0));
    }

    @Test
    public void testCreateTypeWithTwoTypeParameters() {
        Class<Map<List<Integer>, Integer>> clazz = HashMap.class.asSubclass(Map.class);
        TypeVariable<?>[] typeVariables = clazz.getTypeParameters();
        JavaType javaType1 = typeVariables[0].getGenericType();
        JavaType javaType2 = typeVariables[1].getGenericType();
        TypeBindings bindings = TypeBindings.create(clazz, javaType1, javaType2);
        assertFalse(bindings.isEmpty());
        assertEquals(2, bindings.size());
        assertSame(javaType1, bindings.getBoundType(0));
        assertSame(javaType2, bindings.getBoundType(1));
    }

    @Test
    public void testGetBoundName() {
        Class<Map<List<Integer>, Integer>> clazz = HashMap.class.asSubclass(Map.class);
        TypeVariable<?>[] typeVariables = clazz.getTypeParameters();
        JavaType javaType1 = typeVariables[0].getGenericType();
        JavaType javaType2 = typeVariables[1].getGenericType();
        TypeBindings bindings = TypeBindings.create(clazz, javaType1, javaType2);
        assertEquals("List", bindings.getBoundName(0));
        assertEquals("Integer", bindings.getBoundName(1));
    }

    @Test
    public void testFindBoundType() {
        Class<Map<List<Integer>, Integer>> clazz = HashMap.class.asSubclass(Map.class);
        TypeVariable<?>[] typeVariables = clazz.getTypeParameters();
        JavaType javaType1 = typeVariables[0].getGenericType();
        JavaType javaType2 = typeVariables[1].getGenericType();
        TypeBindings bindings = TypeBindings.create(clazz, javaType1, javaType2);
        assertSame(javaType1, bindings.findBoundType("List"));
        assertSame(javaType2, bindings.findBoundType("Integer"));
        assertNull(bindings.findBoundType("String"));
    }

    @Test
    public void testWithUnboundVariable() {
        Class<Map<List<Integer>, Integer>> clazz = HashMap.class.asSubclass(Map.class);
        TypeVariable<?>[] typeVariables = clazz.getTypeParameters();
        JavaType javaType1 = typeVariables[0].getGenericType();
        JavaType javaType2 = typeVariables[1].getGenericType();
        TypeBindings bindings = TypeBindings.create(clazz, javaType1, javaType2);
        TypeBindings bindingsWithUnboundVariable = bindings.withUnboundVariable("Key");
        String[] unboundVariables = bindingsWithUnboundVariable.getUnboundVariables();
        assertArrayEquals(new String[]{"List", "Integer", "Key"}, unboundVariables);
    }

}