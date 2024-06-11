package com.example.test;

import static org.junit.Assert.*;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.MapLikeType;

public class MapLikeTypeTest {

    @Test
    public void testMapLikeTypeCreation() {
        Class<Map<String, Integer>> mapClass = Map.class;
        JavaType javaType = new MapLikeType(mapClass, null, _bogusSuperClass(mapClass),
                null, JavaType.constructParametric(String.class),
                JavaType.constructParametric(Integer.class));
        MapLikeType mapLikeType = new MapLikeType((TypeBase) javaType.getType(),
                JavaType.constructParametric(String.class),
                JavaType.constructParametric(Integer.class));

        assertEquals(mapClass, mapLikeType.getRawClass());
        assertEquals(String.class, mapLikeType.getKeyType().getRawClass());
        assertEquals(Integer.class, mapLikeType.getValueType().getRawClass());
    }

    @Test
    public void testUpgradeFromJavaType() {
        JavaType listType = JavaType.constructParametric(HashMap.class, String.class,
                Integer.class);
        JavaType mapType = MapLikeType.upgradeFrom(listType,
                JavaType.constructParametric(String.class),
                JavaType.constructParametric(Integer.class));

        assertEquals(Map.class, mapType.getRawClass());
        assertEquals(String.class, mapType.getKeyType().getRawClass());
        assertEquals(Integer.class, mapType.getValueType().getRawClass());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidUpgradeFromJavaType() {
        JavaType listType = JavaType.constructParametric(ArrayList.class, String.class,
                Integer.class);
        MapLikeType.upgradeFrom(listType, JavaType.constructParametric(String.class),
                JavaType.constructParametric(Integer.class));
    }

    @Test
    public void testIsTrueMapType() {
        Class<Map<String, Integer>> mapClass = Map.class;
        JavaType javaType = new MapLikeType(mapClass, null, _bogusSuperClass(mapClass),
                null, JavaType.constructParametric(String.class),
                JavaType.constructParametric(Integer.class));
        MapLikeType mapLikeType = new MapLikeType((TypeBase) javaType.getType(),
                JavaType.constructParametric(String.class),
                JavaType.constructParametric(Integer.class));

        assertTrue(mapLikeType.isTrueMapType());
    }

    @Test
    public void testIsNotTrueMapType() {
        Class<List<Map.Entry<String, Integer>>> listMapEntryClass = List.class;
        JavaType javaType = new MapLikeType(listMapEntryClass, null,
                _bogusSuperClass(listMapEntryClass), null,
                JavaType.constructParametric(Map.Entry.class),
                JavaType.constructParametric(Map.Entry.class));
        MapLikeType mapLikeType = new MapLikeType((TypeBase) javaType.getType(),
                JavaType.constructParametric(Map.Entry.class),
                JavaType.constructParametric(Map.Entry.class));

        assertFalse(mapLikeType.isTrueMapType());
    }

    @Test
    public void testToString() {
        Class<Map<String, Integer>> mapClass = Map.class;
        JavaType javaType = new MapLikeType(mapClass, null, _bogusSuperClass(mapClass),
                null, JavaType.constructParametric(String.class),
                JavaType.constructParametric(Integer.class));
        MapLikeType mapLikeType = new MapLikeType((TypeBase) javaType.getType(),
                JavaType.constructParametric(String.class),
                JavaType.constructParametric(Integer.class));

        String expected = "[map-like type; class java.util.Map, java.lang.String -> java.lang.Integer]";
        assertEquals(expected, mapLikeType.toString());
    }

    @Test
    public void testEquals() {
        Class<Map<String, Integer>> mapClass1 = Map.class;
        Class<Map<String, Integer>> mapClass2 = Map.class;
        JavaType javaType1 = new MapLikeType(mapClass1, null, _bogusSuperClass(mapClass1),
                null, JavaType.constructParametric(String.class),
                JavaType.constructParametric(Integer.class));
        JavaType javaType2 = new MapLikeType(mapClass2, null, _bogusSuperClass(mapClass2),
                null, JavaType.constructParametric(String.class),
                JavaType.constructParametric(Integer.class));
        MapLikeType mapLikeType1 = new MapLikeType((TypeBase) javaType1.getType(),
                JavaType.constructParametric(String.class),
                JavaType.constructParametric(Integer.class));
        MapLikeType mapLikeType2 = new MapLikeType((TypeBase) javaType2.getType(),
                JavaType.constructParametric(String.class),
                JavaType.constructParametric(Integer.class));

        assertTrue(mapLikeType1.equals(mapLikeType1));
        assertTrue(mapLikeType1.equals(mapLikeType2));
        assertTrue(mapLikeType2.equals(mapLikeType1));
    }

    // Add more test methods as needed for testing other methods in MapLikeType class

    private static Class<?> _bogusSuperClass(Class<?> clazz) {
        return Object.class;
    }

}