package com.example.junit;

import static org.junit.Assert.*;

import java.util.Arrays;

import org.junit.Test;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.MapLikeType;

public class MapLikeTypeTest {

    @Test
    public void testConstruct() {
        JavaType mapType = Map.class.getTypeParameters()[0];
        JavaType keyType = String.class;
        JavaType valueType = Integer.class;
        MapLikeType mapLikeType = MapLikeType.construct(mapType.getRawType(), keyType, valueType);
        assertEquals(mapType.getRawType(), mapLikeType.getRawClass());
        assertEquals(keyType, mapLikeType.getKeyType());
        assertEquals(valueType, mapLikeType.getValueType());
    }

    @Test
    public void testUpgradeFrom() {
        JavaType listType = Arrays.asList(String.class, Integer.class).getType();
        JavaType keyType = String.class;
        JavaType valueType = Integer.class;
        MapLikeType mapLikeType = MapLikeType.upgradeFrom(listType, keyType, valueType);
        assertEquals(Map.class, mapLikeType.getRawClass());
        assertEquals(keyType, mapLikeType.getKeyType());
        assertEquals(valueType, mapLikeType.getValueType());
    }

    @Test
    public void testIsTrueMapType() {
        JavaType mapType = Map.class.getTypeParameters()[0];
        JavaType keyType = String.class;
        JavaType valueType = Integer.class;
        MapLikeType mapLikeType = new MapLikeType(mapType, null, null, null, keyType, valueType, null, null, false);
        assertTrue(mapLikeType.isTrueMapType());
    }

    @Test
    public void testIsNotTrueMapType() {
        JavaType listType = Arrays.asList(String.class, Integer.class).getType();
        JavaType keyType = String.class;
        JavaType valueType = Integer.class;
        MapLikeType mapLikeType = new MapLikeType(listType, null, null, null, keyType, valueType, null, null, false);
        assertFalse(mapLikeType.isTrueMapType());
    }

    @Test
    public void testWithKeyType() {
        JavaType mapType = Map.class.getTypeParameters()[0];
        JavaType oldKeyType = String.class;
        JavaType newKeyType = Integer.class;
        MapLikeType mapLikeType = new MapLikeType(mapType, null, null, null, oldKeyType, null, null, null, false);
        MapLikeType newMapLikeType = mapLikeType.withKeyType(newKeyType);
        assertEquals(newKeyType, newMapLikeType.getKeyType());
        assertNotEquals(oldKeyType, newMapLikeType.getKeyType());
        assertEquals(mapType, newMapLikeType.getRawClass());
    }

    @Test
    public void testWithContentType() {
        JavaType mapType = Map.class.getTypeParameters()[0];
        JavaType oldValueType = Integer.class;
        JavaType newValueType = Double.class;
        MapLikeType mapLikeType = new MapLikeType(mapType, null, null, null, null, oldValueType, null, null, false);
        MapLikeType newMapLikeType = mapLikeType.withContentType(newValueType);
        assertEquals(newValueType, newMapLikeType.getValueType());
        assertNotEquals(oldValueType, newMapLikeType.getValueType());
        assertEquals(mapType, newMapLikeType.getRawClass());
    }

    @Test
    public void testWithTypeHandler() {
        JavaType mapType = Map.class.getTypeParameters()[0];
        Object oldValueHandler = null;
        Object newValueHandler = new Object() {};
        MapLikeType mapLikeType = new MapLikeType(mapType, null, null, null, null, null, oldValueHandler, null, false);
        MapLikeType newMapLikeType = mapLikeType.withTypeHandler(newValueHandler);
        assertEquals(newValueHandler, newMapLikeType.getTypeHandler());
        assertNotEquals(oldValueHandler, newMapLikeType.getTypeHandler());
        assertEquals(mapType, newMapLikeType.getRawClass());
    }

    @Test
    public void testWithContentTypeHandler() {
        JavaType mapType = Map.class.getTypeParameters()[0];
        Object oldContentValueHandler = null;
        Object newContentValueHandler = new Object() {};
        MapLikeType mapLikeType = new MapLikeType(mapType, null, null, null, null, null, null, oldContentValueHandler, false);
        MapLikeType newMapLikeType = mapLikeType.withContentTypeHandler(newContentValueHandler);
        assertEquals(newContentValueHandler, newMapLikeType.getValueHandler());
        assertNotEquals(oldContentValueHandler, newMapLikeType.getValueHandler());
        assertEquals(mapType, newMapLikeType.getRawClass());
    }

    @Test
    public void testWithKeyTypeHandler() {
        JavaType mapType = Map.class.getTypeParameters()[0];
        Object oldKeyHandler = null;
        Object newKeyHandler = new Object() {};
        MapLikeType mapLikeType = new MapLikeType(mapType, null, null, null, null, null, null, oldKeyHandler, false);
        MapLikeType newMapLikeType = mapLikeType.withKeyTypeHandler(newKeyHandler);
        assertEquals(newKeyHandler, newMapLikeType.getTypeHandler());
        assertNotEquals(oldKeyHandler, newMapLikeType.getTypeHandler());
        assertEquals(mapType, newMapLikeType.getRawClass());
    }

    @Test
    public void testWithKeyValueHandler() {
        JavaType mapType = Map.class.getTypeParameters()[0];
        Object oldKeyHandler = null;
        Object oldValueHandler = null;
        Object newKeyHandler = new Object() {};
        Object newValueHandler = new Object() {};
        MapLikeType mapLikeType = new MapLikeType(mapType, null, null, null, null, null, oldKeyHandler, oldValueHandler, false);
        MapLikeType newMapLikeType = mapLikeType.withKeyValueHandler(newKeyHandler, newValueHandler);
        assertEquals(newKeyHandler, newMapLikeType.getKeyHandler());
        assertNotEquals(oldKeyHandler, newMapLikeType.getKeyHandler());
        assertEquals(newValueHandler, newMapLikeType.getValueHandler());
        assertNotEquals(oldValueHandler, newMapLikeType.getValueHandler());
        assertEquals(mapType, newMapLikeType.getRawClass());
    }

    @Test
    public void testIsContainerType() {
        JavaType mapType = Map.class.getTypeParameters()[0];
        MapLikeType mapLikeType = new MapLikeType(mapType, null, null, null, null, null, null, null, false);
        assertTrue(mapLikeType.isContainerType());
    }

    @Test
    public void testIsMapLikeType() {
        JavaType mapType = Map.class.getTypeParameters()[0];
        MapLikeType mapLikeType = new MapLikeType(mapType, null, null, null, null, null, null, null, false);
        assertTrue(mapLikeType.isMapLikeType());
    }

    @Test
    public void testHasHandlers() {
        JavaType mapType = Map.class.getTypeParameters()[0];
        MapLikeType mapLikeType1 = new MapLikeType(mapType, null, null, null, null, null, null, null, false);
        assertFalse(mapLikeType1.hasHandlers());

        Object keyHandler = new Object() {};
        Object valueHandler = new Object() {};
        MapLikeType mapLikeType2 = mapLikeType1.withKeyTypeHandler(keyHandler).withValueTypeHandler(valueHandler);
        assertTrue(mapLikeType2.hasHandlers());
    }
}