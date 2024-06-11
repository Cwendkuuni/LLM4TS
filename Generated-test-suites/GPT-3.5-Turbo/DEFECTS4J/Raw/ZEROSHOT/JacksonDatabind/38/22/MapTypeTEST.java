package com.fasterxml.jackson.databind.type;

import org.junit.Assert;
import org.junit.Test;

public class MapTypeTest {

    @Test
    public void testConstruct() {
        MapType mapType = MapType.construct(null, null, null, null, null, null);
        Assert.assertNotNull(mapType);
    }

    @Test
    public void testWithKeyTypeHandler() {
        MapType mapType = new MapType(null, null, null, null, null, null, null, null, false);
        MapType result = mapType.withKeyTypeHandler(null);
        Assert.assertNotNull(result);
    }

    @Test
    public void testWithKeyValueHandler() {
        MapType mapType = new MapType(null, null, null, null, null, null, null, null, false);
        MapType result = mapType.withKeyValueHandler(null);
        Assert.assertNotNull(result);
    }

    @Test
    public void testWithContentType() {
        MapType mapType = new MapType(null, null, null, null, null, null, null, null, false);
        JavaType result = mapType.withContentType(null);
        Assert.assertNotNull(result);
    }

    @Test
    public void testWithKeyType() {
        MapType mapType = new MapType(null, null, null, null, null, null, null, null, false);
        MapType result = mapType.withKeyType(null);
        Assert.assertNotNull(result);
    }

    @Test
    public void testRefine() {
        MapType mapType = new MapType(null, null, null, null, null, null, null, null, false);
        JavaType result = mapType.refine(null, null, null, null);
        Assert.assertNotNull(result);
    }

    @Test
    public void testToString() {
        MapType mapType = new MapType(null, null, null, null, null, null, null, null, false);
        String result = mapType.toString();
        Assert.assertNotNull(result);
    }
}