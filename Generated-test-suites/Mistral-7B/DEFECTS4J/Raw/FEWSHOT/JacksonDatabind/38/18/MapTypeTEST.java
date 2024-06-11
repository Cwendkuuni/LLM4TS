package com.fasterxml.jackson.databind.type;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.MapType;

import org.junit.Test;

public class MapTypeTest {

    @Test
    public void testMapTypeConstruction() {
        JavaType keyType = JavaType.constructType(String.class);
        JavaType valueType = JavaType.constructType(Integer.class);
        JavaType mapType = MapType.construct(Map.class, null, null, null, keyType, valueType);
        assertNotNull(mapType);
        assertEquals(Map.class, mapType.getRawClass());
        assertEquals(keyType, mapType.getKeyType());
        assertEquals(valueType, mapType.getValueType());
    }

    @Test
    public void testMapTypeWithTypeHandler() {
        JavaType keyType = JavaType.constructType(String.class);
        JavaType valueType = JavaType.constructType(Integer.class);
        Object typeHandler = new Object() {};
        MapType mapType = new MapType(Map.class, null, null, null, keyType, valueType, null, typeHandler, false);
        assertNotNull(mapType);
        assertEquals(Map.class, mapType.getRawClass());
        assertEquals(keyType, mapType.getKeyType());
        assertEquals(valueType, mapType.getValueType());
        assertNotNull(mapType.getTypeHandler());
    }

    @Test
    public void testMapTypeWithContentTypeHandler() {
        JavaType keyType = JavaType.constructType(String.class);
        JavaType valueType = JavaType.constructType(Map.class);
        Object contentTypeHandler = new Object() {};
        MapType mapType = new MapType(Map.class, null, null, null, keyType, valueType, null, null, false);
        assertNotNull(mapType);
        assertEquals(Map.class, mapType.getRawClass());
        assertEquals(keyType, mapType.getKeyType());
        assertEquals(valueType, mapType.getValueType());
        assertNull(mapType.getTypeHandler());
        MapType mapTypeWithContentTypeHandler = mapType.withContentTypeHandler(contentTypeHandler);
        assertNotNull(mapTypeWithContentTypeHandler);
        assertNotNull(mapTypeWithContentTypeHandler.getTypeHandler());
        assertEquals(contentTypeHandler, mapTypeWithContentTypeHandler.getTypeHandler());
    }

    @Test
    public void testMapTypeWithValueHandler() {
        JavaType keyType = JavaType.constructType(String.class);
        JavaType valueType = JavaType.constructType(Integer.class);
        Object valueHandler = new Object() {};
        MapType mapType = new MapType(Map.class, null, null, null, keyType, valueType, valueHandler, null, false);
        assertNotNull(mapType);
        assertEquals(Map.class, mapType.getRawClass());
        assertEquals(keyType, mapType.getKeyType());
        assertEquals(valueType, mapType.getValueType());
        assertNotNull(mapType.getValueHandler());
        assertEquals(valueHandler, mapType.getValueHandler());
    }

    @Test
    public void testMapTypeWithContentValueHandler() {
        JavaType keyType = JavaType.constructType(String.class);
        JavaType valueType = JavaType.constructType(Map.class);
        Object contentValueHandler = new Object() {};
        MapType mapType = new MapType(Map.class, null, null, null, keyType, valueType, null, null, false);
        assertNotNull(mapType);
        assertEquals(Map.class, mapType.getRawClass());
        assertEquals(keyType, mapType.getKeyType());
        assertEquals(valueType, mapType.getValueType());
        assertNull(mapType.getValueHandler());
        MapType mapTypeWithContentValueHandler = mapType.withContentValueHandler(contentValueHandler);
        assertNotNull(mapTypeWithContentValueHandler);
        assertNotNull(mapTypeWithContentValueHandler.getValueHandler());
        assertEquals(contentValueHandler, mapTypeWithContentValueHandler.getValueHandler());
    }

    @Test
    public void testMapTypeWithStaticTyping() {
        JavaType keyType = JavaType.constructType(String.class);
        JavaType valueType = JavaType.constructType(Integer.class);
        MapType mapType = new MapType(Map.class, null, null, null, keyType, valueType, null, null, false);
        assertNotNull(mapType);
        assertFalse(mapType.isStatic());
        MapType mapTypeWithStaticTyping = mapType.withStaticTyping();
        assertNotNull(mapTypeWithStaticTyping);
        assertTrue(mapTypeWithStaticTyping.isStatic());
    }

    @Test
    public void testMapTypeWithContentType() {
        JavaType keyType = JavaType.constructType(String.class);
        JavaType valueType = JavaType.constructType(Integer.class);
        JavaType contentType = JavaType.constructType(Map.class);
        MapType mapType = new MapType(Map.class, null, null, null, keyType, valueType, null, null, false);
        assertNotNull(mapType);
        assertNull(mapType.getContentType());
        MapType mapTypeWithContentType = mapType.withContentType(contentType);
        assertNotNull(mapTypeWithContentType);
        assertNotNull(mapTypeWithContentType.getContentType());
        assertEquals(contentType, mapTypeWithContentType.getContentType());
    }

    @Test
    public void testMapTypeWithKeyType() {
        JavaType keyType1 = JavaType.constructType(String.class);
        JavaType valueType = JavaType.constructType(Integer.class);
        MapType mapType = new MapType(Map.class, null, null, null, keyType1, valueType, null, null, false);
        assertNotNull(mapType);
        assertNotNull(mapType.getKeyType());
        assertEquals(keyType1, mapType.getKeyType());
        JavaType keyType2 = JavaType.constructType(Long.class);
        MapType mapTypeWithKeyType = mapType.withKeyType(keyType2);
        assertNotNull(mapTypeWithKeyType);
        assertNotNull(mapTypeWithKeyType.getKeyType());
        assertEquals(keyType2, mapTypeWithKeyType.getKeyType());
    }

    @Test
    public void testMapTypeWithKeyTypeHandler() {
        JavaType keyType = JavaType.constructType(String.class);
        JavaType valueType = JavaType.constructType(Integer.class);
        Object keyTypeHandler = new Object() {};
        MapType mapType = new MapType(Map.class, null, null, null, keyType, valueType, null, null, false);
        assertNotNull(mapType);
        assertNull(mapType.getKeyTypeHandler());
        MapType mapTypeWithKeyTypeHandler = mapType.withKeyTypeHandler(keyTypeHandler);
        assertNotNull(mapTypeWithKeyTypeHandler);
        assertNotNull(mapTypeWithKeyTypeHandler.getKeyTypeHandler());
        assertEquals(keyTypeHandler, mapTypeWithKeyTypeHandler.getKeyTypeHandler());
    }

    @Test
    public void testMapTypeWithKeyValueHandler() {
        JavaType keyType = JavaType.constructType(String.class);
        JavaType valueType = JavaType.constructType(Integer.class);
        Object keyValueHandler = new Object() {};
        MapType mapType = new MapType(Map.class, null, null, null, keyType, valueType, null, null, false);
        assertNotNull(mapType);
        assertNull(mapType.getKeyValueHandler());
        MapType mapTypeWithKeyValueHandler = mapType.withKeyValueHandler(keyValueHandler);
        assertNotNull(mapTypeWithKeyValueHandler);
        assertNotNull(mapTypeWithKeyValueHandler.getKeyValueHandler());
        assertEquals(keyValueHandler, mapTypeWithKeyValueHandler.getKeyValueHandler());
    }

    @Test
    public void testMapTypeToString() {
        JavaType keyType = JavaType.constructType(String.class);
        JavaType valueType = JavaType.constructType(Integer.class);
        MapType mapType = new MapType(Map.class, null, null, null, keyType, valueType, null, null, false);
        assertEquals("[map type; class java.util.Map, java.lang.String -> java.lang.Integer]", mapType.toString());
    }
}