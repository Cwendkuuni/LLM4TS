package com.example.mapliketype;

import static org.junit.Assert.*;

import org.junit.Test;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.MapLikeType;

public class MapLikeTypeTest {

    // Basic test cases for constructors

    @Test
    public void testConstruct() {
        JavaType keyType = new JavaType(String.class);
        JavaType valueType = new JavaType(Integer.class);
        MapLikeType mapLikeType = MapLikeType.construct(Map.class, keyType, valueType);
        assertEquals(Map.class, mapLikeType.getRawType());
        assertEquals(keyType, mapLikeType.getKeyType());
        assertEquals(valueType, mapLikeType.getContentType());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructWithInvalidBaseType() {
        JavaType keyType = new JavaType(String.class);
        JavaType valueType = new JavaType(Integer.class);
        MapLikeType mapLikeType = MapLikeType.construct(List.class, keyType, valueType);
    }

    // Basic test cases for methods

    @Test
    public void testGetKeyType() {
        JavaType keyType = new JavaType(String.class);
        JavaType valueType = new JavaType(Integer.class);
        MapLikeType mapLikeType = new MapLikeType(Map.class, null, null, null, keyType, valueType, null, null, false);
        assertEquals(keyType, mapLikeType.getKeyType());
    }

    @Test
    public void testGetContentType() {
        JavaType keyType = new JavaType(String.class);
        JavaType valueType = new JavaType(Integer.class);
        MapLikeType mapLikeType = new MapLikeType(Map.class, null, null, null, keyType, valueType, null, null, false);
        assertEquals(valueType, mapLikeType.getContentType());
    }

    @Test
    public void testIsContainerType() {
        JavaType keyType = new JavaType(String.class);
        JavaType valueType = new JavaType(Integer.class);
        MapLikeType mapLikeType = new MapLikeType(Map.class, null, null, null, keyType, valueType, null, null, false);
        assertTrue(mapLikeType.isContainerType());
    }

    @Test
    public void testIsMapLikeType() {
        JavaType keyType = new JavaType(String.class);
        JavaType valueType = new JavaType(Integer.class);
        MapLikeType mapLikeType = new MapLikeType(Map.class, null, null, null, keyType, valueType, null, null, false);
        assertTrue(mapLikeType.isMapLikeType());
    }

    @Test
    public void testHasHandlers() {
        JavaType keyType = new JavaType(String.class);
        JavaType valueType = new JavaType(Integer.class);
        MapLikeType mapLikeType = new MapLikeType(Map.class, null, null, null, keyType, valueType, null, null, false);
        assertTrue(mapLikeType.hasHandlers());
    }

    // Edge cases and exception handling test cases

    @Test(expected = IllegalArgumentException.class)
    public void testUpgradeFromInvalidBaseType() {
        JavaType keyType = new JavaType(String.class);
        JavaType valueType = new JavaType(Integer.class);
        MapLikeType mapLikeType = new MapLikeType(String.class, null, null, null, keyType, valueType);
        MapLikeType upgradedType = MapLikeType.upgradeFrom(Integer.class, keyType, valueType);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructWithInvalidTypeArguments() {
        JavaType keyType = new JavaType(String.class);
        JavaType valueType = new JavaType(Integer[].class);
        MapLikeType mapLikeType = MapLikeType.construct(Map.class, keyType, valueType);
    }

    @Test
    public void testWithKeyType() {
        JavaType originalKeyType = new JavaType(String.class);
        JavaType newKeyType = new JavaType(Integer.class);
        JavaType valueType = new JavaType(Integer.class);
        MapLikeType mapLikeType = new MapLikeType(Map.class, null, null, null, originalKeyType, valueType, null, null, false);
        MapLikeType newMapLikeType = mapLikeType.withKeyType(newKeyType);
        assertEquals(newKeyType, newMapLikeType.getKeyType());
        assertNotEquals(originalKeyType, newMapLikeType.getKeyType());
    }

    @Test
    public void testWithContentType() {
        JavaType originalValueType = new JavaType(Integer.class);
        JavaType newValueType = new JavaType(Double.class);
        JavaType keyType = new JavaType(String.class);
        MapLikeType mapLikeType = new MapLikeType(Map.class, null, null, null, keyType, originalValueType, null, null, false);
        MapLikeType newMapLikeType = mapLikeType.withContentType(newValueType);
        assertEquals(newValueType, newMapLikeType.getContentType());
        assertNotEquals(originalValueType, newMapLikeType.getContentType());
    }

    @Test
    public void testWithTypeHandler() {
        Object handler = new Object();
        JavaType valueType = new JavaType(Integer.class);
        MapLikeType mapLikeType = new MapLikeType(Map.class, null, null, null, null, valueType, null, null, false);
        MapLikeType newMapLikeType = mapLikeType.withTypeHandler(handler);
        assertNotNull(newMapLikeType.getTypeHandler());
        assertEquals(handler, newMapLikeType.getTypeHandler());
    }

    @Test(expected = NullPointerException.class)
    public void testWithContentTypeHandlerNull() {
        Object handler = new Object();
        JavaType keyType = new JavaType(String.class);
        JavaType valueType = new JavaType(Integer.class);
        MapLikeType mapLikeType = new MapLikeType(Map.class, null, null, null, keyType, valueType, null, handler, false);
        mapLikeType.withContentTypeHandler(null);
    }

    @Test
    public void testWithValueHandler() {
        Object handler = new Object();
        JavaType valueType = new JavaType(Integer.class);
        MapLikeType mapLikeType = new MapLikeType(Map.class, null, null, null, null, valueType, null, null, false);
        MapLikeType newMapLikeType = mapLikeType.withValueHandler(handler);
        assertNotNull(newMapLikeType.getValueHandler());
        assertEquals(handler, newMapLikeType.getValueHandler());
    }

    @Test(expected = NullPointerException.class)
    public void testWithContentValueHandlerNull() {
        Object handler = new Object();
        JavaType keyType = new JavaType(String.class);
        JavaType valueType = new JavaType(Integer.class);
        MapLikeType mapLikeType = new MapLikeType(Map.class, null, null, null, keyType, valueType, null, handler, false);
        mapLikeType.withContentValueHandler(null);
    }

    @Test
    public void testWithKeyTypeHandler() {
        Object handler = new Object();
        JavaType keyType = new JavaType(Integer.class);
        JavaType valueType = new JavaType(Integer.class);
        MapLikeType mapLikeType = new MapLikeType(Map.class, null, null, null, null, valueType, null, null, false);
        MapLikeType newMapLikeType = mapLikeType.withKeyTypeHandler(handler);
        assertNotNull(newMapLikeType.getKeyTypeHandler());
        assertEquals(handler, newMapLikeType.getKeyTypeHandler());
    }

    @Test(expected = NullPointerException.class)
    public void testWithKeyValueHandlerNull() {
        Object handler = new Object();
        JavaType keyType = new JavaType(Integer.class);
        JavaType valueType = new JavaType(Integer.class);
        MapLikeType mapLikeType = new MapLikeType(Map.class, null, null, null, keyType, valueType, null, handler, false);
        mapLikeType.withKeyValueHandler(null);
    }

    @Test
    public void testIsTrueMapType() {
        JavaType keyType = new JavaType(String.class);
        JavaType valueType = new JavaType(Integer.class);
        MapLikeType mapLikeType = new MapLikeType(Map.class, null, null, null, keyType, valueType, null, null, false);
        assertTrue(mapLikeType.isTrueMapType());
    }

    @Test
    public void testIsTrueMapTypeWithList() {
        JavaType keyType = new JavaType(String.class);
        JavaType valueType = new JavaType(Integer.class);
        MapLikeType mapLikeType = new MapLikeType(List.class, null, null, null, keyType, valueType, null, null, false);
        assertFalse(mapLikeType.isTrueMapType());
    }
}