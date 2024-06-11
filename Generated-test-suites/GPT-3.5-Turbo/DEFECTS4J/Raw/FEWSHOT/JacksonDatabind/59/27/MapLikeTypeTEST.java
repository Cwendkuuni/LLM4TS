
package com.fasterxml.jackson.databind.type;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class MapLikeTypeTest {
  
    @Test
    public void testIsContainerType() {
        MapLikeType mapType = new MapLikeType(Map.class, null, null, null, null, null, null, null, false);
        assertTrue(mapType.isContainerType());
    }

    @Test
    public void testIsMapLikeType() {
        MapLikeType mapType = new MapLikeType(Map.class, null, null, null, null, null, null, null, false);
        assertTrue(mapType.isMapLikeType());
    }

    @Test
    public void testGetKeyType() {
        MapLikeType mapType = new MapLikeType(Map.class, null, null, null, String.class, Integer.class, null, null, false);
        assertEquals(String.class, mapType.getKeyType());
    }

    @Test
    public void testGetContentType() {
        MapLikeType mapType = new MapLikeType(Map.class, null, null, null, String.class, Integer.class, null, null, false);
        assertEquals(Integer.class, mapType.getContentType());
    }

    @Test
    public void testGetContentValueHandler() {
        MapLikeType mapType = new MapLikeType(Map.class, null, null, null, String.class, Integer.class, null, null, false);
        assertNull(mapType.getContentValueHandler());
    }

    @Test
    public void testGetContentTypeHandler() {
        MapLikeType mapType = new MapLikeType(Map.class, null, null, null, String.class, Integer.class, null, null, false);
        assertNull(mapType.getContentTypeHandler());
    }

    @Test
    public void testHasHandlers() {
        MapLikeType mapType = new MapLikeType(Map.class, null, null, null, String.class, Integer.class, null, null, false);
        assertFalse(mapType.hasHandlers());
    }

    @Test
    public void testGetErasedSignature() {
        MapLikeType mapType = new MapLikeType(Map.class, null, null, null, String.class, Integer.class, null, null, false);
        StringBuilder sb = new StringBuilder();
        mapType.getErasedSignature(sb);
        assertEquals("Ljava/util/Map;", sb.toString());
    }

    @Test
    public void testGetGenericSignature() {
        MapLikeType mapType = new MapLikeType(Map.class, null, null, null, String.class, Integer.class, null, null, false);
        StringBuilder sb = new StringBuilder();
        mapType.getGenericSignature(sb);
        assertEquals("Ljava/util/Map<Ljava/lang/String;Ljava/lang/Integer;>;", sb.toString());
    }

    @Test
    public void testWithKeyType() {
        MapLikeType mapType = new MapLikeType(Map.class, null, null, null, String.class, Integer.class, null, null, false);
        MapLikeType newMapType = mapType.withKeyType(Integer.class);
        assertEquals(Integer.class, newMapType.getKeyType());
    }

    @Test
    public void testWithContentType() {
        MapLikeType mapType = new MapLikeType(Map.class, null, null, null, String.class, Integer.class, null, null, false);
        MapLikeType newMapType = mapType.withContentType(String.class);
        assertEquals(String.class, newMapType.getContentType());
    }

    @Test
    public void testWithStaticTyping() {
        MapLikeType mapType = new MapLikeType(Map.class, null, null, null, String.class, Integer.class, null, null, false);
        MapLikeType newMapType = mapType.withStaticTyping();
        assertTrue(newMapType.isStaticTyping());
    }

    @Test
    public void testBuildCanonicalName_NoGenericType() {
        MapLikeType mapType = new MapLikeType(Map.class, null, null, null, null, null, null, null, false);
        assertEquals("java.util.Map", mapType.buildCanonicalName());
    }

    @Test
    public void testToString() {
        MapLikeType mapType = new MapLikeType(Map.class, null, null, null, String.class, Integer.class, null, null, false);
        assertEquals("[map-like type; class java.util.Map, class java.lang.String -> class java.lang.Integer]", mapType.toString());
    }

    @Test
    public void testEquals() {
        MapLikeType mapType1 = new MapLikeType(Map.class, null, null, null, String.class, Integer.class, null, null, false);
        MapLikeType mapType2 = new MapLikeType(Map.class, null, null, null, String.class, Integer.class, null, null, false);
        assertTrue(mapType1.equals(mapType2));
    }

    @Test
    public void testTrueMapType() {
        MapLikeType mapType = new MapLikeType(Map.class, null, null, null, String.class, Integer.class, null, null, false);
        assertTrue(mapType.isTrueMapType());
    }

    // Add more test cases as needed

}

