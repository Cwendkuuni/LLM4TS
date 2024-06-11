package com.fasterxml.jackson.databind.type;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;

public class MapTypeTest {

    @Test
    public void testMapTypeConstruction() {
        JavaType keyType = new SimpleType("java.lang.String");
        JavaType valueType = new SimpleType("com.example.MyPojo");
        JavaType mapType = MapType.construct(Map.class, null, null, null, keyType, valueType);
        assertNotNull(mapType);
        assertEquals(Map.class, mapType.getRawClass());
        assertEquals(keyType, mapType.getKeyType());
        assertEquals(valueType, mapType.getValueType());
    }

    @Test
    public void testMapTypeWithTypeHandler() {
        JavaType keyType = new SimpleType("java.lang.String");
        JavaType valueType = new SimpleType("com.example.MyPojo");
        Object valueHandler = new Object() { /* some implementation */ };
        JavaType mapType = new MapType(Map.class, null, null, null, keyType, valueType, valueHandler, null, false);
        assertEquals(mapType.getClass(), MapType.class);
        assertEquals(Map.class, mapType.getRawClass());
        assertEquals(keyType, mapType.getKeyType());
        assertEquals(valueType, mapType.getValueType());
        assertEquals(valueHandler, mapType.getValueHandler());
        assertEquals(null, mapType.getTypeHandler());
        assertEquals(false, mapType.isStatic());
    }

    @Test
    public void testMapTypeWithContentTypeHandler() {
        JavaType keyType = new SimpleType("java.lang.String");
        JavaType valueType = new SimpleType("com.example.MyPojo");
        Object contentTypeHandler = new Object() { /* some implementation */ };
        JavaType mapType = new MapType(Map.class, null, null, null, keyType, valueType, null, contentTypeHandler, false);
        assertEquals(mapType.getClass(), MapType.class);
        assertEquals(Map.class, mapType.getRawClass());
        assertEquals(keyType, mapType.getKeyType());
        assertEquals(valueType, mapType.getValueType());
        assertEquals(null, mapType.getValueHandler());
        assertEquals(contentTypeHandler, mapType.getTypeHandler());
        assertEquals(false, mapType.isStatic());
    }

    @Test
    public void testMapTypeWithValueHandler() {
        JavaType keyType = new SimpleType("java.lang.String");
        JavaType valueType = new SimpleType("com.example.MyPojo");
        Object valueHandler = new Object() { /* some implementation */ };
        JavaType mapType = new MapType(Map.class, null, null, null, keyType, valueType, valueHandler, null, false);
        assertEquals(mapType.getClass(), MapType.class);
        assertEquals(Map.class, mapType.getRawClass());
        assertEquals(keyType, mapType.getKeyType());
        assertEquals(valueType, mapType.getValueType());
        assertEquals(valueHandler, mapType.getValueHandler());
        assertEquals(null, mapType.getTypeHandler());
        assertEquals(false, mapType.isStatic());
    }

    @Test
    public void testMapTypeWithContentValueHandler() {
        JavaType keyType = new SimpleType("java.lang.String");
        JavaType valueType = new SimpleType("com.example.MyPojo");
        Object contentValueHandler = new Object() { /* some implementation */ };
        JavaType mapType = new MapType(Map.class, null, null, null, keyType, valueType, null, null, false);
        JavaType mapTypeWithContentValueHandler = mapType.withContentValueHandler(contentValueHandler);
        assertEquals(mapTypeWithContentValueHandler.getClass(), MapType.class);
        assertEquals(Map.class, mapTypeWithContentValueHandler.getRawClass());
        assertEquals(keyType, mapTypeWithContentValueHandler.getKeyType());
        assertEquals(valueType, mapTypeWithContentValueHandler.getValueType());
        assertEquals(null, mapTypeWithContentValueHandler.getValueHandler());
        assertEquals(null, mapTypeWithContentValueHandler.getTypeHandler());
        assertEquals(false, mapTypeWithContentValueHandler.isStatic());
        assertEquals(contentValueHandler, mapTypeWithContentValueHandler.getContentValueHandler());
    }

    @Test
    public void testMapTypeWithStaticTyping() {
        JavaType keyType = new SimpleType("java.lang.String");
        JavaType valueType = new SimpleType("com.example.MyPojo");
        JavaType mapType = new MapType(Map.class, null, null, null, keyType, valueType, null, null, false);
        JavaType mapTypeWithStaticTyping = mapType.withStaticTyping();
        assertEquals(mapTypeWithStaticTyping.getClass(), MapType.class);
        assertEquals(Map.class, mapTypeWithStaticTyping.getRawClass());
        assertEquals(keyType, mapTypeWithStaticTyping.getKeyType());
        assertEquals(valueType, mapTypeWithStaticTyping.getValueType());
        assertEquals(null, mapTypeWithStaticTyping.getValueHandler());
        assertEquals(null, mapTypeWithStaticTyping.getTypeHandler());
        assertEquals(true, mapTypeWithStaticTyping.isStatic());
    }

    @Test
    public void testMapTypeWithContentType() {
        JavaType keyType = new SimpleType("java.lang.String");
        JavaType valueType = new SimpleType("com.example.MyPojo");
        JavaType contentType = new SimpleType("com.example.MyPojoWithContentType");
        JavaType mapType = new MapType(Map.class, null, null, null, keyType, valueType, null, null, false);
        JavaType mapTypeWithContentType = mapType.withContentType(contentType);
        assertEquals(mapTypeWithContentType.getClass(), MapType.class);
        assertEquals(Map.class, mapTypeWithContentType.getRawClass());
        assertEquals(keyType, mapTypeWithContentType.getKeyType());
        assertEquals(contentType, mapTypeWithContentType.getValueType());
        assertEquals(null, mapTypeWithContentType.getValueHandler());
        assertEquals(null, mapTypeWithContentType.getTypeHandler());
        assertEquals(false, mapTypeWithContentType.isStatic());
    }

    @Test
    public void testMapTypeWithKeyType() {
        JavaType keyType1 = new SimpleType("java.lang.String");
        JavaType valueType = new SimpleType("com.example.MyPojo");
        JavaType keyType2 = new SimpleType("java.lang.Integer");
        JavaType mapType = new MapType(Map.class, null, null, null, keyType1, valueType, null, null, false);
        JavaType mapTypeWithKeyType = mapType.withKeyType(keyType2);
        assertEquals(mapTypeWithKeyType.getClass(), MapType.class);
        assertEquals(Map.class, mapTypeWithKeyType.getRawClass());
        assertEquals(keyType2, mapTypeWithKeyType.getKeyType());
        assertEquals(valueType, mapTypeWithKeyType.getValueType());
        assertEquals(null, mapTypeWithKeyType.getValueHandler());
        assertEquals(null, mapTypeWithKeyType.getTypeHandler());
        assertEquals(false, mapTypeWithKeyType.isStatic());
    }

    @Test
    public void testMapTypeWithKeyTypeHandler() {
        JavaType keyType = new SimpleType("java.lang.String");
        JavaType valueType = new SimpleType("com.example.MyPojo");
        Object keyTypeHandler = new Object() { /* some implementation */ };
        JavaType mapType = new MapType(Map.class, null, null, null, keyType, valueType, null, null, false);
        JavaType mapTypeWithKeyTypeHandler = mapType.withKeyTypeHandler(keyTypeHandler);
        assertEquals(mapTypeWithKeyTypeHandler.getClass(), MapType.class);
        assertEquals(Map.class, mapTypeWithKeyTypeHandler.getRawClass());
        assertEquals(keyType, mapTypeWithKeyTypeHandler.getKeyType());
        assertEquals(valueType, mapTypeWithKeyTypeHandler.getValueType());
        assertEquals(null, mapTypeWithKeyTypeHandler.getValueHandler());
        assertEquals(null, mapTypeWithKeyTypeHandler.getTypeHandler());
        assertEquals(false, mapTypeWithKeyTypeHandler.isStatic());
        assertEquals(keyTypeHandler, mapTypeWithKeyTypeHandler.getKeyTypeHandler());
    }

    @Test
    public void testMapTypeWithKeyValueHandler() {
        JavaType keyType = new SimpleType("java.lang.String");
        JavaType valueType = new SimpleType("com.example.MyPojo");
        Object keyValueHandler = new Object() { /* some implementation */ };
        JavaType mapType = new MapType(Map.class, null, null, null, keyType, valueType, null, null, false);
        JavaType mapTypeWithKeyValueHandler = mapType.withKeyValueHandler(keyValueHandler);
        assertEquals(mapTypeWithKeyValueHandler.getClass(), MapType.class);
        assertEquals(Map.class, mapTypeWithKeyValueHandler.getRawClass());
        assertEquals(keyType, mapTypeWithKeyValueHandler.getKeyType());
        assertEquals(valueType, mapTypeWithKeyValueHandler.getValueType());
        assertEquals(null, mapTypeWithKeyValueHandler.getValueHandler());
        assertEquals(null, mapTypeWithKeyValueHandler.getTypeHandler());
        assertEquals(false, mapTypeWithKeyValueHandler.isStatic());
        assertEquals(keyValueHandler, mapTypeWithKeyValueHandler.getKeyValueHandler());
    }
}