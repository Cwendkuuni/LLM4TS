package com.fasterxml.jackson.databind.type;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class MapTypeTest {
    @Test
    public void testConstructorWithParameters() {
        Class<?> mapType = null;
        TypeBindings bindings = null;
        JavaType superClass = null;
        JavaType[] superInts = null;
        JavaType keyT = null;
        JavaType valueT = null;
        Object valueHandler = null;
        Object typeHandler = null;
        boolean asStatic = false;

        MapType mt = new MapType(mapType, bindings, superClass, superInts, keyT, valueT, valueHandler, typeHandler,
                asStatic);

        assertEquals(mapType, mt.getMapType());
        assertEquals(bindings, mt.getTypeBindings());
        assertEquals(superClass, mt.getSuperClass());
        assertEquals(superInts, mt.getSuperInterfaces());
        assertEquals(keyT, mt.getKeyType());
        assertEquals(valueT, mt.getValueType());
        assertEquals(valueHandler, mt.getValueHandler());
        assertEquals(typeHandler, mt.getTypeHandler());
        assertEquals(asStatic, mt.isStatic());
    }

    @Test
    public void testLifeCycle() {
        TypeBase base = null;
        JavaType keyT = null;
        JavaType valueT = null;

        MapType mt = new MapType(base, keyT, valueT);

        assertEquals(base, mt.getBaseType());
        assertEquals(keyT, mt.getKeyType());
        assertEquals(valueT, mt.getValueType());
    }

    @Test
    public void testConstruct() {
        Class<?> rawType = null;
        TypeBindings bindings = null;
        JavaType superClass = null;
        JavaType[] superInts = null;
        JavaType keyT = null;
        JavaType valueT = null;

        MapType mt = MapType.construct(rawType, bindings, superClass, superInts, keyT, valueT);

        assertEquals(rawType, mt.getMapType());
        assertEquals(bindings, mt.getTypeBindings());
        assertEquals(superClass, mt.getSuperClass());
        assertEquals(superInts, mt.getSuperInterfaces());
        assertEquals(keyT, mt.getKeyType());
        assertEquals(valueT, mt.getValueType());
        assertNull(mt.getValueHandler());
        assertNull(mt.getTypeHandler());
        assertFalse(mt.isStatic());
    }

    @Test
    public void testConstructDeprecated() {
        Class<?> rawType = null;
        JavaType keyT = null;
        JavaType valueT = null;

        MapType mt = MapType.construct(rawType, keyT, valueT);

        assertEquals(rawType, mt.getMapType());
        assertNull(mt.getTypeBindings());
        assertEquals(_bogusSuperClass(rawType), mt.getSuperClass());
        assertNull(mt.getSuperInterfaces());
        assertEquals(keyT, mt.getKeyType());
        assertEquals(valueT, mt.getValueType());
        assertNull(mt.getValueHandler());
        assertNull(mt.getTypeHandler());
        assertFalse(mt.isStatic());
    }

    @Test
    public void testWithKeyHandler() {
        Object h = null;

        MapType mt = MapType.withKeyHandler(h);

        assertEquals(h, mt.getKeyHandler());
    }

    @Test
    public void testWithValueHandler() {
        Object h = null;

        MapType mt = MapType.withValueHandler(h);

        assertEquals(h, mt.getValueHandler());
    }

    @Test
    public void testWithStaticTyping() {
        MapType mt = MapType.withStaticTyping();

        assertTrue(mt.isStatic());
    }

    @Test
    public void testWithContentType() {
        JavaType contentType = null;

        MapType mt = MapType.withContentType(contentType);

        assertEquals(contentType, mt.getContentType());
    }

    @Test
    public void testWithKeyType() {
        JavaType keyType = null;

        MapType mt = MapType.withKeyType(keyType);

        assertEquals(keyType, mt.getKeyType());
    }

    @Test
    public void testRefine() {
        Class<?> rawType = null;
        TypeBindings bindings = null;
        JavaType superClass = null;
        JavaType[] superInterfaces = null;

        MapType mt = MapType.refine(rawType, bindings, superClass, superInterfaces);

        assertEquals(rawType, mt.getMapType());
        assertEquals(bindings, mt.getTypeBindings());
        assertEquals(superClass, mt.getSuperClass());
        assertEquals(superInterfaces, mt.getSuperInterfaces());
    }

    @Test
    public void testWithKeyTypeHandler() {
        Object h = null;

        MapType mt = MapType.withKeyTypeHandler(h);

        assertEquals(h, mt.getKeyTypeHandler());
    }

    @Test
    public void testWithKeyValueHandler() {
        Object h = null;

        MapType mt = MapType.withKeyValueHandler(h);

        assertEquals(h, mt.getKeyValueHandler());
    }

    @Test
    public void testToString() {
        String expected = "[map type; class "+_class.getName()+", "+_keyType+" -> "+_valueType+"]";
        
        assertEquals(expected, mt.toString());
    }
}