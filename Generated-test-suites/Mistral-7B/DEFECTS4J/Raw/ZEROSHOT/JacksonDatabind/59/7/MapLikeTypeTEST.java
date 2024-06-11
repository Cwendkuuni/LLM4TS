package com.example.junit;

import static org.junit.Assert.*;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import com.fasterxml.jackson.databind.type.MapLikeType;
import com.fasterxml.jackson.databind.type.TypeBindings;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.fasterxml.jackson.databind.type.types.MapType;

public class MapLikeTypeTest {

    @Test
    public void testMapLikeTypeConstruction() {
        Class<Map<String, Integer>> mapType = Map.class;
        TypeBindings bindings = TypeBindings.emptyBindings();
        JavaType keyType = TypeFactory.defaultInstance().constructType(String.class);
        JavaType valueType = TypeFactory.defaultInstance().constructType(Integer.class);
        MapLikeType mapLikeType = new MapLikeType(mapType, bindings, _bogusSuperClass(mapType), null, keyType, valueType);
        assertEquals(mapType, mapLikeType.getRawClass());
        assertEquals(keyType, mapLikeType.getKeyType());
        assertEquals(valueType, mapLikeType.getValueType());
    }

    @Test
    public void testMapLikeTypeUpgrade() {
        JavaType baseType = TypeFactory.defaultInstance().constructType(HashMap.class);
        JavaType keyType = TypeFactory.defaultInstance().constructType(String.class);
        JavaType valueType = TypeFactory.defaultInstance().constructType(Integer.class);
        MapLikeType mapLikeType = MapLikeType.upgradeFrom(baseType, keyType, valueType);
        assertEquals(MapLikeType.class, mapLikeType.getClass());
        assertEquals(keyType, mapLikeType.getKeyType());
        assertEquals(valueType, mapLikeType.getValueType());
    }

    @Test
    public void testMapLikeTypeConstructDeprecated() {
        Class<Map<String, Integer>> mapType = Map.class;
        TypeBindings bindings = TypeBindings.emptyBindings();
        JavaType keyType = TypeFactory.defaultInstance().constructType(String.class);
        JavaType valueType = TypeFactory.defaultInstance().constructType(Integer.class);
        MapLikeType mapLikeType = new MapLikeType(mapType, bindings, _bogusSuperClass(mapType), null, keyType, valueType);
        MapType mapTypeInstance = (MapType) TypeFactory.defaultInstance().constructType(mapType);
        MapLikeType mapLikeTypeConstruct = new MapLikeType(mapTypeInstance, keyType, valueType);
        assertEquals(mapLikeType, mapLikeTypeConstruct);
    }

    @Test
    public void testMapLikeTypeNarrow() {
        Class<Map<String, Integer>> mapType = Map.class;
        JavaType baseType = TypeFactory.defaultInstance().constructType(HashMap.class);
        JavaType keyType = TypeFactory.defaultInstance().constructType(String.class);
        JavaType valueType = TypeFactory.defaultInstance().constructType(Integer.class);
        MapLikeType mapLikeType = new MapLikeType(mapType, TypeBindings.emptyBindings(), baseType, null, keyType, valueType);
        JavaType narrowedType = mapLikeType.withStaticTyping();
        assertEquals(Map.class, narrowedType.getRawClass());
    }

    @Test
    public void testMapLikeTypeWithKeyType() {
        Class<Map<String, Integer>> mapType = Map.class;
        JavaType keyType = TypeFactory.defaultInstance().constructType(String.class);
        JavaType valueType = TypeFactory.defaultInstance().constructType(Integer.class);
        MapLikeType mapLikeType = new MapLikeType(mapType, TypeBindings.emptyBindings(), _bogusSuperClass(mapType), null, keyType, valueType);
        JavaType newKeyType = TypeFactory.defaultInstance().constructType(String.class);
        MapLikeType newMapLikeType = mapLikeType.withKeyType(newKeyType);
        assertEquals(newKeyType, newMapLikeType.getKeyType());
        assertEquals(valueType, newMapLikeType.getValueType());
    }

    @Test
    public void testMapLikeTypeWithValueType() {
        Class<Map<String, Integer>> mapType = Map.class;
        JavaType keyType = TypeFactory.defaultInstance().constructType(String.class);
        JavaType valueType = TypeFactory.defaultInstance().constructType(Integer.class);
        MapLikeType mapLikeType = new MapLikeType(mapType, TypeBindings.emptyBindings(), _bogusSuperClass(mapType), null, keyType, valueType);
        JavaType newValueType = TypeFactory.defaultInstance().constructType(Long.class);
        MapLikeType newMapLikeType = mapLikeType.withContentType(newValueType);
        assertEquals(keyType, newMapLikeType.getKeyType());
        assertEquals(newValueType, newMapLikeType.getValueType());
    }

    @Test
    public void testMapLikeTypeWithKeyHandler() {
        Class<Map<String, Integer>> mapType = Map.class;
        JavaType keyType = TypeFactory.defaultInstance().constructType(String.class);
        JavaType valueType = TypeFactory.defaultInstance().constructType(Integer.class);
        MapLikeType mapLikeType = new MapLikeType(mapType, TypeBindings.emptyBindings(), _bogusSuperClass(mapType), null, keyType, valueType);
        Object keyHandler = new Object() {};
        MapLikeType newMapLikeType = mapLikeType.withKeyTypeHandler(keyHandler);
        assertEquals(keyHandler, newMapLikeType.getKeyTypeHandler());
        assertEquals(valueType, newMapLikeType.getValueType());
    }

    @Test
    public void testMapLikeTypeWithValueHandler() {
        Class<Map<String, Integer>> mapType = Map.class;
        JavaType keyType = TypeFactory.defaultInstance().constructType(String.class);
        JavaType valueType = TypeFactory.defaultInstance().constructType(Integer.class);
        MapLikeType mapLikeType = new MapLikeType(mapType, TypeBindings.emptyBindings(), _bogusSuperClass(mapType), null, keyType, valueType);
        Object valueHandler = new Object() {};
        MapLikeType newMapLikeType = mapLikeType.withValueHandler(valueHandler);
        assertEquals(keyType, newMapLikeType.getKeyType());
        assertEquals(valueHandler, newMapLikeType.getValueHandler());
    }

    @Test
    public void testMapLikeTypeIsContainerType() {
        Class<Map<String, Integer>> mapType = Map.class;
        JavaType keyType = TypeFactory.defaultInstance().constructType(String.class);
        JavaType valueType = TypeFactory.defaultInstance().constructType(Integer.class);
        MapLikeType mapLikeType = new MapLikeType(mapType, TypeBindings.emptyBindings(), _bogusSuperClass(mapType), null, keyType, valueType);
        assertTrue(mapLikeType.isContainerType());
        assertTrue(mapLikeType.isMapLikeType());
    }

    @Test
    public void testMapLikeTypeIsTrueMapType() {
        Class<Map<String, Integer>> mapType = Map.class;
        JavaType keyType = TypeFactory.defaultInstance().constructType(String.class);
        JavaType valueType = TypeFactory.defaultInstance().constructType(Integer.class);
        MapLikeType mapLikeType = new MapLikeType(mapType, TypeBindings.emptyBindings(), _bogusSuperClass(mapType), null, keyType, valueType);
        assertTrue(mapLikeType.isTrueMapType());
    }

    @Test
    public void testMapLikeTypeToString() {
        Class<Map<String, Integer>> mapType = Map.class;
        JavaType keyType = TypeFactory.defaultInstance().constructType(String.class);
        JavaType valueType = TypeFactory.defaultInstance().constructType(Integer.class);
        MapLikeType mapLikeType = new MapLikeType(mapType, TypeBindings.emptyBindings(), _bogusSuperClass(mapType), null, keyType, valueType);
        String expected = "[map-like type; class " + mapType.getName() + ", " + keyType + " -> " + valueType + "]";
        assertEquals(expected, mapLikeType.toString());
    }

    @Test
    public void testMapLikeTypeEquals() {
        Class<Map<String, Integer>> mapType = Map.class;
        JavaType keyType1 = TypeFactory.defaultInstance().constructType(String.class);
        JavaType valueType1 = TypeFactory.defaultInstance().constructType(Integer.class);
        JavaType keyType2 = TypeFactory.defaultInstance().constructType(String.class);
        JavaType valueType2 = TypeFactory.defaultInstance().constructType(Integer.class);
        MapLikeType mapLikeType1 = new MapLikeType(mapType, TypeBindings.emptyBindings(), _bogusSuperClass(mapType), null, keyType1, valueType1);
        MapLikeType mapLikeType2 = new MapLikeType(mapType, TypeBindings.emptyBindings(), _bogusSuperClass(mapType), null, keyType2, valueType2);
        MapLikeType mapLikeType3 = new MapLikeType(mapType, TypeBindings.emptyBindings(), _bogusSuperClass(mapType), null, keyType1, valueType1);
        assertTrue(mapLikeType1.equals(mapLikeType3));
        assertFalse(mapLikeType1.equals(mapLikeType2));
        assertFalse(mapLikeType1.equals(null));
    }
}