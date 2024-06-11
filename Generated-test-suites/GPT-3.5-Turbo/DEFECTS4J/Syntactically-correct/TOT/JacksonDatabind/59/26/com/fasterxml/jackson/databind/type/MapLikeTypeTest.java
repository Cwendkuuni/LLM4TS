package com.fasterxml.jackson.databind.type;

import org.junit.Test;
import java.util.Map;
import static org.junit.Assert.*;

public class MapLikeTypeTest {

    @Test
    public void testGetTypeBase() {
        // Test that the class returned by getTypeBase() is the same as the class passed to the constructor
        MapLikeType mapType = new MapLikeType(Map.class, null, null, null, null, null, null, null, false);
        assertEquals(Map.class, mapType.getTypeBase());
    }

    @Test
    public void testGetKeyType() {
        // Test that the key type returned by getKeyType() is the same as the key type passed to the constructor
        JavaType keyType = new SimpleType(String.class);
        MapLikeType mapType = new MapLikeType(Map.class, null, null, null, keyType, null, null, null, false);
        assertEquals(keyType, mapType.getKeyType());
    }

    @Test
    public void testGetValueType() {
        // Test that the value type returned by getValueType() is the same as the value type passed to the constructor
        JavaType valueType = new SimpleType(Integer.class);
        MapLikeType mapType = new MapLikeType(Map.class, null, null, null, null, valueType, null, null, false);
        assertEquals(valueType, mapType.getValueType());
    }

    @Test
    public void testIsTrueMapType() {
        // Test that isTrueMapType() returns true when the underlying class is assignable from Map
        MapLikeType mapType = new MapLikeType(Map.class, null, null, null, null, null, null, null, false);
        assertTrue(mapType.isTrueMapType());
    }

    @Test
    public void testIsTrueMapType_withNonMapClass() {
        // Test that isTrueMapType() returns false when the underlying class is not assignable from Map
        MapLikeType mapType = new MapLikeType(ArrayList.class, null, null, null, null, null, null, null, false);
        assertFalse(mapType.isTrueMapType());
    }

    @Test
    public void testEquals_sameInstance() {
        // Test that two instances of MapLikeType are considered equal when they reference the same object
        MapLikeType mapType1 = new MapLikeType(Map.class, null, null, null, null, null, null, null, false);
        assertTrue(mapType1.equals(mapType1));
    }

    @Test
    public void testEquals_differentClasses() {
        // Test that two instances of MapLikeType with different classes are not considered equal
        MapLikeType mapType1 = new MapLikeType(Map.class, null, null, null, null, null, null, null, false);
        MapLikeType mapType2 = new MapLikeType(ArrayList.class, null, null, null, null, null, null, null, false);
        assertFalse(mapType1.equals(mapType2));
    }

    @Test
    public void testEquals_sameClassAndKeyValueType() {
        // Test that two instances of MapLikeType with the same class, key type, and value type are considered equal
        MapLikeType mapType1 = new MapLikeType(Map.class, null, null, null, null, null, null, null, false);
        MapLikeType mapType2 = new MapLikeType(Map.class, null, null, null, null, null, null, null, false);
        assertTrue(mapType1.equals(mapType2));
    }

    @Test
    public void testEquals_sameClassAndKeyTypeDifferentValueType() {
        // Test that two instances of MapLikeType with the same class and key type but different value types are not considered equal
        JavaType valueType1 = new SimpleType(String.class);
        JavaType valueType2 = new SimpleType(Integer.class);
        MapLikeType mapType1 = new MapLikeType(Map.class, null, null, null, null, valueType1, null, null, false);
        MapLikeType mapType2 = new MapLikeType(Map.class, null, null, null, null, valueType2, null, null, false);
        assertFalse(mapType1.equals(mapType2));
    }

    @Test
    public void testEquals_sameClassAndValueTypeDifferentKeyType() {
        // Test that two instances of MapLikeType with the same class and value type but different key types are not considered equal
        JavaType keyType1 = new SimpleType(String.class);
        JavaType keyType2 = new SimpleType(Integer.class);
        MapLikeType mapType1 = new MapLikeType(Map.class, null, null, null, keyType1, null, null, null, false);
        MapLikeType mapType2 = new MapLikeType(Map.class, null, null, null, keyType2, null, null, null, false);
        assertFalse(mapType1.equals(mapType2));
    }

}