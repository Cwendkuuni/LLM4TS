package com.fasterxml.jackson.databind.type;

import com.fasterxml.jackson.databind.JavaType;
import org.junit.Test;
import static org.junit.Assert.*;

public class MapTypeTest {

    @Test
    public void testConstruct() {
        MapType mapType = MapType.construct(String.class, null, null, null, String.class, String.class);
        
        assertNotNull(mapType);
        assertEquals("[map type; class java.lang.String, class java.lang.String -> class java.lang.String]", mapType.toString());
    }

    @Test
    public void testWithKeyType() {
        MapType mapType = MapType.construct(String.class, null, null, null, String.class, String.class);
        MapType newMapType = mapType.withKeyType(Integer.class);
        
        assertNotNull(newMapType);
        assertNotSame(mapType, newMapType);
        assertEquals("[map type; class java.lang.String, class java.lang.String -> class java.lang.String]", mapType.toString());
        assertEquals("[map type; class java.lang.String, class java.lang.String -> class java.lang.Integer]", newMapType.toString());
    }
    
    @Test
    public void testWithContentType() {
        MapType mapType = MapType.construct(String.class, null, null, null, String.class, String.class);
        MapType newMapType = mapType.withContentType(Integer.class);
        
        assertNotNull(newMapType);
        assertNotSame(mapType, newMapType);
        assertEquals("[map type; class java.lang.String, class java.lang.String -> class java.lang.String]", mapType.toString());
        assertEquals("[map type; class java.lang.String, class java.lang.Integer -> class java.lang.String]", newMapType.toString());
    }
    
    @Test
    public void testWithKeyTypeHandler() {
        MapType mapType = MapType.construct(String.class, null, null, null, String.class, String.class);
        MapType newMapType = mapType.withKeyTypeHandler("handler");
        
        assertNotNull(newMapType);
        assertNotSame(mapType, newMapType);
        assertEquals("[map type; class java.lang.String, class java.lang.String -> class java.lang.String]", mapType.toString());
        assertEquals("[map type; class java.lang.String, class java.lang.String -> class java.lang.String]", newMapType.toString());
    }
    
    @Test
    public void testWithKeyValueHandler() {
        MapType mapType = MapType.construct(String.class, null, null, null, String.class, String.class);
        MapType newMapType = mapType.withKeyValueHandler("handler");
        
        assertNotNull(newMapType);
        assertNotSame(mapType, newMapType);
        assertEquals("[map type; class java.lang.String, class java.lang.String -> class java.lang.String]", mapType.toString());
        assertEquals("[map type; class java.lang.String, class java.lang.String -> class java.lang.String]", newMapType.toString());
    }

}