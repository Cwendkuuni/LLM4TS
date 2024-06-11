package com.fasterxml.jackson.databind.type;

import static org.junit.Assert.*;

import org.junit.Test;

public class MapTypeTest {

    @Test
    public void testConstruct() {
        Class<Map<String, Integer>> rawType = Map.class;
        TypeBindings bindings = TypeBindings.empty();
        JavaType keyType = TypeFactory.defaultInstance().constructType(String.class);
        JavaType valueType = TypeFactory.defaultInstance().constructType(Integer.class);
        MapType mapType = MapType.construct(rawType, bindings, null, null, keyType, valueType);

        assertEquals(Map.class, mapType.getRawClass());
        assertEquals(keyType, mapType.getKeyType());
        assertEquals(valueType, mapType.getValueType());
        assertNull(mapType.getTypeHandler());
        assertNull(mapType.getValueHandler());
        assertFalse(mapType.isStatic());
    }

    @Test
    public void testConstructWithBindingsAndValueHandlers() {
        Class<Map<String, Integer>> rawType = Map.class;
        TypeBindings bindings = TypeBindings.empty();
        JavaType keyType = TypeFactory.defaultInstance().constructType(String.class);
        JavaType valueType = TypeFactory.defaultInstance().constructType(Integer.class);
        Object keyHandler = new Object();
        Object valueHandler = new Object();

        MapType mapType = MapType.construct(rawType, bindings, null, null, keyType, valueType, keyHandler, valueHandler, false);

        assertEquals(Map.class, mapType.getRawClass());
        assertEquals(keyType, mapType.getKeyType());
        assertEquals(valueType, mapType.getValueType());
        assertEquals(keyHandler, mapType.getTypeHandler());
        assertEquals(valueHandler, mapType.getValueHandler());
        assertFalse(mapType.isStatic());
    }

    @Test
    public void testNarrow() {
        Class<Map<String, Integer>> rawType = Map.class;
        JavaType mapType = TypeFactory.defaultInstance().constructType(MapType.class, rawType);

        JavaType narrowedType = mapType.narrow(Map.class);

        assertEquals(Map.class, narrowedType.getRawClass());
        assertEquals(mapType.getKeyType(), narrowedType.getKeyType());
        assertEquals(mapType.getValueType(), narrowedType.getValueType());
        assertNull(narrowedType.getTypeHandler());
        assertNull(narrowedType.getValueHandler());
        assertFalse(narrowedType.isStatic());
    }

    @Test
    public void testWithTypeHandler() {
        Class<Map<String, Integer>> rawType = Map.class;
        JavaType keyType = TypeFactory.defaultInstance().constructType(String.class);
        JavaType valueType = TypeFactory.defaultInstance().constructType(Integer.class);
        Object typeHandler = new Object();

        MapType mapType = new MapType(rawType, null, null, null, keyType, valueType, null, null, false);
        MapType withTypeHandler = mapType.withTypeHandler(typeHandler);

        assertEquals(mapType.getRawClass(), withTypeHandler.getRawClass());
        assertEquals(mapType.getKeyType(), withTypeHandler.getKeyType());
        assertEquals(mapType.getValueType(), withTypeHandler.getValueType());
        assertEquals(typeHandler, withTypeHandler.getTypeHandler());
        assertNull(withTypeHandler.getValueHandler());
        assertFalse(withTypeHandler.isStatic());
    }

    @Test
    public void testWithContentTypeHandler() {
        Class<Map<String, Integer>> rawType = Map.class;
        JavaType mapType = TypeFactory.defaultInstance().constructType(MapType.class, rawType);
        JavaType contentTypeHandler = TypeFactory.defaultInstance().constructType(Object.class);

        MapType withContentTypeHandler = mapType.withContentTypeHandler(contentTypeHandler);

        assertEquals(mapType.getRawClass(), withContentTypeHandler.getRawClass());
        assertEquals(mapType.getKeyType(), withContentTypeHandler.getKeyType());
        JavaType valueType = mapType.getValueType();
        JavaType newValueType = withContentTypeHandler.getValueType();
        assertEquals(valueType.getRawClass(), newValueType.getRawClass());
        assertEquals(valueType.getKeyType(), newValueType.getKeyType());
        assertEquals(contentTypeHandler, newValueType.getTypeHandler());
        assertNull(newValueType.getValueHandler());
        assertFalse(withContentTypeHandler.isStatic());
    }

    @Test
    public void testWithValueHandler() {
        Class<Map<String, Integer>> rawType = Map.class;
        JavaType keyType = TypeFactory.defaultInstance().constructType(String.class);
        JavaType valueType = TypeFactory.defaultInstance().constructType(Integer.class);
        Object valueHandler = new Object();

        MapType mapType = new MapType(rawType, null, null, null, keyType, valueType, null, null, false);
        MapType withValueHandler = mapType.withValueHandler(valueHandler);

        assertEquals(mapType.getRawClass(), withValueHandler.getRawClass());
        assertEquals(mapType.getKeyType(), withValueHandler.getKeyType());
        assertEquals(mapType.getValueType(), withValueHandler.getValueType());
        assertEquals(null, withValueHandler.getTypeHandler());
        assertEquals(valueHandler, withValueHandler.getValueHandler());
        assertFalse(withValueHandler.isStatic());
    }

    @Test
    public void testWithContentValueHandler() {
        Class<Map<String, Integer>> rawType = Map.class;
        JavaType mapType = TypeFactory.defaultInstance().constructType(MapType.class, rawType);
        Object contentValueHandler = new Object();

        MapType withContentValueHandler = mapType.withContentValueHandler(contentValueHandler);

        assertEquals(mapType.getRawClass(), withContentValueHandler.getRawClass());
        assertEquals(mapType.getKeyType(), withContentValueHandler.getKeyType());
        JavaType valueType = mapType.getValueType();
        JavaType newValueType = withContentValueHandler.getValueType();
        assertEquals(valueType.getRawClass(), newValueType.getRawClass());
        assertEquals(valueType.getKeyType(), newValueType.getKeyType());
        assertEquals(null, newValueType.getTypeHandler());
        assertEquals(contentValueHandler, newValueType.getValueHandler());
        assertFalse(withContentValueHandler.isStatic());
    }

    @Test
    public void testWithStaticTyping() {
        Class<Map<String, Integer>> rawType = Map.class;
        JavaType keyType = TypeFactory.defaultInstance().constructType(String.class);
        JavaType valueType = TypeFactory.defaultInstance().constructType(Integer.class);
        MapType mapType = new MapType(rawType, null, null, null, keyType, valueType, null, null, false);
        MapType withStaticTyping = mapType.withStaticTyping();

        assertEquals(mapType.getRawClass(), withStaticTyping.getRawClass());
        assertEquals(mapType.getKeyType(), withStaticTyping.getKeyType());
        assertEquals(mapType.getValueType(), withStaticTyping.getValueType());
        assertNull(withStaticTyping.getTypeHandler());
        assertNull(withStaticTyping.getValueHandler());
        assertTrue(withStaticTyping.isStatic());
    }

    @Test
    public void testWithContentType() {
        Class<Map<String, Integer>> rawType = Map.class;
        JavaType mapType = TypeFactory.defaultInstance().constructType(MapType.class, rawType);
        JavaType contentType = TypeFactory.defaultInstance().constructType(List.class, String.class);

        MapType withContentType = mapType.withContentType(contentType);

        assertEquals(mapType.getRawClass(), withContentType.getRawClass());
        assertEquals(mapType.getKeyType(), withContentType.getKeyType());
        JavaType valueType = mapType.getValueType();
        JavaType newValueType = withContentType.getValueType();
        assertEquals(valueType.getRawClass(), newValueType.getRawClass());
        assertEquals(valueType.getKeyType(), newValueType.getKeyType());
        assertEquals(contentType, newValueType);
        assertNull(newValueType.getTypeHandler());
        assertNull(newValueType.getValueHandler());
        assertFalse(withContentType.isStatic());
    }

    @Test
    public void testWithKeyType() {
        Class<Map<String, Integer>> rawType = Map.class;
        JavaType mapType = TypeFactory.defaultInstance().constructType(MapType.class, rawType);
        JavaType keyType = TypeFactory.defaultInstance().constructType(String.class);

        MapType withKeyType = mapType.withKeyType(keyType);

        assertEquals(mapType.getRawClass(), withKeyType.getRawClass());
        JavaType oldKeyType = mapType.getKeyType();
        JavaType newKeyType = withKeyType.getKeyType();
        assertEquals(oldKeyType.getRawClass(), newKeyType.getRawClass());
        assertEquals(oldKeyType, newKeyType);
        assertNull(withKeyType.getTypeHandler());
        assertNull(withKeyType.getValueHandler());
        assertFalse(withKeyType.isStatic());
    }

    @Test
    public void testRefine() {
        Class<Map<String, Integer>> rawType = Map.class;
        TypeBindings bindings = TypeBindings.empty();
        JavaType mapType = TypeFactory.defaultInstance().constructType(MapType.class, rawType);
        JavaType newSuperClass = TypeFactory.defaultInstance().constructType(Map.class, Object.class);
        JavaType[] newSuperInterfaces = new JavaType[0];

        MapType refinedType = mapType.refine(Map.class, bindings, newSuperClass, newSuperInterfaces);

        assertEquals(mapType.getRawClass(), refinedType.getRawClass());
        assertEquals(mapType.getKeyType(), refinedType.getKeyType());
        assertEquals(mapType.getValueType(), refinedType.getValueType());
        assertNull(refinedType.getTypeHandler());
        assertNull(refinedType.getValueHandler());
        assertFalse(refinedType.isStatic());
    }

    @Test
    public void testWithKeyTypeHandler() {
        Class<Map<String, Integer>> rawType = Map.class;
        JavaType mapType = TypeFactory.defaultInstance().constructType(MapType.class, rawType);
        Object keyTypeHandler = new Object();

        MapType withKeyTypeHandler = mapType.withKeyTypeHandler(keyTypeHandler);

        assertEquals(mapType.getRawClass(), withKeyTypeHandler.getRawClass());
        assertEquals(mapType.getKeyType(), withKeyTypeHandler.getKeyType());
        JavaType oldKeyType = mapType.getKeyType();
        JavaType newKeyType = withKeyTypeHandler.getKeyType();
        assertEquals(oldKeyType.getRawClass(), newKeyType.getRawClass());
        assertEquals(oldKeyType, newKeyType);
        assertEquals(null, withKeyTypeHandler.getTypeHandler());
        assertEquals(null, withKeyTypeHandler.getValueHandler());
        assertFalse(withKeyTypeHandler.isStatic());
    }

    @Test
    public void testWithKeyValueHandler() {
        Class<Map<String, Integer>> rawType = Map.class;
        JavaType mapType = TypeFactory.defaultInstance().constructType(MapType.class, rawType);
        Object keyValueHandler = new Object();

        MapType withKeyValueHandler = mapType.withKeyValueHandler(keyValueHandler);

        assertEquals(mapType.getRawClass(), withKeyValueHandler.getRawClass());
        assertEquals(mapType.getKeyType(), withKeyValueHandler.getKeyType());
        JavaType oldKeyType = mapType.getKeyType();
        JavaType newKeyType = withKeyValueHandler.getKeyType();
        assertEquals(oldKeyType.getRawClass(), newKeyType.getRawClass());
        assertEquals(oldKeyType, newKeyType);
        assertEquals(null, withKeyTypeHandler.getTypeHandler());
        assertEquals(keyValueHandler, withKeyValueHandler.getValueHandler());
        assertFalse(withKeyValueHandler.isStatic());
    }
}