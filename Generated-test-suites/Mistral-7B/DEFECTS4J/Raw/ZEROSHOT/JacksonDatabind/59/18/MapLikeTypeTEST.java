package com.fasterxml.jackson.databind.type.test;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.MapLikeType;
import com.fasterxml.jackson.databind.type.TypeBase;
import com.fasterxml.jackson.databind.type.TypeBindings;
import com.fasterxml.jackson.databind.type.TypeFactory;

public class MapLikeTypeTest {

    @Test
    public void testMapLikeTypeCreation() {
        // Test creation using constructor with TypeBindings
        TypeBindings bindings = TypeBindings.create(Map.class, String.class,
                Integer.class);
        JavaType mapType = TypeFactory.defaultInstance().constructType(Map.class,
                bindings);
        MapLikeType mapLikeType = new MapLikeType(mapType, bindings, null, null,
                null, null, null, null, false);
        assertEquals(Map.class, mapLikeType.getRawClass());
        assertEquals(String.class, mapLikeType.getKeyType().getRawClass());
        assertEquals(Integer.class, mapLikeType.getValueType().getRawClass());

        // Test creation using factory method
        JavaType keyType = TypeFactory.defaultInstance().constructType(String.class);
        JavaType valueType = TypeFactory.defaultInstance().constructType(Integer.class);
        MapLikeType mapLikeType2 = MapLikeType.upgradeFrom(mapType.containedType(0),
                keyType, valueType);
        assertEquals(mapLikeType, mapLikeType2);

        // Test deprecated constructor
        MapLikeType mapLikeType3 = new MapLikeType(Map.class, bindings, null, null,
                keyType, valueType);
        assertEquals(mapLikeType, mapLikeType3);
    }

    @Test
    public void testMapLikeTypeMethods() {
        JavaType listType = TypeFactory.defaultInstance().constructType(List.class,
                String.class);
        JavaType mapType = TypeFactory.defaultInstance().constructType(Map.class,
                String.class, Integer.class);

        MapLikeType mapLikeType = new MapLikeType(mapType, null, null, null, null,
                null, null, null, false);

        JavaType listLikeType = new MapLikeType(listType, null, null, null, null,
                null, null, null, false);

        // test isContainerType()
        assertTrue(mapLikeType.isContainerType());
        assertFalse(listLikeType.isContainerType());

        // test isMapLikeType()
        assertTrue(mapLikeType.isMapLikeType());
        assertFalse(listLikeType.isMapLikeType());

        // test getKeyType()
        assertEquals(String.class, mapLikeType.getKeyType().getRawClass());

        // test getContentType()
        assertEquals(Integer.class, mapLikeType.getValueType().getRawClass());

        // test getContentValueHandler() and getContentTypeHandler()
        assertNull(mapLikeType.getContentValueHandler());
        assertNull(mapLikeType.getContentTypeHandler());

        // test hasHandlers()
        assertFalse(mapLikeType.hasHandlers());

        // test getErasedSignature() and getGenericSignature()
        StringBuilder sb = new StringBuilder();
        mapLikeType.getErasedSignature(sb);
        assertEquals("[Ljava.util.Map;", sb.toString());
        sb.setLength(0);
        mapLikeType.getGenericSignature(sb);
        assertEquals("[Ljava.util.Map<Ljava.lang.String;Ljava.lang.Integer>]",
                sb.toString());

        // test isTrueMapType()
        assertTrue(mapLikeType.isTrueMapType());
        assertFalse(listLikeType.isTrueMapType());
    }

    @Test
    public void testMapLikeTypeWithHandlers() {
        JavaType keyType = TypeFactory.defaultInstance().constructType(String.class);
        JavaType valueType = TypeFactory.defaultInstance().constructType(Integer.class);
        JavaType mapType = TypeFactory.defaultInstance().constructType(Map.class,
                keyType, valueType);
        MapLikeType mapLikeType = new MapLikeType(mapType, null, null, null, null,
                null, null, null, false);

        // test withKeyType()
        JavaType newKeyType = TypeFactory.defaultInstance().constructType(StringBuffer.class);
        MapLikeType newMapLikeType = mapLikeType.withKeyType(newKeyType);
        assertNotSame(mapLikeType, newMapLikeType);
        assertEquals(newKeyType, newMapLikeType.getKeyType());

        // test withContentType()
        JavaType newValueType = TypeFactory.defaultInstance()
                .constructType(LinkedHashMap.class, String.class, Integer.class);
        MapLikeType newMapLikeType2 = mapLikeType.withContentType(newValueType);
        assertNotSame(mapLikeType, newMapLikeType2);
        assertEquals(newValueType, newMapLikeType2.getValueType());

        // test withTypeHandler()
        Object handler = new Object() {};
        MapLikeType newMapLikeType3 = mapLikeType.withTypeHandler(handler);
        assertNotSame(mapLikeType, newMapLikeType3);
        assertEquals(handler, newMapLikeType3.getTypeHandler());

        // test withContentTypeHandler()
        Object handler2 = new Object() {};
        MapLikeType newMapLikeType4 = mapLikeType.withContentTypeHandler(handler2);
        assertNotSame(mapLikeType, newMapLikeType4);
        assertEquals(handler2, newMapLikeType4.getContentTypeHandler());

        // test withValueHandler()
        Object handler3 = new Object() {};
        MapLikeType newMapLikeType5 = mapLikeType.withValueHandler(handler3);
        assertNotSame(mapLikeType, newMapLikeType5);
        assertEquals(handler3, newMapLikeType5.getValueHandler());

        // test withContentValueHandler()
        Object handler4 = new Object() {};
        MapLikeType newMapLikeType6 = mapLikeType.withContentValueHandler(handler4);
        assertNotSame(mapLikeType, newMapLikeType6);
        assertEquals(handler4, newMapLikeType6.getContentValueHandler());

        // test withKeyTypeHandler()
        Object handler5 = new Object() {};
        MapLikeType newMapLikeType7 = mapLikeType.withKeyTypeHandler(handler5);
        assertNotSame(mapLikeType, newMapLikeType7);
        assertEquals(handler5, newMapLikeType7.getKeyTypeHandler());

        // test withKeyValueHandler()
        Object handler6 = new Object() {};
        MapLikeType newMapLikeType8 = mapLikeType.withKeyValueHandler(handler6);
        assertNotSame(mapLikeType, newMapLikeType8);
        assertEquals(handler6, newMapLikeType8.getKeyValueHandler());

        // test withStaticTyping()
        MapLikeType newMapLikeType9 = mapLikeType.withStaticTyping();
        assertTrue(newMapLikeType9.isStaticTyping());
        assertFalse(mapLikeType.isStaticTyping());
        assertEquals(mapLikeType.getClass(), mapLikeType.getClass());
        assertEquals(Map.class, newMapLikeType9.getRawClass());
    }

    @Test
    public void testMapLikeTypeToString() {
        JavaType mapType = TypeFactory.defaultInstance().constructType(Map.class,
                String.class, Integer.class);
        MapLikeType mapLikeType = new MapLikeType(mapType, null, null, null, null,
                null, null, null, false);
        assertEquals(
                "[map-like type; class java.util.Map, java.lang.String -> java.lang.Integer]",
                mapLikeType.toString());
    }
}