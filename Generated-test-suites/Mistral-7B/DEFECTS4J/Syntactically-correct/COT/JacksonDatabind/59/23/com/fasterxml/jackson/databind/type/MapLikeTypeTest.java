package com.example.mapliketype;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import java.lang.reflect.TypeVariable;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.databind.JavaType;

public class MapLikeTypeTest {

    private JavaType keyType;
    private JavaType valueType;
    private JavaType mapLikeType;

    @Before
    public void setup() {
        keyType = JavaType.construct(Map.class, String.class, Integer.class);
        valueType = JavaType.construct(Map.class, String.class, String.class);
        mapLikeType = MapLikeType.construct(Map.class, keyType, valueType);
    }

    @Test
    public void testMapLikeTypeCreation() {
        assertEquals("java.util.Map<java.lang.String, java.lang.Integer>",
                mapLikeType.getGenericSignature().toString());
        assertTrue(mapLikeType.isContainerType());
        assertTrue(mapLikeType.isMapLikeType());
    }

    @Test
    public void testUpgradeFromBasicType() {
        JavaType javaListType = JavaType.construct(ArrayList.class);
        JavaType upgradedMapLikeType = MapLikeType.upgradeFrom(javaListType, keyType, valueType);
        assertEquals("java.util.Map<java.lang.String, java.lang.Integer>",
                upgradedMapLikeType.getGenericSignature().toString());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCannotUpgradeFromInvalidType() {
        JavaType invalidType = JavaType.construct(String.class);
        MapLikeType.upgradeFrom(invalidType, keyType, valueType);
    }

    @Test
    public void testWithKeyType() {
        JavaType newKeyType = JavaType.construct(HashMap.class, Integer.class, String.class);
        MapLikeType newMapLikeType = mapLikeType.withKeyType(newKeyType);
        assertEquals("java.util.Map<java.lang.Integer, java.lang.String>",
                newMapLikeType.getGenericSignature().toString());
        assertFalse(newMapLikeType.equals(mapLikeType));
    }

    @Test
    public void testWithContentType() {
        JavaType newValueType = JavaType.construct(HashMap.class, String.class, Float.class);
        MapLikeType newMapLikeType = mapLikeType.withContentType(newValueType);
        assertEquals("java.util.Map<java.lang.String, java.lang.Float>",
                newMapLikeType.getGenericSignature().toString());
        assertFalse(newMapLikeType.equals(mapLikeType));
    }

    @Test
    public void testWithTypeHandler() {
        Map<String, Integer> map = new HashMap<>();
        MapLikeType newMapLikeType = mapLikeType.withValueHandler(map);
        assertSame(map, newMapLikeType.getValueHandler());
    }

    @Test
    public void testWithContentTypeHandler() {
        Map<String, Map<Integer, String>> nestedMap = new HashMap<>();
        MapLikeType newMapLikeType = mapLikeType.withContentTypeHandler(nestedMap);
        assertSame(nestedMap, newMapLikeType.getContentTypeHandler());
    }

    @Test
    public void testWithKeyTypeHandler() {
        Map<Integer, Map<String, String>> nestedMap = new HashMap<>();
        MapLikeType newMapLikeType = mapLikeType.withKeyTypeHandler(nestedMap);
        assertSame(nestedMap, newMapLikeType.getKeyTypeHandler());
    }

    @Test
    public void testWithValueHandler() {
        Map<String, Integer> map = new HashMap<>();
        Object handler = new Object();
        MapLikeType newMapLikeType = mapLikeType.withValueHandler(handler);
        assertSame(handler, newMapLikeType.getValueHandler());
    }

    @Test
    public void testWithContentValueHandler() {
        Map<String, Map<Integer, String>> nestedMap = new HashMap<>();
        Object handler = new Object();
        MapLikeType newMapLikeType = mapLikeType.withContentValueHandler(handler);
        assertSame(handler, newMapLikeType.getContentValueHandler());
    }

    @Test
    public void testWithStaticTyping() {
        MapLikeType staticTypingMapLikeType = mapLikeType.withStaticTyping();
        assertTrue(staticTypingMapLikeType.isStaticTyping());
    }

    @Test
    public void testRefine() {
        JavaType refinedMapLikeType = mapLikeType.refine(HashMap.class, null, null, null);
        assertEquals("java.util.Map<java.lang.String, java.lang.Integer>",
                refinedMapLikeType.getGenericSignature().toString());
        assertFalse(refinedMapLikeType.equals(mapLikeType));
    }

    @Test
    public void testIsTrueMapType() {
        assertTrue(mapLikeType.isTrueMapType());
    }

    @Test
    public void testEquals() {
        JavaType anotherMapLikeType = JavaType.construct(HashMap.class, String.class, Integer.class);
        assertTrue(mapLikeType.equals(mapLikeType));
        assertTrue(mapLikeType.equals(anotherMapLikeType));
    }

    @Test
    public void testHashCode() {
        JavaType anotherMapLikeType = JavaType.construct(HashMap.class, String.class, Integer.class);
        assertEquals(mapLikeType.hashCode(), anotherMapLikeType.hashCode());
    }

    @Test
    public void testToString() {
        assertEquals("[map-like type; class java.util.Map, java.lang.String -> java.lang.Integer]",
                mapLikeType.toString());
    }

    @Test(expected = IllegalStateException.class)
    public void testConstructWithInvalidSuperClass() {
        JavaType invalidSuperClass = JavaType.construct(String.class);
        MapLikeType invalidMapLikeType = new MapLikeType(null, null, invalidSuperClass, null, keyType, valueType);
    }

    @Test(expected = NullPointerException.class)
    public void testConstructWithNullBindings() {
        MapLikeType nullBindingsMapLikeType = new MapLikeType(null, null, null, null, keyType, valueType);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructWithNullKeyType() {
        MapLikeType nullKeyTypeMapLikeType = new MapLikeType(null, null, null, null, null, valueType);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructWithNullValueType() {
        MapLikeType nullValueTypeMapLikeType = new MapLikeType(null, null, null, null, keyType, null);
    }

    @Test(expected = NullPointerException.class)
    public void testConstructWithNullValueHandler() {
        MapLikeType nullValueHandlerMapLikeType = new MapLikeType(null, null, null, null, keyType, valueType, null);
    }

    @Test(expected = NullPointerException.class)
    public void testConstructWithNullTypeHandler() {
        MapLikeType nullTypeHandlerMapLikeType = new MapLikeType(null, null, null, null, keyType, valueType, null, null);
    }
}