package com.fasterxml.jackson.databind.type;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import org.junit.Test;

public class MapTypeTest {
    @Test
    public void testConstruct() {
        MapType mapType = MapType.construct(Map.class, new TypeBindings(), null, null, String.class, Integer.class);
        assertEquals(Map.class, mapType.getRawClass());
        assertNull(mapType.getSuperClass());
        assertNull(mapType.getSuperInterfaces());
        assertEquals(String.class, mapType.getKeyType());
        assertEquals(Integer.class, mapType.getValueType());
        assertNull(mapType.getValueHandler());
        assertNull(mapType.getTypeHandler());
        assertEquals(false, mapType.isStatic());
    }

    @Test
    public void testConstructDeprecated() {
        MapType mapType = MapType.construct(Map.class, String.class, Integer.class);
        assertEquals(Map.class, mapType.getRawClass());
        assertNull(mapType.getSuperClass());
        assertNull(mapType.getSuperInterfaces());
        assertEquals(String.class, mapType.getKeyType());
        assertEquals(Integer.class, mapType.getValueType());
        assertNull(mapType.getValueHandler());
        assertNull(mapType.getTypeHandler());
        assertEquals(false, mapType.isStatic());
    }

    @Test
    public void testWithTypeHandler() {
        MapType mapType = new MapType(Map.class, new TypeBindings(), null, null, String.class, Integer.class)
            .withTypeHandler(Object.class);
        assertEquals(Map.class, mapType.getRawClass());
        assertNull(mapType.getSuperClass());
        assertNull(mapType.getSuperInterfaces());
        assertEquals(String.class, mapType.getKeyType());
        assertEquals(Integer.class, mapType.getValueType());
        assertNull(mapType.getValueHandler());
        assertEquals(Object.class, mapType.getTypeHandler());
        assertEquals(false, mapType.isStatic());
    }

    @Test
    public void testWithContentTypeHandler() {
        MapType mapType = new MapType(Map.class, new TypeBindings(), null, null, String.class, Integer.class)
            .withContentTypeHandler(Object.class);
        assertEquals(Map.class, mapType.getRawClass());
        assertNull(mapType.getSuperClass());
        assertNull(mapType.getSuperInterfaces());
        assertEquals(String.class, mapType.getKeyType());
        assertEquals(Integer.class, mapType.getValueType());
        assertNull(mapType.getValueHandler());
        assertNull(mapType.getTypeHandler());
        assertEquals(false, mapType.isStatic());
        assertEquals(Object.class, mapType.getValueType().getTypeHandler());
    }

    @Test
    public void testWithValueHandler() {
        MapType mapType = new MapType(Map.class, new TypeBindings(), null, null, String.class, Integer.class)
            .withValueHandler(Object.class);
        assertEquals(Map.class, mapType.getRawClass());
        assertNull(mapType.getSuperClass());
        assertNull(mapType.getSuperInterfaces());
        assertEquals(String.class, mapType.getKeyType());
        assertEquals(Integer.class, mapType.getValueType());
        assertEquals(Object.class, mapType.getValueHandler());
        assertNull(mapType.getTypeHandler());
        assertEquals(false, mapType.isStatic());
    }

    @Test
    public void testWithContentValueHandler() {
        MapType mapType = new MapType(Map.class, new TypeBindings(), null, null, String.class, Integer.class)
            .withContentValueHandler(Object.class);
        assertEquals(Map.class, mapType.getRawClass());
        assertNull(mapType.getSuperClass());
        assertNull(mapType.getSuperInterfaces());
        assertEquals(String.class, mapType.getKeyType());
        assertEquals(Integer.class, mapType.getValueType());
        assertEquals(null, mapType.getValueHandler());
        assertNull(mapType.getTypeHandler());
        assertEquals(false, mapType.isStatic());
        assertEquals(Object.class, mapType.getValueType().getValueHandler());
    }

    @Test
    public void testWithStaticTyping() {
        MapType mapType = new MapType(Map.class, new TypeBindings(), null, null, String.class, Integer.class)
            .withStaticTyping();
        assertEquals(Map.class, mapType.getRawClass());
        assertNull(mapType.getSuperClass());
        assertNull(mapType.getSuperInterfaces());
        assertEquals(String.class, mapType.getKeyType());
        assertEquals(Integer.class, mapType.getValueType());
        assertNull(mapType.getValueHandler());
        assertNull(mapType.getTypeHandler());
        assertEquals(true, mapType.isStatic());
    }

    @Test
    public void testWithContentType() {
        MapType mapType = new MapType(Map.class, new TypeBindings(), null, null, String.class, Integer.class)
            .withContentType(Integer.class);
        assertEquals(Map.class, mapType.getRawClass());
        assertNull(mapType.getSuperClass());
        assertNull(mapType.getSuperInterfaces());
        assertEquals(String.class, mapType.getKeyType());
        assertEquals(Integer.class, mapType.getValueType());
        assertNull(mapType.getValueHandler());
        assertNull(mapType.getTypeHandler());
        assertEquals(false, mapType.isStatic());
        assertEquals(Integer.class, mapType.getValueType());
    }

    @Test
    public void testWithKeyType() {
        MapType mapType = new MapType(Map.class, new TypeBindings(), null, null, String.class, Integer.class)
            .withKeyType(String.class);
        assertEquals(Map.class, mapType.getRawClass());
        assertNull(mapType.getSuperClass());
        assertNull(mapType.getSuperInterfaces());
        assertEquals(String.class, mapType.getKeyType());
        assertEquals(Integer.class, mapType.getValueType());
        assertNull(mapType.getValueHandler());
        assertNull(mapType.getTypeHandler());
        assertEquals(false, mapType.isStatic());
        assertEquals(String.class, mapType.getKeyType());
    }

    @Test
    public void testRefine() {
        MapType mapType = new MapType(Map.class, new TypeBindings(), null, null, String.class, Integer.class)
            .refine(HashMap.class, new TypeBindings(), null, null);
        assertEquals(HashMap.class, mapType.getRawClass());
        assertNull(mapType.getSuperClass());
        assertNull(mapType.getSuperInterfaces());
        assertEquals(String.class, mapType.getKeyType());
        assertEquals(Integer.class, mapType.getValueType());
        assertNull(mapType.getValueHandler());
        assertNull(mapType.getTypeHandler());
        assertEquals(false, mapType.isStatic());
    }

    @Test
    public void testWithKeyTypeHandler() {
        MapType mapType = new MapType(Map.class, new TypeBindings(), null, null, String.class, Integer.class)
            .withKeyTypeHandler(Object.class);
        assertEquals(Map.class, mapType.getRawClass());
        assertNull(mapType.getSuperClass());
        assertNull(mapType.getSuperInterfaces());
        assertEquals(String.class, mapType.getKeyType());
        assertEquals(Integer.class, mapType.getValueType());
        assertEquals(Object.class, mapType.getKeyType().getTypeHandler());
        assertNull(mapType.getValueHandler());
        assertNull(mapType.getTypeHandler());
        assertEquals(false, mapType.isStatic());
    }

    @Test
    public void testWithKeyValueHandler() {
        MapType mapType = new MapType(Map.class, new TypeBindings(), null, null, String.class, Integer.class)
            .withKeyValueHandler(Object.class);
        assertEquals(Map.class, mapType.getRawClass());
        assertNull(mapType.getSuperClass());
        assertNull(mapType.getSuperInterfaces());
        assertEquals(String.class, mapType.getKeyType());
        assertEquals(Integer.class, mapType.getValueType());
        assertEquals(Object.class, mapType.getKeyType().getValueHandler());
        assertNull(mapType.getValueHandler());
        assertNull(mapType.getTypeHandler());
        assertEquals(false, mapType.isStatic());
    }

    @Test
    public void testToString() {
        MapType mapType = new MapType(Map.class, new TypeBindings(), null, null, String.class, Integer.class);
        assertEquals("[map type; class java.util.Map, java.lang.String -> java.lang.Integer]", mapType.toString());
    }
}