package com.fasterxml.jackson.databind.type;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class MapTypeTest {

    @Test
    public void testMapTypeConstruction() {
        JavaType keyType = JavaType.constructType(String.class);
        JavaType valueType = JavaType.constructType(Integer.class);
        Class<?> mapType = Map.class;
        TypeBindings bindings = new TypeBindings();
        JavaType superClass = Object.class;
        JavaType[] superInts = new JavaType[0];

        MapType mapTypeInstance = MapType.construct(mapType, bindings, superClass, superInts, keyType, valueType);
        assertEquals(mapType, mapTypeInstance.getRawClass());
        assertEquals(keyType, mapTypeInstance.getKeyType());
        assertEquals(valueType, mapTypeInstance.getValueType());
        assertEquals(bindings, mapTypeInstance.getBindings());
        assertEquals(superClass, mapTypeInstance.getSuperclass());
        assertEquals(superInts, mapTypeInstance.getSuperinterfaces());
    }

    @Test
    public void testMapTypeWithTypeHandler() {
        JavaType keyType = JavaType.constructType(String.class);
        JavaType valueType = JavaType.constructType(Integer.class);
        Object typeHandler = new Object();
        Class<?> mapType = Map.class;
        TypeBindings bindings = new TypeBindings();
        JavaType superClass = Object.class;
        JavaType[] superInts = new JavaType[0];

        MapType mapTypeInstance = new MapType(mapType, bindings, superClass, superInts, keyType, valueType, null, typeHandler, false);
        assertEquals(mapType, mapTypeInstance.getRawClass());
        assertEquals(keyType, mapTypeInstance.getKeyType());
        assertEquals(valueType, mapTypeInstance.getValueType());
        assertEquals(bindings, mapTypeInstance.getBindings());
        assertEquals(superClass, mapTypeInstance.getSuperclass());
        assertEquals(superInts, mapTypeInstance.getSuperinterfaces());
        assertEquals(typeHandler, mapTypeInstance.getTypeHandler());
    }

    @Test
    public void testMapTypeWithValueHandler() {
        JavaType keyType = JavaType.constructType(String.class);
        JavaType valueType = JavaType.constructType(Integer.class);
        Object valueHandler = new Object();
        Class<?> mapType = Map.class;
        TypeBindings bindings = new TypeBindings();
        JavaType superClass = Object.class;
        JavaType[] superInts = new JavaType[0];

        MapType mapTypeInstance = new MapType(mapType, bindings, superClass, superInts, keyType, valueType, valueHandler, null, false);
        assertEquals(mapType, mapTypeInstance.getRawClass());
        assertEquals(keyType, mapTypeInstance.getKeyType());
        assertEquals(valueType, mapTypeInstance.getValueType());
        assertEquals(bindings, mapTypeInstance.getBindings());
        assertEquals(superClass, mapTypeInstance.getSuperclass());
        assertEquals(superInts, mapTypeInstance.getSuperinterfaces());
        assertEquals(valueHandler, mapTypeInstance.getValueHandler());
    }

    @Test
    public void testMapTypeWithContentTypeHandler() {
        JavaType keyType = JavaType.constructType(String.class);
        JavaType valueType = JavaType.constructType(Integer[].class);
        Object typeHandler = new Object();
        Class<?> mapType = Map.class;
        TypeBindings bindings = new TypeBindings();
        JavaType superClass = Object.class;
        JavaType[] superInts = new JavaType[0];

        MapType mapTypeInstance = new MapType(mapType, bindings, superClass, superInts, keyType, valueType, null, typeHandler, false);
        assertEquals(mapType, mapTypeInstance.getRawClass());
        assertEquals(keyType, mapTypeInstance.getKeyType());
        JavaType contentType = mapTypeInstance.getValueType();
        assertEquals(Integer[].class, contentType.getRawClass());
        assertEquals(typeHandler, mapTypeInstance.getTypeHandler());
    }

    @Test
    public void testMapTypeWithKeyTypeHandler() {
        JavaType keyType1 = JavaType.constructType(String.class);
        JavaType valueType = JavaType.constructType(Integer.class);
        Object keyTypeHandler = new Object();
        Class<?> mapType = Map.class;
        TypeBindings bindings = new TypeBindings();
        JavaType superClass = Object.class;
        JavaType[] superInts = new JavaType[0];

        JavaType mapTypeInstance = new MapType(mapType, bindings, superClass, superInts, keyType1, valueType, null, null, false)
                .withKeyTypeHandler(keyTypeHandler);

        assertEquals(mapType, mapTypeInstance.getRawClass());
        assertEquals(keyType1, mapTypeInstance.getKeyType());
        assertEquals(valueType, mapTypeInstance.getValueType());
        assertEquals(bindings, mapTypeInstance.getBindings());
        assertEquals(superClass, mapTypeInstance.getSuperclass());
        assertEquals(superInts, mapTypeInstance.getSuperinterfaces());
        assertEquals(keyTypeHandler, mapTypeInstance.getKeyTypeHandler());
    }

    @Test
    public void testMapTypeWithKeyValueHandler() {
        JavaType keyType = JavaType.constructType(String.class);
        JavaType valueType = JavaType.constructType(Integer.class);
        Object keyValueHandler = new Object();
        Class<?> mapType = Map.class;
        TypeBindings bindings = new TypeBindings();
        JavaType superClass = Object.class;
        JavaType[] superInts = new JavaType[0];

        MapType mapTypeInstance = new MapType(mapType, bindings, superClass, superInts, keyType, valueType, null, null, false)
                .withKeyValueHandler(keyValueHandler);

        assertEquals(mapType, mapTypeInstance.getRawClass());
        assertEquals(keyType, mapTypeInstance.getKeyType());
        assertEquals(valueType, mapTypeInstance.getValueType());
        assertEquals(bindings, mapTypeInstance.getBindings());
        assertEquals(superClass, mapTypeInstance.getSuperclass());
        assertEquals(superInts, mapTypeInstance.getSuperinterfaces());
        assertEquals(keyValueHandler, mapTypeInstance.getKeyValueHandler());
    }

    @Test
    public void testMapTypeWithStaticTyping() {
        JavaType keyType = JavaType.constructType(String.class);
        JavaType valueType = JavaType.constructType(Integer.class);
        Class<?> mapType = Map.class;
        TypeBindings bindings = new TypeBindings();
        JavaType superClass = Object.class;
        JavaType[] superInts = new JavaType[0];

        MapType mapTypeInstance = new MapType(mapType, bindings, superClass, superInts, keyType, valueType, null, null, false);
        MapType staticMapTypeInstance = mapTypeInstance.withStaticTyping();
        assertEquals(mapType, staticMapTypeInstance.getRawClass());
        assertEquals(keyType, staticMapTypeInstance.getKeyType());
        assertEquals(valueType, staticMapTypeInstance.getValueType());
        assertEquals(bindings, staticMapTypeInstance.getBindings());
        assertEquals(superClass, staticMapTypeInstance.getSuperclass());
        assertEquals(superInts, staticMapTypeInstance.getSuperinterfaces());
        assertTrue(staticMapTypeInstance.isStatic());
    }

    @Test
    public void testMapTypeWithContentType() {
        JavaType keyType = JavaType.constructType(String.class);
        JavaType valueType = JavaType.constructType(Integer.class);
        JavaType contentType = JavaType.constructType(String.class);
        Class<?> mapType = Map.class;
        TypeBindings bindings = new TypeBindings();
        JavaType superClass = Object.class;
        JavaType[] superInts = new JavaType[0];

        MapType mapTypeInstance = new MapType(mapType, bindings, superClass, superInts, keyType, valueType, null, null, false);
        MapType contentMapTypeInstance = mapTypeInstance.withContentType(contentType);
        assertEquals(mapType, contentMapTypeInstance.getRawClass());
        assertEquals(keyType, contentMapTypeInstance.getKeyType());
        assertEquals(contentType, contentMapTypeInstance.getValueType());
        assertEquals(bindings, contentMapTypeInstance.getBindings());
        assertEquals(superClass, contentMapTypeInstance.getSuperclass());
        assertEquals(superInts, contentMapTypeInstance.getSuperinterfaces());
    }

    @Test
    public void testMapTypeWithKeyType() {
        JavaType keyType1 = JavaType.constructType(String.class);
        JavaType valueType = JavaType.constructType(Integer.class);
        Class<?> mapType = Map.class;
        TypeBindings bindings = new TypeBindings();
        JavaType superClass = Object.class;
        JavaType[] superInts = new JavaType[0];

        MapType mapTypeInstance = new MapType(mapType, bindings, superClass, superInts, keyType1, valueType, null, null, false);
        MapType keyMapTypeInstance = mapTypeInstance.withKeyType(JavaType.constructType(Integer.class));
        assertEquals(mapType, keyMapTypeInstance.getRawClass());
        assertEquals(Integer.class, keyMapTypeInstance.getKeyType().getRawClass());
        assertEquals(valueType, keyMapTypeInstance.getValueType());
        assertEquals(bindings, keyMapTypeInstance.getBindings());
        assertEquals(superClass, keyMapTypeInstance.getSuperclass());
        assertEquals(superInts, keyMapTypeInstance.getSuperinterfaces());
    }

    @Test
    public void testMapTypeRefine() {
        JavaType keyType = JavaType.constructType(String.class);
        JavaType valueType = JavaType.constructType(Integer.class);
        Class<?> mapType = Map.class;
        TypeBindings bindings = new TypeBindings();
        JavaType superClass = Object.class;
        JavaType[] superInts = new JavaType[0];

        MapType mapTypeInstance = new MapType(mapType, bindings, superClass, superInts, keyType, valueType, null, null, false);
        JavaType refinedMapTypeInstance = mapTypeInstance.refine(Map.class, bindings, superClass, superInts);
        assertEquals(mapType, refinedMapTypeInstance.getRawClass());
        assertEquals(keyType, refinedMapTypeInstance.getKeyType());
        assertEquals(valueType, refinedMapTypeInstance.getValueType());
        assertEquals(bindings, refinedMapTypeInstance.getBindings());
        assertEquals(superClass, refinedMapTypeInstance.getSuperclass());
        assertEquals(superInts, refinedMapTypeInstance.getSuperinterfaces());
    }

    @Test
    public void testMapTypeNullValues() {
        JavaType keyType = JavaType.constructType(String.class);
        JavaType valueType = JavaType.constructType(Object.class);
        Class<?> mapType = Map.class;
        TypeBindings bindings = new TypeBindings();
        JavaType superClass = Object.class;
        JavaType[] superInts = new JavaType[0];

        MapType mapTypeInstance = new MapType(mapType, bindings, superClass, superInts, keyType, valueType, null, null, false);
        Map<String, Object> map = new HashMap<>();
        map.put("key", null);

        ObjectMapper objectMapper = new ObjectMapper();
        MapType deserializedMapTypeInstance = objectMapper.readValue(map.entrySet().toString().getBytes(), MapType.class);
        assertEquals(mapType, deserializedMapTypeInstance.getRawClass());
        assertEquals(keyType, deserializedMapTypeInstance.getKeyType());
        assertEquals(valueType, deserializedMapTypeInstance.getValueType());
        assertEquals(bindings, deserializedMapTypeInstance.getBindings());
        assertEquals(superClass, deserializedMapTypeInstance.getSuperclass());
        assertEquals(superInts, deserializedMapTypeInstance.getSuperinterfaces());
    }

    @Test
    public void testMapTypeEmptyMap() {
        JavaType keyType = JavaType.constructType(String.class);
        JavaType valueType = JavaType.constructType(Object.class);
        Class<?> mapType = Map.class;
        TypeBindings bindings = new TypeBindings();
        JavaType superClass = Object.class;
        JavaType[] superInts = new JavaType[0];

        MapType mapTypeInstance = new MapType(mapType, bindings, superClass, superInts, keyType, valueType, null, null, false);
        Map<String, Object> emptyMap = new HashMap<>();

        ObjectMapper objectMapper = new ObjectMapper();
        MapType deserializedMapTypeInstance = objectMapper.readValue(emptyMap.toString().getBytes(), MapType.class);
        assertEquals(mapType, deserializedMapTypeInstance.getRawClass());
        assertEquals(keyType, deserializedMapTypeInstance.getKeyType());
        assertEquals(valueType, deserializedMapTypeInstance.getValueType());
        assertEquals(bindings, deserializedMapTypeInstance.getBindings());
        assertEquals(superClass, deserializedMapTypeInstance.getSuperclass());
        assertEquals(superInts, deserializedMapTypeInstance.getSuperinterfaces());
        assertTrue(deserializedMapTypeInstance.isEmpty());
    }

    @Test
    public void testMapTypeNull() {
        assertNull(MapType.construct(null, null, null, null, null, null));
    }
}