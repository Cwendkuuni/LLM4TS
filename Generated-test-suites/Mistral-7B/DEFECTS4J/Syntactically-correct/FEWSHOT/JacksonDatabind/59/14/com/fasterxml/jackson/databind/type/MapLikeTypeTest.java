package org.apache.commons.cli.test;

import static org.junit.Assert.*;

import org.junit.Test;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.MapLikeType;

public class MapLikeTypeTest {

    @Test
    public void testConstruct() {
        JavaType keyType = new SimpleType("String");
        JavaType valueType = new SimpleType("Integer");
        MapLikeType mapLikeType = MapLikeType.construct(Map.class, keyType, valueType);
        assertEquals(Map.class, mapLikeType.getRawType());
        assertEquals(keyType, mapLikeType.getKeyType());
        assertEquals(valueType, mapLikeType.getValueType());
    }

    @Test
    public void testUpgradeFrom() {
        JavaType baseType = new SimpleType("HashMap");
        JavaType keyType = new SimpleType("String");
        JavaType valueType = new SimpleType("Integer");
        MapLikeType mapLikeType = MapLikeType.upgradeFrom(baseType, keyType, valueType);
        assertEquals(Map.class, mapLikeType.getRawType());
        assertEquals(keyType, mapLikeType.getKeyType());
        assertEquals(valueType, mapLikeType.getValueType());
    }

    @Test
    public void testIsContainerType() {
        JavaType keyType = new SimpleType("String");
        JavaType valueType = new SimpleType("Integer");
        MapLikeType mapLikeType = new MapLikeType(Map.class, null, null, null, keyType, valueType, null, null, false);
        assertTrue(mapLikeType.isContainerType());
    }

    @Test
    public void testIsMapLikeType() {
        JavaType keyType = new SimpleType("String");
        JavaType valueType = new SimpleType("Integer");
        MapLikeType mapLikeType = new MapLikeType(Map.class, null, null, null, keyType, valueType, null, null, false);
        assertTrue(mapLikeType.isMapLikeType());
    }

    @Test
    public void testGetKeyType() {
        JavaType keyType = new SimpleType("String");
        JavaType valueType = new SimpleType("Integer");
        MapLikeType mapLikeType = new MapLikeType(Map.class, null, null, null, keyType, valueType, null, null, false);
        assertEquals(keyType, mapLikeType.getKeyType());
    }

    @Test
    public void testGetValueType() {
        JavaType keyType = new SimpleType("String");
        JavaType valueType = new SimpleType("Integer");
        MapLikeType mapLikeType = new MapLikeType(Map.class, null, null, null, keyType, valueType, null, null, false);
        assertEquals(valueType, mapLikeType.getValueType());
    }

    @Test
    public void testHasHandlers() {
        JavaType keyType = new SimpleType("String");
        JavaType valueType = new SimpleType("Integer");
        MapLikeType mapLikeType = new MapLikeType(Map.class, null, null, null, keyType, valueType, null, null, false);
        assertFalse(mapLikeType.hasHandlers());
    }

    @Test
    public void testIsTrueMapType() {
        JavaType keyType = new SimpleType("String");
        JavaType valueType = new SimpleType("Integer");
        MapLikeType mapLikeType = new MapLikeType(Map.class, null, null, null, keyType, valueType, null, null, false);
        assertTrue(mapLikeType.isTrueMapType());
    }

    @Test
    public void testToString() {
        JavaType keyType = new SimpleType("String");
        JavaType valueType = new SimpleType("Integer");
        MapLikeType mapLikeType = new MapLikeType(Map.class, null, null, null, keyType, valueType, null, null, false);
        assertEquals("[map-like type; class class java.util.Map, String -> Integer]", mapLikeType.toString());
    }

    @Test
    public void testEquals() {
        JavaType keyType1 = new SimpleType("String");
        JavaType valueType1 = new SimpleType("Integer");
        JavaType keyType2 = new SimpleType("String");
        JavaType valueType2 = new SimpleType("Integer");
        MapLikeType mapLikeType1 = new MapLikeType(Map.class, null, null, null, keyType1, valueType1, null, null, false);
        MapLikeType mapLikeType2 = new MapLikeType(Map.class, null, null, null, keyType2, valueType2, null, null, false);
        MapLikeType mapLikeType3 = new MapLikeType(List.class, null, null, null, keyType1, valueType1, null, null, false);

        assertTrue(mapLikeType1.equals(mapLikeType1));
        assertTrue(mapLikeType1.equals(mapLikeType1.withKeyType(keyType1).withValueType(valueType1)));
        assertTrue(mapLikeType1.equals(mapLikeType1.withKeyTypeHandler(null).withValueTypeHandler(null)));
        assertTrue(mapLikeType1.equals(mapLikeType1.withStaticTyping()));
        assertTrue(mapLikeType1.equals(mapLikeType1.refine(Map.class, null, null, null)));

        assertFalse(mapLikeType1.equals(null));
        assertFalse(mapLikeType1.equals(mapLikeType2));
        assertFalse(mapLikeType1.equals(mapLikeType3));
    }

    @Test
    public void testRefine() {
        JavaType keyType = new SimpleType("String");
        JavaType valueType = new SimpleType("Integer");
        MapLikeType mapLikeType = new MapLikeType(Map.class, null, null, null, keyType, valueType, null, null, false);
        MapLikeType refinedMapLikeType = mapLikeType.refine(HashMap.class, null, null, null);
        assertEquals(HashMap.class, refinedMapLikeType.getRawType());
        assertEquals(keyType, refinedMapLikeType.getKeyType());
        assertEquals(valueType, refinedMapLikeType.getValueType());
    }

    @Test
    public void testWithKeyType() {
        JavaType keyType = new SimpleType("String");
        JavaType valueType = new SimpleType("Integer");
        MapLikeType mapLikeType = new MapLikeType(Map.class, null, null, null, keyType, valueType, null, null, false);
        MapLikeType withKeyType = mapLikeType.withKeyType(new SimpleType("Integer"));
        assertNotEquals(mapLikeType, withKeyType);
        assertEquals(Integer.class, withKeyType.getKeyType().getRawType());
    }

    @Test
    public void testWithValueType() {
        JavaType keyType = new SimpleType("String");
        JavaType valueType = new SimpleType("Integer");
        MapLikeType mapLikeType = new MapLikeType(Map.class, null, null, null, keyType, valueType, null, null, false);
        MapLikeType withValueType = mapLikeType.withValueType(new SimpleType("Double"));
        assertNotEquals(mapLikeType, withValueType);
        assertEquals(Double.class, withValueType.getValueType().getRawType());
    }

    @Test
    public void testWithValueHandler() {
        JavaType keyType = new SimpleType("String");
        JavaType valueType = new SimpleType("Integer");
        MapLikeType mapLikeType = new MapLikeType(Map.class, null, null, null, keyType, valueType, null, null, false);
        MapLikeType withValueHandler = mapLikeType.withValueHandler(null);
        assertNotEquals(mapLikeType, withValueHandler);
    }

    @Test
    public void testWithContentValueHandler() {
        JavaType keyType = new SimpleType("String");
        JavaType valueType = new SimpleType("Integer");
        MapLikeType mapLikeType = new MapLikeType(Map.class, null, null, null, keyType, valueType, null, null, false);
        MapLikeType withContentValueHandler = mapLikeType.withContentValueHandler(null);
        assertNotEquals(mapLikeType, withContentValueHandler);
    }

    @Test
    public void testWithKeyTypeHandler() {
        JavaType keyType = new SimpleType("String");
        JavaType valueType = new SimpleType("Integer");
        MapLikeType mapLikeType = new MapLikeType(Map.class, null, null, null, keyType, valueType, null, null, false);
        MapLikeType withKeyTypeHandler = mapLikeType.withKeyTypeHandler(null);
        assertNotEquals(mapLikeType, withKeyTypeHandler);
    }

    @Test
    public void testWithKeyValueHandler() {
        JavaType keyType = new SimpleType("String");
        JavaType valueType = new SimpleType("Integer");
        MapLikeType mapLikeType = new MapLikeType(Map.class, null, null, null, keyType, valueType, null, null, false);
        MapLikeType withKeyValueHandler = mapLikeType.withKeyValueHandler(null);
        assertNotEquals(mapLikeType, withKeyValueHandler);
    }

    @Test
    public void testWithStaticTyping() {
        JavaType keyType = new SimpleType("String");
        JavaType valueType = new SimpleType("Integer");
        MapLikeType mapLikeType = new MapLikeType(Map.class, null, null, null, keyType, valueType, null, null, false);
        MapLikeType withStaticTyping = mapLikeType.withStaticTyping();
        assertNotEquals(mapLikeType, withStaticTyping);
        assertTrue(withStaticTyping.isStaticTyping());
    }
}