package com.example.mapliketype;

import static org.junit.Assert.*;

import org.junit.Test;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.MapLikeType;

public class MapLikeTypeTest {

    // Basic test cases for constructors

    @Test
    public void testMapLikeTypeConstruction() {
        JavaType keyType = new JavaType(String.class);
        JavaType valueType = new JavaType(Integer.class);
        MapLikeType mapLikeType = new MapLikeType(null, null, null, null, keyType,
                valueType);
        assertEquals("com.fasterxml.jackson.databind.type.MapLikeType",
                mapLikeType.getClass().getName());
        assertEquals(keyType, mapLikeType.getKeyType());
        assertEquals(valueType, mapLikeType.getContentType());
    }

    @Test
    public void testMapLikeTypeStaticConstruction() {
        JavaType keyType = new JavaType(String.class);
        JavaType valueType = new JavaType(Integer.class);
        MapLikeType mapLikeType = MapLikeType.upgradeFrom(null, keyType, valueType);
        assertEquals("com.fasterxml.jackson.databind.type.MapLikeType",
                mapLikeType.getClass().getName());
        assertEquals(keyType, mapLikeType.getKeyType());
        assertEquals(valueType, mapLikeType.getContentType());
    }

    // Test methods for checking type properties

    @Test
    public void testIsContainerType() {
        JavaType keyType = new JavaType(String.class);
        JavaType valueType = new JavaType(Integer.class);
        MapLikeType mapLikeType = new MapLikeType(null, null, null, null, keyType,
                valueType);
        assertTrue(mapLikeType.isContainerType());
    }

    @Test
    public void testIsMapLikeType() {
        JavaType keyType = new JavaType(String.class);
        JavaType valueType = new JavaType(Integer.class);
        MapLikeType mapLikeType = new MapLikeType(null, null, null, null, keyType,
                valueType);
        assertTrue(mapLikeType.isMapLikeType());
    }

    @Test
    public void testGetKeyType() {
        JavaType keyType = new JavaType(String.class);
        JavaType valueType = new JavaType(Integer.class);
        MapLikeType mapLikeType = new MapLikeType(null, null, null, null, keyType,
                valueType);
        assertEquals(keyType, mapLikeType.getKeyType());
    }

    @Test
    public void testGetContentType() {
        JavaType keyType = new JavaType(String.class);
        JavaType valueType = new JavaType(Integer.class);
        MapLikeType mapLikeType = new MapLikeType(null, null, null, null, keyType,
                valueType);
        assertEquals(valueType, mapLikeType.getContentType());
    }

    @Test
    public void testGetContentValueHandler() {
        JavaType keyType = new JavaType(String.class);
        JavaType valueType = new JavaType(Integer.class);
        MapLikeType mapLikeType = new MapLikeType(null, null, null, null, keyType,
                valueType);
        assertNull(mapLikeType.getContentValueHandler());
    }

    @Test
    public void testHasHandlers() {
        JavaType keyType = new JavaType(String.class);
        JavaType valueType = new JavaType(Integer.class);
        MapLikeType mapLikeType = new MapLikeType(null, null, null, null, keyType,
                valueType);
        assertFalse(mapLikeType.hasHandlers());
    }

    // Test methods for handling type modifications

    @Test
    public void testWithKeyType() {
        JavaType keyType = new JavaType(String.class);
        JavaType valueType = new JavaType(Integer.class);
        JavaType newKeyType = new JavaType(Integer.class);
        MapLikeType mapLikeType = new MapLikeType(null, null, null, null, keyType,
                valueType);
        MapLikeType newMapLikeType = mapLikeType.withKeyType(newKeyType);
        assertNotSame(mapLikeType, newMapLikeType);
        assertEquals(newKeyType, newMapLikeType.getKeyType());
        assertEquals(valueType, newMapLikeType.getContentType());
    }

    @Test
    public void testWithContentType() {
        JavaType keyType = new JavaType(String.class);
        JavaType valueType = new JavaType(Integer.class);
        JavaType newValueType = new JavaType(Double.class);
        MapLikeType mapLikeType = new MapLikeType(null, null, null, null, keyType,
                valueType);
        MapLikeType newMapLikeType = mapLikeType.withContentType(newValueType);
        assertNotSame(mapLikeType, newMapLikeType);
        assertEquals(keyType, newMapLikeType.getKeyType());
        assertEquals(newValueType, newMapLikeType.getContentType());
    }

    @Test
    public void testWithTypeHandler() {
        JavaType keyType = new JavaType(String.class);
        JavaType valueType = new JavaType(Integer.class);
        Object handler = new Object();
        MapLikeType mapLikeType = new MapLikeType(null, null, null, null, keyType,
                valueType);
        MapLikeType newMapLikeType = mapLikeType.withTypeHandler(handler);
        assertNotSame(mapLikeType, newMapLikeType);
        assertEquals(keyType, newMapLikeType.getKeyType());
        assertEquals(valueType, newMapLikeType.getContentType());
        assertEquals(handler, newMapLikeType.getTypeHandler());
    }

    @Test
    public void testWithContentTypeHandler() {
        JavaType keyType = new JavaType(String.class);
        JavaType valueType = new JavaType(Integer.class);
        Object handler = new Object();
        MapLikeType mapLikeType = new MapLikeType(null, null, null, null, keyType,
                valueType);
        MapLikeType newMapLikeType = mapLikeType.withContentTypeHandler(handler);
        assertNotSame(mapLikeType, newMapLikeType);
        assertEquals(keyType, newMapLikeType.getKeyType());
        assertEquals(valueType, newMapLikeType.getContentType());
        assertEquals(handler, newMapLikeType.getContentTypeHandler());
    }

    @Test
    public void testWithValueHandler() {
        JavaType keyType = new JavaType(String.class);
        JavaType valueType = new JavaType(Integer.class);
        Object handler = new Object();
        MapLikeType mapLikeType = new MapLikeType(null, null, null, null, keyType,
                valueType);
        MapLikeType newMapLikeType = mapLikeType.withValueHandler(handler);
        assertNotSame(mapLikeType, newMapLikeType);
        assertEquals(keyType, newMapLikeType.getKeyType());
        assertEquals(valueType, newMapLikeType.getContentType());
        assertEquals(handler, newMapLikeType.getValueHandler());
    }

    @Test
    public void testWithContentValueHandler() {
        JavaType keyType = new JavaType(String.class);
        JavaType valueType = new JavaType(Integer.class);
        Object handler = new Object();
        MapLikeType mapLikeType = new MapLikeType(null, null, null, null, keyType,
                valueType);
        MapLikeType newMapLikeType = mapLikeType.withContentValueHandler(handler);
        assertNotSame(mapLikeType, newMapLikeType);
        assertEquals(keyType, newMapLikeType.getKeyType());
        assertEquals(valueType, newMapLikeType.getContentType());
        assertEquals(handler, newMapLikeType.getContentValueHandler());
    }

    @Test
    public void testWithStaticTyping() {
        JavaType keyType = new JavaType(String.class);
        JavaType valueType = new JavaType(Integer.class);
        MapLikeType mapLikeType = new MapLikeType(null, null, null, null, keyType,
                valueType);
        MapLikeType newMapLikeType = mapLikeType.withStaticTyping();
        assertNotSame(mapLikeType, newMapLikeType);
        assertTrue(newMapLikeType.isStaticTyping());
    }

    // Test methods for refining types

    @Test
    public void testRefine() {
        JavaType keyType = new JavaType(String.class);
        JavaType valueType = new JavaType(Integer.class);
        JavaType refinedKeyType = new JavaType(Character.class);
        JavaType refinedValueType = new JavaType(Double.class);
        MapLikeType mapLikeType = new MapLikeType(null, null, null, null, keyType,
                valueType);
        JavaType refinedMapLikeType = mapLikeType.refine(null, null, null, null);
        assertNotSame(mapLikeType, refinedMapLikeType);
        assertEquals(refinedKeyType, refinedMapLikeType.getKeyType());
        assertEquals(refinedValueType, refinedMapLikeType.getContentType());
    }

    // Edge cases and exception handling tests

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidUpgradeFrom() {
        JavaType baseType = new JavaType(List.class);
        JavaType keyType = new JavaType(String.class);
        JavaType valueType = new JavaType(Integer.class);
        MapLikeType.upgradeFrom(baseType, keyType, valueType);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidRefine() {
        JavaType keyType = new JavaType(String.class);
        JavaType valueType = new JavaType(Integer.class);
        MapLikeType mapLikeType = new MapLikeType(null, null, null, null, keyType,
                valueType);
        mapLikeType.refine(null, null, null, new JavaType(Object.class));
    }
}