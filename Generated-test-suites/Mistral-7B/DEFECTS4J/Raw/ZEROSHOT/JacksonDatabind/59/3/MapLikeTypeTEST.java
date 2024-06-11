package com.fasterxml.jackson.databind.type.test;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.lang.reflect.TypeVariable;
import java.util.Collections;

import org.junit.Test;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.MapLikeType;
import com.fasterxml.jackson.databind.type.TypeBase;
import com.fasterxml.jackson.databind.type.TypeBindings;

public class MapLikeTypeTest {

    @Test
    public void testMapLikeTypeCreation() {
        JavaType javaType = new MapLikeType(Map.class, TypeBindings.emptyBindings(),
                _bogusSuperClass(Map.class), null, String.class, Number.class);
        MapLikeType mapLikeType = (MapLikeType) javaType;
        assertEquals(Map.class, mapLikeType.getRawClass());
        assertEquals(String.class, mapLikeType.getKeyType().getRawClass());
        assertEquals(Number.class, mapLikeType.getValueType().getRawClass());
        assertFalse(mapLikeType.isTrueMapType());
        assertTrue(mapLikeType.isMapLikeType());
        assertTrue(mapLikeType.isContainerType());
    }

    @Test
    public void testUpgradeFrom() {
        JavaType javaType = new JavaType(HashMap.class, TypeBindings.emptyBindings());
        MapLikeType mapLikeType = (MapLikeType) javaType;
        JavaType upgradedType = MapLikeType.upgradeFrom(javaType, String.class,
                Number.class);
        MapLikeType upgradedMapLikeType = (MapLikeType) upgradedType;
        assertEquals(Map.class, upgradedMapLikeType.getRawClass());
        assertEquals(String.class, upgradedMapLikeType.getKeyType().getRawClass());
        assertEquals(Number.class, upgradedMapLikeType.getValueType().getRawClass());
        assertFalse(upgradedMapLikeType.isTrueMapType());
        assertTrue(upgradedMapLikeType.isMapLikeType());
        assertTrue(upgradedMapLikeType.isContainerType());
    }

    @Test
    public void testConstruct() {
        MapLikeType mapLikeType = new MapLikeType(Map.class, TypeBindings.emptyBindings(),
                _bogusSuperClass(Map.class), null, String.class, Number.class);
        assertEquals(Map.class, mapLikeType.getRawClass());
        assertEquals(String.class, mapLikeType.getKeyType().getRawClass());
        assertEquals(Number.class, mapLikeType.getValueType().getRawClass());
        assertFalse(mapLikeType.isTrueMapType());
        assertTrue(mapLikeType.isMapLikeType());
        assertTrue(mapLikeType.isContainerType());
    }

    @Test
    public void testNarrow() {
        JavaType javaType = new MapLikeType(HashMap.class, TypeBindings.emptyBindings(),
                HashMap.class, null, String.class, Number.class);
        MapLikeType mapLikeType = (MapLikeType) javaType;
        JavaType narrowedType = mapLikeType.narrow(HashSet.class);
        assertNull(narrowedType);

        JavaType newMapLikeType = new MapLikeType(mapLikeType, String.class, Number.class);
        JavaType narrowedType2 = newMapLikeType.narrow(HashMap.class);
        assertEquals(HashMap.class, narrowedType2.getRawClass());
        assertEquals(String.class, narrowedType2.getKeyType().getRawClass());
        assertEquals(Number.class, narrowedType2.getValueType().getRawClass());
        assertTrue(narrowedType2.isTrueMapType());
        assertTrue(narrowedType2.isMapLikeType());
        assertTrue(narrowedType2.isContainerType());
    }

    @Test
    public void testWithKeyType() {
        JavaType javaType = new MapLikeType(Map.class, TypeBindings.emptyBindings(),
                _bogusSuperClass(Map.class), null, String.class, Number.class);
        MapLikeType mapLikeType = (MapLikeType) javaType;
        JavaType newKeyType = new JavaType(Integer.class);
        JavaType newMapLikeType = mapLikeType.withKeyType(newKeyType);
        assertEquals(Map.class, newMapLikeType.getRawClass());
        assertEquals(Integer.class, newMapLikeType.getKeyType().getRawClass());
        assertEquals(Number.class, newMapLikeType.getValueType().getRawClass());
        assertFalse(newMapLikeType.isTrueMapType());
        assertTrue(newMapLikeType.isMapLikeType());
        assertTrue(newMapLikeType.isContainerType());
    }

    @Test
    public void testWithContentType() {
        JavaType javaType = new MapLikeType(Map.class, TypeBindings.emptyBindings(),
                _bogusSuperClass(Map.class), null, String.class, Number.class);
        MapLikeType mapLikeType = (MapLikeType) javaType;
        JavaType newValueType = new JavaType(BigDecimal.class);
        JavaType newMapLikeType = mapLikeType.withContentType(newValueType);
        assertEquals(Map.class, newMapLikeType.getRawClass());
        assertEquals(String.class, newMapLikeType.getKeyType().getRawClass());
        assertEquals(BigDecimal.class, newMapLikeType.getValueType().getRawClass());
        assertFalse(newMapLikeType.isTrueMapType());
        assertTrue(newMapLikeType.isMapLikeType());
        assertTrue(newMapLikeType.isContainerType());
    }

    @Test
    public void testWithTypeHandler() {
        JavaType javaType = new MapLikeType(Map.class, TypeBindings.emptyBindings(),
                _bogusSuperClass(Map.class), null, String.class, Number.class);
        MapLikeType mapLikeType = (MapLikeType) javaType;
        Object handler = new Object();
        JavaType newMapLikeType = mapLikeType.withTypeHandler(handler);
        assertEquals(Map.class, newMapLikeType.getRawClass());
        assertEquals(String.class, newMapLikeType.getKeyType().getRawClass());
        assertEquals(Number.class, newMapLikeType.getValueType().getRawClass());
        assertFalse(newMapLikeType.isTrueMapType());
        assertTrue(newMapLikeType.isMapLikeType());
        assertTrue(newMapLikeType.isContainerType());
        assertEquals(handler, newMapLikeType.getTypeHandler());
    }

    @Test
    public void testWithContentTypeHandler() {
        JavaType javaType = new MapLikeType(Map.class, TypeBindings.emptyBindings(),
                _bogusSuperClass(Map.class), null, String.class, Number.class);
        MapLikeType mapLikeType = (MapLikeType) javaType;
        Object handler = new Object();
        JavaType newValueType = new JavaType(BigDecimal.class);
        JavaType newMapLikeType = mapLikeType.withContentTypeHandler(handler);
        assertEquals(Map.class, newMapLikeType.getRawClass());
        assertEquals(String.class, newMapLikeType.getKeyType().getRawClass());
        assertEquals(BigDecimal.class, newMapLikeType.getValueType().getRawClass());
        assertFalse(newMapLikeType.isTrueMapType());
        assertTrue(newMapLikeType.isMapLikeType());
        assertTrue(newMapLikeType.isContainerType());
        assertEquals(handler, newMapLikeType.getValueType().getTypeHandler());
    }

    @Test
    public void testWithValueHandler() {
        JavaType javaType = new MapLikeType(Map.class, TypeBindings.emptyBindings(),
                _bogusSuperClass(Map.class), null, String.class, Number.class);
        MapLikeType mapLikeType = (MapLikeType) javaType;
        Object handler = new Object();
        JavaType newMapLikeType = mapLikeType.withValueHandler(handler);
        assertEquals(Map.class, newMapLikeType.getRawClass());
        assertEquals(String.class, newMapLikeType.getKeyType().getRawClass());
        assertEquals(Number.class, newMapLikeType.getValueType().getRawClass());
        assertFalse(newMapLikeType.isTrueMapType());
        assertTrue(newMapLikeType.isMapLikeType());
        assertTrue(newMapLikeType.isContainerType());
        assertEquals(handler, newMapLikeType.getValueHandler());
    }

    @Test
    public void testWithContentValueHandler() {
        JavaType javaType = new MapLikeType(Map.class, TypeBindings.emptyBindings(),
                _bogusSuperClass(Map.class), null, String.class, Number.class);
        MapLikeType mapLikeType = (MapLikeType) javaType;
        Object handler = new Object();
        JavaType newValueType = new JavaType(BigDecimal.class);
        JavaType newMapLikeType = mapLikeType.withContentValueHandler(handler);
        assertEquals(Map.class, newMapLikeType.getRawClass());
        assertEquals(String.class, newMapLikeType.getKeyType().getRawClass());
        assertEquals(BigDecimal.class, newMapLikeType.getValueType().getRawClass());
        assertFalse(newMapLikeType.isTrueMapType());
        assertTrue(newMapLikeType.isMapLikeType());
        assertTrue(newMapLikeType.isContainerType());
        assertEquals(handler, newMapLikeType.getValueType().getValueHandler());
    }

    @Test
    public void testWithStaticTyping() {
        JavaType javaType = new MapLikeType(Map.class, TypeBindings.emptyBindings(),
                _bogusSuperClass(Map.class), null, String.class, Number.class);
        MapLikeType mapLikeType = (MapLikeType) javaType;
        JavaType newMapLikeType = mapLikeType.withStaticTyping();
        assertEquals(Map.class, newMapLikeType.getRawClass());
        assertEquals(String.class, newMapLikeType.getKeyType().getRawClass());
        assertEquals(Number.class, newMapLikeType.getValueType().getRawClass());
        assertTrue(newMapLikeType.isTrueMapType());
        assertTrue(newMapLikeType.isMapLikeType());
        assertTrue(newMapLikeType.isContainerType());
    }

    @Test
    public void testRefine() {
        JavaType javaType = new MapLikeType(Map.class, TypeBindings.emptyBindings(),
                _bogusSuperClass(Map.class), null, String.class, Number.class);
        MapLikeType mapLikeType = (MapLikeType) javaType;
        JavaType newMapType = new JavaType(LinkedHashMap.class, TypeBindings.emptyBindings());
        JavaType newMapLikeType = mapLikeType.refine(newMapType.getRawClass(),
                TypeBindings.emptyBindings(), null, null);
        assertEquals(LinkedHashMap.class, newMapLikeType.getRawClass());
        assertEquals(String.class, newMapLikeType.getKeyType().getRawClass());
        assertEquals(Number.class, newMapLikeType.getValueType().getRawClass());
        assertTrue(newMapLikeType.isTrueMapType());
        assertTrue(newMapLikeType.isMapLikeType());
        assertTrue(newMapLikeType.isContainerType());
    }

    @Test
    public void testToString() {
        JavaType javaType = new MapLikeType(Map.class, TypeBindings.emptyBindings(),
                _bogusSuperClass(Map.class), null, String.class, Number.class);
        MapLikeType mapLikeType = (MapLikeType) javaType;
        String toString = mapLikeType.toString();
        String expected = "[map-like type; class " + Map.class.getName() + ", "
                + String.class.getName() + " -> " + Number.class.getName() + "]";
        assertEquals(expected, toString);
    }

    @Test
    public void testEquals() {
        JavaType javaType1 = new MapLikeType(HashMap.class, TypeBindings.emptyBindings(),
                HashMap.class, null, String.class, Number.class);
        JavaType javaType2 = new MapLikeType(HashMap.class, TypeBindings.emptyBindings(),
                HashMap.class, null, String.class, Number.class);
        JavaType javaType3 = new MapLikeType(HashMap.class, TypeBindings.emptyBindings(),
                HashMap.class, null, String.class, String.class);
        JavaType javaType4 = new MapLikeType(HashSet.class, TypeBindings.emptyBindings(),
                HashSet.class, null, String.class, Number.class);

        MapLikeType mapLikeType1 = (MapLikeType) javaType1;
        MapLikeType mapLikeType2 = (MapLikeType) javaType2;
        MapLikeType mapLikeType3 = (MapLikeType) javaType3;
        MapLikeType mapLikeType4 = (MapLikeType) javaType4;

        assertEquals(mapLikeType1, mapLikeType1);
        assertEquals(mapLikeType1, mapLikeType2);
        assertFalse(mapLikeType1.equals(mapLikeType3));
        assertFalse(mapLikeType1.equals(mapLikeType4));
    }

    @Test
    public void testGetErasedSignature() {
        JavaType javaType = new MapLikeType(Map.class, TypeBindings.emptyBindings(),
                _bogusSuperClass(Map.class), null, String.class, Number.class);
        MapLikeType mapLikeType = (MapLikeType) javaType;
        StringBuilder erasedSignature = new StringBuilder();
        mapLikeType.getErasedSignature(erasedSignature);
        assertEquals("map", erasedSignature.toString());
    }

    @Test
    public void testGetGenericSignature() {
        JavaType javaType = new MapLikeType(Map.class, TypeBindings.emptyBindings(),
                _bogusSuperClass(Map.class), null, String.class, Number.class);
        MapLikeType mapLikeType = (MapLikeType) javaType;
        StringBuilder genericSignature = new StringBuilder();
        mapLikeType.getGenericSignature(genericSignature);
        String expected = "[map-like type; class java.util.Map, <K>K, <V>V]";
        assertEquals(expected, genericSignature.toString());
    }

    private static Class<?> _bogusSuperClass(Class<?> clazz) {
        return Collections.class;
    }
}