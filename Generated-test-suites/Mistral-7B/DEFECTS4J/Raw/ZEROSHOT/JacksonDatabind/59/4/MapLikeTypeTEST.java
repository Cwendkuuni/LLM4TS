package com.example.test;

import static org.junit.Assert.*;

import java.util.Arrays;

import org.junit.Test;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.MapLikeType;

public class MapLikeTypeTest {

    @Test
    public void testMapLikeTypeConstruction() {
        JavaType mapType = new MapLikeType(Map.class, null, null, null,
                String.class, Integer.class);
        MapLikeType mapLikeType = new MapLikeType(mapType, String.class,
                Integer.class);
        assertEquals(Map.class, mapLikeType.getRawClass());
        assertEquals(String.class, mapLikeType.getKeyType().getRawClass());
        assertEquals(Integer.class, mapLikeType.getValueType().getRawClass());
    }

    @Test
    public void testMapLikeTypeUpgrade() {
        JavaType baseType = new TypeFactory().constructType(Map.class,
                String.class, Integer.class);
        JavaType keyType = String.class;
        JavaType valueType = Integer.class;
        MapLikeType mapLikeType = MapLikeType.upgradeFrom(baseType, keyType,
                valueType);
        assertEquals(Map.class, mapLikeType.getRawClass());
        assertEquals(String.class, mapLikeType.getKeyType().getRawClass());
        assertEquals(Integer.class, mapLikeType.getValueType().getRawClass());
    }

    @Test
    public void testMapLikeTypeWithKeyType() {
        JavaType mapType = new MapLikeType(Map.class, null, null, null,
                String.class, Integer.class);
        JavaType newKeyType = Long.class;
        MapLikeType mapLikeType = mapType.withKeyType(newKeyType);
        assertEquals(Map.class, mapLikeType.getRawClass());
        assertEquals(Long.class, mapLikeType.getKeyType().getRawClass());
        assertEquals(Integer.class, mapLikeType.getValueType().getRawClass());
    }

    @Test
    public void testMapLikeTypeWithContentType() {
        JavaType mapType = new MapLikeType(Map.class, null, null, null,
                String.class, Integer.class);
        JavaType newValueType = Float.class;
        MapLikeType mapLikeType = mapType.withContentType(newValueType);
        assertEquals(Map.class, mapLikeType.getRawClass());
        assertEquals(String.class, mapLikeType.getKeyType().getRawClass());
        assertEquals(Float.class, mapLikeType.getValueType().getRawClass());
    }

    @Test
    public void testMapLikeTypeWithTypeHandler() {
        JavaType mapType = new MapLikeType(Map.class, null, null, null,
                String.class, Integer.class);
        Object handler = new Object();
        MapLikeType mapLikeType = mapType.withTypeHandler(handler);
        assertEquals(handler, mapLikeType.getTypeHandler());
    }

    @Test
    public void testMapLikeTypeWithContentTypeHandler() {
        JavaType mapType = new MapLikeType(Map.class, null, null, null,
                String.class, Integer.class);
        Object handler = new Object();
        JavaType newValueType = Float.class.withTypeHandler(handler);
        MapLikeType mapLikeType = mapType.withContentTypeHandler(newValueType);
        assertEquals(handler, mapLikeType.getValueType().getTypeHandler());
    }

    @Test
    public void testMapLikeTypeWithValueHandler() {
        JavaType mapType = new MapLikeType(Map.class, null, null, null,
                String.class, Integer.class);
        Object handler = new Object();
        MapLikeType mapLikeType = mapType.withValueHandler(handler);
        assertEquals(handler, mapLikeType.getValueHandler());
    }

    @Test
    public void testMapLikeTypeWithContentValueHandler() {
        JavaType mapType = new MapLikeType(Map.class, null, null, null,
                String.class, Integer.class);
        Object handler = new Object();
        JavaType newValueType = Integer.class.withValueHandler(handler);
        MapLikeType mapLikeType = mapType.withContentValueHandler(newValueType);
        assertEquals(handler, mapLikeType.getValueType().getValueHandler());
    }

    @Test
    public void testMapLikeTypeWithStaticTyping() {
        JavaType mapType = new MapLikeType(Map.class, null, null, null,
                String.class, Integer.class);
        MapLikeType mapLikeType = mapType.withStaticTyping();
        assertTrue(mapLikeType.isStatic());
    }

    @Test
    public void testMapLikeTypeRefine() {
        JavaType mapType = new MapLikeType(Map.class, null, null, null,
                String.class, Integer.class);
        TypeBindings bindings = TypeBindings.emptyBindings();
        JavaType refinedType = mapType.refine(HashMap.class, bindings, null,
                null);
        assertEquals(HashMap.class, refinedType.getRawClass());
        assertEquals(String.class, refinedType.getKeyType().getRawClass());
        assertEquals(Integer.class, refinedType.getValueType().getRawClass());
    }

    @Test
    public void testMapLikeTypeIsContainerType() {
        JavaType mapType = new MapLikeType(Map.class, null, null, null,
                String.class, Integer.class);
        assertTrue(mapType.isContainerType());
    }

    @Test
    public void testMapLikeTypeIsMapLikeType() {
        JavaType mapType = new MapLikeType(Map.class, null, null, null,
                String.class, Integer.class);
        assertTrue(mapType.isMapLikeType());
    }

    @Test
    public void testMapLikeTypeEquals() {
        JavaType mapType1 = new MapLikeType(Map.class, null, null, null,
                String.class, Integer.class);
        JavaType mapType2 = new MapLikeType(Map.class, null, null, null,
                String.class, Integer.class);
        JavaType mapType3 = new MapLikeType(ArrayList.class, null, null, null,
                String.class, Integer.class);
        assertTrue(mapType1.equals(mapType2));
        assertFalse(mapType1.equals(mapType3));
    }

    @Test
    public void testMapLikeTypeHashCode() {
        JavaType mapType1 = new MapLikeType(Map.class, null, null, null,
                String.class, Integer.class);
        JavaType mapType2 = new MapLikeType(Map.class, null, null, null,
                String.class, Integer.class);
        JavaType mapType3 = new MapLikeType(ArrayList.class, null, null, null,
                String.class, Integer.class);
        assertEquals(mapType1.hashCode(), mapType1.hashCode());
        assertEquals(mapType1.hashCode(), mapType2.hashCode());
        assertNotEquals(mapType1.hashCode(), mapType3.hashCode());
    }

    @Test
    public void testMapLikeTypeToString() {
        JavaType mapType = new MapLikeType(Map.class, null, null, null,
                String.class, Integer.class);
        String expected = String.format(
                "[map-like type; class %s, %s -> %s]", Map.class.getName(),
                String.class.getName(), Integer.class.getName());
        assertEquals(expected, mapType.toString());
    }
}