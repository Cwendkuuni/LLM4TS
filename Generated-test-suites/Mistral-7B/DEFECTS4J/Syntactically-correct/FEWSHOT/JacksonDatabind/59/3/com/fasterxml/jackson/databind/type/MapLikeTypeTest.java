package com.fasterxml.jackson.databind.type;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertFalse;

import java.lang.reflect.TypeVariable;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

public class MapLikeTypeTest {

    @Test
    public void testMapLikeType() {
        JavaType keyType = new SimpleType("java.lang.String");
        JavaType valueType = new SimpleType("java.lang.Integer");
        JavaType mapType = new MapLikeType(Map.class, null, null, null, keyType, valueType, null, null, false);
        assertNotNull(mapType);
        assertEquals(Map.class, mapType.getRawClass());
        assertEquals(keyType, mapType.getKeyType());
        assertEquals(valueType, mapType.getContentType());
        assertFalse(mapType.isTrueMapType());
    }

    @Test
    public void testMapLikeTypeWithTypeVariables() {
        Class<?> rawType = new GenericType<Map<String, Integer>>() {}.getType();
        JavaType mapType = new MapLikeType(rawType, TypeBindings.emptyBindings(), null, null, null, null, null, null, false);
        assertNotNull(mapType);
        assertEquals(Map.class, mapType.getRawClass());
        JavaType keyType = mapType.getKeyType();
        JavaType valueType = mapType.getContentType();
        assertEquals("java.lang.String", keyType.getGenericName());
        assertEquals("java.lang.Integer", valueType.getGenericName());
        assertEquals("Ljava/util/Map;<Ljava/lang/String;Ljava/lang/Integer;>", mapType.toCanonical());
        assertFalse(mapType.isTrueMapType());
    }

    @Test
    public void testMapLikeTypeWithKeyType() {
        JavaType mapType = new MapLikeType(Map.class, null, null, null, null, new SimpleType("java.lang.Integer"), null, null, false);
        JavaType keyType = mapType.withKeyType(new SimpleType("java.lang.String"));
        assertNotNull(keyType);
        assertEquals("Ljava/lang/String;", keyType.getGenericName());
        assertEquals("Ljava/lang/Integer;", mapType.getContentType().getGenericName());
        assertEquals("Ljava/util/Map;<Ljava/lang/String;Ljava/lang/Integer;>", mapType.toCanonical());
        assertTrue(mapType.isTrueMapType());
    }

    @Test
    public void testMapLikeTypeWithContentType() {
        JavaType mapType = new MapLikeType(Map.class, null, null, null, null, new SimpleType("java.lang.Integer"), null, null, false);
        JavaType valueType = mapType.withContentType(new SimpleType("java.lang.Double"));
        assertNotNull(valueType);
        assertEquals("java.lang.Double", valueType.getGenericName());
        assertEquals("Ljava/lang/Integer;", mapType.getKeyType().getGenericName());
        assertEquals("Ljava/lang/Double;", valueType.getGenericName());
        assertEquals("Ljava/util/Map;<Ljava/lang/String;Ljava/lang/Double;>", mapType.toCanonical());
        assertTrue(mapType.isTrueMapType());
    }

    @Test
    public void testMapLikeTypeWithTypeHandler() {
        JavaType mapType = new MapLikeType(Map.class, null, null, null, null, new SimpleType("java.lang.Integer"), null, null, false);
        Object valueHandler = new Object() {};
        JavaType mapTypeWithHandler = mapType.withTypeHandler(valueHandler);
        assertNotNull(mapTypeWithHandler);
        assertEquals(mapType, mapTypeWithHandler);
        assertSame(valueHandler, mapTypeWithHandler.getTypeHandler());
    }

    @Test
    public void testMapLikeTypeWithContentTypeHandler() {
        JavaType mapType = new MapLikeType(Map.class, null, null, null, null,
                new SimpleType("java.lang.Integer"), null, null, false);
        Object contentTypeHandler = new Object() {};
        JavaType valueType = mapType.getContentType();
        JavaType mapTypeWithHandler = valueType.withTypeHandler(contentTypeHandler);
        assertNotNull(mapTypeWithHandler);
        assertEquals(valueType, mapTypeWithHandler);
        assertSame(contentTypeHandler, mapTypeWithHandler.getTypeHandler());
        assertEquals(mapType, mapTypeWithHandler.getRawType());
        assertEquals("Ljava/util/Map;<Ljava/lang/String;Ljava/lang/Integer;>", mapTypeWithHandler.toCanonical());
        assertTrue(mapTypeWithHandler.isTrueMapType());
    }

    @Test
    public void testMapLikeTypeWithKeyTypeHandler() {
        JavaType mapType = new MapLikeType(Map.class, null, null, null, null,
                new SimpleType("java.lang.Integer"), null, null, false);
        Object keyHandler = new Object() {};
        JavaType keyType = mapType.getKeyType();
        JavaType mapTypeWithHandler = keyType.withTypeHandler(keyHandler);
        assertNotNull(mapTypeWithHandler);
        assertEquals(keyType, mapTypeWithHandler);
        assertSame(keyHandler, mapTypeWithHandler.getTypeHandler());
    }

    @Test
    public void testMapLikeTypeWithKeyValueHandler() {
        JavaType mapType = new MapLikeType(Map.class, null, null, null, null,
                new SimpleType("java.lang.Integer"), null, null, false);
        Object keyValueHandler = new Object() {};
        JavaType keyType = mapType.getKeyType();
        JavaType mapTypeWithHandler = keyType.withValueHandler(keyValueHandler);
        assertNotNull(mapTypeWithHandler);
        assertEquals(keyType, mapTypeWithHandler);
        assertSame(keyValueHandler, mapTypeWithHandler.getValueHandler());
    }

    @Test
    public void testMapLikeTypeWithStaticTyping() {
        JavaType mapType = new MapLikeType(Map.class, null, null, null, null,
                new SimpleType("java.lang.Integer"), null, null, false);
        JavaType mapTypeWithStaticTyping = mapType.withStaticTyping();
        assertNotNull(mapTypeWithStaticTyping);
        assertTrue(mapTypeWithStaticTyping.isTrueMapType());
    }

    @Test
    public void testMapLikeTypeRefine() {
        JavaType baseType = new SimpleType("com.fasterxml.jackson.databind.type.MapLikeType");
        JavaType keyType = new SimpleType("java.lang.String");
        JavaType valueType = new SimpleType("java.lang.Integer");
        JavaType mapType = new MapLikeType(baseType, null, null, null, keyType, valueType, null, null, false);
        JavaType refinedMapType = mapType.refine(Map.class, null, null, new Class[]{Collection.class});
        assertNotNull(refinedMapType);
        assertEquals("Ljava/util/Map;<Ljava/lang/String;Ljava/lang/Integer;>", refinedMapType.toCanonical());
        assertTrue(refinedMapType.isTrueMapType());
    }

    @Test
    public void testMapLikeTypeConstruct() {
        @SuppressWarnings("unchecked")
        JavaType mapType = MapLikeType.construct(Map.class,
                new SimpleType("java.lang.String"), new SimpleType("java.lang.Integer"));
        assertNotNull(mapType);
        assertEquals("Ljava/util/Map;<Ljava/lang/String;Ljava/lang/Integer;>", mapType.toCanonical());
        assertTrue(mapType.isTrueMapType());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testMapLikeTypeConstructInvalidBaseType() {
        JavaType baseType = new SimpleType("java.lang.Object");
        MapLikeType.construct(List.class, new SimpleType("java.lang.String"), new SimpleType("java.lang.Integer"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testMapLikeTypeConstructMissingTypeVariables() {
        Class<?> rawType = new GenericType<Map<String, Integer>>() {}.getType();
        MapLikeType.construct(rawType, null, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testMapLikeTypeConstructMissingKeyType() {
        Class<?> rawType = new GenericType<Map<String, Integer>>() {}.getType();
        JavaType keyType = null;
        MapLikeType.construct(rawType, null, null, null, keyType, new SimpleType("java.lang.Integer"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testMapLikeTypeConstructMissingValueType() {
        Class<?> rawType = new GenericType<Map<String, Integer>>() {}.getType();
        JavaType keyType = new SimpleType("java.lang.String");
        JavaType valueType = null;
        MapLikeType.construct(rawType, null, null, null, keyType, valueType);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testMapLikeTypeUpgradeFromInvalidBaseType() {
        JavaType baseType = new SimpleType("java.util.List");
        JavaType keyType = new SimpleType("java.lang.String");
        JavaType valueType = new SimpleType("java.lang.Integer");
        try {
            MapLikeType.upgradeFrom(baseType, keyType, valueType);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testMapLikeTypeUpgradeFrom() {
        JavaType baseType = new SimpleType("java.util.Map");
        JavaType keyType = new SimpleType("java.lang.String");
        JavaType valueType = new SimpleType("java.lang.Integer");
        JavaType mapLikeType = MapLikeType.upgradeFrom(baseType, keyType, valueType);
        assertNotNull(mapLikeType);
        assertEquals("Ljava/util/Map;<Ljava/lang/String;Ljava/lang/Integer;>", mapLikeType.toCanonical());
        assertTrue(mapLikeType.isTrueMapType());
    }
}