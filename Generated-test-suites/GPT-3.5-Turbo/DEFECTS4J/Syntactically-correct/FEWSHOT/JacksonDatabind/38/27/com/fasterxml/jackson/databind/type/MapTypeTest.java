package com.fasterxml.jackson.databind.type;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;

public class MapTypeTest {
    @Test
    public void testConstruct() {
        MapType mapType = MapType.construct(Map.class, TypeBindings.emptyBindings(), null, null, String.class, Integer.class);
        assertNotNull(mapType);
        assertEquals(Map.class, mapType.getRawClass());
        assertEquals(TypeBindings.emptyBindings(), mapType.getBindings());
        assertNull(mapType.getSuperClass());
        assertNull(mapType.getSuperInterfaces());
        assertEquals(String.class, mapType.getKeyType());
        assertEquals(Integer.class, mapType.getValueType());
        assertNull(mapType.getValueHandler());
        assertNull(mapType.getTypeHandler());
        assertEquals(false, mapType.isContainerType());
    }
    
    @Test
    public void testWithKeyTypeHandler() {
        Object keyTypeHandler = new Object();
        MapType mapType = new MapType(Map.class, TypeBindings.emptyBindings(), null, null, String.class, Integer.class, null, null, false);
        assertNotNull(mapType);
        MapType newMapType = mapType.withKeyTypeHandler(keyTypeHandler);
        assertNotNull(newMapType);
        assertEquals(mapType.getRawClass(), newMapType.getRawClass());
        assertEquals(mapType.getBindings(), newMapType.getBindings());
        assertEquals(mapType.getSuperClass(), newMapType.getSuperClass());
        assertEquals(mapType.getSuperInterfaces(), newMapType.getSuperInterfaces());
        assertEquals(mapType.getKeyType().withTypeHandler(keyTypeHandler), newMapType.getKeyType());
        assertEquals(mapType.getValueType(), newMapType.getValueType());
        assertEquals(mapType.getValueHandler(), newMapType.getValueHandler());
        assertEquals(mapType.getTypeHandler(), newMapType.getTypeHandler());
        assertEquals(mapType.isContainerType(), newMapType.isContainerType());
    }
    
    @Test
    public void testWithKeyValueHandler() {
        Object keyValueHandler = new Object();
        MapType mapType = new MapType(Map.class, TypeBindings.emptyBindings(), null, null, String.class, Integer.class, null, null, false);
        assertNotNull(mapType);
        MapType newMapType = mapType.withKeyValueHandler(keyValueHandler);
        assertNotNull(newMapType);
        assertEquals(mapType.getRawClass(), newMapType.getRawClass());
        assertEquals(mapType.getBindings(), newMapType.getBindings());
        assertEquals(mapType.getSuperClass(), newMapType.getSuperClass());
        assertEquals(mapType.getSuperInterfaces(), newMapType.getSuperInterfaces());
        assertEquals(mapType.getKeyType().withValueHandler(keyValueHandler), newMapType.getKeyType());
        assertEquals(mapType.getValueType(), newMapType.getValueType());
        assertEquals(mapType.getValueHandler(), newMapType.getValueHandler());
        assertEquals(mapType.getTypeHandler(), newMapType.getTypeHandler());
        assertEquals(mapType.isContainerType(), newMapType.isContainerType());
    }
    
    @Test
    public void testWithStaticTyping() {
        MapType mapType = new MapType(Map.class, TypeBindings.emptyBindings(), null, null, String.class, Integer.class, null, null, false);
        assertNotNull(mapType);
        MapType newMapType = mapType.withStaticTyping();
        assertNotNull(newMapType);
        assertEquals(mapType.getRawClass(), newMapType.getRawClass());
        assertEquals(mapType.getBindings(), newMapType.getBindings());
        assertEquals(mapType.getSuperClass(), newMapType.getSuperClass());
        assertEquals(mapType.getSuperInterfaces(), newMapType.getSuperInterfaces());
        assertEquals(mapType.getKeyType().withStaticTyping(), newMapType.getKeyType());
        assertEquals(mapType.getValueType().withStaticTyping(), newMapType.getValueType());
        assertEquals(mapType.getValueHandler(), newMapType.getValueHandler());
        assertEquals(mapType.getTypeHandler(), newMapType.getTypeHandler());
        assertEquals(true, newMapType.isContainerType());
    }
    
    @Test
    public void testWithContentType() {
        JavaType contentType = new JavaType();
        MapType mapType = new MapType(Map.class, TypeBindings.emptyBindings(), null, null, String.class, Integer.class, null, null, false);
        assertNotNull(mapType);
        MapType newMapType = mapType.withContentType(contentType);
        assertNotNull(newMapType);
        assertEquals(mapType.getRawClass(), newMapType.getRawClass());
        assertEquals(mapType.getBindings(), newMapType.getBindings());
        assertEquals(mapType.getSuperClass(), newMapType.getSuperClass());
        assertEquals(mapType.getSuperInterfaces(), newMapType.getSuperInterfaces());
        assertEquals(mapType.getKeyType(), newMapType.getKeyType());
        assertEquals(contentType, newMapType.getValueType());
        assertEquals(mapType.getValueHandler(), newMapType.getValueHandler());
        assertEquals(mapType.getTypeHandler(), newMapType.getTypeHandler());
        assertEquals(mapType.isContainerType(), newMapType.isContainerType());
    }
    
    @Test
    public void testWithKeyType() {
        JavaType keyType = new JavaType();
        MapType mapType = new MapType(Map.class, TypeBindings.emptyBindings(), null, null, String.class, Integer.class, null, null, false);
        assertNotNull(mapType);
        MapType newMapType = mapType.withKeyType(keyType);
        assertNotNull(newMapType);
        assertEquals(mapType.getRawClass(), newMapType.getRawClass());
        assertEquals(mapType.getBindings(), newMapType.getBindings());
        assertEquals(mapType.getSuperClass(), newMapType.getSuperClass());
        assertEquals(mapType.getSuperInterfaces(), newMapType.getSuperInterfaces());
        assertEquals(keyType, newMapType.getKeyType());
        assertEquals(mapType.getValueType(), newMapType.getValueType());
        assertEquals(mapType.getValueHandler(), newMapType.getValueHandler());
        assertEquals(mapType.getTypeHandler(), newMapType.getTypeHandler());
        assertEquals(mapType.isContainerType(), newMapType.isContainerType());
    }
    
    @Test
    public void testRefine() {
        MapType mapType = new MapType(Map.class, TypeBindings.emptyBindings(), null, null, String.class, Integer.class, null, null, false);
        assertNotNull(mapType);
        JavaType superClass = new JavaType();
        JavaType[] superInterfaces = new JavaType[2];
        superInterfaces[0] = new JavaType();
        superInterfaces[1] = new JavaType();
        MapType newMapType = mapType.refine(HashMap.class, TypeBindings.emptyBindings(), superClass, superInterfaces);
        assertNotNull(newMapType);
        assertEquals(HashMap.class, newMapType.getRawClass());
        assertEquals(TypeBindings.emptyBindings(), newMapType.getBindings());
        assertEquals(superClass, newMapType.getSuperClass());
        assertEquals(superInterfaces, newMapType.getSuperInterfaces());
        assertEquals(mapType.getKeyType(), newMapType.getKeyType());
        assertEquals(mapType.getValueType(), newMapType.getValueType());
        assertEquals(mapType.getValueHandler(), newMapType.getValueHandler());
        assertEquals(mapType.getTypeHandler(), newMapType.getTypeHandler());
        assertEquals(mapType.isContainerType(), newMapType.isContainerType());
    }

    @Test
    public void testToString() {
        MapType mapType = new MapType(Map.class, TypeBindings.emptyBindings(), null, null, String.class, Integer.class, null, null, false);
        assertNotNull(mapType);
        String expected = "[map type; class java.util.Map, java.lang.String -> java.lang.Integer]";
        assertEquals(expected, mapType.toString());
    }
}