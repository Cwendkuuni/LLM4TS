package com.fasterxml.jackson.databind.type.test;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.MapLikeType;
import com.fasterxml.jackson.databind.type.MapType;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@RunWith(JUnit4.class)
public class MapTypeTest {

    @Test
    public void testMapTypeConstructor() {
        Class<Map<String, Integer>> mapType = Map.class;
        TypeBindings bindings = TypeBindings.getDefaultInstance();
        JavaType superClass = _getClass("java.util.Map");
        JavaType[] superInts = new JavaType[0];
        JavaType keyT = _getType("java.lang.String");
        JavaType valueT = _getType("java.lang.Integer");
        MapType mapTypeInstance = MapType.construct(mapType, bindings, superClass, superInts, keyT, valueT);
        assertNotNull(mapTypeInstance);
    }

    @Test
    public void testDeprecatedMapTypeConstruct() {
        Class<Map<String, Integer>> mapType = Map.class;
        JavaType keyT = _getType("java.lang.String");
        JavaType valueT = _getType("java.lang.Integer");
        MapType mapTypeInstance = MapType.construct(mapType, keyT, valueT);
        assertNotNull(mapTypeInstance);
    }

    @Test
    public void testNarrowMethod() {
        Class<Map<String, Integer>> mapType = Map.class;
        TypeBindings bindings = TypeBindings.getDefaultInstance();
        JavaType superClass = _getClass("java.util.Map");
        JavaType[] superInts = new JavaType[0];
        JavaType keyT = _getType("java.lang.String");
        JavaType valueT = _getType("java.lang.Integer");
        MapType mapTypeInstance = new MapType(mapType, bindings, superClass, superInts, keyT, valueT, null, null, false);
        MapType narrowedMapType = mapTypeInstance._narrow(Map.class);
        assertNotNull(narrowedMapType);
        assertEquals(mapTypeInstance, narrowedMapType);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIncorrectNarrowMethod() {
        Class<Map<String, Integer>> mapType = Map.class;
        TypeBindings bindings = TypeBindings.getDefaultInstance();
        JavaType superClass = _getClass("java.util.List");
        JavaType[] superInts = new JavaType[0];
        JavaType keyT = _getType("java.lang.String");
        JavaType valueT = _getType("java.lang.Integer");
        MapType mapTypeInstance = new MapType(mapType, bindings, superClass, superInts, keyT, valueT, null, null, false);
        mapTypeInstance._narrow(Map.class);
    }

    @Test
    public void testWithTypeHandlerMethod() {
        Class<Map<String, Integer>> mapType = Map.class;
        TypeBindings bindings = TypeBindings.getDefaultInstance();
        JavaType superClass = _getClass("java.util.Map");
        JavaType[] superInts = new JavaType[0];
        JavaType keyT = _getType("java.lang.String");
        JavaType valueT = _getType("java.lang.Integer");
        MapType mapTypeInstance = new MapType(mapType, bindings, superClass, superInts, keyT, valueT, null, null, false);
        Object handler = new Object();
        MapType withHandlerMapType = mapTypeInstance.withTypeHandler(handler);
        assertNotNull(withHandlerMapType);
        assertEquals(mapTypeInstance, withHandlerMapType);
    }

    @Test
    public void testWithContentTypeHandlerMethod() {
        Class<Map<String, Integer>> mapType = Map.class;
        TypeBindings bindings = TypeBindings.getDefaultInstance();
        JavaType superClass = _getClass("java.util.Map");
        JavaType[] superInts = new JavaType[0];
        JavaType keyT = _getType("java.lang.String");
        JavaType valueT = _getType("java.lang.Integer");
        JavaType contentTypeHandler = _getType("com.custom.ContentTypeHandler");
        MapType mapTypeInstance = new MapType(mapType, bindings, superClass, superInts, keyT, valueT, null, null, false);
        MapType withContentTypeHandlerMapType = mapTypeInstance.withContentTypeHandler(contentTypeHandler);
        assertNotNull(withContentTypeHandlerMapType);
        assertEquals(mapTypeInstance, withContentTypeHandlerMapType);
    }

    @Test
    public void testWithValueHandlerMethod() {
        Class<Map<String, Integer>> mapType = Map.class;
        TypeBindings bindings = TypeBindings.getDefaultInstance();
        JavaType superClass = _getClass("java.util.Map");
        JavaType[] superInts = new JavaType[0];
        JavaType keyT = _getType("java.lang.String");
        JavaType valueT = _getType("java.lang.Integer");
        MapType mapTypeInstance = new MapType(mapType, bindings, superClass, superInts, keyT, valueT, null, null, false);
        Object handler = new Object();
        MapType withHandlerMapType = mapTypeInstance.withValueHandler(handler);
        assertNotNull(withHandlerMapType);
        assertEquals(mapTypeInstance, withHandlerMapType);
    }

    @Test
    public void testWithContentValueHandlerMethod() {
        Class<Map<String, Integer>> mapType = Map.class;
        TypeBindings bindings = TypeBindings.getDefaultInstance();
        JavaType superClass = _getClass("java.util.Map");
        JavaType[] superInts = new JavaType[0];
        JavaType keyT = _getType("java.lang.String");
        JavaType valueT = _getType("java.lang.Integer");
        JavaType contentValueHandler = _getType("com.custom.ContentValueHandler");
        MapType mapTypeInstance = new MapType(mapType, bindings, superClass, superInts, keyT, valueT, null, null, false);
        MapType withContentValueHandlerMapType = mapTypeInstance.withContentValueHandler(contentValueHandler);
        assertNotNull(withContentValueHandlerMapType);
        assertEquals(mapTypeInstance, withContentValueHandlerMapType);
    }

    @Test
    public void testWithStaticTypingMethod() {
        Class<Map<String, Integer>> mapType = Map.class;
        TypeBindings bindings = TypeBindings.getDefaultInstance();
        JavaType superClass = _getClass("java.util.Map");
        JavaType[] superInts = new JavaType[0];
        JavaType keyT = _getType("java.lang.String");
        JavaType valueT = _getType("java.lang.Integer");
        MapType mapTypeInstance = new MapType(mapType, bindings, superClass, superInts, keyT, valueT, null, null, false);
        MapType withStaticTypingMapType = mapTypeInstance.withStaticTyping();
        assertNotNull(withStaticTypingMapType);
        assertEquals(mapTypeInstance, withStaticTypingMapType);
        assertTrue(withStaticTypingMapType._asStatic);
    }

    @Test
    public void testWithContentTypeMethod() {
        Class<Map<String, Integer>> mapType = Map.class;
        TypeBindings bindings = TypeBindings.getDefaultInstance();
        JavaType superClass = _getClass("java.util.Map");
        JavaType[] superInts = new JavaType[0];
        JavaType keyT = _getType("java.lang.String");
        JavaType valueT = _getType("java.lang.Integer");
        JavaType contentType = _getType("java.util.List");
        MapType mapTypeInstance = new MapType(mapType, bindings, superClass, superInts, keyT, valueT, null, null, false);
        MapType withContentTypeMapType = mapTypeInstance.withContentType(contentType);
        assertNotNull(withContentTypeMapType);
        assertEquals(mapTypeInstance, withContentTypeMapType);
    }

    @Test
    public void testWithKeyTypeMethod() {
        Class<Map<String, Integer>> mapType = Map.class;
        TypeBindings bindings = TypeBindings.getDefaultInstance();
        JavaType superClass = _getClass("java.util.Map");
        JavaType[] superInts = new JavaType[0];
        JavaType keyT = _getType("java.lang.String");
        JavaType valueT = _getType("java.lang.Integer");
        JavaType newKeyType = _getType("java.lang.Long");
        MapType mapTypeInstance = new MapType(mapType, bindings, superClass, superInts, keyT, valueT, null, null, false);
        MapType withNewKeyTypeMapType = mapTypeInstance.withKeyType(newKeyType);
        assertNotNull(withNewKeyTypeMapType);
        assertEquals(mapTypeInstance, withNewKeyTypeMapType);
    }

    @Test
    public void testRefineMethod() {
        Class<Map<String, Integer>> mapType = Map.class;
        TypeBindings bindings = TypeBindings.getDefaultInstance();
        JavaType superClass = _getClass("java.util.Map");
        JavaType[] superInts = new JavaType[0];
        JavaType keyT = _getType("java.lang.String");
        JavaType valueT = _getType("java.lang.Integer");
        MapType mapTypeInstance = new MapType(mapType, bindings, superClass, superInts, keyT, valueT, null, null, false);
        Class<Map<Long, Integer>> newMapType = Map.class;
        TypeBindings newBindings = TypeBindings.getDefaultInstance();
        JavaType newSuperClass = _getClass("java.util.Map");
        JavaType[] newSuperInts = new JavaType[0];
        JavaType newKeyType = _getType("java.lang.Long");
        JavaType newValueType = _getType("java.lang.Integer");
        MapType refinedMapType = mapTypeInstance.refine(newMapType.getClass(), newBindings, newSuperClass, newSuperInts);
        assertNotNull(refinedMapType);
        assertEquals(mapTypeInstance, refinedMapType);
    }

    @Test
    public void testWithKeyTypeHandlerMethod() {
        Class<Map<String, Integer>> mapType = Map.class;
        TypeBindings bindings = TypeBindings.getDefaultInstance();
        JavaType superClass = _getClass("java.util.Map");
        JavaType[] superInts = new JavaType[0];
        JavaType keyT = _getType("java.lang.String");
        JavaType valueT = _getType("java.lang.Integer");
        Object handler = new Object();
        MapType mapTypeInstance = new MapType(mapType, bindings, superClass, superInts, keyT, valueT, null, null, false);
        MapType withKeyTypeHandlerMapType = mapTypeInstance.withKeyTypeHandler(handler);
        assertNotNull(withKeyTypeHandlerMapType);
        assertEquals(mapTypeInstance, withKeyTypeHandlerMapType);
    }

    @Test
    public void testWithKeyValueHandlerMethod() {
        Class<Map<String, Integer>> mapType = Map.class;
        TypeBindings bindings = TypeBindings.getDefaultInstance();
        JavaType superClass = _getClass("java.util.Map");
        JavaType[] superInts = new JavaType[0];
        JavaType keyT = _getType("java.lang.String");
        JavaType valueT = _getType("java.lang.Integer");
        Object handler = new Object();
        MapType mapTypeInstance = new MapType(mapType, bindings, superClass, superInts, keyT, valueT, null, null, false);
        MapType withKeyValueHandlerMapType = mapTypeInstance.withKeyValueHandler(handler);
        assertNotNull(withKeyValueHandlerMapType);
        assertEquals(mapTypeInstance, withKeyValueHandlerMapType);
    }

    @Test
    public void testToStringMethod() {
        Class<Map<String, Integer>> mapType = Map.class;
        TypeBindings bindings = TypeBindings.getDefaultInstance();
        JavaType superClass = _getClass("java.util.Map");
        JavaType[] superInts = new JavaType[0];
        JavaType keyT = _getType("java.lang.String");
        JavaType valueT = _getType("java.lang.Integer");
        MapType mapTypeInstance = new MapType(mapType, bindings, superClass, superInts, keyT, valueT, null, null, false);
        String toString = mapTypeInstance.toString();
        String expected = "[map type; class java.util.Map<java.lang.String, java.lang.Integer>, java.lang.String -> java.lang.Integer]";
        assertEquals(expected, toString);
    }

    private JavaType _getType(String className) {
        try {
            return Class.forName(className).asSubTypeOf(JavaType.class);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("Error loading class: " + className, e);
        }
    }

    private Class<?> _getClass(String className) {
        try {
            return Class.forName(className);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("Error loading class: " + className, e);
        }
    }
}