package com.fasterxml.jackson.databind.type;

import static org.junit.Assert.*;

import org.junit.Test;

public class MapTypeTest {

    @Test
    public void testConstruct() {
        Class<?> rawType = Map.class;
        TypeBindings bindings = TypeBindings.empty();
        JavaType keyType = JavaType.construct(String.class, null);
        JavaType valueType = JavaType.construct(Integer.class, null);
        MapType mapType = MapType.construct(rawType, bindings, null, null, keyType, valueType);
        assertEquals(Map.class, mapType.getRawClass());
        assertEquals(keyType, mapType.getKeyType());
        assertEquals(valueType, mapType.getValueType());
    }

    @Test
    public void testConstructWithBindingsAndSupertypes() {
        Class<?> rawType = Map.class;
        TypeBindings bindings = TypeBindings.empty();
        JavaType superClass = JavaType.construct(Object.class, null);
        JavaType[] superInterfaces = new JavaType[]{JavaType.construct(Serializable.class, null)};
        JavaType keyType = JavaType.construct(String.class, null);
        JavaType valueType = JavaType.construct(Integer.class, null);
        MapType mapType = MapType.construct(rawType, bindings, superClass, superInterfaces, keyType, valueType);
        assertEquals(Map.class, mapType.getRawClass());
        assertEquals(keyType, mapType.getKeyType());
        assertEquals(valueType, mapType.getValueType());
        assertEquals(superClass, mapType.getSuperclass());
        assertArrayEquals(superInterfaces, mapType.getSuperinterfaces());
    }

    @Test
    public void testNarrow() {
        Class<?> rawType = Map.class;
        JavaType mapType = JavaType.construct(rawType, null);
        JavaType subclass = HashMap.class;
        MapType narrowedMapType = mapType.narrow(subclass);
        assertEquals(HashMap.class, narrowedMapType.getRawClass());
        assertEquals(mapType.getKeyType(), narrowedMapType.getKeyType());
        assertEquals(mapType.getValueType(), narrowedMapType.getValueType());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testNarrowThrowsException() {
        Class<?> rawType = Map.class;
        JavaType mapType = JavaType.construct(rawType, null);
        JavaType subclass = Object.class;
        mapType.narrow(subclass);
    }

    @Test
    public void testWithTypeHandler() {
        Class<?> rawType = Map.class;
        JavaType keyType = JavaType.construct(String.class, null);
        JavaType valueType = JavaType.construct(Integer.class, null);
        Object handler = new Object(){};
        MapType mapType = new MapType(rawType, null, null, null, keyType, valueType);
        MapType withTypeHandler = mapType.withTypeHandler(handler);
        assertEquals(handler, withTypeHandler.getTypeHandler());
    }

    @Test
    public void testWithContentTypeHandler() {
        Class<?> rawType = Map.class;
        JavaType keyType = JavaType.construct(String.class, null);
        JavaType valueType = JavaType.construct(Integer.class, null);
        Object contentTypeHandler = new Object(){};
        MapType mapType = new MapType(rawType, null, null, null, keyType, valueType);
        MapType withContentTypeHandler = mapType.withContentTypeHandler(contentTypeHandler);
        assertEquals(contentTypeHandler, withContentTypeHandler.getContentTypeHandler());
    }

    @Test
    public void testWithValueHandler() {
        Class<?> rawType = Map.class;
        JavaType keyType = JavaType.construct(String.class, null);
        JavaType valueType = JavaType.construct(Integer.class, null);
        Object valueHandler = new Object(){};
        MapType mapType = new MapType(rawType, null, null, null, keyType, valueType);
        MapType withValueHandler = mapType.withValueHandler(valueHandler);
        assertEquals(valueHandler, withValueHandler.getValueHandler());
    }

    @Test
    public void testWithContentValueHandler() {
        Class<?> rawType = Map.class;
        JavaType keyType = JavaType.construct(String.class, null);
        JavaType valueType = JavaType.construct(Integer.class, null);
        Object contentValueHandler = new Object(){};
        MapType mapType = new MapType(rawType, null, null, null, keyType, valueType);
        MapType withContentValueHandler = mapType.withContentValueHandler(contentValueHandler);
        assertEquals(contentValueHandler, withContentValueHandler.getContentValueHandler());
    }

    @Test
    public void testWithStaticTyping() {
        Class<?> rawType = Map.class;
        JavaType keyType = JavaType.construct(String.class, null);
        JavaType valueType = JavaType.construct(Integer.class, null);
        MapType mapType = new MapType(rawType, null, null, null, keyType, valueType, null, null, false);
        MapType withStaticTyping = mapType.withStaticTyping();
        assertTrue(withStaticTyping.isStatic());
    }

    @Test
    public void testWithContentType() {
        Class<?> rawType = Map.class;
        JavaType mapType = JavaType.construct(rawType, null);
        JavaType contentType = JavaType.construct(Number.class, null);
        MapType withContentType = mapType.withContentType(contentType);
        assertEquals(contentType, withContentType.getValueType());
    }

    @Test
    public void testWithKeyType() {
        Class<?> rawType = Map.class;
        JavaType mapType = JavaType.construct(rawType, null);
        JavaType keyType = JavaType.construct(String.class, null);
        MapType withKeyType = mapType.withKeyType(keyType);
        assertEquals(keyType, withKeyType.getKeyType());
    }

    @Test
    public void testRefine() {
        Class<?> rawType = Map.class;
        JavaType mapType = JavaType.construct(rawType, null);
        JavaType refinedType = MapType.construct(HashSet.class, TypeBindings.empty(), null, null,
                JavaType.construct(String.class, null), JavaType.construct(Integer.class, null));
        MapType refinedMapType = mapType.refine(HashSet.class, TypeBindings.empty(), null, null);
        assertEquals(refinedType, refinedMapType);
    }

    @Test
    public void testWithKeyTypeHandler() {
        Class<?> rawType = Map.class;
        JavaType keyType = JavaType.construct(String.class, null);
        JavaType valueType = JavaType.construct(Integer.class, null);
        Object keyTypeHandler = new Object(){};
        MapType mapType = new MapType(rawType, null, null, null, keyType, valueType);
        MapType withKeyTypeHandler = mapType.withKeyTypeHandler(keyTypeHandler);
        assertEquals(keyTypeHandler, withKeyTypeHandler.getKeyTypeHandler());
    }

    @Test
    public void testWithKeyValueHandler() {
        Class<?> rawType = Map.class;
        JavaType keyType = JavaType.construct(String.class, null);
        JavaType valueType = JavaType.construct(Integer.class, null);
        Object keyValueHandler = new Object(){};
        MapType mapType = new MapType(rawType, null, null, null, keyType, valueType);
        MapType withKeyValueHandler = mapType.withKeyValueHandler(keyValueHandler);
        assertEquals(keyValueHandler, withKeyValueHandler.getKeyValueHandler());
    }
}