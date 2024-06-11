package com.example.junit.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.junit.Test;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.MapLikeType;

public class MapLikeTypeTest {

    // Test constructor with arguments
    @Test
    public void testMapLikeTypeConstructorWithArguments() {
        JavaType keyType = new JavaType(String.class);
        JavaType valueType = new JavaType(Integer.class);
        MapLikeType mapLikeType = new MapLikeType(null, null, null, null, keyType,
                valueType);
        assertNotNull(mapLikeType);
        assertEquals(keyType, mapLikeType.getKeyType());
        assertEquals(valueType, mapLikeType.getValueType());
    }

    // Test upgradeFrom method
    @Test
    public void testMapLikeTypeUpgradeFrom() {
        JavaType baseType = new JavaType(HashMap.class);
        JavaType keyType = new JavaType(String.class);
        JavaType valueType = new JavaType(Integer.class);
        MapLikeType mapLikeType = MapLikeType.upgradeFrom(baseType, keyType, valueType);
        assertNotNull(mapLikeType);
        assertEquals(keyType, mapLikeType.getKeyType());
        assertEquals(valueType, mapLikeType.getValueType());
    }

    // Test construct deprecated method
    @Test
    public void testMapLikeTypeConstructDeprecated() {
        Class<Map<String, Integer>> rawType = Map.class;
        JavaType keyType = new JavaType(String.class);
        JavaType valueType = new JavaType(Integer.class);
        MapLikeType mapLikeType = new MapLikeType(rawType, keyType, valueType);
        assertNotNull(mapLikeType);
        assertEquals(keyType, mapLikeType.getKeyType());
        assertEquals(valueType, mapLikeType.getValueType());
    }

    // Test isTrueMapType method
    @Test
    public void testMapLikeTypeIsTrueMapType() {
        JavaType mapType = new JavaType(HashMap.class);
        MapLikeType mapLikeType = new MapLikeType(mapType, null, null, null,
                null, null);
        assertTrue(mapLikeType.isTrueMapType());

        JavaType listType = new JavaType(ArrayList.class);
        MapLikeType mapLikeListType = new MapLikeType(listType, null, null, null,
                null, null);
        assertFalse(mapLikeListType.isTrueMapType());
    }

    // Test getKeyType method
    @Test
    public void testMapLikeTypeGetKeyType() {
        JavaType mapType = new JavaType(HashMap.class);
        JavaType keyType = new JavaType(String.class);
        JavaType valueType = new JavaType(Integer.class);
        MapLikeType mapLikeType = new MapLikeType(mapType, null, null, null,
                keyType, valueType);
        assertEquals(keyType, mapLikeType.getKeyType());
    }

    // Test getContentType method
    @Test
    public void testMapLikeTypeGetContentType() {
        JavaType mapType = new JavaType(HashMap.class);
        JavaType keyType = new JavaType(String.class);
        JavaType valueType = new JavaType(Integer.class);
        MapLikeType mapLikeType = new MapLikeType(mapType, null, null, null,
                keyType, valueType);
        assertEquals(valueType, mapLikeType.getContentType());
    }

    // Test hasHandlers method
    @Test
    public void testMapLikeTypeHasHandlers() {
        JavaType mapType = new JavaType(HashMap.class);
        JavaType keyType = new JavaType(String.class);
        JavaType valueType = new JavaType(Integer.class);
        MapLikeType mapLikeType = new MapLikeType(mapType, null, null, null,
                keyType, valueType);
        assertTrue(mapLikeType.hasHandlers());
    }

    // Test empty constructor
    @Test
    public void testMapLikeTypeEmptyConstructor() {
        MapLikeType mapLikeType = new MapLikeType();
        assertNull(mapLikeType.getKeyType());
        assertNull(mapLikeType.getValueType());
    }

    // Test withKeyType method
    @Test
    public void testMapLikeTypeWithKeyType() {
        JavaType mapType = new JavaType(HashMap.class);
        JavaType keyType = new JavaType(String.class);
        JavaType valueType = new JavaType(Integer.class);
        MapLikeType mapLikeType = new MapLikeType(mapType, null, null, null,
                null, null);
        JavaType newKeyType = new JavaType(Long.class);
        MapLikeType newMapLikeType = mapLikeType.withKeyType(newKeyType);
        assertEquals(newKeyType, newMapLikeType.getKeyType());
        assertEquals(valueType, newMapLikeType.getValueType());
    }

    // Test withContentType method
    @Test
    public void testMapLikeTypeWithContentType() {
        JavaType mapType = new JavaType(HashMap.class);
        JavaType keyType = new JavaType(String.class);
        JavaType valueType = new JavaType(Integer.class);
        MapLikeType mapLikeType = new MapLikeType(mapType, null, null, null,
                keyType, valueType);
        JavaType newValueType = new JavaType(Double.class);
        MapLikeType newMapLikeType = mapLikeType.withContentType(newValueType);
        assertEquals(keyType, newMapLikeType.getKeyType());
        assertEquals(newValueType, newMapLikeType.getValueType());
    }

    // Test withTypeHandler method
    @Test
    public void testMapLikeTypeWithTypeHandler() {
        JavaType mapType = new JavaType(HashMap.class);
        JavaType keyType = new JavaType(String.class);
        JavaType valueType = new JavaType(Integer.class);
        MapLikeType mapLikeType = new MapLikeType(mapType, null, null, null,
                keyType, valueType);
        Object handler = new Object();
        MapLikeType newMapLikeType = mapLikeType.withTypeHandler(handler);
        assertEquals(handler, newMapLikeType.getTypeHandler());
    }

    // Test withContentTypeHandler method
    @Test
    public void testMapLikeTypeWithContentTypeHandler() {
        JavaType mapType = new JavaType(HashMap.class);
        JavaType keyType = new JavaType(String.class);
        JavaType valueType = new JavaType(Integer.class);
        MapLikeType mapLikeType = new MapLikeType(mapType, null, null, null,
                keyType, valueType);
        Object handler = new Object();
        MapLikeType newMapLikeType = mapLikeType.withContentTypeHandler(handler);
        assertEquals(handler, newMapLikeType.getContentTypeHandler());
    }

    // Test withValueHandler method
    @Test
    public void testMapLikeTypeWithValueHandler() {
        JavaType mapType = new JavaType(HashMap.class);
        JavaType keyType = new JavaType(String.class);
        JavaType valueType = new JavaType(Integer.class);
        MapLikeType mapLikeType = new MapLikeType(mapType, null, null, null,
                keyType, valueType);
        Object handler = new Object();
        MapLikeType newMapLikeType = mapLikeType.withValueHandler(handler);
        assertEquals(handler, newMapLikeType.getValueHandler());
    }

    // Test withContentValueHandler method
    @Test
    public void testMapLikeTypeWithContentValueHandler() {
        JavaType mapType = new JavaType(HashMap.class);
        JavaType keyType = new JavaType(String.class);
        JavaType valueType = new JavaType(Integer.class);
        MapLikeType mapLikeType = new MapLikeType(mapType, null, null, null,
                keyType, valueType);
        Object handler = new Object();
        MapLikeType newMapLikeType = mapLikeType.withContentValueHandler(handler);
        assertEquals(handler, newMapLikeType.getValueHandler());
    }

    // Test withStaticTyping method
    @Test
    public void testMapLikeTypeWithStaticTyping() {
        JavaType mapType = new JavaType(HashMap.class);
        JavaType keyType = new JavaType(String.class);
        JavaType valueType = new JavaType(Integer.class);
        MapLikeType mapLikeType = new MapLikeType(mapType, null, null, null,
                keyType, valueType);
        MapLikeType newMapLikeType = mapLikeType.withStaticTyping();
        assertTrue(newMapLikeType.isStatic());
    }

    // Test refine method
    @Test
    public void testMapLikeTypeRefine() {
        JavaType mapType = new JavaType(HashMap.class);
        JavaType keyType = new JavaType(String.class);
        JavaType valueType = new JavaType(Integer.class);
        MapLikeType mapLikeType = new MapLikeType(mapType, null, null, null,
                keyType, valueType);
        Class<Map<String, Integer>> rawType = Map.class;
        TypeBindings bindings = TypeBindings.emptyBindings();
        JavaType[] superInterfaces = null;
        MapLikeType newMapLikeType = mapLikeType.refine(rawType, bindings, null,
                superInterfaces);
        assertEquals(rawType, newMapLikeType.getRawType());
        assertEquals(keyType, newMapLikeType.getKeyType());
        assertEquals(valueType, newMapLikeType.getValueType());
    }

    // Test buildCanonicalName method
    @Test
    public void testMapLikeTypeBuildCanonicalName() {
        JavaType mapType = new JavaType(HashMap.class);
        JavaType keyType = new JavaType(String.class);
        JavaType valueType = new JavaType(Integer.class);
        MapLikeType mapLikeType = new MapLikeType(mapType, null, null, null,
                keyType, valueType);
        String canonicalName = mapLikeType.buildCanonicalName();
        String expectedCanonicalName = "class [map-like type; class java.util.HashMap, java.lang.String -> java.lang.Integer]";
        assertEquals(expectedCanonicalName, canonicalName);
    }
}