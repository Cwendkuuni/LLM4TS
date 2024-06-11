package com.fasterxml.jackson.databind.type;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class MapTypeTest {

    @Test
    public void testConstruct() {
        JavaType keyType = new SimpleType("java.lang.String");
        JavaType valueType = new SimpleType("java.lang.Integer");
        MapType mapType = MapType.construct(Map.class, null, null, null, keyType, valueType);
        assertNotNull(mapType);
        assertSame(Map.class, mapType.getRawClass());
        assertEquals(keyType, mapType.getKeyType());
        assertEquals(valueType, mapType.getValueType());
    }

    @Test
    public void testConstructWithBindings() {
        JavaType keyType = new SimpleType("java.lang.String");
        JavaType valueType = new SimpleType("java.lang.Integer");
        TypeBindings bindings = new TypeBindings();
        MapType mapType = MapType.construct(Map.class, bindings, null, null, keyType, valueType);
        assertNotNull(mapType);
        assertSame(Map.class, mapType.getRawClass());
        assertEquals(keyType, mapType.getKeyType());
        assertEquals(valueType, mapType.getValueType());
        assertSame(bindings, mapType._bindings);
    }

    @Test
    public void testNarrow() {
        JavaType mapType = new MapType(Map.class, null, null, null, new SimpleType("java.lang.String"), new SimpleType("java.lang.Integer"));
        JavaType narrowedType = mapType._narrow(HashMap.class);
        assertNotNull(narrowedType);
        assertSame(HashMap.class, narrowedType.getRawClass());
        assertEquals(mapType.getKeyType(), narrowedType.getKeyType());
        assertEquals(mapType.getValueType(), narrowedType.getValueType());
    }

    @Test
    public void testWithTypeHandler() {
        JavaType mapType = new MapType(Map.class, null, null, null, new SimpleType("java.lang.String"), new SimpleType("java.lang.Integer"));
        Object handler = new Object();
        MapType withHandler = mapType.withTypeHandler(handler);
        assertNotNull(withHandler);
        assertEquals(mapType.getRawClass(), withHandler.getRawClass());
        assertEquals(mapType.getKeyType(), withHandler.getKeyType());
        assertEquals(mapType.getValueType(), withHandler.getValueType());
        assertSame(handler, withHandler._typeHandler);
    }

    @Test
    public void testWithContentTypeHandler() {
        JavaType keyType = new SimpleType("java.lang.String");
        JavaType valueType = new SimpleType("java.lang.Integer");
        JavaType contentTypeHandler = new SimpleType("com.custom.ContentTypeHandler");
        MapType mapType = new MapType(Map.class, null, null, null, keyType, valueType);
        MapType withContentTypeHandler = mapType.withContentTypeHandler(contentTypeHandler);
        assertNotNull(withContentTypeHandler);
        assertEquals(mapType.getRawClass(), withContentTypeHandler.getRawClass());
        assertEquals(mapType.getKeyType(), withContentTypeHandler.getKeyType());
        assertEquals(mapType.getValueType(), withContentTypeHandler.getValueType());
        assertSame(contentTypeHandler, withContentTypeHandler._typeHandler);
    }

    @Test
    public void testWithValueHandler() {
        JavaType keyType = new SimpleType("java.lang.String");
        JavaType valueType = new SimpleType("java.lang.Integer");
        Object valueHandler = new Object();
        MapType mapType = new MapType(Map.class, null, null, null, keyType, valueType);
        MapType withValueHandler = mapType.withValueHandler(valueHandler);
        assertNotNull(withValueHandler);
        assertEquals(mapType.getRawClass(), withValueHandler.getRawClass());
        assertEquals(mapType.getKeyType(), withValueHandler.getKeyType());
        assertEquals(mapType.getValueType(), withValueHandler.getValueType());
        assertSame(valueHandler, withValueHandler._valueHandler);
    }

    @Test
    public void testWithContentValueHandler() {
        JavaType keyType = new SimpleType("java.lang.String");
        JavaType valueType = new SimpleType("java.lang.Integer");
        Object contentValueHandler = new Object();
        MapType mapType = new MapType(Map.class, null, null, null, keyType, valueType);
        MapType withContentValueHandler = mapType.withContentValueHandler(contentValueHandler);
        assertNotNull(withContentValueHandler);
        assertEquals(mapType.getRawClass(), withContentValueHandler.getRawClass());
        assertEquals(mapType.getKeyType(), withContentValueHandler.getKeyType());
        assertEquals(mapType.getValueType(), withContentValueHandler.getValueType());
        assertSame(contentValueHandler, withContentValueHandler._contentValueHandler);
    }

    @Test
    public void testWithStaticTyping() {
        JavaType mapType = new MapType(Map.class, null, null, null, new SimpleType("java.lang.String"), new SimpleType("java.lang.Integer"));
        MapType withStaticTyping = mapType.withStaticTyping();
        assertNotNull(withStaticTyping);
        assertEquals(mapType.getRawClass(), withStaticTyping.getRawClass());
        assertEquals(mapType.getKeyType(), withStaticTyping.getKeyType());
        assertEquals(mapType.getValueType(), withStaticTyping.getValueType());
        assertTrue(withStaticTyping._asStatic);
    }

    @Test
    public void testWithContentType() {
        JavaType keyType = new SimpleType("java.lang.String");
        JavaType valueType = new SimpleType("java.lang.Integer");
        JavaType contentType = new SimpleType("java.lang.Double");
        MapType mapType = new MapType(Map.class, null, null, null, keyType, valueType);
        MapType withContentType = mapType.withContentType(contentType);
        assertNotNull(withContentType);
        assertEquals(mapType.getRawClass(), withContentType.getRawClass());
        assertEquals(mapType.getKeyType(), withContentType.getKeyType());
        assertEquals(contentType, withContentType.getValueType());
    }

    @Test
    public void testWithKeyType() {
        JavaType mapType = new MapType(Map.class, null, null, null, new SimpleType("java.lang.String"), new SimpleType("java.lang.Integer"));
        JavaType newKeyType = new SimpleType("java.lang.Long");
        MapType withKeyType = mapType.withKeyType(newKeyType);
        assertNotNull(withKeyType);
        assertEquals(mapType.getRawClass(), withKeyType.getRawClass());
        assertEquals(newKeyType, withKeyType.getKeyType());
        assertEquals(mapType.getValueType(), withKeyType.getValueType());
    }

    @Test
    public void testRefine() {
        JavaType rawType = Map.class;
        JavaType keyType = new SimpleType("java.lang.String");
        JavaType valueType = new SimpleType("java.lang.Integer");
        TypeBindings bindings = new TypeBindings();
        MapType mapType = new MapType(rawType, bindings, null, null, keyType, valueType);
        JavaType refinedType = mapType.refine(HashMap.class, null, null, null);
        assertNotNull(refinedType);
        assertEquals(hashMapClass, refinedType.getRawClass());
        assertEquals(keyType, refinedType.getKeyType());
        assertEquals(valueType, refinedType.getValueType());
    }

    @Test
    public void testWithKeyTypeHandler() {
        JavaType mapType = new MapType(Map.class, null, null, null, new SimpleType("java.lang.String"), new SimpleType("java.lang.Integer"));
        Object keyTypeHandler = new Object();
        MapType withKeyTypeHandler = mapType.withKeyTypeHandler(keyTypeHandler);
        assertNotNull(withKeyTypeHandler);
        assertEquals(mapType.getRawClass(), withKeyTypeHandler.getRawClass());
        assertEquals(mapType.getKeyType(), withKeyTypeHandler.getKeyType());
        assertEquals(mapType.getValueType(), withKeyTypeHandler.getValueType());
        assertSame(keyTypeHandler, withKeyTypeHandler._keyTypeHandler);
    }

    @Test
    public void testWithKeyValueHandler() {
        JavaType mapType = new MapType(Map.class, null, null, null, new SimpleType("java.lang.String"), new SimpleType("java.lang.Integer"));
        Object keyValueHandler = new Object();
        MapType withKeyValueHandler = mapType.withKeyValueHandler(keyValueHandler);
        assertNotNull(withKeyValueHandler);
        assertEquals(mapType.getRawClass(), withKeyValueHandler.getRawClass());
        assertEquals(mapType.getKeyType(), withKeyValueHandler.getKeyType());
        assertEquals(mapType.getValueType(), withKeyValueHandler.getValueType());
        assertSame(keyValueHandler, withKeyValueHandler._keyValueHandler);
    }
}