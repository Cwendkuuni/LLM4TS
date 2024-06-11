package com.fasterxml.jackson.databind.type;

import com.fasterxml.jackson.databind.JavaType;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class MapTypeTest {
    @Test
    public void testConstructor() {
        Class<?> mapType = MapType.class;
        TypeBindings bindings = null;
        JavaType superClass = null;
        JavaType[] superInts = null;
        JavaType keyT = null;
        JavaType valueT = null;
        Object valueHandler = null;
        Object typeHandler = null;
        boolean asStatic = false;

        MapType map = new MapType(mapType, bindings, superClass, superInts, keyT, valueT, valueHandler, typeHandler, asStatic);

        assertEquals(mapType, map.getMapType());
        assertEquals(bindings, map.getBindings());
        assertEquals(superClass, map.getSuperClass());
        assertEquals(superInts, map.getSuperInterfaces());
        assertEquals(keyT, map.getKeyType());
        assertEquals(valueT, map.getValueType());
        assertEquals(valueHandler, map.getValueHandler());
        assertEquals(typeHandler, map.getTypeHandler());
        assertEquals(asStatic, map.isStatic());
    }

    @Test
    public void testConstruct() {
        Class<?> rawType = MapType.class;
        TypeBindings bindings = null;
        JavaType superClass = null;
        JavaType[] superInts = null;
        JavaType keyT = null;
        JavaType valueT = null;

        MapType map = MapType.construct(rawType, bindings, superClass, superInts, keyT, valueT);

        assertEquals(rawType, map.getBaseType());
        assertEquals(bindings, map.getBindings());
        assertEquals(superClass, map.getSuperClass());
        assertEquals(superInts, map.getSuperInterfaces());
        assertEquals(keyT, map.getKeyType());
        assertEquals(valueT, map.getValueType());
        assertNull(map.getValueHandler());
        assertNull(map.getTypeHandler());
        assertFalse(map.isStatic());
    }

    @Test
    public void testNarrow() {
        Class<?> subclass = MapType.class;
        TypeBindings bindings = null;
        JavaType superClass = null;
        JavaType[] superInts = null;
        JavaType keyT = null;
        JavaType valueT = null;
        Object valueHandler = null;
        Object typeHandler = null;
        boolean asStatic = false;

        MapType map = new MapType(subclass, bindings, superClass, superInts, keyT, valueT, valueHandler, typeHandler, asStatic);
        JavaType narrowedMap = map._narrow(subclass);

        assertEquals(subclass, narrowedMap.getBaseType());
        assertEquals(bindings, narrowedMap.getBindings());
        assertEquals(superClass, narrowedMap.getSuperClass());
        assertEquals(superInts, narrowedMap.getSuperInterfaces());
        assertEquals(keyT, narrowedMap.getKeyType());
        assertEquals(valueT, narrowedMap.getValueType());
        assertEquals(valueHandler, narrowedMap.getValueHandler());
        assertEquals(typeHandler, narrowedMap.getTypeHandler());
        assertEquals(asStatic, narrowedMap.isStatic());
    }

    @Test
    public void testWithKeyType() {
        Class<?> mapType = MapType.class;
        TypeBindings bindings = null;
        JavaType superClass = null;
        JavaType[] superInts = null;
        JavaType keyT = null;
        JavaType valueT = null;
        Object valueHandler = null;
        Object typeHandler = null;
        boolean asStatic = false;

        MapType map = new MapType(mapType, bindings, superClass, superInts, keyT, valueT, valueHandler, typeHandler, asStatic);
        JavaType newKeyType = null;
        MapType newMap = map.withKeyType(newKeyType);

        assertEquals(mapType, newMap.getMapType());
        assertEquals(bindings, newMap.getBindings());
        assertEquals(superClass, newMap.getSuperClass());
        assertEquals(superInts, newMap.getSuperInterfaces());
        assertEquals(newKeyType, newMap.getKeyType());
        assertEquals(valueT, newMap.getValueType());
        assertEquals(valueHandler, newMap.getValueHandler());
        assertEquals(typeHandler, newMap.getTypeHandler());
        assertEquals(asStatic, newMap.isStatic());
    }

    @Test
    public void testWithContentType() {
        Class<?> mapType = MapType.class;
        TypeBindings bindings = null;
        JavaType superClass = null;
        JavaType[] superInts = null;
        JavaType keyT = null;
        JavaType valueT = null;
        Object valueHandler = null;
        Object typeHandler = null;
        boolean asStatic = false;

        MapType map = new MapType(mapType, bindings, superClass, superInts, keyT, valueT, valueHandler, typeHandler, asStatic);
        JavaType newContentType = null;
        MapType newMap = map.withContentType(newContentType);

        assertEquals(mapType, newMap.getMapType());
        assertEquals(bindings, newMap.getBindings());
        assertEquals(superClass, newMap.getSuperClass());
        assertEquals(superInts, newMap.getSuperInterfaces());
        assertEquals(keyT, newMap.getKeyType());
        assertEquals(newContentType, newMap.getValueType());
        assertEquals(valueHandler, newMap.getValueHandler());
        assertEquals(typeHandler, newMap.getTypeHandler());
        assertEquals(asStatic, newMap.isStatic());
    }
}