package com.fasterxml.jackson.databind.type.test;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.MapLikeType;
import com.fasterxml.jackson.databind.type.TypeBase;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Test for {@link MapLikeType} class.
 */
public class MapLikeTypeTest {

    @Test
    public void testMapLikeTypeCreation() {
        // Test constructors
        JavaType javaIntMap = MapLikeType.construct(Map.class, Integer.TYPE, String.class);
        TypeBase mapLikeType1 = new MapLikeType(javaIntMap.getType(), Integer.TYPE, String.class);
        JavaType keyValueType1 = mapLikeType1.getKeyType();
        JavaType valueType1 = mapLikeType1.getContentType();
        assertEquals("java.lang.Integer", keyValueType1.getRawClass().getName());
        assertEquals("java.lang.String", valueType1.getRawClass().getName());

        JavaType javaStringMap = MapLikeType.construct(HashMap.class, String.class, String.class);
        TypeBase mapLikeType2 = new MapLikeType(javaStringMap.getType(), null, null, null, String.class, String.class);
        JavaType keyValueType2 = mapLikeType2.getKeyType();
        JavaType valueType2 = mapLikeType2.getContentType();
        assertEquals("java.lang.String", keyValueType2.getRawClass().getName());
        assertEquals("java.lang.String", valueType2.getRawClass().getName());

        // Test factory method
        JavaType javaListMap = JavaType.constructMapType(List.class, Integer.TYPE, String.class);
        MapLikeType mapLikeType3 = MapLikeType.upgradeFrom(javaListMap, Integer.TYPE, String.class);
        JavaType keyValueType3 = mapLikeType3.getKeyType();
        JavaType valueType3 = mapLikeType3.getContentType();
        assertEquals("java.lang.Integer", keyValueType3.getRawClass().getName());
        assertEquals("java.lang.String", valueType3.getRawClass().getName());
    }

    @Test
    public void testMapLikeTypeMethods() {
        // Test isMapLikeType method
        JavaType javaIntMap = MapLikeType.construct(Map.class, Integer.TYPE, String.class);
        assertTrue(javaIntMap.isMapLikeType());

        // Test isContainerType method
        JavaType javaList = JavaType.constructCollectionType(ArrayList.class, Integer.TYPE);
        assertFalse(javaList.isMapLikeType());
        assertFalse(javaList.isContainerType());

        // Test getKeyType method
        JavaType javaIntMap = MapLikeType.construct(Map.class, Integer.TYPE, String.class);
        TypeBase mapLikeType = new MapLikeType(javaIntMap.getType(), null, null, null, Integer.TYPE, String.class);
        JavaType keyValueType = mapLikeType.getKeyType();
        assertEquals("java.lang.Integer", keyValueType.getRawClass().getName());

        // Test getContentType method
        JavaType javaIntMap = MapLikeType.construct(Map.class, Integer.TYPE, String.class);
        TypeBase mapLikeType = new MapLikeType(javaIntMap.getType(), null, null, null, Integer.TYPE, String.class);
        JavaType valueType = mapLikeType.getContentType();
        assertEquals("java.lang.String", valueType.getRawClass().getName());

        // Test hasHandlers method
        JavaType javaIntMap = MapLikeType.construct(Map.class, Integer.TYPE, String.class);
        TypeBase mapLikeType = new MapLikeType(javaIntMap.getType(), null, null, null, Integer.TYPE, String.class);
        assertFalse(mapLikeType.hasHandlers());

        // Test withKeyType method
        JavaType javaIntStringMap = MapLikeType.construct(Map.class, Integer.class, String.class);
        TypeBase mapLikeType = new MapLikeType(javaIntStringMap.getType(), null, null, null, Integer.class, String.class);
        JavaType newKeyType = JavaType.constructType(Long.class);
        TypeBase newMapLikeType = mapLikeType.withKeyType(newKeyType);
        JavaType newKeyValueType = newMapLikeType.getKeyType();
        assertEquals("java.lang.Long", newKeyValueType.getRawClass().getName());

        // Test withContentType method
        JavaType javaIntStringMap = MapLikeType.construct(Map.class, Integer.class, String.class);
        TypeBase mapLikeType = new MapLikeType(javaIntStringMap.getType(), null, null, null, Integer.class, String.class);
        JavaType newValueType = JavaType.constructType(Double.class);
        TypeBase newMapLikeType = mapLikeType.withContentType(newValueType);
        JavaType newValueType
                = newMapLikeType.getContentType();
        assertEquals("java.lang.Double", newValueType.getRawClass().getName());

        // Test withTypeHandler method
        JavaType javaIntMap = MapLikeType.construct(Map.class, Integer.TYPE, String.class);
        TypeBase mapLikeType = new MapLikeType(javaIntMap.getType(), null, null, null, Integer.TYPE, String.class);
        Object handler = new Object();
        TypeBase newMapLikeType = mapLikeType.withTypeHandler(handler);
        assertEquals(handler, newMapLikeType.getValueHandler());

        // Test withContentTypeHandler method
        JavaType javaIntMap = MapLikeType.construct(Map.class, Integer.TYPE, String.class);
        TypeBase mapLikeType = new MapLikeType(javaIntMap.getType(), null, null, null, Integer.TYPE, String.class);
        Object handler = new Object();
        TypeBase newMapLikeType = mapLikeType.withContentTypeHandler(handler);
        assertEquals(handler, newMapLikeType.getTypeHandler().getValueHandler());

        // Test withValueHandler method
        JavaType javaIntMap = MapLikeType.construct(Map.class, Integer.TYPE, String.class);
        TypeBase mapLikeType = new MapLikeType(javaIntMap.getType(), null, null, null, Integer.TYPE, String.class);
        Object handler = new Object();
        TypeBase newMapLikeType = mapLikeType.withValueHandler(handler);
        assertEquals(handler, newMapLikeType.getValueHandler());

        // Test withContentValueHandler method
        JavaType javaIntMap = MapLikeType.construct(Map.class, Integer.TYPE, String.class);
        TypeBase mapLikeType = new MapLikeType(javaIntMap.getType(), null, null, null, Integer.TYPE, String.class);
        Object handler = new Object();
        TypeBase newMapLikeType = mapLikeType.withContentValueHandler(handler);
        assertEquals(handler, newMapLikeType.getValueType().getValueHandler());
    }

    @Test
    public void testMapLikeTypeEquality() {
        JavaType javaIntMap1 = MapLikeType.construct(Map.class, Integer.TYPE, String.class);
        JavaType javaIntMap2 = MapLikeType.construct(Map.class, Integer.TYPE, String.class);
        TypeBase mapLikeType1 = new MapLikeType(javaIntMap1.getType(), null, null, null, Integer.TYPE, String.class);
        TypeBase mapLikeType2 = new MapLikeType(javaIntMap2.getType(), null, null, null, Integer.TYPE, String.class);
        assertTrue(mapLikeType1.equals(mapLikeType2));

        JavaType javaStringMap1 = MapLikeType.construct(Map.class, String.class, String.class);
        JavaType javaStringMap2 = MapLikeType.construct(Map.class, String.class, String.class);
        TypeBase mapLikeType1 = new MapLikeType(javaStringMap1.getType(), null, null, null, String.class, String.class);
        TypeBase mapLikeType2 = new MapLikeType(javaStringMap2.getType(), null, null, null, String.class, String.class);
        assertTrue(mapLikeType1.equals(mapLikeType2));

        JavaType javaIntMap1 = MapLikeType.construct(Map.class, Integer.TYPE, String.class);
        JavaType javaStringMap1 = MapLikeType.construct(Map.class, String.class, Integer.class);
        TypeBase mapLikeType1 = new MapLikeType(javaIntMap1.getType(), null, null, null, Integer.TYPE, String.class);
        TypeBase mapLikeType2 = new MapLikeType(javaStringMap1.getType(), null, null, null, String.class, Integer.class);
        assertFalse(mapLikeType1.equals(mapLikeType2));

        JavaType javaIntMap1 = MapLikeType.construct(Map.class, Integer.TYPE, String.class);
        JavaType javaIntMap2 = MapLikeType.construct(Map.class, Integer.TYPE, String.class);
        TypeBase mapLikeType1 = new MapLikeType(javaIntMap1.getType(), null, null, null, Integer.TYPE, String.class);
        TypeBase mapLikeType2 = new MapLikeType(javaIntMap1.getType(), null, null, null, Integer.TYPE, String.class);
        TypeBase mapLikeType3 = new MapLikeType(javaIntMap2.getType(), null, null, null, Integer.TYPE, String.class);
        assertTrue(mapLikeType1.equals(mapLikeType2));
        assertTrue(mapLikeType1.equals(mapLikeType3));
    }
}