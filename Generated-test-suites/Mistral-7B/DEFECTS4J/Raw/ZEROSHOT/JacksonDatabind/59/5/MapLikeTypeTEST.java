package com.fasterxml.jackson.databind.type;

import static org.junit.Assert.*;

import java.lang.reflect.TypeVariable;
import java.util.*;

import org.junit.Test;

import com.fasterxml.jackson.databind.JavaType;

public class MapLikeTypeTest {

    @Test
    public void testMapLikeTypeConstruction() {
        JavaType keyType = new BasicType(String.class);
        JavaType valueType = new BasicType(Integer.class);
        MapLikeType mapLikeType = new MapLikeType(Map.class, null, null, null,
                keyType, valueType);
        assertEquals(Map.class, mapLikeType.getRawClass());
        assertEquals(keyType, mapLikeType.getKeyType());
        assertEquals(valueType, mapLikeType.getValueType());
    }

    @Test
    public void testMapLikeTypeUpgradeFrom() {
        JavaType baseType = new BasicType(HashMap.class);
        JavaType keyType = new BasicType(String.class);
        JavaType valueType = new BasicType(Integer.class);
        MapLikeType mapLikeType = MapLikeType.upgradeFrom(baseType, keyType,
                valueType);
        assertEquals(Map.class, mapLikeType.getRawClass());
        assertEquals(keyType, mapLikeType.getKeyType());
        assertEquals(valueType, mapLikeType.getValueType());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testMapLikeTypeUpgradeFromInvalidType() {
        JavaType baseType = new BasicType(Integer.class);
        JavaType keyType = new BasicType(String.class);
        JavaType valueType = new BasicType(Integer.class);
        MapLikeType.upgradeFrom(baseType, keyType, valueType);
    }

    @Test
    public void testMapLikeTypeFactoryMethods() {
        JavaType keyType = new BasicType(String.class);
        JavaType valueType = new BasicType(Integer.class);
        MapLikeType mapLikeType = new MapLikeType(Map.class, null, null, null,
                keyType, valueType);

        MapLikeType withKeyType = mapLikeType.withKeyType(new BasicType(Long.class));
        MapLikeType withValueType = mapLikeType.withValueType(new BasicType(Float.class));
        MapLikeType withKeyTypeHandler = mapLikeType.withKeyTypeHandler(null);
        MapLikeType withValueHandler = mapLikeType.withValueHandler(null);
        MapLikeType withStaticTyping = mapLikeType.withStaticTyping();

        assertEquals(mapLikeType, mapLikeType);
        assertNotEquals(mapLikeType, null);
        assertNotEquals(mapLikeType, new Object());

        assertNotEquals(mapLikeType, withKeyType);
        assertNotEquals(mapLikeType, withValueType);
        assertNotEquals(mapLikeType, withKeyTypeHandler);
        assertNotEquals(mapLikeType, withValueHandler);
        assertNotEquals(mapLikeType, withStaticTyping);

        assertEquals(mapLikeType.getRawClass(), withKeyType.getRawClass());
        assertNotEquals(mapLikeType.getKeyType(), withKeyType.getKeyType());
        assertNotEquals(mapLikeType.getValueType(), withKeyType.getValueType());

        assertEquals(mapLikeType.getKeyType(), withKeyTypeHandler.getKeyType());
        assertNull(withKeyTypeHandler.getValueType());

        assertNull(mapLikeType.getValueHandler());
        assertNull(mapLikeType.getTypeHandler());
        assertFalse(mapLikeType.isStatic());

        assertTrue(withKeyTypeHandler.isStatic());
        assertNull(withKeyTypeHandler.getValueType());
        assertNull(withKeyTypeHandler.getKeyType());
        assertNull(withKeyTypeHandler.getValueHandler());
        assertNull(withKeyTypeHandler.getTypeHandler());
    }

    @Test
    public void testMapLikeTypeIsContainerType() {
        JavaType keyType = new BasicType(String.class);
        JavaType valueType = new BasicType(Integer.class);
        MapLikeType mapLikeType = new MapLikeType(Map.class, null, null, null,
                keyType, valueType);
        assertTrue(mapLikeType.isContainerType());
        assertTrue(mapLikeType.isMapLikeType());
    }

    @Test
    public void testMapLikeTypeIsTrueMapType() {
        JavaType keyType = new BasicType(String.class);
        JavaType valueType = new BasicType(Integer.class);
        MapLikeType mapLikeType = new MapLikeType(Map.class, null, null, null,
                keyType, valueType);
        assertTrue(mapLikeType.isTrueMapType());

        JavaType listType = new BasicType(ArrayList.class);
        JavaType listLikeType = new MapLikeType(List.class, null, null, null,
                keyType, valueType);
        assertFalse(listLikeType.isTrueMapType());
    }

    @Test
    public void testMapLikeTypeHashCode() {
        JavaType keyType = new BasicType(String.class);
        JavaType valueType = new BasicType(Integer.class);
        MapLikeType mapLikeType1 = new MapLikeType(Map.class, null, null, null,
                keyType, valueType);
        MapLikeType mapLikeType2 = new MapLikeType(Map.class, null, null, null,
                keyType, valueType);

        assertEquals(mapLikeType1.hashCode(), mapLikeType2.hashCode());

        JavaType differentKeyType = new BasicType(Long.class);
        MapLikeType differentMapLikeType = new MapLikeType(Map.class, null, null,
                null, differentKeyType, valueType);
        assertNotEquals(mapLikeType1.hashCode(), differentMapLikeType.hashCode());
    }

    @Test
    public void testMapLikeTypeToString() {
        JavaType keyType = new BasicType(String.class);
        JavaType valueType = new BasicType(Integer.class);
        MapLikeType mapLikeType = new MapLikeType(Map.class, null, null, null,
                keyType, valueType);
        String toString = mapLikeType.toString();
        assertTrue(toString.startsWith("[map-like type; class"));
        assertTrue(toString.endsWith("]"));
        assertTrue(toString.contains("Map"));
        assertTrue(toString.contains("String"));
        assertTrue(toString.contains("Integer"));
    }
}