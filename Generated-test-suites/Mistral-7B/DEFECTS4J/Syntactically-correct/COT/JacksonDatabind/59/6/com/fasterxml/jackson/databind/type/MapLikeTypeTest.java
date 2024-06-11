package com.example.mapliketype.test;

import static org.junit.Assert.*;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.MapLikeType;

/**
 * Test class for MapLikeType.
 */
public class MapLikeTypeTest {

    @Test
    public void testMapLikeTypeConstruction() {
        Class<Map<String, Integer>> mapClass = Map.class;
        JavaType keyType = JavaType.constructParametricType(Map.class, String.class, Integer.class);
        JavaType valueType = JavaType.constructParametricType(Map.class, String.class, Integer.class);

        MapLikeType mapLikeType = new MapLikeType(mapClass, TypeBindings.emptyBindings(), _bogusSuperClass(mapClass), null, keyType, valueType);

        assertEquals(mapClass, mapLikeType.getRawClass());
        assertEquals(keyType, mapLikeType.getKeyType());
        assertEquals(valueType, mapLikeType.getValueType());
    }

    @Test
    public void testMapLikeTypeUpgrade() {
        JavaType baseType = JavaType.constructRawType(Map.class);
        JavaType keyType = JavaType.constructParametricType(Map.class, String.class, Integer.class);
        JavaType valueType = JavaType.constructParametricType(Map.class, String.class, Integer.class);

        MapLikeType mapLikeType = MapLikeType.upgradeFrom(baseType, keyType, valueType);

        assertEquals(mapLikeType.getClass(), MapLikeType.class);
        assertEquals(keyType, mapLikeType.getKeyType());
        assertEquals(valueType, mapLikeType.getValueType());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testMapLikeTypeUpgradeInvalidBaseType() {
        JavaType baseType = JavaType.constructRawType(List.class);
        JavaType keyType = JavaType.constructParametricType(Map.class, String.class, Integer.class);
        JavaType valueType = JavaType.constructParametricType(Map.class, String.class, Integer.class);

        MapLikeType.upgradeFrom(baseType, keyType, valueType);
    }

    @Test
    public void testMapLikeTypeWithKeyType() {
        Class<Map<String, Integer>> mapClass = Map.class;
        JavaType keyType = JavaType.constructParametricType(Map.class, String.class, Integer.class);
        JavaType valueType = JavaType.constructParametricType(Map.class, String.class, Integer.class);

        MapLikeType mapLikeType = new MapLikeType(mapClass, TypeBindings.emptyBindings(), _bogusSuperClass(mapClass), null, keyType, valueType);
        JavaType newKeyType = JavaType.constructParametricType(HashMap.class, Integer.class, String.class);

        MapLikeType newMapLikeType = mapLikeType.withKeyType(newKeyType);

        assertEquals(mapLikeType.getClass(), newMapLikeType.getClass());
        assertEquals(newKeyType, newMapLikeType.getKeyType());
        assertEquals(valueType, newMapLikeType.getValueType());
    }

    @Test
    public void testMapLikeTypeWithContentType() {
        Class<Map<String, Integer>> mapClass = Map.class;
        JavaType keyType = JavaType.constructParametricType(Map.class, String.class, Integer.class);
        JavaType valueType = JavaType.constructParametricType(Map.class, String.class, String.class);

        MapLikeType mapLikeType = new MapLikeType(mapClass, TypeBindings.emptyBindings(), _bogusSuperClass(mapClass), null, keyType, valueType);
        JavaType newValueType = JavaType.constructParametricType(HashMap.class, Integer.class, String.class);

        MapLikeType newMapLikeType = mapLikeType.withContentType(newValueType);

        assertEquals(mapLikeType.getClass(), newMapLikeType.getClass());
        assertEquals(keyType, newMapLikeType.getKeyType());
        assertEquals(newValueType, newMapLikeType.getValueType());
    }

    @Test
    public void testMapLikeTypeWithTypeHandler() {
        Class<Map<String, Integer>> mapClass = Map.class;
        JavaType keyType = JavaType.constructParametricType(Map.class, String.class, Integer.class);
        JavaType valueType = JavaType.constructParametricType(Map.class, String.class, Integer.class);

        MapLikeType mapLikeType = new MapLikeType(mapClass, TypeBindings.emptyBindings(), _bogusSuperClass(mapClass), null, keyType, valueType);

        Map<String, Integer> map = new HashMap<>();
        Map<String, String> newMap = new HashMap<>();

        MapLikeType newMapLikeType = mapLikeType.withValueHandler(map);
        assertEquals(map, newMapLikeType.getValueHandler());

        newMapLikeType = mapLikeType.withValueHandler(newMap);
        assertEquals(newMap, newMapLikeType.getValueHandler());
    }

    @Test(expected = NullPointerException.class)
    public void testMapLikeTypeWithNullValueHandler() {
        Class<Map<String, Integer>> mapClass = Map.class;
        JavaType keyType = JavaType.constructParametricType(Map.class, String.class, Integer.class);
        JavaType valueType = JavaType.constructParametricType(Map.class, String.class, Integer.class);

        MapLikeType mapLikeType = new MapLikeType(mapClass, TypeBindings.emptyBindings(), _bogusSuperClass(mapClass), null, keyType, valueType);

        mapLikeType.withValueHandler(null);
    }

    @Test
    public void testMapLikeTypeWithContentValueHandler() {
        Class<Map<String, Integer>> mapClass = Map.class;
        JavaType keyType = JavaType.constructParametricType(Map.class, String.class, Integer.class);
        JavaType valueType = JavaType.constructParametricType(Map.class, String.class, Integer.class);

        MapLikeType mapLikeType = new MapLikeType(mapClass, TypeBindings.emptyBindings(), _bogusSuperClass(mapClass), null, keyType, valueType);

        Map<String, Integer> map = new HashMap<>();
        Map<String, String> newMap = new HashMap<>();

        MapLikeType newMapLikeType = mapLikeType.withContentValueHandler(map);
        assertEquals(map, newMapLikeType.getContentValueHandler());

        newMapLikeType = mapLikeType.withContentValueHandler(newMap);
        assertEquals(newMap, newMapLikeType.getContentValueHandler());
    }

    @Test(expected = NullPointerException.class)
    public void testMapLikeTypeWithNullContentValueHandler() {
        Class<Map<String, Integer>> mapClass = Map.class;
        JavaType keyType = JavaType.constructParametricType(Map.class, String.class, Integer.class);
        JavaType valueType = JavaType.constructParametricType(Map.class, String.class, Integer.class);

        MapLikeType mapLikeType = new MapLikeType(mapClass, TypeBindings.emptyBindings(), _bogusSuperClass(mapClass), null, keyType, valueType);

        mapLikeType.withContentValueHandler(null);
    }

    @Test
    public void testMapLikeTypeIsContainerType() {
        Class<Map<String, Integer>> mapClass = Map.class;
        JavaType keyType = JavaType.constructParametricType(Map.class, String.class, Integer.class);
        JavaType valueType = JavaType.constructParametricType(Map.class, String.class, Integer.class);

        MapLikeType mapLikeType = new MapLikeType(mapClass, TypeBindings.emptyBindings(), _bogusSuperClass(mapClass), null, keyType, valueType);

        assertTrue(mapLikeType.isContainerType());
        assertTrue(mapLikeType.isMapLikeType());
    }

    @Test
    public void testMapLikeTypeIsTrueMapType() {
        Class<Map<String, Integer>> mapClass = Map.class;
        JavaType keyType = JavaType.constructParametricType(Map.class, String.class, Integer.class);
        JavaType valueType = JavaType.constructParametricType(Map.class, String.class, Integer.class);

        MapLikeType mapLikeType = new MapLikeType(mapClass, TypeBindings.emptyBindings(), _bogusSuperClass(mapClass), null, keyType, valueType);

        assertTrue(mapLikeType.isTrueMapType());
    }

    @Test
    public void testMapLikeTypeToString() {
        Class<Map<String, Integer>> mapClass = Map.class;
        JavaType keyType = JavaType.constructParametricType(Map.class, String.class, Integer.class);
        JavaType valueType = JavaType.constructParametricType(Map.class, String.class, Integer.class);

        MapLikeType mapLikeType = new MapLikeType(mapClass, TypeBindings.emptyBindings(), _bogusSuperClass(mapClass), null, keyType, valueType);

        String expected = String.format("[map-like type; class %s, %s -> %s]", mapClass.getName(), keyType, valueType);
        assertEquals(expected, mapLikeType.toString());
    }

    @Test
    public void testMapLikeTypeEquals() {
        Class<Map<String, Integer>> mapClass = Map.class;
        JavaType keyType = JavaType.constructParametricType(Map.class, String.class, Integer.class);
        JavaType valueType = JavaType.constructParametricType(Map.class, String.class, Integer.class);

        MapLikeType mapLikeType1 = new MapLikeType(mapClass, TypeBindings.emptyBindings(), _bogusSuperClass(mapClass), null, keyType, valueType);
        MapLikeType mapLikeType2 = new MapLikeType(mapClass, TypeBindings.emptyBindings(), _bogusSuperClass(mapClass), null, keyType, valueType);

        MapLikeType mapLikeType3 = new MapLikeType(Map.class, TypeBindings.emptyBindings(), _bogusSuperClass(Map.class), null, keyType, valueType);

        MapLikeType mapLikeType4 = new MapLikeType(mapClass, TypeBindings.emptyBindings(), _bogusSuperClass(mapClass), null, keyType, valueType);
        MapLikeType mapLikeType5 = new MapLikeType(mapClass, TypeBindings.emptyBindings(), _bogusSuperClass(mapClass), null, keyType, JavaType.constructParametricType(Map.class, String.class, String.class));

        assertTrue(mapLikeType1.equals(mapLikeType2));
        assertFalse(mapLikeType1.equals(mapLikeType3));
        assertFalse(mapLikeType1.equals(mapLikeType4));
        assertFalse(mapLikeType1.equals(mapLikeType5));
    }
}