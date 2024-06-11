package com.fasterxml.jackson.databind.type.test;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.MapType;
import org.junit.Test;
import static org.junit.Assert.*;

public class MapTypeTest {

    @Test
    public void testConstruct() {
        Class<Map<String, Integer>> mapType = Map.class;
        TypeBindings bindings = TypeBindings.empty();
        JavaType superClass = Object.class;
        JavaType[] superInts = new JavaType[0];
        JavaType keyType = String.class;
        JavaType valueType = Integer.class;

        MapType mapTypeInstance = MapType.construct(mapType, bindings, superClass, superInts, keyType, valueType);

        assertEquals(mapType, mapTypeInstance.getRawClass());
        assertEquals(keyType, mapTypeInstance.getKeyType());
        assertEquals(valueType, mapTypeInstance.getValueType());
    }

    @Test
    public void testConstructDeprecated() {
        Class<Map<String, Integer>> mapType = Map.class;
        JavaType keyType = String.class;
        JavaType valueType = Integer.class;

        try {
            MapType mapTypeInstance = MapType.construct(mapType, keyType, valueType);
            fail("The construct method with only keyType and valueType should be deprecated.");
        } catch (Exception e) {
            // Expected
        }

        MapType mapTypeInstance = MapType.construct(mapType, null, _bogusSuperClass(mapType), null, keyType, valueType);

        assertEquals(mapType, mapTypeInstance.getRawClass());
        assertEquals(keyType, mapTypeInstance.getKeyType());
        assertEquals(valueType, mapTypeInstance.getValueType());
    }

    @Test(expected = IllegalStateException.class)
    public void testNarrowDeprecated() {
        MapType mapType = new MapType(Map.class, null, _bogusSuperClass(Map.class), null, String.class, Integer.class);
        mapType.narrow(Map.class);
    }

    @Test
    public void testWithTypeHandler() {
        Class<Map<String, Integer>> mapType = Map.class;
        TypeBindings bindings = TypeBindings.empty();
        JavaType superClass = Object.class;
        JavaType[] superInts = new JavaType[0];
        JavaType keyType = String.class;
        JavaType valueType = Integer.class;
        Object valueHandler = new Object();

        MapType mapTypeInstance = new MapType(mapType, bindings, superClass, superInts, keyType, valueType);
        MapType mapTypeWithHandler = mapTypeInstance.withTypeHandler(valueHandler);

        assertNotSame(mapTypeInstance, mapTypeWithHandler);
        assertEquals(valueHandler, mapTypeWithHandler.getTypeHandler());
    }

    @Test
    public void testWithContentTypeHandler() {
        Class<Map<String, Integer>> mapType = Map.class;
        TypeBindings bindings = TypeBindings.empty();
        JavaType superClass = Object.class;
        JavaType[] superInts = new JavaType[0];
        JavaType keyType = String.class;
        JavaType valueType = new JavaType(Integer[].class);
        Object valueHandler = new Object();
        Object typeHandler = new Object();

        MapType mapTypeInstance = new MapType(mapType, bindings, superClass, superInts, keyType, valueType, valueHandler, typeHandler, false);
        MapType mapTypeWithContentTypeHandler = mapTypeInstance.withContentTypeHandler(valueHandler);

        assertNotSame(mapTypeInstance, mapTypeWithContentTypeHandler);
        assertEquals(valueHandler, mapTypeWithContentTypeHandler.getContentTypeHandler());
    }

    @Test
    public void testWithValueHandler() {
        Class<Map<String, Integer>> mapType = Map.class;
        TypeBindings bindings = TypeBindings.empty();
        JavaType superClass = Object.class;
        JavaType[] superInts = new JavaType[0];
        JavaType keyType = String.class;
        JavaType valueType = Integer.class;
        Object valueHandler = new Object();

        MapType mapTypeInstance = new MapType(mapType, bindings, superClass, superInts, keyType, valueType);
        MapType mapTypeWithValueHandler = mapTypeInstance.withValueHandler(valueHandler);

        assertNotSame(mapTypeInstance, mapTypeWithValueHandler);
        assertEquals(valueHandler, mapTypeWithValueHandler.getValueHandler());
    }

    @Test
    public void testWithContentValueHandler() {
        Class<Map<String, Integer>> mapType = Map.class;
        TypeBindings bindings = TypeBindings.empty();
        JavaType superClass = Object.class;
        JavaType[] superInts = new JavaType[0];
        JavaType keyType = String.class;
        JavaType valueType = new JavaType(Integer[].class);
        Object valueHandler = new Object();
        Object contentValueHandler = new Object();

        MapType mapTypeInstance = new MapType(mapType, bindings, superClass, superInts, keyType, valueType, valueHandler, null, false);
        MapType mapTypeWithContentValueHandler = mapTypeInstance.withContentValueHandler(contentValueHandler);

        assertNotSame(mapTypeInstance, mapTypeWithContentValueHandler);
        assertEquals(contentValueHandler, mapTypeWithContentValueHandler.getContentValueHandler());
    }

    @Test
    public void testWithStaticTyping() {
        Class<Map<String, Integer>> mapType = Map.class;
        TypeBindings bindings = TypeBindings.empty();
        JavaType superClass = Object.class;
        JavaType[] superInts = new JavaType[0];
        JavaType keyType = String.class;
        JavaType valueType = Integer.class;

        MapType mapTypeInstance = new MapType(mapType, bindings, superClass, superInts, keyType, valueType);
        MapType mapTypeWithStaticTyping = mapTypeInstance.withStaticTyping();

        assertNotSame(mapTypeInstance, mapTypeWithStaticTyping);
        assertTrue(mapTypeWithStaticTyping.isStatic());
    }

    @Test
    public void testWithContentType() {
        Class<Map<String, Integer>> mapType = Map.class;
        TypeBindings bindings = TypeBindings.empty();
        JavaType superClass = Object.class;
        JavaType[] superInts = new JavaType[0];
        JavaType keyType = String.class;
        JavaType valueType = Integer.class;
        JavaType newValueType = String.class;

        MapType mapTypeInstance = new MapType(mapType, bindings, superClass, superInts, keyType, valueType);
        MapType mapTypeWithContentType = mapTypeInstance.withContentType(newValueType);

        assertNotSame(mapTypeInstance, mapTypeWithContentType);
        assertEquals(newValueType, mapTypeWithContentType.getValueType());
    }

    @Test
    public void testWithKeyType() {
        Class<Map<String, Integer>> mapType = Map.class;
        TypeBindings bindings = TypeBindings.empty();
        JavaType superClass = Object.class;
        JavaType[] superInts = new JavaType[0];
        JavaType oldKeyType = String.class;
        JavaType newKeyType = Integer.class;

        MapType mapTypeInstance = new MapType(mapType, bindings, superClass, superInts, oldKeyType, Integer.class);
        MapType mapTypeWithNewKeyType = mapTypeInstance.withKeyType(newKeyType);

        assertNotSame(mapTypeInstance, mapTypeWithNewKeyType);
        assertEquals(newKeyType, mapTypeWithNewKeyType.getKeyType());
    }

    @Test
    public void testRefine() {
        Class<Map<String, Integer>> mapType = Map.class;
        TypeBindings bindings = TypeBindings.empty();
        JavaType superClass = Object.class;
        JavaType[] superInts = new JavaType[0];
        JavaType keyType = String.class;
        JavaType valueType = Integer.class;
        Class<Map<Integer, String>> newMapType = Map.class;

        MapType mapTypeInstance = new MapType(mapType, bindings, superClass, superInts, keyType, valueType);
        MapType mapTypeRefined = mapTypeInstance.refine(newMapType.getClass(), bindings, superClass, superInts);

        assertNotSame(mapTypeInstance, mapTypeRefined);
        assertEquals(newMapType.getClass(), mapTypeRefined.getRawClass());
    }

    @Test
    public void testWithKeyTypeHandler() {
        Class<Map<String, Integer>> mapType = Map.class;
        TypeBindings bindings = TypeBindings.empty();
        JavaType superClass = Object.class;
        JavaType[] superInts = new JavaType[0];
        JavaType keyType = String.class;
        JavaType valueType = Integer.class;
        Object keyTypeHandler = new Object();

        MapType mapTypeInstance = new MapType(mapType, bindings, superClass, superInts, keyType, valueType);
        MapType mapTypeWithKeyTypeHandler = mapTypeInstance.withKeyTypeHandler(keyTypeHandler);

        assertNotSame(mapTypeInstance, mapTypeWithKeyTypeHandler);
        assertEquals(keyTypeHandler, mapTypeWithKeyTypeHandler.getKeyTypeHandler());
    }

    @Test
    public void testWithKeyValueHandler() {
        Class<Map<String, Integer>> mapType = Map.class;
        TypeBindings bindings = TypeBindings.empty();
        JavaType superClass = Object.class;
        JavaType[] superInts = new JavaType[0];
        JavaType keyType = String.class;
        JavaType valueType = Integer.class;
        Object keyValueHandler = new Object();

        MapType mapTypeInstance = new MapType(mapType, bindings, superClass, superInts, keyType, valueType);
        MapType mapTypeWithKeyValueHandler = mapTypeInstance.withKeyValueHandler(keyValueHandler);

        assertNotSame(mapTypeInstance, mapTypeWithKeyValueHandler);
        assertEquals(keyValueHandler, mapTypeWithKeyValueHandler.getKeyValueHandler());
    }

    private static Class<?> _bogusSuperClass(Class<?> clazz) {
        // This is a placeholder, you should use a proper implementation
        // to create a JavaType representing the bogus superclass.
        return Object.class;
    }
}