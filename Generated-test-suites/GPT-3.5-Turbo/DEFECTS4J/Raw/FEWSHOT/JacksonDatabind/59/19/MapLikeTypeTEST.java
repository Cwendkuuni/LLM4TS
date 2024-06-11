package com.fasterxml.jackson.databind.type;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.fasterxml.jackson.databind.JavaType;

public class MapLikeTypeTest {

    private final JavaType keyType = TypeFactory.defaultInstance().constructType(String.class);
    private final JavaType valueType = TypeFactory.defaultInstance().constructType(Integer.class);

    @Test
    public void testIsTrueMapType() {
        MapLikeType mapType = MapLikeType.upgradeFrom(TypeFactory.defaultInstance().constructType(Map.class), keyType, valueType);
        assertTrue(mapType.isTrueMapType());

        mapType = MapLikeType.upgradeFrom(TypeFactory.defaultInstance().constructType(HashMap.class), keyType, valueType);
        assertTrue(mapType.isTrueMapType());

        mapType = MapLikeType.upgradeFrom(TypeFactory.defaultInstance().constructType(ArrayList.class), keyType, valueType);
        assertFalse(mapType.isTrueMapType());
    }

    @Test
    public void testKeyTypeHandler() {
        MapLikeType mapType = MapLikeType.upgradeFrom(TypeFactory.defaultInstance().constructType(Map.class), keyType, valueType);
        assertNull(mapType.getKeyType().getTypeHandler());

        TypeHandler keyTypeHandler = new TypeHandler();
        mapType = mapType.withKeyTypeHandler(keyTypeHandler);
        assertEquals(keyTypeHandler, mapType.getKeyType().getTypeHandler());
    }

    @Test
    public void testKeyValueHandler() {
        MapLikeType mapType = MapLikeType.upgradeFrom(TypeFactory.defaultInstance().constructType(Map.class), keyType, valueType);
        assertNull(mapType.getKeyType().getValueHandler());

        ValueHandler keyValueHandler = new ValueHandler();
        mapType = mapType.withKeyValueHandler(keyValueHandler);
        assertEquals(keyValueHandler, mapType.getKeyType().getValueHandler());
    }
}

class TypeHandler {}

class ValueHandler {}