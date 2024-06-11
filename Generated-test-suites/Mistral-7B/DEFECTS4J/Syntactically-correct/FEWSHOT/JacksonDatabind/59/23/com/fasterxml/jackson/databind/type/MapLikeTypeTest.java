package com.fasterxml.jackson.databind.type;

import static org.junit.Assert.*;

import org.junit.Test;

public class MapLikeTypeTest {

    @Test
    public void testIsContainerType() {
        MapLikeType mapLikeType = new MapLikeType(Map.class, TypeBindings.emptyBindings(), _bogusSuperClass(Map.class), null, String.class, Integer.class);
        assertTrue(mapLikeType.isContainerType());
    }

    @Test
    public void testIsMapLikeType() {
        MapLikeType mapLikeType = new MapLikeType(Map.class, TypeBindings.emptyBindings(), _bogusSuperClass(Map.class), null, String.class, Integer.class);
        assertTrue(mapLikeType.isMapLikeType());
    }

    @Test
    public void testGetKeyType() {
        MapLikeType mapLikeType = new MapLikeType(Map.class, TypeBindings.emptyBindings(), _bogusSuperClass(Map.class), null, String.class, Integer.class);
        assertEquals(String.class, mapLikeType.getKeyType());
    }

    @Test
    public void testGetContentType() {
        MapLikeType mapLikeType = new MapLikeType(Map.class, TypeBindings.emptyBindings(), _bogusSuperClass(Map.class), null, String.class, Integer.class);
        assertEquals(Integer.class, mapLikeType.getContentType());
    }

    @Test
    public void testGetContentValueHandler() {
        MapLikeType mapLikeType = new MapLikeType(Map.class, TypeBindings.emptyBindings(), _bogusSuperClass(Map.class), null, String.class, Integer.class);
        assertNull(mapLikeType.getContentValueHandler());
    }

    @Test
    public void testGetContentTypeHandler() {
        MapLikeType mapLikeType = new MapLikeType(Map.class, TypeBindings.emptyBindings(), _bogusSuperClass(Map.class), null, String.class, Integer.class);
        assertNull(mapLikeType.getContentTypeHandler());
    }

    @Test
    public void testHasHandlers() {
        MapLikeType mapLikeType = new MapLikeType(Map.class, TypeBindings.emptyBindings(), _bogusSuperClass(Map.class), null, String.class, Integer.class);
        assertFalse(mapLikeType.hasHandlers());
    }

    @Test
    public void testIsTrueMapType() {
        MapLikeType mapLikeType = new MapLikeType(Map.class, TypeBindings.emptyBindings(), _bogusSuperClass(Map.class), null, String.class, Integer.class);
        assertTrue(mapLikeType.isTrueMapType());
    }

    @Test
    public void testBuildCanonicalName() {
        MapLikeType mapLikeType = new MapLikeType(Map.class, TypeBindings.emptyBindings(), _bogusSuperClass(Map.class), null, String.class, Integer.class);
        assertEquals("[map-like type; class java.util.Map, Ljava/lang/String;Ljava/lang/Integer;]", mapLikeType.buildCanonicalName());
    }

    @Test
    public void testEquals() {
        MapLikeType mapLikeType1 = new MapLikeType(Map.class, TypeBindings.emptyBindings(), _bogusSuperClass(Map.class), null, String.class, Integer.class);
        MapLikeType mapLikeType2 = new MapLikeType(Map.class, TypeBindings.emptyBindings(), _bogusSuperClass(Map.class), null, String.class, Integer.class);
        MapLikeType mapLikeType3 = new MapLikeType(List.class, TypeBindings.emptyBindings(), _bogusSuperClass(Map.class), null, String.class, Integer.class);

        assertTrue(mapLikeType1.equals(mapLikeType2));
        assertFalse(mapLikeType1.equals(mapLikeType3));
    }

    @Test
    public void testIsContainerType_whenNotMapLikeType() {
        JavaType javaType = new TypeFactory().constructType(int[].class);
        MapLikeType mapLikeType = new MapLikeType(javaType.getRawClass(), TypeBindings.emptyBindings(), _bogusSuperClass(javaType.getRawClass()), null, null, null);
        assertFalse(mapLikeType.isContainerType());
    }

    @Test
    public void testIsMapLikeType_whenNotMapLikeType() {
        JavaType javaType = new TypeFactory().constructType(int[].class);
        MapLikeType mapLikeType = new MapLikeType(javaType.getRawClass(), TypeBindings.emptyBindings(), _bogusSuperClass(javaType.getRawClass()), null, null, null);
        assertFalse(mapLikeType.isMapLikeType());
    }

    @Test
    public void testGetKeyType_whenNullKeyType() {
        MapLikeType mapLikeType = new MapLikeType(Map.class, TypeBindings.emptyBindings(), _bogusSuperClass(Map.class), null, null, null);
        assertNull(mapLikeType.getKeyType());
    }

    @Test
    public void testGetContentType_whenNullContentType() {
        MapLikeType mapLikeType = new MapLikeType(Map.class, TypeBindings.emptyBindings(), _bogusSuperClass(Map.class), null, null, null);
        assertNull(mapLikeType.getContentType());
    }

    @Test
    public void testGetContentValueHandler_whenNullContentValueHandler() {
        MapLikeType mapLikeType = new MapLikeType(Map.class, TypeBindings.emptyBindings(), _bogusSuperClass(Map.class), null, String.class, Integer.class);
        assertNull(mapLikeType.getContentValueHandler());
    }

    @Test
    public void testGetContentTypeHandler_whenNullContentTypeHandler() {
        MapLikeType mapLikeType = new MapLikeType(Map.class, TypeBindings.emptyBindings(), _bogusSuperClass(Map.class), null, String.class, Integer.class);
        assertNull(mapLikeType.getContentTypeHandler());
    }

    @Test
    public void testIsTrueMapType_whenNotInstanceOfMap() {
        JavaType javaType = new TypeFactory().constructType(int.class);
        MapLikeType mapLikeType = new MapLikeType(javaType.getRawClass(), TypeBindings.emptyBindings(), _bogusSuperClass(javaType.getRawClass()), null, null, null);
        assertFalse(mapLikeType.isTrueMapType());
    }
}