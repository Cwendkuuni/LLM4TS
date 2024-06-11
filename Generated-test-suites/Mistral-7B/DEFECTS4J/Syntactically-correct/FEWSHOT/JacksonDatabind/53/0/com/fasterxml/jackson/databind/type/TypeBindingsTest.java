package com.fasterxml.jackson.databind.type;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertEquals;

import java.lang.reflect.TypeVariable;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;

public class TypeBindingsTest {

    @Test
    public void testEmptyBindings() {
        TypeBindings bindings = TypeBindings.emptyBindings();
        assertNull(bindings._names);
        assertArrayEquals(new JavaType[0], bindings._types);
        assertNull(bindings._unboundVariables);
        assertEquals(0, bindings.size());
        assertEquals(TypeBindings.EMPTY, bindings);
    }

    @Test
    public void testCreate() {
        Class<List<String>> listType = List.class.getTypeParameterTypes()[0];
        JavaType javaType = TypeFactory.defaultInstance().constructType(String.class);
        TypeBindings bindings = TypeBindings.create(List.class, Arrays.asList(javaType));
        String[] names = bindings._names;
        JavaType[] types = bindings._types;
        assertEquals(1, names.length);
        assertEquals(1, types.length);
        assertEquals(names[0], listType.getTypeName());
        assertEquals(types[0], javaType);
        assertEquals(1, bindings.size());
    }

    @Test
    public void testCreateWithNullTypes() {
        Class<List<String>> listType = List.class.getTypeParameterTypes()[0];
        JavaType javaType = TypeFactory.defaultInstance().constructType(String.class);
        TypeBindings bindings = TypeBindings.create(List.class, null);
        String[] names = bindings._names;
        JavaType[] types = bindings._types;
        assertEquals(0, names.length);
        assertEquals(0, types.length);
        assertEquals(TypeBindings.EMPTY, bindings);
    }

    @Test
    public void testCreateWithMultipleTypes() {
        Class<Map<String, List<String>>> mapType = Map.class.getTypeParameterTypes()[0];
        Class<List<String>> listType = List.class.getTypeParameterTypes()[0];
        JavaType listJavaType = TypeFactory.defaultInstance().constructType(List.class, String.class);
        JavaType mapJavaType = TypeFactory.defaultInstance().constructType(Map.class, String.class, listJavaType);
        TypeBindings bindings = TypeBindings.create(Map.class, Arrays.asList(mapJavaType, listJavaType));
        String[] names = bindings._names;
        JavaType[] types = bindings._types;
        assertEquals(2, names.length);
        assertEquals(2, types.length);
        assertEquals(names[0], mapType.getTypeName());
        assertEquals(names[1], listType.getTypeName());
        assertEquals(types[0], mapJavaType);
        assertEquals(types[1], listJavaType);
        assertEquals(2, bindings.size());
    }

    @Test
    public void testCreateIfNeeded() {
        Class<List<String>> listType = List.class.getTypeParameterTypes()[0];
        JavaType javaType = TypeFactory.defaultInstance().constructType(String.class);
        TypeBindings bindings = TypeBindings.createIfNeeded(List.class, javaType);
        String[] names = bindings._names;
        JavaType[] types = bindings._types;
        assertEquals(1, names.length);
        assertEquals(1, types.length);
        assertEquals(names[0], listType.getTypeName());
        assertEquals(types[0], javaType);
        assertEquals(1, bindings.size());
    }

    @Test
    public void testCreateIfNeededWithNullTypes() {
        Class<List<String>> listType = List.class.getTypeParameterTypes()[0];
        JavaType javaType = TypeFactory.defaultInstance().constructType(String.class);
        TypeBindings bindings = TypeBindings.createIfNeeded(List.class, null);
        String[] names = bindings._names;
        JavaType[] types = bindings._types;
        assertEquals(0, names.length);
        assertEquals(0, types.length);
        assertEquals(TypeBindings.EMPTY, bindings);
    }

    @Test
    public void testCreateIfNeededWithMultipleTypes() {
        Class<Map<String, List<String>>> mapType = Map.class.getTypeParameterTypes()[0];
        Class<List<String>> listType = List.class.getTypeParameterTypes()[0];
        JavaType listJavaType = TypeFactory.defaultInstance().constructType(List.class, String.class);
        JavaType mapJavaType = TypeFactory.defaultInstance().constructType(Map.class, String.class, listJavaType);
        TypeBindings bindings = TypeBindings.createIfNeeded(Map.class, mapJavaType, listJavaType);
        String[] names = bindings._names;
        JavaType[] types = bindings._types;
        assertEquals(2, names.length);
        assertEquals(2, types.length);
        assertEquals(names[0], mapType.getTypeName());
        assertEquals(names[1], listType.getTypeName());
        assertEquals(types[0], mapJavaType);
        assertEquals(types[1], listJavaType);
        assertEquals(2, bindings.size());
    }

    @Test
    public void testFindBoundType() {
        Class<Map<String, List<String>>> mapType = Map.class.getTypeParameterTypes()[0];
        Class<List<String>> listType = List.class.getTypeParameterTypes()[0];
        JavaType mapJavaType = TypeFactory.defaultInstance().constructType(Map.class, String.class, listJavaType);
        JavaType listJavaType = TypeFactory.defaultInstance().constructType(List.class, String.class);
        TypeBindings bindings = TypeBindings.create(Map.class, Arrays.asList(mapJavaType, listJavaType));
        assertEquals(mapJavaType, bindings.findBoundType("Map"));
        assertEquals(listJavaType, bindings.findBoundType("List"));
        assertNull(bindings.findBoundType("Object"));
    }

    @Test
    public void testHasUnbound() {
        Class<Map<String, List<String>>> mapType = Map.class.getTypeParameterTypes()[0];
        Class<List<String>> listType = List.class.getTypeParameterTypes()[0];
        JavaType mapJavaType = TypeFactory.defaultInstance().constructType(Map.class, String.class, listJavaType);
        JavaType listJavaType = TypeFactory.defaultInstance().constructType(List.class, String.class);
        TypeBindings bindings = TypeBindings.create(Map.class, Arrays.asList(mapJavaType, listJavaType));
        assertFalse(bindings.hasUnbound("Map"));
        assertFalse(bindings.hasUnbound("List"));
        assertTrue(bindings.hasUnbound("Unknown"));
    }

    @Test
    public void testToString() {
        Class<Map<String, List<String>>> mapType = Map.class.getTypeParameterTypes()[0];
        Class<List<String>> listType = List.class.getTypeParameterTypes()[0];
        JavaType mapJavaType = TypeFactory.defaultInstance().constructType(Map.class, String.class, listJavaType);
        JavaType listJavaType = TypeFactory.defaultInstance().constructType(List.class, String.class);
        TypeBindings bindings = TypeBindings.create(Map.class, Arrays.asList(mapJavaType, listJavaType));
        String expected = "<Map<java.lang.String, List<java.lang.String>>, java.util.List<java.lang.String>>";
        assertEquals(expected, bindings.toString());
    }

    @Test
    public void testEquals() {
        Class<Map<String, List<String>>> mapType = Map.class.getTypeParameterTypes()[0];
        Class<List<String>> listType = List.class.getTypeParameterTypes()[0];
        JavaType mapJavaType = TypeFactory.defaultInstance().constructType(Map.class, String.class, listJavaType);
        JavaType listJavaType = TypeFactory.defaultInstance().constructType(List.class, String.class);
        JavaType mapJavaType2 = TypeFactory.defaultInstance().constructType(Map.class, String.class, listJavaType);
        JavaType listJavaType2 = TypeFactory.defaultInstance().constructType(List.class, String.class);
        TypeBindings bindings1 = TypeBindings.create(Map.class, Arrays.asList(mapJavaType, listJavaType));
        TypeBindings bindings2 = TypeBindings.create(Map.class, Arrays.asList(mapJavaType2, listJavaType2));
        TypeBindings bindings3 = TypeBindings.create(Map.class, Arrays.asList(mapJavaType, listJavaType2));
        assertTrue(bindings1.equals(bindings1));
        assertTrue(bindings1.equals(bindings2));
        assertFalse(bindings1.equals(bindings3));
    }

    @Test
    public void testWithUnboundVariable() {
        Class<Map<String, List<String>>> mapType = Map.class.getTypeParameterTypes()[0];
        Class<List<String>> listType = List.class.getTypeParameterTypes()[0];
        JavaType mapJavaType = TypeFactory.defaultInstance().constructType(Map.class, String.class, listJavaType);
        JavaType listJavaType = TypeFactory.defaultInstance().constructType(List.class, String.class);
        JavaType mapJavaType2 = TypeFactory.defaultInstance().constructType(Map.class, String.class, listJavaType);
        JavaType listJavaType2 = TypeFactory.defaultInstance().constructType(List.class, String.class);
        TypeBindings bindings1 = TypeBindings.create(Map.class, Arrays.asList(mapJavaType, listJavaType));
        TypeBindings bindings2 = bindings1.withUnboundVariable("Map");
        assertArrayEquals(new String[]{"Map", "List"}, bindings2._names);
        assertEquals(2, bindings2._types.length);
        assertEquals(TypeBindings.class, bindings2.getClass());
    }
}