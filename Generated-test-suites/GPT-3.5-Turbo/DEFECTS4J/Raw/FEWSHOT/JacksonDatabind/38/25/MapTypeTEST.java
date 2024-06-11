package com.fasterxml.jackson.databind.type;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;

public class MapTypeTest {
    @Test
    public void testConstruct() {
        MapType mapType = MapType.construct(java.util.Map.class, null, null, null,
                null, null);
        assertNotNull(mapType);
    }
    
    @Test
    public void testWithContentType() {
        MapType mapType = new MapType(java.util.Map.class, null, null, null, null,
                null, null, null, false);
        JavaType contentType = new JavaType(Object.class);
        assertEquals(mapType, mapType.withContentType(contentType));
    }
    
    @Test
    public void testWithKeyType() {
        MapType mapType = new MapType(java.util.Map.class, null, null, null, null,
                null, null, null, false);
        JavaType keyType = new JavaType(Object.class);
        assertEquals(mapType, mapType.withKeyType(keyType));
    }
    
    @Test
    public void testWithKeyTypeHandler() {
        MapType mapType = new MapType(java.util.Map.class, null, null, null, null,
                null, null, null, false);
        Object keyTypeHandler = new Object();
        assertEquals(mapType, mapType.withKeyTypeHandler(keyTypeHandler));
    }
    
    @Test
    public void testWithKeyValueHandler() {
        MapType mapType = new MapType(java.util.Map.class, null, null, null, null,
                null, null, null, false);
        Object keyValueHandler = new Object();
        assertEquals(mapType, mapType.withKeyValueHandler(keyValueHandler));
    }
    
    @Test
    public void testWithStaticTyping() {
        MapType mapType = new MapType(java.util.Map.class, null, null, null, null,
                null, null, null, false);
        assertEquals(mapType, mapType.withStaticTyping());
    }
    
    @Test
    public void testRefine() {
        MapType mapType = new MapType(java.util.Map.class, null, null, null, null,
                null, null, null, false);
        Class<?> rawType = java.util.Map.class;
        TypeBindings bindings = null;
        JavaType superClass = null;
        JavaType[] superInterfaces = null;
        assertEquals(mapType, mapType.refine(rawType, bindings, superClass, superInterfaces));
    }
    
    @Test
    public void testToString() {
        MapType mapType = new MapType(java.util.Map.class, null, null, null, null,
                null, null, null, false);
        String expected = "[map type; class java.util.Map, null -> null]";
        assertEquals(expected, mapType.toString());
    }
    
}