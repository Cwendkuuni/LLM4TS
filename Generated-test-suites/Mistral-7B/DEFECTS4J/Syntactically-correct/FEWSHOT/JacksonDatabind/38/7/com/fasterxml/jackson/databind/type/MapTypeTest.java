package com.fasterxml.jackson.databind.type;

import static org.junit.Assert.*;

import org.junit.Test;

public class MapTypeTest {

    @Test
    public void testConstruct() {
        Class<?> rawType = Map.class;
        TypeBindings bindings = TypeBindings.empty();
        JavaType superClass = Object.class;
        JavaType[] superInts = new JavaType[0];
        JavaType keyT = String.class;
        JavaType valueT = Integer.class;

        MapType mapType = MapType.construct(rawType, bindings, superClass, superInts, keyT, valueT);

        assertEquals(Map.class, mapType.getRawClass());
        assertEquals(bindings, mapType.getTypeBindings());
        assertEquals(Object.class, mapType.getSuperClass());
        assertArrayEquals(new JavaType[0], mapType.getSuperInterfaces());
        assertEquals(keyT, mapType.getKeyType());
        assertEquals(valueT, mapType.getValueType());
        assertNull(mapType.getValueHandler());
        assertNull(mapType.getTypeHandler());
        assertFalse(mapType.isStatic());
    }

    @Test
    public void testConstructWithTypeHandler() {
        Class<?> rawType = Map.class;
        TypeBindings bindings = TypeBindings.empty();
        JavaType superClass = Object.class;
        JavaType[] superInts = new JavaType[0];
        JavaType keyT = String.class;
        JavaType valueT = Integer.class;
        Object valueHandler = new Object() {
        };

        MapType mapType = MapType.construct(rawType, bindings, superClass, superInts, keyT, valueT, valueHandler);

        assertEquals(Map.class, mapType.getRawClass());
        assertEquals(bindings, mapType.getTypeBindings());
        assertEquals(Object.class, mapType.getSuperClass());
        assertArrayEquals(new JavaType[0], mapType.getSuperInterfaces());
        assertEquals(keyT, mapType.getKeyType());
        assertEquals(valueT, mapType.getValueType());
        assertEquals(valueHandler, mapType.getValueHandler());
        assertNull(mapType.getTypeHandler());
        assertFalse(mapType.isStatic());
    }

    @Test
    public void testConstructWithTypeHandlerAndTypeHandler() {
        Class<?> rawType = Map.class;
        TypeBindings bindings = TypeBindings.empty();
        JavaType superClass = Object.class;
        JavaType[] superInts = new JavaType[0];
        JavaType keyT = String.class;
        JavaType valueT = Integer.class;
        Object valueHandler = new Object() {
        };
        Object typeHandler = new Object() {
        };

        MapType mapType = MapType.construct(rawType, bindings, superClass, superInts, keyT, valueT, valueHandler, typeHandler);

        assertEquals(Map.class, mapType.getRawClass());
        assertEquals(bindings, mapType.getTypeBindings());
        assertEquals(Object.class, mapType.getSuperClass());
        assertArrayEquals(new JavaType[0], mapType.getSuperInterfaces());
        assertEquals(keyT, mapType.getKeyType());
        assertEquals(valueT, mapType.getValueType());
        assertEquals(valueHandler, mapType.getValueHandler());
        assertEquals(typeHandler, mapType.getTypeHandler());
        assertFalse(mapType.isStatic());
    }

    @Test
    public void testConstructWithTypeHandlerAndStaticTyping() {
        Class<?> rawType = Map.class;
        TypeBindings bindings = TypeBindings.empty();
        JavaType superClass = Object.class;
        JavaType[] superInts = new JavaType[0];
        JavaType keyT = String.class;
        JavaType valueT = Integer.class;
        Object valueHandler = new Object() {
        };

        MapType mapType = MapType.construct(rawType, bindings, superClass, superInts, keyT, valueT, valueHandler, null).withStaticTyping();

        assertEquals(Map.class, mapType.getRawClass());
        assertEquals(bindings, mapType.getTypeBindings());
        assertEquals(Object.class, mapType.getSuperClass());
        assertArrayEquals(new JavaType[0], mapType.getSuperInterfaces());
        assertEquals(keyT, mapType.getKeyType());
        assertEquals(valueT, mapType.getValueType());
        assertEquals(valueHandler, mapType.getValueHandler());
        assertNull(mapType.getTypeHandler());
        assertTrue(mapType.isStatic());
    }

    @Test
    public void testWithTypeHandler() {
        Class<?> rawType = Map.class;
        TypeBindings bindings = TypeBindings.empty();
        JavaType superClass = Object.class;
        JavaType[] superInts = new JavaType[0];
        JavaType keyT = String.class;
        JavaType valueT = Integer.class;
        Object valueHandler = new Object() {
        };

        MapType mapType = new MapType(rawType, bindings, superClass, superInts, keyT, valueT);
        JavaType newMapType = mapType.withTypeHandler(valueHandler);

        assertEquals(Map.class, newMapType.getRawClass());
        assertEquals(bindings, newMapType.getTypeBindings());
        assertEquals(Object.class, newMapType.getSuperClass());
        assertArrayEquals(new JavaType[0], newMapType.getSuperInterfaces());
        assertEquals(keyT, newMapType.getKeyType());
        assertEquals(valueT, newMapType.getValueType());
        assertEquals(valueHandler, newMapType.getValueHandler());
        assertNull(newMapType.getTypeHandler());
        assertFalse(newMapType.isStatic());
    }

    @Test
    public void testWithContentTypeHandler() {
        Class<?> rawType = Map.class;
        TypeBindings bindings = TypeBindings.empty();
        JavaType superClass = Object.class;
        JavaType[] superInts = new JavaType[0];
        JavaType keyT = String.class;
        JavaType valueT = Integer.class;
        Object valueHandler = new Object() {
        };
        Object typeHandler = new Object() {
        };

        MapType mapType = new MapType(rawType, bindings, superClass, superInts, keyT, valueT);
        JavaType newMapType = mapType.withContentTypeHandler(typeHandler);

        assertEquals(Map.class, newMapType.getRawClass());
        assertEquals(bindings, newMapType.getTypeBindings());
        assertEquals(Object.class, newMapType.getSuperClass());
        assertArrayEquals(new JavaType[0], newMapType.getSuperInterfaces());
        assertEquals(keyT, newMapType.getKeyType());
        assertEquals(valueT.withTypeHandler(typeHandler), newMapType.getValueType());
        assertEquals(valueHandler, newMapType.getValueHandler());
        assertEquals(typeHandler, newMapType.getTypeHandler());
        assertFalse(newMapType.isStatic());
    }

    @Test
    public void testWithValueHandler() {
        Class<?> rawType = Map.class;
        TypeBindings bindings = TypeBindings.empty();
        JavaType superClass = Object.class;
        JavaType[] superInts = new JavaType[0];
        JavaType keyT = String.class;
        JavaType valueT = Integer.class;
        Object valueHandler = new Object() {
        };

        MapType mapType = new MapType(rawType, bindings, superClass, superInts, keyT, valueT);
        JavaType newMapType = mapType.withValueHandler(valueHandler);

        assertEquals(Map.class, newMapType.getRawClass());
        assertEquals(bindings, newMapType.getTypeBindings());
        assertEquals(Object.class, newMapType.getSuperClass());
        assertArrayEquals(new JavaType[0], newMapType.getSuperInterfaces());
        assertEquals(keyT, newMapType.getKeyType());
        assertEquals(valueT, newMapType.getValueType());
        assertEquals(valueHandler, newMapType.getValueHandler());
        assertNull(newMapType.getTypeHandler());
        assertFalse(newMapType.isStatic());
    }

    @Test
    public void testWithContentValueHandler() {
        Class<?> rawType = Map.class;
        TypeBindings bindings = TypeBindings.empty();
        JavaType superClass = Object.class;
        JavaType[] superInts = new JavaType[0];
        JavaType keyT = String.class;
        JavaType valueT = Integer.class;
        Object valueHandler = new Object() {
        };
        Object contentValueHandler = new Object() {
        };

        MapType mapType = new MapType(rawType, bindings, superClass, superInts, keyT, valueT);
        JavaType newMapType = mapType.withContentValueHandler(contentValueHandler);

        assertEquals(Map.class, newMapType.getRawClass());
        assertEquals(bindings, newMapType.getTypeBindings());
        assertEquals(Object.class, newMapType.getSuperClass());
        assertArrayEquals(new JavaType[0], newMapType.getSuperInterfaces());
        assertEquals(keyT, newMapType.getKeyType());
        assertEquals(valueT.withValueHandler(contentValueHandler), newMapType.getValueType());
        assertEquals(valueHandler, newMapType.getValueHandler());
        assertNull(newMapType.getTypeHandler());
        assertFalse(newMapType.isStatic());
    }

    @Test
    public void testWithStaticTyping() {
        Class<?> rawType = Map.class;
        TypeBindings bindings = TypeBindings.empty();
        JavaType superClass = Object.class;
        JavaType[] superInts = new JavaType[0];
        JavaType keyT = String.class;
        JavaType valueT = Integer.class;

        MapType mapType = new MapType(rawType, bindings, superClass, superInts, keyT, valueT, null, null, false);
        JavaType newMapType = mapType.withStaticTyping();

        assertEquals(Map.class, newMapType.getRawClass());
        assertEquals(bindings, newMapType.getTypeBindings());
        assertEquals(Object.class, newMapType.getSuperClass());
        assertArrayEquals(new JavaType[0], newMapType.getSuperInterfaces());
        assertEquals(keyT, newMapType.getKeyType());
        assertEquals(valueT, newMapType.getValueType());
        assertNull(newMapType.getValueHandler());
        assertNull(newMapType.getTypeHandler());
        assertTrue(newMapType.isStatic());
    }

    @Test
    public void testWithContentType() {
        Class<?> rawType = Map.class;
        TypeBindings bindings = TypeBindings.empty();
        JavaType superClass = Object.class;
        JavaType[] superInts = new JavaType[0];
        JavaType keyT = String.class;
        JavaType valueT = Integer.class;
        JavaType newValueType = Long.class;

        MapType mapType = new MapType(rawType, bindings, superClass, superInts, keyT, valueT);
        JavaType newMapType = mapType.withContentType(newValueType);

        assertEquals(Map.class, newMapType.getRawClass());
        assertEquals(bindings, newMapType.getTypeBindings());
        assertEquals(Object.class, newMapType.getSuperClass());
        assertArrayEquals(new JavaType[0], newMapType.getSuperInterfaces());
        assertEquals(keyT, newMapType.getKeyType());
        assertEquals(newValueType, newMapType.getValueType());
        assertNull(newMapType.getValueHandler());
        assertNull(newMapType.getTypeHandler());
        assertFalse(newMapType.isStatic());
    }

    @Test
    public void testWithKeyType() {
        Class<?> rawType = Map.class;
        TypeBindings bindings = TypeBindings.empty();
        JavaType superClass = Object.class;
        JavaType[] superInts = new JavaType[0];
        JavaType keyT = String.class;
        JavaType valueT = Integer.class;
        JavaType newKeyType = Long.class;

        MapType mapType = new MapType(rawType, bindings, superClass, superInts, keyT, valueT);
        JavaType newMapType = mapType.withKeyType(newKeyType);

        assertEquals(Map.class, newMapType.getRawClass());
        assertEquals(bindings, newMapType.getTypeBindings());
        assertEquals(Object.class, newMapType.getSuperClass());
        assertArrayEquals(new JavaType[0], newMapType.getSuperInterfaces());
        assertEquals(newKeyType, newMapType.getKeyType());
        assertEquals(valueT, newMapType.getValueType());
        assertNull(newMapType.getValueHandler());
        assertNull(newMapType.getTypeHandler());
        assertFalse(newMapType.isStatic());
    }

    @Test
    public void testRefine() {
        Class<?> rawType = Map.class;
        TypeBindings bindings = TypeBindings.empty();
        JavaType superClass = Object.class;
        JavaType[] superInts = new JavaType[0];
        JavaType keyT = String.class;
        JavaType valueT = Integer.class;
        Class<?> subClass = HashMap.class;
        TypeBindings subBindings = TypeBindings.empty();

        MapType mapType = new MapType(rawType, bindings, superClass, superInts, keyT, valueT);
        JavaType newMapType = mapType.refine(subClass, subBindings, null, new JavaType[0]);

        assertEquals(HashMap.class, newMapType.getRawClass());
        assertEquals(subBindings, newMapType.getTypeBindings());
        assertEquals(Object.class, newMapType.getSuperClass());
        assertArrayEquals(new JavaType[0], newMapType.getSuperInterfaces());
        assertEquals(keyT, newMapType.getKeyType());
        assertEquals(valueT, newMapType.getValueType());
        assertNull(newMapType.getValueHandler());
        assertNull(newMapType.getTypeHandler());
        assertFalse(newMapType.isStatic());
    }

    @Test
    public void testWithKeyTypeHandler() {
        Class<?> rawType = Map.class;
        TypeBindings bindings = TypeBindings.empty();
        JavaType superClass = Object.class;
        JavaType[] superInts = new JavaType[0];
        JavaType keyT = String.class;
        JavaType valueT = Integer.class;
        Object keyTypeHandler = new Object() {
        };

        MapType mapType = new MapType(rawType, bindings, superClass, superInts, keyT, valueT);
        JavaType newMapType = mapType.withKeyTypeHandler(keyTypeHandler);

        assertEquals(Map.class, newMapType.getRawClass());
        assertEquals(bindings, newMapType.getTypeBindings());
        assertEquals(Object.class, newMapType.getSuperClass());
        assertArrayEquals(new JavaType[0], newMapType.getSuperInterfaces());
        assertEquals(keyT.withTypeHandler(keyTypeHandler), newMapType.getKeyType());
        assertEquals(valueT, newMapType.getValueType());
        assertNull(newMapType.getValueHandler());
        assertNull(newMapType.getTypeHandler());
        assertFalse(newMapType.isStatic());
    }

    @Test
    public void testWithKeyValueHandler() {
        Class<?> rawType = Map.class;
        TypeBindings bindings = TypeBindings.empty();
        JavaType superClass = Object.class;
        JavaType[] superInts = new JavaType[0];
        JavaType keyT = String.class;
        JavaType valueT = Integer.class;
        Object keyValueHandler = new Object() {
        };

        MapType mapType = new MapType(rawType, bindings, superClass, superInts, keyT, valueT);
        JavaType newMapType = mapType.withKeyValueHandler(keyValueHandler);

        assertEquals(Map.class, newMapType.getRawClass());
        assertEquals(bindings, newMapType.getTypeBindings());
        assertEquals(Object.class, newMapType.getSuperClass());
        assertArrayEquals(new JavaType[0], newMapType.getSuperInterfaces());
        assertEquals(keyT, newMapType.getKeyType());
        assertEquals(valueT, newMapType.getValueType());
        assertEquals(keyValueHandler, newMapType.getKeyHandler());
        assertNull(newMapType.getValueHandler());
        assertNull(newMapType.getTypeHandler());
        assertFalse(newMapType.isStatic());
    }
}