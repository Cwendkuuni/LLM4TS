package com.example.junit.test;

import static org.junit.Assert.*;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.MapLikeType;

import org.junit.Test;

public class MapLikeTypeTest {

    // Test constructor with valid arguments
    @Test
    public void testConstructValidArguments() {
        Class<?> mapType = Map.class;
        TypeBindings bindings = TypeBindings.emptyBindings();
        JavaType superClass = _bogusSuperClass(mapType);
        JavaType[] superInts = null;
        JavaType keyT = JavaType.constructType(String.class, null);
        JavaType valueT = JavaType.constructType(Integer.class, null);

        MapLikeType mapLikeType = new MapLikeType(mapType, bindings, superClass,
                superInts, keyT, valueT, null, null, false);

        assertEquals(Map.class, mapLikeType.getClass());
        assertEquals(keyT, mapLikeType.getKeyType());
        assertEquals(valueT, mapLikeType.getContentType());
        assertFalse(mapLikeType.isStatic());
    }

    // Test constructor with invalid argument
    @Test(expected = IllegalArgumentException.class)
    public void testConstructInvalidArgument() {
        JavaType baseType = JavaType.constructType(String.class, null);
        JavaType keyT = JavaType.constructType(String.class, null);
        JavaType valueT = JavaType.constructType(Integer.class, null);

        new MapLikeType(baseType, keyT, valueT);
    }

    // Test upgradeFrom method with valid arguments
    @Test
    public void testUpgradeFromValidArguments() {
        JavaType baseType = JavaType.constructType(Map.class, null);
        JavaType keyT = JavaType.constructType(String.class, null);
        JavaType valueT = JavaType.constructType(Integer.class, null);

        MapLikeType mapLikeType = MapLikeType.upgradeFrom(baseType, keyT, valueT);

        assertEquals(MapLikeType.class, mapLikeType.getClass());
        assertEquals(keyT, mapLikeType.getKeyType());
        assertEquals(valueT, mapLikeType.getContentType());
    }

    // Test upgradeFrom method with invalid argument
    @Test(expected = IllegalArgumentException.class)
    public void testUpgradeFromInvalidArgument() {
        JavaType baseType = JavaType.constructType(List.class, null);
        JavaType keyT = JavaType.constructType(String.class, null);
        JavaType valueT = JavaType.constructType(Integer.class, null);

        MapLikeType.upgradeFrom(baseType, keyT, valueT);
    }

    // Test isContainerType method
    @Test
    public void testIsContainerType() {
        MapLikeType mapLikeType = new MapLikeType(Map.class, null, null, null,
                JavaType.constructType(String.class, null),
                JavaType.constructType(Integer.class, null), null, null, false);

        assertTrue(mapLikeType.isContainerType());
    }

    // Test isMapLikeType method
    @Test
    public void testIsMapLikeType() {
        MapLikeType mapLikeType = new MapLikeType(Map.class, null, null, null,
                JavaType.constructType(String.class, null),
                JavaType.constructType(Integer.class, null), null, null, false);

        assertTrue(mapLikeType.isMapLikeType());
    }

    // Test getKeyType method
    @Test
    public void testGetKeyType() {
        MapLikeType mapLikeType = new MapLikeType(Map.class, null, null, null,
                JavaType.constructType(String.class, null),
                JavaType.constructType(Integer.class, null), null, null, false);

        assertEquals(JavaType.constructType(String.class, null), mapLikeType.getKeyType());
    }

    // Test getContentType method
    @Test
    public void testGetContentType() {
        MapLikeType mapLikeType = new MapLikeType(Map.class, null, null, null,
                JavaType.constructType(String.class, null),
                JavaType.constructType(Integer.class, null), null, null, false);

        assertEquals(JavaType.constructType(Integer.class, null), mapLikeType.getContentType());
    }

    // Test hasHandlers method
    @Test
    public void testHasHandlers() {
        MapLikeType mapLikeType = new MapLikeType(Map.class, null, null, null,
                JavaType.constructType(String.class, null),
                JavaType.constructType(Integer.class, null), null, null, false);

        assertFalse(mapLikeType.hasHandlers());
    }

    // Test withKeyType method
    @Test
    public void testWithKeyType() {
        MapLikeType mapLikeType = new MapLikeType(Map.class, null, null, null,
                JavaType.constructType(String.class, null),
                JavaType.constructType(Integer.class, null), null, null, false);

        JavaType newKeyType = JavaType.constructType(Long.class, null);

        MapLikeType newMapLikeType = mapLikeType.withKeyType(newKeyType);

        assertEquals(newKeyType, newMapLikeType.getKeyType());
        assertNotSame(mapLikeType, newMapLikeType);
    }

    // Test withContentType method
    @Test
    public void testWithContentType() {
        MapLikeType mapLikeType = new MapLikeType(Map.class, null, null, null,
                JavaType.constructType(String.class, null),
                JavaType.constructType(Integer.class, null), null, null, false);

        JavaType newContentType = JavaType.constructType(Float.class, null);

        MapLikeType newMapLikeType = mapLikeType.withContentType(newContentType);

        assertEquals(newContentType, newMapLikeType.getContentType());
        assertNotSame(mapLikeType, newMapLikeType);
    }

    // Test withTypeHandler method
    @Test
    public void testWithTypeHandler() {
        MapLikeType mapLikeType = new MapLikeType(Map.class, null, null, null,
                JavaType.constructType(String.class, null),
                JavaType.constructType(Integer.class, null), null, null, false);

        Object newValueHandler = new Object();

        MapLikeType newMapLikeType = mapLikeType.withTypeHandler(newValueHandler);

        assertNotSame(mapLikeType.getValueHandler(), newMapLikeType.getValueHandler());
        assertEquals(newValueHandler, newMapLikeType.getValueHandler());
    }

    // Test withContentTypeHandler method
    @Test
    public void testWithContentTypeHandler() {
        MapLikeType mapLikeType = new MapLikeType(Map.class, null, null, null,
                JavaType.constructType(String.class, null),
                JavaType.constructType(Integer.class, null), null, null, false);

        Object newContentTypeHandler = new Object();

        MapLikeType newMapLikeType = mapLikeType.withContentTypeHandler(newContentTypeHandler);

        assertNotSame(mapLikeType.getTypeHandler(), newMapLikeType.getTypeHandler());
        assertEquals(newContentTypeHandler, newMapLikeType.getTypeHandler());
    }

    // Test withValueHandler method
    @Test
    public void testWithValueHandler() {
        MapLikeType mapLikeType = new MapLikeType(Map.class, null, null, null,
                JavaType.constructType(String.class, null),
                JavaType.constructType(Integer.class, null), null, null, false);

        Object newValueHandler = new Object();

        MapLikeType newMapLikeType = mapLikeType.withValueHandler(newValueHandler);

        assertNotSame(mapLikeType.getValueHandler(), newMapLikeType.getValueHandler());
        assertEquals(newValueHandler, newMapLikeType.getValueHandler());
    }

    // Test withContentValueHandler method
    @Test
    public void testWithContentValueHandler() {
        MapLikeType mapLikeType = new MapLikeType(Map.class, null, null, null,
                JavaType.constructType(String.class, null),
                JavaType.constructType(Integer.class, null), null, null, false);

        Object newContentValueHandler = new Object();

        MapLikeType newMapLikeType = mapLikeType.withContentValueHandler(newContentValueHandler);

        assertNotSame(mapLikeType.getContentValueHandler(), newMapLikeType.getContentValueHandler());
        assertEquals(newContentValueHandler, newMapLikeType.getContentValueHandler());
    }

    // Test isTrueMapType method
    @Test
    public void testIsTrueMapType() {
        MapLikeType mapLikeType = new MapLikeType(Map.class, null, null, null,
                JavaType.constructType(String.class, null),
                JavaType.constructType(Integer.class, null), null, null, false);

        assertTrue(mapLikeType.isTrueMapType());
    }

    // Test toString method
    @Test
    public void testToString() {
        MapLikeType mapLikeType = new MapLikeType(Map.class, null, null, null,
                JavaType.constructType(String.class, null),
                JavaType.constructType(Integer.class, null), null, null, false);

        assertEquals("[map-like type; class class java.util.Map, class java.lang.String -> class java.lang.Integer]", mapLikeType.toString());
    }

    // Test equals method
    @Test
    public void testEquals() {
        MapLikeType mapLikeType1 = new MapLikeType(Map.class, null, null, null,
                JavaType.constructType(String.class, null),
                JavaType.constructType(Integer.class, null), null, null, false);

        MapLikeType mapLikeType2 = new MapLikeType(Map.class, null, null, null,
                JavaType.constructType(String.class, null),
                JavaType.constructType(Integer.class, null), null, null, false);

        MapLikeType mapLikeType3 = new MapLikeType(Map.class, null, null, null,
                JavaType.constructType(String.class, null),
                JavaType.constructType(Float.class, null), null, null, false);

        assertTrue(mapLikeType1.equals(mapLikeType2));
        assertFalse(mapLikeType1.equals(mapLikeType3));
    }
}