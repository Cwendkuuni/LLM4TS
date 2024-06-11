package com.fasterxml.jackson.databind.type;

import static org.junit.Assert.*;

import org.junit.Test;

public class MapLikeTypeTest {

    @Test
    public void testIsContainerType() {
        JavaType mapType = MapLikeType.upgradeFrom(Map.class, String.class, Number.class);
        assertTrue(mapType.isContainerType());
    }

    @Test
    public void testIsMapLikeType() {
        JavaType mapType = MapLikeType.upgradeFrom(Map.class, String.class, Number.class);
        assertTrue(mapType.isMapLikeType());
    }

    @Test
    public void testGetKeyType() {
        JavaType mapType = MapLikeType.upgradeFrom(Map.class, String.class, Number.class);
        assertEquals(String.class, mapType.getKeyType());
    }

    @Test
    public void testGetContentType() {
        JavaType mapType = MapLikeType.upgradeFrom(Map.class, String.class, Number.class);
        assertEquals(Number.class, mapType.getContentType());
    }

    @Test
    public void testGetContentValueHandler() {
        JavaType mapType = MapLikeType.upgradeFrom(Map.class, String.class, Number.class);
        assertNull(mapType.getContentValueHandler());
    }

    @Test
    public void testGetContentTypeHandler() {
        JavaType mapType = MapLikeType.upgradeFrom(Map.class, String.class, Number.class);
        assertNull(mapType.getContentTypeHandler());
    }

    @Test
    public void testHasHandlers() {
        JavaType mapType = MapLikeType.upgradeFrom(Map.class, String.class, Number.class);
        assertFalse(mapType.hasHandlers());
    }

    @Test
    public void testIsTrueMapType() {
        JavaType mapType = MapLikeType.upgradeFrom(Map.class, String.class, Number.class);
        assertTrue(mapType.isTrueMapType());
    }

    @Test
    public void testToString() {
        JavaType mapType = MapLikeType.upgradeFrom(Map.class, String.class, Number.class);
        assertEquals("[map-like type; class java.util.Map, java.lang.String -> java.lang.Number]", mapType.toString());
    }

    @Test
    public void testEquals() {
        JavaType mapType1 = MapLikeType.upgradeFrom(Map.class, String.class, Number.class);
        JavaType mapType2 = MapLikeType.upgradeFrom(Map.class, String.class, Number.class);
        assertTrue(mapType1.equals(mapType2));
    }

    @Test
    public void testWithKeyType() {
        JavaType mapType = MapLikeType.upgradeFrom(Map.class, String.class, Number.class);
        JavaType newKeyType = Integer.class;
        MapLikeType newMapType = mapType.withKeyType(newKeyType);
        assertEquals(newKeyType, newMapType.getKeyType());
    }

    @Test
    public void testWithContentType() {
        JavaType mapType = MapLikeType.upgradeFrom(Map.class, String.class, Number.class);
        JavaType newContentType = BigDecimal.class;
        MapLikeType newMapType = mapType.withContentType(newContentType);
        assertEquals(newContentType, newMapType.getContentType());
    }

    @Test
    public void testWithTypeHandler() {
        JavaType mapType = MapLikeType.upgradeFrom(Map.class, String.class, Number.class);
        Object newHandler = new Object();
        MapLikeType newMapType = mapType.withTypeHandler(newHandler);
        assertEquals(newHandler, newMapType.getTypeHandler());
    }

    @Test
    public void testWithContentTypeHandler() {
        JavaType mapType = MapLikeType.upgradeFrom(Map.class, String.class, Number.class);
        Object newHandler = new Object();
        MapLikeType newMapType = mapType.withContentTypeHandler(newHandler);
        assertEquals(newHandler, newMapType.getContentTypeHandler());
    }

    @Test
    public void testWithKeyTypeHandler() {
        JavaType mapType = MapLikeType.upgradeFrom(Map.class, String.class, Number.class);
        Object newHandler = new Object();
        MapLikeType newMapType = mapType.withKeyTypeHandler(newHandler);
        assertEquals(newHandler, newMapType.getKeyTypeHandler());
    }

    @Test
    public void testWithKeyValueHandler() {
        JavaType mapType = MapLikeType.upgradeFrom(Map.class, String.class, Number.class);
        Object newHandler = new Object();
        MapLikeType newMapType = mapType.withKeyValueHandler(newHandler);
        assertEquals(newHandler, newMapType.getKeyValueHandler());
    }

    @Test
    public void testWithStaticTyping() {
        JavaType mapType = MapLikeType.upgradeFrom(Map.class, String.class, Number.class);
        MapLikeType newMapType = mapType.withStaticTyping();
        assertTrue(newMapType.isStaticTyping());
    }

    @Test
    public void testRefine() {
        JavaType baseType = HashMap.class;
        JavaType keyType = String.class;
        JavaType valueType = Number.class;
        TypeBindings bindings = TypeBindings.emptyBindings();
        JavaType refinedType = MapLikeType.upgradeFrom(baseType, keyType, valueType).refine(Map.class, bindings, _bogusSuperClass(baseType), null);
        assertEquals(Map.class, refinedType.getRawClass());
        assertEquals(keyType, refinedType.getKeyType());
        assertEquals(valueType, refinedType.getContentType());
    }
}