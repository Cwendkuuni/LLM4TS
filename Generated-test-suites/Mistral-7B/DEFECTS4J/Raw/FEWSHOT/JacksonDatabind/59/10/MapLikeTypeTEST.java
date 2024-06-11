package com.fasterxml.jackson.databind.type;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

import java.lang.reflect.TypeVariable;
import java.util.Arrays;

import org.junit.Test;

public class MapLikeTypeTest {

    @Test
    public void testConstruct() {
        JavaType keyType = new SimpleType("java.lang.String").withoutErasing();
        JavaType valueType = new SimpleType("java.lang.Integer").withoutErasing();
        MapLikeType mapType = MapLikeType.construct(Map.class, keyType, valueType);
        assertEquals(Map.class, mapType.getRawType());
        assertEquals(keyType, mapType.getKeyType());
        assertEquals(valueType, mapType.getValueType());
        assertEquals(false, mapType.isStaticTyping());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructInvalidBaseType() {
        JavaType keyType = new SimpleType("java.lang.String").withoutErasing();
        JavaType valueType = new SimpleType("java.lang.Integer").withoutErasing();
        MapLikeType.construct(List.class, keyType, valueType);
    }

    @Test
    public void testUpgradeFrom() {
        JavaType baseType = new SimpleType("java.util.HashMap").withoutErasing();
        JavaType keyType = new SimpleType("java.lang.String").withoutErasing();
        JavaType valueType = new SimpleType("java.lang.Integer").withoutErasing();
        MapLikeType mapLikeType = MapLikeType.upgradeFrom(baseType, keyType, valueType);
        assertEquals(Map.class, mapLikeType.getRawType());
        assertEquals(keyType, mapLikeType.getKeyType());
        assertEquals(valueType, mapLikeType.getValueType());
        assertEquals(false, mapLikeType.isStaticTyping());
    }

    @Test
    public void testIsMapLikeType() {
        JavaType keyType = new SimpleType("java.lang.String").withoutErasing();
        JavaType valueType = new SimpleType("java.lang.Integer").withoutErasing();
        MapLikeType mapType = new MapLikeType(Map.class, null, null, null, keyType, valueType, null, null, false);
        assertTrue(mapType.isMapLikeType());
    }

    @Test
    public void testIsContainerType() {
        JavaType keyType = new SimpleType("java.lang.String").withoutErasing();
        JavaType valueType = new SimpleType("java.lang.Integer").withoutErasing();
        MapLikeType mapType = new MapLikeType(Map.class, null, null, null, keyType, valueType, null, null, false);
        assertTrue(mapType.isContainerType());
    }

    @Test
    public void testGetKeyType() {
        JavaType keyType = new SimpleType("java.lang.String").withoutErasing();
        JavaType valueType = new SimpleType("java.lang.Integer").withoutErasing();
        MapLikeType mapType = new MapLikeType(Map.class, null, null, null, keyType, valueType, null, null, false);
        assertEquals(keyType, mapType.getKeyType());
    }

    @Test
    public void testGetValueType() {
        JavaType keyType = new SimpleType("java.lang.String").withoutErasing();
        JavaType valueType = new SimpleType("java.lang.Integer").withoutErasing();
        MapLikeType mapType = new MapLikeType(Map.class, null, null, null, keyType, valueType, null, null, false);
        assertEquals(valueType, mapType.getValueType());
    }

    @Test
    public void testGetContentType() {
        JavaType keyType = new SimpleType("java.lang.String").withoutErasing();
        JavaType valueType = new SimpleType("java.lang.Integer").withoutErasing();
        MapLikeType mapType = new MapLikeType(Map.class, null, null, null, keyType, valueType, null, null, false);
        assertEquals(valueType, mapType.getContentType());
    }

    @Test
    public void testGetContentValueHandler() {
        JavaType keyType = new SimpleType("java.lang.String").withoutErasing();
        JavaType valueType = new SimpleType("java.lang.Integer").withoutErasing();
        MapLikeType mapType = new MapLikeType(Map.class, null, null, null, keyType, valueType, null, null, false);
        assertNull(mapType.getContentValueHandler());
    }

    @Test
    public void testGetContentTypeHandler() {
        JavaType keyType = new SimpleType("java.lang.String").withoutErasing();
        JavaType valueType = new SimpleType("java.lang.Integer").withoutErasing();
        MapLikeType mapType = new MapLikeType(Map.class, null, null, null, keyType, valueType, null, null, false);
        assertNull(mapType.getContentTypeHandler());
    }

    @Test
    public void testHasHandlers() {
        JavaType keyType = new SimpleType("java.lang.String").withoutErasing();
        JavaType valueType = new SimpleType("java.lang.Integer").withoutErasing();
        Object handler = new Object();
        MapLikeType mapType = new MapLikeType(Map.class, null, null, null, keyType, valueType, null, null, false);
        assertFalse(mapType.hasHandlers());

        MapLikeType mapTypeWithHandler = new MapLikeType(Map.class, null, null, null, keyType, valueType, handler, null, false);
        assertTrue(mapTypeWithHandler.hasHandlers());
    }

    @Test
    public void testBuildCanonicalName() {
        JavaType keyType = new SimpleType("java.lang.String").withoutErasing();
        JavaType valueType = new SimpleType("java.lang.Integer").withoutErasing();
        MapLikeType mapType = new MapLikeType(Map.class, null, null, null, keyType, valueType, null, null, false);
        assertEquals("[map-like type; class java.util.Map, Ljava/lang/String;->Ljava/lang/Integer;]", mapType.buildCanonicalName());
    }

    @Test
    public void testIsTrueMapType() {
        JavaType keyType = new SimpleType("java.lang.String").withoutErasing();
        JavaType valueType = new SimpleType("java.lang.Integer").withoutErasing();
        MapLikeType mapType = new MapLikeType(Map.class, null, null, null, keyType, valueType, null, null, false);
        assertTrue(mapType.isTrueMapType());

        JavaType listType = new SimpleType("java.util.ArrayList").withoutErasing();
        MapLikeType listLikeType = new MapLikeType(List.class, null, null, null, keyType, valueType, null, null, false);
        assertFalse(listLikeType.isTrueMapType());
    }

    @Test
    public void testWithKeyTypeHandler() {
        JavaType keyType = new SimpleType("java.lang.String").withoutErasing();
        JavaType valueType = new SimpleType("java.lang.Integer").withoutErasing();
        Object handler = new Object();
        MapLikeType mapType = new MapLikeType(Map.class, null, null, null, keyType, valueType, null, null, false);
        MapLikeType mapTypeWithHandler = mapType.withKeyTypeHandler(handler);
        assertSame(mapType, mapTypeWithHandler);
        assertEquals(handler, mapTypeWithHandler.getKeyTypeHandler());
    }

    @Test
    public void testWithKeyValueHandler() {
        JavaType keyType = new SimpleType("java.lang.String").withoutErasing();
        JavaType valueType = new SimpleType("java.lang.Integer").withoutErasing();
        Object handler = new Object();
        MapLikeType mapType = new MapLikeType(Map.class, null, null, null, keyType, valueType, null, null, false);
        MapLikeType mapTypeWithHandler = mapType.withKeyValueHandler(handler);
        assertSame(mapType, mapTypeWithHandler);
        assertEquals(handler, mapTypeWithHandler.getKeyValueHandler());
    }

    @Test
    public void testWithKeyType() {
        JavaType oldKeyType = new SimpleType("java.lang.String").withoutErasing();
        JavaType newKeyType = new SimpleType("java.lang.Integer").withoutErasing();
        JavaType valueType = new SimpleType("java.lang.Integer").withoutErasing();
        MapLikeType mapType = new MapLikeType(Map.class, null, null, null, oldKeyType, valueType, null, null, false);
        MapLikeType mapTypeWithNewKeyType = mapType.withKeyType(newKeyType);
        assertSame(mapType, mapTypeWithNewKeyType);
        assertEquals(newKeyType, mapTypeWithNewKeyType.getKeyType());
    }

    @Test
    public void testWithValueType() {
        JavaType keyType = new SimpleType("java.lang.String").withoutErasing();
        JavaType oldValueType = new SimpleType("java.lang.Integer").withoutErasing();
        JavaType newValueType = new SimpleType("java.lang.Long").withoutErasing();
        MapLikeType mapType = new MapLikeType(Map.class, null, null, null, keyType, oldValueType, null, null, false);
        MapLikeType mapTypeWithNewValueType = mapType.withValueType(newValueType);
        assertSame(mapType, mapTypeWithNewValueType);
        assertEquals(newValueType, mapTypeWithNewValueType.getValueType());
    }

    @Test
    public void testWithTypeHandler() {
        JavaType keyType = new SimpleType("java.lang.String").withoutErasing();
        JavaType valueType = new SimpleType("java.lang.Integer").withoutErasing();
        Object handler = new Object();
        MapLikeType mapType = new MapLikeType(Map.class, null, null, null, keyType, valueType, null, null, false);
        MapLikeType mapTypeWithHandler = mapType.withTypeHandler(handler);
        assertSame(mapType, mapTypeWithHandler);
        assertEquals(handler, mapTypeWithHandler.getTypeHandler());
    }

    @Test
    public void testWithContentTypeHandler() {
        JavaType keyType = new SimpleType("java.lang.String").withoutErasing();
        JavaType valueType = new SimpleType("java.lang.Integer").withoutErasing();
        Object handler = new Object();
        MapLikeType mapType = new MapLikeType(Map.class, null, null, null, keyType, valueType, null, null, false);
        MapLikeType mapTypeWithContentHandler = mapType.withContentTypeHandler(handler);
        assertSame(mapType, mapTypeWithContentHandler);
        assertEquals(handler, mapTypeWithContentHandler.getContentTypeHandler());
    }

    @Test
    public void testWithValueHandler() {
        JavaType keyType = new SimpleType("java.lang.String").withoutErasing();
        JavaType valueType = new SimpleType("java.lang.Integer").withoutErasing();
        Object handler = new Object();
        MapLikeType mapType = new MapLikeType(Map.class, null, null, null, keyType, valueType, null, null, false);
        MapLikeType mapTypeWithHandler = mapType.withValueHandler(handler);
        assertSame(mapType, mapTypeWithHandler);
        assertEquals(handler, mapTypeWithHandler.getValueHandler());
    }

    @Test
    public void testWithContentValueHandler() {
        JavaType keyType = new SimpleType("java.lang.String").withoutErasing();
        JavaType valueType = new SimpleType("java.lang.Integer").withoutErasing();
        Object handler = new Object();
        MapLikeType mapType = new MapLikeType(Map.class, null, null, null, keyType, valueType, null, null, false);
        MapLikeType mapTypeWithContentHandler = mapType.withContentValueHandler(handler);
        assertSame(mapType, mapTypeWithContentHandler);
        assertEquals(handler, mapTypeWithContentHandler.getContentValueHandler());
    }

    @Test
    public void testWithStaticTyping() {
        JavaType keyType = new SimpleType("java.lang.String").withoutErasing();
        JavaType valueType = new SimpleType("java.lang.Integer").withoutErasing();
        MapLikeType mapType = new MapLikeType(Map.class, null, null, null, keyType, valueType, null, null, false);
        MapLikeType mapTypeWithStaticTyping = mapType.withStaticTyping();
        assertSame(mapType, mapTypeWithStaticTyping);
        assertTrue(mapTypeWithStaticTyping.isStaticTyping());
    }

    @Test
    public void testRefine() {
        JavaType rawType = new SimpleType("java.util.HashMap").withoutErasing();
        JavaType keyType = new SimpleType("java.lang.String").withoutErasing();
        JavaType valueType = new SimpleType("java.lang.Integer").withoutErasing();
        TypeBindings bindings = TypeBindings.create(rawType, keyType, valueType);
        MapLikeType baseType = new MapLikeType(rawType, bindings, _bogusSuperClass(rawType), null, keyType, valueType, null, null, false);
        JavaType refinedType = baseType.refine(Map.class, null, null, null);
        assertEquals(Map.class, refinedType.getRawType());
        assertEquals(keyType, refinedType.getKeyType());
        assertEquals(valueType, refinedType.getValueType());
        assertFalse(refinedType.isStaticTyping());
    }

    private static Class<?> _bogusSuperClass(Class<?> clazz) {
        return Object.class;
    }
}