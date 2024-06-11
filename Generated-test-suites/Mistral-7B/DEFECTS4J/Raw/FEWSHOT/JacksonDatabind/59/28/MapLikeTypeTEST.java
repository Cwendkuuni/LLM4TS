package org.apache.commons.cli; // for consistency with the givenexample

import static org.junit.Assert.*;
import org.junit.Test;

public class MapLikeTypeTest {

    @Test
    public void testConstruct() {
        JavaType keyType = new ClassType(String.class);
        JavaType valueType = new ClassType(Integer.class);
        MapLikeType mapLikeType = MapLikeType.construct(Map.class, keyType, valueType);
        assertEquals(Map.class, mapLikeType.getRawType());
        assertEquals(keyType, mapLikeType.getKeyType());
        assertEquals(valueType, mapLikeType.getValueType());
    }

    @Test
    public void testUpgradeFrom() {
        JavaType baseType = new ClassType(HashMap.class);
        JavaType keyType = new ClassType(String.class);
        JavaType valueType = new ClassType(Integer.class);
        MapLikeType mapLikeType = MapLikeType.upgradeFrom(baseType, keyType, valueType);
        assertEquals(Map.class, mapLikeType.getRawType());
        assertEquals(keyType, mapLikeType.getKeyType());
        assertEquals(valueType, mapLikeType.getValueType());
    }

    @Test
    public void testIsTrueMapType() {
        JavaType keyType = new ClassType(String.class);
        JavaType valueType = new ClassType(Integer.class);
        MapLikeType mapLikeType = new MapLikeType(Map.class, null, null, null, keyType, valueType, null, null, false);
        assertTrue(mapLikeType.isTrueMapType());

        JavaType listType = new ArrayType(int[].class);
        MapLikeType listLikeType = new MapLikeType(List.class, null, null, null, keyType, valueType, null, null, false);
        assertFalse(listLikeType.isTrueMapType());
    }

    @Test
    public void testGetGenericSignature() {
        JavaType keyType = new ClassType(String.class);
        JavaType valueType = new ClassType(Integer.class);
        MapLikeType mapLikeType = new MapLikeType(Map.class, null, null, null, keyType, valueType, null, null, false);
        String expected = "[map-like type; class java.util.Map, java.lang.String->java.lang.Integer]";
        assertEquals(expected, mapLikeType.getGenericSignature(new StringBuilder()).toString());
    }

    @Test
    public void testGetErasedSignature() {
        JavaType keyType = new ClassType(String.class);
        JavaType valueType = new ClassType(Integer.class);
        MapLikeType mapLikeType = new MapLikeType(Map.class, null, null, null, keyType, valueType, null, null, false);
        String expected = "java.util.Map<java.lang.String, java.lang.Integer>";
        assertEquals(expected, mapLikeType.getErasedSignature(new StringBuilder()).toString());
    }

    @Test
    public void testWithKeyType() {
        JavaType baseType = new ClassType(HashMap.class);
        JavaType keyType = new ClassType(String.class);
        JavaType valueType = new ClassType(Integer.class);
        MapLikeType mapLikeType = MapLikeType.upgradeFrom(baseType, keyType, valueType);
        MapLikeType newMapLikeType = mapLikeType.withKeyType(new ClassType(Long.class));
        assertEquals(Long.class, newMapLikeType.getKeyType().getRawType());
        assertEquals(valueType, newMapLikeType.getValueType());
    }

    @Test
    public void testWithValueType() {
        JavaType keyType = new ClassType(String.class);
        JavaType baseValueType = new ClassType(Integer.class);
        JavaType newValueType = new ClassType(Long.class);
        MapLikeType mapLikeType = new MapLikeType(Map.class, null, null, null, keyType, baseValueType, null, null, false);
        MapLikeType newMapLikeType = mapLikeType.withValueType(newValueType);
        assertEquals(keyType, newMapLikeType.getKeyType());
        assertEquals(newValueType, newMapLikeType.getValueType());
    }

    @Test
    public void testWithTypeHandler() {
        JavaType keyType = new ClassType(String.class);
        JavaType valueType = new ClassType(Integer.class);
        MapLikeType mapLikeType = new MapLikeType(Map.class, null, null, null, keyType, valueType, null, null, false);
        Object handler = new Object(){};
        MapLikeType newMapLikeType = mapLikeType.withTypeHandler(handler);
        assertEquals(handler, newMapLikeType.getTypeHandler());
    }

    @Test
    public void testWithContentTypeHandler() {
        JavaType keyType = new ClassType(String.class);
        JavaType valueType = new ClassType(Integer.class);
        MapLikeType mapLikeType = new MapLikeType(Map.class, null, null, null, keyType, valueType, null, null, false);
        Object handler = new Object(){};
        JavaType newValueType = new ClassType(Long.class);
        MapLikeType newMapLikeType = mapLikeType.withContentTypeHandler(handler).withValueType(newValueType);
        assertEquals(newValueType, newMapLikeType.getValueType());
        assertEquals(handler, newMapLikeType.getTypeHandler());
    }

    @Test
    public void testWithContentValueHandler() {
        JavaType keyType = new ClassType(String.class);
        JavaType valueType = new ClassType(Integer.class);
        MapLikeType mapLikeType = new MapLikeType(Map.class, null, null, null, keyType, valueType, null, null, false);
        Object handler = new Object(){};
        MapLikeType newMapLikeType = mapLikeType.withContentValueHandler(handler);
        assertEquals(handler, newMapLikeType.getValueHandler());
    }

    @Test
    public void testWithKeyTypeHandler() {
        JavaType keyType = new ClassType(String.class);
        JavaType valueType = new ClassType(Integer.class);
        MapLikeType mapLikeType = new MapLikeType(Map.class, null, null, null, keyType, valueType, null, null, false);
        Object handler = new Object(){};
        MapLikeType newMapLikeType = mapLikeType.withKeyTypeHandler(handler);
        assertEquals(handler, newMapLikeType.getKeyTypeHandler());
    }

    @Test
    public void testWithKeyValueHandler() {
        JavaType keyType = new ClassType(String.class);
        JavaType valueType = new ClassType(Integer.class);
        MapLikeType mapLikeType = new MapLikeType(Map.class, null, null, null, keyType, valueType, null, null, false);
        Object handler = new Object(){};
        MapLikeType newMapLikeType = mapLikeType.withKeyValueHandler(handler);
        assertEquals(handler, newMapLikeType.getKeyValueHandler());
    }

    @Test
    public void testRefine() {
        JavaType keyType = new ClassType(String.class);
        JavaType valueType = new ClassType(Integer.class);
        MapLikeType mapLikeType = new MapLikeType(Map.class, null, null, null, keyType, valueType, null, null, false);
        JavaType refinedType = mapLikeType.refine(HashMap.class, null, null, null);
        assertEquals(HashMap.class, refinedType.getRawType());
        assertEquals(keyType, refinedType.getKeyType());
        assertEquals(valueType, refinedType.getValueType());
    }
}