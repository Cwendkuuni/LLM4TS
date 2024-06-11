package com.fasterxml.jackson.databind.type;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;

public class MapTypeTest {

    @Test
    public void testMapTypeConstruction() {
        Class<Map<String, Integer>> mapClass = Map.class;
        TypeBindings bindings = TypeBindings.empty();
        JavaType superClass = Object.class;
        JavaType[] superInterfaces = new JavaType[] { Serializable.class };
        JavaType keyType = String.class;
        JavaType valueType = Integer.class;

        MapType mapType = MapType.construct(mapClass, bindings, superClass, superInterfaces, keyType, valueType);
        assertNotNull(mapType);
        assertEquals(mapClass, mapType.getRawClass());
        assertEquals(keyType, mapType.getKeyType());
        assertEquals(valueType, mapType.getValueType());
    }

    @Test
    public void testMapTypeConstructionDeprecated() {
        Class<Map<String, Integer>> mapClass = Map.class;
        JavaType keyType = String.class;
        JavaType valueType = Integer.class;

        MapType mapType = MapType.construct(mapClass, keyType, valueType);
        assertNotNull(mapType);
        assertEquals(mapClass, mapType.getRawClass());
        assertEquals(keyType, mapType.getKeyType());
        assertEquals(valueType, mapType.getValueType());
    }

    @Test
    public void testMapTypeWithTypeHandler() {
        Class<Map<String, Integer>> mapClass = Map.class;
        TypeBindings bindings = TypeBindings.empty();
        JavaType superClass = Object.class;
        JavaType[] superInterfaces = new JavaType[] { Serializable.class };
        JavaType keyType = String.class;
        JavaType valueType = Integer.class;
        Object valueHandler = new Object() {};

        MapType mapType = new MapType(mapClass, bindings, superClass, superInterfaces, keyType, valueType, null, valueHandler, false);
        assertNotNull(mapType);
        assertEquals(mapClass, mapType.getRawClass());
        assertEquals(keyType, mapType.getKeyType());
        assertEquals(valueType, mapType.getValueType());
        assertEquals(valueHandler, mapType.getValueHandler());
    }

    @Test
    public void testMapTypeStaticTyping() {
        Class<Map<String, Integer>> mapClass = Map.class;
        TypeBindings bindings = TypeBindings.empty();
        JavaType superClass = Object.class;
        JavaType[] superInterfaces = new JavaType[] { Serializable.class };
        JavaType keyType = String.class.getTypeName().equals("java.lang.String") ? String.class : Class.forName("java.lang.String");
        JavaType valueType = Integer.class;

        MapType mapType = new MapType(mapClass, bindings, superClass, superInterfaces, keyType, valueType, null, null, false);
        MapType staticMapType = mapType.withStaticTyping();
        assertNotNull(staticMapType);
        assertTrue(staticMapType.isStatic());
        assertEquals(mapClass, staticMapType.getRawClass());
        assertEquals(keyType, staticMapType.getKeyType());
        assertEquals(valueType, staticMapType.getValueType());
    }

    @Test
    public void testMapTypeWithValueHandler() {
        Class<Map<String, Integer>> mapClass = Map.class;
        TypeBindings bindings = TypeBindings.empty();
        JavaType superClass = Object.class;
        JavaType[] superInterfaces = new JavaType[] { Serializable.class };
        JavaType keyType = String.class;
        JavaType valueType = Integer.class;
        Object valueHandler = new Object() {};

        MapType mapType = new MapType(mapClass, bindings, superClass, superInterfaces, keyType, valueType, valueHandler, null, false);
        assertNotNull(mapType);
        assertEquals(mapClass, mapType.getRawClass());
        assertEquals(keyType, mapType.getKeyType());
        assertEquals(valueType, mapType.getValueType());
        assertEquals(valueHandler, mapType.getValueHandler());
    }

    @Test
    public void testMapTypeWithContentTypeHandler() {
        Class<Map<String, Integer>> mapClass = Map.class;
        TypeBindings bindings = TypeBindings.empty();
        JavaType superClass = Object.class;
        JavaType[] superInterfaces = new JavaType[] { Serializable.class };
        JavaType keyType = String.class;
        JavaType valueType = Integer.class;
        JavaType contentTypeHandler = Integer[].class;

        MapType mapType = new MapType(mapClass, bindings, superClass, superInterfaces, keyType, valueType, null, null, false);
        MapType mapTypeWithContentTypeHandler = mapType.withContentTypeHandler(contentTypeHandler);
        assertNotNull(mapTypeWithContentTypeHandler);
        assertEquals(mapClass, mapTypeWithContentTypeHandler.getRawClass());
        assertEquals(keyType, mapTypeWithContentTypeHandler.getKeyType());
        assertEquals(contentTypeHandler, mapTypeWithContentTypeHandler.getValueType());
    }

    @Test
    public void testMapTypeWithKeyTypeHandler() {
        Class<Map<String, Integer>> mapClass = Map.class;
        TypeBindings bindings = TypeBindings.empty();
        JavaType superClass = Object.class;
        JavaType[] superInterfaces = new JavaType[] { Serializable.class };
        JavaType keyType = String.class;
        JavaType valueType = Integer.class;
        Object keyTypeHandler = new Object() {};

        MapType mapType = new MapType(mapClass, bindings, superClass, superInterfaces, keyType, valueType, null, null, false);
        MapType mapTypeWithKeyTypeHandler = mapType.withKeyTypeHandler(keyTypeHandler);
        assertNotNull(mapTypeWithKeyTypeHandler);
        assertEquals(mapClass, mapTypeWithKeyTypeHandler.getRawClass());
        assertEquals(keyTypeHandler, mapTypeWithKeyTypeHandler.getKeyTypeHandler());
        assertEquals(valueType, mapTypeWithKeyTypeHandler.getValueType());
    }

    @Test
    public void testMapTypeWithKeyValueHandler() {
        Class<Map<String, Integer>> mapClass = Map.class;
        TypeBindings bindings = TypeBindings.empty();
        JavaType superClass = Object.class;
        JavaType[] superInterfaces = new JavaType[] { Serializable.class };
        JavaType keyType = String.class;
        JavaType valueType = Integer.class;
        Object keyValueHandler = new Object() {};

        MapType mapType = new MapType(mapClass, bindings, superClass, superInterfaces, keyType, valueType, null, null, false);
        MapType mapTypeWithKeyValueHandler = mapType.withKeyValueHandler(keyValueHandler);
        assertNotNull(mapTypeWithKeyValueHandler);
        assertEquals(mapClass, mapTypeWithKeyValueHandler.getRawClass());
        assertEquals(keyType, mapTypeWithKeyValueHandler.getKeyType());
        assertEquals(keyValueHandler, mapTypeWithKeyValueHandler.getKeyValueHandler());
        assertEquals(valueType, mapTypeWithKeyValueHandler.getValueType());
    }

    @Test
    public void testMapTypeToString() {
        Class<Map<String, Integer>> mapClass = Map.class;
        TypeBindings bindings = TypeBindings.empty();
        JavaType superClass = Object.class;
        JavaType[] superInterfaces = new JavaType[] { Serializable.class };
        JavaType keyType = String.class;
        JavaType valueType = Integer.class;

        MapType mapType = new MapType(mapClass, bindings, superClass, superInterfaces, keyType, valueType, null, null, false);
        assertEquals("[map type; class java.util.Map, java.lang.String -> java.lang.Integer]", mapType.toString());
    }
}