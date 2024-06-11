package com.fasterxml.jackson.databind.type;

import org.junit.Test;

import java.util.Map;

import com.fasterxml.jackson.databind.JavaType;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class MapLikeTypeTest {

    @Test
    public void testIsTrueMapType() {
        MapLikeType mapLikeType = new MapLikeType(Map.class, null, null, null, null, null, null, null, false);
        assertTrue(mapLikeType.isTrueMapType());

        mapLikeType = new MapLikeType(MapLikeType.class, null, null, null, null, null, null, null, false);
        assertFalse(mapLikeType.isTrueMapType());
    }

    @Test 
    public void testHasKeyType() {
        JavaType keyType = new JavaType();
        JavaType valueType = new JavaType();
        MapLikeType mapLikeType = new MapLikeType(Map.class, null, null, null, keyType, valueType, null, null, false);
        assertEquals(keyType, mapLikeType.getKeyType());
    }

    @Test 
    public void testHasContentType() {
        JavaType keyType = new JavaType();
        JavaType valueType = new JavaType();
        MapLikeType mapLikeType = new MapLikeType(Map.class, null, null, null, keyType, valueType, null, null, false);
        assertEquals(valueType, mapLikeType.getContentType());
    }

    @Test 
    public void testHasContentValueHandler() {
        JavaType keyType = new JavaType();
        JavaType valueType = new JavaType();
        Object valueHandler = new Object();
        MapLikeType mapLikeType = new MapLikeType(Map.class, null, null, null, keyType, valueType, valueHandler, null, false);
        assertEquals(valueHandler, mapLikeType.getContentValueHandler());
    }

    @Test 
    public void testHasContentTypeHandler() {
        JavaType keyType = new JavaType();
        JavaType valueType = new JavaType();
        Object typeHandler = new Object();
        MapLikeType mapLikeType = new MapLikeType(Map.class, null, null, null, keyType, valueType, null, typeHandler, false);
        assertEquals(typeHandler, mapLikeType.getContentTypeHandler());
    }

}