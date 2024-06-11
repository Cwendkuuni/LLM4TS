package com.fasterxml.jackson.databind.type;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.MapType;
import org.junit.Before;
import org.junit.Test;

public class MapTypeTest {

    private MapType mapType;

    @Before
    public void setup() {
        mapType = MapType.construct(Map.class, null, null, null, null, null);
    }

    @Test
    public void testConstruct() {
        assertEquals(MapType.class, mapType.getClass());
    }

    @Test
    public void testWithKeyType() {
        JavaType keyType = mapType.getKeyType();
        assertEquals(keyType, mapType.withKeyType(keyType));
    }

    @Test
    public void testWithKeyValueHandler() {
        Object handler = new Object();
        mapType = mapType.withKeyValueHandler(handler);
        assertEquals(handler, mapType.getKeyType().getValueHandler());
    }

    @Test
    public void testWithKeyTypeHandler() {
        Object handler = new Object();
        mapType = mapType.withKeyTypeHandler(handler);
        assertEquals(handler, mapType.getKeyType().getTypeHandler());
    }

    @Test
    public void testWithContentType() {
        JavaType valueType = mapType.getContentType();
        assertEquals(valueType, mapType.withContentType(valueType));
    }

    @Test
    public void testWithStaticTyping() {
        mapType = mapType.withStaticTyping();
        assertTrue(mapType.isStaticTyping());
    }

    @Test
    public void testWithContentValueHandler() {
        Object handler = new Object();
        mapType = mapType.withContentValueHandler(handler);
        assertEquals(handler, mapType.getContentType().getValueHandler());
    }

    @Test
    public void testWithContentTypeHandler() {
        Object handler = new Object();
        mapType = mapType.withContentTypeHandler(handler);
        assertEquals(handler, mapType.getContentType().getTypeHandler());
    }

    @Test
    public void testWithKeyValue() {
        JavaType keyType = mapType.getKeyType();
        JavaType valueType = mapType.getContentType();
        mapType = mapType.withKeyValue(keyType, valueType);
        assertEquals(keyType, mapType.getKeyType());
        assertEquals(valueType, mapType.getContentType());
    }

    @Test
    public void testRefine() {
        Class<?> rawType = Map.class;
        TypeBindings bindings = null;
        JavaType superClass = null;
        JavaType[] superInterfaces = null;
        JavaType keyType = null;
        JavaType valueType = null;
        Object valueHandler = null;
        Object typeHandler = null;
        boolean asStatic = false;

        mapType = mapType.refine(rawType, bindings, superClass, superInterfaces);
        assertEquals(rawType, mapType.getRawClass());
        assertEquals(bindings, mapType.getBindings());
        assertEquals(superClass, mapType.getSuperClass());
        assertEquals(superInterfaces, mapType.getInterfaces());
        assertEquals(keyType, mapType.getKeyType());
        assertEquals(valueType, mapType.getContentType());
        assertEquals(valueHandler, mapType.getValueHandler());
        assertEquals(typeHandler, mapType.getTypeHandler());
        assertEquals(asStatic, mapType.isStaticTyping());
    }

    @Test
    public void testToString() {
        String expected = "[map type; class java.util.Map, null -> null]";
        assertEquals(expected, mapType.toString());
    }
}