package com.fasterxml.jackson.databind.type;

import static org.junit.Assert.*;

import org.junit.Test;

public class MapLikeTypeTest {

    @Test
    public void testIsContainerType() {
        JavaType mapType = MapLikeType.upgradeFrom(Map.class, String.class, Integer.class);
        assertTrue(mapType.isContainerType());
        assertFalse(String.class.isContainerType());
    }

    @Test
    public void testIsMapLikeType() {
        JavaType mapType = MapLikeType.upgradeFrom(Map.class, String.class, Integer.class);
        assertTrue(mapType.isMapLikeType());
        assertFalse(String.class.isMapLikeType());
    }

    @Test
    public void testGetKeyType() {
        JavaType mapType = MapLikeType.upgradeFrom(Map.class, String.class, Integer.class);
        assertEquals(String.class, mapType.getKeyType());
    }

    @Test
    public void testGetContentType() {
        JavaType mapType = MapLikeType.upgradeFrom(Map.class, String.class, Integer.class);
        assertEquals(Integer.class, mapType.getContentType());
    }

    @Test
    public void testGetContentValueHandler() {
        JavaType mapType = MapLikeType.upgradeFrom(Map.class, String.class, Integer.class);
        assertNull(mapType.getContentValueHandler());
    }

    @Test
    public void testGetContentTypeHandler() {
        JavaType mapType = MapLikeType.upgradeFrom(Map.class, String.class, Integer.class);
        assertNull(mapType.getContentTypeHandler());
    }

    @Test
    public void testHasHandlers() {
        JavaType mapType = MapLikeType.upgradeFrom(Map.class, String.class, Integer.class);
        assertFalse(mapType.hasHandlers());
    }

    @Test
    public void testBuildCanonicalName() {
        JavaType mapType = MapLikeType.upgradeFrom(Map.class, String.class, Integer.class);
        assertEquals("class java.util.Map<java.lang.String, java.lang.Integer>", mapType.buildCanonicalName());
    }

    @Test
    public void testIsTrueMapType() {
        JavaType mapType = MapLikeType.upgradeFrom(Map.class, String.class, Integer.class);
        assertTrue(mapType.isTrueMapType());
    }

    @Test
    public void testWithKeyType() {
        JavaType mapType = MapLikeType.upgradeFrom(Map.class, String.class, Integer.class);
        JavaType stringMapType = mapType.withKeyType(Character.class);
        assertNotEquals(mapType, stringMapType);
        assertEquals(Integer.class, stringMapType.getKeyType());
        assertEquals(Integer.class, stringMapType.getContentType());
    }

    @Test
    public void testWithContentType() {
        JavaType mapType = MapLikeType.upgradeFrom(Map.class, String.class, Integer.class);
        JavaType intMapType = mapType.withContentType(Float.class);
        assertNotEquals(mapType, intMapType);
        assertEquals(String.class, intMapType.getKeyType());
        assertEquals(Float.class, intMapType.getContentType());
    }

    @Test
    public void testWithTypeHandler() {
        JavaType mapType = MapLikeType.upgradeFrom(Map.class, String.class, Integer.class);
        Object handler = new Object();
        JavaType handlerType = MapLikeType.upgradeFrom(Object.class, handler.getClass(), Object.class);
        JavaType handlerMapType = mapType.withTypeHandler(handlerType);
        assertNotEquals(mapType, handlerMapType);
        assertEquals(String.class, handlerMapType.getKeyType());
        assertEquals(Float.class, handlerMapType.getContentType());
        assertEquals(handlerType, handlerMapType.getTypeHandler());
    }

    @Test
    public void testWithContentTypeHandler() {
        JavaType mapType = MapLikeType.upgradeFrom(Map.class, String.class, Integer.class);
        Object handler = new Object();
        JavaType handlerType = MapLikeType.upgradeFrom(Object.class, handler.getClass(), Object.class);
        JavaType handlerMapType = mapType.withContentTypeHandler(handlerType);
        assertNotEquals(mapType, handlerMapType);
        assertEquals(String.class, handlerMapType.getKeyType());
        assertEquals(handlerType, handlerMapType.getContentTypeHandler());
        assertEquals(Integer.class, handlerMapType.getContentType());
    }

    @Test
    public void testWithValueHandler() {
        JavaType mapType = MapLikeType.upgradeFrom(Map.class, String.class, Integer.class);
        Object handler = new Object();
        JavaType handlerType = MapLikeType.upgradeFrom(Object.class, handler.getClass(), Object.class);
        JavaType handlerMapType = mapType.withValueHandler(handlerType);
        assertNotEquals(mapType, handlerMapType);
        assertEquals(String.class, handlerMapType.getKeyType());
        assertEquals(handlerType, handlerMapType.getValueHandler());
        assertEquals(Integer.class, handlerMapType.getContentType());
    }

    @Test
    public void testWithContentValueHandler() {
        JavaType mapType = MapLikeType.upgradeFrom(Map.class, String.class, Integer.class);
        Object handler = new Object();
        JavaType handlerType = MapLikeType.upgradeFrom(Object.class, handler.getClass(), Object.class);
        JavaType handlerMapType = mapType.withContentValueHandler(handlerType);
        assertNotEquals(mapType, handlerMapType);
        assertEquals(String.class, handlerMapType.getKeyType());
        assertEquals(handlerType, handlerMapType.getContentValueHandler());
        assertEquals(Integer.class, handlerMapType.getContentType());
    }

    @Test
    public void testWithStaticTyping() {
        JavaType mapType = MapLikeType.upgradeFrom(Map.class, String.class, Integer.class);
        JavaType staticMapType = mapType.withStaticTyping();
        assertTrue(staticMapType.isStatic());
        assertFalse(mapType.isStatic());
    }

    @Test
    public void testRefine() {
        JavaType mapType = MapLikeType.upgradeFrom(Map.class, String.class, Integer.class);
        TypeBindings bindings = TypeBindings.create(HashMap.class, String.class, Integer.class);
        JavaType refinedMapType = mapType.refine(HashMap.class, bindings, null, null);
        assertEquals(refinedMapType.getClass(), MapLikeType.class);
        assertEquals(String.class, refinedMapType.getKeyType());
        assertEquals(Integer.class, refinedMapType.getContentType());
    }
}