package com.example.mapliketype;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.TypeBase;
import com.fasterxml.jackson.databind.type.MapLikeType;

public class MapLikeTypeTest {

    // Basic test cases

    @Test
    public void testMapLikeTypeConstructor() {
        JavaType keyType = new JavaType(String.class, null);
        JavaType valueType = new JavaType(Integer.class, null);
        MapLikeType mapLikeType = new MapLikeType(HashMap.class, null, null, null,
                keyType, valueType);
        assertNotNull(mapLikeType);
    }

    @Test
    public void testUpgradeFrom() {
        JavaType baseType = new JavaType(HashMap.class, null);
        JavaType keyType = new JavaType(String.class, null);
        JavaType valueType = new JavaType(Integer.class, null);
        MapLikeType mapLikeType = MapLikeType.upgradeFrom(baseType, keyType, valueType);
        assertNotNull(mapLikeType);
    }

    @Test
    public void testConstruct() {
        Class<?> rawType = HashMap.class;
        JavaType keyType = new JavaType(String.class, null);
        JavaType valueType = new JavaType(Integer.class, null);
        TypeBindings bindings = TypeBindings.emptyBindings();
        MapLikeType mapLikeType = MapLikeType.construct(rawType, keyType, valueType);
        assertNotNull(mapLikeType);
    }

    @Test
    public void testNarrow() {
        Class<?> subclass = HashMap.class;
        JavaType keyType = new JavaType(String.class, null);
        JavaType valueType = new JavaType(Integer.class, null);
        MapLikeType baseType = new MapLikeType(subclass, null, null, null, keyType,
                valueType);
        MapLikeType narrowType = baseType.narrow(subclass);
        assertNotNull(narrowType);
        assertEquals(baseType.getClass(), narrowType.getClass());
    }

    @Test
    public void testIsContainerType() {
        JavaType mapLikeType = new JavaType(HashMap.class, null);
        assertTrue(mapLikeType.isContainerType());
    }

    @Test
    public void testIsMapLikeType() {
        JavaType mapLikeType = new JavaType(HashMap.class, null);
        assertTrue(mapLikeType.isMapLikeType());
    }

    @Test
    public void testGetKeyType() {
        JavaType mapLikeType = new JavaType(HashMap.class, null);
        JavaType keyType = mapLikeType.getKeyType();
        assertEquals(String.class, keyType.getRawClass());
    }

    @Test
    public void testGetContentType() {
        JavaType mapLikeType = new JavaType(HashMap.class, null);
        JavaType valueType = mapLikeType.getContentType();
        assertEquals(Integer.class, valueType.getRawClass());
    }

    @Test
    public void testGetContentValueHandler() {
        JavaType mapLikeType = new JavaType(HashMap.class, null);
        Object valueHandler = mapLikeType.getContentValueHandler();
        assertNull(valueHandler);
    }

    @Test
    public void testGetContentTypeHandler() {
        JavaType mapLikeType = new JavaType(HashMap.class, null);
        Object typeHandler = mapLikeType.getContentTypeHandler();
        assertNull(typeHandler);
    }

    @Test
    public void testHasHandlers() {
        JavaType mapLikeType = new JavaType(HashMap.class, null);
        assertFalse(mapLikeType.hasHandlers());
    }

    @Test
    public void testBuildCanonicalName() {
        Class<?> rawType = HashMap.class;
        JavaType keyType = new JavaType(String.class, null);
        JavaType valueType = new JavaType(Integer.class, null);
        MapLikeType mapLikeType = new MapLikeType(rawType, null, null, null, keyType,
                valueType);
        String canonicalName = mapLikeType.buildCanonicalName();
        String expected = "[map-like type; class java.util.HashMap, java.lang.String -> java.lang.Integer]";
        assertEquals(expected, canonicalName);
    }

    // Edge cases and exception handling test cases

    @Test(expected = IllegalArgumentException.class)
    public void testUpgradeFromInvalidBaseType() {
        JavaType baseType = new JavaType(String.class, null);
        JavaType keyType = new JavaType(String.class, null);
        JavaType valueType = new JavaType(Integer.class, null);
        MapLikeType.upgradeFrom(baseType, keyType, valueType);
    }

    @Test
    public void testWithKeyType() {
        JavaType mapLikeType = new JavaType(HashMap.class, null);
        JavaType newKeyType = new JavaType(Integer.class, null);
        MapLikeType newMapLikeType = mapLikeType.withKeyType(newKeyType);
        JavaType newKeyType1 = newMapLikeType.getKeyType();
        assertEquals(Integer.class, newKeyType1.getRawClass());
        assertEquals(mapLikeType, newMapLikeType.baseType());
    }

    @Test
    public void testWithContentType() {
        JavaType mapLikeType = new JavaType(HashMap.class, null);
        JavaType newContentType = new JavaType(Double.class, null);
        MapLikeType newMapLikeType = mapLikeType.withContentType(newContentType);
        JavaType newContentType1 = newMapLikeType.getContentType();
        assertEquals(Double.class, newContentType1.getRawClass());
        assertEquals(mapLikeType, newMapLikeType.baseType());
    }

    @Test
    public void testWithTypeHandler() {
        JavaType mapLikeType = new JavaType(HashMap.class, null);
        Object handler = new Object();
        MapLikeType newMapLikeType = mapLikeType.withTypeHandler(handler);
        Object handler1 = newMapLikeType.getTypeHandler();
        assertEquals(handler, handler1);
        assertEquals(mapLikeType, newMapLikeType.baseType());
    }

    @Test
    public void testWithContentTypeHandler() {
        JavaType mapLikeType = new JavaType(HashMap.class, null);
        Object handler = new Object();
        JavaType newContentType = new JavaType(Double.class, null);
        MapLikeType newMapLikeType = mapLikeType.withContentTypeHandler(handler);
        Object handler1 = newMapLikeType.getContentTypeHandler();
        assertEquals(handler, handler1);
        JavaType newContentType1 = newMapLikeType.getContentType();
        assertEquals(Double.class, newContentType1.getRawClass());
        assertEquals(mapLikeType, newMapLikeType.baseType());
    }

    @Test
    public void testWithValueHandler() {
        JavaType mapLikeType = new JavaType(HashMap.class, null);
        Object handler = new Object();
        MapLikeType newMapLikeType = mapLikeType.withValueHandler(handler);
        Object handler1 = newMapLikeType.getValueHandler();
        assertEquals(handler, handler1);
        assertEquals(mapLikeType, newMapLikeType.baseType());
    }

    @Test
    public void testWithContentValueHandler() {
        JavaType mapLikeType = new JavaType(HashMap.class, null);
        Object handler = new Object();
        JavaType newContentType = new JavaType(Double.class, null);
        MapLikeType newMapLikeType = mapLikeType.withContentValueHandler(handler);
        Object handler1 = newMapLikeType.getContentValueHandler();
        assertEquals(handler, handler1);
        JavaType newContentType1 = newMapLikeType.getContentType();
        assertEquals(Double.class, newContentType1.getRawClass());
        assertEquals(mapLikeType, newMapLikeType.baseType());
    }

    @Test
    public void testWithKeyTypeHandler() {
        JavaType mapLikeType = new JavaType(HashMap.class, null);
        Object handler = new Object();
        MapLikeType newMapLikeType = mapLikeType.withKeyTypeHandler(handler);
        Object handler1 = newMapLikeType.getKeyTypeHandler();
        assertEquals(handler, handler1);
        assertEquals(mapLikeType, newMapLikeType.baseType());
    }

    @Test
    public void testWithKeyValueHandler() {
        JavaType mapLikeType = new JavaType(HashMap.class, null);
        Object handler = new Object();
        MapLikeType newMapLikeType = mapLikeType.withKeyValueHandler(handler);
        Object handler1 = newMapLikeType.getKeyValueHandler();
        assertEquals(handler, handler1);
        assertEquals(mapLikeType, newMapLikeType.baseType());
    }

    @Test
    public void testIsTrueMapType() {
        JavaType mapLikeType = new JavaType(HashMap.class, null);
        assertTrue(mapLikeType.isTrueMapType());
    }

    @Test
    public void testNotTrueMapType() {
        JavaType notMapLikeType = new JavaType(String.class, null);
        assertFalse(notMapLikeType.isTrueMapType());
    }
}