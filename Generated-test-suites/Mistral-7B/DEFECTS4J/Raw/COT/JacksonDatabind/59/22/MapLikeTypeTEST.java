package com.example.mapliketype;

import static org.junit.Assert.*;

import java.util.Arrays;

import org.junit.Test;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.MapLikeType;

public class MapLikeTypeTest {

    // Test constructor with MapType, TypeBindings, superClass, superInts, keyT, valueT
    @Test
    public void testConstructor1() {
        JavaType keyType = new SimpleType("java.lang.String");
        JavaType valueType = new SimpleType("java.lang.Integer");
        MapLikeType mapLikeType = new MapLikeType(Map.class, null, null, null, keyType, valueType);
        assertEquals(Map.class, mapLikeType.getRawType());
        assertEquals(keyType, mapLikeType.getKeyType());
        assertEquals(valueType, mapLikeType.getValueType());
    }

    // Test constructor with TypeBase, keyT, valueT
    @Test
    public void testConstructor2() {
        JavaType keyType = new SimpleType("java.lang.String");
        JavaType valueType = new SimpleType("java.lang.Integer");
        TypeBase base = new SimpleType("com.example.MyMap");
        MapLikeType mapLikeType = new MapLikeType((TypeBase) base, keyType, valueType);
        assertEquals(base.getRawClass(), mapLikeType.getRawType());
        assertEquals(keyType, mapLikeType.getKeyType());
        assertEquals(valueType, mapLikeType.getValueType());
    }

    // Test upgradeFrom method
    @Test
    public void testUpgradeFrom() {
        JavaType baseType = new SimpleType("com.example.MyList");
        JavaType keyType = new SimpleType("java.lang.String");
        JavaType valueType = new SimpleType("java.lang.Integer");
        MapLikeType mapLikeType = MapLikeType.upgradeFrom(baseType, keyType, valueType);
        assertEquals(Map.class, mapLikeType.getRawType());
        assertEquals(keyType, mapLikeType.getKeyType());
        assertEquals(valueType, mapLikeType.getValueType());
    }

    // Test isTrueMapType method
    @Test
    public void testIsTrueMapType() {
        JavaType mapLikeType1 = new SimpleType("java.util.HashMap");
        assertTrue(mapLikeType1.isTrueMapType());
        JavaType listType = new SimpleType("java.util.ArrayList");
        assertFalse(listType.isTrueMapType());
    }

    // Test getErasedSignature and getGenericSignature methods
    @Test
    public void testGetErasedSignatureAndGetGenericSignature() {
        JavaType keyType = new SimpleType("java.lang.String");
        JavaType valueType = new SimpleType("java.lang.Integer");
        String expectedErasedSignature = "[map-like type; class java.util.Map, java.lang.String -> java.lang.Integer]";
        String expectedGenericSignature = "[map-like type; class com.example.MyMap<K, V>, K extends java.lang.Object, V extends java.lang.Object >]";
        MapLikeType mapLikeType = new MapLikeType(Map.class, null, null, null, keyType, valueType);
        assertEquals(expectedErasedSignature, mapLikeType.getErasedSignature(new StringBuilder()).toString());
        assertEquals(expectedGenericSignature, mapLikeType.getGenericSignature(new StringBuilder()).toString());
    }

    // Test isContainerType and isMapLikeType methods
    @Test
    public void testIsContainerTypeAndIsMapLikeType() {
        JavaType mapLikeType = new SimpleType("java.util.HashMap");
        assertTrue(mapLikeType.isContainerType());
        assertTrue(mapLikeType.isMapLikeType());
        JavaType listType = new SimpleType("java.util.ArrayList");
        assertTrue(listType.isContainerType());
        assertFalse(listType.isMapLikeType());
    }

    // Test getKeyType, getContentType, getContentValueHandler, getContentTypeHandler methods
    @Test
    public void testGetKeyTypeGetContentTypeGetContentValueHandlerGetContentTypeHandler() {
        JavaType keyType = new SimpleType("java.lang.String");
        JavaType valueType = new SimpleType("java.lang.Integer");
        JavaType mapLikeType = new MapLikeType(Map.class, null, null, null, keyType, valueType);
        assertEquals(keyType, mapLikeType.getKeyType());
        assertEquals(valueType, mapLikeType.getValueType());
        assertNull(mapLikeType.getContentValueHandler());
        assertNull(mapLikeType.getContentTypeHandler());
    }

    // Test withKeyType, withContentType, withTypeHandler, withContentTypeHandler, withValueHandler, withContentValueHandler methods
    @Test
    public void testWithKeyTypeWithContentTypeWithTypeHandlerWithContentTypeHandlerWithValueHandlerWithContentValueHandler() {
        JavaType keyType = new SimpleType("java.lang.String");
        JavaType valueType = new SimpleType("java.lang.Integer");
        JavaType mapLikeType = new MapLikeType(Map.class, null, null, null, keyType, valueType);
        JavaType newKeyType = new SimpleType("java.lang.Integer");
        JavaType newValueType = new SimpleType("java.lang.Long");
        JavaType newMapLikeType = mapLikeType.withKeyType(newKeyType).withContentType(newValueType);
        assertEquals(newKeyType, newMapLikeType.getKeyType());
        assertEquals(newValueType, newMapLikeType.getValueType());
        assertNull(newMapLikeType.getContentValueHandler());
        assertNull(newMapLikeType.getContentTypeHandler());
    }

    // Test withKeyTypeHandler and withKeyValueHandler methods
    @Test
    public void testWithKeyTypeHandlerAndWithKeyValueHandler() {
        JavaType keyType = new SimpleType("java.lang.String");
        JavaType valueType = new SimpleType("java.lang.Integer");
        JavaType mapLikeType = new MapLikeType(Map.class, null, null, null, keyType, valueType);
        Object keyHandler = new Object() {};
        JavaType newMapLikeType = mapLikeType.withKeyTypeHandler(keyHandler);
        assertEquals(keyHandler, newMapLikeType.getKeyTypeHandler());
        assertNull(newMapLikeType.getValueHandler());
        assertNull(newMapLikeType.getContentValueHandler());
        Object keyValueHandler = new Object() {};
        JavaType newNewMapLikeType = newMapLikeType.withKeyValueHandler(keyValueHandler);
        assertEquals(keyValueHandler, newNewMapLikeType.getKeyValueHandler());
        assertNull(newNewMapLikeType.getValueHandler());
        assertNull(newNewMapLikeType.getContentValueHandler());
    }

    // Test hasHandlers method
    @Test
    public void testHasHandlers() {
        JavaType mapLikeType = new MapLikeType(Map.class, null, null, null, null, null);
        assertFalse(mapLikeType.hasHandlers());
        JavaType mapLikeTypeWithKeyHandler = mapLikeType.withKeyTypeHandler(new Object() {});
        assertTrue(mapLikeTypeWithKeyHandler.hasHandlers());
        JavaType mapLikeTypeWithValueHandler = mapLikeTypeWithKeyHandler.withValueHandler(new Object() {});
        assertTrue(mapLikeTypeWithValueHandler.hasHandlers());
        JavaType mapLikeTypeWithKeyValueHandler = mapLikeTypeWithValueHandler.withKeyValueHandler(new Object() {});
        assertTrue(mapLikeTypeWithKeyValueHandler.hasHandlers());
        JavaType mapLikeTypeWithKeyTypeHandler = mapLikeTypeWithKeyHandler.withKeyTypeHandler(new Object() {});
        assertTrue(mapLikeTypeWithKeyTypeHandler.hasHandlers());
    }

    // Test refine method
    @Test
    public void testRefine() {
        JavaType keyType = new SimpleType("java.lang.String");
        JavaType valueType = new SimpleType("java.lang.Integer");
        JavaType mapLikeType = new MapLikeType(Map.class, null, null, null, keyType, valueType);
        Class<?> rawType = Class.forName("com.example.MyMap");
        TypeBindings bindings = TypeBindings.create(rawType, keyType, valueType);
        JavaType newMapLikeType = mapLikeType.refine(rawType, bindings, null, null);
        assertEquals(rawType, newMapLikeType.getRawType());
        assertEquals(keyType, newMapLikeType.getKeyType());
        assertEquals(valueType, newMapLikeType.getValueType());
    }

    // Test equals method
    @Test
    public void testEquals() {
        JavaType keyType = new SimpleType("java.lang.String");
        JavaType valueType = new SimpleType("java.lang.Integer");
        JavaType mapLikeType1 = new MapLikeType(Map.class, null, null, null, keyType, valueType);
        JavaType mapLikeType2 = new MapLikeType(Map.class, null, null, null, keyType, valueType);
        JavaType mapLikeType3 = new MapLikeType(List.class, null, null, null, keyType, valueType);
        assertTrue(mapLikeType1.equals(mapLikeType1));
        assertTrue(mapLikeType1.equals(mapLikeType2));
        assertFalse(mapLikeType1.equals(mapLikeType3));
    }

    // Test toString method
    @Test
    public void testToString() {
        JavaType keyType = new SimpleType("java.lang.String");
        JavaType valueType = new SimpleType("java.lang.Integer");
        JavaType mapLikeType = new MapLikeType(Map.class, null, null, null, keyType, valueType);
        String expectedToString = "[map-like type; class java.util.Map, java.lang.String -> java.lang.Integer]";
        assertEquals(expectedToString, mapLikeType.toString());
    }
}