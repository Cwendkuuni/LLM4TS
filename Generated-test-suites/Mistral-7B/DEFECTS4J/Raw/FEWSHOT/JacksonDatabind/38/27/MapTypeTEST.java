package com.fasterxml.jackson.databind.type;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class MapTypeTest {

    @Test
    public void testMapTypeConstruction() {
        JavaType keyType = new SimpleType("java.lang.String");
        JavaType valueType = new SimpleType("com.example.MyClass");
        MapType mapType = MapType.construct(Map.class, null, null, null, keyType, valueType);
        assertNotNull(mapType);
        assertEquals(Map.class, mapType.getRawClass());
        assertEquals(keyType, mapType.getKeyType());
        assertEquals(valueType, mapType.getValueType());
    }

    @Test
    public void testMapTypeWithTypeHandler() {
        JavaType keyType = new SimpleType("java.lang.String");
        JavaType valueType = new SimpleType("com.example.MyClass");
        Object handler = new Object();

        MapType mapType = new MapType(Map.class, null, null, null, keyType, valueType);
        MapType withHandler = mapType.withTypeHandler(handler);

        assertEquals(mapType.getRawClass(), withHandler.getRawClass());
        assertEquals(keyType, withHandler.getKeyType());
        assertEquals(valueType, withHandler.getValueType());
        assertNotNull(withHandler.getTypeHandler());
    }

    @Test
    public void testMapTypeWithContentTypeHandler() {
        JavaType keyType = new SimpleType("java.lang.String");
        JavaType contentType = new SimpleType("com.example.MyContentClass");
        JavaType valueType = new SimpleType("com.example.MyClass");
        Object handler = new Object();

        MapType mapType = new MapType(Map.class, null, null, null, keyType, valueType);
        MapType withContentTypeHandler = mapType.withContentTypeHandler(handler);

        assertEquals(mapType.getRawClass(), withContentTypeHandler.getRawClass());
        assertEquals(keyType, withContentTypeHandler.getKeyType());
        assertEquals(contentType, withContentTypeHandler.getValueType());
        assertNotNull(withContentTypeHandler.getTypeHandler());
    }

    @Test
    public void testMapTypeWithValueHandler() {
        JavaType keyType = new SimpleType("java.lang.String");
        JavaType valueType = new SimpleType("com.example.MyClass");
        Object handler = new Object();

        MapType mapType = new MapType(Map.class, null, null, null, keyType, valueType);
        MapType withValueHandler = mapType.withValueHandler(handler);

        assertEquals(mapType.getRawClass(), withValueHandler.getRawClass());
        assertEquals(keyType, withValueHandler.getKeyType());
        assertEquals(valueType, withValueHandler.getValueType());
        assertNotNull(withValueHandler.getValueHandler());
    }

    @Test
    public void testMapTypeWithContentValueHandler() {
        JavaType keyType = new SimpleType("java.lang.String");
        JavaType contentValueType = new SimpleType("com.example.MyContentValueClass");
        JavaType valueType = new SimpleType("com.example.MyClass");
        Object handler = new Object();

        MapType mapType = new MapType(Map.class, null, null, null, keyType, valueType);
        MapType withContentValueHandler = mapType.withContentValueHandler(handler);

        assertEquals(mapType.getRawClass(), withContentValueHandler.getRawClass());
        assertEquals(keyType, withContentValueHandler.getKeyType());
        assertEquals(contentValueType, withContentValueHandler.getValueType());
        assertNotNull(withContentValueHandler.getValueHandler());
    }

    @Test
    public void testMapTypeWithStaticTyping() {
        JavaType keyType = new SimpleType("java.lang.String");
        JavaType valueType = new SimpleType("com.example.MyClass");
        MapType mapType = new MapType(Map.class, null, null, null, keyType, valueType);
        MapType withStaticTyping = mapType.withStaticTyping();

        assertEquals(mapType.getRawClass(), withStaticTyping.getRawClass());
        assertEquals(keyType, withStaticTyping.getKeyType());
        assertEquals(valueType, withStaticTyping.getValueType());
        assertTrue(withStaticTyping.isStatic());
    }

    @Test
    public void testMapTypeWithContentType() {
        JavaType keyType = new SimpleType("java.lang.String");
        JavaType contentType = new SimpleType("com.example.MyContentClass");
        JavaType valueType = new SimpleType("com.example.MyClass");
        MapType mapType = new MapType(Map.class, null, null, null, keyType, valueType);
        MapType withContentType = mapType.withContentType(contentType);

        assertEquals(mapType.getRawClass(), withContentType.getRawClass());
        assertEquals(keyType, withContentType.getKeyType());
        assertEquals(contentType, withContentType.getValueType());
    }

    @Test
    public void testMapTypeWithKeyType() {
        JavaType rawType = Map.class;
        JavaType keyType = new SimpleType("java.lang.String");
        JavaType valueType = new SimpleType("com.example.MyClass");
        MapType mapType = new MapType(rawType, null, null, null, null, null);
        MapType withKeyType = mapType.withKeyType(keyType);

        assertEquals(rawType, withKeyType.getRawClass());
        assertEquals(keyType, withKeyType.getKeyType());
        assertEquals(valueType, withKeyType.getValueType());
    }

    @Test
    public void testMapTypeRefine() {
        JavaType rawType = Map.class;
        JavaType keyType = new SimpleType("java.lang.String");
        JavaType valueType = new SimpleType("com.example.MyClass");
        MapType mapType = new MapType(rawType, null, null, null, null, null);
        TypeBindings bindings = new TypeBindings();
        JavaType refinedType = mapType.refine(Map.class, bindings, null, null);

        assertEquals(mapType.getRawClass(), refinedType.getRawClass());
        assertEquals(keyType, refinedType.getKeyType());
        assertEquals(valueType, refinedType.getValueType());
    }

    @Test
    public void testMapTypeWithKeyTypeHandler() {
        JavaType keyType = new SimpleType("java.lang.String");
        JavaType valueType = new SimpleType("com.example.MyClass");
        Object handler = new Object();

        MapType mapType = new MapType(Map.class, null, null, null, keyType, valueType);
        MapType withKeyTypeHandler = mapType.withKeyTypeHandler(handler);

        assertEquals(mapType.getRawClass(), withKeyTypeHandler.getRawClass());
        assertEquals(keyType, withKeyTypeHandler.getKeyType());
        assertEquals(valueType, withKeyTypeHandler.getValueType());
        assertNotNull(withKeyTypeHandler.getKeyTypeHandler());
    }

    @Test
    public void testMapTypeWithKeyValueHandler() {
        JavaType keyType = new SimpleType("java.lang.String");
        JavaType valueType = new SimpleType("com.example.MyClass");
        Object handler = new Object();

        MapType mapType = new MapType(Map.class, null, null, null, keyType, valueType);
        MapType withKeyValueHandler = mapType.withKeyValueHandler(handler);

        assertEquals(mapType.getRawClass(), withKeyValueHandler.getRawClass());
        assertEquals(keyType, withKeyValueHandler.getKeyType());
        assertEquals(valueType, withKeyValueHandler.getValueType());
        assertNotNull(withKeyValueHandler.getKeyValueHandler());
    }
}