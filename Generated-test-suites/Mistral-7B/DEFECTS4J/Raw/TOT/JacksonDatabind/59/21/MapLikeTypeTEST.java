package com.fasterxml.jackson.databind.type.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;

import java.lang.reflect.TypeVariable;
import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.MapLikeType;
import com.fasterxml.jackson.databind.type.TypeBase;
import com.fasterxml.jackson.databind.type.TypeBindings;
import com.fasterxml.jackson.databind.type.TypeModifier;

public class MapLikeTypeTest {

    @Test
    public void testMapLikeTypeCreation() {
        // Test creating MapLikeType from constructor with Class, TypeBindings, JavaType, JavaType
        Class<Map<String, Integer>> rawType = Map.class;
        JavaType keyType = JavaType.constructParametricType(Map.class, String.class, Integer.class);
        JavaType valueType = JavaType.constructParametricType(Map.class, String.class, Integer.class);
        TypeBindings bindings = TypeBindings.create(rawType, keyType, valueType);
        MapLikeType mapLikeType = new MapLikeType(rawType, bindings, _bogusSuperClass(rawType), null, keyType, valueType, null, null, false);
        assertEquals("Map<String, Integer>", mapLikeType.toString());
        assertEquals(Map.class, mapLikeType.getRawClass());
        assertEquals(String.class, mapLikeType.getKeyType().getRawClass());
        assertEquals(Integer.class, mapLikeType.getValueType().getRawClass());

        // Test creating MapLikeType from constructor with TypeBase, JavaType, JavaType
        TypeBase baseType = new TypeBase() {
            @Override
            public JavaType getRawType() {
                return Map.class;
            }

            @Override
            public String toString() {
                return "Map<String, Integer>";
            }
        };
        mapLikeType = new MapLikeType(baseType, keyType, valueType);
        assertEquals("Map<String, Integer>", mapLikeType.toString());
        assertEquals(Map.class, mapLikeType.getRawClass());
        assertEquals(String.class, mapLikeType.getKeyType().getRawClass());
        assertEquals(Integer.class, mapLikeType.getValueType().getRawClass());

        // Test creating MapLikeType from factory method
        JavaType baseType2 = JavaType.constructParametricType(HashMap.class, Object.class, Object.class);
        JavaType keyType2 = JavaType.constructParametricType(String.class, Object.class);
        JavaType valueType2 = JavaType.constructParametricType(Integer.class, Object.class);
        MapLikeType mapLikeType2 = MapLikeType.upgradeFrom(baseType2, keyType2, valueType2);
        assertEquals("Map<String, Integer>", mapLikeType2.toString());
        assertEquals(HashMap.class, mapLikeType2.getRawClass());
        assertEquals(String.class, mapLikeType2.getKeyType().getRawClass());
        assertEquals(Integer.class, mapLikeType2.getValueType().getRawClass());
    }

    @Test
    public void testMapLikeTypeModifications() {
        // Test withKeyType()
        JavaType mapType = JavaType.constructParametricType(Map.class, String.class, Integer.class);
        MapLikeType mapLikeType = new MapLikeType(mapType.getRawClass(), null, null, null, null, null, null, null, false);
        JavaType newKeyType = JavaType.constructParametricType(StringBuilder.class, String.class);
        MapLikeType newMapLikeType = mapLikeType.withKeyType(newKeyType);
        assertEquals("Map<java.lang.StringBuilder, java.lang.Integer>", newMapLikeType.toString());
        assertEquals(StringBuilder.class, newMapLikeType.getKeyType().getRawClass());
        assertEquals(Integer.class, newMapLikeType.getValueType().getRawClass());

        // Test withContentType()
        JavaType newValueType = JavaType.constructParametricType(Long.class, Object.class);
        MapLikeType newNewMapLikeType = newMapLikeType.withContentType(newValueType);
        assertEquals("Map<java.lang.StringBuilder, java.lang.Long>", newNewMapLikeType.toString());
        assertEquals(StringBuilder.class, newNewMapLikeType.getKeyType().getRawClass());
        assertEquals(Long.class, newNewMapLikeType.getValueType().getRawClass());

        // Test withTypeHandler()
        Object handler = new Object() {
            public void handle(MapLikeType type, JavaType valueType) {
                // Do something
            }
        };
        MapLikeType handlerMapLikeType = new MapLikeType(mapType.getRawClass(), null, null, null, null, null, handler, null, false);
        MapLikeType newHandlerMapLikeType = newMapLikeType.withTypeHandler(handler);
        assertNotNull(newHandlerMapLikeType.getTypeHandler());
        assertEquals(handler, newHandlerMapLikeType.getTypeHandler());

        // Test withContentTypeHandler()
        Object contentHandler = new Object() {
            public void handle(MapLikeType type, JavaType contentType, Object content) {
                // Do something
            }
        };
        MapLikeType contentHandlerMapLikeType = new MapLikeType(mapType.getRawClass(), null, null, null, null, null, null, contentHandler, false);
        MapLikeType newContentHandlerMapLikeType = newMapLikeType.withContentTypeHandler(contentHandler);
        assertNotNull(newContentHandlerMapLikeType.getContentTypeHandler());
        assertEquals(contentHandler, newContentHandlerMapLikeType.getContentTypeHandler());

        // Test withValueHandler()
        Object valueHandler = new Object() {
            public void handle(MapLikeType type, Object value) {
                // Do something
            }
        };
        MapLikeType valueHandlerMapLikeType = new MapLikeType(mapType.getRawClass(), null, null, null, null, null, null, null, false);
        MapLikeType newValueHandlerMapLikeType = newMapLikeType.withValueHandler(valueHandler);
        assertNotNull(newValueHandlerMapLikeType.getValueHandler());
        assertEquals(valueHandler, newValueHandlerMapLikeType.getValueHandler());

        // Test withContentValueHandler()
        Object contentValueHandler = new Object() {
            public void handle(MapLikeType type, Object contentValue) {
                // Do something
            }
        };
        MapLikeType contentValueHandlerMapLikeType = new MapLikeType(mapType.getRawClass(), null, null, null, null, null, null, null, false);
        MapLikeType newContentValueHandlerMapLikeType = newMapLikeType.withContentValueHandler(contentValueHandler);
        assertNotNull(newContentValueHandlerMapLikeType.getContentValueHandler());
        assertEquals(contentValueHandler, newContentValueHandlerMapLikeType.getContentValueHandler());

        // Test withStaticTyping()
        MapLikeType staticMapLikeType = mapLikeType.withStaticTyping();
        assertTrue(staticMapLikeType.isStatic());
        assertEquals(Map.class, staticMapLikeType.getRawClass());

        // Test refine()
        JavaType refinedType = JavaType.constructParametricType(HashMap.class, String.class, String.class);
        MapLikeType refinedMapLikeType = mapLikeType.refine(refinedType.getRawClass(), null, null, null);
        assertEquals("Map<java.lang.String, java.lang.String>", refinedMapLikeType.toString());
        assertEquals(HashMap.class, refinedMapLikeType.getRawClass());
        assertEquals(String.class, refinedMapLikeType.getKeyType().getRawClass());
        assertEquals(String.class, refinedMapLikeType.getValueType().getRawClass());
    }

    @Test
    public void testMapLikeTypeInvalidOperations() {
        // Test _narrow() deprecated
        JavaType invalidType = JavaType.constructParametricType(List.class, Object.class);
        MapLikeType mapLikeType = new MapLikeType(Map.class, null, null, null, null, null, null, null, false);
        assertThrows(UnsupportedOperationException.class, () -> mapLikeType._narrow(invalidType));

        // Test withKeyType() with same keyType
        JavaType sameKeyType = JavaType.constructParametricType(Map.class, String.class, Integer.class);
        assertThrows(IllegalArgumentException.class, () -> mapLikeType.withKeyType(sameKeyType));

        // Test withContentType() with same valueType
        JavaType sameValueType = JavaType.constructParametricType(Map.class, String.class, Integer.class);
        assertThrows(IllegalArgumentException.class, () -> mapLikeType.withContentType(sameValueType));

        // Test withTypeHandler() with same handler
        Object sameHandler = new Object() {
            public void handle(MapLikeType type, JavaType valueType) {
                // Do something
            }
        };
        assertThrows(IllegalArgumentException.class, () -> mapLikeType.withTypeHandler(sameHandler));

        // Test withContentTypeHandler() with same handler
        Object sameContentHandler = new Object() {
            public void handle(MapLikeType type, JavaType contentType, Object content) {
                // Do something
            }
        };
        assertThrows(IllegalArgumentException.class, () -> mapLikeType.withContentTypeHandler(sameContentHandler));

        // Test withValueHandler() with same handler
        Object sameValueHandler = new Object() {
            public void handle(MapLikeType type, Object value) {
                // Do something
            }
        };
        assertThrows(IllegalArgumentException.class, () -> mapLikeType.withValueHandler(sameValueHandler));

        // Test withContentValueHandler() with same handler
        Object sameContentValueHandler = new Object() {
            public void handle(MapLikeType type, Object contentValue) {
                // Do something
            }
        };
        assertThrows(IllegalArgumentException.class, () -> mapLikeType.withContentValueHandler(sameContentValueHandler));

        // Test construct() deprecated
        assertThrows(Deprecated.class, () -> MapLikeType.construct(Map.class, keyType, valueType));
    }

    @Test
    public void testMapLikeTypeCanonicalName() {
        // Test getCanonicalName()
        JavaType mapType = JavaType.constructParametricType(Map.class, String.class, Integer.class);
        MapLikeType mapLikeType = new MapLikeType(mapType.getRawClass(), null, null, null, null, null, null, null, false);
        assertEquals("class Map<java.lang.String, java.lang.Integer>", mapLikeType.buildCanonicalName());

        // Test getErasedSignature() and getGenericSignature()
        StringBuilder erasedSignature = new StringBuilder();
        mapLikeType.getErasedSignature(erasedSignature);
        assertEquals("Map<K, V>", erasedSignature.toString());

        StringBuilder genericSignature = new StringBuilder();
        mapLikeType.getGenericSignature(genericSignature);
        assertEquals("Map<K, V>", genericSignature.toString());
    }
}