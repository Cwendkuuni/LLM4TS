package com.fasterxml.jackson.databind.type;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class MapTypeTest {
    @Test
    public void testConstruct() {
        MapType mapType = MapType.construct(Map.class, TypeBindings.emptyBindings(), null, null, String.class, Integer.class);
        assertEquals(Map.class, mapType.getMapType());
        assertEquals(TypeBindings.emptyBindings(), mapType.getBindings());
        assertNull(mapType.getSuperClass());
        assertNull(mapType.getSuperInterfaces());
        assertEquals(String.class, mapType.getKeyType());
        assertEquals(Integer.class, mapType.getValueType());
        assertNull(mapType.getValueHandler());
        assertNull(mapType.getTypeHandler());
        assertEquals(false, mapType.isAsStatic());
    }

    @Test
    public void testConstructNegative() {
        MapType mapType = MapType.construct(HashMap.class, TypeBindings.emptyBindings(), null, null, String.class, Integer.class);
        assertNotEquals(HashMap.class, mapType.getMapType());
    }

    @Test
    public void testWithKeyType() {
        MapType mapType = MapType.construct(Map.class, TypeBindings.emptyBindings(), null, null, String.class, Integer.class);
        MapType newMapType = mapType.withKeyType(String.class);
        assertEquals(mapType.getMapType(), newMapType.getMapType());
        assertEquals(mapType.getBindings(), newMapType.getBindings());
        assertEquals(mapType.getSuperClass(), newMapType.getSuperClass());
        assertEquals(mapType.getSuperInterfaces(), newMapType.getSuperInterfaces());
        assertEquals(String.class, newMapType.getKeyType());
        assertEquals(mapType.getValueType(), newMapType.getValueType());
        assertEquals(mapType.getValueHandler(), newMapType.getValueHandler());
        assertEquals(mapType.getTypeHandler(), newMapType.getTypeHandler());
        assertEquals(mapType.isAsStatic(), newMapType.isAsStatic());
    }

    @Test
    public void testWithContentType() {
        MapType mapType = MapType.construct(Map.class, TypeBindings.emptyBindings(), null, null, String.class, Integer.class);
        MapType newMapType = mapType.withContentType(Integer.class);
        assertEquals(mapType.getMapType(), newMapType.getMapType());
        assertEquals(mapType.getBindings(), newMapType.getBindings());
        assertEquals(mapType.getSuperClass(), newMapType.getSuperClass());
        assertEquals(mapType.getSuperInterfaces(), newMapType.getSuperInterfaces());
        assertEquals(mapType.getKeyType(), newMapType.getKeyType());
        assertEquals(Integer.class, newMapType.getValueType());
        assertEquals(mapType.getValueHandler(), newMapType.getValueHandler());
        assertEquals(mapType.getTypeHandler(), newMapType.getTypeHandler());
        assertEquals(mapType.isAsStatic(), newMapType.isAsStatic());
    }

    @Test
    public void testWithKeyTypeHandler() {
        MapType mapType = MapType.construct(Map.class, TypeBindings.emptyBindings(), null, null, String.class, Integer.class);
        MapType newMapType = mapType.withKeyTypeHandler("keyTypeHandler");
        assertEquals(mapType.getMapType(), newMapType.getMapType());
        assertEquals(mapType.getBindings(), newMapType.getBindings());
        assertEquals(mapType.getSuperClass(), newMapType.getSuperClass());
        assertEquals(mapType.getSuperInterfaces(), newMapType.getSuperInterfaces());
        assertEquals(mapType.getKeyType().withTypeHandler("keyTypeHandler"), newMapType.getKeyType());
        assertEquals(mapType.getValueType(), newMapType.getValueType());
        assertEquals(mapType.getValueHandler(), newMapType.getValueHandler());
        assertEquals(mapType.getTypeHandler(), newMapType.getTypeHandler());
        assertEquals(mapType.isAsStatic(), newMapType.isAsStatic());
    }

    @Test
    public void testWithKeyValueHandler() {
        MapType mapType = MapType.construct(Map.class, TypeBindings.emptyBindings(), null, null, String.class, Integer.class);
        MapType newMapType = mapType.withKeyValueHandler("keyValueHandler");
        assertEquals(mapType.getMapType(), newMapType.getMapType());
        assertEquals(mapType.getBindings(), newMapType.getBindings());
        assertEquals(mapType.getSuperClass(), newMapType.getSuperClass());
        assertEquals(mapType.getSuperInterfaces(), newMapType.getSuperInterfaces());
        assertEquals(mapType.getKeyType().withValueHandler("keyValueHandler"), newMapType.getKeyType());
        assertEquals(mapType.getValueType(), newMapType.getValueType());
        assertEquals(mapType.getValueHandler(), newMapType.getValueHandler());
        assertEquals(mapType.getTypeHandler(), newMapType.getTypeHandler());
        assertEquals(mapType.isAsStatic(), newMapType.isAsStatic());
    }

    @Test
    public void testWithStaticTyping() {
        MapType mapType = MapType.construct(Map.class, TypeBindings.emptyBindings(), null, null, String.class, Integer.class);
        MapType newMapType = mapType.withStaticTyping();
        assertEquals(mapType.getMapType(), newMapType.getMapType());
        assertEquals(mapType.getBindings(), newMapType.getBindings());
        assertEquals(mapType.getSuperClass(), newMapType.getSuperClass());
        assertEquals(mapType.getSuperInterfaces(), newMapType.getSuperInterfaces());
        assertEquals(mapType.getKeyType().withStaticTyping(), newMapType.getKeyType());
        assertEquals(mapType.getValueType().withStaticTyping(), newMapType.getValueType());
        assertEquals(mapType.getValueHandler(), newMapType.getValueHandler());
        assertEquals(mapType.getTypeHandler(), newMapType.getTypeHandler());
        assertEquals(true, newMapType.isAsStatic());
    }

    @Test
    public void testRefine() {
        MapType mapType = MapType.construct(Map.class, TypeBindings.emptyBindings(), null, null, String.class, Integer.class);
        MapType newMapType = mapType.refine(HashMap.class, TypeBindings.emptyBindings(), null, null);
        assertEquals(HashMap.class, newMapType.getMapType());
        assertEquals(mapType.getBindings(), newMapType.getBindings());
        assertEquals(mapType.getSuperClass(), newMapType.getSuperClass());
        assertEquals(mapType.getSuperInterfaces(), newMapType.getSuperInterfaces());
        assertEquals(mapType.getKeyType(), newMapType.getKeyType());
        assertEquals(mapType.getValueType(), newMapType.getValueType());
        assertEquals(mapType.getValueHandler(), newMapType.getValueHandler());
        assertEquals(mapType.getTypeHandler(), newMapType.getTypeHandler());
        assertEquals(mapType.isAsStatic(), newMapType.isAsStatic());
    }

    @Test
    public void testToString() {
        MapType mapType = MapType.construct(Map.class, TypeBindings.emptyBindings(), null, null, String.class, Integer.class);
        assertEquals("[map type; class java.util.Map, class java.lang.String -> class java.lang.Integer]", mapType.toString());
    }
}