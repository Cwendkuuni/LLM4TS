package com.fasterxml.jackson.databind.type;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;

public class MapTypeTest {

    @Test
    public void testMapTypeConstruction() {
        JavaType keyType = TypeFactory.defaultInstance().constructType(String.class);
        JavaType valueType = TypeFactory.defaultInstance().constructType(Integer.class);
        JavaType mapType = MapType.construct(Map.class, null, null, null, keyType, valueType);
        assertEquals(Map.class, mapType.getRawClass());
        assertEquals(keyType, mapType.getKeyType());
        assertEquals(valueType, mapType.getValueType());
    }

    @Test
    public void testMapTypeConstructionWithBindings() {
        TypeBindings bindings = new TypeBindings();
        JavaType keyType = TypeFactory.defaultInstance().constructType(String.class);
        JavaType valueType = TypeFactory.defaultInstance().constructType(Integer.class);
        JavaType mapType = MapType.construct(Map.class, bindings, null, null, keyType, valueType);
        assertEquals(Map.class, mapType.getRawClass());
        assertEquals(keyType, mapType.getKeyType());
        assertEquals(valueType, mapType.getValueType());
    }

    @Test
    public void testMapTypeWithTypeHandler() {
        JavaType keyType = TypeFactory.defaultInstance().constructType(String.class);
        JavaType valueType = TypeFactory.defaultInstance().constructType(Integer.class);
        Object handler = new Object();
        JavaType mapType = new MapType(Map.class, null, null, null, keyType, valueType).withTypeHandler(handler);
        assertEquals(mapType.getClass(), MapType.class);
        assertEquals(handler, mapType.getTypeHandler());
    }

    @Test
    public void testMapTypeWithContentTypeHandler() {
        JavaType keyType = TypeFactory.defaultInstance().constructType(String.class);
        JavaType valueType = TypeFactory.defaultInstance().constructType(Integer.class);
        Object handler = new Object();
        JavaType contentType = TypeFactory.defaultInstance().constructType(String[].class);
        JavaType mapType = new MapType(Map.class, null, null, null, keyType, valueType).withContentTypeHandler(handler);
        assertEquals(mapType.getClass(), MapType.class);
        assertEquals(handler, mapType.getContentTypeHandler());
        assertEquals(contentType, mapType.getValueType());
    }

    @Test
    public void testMapTypeWithValueHandler() {
        JavaType keyType = TypeFactory.defaultInstance().constructType(String.class);
        JavaType valueType = TypeFactory.defaultInstance().constructType(Integer.class);
        Object handler = new Object();
        JavaType mapType = new MapType(Map.class, null, null, null, keyType, valueType).withValueHandler(handler);
        assertEquals(mapType.getClass(), MapType.class);
        assertEquals(handler, mapType.getValueHandler());
    }

    @Test
    public void testMapTypeWithContentValueHandler() {
        JavaType keyType = TypeFactory.defaultInstance().constructType(String.class);
        JavaType valueType = TypeFactory.defaultInstance().constructType(Integer.class);
        Object handler = new Object();
        JavaType contentValueType = TypeFactory.defaultInstance().constructType(String.class);
        JavaType mapType = new MapType(Map.class, null, null, null, keyType, valueType).withContentValueHandler(handler);
        assertEquals(mapType.getClass(), MapType.class);
        assertEquals(handler, mapType.getContentValueHandler());
        assertEquals(contentValueType, mapType.getValueType().getValueType());
    }

    @Test
    public void testMapTypeWithStaticTyping() {
        JavaType keyType = TypeFactory.defaultInstance().constructType(String.class);
        JavaType valueType = TypeFactory.defaultInstance().constructType(Integer.class);
        JavaType mapType = new MapType(Map.class, null, null, null, keyType, valueType).withStaticTyping();
        assertTrue(mapType.isStatic());
        assertEquals(mapType.getClass(), MapType.class);
    }

    @Test
    public void testMapTypeWithContentType() {
        JavaType keyType = TypeFactory.defaultInstance().constructType(String.class);
        JavaType valueType = TypeFactory.defaultInstance().constructType(Integer.class);
        JavaType contentType = TypeFactory.defaultInstance().constructType(String[].class);
        JavaType mapType = new MapType(Map.class, null, null, null, keyType, valueType).withContentType(contentType);
        assertEquals(mapType.getValueType(), contentType);
    }

    @Test
    public void testMapTypeWithKeyType() {
        JavaType keyType1 = TypeFactory.defaultInstance().constructType(String.class);
        JavaType valueType = TypeFactory.defaultInstance().constructType(Integer.class);
        JavaType keyType2 = TypeFactory.defaultInstance().constructType(Integer.class);
        JavaType mapType = new MapType(Map.class, null, null, null, keyType1, valueType).withKeyType(keyType2);
        assertEquals(keyType2, mapType.getKeyType());
    }

    @Test
    public void testMapTypeRefine() {
        JavaType rawType = Map.class;
        TypeBindings bindings = new TypeBindings();
        JavaType keyType = TypeFactory.defaultInstance().constructType(String.class);
        JavaType valueType = TypeFactory.defaultInstance().constructType(Integer.class);
        JavaType mapType = new MapType(rawType, null, null, null, keyType, valueType).refine(Map.class, bindings, null, null);
        assertEquals(Map.class, mapType.getRawClass());
        assertEquals(keyType, mapType.getKeyType());
        assertEquals(valueType, mapType.getValueType());
    }

    @Test
    public void testMapTypeWithKeyTypeHandler() {
        JavaType keyType = TypeFactory.defaultInstance().constructType(String.class);
        JavaType valueType = TypeFactory.defaultInstance().constructType(Integer.class);
        Object handler = new Object();
        JavaType mapType = new MapType(Map.class, null, null, null, keyType, valueType).withKeyTypeHandler(handler);
        assertEquals(mapType.getClass(), MapType.class);
        assertEquals(handler, mapType.getKeyTypeHandler());
    }

    @Test
    public void testMapTypeWithKeyValueHandler() {
        JavaType keyType = TypeFactory.defaultInstance().constructType(String.class);
        JavaType valueType = TypeFactory.defaultInstance().constructType(Integer.class);
        Object handler = new Object();
        JavaType mapType = new MapType(Map.class, null, null, null, keyType, valueType).withKeyValueHandler(handler);
        assertEquals(mapType.getClass(), MapType.class);
        assertEquals(handler, mapType.getKeyValueHandler());
    }

    @Test
    public void testToString() {
        JavaType keyType = TypeFactory.defaultInstance().constructType(String.class);
        JavaType valueType = TypeFactory.defaultInstance().constructType(Integer.class);
        String expected = "[map type; class java.util.Map, class java.lang.String -> class java.lang.Integer]";
        JavaType mapType = new MapType(Map.class, null, null, null, keyType, valueType);
        assertEquals(expected, mapType.toString());
    }
}