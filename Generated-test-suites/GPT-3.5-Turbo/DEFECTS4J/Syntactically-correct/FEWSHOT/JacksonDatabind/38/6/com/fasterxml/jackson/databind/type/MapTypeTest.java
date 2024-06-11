package com.fasterxml.jackson.databind.type;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

import org.junit.Test;

public class MapTypeTest {

    @Test
    public void testConstruct() {
        Class<?> rawType = Object.class;
        TypeBindings bindings = null;
        JavaType superClass = null;
        JavaType[] superInts = null;
        JavaType keyT = null;
        JavaType valueT = null;
        MapType mapType = MapType.construct(rawType, bindings, superClass, superInts, keyT, valueT);
        assertNotNull(mapType);
        assertSame(rawType, mapType.getRawClass());
        assertSame(bindings, mapType.getBindings());
        assertSame(superClass, mapType.getSuperClass());
        assertSame(superInts, mapType.getInterfaces());
        assertSame(keyT, mapType.getKeyType());
        assertSame(valueT, mapType.getValueType());
        assertNull(mapType.getValueHandler());
        assertNull(mapType.getTypeHandler());
        assertEquals(false, mapType.isStatic());
    }
    
    @Test
    public void testConstructWithDeprecatedMethod() {
        Class<?> rawType = Object.class;
        JavaType keyT = null;
        JavaType valueT = null;
        MapType mapType = MapType.construct(rawType, keyT, valueT);
        assertNotNull(mapType);
        assertSame(rawType, mapType.getRawClass());
        assertNull(mapType.getBindings());
        assertTrue(mapType.getSuperClass() instanceof TypeBase);
        assertSame(keyT, mapType.getKeyType());
        assertSame(valueT, mapType.getValueType());
        assertNull(mapType.getValueHandler());
        assertNull(mapType.getTypeHandler());
        assertEquals(false, mapType.isStatic());
    }
    
    @Test
    public void testWithKeyTypeHandler() {
        MapType mapType = new MapType(Object.class, null, null, null, null, null, null, null, false);
        Object h = new Object();
        MapType newMapType = mapType.withKeyTypeHandler(h);
        assertNotNull(newMapType);
        assertSame(mapType.getRawClass(), newMapType.getRawClass());
        assertSame(mapType.getBindings(), newMapType.getBindings());
        assertSame(mapType.getSuperClass(), newMapType.getSuperClass());
        assertSame(mapType.getInterfaces(), newMapType.getInterfaces());
        assertSame(mapType.getKeyType().withTypeHandler(h), newMapType.getKeyType());
        assertSame(mapType.getValueType(), newMapType.getValueType());
        assertNull(newMapType.getValueHandler());
        assertNull(newMapType.getTypeHandler());
        assertEquals(false, newMapType.isStatic());
    }
    
    @Test
    public void testWithKeyValueHandler() {
        MapType mapType = new MapType(Object.class, null, null, null, null, null, null, null, false);
        Object h = new Object();
        MapType newMapType = mapType.withKeyValueHandler(h);
        assertNotNull(newMapType);
        assertSame(mapType.getRawClass(), newMapType.getRawClass());
        assertSame(mapType.getBindings(), newMapType.getBindings());
        assertSame(mapType.getSuperClass(), newMapType.getSuperClass());
        assertSame(mapType.getInterfaces(), newMapType.getInterfaces());
        assertSame(mapType.getKeyType().withValueHandler(h), newMapType.getKeyType());
        assertSame(mapType.getValueType(), newMapType.getValueType());
        assertNull(newMapType.getValueHandler());
        assertNull(newMapType.getTypeHandler());
        assertEquals(false, newMapType.isStatic());
    }
    
    @Test
    public void testWithStaticTyping() {
        MapType mapType = new MapType(Object.class, null, null, null, null, null, null, null, false);
        MapType newMapType = mapType.withStaticTyping();
        assertNotNull(newMapType);
        assertSame(mapType.getRawClass(), newMapType.getRawClass());
        assertSame(mapType.getBindings(), newMapType.getBindings());
        assertSame(mapType.getSuperClass(), newMapType.getSuperClass());
        assertSame(mapType.getInterfaces(), newMapType.getInterfaces());
        assertSame(mapType.getKeyType().withStaticTyping(), newMapType.getKeyType());
        assertSame(mapType.getValueType().withStaticTyping(), newMapType.getValueType());
        assertNull(newMapType.getValueHandler());
        assertNull(newMapType.getTypeHandler());
        assertEquals(true, newMapType.isStatic());
    }
    
    @Test
    public void testWithContentType() {
        MapType mapType = new MapType(Object.class, null, null, null, null, null, null, null, false);
        JavaType contentType = new JavaType();
        MapType newMapType = mapType.withContentType(contentType);
        assertNotNull(newMapType);
        assertSame(mapType.getRawClass(), newMapType.getRawClass());
        assertSame(mapType.getBindings(), newMapType.getBindings());
        assertSame(mapType.getSuperClass(), newMapType.getSuperClass());
        assertSame(mapType.getInterfaces(), newMapType.getInterfaces());
        assertSame(mapType.getKeyType(), newMapType.getKeyType());
        assertSame(contentType, newMapType.getValueType());
        assertNull(newMapType.getValueHandler());
        assertNull(newMapType.getTypeHandler());
        assertEquals(false, newMapType.isStatic());
    }
    
    @Test
    public void testWithKeyType() {
        MapType mapType = new MapType(Object.class, null, null, null, null, null, null, null, false);
        JavaType keyType = new JavaType();
        MapType newMapType = mapType.withKeyType(keyType);
        assertNotNull(newMapType);
        assertSame(mapType.getRawClass(), newMapType.getRawClass());
        assertSame(mapType.getBindings(), newMapType.getBindings());
        assertSame(mapType.getSuperClass(), newMapType.getSuperClass());
        assertSame(mapType.getInterfaces(), newMapType.getInterfaces());
        assertSame(keyType, newMapType.getKeyType());
        assertSame(mapType.getValueType(), newMapType.getValueType());
        assertNull(newMapType.getValueHandler());
        assertNull(newMapType.getTypeHandler());
        assertEquals(false, newMapType.isStatic());
    }
    
    @Test
    public void testRefine() {
        MapType mapType = new MapType(Object.class, null, null, null, null, null, null, null, false);
        Class<?> rawType = Object.class;
        TypeBindings bindings = null;
        JavaType superClass = null;
        JavaType[] superInterfaces = null;
        MapType newMapType = (MapType) mapType.refine(rawType, bindings, superClass, superInterfaces);
        assertNotNull(newMapType);
        assertSame(rawType, newMapType.getRawClass());
        assertSame(bindings, newMapType.getBindings());
        assertSame(superClass, newMapType.getSuperClass());
        assertSame(superInterfaces, newMapType.getInterfaces());
        assertSame(mapType.getKeyType(), newMapType.getKeyType());
        assertSame(mapType.getValueType(), newMapType.getValueType());
        assertNull(newMapType.getValueHandler());
        assertNull(newMapType.getTypeHandler());
        assertEquals(false, newMapType.isStatic());
    }

    @Test
    public void testToString() {
        MapType mapType = new MapType(Object.class, null, null, null, null, null, null, null, false);
        String expected = "[map type; class java.lang.Object, null -> null]";
        assertEquals(expected, mapType.toString());
    }

}