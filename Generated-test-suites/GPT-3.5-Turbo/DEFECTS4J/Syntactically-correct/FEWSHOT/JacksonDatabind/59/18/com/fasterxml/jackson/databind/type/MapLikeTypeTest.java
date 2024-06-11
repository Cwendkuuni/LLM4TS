
package com.fasterxml.jackson.databind.type;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertNull;

import org.junit.Test;

import com.fasterxml.jackson.databind.JavaType;

public class MapLikeTypeTest {

    @Test
    public void testIsContainerType() {
        MapLikeType mapType = new MapLikeType(Map.class, TypeBindings.emptyBindings(), null, null, JavaType.INT, JavaType.STRING, null, null, false);
        assertTrue(mapType.isContainerType());
    }

    @Test
    public void testIsMapLikeType() {
        MapLikeType mapType = new MapLikeType(Map.class, TypeBindings.emptyBindings(), null, null, JavaType.INT, JavaType.STRING, null, null, false);
        assertTrue(mapType.isMapLikeType());
    }

    @Test
    public void testGetKeyType() {
        MapLikeType mapType = new MapLikeType(Map.class, TypeBindings.emptyBindings(), null, null, JavaType.INT, JavaType.STRING, null, null, false);
        assertEquals(JavaType.INT, mapType.getKeyType());
    }

    @Test
    public void testGetContentType() {
        MapLikeType mapType = new MapLikeType(Map.class, TypeBindings.emptyBindings(), null, null, JavaType.INT, JavaType.STRING, null, null, false);
        assertEquals(JavaType.STRING, mapType.getContentType());
    }

    @Test
    public void testGetContentValueHandler() {
        MapLikeType mapType = new MapLikeType(Map.class, TypeBindings.emptyBindings(), null, null, JavaType.INT, JavaType.STRING, null, null, false);
        assertNull(mapType.getContentValueHandler());
    }

    @Test
    public void testGetContentTypeHandler() {
        MapLikeType mapType = new MapLikeType(Map.class, TypeBindings.emptyBindings(), null, null, JavaType.INT, JavaType.STRING, null, null, false);
        assertNull(mapType.getContentTypeHandler());
    }

    @Test
    public void testHasHandlers() {
        MapLikeType mapType = new MapLikeType(Map.class, TypeBindings.emptyBindings(), null, null, JavaType.INT, JavaType.STRING, null, null, false);
        assertFalse(mapType.hasHandlers());
    }

    @Test
    public void testWithKeyType() {
        MapLikeType mapType = new MapLikeType(Map.class, TypeBindings.emptyBindings(), null, null, JavaType.INT, JavaType.STRING, null, null, false);
        MapLikeType newMapType = mapType.withKeyType(JavaType.INTEGER);
        assertEquals(JavaType.INTEGER, newMapType.getKeyType());
    }

    @Test
    public void testWithContentType() {
        MapLikeType mapType = new MapLikeType(Map.class, TypeBindings.emptyBindings(), null, null, JavaType.INT, JavaType.STRING, null, null, false);
        MapLikeType newMapType = mapType.withContentType(JavaType.OBJECT);
        assertEquals(JavaType.OBJECT, newMapType.getContentType());
    }

    @Test
    public void testWithKeyValueHandler() {
        MapLikeType mapType = new MapLikeType(Map.class, TypeBindings.emptyBindings(), null, null, JavaType.INT, JavaType.STRING, null, null, false);
        MapLikeType newMapType = mapType.withKeyValueHandler(new Object());
        Object handler = newMapType.getKeyType().getValueHandler();
        assertTrue(handler instanceof Object);
    }

    @Test
    public void testWithContentTypeHandler() {
        MapLikeType mapType = new MapLikeType(Map.class, TypeBindings.emptyBindings(), null, null, JavaType.INT, JavaType.STRING, null, null, false);
        MapLikeType newMapType = mapType.withContentTypeHandler(new Object());
        Object handler = newMapType.getContentType().getTypeHandler();
        assertTrue(handler instanceof Object);
    }

    @Test
    public void testIsTrueMapType() {
        MapLikeType mapType = new MapLikeType(Map.class, TypeBindings.emptyBindings(), null, null, JavaType.INT, JavaType.STRING, null, null, false);
        assertTrue(mapType.isTrueMapType());
        MapLikeType customMapType = new MapLikeType(CustomMap.class, TypeBindings.emptyBindings(), null, null, JavaType.INT, JavaType.STRING, null, null, false);
        assertFalse(customMapType.isTrueMapType());
    }

    @Test
    public void testToString() {
        MapLikeType mapType = new MapLikeType(Map.class, TypeBindings.emptyBindings(), null, null, JavaType.INT, JavaType.STRING, null, null, false);
        String expected = "[map-like type; class java.util.Map, int -> class java.lang.String]";
        assertEquals(expected, mapType.toString());
    }

    @Test
    public void testEquals() {
        MapLikeType mapType1 = new MapLikeType(Map.class, TypeBindings.emptyBindings(), null, null, JavaType.INT, JavaType.STRING, null, null, false);
        MapLikeType mapType2 = new MapLikeType(Map.class, TypeBindings.emptyBindings(), null, null, JavaType.INT, JavaType.STRING, null, null, false);
        assertTrue(mapType1.equals(mapType2));
    }

    private static class CustomMap<K, V> extends HashMap<K, V> {
        private static final long serialVersionUID = 1L;
    }
}
