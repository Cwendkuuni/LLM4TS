package com.example.junit;

import static org.junit.Assert.*;

import java.util.Arrays;

import org.junit.Test;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.MapLikeType;

public class MapLikeTypeTest {

    @Test
    public void testMapLikeTypeConstruction() {
        Class<Map<String, Integer>> rawType = Map.class;
        JavaType keyType = JavaType.constructType(String.class);
        JavaType valueType = JavaType.constructType(Integer.class);

        MapLikeType mapLikeType = new MapLikeType(rawType, null, null, null, keyType, valueType);

        assertEquals("java.util.Map", mapLikeType.getClassName());
        assertEquals(keyType, mapLikeType.getKeyType());
        assertEquals(valueType, mapLikeType.getValueType());
    }

    @Test
    public void testMapLikeTypeUpgrade() {
        JavaType javaType = JavaType.constructType(Map.class, String.class, Integer.class);
        JavaType keyType = JavaType.constructType(String.class);
        JavaType valueType = JavaType.constructType(Integer.class);

        MapLikeType mapLikeType = MapLikeType.upgradeFrom(javaType, keyType, valueType);

        assertEquals("java.util.Map", mapLikeType.getClassName());
        assertEquals(keyType, mapLikeType.getKeyType());
        assertEquals(valueType, mapLikeType.getValueType());
    }

    @Test
    public void testMapLikeTypeIsContainerType() {
        Class<Map<String, Integer>> rawType = Map.class;
        JavaType keyType = JavaType.constructType(String.class);
        JavaType valueType = JavaType.constructType(Integer.class);

        MapLikeType mapLikeType = new MapLikeType(rawType, null, null, null, keyType, valueType);

        assertTrue(mapLikeType.isContainerType());
        assertTrue(mapLikeType.isMapLikeType());
    }

    @Test
    public void testMapLikeTypeIsTrueMapType() {
        Class<Map<String, Integer>> rawType = Map.class;
        JavaType keyType = JavaType.constructType(String.class);
        JavaType valueType = JavaType.constructType(Integer.class);

        MapLikeType mapLikeType = new MapLikeType(rawType, null, null, null, keyType, valueType);

        assertTrue(mapLikeType.isTrueMapType());
    }

    @Test
    public void testMapLikeTypeWithKeyType() {
        Class<Map<String, Integer>> rawType = Map.class;
        JavaType keyType = JavaType.constructType(String.class);
        JavaType valueType = JavaType.constructType(Integer.class);

        MapLikeType mapLikeType = new MapLikeType(rawType, null, null, null, keyType, valueType);
        JavaType newKeyType = JavaType.constructType(Long.class);

        MapLikeType newMapLikeType = mapLikeType.withKeyType(newKeyType);

        assertEquals(newKeyType, newMapLikeType.getKeyType());
        assertEquals(valueType, newMapLikeType.getValueType());
    }

    @Test
    public void testMapLikeTypeWithContentType() {
        Class<Map<String, Integer>> rawType = Map.class;
        JavaType keyType = JavaType.constructType(String.class);
        JavaType valueType = JavaType.constructType(Integer.class);

        MapLikeType mapLikeType = new MapLikeType(rawType, null, null, null, keyType, valueType);
        JavaType newValueType = JavaType.constructType(Long.class);

        MapLikeType newMapLikeType = mapLikeType.withContentType(newValueType);

        assertEquals(keyType, newMapLikeType.getKeyType());
        assertEquals(newValueType, newMapLikeType.getValueType());
    }

    @Test
    public void testMapLikeTypeWithTypeHandler() {
        Class<Map<String, Integer>> rawType = Map.class;
        JavaType keyType = JavaType.constructType(String.class);
        JavaType valueType = JavaType.constructType(Integer.class);

        MapLikeType mapLikeType = new MapLikeType(rawType, null, null, null, keyType, valueType);
        Object handler = new Object() {};

        MapLikeType newMapLikeType = mapLikeType.withTypeHandler(handler);

        assertNotNull(newMapLikeType.getTypeHandler());
        assertEquals(handler, newMapLikeType.getTypeHandler());
    }

    @Test
    public void testMapLikeTypeWithContentTypeHandler() {
        Class<Map<String, Integer>> rawType = Map.class;
        JavaType keyType = JavaType.constructType(String.class);
        JavaType valueType = JavaType.constructType(Integer.class);

        MapLikeType mapLikeType = new MapLikeType(rawType, null, null, null, keyType, valueType);
        Object handler = new Object() {};

        JavaType newValueType = JavaType.constructType(Long.class).withTypeHandler(handler);

        MapLikeType newMapLikeType = mapLikeType.withContentTypeHandler(newValueType);

        assertEquals(keyType, newMapLikeType.getKeyType());
        assertEquals(newValueType, newMapLikeType.getValueType());
        assertNotNull(newMapLikeType.getValueType().getTypeHandler());
        assertEquals(handler, newMapLikeType.getValueType().getTypeHandler());
    }

    @Test
    public void testMapLikeTypeWithValueHandler() {
        Class<Map<String, Integer>> rawType = Map.class;
        JavaType keyType = JavaType.constructType(String.class);
        JavaType valueType = JavaType.constructType(Integer.class);

        MapLikeType mapLikeType = new MapLikeType(rawType, null, null, null, keyType, valueType);
        Object handler = new Object() {};

        MapLikeType newMapLikeType = mapLikeType.withValueHandler(handler);

        assertNotNull(newMapLikeType.getValueHandler());
        assertEquals(handler, newMapLikeType.getValueHandler());
    }

    @Test
    public void testMapLikeTypeWithContentValueHandler() {
        Class<Map<String, Integer>> rawType = Map.class;
        JavaType keyType = JavaType.constructType(String.class);
        JavaType valueType = JavaType.constructType(Integer.class);

        MapLikeType mapLikeType = new MapLikeType(rawType, null, null, null, keyType, valueType);
        Object handler = new Object() {};

        JavaType newValueType = JavaType.constructType(Long.class).withValueHandler(handler);

        MapLikeType newMapLikeType = mapLikeType.withContentValueHandler(newValueType);

        assertEquals(keyType, newMapLikeType.getKeyType());
        assertEquals(newValueType, newMapLikeType.getValueType());
        assertNotNull(newMapLikeType.getValueType().getValueHandler());
        assertEquals(handler, newMapLikeType.getValueType().getValueHandler());
    }

    @Test
    public void testMapLikeTypeWithStaticTyping() {
        Class<Map<String, Integer>> rawType = Map.class;
        JavaType keyType = JavaType.constructType(String.class);
        JavaType valueType = JavaType.constructType(Integer.class);

        MapLikeType mapLikeType = new MapLikeType(rawType, null, null, null, keyType, valueType);

        MapLikeType newMapLikeType = mapLikeType.withStaticTyping();

        assertTrue(newMapLikeType.isStaticTyping());
    }
}