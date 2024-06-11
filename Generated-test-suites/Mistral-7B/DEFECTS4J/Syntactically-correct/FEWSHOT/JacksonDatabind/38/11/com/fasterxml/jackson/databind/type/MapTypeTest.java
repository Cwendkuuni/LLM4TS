package com.fasterxml.jackson.databind.type;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class MapTypeTest {

    @Test
    public void testConstruct() {
        JavaType keyType = new SimpleType(String.class);
        JavaType valueType = new SimpleType(Integer.class);
        MapType mapType = MapType.construct(Map.class, null, null, null, keyType, valueType);
        assertEquals(Map.class, mapType.getRawClass());
        assertEquals(keyType, mapType.getKeyType());
        assertEquals(valueType, mapType.getValueType());
        assertNull(mapType.getTypeHandler());
        assertNull(mapType.getValueHandler());
        assertNull(mapType.getTypeHandlerInstantiator());
        assertFalse(mapType.isStatic());
    }

    @Test
    public void testConstructWithTypeHandlers() {
        JavaType keyType = new SimpleType(String.class);
        JavaType valueType = new SimpleType(Integer.class);
        Object keyTypeHandler = new KeyTypeHandler();
        Object valueHandler = new ValueHandler();
        MapType mapType = MapType.construct(Map.class, null, null, null, keyType, valueType, keyTypeHandler, valueHandler, false);
        assertEquals(Map.class, mapType.getRawClass());
        assertEquals(keyType, mapType.getKeyType());
        assertEquals(valueType, mapType.getValueType());
        assertEquals(keyTypeHandler, mapType.getTypeHandler());
        assertEquals(valueHandler, mapType.getValueHandler());
        assertNull(mapType.getTypeHandlerInstantiator());
        assertFalse(mapType.isStatic());
    }

    @Test
    public void testNarrow() {
        JavaType mapType = createMapType();
        JavaType subclass = HashMap.class;
        MapType narrowedType = mapType.narrow(subclass);
        assertEquals(subclass, narrowedType.getRawClass());
        assertEquals(mapType.getKeyType(), narrowedType.getKeyType());
        assertEquals(mapType.getValueType(), narrowedType.getValueType());
        assertNull(narrowedType.getTypeHandler());
        assertNull(narrowedType.getValueHandler());
        assertNull(narrowedType.getTypeHandlerInstantiator());
        assertFalse(narrowedType.isStatic());
    }

    @Test
    public void testWithTypeHandler() {
        JavaType mapType = createMapType();
        Object typeHandler = new TypeHandler();
        MapType withTypeHandler = mapType.withTypeHandler(typeHandler);
        assertEquals(mapType.getRawClass(), withTypeHandler.getRawClass());
        assertEquals(mapType.getKeyType(), withTypeHandler.getKeyType());
        assertEquals(mapType.getValueType(), withTypeHandler.getValueType());
        assertEquals(typeHandler, withTypeHandler.getTypeHandler());
        assertNull(withTypeHandler.getValueHandler());
        assertNull(withTypeHandler.getValueHandlerInstantiator());
        assertFalse(withTypeHandler.isStatic());
    }

    @Test
    public void testWithContentTypeHandler() {
        JavaType mapType = createMapType();
        Object contentTypeHandler = new ContentTypeHandler();
        MapType withContentTypeHandler = mapType.withContentTypeHandler(contentTypeHandler);
        assertEquals(mapType.getRawClass(), withContentTypeHandler.getRawClass());
        assertEquals(mapType.getKeyType(), withContentTypeHandler.getKeyType());
        JavaType contentValueType = new SimpleType(String.class);
        assertEquals(mapType.getValueType().withTypeHandler(contentTypeHandler), withContentTypeHandler.getValueType());
        assertNull(withContentTypeHandler.getTypeHandler());
        assertNull(withContentTypeHandler.getValueHandler());
        assertNull(withContentTypeHandler.getValueHandlerInstantiator());
        assertFalse(withContentTypeHandler.isStatic());
    }

    @Test
    public void testWithValueHandler() {
        JavaType mapType = createMapType();
        Object valueHandler = new ValueHandler();
        MapType withValueHandler = mapType.withValueHandler(valueHandler);
        assertEquals(mapType.getRawClass(), withValueHandler.getRawClass());
        assertEquals(mapType.getKeyType(), withValueHandler.getKeyType());
        assertEquals(mapType.getValueType(), withValueHandler.getValueType());
        assertNull(withValueHandler.getTypeHandler());
        assertEquals(valueHandler, withValueHandler.getValueHandler());
        assertNull(withValueHandler.getValueHandlerInstantiator());
        assertFalse(withValueHandler.isStatic());
    }

    @Test
    public void testWithContentValueHandler() {
        JavaType mapType = createMapType();
        Object contentValueHandler = new ContentValueHandler();
        MapType withContentValueHandler = mapType.withContentValueHandler(contentValueHandler);
        assertEquals(mapType.getRawClass(), withContentValueHandler.getRawClass());
        assertEquals(mapType.getKeyType(), withContentValueHandler.getKeyType());
        JavaType keyType = new SimpleType(String.class);
        JavaType contentKeyType = new SimpleType(Integer.class);
        assertEquals(mapType.getValueType().withValueHandler(contentValueHandler), withContentValueHandler.getValueType());
        assertEquals(keyType, withContentValueHandler.getKeyType());
        assertNull(withContentValueHandler.getTypeHandler());
        assertNull(withContentValueHandler.getValueHandler());
        assertNull(withContentValueHandler.getValueHandlerInstantiator());
        assertFalse(withContentValueHandler.isStatic());
    }

    @Test
    public void testWithStaticTyping() {
        JavaType mapType = createMapType();
        MapType withStaticTyping = mapType.withStaticTyping();
        assertEquals(mapType.getRawClass(), withStaticTyping.getRawClass());
        assertEquals(mapType.getKeyType(), withStaticTyping.getKeyType());
        assertEquals(mapType.getValueType(), withStaticTyping.getValueType());
        assertNull(withStaticTyping.getTypeHandler());
        assertNull(withStaticTyping.getValueHandler());
        assertNull(withStaticTyping.getValueHandlerInstantiator());
        assertTrue(withStaticTyping.isStatic());
    }

    @Test
    public void testWithContentType() {
        JavaType mapType = createMapType();
        JavaType contentType = new SimpleType(String[].class);
        MapType withContentType = mapType.withContentType(contentType);
        assertEquals(mapType.getRawClass(), withContentType.getRawClass());
        assertEquals(mapType.getKeyType(), withContentType.getKeyType());
        assertEquals(contentType, withContentType.getValueType());
        assertNull(withContentType.getTypeHandler());
        assertNull(withContentType.getValueHandler());
        assertNull(withContentType.getValueHandlerInstantiator());
        assertFalse(withContentType.isStatic());
    }

    @Test
    public void testWithKeyType() {
        JavaType mapType = createMapType();
        JavaType keyType = new SimpleType(Integer.class);
        MapType withKeyType = mapType.withKeyType(keyType);
        assertEquals(mapType.getRawClass(), withKeyType.getRawClass());
        assertEquals(keyType, withKeyType.getKeyType());
        assertEquals(mapType.getValueType(), withKeyType.getValueType());
        assertNull(withKeyType.getTypeHandler());
        assertNull(withKeyType.getValueHandler());
        assertNull(withKeyType.getValueHandlerInstantiator());
        assertFalse(withKeyType.isStatic());
    }

    @Test
    public void testRefine() {
        JavaType mapType = createMapType();
        Class<?> rawType = HashMap.class;
        TypeBindings bindings = new TypeBindings();
        JavaType superClass = HashMap.class;
        JavaType[] superInterfaces = new JavaType[]{List.class};
        MapType refinedType = mapType.refine(rawType, bindings, superClass, superInterfaces);
        assertEquals(rawType, refinedType.getRawClass());
        assertEquals(mapType.getKeyType(), refinedType.getKeyType());
        assertEquals(mapType.getValueType(), refinedType.getValueType());
        assertNull(refinedType.getTypeHandler());
        assertNull(refinedType.getValueHandler());
        assertNull(refinedType.getValueHandlerInstantiator());
        assertFalse(refinedType.isStatic());
    }

    @Test
    public void testWithKeyTypeHandler() {
        JavaType mapType = createMapType();
        Object keyTypeHandler = new KeyTypeHandler();
        MapType withKeyTypeHandler = mapType.withKeyTypeHandler(keyTypeHandler);
        assertEquals(mapType.getRawClass(), withKeyTypeHandler.getRawClass());
        assertEquals(mapType.getKeyType().withTypeHandler(keyTypeHandler), withKeyTypeHandler.getKeyType());
        assertEquals(mapType.getValueType(), withKeyTypeHandler.getValueType());
        assertNull(withKeyTypeHandler.getTypeHandler());
        assertNull(withKeyTypeHandler.getValueHandler());
        assertNull(withKeyTypeHandler.getValueHandlerInstantiator());
        assertFalse(withKeyTypeHandler.isStatic());
    }

    @Test
    public void testWithKeyValueHandler() {
        JavaType mapType = createMapType();
        Object keyValueHandler = new KeyValueHandler();
        MapType withKeyValueHandler = mapType.withKeyValueHandler(keyValueHandler);
        assertEquals(mapType.getRawClass(), withKeyValueHandler.getRawClass());
        assertEquals(mapType.getKeyType(), withKeyValueHandler.getKeyType());
        assertEquals(mapType.getValueType(), withKeyValueHandler.getValueType());
        assertNull(withKeyValueHandler.getTypeHandler());
        assertEquals(keyValueHandler, withKeyValueHandler.getValueHandler());
        assertNull(withKeyValueHandler.getValueHandlerInstantiator());
        assertFalse(withKeyValueHandler.isStatic());
    }

    private JavaType createMapType() {
        JavaType keyType = new SimpleType(String.class);
        JavaType valueType = new SimpleType(Integer.class);
        return new MapType(Map.class, null, null, null, keyType, valueType);
    }
}