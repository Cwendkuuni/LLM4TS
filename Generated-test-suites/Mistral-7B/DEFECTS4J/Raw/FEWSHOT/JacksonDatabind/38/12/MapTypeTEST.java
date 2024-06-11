package com.fasterxml.jackson.databind.type;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;

public class MapTypeTest {

    @Test
    public void testMapTypeWithConstructors() {
        Class<?> rawType = Map.class;
        TypeBindings bindings = TypeBindings.empty();
        JavaType superClass = Object.class;
        JavaType[] superInts = new JavaType[0];
        JavaType keyT = String.class;
        JavaType valueT = Integer.class;

        MapType mapType = MapType.construct(rawType, bindings, superClass, superInts, keyT, valueT);
        assertEquals(Map.class, mapType.getRawClass());
        assertEquals(keyT, mapType.getKeyType());
        assertEquals(valueT, mapType.getValueType());

        mapType = MapType.construct(rawType, bindings, superClass, superInts, keyT, valueT, null, null, false);
        assertEquals(Map.class, mapType.getRawClass());
        assertEquals(keyT, mapType.getKeyType());
        assertEquals(valueT, mapType.getValueType());

        mapType = MapType.construct(rawType, keyT, valueT);
        assertEquals(Map.class, mapType.getRawClass());
        assertEquals(keyT, mapType.getKeyType());
        assertEquals(valueT, mapType.getValueType());
    }

    @Test
    public void testMapTypeWithMethods() {
        Class<?> rawType = Map.class;
        TypeBindings bindings = TypeBindings.empty();
        JavaType superClass = Object.class;
        JavaType[] superInts = new JavaType[0];
        JavaType keyT = String.class;
        JavaType valueT = Integer.class;

        MapType mapType = new MapType(rawType, bindings, superClass, superInts, keyT, valueT);
        assertEquals(Map.class, mapType.getRawClass());
        assertEquals(keyT, mapType.getKeyType());
        assertEquals(valueT, mapType.getValueType());

        mapType = mapType.withTypeHandler(null);
        assertEquals(Map.class, mapType.getRawClass());
        assertEquals(keyT, mapType.getKeyType());
        assertEquals(valueT, mapType.getValueType());

        mapType = mapType.withContentTypeHandler(null);
        assertEquals(Map.class, mapType.getRawClass());
        assertEquals(keyT, mapType.getKeyType());
        assertEquals(valueT, mapType.getValueType());

        mapType = mapType.withValueHandler(null);
        assertEquals(Map.class, mapType.getRawClass());
        assertEquals(keyT, mapType.getKeyType());
        assertEquals(valueT, mapType.getValueType());

        mapType = mapType.withContentValueHandler(null);
        assertEquals(Map.class, mapType.getRawClass());
        assertEquals(keyT, mapType.getKeyType());
        assertEquals(valueT, mapType.getValueType());

        mapType = mapType.withStaticTyping();
        assertEquals(Map.class, mapType.getRawClass());
        assertEquals(keyT, mapType.getKeyType().withStaticTyping());
        assertEquals(valueT, mapType.getValueType().withStaticTyping());

        mapType = mapType.withContentType(valueT);
        assertEquals(Map.class, mapType.getRawClass());
        assertEquals(keyT, mapType.getKeyType());
        assertEquals(valueT, mapType.getValueType());

        mapType = mapType.withKeyType(Integer.class);
        assertEquals(Map.class, mapType.getRawClass());
        assertEquals(Integer.class, mapType.getKeyType());
        assertEquals(valueT, mapType.getValueType());

        mapType = mapType.refine(Map.class, bindings, superClass, superInts);
        assertEquals(Map.class, mapType.getRawClass());
        assertEquals(keyT, mapType.getKeyType());
        assertEquals(valueT, mapType.getValueType());

        mapType = mapType.withKeyTypeHandler(null);
        assertEquals(Map.class, mapType.getRawClass());
        assertEquals(keyT, mapType.getKeyType());
        assertEquals(valueT, mapType.getValueType());

        mapType = mapType.withKeyValueHandler(null);
        assertEquals(Map.class, mapType.getRawClass());
        assertEquals(keyT, mapType.getKeyType());
        assertEquals(valueT, mapType.getValueType());
    }
}