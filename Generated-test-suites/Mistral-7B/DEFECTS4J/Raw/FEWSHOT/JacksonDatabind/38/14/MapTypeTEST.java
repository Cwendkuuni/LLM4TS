package com.fasterxml.jackson.databind.type;

import static org.junit.Assert.*;

import org.junit.Test;

public class MapTypeTest {

    @Test
    public void testConstruct() {
        JavaType keyType = new SimpleType("java.lang.String");
        JavaType valueType = new SimpleType("com.example.MyClass");
        MapType mapType = MapType.construct(Map.class, null, null, null, keyType, valueType);
        assertEquals(Map.class, mapType.getRawClass());
        assertEquals(null, mapType.getName());
        assertEquals(keyType, mapType.getKeyType());
        assertEquals(valueType, mapType.getValueType());
        assertNull(mapType.getValueHandler());
        assertNull(mapType.getTypeHandler());
        assertFalse(mapType.isStatic());
    }

    @Test
    public void testConstructWithBindings() {
        JavaType keyType = new SimpleType("java.lang.String");
        JavaType valueType = new SimpleType("com.example.MyClass");
        TypeBindings bindings = new TypeBindings();
        MapType mapType = MapType.construct(Map.class, bindings, null, null, keyType, valueType);
        assertEquals(Map.class, mapType.getRawClass());
        assertNotNull(mapType.getName());
        assertEquals(keyType, mapType.getKeyType());
        assertEquals(valueType, mapType.getValueType());
        assertNull(mapType.getValueHandler());
        assertNull(mapType.getTypeHandler());
        assertFalse(mapType.isStatic());
        assertEquals(bindings, mapType.getTypeBindings());
    }

    @Test
    public void testConstructDeprecated() {
        JavaType keyType = new SimpleType("java.lang.String");
        JavaType valueType = new SimpleType("com.example.MyClass");
        MapType mapType = MapType.construct(Map.class, null, _bogusSuperClass(Map.class), null, keyType, valueType);
        assertEquals(Map.class, mapType.getRawClass());
        assertEquals(null, mapType.getName());
        assertEquals(keyType, mapType.getKeyType());
        assertEquals(valueType, mapType.getValueType());
        assertNull(mapType.getValueHandler());
        assertNull(mapType.getTypeHandler());
        assertFalse(mapType.isStatic());
    }

    @Test
    public void testNarrow() {
        JavaType keyType = new SimpleType("java.lang.String");
        JavaType valueType = new SimpleType("com.example.MyClass");
        Class<?> rawType = HashMap.class;
        MapType parentMapType = new MapType(Map.class, null, null, null, keyType, valueType);
        MapType childMapType = parentMapType.narrow(rawType);
        assertEquals(HashMap.class, childMapType.getRawClass());
        assertEquals(parentMapType.getName(), childMapType.getName());
        assertEquals(keyType, childMapType.getKeyType());
        assertEquals(valueType, childMapType.getValueType());
        assertNull(childMapType.getValueHandler());
        assertNull(childMapType.getTypeHandler());
        assertFalse(childMapType.isStatic());
    }

    @Test
    public void testWithTypeHandler() {
        JavaType keyType = new SimpleType("java.lang.String");
        JavaType valueType = new SimpleType("com.example.MyClass");
        MapType mapType = new MapType(Map.class, null, null, null, keyType, valueType);
        Object valueHandler = new Object() {};
        MapType withValueHandler = mapType.withValueHandler(valueHandler);
        assertEquals(mapType.getRawClass(), withValueHandler.getRawClass());
        assertEquals(mapType.getName(), withValueHandler.getName());
        assertEquals(mapType.getKeyType(), withValueHandler.getKeyType());
        assertEquals(valueType, withValueHandler.getValueType());
        assertEquals(valueHandler, withValueHandler.getValueHandler());
        assertNull(withValueHandler.getTypeHandler());
        assertFalse(withValueHandler.isStatic());
    }

    @Test
    public void testWithContentTypeHandler() {
        JavaType keyType = new SimpleType("java.lang.String");
        JavaType valueType = new SimpleType("com.example.MyClass");
        MapType mapType = new MapType(Map.class, null, null, null, keyType, valueType);
        Object contentTypeHandler = new Object() {};
        JavaType valueTypeWithContentTypeHandler = valueType.withTypeHandler(contentTypeHandler);
        MapType withContentTypeHandler = mapType.withContentTypeHandler(contentTypeHandler);
        assertEquals(mapType.getRawClass(), withContentTypeHandler.getRawClass());
        assertEquals(mapType.getName(), withContentTypeHandler.getName());
        assertEquals(keyType, withContentTypeHandler.getKeyType());
        assertEquals(valueTypeWithContentTypeHandler, withContentTypeHandler.getValueType());
        assertEquals(mapType.getValueHandler(), withContentTypeHandler.getValueHandler());
        assertEquals(contentTypeHandler, withContentTypeHandler.getTypeHandler());
        assertFalse(withContentTypeHandler.isStatic());
    }

    @Test
    public void testWithValueHandler() {
        JavaType keyType = new SimpleType("java.lang.String");
        JavaType valueType = new SimpleType("com.example.MyClass");
        MapType mapType = new MapType(Map.class, null, null, null, keyType, valueType);
        Object valueHandler = new Object() {};
        MapType withValueHandler = mapType.withValueHandler(valueHandler);
        assertEquals(mapType.getRawClass(), withValueHandler.getRawClass());
        assertEquals(mapType.getName(), withValueHandler.getName());
        assertEquals(keyType, withValueHandler.getKeyType());
        assertEquals(valueType, withValueHandler.getValueType());
        assertEquals(valueHandler, withValueHandler.getValueHandler());
        assertNull(withValueHandler.getTypeHandler());
        assertFalse(withValueHandler.isStatic());
    }

    @Test
    public void testWithContentValueHandler() {
        JavaType keyType = new SimpleType("java.lang.String");
        JavaType valueType = new SimpleType("com.example.MyClass");
        MapType mapType = new MapType(Map.class, null, null, null, keyType, valueType);
        Object contentValueHandler = new Object() {};
        JavaType valueTypeWithContentValueHandler = valueType.withValueHandler(contentValueHandler);
        MapType withContentValueHandler = mapType.withContentValueHandler(contentValueHandler);
        assertEquals(mapType.getRawClass(), withContentValueHandler.getRawClass());
        assertEquals(mapType.getName(), withContentValueHandler.getName());
        assertEquals(keyType, withContentValueHandler.getKeyType());
        assertEquals(valueTypeWithContentValueHandler, withContentValueHandler.getValueType());
        assertEquals(mapType.getValueHandler(), withContentValueHandler.getValueHandler());
        assertEquals(contentValueHandler, withContentValueHandler.getValueHandler());
        assertNull(withContentValueHandler.getTypeHandler());
        assertFalse(withContentValueHandler.isStatic());
    }

    @Test
    public void testWithStaticTyping() {
        JavaType keyType = new SimpleType("java.lang.String");
        JavaType valueType = new SimpleType("com.example.MyClass");
        MapType mapType = new MapType(Map.class, null, null, null, keyType, valueType);
        MapType withStaticTyping = mapType.withStaticTyping();
        assertEquals(mapType.getRawClass(), withStaticTyping.getRawClass());
        assertEquals(mapType.getName(), withStaticTyping.getName());
        assertEquals(keyType, withStaticTyping.getKeyType());
        assertEquals(valueType, withStaticTyping.getValueType());
        assertNull(withStaticTyping.getValueHandler());
        assertNull(withStaticTyping.getTypeHandler());
        assertTrue(withStaticTyping.isStatic());
    }

    @Test
    public void testWithContentType() {
        JavaType keyType = new SimpleType("java.lang.String");
        JavaType valueType = new SimpleType("com.example.MyClass");
        JavaType contentType = new SimpleType("com.example.MyOtherClass");
        MapType mapType = new MapType(Map.class, null, null, null, keyType, valueType);
        MapType withContentType = mapType.withContentType(contentType);
        assertEquals(mapType.getRawClass(), withContentType.getRawClass());
        assertEquals(mapType.getName(), withContentType.getName());
        assertEquals(keyType, withContentType.getKeyType());
        assertEquals(contentType, withContentType.getValueType());
        assertNull(withContentType.getValueHandler());
        assertNull(withContentType.getTypeHandler());
        assertFalse(withContentType.isStatic());
    }

    @Test
    public void testWithKeyType() {
        JavaType keyType = new SimpleType("java.lang.String");
        JavaType valueType = new SimpleType("com.example.MyClass");
        JavaType newKeyType = new SimpleType("java.lang.Integer");
        MapType mapType = new MapType(Map.class, null, null, null, keyType, valueType);
        MapType withNewKeyType = mapType.withKeyType(newKeyType);
        assertEquals(mapType.getRawClass(), withNewKeyType.getRawClass());
        assertEquals(mapType.getName(), withNewKeyType.getName());
        assertEquals(newKeyType, withNewKeyType.getKeyType());
        assertEquals(valueType, withNewKeyType.getValueType());
        assertNull(withNewKeyType.getValueHandler());
        assertNull(withNewKeyType.getTypeHandler());
        assertFalse(withNewKeyType.isStatic());
    }

    @Test
    public void testRefine() {
        JavaType keyType = new SimpleType("java.lang.String");
        JavaType valueType = new SimpleType("com.example.MyClass");
        Class<?> rawType = HashMap.class;
        MapType parentMapType = new MapType(Map.class, null, null, null, keyType, valueType);
        MapType childMapType = parentMapType.refine(rawType, null, null, null);
        assertEquals(HashMap.class, childMapType.getRawClass());
        assertEquals(parentMapType.getName(), childMapType.getName());
        assertEquals(keyType, childMapType.getKeyType());
        assertEquals(valueType, childMapType.getValueType());
        assertNull(childMapType.getValueHandler());
        assertNull(childMapType.getTypeHandler());
        assertFalse(childMapType.isStatic());
    }

    @Test
    public void testWithKeyTypeHandler() {
        JavaType keyType = new SimpleType("java.lang.String");
        JavaType valueType = new SimpleType("com.example.MyClass");
        MapType mapType = new MapType(Map.class, null, null, null, keyType, valueType);
        Object keyTypeHandler = new Object() {};
        MapType withKeyTypeHandler = mapType.withKeyTypeHandler(keyTypeHandler);
        assertEquals(mapType.getRawClass(), withKeyTypeHandler.getRawClass());
        assertEquals(mapType.getName(), withKeyTypeHandler.getName());
        assertEquals(keyType, withKeyTypeHandler.getKeyType());
        assertEquals(valueType, withKeyTypeHandler.getValueType());
        assertNull(withKeyTypeHandler.getValueHandler());
        assertEquals(keyTypeHandler, withKeyTypeHandler.getTypeHandler());
        assertFalse(withKeyTypeHandler.isStatic());
    }

    @Test
    public void testWithKeyValueHandler() {
        JavaType keyType = new SimpleType("java.lang.String");
        JavaType valueType = new SimpleType("com.example.MyClass");
        MapType mapType = new MapType(Map.class, null, null, null, keyType, valueType);
        Object keyValueHandler = new Object() {};
        MapType withKeyValueHandler = mapType.withKeyValueHandler(keyValueHandler);
        assertEquals(mapType.getRawClass(), withKeyValueHandler.getRawClass());
        assertEquals(mapType.getName(), withKeyValueHandler.getName());
        assertEquals(keyType, withKeyValueHandler.getKeyType());
        assertEquals(valueType, withKeyValueHandler.getValueType());
        assertEquals(keyValueHandler, withKeyValueHandler.getValueHandler());
        assertNull(withKeyValueHandler.getTypeHandler());
        assertFalse(withKeyValueHandler.isStatic());
    }
}