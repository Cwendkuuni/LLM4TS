package com.fasterxml.jackson.databind.type;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;
import com.fasterxml.jackson.databind.JavaType;

public class MapTypeTest {
    @Test
    public void testConstruct() {
        MapType mapType = MapType.construct(HashMap.class, new TypeBindings(), null, null, String.class, Integer.class);
        assertEquals(HashMap.class, mapType.getRawClass());
        assertNull(mapType.getSuperClass());
        assertNull(mapType.getSuperInterfaces());
        assertEquals(String.class, mapType.getKeyType());
        assertEquals(Integer.class, mapType.getValueType());
        assertNull(mapType.getValueHandler());
        assertNull(mapType.getTypeHandler());
        assertEquals(false, mapType.isAsStatic());
    }
    
    @Test
    public void testConstructWithBogusSuperClass() {
        MapType mapType = MapType.construct(HashMap.class, String.class, Integer.class);
        assertEquals(HashMap.class, mapType.getRawClass());
        assertEquals(TypeBase.class, mapType.getSuperClass());
        assertNull(mapType.getSuperInterfaces());
        assertEquals(String.class, mapType.getKeyType());
        assertEquals(Integer.class, mapType.getValueType());
        assertNull(mapType.getValueHandler());
        assertNull(mapType.getTypeHandler());
        assertEquals(false, mapType.isAsStatic());
    }

    @Test
    public void testNarrow() {
        MapType mapType = MapType._narrow(HashMap.class);
        assertEquals(HashMap.class, mapType.getRawClass());
        assertEquals(null, mapType.getSuperClass());
        assertEquals(null, mapType.getSuperInterfaces());
        assertEquals(null, mapType.getKeyType());
        assertEquals(null, mapType.getValueType());
        assertEquals(null, mapType.getValueHandler());
        assertEquals(null, mapType.getTypeHandler());
        assertEquals(false, mapType.isAsStatic());
    }

    @Test
    public void testWithTypeHandler() {
        MapType mapType = new MapType(HashMap.class, new TypeBindings(), null, null, String.class, Integer.class);
        mapType = mapType.withTypeHandler("handler");
        assertEquals(HashMap.class, mapType.getRawClass());
        assertEquals(null, mapType.getSuperClass());
        assertEquals(null, mapType.getSuperInterfaces());
        assertEquals(String.class, mapType.getKeyType());
        assertEquals(Integer.class, mapType.getValueType());
        assertEquals(null, mapType.getValueHandler());
        assertEquals("handler", mapType.getTypeHandler());
        assertEquals(false, mapType.isAsStatic());
    }

    @Test
    public void testWithContentTypeHandler() {
        MapType mapType = new MapType(HashMap.class, new TypeBindings(), null, null, String.class, Integer.class);
        mapType = mapType.withContentTypeHandler("handler");
        assertEquals(HashMap.class, mapType.getRawClass());
        assertEquals(null, mapType.getSuperClass());
        assertEquals(null, mapType.getSuperInterfaces());
        assertEquals(String.class, mapType.getKeyType());
        assertEquals(Integer.class, mapType.getValueType());
        assertEquals(null, mapType.getValueHandler());
        assertEquals(null, mapType.getTypeHandler());
        assertEquals(false, mapType.isAsStatic());
        assertEquals("handler", mapType.getValueType().getTypeHandler());
    }
    
    @Test
    public void testWithValueHandler() {
        MapType mapType = new MapType(HashMap.class, new TypeBindings(), null, null, String.class, Integer.class);
        mapType = mapType.withValueHandler("handler");
        assertEquals(HashMap.class, mapType.getRawClass());
        assertEquals(null, mapType.getSuperClass());
        assertEquals(null, mapType.getSuperInterfaces());
        assertEquals(String.class, mapType.getKeyType());
        assertEquals(Integer.class, mapType.getValueType());
        assertEquals("handler", mapType.getValueHandler());
        assertEquals(null, mapType.getTypeHandler());
        assertEquals(false, mapType.isAsStatic());
    }

    @Test
    public void testWithContentValueHandler() {
        MapType mapType = new MapType(HashMap.class, new TypeBindings(), null, null, String.class, Integer.class);
        mapType = mapType.withContentValueHandler("handler");
        assertEquals(HashMap.class, mapType.getRawClass());
        assertEquals(null, mapType.getSuperClass());
        assertEquals(null, mapType.getSuperInterfaces());
        assertEquals(String.class, mapType.getKeyType());
        assertEquals(Integer.class, mapType.getValueType());
        assertEquals(null, mapType.getValueHandler());
        assertEquals(null, mapType.getTypeHandler());
        assertEquals(false, mapType.isAsStatic());
        assertEquals("handler", mapType.getValueType().getValueHandler());
    }

    @Test
    public void testWithStaticTyping() {
        MapType mapType = new MapType(HashMap.class, new TypeBindings(), null, null, String.class, Integer.class);
        mapType = mapType.withStaticTyping();
        assertEquals(HashMap.class, mapType.getRawClass());
        assertEquals(null, mapType.getSuperClass());
        assertEquals(null, mapType.getSuperInterfaces());
        assertEquals(String.class, mapType.getKeyType());
        assertEquals(Integer.class, mapType.getValueType());
        assertEquals(null, mapType.getValueHandler());
        assertEquals(null, mapType.getTypeHandler());
        assertEquals(true, mapType.isAsStatic());
    }

    @Test
    public void testWithContentType() {
        MapType mapType = new MapType(HashMap.class, new TypeBindings(), null, null, String.class, Integer.class);
        mapType = mapType.withContentType(Long.class);
        assertEquals(HashMap.class, mapType.getRawClass());
        assertEquals(null, mapType.getSuperClass());
        assertEquals(null, mapType.getSuperInterfaces());
        assertEquals(String.class, mapType.getKeyType());
        assertEquals(Long.class, mapType.getValueType());
        assertEquals(null, mapType.getValueHandler());
        assertEquals(null, mapType.getTypeHandler());
        assertEquals(false, mapType.isAsStatic());
    }

    @Test
    public void testWithKeyType() {
        MapType mapType = new MapType(HashMap.class, new TypeBindings(), null, null, String.class, Integer.class);
        mapType = mapType.withKeyType(Long.class);
        assertEquals(HashMap.class, mapType.getRawClass());
        assertEquals(null, mapType.getSuperClass());
        assertEquals(null, mapType.getSuperInterfaces());
        assertEquals(Long.class, mapType.getKeyType());
        assertEquals(Integer.class, mapType.getValueType());
        assertEquals(null, mapType.getValueHandler());
        assertEquals(null, mapType.getTypeHandler());
        assertEquals(false, mapType.isAsStatic());
    }

    @Test
    public void testRefine() {
        MapType mapType = new MapType(HashMap.class, new TypeBindings(), null, null, String.class, Integer.class);
        JavaType[] superInterfaces = {Collection.class, List.class};
        mapType = mapType.refine(Map.class, new TypeBindings(), List.class, superInterfaces);
        assertEquals(Map.class, mapType.getRawClass());
        assertEquals(new TypeBindings(), mapType.getBindings());
        assertEquals(List.class, mapType.getSuperClass());
        assertEquals(superInterfaces, mapType.getSuperInterfaces());
        assertEquals(String.class, mapType.getKeyType());
        assertEquals(Integer.class, mapType.getValueType());
        assertEquals(null, mapType.getValueHandler());
        assertEquals(null, mapType.getTypeHandler());
        assertEquals(false, mapType.isAsStatic());
    }

    @Test
    public void testWithKeyTypeHandler() {
        MapType mapType = new MapType(HashMap.class, new TypeBindings(), null, null, String.class, Integer.class);
        mapType = mapType.withKeyTypeHandler("handler");
        assertEquals(HashMap.class, mapType.getRawClass());
        assertEquals(null, mapType.getSuperClass());
        assertEquals(null, mapType.getSuperInterfaces());
        assertEquals(String.class, mapType.getKeyType());
        assertEquals(Integer.class, mapType.getValueType());
        assertEquals("handler", mapType.getKeyType().getTypeHandler());
        assertEquals(null, mapType.getTypeHandler());
        assertEquals(false, mapType.isAsStatic());
    }

    @Test
    public void testWithKeyValueHandler() {
        MapType mapType = new MapType(HashMap.class, new TypeBindings(), null, null, String.class, Integer.class);
        mapType = mapType.withKeyValueHandler("handler");
        assertEquals(HashMap.class, mapType.getRawClass());
        assertEquals(null, mapType.getSuperClass());
        assertEquals(null, mapType.getSuperInterfaces());
        assertEquals(String.class, mapType.getKeyType());
        assertEquals(Integer.class, mapType.getValueType());
        assertEquals("handler", mapType.getKeyType().getValueHandler());
        assertEquals(null, mapType.getTypeHandler());
        assertEquals(false, mapType.isAsStatic());
    }

    @Test
    public void testToString() {
        MapType mapType = new MapType(HashMap.class, new TypeBindings(), null, null, String.class, Integer.class);
        assertEquals("[map type; class java.util.HashMap, java.lang.String -> java.lang.Integer]", mapType.toString());
    }
}