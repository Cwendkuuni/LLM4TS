package com.fasterxml.jackson.databind.type;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;

public class MapTypeTest {
    
    @Test
    public void testConstruct() {
        Class<?> rawType = MapType.class;
        TypeBindings bindings = null;
        JavaType superClass = null;
        JavaType[] superInts = null;
        JavaType keyT = null;
        JavaType valueT = null;
        MapType mapType = MapType.construct(rawType, bindings, superClass, superInts, keyT, valueT);
        assertNotNull(mapType);
    }
    
    @Test
    public void testConstructWithTypes() {
        Class<?> rawType = MapType.class;
        JavaType keyT = null;
        JavaType valueT = null;
        MapType mapType = MapType.construct(rawType, keyT, valueT);
        assertNotNull(mapType);
    }
    
    @Test
    public void testNarrow() {
        Class<?> subclass = MapType.class;
        MapType mapType = new MapType(subclass, null, null, null, null, null, null, null, false);
        JavaType narrowType = mapType._narrow(subclass);
        assertEquals(subclass, narrowType.getRawClass());
        assertNotEquals(mapType, narrowType);
    }
    
    @Test
    public void testWithTypeHandler() {
        Object typeHandler = null;
        MapType mapType = new MapType(MapType.class, null, null, null, null, null, null, null, false);
        MapType newMapType = mapType.withTypeHandler(typeHandler);
        assertEquals(typeHandler, newMapType.getTypeHandler());
        assertNotEquals(mapType, newMapType);
    }
    
    @Test
    public void testWithContentTypeHandler() {
        Object typeHandler = null;
        MapType mapType = new MapType(MapType.class, null, null, null, null, null, null, null, false);
        MapType newMapType = mapType.withContentTypeHandler(typeHandler);
        assertEquals(typeHandler, newMapType.getContentTypeHandler());
        assertNotEquals(mapType, newMapType);
    }
    
    @Test
    public void testWithValueHandler() {
        Object valueHandler = null;
        MapType mapType = new MapType(MapType.class, null, null, null, null, null, null, null, false);
        MapType newMapType = mapType.withValueHandler(valueHandler);
        assertEquals(valueHandler, newMapType.getValueHandler());
        assertNotEquals(mapType, newMapType);
    }
    
    @Test
    public void testWithContentValueHandler() {
        Object valueHandler = null;
        MapType mapType = new MapType(MapType.class, null, null, null, null, null, null, null, false);
        MapType newMapType = mapType.withContentValueHandler(valueHandler);
        assertEquals(valueHandler, newMapType.getContentValueHandler());
        assertNotEquals(mapType, newMapType);
    }
    
    @Test
    public void testWithStaticTyping() {
        MapType mapType = new MapType(MapType.class, null, null, null, null, null, null, null, false);
        MapType newMapType = mapType.withStaticTyping();
        assertTrue(newMapType.hasStaticTyping());
        assertNotEquals(mapType, newMapType);
    }
    
    @Test
    public void testWithType() {
        JavaType contentType = null;
        MapType mapType = new MapType(MapType.class, null, null, null, null, null, null, null, false);
        MapType newMapType = mapType.withType(contentType);
        assertEquals(contentType, newMapType.getContentType());
        assertNotEquals(mapType, newMapType);
    }
    
    @Test
    public void testWithKeyType() {
        JavaType keyType = null;
        MapType mapType = new MapType(MapType.class, null, null, null, null, null, null, null, false);
        MapType newMapType = mapType.withKeyType(keyType);
        assertEquals(keyType, newMapType.getKeyType());
        assertNotEquals(mapType, newMapType);
    }
    
    @Test
    public void testRefine() {
        Class<?> rawType = MapType.class;
        TypeBindings bindings = null;
        JavaType superClass = null;
        JavaType[] superInterfaces = null;
        JavaType keyT = null;
        JavaType valueT = null;
        MapType mapType = new MapType(rawType, bindings, superClass, superInterfaces, null, null, null, null, false);
        JavaType refinedType = mapType.refine(rawType, bindings, superClass, superInterfaces);
        assertEquals(refinedType, mapType);
    }
    
    @Test
    public void testWithKeyTypeHandler() {
        Object typeHandler = null;
        MapType mapType = new MapType(MapType.class, null, null, null, null, null, null, null, false);
        MapType newMapType = mapType.withKeyTypeHandler(typeHandler);
        assertEquals(typeHandler, newMapType.getKeyType().getTypeHandler());
        assertNotEquals(mapType, newMapType);
    }
    
    @Test
    public void testWithKeyValueHandler() {
        Object valueHandler = null;
        MapType mapType = new MapType(MapType.class, null, null, null, null, null, null, null, false);
        MapType newMapType = mapType.withKeyValueHandler(valueHandler);
        assertEquals(valueHandler, newMapType.getValueType().getValueHandler());
        assertNotEquals(mapType, newMapType);
    }
    
    @Test
    public void testToString() {
        MapType mapType = new MapType(MapType.class, null, null, null, null, null, null, null, false);
        String expected = "[map type; class "+ mapType.getClass().getName() +", null" +" -> " + mapType.getClass().getName() +"]";
        assertEquals(expected, mapType.toString());
    }
}