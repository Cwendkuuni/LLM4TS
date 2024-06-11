package com.fasterxml.jackson.databind.type;

import static org.junit.Assert.*;

import org.junit.Test;

public class MapLikeTypeTest {

    @Test
    public void testIsContainerType() {
        MapLikeType mapLikeType = new MapLikeType(Map.class, TypeBindings.emptyBindings(), _bogusSuperClass(Map.class), null, StringType.instance, IntType.instance);
        assertTrue(mapLikeType.isContainerType());
    }

    @Test
    public void testIsMapLikeType() {
        MapLikeType mapLikeType = new MapLikeType(Map.class, TypeBindings.emptyBindings(), _bogusSuperClass(Map.class), null, StringType.instance, IntType.instance);
        assertTrue(mapLikeType.isMapLikeType());
    }

    @Test
    public void testGetKeyType() {
        MapLikeType mapLikeType = new MapLikeType(Map.class, TypeBindings.emptyBindings(), _bogusSuperClass(Map.class), null, StringType.instance, IntType.instance);
        assertEquals(StringType.instance, mapLikeType.getKeyType());
    }

    @Test
    public void testGetContentType() {
        MapLikeType mapLikeType = new MapLikeType(Map.class, TypeBindings.emptyBindings(), _bogusSuperClass(Map.class), null, StringType.instance, IntType.instance);
        assertEquals(IntType.instance, mapLikeType.getContentType());
    }

    @Test
    public void testGetContentValueHandler() {
        MapLikeType mapLikeType = new MapLikeType(Map.class, TypeBindings.emptyBindings(), _bogusSuperClass(Map.class), null, StringType.instance, IntType.instance);
        assertEquals(IntType.ValueHandler.instance, mapLikeType.getContentValueHandler());
    }

    @Test
    public void testGetContentTypeHandler() {
        MapLikeType mapLikeType = new MapLikeType(Map.class, TypeBindings.emptyBindings(), _bogusSuperClass(Map.class), null, StringType.instance, IntType.instance);
        assertEquals(IntType.TypeHandler.instance, mapLikeType.getContentTypeHandler());
    }

    @Test
    public void testHasHandlers() {
        MapLikeType mapLikeType = new MapLikeType(Map.class, TypeBindings.emptyBindings(), _bogusSuperClass(Map.class), null, StringType.instance, IntType.instance);
        assertTrue(mapLikeType.hasHandlers());
    }

    @Test
    public void testIsTrueMapType() {
        MapLikeType mapLikeType = new MapLikeType(Map.class, TypeBindings.emptyBindings(), _bogusSuperClass(Map.class), null, StringType.instance, IntType.instance);
        assertTrue(mapLikeType.isTrueMapType());
    }

    @Test
    public void testToString() {
        MapLikeType mapLikeType = new MapLikeType(Map.class, TypeBindings.emptyBindings(), _bogusSuperClass(Map.class), null, StringType.instance, IntType.instance);
        assertEquals("[map-like type; class java.util.Map, class java.lang.String, class int]", mapLikeType.toString());
    }

    @Test
    public void testEquals() {
        MapLikeType mapLikeType1 = new MapLikeType(Map.class, TypeBindings.emptyBindings(), _bogusSuperClass(Map.class), null, StringType.instance, IntType.instance);
        MapLikeType mapLikeType2 = new MapLikeType(Map.class, TypeBindings.emptyBindings(), _bogusSuperClass(Map.class), null, StringType.instance, IntType.instance);
        MapLikeType mapLikeType3 = new MapLikeType(ArrayList.class, TypeBindings.emptyBindings(), _bogusSuperClass(ArrayList.class), null, StringType.instance, IntType.instance);

        assertTrue(mapLikeType1.equals(mapLikeType2));
        assertFalse(mapLikeType1.equals(mapLikeType3));
        assertFalse(mapLikeType1.equals(null));
    }

    @Test
    public void testWithKeyType() {
        JavaType javaType = new TypeReference<Map<String, Integer>>() {}.getType();
        MapLikeType mapLikeType = new MapLikeType(Map.class, TypeBindings.emptyBindings(), _bogusSuperClass(Map.class), null, StringType.instance, IntType.instance);
        MapLikeType newMapLikeType = mapLikeType.withKeyType(StringType.instance);

        assertEquals(javaType, newMapLikeType.getType());
        assertNotSame(mapLikeType, newMapLikeType);
    }

    @Test
    public void testWithContentType() {
        JavaType javaType = new TypeReference<Map<String, Integer>>() {}.getType();
        MapLikeType mapLikeType = new MapLikeType(Map.class, TypeBindings.emptyBindings(), _bogusSuperClass(Map.class), null, StringType.instance, IntType.instance);
        MapLikeType newMapLikeType = mapLikeType.withContentType(IntType.instance);

        assertEquals(javaType, newMapLikeType.getType());
        assertNotSame(mapLikeType, newMapLikeType);
    }

    @Test
    public void testWithTypeHandler() {
        MapLikeType mapLikeType = new MapLikeType(Map.class, TypeBindings.emptyBindings(), _bogusSuperClass(Map.class), null, StringType.instance, IntType.instance);
        MapLikeType newMapLikeType = mapLikeType.withTypeHandler(IntType.TypeHandler.instance);

        assertNotSame(mapLikeType, newMapLikeType);
    }

    @Test
    public void testWithContentTypeHandler() {
        MapLikeType mapLikeType = new MapLikeType(Map.class, TypeBindings.emptyBindings(), _bogusSuperClass(Map.class), null, StringType.instance, IntType.instance);
        MapLikeType newMapLikeType = mapLikeType.withContentTypeHandler(IntType.TypeHandler.instance);

        assertNotSame(mapLikeType, newMapLikeType);
    }
}