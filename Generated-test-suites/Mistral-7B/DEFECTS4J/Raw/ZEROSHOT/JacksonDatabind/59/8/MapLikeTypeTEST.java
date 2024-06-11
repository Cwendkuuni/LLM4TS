package com.example.junit;

import static org.junit.Assert.*;

import java.lang.reflect.TypeVariable;
import java.util.Arrays;

import org.junit.Test;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.MapLikeType;

public class MapLikeTypeTest {

    @Test
    public void testMapLikeTypeConstruction() {
        JavaType keyType = JavaType.constructTypes("java.lang.String");
        JavaType valueType = JavaType.constructTypes("java.lang.Integer");
        MapLikeType mapLikeType = new MapLikeType(Map.class, null, null, null,
                keyType, valueType);

        assertEquals("java.util.Map", mapLikeType.getRawClass().getName());
        assertEquals(keyType, mapLikeType.getKeyType());
        assertEquals(valueType, mapLikeType.getValueType());
    }

    @Test
    public void testMapLikeTypeUpgrade() {
        JavaType baseType = JavaType.constructTypes("java.util.HashMap");
        JavaType keyType = JavaType.constructTypes("java.lang.String");
        JavaType valueType = JavaType.constructTypes("java.lang.Integer");
        MapLikeType mapLikeType = MapLikeType.upgradeFrom(baseType, keyType,
                valueType);

        assertEquals("java.util.HashMap", mapLikeType.getRawClass().getName());
        assertEquals(keyType, mapLikeType.getKeyType());
        assertEquals(valueType, mapLikeType.getValueType());
    }

    @Test
    public void testMapLikeTypeWithKeyType() {
        JavaType baseType = JavaType.constructTypes("java.util.HashMap");
        JavaType keyType = JavaType.constructTypes("java.lang.String");
        JavaType valueType = JavaType.constructTypes("java.lang.Integer");
        MapLikeType mapLikeType = new MapLikeType(baseType, keyType, valueType);
        MapLikeType newMapLikeType = mapLikeType.withKeyType(JavaType.constructTypes("java.lang.Integer"));

        assertNotSame(mapLikeType, newMapLikeType);
        assertEquals(JavaType.constructTypes("java.lang.Integer"), newMapLikeType.getKeyType());
        assertEquals(valueType, newMapLikeType.getValueType());
    }

    @Test
    public void testMapLikeTypeWithContentType() {
        JavaType baseType = JavaType.constructTypes("java.util.HashMap");
        JavaType keyType = JavaType.constructTypes("java.lang.String");
        JavaType valueType = JavaType.constructTypes("java.lang.Integer");
        MapLikeType mapLikeType = new MapLikeType(baseType, keyType, valueType);
        JavaType newValueType = JavaType.constructTypes("java.lang.Double");
        MapLikeType newMapLikeType = mapLikeType.withContentType(newValueType);

        assertNotSame(mapLikeType, newMapLikeType);
        assertEquals(keyType, newMapLikeType.getKeyType());
        assertEquals(newValueType, newMapLikeType.getValueType());
    }

    @Test
    public void testMapLikeTypeWithTypeHandler() {
        JavaType baseType = JavaType.constructTypes("java.util.HashMap");
        JavaType keyType = JavaType.constructTypes("java.lang.String");
        JavaType valueType = JavaType.constructTypes("java.lang.Integer");
        MapLikeType mapLikeType = new MapLikeType(baseType, keyType, valueType);
        Object handler = new Object();
        MapLikeType newMapLikeType = mapLikeType.withTypeHandler(handler);

        assertNotSame(mapLikeType, newMapLikeType);
        assertEquals(handler, newMapLikeType.getTypeHandler());
        assertEquals(handler, newMapLikeType.getValueType().getTypeHandler());
    }

    @Test
    public void testMapLikeTypeWithContentTypeHandler() {
        JavaType baseType = JavaType.constructTypes("java.util.HashMap");
        JavaType keyType = JavaType.constructTypes("java.lang.String");
        JavaType valueType = JavaType.constructTypes("java.lang.Integer");
        MapLikeType mapLikeType = new MapLikeType(baseType, keyType, valueType);
        Object handler = new Object();
        JavaType newValueType = JavaType.constructTypes("java.lang.Double")
                .withTypeHandler(handler);
        MapLikeType newMapLikeType = mapLikeType.withContentTypeHandler(handler);

        assertNotSame(mapLikeType, newMapLikeType);
        assertEquals(keyType, newMapLikeType.getKeyType());
        assertEquals(newValueType, newMapLikeType.getValueType());
        assertEquals(handler, newMapLikeType.getValueType().getTypeHandler());
    }

    @Test
    public void testMapLikeTypeWithValueHandler() {
        JavaType baseType = JavaType.constructTypes("java.util.HashMap");
        JavaType keyType = JavaType.constructTypes("java.lang.String");
        JavaType valueType = JavaType.constructTypes("java.lang.Integer");
        MapLikeType mapLikeType = new MapLikeType(baseType, keyType, valueType);
        Object handler = new Object();
        MapLikeType newMapLikeType = mapLikeType.withValueHandler(handler);

        assertNotSame(mapLikeType, newMapLikeType);
        assertEquals(handler, newMapLikeType.getValueHandler());
    }

    @Test
    public void testMapLikeTypeWithContentValueHandler() {
        JavaType baseType = JavaType.constructTypes("java.util.HashMap");
        JavaType keyType = JavaType.constructTypes("java.lang.String");
        JavaType valueType = JavaType.constructTypes("java.lang.Integer");
        MapLikeType mapLikeType = new MapLikeType(baseType, keyType,
                valueType);
        Object handler = new Object();
        JavaType newValueType = JavaType.constructTypes("java.lang.Double")
                .withValueHandler(handler);
        MapLikeType newMapLikeType = mapLikeType.withContentValueHandler(handler);

        assertNotSame(mapLikeType, newMapLikeType);
        assertEquals(newValueType, newMapLikeType.getValueType());
        assertEquals(handler, newMapLikeType.getValueType().getValueHandler());
    }

    @Test
    public void testMapLikeTypeWithKeyTypeHandler() {
        JavaType baseType = JavaType.constructTypes("java.util.HashMap");
        JavaType keyType = JavaType.constructTypes("java.lang.String");
        JavaType valueType = JavaType.constructTypes("java.lang.Integer");
        MapLikeType mapLikeType = new MapLikeType(baseType, keyType, valueType);
        Object handler = new Object();
        MapLikeType newMapLikeType = mapLikeType.withKeyTypeHandler(handler);

        assertNotSame(mapLikeType, newMapLikeType);
        assertEquals(handler, newMapLikeType.getKeyType().getTypeHandler());
    }

    @Test
    public void testMapLikeTypeWithKeyValueHandler() {
        JavaType baseType = JavaType.constructTypes("java.util.HashMap");
        JavaType keyType = JavaType.constructTypes("java.lang.String");
        JavaType valueType = JavaType.constructTypes("java.lang.Integer");
        MapLikeType mapLikeType = new MapLikeType(baseType, keyType, valueType);
        Object handler = new Object();
        MapLikeType newMapLikeType = mapLikeType.withKeyValueHandler(handler);

        assertNotSame(mapLikeType, newMapLikeType);
        assertEquals(handler, newMapLikeType.getKeyType().getValueHandler());
    }

    @Test
    public void testMapLikeTypeIsContainerType() {
        JavaType baseType = JavaType.constructTypes("java.util.HashMap");
        MapLikeType mapLikeType = new MapLikeType(baseType, null, null);

        assertTrue(mapLikeType.isContainerType());
        assertTrue(mapLikeType.isMapLikeType());
    }

    @Test
    public void testMapLikeTypeIsTrueMapType() {
        JavaType baseType = JavaType.constructTypes("java.util.HashMap");
        MapLikeType mapLikeType = new MapLikeType(baseType, null, null);

        assertTrue(mapLikeType.isTrueMapType());
    }

    @Test
    public void testMapLikeTypeToString() {
        JavaType baseType = JavaType.constructTypes("java.util.HashMap");
        MapLikeType mapLikeType = new MapLikeType(baseType, null, null);

        String expected = String.format(
                "[map-like type; class %s, %s -> %s]", baseType.getRawClass()
                        .getName(), mapLikeType.getKeyType(), mapLikeType
                        .getValueType());
        String result = mapLikeType.toString();

        assertEquals(expected, result);
    }
}