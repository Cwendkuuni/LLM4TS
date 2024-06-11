package com.example.mapliketype.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.TypeBindings;
import com.fasterxml.jackson.databind.type.MapLikeType;

public class MapLikeTypeTest {

    @Test
    public void testMapLikeTypeConstructor() {
        Class<?> mapType = Map.class;
        TypeBindings bindings = TypeBindings.emptyBindings();
        JavaType keyType = JavaType.construct(String.class, null);
        JavaType valueType = JavaType.construct(Integer.class, null);
        MapLikeType mapLikeType = new MapLikeType(mapType, bindings, _bogusSuperClass(mapType), null, keyType, valueType, null, null, false);
        assertNotNull(mapLikeType);
    }

    @Test
    public void testUpgradeFrom() {
        JavaType baseType = JavaType.construct(HashMap.class, null);
        JavaType keyType = JavaType.construct(String.class, null);
        JavaType valueType = JavaType.construct(Integer.class, null);
        MapLikeType mapLikeType = MapLikeType.upgradeFrom(baseType, keyType, valueType);
        assertNotNull(mapLikeType);
        assertEquals(mapLikeType.getClass(), MapLikeType.class);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testUpgradeFromInvalidBaseType() {
        JavaType baseType = JavaType.construct(List.class, null);
        JavaType keyType = JavaType.construct(String.class, null);
        JavaType valueType = JavaType.construct(Integer.class, null);
        MapLikeType.upgradeFrom(baseType, keyType, valueType);
    }

    @Test
    public void testWithKeyType() {
        Class<?> mapType = Map.class;
        TypeBindings bindings = TypeBindings.emptyBindings();
        JavaType keyType = JavaType.construct(String.class, null);
        JavaType valueType = JavaType.construct(Integer.class, null);
        MapLikeType mapLikeType = new MapLikeType(mapType, bindings, _bogusSuperClass(mapType), null, keyType, valueType, null, null, false);
        JavaType newKeyType = JavaType.construct(Long.class, null);
        MapLikeType newMapLikeType = mapLikeType.withKeyType(newKeyType);
        assertEquals(newMapLikeType.getKeyType(), newKeyType);
    }

    @Test
    public void testWithContentType() {
        Class<?> mapType = Map.class;
        TypeBindings bindings = TypeBindings.emptyBindings();
        JavaType keyType = JavaType.construct(String.class, null);
        JavaType valueType = JavaType.construct(Integer.class, null);
        MapLikeType mapLikeType = new MapLikeType(mapType, bindings, _bogusSuperClass(mapType), null, keyType, valueType, null, null, false);
        JavaType newValueType = JavaType.construct(Long.class, null);
        MapLikeType newMapLikeType = mapLikeType.withContentType(newValueType);
        assertEquals(newMapLikeType.getContentType(), newValueType);
    }

    @Test
    public void testWithTypeHandler() {
        Class<?> mapType = Map.class;
        TypeBindings bindings = TypeBindings.emptyBindings();
        JavaType keyType = JavaType.construct(String.class, null);
        JavaType valueType = JavaType.construct(Integer.class, null);
        MapLikeType mapLikeType = new MapLikeType(mapType, bindings, _bogusSuperClass(mapType), null, keyType, valueType, null, null, false);
        Object newValueHandler = new Object();
        MapLikeType newMapLikeType = mapLikeType.withValueHandler(newValueHandler);
        assertEquals(newMapLikeType.getValueHandler(), newValueHandler);
    }

    @Test
    public void testWithContentTypeHandler() {
        Class<?> mapType = Map.class;
        TypeBindings bindings = TypeBindings.emptyBindings();
        JavaType keyType = JavaType.construct(String.class, null);
        JavaType valueType = JavaType.construct(Integer.class, null);
        MapLikeType mapLikeType = new MapLikeType(mapType, bindings, _bogusSuperClass(mapType), null, keyType, valueType, null, null, false);
        Object newContentTypeHandler = new Object();
        JavaType newValueType = JavaType.construct(Long.class, null);
        MapLikeType newMapLikeType = mapLikeType.withContentTypeHandler(newContentTypeHandler);
        assertEquals(newMapLikeType.getContentTypeHandler(), newContentTypeHandler);
        assertEquals(newMapLikeType.getContentType(), newValueType);
    }

    @Test
    public void testIsContainerType() {
        Class<?> mapType = Map.class;
        TypeBindings bindings = TypeBindings.emptyBindings();
        JavaType keyType = JavaType.construct(String.class, null);
        JavaType valueType = JavaType.construct(Integer.class, null);
        MapLikeType mapLikeType = new MapLikeType(mapType, bindings, _bogusSuperClass(mapType), null, keyType, valueType, null, null, false);
        assertTrue(mapLikeType.isContainerType());
    }

    @Test
    public void testIsMapLikeType() {
        Class<?> mapType = Map.class;
        TypeBindings bindings = TypeBindings.emptyBindings();
        JavaType keyType = JavaType.construct(String.class, null);
        JavaType valueType = JavaType.construct(Integer.class, null);
        MapLikeType mapLikeType = new MapLikeType(mapType, bindings, _bogusSuperClass(mapType), null, keyType, valueType, null, null, false);
        assertTrue(mapLikeType.isMapLikeType());
    }

    @Test
    public void testGetKeyType() {
        Class<?> mapType = Map.class;
        TypeBindings bindings = TypeBindings.emptyBindings();
        JavaType keyType = JavaType.construct(String.class, null);
        JavaType valueType = JavaType.construct(Integer.class, null);
        MapLikeType mapLikeType = new MapLikeType(mapType, bindings, _bogusSuperClass(mapType), null, keyType, valueType, null, null, false);
        assertEquals(mapLikeType.getKeyType(), keyType);
    }

    @Test
    public void testGetContentType() {
        Class<?> mapType = Map.class;
        TypeBindings bindings = TypeBindings.emptyBindings();
        JavaType keyType = JavaType.construct(String.class, null);
        JavaType valueType = JavaType.construct(Integer.class, null);
        MapLikeType mapLikeType = new MapLikeType(mapType, bindings, _bogusSuperClass(mapType), null, keyType, valueType, null, null, false);
        assertEquals(mapLikeType.getContentType(), valueType);
    }

    @Test
    public void testHasHandlers() {
        Class<?> mapType = Map.class;
        TypeBindings bindings = TypeBindings.emptyBindings();
        JavaType keyType = JavaType.construct(String.class, null);
        JavaType valueType = JavaType.construct(Integer.class, null);
        MapLikeType mapLikeType = new MapLikeType(mapType, bindings, _bogusSuperClass(mapType), null, keyType, valueType, null, null, false);
        assertTrue(mapLikeType.hasHandlers());
    }

    @Test
    public void testBuildCanonicalName() {
        Class<?> mapType = Map.class;
        TypeBindings bindings = TypeBindings.emptyBindings();
        JavaType keyType = JavaType.construct(String.class, null);
        JavaType valueType = JavaType.construct(Integer.class, null);
        MapLikeType mapLikeType = new MapLikeType(mapType, bindings, _bogusSuperClass(mapType), null, keyType, valueType, null, null, false);
        String canonicalName = mapLikeType.buildCanonicalName();
        assertTrue(canonicalName.contains("map-like type"));
        assertTrue(canonicalName.contains("[map-like type; class " + mapType.getName()));
        assertTrue(canonicalName.contains("<" + keyType.toCanonical() + ", " + valueType.toCanonical() + ">"));
    }
}