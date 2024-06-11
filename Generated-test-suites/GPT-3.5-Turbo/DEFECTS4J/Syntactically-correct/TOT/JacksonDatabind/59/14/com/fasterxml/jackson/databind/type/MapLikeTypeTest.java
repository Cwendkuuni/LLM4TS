package com.fasterxml.jackson.databind.type;

import org.junit.Test;
import static org.junit.Assert.*;

public class MapLikeTypeTest {

    @Test
    public void testConstructor() {
        MapLikeType mapLikeType = new MapLikeType(Map.class, null, null, null, null, null, null, null, false);
        assertNotNull(mapLikeType);
    }

    @Test
    public void testUpgradeFrom() {
        MapLikeType baseType = new MapLikeType(Map.class, null, null, null, null, null, null, null, false);
        MapLikeType upgradedType = MapLikeType.upgradeFrom(baseType, null, null);
        assertNotNull(upgradedType);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testUpgradeFromWithInvalidBaseType() {
        JavaType baseType = new SimpleType();
        MapLikeType.upgradeFrom(baseType, null, null);
    }

    @Test
    public void testConstruct() {
        JavaType keyType = new SimpleType();
        JavaType valueType = new SimpleType();
        MapLikeType mapLikeType = MapLikeType.construct(Map.class, keyType, valueType);
        assertNotNull(mapLikeType);
        assertEquals(Map.class, mapLikeType.getRawClass());
        assertEquals(keyType, mapLikeType.getKeyType());
        assertEquals(valueType, mapLikeType.getContentType());
    }

    @Test
    public void testNarrow() {
        MapLikeType mapLikeType = new MapLikeType(Map.class, null, null, null, null, null, null, null, false);
        JavaType narrowedType = mapLikeType._narrow(HashMap.class);
        assertNotNull(narrowedType);
        assertEquals(HashMap.class, narrowedType.getRawClass());
    }

    @Test
    public void testWithKeyType() {
        JavaType keyType = new SimpleType();
        JavaType valueType = new SimpleType();
        MapLikeType mapLikeType = new MapLikeType(Map.class, null, null, null, keyType, valueType, null, null, false);
        MapLikeType newMapLikeType = mapLikeType.withKeyType(valueType);
        assertNotNull(newMapLikeType);
        assertEquals(valueType, newMapLikeType.getKeyType());
        assertEquals(valueType, newMapLikeType.getContentType());
    }

    @Test
    public void testWithContentTypeHandler() {
        JavaType keyType = new SimpleType();
        JavaType valueType = new SimpleType();
        MapLikeType mapLikeType = new MapLikeType(Map.class, null, null, null, keyType, valueType, null, null, false);
        MapLikeType newMapLikeType = mapLikeType.withContentTypeHandler("handler");
        assertNotNull(newMapLikeType);
        assertEquals("handler", newMapLikeType.getContentTypeHandler());
    }

    @Test
    public void testHasKeyTypeHandler() {
        JavaType keyType = new SimpleType();
        JavaType valueType = new SimpleType();
        MapLikeType mapLikeType = new MapLikeType(Map.class, null, null, null, keyType, valueType, null, null, false);
        MapLikeType newMapLikeType = mapLikeType.withKeyTypeHandler("handler");
        assertNotNull(newMapLikeType);
        assertEquals("handler", newMapLikeType.getKeyTypeHandler());
    }
}