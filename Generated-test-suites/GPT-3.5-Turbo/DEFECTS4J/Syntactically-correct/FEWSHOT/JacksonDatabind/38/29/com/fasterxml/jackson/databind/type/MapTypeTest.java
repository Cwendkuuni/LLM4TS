package com.fasterxml.jackson.databind.type;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.MapType;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class MapTypeTest {

    @Test
    public void testConstruct() {
        MapType mapType = MapType.construct(MapType.class, null, null, null);
        assertEquals(MapType.class, mapType.getRawClass());
        assertEquals(null, mapType.getKeyType());
        assertEquals(null, mapType.getValueType());
    }

    @Test
    public void testWithKeyType() {
        MapType mapType = new MapType(MapType.class, null, null, null, null, null, null, null, false);
        JavaType keyType = JavaType.construct(MapType.class);
        MapType result = mapType.withKeyType(keyType);
        assertEquals(keyType, result.getKeyType());
    }

    @Test
    public void testWithContentType() {
        MapType mapType = new MapType(MapType.class, null, null, null, null, null, null, null, false);
        JavaType contentType = JavaType.construct(MapType.class);
        MapType result = mapType.withContentType(contentType);
        assertEquals(contentType, result.getValueType());
    }

    @Test
    public void testWithKeyTypeHandler() {
        MapType mapType = new MapType(MapType.class, null, null, null, null, null, null, null, false);
        Object handler = new Object();
        MapType result = mapType.withKeyTypeHandler(handler);
        assertEquals(handler, result.getKeyType().getTypeHandler());
    }

    @Test
    public void testWithKeyValueHandler() {
        MapType mapType = new MapType(MapType.class, null, null, null, null, null, null, null, false);
        Object handler = new Object();
        MapType result = mapType.withKeyValueHandler(handler);
        assertEquals(handler, result.getValueType().getValueHandler());
    }
}