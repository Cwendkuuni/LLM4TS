package com.fasterxml.jackson.databind.type.test;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.MapLikeType;
import com.fasterxml.jackson.databind.type.TypeBase;
import com.fasterxml.jackson.databind.type.TypeBindings;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class MapLikeTypeTest {

    @Test
    public void testMapLikeTypeConstruction() {
        JavaType keyType = TypeFactory.defaultInstance().constructType(String.class);
        JavaType valueType = TypeFactory.defaultInstance().constructType(Integer.class);
        JavaType mapType = TypeFactory.defaultInstance().constructType(Map.class, keyType, valueType);
        MapLikeType mapLikeType = MapLikeType.construct(mapType.getRawClass(), keyType, valueType);
        assertEquals(mapType, mapLikeType);
        assertEquals(mapType.getRawClass(), mapLikeType.getRawClass());
        assertEquals(keyType, mapLikeType.getKeyType());
        assertEquals(valueType, mapLikeType.getValueType());
        assertNotNull(mapLikeType.getValueHandler());
        assertNotNull(mapLikeType.getTypeHandler());
    }

    @Test
    public void testMapLikeTypeUpgrade() {
        JavaType listType = TypeFactory.defaultInstance().constructType(ArrayList.class, String.class);
        JavaType keyType = TypeFactory.defaultInstance().constructType(String.class);
        JavaType valueType = TypeFactory.defaultInstance().constructType(Integer.class);
        MapLikeType mapLikeType = MapLikeType.upgradeFrom(listType, keyType, valueType);
        assertEquals(mapLikeType.getClass(), MapLikeType.class);
        assertEquals(mapLikeType.getRawClass(), mapLikeType.getRawClass());
        assertEquals(keyType, mapLikeType.getKeyType());
        assertEquals(valueType, mapLikeType.getValueType());
        assertNotNull(mapLikeType.getValueHandler());
        assertNotNull(mapLikeType.getTypeHandler());
    }

    @Test
    public void testMapLikeTypeWithKeyType() {
        JavaType keyType = TypeFactory.defaultInstance().constructType(String.class);
        JavaType valueType = TypeFactory.defaultInstance().constructType(Integer.class);
        MapLikeType mapLikeType = new MapLikeType(null, null, null, null, keyType, valueType);
        JavaType newKeyType = TypeFactory.defaultInstance().constructType(Integer.class);
        MapLikeType newMapLikeType = mapLikeType.withKeyType(newKeyType);
        assertEquals(mapLikeType.getKeyType(), keyType);
        assertEquals(newMapLikeType.getKeyType(), newKeyType);
        assertEquals(mapLikeType.getClass(), MapLikeType.class);
        assertEquals(newMapLikeType.getClass(), MapLikeType.class);
    }

    @Test
    public void testMapLikeTypeWithContentType() {
        JavaType keyType = TypeFactory.defaultInstance().constructType(String.class);
        JavaType valueType = TypeFactory.defaultInstance().constructType(Integer.class);
        JavaType mapLikeType = TypeFactory.defaultInstance().constructType(MapLikeType, keyType, valueType);
        MapLikeType original = new MapLikeType(null, null, null, null, keyType, valueType);
        JavaType newValueType = TypeFactory.defaultInstance().constructType(Long.class);
        MapLikeType newMapLikeType = original.withContentType(newValueType);
        assertEquals(original.getValueType(), valueType);
        assertEquals(newMapLikeType.getValueType(), newValueType);
        assertEquals(original.getClass(), MapLikeType.class);
        assertEquals(newMapLikeType.getClass(), MapLikeType.class);
    }

    @Test
    public void testMapLikeTypeWithStaticTyping() {
        JavaType keyType = TypeFactory.defaultInstance().constructType(String.class);
        JavaType valueType = TypeFactory.defaultInstance().constructType(Integer.class);
        MapLikeType mapLikeType = new MapLikeType(null, null, null, null, keyType, valueType, false);
        MapLikeType withStaticTyping = mapLikeType.withStaticTyping();
        assertTrue(mapLikeType.isStaticTyping());
        assertFalse(withStaticTyping.isStaticTyping());
    }

    @Test
    public void testMapLikeTypeIsContainerType() {
        JavaType keyType = TypeFactory.defaultInstance().constructType(String.class);
        JavaType valueType = TypeFactory.defaultInstance().constructType(Integer.class);
        MapLikeType mapLikeType = new MapLikeType(null, null, null, null, keyType, valueType);
        assertTrue(mapLikeType.isContainerType());
    }

    @Test
    public void testMapLikeTypeIsMapLikeType() {
        JavaType keyType = TypeFactory.defaultInstance().constructType(String.class);
        JavaType valueType = TypeFactory.defaultInstance().constructType(Integer.class);
        MapLikeType mapLikeType = new MapLikeType(null, null, null, null, keyType, valueType);
        assertTrue(mapLikeType.isMapLikeType());
    }

    @Test
    public void testMapLikeTypeGetKeyType() {
        JavaType keyType = TypeFactory.defaultInstance().constructType(String.class);
        JavaType valueType = TypeFactory.defaultInstance().constructType(Integer.class);
        MapLikeType mapLikeType = new MapLikeType(null, null, null, null, keyType, valueType);
        assertEquals(mapLikeType.getKeyType(), keyType);
    }

    @Test
    public void testMapLikeTypeGetContentType() {
        JavaType keyType = TypeFactory.defaultInstance().constructType(String.class);
        JavaType valueType = TypeFactory.defaultInstance().constructType(Integer.class);
        MapLikeType mapLikeType = new MapLikeType(null, null, null, null, keyType, valueType);
        assertEquals(mapLikeType.getValueType(), valueType);
    }

    @Test
    public void testMapLikeTypeHasHandlers() {
        JavaType keyType = TypeFactory.defaultInstance().constructType(String.class);
        JavaType valueType = TypeFactory.defaultInstance().constructType(Integer.class);
        MapLikeType mapLikeType = new MapLikeType(null, null, null, null, keyType, valueType);
        assertTrue(mapLikeType.hasHandlers());
        assertFalse(mapLikeType.getKeyType().hasHandlers());
        assertFalse(mapLikeType.getValueType().hasHandlers());
    }

    @Test
    public void testMapLikeTypeGetErasedSignature() {
        JavaType keyType = TypeFactory.defaultInstance().constructType(String.class);
        JavaType valueType = TypeFactory.defaultInstance().constructType(Integer.class);
        MapLikeType mapLikeType = new MapLikeType(null, null, null, null, keyType, valueType);
        String erasedSignature = mapLikeType.getErasedSignature(new StringBuilder()).toString();
        assertEquals("java.util.Map", erasedSignature);
    }

    @Test
    public void testMapLikeTypeGetGenericSignature() {
        JavaType keyType = TypeFactory.defaultInstance().constructType(String.class);
        JavaType valueType = TypeFactory.defaultInstance().constructType(Integer.class);
        MapLikeType mapLikeType = new MapLikeType(null, null, null, null, keyType, valueType);
        String genericSignature = mapLikeType.getGenericSignature(new StringBuilder()).toString();
        assertEquals("[Ljava.util.Map;<Ljava.lang.String;Ljava.lang.Integer;>", genericSignature);
    }
}