package com.fasterxml.jackson.databind.type;

import org.junit.Test;
import static org.junit.Assert.*;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.MapLikeType;

public class MapLikeTypeTest {

    @Test
    public void testMapLikeTypeCreation() {
        // Test creating a new MapLikeType instance
        JavaType mapType = new TypeFactory().constructType(Map.class, String.class, Integer.class);
        MapLikeType mapLikeType = MapLikeType.upgradeFrom(mapType, String.class, Integer.class);

        assertEquals("Map<String, Integer>", mapLikeType.toString());
        assertEquals(String.class, mapLikeType.getKeyType());
        assertEquals(Integer.class, mapLikeType.getContentType());
        assertTrue(mapLikeType.isMapLikeType());
        assertFalse(mapLikeType.isTrueMapType());
    }

    @Test
    public void testMapLikeTypeProperties() {
        // Test getting properties from a MapLikeType instance
        JavaType mapType = new TypeFactory().constructType(Map.class, String.class, Integer.class);
        MapLikeType mapLikeType = MapLikeType.upgradeFrom(mapType, String.class, Integer.class);

        JavaType keyType = mapLikeType.getKeyType();
        JavaType valueType = mapLikeType.getContentType();

        assertEquals(String.class, keyType);
        assertEquals(Integer.class, valueType);

        // Test properties after changing key and value types
        JavaType newKeyType = new TypeFactory().constructType(String[].class);
        JavaType newValueType = new TypeFactory().constructType(List.class, Integer.class);

        MapLikeType newMapLikeType = mapLikeType.withKeyType(newKeyType).withContentType(newValueType);

        assertEquals(newKeyType, newMapLikeType.getKeyType());
        assertEquals(newValueType, newMapLikeType.getContentType());
    }
}