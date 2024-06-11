package com.fasterxml.jackson.databind.type;

import static org.junit.Assert.*;

import org.junit.Test;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.MapType;

public class MapTypeTest {

    @Test
    public void testConstruct() {
        Class<?> rawType = Map.class;
        TypeBindings bindings = TypeBindings.empty();
        JavaType keyType = JavaType.constructCollectionType(List.class, String.class);
        JavaType valueType = JavaType.constructCollectionType(List.class, Integer.class);
        MapType mapType = MapType.construct(rawType, bindings, null, null, keyType, valueType);
        assertEquals(MapType.class, mapType.getClass());
        assertEquals(rawType, mapType.getRawClass());
        assertEquals(keyType, mapType.getKeyType());
        assertEquals(valueType, mapType.getValueType());
    }

    @Test
    public void testConstructDeprecated() {
        Class<?> rawType = Map.class;
        JavaType keyType = JavaType.constructCollectionType(List.class, String.class);
        JavaType valueType = JavaType.constructCollectionType(List.class, Integer.class);
        MapType mapType = MapType.construct(rawType, keyType, valueType);
        assertEquals(MapType.class, mapType.getClass());
        assertEquals(rawType, mapType.getRawClass());
        assertEquals(keyType, mapType.getKeyType());
        assertEquals(valueType, mapType.getValueType());
    }

    @Test
    public void testNarrow() {
        Class<?> subclass = HashMap.class;
        Class<?> rawType = Map.class;
        JavaType keyType = JavaType.constructCollectionType(List.class, String.class);
        JavaType valueType = JavaType.constructCollectionType(List.class, Integer.class);
        MapType mapType = MapType.construct(rawType, null, null, null, keyType, valueType);
        MapType narrowedType = mapType.narrow(subclass);
        assertEquals(MapType.class, narrowedType.getClass());
        assertEquals(subclass, narrowedType.getRawClass());
        assertEquals(keyType, narrowedType.getKeyType());
        assertEquals(valueType, narrowedType.getValueType());
    }

    @Test
    public void testWithTypeHandler() {
        Class<?> rawType = Map.class;
        JavaType keyType = JavaType.constructCollectionType(List.class, String.class);
        JavaType valueType = JavaType.constructCollectionType(List.class, Integer.class);
        Object handler = new Object();
        MapType mapType = MapType.construct(rawType, null, null, null, keyType, valueType);
        MapType withHandlerType = mapType.withTypeHandler(handler);
        assertEquals(MapType.class, withHandlerType.getClass());
        assertEquals(rawType, withHandlerType.getRawClass());
        assertEquals(keyType, withHandlerType.getKeyType());
        assertEquals(valueType, withHandlerType.getValueType());
        assertNotNull(withHandlerType.getTypeHandler());
        assertEquals(handler, withHandlerType.getTypeHandler());
    }

    @Test
    public void testWithContentTypeHandler() {
        Class<?> rawType = Map.class;
        JavaType keyType = JavaType.constructCollectionType(List.class, String.class);
        JavaType valueType = JavaType.constructCollectionType(List.class, Integer.class);
        JavaType contentType = JavaType.constructCollectionType(ArrayList.class, String.class);
        Object handler = new Object();
        MapType mapType = MapType.construct(rawType, null, null, null, keyType, valueType);
        MapType withContentTypeHandlerType = mapType.withContentTypeHandler(handler);
        assertEquals(MapType.class, withContentTypeHandlerType.getClass());
        assertEquals(rawType, withContentTypeHandlerType.getRawClass());
        assertEquals(keyType, withContentTypeHandlerType.getKeyType());
        assertEquals(contentType, withContentTypeHandlerType.getValueType());
        assertNotNull(withContentTypeHandlerType.getValueHandler());
        assertEquals(handler, withContentTypeHandlerType.getValueHandler());
    }

    @Test
    public void testWithValueHandler() {
        Class<?> rawType = Map.class;
        JavaType keyType = JavaType.constructCollectionType(List.class, String.class);
        JavaType valueType = JavaType.constructCollectionType(List.class, Integer.class);
        Object handler = new Object();
        MapType mapType = MapType.construct(rawType, null, null, null, keyType, valueType);
        MapType withHandlerType = mapType.withValueHandler(handler);
        assertEquals(MapType.class, withHandlerType.getClass());
        assertEquals(rawType, withHandlerType.getRawClass());
        assertEquals(keyType, withHandlerType.getKeyType());
        assertEquals(valueType, withHandlerType.getValueType());
        assertNotNull(withHandlerType.getValueHandler());
        assertEquals(handler, withHandlerType.getValueHandler());
    }

    @Test
    public void testWithContentValueHandler() {
        Class<?> rawType = Map.class;
        JavaType keyType = JavaType.constructCollectionType(List.class, String.class);
        JavaType valueType = JavaType.constructCollectionType(List.class, Integer.class);
        JavaType contentValueType = JavaType.constructCollectionType(ArrayList.class, Integer.class);
        Object handler = new Object();
        MapType mapType = MapType.construct(rawType, null, null, null, keyType, valueType);
        MapType withContentValueHandlerType = mapType.withContentValueHandler(handler);
        assertEquals(MapType.class, withContentValueHandlerType.getClass());
        assertEquals(rawType, withContentValueHandlerType.getRawClass());
        assertEquals(keyType, withContentValueHandlerType.getKeyType());
        assertEquals(contentValueType, withContentValueHandlerType.getValueType());
        assertNotNull(withContentValueHandlerType.getValueHandler());
        assertEquals(handler, withContentValueHandlerType.getValueHandler());
    }

    @Test
    public void testWithStaticTyping() {
        Class<?> rawType = Map.class;
        JavaType keyType = JavaType.constructCollectionType(List.class, String.class);
        JavaType valueType = JavaType.constructCollectionType(List.class, Integer.class);
        MapType mapType = MapType.construct(rawType, null, null, null, keyType, valueType);
        MapType withStaticTypingType = mapType.withStaticTyping();
        assertEquals(MapType.class, withStaticTypingType.getClass());
        assertEquals(rawType, withStaticTypingType.getRawClass());
        assertEquals(keyType.withStaticTyping(), withStaticTypingType.getKeyType());
        assertEquals(valueType.withStaticTyping(), withStaticTypingType.getValueType());
        assertFalse(withStaticTypingType._asStatic);
    }

    @Test
    public void testWithContentType() {
        Class<?> rawType = Map.class;
        JavaType keyType = JavaType.constructCollectionType(List.class, String.class);
        JavaType valueType = JavaType.constructCollectionType(List.class, Integer.class);
        JavaType contentType = JavaType.constructCollectionType(ArrayList.class, String.class);
        MapType mapType = MapType.construct(rawType, null, null, null, keyType, valueType);
        MapType withContentTypeType = mapType.withContentType(contentType);
        assertEquals(MapType.class, withContentTypeType.getClass());
        assertEquals(rawType, withContentTypeType.getRawClass());
        assertEquals(keyType, withContentTypeType.getKeyType());
        assertEquals(contentType, withContentTypeType.getValueType());
    }

    @Test
    public void testWithKeyType() {
        Class<?> rawType = Map.class;
        JavaType oldKeyType = JavaType.constructCollectionType(List.class, String.class);
        JavaType newKeyType = JavaType.constructCollectionType(ArrayList.class, String.class);
        JavaType valueType = JavaType.constructCollectionType(List.class, Integer.class);
        MapType mapType = MapType.construct(rawType, null, null, null, oldKeyType, valueType);
        MapType withNewKeyTypeType = mapType.withKeyType(newKeyType);
        assertEquals(MapType.class, withNewKeyTypeType.getClass());
        assertEquals(rawType, withNewKeyTypeType.getRawClass());
        assertEquals(newKeyType, withNewKeyTypeType.getKeyType());
        assertEquals(valueType, withNewKeyTypeType.getValueType());
    }

    @Test
    public void testRefine() {
        Class<?> rawType = HashMap.class;
        TypeBindings bindings = TypeBindings.empty();
        JavaType keyType = JavaType.constructCollectionType(List.class, String.class);
        JavaType valueType = JavaType.constructCollectionType(List.class, Integer.class);
        MapType mapType = MapType.construct(Map.class, bindings, null, null, keyType, valueType);
        JavaType refinedType = mapType.refine(rawType, bindings, null, null);
        assertEquals(MapType.class, refinedType.getClass());
        assertEquals(rawType, refinedType.getRawClass());
        assertEquals(keyType, refinedType.getKeyType());
        assertEquals(valueType, refinedType.getValueType());
    }

    @Test
    public void testWithKeyTypeHandler() {
        Class<?> rawType = Map.class;
        JavaType keyType = JavaType.constructCollectionType(List.class, String.class);
        JavaType valueType = JavaType.constructCollectionType(List.class, Integer.class);
        Object handler = new Object();
        MapType mapType = MapType.construct(rawType, null, null, null, keyType, valueType);
        MapType withKeyTypeHandlerType = mapType.withKeyTypeHandler(handler);
        assertEquals(MapType.class, withKeyTypeHandlerType.getClass());
        assertEquals(rawType, withKeyTypeHandlerType.getRawClass());
        assertEquals(keyType, withKeyTypeHandlerType.getKeyType());
        assertEquals(valueType, withKeyTypeHandlerType.getValueType());
        assertNotNull(withKeyTypeHandlerType.getKeyTypeHandler());
        assertEquals(handler, withKeyTypeHandlerType.getKeyTypeHandler());
    }

    @Test
    public void testWithKeyValueHandler() {
        Class<?> rawType = Map.class;
        JavaType keyType = JavaType.constructCollectionType(List.class, String.class);
        JavaType valueType = JavaType.constructCollectionType(List.class, Integer.class);
        Object handler = new Object();
        MapType mapType = MapType.construct(rawType, null, null, null, keyType, valueType);
        MapType withKeyValueHandlerType = mapType.withKeyValueHandler(handler);
        assertEquals(MapType.class, withKeyValueHandlerType.getClass());
        assertEquals(rawType, withKeyValueHandlerType.getRawClass());
        assertEquals(keyType, withKeyValueHandlerType.getKeyType());
        assertEquals(valueType, withKeyValueHandlerType.getValueType());
        assertNotNull(withKeyValueHandlerType.getKeyValueHandler());
        assertEquals(handler, withKeyValueHandlerType.getKeyValueHandler());
    }
}