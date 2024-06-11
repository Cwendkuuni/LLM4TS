package com.fasterxml.jackson.databind.type;

import org.junit.Test;
import static org.junit.Assert.*;

public class MapLikeTypeTest {

    @Test
    public void testUpgradeFrom() {
        // Test basic upgrade from method
        JavaType baseType = new SimpleType(String.class);
        JavaType keyType = new SimpleType(Integer.class);
        JavaType valueType = new SimpleType(Double.class);

        MapLikeType mapLikeType = MapLikeType.upgradeFrom(baseType, keyType, valueType);

        assertEquals(baseType, mapLikeType.getSuperClass());
        assertEquals(keyType, mapLikeType.getKeyType());
        assertEquals(valueType, mapLikeType.getContentType());
    }

    @Test
    public void testBuildCanonicalName() {
        // Test building canonical name
        JavaType keyType = new SimpleType(Integer.class);
        JavaType valueType = new SimpleType(Double.class);

        MapLikeType mapLikeType = new MapLikeType(Map.class, null, null, null, keyType, valueType, null, null, false);

        assertEquals("java.util.Map<java.lang.Integer,java.lang.Double>", mapLikeType.buildCanonicalName());
    }

    // Add more test cases for other methods as needed
    
}