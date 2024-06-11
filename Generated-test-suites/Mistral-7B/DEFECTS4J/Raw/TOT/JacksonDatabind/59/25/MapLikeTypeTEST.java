package com.fasterxml.jackson.databind.type.test;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.MapLikeType;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class MapLikeTypeTest {

    @Test
    public void testMapLikeTypeCreation() {
        JavaType mapType = Map.class;
        TypeVariable<?>[] mapTypeParameters = mapType.getTypeParameters();
        JavaType keyType = String.class;
        JavaType valueType = Integer.class;

        MapLikeType mapLikeType = MapLikeType.construct(mapType, keyType, valueType);

        assertEquals("java.util.Map", mapLikeType.getClassName());
        assertEquals(keyType, mapLikeType.getKeyType());
        assertEquals(valueType, mapLikeType.getContentType());
        assertNotNull(mapLikeType.getValueHandler());
        assertNotNull(mapLikeType.getTypeHandler());
    }

    @Test
    public void testMapLikeTypeCreationFromTypeBase() {
        JavaType baseType = Map.class;
        JavaType keyType = String.class;
        JavaType valueType = Integer.class;

        MapLikeType mapLikeType = MapLikeType.upgradeFrom(baseType, keyType, valueType);

        assertEquals("java.util.Map", mapLikeType.getClassName());
        assertEquals(keyType, mapLikeType.getKeyType());
        assertEquals(valueType, mapLikeType.getContentType());
        assertNotNull(mapLikeType.getValueHandler());
        assertNotNull(mapLikeType.getTypeHandler());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testMapLikeTypeCreationInvalidBaseType() {
        JavaType baseType = List.class;
        JavaType keyType = String.class;
        JavaType valueType = Integer.class;

        MapLikeType mapLikeType = MapLikeType.construct(baseType, keyType, valueType);
    }

    @Test
    public void testMapLikeTypeWithKeyType() {
        JavaType mapType = Map.class;
        TypeVariable<?>[] mapTypeParameters = mapType.getTypeParameters();
        JavaType javaTypeKey = String.class;
        JavaType javaTypeValue = Integer.class;

        MapLikeType mapLikeType = new MapLikeType(mapType, javaTypeKey, javaTypeValue);

        assertEquals("java.util.Map", mapLikeType.getClassName());
        assertEquals(javaTypeKey, mapLikeType.getKeyType());
        assertEquals(javaTypeValue, mapLikeType.getContentType());
        assertNotNull(mapLikeType.getValueHandler());
        assertNotNull(mapLikeType.getTypeHandler());
    }

    @Test
    public void testMapLikeTypeWithContentType() {
        JavaType mapType = Map.class;
        TypeVariable<?>[] mapTypeParameters = mapType.getTypeParameters();
        JavaType javaTypeKey = String.class;
        JavaType javaTypeValue = Integer.class;

        MapLikeType mapLikeType = new MapLikeType(mapType, javaTypeKey, javaTypeValue);

        JavaType newValueType = Long.class;

        MapLikeType newMapLikeType = mapLikeType.withContentType(newValueType);

        assertEquals("java.util.Map", newMapLikeType.getClassName());
        assertEquals(javaTypeKey, newMapLikeType.getKeyType());
        assertEquals(newValueType, newMapLikeType.getContentType());
        assertNotNull(newMapLikeType.getValueHandler());
        assertNotNull(newMapLikeType.getTypeHandler());
    }

    @Test
    public void testMapLikeTypeWithValueHandler() {
        JavaType mapType = Map.class;
        TypeVariable<?>[] mapTypeParameters = mapType.getTypeParameters();
        JavaType javaTypeKey = String.class;
        JavaType javaTypeValue = Integer.class;

        MapLikeType mapLikeType = new MapLikeType(mapType, javaTypeKey, javaTypeValue);

        Object valueHandler = new Object() {};

        MapLikeType newMapLikeType = mapLikeType.withValueHandler(valueHandler);

        assertEquals("java.util.Map", newMapLikeType.getClassName());
        assertEquals(javaTypeKey, newMapLikeType.getKeyType());
        assertEquals(javaTypeValue, newMapLikeType.getContentType());
        assertEquals(valueHandler, newMapLikeType.getValueHandler());
        assertNotNull(newMapLikeType.getTypeHandler());
    }

    @Test
    public void testMapLikeTypeWithContentValueHandler() {
        JavaType mapType = Map.class;
        TypeVariable<?>[] mapTypeParameters = mapType.getTypeParameters();
        JavaType javaTypeKey = String.class;
        JavaType javaTypeValue = Integer.class;

        MapLikeType mapLikeType = new MapLikeType(mapType, javaTypeKey, javaTypeValue);

        Object contentValueHandler = new Object() {};

        MapLikeType newMapLikeType = mapLikeType.withContentValueHandler(contentValueHandler);

        assertEquals("java.util.Map", newMapLikeType.getClassName());
        assertEquals(javaTypeKey, newMapLikeType.getKeyType());
        assertEquals(javaTypeValue, newMapLikeType.getContentType());
        assertNotNull(newMapLikeType.getValueHandler());
        assertEquals(contentValueHandler, newMapLikeType.getContentValueHandler());
        assertNotNull(newMapLikeType.getTypeHandler());
    }

    @Test
    public void testMapLikeTypeIsContainer() {
        JavaType mapType = Map.class;
        JavaType javaTypeKey = String.class;
        JavaType javaTypeValue = Integer.class;
        MapLikeType mapLikeType = new MapLikeType(mapType, javaTypeKey, javaTypeValue);

        assertTrue(mapLikeType.isContainerType());
        assertTrue(mapLikeType.isMapLikeType());
    }
}