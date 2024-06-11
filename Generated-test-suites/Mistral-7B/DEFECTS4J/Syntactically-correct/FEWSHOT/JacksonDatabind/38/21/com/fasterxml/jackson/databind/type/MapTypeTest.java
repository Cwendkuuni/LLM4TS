package com.fasterxml.jackson.databind.type;

import static org.junit.Assert.*;

import org.junit.Test;

public class MapTypeTest {

    @Test
    public void testMapTypeConstruction() {
        Class<?> rawType = Map.class;
        TypeBindings bindings = TypeBindings.empty();
        JavaType keyType = JavaType.construct(String.class);
        JavaType valueType = JavaType.construct(Integer.class);
        MapType mapType = MapType.construct(rawType, bindings, null, null, keyType, valueType);
        assertEquals(Map.class, mapType.getRawClass());
        assertEquals(keyType, mapType.getKeyType());
        assertEquals(valueType, mapType.getValueType());
    }

    @Test
    public void testMapTypeWithTypeHandlers() {
        Class<?> rawType = Map.class;
        TypeBindings bindings = TypeBindings.empty();
        JavaType keyType = JavaType.construct(String.class);
        JavaType valueType = JavaType.construct(Integer.class);
        Object keyHandler = new Object();
        Object valueHandler = new Object();
        Object typeHandler = new Object();
        MapType mapType = new MapType(rawType, bindings, null, null, keyType, valueType, keyHandler, typeHandler, false);
        MapType mapTypeWithValueHandler = mapType.withValueHandler(valueHandler);
        assertEquals(mapType.getClass(), mapTypeWithValueHandler.getClass());
        assertEquals(valueHandler, mapTypeWithValueHandler.getValueHandler());

        MapType mapTypeWithKeyHandler = mapType.withKeyTypeHandler(keyHandler);
        assertEquals(mapType.getClass(), mapTypeWithKeyHandler.getClass());
        assertEquals(keyHandler, mapTypeWithKeyHandler.getKeyTypeHandler());
    }

    @Test
    public void testMapTypeWithStaticTyping() {
        Class<?> rawType = Map.class;
        TypeBindings bindings = TypeBindings.empty();
        JavaType keyType = JavaType.construct(String.class).withStaticTyping();
        JavaType valueType = JavaType.construct(Integer.class).withStaticTyping();
        MapType mapType = new MapType(rawType, bindings, null, null, keyType, valueType, null, null, false);
        MapType mapTypeWithStaticTyping = mapType.withStaticTyping();
        assertTrue(mapTypeWithStaticTyping.isStatic());
        assertFalse(mapType.isStatic());
    }

    @Test
    public void testMapTypeWithContentType() {
        Class<?> rawType = Map.class;
        TypeBindings bindings = TypeBindings.empty();
        JavaType keyType = JavaType.construct(String.class);
        JavaType valueType = JavaType.construct(Integer.class);
        JavaType contentType = JavaType.construct(Double.class);
        MapType mapType = new MapType(rawType, bindings, null, null, keyType, valueType, null, null, false);
        MapType mapTypeWithContentType = mapType.withContentType(contentType);
        assertEquals(contentType, mapTypeWithContentType.getValueType());
    }

    @Test
    public void testMapTypeWithKeyType() {
        Class<?> rawType = Map.class;
        TypeBindings bindings = TypeBindings.empty();
        JavaType keyType1 = JavaType.construct(String.class);
        JavaType valueType = JavaType.construct(Integer.class);
        JavaType keyType2 = JavaType.construct(Character.class);
        MapType mapType = new MapType(rawType, bindings, null, null, keyType1, valueType, null, null, false);
        MapType mapTypeWithKeyType = mapType.withKeyType(keyType2);
        assertEquals(keyType2, mapTypeWithKeyType.getKeyType());
    }

    @Test
    public void testMapTypeRefine() {
        Class<?> rawType = Map.class;
        TypeBindings bindings = TypeBindings.empty();
        JavaType keyType = JavaType.construct(String.class);
        JavaType valueType = JavaType.construct(Integer.class);
        JavaType refinedKeyType = JavaType.construct(Character.class);
        JavaType refinedValueType = JavaType.construct(Double.class);
        MapType mapType = new MapType(rawType, bindings, null, null, keyType, valueType, null, null, false);
        MapType refinedMapType = mapType.refine(Map.class, bindings, null, new JavaType[]{JavaType.construct(Object.class)});
        assertEquals(refinedKeyType, refinedMapType.getKeyType());
        assertEquals(refinedValueType, refinedMapType.getValueType());
    }
}