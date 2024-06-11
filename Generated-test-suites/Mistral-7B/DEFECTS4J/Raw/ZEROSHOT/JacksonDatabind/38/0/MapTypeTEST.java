package com.fasterxml.jackson.databind.type;

import static org.junit.Assert.*;

import org.junit.Test;

import com.fasterxml.jackson.core.type.TypeBindings;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.MapType;

public class MapTypeTest {

    @Test
    public void testConstruct() {
        JavaType keyType = JavaType.constructType(int.class, int.class);
        JavaType valueType = JavaType.constructType(String.class, String.class);
        JavaType mapType = MapType.construct(Map.class, TypeBindings.empty(), null, null, keyType, valueType);
        assertEquals(Map.class, mapType.getRawClass());
        assertEquals(keyType, mapType.getKeyType());
        assertEquals(valueType, mapType.getValueType());
    }

    @Test
    public void testConstructWithTypeBindings() {
        JavaType keyType = JavaType.constructType(int.class, int.class);
        JavaType valueType = JavaType.constructType(String.class, String.class);
        JavaType mapType = MapType.construct(Map.class, TypeBindings.defaultMapBindings(), null, null, keyType, valueType);
        assertEquals(Map.class, mapType.getRawClass());
        assertEquals(keyType, mapType.getKeyType());
        assertEquals(valueType, mapType.getValueType());
        assertEquals(TypeBindings.defaultMapBindings(), mapType.getBindings());
    }

    @Test
    public void testWithTypeHandler() {
        JavaType keyType = JavaType.constructType(int.class, int.class);
        JavaType valueType = JavaType.constructType(String.class, String.class);
        Object handler = new Object();
        JavaType mapType = new MapType(Map.class, TypeBindings.empty(), null, null, keyType, valueType);
        JavaType newMapType = mapType.withTypeHandler(handler);
        assertEquals(mapType.getRawClass(), newMapType.getRawClass());
        assertEquals(mapType.getKeyType(), newMapType.getKeyType());
        assertEquals(mapType.getValueType(), newMapType.getValueType());
        assertNotSame(mapType.getTypeHandler(), newMapType.getTypeHandler());
        assertSame(handler, newMapType.getTypeHandler());
    }

    @Test
    public void testWithContentTypeHandler() {
        JavaType keyType = JavaType.constructType(int.class, int.class);
        JavaType contentType = JavaType.constructType(String.class, String.class);
        JavaType valueType = JavaType.constructType(contentType, String.class);
        Object handler = new Object();
        JavaType mapType = new MapType(Map.class, TypeBindings.empty(), null, null, keyType, valueType);
        JavaType newMapType = mapType.withContentTypeHandler(handler);
        assertEquals(mapType.getRawClass(), newMapType.getRawClass());
        assertEquals(mapType.getKeyType(), newMapType.getKeyType());
        assertEquals(contentType, newMapType.getValueType().getValueType());
        assertNotSame(mapType.getValueType().getTypeHandler(), newMapType.getValueType().getTypeHandler());
        assertSame(handler, newMapType.getValueType().getTypeHandler());
    }

    @Test
    public void testWithValueHandler() {
        JavaType keyType = JavaType.constructType(int.class, int.class);
        JavaType valueType = JavaType.constructType(String.class, String.class);
        Object handler = new Object();
        JavaType mapType = new MapType(Map.class, TypeBindings.empty(), null, null, keyType, valueType);
        JavaType newMapType = mapType.withValueHandler(handler);
        assertEquals(mapType.getRawClass(), newMapType.getRawClass());
        assertEquals(mapType.getKeyType(), newMapType.getKeyType());
        assertEquals(mapType.getValueType(), newMapType.getValueType());
        assertNotSame(mapType.getValueType().getValueHandler(), newMapType.getValueType().getValueHandler());
        assertSame(handler, newMapType.getValueType().getValueHandler());
    }

    @Test
    public void testWithContentValueHandler() {
        JavaType keyType = JavaType.constructType(int.class, int.class);
        JavaType contentValueType = JavaType.constructType(String.class, String.class);
        JavaType valueType = JavaType.constructType(contentValueType, String.class);
        Object handler = new Object();
        JavaType mapType = new MapType(Map.class, TypeBindings.empty(), null, null, keyType, valueType);
        JavaType newMapType = mapType.withContentValueHandler(handler);
        assertEquals(mapType.getRawClass(), newMapType.getRawClass());
        assertEquals(mapType.getKeyType(), newMapType.getKeyType());
        assertEquals(contentValueType, newMapType.getValueType().getValueType().getValueType());
        assertNotSame(mapType.getValueType().getValueHandler(), newMapType.getValueType().getValueHandler());
        assertSame(handler, newMapType.getValueType().getValueHandler());
    }

    @Test
    public void testWithStaticTyping() {
        JavaType keyType = JavaType.constructType(int.class, int.class);
        JavaType valueType = JavaType.constructType(String.class, String.class);
        JavaType mapType = new MapType(Map.class, TypeBindings.empty(), null, null, keyType, valueType);
        JavaType newMapType = mapType.withStaticTyping();
        assertEquals(mapType.getRawClass(), newMapType.getRawClass());
        assertEquals(mapType.getKeyType(), newMapType.getKeyType());
        assertEquals(mapType.getValueType(), newMapType.getValueType());
        assertTrue(newMapType.isStatic());
        assertFalse(mapType.isStatic());
    }

    @Test
    public void testWithContentType() {
        JavaType keyType = JavaType.constructType(int.class, int.class);
        JavaType contentType = JavaType.constructType(String.class, String.class);
        JavaType valueType = JavaType.constructType(contentType, String.class);
        JavaType mapType = new MapType(Map.class, TypeBindings.empty(), null, null, keyType, valueType);
        JavaType newMapType = mapType.withContentType(contentType);
        assertEquals(mapType.getRawClass(), newMapType.getRawClass());
        assertEquals(mapType.getKeyType(), newMapType.getKeyType());
        assertEquals(contentType, newMapType.getValueType());
        assertNotSame(mapType.getValueType(), newMapType.getValueType());
    }

    @Test
    public void testWithKeyType() {
        JavaType oldKeyType = JavaType.constructType(int.class, int.class);
        JavaType newKeyType = JavaType.constructType(long.class, long.class);
        JavaType valueType = JavaType.constructType(String.class, String.class);
        JavaType mapType = new MapType(Map.class, TypeBindings.empty(), null, null, oldKeyType, valueType);
        JavaType newMapType = mapType.withKeyType(newKeyType);
        assertEquals(mapType.getRawClass(), newMapType.getRawClass());
        assertEquals(newKeyType, newMapType.getKeyType());
        assertEquals(mapType.getValueType(), newMapType.getValueType());
        assertNotSame(oldKeyType, newMapType.getKeyType());
    }

    @Test
    public void testWithKeyTypeHandler() {
        JavaType keyType = JavaType.constructType(int.class, int.class);
        JavaType valueType = JavaType.constructType(String.class, String.class);
        Object handler = new Object();
        JavaType mapType = new MapType(Map.class, TypeBindings.empty(), null, null, keyType, valueType);
        JavaType newMapType = mapType.withKeyTypeHandler(handler);
        assertEquals(mapType.getRawClass(), newMapType.getRawClass());
        assertEquals(mapType.getKeyType(), newMapType.getKeyType());
        assertEquals(mapType.getValueType(), newMapType.getValueType());
        assertNotSame(mapType.getKeyTypeHandler(), newMapType.getKeyTypeHandler());
        assertSame(handler, newMapType.getKeyTypeHandler());
    }

    @Test
    public void testWithKeyValueHandler() {
        JavaType keyType = JavaType.constructType(int.class, int.class);
        JavaType valueType = JavaType.constructType(String.class, String.class);
        Object handler = new Object();
        JavaType mapType = new MapType(Map.class, TypeBindings.empty(), null, null, keyType, valueType);
        JavaType newMapType = mapType.withKeyValueHandler(handler);
        assertEquals(mapType.getRawClass(), newMapType.getRawClass());
        assertEquals(mapType.getKeyType(), newMapType.getKeyType());
        assertEquals(mapType.getValueType(), newMapType.getValueType());
        assertNotSame(mapType.getKeyValueHandler(), newMapType.getKeyValueHandler());
        assertSame(handler, newMapType.getKeyValueHandler());
    }

    @Test
    public void testToString() {
        JavaType keyType = JavaType.constructType(int.class, int.class);
        JavaType valueType = JavaType.constructType(String.class, String.class);
        JavaType mapType = new MapType(Map.class, TypeBindings.empty(), null, null, keyType, valueType);
        String expectedResult = "[map type; class java.util.Map, class int, int -> class java.lang.String, java.lang.String]";
        assertEquals(expectedResult, mapType.toString());
    }
}