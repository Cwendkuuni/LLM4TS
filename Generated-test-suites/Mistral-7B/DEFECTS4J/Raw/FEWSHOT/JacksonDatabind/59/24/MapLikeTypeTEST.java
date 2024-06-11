package com.fasterxml.jackson.databind.type;

import static org.junit.Assert.*;

import org.junit.Test;

public class MapLikeTypeTest {

    @Test
    public void testIsContainerType() {
        JavaType mapLikeType = new MapLikeType(Map.class, TypeBindings.emptyBindings(), null, null, String.class, IntType.instance, null, null, false);
        assertTrue(mapLikeType.isContainerType());
        assertTrue(mapLikeType.isMapLikeType());
    }

    @Test
    public void testIsTrueMapType() {
        JavaType mapLikeType = new MapLikeType(Map.class, TypeBindings.emptyBindings(), null, null, String.class, IntType.instance, null, null, false);
        assertTrue(mapLikeType.isTrueMapType());

        JavaType listLikeType = new TypeBase(List.class, TypeBindings.emptyBindings(), null, null, IntType.instance, null, null, null, false);
        assertFalse(listLikeType.isTrueMapType());
    }

    @Test
    public void testGetKeyType() {
        JavaType mapLikeType = new MapLikeType(Map.class, TypeBindings.emptyBindings(), null, null, String.class, IntType.instance, null, null, false);
        assertEquals(String.class, mapLikeType.getKeyType());
    }

    @Test
    public void testGetContentType() {
        JavaType mapLikeType = new MapLikeType(Map.class, TypeBindings.emptyBindings(), null, null, String.class, IntType.instance, null, null, false);
        assertEquals(IntType.instance, mapLikeType.getContentType());
    }

    @Test
    public void testGetContentValueHandler() {
        JavaType mapLikeType = new MapLikeType(Map.class, TypeBindings.emptyBindings(), null, null, String.class, IntType.instance, null, null, false);
        assertNull(mapLikeType.getContentValueHandler());
    }

    @Test
    public void testGetContentTypeHandler() {
        JavaType mapLikeType = new MapLikeType(Map.class, TypeBindings.emptyBindings(), null, null, String.class, IntType.instance, null, null, false);
        assertNull(mapLikeType.getContentTypeHandler());
    }

    @Test
    public void testHasHandlers() {
        JavaType mapLikeType = new MapLikeType(Map.class, TypeBindings.emptyBindings(), null, null, String.class, IntType.instance, null, null, false);
        assertFalse(mapLikeType.hasHandlers());

        JavaType mapLikeTypeWithHandlers = new MapLikeType(Map.class, TypeBindings.emptyBindings(), null, null, String.class, IntType.instance, new IntValueHandler(), new IntTypeHandler(), false);
        assertTrue(mapLikeTypeWithHandlers.hasHandlers());
    }

    @Test
    public void testBuildCanonicalName() {
        JavaType mapLikeType = new MapLikeType(Map.class, TypeBindings.emptyBindings(), null, null, String.class, IntType.instance, null, null, false);
        assertEquals("class java.util.Map<java.lang.String, Integer>", mapLikeType.buildCanonicalName());
    }

    @Test
    public void testGetErasedSignature() {
        JavaType mapLikeType = new MapLikeType(Map.class, TypeBindings.emptyBindings(), null, null, String.class, IntType.instance, null, null, false);
        assertEquals("[map-like type; class java.util.Map, <Ljava.lang.String; Ljava.lang.Integer;>]", mapLikeType.getErasedSignature(new StringBuilder()).toString());
    }

    @Test
    public void testGetGenericSignature() {
        JavaType mapLikeType = new MapLikeType(Map.class, TypeBindings.emptyBindings(), null, null, String.class, IntType.instance, null, null, false);
        assertEquals("[map-like type; class java.util.Map<java.lang.String, Integer>]", mapLikeType.getGenericSignature(new StringBuilder()).toString());
    }

    @Test
    public void testWithKeyType() {
        JavaType mapLikeType = new MapLikeType(Map.class, TypeBindings.emptyBindings(), null, null, String.class, IntType.instance, null, null, false);
        JavaType newMapLikeType = mapLikeType.withKeyType(Character.TYPE);
        assertNotEquals(mapLikeType, newMapLikeType);
        assertEquals(Character.TYPE, newMapLikeType.getKeyType());
    }

    @Test
    public void testWithContentType() {
        JavaType mapLikeType = new MapLikeType(Map.class, TypeBindings.emptyBindings(), null, null, String.class, IntType.instance, null, null, false);
        JavaType newMapLikeType = mapLikeType.withContentType(DoubleType.instance);
        assertNotEquals(mapLikeType, newMapLikeType);
        assertEquals(DoubleType.instance, newMapLikeType.getContentType());
    }

    @Test
    public void testWithTypeHandler() {
        JavaType mapLikeType = new MapLikeType(Map.class, TypeBindings.emptyBindings(), null, null, String.class, IntType.instance, null, null, false);
        JavaType newMapLikeType = mapLikeType.withTypeHandler(new IntTypeHandler());
        assertNotEquals(mapLikeType, newMapLikeType);
    }

    @Test
    public void testWithContentTypeHandler() {
        JavaType mapLikeType = new MapLikeType(Map.class, TypeBindings.emptyBindings(), null, null, String.class, IntType.instance, null, null, false);
        JavaType newMapLikeType = mapLikeType.withContentTypeHandler(new IntContentTypeHandler());
        assertNotEquals(mapLikeType, newMapLikeType);
    }

    @Test
    public void testWithValueHandler() {
        JavaType mapLikeType = new MapLikeType(Map.class, TypeBindings.emptyBindings(), null, null, String.class, IntType.instance, null, null, false);
        JavaType newMapLikeType = mapLikeType.withValueHandler(new IntValueHandler());
        assertNotEquals(mapLikeType, newMapLikeType);
    }

    @Test
    public void testWithContentValueHandler() {
        JavaType mapLikeType = new MapLikeType(Map.class, TypeBindings.emptyBindings(), null, null, String.class, IntType.instance, null, null, false);
        JavaType newMapLikeType = mapLikeType.withContentValueHandler(new IntContentValueHandler());
        assertNotEquals(mapLikeType, newMapLikeType);
    }

    @Test
    public void testWithStaticTyping() {
        JavaType mapLikeType = new MapLikeType(Map.class, TypeBindings.emptyBindings(), null, null, String.class, IntType.instance, null, null, false);
        JavaType newMapLikeType = mapLikeType.withStaticTyping();
        assertNotEquals(mapLikeType, newMapLikeType);
        assertTrue(newMapLikeType.isStaticTyping());
    }

    @Test
    public void testRefine() {
        JavaType baseType = new TypeBase(Map.class, TypeBindings.emptyBindings(), null, null, null, null, null, null, false);
        JavaType mapLikeType = new MapLikeType(baseType, TypeBindings.emptyBindings(), null, null, String.class, IntType.instance, null, null, false);
        JavaType refinedMapLikeType = mapLikeType.refine(HashMap.class, TypeBindings.emptyBindings(), null, null);
        assertNotEquals(mapLikeType, refinedMapLikeType);
        assertEquals(HashMap.class, refinedMapLikeType.getRawClass());
    }
}