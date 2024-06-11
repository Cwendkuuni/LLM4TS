package com.fasterxml.jackson.databind.type.test;

import static org.junit.Assert.*;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.TypeBindings;
import com.fasterxml.jackson.databind.type.MapType;

import org.junit.Test;

public class MapTypeTest {

    // Basic test cases for construct method
    @Test
    public void testConstructWithTypeBindingsAndTypes() {
        JavaType keyType = JavaType.construct(int.class, int.class);
        JavaType valueType = JavaType.construct(String.class, String.class);
        TypeBindings bindings = TypeBindings.defaultBindings();
        MapType mapType = MapType.construct(Map.class, bindings, null, null, keyType, valueType);
        assertEquals(Map.class, mapType.getRawClass());
        assertEquals(keyType, mapType.getKeyType());
        assertEquals(valueType, mapType.getValueType());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructWithInvalidClass() {
        JavaType keyType = JavaType.construct(int.class, int.class);
        JavaType valueType = JavaType.construct(String.class, String.class);
        MapType mapType = MapType.construct(String.class, null, null, null, keyType, valueType);
    }

    // Basic test cases for construct deprecated method
    @Test
    public void testConstructWithKeyTypeAndValueType() {
        JavaType keyType = JavaType.construct(int.class, int.class);
        JavaType valueType = JavaType.construct(String.class, String.class);
        MapType mapType = MapType.construct(int[].class, null, null, null, keyType, valueType);
        assertEquals(int[].class, mapType.getRawClass());
        assertEquals(keyType, mapType.getKeyType());
        assertEquals(valueType, mapType.getValueType());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructWithInvalidKeyTypeOrValueType() {
        JavaType keyType = JavaType.construct(int.class, int.class);
        JavaType valueType = JavaType.construct(String.class, int.class);
        MapType mapType = MapType.construct(int[].class, null, null, null, keyType, valueType);
    }

    // Test for _narrow method
    @Test
    public void testNarrowMethod() {
        JavaType mapTypeAsList = JavaType.construct(ArrayList.class, int.class);
        JavaType mapTypeAsMap = JavaType.construct(Map.class, int.class, int.class);
        MapType mapType = (MapType) mapTypeAsMap.getInstance(MapType.class);
        JavaType narrowedType = mapType._narrow(HashMap.class);
        assertEquals(HashMap.class, narrowedType.getRawClass());
    }

    @Test(expected = ClassCastException.class)
    public void testNarrowMethodWithInvalidClass() {
        JavaType mapTypeAsList = JavaType.construct(ArrayList.class, int.class);
        JavaType mapTypeAsMap = JavaType.construct(Map.class, int.class, int.class);
        MapType mapType = (MapType) mapTypeAsMap.getInstance(MapType.class);
        JavaType narrowedType = mapType._narrow(String.class);
    }

    // Test for withTypeHandler method
    @Test
    public void testWithTypeHandler() {
        JavaType mapType = JavaType.construct(Map.class, int.class, int.class);
        MapType newMapType = mapType.withTypeHandler(new TestTypeHandler());
        assertEquals(TestTypeHandler.class, newMapType.getTypeHandler().getClass());
    }

    // Test for withContentTypeHandler method
    @Test
    public void testWithContentTypeHandler() {
        JavaType mapType = JavaType.construct(Map.class, int.class, int.class);
        JavaType contentTypeHandler = JavaType.construct(String.class, String.class);
        MapType newMapType = mapType.withContentTypeHandler(contentTypeHandler);
        assertEquals(contentTypeHandler, newMapType.getContentTypeHandler());
    }

    // Test for withValueHandler method
    @Test
    public void testWithValueHandler() {
        JavaType mapType = JavaType.construct(Map.class, int.class, int.class);
        MapType newMapType = mapType.withValueHandler(new TestValueHandler());
        assertEquals(TestValueHandler.class, newMapType.getValueHandler().getClass());
    }

    // Test for withContentValueHandler method
    @Test
    public void testWithContentValueHandler() {
        JavaType mapType = JavaType.construct(Map.class, int.class, int.class);
        JavaType contentValueHandler = JavaType.construct(String.class, String.class);
        MapType newMapType = mapType.withContentValueHandler(contentValueHandler);
        assertEquals(contentValueHandler, newMapType.getContentValueHandler());
    }

    // Test for withStaticTyping method
    @Test
    public void testWithStaticTyping() {
        JavaType mapType = JavaType.construct(Map.class, int.class, int.class);
        MapType newMapType = mapType.withStaticTyping();
        assertTrue(newMapType.isStatic());
    }

    // Test for withContentType method
    @Test
    public void testWithContentType() {
        JavaType mapType = JavaType.construct(Map.class, int.class, String.class);
        JavaType newContentType = JavaType.construct(Object.class, String.class);
        MapType newMapType = mapType.withContentType(newContentType);
        assertEquals(newContentType, newMapType.getValueType());
    }

    // Test for withKeyType method
    @Test
    public void testWithKeyType() {
        JavaType mapType = JavaType.construct(Map.class, String.class, int.class);
        JavaType newKeyType = JavaType.construct(Integer.class, Integer.class);
        MapType newMapType = mapType.withKeyType(newKeyType);
        assertEquals(newKeyType, newMapType.getKeyType());
    }

    // Test for refine method
    @Test
    public void testRefineMethod() {
        JavaType mapType = JavaType.construct(Map.class, int.class, String.class);
        TypeBindings bindings = TypeBindings.defaultBindings();
        JavaType newMapType = mapType.refine(HashMap.class, bindings, null, null);
        assertEquals(HashMap.class, newMapType.getRawClass());
    }

    // Test for withKeyTypeHandler method
    @Test
    public void testWithKeyTypeHandler() {
        JavaType mapType = JavaType.construct(Map.class, int.class, int.class);
        Object keyTypeHandler = new TestKeyTypeHandler();
        MapType newMapType = mapType.withKeyTypeHandler(keyTypeHandler);
        assertEquals(keyTypeHandler, newMapType.getKeyTypeHandler());
    }

    // Test for withKeyValueHandler method
    @Test
    public void testWithKeyValueHandler() {
        JavaType mapType = JavaType.construct(Map.class, int.class, int.class);
        Object keyValueHandler = new TestKeyValueHandler();
        MapType newMapType = mapType.withKeyValueHandler(keyValueHandler);
        assertEquals(keyValueHandler, newMapType.getKeyValueHandler());
    }

    // Test for toString method
    @Test
    public void testToStringMethod() {
        JavaType mapType = JavaType.construct(Map.class, String.class, String.class);
        String expected = "[map type; class java.util.Map, java.lang.String -> java.lang.String]";
        assertEquals(expected, mapType.toString());
    }
}