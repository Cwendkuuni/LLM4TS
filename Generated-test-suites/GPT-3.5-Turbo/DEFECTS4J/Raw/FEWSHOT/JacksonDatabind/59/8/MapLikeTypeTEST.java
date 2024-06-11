package com.fasterxml.jackson.databind.type;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.fasterxml.jackson.databind.JavaType;

public class MapLikeTypeTest {

    @Test
    public void testUpgradeFrom() {
        JavaType baseType = new SimpleType(Object.class);
        JavaType keyType = new SimpleType(Integer.class);
        JavaType valueType = new SimpleType(String.class);
        MapLikeType mapLikeType = MapLikeType.upgradeFrom(baseType, keyType, valueType);

        assertNotNull(mapLikeType);
        assertEquals(Object.class, mapLikeType.getRawClass());
        assertEquals(keyType, mapLikeType.getKeyType());
        assertEquals(valueType, mapLikeType.getContentType());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testUpgradeFromWithInvalidBaseType() {
        JavaType baseType = new SimpleType(String.class);
        JavaType keyType = new SimpleType(Integer.class);
        JavaType valueType = new SimpleType(String.class);
        MapLikeType.upgradeFrom(baseType, keyType, valueType);
    }

    @Test
    public void testConstruct() {
        JavaType keyType = new SimpleType(Integer.class);
        JavaType valueType = new SimpleType(String.class);
        MapLikeType mapLikeType = MapLikeType.construct(Map.class, keyType, valueType);

        assertNotNull(mapLikeType);
        assertEquals(Map.class, mapLikeType.getRawClass());
        assertEquals(keyType, mapLikeType.getKeyType());
        assertEquals(valueType, mapLikeType.getContentType());
    }

    @Test
    public void testNarrow() {
        JavaType keyType = new SimpleType(Integer.class);
        JavaType valueType = new SimpleType(String.class);
        MapLikeType baseType = MapLikeType.construct(Map.class, keyType, valueType);

        JavaType narrowedType = baseType._narrow(HashMap.class);

        assertNotNull(narrowedType);
        assertEquals(HashMap.class, narrowedType.getRawClass());
        assertEquals(keyType, narrowedType.getKeyType());
        assertEquals(valueType, narrowedType.getContentType());
    }

    @Test
    public void testWithKeyType() {
        JavaType baseType = new SimpleType(Map.class);
        JavaType keyType = new SimpleType(String.class);
        JavaType valueType = new SimpleType(Integer.class);

        MapLikeType mapLikeType = MapLikeType.upgradeFrom(baseType, keyType, valueType);

        assertEquals(keyType, mapLikeType.getKeyType());
        assertEquals(mapLikeType, mapLikeType.withKeyType(keyType));
    }

    @Test
    public void testWithContentType() {
        JavaType baseType = new SimpleType(Map.class);
        JavaType keyType = new SimpleType(String.class);
        JavaType valueType = new SimpleType(Integer.class);

        MapLikeType mapLikeType = MapLikeType.upgradeFrom(baseType, keyType, valueType);

        assertEquals(valueType, mapLikeType.getContentType());
        assertEquals(mapLikeType, mapLikeType.withContentType(valueType));
    }

    @Test
    public void testWithKeyHandler() {
        JavaType baseType = new SimpleType(Map.class);
        JavaType keyType = new SimpleType(String.class);
        JavaType valueType = new SimpleType(Integer.class);

        Object keyHandler = new Object();
        MapLikeType mapLikeType = MapLikeType.upgradeFrom(baseType, keyType, valueType);

        assertEquals(keyHandler, mapLikeType.withKeyTypeHandler(keyHandler).getKeyType().getTypeHandler());
    }

    @Test
    public void testWithKeyValueHandler() {
        JavaType baseType = new SimpleType(Map.class);
        JavaType keyType = new SimpleType(String.class);
        JavaType valueType = new SimpleType(Integer.class);

        Object keyValueHandler = new Object();
        MapLikeType mapLikeType = MapLikeType.upgradeFrom(baseType, keyType, valueType);

        assertEquals(keyValueHandler, mapLikeType.withKeyValueHandler(keyValueHandler).getKeyType().getValueHandler());
    }

    @Test
    public void testWithStaticTyping() {
        JavaType baseType = new SimpleType(Map.class);
        JavaType keyType = new SimpleType(String.class);
        JavaType valueType = new SimpleType(Integer.class);

        MapLikeType mapLikeType = MapLikeType.upgradeFrom(baseType, keyType, valueType);

        assertFalse(mapLikeType.isStaticTyping());
        assertTrue(mapLikeType.withStaticTyping().isStaticTyping());
    }

    @Test
    public void testToString() {
        JavaType keyType = new SimpleType(String.class);
        JavaType valueType = new SimpleType(Integer.class);
        MapLikeType mapLikeType = MapLikeType.construct(Map.class, keyType, valueType);

        assertEquals("[map-like type; class java.util.Map, class java.lang.String -> class java.lang.Integer]",
                mapLikeType.toString());
    }

    @Test
    public void testEquals() {
        JavaType keyType = new SimpleType(String.class);
        JavaType valueType = new SimpleType(Integer.class);
        MapLikeType mapLikeType1 = MapLikeType.construct(Map.class, keyType, valueType);
        MapLikeType mapLikeType2 = MapLikeType.construct(Map.class, keyType, valueType);

        assertTrue(mapLikeType1.equals(mapLikeType2));
    }
}