package com.fasterxml.jackson.databind.type;

import static org.junit.Assert.*;

import org.junit.Test;

public class MapTypeTest {

    @Test
    public void testConstruct() {
        Class<?> rawType = Map.class;
        TypeBindings bindings = TypeBindings.empty();
        JavaType keyType = String.class;
        JavaType valueType = Integer.class;
        MapType mapType = MapType.construct(rawType, bindings, null, null, keyType, valueType);
        assertEquals(rawType, mapType.getRawClass());
        assertEquals(bindings, mapType.getTypeBindings());
        assertEquals(keyType, mapType.getKeyType());
        assertEquals(valueType, mapType.getValueType());
        assertEquals(false, mapType.isAsStatic());
    }

    @Test
    public void testConstructDeprecated() {
        Class<?> rawType = Map.class;
        JavaType keyType = String.class;
        JavaType valueType = Integer.class;
        MapType mapType = MapType.construct(rawType, keyType, valueType);
        assertEquals(rawType, mapType.getRawClass());
        assertNull(mapType.getTypeBindings());
        assertEquals(keyType, mapType.getKeyType());
        assertEquals(valueType, mapType.getValueType());
        assertEquals(false, mapType.isAsStatic());
    }

    @Test
    public void testNarrowDeprecated() {
        Class<?> subclass = HashMap.class;
        MapType mapType = new MapType(HashMap.class, null, null, null, String.class, Integer.class);
        MapType narrowedType = mapType.withTypeHandler(null)._narrow(subclass);
        assertEquals(subclass, narrowedType.getRawClass());
        assertEquals(mapType.getTypeBindings(), narrowedType.getTypeBindings());
        assertEquals(mapType.getKeyType(), narrowedType.getKeyType());
        assertEquals(mapType.getValueType(), narrowedType.getValueType());
        assertEquals(false, narrowedType.isAsStatic());
    }

    @Test
    public void testWithTypeHandler() {
        Class<?> rawType = Map.class;
        TypeBindings bindings = TypeBindings.empty();
        JavaType keyType = String.class;
        JavaType valueType = Integer.class;
        MapType mapType = new MapType(rawType, bindings, null, null, keyType, valueType, null, null, false);
        Object valueHandler = new Object() {};
        MapType withTypeHandler = mapType.withTypeHandler(valueHandler);
        assertEquals(mapType.getRawClass(), withTypeHandler.getRawClass());
        assertEquals(mapType.getTypeBindings(), withTypeHandler.getTypeBindings());
        assertEquals(mapType.getKeyType(), withTypeHandler.getKeyType());
        assertEquals(valueType, withTypeHandler.getValueType());
        assertEquals(valueHandler, withTypeHandler.getTypeHandler());
        assertEquals(false, withTypeHandler.isAsStatic());
    }

    @Test
    public void testWithContentTypeHandler() {
        Class<?> rawType = Map.class;
        TypeBindings bindings = TypeBindings.empty();
        JavaType keyType = String.class;
        JavaType valueType = Integer.class;
        MapType mapType = new MapType(rawType, bindings, null, null, keyType, valueType, null, null, false);
        Object contentTypeHandler = new Object() {};
        MapType withContentTypeHandler = mapType.withContentTypeHandler(contentTypeHandler);
        assertEquals(mapType.getRawClass(), withContentTypeHandler.getRawClass());
        assertEquals(mapType.getTypeBindings(), withContentTypeHandler.getTypeBindings());
        assertEquals(keyType, withContentTypeHandler.getKeyType());
        JavaType contentType = Integer.class.withTypeHandler(contentTypeHandler);
        assertEquals(contentType, withContentTypeHandler.getValueType());
        assertEquals(mapType.getTypeHandler(), withContentTypeHandler.getTypeHandler());
        assertEquals(false, withContentTypeHandler.isAsStatic());
    }

    @Test
    public void testWithValueHandler() {
        Class<?> rawType = Map.class;
        TypeBindings bindings = TypeBindings.empty();
        JavaType keyType = String.class;
        JavaType valueType = Integer.class;
        MapType mapType = new MapType(rawType, bindings, null, null, keyType, valueType, null, null, false);
        Object valueHandler = new Object() {};
        MapType withValueHandler = mapType.withValueHandler(valueHandler);
        assertEquals(mapType.getRawClass(), withValueHandler.getRawClass());
        assertEquals(mapType.getTypeBindings(), withValueHandler.getTypeBindings());
        assertEquals(keyType, withValueHandler.getKeyType());
        assertEquals(valueType, withValueHandler.getValueType());
        assertEquals(valueHandler, withValueHandler.getValueHandler());
        assertEquals(false, withValueHandler.isAsStatic());
    }

    @Test
    public void testWithContentValueHandler() {
        Class<?> rawType = Map.class;
        TypeBindings bindings = TypeBindings.empty();
        JavaType keyType = String.class;
        JavaType valueType = Integer.class;
        MapType mapType = new MapType(rawType, bindings, null, null, keyType, valueType, null, null, false);
        Object contentValueHandler = new Object() {};
        MapType withContentValueHandler = mapType.withContentValueHandler(contentValueHandler);
        assertEquals(mapType.getRawClass(), withContentValueHandler.getRawClass());
        assertEquals(mapType.getTypeBindings(), withContentValueHandler.getTypeBindings());
        assertEquals(keyType, withContentValueHandler.getKeyType());
        JavaType contentValueType = Integer.class.withValueHandler(contentValueHandler);
        assertEquals(contentValueType, withContentValueHandler.getValueType());
        assertEquals(mapType.getTypeHandler(), withContentValueHandler.getTypeHandler());
        assertEquals(mapType.getValueHandler(), withContentValueHandler.getValueHandler());
        assertEquals(false, withContentValueHandler.isAsStatic());
    }

    @Test
    public void testWithStaticTyping() {
        Class<?> rawType = Map.class;
        TypeBindings bindings = TypeBindings.empty();
        JavaType keyType = String.class;
        JavaType valueType = Integer.class;
        MapType mapType = new MapType(rawType, bindings, null, null, keyType, valueType, null, null, false);
        MapType withStaticTyping = mapType.withStaticTyping();
        assertEquals(mapType.getRawClass(), withStaticTyping.getRawClass());
        assertEquals(mapType.getTypeBindings(), withStaticTyping.getTypeBindings());
        assertEquals(keyType.withStaticTyping(), withStaticTyping.getKeyType());
        assertEquals(valueType.withStaticTyping(), withStaticTyping.getValueType());
        assertEquals(mapType.getValueHandler(), withStaticTyping.getValueHandler());
        assertEquals(mapType.getTypeHandler(), withStaticTyping.getTypeHandler());
        assertTrue(withStaticTyping.isAsStatic());
    }

    @Test
    public void testWithContentType() {
        Class<?> rawType = Map.class;
        TypeBindings bindings = TypeBindings.empty();
        JavaType keyType = String.class;
        JavaType valueType = Integer.class;
        MapType mapType = new MapType(rawType, bindings, null, null, keyType, valueType, null, null, false);
        JavaType contentType = Integer.class;
        MapType withContentType = mapType.withContentType(contentType);
        assertEquals(mapType.getRawClass(), withContentType.getRawClass());
        assertEquals(mapType.getTypeBindings(), withContentType.getTypeBindings());
        assertEquals(keyType, withContentType.getKeyType());
        assertEquals(contentType, withContentType.getValueType());
        assertEquals(mapType.getValueHandler(), withContentType.getValueHandler());
        assertEquals(mapType.getTypeHandler(), withContentType.getTypeHandler());
        assertEquals(false, withContentType.isAsStatic());
    }

    @Test
    public void testWithKeyType() {
        Class<?> rawType = Map.class;
        TypeBindings bindings = TypeBindings.empty();
        JavaType keyType = String.class;
        JavaType valueType = Integer.class;
        MapType mapType = new MapType(rawType, bindings, null, null, keyType, valueType, null, null, false);
        JavaType newKeyType = Long.class;
        MapType withKeyType = mapType.withKeyType(newKeyType);
        assertEquals(mapType.getRawClass(), withKeyType.getRawClass());
        assertEquals(mapType.getTypeBindings(), withKeyType.getTypeBindings());
        assertEquals(newKeyType, withKeyType.getKeyType());
        assertEquals(valueType, withKeyType.getValueType());
        assertEquals(mapType.getValueHandler(), withKeyType.getValueHandler());
        assertEquals(mapType.getTypeHandler(), withKeyType.getTypeHandler());
        assertEquals(false, withKeyType.isAsStatic());
    }

    @Test
    public void testRefine() {
        Class<?> rawType = Map.class;
        TypeBindings bindings = TypeBindings.empty();
        JavaType keyType = String.class;
        JavaType valueType = Integer.class;
        MapType mapType = new MapType(rawType, bindings, null, null, keyType, valueType, null, null, false);
        Class<?> subclass = HashMap.class;
        TypeBindings newBindings = TypeBindings.empty().withBinding(subclass, Map.class);
        JavaType newKeyType = Long.class;
        JavaType newValueType = Double.class;
        MapType refinedType = mapType.refine(subclass, newBindings, null, null).withKeyType(newKeyType).withValueType(newValueType);
        assertEquals(subclass, refinedType.getRawClass());
        assertEquals(newBindings, refinedType.getTypeBindings());
        assertEquals(newKeyType, refinedType.getKeyType());
        assertEquals(newValueType, refinedType.getValueType());
        assertEquals(mapType.getValueHandler(), refinedType.getValueHandler());
        assertEquals(mapType.getTypeHandler(), refinedType.getTypeHandler());
        assertEquals(false, refinedType.isAsStatic());
    }

    @Test
    public void testWithKeyTypeHandler() {
        Class<?> rawType = Map.class;
        TypeBindings bindings = TypeBindings.empty();
        JavaType keyType = String.class;
        JavaType valueType = Integer.class;
        MapType mapType = new MapType(rawType, bindings, null, null, keyType, valueType, null, null, false);
        Object keyTypeHandler = new Object() {};
        MapType withKeyTypeHandler = mapType.withKeyTypeHandler(keyTypeHandler);
        assertEquals(mapType.getRawClass(), withKeyTypeHandler.getRawClass());
        assertEquals(mapType.getTypeBindings(), withKeyTypeHandler.getTypeBindings());
        assertEquals(keyType, withKeyTypeHandler.getKeyType());
        assertEquals(valueType, withKeyTypeHandler.getValueType());
        assertEquals(mapType.getTypeHandler(), withKeyTypeHandler.getTypeHandler());
        assertEquals(mapType.getValueHandler(), withKeyTypeHandler.getValueHandler());
        assertEquals(false, withKeyTypeHandler.isAsStatic());
    }

    @Test
    public void testWithKeyValueHandler() {
        Class<?> rawType = Map.class;
        TypeBindings bindings = TypeBindings.empty();
        JavaType keyType = String.class;
        JavaType valueType = Integer.class;
        MapType mapType = new MapType(rawType, bindings, null, null, keyType, valueType, null, null, false);
        Object keyValueHandler = new Object() {};
        MapType withKeyValueHandler = mapType.withKeyValueHandler(keyValueHandler);
        assertEquals(mapType.getRawClass(), withKeyValueHandler.getRawClass());
        assertEquals(mapType.getTypeBindings(), withKeyValueHandler.getTypeBindings());
        assertEquals(keyType, withKeyValueHandler.getKeyType());
        assertEquals(valueType, withKeyValueHandler.getValueType());
        assertEquals(mapType.getTypeHandler(), withKeyValueHandler.getTypeHandler());
        assertEquals(mapType.getValueHandler(), withKeyValueHandler.getValueHandler());
        assertEquals(false, withKeyValueHandler.isAsStatic());
    }
}