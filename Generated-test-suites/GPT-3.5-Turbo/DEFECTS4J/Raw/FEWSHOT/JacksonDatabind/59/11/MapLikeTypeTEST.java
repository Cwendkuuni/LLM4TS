package com.fasterxml.jackson.databind.type;

import org.junit.Assert;
import org.junit.Test;

import java.util.Map;

public class MapLikeTypeTest {

    @Test
    public void testIsTrueMapType() {
        MapLikeType mapType = new MapLikeType(Map.class, null, null, null, null, null, null, null, false);
        Assert.assertTrue(mapType.isTrueMapType());
    }

    @Test
    public void testEquals() {
        MapLikeType mapType1 = new MapLikeType(Map.class, null, null, null, null, null, null, null, false);
        MapLikeType mapType2 = new MapLikeType(Map.class, null, null, null, null, null, null, null, false);
        Assert.assertEquals(mapType1, mapType2);
    }

    @Test
    public void testEqualsFalse() {
        MapLikeType mapType1 = new MapLikeType(Map.class, null, null, null, null, null, null, null, false);
        MapLikeType mapType2 = new MapLikeType(Map.class, null, null, null, null, null, null, null, true);
        Assert.assertNotEquals(mapType1, mapType2);
    }

    @Test
    public void testToString() {
        MapLikeType mapType = new MapLikeType(Map.class, null, null, null, null, null, null, null, false);
        String expected = "[map-like type; class java.util.Map, null -> null]";
        Assert.assertEquals(expected, mapType.toString());
    }

    @Test
    public void testWithKeyValueHandler() {
        MapLikeType mapType = new MapLikeType(Map.class, null, null, null, null, null, null, null, false);
        MapLikeType expected = new MapLikeType(Map.class, null, null, null, null, null, null, null, false);
        Assert.assertEquals(expected, mapType.withKeyValueHandler(null));
    }

    @Test
    public void testWithKeyTypeHandler() {
        MapLikeType mapType = new MapLikeType(Map.class, null, null, null, null, null, null, null, false);
        MapLikeType expected = new MapLikeType(Map.class, null, null, null, null, null, null, null, false);
        Assert.assertEquals(expected, mapType.withKeyTypeHandler(null));
    }

    @Test
    public void testGetGenericSignature() {
        MapLikeType mapType = new MapLikeType(Map.class, null, null, null, null, null, null, null, false);
        StringBuilder sb = new StringBuilder();
        StringBuilder expected = new StringBuilder();
        expected.append("Ljava/util/Map<;>;");
        Assert.assertEquals(expected.toString(), mapType.getGenericSignature(sb).toString());
    }

    @Test
    public void testGetErasedSignature() {
        MapLikeType mapType = new MapLikeType(Map.class, null, null, null, null, null, null, null, false);
        StringBuilder sb = new StringBuilder();
        StringBuilder expected = new StringBuilder();
        expected.append("Ljava/util/Map;");
        Assert.assertEquals(expected.toString(), mapType.getErasedSignature(sb).toString());
    }

    @Test
    public void testHasHandlers() {
        MapLikeType mapType = new MapLikeType(Map.class, null, null, null, null, null, null, null, false);
        Assert.assertFalse(mapType.hasHandlers());
    }

    @Test
    public void testGetContentTypeHandler() {
        MapLikeType mapType = new MapLikeType(Map.class, null, null, null, null, null, null, null, false);
        Assert.assertNull(mapType.getContentTypeHandler());
    }

    @Test
    public void testGetContentValueHandler() {
        MapLikeType mapType = new MapLikeType(Map.class, null, null, null, null, null, null, null, false);
        Assert.assertNull(mapType.getContentValueHandler());
    }

    @Test
    public void testGetContentType() {
        MapLikeType mapType = new MapLikeType(Map.class, null, null, null, null, null, null, null, false);
        Assert.assertNull(mapType.getContentType());
    }

    @Test
    public void testGetKeyType() {
        MapLikeType mapType = new MapLikeType(Map.class, null, null, null, null, null, null, null, false);
        Assert.assertNull(mapType.getKeyType());
    }

    @Test
    public void testIsMapLikeType() {
        MapLikeType mapType = new MapLikeType(Map.class, null, null, null, null, null, null, null, false);
        Assert.assertTrue(mapType.isMapLikeType());
    }

    @Test
    public void testIsContainerType() {
        MapLikeType mapType = new MapLikeType(Map.class, null, null, null, null, null, null, null, false);
        Assert.assertTrue(mapType.isContainerType());
    }

}