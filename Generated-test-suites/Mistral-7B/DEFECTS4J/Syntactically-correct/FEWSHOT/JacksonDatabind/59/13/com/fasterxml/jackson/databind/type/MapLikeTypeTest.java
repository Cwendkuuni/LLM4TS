package org.apache.commons.cli.test; // make sure to use the correct package name

import static org.junit.Assert.*;

import org.junit.Test;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.MapLikeType;

public class MapLikeTypeTest {

    @Test
    public void testMapLikeType() {
        Class<Map<String, Integer>> mapType = Map.class;
        TypeVariable<?>[] vars = mapType.getTypeParameters();
        JavaType keyType = TypeFactory.defaultInstance().constructType(String.class);
        JavaType valueType = TypeFactory.defaultInstance().constructType(Integer.class);
        MapLikeType mapLikeType = MapLikeType.upgradeFrom(mapType, keyType, valueType);
        assertEquals(MapLikeType.class, mapLikeType.getClass());
        assertEquals(mapType, mapLikeType.getRawType());
        assertEquals(keyType, mapLikeType.getKeyType());
        assertEquals(valueType, mapLikeType.getValueType());
    }

    @Test
    public void testMapLikeTypeWithKeyType() {
        JavaType mapLikeType = MapLikeType.construct(Map.class, String.class, Integer.class);
        assertEquals(MapLikeType.class, mapLikeType.getClass());
        assertEquals(Map.class, mapLikeType.getRawType());
        assertEquals(String.class, mapLikeType.getKeyType().getRawClass());
        assertEquals(Integer.class, mapLikeType.getValueType().getRawClass());

        MapLikeType newMapLikeType = mapLikeType.withKeyType(Long.TYPE);
        assertEquals(Long.TYPE, newMapLikeType.getKeyType().getRawClass());
        assertNotEquals(mapLikeType.hashCode(), newMapLikeType.hashCode());
    }

    @Test
    public void testMapLikeTypeWithValueType() {
        JavaType mapLikeType = MapLikeType.construct(Map.class, String.class, Integer.class);
        assertEquals(Map.class, mapLikeType.getRawType());
        assertEquals(String.class, mapLikeType.getKeyType().getRawClass());
        assertEquals(Integer.class, mapLikeType.getValueType().getRawClass());

        JavaType intArrayType = TypeFactory.defaultInstance().constructType(int[].class);
        JavaType newMapLikeType = mapLikeType.withContentType(intArrayType);
        assertEquals(int[].class, newMapLikeType.getValueType().getRawClass());
        assertNotEquals(mapLikeType.hashCode(), newMapLikeType.hashCode());
    }

    @Test
    public void testMapLikeTypeWithKeyTypeHandler() {
        JavaType mapLikeType = MapLikeType.construct(Map.class, String.class, Integer.class);
        Object keyHandler = new Object() {};
        JavaType newMapLikeType = mapLikeType.withKeyTypeHandler(keyHandler);
        assertEquals(keyHandler, newMapLikeType.getKeyTypeHandler());
        assertNull(mapLikeType.getKeyTypeHandler());
        assertNotEquals(mapLikeType.hashCode(), newMapLikeType.hashCode());
    }

    @Test
    public void testMapLikeTypeWithValueHandler() {
        JavaType mapLikeType = MapLikeType.construct(Map.class, String.class, Integer.class);
        Object valueHandler = new Object() {};
        JavaType newMapLikeType = mapLikeType.withValueHandler(valueHandler);
        assertEquals(valueHandler, newMapLikeType.getValueHandler());
        assertNull(mapLikeType.getValueHandler());
        assertNotEquals(mapLikeType.hashCode(), newMapLikeType.hashCode());
    }

    @Test
    public void testMapLikeTypeWithContentValueHandler() {
        JavaType mapLikeType = MapLikeType.construct(Map.class, String.class, Integer.class);
        Object contentValueHandler = new Object() {};
        JavaType newMapLikeType = mapLikeType.withContentValueHandler(contentValueHandler);
        assertEquals(contentValueHandler, newMapLikeType.getContentValueHandler());
        assertNull(mapLikeType.getContentValueHandler());
        assertNotEquals(mapLikeType.hashCode(), newMapLikeType.hashCode());
    }

    @Test
    public void testMapLikeTypeWithTypeHandler() {
        JavaType mapLikeType = MapLikeType.construct(Map.class, String.class, Integer.class);
        Object typeHandler = new Object() {};
        JavaType newMapLikeType = mapLikeType.withTypeHandler(typeHandler);
        assertEquals(typeHandler, newMapLikeType.getTypeHandler());
        assertNull(mapLikeType.getTypeHandler());
        assertNotEquals(mapLikeType.hashCode(), newMapLikeType.hashCode());
    }

    @Test
    public void testMapLikeTypeIsContainerType() {
        JavaType mapLikeType = MapLikeType.construct(Map.class, String.class, Integer.class);
        assertTrue(mapLikeType.isContainerType());
    }

    @Test
    public void testMapLikeTypeIsMapLikeType() {
        JavaType mapLikeType = MapLikeType.construct(Map.class, String.class, Integer.class);
        assertTrue(mapLikeType.isMapLikeType());
    }
}