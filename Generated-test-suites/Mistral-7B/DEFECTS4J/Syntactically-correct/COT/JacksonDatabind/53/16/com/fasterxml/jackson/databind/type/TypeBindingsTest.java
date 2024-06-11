package com.example.test;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.TypeBindings;
import org.junit.Test;

import java.lang.reflect.TypeVariable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;

public class TypeBindingsTest {

    @Test
    public void testEmptyBindings() {
        TypeBindings empty = TypeBindings.emptyBindings();
        assertTrue(empty.isEmpty());
        assertEquals(0, empty.size());
    }

    @Test
    public void testCreate() {
        JavaType javaType = TypeFactory.defaultInstance().constructType(List.class, Integer.class);
        TypeBindings bindings = TypeBindings.create(List.class, javaType);
        assertFalse(bindings.isEmpty());
        assertEquals(1, bindings.size());
        assertEquals("T", bindings.getBoundName(0));
        assertEquals(javaType, bindings.getBoundType(0));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateWithInvalidNumberOfTypeArguments() {
        JavaType[] types = new JavaType[3];
        TypeBindings bindings = TypeBindings.create(List.class, types);
    }

    @Test
    public void testCreateWithMissingTypeArguments() {
        JavaType[] types = new JavaType[1];
        TypeBindings bindings = TypeBindings.create(List.class, types);
        assertTrue(bindings.isEmpty());
        assertEquals(0, bindings.size());
    }

    @Test
    public void testCreateForCollectionClass() {
        JavaType[] types = new JavaType[2];
        types[0] = TypeFactory.defaultInstance().constructType(ArrayList.class, Integer.class);
        types[1] = TypeFactory.defaultInstance().constructType(Integer.class);
        TypeBindings bindings = TypeBindings.create(List.class, types);
        assertFalse(bindings.isEmpty());
        assertEquals(2, bindings.size());
        assertEquals("T", bindings.getBoundName(0));
        assertEquals(types[0], bindings.getBoundType(0));
        assertEquals("E", bindings.getBoundName(1));
        assertEquals(types[1], bindings.getBoundType(1));
    }

    @Test
    public void testCreateForMapClass() {
        JavaType keyType = TypeFactory.defaultInstance().constructType(String.class);
        JavaType valueType = TypeFactory.defaultInstance().constructType(Integer.class);
        JavaType mapType = TypeFactory.defaultInstance().constructType(HashMap.class, keyType, valueType);
        TypeBindings bindings = TypeBindings.create(Map.class, mapType);
        assertFalse(bindings.isEmpty());
        assertEquals(2, bindings.size());
        assertEquals("K", bindings.getBoundName(0));
        assertEquals(keyType, bindings.getBoundType(0));
        assertEquals("V", bindings.getBoundName(1));
        assertEquals(valueType, bindings.getBoundType(1));
    }

    @Test
    public void testFindBoundType() {
        JavaType[] types = new JavaType[2];
        types[0] = TypeFactory.defaultInstance().constructType(ArrayList.class, Integer.class);
        types[1] = TypeFactory.defaultInstance().constructType(Integer.class);
        TypeBindings bindings = TypeBindings.create(List.class, types);
        assertEquals(types[0], bindings.findBoundType("T"));
        assertEquals(types[1], bindings.findBoundType("E"));
        assertNull(bindings.findBoundType("unknown"));
    }

    @Test(expected = IllegalStateException.class)
    public void testFindBoundTypeForUnresolvedType() {
        JavaType[] types = new JavaType[1];
        types[0] = TypeFactory.defaultInstance().constructType(List.class, Integer.class);
        TypeBindings bindings = TypeBindings.create(List.class, types);
        bindings.findBoundType("E");
    }

    @Test
    public void testGetSize() {
        JavaType[] types = new JavaType[2];
        types[0] = TypeFactory.defaultInstance().constructType(ArrayList.class, Integer.class);
        types[1] = TypeFactory.defaultInstance().constructType(Integer.class);
        TypeBindings bindings = TypeBindings.create(List.class, types);
        assertEquals(2, bindings.size());
    }

    @Test
    public void testGetBoundName() {
        JavaType[] types = new JavaType[2];
        types[0] = TypeFactory.defaultInstance().constructType(ArrayList.class, Integer.class);
        types[1] = TypeFactory.defaultInstance().constructType(Integer.class);
        TypeBindings bindings = TypeBindings.create(List.class, types);
        assertEquals("T", bindings.getBoundName(0));
        assertEquals("E", bindings.getBoundName(1));
    }

    @Test
    public void testGetBoundType() {
        JavaType[] types = new JavaType[2];
        types[0] = TypeFactory.defaultInstance().constructType(ArrayList.class, Integer.class);
        types[1] = TypeFactory.defaultInstance().constructType(Integer.class);
        TypeBindings bindings = TypeBindings.create(List.class, types);
        assertEquals(types[0], bindings.getBoundType(0));
        assertEquals(types[1], bindings.getBoundType(1));
    }

    @Test
    public void testGetTypeParameters() {
        JavaType[] types = new JavaType[2];
        types[0] = TypeFactory.defaultInstance().constructType(ArrayList.class, Integer.class);
        types[1] = TypeFactory.defaultInstance().constructType(Integer.class);
        TypeBindings bindings = TypeBindings.create(List.class, types);
        List<JavaType> typeParameters = bindings.getTypeParameters();
        assertEquals(2, typeParameters.size());
        assertEquals(types[0], typeParameters.get(0));
        assertEquals(types[1], typeParameters.get(1));
    }

    @Test
    public void testHasUnbound() {
        JavaType[] types = new JavaType[2];
        types[0] = TypeFactory.defaultInstance().constructType(ArrayList.class, Integer.class);
        types[1] = TypeFactory.defaultInstance().constructType(Integer.class);
        TypeBindings bindings = TypeBindings.create(List.class, types);
        assertFalse(bindings.hasUnbound("T"));
        assertFalse(bindings.hasUnbound("E"));

        TypeBindings bindingsWithUnbound = bindings.withUnboundVariable("U");
        assertTrue(bindingsWithUnbound.hasUnbound("U"));
    }
}