package com.fasterxml.jackson.databind.type;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.Map;

import org.junit.Test;

public class MapLikeTypeTest {
    @Test
    public void testIsContainerType() {
        MapLikeType mapType = new MapLikeType(Map.class, null, null, null,
                null, null, null, null, false);
        assertTrue(mapType.isContainerType());
    }
    
    @Test
    public void testIsMapLikeType() {
        MapLikeType mapType = new MapLikeType(Map.class, null, null, null,
                null, null, null, null, false);
        assertTrue(mapType.isMapLikeType());
    }
    
    @Test
    public void testGetKeyType() {
        JavaType keyType = new SimpleType(Object.class);
        JavaType valueType = new SimpleType(Object.class);
        MapLikeType mapType = new MapLikeType(Map.class, null, null, null,
                keyType, valueType, null, null, false);
        assertEquals(keyType, mapType.getKeyType());
    }
    
    @Test
    public void testGetContentType() {
        JavaType keyType = new SimpleType(Object.class);
        JavaType valueType = new SimpleType(Object.class);
        MapLikeType mapType = new MapLikeType(Map.class, null, null, null,
                keyType, valueType, null, null, false);
        assertEquals(valueType, mapType.getContentType());
    }
    
    @Test
    public void testGetContentValueHandler() {
        Object valueHandler = new Object();
        JavaType keyType = new SimpleType(Object.class);
        JavaType valueType = new SimpleType(Object.class, null, null,
                null, null, valueHandler, null, null, false);
        MapLikeType mapType = new MapLikeType(Map.class, null, null, null,
                keyType, valueType, null, null, false);
        assertEquals(valueHandler, mapType.getContentValueHandler());
    }
    
    @Test
    public void testGetContentTypeHandler() {
        Object typeHandler = new Object();
        JavaType keyType = new SimpleType(Object.class);
        JavaType valueType = new SimpleType(Object.class, null, null,
                null, null, null, typeHandler, null, false);
        MapLikeType mapType = new MapLikeType(Map.class, null, null, null,
                keyType, valueType, null, null, false);
        assertEquals(typeHandler, mapType.getContentTypeHandler());
    }
    
    @Test
    public void testHasHandlers() {
        JavaType keyType = new SimpleType(Object.class, null, null,
                null, null, null, null, null, false);
        JavaType valueType = new SimpleType(Object.class, null, null,
                null, null, null, null, null, false);
        MapLikeType mapType = new MapLikeType(Map.class, null, null, null,
                keyType, valueType, null, null, false);
        assertFalse(mapType.hasHandlers());
        
        Object valueHandler = new Object();
        valueType.setValueHandler(valueHandler);
        assertTrue(mapType.hasHandlers());
    }
    
    @Test
    public void testToString() {
        JavaType keyType = new SimpleType(Object.class);
        JavaType valueType = new SimpleType(Object.class);
        MapLikeType mapType = new MapLikeType(Map.class, null, null, null,
                keyType, valueType, null, null, false);
        assertEquals("[map-like type; class java.util.Map, java.lang.Object -> java.lang.Object]",
                mapType.toString());
    }
    
    @Test
    public void testEquals() {
        JavaType keyType1 = new SimpleType(Object.class);
        JavaType valueType1 = new SimpleType(Object.class);
        MapLikeType mapType1 = new MapLikeType(Map.class, null, null, null,
                keyType1, valueType1, null, null, false);
        
        JavaType keyType2 = new SimpleType(String.class);
        JavaType valueType2 = new SimpleType(Integer.class);
        MapLikeType mapType2 = new MapLikeType(Map.class, null, null, null,
                keyType2, valueType2, null, null, false);
        
        assertFalse(mapType1.equals(mapType2));
        assertFalse(mapType2.equals(mapType1));
        
        MapLikeType mapType3 = new MapLikeType(Map.class, null, null, null,
                keyType1, valueType1, null, null, false);
        
        assertTrue(mapType1.equals(mapType3));
        assertTrue(mapType3.equals(mapType1));
    }
}