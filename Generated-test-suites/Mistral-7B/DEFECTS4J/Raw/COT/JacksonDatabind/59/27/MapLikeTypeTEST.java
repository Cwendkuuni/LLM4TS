package com.example.mapliketype; // please replace with your test package name

import static org.junit.Assert.*;

import org.junit.Test;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.MapLikeType;

public class MapLikeTypeTest {

    @Test
    public void testMapLikeTypeConstructor() {
        Class<Map<String, Integer>> mapClass = Map.class;
        JavaType mapType = MapLikeType.construct(mapClass, String.class, Integer.class);
        MapLikeType mapLikeType = (MapLikeType) mapType;
        assertEquals(mapClass, mapLikeType.getRawClass());
        assertEquals(String.class, mapLikeType.getKeyType());
        assertEquals(Integer.class, mapLikeType.getValueType());
        assertFalse(mapLikeType.isTrueMapType());
    }

    @Test
    public void testMapLikeTypeWithKeyType() {
        Class<Map<String, Integer>> mapClass = Map.class;
        JavaType mapType = MapLikeType.construct(mapClass, String.class, Integer.class);
        MapLikeType mapLikeType = (MapLikeType) mapType;
        JavaType newKeyType = JavaType.construct(String.class, Integer.class);
        MapLikeType newMapLikeType = mapLikeType.withKeyType(newKeyType);
        assertEquals(mapClass, newMapLikeType.getRawClass());
        assertEquals(newKeyType, newMapLikeType.getKeyType());
        assertEquals(Integer.class, newMapLikeType.getValueType());
        assertFalse(newMapLikeType.isTrueMapType());
    }

    @Test
    public void testMapLikeTypeWithValueType() {
        Class<Map<String, Integer>> mapClass = Map.class;
        JavaType mapType = MapLikeType.construct(mapClass, String.class, Integer.class);
        MapLikeType mapLikeType = (MapLikeType) mapType;
        JavaType newValueType = JavaType.construct(Double.class);
        MapLikeType newMapLikeType = mapLikeType.withContentType(newValueType);
        assertEquals(mapClass, newMapLikeType.getRawClass());
        assertEquals(String.class, newMapLikeType.getKeyType());
        assertEquals(newValueType, newMapLikeType.getValueType());
        assertFalse(newMapLikeType.isTrueMapType());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIllegalArgumentExceptionInConstruct() {
        JavaType baseType = JavaType.construct(String.class);
        JavaType keyType = JavaType.construct(String.class, Integer.class);
        JavaType valueType = JavaType.construct(String.class);
        MapLikeType.construct(baseType, keyType, valueType);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIllegalArgumentExceptionInUpgradeFrom() {
        JavaType baseType = JavaType.construct(String.class);
        JavaType keyType = JavaType.construct(String.class, Integer.class);
        JavaType valueType = JavaType.construct(String.class);
        MapLikeType mapLikeType = new MapLikeType(baseType, keyType, valueType);
        MapLikeType.upgradeFrom(baseType, keyType, valueType);
    }
}