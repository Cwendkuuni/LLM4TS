package com.fasterxml.jackson.databind.type;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class MapTypeTest {

    @Test
    public void testConstruct() {
        JavaType keyType = TypeFactory.defaultInstance().constructType(String.class);
        JavaType valueType = TypeFactory.defaultInstance().constructType(Integer.class);
        Class<?> rawType = Map.class;

        MapType mapType = MapType.construct(rawType, null, null, null, keyType, valueType);
        assertNotNull(mapType);
        assertEquals(Map.class, mapType.getRawClass());
        assertEquals(keyType, mapType.getKeyType());
        assertEquals(valueType, mapType.getValueType());
    }

    @Test
    public void testConstructDeprecated() {
        JavaType keyType = TypeFactory.defaultInstance().constructType(String.class);
        JavaType valueType = TypeFactory.defaultInstance().constructType(Integer.class);
        Class<?> rawType = Map.class;

        MapType mapType = MapType.construct(rawType, keyType, valueType);
        assertNotNull(mapType);
        assertEquals(Map.class, mapType.getRawClass());
        assertEquals(keyType, mapType.getKeyType());
        assertEquals(valueType, mapType.getValueType());
    }

    @Test
    public void testNarrow() {
        JavaType keyType = TypeFactory.defaultInstance().constructType(String.class);
        JavaType valueType = TypeFactory.defaultInstance().constructType(Integer.class);
        Class<?> rawType = HashMap.class;

        MapType mapType = new MapType(rawType, null, null, null, keyType, valueType, null, null, false);
        JavaType narrowedType = mapType.narrow(LinkedHashMap.class);
        assertNotNull(narrowedType);
        assertEquals(LinkedHashMap.class, narrowedType.getRawClass());
        assertEquals(keyType, narrowedType.getKeyType());
        assertEquals(valueType, narrowedType.getValueType());
    }

    @Test
    public void testWithTypeHandler() {
        JavaType keyType = TypeFactory.defaultInstance().constructType(String.class);
        JavaType valueType = TypeFactory.defaultInstance().constructType(Integer.class);
        Class<?> rawType = Map.class;

        Object handler = new Object();

        MapType mapType = new MapType(rawType, null, null, null, keyType, valueType, null, null, false);
        MapType withHandler = mapType.withTypeHandler(handler);
        assertNotNull(withHandler);
        assertEquals(mapType.getRawClass(), withHandler.getRawClass());
        assertEquals(mapType.getKeyType(), withHandler.getKeyType());
        assertEquals(mapType.getValueType(), withHandler.getValueType());
        assertEquals(handler, withHandler.getTypeHandler());
    }

    @Test
    public void testWithContentTypeHandler() {
        JavaType keyType = TypeFactory.defaultInstance().constructType(String.class);
        JavaType valueType = TypeFactory.defaultInstance().constructType(Integer.class);
        JavaType contentTypeHandler = TypeFactory.defaultInstance().constructType(Long.class);
        Class<?> rawType = Map.class;

        MapType mapType = new MapType(rawType, null, null, null, keyType, valueType, null, null, false);
        MapType withContentHandler = mapType.withContentTypeHandler(contentTypeHandler);
        assertNotNull(withContentHandler);
        assertEquals(mapType.getRawClass(), withContentHandler.getRawClass());
        assertEquals(mapType.getKeyType(), withContentHandler.getKeyType());
        assertEquals(contentTypeHandler, withContentHandler.getValueType());
    }

    @Test
    public void testWithValueHandler() {
        JavaType keyType = TypeFactory.defaultInstance().constructType(String.class);
        JavaType valueType = TypeFactory.defaultInstance().constructType(Integer.class);
        Class<?> rawType = Map.class;

        Object handler = new Object();

        MapType mapType = new MapType(rawType, null, null, null, keyType, valueType, null, null, false);
        MapType withHandler = mapType.withValueHandler(handler);
        assertNotNull(withHandler);
        assertEquals(mapType.getRawClass(), withHandler.getRawClass());
        assertEquals(mapType.getKeyType(), withHandler.getKeyType());
        assertEquals(handler, withHandler.getValueHandler());
    }

    @Test
    public void testWithContentValueHandler() {
        JavaType keyType = TypeFactory.defaultInstance().constructType(String.class);
        JavaType valueType = TypeFactory.defaultInstance().constructType(Integer.class);
        JavaType contentValueHandler = TypeFactory.defaultInstance().constructType(Long.class);
        Class<?> rawType = Map.class;

        MapType mapType = new MapType(rawType, null, null, null, keyType, valueType, null, null, false);
        MapType withContentHandler = mapType.withContentValueHandler(contentValueHandler);
        assertNotNull(withContentHandler);
        assertEquals(mapType.getRawClass(), withContentHandler.getRawClass());
        assertEquals(mapType.getKeyType(), withContentHandler.getKeyType());
        assertEquals(contentValueHandler, withContentHandler.getValueType());
        assertEquals(contentValueHandler, withContentHandler.getValueHandler());
    }

    @Test
    public void testWithStaticTyping() {
        JavaType keyType = TypeFactory.defaultInstance().constructType(String.class);
        JavaType valueType = TypeFactory.defaultInstance().constructType(Integer.class);
        Class<?> rawType = Map.class;

        MapType mapType = new MapType(rawType, null, null, null, keyType, valueType, null, null, false);
        MapType staticMapType = mapType.withStaticTyping();
        assertNotNull(staticMapType);
        assertEquals(mapType.getRawClass(), staticMapType.getRawClass());
        assertEquals(mapType.getKeyType(), staticMapType.getKeyType());
        assertEquals(mapType.getValueType(), staticMapType.getValueType());
        assertTrue(staticMapType.isStatic());
    }

    @Test
    public void testWithContentType() {
        JavaType keyType = TypeFactory.defaultInstance().constructType(String.class);
        JavaType valueType = TypeFactory.defaultInstance().constructType(Integer.class);
        JavaType contentType = TypeFactory.defaultInstance().constructType(Long.class);
        Class<?> rawType = Map.class;

        MapType mapType = new MapType(rawType, null, null, null, keyType, valueType, null, null, false);
        MapType withContentType = mapType.withContentType(contentType);
        assertNotNull(withContentType);
        assertEquals(mapType.getRawClass(), withContentType.getRawClass());
        assertEquals(mapType.getKeyType(), withContentType.getKeyType());
        assertEquals(contentType, withContentType.getValueType());
    }

    @Test
    public void testWithKeyType() {
        JavaType keyType1 = TypeFactory.defaultInstance().constructType(String.class);
        JavaType valueType = TypeFactory.defaultInstance().constructType(Integer.class);
        JavaType keyType2 = TypeFactory.defaultInstance().constructType(Integer.class);
        Class<?> rawType = Map.class;

        MapType mapType = new MapType(rawType, null, null, null, keyType1, valueType, null, null, false);
        MapType withKeyType = mapType.withKeyType(keyType2);
        assertNotNull(withKeyType);
        assertEquals(mapType.getRawClass(), withKeyType.getRawClass());
        assertEquals(keyType2, withKeyType.getKeyType());
        assertEquals(valueType, withKeyType.getValueType());
    }

    @Test
    public void testRefine() {
        JavaType keyType = TypeFactory.defaultInstance().constructType(String.class);
        JavaType valueType = TypeFactory.defaultInstance().constructType(Integer.class);
        Class<?> rawType = Map.class;

        MapType mapType = new MapType(rawType, null, null, null, keyType, valueType, null, null, false);
        JavaType refinedType = mapType.refine(HashMap.class, null, null, null);
        assertNotNull(refinedType);
        assertEquals(HashMap.class, refinedType.getRawClass());
        assertEquals(keyType, refinedType.getKeyType());
        assertEquals(valueType, refinedType.getValueType());
    }

    @Test
    public void testWithKeyTypeHandler() {
        JavaType keyType = TypeFactory.defaultInstance().constructType(String.class);
        JavaType valueType = TypeFactory.defaultInstance().constructType(Integer.class);
        Object keyHandler = new Object();
        Class<?> rawType = Map.class;

        MapType mapType = new MapType(rawType, null, null, null, keyType, valueType, null, null, false);
        MapType withKeyHandler = mapType.withKeyTypeHandler(keyHandler);
        assertNotNull(withKeyHandler);
        assertEquals(mapType.getRawClass(), withKeyHandler.getRawClass());
        assertEquals(mapType.getKeyType(), withKeyHandler.getKeyType());
        assertEquals(mapType.getValueType(), withKeyHandler.getValueType());
        assertEquals(keyHandler, withKeyHandler.getKeyTypeHandler());
    }

    @Test
    public void testWithKeyValueHandler() {
        JavaType keyType = TypeFactory.defaultInstance().constructType(String.class);
        JavaType valueType = TypeFactory.defaultInstance().constructType(Integer.class);
        Object keyValueHandler = new Object();
        Class<?> rawType = Map.class;

        MapType mapType = new MapType(rawType, null, null, null, keyType, valueType, null, null, false);
        MapType withKeyValueHandler = mapType.withKeyValueHandler(keyValueHandler);
        assertNotNull(withKeyValueHandler);
        assertEquals(mapType.getRawClass(), withKeyValueHandler.getRawClass());
        assertEquals(mapType.getKeyType(), withKeyValueHandler.getKeyType());
        assertEquals(mapType.getValueType(), withKeyValueHandler.getValueType());
        assertEquals(keyValueHandler, withKeyValueHandler.getKeyValueHandler());
    }
}