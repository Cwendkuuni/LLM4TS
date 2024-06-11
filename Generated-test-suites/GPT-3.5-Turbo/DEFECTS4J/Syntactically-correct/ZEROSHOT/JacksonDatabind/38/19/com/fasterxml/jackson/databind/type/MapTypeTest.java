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
    public void testConstructWithDeprecatedParameters() {
        Class<?> rawType = null;
        JavaType keyT = null;
        JavaType valueT = null;
        MapType result = MapType.construct(rawType, keyT, valueT);
        assertNotNull(result);
    }

    @Test
    public void testNarrow() {
        Class<?> subclass = null;
        MapType instance = new MapType();
        JavaType result = instance._narrow(subclass);
        assertNotNull(result);
    }

    @Test
    public void testWithTypeHandler() {
        Object h = null;
        MapType instance = new MapType();
        MapType result = instance.withTypeHandler(h);
        assertNotNull(result);
    }

    @Test
    public void testWithContentTypeHandler() {
        Object h = null;
        MapType instance = new MapType();
        MapType result = instance.withContentTypeHandler(h);
        assertNotNull(result);
    }

    @Test
    public void testWithValueHandler() {
        Object h = null;
        MapType instance = new MapType();
        MapType result = instance.withValueHandler(h);
        assertNotNull(result);
    }

    @Test
    public void testWithContentValueHandler() {
        Object h = null;
        MapType instance = new MapType();
        MapType result = instance.withContentValueHandler(h);
        assertNotNull(result);
    }

    @Test
    public void testWithStaticTyping() {
        MapType instance = new MapType();
        MapType result = instance.withStaticTyping();
        assertNotNull(result);
    }

    @Test
    public void testWithContentType() {
        JavaType contentType = null;
        MapType instance = new MapType();
        JavaType result = instance.withContentType(contentType);
        assertNotNull(result);
    }

    @Test
    public void testWithKeyType() {
        JavaType keyType = null;
        MapType instance = new MapType();
        MapType result = instance.withKeyType(keyType);
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
        MapType instance = new MapType();
        JavaType result = instance.refine(rawType, bindings, superClass, superInterfaces);
        assertNotNull(result);
    }

    @Test
    public void testWithKeyTypeHandler() {
        Object h = null;
        MapType instance = new MapType();
        MapType result = instance.withKeyTypeHandler(h);
        assertNotNull(result);
    }

    @Test
    public void testWithKeyValueHandler() {
        Object h = null;
        MapType instance = new MapType();
        MapType result = instance.withKeyValueHandler(h);
        assertNotNull(result);
    }

    @Test
    public void testToString() {
        MapType instance = new MapType();
        String result = instance.toString();
        assertNotNull(result);
    }
}