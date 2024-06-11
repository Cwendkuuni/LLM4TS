package com.fasterxml.jackson.databind.type.test;

import static org.junit.Assert.*;

import java.lang.ClassCastException;
import java.lang.ClassNotFoundException;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;

import org.junit.Test;

import com.fasterxml.jackson.core.type.TypeBindings;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.MapLikeType;
import com.fasterxml.jackson.databind.type.MapType;

public class MapTypeTest {

    @Test
    public void testMapTypeConstructor() {
        JavaType mapType = MapType.construct(Map.class, TypeBindings.defaultMapBindings(), null, null,
                String.class, Integer.class);
        MapType mapTypeInstance = new MapType((TypeBase) mapType.getType(), String.class, Integer.class);
        assertEquals(Map.class, mapTypeInstance.getRawClass());
        assertEquals(String.class, mapTypeInstance.getKeyType());
        assertEquals(Integer.class, mapTypeInstance.getValueType());
    }

    @Test
    public void testMapTypeWithTypeHandler() {
        JavaType mapType = MapType.construct(Map.class, TypeBindings.defaultMapBindings(), null, null,
                String.class, Integer.class);
        Object handler = new Object();
        MapType mapTypeWithHandler = mapType.withTypeHandler(handler);
        assertEquals(mapType.getValueType(), mapTypeWithHandler.getValueType());
        assertEquals(handler, mapTypeWithHandler.getTypeHandler());
    }

    @Test(expected = ClassCastException.class)
    public void testMapTypeWithWrongTypeHandler() {
        JavaType mapType = MapType.construct(Map.class, TypeBindings.defaultMapBindings(), null, null,
                String.class, Integer.class);
        Object handler = new Object();
        MapType mapTypeWithHandler = mapType.withTypeHandler(new StringHandler());
    }

    @Test(expected = ClassNotFoundException.class)
    public void testMapTypeWithUnknownTypeHandler() throws ClassNotFoundException {
        JavaType mapType = MapType.construct(Map.class, TypeBindings.defaultMapBindings(), null, null,
                String.class, Integer.class);
        String unknownHandlerClassName = "UnknownTypeHandler";
        MapType mapTypeWithHandler = mapType.withTypeHandler(Class.forName(unknownHandlerClassName));
    }

    @Test(expected = InvocationTargetException.class)
    public void testMapTypeWithExceptionThrowingTypeHandler() throws Exception {
        JavaType mapType = MapType.construct(Map.class, TypeBindings.defaultMapBindings(), null, null,
                String.class, Integer.class);
        Throwable throwingHandler = new Throwable("Test exception");
        MapType mapTypeWithHandler = mapType.withTypeHandler(() -> {
            throw new Exception("Exception thrown from type handler");
        });
    }

    @Test
    public void testMapTypeWithValueHandler() {
        JavaType mapType = MapType.construct(Map.class, TypeBindings.defaultMapBindings(), null, null,
                String.class, Integer.class);
        Object handler = new Object();
        MapType mapTypeWithHandler = mapType.withValueHandler(handler);
        assertEquals(mapType.getValueType(), mapTypeWithHandler.getValueType());
        assertEquals(handler, mapTypeWithHandler.getValueHandler());
    }

    @Test(expected = ClassCastException.class)
    public void testMapTypeWithWrongValueHandler() {
        JavaType mapType = MapType.construct(Map.class, TypeBindings.defaultMapBindings(), null, null,
                String.class, Integer.class);
        Object handler = new Object();
        MapType mapTypeWithHandler = mapType.withValueHandler(new StringHandler());
    }

    @Test(expected = NullPointerException.class)
    public void testMapTypeWithNullValueHandler() {
        JavaType mapType = MapType.construct(Map.class, TypeBindings.defaultMapBindings(), null, null,
                String.class, Integer.class);
        MapType mapTypeWithNullHandler = mapType.withValueHandler(null);
    }

    @Test(expected = ClassNotFoundException.class)
    public void testMapTypeWithUnknownValueHandler() throws ClassNotFoundException {
        JavaType mapType = MapType.construct(Map.class, TypeBindings.defaultMapBindings(), null, null,
                String.class, Integer.class);
        String unknownHandlerClassName = "UnknownValueHandler";
        MapType mapTypeWithHandler = mapType.withValueHandler(Class.forName(unknownHandlerClassName));
    }

    @Test(expected = InvocationTargetException.class)
    public void testMapTypeWithExceptionThrowingValueHandler() throws Exception {
        JavaType mapType = MapType.construct(Map.class, TypeBindings.defaultMapBindings(), null, null,
                String.class, Integer.class);
        Throwable throwingHandler = new Throwable("Test exception");
        MapType mapTypeWithHandler = mapType.withValueHandler(() -> {
            throw new Exception("Exception thrown from value handler");
        });
    }

    @Test
    public void testMapTypeWithKeyTypeHandler() {
        JavaType mapType = MapType.construct(Map.class, TypeBindings.defaultMapBindings(), null, null,
                String.class, Integer.class);
        Object handler = new Object();
        MapType mapTypeWithHandler = mapType.withKeyTypeHandler(handler);
        assertEquals(mapType.getKeyType(), mapTypeWithHandler.getKeyType());
        assertEquals(handler, mapTypeWithHandler.getKeyTypeHandler());
    }

    @Test(expected = ClassCastException.class)
    public void testMapTypeWithWrongKeyTypeHandler() {
        JavaType mapType = MapType.construct(Map.class, TypeBindings.defaultMapBindings(), null, null,
                String.class, Integer.class);
        Object handler = new Object();
        MapType mapTypeWithHandler = mapType.withKeyTypeHandler(new StringHandler());
    }

    @Test(expected = NullPointerException.class)
    public void testMapTypeWithNullKeyTypeHandler() {
        JavaType mapType = MapType.construct(Map.class, TypeBindings.defaultMapBindings(), null, null,
                String.class, Integer.class);
        MapType mapTypeWithNullHandler = mapType.withKeyTypeHandler(null);
    }

    @Test(expected = ClassNotFoundException.class)
    public void testMapTypeWithUnknownKeyTypeHandler() throws ClassNotFoundException {
        JavaType mapType = MapType.construct(Map.class, TypeBindings.defaultMapBindings(), null, null,
                String.class, Integer.class);
        String unknownHandlerClassName = "UnknownKeyTypeHandler";
        MapType mapTypeWithHandler = mapType.withKeyTypeHandler(Class.forName(unknownHandlerClassName));
    }

    @Test(expected = InvocationTargetException.class)
    public void testMapTypeWithExceptionThrowingKeyTypeHandler() throws Exception {
        JavaType mapType = MapType.construct(Map.class, TypeBindings.defaultMapBindings(), null, null,
                String.class, Integer.class);
        Throwable throwingHandler = new Throwable("Test exception");
        MapType mapTypeWithHandler = mapType.withKeyTypeHandler(() -> {
            throw new Exception("Exception thrown from key type handler");
        });
    }

    @Test
    public void testMapTypeWithStaticTyping() {
        JavaType mapType = MapType.construct(Map.class, TypeBindings.defaultMapBindings(), null, null,
                String.class, Integer.class);
        MapType mapTypeWithStaticTyping = mapType.withStaticTyping();
        assertTrue(mapTypeWithStaticTyping.isStatic());
        JavaType keyTypeWithStaticTyping = mapTypeWithStaticTyping.getKeyType();
        JavaType valueTypeWithStaticTyping = mapTypeWithStaticTyping.getValueType();
        assertEquals(mapType.getKeyType().getClass(), keyTypeWithStaticTyping.getClass());
        assertEquals(mapType.getValueType().getClass(), valueTypeWithStaticTyping.getClass());
    }

    @Test
    public void testMapTypeWithContentType() {
        JavaType mapType = MapType.construct(Map.class, TypeBindings.defaultMapBindings(), null, null,
                String.class, Integer.class);
        JavaType contentType = Arrays.asList(String[].class, Integer[].class).get(0);
        MapType mapTypeWithContentType = mapType.withContentType(contentType);
        assertEquals(contentType, mapTypeWithContentType.getValueType());
    }

    @Test
    public void testMapTypeWithKeyType() {
        JavaType mapType = MapType.construct(Map.class, TypeBindings.defaultMapBindings(), null, null,
                String.class, Integer.class);
        JavaType newKeyType = Long.TYPE;
        MapType mapTypeWithNewKeyType = mapType.withKeyType(newKeyType);
        assertEquals(newKeyType, mapTypeWithNewKeyType.getKeyType());
    }

    @Test
    public void testMapTypeRefine() {
        JavaType mapType = MapType.construct(Map.class, TypeBindings.defaultMapBindings(), null, null,
                String.class, Integer.class);
        JavaType refinedMapType = mapType.refine(HashMap.class, TypeBindings.defaultMapBindings(), null, null);
        assertEquals(HashMap.class, refinedMapType.getRawClass());
        assertEquals(String.class, refinedMapType.getKeyType());
        assertEquals(Integer.class, refinedMapType.getValueType());
    }
}