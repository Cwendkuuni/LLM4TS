package com.fasterxml.jackson.databind.type;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class MapTypeTest {

    @Test
    public void testConstruct() {
        Class<Map<String, Integer>> mapType = Map.class;
        TypeBindings bindings = TypeBindings.empty();
        JavaType superClass = Object.class;
        JavaType[] superInts = new JavaType[0];
        JavaType keyT = String.class;
        JavaType valueT = Integer.class;
        MapType mapTypeInstance = MapType.construct(mapType, bindings, superClass, superInts, keyT, valueT);
        assertEquals(mapType, mapTypeInstance.getRawClass());
        assertEquals(bindings, mapTypeInstance._bindings);
        assertEquals(superClass, mapTypeInstance._superClass);
        assertNull(mapTypeInstance._superInterfaces);
        assertEquals(keyT, mapTypeInstance._keyType);
        assertEquals(valueT, mapTypeInstance._valueType);
        assertNull(mapTypeInstance._valueHandler);
        assertNull(mapTypeInstance._typeHandler);
        assertFalse(mapTypeInstance._asStatic);
    }

    @Test
    public void testConstructWithTypeHandler() {
        Class<Map<String, Integer>> mapType = Map.class;
        TypeBindings bindings = TypeBindings.empty();
        JavaType superClass = Object.class;
        JavaType[] superInts = new JavaType[0];
        JavaType keyT = String.class;
        JavaType valueT = Integer.class;
        Object valueHandler = new Object() {};
        MapType mapTypeInstance = MapType.construct(mapType, bindings, superClass, superInts, keyT, valueT, valueHandler);
        assertEquals(mapType, mapTypeInstance.getRawClass());
        assertEquals(bindings, mapTypeInstance._bindings);
        assertEquals(superClass, mapTypeInstance._superClass);
        assertNull(mapTypeInstance._superInterfaces);
        assertEquals(keyT, mapTypeInstance._keyType);
        assertEquals(valueT, mapTypeInstance._valueType);
        assertEquals(valueHandler, mapTypeInstance._valueHandler);
        assertNull(mapTypeInstance._typeHandler);
        assertFalse(mapTypeInstance._asStatic);
    }

    @Test
    public void testRefine() {
        Class<Map<String, Integer>> mapType = Map.class;
        TypeBindings bindings = TypeBindings.empty();
        JavaType superClass = Object.class;
        JavaType[] superInts = new JavaType[0];
        JavaType newKeyType = String.class;
        JavaType newValueType = Long.class;
        MapType mapTypeInstance = new MapType(mapType, bindings, superClass, superInts, String.class, Integer.class);
        JavaType refinedType = mapTypeInstance.refine(Map.class, bindings, Object.class, new JavaType[0]);
        assertEquals(Map.class, refinedType.getRawClass());
        assertEquals(bindings, refinedType._bindings);
        assertEquals(Object.class, refinedType._superClass);
        assertNull(refinedType._superInterfaces);
        assertEquals(newKeyType, refinedType._keyType);
        assertEquals(newValueType, refinedType._valueType);
        assertNull(refinedType._valueHandler);
        assertNull(refinedType._typeHandler);
        assertFalse(refinedType._asStatic);
    }

    @Test
    public void testWithKeyTypeHandler() {
        Class<Map<String, Integer>> mapType = Map.class;
        TypeBindings bindings = TypeBindings.empty();
        JavaType superClass = Object.class;
        JavaType[] superInts = new JavaType[0];
        JavaType keyT = String.class;
        JavaType valueT = Integer.class;
        Object keyTypeHandler = new Object() {};
        MapType mapTypeInstance = new MapType(mapType, bindings, superClass, superInts, keyT, valueT);
        MapType mapTypeWithKeyTypeHandler = mapTypeInstance.withKeyTypeHandler(keyTypeHandler);
        assertEquals(mapType, mapTypeWithKeyTypeHandler.getRawClass());
        assertEquals(bindings, mapTypeWithKeyTypeHandler._bindings);
        assertEquals(superClass, mapTypeWithKeyTypeHandler._superClass);
        assertNull(mapTypeWithKeyTypeHandler._superInterfaces);
        assertEquals(keyT, mapTypeWithKeyTypeHandler._keyType);
        assertEquals(valueT, mapTypeWithKeyTypeHandler._valueType);
        assertEquals(keyTypeHandler, mapTypeWithKeyTypeHandler._keyTypeHandler);
        assertNull(mapTypeWithKeyTypeHandler._valueHandler);
        assertNull(mapTypeWithKeyTypeHandler._typeHandler);
        assertFalse(mapTypeWithKeyTypeHandler._asStatic);
    }

    @Test
    public void testWithKeyValueHandler() {
        Class<Map<String, Integer>> mapType = Map.class;
        TypeBindings bindings = TypeBindings.empty();
        JavaType superClass = Object.class;
        JavaType[] superInts = new JavaType[0];
        JavaType keyT = String.class;
        JavaType valueT = Integer.class;
        Object keyValueHandler = new Object() {};
        MapType mapTypeInstance = new MapType(mapType, bindings, superClass, superInts, keyT, valueT);
        MapType mapTypeWithKeyValueHandler = mapTypeInstance.withKeyValueHandler(keyValueHandler);
        assertEquals(mapType, mapTypeWithKeyValueHandler.getRawClass());
        assertEquals(bindings, mapTypeWithKeyValueHandler._bindings);
        assertEquals(superClass, mapTypeWithKeyValueHandler._superClass);
        assertNull(mapTypeWithKeyValueHandler._superInterfaces);
        assertEquals(keyT, mapTypeWithKeyValueHandler._keyType);
        assertEquals(valueT, mapTypeWithKeyValueHandler._valueType);
        assertEquals(keyValueHandler, mapTypeWithKeyValueHandler._keyValueHandler);
        assertNull(mapTypeWithKeyValueHandler._valueHandler);
        assertNull(mapTypeWithKeyValueHandler._typeHandler);
        assertFalse(mapTypeWithKeyValueHandler._asStatic);
    }

    @Test
    public void testToString() {
        Class<Map<String, Integer>> mapType = Map.class;
        TypeBindings bindings = TypeBindings.empty();
        JavaType superClass = Object.class;
        JavaType[] superInts = new JavaType[0];
        JavaType keyT = String.class;
        JavaType valueT = Integer.class;
        MapType mapTypeInstance = new MapType(mapType, bindings, superClass, superInts, keyT, valueT);
        String toString = mapTypeInstance.toString();
        String expectedToString = "[map type; class " + mapType.getName() + ", " + keyT.getTypeName() + " -> " + valueT.getTypeName() + "]";
        assertEquals(expectedToString, toString);
    }
}