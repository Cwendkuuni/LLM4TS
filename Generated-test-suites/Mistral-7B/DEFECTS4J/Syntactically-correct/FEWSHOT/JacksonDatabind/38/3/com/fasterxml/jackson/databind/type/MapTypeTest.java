package com.fasterxml.jackson.databind.type;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class MapTypeTest {

    @Test
    public void testConstruct() {
        JavaType keyType = new SimpleType("java.lang.String");
        JavaType valueType = new SimpleType("java.lang.Integer");
        MapType mapType = MapType.construct(Map.class, null, null, null, keyType, valueType);
        assertEquals(Map.class, mapType.getRawClass());
        assertEquals(keyType, mapType.getKeyType());
        assertEquals(valueType, mapType.getValueType());
        assertNull(mapType.getValueHandler());
        assertNull(mapType.getTypeHandler());
        assertFalse(mapType.isStatic());
    }

    @Test
    public void testConstructDeprecated() {
        JavaType keyType = new SimpleType("java.lang.String");
        JavaType valueType = new SimpleType("java.lang.Integer");
        MapType mapType = MapType.construct(Map.class, keyType, valueType);
        assertEquals(Map.class, mapType.getRawClass());
        assertEquals(keyType, mapType.getKeyType());
        assertEquals(valueType, mapType.getValueType());
        assertNull(mapType.getValueHandler());
        assertNull(mapType.getTypeHandler());
        assertFalse(mapType.isStatic());
    }

    @Test
    public void testNarrow() {
        JavaType mapType = new SimpleType(HashMap.class);
        JavaType subclass = new SimpleType(TreeMap.class);
        MapType newMapType = mapType.narrow(subclass.getRawClass());
        assertEquals(TreeMap.class, newMapType.getRawClass());
        assertEquals(mapType.getKeyType(), newMapType.getKeyType());
        assertEquals(mapType.getValueType(), newMapType.getValueType());
        assertNull(newMapType.getValueHandler());
        assertNull(newMapType.getTypeHandler());
        assertFalse(newMapType.isStatic());
    }

    @Test
    public void testWithTypeHandler() {
        JavaType mapType = new SimpleType(HashMap.class);
        Object handler = new Object();
        MapType newMapType = mapType.withTypeHandler(handler);
        assertEquals(mapType.getRawClass(), newMapType.getRawClass());
        assertEquals(mapType.getKeyType(), newMapType.getKeyType());
        assertEquals(mapType.getValueType(), newMapType.getValueType());
        assertEquals(handler, newMapType.getTypeHandler());
        assertNull(newMapType.getValueHandler());
        assertFalse(newMapType.isStatic());
    }

    @Test
    public void testWithContentTypeHandler() {
        JavaType mapType = new SimpleType(HashMap.class);
        JavaType contentType = new SimpleType("java.lang.String");
        Object handler = new Object();
        MapType newMapType = mapType.withContentTypeHandler(handler);
        assertEquals(mapType.getRawClass(), newMapType.getRawClass());
        assertEquals(mapType.getKeyType(), newMapType.getKeyType());
        assertEquals(contentType, newMapType.getValueType());
        assertNull(newMapType.getValueHandler());
        assertEquals(handler, newMapType.getTypeHandler());
        assertFalse(newMapType.isStatic());
    }

    @Test
    public void testWithValueHandler() {
        JavaType mapType = new SimpleType(HashMap.class);
        Object handler = new Object();
        MapType newMapType = mapType.withValueHandler(handler);
        assertEquals(mapType.getRawClass(), newMapType.getRawClass());
        assertEquals(mapType.getKeyType(), newMapType.getKeyType());
        assertEquals(mapType.getValueType(), newMapType.getValueType());
        assertEquals(handler, newMapType.getValueHandler());
        assertNull(newMapType.getTypeHandler());
        assertFalse(newMapType.isStatic());
    }

    @Test
    public void testWithContentValueHandler() {
        JavaType mapType = new SimpleType(HashMap.class);
        JavaType valueType = new SimpleType("java.lang.String");
        Object handler = new Object();
        MapType newMapType = mapType.withContentValueHandler(handler);
        assertEquals(mapType.getRawClass(), newMapType.getRawClass());
        assertEquals(mapType.getKeyType(), newMapType.getKeyType());
        assertEquals(valueType, newMapType.getValueType());
        assertEquals(handler, newMapType.getValueHandler());
        assertNull(newMapType.getTypeHandler());
        assertFalse(newMapType.isStatic());
    }

    @Test
    public void testWithStaticTyping() {
        JavaType mapType = new SimpleType(HashMap.class);
        MapType newMapType = mapType.withStaticTyping();
        assertEquals(mapType.getRawClass(), newMapType.getRawClass());
        assertEquals(mapType.getKeyType(), newMapType.getKeyType());
        assertEquals(mapType.getValueType(), newMapType.getValueType());
        assertNull(newMapType.getValueHandler());
        assertNull(newMapType.getTypeHandler());
        assertTrue(newMapType.isStatic());
    }

    @Test
    public void testWithContentType() {
        JavaType mapType = new SimpleType(HashMap.class);
        JavaType contentType = new SimpleType("java.lang.String");
        MapType newMapType = mapType.withContentType(contentType);
        assertEquals(mapType.getRawClass(), newMapType.getRawClass());
        assertEquals(mapType.getKeyType(), newMapType.getKeyType());
        assertEquals(contentType, newMapType.getValueType());
        assertNull(newMapType.getValueHandler());
        assertNull(newMapType.getTypeHandler());
        assertFalse(newMapType.isStatic());
    }

    @Test
    public void testWithKeyType() {
        JavaType mapType = new SimpleType(HashMap.class);
        JavaType keyType = new SimpleType("java.lang.Integer");
        MapType newMapType = mapType.withKeyType(keyType);
        assertEquals(mapType.getRawClass(), newMapType.getRawClass());
        assertEquals(keyType, newMapType.getKeyType());
        assertEquals(mapType.getValueType(), newMapType.getValueType());
        assertNull(newMapType.getValueHandler());
        assertNull(newMapType.getTypeHandler());
        assertFalse(newMapType.isStatic());
    }

    @Test
    public void testRefine() {
        JavaType mapType = new SimpleType(HashMap.class);
        JavaType rawType = new SimpleType(TreeMap.class);
        TypeBindings bindings = new TypeBindings();
        JavaType newMapType = mapType.refine(rawType.getRawClass(), null, null, null);
        assertEquals(TreeMap.class, newMapType.getRawClass());
        assertEquals(mapType.getKeyType(), newMapType.getKeyType());
        assertEquals(mapType.getValueType(), newMapType.getValueType());
        assertNull(newMapType.getValueHandler());
        assertNull(newMapType.getTypeHandler());
        assertFalse(newMapType.isStatic());
    }

    @Test
    public void testWithKeyTypeHandler() {
        JavaType mapType = new SimpleType(HashMap.class);
        Object handler = new Object();
        JavaType keyType = new SimpleType("java.lang.Integer");
        MapType newMapType = mapType.withKeyTypeHandler(handler);
        assertEquals(mapType.getRawClass(), newMapType.getRawClass());
        assertEquals(keyType, newMapType.getKeyType());
        assertEquals(mapType.getValueType(), newMapType.getValueType());
        assertNull(newMapType.getValueHandler());
        assertEquals(handler, newMapType.getTypeHandler());
        assertFalse(newMapType.isStatic());
    }

    @Test
    public void testWithKeyValueHandler() {
        JavaType mapType = new SimpleType(HashMap.class);
        Object handler = new Object();
        JavaType keyType = new SimpleType("java.lang.Integer");
        MapType newMapType = mapType.withKeyValueHandler(handler);
        assertEquals(mapType.getRawClass(), newMapType.getRawClass());
        assertEquals(keyType, newMapType.getKeyType());
        assertEquals(mapType.getValueType(), newMapType.getValueType());
        assertEquals(handler, newMapType.getValueHandler());
        assertNull(newMapType.getTypeHandler());
        assertFalse(newMapType.isStatic());
    }

    @Test
    public void testToString() {
        JavaType mapType = new SimpleType(HashMap.class);
        String mapTypeString = mapType.toString();
        assertEquals("[map type; class java.util.HashMap, java.lang.Object -> java.lang.Object]", mapTypeString);
    }
}