package com.fasterxml.jackson.databind.type;

import static org.junit.Assert.*;

import org.junit.Test;

public class MapLikeTypeTest {

    @Test
    public void testIsContainerType() {
        MapLikeType mapLikeType = new MapLikeType(Map.class, TypeBindings.emptyBindings(), _bogusSuperClass(Map.class), null, StringType.INSTANCE, IntType.INSTANCE);
        assertTrue(mapLikeType.isContainerType());
    }

    @Test
    public void testIsMapLikeType() {
        MapLikeType mapLikeType = new MapLikeType(Map.class, TypeBindings.emptyBindings(), _bogusSuperClass(Map.class), null, StringType.INSTANCE, IntType.INSTANCE);
        assertTrue(mapLikeType.isMapLikeType());
    }

    @Test
    public void testGetKeyType() {
        MapLikeType mapLikeType = new MapLikeType(Map.class, TypeBindings.emptyBindings(), _bogusSuperClass(Map.class), null, StringType.INSTANCE, IntType.INSTANCE);
        assertEquals(StringType.INSTANCE, mapLikeType.getKeyType());
    }

    @Test
    public void testGetContentType() {
        MapLikeType mapLikeType = new MapLikeType(Map.class, TypeBindings.emptyBindings(), _bogusSuperClass(Map.class), null, StringType.INSTANCE, IntType.INSTANCE);
        assertEquals(IntType.INSTANCE, mapLikeType.getContentType());
    }

    @Test
    public void testGetContentValueHandler() {
        MapLikeType mapLikeType = new MapLikeType(Map.class, TypeBindings.emptyBindings(), _bogusSuperClass(Map.class), null, StringType.INSTANCE, IntType.INSTANCE);
        assertNull(mapLikeType.getContentValueHandler());
    }

    @Test
    public void testGetContentTypeHandler() {
        MapLikeType mapLikeType = new MapLikeType(Map.class, TypeBindings.emptyBindings(), _bogusSuperClass(Map.class), null, StringType.INSTANCE, IntType.INSTANCE);
        assertNull(mapLikeType.getContentTypeHandler());
    }

    @Test
    public void testHasHandlers() {
        MapLikeType mapLikeType = new MapLikeType(Map.class, TypeBindings.emptyBindings(), _bogusSuperClass(Map.class), null, StringType.INSTANCE, IntType.INSTANCE);
        assertFalse(mapLikeType.hasHandlers());
    }

    @Test
    public void testIsTrueMapType() {
        MapLikeType mapLikeType = new MapLikeType(Map.class, TypeBindings.emptyBindings(), _bogusSuperClass(Map.class), null, StringType.INSTANCE, IntType.INSTANCE);
        assertTrue(mapLikeType.isTrueMapType());
    }

    @Test
    public void testIsTrueMapTypeFalse() {
        MapLikeType mapLikeType = new MapLikeType(List.class, TypeBindings.emptyBindings(), _bogusSuperClass(List.class), null, StringType.INSTANCE, IntType.INSTANCE);
        assertFalse(mapLikeType.isTrueMapType());
    }

    @Test
    public void testToString() {
        MapLikeType mapLikeType = new MapLikeType(Map.class, TypeBindings.emptyBindings(), _bogusSuperClass(Map.class), null, StringType.INSTANCE, IntType.INSTANCE);
        assertEquals("[map-like type; class java.util.Map, class java.lang.String -> class java.lang.Integer]", mapLikeType.toString());
    }

    @Test
    public void testEquals() {
        MapLikeType mapLikeType1 = new MapLikeType(Map.class, TypeBindings.emptyBindings(), _bogusSuperClass(Map.class), null, StringType.INSTANCE, IntType.INSTANCE);
        MapLikeType mapLikeType2 = new MapLikeType(Map.class, TypeBindings.emptyBindings(), _bogusSuperClass(Map.class), null, StringType.INSTANCE, IntType.INSTANCE);
        MapLikeType mapLikeType3 = new MapLikeType(List.class, TypeBindings.emptyBindings(), _bogusSuperClass(List.class), null, StringType.INSTANCE, IntType.INSTANCE);

        assertTrue(mapLikeType1.equals(mapLikeType2));
        assertFalse(mapLikeType1.equals(mapLikeType3));
    }

    @Test
    public void testRefine() {
        JavaType baseType = TypeFactory.defaultInstance().constructType(HashMap.class);
        MapLikeType mapLikeType = new MapLikeType(Map.class, TypeBindings.emptyBindings(), _bogusSuperClass(Map.class), null, StringType.INSTANCE, IntType.INSTANCE);
        JavaType refinedType = mapLikeType.refine(HashMap.class, TypeBindings.emptyBindings(), baseType, null);

        assertEquals(HashMapType.getInstance(), refinedType);
    }

    @Test
    public void testWithKeyType() {
        JavaType keyType = StringType.INSTANCE;
        MapLikeType mapLikeType = new MapLikeType(Map.class, TypeBindings.emptyBindings(), _bogusSuperClass(Map.class), null, StringType.INSTANCE, IntType.INSTANCE);
        MapLikeType newMapLikeType = mapLikeType.withKeyType(keyType);

        assertEquals(mapLikeType.getClass(), newMapLikeType.getClass());
        assertEquals(keyType, newMapLikeType.getKeyType());
        assertEquals(IntType.INSTANCE, newMapLikeType.getContentType());
    }

    @Test
    public void testWithContentType() {
        JavaType valueType = IntType.INSTANCE;
        MapLikeType mapLikeType = new MapLikeType(Map.class, TypeBindings.emptyBindings(), _bogusSuperClass(Map.class), null, StringType.INSTANCE, IntType.INSTANCE);
        MapLikeType newMapLikeType = mapLikeType.withContentType(valueType);

        assertEquals(mapLikeType.getClass(), newMapLikeType.getClass());
        assertEquals(StringType.INSTANCE, newMapLikeType.getKeyType());
        assertEquals(valueType, newMapLikeType.getContentType());
    }

    @Test
    public void testWithTypeHandler() {
        Object valueHandler = new Object();
        MapLikeType mapLikeType = new MapLikeType(Map.class, TypeBindings.emptyBindings(), _bogusSuperClass(Map.class), null, StringType.INSTANCE, IntType.INSTANCE);
        MapLikeType newMapLikeType = mapLikeType.withTypeHandler(valueHandler);

        assertEquals(mapLikeType.getClass(), newMapLikeType.getClass());
        assertEquals(StringType.INSTANCE, newMapLikeType.getKeyType());
        assertEquals(IntType.INSTANCE, newMapLikeType.getContentType());
        assertEquals(valueHandler, newMapLikeType.getTypeHandler());
    }

    @Test
    public void testWithContentTypeHandler() {
        Object contentTypeHandler = new Object();
        MapLikeType mapLikeType = new MapLikeType(Map.class, TypeBindings.emptyBindings(), _bogusSuperClass(Map.class), null, StringType.INSTANCE, IntType.INSTANCE);
        MapLikeType newMapLikeType = mapLikeType.withContentTypeHandler(contentTypeHandler);

        assertEquals(mapLikeType.getClass(), newMapLikeType.getClass());
        assertEquals(StringType.INSTANCE, newMapLikeType.getKeyType());
        assertEquals(IntType.INSTANCE, newMapLikeType.getContentType());
        assertEquals(contentTypeHandler, newMapLikeType.getContentTypeHandler());
    }

    @Test
    public void testWithValueHandler() {
        Object valueHandler = new Object();
        MapLikeType mapLikeType = new MapLikeType(Map.class, TypeBindings.emptyBindings(), _bogusSuperClass(Map.class), null, StringType.INSTANCE, IntType.INSTANCE);
        MapLikeType newMapLikeType = mapLikeType.withValueHandler(valueHandler);

        assertEquals(mapLikeType.getClass(), newMapLikeType.getClass());
        assertEquals(StringType.INSTANCE, newMapLikeType.getKeyType());
        assertEquals(IntType.INSTANCE, newMapLikeType.getContentType());
        assertEquals(valueHandler, newMapLikeType.getValueHandler());
    }

    @Test
    public void testWithContentValueHandler() {
        Object contentValueHandler = new Object();
        MapLikeType mapLikeType = new MapLikeType(Map.class, TypeBindings.emptyBindings(), _bogusSuperClass(Map.class), null, StringType.INSTANCE, IntType.INSTANCE);
        MapLikeType newMapLikeType = mapLikeType.withContentValueHandler(contentValueHandler);

        assertEquals(mapLikeType.getClass(), newMapLikeType.getClass());
        assertEquals(StringType.INSTANCE, newMapLikeType.getKeyType());
        assertEquals(IntType.INSTANCE, newMapLikeType.getContentType());
        assertEquals(contentValueHandler, newMapLikeType.getContentValueHandler());
    }

    @Test
    public void testWithStaticTyping() {
        MapLikeType mapLikeType = new MapLikeType(Map.class, TypeBindings.emptyBindings(), _bogusSuperClass(Map.class), null, StringType.INSTANCE, IntType.INSTANCE, false);
        MapLikeType newMapLikeType = mapLikeType.withStaticTyping();

        assertTrue(newMapLikeType.isStaticTyping());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructWithInvalidBaseType() {
        JavaType baseType = TypeFactory.defaultInstance().constructType(Integer.class);
        new MapLikeType(baseType, TypeBindings.emptyBindings(), _bogusSuperClass(Map.class), null, StringType.INSTANCE, IntType.INSTANCE);
    }

    @Test
    public void testUpgradeFrom() {
        JavaType baseType = TypeFactory.defaultInstance().constructType(HashMap.class);
        JavaType keyType = StringType.INSTANCE;
        JavaType valueType = IntType.INSTANCE;
        MapLikeType mapLikeType = MapLikeType.upgradeFrom(baseType, keyType, valueType);

        assertEquals(HashMapType.getInstance(), mapLikeType);
    }
}