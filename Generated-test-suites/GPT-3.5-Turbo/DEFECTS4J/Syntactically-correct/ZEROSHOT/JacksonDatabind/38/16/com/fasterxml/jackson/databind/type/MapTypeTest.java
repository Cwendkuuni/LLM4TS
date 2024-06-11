package com.fasterxml.jackson.databind.type;

import org.junit.Assert;
import org.junit.Test;

public class MapTypeTest {

    @Test
    public void testConstruct() {
        MapType mapType = MapType.construct(Map.class, null, null, null, null, null);
        Assert.assertNotNull(mapType);
        Assert.assertEquals(Map.class, mapType.getRawClass());
    }

    @Test
    public void testWithKeyType() {
        JavaType keyType = new KeyType();
        MapType mapType = new MapType(null, null, null, null, null, null, null, null, false);
        MapType newMapType = mapType.withKeyType(keyType);
        Assert.assertNotNull(newMapType);
        Assert.assertEquals(keyType, newMapType.getKeyType());
    }

    @Test
    public void testWithContentValueHandler() {
        Object handler = new ValueHandler();
        MapType mapType = new MapType(null, null, null, null, null, null, null, null, false);
        MapType newMapType = mapType.withContentValueHandler(handler);
        Assert.assertNotNull(newMapType);
        Assert.assertEquals(handler, newMapType.getContentValueHandler());
    }

    @Test
    public void testWithStaticTyping() {
        MapType mapType = new MapType(null, null, null, null, null, null, null, null, false);
        MapType newMapType = mapType.withStaticTyping();
        Assert.assertNotNull(newMapType);
        Assert.assertTrue(newMapType.isStatic());
    }

    @Test
    public void testToString() {
        MapType mapType = new MapType(Map.class, null, null, null, null, null, null, null, false);
        String toString = "[map type; class java.util.Map, null -> null]";
        Assert.assertEquals(toString, mapType.toString());
    }

    private static class KeyType extends JavaType {
    }

    private static class ValueHandler {
    }
}