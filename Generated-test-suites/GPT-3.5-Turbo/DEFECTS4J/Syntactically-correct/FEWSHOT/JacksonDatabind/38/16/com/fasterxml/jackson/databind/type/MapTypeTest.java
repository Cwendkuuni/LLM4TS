package com.fasterxml.jackson.databind.type;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class MapTypeTest {
    @Test
    public void testConstruct() {
        // test for non-null values
        MapType mapType1 = MapType.construct(HashMap.class, null, Object.class, null, String.class, Integer.class);
        assertNotNull(mapType1);
        
        MapType mapType2 = MapType.construct(LinkedHashMap.class, null, Object.class, null, String.class, Integer.class);
        assertNotNull(mapType2);
        
        // test for null values
        MapType mapType3 = MapType.construct(null, null, null, null, null, null);
        assertNull(mapType3);
    }
    
    @Test
    public void testWithKeyType() {
        MapType mapType = MapType.construct(HashMap.class, null, Object.class, null, String.class, Integer.class);
        
        // test with same key type
        MapType newMapType1 = mapType.withKeyType(String.class);
        assertNotNull(newMapType1);
        assertEquals(newMapType1.getKeyType(), String.class);
        
        // test with different key type
        MapType newMapType2 = mapType.withKeyType(Integer.class);
        assertNotNull(newMapType2);
        assertEquals(newMapType2.getKeyType(), Integer.class);
    }
    
    @Test
    public void testWithContentType() {
        MapType mapType = MapType.construct(HashMap.class, null, Object.class, null, String.class, Integer.class);
        
        // test with same content type
        MapType newMapType1 = mapType.withContentType(Integer.class);
        assertNotNull(newMapType1);
        assertEquals(newMapType1.getContentType(), Integer.class);
        
        // test with different content type
        MapType newMapType2 = mapType.withContentType(String.class);
        assertNotNull(newMapType2);
        assertEquals(newMapType2.getContentType(), String.class);
    }
    
    @Test
    public void testWithKeyValueHandler() {
        MapType mapType = MapType.construct(HashMap.class, null, Object.class, null, String.class, Integer.class);
        
        // test with non-null value handler
        MapType newMapType1 = mapType.withKeyValueHandler(new ValueHandler());
        assertNotNull(newMapType1);
        assertEquals(newMapType1.getValueHandler().getClass(), ValueHandler.class);
        
        // test with null value handler
        MapType newMapType2 = mapType.withKeyValueHandler(null);
        assertNotNull(newMapType2);
        assertNull(newMapType2.getValueHandler());
    }
    
    @Test
    public void testWithKeyTypeHandler() {
        MapType mapType = MapType.construct(HashMap.class, null, Object.class, null, String.class, Integer.class);
        
        // test with non-null type handler
        MapType newMapType1 = mapType.withKeyTypeHandler(new TypeHandler());
        assertNotNull(newMapType1);
        assertEquals(newMapType1.getKeyTypeHandler().getClass(), TypeHandler.class);
        
        // test with null type handler
        MapType newMapType2 = mapType.withKeyTypeHandler(null);
        assertNotNull(newMapType2);
        assertNull(newMapType2.getKeyTypeHandler());
    }
    
    @Test
    public void testWithContentValueHandler() {
        MapType mapType = MapType.construct(HashMap.class, null, Object.class, null, String.class, Integer.class);
        
        // test with non-null value handler
        MapType newMapType1 = mapType.withContentValueHandler(new ValueHandler());
        assertNotNull(newMapType1);
        assertEquals(newMapType1.getContentValueHandler().getClass(), ValueHandler.class);
        
        // test with null value handler
        MapType newMapType2 = mapType.withContentValueHandler(null);
        assertNotNull(newMapType2);
        assertNull(newMapType2.getContentValueHandler());
    }
    
    @Test
    public void testWithStaticTyping() {
        MapType mapType = MapType.construct(HashMap.class, null, Object.class, null, String.class, Integer.class);
        
        // test with static typing enabled
        MapType newMapType1 = mapType.withStaticTyping();
        assertNotNull(newMapType1);
        assertEquals(newMapType1.isStaticTyping(), true);
        
        // test with static typing disabled
        MapType newMapType2 = mapType.withStaticTyping();
        assertNotNull(newMapType2);
        assertEquals(newMapType2.isStaticTyping(), false);
    }
    
    @Test
    public void testRefine() {
        MapType mapType = MapType.construct(HashMap.class, null, Object.class, null, String.class, Integer.class);
        
        JavaType refinedType = mapType.refine(LinkedHashMap.class, null, Object.class, null);
        assertNotNull(refinedType);
        assertEquals(refinedType.getRawClass(), LinkedHashMap.class);
    }
    
    @Test
    public void testToString() {
        MapType mapType = MapType.construct(HashMap.class, null, Object.class, null, String.class, Integer.class);
        String str = mapType.toString();
        assertEquals(str, "[map type; class java.util.HashMap, java.lang.String -> java.lang.Integer]");
    }
}