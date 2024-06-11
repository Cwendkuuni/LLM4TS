package org.apache.commons.cli.test; // Make sure to import necessary packages

import static org.junit.Assert.*;

import java.lang.reflect.TypeVariable;
import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import com.fasterxml.jackson.databind.JavaType;

public class MapLikeTypeTest {

    @Test
    public void testMapLikeType() {
        JavaType mapType = new MapLikeType(Map.class, TypeBindings.emptyBindings(), _bogusSuperClass(Map.class), null, String.class, String.class, null, null, false);
        assertEquals(Map.class, mapType.getRawClass());
        assertEquals(String.class, mapType.getKeyType());
        assertEquals(String.class, mapType.getValueType());
        assertTrue(mapType.isContainerType());
        assertTrue(mapType.isMapLikeType());
        assertFalse(mapType.isTrueMapType());
    }

    @Test
    public void testMapLikeTypeWithCustomBindings() {
        JavaType mapType = new MapLikeType(HashMap.class, TypeBindings.create(HashMap.class, String.class, Integer.class), _bogusSuperClass(HashMap.class), null, String.class, Integer.class, null, null, false);
        assertEquals(HashMap.class, mapType.getRawClass());
        assertEquals(String.class, mapType.getKeyType());
        assertEquals(Integer.class, mapType.getValueType());
        assertTrue(mapType.isContainerType());
        assertTrue(mapType.isMapLikeType());
        assertTrue(mapType.isTrueMapType());
    }

    @Test
    public void testUpgradeFromJavaType() {
        JavaType listType = new ArrayType(int[].class);
        JavaType mapType = MapLikeType.upgradeFrom(listType, String.class, Integer.class);
        assertEquals(Map.class, mapType.getRawClass());
        assertEquals(String.class, mapType.getKeyType());
        assertEquals(Integer.class, mapType.getValueType());
        assertTrue(mapType.isContainerType());
        assertTrue(mapType.isMapLikeType());
        assertFalse(mapType.isTrueMapType());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testUpgradeFromInvalidBaseType() {
        JavaType listType = new ArrayType(int[].class);
        MapLikeType.upgradeFrom(new Object(), String.class, Integer.class);
    }

    @Test
    public void testWithKeyType() {
        JavaType mapType = new MapLikeType(Map.class, TypeBindings.emptyBindings(), _bogusSuperClass(Map.class), null, String.class, String.class, null, null, false);
        JavaType newMapType = mapType.withKeyType(Integer.class);
        assertEquals(mapType.getRawClass(), newMapType.getRawClass());
        assertEquals(Integer.class, newMapType.getKeyType());
        assertEquals(String.class, newMapType.getValueType());
    }

    @Test
    public void testWithValueType() {
        JavaType mapType = new MapLikeType(Map.class, TypeBindings.emptyBindings(), _bogusSuperClass(Map.class), null, String.class, String.class, null, null, false);
        JavaType newMapType = mapType.withContentType(Integer.class);
        assertEquals(mapType.getRawClass(), newMapType.getRawClass());
        assertEquals(String.class, mapType.getKeyType());
        assertEquals(Integer.class, newMapType.getValueType());
    }

    @Test
    public void testWithTypeHandler() {
        JavaType mapType = new MapLikeType(Map.class, TypeBindings.emptyBindings(), _bogusSuperClass(Map.class), null, String.class, String.class, null, null, false);
        Object handler = new Object();
        JavaType newMapType = mapType.withTypeHandler(handler);
        assertEquals(mapType.getRawClass(), newMapType.getRawClass());
        assertEquals(String.class, newMapType.getKeyType());
        assertEquals(String.class, newMapType.getValueType());
        assertEquals(handler, newMapType.getTypeHandler());
    }

    @Test
    public void testWithContentTypeHandler() {
        JavaType mapType = new MapLikeType(Map.class, TypeBindings.emptyBindings(), _bogusSuperClass(Map.class), null, String.class, String.class, null, null, false);
        Object handler = new Object();
        JavaType valueType = new BasicType(Integer.class);
        JavaType newMapType = mapType.withContentTypeHandler(handler);
        assertEquals(mapType.getRawClass(), newMapType.getRawClass());
        assertEquals(String.class, newMapType.getKeyType());
        assertEquals(valueType, newMapType.getValueType());
        assertEquals(handler, newMapType.getTypeHandler());
        assertEquals(handler, valueType.getTypeHandler());
    }

    @Test
    public void testWithKeyTypeHandler() {
        JavaType mapType = new MapLikeType(Map.class, TypeBindings.emptyBindings(), _bogusSuperClass(Map.class), null, String.class, String.class, null, null, false);
        Object handler = new Object();
        JavaType newMapType = mapType.withKeyTypeHandler(handler);
        assertEquals(mapType.getRawClass(), newMapType.getRawClass());
        assertEquals(handler, newMapType.getTypeHandler());
        assertEquals(mapType.getKeyType(), newMapType.getKeyType());
    }

    @Test
    public void testWithKeyValueHandler() {
        JavaType mapType = new MapLikeType(Map.class, TypeBindings.emptyBindings(), _bogusSuperClass(Map.class), null, String.class, String.class, null, null, false);
        Object handler = new Object();
        JavaType newMapType = mapType.withKeyValueHandler(handler);
        assertEquals(mapType.getRawClass(), newMapType.getRawClass());
        assertEquals(mapType.getKeyType(), newMapType.getKeyType());
        assertEquals(mapType.getValueType(), newMapType.getValueType());
        assertEquals(handler, newMapType.getValueHandler());
    }

    @Test
    public void testWithStaticTyping() {
        JavaType mapType = new MapLikeType(Map.class, TypeBindings.emptyBindings(), _bogusSuperClass(Map.class), null, String.class, String.class, null, null, false);
        JavaType newMapType = mapType.withStaticTyping();
        assertEquals(mapType.getRawClass(), newMapType.getRawClass());
        assertTrue(newMapType.isStatic());
    }

    @Test
    public void testRefine() {
        JavaType mapType = new MapLikeType(Map.class, TypeBindings.emptyBindings(), _bogusSuperClass(Map.class), null, String.class, String.class, null, null, false);
        TypeVariable[] typeVariables = HashMap.class.getTypeParameters();
        MapBindings bindings = new MapBindings(typeVariables[0].getType(), typeVariables[1].getType());
        JavaType newMapType = mapType.refine(HashMap.class, bindings, null, null);
        assertEquals(HashMap.class, newMapType.getRawClass());
        assertEquals(String.class, newMapType.getKeyType());
        assertEquals(String.class, newMapType.getValueType());
    }

    @Test
    public void testBuildCanonicalName() {
        JavaType mapType = new MapLikeType(Map.class, TypeBindings.emptyBindings(), _bogusSuperClass(Map.class), null, String.class, String.class, null, null, false);
        assertEquals("[map-like type; class java.util.Map, Ljava/lang/String;Ljava/lang/String;]", mapType.buildCanonicalName());
    }
}