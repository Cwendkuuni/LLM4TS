package com.fasterxml.jackson.databind.type.test;

import static org.junit.Assert.*;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.MapLikeType;
import com.fasterxml.jackson.databind.type.MapType;
import com.fasterxml.jackson.databind.type.TypeBindings;
import com.fasterxml.jackson.databind.type.TypeFactory;

import org.junit.Test;

public class MapTypeTest {

    @Test
    public void testMapTypeConstructor() {
        Class<Map<String, Integer>> rawType = Map.class;
        TypeBindings bindings = TypeFactory.defaultBindings();
        JavaType keyType = TypeFactory.defaultInstance().constructType(String.class);
        JavaType valueType = TypeFactory.defaultInstance().constructType(Integer.class);
        MapType mapType = MapType.construct(rawType, bindings, null, null, keyType, valueType);
        assertEquals("map type; class Map<java.lang.String, java.lang.Integer>, " +
                "java.lang.String -> java.lang.Integer", mapType.toString());
    }

    @Test
    public void testMapTypeWithTypeHandler() {
        Class<Map<String, Integer>> rawType = Map.class;
        TypeBindings bindings = TypeFactory.defaultBindings();
        JavaType keyType = TypeFactory.defaultInstance().constructType(String.class);
        JavaType valueType = TypeFactory.defaultInstance().constructType(Integer.class);
        MapType mapType = new MapType(rawType, bindings, null, null, keyType, valueType);
        Object valueHandler = new Object();
        MapType mapTypeWithValueHandler = mapType.withValueHandler(valueHandler);
        assertEquals(mapType.toString(), mapTypeWithValueHandler.toString());
        assertNotSame(mapType.getValueHandler(), mapTypeWithValueHandler.getValueHandler());
        assertEquals(valueHandler, mapTypeWithValueHandler.getValueHandler());
    }

    @Test
    public void testMapTypeWithContentTypeHandler() {
        Class<Map<String, Integer>> rawType = Map.class;
        TypeBindings bindings = TypeFactory.defaultBindings();
        JavaType keyType = TypeFactory.defaultInstance().constructType(String.class);
        JavaType valueType = TypeFactory.defaultInstance().constructType(Integer.class);
        MapType mapType = new MapType(rawType, bindings, null, null, keyType, valueType);
        Object contentTypeHandler = new Object();
        JavaType contentType = mapType.getValueType().withTypeHandler(contentTypeHandler);
        MapType mapTypeWithContentTypeHandler = mapType.withContentTypeHandler(contentType);
        assertEquals(mapType.toString(), mapTypeWithContentTypeHandler.toString());
        assertNotSame(mapType.getValueType(), mapTypeWithContentTypeHandler.getValueType());
        assertEquals(contentType, mapTypeWithContentTypeHandler.getValueType());
    }

    @Test
    public void testMapTypeWithStaticTyping() {
        Class<Map<String, Integer>> rawType = Map.class;
        TypeBindings bindings = TypeFactory.defaultBindings();
        JavaType keyType = TypeFactory.defaultInstance().constructType(String.class);
        JavaType valueType = TypeFactory.defaultInstance().constructType(Integer.class);
        MapType mapType = new MapType(rawType, bindings, null, null, keyType, valueType);
        MapType mapTypeWithStaticTyping = mapType.withStaticTyping();
        assertTrue(mapTypeWithStaticTyping.isStatic());
        assertFalse(mapType.isStatic());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testMapTypeWithInvalidContentType() {
        Class<Map<String, Integer>> rawType = Map.class;
        TypeBindings bindings = TypeFactory.defaultBindings();
        JavaType keyType = TypeFactory.defaultInstance().constructType(String.class);
        JavaType valueType = TypeFactory.defaultInstance().constructType(Object.class);
        MapType mapType = new MapType(rawType, bindings, null, null, keyType, valueType);
        MapType mapTypeWithContentType = mapType.withContentType(TypeFactory.defaultInstance().constructType(Integer.class));
        MapType mapTypeWithInvalidContentType = mapTypeWithContentType.withContentType(TypeFactory.defaultInstance().constructType(String.class));
    }

    @Test
    public void testMapTypeWithKeyType() {
        Class<Map<String, Integer>> rawType = Map.class;
        TypeBindings bindings = TypeFactory.defaultBindings();
        JavaType keyType1 = TypeFactory.defaultInstance().constructType(String.class);
        JavaType valueType = TypeFactory.defaultInstance().constructType(Integer.class);
        MapType mapType = new MapType(rawType, bindings, null, null, keyType1, valueType);
        JavaType keyType2 = TypeFactory.defaultInstance().constructType(Long.class);
        MapType mapTypeWithKeyType = mapType.withKeyType(keyType2);
        assertNotSame(mapType.getKeyType(), mapTypeWithKeyType.getKeyType());
        assertEquals(keyType2, mapTypeWithKeyType.getKeyType());
    }

    @Test
    public void testMapTypeRefine() {
        Class<Map<String, Integer>> rawType = Map.class;
        TypeBindings bindings = TypeFactory.defaultBindings();
        JavaType keyType = TypeFactory.defaultInstance().constructType(String.class);
        JavaType valueType = TypeFactory.defaultInstance().constructType(Integer.class);
        MapType mapType = new MapType(rawType, bindings, null, null, keyType, valueType);
        Class<Map<Long, Double>> rawTypeRefined = Map.class;
        TypeBindings bindingsRefined = TypeFactory.defaultBindings();
        JavaType keyTypeRefined = TypeFactory.defaultInstance().constructType(Long.class);
        JavaType valueTypeRefined = TypeFactory.defaultInstance().constructType(Double.class);
        MapType mapTypeRefined = mapType.refine(rawTypeRefined, bindingsRefined, null, null);
        assertNotSame(mapType.toString(), mapTypeRefined.toString());
        assertEquals(rawTypeRefined, mapTypeRefined.getRawClass());
        assertEquals(keyTypeRefined, mapTypeRefined.getKeyType());
        assertEquals(valueTypeRefined, mapTypeRefined.getValueType());
    }

    @Test
    public void testMapTypeWithKeyTypeHandler() {
        Class<Map<String, Integer>> rawType = Map.class;
        TypeBindings bindings = TypeFactory.defaultBindings();
        JavaType keyType = TypeFactory.defaultInstance().constructType(String.class);
        JavaType valueType = TypeFactory.defaultInstance().constructType(Integer.class);
        MapType mapType = new MapType(rawType, bindings, null, null, keyType, valueType);
        Object keyTypeHandler = new Object();
        MapType mapTypeWithKeyTypeHandler = mapType.withKeyTypeHandler(keyTypeHandler);
        assertNotSame(mapType.getKeyType(), mapTypeWithKeyTypeHandler.getKeyType());
        assertEquals(keyType, mapTypeWithKeyTypeHandler.getKeyType());
        assertNotSame(mapType.getKeyTypeHandler(), mapTypeWithKeyTypeHandler.getKeyTypeHandler());
        assertEquals(keyTypeHandler, mapTypeWithKeyTypeHandler.getKeyTypeHandler());
    }

    @Test
    public void testMapTypeWithKeyValueHandler() {
        Class<Map<String, Integer>> rawType = Map.class;
        TypeBindings bindings = TypeFactory.defaultBindings();
        JavaType keyType = TypeFactory.defaultInstance().constructType(String.class);
        JavaType valueType = TypeFactory.defaultInstance().constructType(Integer.class);
        MapType mapType = new MapType(rawType, bindings, null, null, keyType, valueType);
        Object keyValueHandler = new Object();
        MapType mapTypeWithKeyValueHandler = mapType.withKeyValueHandler(keyValueHandler);
        assertNotSame(mapType.getKeyType(), mapTypeWithKeyValueHandler.getKeyType());
        assertNotSame(mapType.getValueType(), mapTypeWithKeyValueHandler.getValueType());
        assertEquals(keyValueHandler, mapTypeWithKeyValueHandler.getKeyValueHandler());
    }
}