package com.fasterxml.jackson.databind.type;

import static org.junit.Assert.*;

import java.lang.reflect.TypeVariable;

import org.junit.Test;

public class TypeBindingsTest {

    @Test
    public void testEmptyBindings() {
        TypeBindings bindings = TypeBindings.emptyBindings();
        assertTrue(bindings.isEmpty());
        assertEquals(0, bindings.size());
    }

    @Test
    public void testCreateBindingsWithNoTypeArgs() {
        Class<?> clazz = Object.class;
        TypeBindings bindings = TypeBindings.create(clazz, null);
        assertTrue(bindings.isEmpty());
        assertEquals(0, bindings.size());
    }

    @Test
    public void testCreateBindingsWithOneTypeArg() {
        Class<?> listClass = List.class;
        TypeVariable<?>[] typeParams = listClass.getTypeParameters();
        String name = typeParams[0].getName();
        JavaType javaType = TypeToken.of(int[].class).getType();
        TypeBindings bindings = TypeBindings.create(listClass, javaType);
        assertEquals(name, bindings.getBoundName(0));
        assertEquals(javaType, bindings.getBoundType(0));
        assertEquals(1, bindings.size());
    }

    @Test
    public void testCreateBindingsWithTwoTypeArgs() {
        Class<?> mapClass = Map.class;
        TypeVariable<?>[] typeParams = mapClass.getTypeParameters();
        String keyTypeName = typeParams[0].getName();
        String valueTypeName = typeParams[1].getName();
        JavaType keyType = TypeToken.of(String.class).getType();
        JavaType valueType = TypeToken.of(Integer.class).getType();
        TypeBindings bindings = TypeBindings.create(mapClass, keyType, valueType);
        assertEquals(keyTypeName, bindings.getBoundName(0));
        assertEquals(keyType, bindings.getBoundType(0));
        assertEquals(valueTypeName, bindings.getBoundName(1));
        assertEquals(valueType, bindings.getBoundType(1));
        assertEquals(2, bindings.size());
    }

    @Test
    public void testFindBoundType() {
        Class<?> listClass = List.class;
        TypeVariable<?>[] typeParams = listClass.getTypeParameters();
        String name = typeParams[0].getName();
        JavaType javaType = TypeToken.of(int[].class).getType();
        TypeBindings bindings = TypeBindings.create(listClass, javaType);
        JavaType returnedType = bindings.findBoundType(name);
        assertNotNull(returnedType);
        assertEquals(javaType, returnedType);
    }

    @Test
    public void testFindBoundTypeNull() {
        Class<?> listClass = List.class;
        TypeVariable<?>[] typeParams = listClass.getTypeParameters();
        String name = "NonexistentType";
        TypeBindings bindings = TypeBindings.create(listClass, null);
        assertNull(bindings.findBoundType(name));
    }

    @Test
    public void testHasUnboundVariable() {
        Class<?> mapClass = Map.class;
        TypeVariable<?>[] typeParams = mapClass.getTypeParameters();
        String keyTypeName = typeParams[0].getName();
        String valueTypeName = typeParams[1].getName();
        JavaType keyType = TypeToken.of(String.class).getType();
        JavaType valueType = TypeToken.of(Integer.class).getType();
        TypeBindings bindings = TypeBindings.create(mapClass, keyType, valueType);
        assertTrue(bindings.hasUnbound(keyTypeName));
        assertTrue(bindings.hasUnbound(valueTypeName));
    }

    @Test
    public void testHasUnboundVariableNull() {
        Class<?> mapClass = Map.class;
        TypeBindings bindings = TypeBindings.create(mapClass, null);
        assertFalse(bindings.hasUnbound(null));
    }
}