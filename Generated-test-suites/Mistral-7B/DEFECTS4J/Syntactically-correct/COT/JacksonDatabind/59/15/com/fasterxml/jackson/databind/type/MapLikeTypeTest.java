package com.yourpackage; // replace with your package name

import static org.junit.Assert.*;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.MapLikeType;
import org.junit.Test;

/**
 * Test class for MapLikeType
 */
public class MapLikeTypeTest {

    @Test
    public void testMapLikeTypeConstructor() {
        Class<Map<Integer, String>> mapType = Map.class;
        TypeBindings bindings = TypeBindings.emptyBindings();
        JavaType keyType = JavaType.construct(Integer.class, bindings);
        JavaType valueType = JavaType.construct(String.class, bindings);
        MapLikeType mapLikeType = new MapLikeType(mapType, bindings, null, null,
                keyType, valueType);
        assertEquals(MapLikeType.class, mapLikeType.getClass());
        assertEquals(mapType, mapLikeType.getType());
        assertEquals(keyType, mapLikeType.getKeyType());
        assertEquals(valueType, mapLikeType.getContentType());
        assertNull(mapLikeType.getValueHandler());
        assertNull(mapLikeType.getTypeHandler());
    }

    @Test
    public void testUpgradeFromJavaType() {
        JavaType baseType = JavaType.construct(HashMap.class, null);
        JavaType keyType = JavaType.construct(Integer.class, null);
        JavaType valueType = JavaType.construct(String.class, null);
        MapLikeType mapLikeType = MapLikeType.upgradeFrom(baseType, keyType,
                valueType);
        assertEquals(MapLikeType.class, mapLikeType.getClass());
        assertEquals(baseType.getRawClass(), mapLikeType.getType());
        assertEquals(keyType, mapLikeType.getKeyType());
        assertEquals(valueType, mapLikeType.getContentType());
    }

    @Test
    public void testConstructDeprecated() {
        // Deprecated constructor is not public, so it can't be tested directly
        // Instead, test if an exception is thrown when trying to use it
        try {
            Class<?> rawType = Map.class;
            TypeBindings bindings = TypeBindings.emptyBindings();
            MapLikeType mapLikeType = MapLikeType.construct(rawType, null,
                    null);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNarrowWithInvalidSubclass() {
        JavaType baseType = JavaType.construct(HashMap.class, null);
        Class<?> subclass = ArrayList.class;
        MapLikeType mapLikeType = new MapLikeType(baseType, null, null, null,
                null, null);
        JavaType narrowType = mapLikeType.narrow(subclass);
    }

    @Test
    public void testWithKeyType() {
        Class<Map<Integer, String>> mapType = Map.class;
        TypeBindings bindings = TypeBindings.emptyBindings();
        JavaType keyType = JavaType.construct(Integer.class, bindings);
        JavaType valueType = JavaType.construct(String.class, bindings);
        MapLikeType mapLikeType = new MapLikeType(mapType, bindings, null, null,
                keyType, valueType);
        JavaType newKeyType = JavaType.construct(Long.class, bindings);
        MapLikeType newMapLikeType = mapLikeType.withKeyType(newKeyType);
        assertEquals(mapLikeType.getClass(), newMapLikeType.getClass());
        assertEquals(mapType, newMapLikeType.getType());
        assertEquals(newKeyType, newMapLikeType.getKeyType());
        assertEquals(valueType, newMapLikeType.getContentType());
    }

    @Test
    public void testWithContentType() {
        Class<Map<Integer, String>> mapType = Map.class;
        TypeBindings bindings = TypeBindings.emptyBindings();
        JavaType keyType = JavaType.construct(Integer.class, bindings);
        JavaType valueType = JavaType.construct(String.class, bindings);
        MapLikeType mapLikeType = new MapLikeType(mapType, bindings, null, null,
                keyType, valueType);
        JavaType newValueType = JavaType.construct(List.class, bindings);
        MapLikeType newMapLikeType = mapLikeType.withContentType(newValueType);
        assertEquals(mapLikeType.getClass(), newMapLikeType.getClass());
        assertEquals(mapType, newMapLikeType.getType());
        assertEquals(keyType, newMapLikeType.getKeyType());
        assertEquals(newValueType, newMapLikeType.getContentType());
    }

    @Test
    public void testWithTypeHandler() {
        Class<Map<Integer, String>> mapType = Map.class;
        TypeBindings bindings = TypeBindings.emptyBindings();
        JavaType keyType = JavaType.construct(Integer.class, bindings);
        JavaType valueType = JavaType.construct(String.class, bindings);
        MapLikeType mapLikeType = new MapLikeType(mapType, bindings, null, null,
                keyType, valueType);
        Object handler = new Object();
        MapLikeType newMapLikeType = mapLikeType.withTypeHandler(handler);
        assertEquals(mapLikeType.getClass(), newMapLikeType.getClass());
        assertEquals(mapType, newMapLikeType.getType());
        assertEquals(keyType, newMapLikeType.getKeyType());
        assertEquals(valueType, newMapLikeType.getContentType());
        assertEquals(handler, newMapLikeType.getTypeHandler());
    }

    @Test
    public void testWithContentTypeHandler() {
        Class<Map<Integer, String>> mapType = Map.class;
        TypeBindings bindings = TypeBindings.emptyBindings();
        JavaType keyType = JavaType.construct(Integer.class, bindings);
        JavaType valueType = JavaType.construct(String.class, bindings);
        MapLikeType mapLikeType = new MapLikeType(mapType, bindings, null, null,
                keyType, valueType);
        Object handler = new Object();
        JavaType newValueType = JavaType.construct(List.class, bindings);
        Object newHandler = new Object();
        MapLikeType newMapLikeType = mapLikeType.withContentTypeHandler(newHandler);
        assertEquals(mapLikeType.getClass(), newMapLikeType.getClass());
        assertEquals(mapType, newMapLikeType.getType());
        assertEquals(keyType, newMapLikeType.getKeyType());
        assertEquals(newValueType, newMapLikeType.getContentType());
        assertEquals(newHandler, newMapLikeType.getContentTypeHandler());
        assertEquals(handler, mapLikeType.getTypeHandler());
    }

    @Test
    public void testWithValueHandler() {
        Class<Map<Integer, String>> mapType = Map.class;
        TypeBindings bindings = TypeBindings.emptyBindings();
        JavaType keyType = JavaType.construct(Integer.class, bindings);
        JavaType valueType = JavaType.construct(String.class, bindings);
        MapLikeType mapLikeType = new MapLikeType(mapType, bindings, null, null,
                keyType, valueType);
        Object handler = new Object();
        MapLikeType newMapLikeType = mapLikeType.withValueHandler(handler);
        assertEquals(mapLikeType.getClass(), newMapLikeType.getClass());
        assertEquals(mapType, newMapLikeType.getType());
        assertEquals(keyType, newMapLikeType.getKeyType());
        assertEquals(valueType, newMapLikeType.getContentType());
        assertEquals(handler, newMapLikeType.getValueHandler());
    }

    @Test
    public void testWithContentValueHandler() {
        Class<Map<Integer, String>> mapType = Map.class;
        TypeBindings bindings = TypeBindings.emptyBindings();
        JavaType keyType = JavaType.construct(Integer.class, bindings);
        JavaType valueType = JavaType.construct(String.class, bindings);
        MapLikeType mapLikeType = new MapLikeType(mapType, bindings, null, null,
                keyType, valueType);
        Object handler = new Object();
        JavaType newValueType = JavaType.construct(List.class, bindings);
        MapLikeType newMapLikeType = mapLikeType.withContentValueHandler(handler);
        assertEquals(mapLikeType.getClass(), newMapLikeType.getClass());
        assertEquals(mapType, newMapLikeType.getType());
        assertEquals(keyType, newMapLikeType.getKeyType());
        assertEquals(newValueType, newMapLikeType.getContentType());
        assertEquals(handler, newMapLikeType.getContentValueHandler());
        assertEquals(handler, mapLikeType.getValueHandler());
    }

    @Test
    public void testWithStaticTyping() {
        Class<Map<Integer, String>> mapType = Map.class;
        TypeBindings bindings = TypeBindings.emptyBindings();
        JavaType keyType = JavaType.construct(Integer.class, bindings);
        JavaType valueType = JavaType.construct(String.class, bindings);
        MapLikeType mapLikeType = new MapLikeType(mapType, bindings, null, null,
                keyType, valueType);
        MapLikeType newMapLikeType = mapLikeType.withStaticTyping();
        assertEquals(mapLikeType.getClass(), newMapLikeType.getClass());
        assertEquals(mapType, newMapLikeType.getType());
        assertEquals(keyType, newMapLikeType.getKeyType());
        assertEquals(valueType, newMapLikeType.getContentType());
        assertTrue(newMapLikeType.isStatic());
        assertFalse(mapLikeType.isStatic());
    }

    @Test
    public void testRefine() {
        Class<Map<Integer, String>> mapType = Map.class;
        TypeBindings bindings = TypeBindings.emptyBindings();
        JavaType keyType = JavaType.construct(Integer.class, bindings);
        JavaType valueType = JavaType.construct(String.class, bindings);
        MapLikeType mapLikeType = new MapLikeType(mapType, bindings, null, null,
                keyType, valueType);
        Class<?> rawType = HashMap.class;
        JavaType newMapType = mapLikeType.refine(rawType, null, null, null);
        assertEquals(MapLikeType.class, newMapType.getClass());
        assertEquals(mapType, newMapType.getType());
        assertEquals(keyType, newMapType.getKeyType());
        assertEquals(valueType, newMapType.getContentType());
    }

    @Test
    public void testBuildCanonicalName() {
        Class<Map<Integer, String>> mapType = Map.class;
        JavaType keyType = JavaType.construct(Integer.class, null);
        JavaType valueType = JavaType.construct(String.class, null);
        MapLikeType mapLikeType = new MapLikeType(mapType, null, null, null,
                keyType, valueType);
        String canonicalName = mapLikeType.buildCanonicalName();
        String expected = String.format("[map-like type; class %s, %s -> %s]",
                mapType.getName(), keyType.toCanonical(), valueType.toCanonical());
        assertEquals(expected, canonicalName);
    }

    @Test
    public void testIsContainerType() {
        Class<Map<Integer, String>> mapType = Map.class;
        JavaType keyType = JavaType.construct(Integer.class, null);
        JavaType valueType = JavaType.construct(String.class, null);
        MapLikeType mapLikeType = new MapLikeType(mapType, null, null, null,
                keyType, valueType);
        assertTrue(mapLikeType.isContainerType());
    }

    @Test
    public void testIsMapLikeType() {
        Class<Map<Integer, String>> mapType = Map.class;
        JavaType keyType = JavaType.construct(Integer.class, null);
        JavaType valueType = JavaType.construct(String.class, null);
        MapLikeType mapLikeType = new MapLikeType(mapType, null, null, null,
                keyType, valueType);
        assertTrue(mapLikeType.isMapLikeType());
    }

    @Test
    public void testEquals() {
        Class<Map<Integer, String>> mapType = Map.class;
        JavaType keyType = JavaType.construct(Integer.class, null);
        JavaType valueType = JavaType.construct(String.class, null);
        MapLikeType mapLikeType1 = new MapLikeType(mapType, null, null, null,
                keyType, valueType);
        MapLikeType mapLikeType2 = new MapLikeType(mapType, null, null, null,
                keyType, valueType);
        Object other = new Object();
        MapLikeType mapLikeType3 = new MapLikeType(mapType, null, null, null,
                keyType, valueType);
        MapLikeType mapLikeType4 = new MapLikeType(HashMap.class, null, null,
                null, keyType, valueType);
        MapLikeType mapLikeType5 = new MapLikeType(mapType, null, null, null,
                null, valueType);
        assertEquals(mapLikeType1, mapLikeType2);
        assertNotEquals(mapLikeType1, other);
        assertNotEquals(mapLikeType1, mapLikeType3);
        assertNotEquals(mapLikeType1, mapLikeType4);
        assertNotEquals(mapLikeType1, mapLikeType5);
    }

    @Test
    public void testToString() {
        Class<Map<Integer, String>> mapType = Map.class;
        JavaType keyType = JavaType.construct(Integer.class, null);
        JavaType valueType = JavaType.construct(String.class, null);
        MapLikeType mapLikeType = new MapLikeType(mapType, null, null, null,
                keyType, valueType);
        String expected = String.format("[map-like type; class %s, %s -> %s]",
                mapType.getName(), keyType, valueType);
        assertEquals(expected, mapLikeType.toString());
    }

    @Test
    public void testIsTrueMapType() {
        Class<Map<Integer, String>> mapType = Map.class;
        JavaType keyType = JavaType.construct(Integer.class, null);
        JavaType valueType = JavaType.construct(String.class, null);
        MapLikeType mapLikeType = new MapLikeType(mapType, null, null, null,
                keyType, valueType);
        assertTrue(mapLikeType.isTrueMapType());
    }
}