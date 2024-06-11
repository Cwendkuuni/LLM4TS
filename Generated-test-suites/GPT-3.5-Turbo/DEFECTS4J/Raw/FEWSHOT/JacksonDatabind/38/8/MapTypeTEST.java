package com.fasterxml.jackson.databind.type;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class MapTypeTest {

    @Test
    public void testConstruct() {
        MapType mapType = MapType.construct(java.util.Map.class, null, null, null, String.class, Integer.class);
        assertNotNull(mapType);
        assertEquals(java.util.Map.class, mapType.getRawClass());
        assertNull(mapType.getSuperClass());
        assertNull(mapType.getSuperInterfaces());
        assertEquals(String.class, mapType.getKeyType());
        assertEquals(Integer.class, mapType.getValueType());
        assertNull(mapType.getTypeHandler());
    }

    @Test
    public void testWithStaticTyping() {
        MapType mapType = MapType.construct(java.util.Map.class, null, null, null, String.class, Integer.class);
        assertNotSame(mapType, mapType.withStaticTyping());
        assertEquals(mapType.getRawClass(), mapType.withStaticTyping().getRawClass());
        assertEquals(mapType.getKeyType(), mapType.withStaticTyping().getKeyType());
        assertEquals(mapType.getValueType(), mapType.withStaticTyping().getValueType());
        assertNull(mapType.withStaticTyping().getSuperClass());
        assertNull(mapType.withStaticTyping().getSuperInterfaces());
        assertNull(mapType.withStaticTyping().getTypeHandler());
    }

    @Test
    public void testWithContentType() {
        MapType mapType = MapType.construct(java.util.Map.class, null, null, null, String.class, Integer.class);
        assertEquals(String.class, mapType.getKeyType());
        assertEquals(Integer.class, mapType.getValueType());
        MapType newMapType = mapType.withContentType(Long.class);
        assertEquals(java.util.Map.class, newMapType.getRawClass());
        assertNull(newMapType.getSuperClass());
        assertNull(newMapType.getSuperInterfaces());
        assertEquals(String.class, newMapType.getKeyType());
        assertEquals(Long.class, newMapType.getValueType());
        assertNull(newMapType.getTypeHandler());
    }

    @Test
    public void testWithKeyType() {
        MapType mapType = MapType.construct(java.util.Map.class, null, null, null, String.class, Integer.class);
        assertEquals(String.class, mapType.getKeyType());
        assertEquals(Integer.class, mapType.getValueType());
        MapType newMapType = mapType.withKeyType(Integer.class);
        assertEquals(java.util.Map.class, newMapType.getRawClass());
        assertNull(newMapType.getSuperClass());
        assertNull(newMapType.getSuperInterfaces());
        assertEquals(Integer.class, newMapType.getKeyType());
        assertEquals(Integer.class, newMapType.getValueType());
        assertNull(newMapType.getTypeHandler());
    }
    
    @Test
    public void testRefine() {
        MapType mapType = MapType.construct(java.util.Map.class, null, null, null, String.class, Integer.class);
        assertEquals(String.class, mapType.getKeyType());
        assertEquals(Integer.class, mapType.getValueType());
        MapType newMapType = (MapType) mapType.refine(java.util.HashMap.class, null, null, null);
        assertEquals(java.util.HashMap.class, newMapType.getRawClass());
        assertNull(newMapType.getSuperClass());
        assertNull(newMapType.getSuperInterfaces());
        assertEquals(String.class, newMapType.getKeyType());
        assertEquals(Integer.class, newMapType.getValueType());
        assertNull(newMapType.getTypeHandler());
    }
    
    @Test
    public void testWithKeyTypeHandler() {
        MapType mapType = MapType.construct(java.util.Map.class, null, null, null, String.class, Integer.class);
        mapType = mapType.withKeyTypeHandler("keyHandler");
        assertEquals(String.class, mapType.getKeyType());
        assertEquals(Integer.class, mapType.getValueType());
        assertEquals("keyHandler", mapType.getTypeHandler());
    }
    
    @Test
    public void testWithKeyValueHandler() {
        MapType mapType = MapType.construct(java.util.Map.class, null, null, null, String.class, Integer.class);
        mapType = mapType.withKeyValueHandler("valueHandler");
        assertEquals(String.class, mapType.getKeyType());
        assertEquals(Integer.class, mapType.getValueType());
        assertEquals("valueHandler", mapType.getTypeHandler());
    }
    
    @Test
    public void testToString() {
        MapType mapType = MapType.construct(java.util.Map.class, null, null, null, String.class, Integer.class);
        assertEquals("[map type; class java.util.Map, class java.lang.String -> class java.lang.Integer]", mapType.toString());
    }
}