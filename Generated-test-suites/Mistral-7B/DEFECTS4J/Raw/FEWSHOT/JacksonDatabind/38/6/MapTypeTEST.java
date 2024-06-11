package com.fasterxml.jackson.databind.type;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class MapTypeTest {

    @Test
    public void testMapTypeConstruction() {
        Class<Map<String, Integer>> mapType = Map.class;
        TypeBindings bindings = TypeBindings.empty();
        JavaType intType = TypeFactory.defaultInstance().constructType(Integer.class);
        JavaType stringType = TypeFactory.defaultInstance().constructType(String.class);
        MapType mapTypeInstance = MapType.construct(mapType, bindings, null, null, stringType, intType);
        assertEquals(mapType, mapTypeInstance.getRawClass());
        assertEquals(stringType, mapTypeInstance.getKeyType());
        assertEquals(intType, mapTypeInstance.getValueType());
        assertNull(mapTypeInstance.getTypeHandler());
        assertNull(mapTypeInstance.getValueHandler());
        assertFalse(mapTypeInstance.isStatic());
    }

    @Test
    public void testMapTypeWithTypeHandler() {
        Class<Map<String, Integer>> mapType = Map.class;
        TypeBindings bindings = TypeBindings.empty();
        JavaType intType = TypeFactory.defaultInstance().constructType(Integer.class);
        JavaType stringType = TypeFactory.defaultInstance().constructType(String.class);
        Object handler = new Object();
        MapType mapTypeInstance = MapType.construct(mapType, bindings, null, null, stringType, intType);
        MapType withTypeHandler = mapTypeInstance.withTypeHandler(handler);
        assertEquals(handler, withTypeHandler.getTypeHandler());
    }

    @Test
    public void testMapTypeWithValueHandler() {
        Class<Map<String, Integer>> mapType = Map.class;
        TypeBindings bindings = TypeBindings.empty();
        JavaType intType = TypeFactory.defaultInstance().constructType(Integer.class);
        JavaType stringType = TypeFactory.defaultInstance().constructType(String.class);
        Object handler = new Object();
        MapType mapTypeInstance = MapType.construct(mapType, bindings, null, null, stringType, intType);
        MapType withValueHandler = mapTypeInstance.withValueHandler(handler);
        assertEquals(handler, withValueHandler.getValueHandler());
    }

    @Test
    public void testMapTypeWithStaticTyping() {
        Class<Map<String, Integer>> mapType = Map.class;
        TypeBindings bindings = TypeBindings.empty();
        JavaType intType = TypeFactory.defaultInstance().constructType(Integer.class);
        JavaType stringType = TypeFactory.defaultInstance().constructType(String.class);
        MapType mapTypeInstance = MapType.construct(mapType, bindings, null, null, stringType, intType);
        MapType withStaticTyping = mapTypeInstance.withStaticTyping();
        assertTrue(withStaticTyping.isStatic());
    }

    @Test
    public void testMapTypeWithContentType() {
        Class<Map<String, Integer>> mapType = Map.class;
        TypeBindings bindings = TypeBindings.empty();
        JavaType intType = TypeFactory.defaultInstance().constructType(Integer.class);
        JavaType stringType = TypeFactory.defaultInstance().constructType(String.class);
        JavaType intArrayType = TypeFactory.defaultInstance().constructType(int[].class);
        MapType mapTypeInstance = MapType.construct(mapType, bindings, null, null, stringType, intType);
        MapType withContentType = mapTypeInstance.withContentType(intArrayType);
        assertEquals(intArrayType, withContentType.getValueType());
    }

    @Test
    public void testMapTypeWithKeyType() {
        Class<Map<String, Integer>> mapType = Map.class;
        TypeBindings bindings = TypeBindings.empty();
        JavaType intType = TypeFactory.defaultInstance().constructType(Integer.class);
        JavaType stringType = TypeFactory.defaultInstance().constructType(String.class);
        JavaType charType = TypeFactory.defaultInstance().constructType(char.class);
        MapType mapTypeInstance = MapType.construct(mapType, bindings, null, null, stringType, intType);
        MapType withKeyType = mapTypeInstance.withKeyType(charType);
        assertEquals(charType, withKeyType.getKeyType());
    }

    @Test
    public void testMapTypeRefine() {
        Class<Map<String, Integer>> mapType = Map.class;
        TypeBindings bindings = TypeBindings.empty();
        JavaType intType = TypeFactory.defaultInstance().constructType(Integer.class);
        JavaType stringType = TypeFactory.defaultInstance().constructType(String.class);
        Class<Map<String, Long>> mapTypeWithLongValues = Map.class;
        TypeBindings bindingsWithLongValues = TypeBindings.empty();
        JavaType longType = TypeFactory.defaultInstance().constructType(Long.class);
        MapType mapTypeInstance = MapType.construct(mapType, bindings, null, null, stringType, intType);
        JavaType refinedType = mapTypeInstance.refine(mapTypeWithLongValues, bindingsWithLongValues, null, null);
        assertEquals(mapTypeWithLongValues, refinedType.getRawClass());
        assertEquals(stringType, refinedType.getKeyType());
        assertEquals(longType, refinedType.getValueType());
    }

    @Test
    public void testMapTypeWithKeyTypeHandler() {
        Class<Map<String, Integer>> mapType = Map.class;
        TypeBindings bindings = TypeBindings.empty();
        JavaType intType = TypeFactory.defaultInstance().constructType(Integer.class);
        JavaType stringType = TypeFactory.defaultInstance().constructType(String.class);
        Object handler = new Object();
        MapType mapTypeInstance = MapType.construct(mapType, bindings, null, null, stringType, intType);
        MapType withKeyTypeHandler = mapTypeInstance.withKeyTypeHandler(handler);
        assertEquals(handler, withKeyTypeHandler.getKeyTypeHandler());
    }

    @Test
    public void testMapTypeWithKeyValueHandler() {
        Class<Map<String, Integer>> mapType = Map.class;
        TypeBindings bindings = TypeBindings.empty();
        JavaType intType = TypeFactory.defaultInstance().constructType(Integer.class);
        JavaType stringType = TypeFactory.defaultInstance().constructType(String.class);
        Object handler = new Object();
        MapType mapTypeInstance = MapType.construct(mapType, bindings, null, null, stringType, intType);
        MapType withKeyValueHandler = mapTypeInstance.withKeyValueHandler(handler);
        assertEquals(handler, withKeyValueHandler.getKeyValueHandler());
    }

    @Test
    public void testMapTypeToString() {
        Class<Map<String, Integer>> mapType = Map.class;
        TypeBindings bindings = TypeBindings.empty();
        JavaType intType = TypeFactory.defaultInstance().constructType(Integer.class);
        JavaType stringType = TypeFactory.defaultInstance().constructType(String.class);
        MapType mapTypeInstance = MapType.construct(mapType, bindings, null, null, stringType, intType);
        String toString = mapTypeInstance.toString();
        assertTrue(toString.startsWith("[[map type; class "));
        assertTrue(toString.endsWith("]]"));
        assertTrue(toString.contains("class "+mapType.getName()+", "));
        assertTrue(toString.contains(stringType.getName()+" -> "+intType.getName()));
    }
}