package com.fasterxml.jackson.databind.type.test;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.TypeBindings;
import com.fasterxml.jackson.databind.type.MapLikeType;
import com.fasterxml.jackson.databind.type.MapType;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import static org.junit.Assert.*;

@RunWith(JUnit4.class)
public class MapTypeTest {

    @Test
    public void testMapTypeConstructor() {
        Class<Map<String, Integer>> mapType = Map.class;
        TypeBindings bindings = new TypeBindings();
        JavaType superClass = Object.class;
        JavaType[] superInts = new JavaType[0];
        JavaType keyT = String.class;
        JavaType valueT = Integer.class;
        MapType mapTypeInstance = MapType.construct(mapType, bindings, superClass, superInts, keyT, valueT);
        assertEquals(mapType, mapTypeInstance.getRawClass());
        assertEquals(keyT, mapTypeInstance.getKeyType());
        assertEquals(valueT, mapTypeInstance.getValueType());
    }

    @Test
    public void testMapTypeWithTypeHandler() {
        Class<Map<String, Integer>> mapType = Map.class;
        TypeBindings bindings = new TypeBindings();
        JavaType superClass = Object.class;
        JavaType[] superInts = new JavaType[0];
        JavaType keyT = String.class;
        JavaType valueT = Integer.class;
        Object valueHandler = new Object();
        MapType mapTypeInstance = new MapType(mapType, bindings, superClass, superInts, keyT, valueT, valueHandler, null, false);
        MapType withTypeHandler = mapTypeInstance.withTypeHandler(valueHandler);
        assertEquals(mapTypeInstance.getRawClass(), withTypeHandler.getRawClass());
        assertEquals(mapTypeInstance.getKeyType(), withTypeHandler.getKeyType());
        assertEquals(mapTypeInstance.getValueType(), withTypeHandler.getValueType());
        assertNotSame(mapTypeInstance.getTypeHandler(), withTypeHandler.getTypeHandler());
        assertEquals(valueHandler, withTypeHandler.getTypeHandler());
    }

    @Test
    public void testMapTypeWithContentTypeHandler() {
        Class<Map<String, Integer>> mapType = Map.class;
        TypeBindings bindings = new TypeBindings();
        JavaType superClass = Object.class;
        JavaType[] superInts = new JavaType[0];
        JavaType keyT = String.class;
        JavaType valueT = Integer.class;
        Object valueHandler = new Object();
        Object contentTypeHandler = new Object();
        MapType mapTypeInstance = new MapType(mapType, bindings, superClass, superInts, keyT, valueT, valueHandler, null, false);
        MapType withContentTypeHandler = mapTypeInstance.withContentTypeHandler(contentTypeHandler);
        assertEquals(mapTypeInstance.getRawClass(), withContentTypeHandler.getRawClass());
        assertEquals(mapTypeInstance.getKeyType(), withContentTypeHandler.getKeyType());
        JavaType contentValueType = valueT.withTypeHandler(contentTypeHandler);
        assertEquals(contentValueType, withContentTypeHandler.getValueType());
        assertNotSame(mapTypeInstance.getTypeHandler(), withContentTypeHandler.getTypeHandler());
        assertNull(withContentTypeHandler.getValueHandler());
        assertEquals(contentTypeHandler, withContentTypeHandler.getTypeHandler());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidNarrowType() {
        Class<Map<String, Integer>> mapType = Map.class;
        TypeBindings bindings = new TypeBindings();
        JavaType superClass = List.class;
        JavaType[] superInts = new JavaType[0];
        JavaType keyT = String.class;
        JavaType valueT = Integer.class;
        MapType mapTypeInstance = new MapType(mapType, bindings, superClass, superInts, keyT, valueT, null, null, false);
        mapTypeInstance.narrow(ArrayList.class);
    }

    @Test
    public void testMapTypeStaticTyping() {
        Class<Map<String, Integer>> mapType = Map.class;
        TypeBindings bindings = new TypeBindings();
        JavaType superClass = Object.class;
        JavaType[] superInts = new JavaType[0];
        JavaType keyT = String.class;
        JavaType valueT = Integer.class;
        MapType mapTypeInstance = new MapType(mapType, bindings, superClass, superInts, keyT, valueT, null, null, false);
        MapType withStaticTyping = mapTypeInstance.withStaticTyping();
        assertEquals(mapTypeInstance.getRawClass(), withStaticTyping.getRawClass());
        assertEquals(mapTypeInstance.getKeyType(), withStaticTyping.getKeyType());
        assertEquals(mapTypeInstance.getValueType(), withStaticTyping.getValueType());
        assertTrue(withStaticTyping.isStatic());
        assertFalse(mapTypeInstance.isStatic());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testMapTypeWithNullValueType() {
        Class<Map<String, Integer>> mapType = Map.class;
        TypeBindings bindings = new TypeBindings();
        JavaType superClass = Object.class;
        JavaType[] superInts = new JavaType[0];
        JavaType keyT = String.class;
        JavaType valueT = null;
        MapType mapTypeInstance = new MapType(mapType, bindings, superClass, superInts, keyT, valueT, null, null, false);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testMapTypeWithNullKeyType() {
        Class<Map<String, Integer>> mapType = Map.class;
        TypeBindings bindings = new TypeBindings();
        JavaType superClass = Object.class;
        JavaType[] superInts = new JavaType[0];
        JavaType keyT = null;
        JavaType valueT = Integer.class;
        MapType mapTypeInstance = new MapType(mapType, bindings, superClass, superInts, keyT, valueT, null, null, false);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testMapTypeWithNullSupertype() {
        Class<Map<String, Integer>> mapType = Map.class;
        TypeBindings bindings = new TypeBindings();
        JavaType superClass = null;
        JavaType[] superInts = new JavaType[0];
        JavaType keyT = String.class;
        JavaType valueT = Integer.class;
        MapType mapTypeInstance = new MapType(mapType, bindings, superClass, superInts, keyT, valueT, null, null, false);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testMapTypeWithInvalidSupertype() {
        Class<Map<String, Integer>> mapType = Map.class;
        TypeBindings bindings = new TypeBindings();
        JavaType superClass = new Object() {}.getClass();
        JavaType[] superInts = new JavaType[0];
        JavaType keyT = String.class;
        JavaType valueT = Integer.class;
        MapType mapTypeInstance = new MapType(mapType, bindings, superClass, superInts, keyT, valueT, null, null, false);
    }
}