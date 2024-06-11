package com.fasterxml.jackson.databind.type;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class MapTypeTest {

    @Test
    public void testConstruct() {
        JavaType keyType = new SimpleType("java.lang.String");
        JavaType valueType = new SimpleType("java.lang.Integer");
        Class<?> rawType = Map.class;
        TypeBindings bindings = TypeBindings.empty();
        JavaType superClass = Object.class;
        JavaType[] superInts = new JavaType[0];

        MapType mapType = MapType.construct(rawType, bindings, superClass, superInts, keyType, valueType);
        assertEquals(Map.class, mapType.getRawClass());
        assertEquals(keyType, mapType.getKeyType());
        assertEquals(valueType, mapType.getValueType());
    }

    @Test
    public void testConstructDeprecated() {
        JavaType keyType = new SimpleType("java.lang.String");
        JavaType valueType = new SimpleType("java.lang.Integer");
        Class<?> rawType = Map.class;

        MapType mapType = MapType.construct(rawType, keyType, valueType);
        assertEquals(Map.class, mapType.getRawClass());
        assertEquals(keyType, mapType.getKeyType());
        assertEquals(valueType, mapType.getValueType());
    }

    @Test
    public void testNarrow() {
        JavaType keyType = new SimpleType("java.lang.String");
        JavaType valueType = new SimpleType("java.lang.Integer");
        Class<?> subclass = HashMap.class;

        MapType mapType = new MapType(Map.class, null, Object.class, new JavaType[0], keyType, valueType);
        MapType narrowedType = mapType.narrow(subclass);
        assertEquals(HashMap.class, narrowedType.getRawClass());
        assertEquals(keyType, narrowedType.getKeyType());
        assertEquals(valueType, narrowedType.getValueType());
    }

    @Test
    public void testWithTypeHandler() {
        JavaType keyType = new SimpleType("java.lang.String");
        JavaType valueType = new SimpleType("java.lang.Integer");
        Object valueHandler = new Object();

        MapType mapType = new MapType(Map.class, null, Object.class, new JavaType[0], keyType, valueType);
        MapType withTypeHandler = mapType.withTypeHandler(valueHandler);
        assertEquals(mapType.getRawClass(), withTypeHandler.getRawClass());
        assertEquals(keyType, withTypeHandler.getKeyType());
        assertEquals(valueType, withTypeHandler.getValueType());
        assertNotNull(withTypeHandler.getTypeHandler());
    }

    @Test
    public void testWithContentTypeHandler() {
        JavaType keyType = new SimpleType("java.lang.String");
        JavaType valueType = new SimpleType("java.lang.Integer");
        Object contentTypeHandler = new Object();

        MapType mapType = new MapType(Map.class, null, Object.class, new JavaType[0], keyType, valueType);
        MapType withContentTypeHandler = mapType.withContentTypeHandler(contentTypeHandler);
        assertEquals(mapType.getRawClass(), withContentTypeHandler.getRawClass());
        assertEquals(keyType, withContentTypeHandler.getKeyType());
        assertEquals(valueType, withContentTypeHandler.getValueType());
        assertNotNull(withContentTypeHandler.getTypeHandler());
    }

    @Test
    public void testWithValueHandler() {
        JavaType keyType = new SimpleType("java.lang.String");
        JavaType valueType = new SimpleType("java.lang.Integer");
        Object valueHandler = new Object();

        MapType mapType = new MapType(Map.class, null, Object.class, new JavaType[0], keyType, valueType);
        MapType withValueHandler = mapType.withValueHandler(valueHandler);
        assertEquals(mapType.getRawClass(), withValueHandler.getRawClass());
        assertEquals(keyType, withValueHandler.getKeyType());
        assertEquals(valueType, withValueHandler.getValueType());
        assertNotNull(withValueHandler.getValueHandler());
    }

    @Test
    public void testWithContentValueHandler() {
        JavaType keyType = new SimpleType("java.lang.String");
        JavaType valueType = new SimpleType("java.lang.Integer");
        Object contentValueHandler = new Object();

        MapType mapType = new MapType(Map.class, null, Object.class, new JavaType[0], keyType, valueType);
        MapType withContentValueHandler = mapType.withContentValueHandler(contentValueHandler);
        assertEquals(mapType.getRawClass(), withContentValueHandler.getRawClass());
        assertEquals(keyType, withContentValueHandler.getKeyType());
        assertEquals(valueType, withContentValueHandler.getValueType());
        assertNotNull(withContentValueHandler.getValueHandler());
    }

    @Test
    public void testWithStaticTyping() {
        JavaType keyType = new SimpleType("java.lang.String").withStaticTyping();
        JavaType valueType = new SimpleType("java.lang.Integer").withStaticTyping();
        Class<?> rawType = Map.class;

        MapType mapType = new MapType(rawType, null, Object.class, new JavaType[0], keyType, valueType);
        MapType withStaticTyping = mapType.withStaticTyping();
        assertEquals(mapType.getRawClass(), withStaticTyping.getRawClass());
        assertEquals(keyType, withStaticTyping.getKeyType());
        assertEquals(valueType, withStaticTyping.getValueType());
        assertTrue(withStaticTyping.isStatic());
    }

    @Test
    public void testWithContentType() {
        JavaType keyType = new SimpleType("java.lang.String");
        JavaType contentType = new SimpleType("java.lang.Double");
        JavaType valueType = new SimpleType("java.lang.Integer");

        MapType mapType = new MapType(Map.class, null, Object.class, new JavaType[0], keyType, valueType);
        MapType withContentType = mapType.withContentType(contentType);
        assertEquals(mapType.getRawClass(), withContentType.getRawClass());
        assertEquals(keyType, withContentType.getKeyType());
        assertEquals(contentType, withContentType.getValueType());
    }

    @Test
    public void testWithKeyType() {
        JavaType keyType1 = new SimpleType("java.lang.String");
        JavaType keyType2 = new SimpleType("java.lang.Integer");
        JavaType valueType = new SimpleType("java.lang.Integer");

        MapType mapType = new MapType(Map.class, null, Object.class, new JavaType[0], keyType1, valueType);
        MapType withKeyType = mapType.withKeyType(keyType2);
        assertEquals(mapType.getRawClass(), withKeyType.getRawClass());
        assertEquals(keyType2, withKeyType.getKeyType());
        assertEquals(valueType, withKeyType.getValueType());
    }

    @Test
    public void testRefine() {
        JavaType keyType = new SimpleType("java.lang.String");
        JavaType valueType = new SimpleType("java.lang.Integer");
        Class<?> rawType = HashMap.class;
        TypeBindings bindings = TypeBindings.empty();
        JavaType superClass = Object.class;
        JavaType[] superInterfaces = new JavaType[0];

        MapType mapType = new MapType(Map.class, null, Object.class, new JavaType[0], keyType, valueType);
        JavaType refinedType = mapType.refine(rawType, bindings, superClass, superInterfaces);
        assertEquals(HashMap.class, refinedType.getRawClass());
        assertEquals(keyType, refinedType.getKeyType());
        assertEquals(valueType, refinedType.getValueType());
    }

    @Test
    public void testWithKeyTypeHandler() {
        JavaType keyType = new SimpleType("java.lang.String");
        JavaType valueType = new SimpleType("java.lang.Integer");
        Object keyTypeHandler = new Object();

        MapType mapType = new MapType(Map.class, null, Object.class, new JavaType[0], keyType, valueType);
        MapType withKeyTypeHandler = mapType.withKeyTypeHandler(keyTypeHandler);
        assertEquals(mapType.getRawClass(), withKeyTypeHandler.getRawClass());
        assertEquals(keyType, withKeyTypeHandler.getKeyType());
        assertEquals(valueType, withKeyTypeHandler.getValueType());
        assertNotNull(withKeyTypeHandler.getKeyTypeHandler());
    }

    @Test
    public void testWithKeyValueHandler() {
        JavaType keyType = new SimpleType("java.lang.String");
        JavaType valueType = new SimpleType("java.lang.Integer");
        Object keyValueHandler = new Object();

        MapType mapType = new MapType(Map.class, null, Object.class, new JavaType[0], keyType, valueType);
        MapType withKeyValueHandler = mapType.withKeyValueHandler(keyValueHandler);
        assertEquals(mapType.getRawClass(), withKeyValueHandler.getRawClass());
        assertEquals(keyType, withKeyValueHandler.getKeyType());
        assertEquals(valueType, withKeyValueHandler.getValueType());
        assertNotNull(withKeyValueHandler.getKeyValueHandler());
    }
}