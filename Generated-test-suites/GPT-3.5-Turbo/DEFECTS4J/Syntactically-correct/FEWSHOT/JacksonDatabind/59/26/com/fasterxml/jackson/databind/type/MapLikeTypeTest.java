package com.fasterxml.jackson.databind.type;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class MapLikeTypeTest {
    @Test
    public void testIsContainerType() {
        MapLikeType type = new MapLikeType(String.class, null, null, null, null, null, null, null, false);
        assertTrue(type.isContainerType());
    }
    
    @Test
    public void testIsMapLikeType() {
        MapLikeType type = new MapLikeType(String.class, null, null, null, null, null, null, null, false);
        assertTrue(type.isMapLikeType());
    }
    
    @Test
    public void testGetKeyType() {
        JavaType keyType = new JavaType(String.class);
        JavaType valueType = new JavaType(Integer.class);
        MapLikeType type = new MapLikeType(String.class, null, null, null, keyType, valueType, null, null, false);
        assertEquals(keyType, type.getKeyType());
    }
    
    @Test
    public void testGetContentType() {
        JavaType keyType = new JavaType(String.class);
        JavaType valueType = new JavaType(Integer.class);
        MapLikeType type = new MapLikeType(String.class, null, null, null, keyType, valueType, null, null, false);
        assertEquals(valueType, type.getContentType());
    }
    
    @Test
    public void testHasHandlers() {
        JavaType keyType = new JavaType(String.class);
        JavaType valueType = new JavaType(Integer.class);
        MapLikeType type = new MapLikeType(String.class, null, null, null, keyType, valueType, "valueHandler", "typeHandler", false);
        assertTrue(type.hasHandlers());
    }
    
    @Test
    public void testHasHandlers_false() {
        JavaType keyType = new JavaType(String.class);
        JavaType valueType = new JavaType(Integer.class);
        MapLikeType type = new MapLikeType(String.class, null, null, null, keyType, valueType, null, null, false);
        assertFalse(type.hasHandlers());
    }
    
    @Test
    public void testBuildCanonicalName() {
        JavaType keyType = new JavaType(String.class);
        JavaType valueType = new JavaType(Integer.class);
        MapLikeType type = new MapLikeType(String.class, null, null, null, keyType, valueType, null, null, false);
        String expected = "java.lang.String<java.lang.String,java.lang.Integer>";
        assertEquals(expected, type.buildCanonicalName());
    }
    
    @Test
    public void testIsTrueMapType_true() {
        JavaType keyType = new JavaType(String.class);
        JavaType valueType = new JavaType(Integer.class);
        MapLikeType type = new MapLikeType(Map.class, null, null, null, keyType, valueType, null, null, false);
        assertTrue(type.isTrueMapType());
    }
    
    @Test
    public void testIsTrueMapType_false() {
        JavaType keyType = new JavaType(String.class);
        JavaType valueType = new JavaType(Integer.class);
        MapLikeType type = new MapLikeType(String.class, null, null, null, keyType, valueType, null, null, false);
        assertFalse(type.isTrueMapType());
    }
    
    @Test
    public void testToString() {
        JavaType keyType = new JavaType(String.class);
        JavaType valueType = new JavaType(Integer.class);
        MapLikeType type = new MapLikeType(String.class, null, null, null, keyType, valueType, null, null, false);
        String expected = "[map-like type; class java.lang.String, java.lang.String -> java.lang.Integer]";
        assertEquals(expected, type.toString());
    }
    
    @Test
    public void testEquals_true() {
        JavaType keyType = new JavaType(String.class);
        JavaType valueType = new JavaType(Integer.class);
        MapLikeType type1 = new MapLikeType(String.class, null, null, null, keyType, valueType, null, null, false);
        MapLikeType type2 = new MapLikeType(String.class, null, null, null, keyType, valueType, null, null, false);
        assertTrue(type1.equals(type2));
    }
    
    @Test
    public void testEquals_false() {
        JavaType keyType1 = new JavaType(String.class);
        JavaType valueType1 = new JavaType(Integer.class);
        JavaType keyType2 = new JavaType(Integer.class);
        JavaType valueType2 = new JavaType(String.class);
        MapLikeType type1 = new MapLikeType(String.class, null, null, null, keyType1, valueType1, null, null, false);
        MapLikeType type2 = new MapLikeType(Integer.class, null, null, null, keyType2, valueType2, null, null, false);
        assertFalse(type1.equals(type2));
    }
}