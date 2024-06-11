
package com.fasterxml.jackson.databind.type;

import org.junit.Test;
import static org.junit.Assert.*;

public class MapLikeTypeTest {
    
    @Test
    public void testIsContainerType() {
        MapLikeType type = new MapLikeType(Map.class, null, null, null, null, null, null, null, false);
        assertTrue(type.isContainerType());
    }
    
    @Test
    public void testIsMapLikeType() {
        MapLikeType type = new MapLikeType(Map.class, null, null, null, null, null, null, null, false);
        assertTrue(type.isMapLikeType());
    }
    
    @Test
    public void testKeyType() {
        JavaType keyType = new SimpleType(String.class);
        MapLikeType type = new MapLikeType(Map.class, null, null, null, keyType, null, null, null, false);
        assertEquals(keyType, type.getKeyType());
    }
    
    @Test
    public void testContentType() {
        JavaType contentType = new SimpleType(Integer.class);
        MapLikeType type = new MapLikeType(Map.class, null, null, null, null, contentType, null, null, false);
        assertEquals(contentType, type.getContentType());
    }
    
    @Test
    public void testContentValueHandler() {
        Object valueHandler = new Object();
        MapLikeType type = new MapLikeType(Map.class, null, null, null, null, null, valueHandler, null, false);
        assertEquals(valueHandler, type.getContentValueHandler());
    }
    
    @Test
    public void testContentTypeHandler() {
        Object typeHandler = new Object();
        MapLikeType type = new MapLikeType(Map.class, null, null, null, null, null, null, typeHandler, false);
        assertEquals(typeHandler, type.getContentTypeHandler());
    }
    
    @Test
    public void testHasHandlers() {
        JavaType keyType = new SimpleType(String.class);
        JavaType contentType = new SimpleType(Integer.class);
        Object valueHandler = new Object();
        Object typeHandler = new Object();
        
        MapLikeType type = new MapLikeType(Map.class, null, null, null, keyType, contentType, valueHandler, typeHandler, false);
        assertTrue(type.hasHandlers());
    }
    
    @Test
    public void testToString() {
        JavaType keyType = new SimpleType(String.class);
        JavaType contentType = new SimpleType(Integer.class);
        
        MapLikeType type = new MapLikeType(Map.class, null, null, null, keyType, contentType, null, null, false);
        String expected = "[map-like type; class java.util.Map, java.lang.String -> java.lang.Integer]";
        assertEquals(expected, type.toString());
    }
    
    @Test
    public void testEquals() {
        JavaType keyType = new SimpleType(String.class);
        JavaType contentType = new SimpleType(Integer.class);
        
        MapLikeType type1 = new MapLikeType(Map.class, null, null, null, keyType, contentType, null, null, false);
        MapLikeType type2 = new MapLikeType(Map.class, null, null, null, keyType, contentType, null, null, false);
        
        assertTrue(type1.equals(type2));
    }
}
