package com.fasterxml.jackson.databind.type;

import static org.junit.Assert.*;

import org.junit.Test;

public class MapLikeTypeTest {

    @Test
    public void testIsContainerType() {
        MapLikeType mapLikeType = new MapLikeType(Map.class, TypeBindings.emptyBindings(), _bogusSuperClass(Map.class), null, null, null, null, null, false);
        assertTrue(mapLikeType.isContainerType());
        assertTrue(mapLikeType.isMapLikeType());

        JavaType javaType = new TypeFactory().constructType(Map.class, String.class, Integer.class);
        MapLikeType mapLikeTypeJavaType = MapLikeType.upgradeFrom(javaType.getRawType(), String.class, Integer.class);
        assertTrue(mapLikeTypeJavaType.isContainerType());
        assertTrue(mapLikeTypeJavaType.isMapLikeType());
    }

    @Test
    public void testIsTrueMapType() {
        MapLikeType mapLikeType = new MapLikeType(Map.class, TypeBindings.emptyBindings(), _bogusSuperClass(Map.class), null, null, null, null, null, false);
        assertTrue(mapLikeType.isTrueMapType());

        JavaType javaType = new TypeFactory().constructType(HashMap.class, String.class, Integer.class);
        MapLikeType mapLikeTypeHashMap = MapLikeType.upgradeFrom(javaType.getRawType(), String.class, Integer.class);
        assertTrue(mapLikeTypeHashMap.isTrueMapType());

        JavaType javaTypeWrongType = new TypeFactory().constructType(ArrayList.class, String.class, Integer.class);
        MapLikeType mapLikeTypeArrayList = MapLikeType.upgradeFrom(javaTypeWrongType.getRawType(), String.class, Integer.class);
        assertFalse(mapLikeTypeArrayList.isTrueMapType());
    }

    @Test
    public void testGetKeyType() {
        MapLikeType mapLikeType = new MapLikeType(Map.class, TypeBindings.emptyBindings(), _bogusSuperClass(Map.class), null, null, null, null, null, false);
        JavaType expectedKeyType = new TypeFactory().constructType(String.class);
        assertEquals(expectedKeyType, mapLikeType.getKeyType());

        JavaType javaType = new TypeFactory().constructType(Map.class, String.class, Integer.class);
        MapLikeType mapLikeTypeJavaType = MapLikeType.upgradeFrom(javaType.getRawType(), String.class, Integer.class);
        JavaType expectedKeyTypeJavaType = new TypeFactory().constructType(String.class);
        assertEquals(expectedKeyTypeJavaType, mapLikeTypeJavaType.getKeyType());
    }

    @Test
    public void testGetContentType() {
        MapLikeType mapLikeType = new MapLikeType(Map.class, TypeBindings.emptyBindings(), _bogusSuperClass(Map.class), null, null, null, null, null, false);
        JavaType expectedContentType = new TypeFactory().constructType(Integer.class);
        assertEquals(expectedContentType, mapLikeType.getContentType());

        JavaType javaType = new TypeFactory().constructType(Map.class, String.class, Integer.class);
        MapLikeType mapLikeTypeJavaType = MapLikeType.upgradeFrom(javaType.getRawType(), String.class, Integer.class);
        JavaType expectedContentTypeJavaType = new TypeFactory().constructType(Integer.class);
        assertEquals(expectedContentTypeJavaType, mapLikeTypeJavaType.getContentType());
    }

    @Test
    public void testToString() {
        MapLikeType mapLikeType = new MapLikeType(Map.class, TypeBindings.emptyBindings(), _bogusSuperClass(Map.class), null, null, null, null, null, false);
        String expectedToString = "[map-like type; class java.util.Map, Ljava/lang/String; => Ljava/lang/Integer;]";
        assertEquals(expectedToString, mapLikeType.toString());

        JavaType javaType = new TypeFactory().constructType(Map.class, String.class, Integer.class);
        MapLikeType mapLikeTypeJavaType = MapLikeType.upgradeFrom(javaType.getRawType(), String.class, Integer.class);
        String expectedToStringJavaType = "[map-like type; class java.util.Map, Ljava/lang/String; => Ljava/lang/Integer;]";
        assertEquals(expectedToStringJavaType, mapLikeTypeJavaType.toString());
    }

    @Test
    public void testEquals() {
        MapLikeType mapLikeType1 = new MapLikeType(Map.class, TypeBindings.emptyBindings(), _bogusSuperClass(Map.class), null, null, null, null, null, false);
        MapLikeType mapLikeType2 = new MapLikeType(Map.class, TypeBindings.emptyBindings(), _bogusSuperClass(Map.class), null, null, null, null, null, false);
        assertTrue(mapLikeType1.equals(mapLikeType2));

        MapLikeType mapLikeType3 = new MapLikeType(List.class, TypeBindings.emptyBindings(), _bogusSuperClass(List.class), null, null, null, null, null, false);
        assertFalse(mapLikeType1.equals(mapLikeType3));

        MapLikeType mapLikeType4 = new MapLikeType(Map.class, TypeBindings.emptyBindings(), _bogusSuperClass(Map.class), null, null, null, null, null, true);
        MapLikeType mapLikeType5 = new MapLikeType(Map.class, TypeBindings.emptyBindings(), _bogusSuperClass(Map.class), null, null, null, null, null, true);
        assertTrue(mapLikeType4.equals(mapLikeType5));

        MapLikeType mapLikeType6 = new MapLikeType(HashMap.class, TypeBindings.emptyBindings(), _bogusSuperClass(HashMap.class), null, null, null, null, null, false);
        MapLikeType mapLikeType7 = new MapLikeType(TreeMap.class, TypeBindings.emptyBindings(), _bogusSuperClass(TreeMap.class), null, null, null, null, null, false);
        assertFalse(mapLikeType6.equals(mapLikeType7));
    }
}