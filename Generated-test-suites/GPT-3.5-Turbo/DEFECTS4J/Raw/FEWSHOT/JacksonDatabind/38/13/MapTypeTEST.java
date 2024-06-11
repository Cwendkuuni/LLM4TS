package com.fasterxml.jackson.databind.type;

import static org.junit.Assert.*;
import org.junit.Test;

public class MapTypeTest {

    @Test
    public void testConstructor() {
        MapType mapType = MapType.construct(MapType.class, null, null, null, null, null);
        assertNotNull(mapType);
    }

    @Test
    public void testWithTypeHandler() {
        MapType mapType = new MapType(null, null, null, null, null, null, null, null, false);
        MapType result = mapType.withTypeHandler(null);
        assertNotNull(result);
    }

    @Test
    public void testWithContentTypeHandler() {
        MapType mapType = new MapType(null, null, null, null, null, null, null, null, false);
        MapType result = mapType.withContentTypeHandler(null);
        assertNotNull(result);
    }

    @Test
    public void testWithValueHandler() {
        MapType mapType = new MapType(null, null, null, null, null, null, null, null, false);
        MapType result = mapType.withValueHandler(null);
        assertNotNull(result);
    }

    @Test
    public void testWithContentValueHandler() {
        MapType mapType = new MapType(null, null, null, null, null, null, null, null, false);
        MapType result = mapType.withContentValueHandler(null);
        assertNotNull(result);
    }

    @Test
    public void testWithStaticTyping() {
        MapType mapType = new MapType(null, null, null, null, null, null, null, null, false);
        MapType result = mapType.withStaticTyping();
        assertNotNull(result);
    }

    @Test
    public void testWithContentType() {
        MapType mapType = new MapType(null, null, null, null, null, null, null, null, false);
        JavaType contentType = null;
        MapType result = mapType.withContentType(contentType);
        assertNotNull(result);
    }

    @Test
    public void testWithKeyType() {
        MapType mapType = new MapType(null, null, null, null, null, null, null, null, false);
        JavaType keyType = null;
        MapType result = mapType.withKeyType(keyType);
        assertNotNull(result);
    }

    @Test
    public void testRefine() {
        MapType mapType = new MapType(null, null, null, null, null, null, null, null, false);
        Class<?> rawType = null;
        TypeBindings bindings = null;
        JavaType superClass = null;
        JavaType[] superInterfaces = null;
        JavaType result = mapType.refine(rawType, bindings, superClass, superInterfaces);
        assertNotNull(result);
    }

    @Test
    public void testWithKeyTypeHandler() {
        MapType mapType = new MapType(null, null, null, null, null, null, null, null, false);
        MapType result = mapType.withKeyTypeHandler(null);
        assertNotNull(result);
    }

    @Test
    public void testWithKeyValueHandler() {
        MapType mapType = new MapType(null, null, null, null, null, null, null, null, false);
        MapType result = mapType.withKeyValueHandler(null);
        assertNotNull(result);
    }

    @Test
    public void testToString() {
        MapType mapType = new MapType(null, null, null, null, null, null, null, null, false);
        String result = mapType.toString();
        assertNotNull(result);
    }
}