package com.fasterxml.jackson.databind.type.test;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.MapType;
import org.junit.Test;
import static org.junit.Assert.*;

public class MapTypeTest {

    @Test
    public void testMapTypeConstructor() {
        Class<Map<String, Integer>> mapType = Map.class;
        TypeBindings bindings = TypeBindings.getDefaultInstance();
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
        TypeBindings bindings = TypeBindings.getDefaultInstance();
        JavaType superClass = Object.class;
        JavaType[] superInts = new JavaType[0];
        JavaType keyT = String.class;
        JavaType valueT = Integer.class;
        MapType mapTypeInstance = new MapType(mapType, bindings, superClass, superInts, keyT, valueT);
        Object valueHandler = new Object() {};
        MapType mapTypeWithHandler = mapTypeInstance.withTypeHandler(valueHandler);
        assertEquals(valueHandler, mapTypeWithHandler.getTypeHandler());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testDeprecatedConstructWithOnlyKeyTypeAndValueType() {
        Class<Map<String, Integer>> mapType = Map.class;
        JavaType keyT = String.class;
        JavaType valueT = Integer.class;
        MapType mapTypeInstance = MapType.construct(mapType, keyT, valueT);
    }

    @Test
    public void testMapTypeWithStaticTyping() {
        Class<Map<String, Integer>> mapType = Map.class;
        TypeBindings bindings = TypeBindings.getDefaultInstance();
        JavaType superClass = Object.class;
        JavaType[] superInts = new JavaType[0];
        JavaType keyT = String.class;
        JavaType valueT = Integer.class;
        MapType mapTypeInstance = new MapType(mapType, bindings, superClass, superInts, keyT, valueT);
        MapType mapTypeWithStaticTyping = mapTypeInstance.withStaticTyping();
        assertTrue(mapTypeWithStaticTyping.isStatic());
    }

    @Test
    public void testMapTypeWithContentTypeHandler() {
        Class<Map<String, Map<String, Integer>>> mapOfMapType = Map.class;
        TypeBindings bindings = TypeBindings.getDefaultInstance();
        JavaType superClass = Object.class;
        JavaType[] superInts = new JavaType[0];
        JavaType keyT = String.class;
        JavaType valueT = Map.class;
        JavaType valueTypeHandler = Object.class;
        MapType mapTypeInstance = new MapType(mapOfMapType, bindings, superClass, superInts, keyT, valueT);
        MapType mapTypeWithHandler = mapTypeInstance.withContentTypeHandler(valueTypeHandler);
        assertEquals(valueTypeHandler, mapTypeWithHandler.getContentTypeHandler());
    }

    @Test
    public void testMapTypeWithKeyTypeHandler() {
        Class<Map<String, Integer>> mapType = Map.class;
        TypeBindings bindings = TypeBindings.getDefaultInstance();
        JavaType superClass = Object.class;
        JavaType[] superInts = new JavaType[0];
        JavaType keyT = String.class;
        JavaType valueT = Integer.class;
        Object keyTypeHandler = new Object() {};
        MapType mapTypeInstance = new MapType(mapType, bindings, superClass, superInts, keyT, valueT);
        MapType mapTypeWithHandler = mapTypeInstance.withKeyTypeHandler(keyTypeHandler);
        assertEquals(keyTypeHandler, mapTypeWithHandler.getKeyTypeHandler());
    }

    @Test
    public void testMapTypeWithValueHandler() {
        Class<Map<String, Integer>> mapType = Map.class;
        TypeBindings bindings = TypeBindings.getDefaultInstance();
        JavaType superClass = Object.class;
        JavaType[] superInts = new JavaType[0];
        JavaType keyT = String.class;
        JavaType valueT = Integer.class;
        Object valueHandler = new Object() {};
        MapType mapTypeInstance = new MapType(mapType, bindings, superClass, superInts, keyT, valueT);
        MapType mapTypeWithHandler = mapTypeInstance.withValueHandler(valueHandler);
        assertEquals(valueHandler, mapTypeWithHandler.getValueHandler());
    }

    @Test
    public void testMapTypeWithContentValueHandler() {
        Class<Map<String, Map<String, Integer>>> mapOfMapType = Map.class;
        TypeBindings bindings = TypeBindings.getDefaultInstance();
        JavaType superClass = Object.class;
        JavaType[] superInts = new JavaType[0];
        JavaType keyT = String.class;
        JavaType valueT = Map.class;
        JavaType valueTypeHandler = Object.class;
        JavaType valueHandler = Object.class;
        MapType mapTypeInstance = new MapType(mapOfMapType, bindings, superClass, superInts, keyT, valueT);
        MapType mapTypeWithHandler = mapTypeInstance.withContentValueHandler(valueHandler);
        assertEquals(valueHandler, mapTypeWithHandler.getContentValueHandler());
    }

    @Test
    public void testMapTypeToString() {
        Class<Map<String, Integer>> mapType = Map.class;
        TypeBindings bindings = TypeBindings.getDefaultInstance();
        JavaType superClass = Object.class;
        JavaType[] superInts = new JavaType[0];
        JavaType keyT = String.class;
        JavaType valueT = Integer.class;
        MapType mapTypeInstance = new MapType(mapType, bindings, superClass, superInts, keyT, valueT);
        String expected = "[map type; class Map<java.lang.String,java.lang.Integer>, java.lang.String -> java.lang.Integer]";
        String actual = mapTypeInstance.toString();
        assertEquals(expected, actual);
    }
}