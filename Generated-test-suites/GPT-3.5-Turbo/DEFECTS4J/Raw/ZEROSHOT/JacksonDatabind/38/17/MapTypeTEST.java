package com.fasterxml.jackson.databind.type;

import org.junit.Test;
import static org.junit.Assert.*;

public class MapTypeTest {

    @Test
    public void testConstruct() {
        Class<?> rawType = null;
        TypeBindings bindings = null;
        JavaType superClass = null;
        JavaType[] superInts = null;
        JavaType keyT = null;
        JavaType valueT = null;
        MapType result = MapType.construct(rawType, bindings, superClass, superInts, keyT, valueT);
        assertNotNull(result);
    }

    @Test
    public void testConstruct_deprecated() {
        Class<?> rawType = null;
        JavaType keyT = null;
        JavaType valueT = null;
        MapType result = MapType.construct(rawType, keyT, valueT);
        assertNotNull(result);
    }

    @Test
    public void test_narrow() {
        Class<?> subclass = null;
        JavaType result = MapType._narrow(subclass);
        assertNotNull(result);
    }

    @Test
    public void testWithTypeHandler() {
        Object h = null;
        MapType result = MapType.withTypeHandler(h);
        assertNotNull(result);
    }

    @Test
    public void testWithContentTypeHandler() {
        Object h = null;
        MapType result = MapType.withContentTypeHandler(h);
        assertNotNull(result);
    }

    @Test
    public void testWithValueHandler() {
        Object h = null;
        MapType result = MapType.withValueHandler(h);
        assertNotNull(result);
    }

    @Test
    public void testWithContentValueHandler() {
        Object h = null;
        MapType result = MapType.withContentValueHandler(h);
        assertNotNull(result);
    }

    @Test
    public void testWithStaticTyping() {
        MapType result = MapType.withStaticTyping();
        assertNotNull(result);
    }

    @Test
    public void testWithContentType() {
        JavaType contentType = null;
        MapType result = MapType.withContentType(contentType);
        assertNotNull(result);
    }

    @Test
    public void testWithKeyType() {
        JavaType keyType = null;
        MapType result = MapType.withKeyType(keyType);
        assertNotNull(result);
    }

    @Test
    public void testRefine() {
        Class<?> rawType = null;
        TypeBindings bindings = null;
        JavaType superClass = null;
        JavaType[] superInterfaces = null;
        MapType result = MapType.refine(rawType, bindings, superClass, superInterfaces);
        assertNotNull(result);
    }

    @Test
    public void testWithKeyTypeHandler() {
        Object h = null;
        MapType result = MapType.withKeyTypeHandler(h);
        assertNotNull(result);
    }

    @Test
    public void testWithKeyValueHandler() {
        Object h = null;
        MapType result = MapType.withKeyValueHandler(h);
        assertNotNull(result);
    }

    @Test
    public void testToString() {
        MapType mapType = new MapType();
        String result = mapType.toString();
        assertNotNull(result);
    }
}