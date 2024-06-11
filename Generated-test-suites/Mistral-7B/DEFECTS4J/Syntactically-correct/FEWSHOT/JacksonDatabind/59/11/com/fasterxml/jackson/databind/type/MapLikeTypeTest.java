package org.apache.commons.cli.test;

import static org.junit.Assert.*;

import org.junit.Test;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.MapLikeType;

public class MapLikeTypeTest {

    @Test
    public void testMapLikeTypeCreation() {
        Class<?> mapType = Map.class;
        JavaType keyType = JavaType.construct(int[].class);
        JavaType valueType = JavaType.construct(String.class);
        MapLikeType mapLikeType = new MapLikeType(mapType, null, null, null, keyType, valueType);
        assertEquals(mapType, mapLikeType.getClass());
        assertEquals(keyType, mapLikeType.getKeyType());
        assertEquals(valueType, mapLikeType.getValueType());
    }

    @Test
    public void testMapLikeTypeUpgrade() {
        Class<?> baseType = List.class;
        JavaType keyType = JavaType.construct(int[].class);
        JavaType valueType = JavaType.construct(String.class);
        JavaType mapLikeType = MapLikeType.upgradeFrom(JavaType.construct(baseType), keyType, valueType);
        assertEquals(MapLikeType.class, mapLikeType.getClass());
        assertEquals(keyType, mapLikeType.getKeyType());
        assertEquals(valueType, mapLikeType.getValueType());
    }

    @Test
    public void testMapLikeTypeWithKeyType() {
        Class<?> mapType = Map.class;
        JavaType keyType = JavaType.construct(int[].class);
        JavaType valueType = JavaType.construct(String.class);
        MapLikeType mapLikeType = new MapLikeType(mapType, null, null, null, keyType, valueType);
        MapLikeType newMapLikeType = mapLikeType.withKeyType(JavaType.construct(String.class));
        assertEquals(mapType, newMapLikeType.getClass());
        assertNotSame(keyType, newMapLikeType.getKeyType());
        assertEquals(valueType, newMapLikeType.getValueType());
    }

    @Test
    public void testMapLikeTypeWithContentType() {
        Class<?> mapType = Map.class;
        JavaType keyType = JavaType.construct(int[].class);
        JavaType valueType = JavaType.construct(String.class);
        JavaType newValueType = JavaType.construct(Integer.class);
        MapLikeType mapLikeType = new MapLikeType(mapType, null, null, null, keyType, valueType);
        MapLikeType newMapLikeType = mapLikeType.withContentType(newValueType);
        assertEquals(mapType, newMapLikeType.getClass());
        assertEquals(keyType, newMapLikeType.getKeyType());
        assertNotSame(valueType, newMapLikeType.getValueType());
        assertEquals(newValueType, newMapLikeType.getValueType());
    }

    @Test
    public void testMapLikeTypeIsMapLikeType() {
        Class<?> mapType = Map.class;
        JavaType keyType = JavaType.construct(int[].class);
        JavaType valueType = JavaType.construct(String.class);
        MapLikeType mapLikeType = new MapLikeType(mapType, null, null, null, keyType, valueType);
        assertTrue(mapLikeType.isMapLikeType());
    }

    @Test
    public void testMapLikeTypeIsContainerType() {
        Class<?> mapType = Map.class;
        JavaType keyType = JavaType.construct(int[].class);
        JavaType valueType = JavaType.construct(String.class);
        MapLikeType mapLikeType = new MapLikeType(mapType, null, null, null, keyType, valueType);
        assertTrue(mapLikeType.isContainerType());
    }

    @Test
    public void testMapLikeTypeGetKeyType() {
        Class<?> mapType = Map.class;
        JavaType keyType = JavaType.construct(int[].class);
        JavaType valueType = JavaType.construct(String.class);
        MapLikeType mapLikeType = new MapLikeType(mapType, null, null, null, keyType, valueType);
        assertEquals(keyType, mapLikeType.getKeyType());
    }

    @Test
    public void testMapLikeTypeGetContentType() {
        Class<?> mapType = Map.class;
        JavaType keyType = JavaType.construct(int[].class);
        JavaType valueType = JavaType.construct(String.class);
        MapLikeType mapLikeType = new MapLikeType(mapType, null, null, null, keyType, valueType);
        assertEquals(valueType, mapLikeType.getValueType());
    }

    @Test
    public void testMapLikeTypeHasHandlers() {
        Class<?> mapType = Map.class;
        JavaType keyType = JavaType.construct(int[].class);
        JavaType valueType = JavaType.construct(String.class);
        MapLikeType mapLikeType = new MapLikeType(mapType, null, null, null, keyType, valueType);
        assertTrue(mapLikeType.hasHandlers());
    }

    @Test
    public void testMapLikeTypeToString() {
        Class<?> mapType = Map.class;
        JavaType keyType = JavaType.construct(int[].class);
        JavaType valueType = JavaType.construct(String.class);
        MapLikeType mapLikeType = new MapLikeType(mapType, null, null, null, keyType, valueType);
        assertEquals("[map-like type; class " + mapType.getName() + ", int[] -> String]", mapLikeType.toString());
    }

    @Test
    public void testMapLikeTypeEquals() {
        Class<?> mapType = Map.class;
        JavaType keyType1 = JavaType.construct(int[].class);
        JavaType valueType1 = JavaType.construct(String.class);
        JavaType keyType2 = JavaType.construct(int[].class);
        JavaType valueType2 = JavaType.construct(String.class);
        MapLikeType mapLikeType1 = new MapLikeType(mapType, null, null, null, keyType1, valueType1);
        MapLikeType mapLikeType2 = new MapLikeType(mapType, null, null, null, keyType2, valueType2);
        MapLikeType mapLikeType3 = new MapLikeType(mapType, null, null, null, keyType1, valueType1);
        assertTrue(mapLikeType1.equals(mapLikeType3));
        assertFalse(mapLikeType1.equals(mapLikeType2));
    }
}