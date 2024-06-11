package com.fasterxml.jackson.databind.type.test;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.TypeBindings;
import com.fasterxml.jackson.databind.type.MapType;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * Test cases for {@link MapType} class.
 */
public class MapTypeTest {

    @Test
    public void testConstructWithTypeBindingsAndTypes() {
        Class<Map<String, Integer>> rawType = Map.class;
        TypeBindings bindings = TypeBindings.defaultBindings();
        JavaType keyType = TypeFactory.defaultInstance().constructType(String.class);
        JavaType valueType = TypeFactory.defaultInstance().constructType(Integer.class);

        MapType mapType = MapType.construct(rawType, bindings, null, null, keyType, valueType);

        assertNotNull(mapType);
        assertEquals(Map.class, mapType.getRawClass());
        assertEquals(keyType, mapType.getKeyType());
        assertEquals(valueType, mapType.getValueType());
    }

    @Test
    public void testConstructWithClassAndTypes() {
        Class<Map<String, Integer>> rawType = Map.class;
        JavaType keyType = TypeFactory.defaultInstance().constructType(String.class);
        JavaType valueType = TypeFactory.defaultInstance().constructType(Integer.class);

        MapType mapType = MapType.construct(rawType, null, null, null, keyType, valueType);

        assertNotNull(mapType);
        assertEquals(Map.class, mapType.getRawClass());
        assertEquals(keyType, mapType.getKeyType());
        assertEquals(valueType, mapType.getValueType());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructWithWrongArguments() {
        Class<?> rawType = List.class; // wrong class
        JavaType keyType = TypeFactory.defaultInstance().constructType(String.class);
        JavaType valueType = TypeFactory.defaultInstance().constructType(Integer.class);

        MapType mapType = MapType.construct(rawType, null, null, null, keyType, valueType);

        // This test should fail due to the wrong class type
    }

    @Test
    public void testWithTypeHandler() {
        Class<Map<String, Integer>> rawType = Map.class;
        JavaType keyType = TypeFactory.defaultInstance().constructType(String.class);
        JavaType valueType = TypeFactory.defaultInstance().constructType(Integer.class);

        MapType mapType = new MapType(rawType, null, null, null, keyType, valueType);
        Object handler = new Object();

        MapType newMapType = mapType.withTypeHandler(handler);

        assertNotNull(newMapType);
        assertEquals(mapType.getRawClass(), newMapType.getRawClass());
        assertEquals(mapType.getKeyType(), newMapType.getKeyType());
        assertEquals(mapType.getValueType(), newMapType.getValueType());
        assertEquals(handler, newMapType.getTypeHandler());
    }

    @Test
    public void testWithContentTypeHandler() {
        Class<Map<String, Integer>> rawType = Map.class;
        JavaType keyType = TypeFactory.defaultInstance().constructType(String.class);
        JavaType valueType = TypeFactory.defaultInstance().constructType(Integer.class);

        MapType mapType = new MapType(rawType, null, null, null, keyType, valueType);
        Object handler = new Object();

        JavaType newValueType = TypeFactory.defaultInstance().constructType(String.class);
        newValueType = newValueType.withTypeHandler(handler);

        MapType newMapType = mapType.withContentTypeHandler(newValueType);

        assertNotNull(newMapType);
        assertEquals(mapType.getRawClass(), newMapType.getRawClass());
        assertEquals(mapType.getKeyType(), newMapType.getKeyType());
        assertEquals(newValueType, newMapType.getValueType());
        assertEquals(handler, newMapType.getTypeHandler());
    }

    @Test
    public void testWithValueHandler() {
        Class<Map<String, Integer>> rawType = Map.class;
        JavaType keyType = TypeFactory.defaultInstance().constructType(String.class);
        JavaType valueType = TypeFactory.defaultInstance().constructType(Integer.class);

        MapType mapType = new MapType(rawType, null, null, null, keyType, valueType);
        Object handler = new Object();

        MapType newMapType = mapType.withValueHandler(handler);

        assertNotNull(newMapType);
        assertEquals(mapType.getRawClass(), newMapType.getRawClass());
        assertEquals(mapType.getKeyType(), newMapType.getKeyType());
        assertEquals(valueType, newMapType.getValueType());
        assertEquals(handler, newMapType.getValueHandler());
    }

    @Test
    public void testWithContentValueHandler() {
        Class<Map<String, Integer>> rawType = Map.class;
        JavaType keyType = TypeFactory.defaultInstance().constructType(String.class);
        JavaType valueType = TypeFactory.defaultInstance().constructType(Integer.class);

        MapType mapType = new MapType(rawType, null, null, null, keyType, valueType);
        Object handler = new Object();

        JavaType newKeyType = TypeFactory.defaultInstance().constructType(String.class);
        newKeyType = newKeyType.withValueHandler(handler);

        MapType newMapType = mapType.withContentValueHandler(newKeyType);

        assertNotNull(newMapType);
        assertEquals(mapType.getRawClass(), newMapType.getRawClass());
        assertEquals(newKeyType, newMapType.getKeyType());
        assertEquals(valueType, newMapType.getValueType());
        assertEquals(handler, newMapType.getValueHandler());
    }

    @Test
    public void testWithStaticTyping() {
        Class<Map<String, Integer>> rawType = Map.class;
        JavaType keyType = TypeFactory.defaultInstance().constructType(String.class);
        JavaType valueType = TypeFactory.defaultInstance().constructType(Integer.class);

        MapType mapType = new MapType(rawType, null, null, null, keyType, valueType);
        MapType newMapType = mapType.withStaticTyping();

        assertNotNull(newMapType);
        assertEquals(mapType.getRawClass(), newMapType.getRawClass());
        assertEquals(keyType.withStaticTyping(), newMapType.getKeyType());
        assertEquals(valueType.withStaticTyping(), newMapType.getValueType());
        assertTrue(newMapType.isStatic());
    }

    @Test
    public void testWithContentType() {
        Class<Map<String, Integer>> rawType = Map.class;
        JavaType keyType = TypeFactory.defaultInstance().constructType(String.class);
        JavaType valueType = TypeFactory.defaultInstance().constructType(Integer.class);

        MapType mapType = new MapType(rawType, null, null, null, keyType, valueType);
        JavaType newValueType = TypeFactory.defaultInstance().constructType(String.class);

        MapType newMapType = mapType.withContentType(newValueType);

        assertNotNull(newMapType);
        assertEquals(mapType.getRawClass(), newMapType.getRawClass());
        assertEquals(mapType.getKeyType(), newMapType.getKeyType());
        assertEquals(newValueType, newMapType.getValueType());
    }

    @Test
    public void testWithKeyType() {
        Class<Map<String, Integer>> rawType = Map.class;
        JavaType keyType = TypeFactory.defaultInstance().constructType(String.class);
        JavaType valueType = TypeFactory.defaultInstance().constructType(Integer.class);

        MapType mapType = new MapType(rawType, null, null, null, keyType, valueType);
        JavaType newKeyType = TypeFactory.defaultInstance().constructType(String.class);

        MapType newMapType = mapType.withKeyType(newKeyType);

        assertNotNull(newMapType);
        assertEquals(mapType.getRawClass(), newMapType.getRawClass());
        assertEquals(newKeyType, newMapType.getKeyType());
        assertEquals(valueType, newMapType.getValueType());
    }

    @Test
    public void testRefine() {
        Class<Map<String, Integer>> rawType = Map.class;
        JavaType keyType = TypeFactory.defaultInstance().constructType(String.class);
        JavaType valueType = TypeFactory.defaultInstance().constructType(Integer.class);

        MapType mapType = new MapType(rawType, null, null, null, keyType, valueType);
        TypeBindings newBindings = TypeBindings.defaultBindings();
        JavaType newSuperClass = TypeFactory.defaultInstance().constructType(HashMap.class);
        JavaType[] newSuperInterfaces = new JavaType[0];

        MapType newMapType = mapType.refine(Map.class, newBindings, newSuperClass, newSuperInterfaces);

        assertNotNull(newMapType);
        assertEquals(Map.class, newMapType.getRawClass());
        assertEquals(newKeyType, newMapType.getKeyType());
        assertEquals(newValueType, newMapType.getValueType());
    }

    @Test
    public void testToString() {
        Class<Map<String, Integer>> rawType = Map.class;
        JavaType keyType = TypeFactory.defaultInstance().constructType(String.class);
        JavaType valueType = TypeFactory.defaultInstance().constructType(Integer.class);

        MapType mapType = new MapType(rawType, null, null, null, keyType, valueType);

        String expected = "[map type; class " + Map.class.getName() + ", " + keyType + " -> " + valueType + "]";
        assertEquals(expected, mapType.toString());
    }
}