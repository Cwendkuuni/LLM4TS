package com.fasterxml.jackson.databind.type.test; // Make sure to import necessary dependencies

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.MapLikeType;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Test class for MapLikeType.
 */
public class MapLikeTypeTest {

    

    @Test
    public void testMapLikeTypeConstruction() {
        JavaType keyType = new SimpleType("java.lang.String");
        JavaType valueType = new SimpleType("java.lang.Integer");
        MapLikeType mapLikeType = new MapLikeType(Map.class, null, null, null, keyType, valueType);
        assertEquals(Map.class, mapLikeType.getRawClass());
        assertEquals(keyType, mapLikeType.getKeyType());
        assertEquals(valueType, mapLikeType.getValueType());
    }

    @Test
    public void testUpgradeFromJavaType() {
        JavaType baseType = new SimpleType("java.util.HashMap");
        JavaType keyType = new SimpleType("java.lang.String");
        JavaType valueType = new SimpleType("java.lang.Integer");
        MapLikeType mapLikeType = MapLikeType.upgradeFrom(baseType, keyType, valueType);
        assertEquals(Map.class, mapLikeType.getRawClass());
        assertEquals(keyType, mapLikeType.getKeyType());
        assertEquals(valueType, mapLikeType.getValueType());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testUpgradeFromInvalidBaseType() {
        JavaType baseType = new SimpleType("java.lang.Integer");
        JavaType keyType = new SimpleType("java.lang.String");
        JavaType valueType = new SimpleType("java.lang.Integer");
        MapLikeType.upgradeFrom(baseType, keyType, valueType);
    }

    @Test
    public void testWithKeyType() {
        JavaType mapLikeType = new MapLikeType(Map.class, null, null, null, null, null);
        JavaType newKeyType = new SimpleType("java.lang.Long");
        MapLikeType newMapLikeType = mapLikeType.withKeyType(newKeyType);
        assertEquals(newKeyType, newMapLikeType.getKeyType());
        assertNotSame(mapLikeType, newMapLikeType);
    }

    @Test
    public void testWithContentType() {
        JavaType mapLikeType = new MapLikeType(Map.class, null, null, null, null, null);
        JavaType newValueType = new SimpleType("java.lang.Double");
        MapLikeType newMapLikeType = mapLikeType.withContentType(newValueType);
        assertEquals(newValueType, newMapLikeType.getValueType());
        assertNotSame(mapLikeType, newMapLikeType);
    }

    @Test
    public void testWithTypeHandler() {
        Object handler = new Object();
        JavaType mapLikeType = new MapLikeType(Map.class, null, null, null, null, null);
        MapLikeType newMapLikeType = mapLikeType.withTypeHandler(handler);
        assertEquals(handler, newMapLikeType.getTypeHandler());
        assertNotSame(mapLikeType, newMapLikeType);
    }

    @Test
    public void testIsTrueMapType() {
        JavaType mapType = new MapLikeType(Map.class, null, null, null, null, null);
        assertTrue(mapType.isTrueMapType());

        JavaType listType = new SimpleType("java.util.ArrayList");
        assertFalse(listType.isTrueMapType());
    }

    @Test
    public void testEquals() {
        JavaType mapLikeType1 = new MapLikeType(Map.class, null, null, null, null, null);
        JavaType mapLikeType2 = new MapLikeType(Map.class, null, null, null, null, null);
        JavaType mapLikeType3 = new MapLikeType(List.class, null, null, null, null, null);

        assertTrue(mapLikeType1.equals(mapLikeType1));
        assertTrue(mapLikeType1.equals(mapLikeType2));
        assertFalse(mapLikeType1.equals(mapLikeType3));
        assertFalse(mapLikeType2.equals(mapLikeType3));
    }

    
}