package com.yourpackage.test;

import static org.junit.Assert.*;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.MapType;

import org.junit.Test;

public class MapTypeTest {

    @Test
    public void testConstruct() {
        Class<?> rawType = Map.class;
        TypeBindings bindings = TypeBindings.empty();
        JavaType keyType = JavaType.constructRawListType(String.class, 0);
        JavaType valueType = JavaType.constructRawListType(Integer.class, 0);
        MapType mapType = MapType.construct(rawType, bindings, null, null, keyType, valueType);
        assertEquals(Map.class, mapType.getRawClass());
        assertEquals(keyType, mapType.getKeyType());
        assertEquals(valueType, mapType.getValueType());
    }

    @Test
    public void testConstructDeprecated() {
        Class<?> rawType = Map.class;
        JavaType keyType = JavaType.constructRawClass(String.class);
        JavaType valueType = JavaType.constructRawClass(Integer.class);
        MapType mapType = MapType.construct(rawType, keyType, valueType);
        assertEquals(Map.class, mapType.getRawClass());
        assertEquals(keyType, mapType.getKeyType());
        assertEquals(valueType, mapType.getValueType());
    }

    @Test
    public void testNarrow() {
        Class<?> subclass = HashMap.class;
        MapType mapType = new MapType(Map.class, null, null, null, null, null, null, null, false);
        MapType narrowType = mapType.narrow(subclass);
        assertEquals(HashMap.class, narrowType.getRawClass());
        assertEquals(mapType.getKeyType(), narrowType.getKeyType());
        assertEquals(mapType.getValueType(), narrowType.getValueType());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNarrowException() {
        Class<?> subclass = List.class;
        MapType mapType = new MapType(Map.class, null, null, null, null, null, null, null, false);
        mapType.narrow(subclass);
    }

    @Test
    public void testWithTypeHandler() {
        Class<?> rawType = Map.class;
        JavaType keyType = JavaType.constructRawClass(String.class);
        JavaType valueType = JavaType.constructRawClass(Integer.class);
        MapType mapType = new MapType(rawType, null, null, null, keyType, valueType, null, null, false);
        Object handler = new Object();
        MapType withTypeHandler = mapType.withTypeHandler(handler);
        assertEquals(handler, withTypeHandler.getTypeHandler());
    }

    @Test
    public void testWithContentTypeHandler() {
        Class<?> rawType = Map.class;
        JavaType keyType = JavaType.constructRawClass(String.class);
        JavaType valueType = JavaType.constructRawClass(Integer.class);
        JavaType contentTypeHandler = JavaType.constructRawClass(StringSerializer.class);
        MapType mapType = new MapType(rawType, null, null, null, keyType, valueType, null, null, false);
        MapType withContentTypeHandler = mapType.withContentTypeHandler(contentTypeHandler);
        assertEquals(contentTypeHandler, withContentTypeHandler.getContentTypeHandler());
    }

    @Test
    public void testWithValueHandler() {
        Class<?> rawType = Map.class;
        JavaType keyType = JavaType.constructRawClass(String.class);
        JavaType valueType = JavaType.constructRawClass(Integer.class);
        Object valueHandler = new Object();
        MapType mapType = new MapType(rawType, null, null, null, keyType, valueType, null, null, false);
        MapType withValueHandler = mapType.withValueHandler(valueHandler);
        assertEquals(valueHandler, withValueHandler.getValueHandler());
    }

    @Test
    public void testWithContentValueHandler() {
        Class<?> rawType = Map.class;
        JavaType keyType = JavaType.constructRawClass(String.class);
        JavaType valueType = JavaType.constructRawClass(Integer.class);
        Object contentValueHandler = new Object();
        MapType mapType = new MapType(rawType, null, null, null, keyType, valueType, null, null, false);
        MapType withContentValueHandler = mapType.withContentValueHandler(contentValueHandler);
        assertEquals(contentValueHandler, withContentValueHandler.getContentValueHandler());
    }

    @Test
    public void testWithStaticTyping() {
        Class<?> rawType = Map.class;
        JavaType keyType = JavaType.constructRawClass(String.class);
        JavaType valueType = JavaType.constructRawClass(Integer.class);
        MapType mapType = new MapType(rawType, null, null, null, keyType, valueType, null, null, false);
        MapType withStaticTyping = mapType.withStaticTyping();
        assertTrue(withStaticTyping.isStatic());
    }

    @Test
    public void testWithContentType() {
        Class<?> rawType = Map.class;
        JavaType keyType = JavaType.constructRawClass(String.class);
        JavaType valueType = JavaType.constructRawClass(Integer.class);
        JavaType contentType = JavaType.constructRawClass(String.class);
        MapType mapType = new MapType(rawType, null, null, null, keyType, valueType, null, null, false);
        MapType withContentType = mapType.withContentType(contentType);
        assertEquals(contentType, withContentType.getValueType());
    }

    @Test
    public void testWithKeyType() {
        Class<?> rawType = Map.class;
        JavaType keyType1 = JavaType.constructRawClass(String.class);
        JavaType valueType = JavaType.constructRawClass(Integer.class);
        MapType mapType = new MapType(rawType, null, null, null, keyType1, valueType, null, null, false);
        JavaType keyType2 = JavaType.constructRawClass(Integer.class);
        MapType withKeyType = mapType.withKeyType(keyType2);
        assertEquals(keyType2, withKeyType.getKeyType());
    }

    @Test
    public void testRefine() {
        Class<?> rawType = HashMap.class;
        TypeBindings bindings = TypeBindings.empty();
        JavaType superClass = Map.class;
        JavaType[] superInterfaces = new JavaType[0];
        JavaType keyType = JavaType.constructRawClass(String.class);
        JavaType valueType = JavaType.constructRawClass(Integer.class);
        MapType mapType = new MapType(rawType, null, superClass, superInterfaces, keyType, valueType, null, null, false);
        MapType refinedType = mapType.refine(Map.class, bindings, superClass, superInterfaces);
        assertEquals(Map.class, refinedType.getRawClass());
        assertEquals(keyType, refinedType.getKeyType());
        assertEquals(valueType, refinedType.getValueType());
    }

    @Test
    public void testWithKeyTypeHandler() {
        Class<?> rawType = Map.class;
        JavaType keyType = JavaType.constructRawClass(String.class);
        JavaType valueType = JavaType.constructRawClass(Integer.class);
        Object keyTypeHandler = new Object();
        MapType mapType = new MapType(rawType, null, null, null, keyType, valueType, null, null, false);
        MapType withKeyTypeHandler = mapType.withKeyTypeHandler(keyTypeHandler);
        assertEquals(keyTypeHandler, withKeyTypeHandler.getKeyTypeHandler());
    }

    @Test
    public void testWithKeyValueHandler() {
        Class<?> rawType = Map.class;
        JavaType keyType = JavaType.constructRawClass(String.class);
        JavaType valueType = JavaType.constructRawClass(Integer.class);
        Object keyValueHandler = new Object();
        MapType mapType = new MapType(rawType, null, null, null, keyType, valueType, null, null, false);
        MapType withKeyValueHandler = mapType.withKeyValueHandler(keyValueHandler);
        assertEquals(keyValueHandler, withKeyValueHandler.getKeyValueHandler());
    }

    @Test
    public void testToString() {
        Class<?> rawType = Map.class;
        JavaType keyType = JavaType.constructRawClass(String.class);
        JavaType valueType = JavaType.constructRawClass(Integer.class);
        MapType mapType = new MapType(rawType, null, null, null, keyType, valueType, null, null, false);
        assertEquals("[map type; class " + Map.class.getName() + ", " + keyType + " -> " + valueType + "]", mapType.toString());
    }
}