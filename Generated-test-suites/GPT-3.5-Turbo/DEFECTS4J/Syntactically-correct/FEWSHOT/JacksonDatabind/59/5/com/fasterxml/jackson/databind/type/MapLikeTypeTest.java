package com.fasterxml.jackson.databind.type;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class MapLikeTypeTest {
    @Test
    public void testIsContainerType() {
        MapLikeType mapType = new MapLikeType(Map.class, new TypeBindings(), null, null, null, null, null, null, false);
        assertTrue(mapType.isContainerType());
    }

    @Test
    public void testIsMapLikeType() {
        MapLikeType mapType = new MapLikeType(Map.class, new TypeBindings(), null, null, null, null, null, null, false);
        assertTrue(mapType.isMapLikeType());
    }

    @Test
    public void testGetKeyType() {
        JavaType keyType = new JavaType(Key.class);
        MapLikeType mapType = new MapLikeType(Map.class, new TypeBindings(), null, null, keyType, null, null, null, false);
        assertEquals(keyType, mapType.getKeyType());
    }

    @Test
    public void testGetContentType() {
        JavaType contentType = new JavaType(Value.class);
        MapLikeType mapType = new MapLikeType(Map.class, new TypeBindings(), null, null, null, contentType, null, null, false);
        assertEquals(contentType, mapType.getContentType());
    }

    @Test
    public void testGetContentValueHandler() {
        Object valueHandler = new Object();
        JavaType contentType = new JavaType(Value.class).withValueHandler(valueHandler);
        MapLikeType mapType = new MapLikeType(Map.class, new TypeBindings(), null, null, null, contentType, null, null, false);
        assertEquals(valueHandler, mapType.getContentValueHandler());
    }

    @Test
    public void testGetContentTypeHandler() {
        Object typeHandler = new Object();
        JavaType contentType = new JavaType(Value.class).withTypeHandler(typeHandler);
        MapLikeType mapType = new MapLikeType(Map.class, new TypeBindings(), null, null, null, contentType, null, null, false);
        assertEquals(typeHandler, mapType.getContentTypeHandler());
    }

    @Test
    public void testHasHandlers() {
        Object typeHandler = new Object();
        JavaType keyType = new JavaType(Key.class).withTypeHandler(typeHandler);
        JavaType valueType = new JavaType(Value.class).withValueHandler(typeHandler);
        MapLikeType mapType = new MapLikeType(Map.class, new TypeBindings(), null, null, keyType, valueType, null, null, false);
        assertTrue(mapType.hasHandlers());
    }

    @Test
    public void testHasNoHandlers() {
        JavaType keyType = new JavaType(Key.class);
        JavaType valueType = new JavaType(Value.class);
        MapLikeType mapType = new MapLikeType(Map.class, new TypeBindings(), null, null, keyType, valueType, null, null, false);
        assertFalse(mapType.hasHandlers());
    }

    @Test
    public void testWithKeyType() {
        JavaType newKeyType = new JavaType(Object.class);
        JavaType keyType = new JavaType(Key.class);
        JavaType valueType = new JavaType(Value.class);
        MapLikeType mapType = new MapLikeType(Map.class, new TypeBindings(), null, null, keyType, valueType, null, null, false);
        MapLikeType newMapType = mapType.withKeyType(newKeyType);
        assertEquals(newKeyType, newMapType.getKeyType());
        assertEquals(valueType, newMapType.getContentType());
    }

    @Test
    public void testWithContentType() {
        JavaType newContentType = new JavaType(Object.class);
        JavaType keyType = new JavaType(Key.class);
        JavaType valueType = new JavaType(Value.class);
        MapLikeType mapType = new MapLikeType(Map.class, new TypeBindings(), null, null, keyType, valueType, null, null, false);
        MapLikeType newMapType = mapType.withContentType(newContentType);
        assertEquals(keyType, newMapType.getKeyType());
        assertEquals(newContentType, newMapType.getContentType());
    }

    @Test
    public void testWithStaticTyping() {
        JavaType keyType = new JavaType(Key.class);
        JavaType valueType = new JavaType(Value.class);
        MapLikeType mapType = new MapLikeType(Map.class, new TypeBindings(), null, null, keyType, valueType, null, null, false);
        MapLikeType newMapType = mapType.withStaticTyping();
        assertTrue(newMapType.isStaticTyping());
    }

    @Test
    public void testWithKeyTypeHandler() {
        Object typeHandler = new Object();
        JavaType keyType = new JavaType(Key.class);
        JavaType valueType = new JavaType(Value.class);
        MapLikeType mapType = new MapLikeType(Map.class, new TypeBindings(), null, null, keyType, valueType, null, null, false);
        MapLikeType newMapType = mapType.withKeyTypeHandler(typeHandler);
        assertEquals(typeHandler, newMapType.getKeyType().getTypeHandler());
    }

    @Test
    public void testWithKeyValueHandler() {
        Object valueHandler = new Object();
        JavaType keyType = new JavaType(Key.class);
        JavaType valueType = new JavaType(Value.class);
        MapLikeType mapType = new MapLikeType(Map.class, new TypeBindings(), null, null, keyType, valueType, null, null, false);
        MapLikeType newMapType = mapType.withKeyValueHandler(valueHandler);
        assertEquals(valueHandler, newMapType.getKeyType().getValueHandler());
    }

    @Test
    public void testToString() {
        JavaType keyType = new JavaType(Key.class);
        JavaType valueType = new JavaType(Value.class);
        MapLikeType mapType = new MapLikeType(MyMap.class, new TypeBindings(), null, null, keyType, valueType, null, null, false);
        String expected = "[map-like type; class com.fasterxml.jackson.databind.type.MapLikeTypeTest$MyMap, " + keyType + " -> " + valueType + "]";
        assertEquals(expected, mapType.toString());
    }
    
    private static class Key {
        // empty
    }

    private static class Value {
        // empty
    }

    private static class MyMap extends HashMap<Key, Value> {
        // empty
    }
}