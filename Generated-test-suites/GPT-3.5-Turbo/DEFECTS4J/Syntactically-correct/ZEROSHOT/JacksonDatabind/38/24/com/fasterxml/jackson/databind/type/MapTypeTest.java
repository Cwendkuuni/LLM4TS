
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
        MapType mapType = MapType.construct(rawType, bindings, superClass, superInts, keyT, valueT);
        assertNotNull(mapType);
    }

    @Test
    public void testConstructDeprecated() {
        Class<?> rawType = null;
        JavaType keyT = null;
        JavaType valueT = null;
        MapType mapType = MapType.construct(rawType, keyT, valueT);
        assertNotNull(mapType);
    }

    @Test
    public void test_narrow() {
        Class<?> subclass = null;
        MapType mapType = new MapType();
        JavaType result = mapType._narrow(subclass);
        assertNotNull(result);
    }

    @Test
    public void testWithTypeHandler() {
        Object h = null;
        MapType mapType = new MapType();
        MapType result = mapType.withTypeHandler(h);
        assertNotNull(result);
    }

    @Test
    public void testWithContentTypeHandler() {
        Object h = null;
        MapType mapType = new MapType();
        MapType result = mapType.withContentTypeHandler(h);
        assertNotNull(result);
    }

    @Test
    public void testWithValueHandler() {
        Object h = null;
        MapType mapType = new MapType();
        MapType result = mapType.withValueHandler(h);
        assertNotNull(result);
    }

    @Test
    public void testWithContentValueHandler() {
        Object h = null;
        MapType mapType = new MapType();
        MapType result = mapType.withContentValueHandler(h);
        assertNotNull(result);
    }

    @Test
    public void testWithStaticTyping() {
        MapType mapType = new MapType();
        MapType result = mapType.withStaticTyping();
        assertNotNull(result);
    }

    @Test
    public void testWithContentType() {
        JavaType contentType = null;
        MapType mapType = new MapType();
        MapType result = mapType.withContentType(contentType);
        assertNotNull(result);
    }

    @Test
    public void testWithKeyType() {
        JavaType keyType = null;
        MapType mapType = new MapType();
        MapType result = mapType.withKeyType(keyType);
        assertNotNull(result);
    }

    @Test
    public void testRefine() {
        Class<?> rawType = null;
        TypeBindings bindings = null;
        JavaType superClass = null;
        JavaType[] superInterfaces = null;
        JavaType keyType = null;
        JavaType valueType = null;
        MapType mapType = new MapType();
        JavaType result = mapType.refine(rawType, bindings, superClass, superInterfaces);
        assertNotNull(result);
    }

    @Test
    public void testWithKeyTypeHandler() {
        Object h = null;
        MapType mapType = new MapType();
        MapType result = mapType.withKeyTypeHandler(h);
        assertNotNull(result);
    }

    @Test
    public void testWithKeyValueHandler() {
        Object h = null;
        MapType mapType = new MapType();
        MapType result = mapType.withKeyValueHandler(h);
        assertNotNull(result);
    }
}
