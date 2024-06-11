package com.fasterxml.jackson.databind.type;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class MapTypeTest {

    @Test
    public void testConstruct() {
        JavaType keyType = new SimpleType("java.lang.String");
        JavaType valueType = new SimpleType("com.example.MyClass");
        JavaType mapType = MapType.construct(Map.class, null, null, null, keyType, valueType);
        assertNotNull(mapType);
        assertEquals(Map.class, mapType.getRawClass());
        assertEquals(keyType, mapType.getKeyType());
        assertEquals(valueType, mapType.getValueType());
    }

    @Test
    public void testConstructDeprecated() {
        JavaType keyType = new SimpleType("java.lang.String");
        JavaType valueType = new SimpleType("com.example.MyClass");
        JavaType mapType = MapType.construct(Map.class, keyType, valueType);
        assertNotNull(mapType);
        assertEquals(Map.class, mapType.getRawClass());
        assertEquals(keyType, mapType.getKeyType());
        assertEquals(valueType, mapType.getValueType());
    }

    @Test
    public void testWithTypeHandler() {
        JavaType keyType = new SimpleType("java.lang.String");
        JavaType valueType = new SimpleType("com.example.MyClass");
        Object valueHandler = new Object();
        JavaType mapType = new MapType(Map.class, null, null, null, keyType, valueType);
        JavaType newMapType = mapType.withTypeHandler(valueHandler);
        assertNotNull(newMapType);
        assertEquals(mapType.getRawClass(), newMapType.getRawClass());
        assertEquals(mapType.getKeyType(), newMapType.getKeyType());
        assertEquals(mapType.getValueType(), newMapType.getValueType());
        assertEquals(valueHandler, newMapType.getTypeHandler());
    }

    @Test
    public void testWithContentTypeHandler() {
        JavaType keyType = new SimpleType("java.lang.String");
        JavaType valueType = new SimpleType("com.example.MyClass");
        Object valueHandler = new Object();
        Object contentTypeHandler = new Object();
        JavaType mapType = new MapType(Map.class, null, null, null, keyType, valueType);
        JavaType newMapType = mapType.withContentTypeHandler(contentTypeHandler);
        assertNotNull(newMapType);
        assertEquals(mapType.getRawClass(), newMapType.getRawClass());
        assertEquals(mapType.getKeyType(), newMapType.getKeyType());
        JavaType newValueType = newMapType.getValueType();
        assertEquals(valueType, newValueType.getValueType());
        assertEquals(contentTypeHandler, newValueType.getTypeHandler());
    }

    @Test
    public void testWithValueHandler() {
        JavaType keyType = new SimpleType("java.lang.String");
        JavaType valueType = new SimpleType("com.example.MyClass");
        Object valueHandler = new Object();
        JavaType mapType = new MapType(Map.class, null, null, null, keyType, valueType);
        JavaType newMapType = mapType.withValueHandler(valueHandler);
        assertNotNull(newMapType);
        assertEquals(mapType.getRawClass(), newMapType.getRawClass());
        assertEquals(mapType.getKeyType(), newMapType.getKeyType());
        assertEquals(mapType.getValueType(), newMapType.getValueType());
        assertEquals(valueHandler, newMapType.getValueHandler());
    }

    @Test
    public void testWithContentValueHandler() {
        JavaType keyType = new SimpleType("java.lang.String");
        JavaType valueType = new SimpleType("com.example.MyClass");
        Object valueHandler = new Object();
        Object contentValueHandler = new Object();
        JavaType mapType = new MapType(Map.class, null, null, null, keyType, valueType);
        JavaType newMapType = mapType.withContentValueHandler(contentValueHandler);
        assertNotNull(newMapType);
        assertEquals(mapType.getRawClass(), newMapType.getRawClass());
        assertEquals(mapType.getKeyType(), newMapType.getKeyType());
        JavaType newValueType = newMapType.getValueType();
        assertEquals(valueType, newValueType.getValueType());
        assertEquals(contentValueHandler, newValueType.getValueHandler());
    }

    @Test
    public void testWithStaticTyping() {
        JavaType keyType = new SimpleType("java.lang.String");
        JavaType valueType = new SimpleType("com.example.MyClass");
        JavaType mapType = new MapType(Map.class, null, null, null, keyType, valueType);
        JavaType newMapType = mapType.withStaticTyping();
        assertNotNull(newMapType);
        assertTrue(newMapType.isStatic());
        assertEquals(mapType.getRawClass(), newMapType.getRawClass());
        assertEquals(mapType.getKeyType(), newMapType.getKeyType());
        assertEquals(mapType.getValueType(), newMapType.getValueType());
    }

    @Test
    public void testWithContentType() {
        JavaType keyType = new SimpleType("java.lang.String");
        JavaType originalValueType = new SimpleType("com.example.MyClass");
        JavaType newValueType = new SimpleType("com.example.MyOtherClass");
        JavaType mapType = new MapType(Map.class, null, null, null, keyType, originalValueType);
        JavaType newMapType = mapType.withContentType(newValueType);
        assertNotNull(newMapType);
        assertEquals(mapType.getRawClass(), newMapType.getRawClass());
        assertEquals(mapType.getKeyType(), newMapType.getKeyType());
        assertEquals(newValueType, newMapType.getValueType());
    }

    @Test
    public void testWithKeyType() {
        JavaType originalKeyType = new SimpleType("java.lang.String");
        JavaType valueType = new SimpleType("com.example.MyClass");
        JavaType mapType = new MapType(Map.class, null, null, null, originalKeyType, valueType);
        JavaType newMapType = mapType.withKeyType(new SimpleType("java.lang.Integer"));
        assertNotNull(newMapType);
        assertEquals(mapType.getRawClass(), newMapType.getRawClass());
        assertEquals(new SimpleType("java.lang.Integer"), newMapType.getKeyType());
        assertEquals(valueType, newMapType.getValueType());
    }

    @Test
    public void testRefine() {
        JavaType rawType = Map.class;
        TypeBindings bindings = new TypeBindings();
        JavaType superClass = null;
        JavaType[] superInterfaces = new JavaType[0];
        JavaType originalKeyType = new SimpleType("java.lang.String");
        JavaType originalValueType = new SimpleType("com.example.MyClass");
        JavaType mapType = new MapType(rawType, null, superClass, superInterfaces, originalKeyType, originalValueType);
        JavaType newMapType = mapType.refine(HashMap.class, null, null, null);
        assertNotNull(newMapType);
        assertEquals(HashMap.class, newMapType.getRawClass());
        assertEquals(originalKeyType, newMapType.getKeyType());
        assertEquals(originalValueType, newMapType.getValueType());
    }

    @Test
    public void testWithKeyTypeHandler() {
        JavaType keyType = new SimpleType("java.lang.String");
        JavaType valueType = new SimpleType("com.example.MyClass");
        Object keyTypeHandler = new Object();
        JavaType mapType = new MapType(Map.class, null, null, null, keyType, valueType);
        JavaType newMapType = mapType.withKeyTypeHandler(keyTypeHandler);
        assertNotNull(newMapType);
        assertEquals(mapType.getRawClass(), newMapType.getRawClass());
        assertEquals(mapType.getKeyType(), newMapType.getKeyType());
        assertEquals(valueType, newMapType.getValueType());
        assertEquals(keyTypeHandler, newMapType.getKeyTypeHandler());
    }

    @Test
    public void testWithKeyValueHandler() {
        JavaType keyType = new SimpleType("java.lang.String");
        JavaType valueType = new SimpleType("com.example.MyClass");
        Object keyValueHandler = new Object();
        JavaType mapType = new MapType(Map.class, null, null, null, keyType, valueType);
        JavaType newMapType = mapType.withKeyValueHandler(keyValueHandler);
        assertNotNull(newMapType);
        assertEquals(mapType.getRawClass(), newMapType.getRawClass());
        assertEquals(mapType.getKeyType(), newMapType.getKeyType());
        assertEquals(valueType, newMapType.getValueType());
        assertEquals(keyValueHandler, newMapType.getKeyValueHandler());
    }
}