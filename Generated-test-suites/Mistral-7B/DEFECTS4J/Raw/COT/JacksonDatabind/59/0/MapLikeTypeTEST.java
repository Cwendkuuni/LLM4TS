package com.example.mapliketype; // replace with your package name

import static org.junit.Assert.*;

import org.junit.Test;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.MapLikeType;

public class MapLikeTypeTest {

    // Basic test cases for constructors

    @Test
    public void testConstructorsWithArguments() {
        JavaType keyType = new JavaType(String.class);
        JavaType valueType = new JavaType(Integer.class);
        MapLikeType mapLikeType = new MapLikeType(Map.class, TypeBindings.emptyBindings(), null, null, keyType, valueType);
        assertNotNull(mapLikeType);
    }

    @Test
    public void testConstructorsWithBaseType() {
        JavaType keyType = new JavaType(String.class);
        JavaType valueType = new JavaType(Integer.class);
        MapLikeType mapLikeType = MapLikeType.upgradeFrom(new JavaType(HashMap.class), keyType, valueType);
        assertNotNull(mapLikeType);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructorsWithInvalidBaseType() {
        JavaType keyType = new JavaType(String.class);
        JavaType valueType = new JavaType(Integer.class);
        MapLikeType.upgradeFrom(new JavaType(List.class), keyType, valueType);
    }

    // Test methods from MapLikeType interface

    @Test
    public void testIsContainerType() {
        JavaType keyType = new JavaType(String.class);
        JavaType valueType = new JavaType(Integer.class);
        MapLikeType mapLikeType = new MapLikeType(Map.class, TypeBindings.emptyBindings(), null, null, keyType, valueType);
        assertTrue(mapLikeType.isContainerType());
    }

    @Test
    public void testIsMapLikeType() {
        JavaType keyType = new JavaType(String.class);
        JavaType valueType = new JavaType(Integer.class);
        MapLikeType mapLikeType = new MapLikeType(Map.class, TypeBindings.emptyBindings(), null, null, keyType, valueType);
        assertTrue(mapLikeType.isMapLikeType());
    }

    @Test
    public void testGetKeyType() {
        JavaType keyType = new JavaType(String.class);
        JavaType valueType = new JavaType(Integer.class);
        MapLikeType mapLikeType = new MapLikeType(Map.class, TypeBindings.emptyBindings(), null, null, keyType, valueType);
        assertEquals(keyType, mapLikeType.getKeyType());
    }

    @Test
    public void testGetContentType() {
        JavaType keyType = new JavaType(String.class);
        JavaType valueType = new JavaType(Integer.class);
        MapLikeType mapLikeType = new MapLikeType(Map.class, TypeBindings.emptyBindings(), null, null, keyType, valueType);
        assertEquals(valueType, mapLikeType.getContentType());
    }

    @Test
    public void testGetContentValueHandler() {
        JavaType keyType = new JavaType(String.class);
        JavaType valueType = new JavaType(Integer.class);
        MapLikeType mapLikeType = new MapLikeType(Map.class, TypeBindings.emptyBindings(), null, null, keyType, valueType);
        assertNull(mapLikeType.getContentValueHandler());
    }

    @Test
    public void testGetContentTypeHandler() {
        JavaType keyType = new JavaType(String.class);
        JavaType valueType = new JavaType(Integer.class);
        MapLikeType mapLikeType = new MapLikeType(Map.class, TypeBindings.emptyBindings(), null, null, keyType, valueType);
        assertNull(mapLikeType.getContentTypeHandler());
    }

    @Test
    public void testHasHandlers() {
        JavaType keyType = new JavaType(String.class);
        JavaType valueType = new JavaType(Integer.class);
        MapLikeType mapLikeType = new MapLikeType(Map.class, TypeBindings.emptyBindings(), null, null, keyType, valueType);
        assertFalse(mapLikeType.hasHandlers());
    }

    // Test extended methods

    @Test
    public void testIsTrueMapType() {
        JavaType keyType = new JavaType(String.class);
        JavaType valueType = new JavaType(Integer.class);
        MapLikeType mapLikeType = new MapLikeType(Map.class, TypeBindings.emptyBindings(), null, null, keyType, valueType);
        assertTrue(mapLikeType.isTrueMapType());
    }

    // Test methods for setters

    @Test
    public void testWithKeyType() {
        JavaType oldKeyType = new JavaType(String.class);
        JavaType newKeyType = new JavaType(Integer.class);
        MapLikeType mapLikeType = new MapLikeType(Map.class, TypeBindings.emptyBindings(), null, null, oldKeyType, new JavaType(Integer.class));
        MapLikeType newMapLikeType = mapLikeType.withKeyType(newKeyType);
        assertNotSame(oldKeyType, newMapLikeType.getKeyType());
        assertEquals(newKeyType, newMapLikeType.getKeyType());
    }

    @Test
    public void testWithContentType() {
        JavaType oldValueType = new JavaType(Integer.class);
        JavaType newValueType = new JavaType(Long.class);
        MapLikeType mapLikeType = new MapLikeType(Map.class, TypeBindings.emptyBindings(), null, null, new JavaType(String.class), oldValueType);
        MapLikeType newMapLikeType = mapLikeType.withContentType(newValueType);
        assertNotSame(oldValueType, newMapLikeType.getContentType());
        assertEquals(newValueType, newMapLikeType.getContentType());
    }

    @Test
    public void testWithTypeHandler() {
        Object oldHandler = new Object();
        Object newHandler = new Object();
        MapLikeType mapLikeType = new MapLikeType(Map.class, TypeBindings.emptyBindings(), null, null, new JavaType(String.class), new JavaType(Integer.class), oldHandler, null, false);
        MapLikeType newMapLikeType = mapLikeType.withTypeHandler(newHandler);
        assertNotSame(oldHandler, newMapLikeType.getTypeHandler());
        assertEquals(newHandler, newMapLikeType.getTypeHandler());
    }

    @Test
    public void testWithContentTypeHandler() {
        Object oldHandler = new Object();
        Object newHandler = new Object();
        MapLikeType mapLikeType = new MapLikeType(Map.class, TypeBindings.emptyBindings(), null, null, new JavaType(String.class), new JavaType(Integer.class).withTypeHandler(oldHandler), null, oldHandler, false);
        MapLikeType newMapLikeType = mapLikeType.withContentTypeHandler(newHandler);
        assertNotSame(oldHandler, newMapLikeType.getContentTypeHandler());
        assertEquals(newHandler, newMapLikeType.getContentTypeHandler());
    }

    @Test
    public void testWithValueHandler() {
        Object oldHandler = new Object();
        Object newHandler = new Object();
        MapLikeType mapLikeType = new MapLikeType(Map.class, TypeBindings.emptyBindings(), null, null, new JavaType(String.class), new JavaType(Integer.class), oldHandler, null, false);
        MapLikeType newMapLikeType = mapLikeType.withValueHandler(newHandler);
        assertNotSame(oldHandler, newMapLikeType.getValueHandler());
        assertEquals(newHandler, newMapLikeType.getValueHandler());
    }

    @Test
    public void testWithContentValueHandler() {
        Object oldHandler = new Object();
        Object newHandler = new Object();
        MapLikeType mapLikeType = new MapLikeType(Map.class, TypeBindings.emptyBindings(), null, null, new JavaType(String.class), new JavaType(Integer.class).withValueHandler(oldHandler), oldHandler, null, false);
        MapLikeType newMapLikeType = mapLikeType.withContentValueHandler(newHandler);
        assertNotSame(oldHandler, newMapLikeType.getContentValueHandler());
        assertEquals(newHandler, newMapLikeType.getContentValueHandler());
    }

    @Test
    public void testWithStaticTyping() {
        MapLikeType mapLikeType = new MapLikeType(Map.class, TypeBindings.emptyBindings(), null, null, new JavaType(String.class), new JavaType(Integer.class), null, null, false);
        MapLikeType newMapLikeType = mapLikeType.withStaticTyping();
        assertTrue(newMapLikeType.isStaticTyping());
    }

    // Edge cases and exception handling

    @Test(expected = UnsupportedOperationException.class)
    public void testRefineWithNullTypeBindings() {
        JavaType keyType = new JavaType(String.class);
        JavaType valueType = new JavaType(Integer.class);
        MapLikeType mapLikeType = new MapLikeType(Map.class, null, null, null, keyType, valueType);
        mapLikeType.refine(null, null, null, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testRefineWithInvalidClass() {
        JavaType keyType = new JavaType(String.class);
        JavaType valueType = new JavaType(Integer.class);
        MapLikeType mapLikeType = new MapLikeType(Map.class, null, null, null, keyType, valueType);
        mapLikeType.refine(HashMap.class, null, null, null);
    }

    @Test(expected = NullPointerException.class)
    public void testToStringWithNullClass() {
        JavaType keyType = new JavaType(String.class);
        JavaType valueType = new JavaType(Integer.class);
        MapLikeType mapLikeType = new MapLikeType(null, null, null, null, keyType, valueType);
        mapLikeType.toString();
    }

    @Test(expected = NullPointerException.class)
    public void testEqualsWithNullObject() {
        JavaType keyType = new JavaType(String.class);
        JavaType valueType = new JavaType(Integer.class);
        MapLikeType mapLikeType = new MapLikeType(Map.class, null, null, null, keyType, valueType);
        mapLikeType.equals(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testUpgradeFromWithInvalidBaseType() {
        JavaType keyType = new JavaType(String.class);
        JavaType valueType = new JavaType(Integer.class);
        MapLikeType.upgradeFrom(new JavaType(ArrayList.class), keyType, valueType);
    }
}