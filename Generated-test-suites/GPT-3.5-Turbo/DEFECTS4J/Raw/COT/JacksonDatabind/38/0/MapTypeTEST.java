
package com.example.test;

import static org.junit.Assert.*;
import org.junit.Test;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.MapType;

public class MapTypeTest {


    @Test
    public void testConstruct() {
        MapType mapType = MapType.construct(Map.class, new TypeBindings(), null, null, String.class, Integer.class);
        
        assertEquals(Map.class, mapType.getRawClass());
        assertEquals(String.class, mapType.getKeyType());
        assertEquals(Integer.class, mapType.getValueType());
    }
    
    @Test
    public void testWithKeyType() {
        MapType mapType = MapType.construct(Map.class, new TypeBindings(), null, null, String.class, Integer.class);
        
        MapType newMapType = mapType.withKeyType(Long.class);
        
        assertEquals(Map.class, newMapType.getRawClass());
        assertEquals(Long.class, newMapType.getKeyType());
        assertEquals(Integer.class, newMapType.getValueType());
    }
    
    @Test
    public void testWithContentType() {
        MapType mapType = MapType.construct(Map.class, new TypeBindings(), null, null, String.class, Integer.class);
        
        MapType newMapType = mapType.withContentType(Double.class);
        
        assertEquals(Map.class, newMapType.getRawClass());
        assertEquals(String.class, newMapType.getKeyType());
        assertEquals(Double.class, newMapType.getValueType());
    }
    
    @Test
    public void testWithKeyTypeHandler() {
        MapType mapType = MapType.construct(Map.class, new TypeBindings(), null, null, String.class, Integer.class);
        
        MapType newMapType = mapType.withKeyTypeHandler(new KeyTypeHandler());
        
        assertEquals(Map.class, newMapType.getRawClass());
        assertEquals(String.class, newMapType.getKeyType());
        assertEquals(Integer.class, newMapType.getValueType());
        assertEquals(new KeyTypeHandler(), newMapType.getKeyType().getTypeHandler());
    }
    
    @Test
    public void testWithKeyValueHandler() {
        MapType mapType = MapType.construct(Map.class, new TypeBindings(), null, null, String.class, Integer.class);
        
        MapType newMapType = mapType.withKeyValueHandler(new KeyValueHandler());
        
        assertEquals(Map.class, newMapType.getRawClass());
        assertEquals(String.class, newMapType.getKeyType());
        assertEquals(Integer.class, newMapType.getValueType());
        assertEquals(new KeyValueHandler(), newMapType.getValueType().getValueHandler());
    }
    
    @Test
    public void testWithStaticTyping() {
        MapType mapType = MapType.construct(Map.class, new TypeBindings(), null, null, String.class, Integer.class);
        
        MapType newMapType = mapType.withStaticTyping();
        
        assertEquals(Map.class, newMapType.getRawClass());
        assertEquals(String.class, newMapType.getKeyType());
        assertEquals(Integer.class, newMapType.getValueType());
        assertTrue(newMapType.getKeyType().isStatic());
        assertTrue(newMapType.getValueType().isStatic());
    }
    
    @Test
    public void testToString() {
        MapType mapType = MapType.construct(Map.class, new TypeBindings(), null, null, String.class, Integer.class);
        
        String expected = "[map type; class java.util.Map, class java.lang.String -> class java.lang.Integer]";
        assertEquals(expected, mapType.toString());
    }
    
    private class KeyTypeHandler { }
    
    private class KeyValueHandler { }

}
