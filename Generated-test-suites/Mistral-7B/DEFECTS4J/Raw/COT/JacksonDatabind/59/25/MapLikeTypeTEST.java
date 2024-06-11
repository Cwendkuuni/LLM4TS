package com.example.mapliketype; // replace with your test package if necessary

import static org.junit.Assert.*;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.MapLikeType;

public class MapLikeTypeTest {

    @Test
    public void testMapLikeTypeConstructor() {
        Class<?> mapType = Map.class;
        TypeBindings bindings = TypeBindings.emptyBindings();
        JavaType superClass = _bogusSuperClass(mapType);
        JavaType[] superInts = null;
        JavaType keyT = String.class;
        JavaType valueT = Integer.class;

        MapLikeType mapLikeType = new MapLikeType(mapType, bindings, superClass,
                superInts, keyT, valueT);

        assertEquals(mapType, mapLikeType.getRawType());
        assertEquals(keyT, mapLikeType.getKeyType());
        assertEquals(valueT, mapLikeType.getValueType());
    }

    @Test
    public void testUpgradeFrom() {
        JavaType baseType = List.class;
        JavaType keyT = String.class;
        JavaType valueT = Integer.class;

        MapLikeType mapLikeType = MapLikeType.upgradeFrom(baseType, keyT, valueT);

        assertEquals(MapLikeType.class, mapLikeType.getClass());
        assertEquals(keyT, mapLikeType.getKeyType());
        assertEquals(valueT, mapLikeType.getValueType());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructIllegalArgumentException() {
        JavaType baseType = Object.class;
        JavaType keyT = String.class;
        JavaType valueT = Integer.class;

        MapLikeType.construct(baseType, keyT, valueT);
    }

    @Test
    public void testIsContainerType() {
        MapLikeType mapLikeType = new MapLikeType(Map.class, TypeBindings.emptyBindings(),
                _bogusSuperClass(Map.class), null, String.class, Integer.class);

        assertTrue(mapLikeType.isContainerType());
    }

    @Test
    public void testIsMapLikeType() {
        MapLikeType mapLikeType = new MapLikeType(Map.class, TypeBindings.emptyBindings(),
                _bogusSuperClass(Map.class), null, String.class, Integer.class);

        assertTrue(mapLikeType.isMapLikeType());
    }

    @Test
    public void testGetKeyType() {
        MapLikeType mapLikeType = new MapLikeType(Map.class, TypeBindings.emptyBindings(),
                _bogusSuperClass(Map.class), null, String.class, Integer.class);

        assertEquals(String.class, mapLikeType.getKeyType());
    }

    @Test
    public void testGetContentType() {
        MapLikeType mapLikeType = new MapLikeType(Map.class, TypeBindings.emptyBindings(),
                _bogusSuperClass(Map.class), null, String.class, Integer.class);

        assertEquals(Integer.class, mapLikeType.getValueType());
    }

    @Test
    public void testGetContentValueHandler() {
        MapLikeType mapLikeType = new MapLikeType(Map.class, TypeBindings.emptyBindings(),
                _bogusSuperClass(Map.class), null, String.class, Integer.class,
                null, null, false);

        assertNull(mapLikeType.getContentValueHandler());
    }

    @Test
    public void testGetContentTypeHandler() {
        MapLikeType mapLikeType = new MapLikeType(Map.class, TypeBindings.emptyBindings(),
                _bogusSuperClass(Map.class), null, String.class, Integer.class,
                null, null, false);

        assertNull(mapLikeType.getContentTypeHandler());
    }

    @Test
    public void testHasHandlers() {
        MapLikeType mapLikeType = new MapLikeType(Map.class, TypeBindings.emptyBindings(),
                _bogusSuperClass(Map.class), null, String.class, Integer.class,
                null, null, false);

        assertFalse(mapLikeType.hasHandlers());
    }

    @Test
    public void testIsTrueMapType() {
        MapLikeType mapLikeType = new MapLikeType(Map.class, TypeBindings.emptyBindings(),
                _bogusSuperClass(Map.class), null, String.class, Integer.class);

        assertTrue(mapLikeType.isTrueMapType());
    }

    @Test
    public void testToString() {
        MapLikeType mapLikeType = new MapLikeType(Map.class, TypeBindings.emptyBindings(),
                _bogusSuperClass(Map.class), null, String.class, Integer.class);

        assertEquals("[map-like type; class class java.util.Map, java.lang.String -> java.lang.Integer]",
                mapLikeType.toString());
    }

    @Test
    public void testEquals() {
        MapLikeType mapLikeType1 = new MapLikeType(Map.class, TypeBindings.emptyBindings(),
                _bogusSuperClass(Map.class), null, String.class, Integer.class);
        MapLikeType mapLikeType2 = new MapLikeType(Map.class, TypeBindings.emptyBindings(),
                _bogusSuperClass(Map.class), null, String.class, Integer.class);

        MapLikeType mapLikeType3 = new MapLikeType(List.class, TypeBindings.emptyBindings(),
                _bogusSuperClass(List.class), null, String.class, Integer.class);

        assertTrue(mapLikeType1.equals(mapLikeType2));
        assertFalse(mapLikeType1.equals(mapLikeType3));
    }

    @Test
    public void testWithKeyType() {
        JavaType keyType = Character.class;
        MapLikeType mapLikeType = new MapLikeType(Map.class, TypeBindings.emptyBindings(),
                _bogusSuperClass(Map.class), null, String.class, Integer.class);

        MapLikeType newMapLikeType = mapLikeType.withKeyType(keyType);

        assertNotSame(mapLikeType, newMapLikeType);
        assertEquals(keyType, newMapLikeType.getKeyType());
        assertEquals(Integer.class, newMapLikeType.getValueType());
    }

    @Test
    public void testWithContentType() {
        JavaType contentType = Boolean.class;
        MapLikeType mapLikeType = new MapLikeType(Map.class, TypeBindings.emptyBindings(),
                _bogusSuperClass(Map.class), null, String.class, Integer.class);

        MapLikeType newMapLikeType = mapLikeType.withContentType(contentType);

        assertNotSame(mapLikeType, newMapLikeType);
        assertEquals(String.class, newMapLikeType.getKeyType());
        assertEquals(contentType, newMapLikeType.getValueType());
    }

    @Test
    public void testWithTypeHandler() {
        Object handler = new Object();
        MapLikeType mapLikeType = new MapLikeType(Map.class, TypeBindings.emptyBindings(),
                _bogusSuperClass(Map.class), null, String.class, Integer.class);

        MapLikeType newMapLikeType = mapLikeType.withTypeHandler(handler);

        assertNotSame(mapLikeType, newMapLikeType);
        assertEquals(handler, newMapLikeType.getTypeHandler());
        assertEquals(Integer.class, newMapLikeType.getValueType());
    }

    @Test
    public void testWithContentTypeHandler() {
        Object handler = new Object();
        MapLikeType mapLikeType = new MapLikeType(Map.class, TypeBindings.emptyBindings(),
                _bogusSuperClass(Map.class), null, String.class, Integer.class);

        MapLikeType newMapLikeType = mapLikeType.withContentTypeHandler(handler);

        assertNotSame(mapLikeType, newMapLikeType);
        assertEquals(Integer.class, newMapLikeType.getValueType());
        assertEquals(handler, newMapLikeType.getContentTypeHandler());
    }

    @Test
    public void testWithValueHandler() {
        Object handler = new Object();
        MapLikeType mapLikeType = new MapLikeType(Map.class, TypeBindings.emptyBindings(),
                _bogusSuperClass(Map.class), null, String.class, Integer.class);

        MapLikeType newMapLikeType = mapLikeType.withValueHandler(handler);

        assertNotSame(mapLikeType, newMapLikeType);
        assertEquals(Integer.class, newMapLikeType.getValueType());
        assertEquals(handler, newMapLikeType.getValueHandler());
    }

    @Test
    public void testWithContentValueHandler() {
        Object handler = new Object();
        MapLikeType mapLikeType = new MapLikeType(Map.class, TypeBindings.emptyBindings(),
                _bogusSuperClass(Map.class), null, String.class, Integer.class);

        MapLikeType newMapLikeType = mapLikeType.withContentValueHandler(handler);

        assertNotSame(mapLikeType, newMapLikeType);
        assertEquals(Integer.class, newMapLikeType.getValueType());
        assertEquals(handler, newMapLikeType.getContentValueHandler());
    }

    @Test
    public void testWithStaticTyping() {
        MapLikeType mapLikeType = new MapLikeType(Map.class, TypeBindings.emptyBindings(),
                _bogusSuperClass(Map.class), null, String.class, Integer.class);

        MapLikeType newMapLikeType = mapLikeType.withStaticTyping();

        assertTrue(newMapLikeType.isStaticTyping());
        assertEquals(mapLikeType.getClass(), newMapLikeType.getClass());
    }

    @Test
    public void testRefine() {
        MapLikeType mapLikeType = new MapLikeType(Map.class, TypeBindings.emptyBindings(),
                _bogusSuperClass(Map.class), null, String.class, Integer.class);

        JavaType refinedType = mapLikeType.refine(HashMap.class, TypeBindings.emptyBindings(),
                Object.class, new Class[0]);

        assertEquals(HashMap.class, refinedType.getRawType());
        assertEquals(String.class, refinedType.getKeyType());
        assertEquals(Integer.class, refinedType.getValueType());
    }

    @Test
    public void testBuildCanonicalName() {
        MapLikeType mapLikeType = new MapLikeType(Map.class, TypeBindings.emptyBindings(),
                _bogusSuperClass(Map.class), null, String.class, Integer.class);

        assertEquals("[map-like type; class java.util.Map, java.lang.String -> java.lang.Integer]",
                mapLikeType.buildCanonicalName());
    }

    @Test
    public void testIsContainerType_False() {
        JavaType javaType = new ClassType(HashMap.class);
        MapLikeType mapLikeType = new MapLikeType(javaType, TypeBindings.emptyBindings(),
                _bogusSuperClass(HashMap.class), null, String.class, Integer.class);

        assertFalse(mapLikeType.isContainerType());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetContentValueHandler_UnsupportedOperationException() {
        MapLikeType mapLikeType = new MapLikeType(Map.class, TypeBindings.emptyBindings(),
                _bogusSuperClass(Map.class), null, String.class, Integer.class);

        mapLikeType.getContentValueHandler();
    }

}