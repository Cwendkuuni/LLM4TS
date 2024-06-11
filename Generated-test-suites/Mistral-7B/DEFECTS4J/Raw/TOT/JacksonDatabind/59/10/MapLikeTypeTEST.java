package com.fasterxml.jackson.databind.type.test;

import static org.junit.Assert.*;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.MapLikeType;
import com.fasterxml.jackson.databind.type.TypeBindings;
import com.fasterxml.jackson.databind.type.TypeBase;
import com.fasterxml.jackson.databind.type.TypeFactory;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

public class MapLikeTypeTest {

    @Test
    public void testBasicMapLikeTypeConstruction() {
        TypeFactory tf = new TypeFactory();
        JavaType mapType = tf.constructCollectionType(Map.class, String.class, Integer.class);
        MapLikeType mapLikeType = MapLikeType.construct(mapType.getRawClass(), String.class, Integer.class);

        assertEquals(mapType.getRawClass(), mapLikeType.getRawClass());
        assertEquals(String.class, mapLikeType.getKeyType());
        assertEquals(Integer.class, mapLikeType.getContentType());
        assertEquals(String.class, mapLikeType.getKeyType().getRawClass());
        assertEquals(Integer.class, mapLikeType.getContentType().getRawClass());
    }

    @Test
    public void testMapLikeTypeWithKeyTypeHandler() {
        TypeFactory tf = new TypeFactory();
        JavaType mapType = tf.constructCollectionType(Map.class, String.class, String.class);
        MapLikeType mapLikeType = MapLikeType.construct(mapType.getRawClass(), String.class, String.class);

        Object keyHandler = new Object() {};
        mapLikeType = mapLikeType.withKeyTypeHandler(keyHandler);

        assertEquals(keyHandler, mapLikeType.getKeyType().getValueHandler());
    }

    @Test
    public void testMapLikeTypeWithContentTypeHandler() {
        TypeFactory tf = new TypeFactory();
        JavaType mapType = tf.constructCollectionType(Map.class, String.class, Integer.class);
        MapLikeType mapLikeType = MapLikeType.construct(mapType.getRawClass(), String.class, Integer.class);

        Object contentHandler = new Object() {};
        mapLikeType = mapLikeType.withContentTypeHandler(contentHandler);

        assertEquals(contentHandler, mapLikeType.getContentType().getValueHandler());
    }

    @Test
    public void testMapLikeTypeWithKeyValueHandler() {
        TypeFactory tf = new TypeFactory();
        JavaType mapType = tf.constructCollectionType(Map.class, String.class, String.class);
        MapLikeType mapLikeType = MapLikeType.construct(mapType.getRawClass(), String.class, String.class);

        Object keyHandler = new Object() {};
        Object valueHandler = new Object() {};
        mapLikeType = mapLikeType.withKeyValueHandler(keyHandler, valueHandler);

        assertEquals(keyHandler, mapLikeType.getKeyType().getValueHandler());
        assertEquals(valueHandler, mapLikeType.getContentType().getValueHandler());
    }

    @Test
    public void testMapLikeTypeWithStaticTyping() {
        TypeFactory tf = new TypeFactory();
        JavaType mapType = tf.constructCollectionType(HashMap.class, String.class, Integer.class);
        MapLikeType mapLikeType = MapLikeType.construct(mapType.getRawClass(), String.class, Integer.class);

        mapLikeType = mapLikeType.withStaticTyping();

        assertTrue(mapLikeType.isStatic());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testMapLikeTypeUpgradeFromInvalidBaseType() {
        TypeFactory tf = new TypeFactory();
        JavaType baseType = tf.constructType(int[].class);
        JavaType keyType = tf.constructType(String.class);
        JavaType valueType = tf.constructType(Integer.class);

        MapLikeType mapLikeType = MapLikeType.upgradeFrom(baseType, keyType, valueType);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testMapLikeTypeConstructInvalidRawType() {
        TypeFactory tf = new TypeFactory();
        JavaType keyType = tf.constructType(String.class);
        JavaType valueType = tf.constructType(Integer.class);

        MapLikeType mapLikeType = MapLikeType.construct(Object.class, keyType, valueType);
    }

    @Test
    public void testMapLikeTypeWithContentTypeRefinement() {
        TypeFactory tf = new TypeFactory();
        JavaType mapType = tf.constructCollectionType(Map.class, String.class, Integer.class);
        JavaType contentType = tf.constructType(Long.class);

        MapLikeType mapLikeType = MapLikeType.construct(mapType.getRawClass(), String.class, Integer.class);

        MapLikeType refinedMapLikeType = mapLikeType.withContentType(contentType);

        assertEquals(contentType, refinedMapLikeType.getContentType());
    }

    @Test
    public void testMapLikeTypeWithKeyTypeRefinement() {
        TypeFactory tf = new TypeFactory();
        JavaType mapType = tf.constructCollectionType(Map.class, String.class, Integer.class);
        JavaType keyType = tf.constructType(Long.class);

        MapLikeType mapLikeType = MapLikeType.construct(mapType.getRawClass(), String.class, Integer.class);

        MapLikeType refinedMapLikeType = mapLikeType.withKeyType(keyType);

        assertEquals(keyType, refinedMapLikeType.getKeyType());
    }

    @Test
    public void testMapLikeTypeWithTypeHandler() {
        TypeFactory tf = new TypeFactory();
        JavaType mapType = tf.constructCollectionType(Map.class, String.class, Integer.class);
        JavaType keyType = tf.constructType(String.class);
        JavaType valueType = tf.constructType(Integer.class);

        MapLikeType mapLikeType = MapLikeType.construct(mapType.getRawClass(), keyType, valueType);
        Object handler = new Object() {};

        MapLikeType refinedMapLikeType = mapLikeType.withTypeHandler(handler);

        assertEquals(handler, refinedMapLikeType.getTypeHandler());
    }

    @Test
    public void testMapLikeTypeWithContentTypeHandler() {
        TypeFactory tf = new TypeFactory();
        JavaType mapType = tf.constructCollectionType(Map.class, String.class, Integer.class);
        JavaType keyType = tf.constructType(String.class);
        JavaType valueType = tf.constructType(Integer.class);

        MapLikeType mapLikeType = MapLikeType.construct(mapType.getRawClass(), keyType, valueType);
        Object contentHandler = new Object() {};

        MapLikeType refinedMapLikeType = mapLikeType.withContentTypeHandler(contentHandler);

        assertEquals(contentHandler, refinedMapLikeType.getContentTypeHandler());
    }

    @Test
    public void testMapLikeTypeWithValueHandler() {
        TypeFactory tf = new TypeFactory();
        JavaType mapType = tf.constructCollectionType(Map.class, String.class, Integer.class);
        JavaType keyType = tf.constructType(String.class);
        JavaType valueType = tf.constructType(Integer.class);

        MapLikeType mapLikeType = MapLikeType.construct(mapType.getRawClass(), keyType, valueType);
        Object valueHandler = new Object() {};

        MapLikeType refinedMapLikeType = mapLikeType.withValueHandler(valueHandler);

        assertEquals(valueHandler, refinedMapLikeType.getValueHandler());
    }

    @Test
    public void testMapLikeTypeWithContentValueHandler() {
        TypeFactory tf = new TypeFactory();
        JavaType mapType = tf.constructCollectionType(Map.class, String.class, Integer.class);
        JavaType keyType = tf.constructType(String.class);
        JavaType valueType = tf.constructType(Integer.class);

        MapLikeType mapLikeType = MapLikeType.construct(mapType.getRawClass(), keyType, valueType);
        Object contentValueHandler = new Object() {};

        MapLikeType refinedMapLikeType = mapLikeType.withContentValueHandler(contentValueHandler);

        assertEquals(contentValueHandler, refinedMapLikeType.getContentValueHandler());
    }

    @Test
    public void testMapLikeTypeIsMapLikeType() {
        TypeFactory tf = new TypeFactory();
        JavaType mapType = tf.constructCollectionType(Map.class, String.class, Integer.class);
        MapLikeType mapLikeType = MapLikeType.construct(mapType.getRawClass(), String.class, Integer.class);

        assertTrue(mapLikeType.isMapLikeType());
    }

    @Test
    public void testMapLikeTypeIsContainerType() {
        TypeFactory tf = new TypeFactory();
        JavaType mapType = tf.constructCollectionType(Map.class, String.class, Integer.class);
        MapLikeType mapLikeType = MapLikeType.construct(mapType.getRawClass(), String.class, Integer.class);

        assertTrue(mapLikeType.isContainerType());
    }
}