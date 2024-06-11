package com.fasterxml.jackson.databind.type;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class MapTypeTest {

    private MapType mapType;

    @Before
    public void setUp() {
        mapType = MapType.construct(Map.class, null, null, null, String.class, Integer.class);
    }

    @Test
    public void testConstruct() {
        MapType constructMapType = MapType.construct(Map.class, null, null, null, String.class, Integer.class);

        assertNotNull(constructMapType);
        assertEquals(Map.class, constructMapType.getRawClass());
        assertNull(constructMapType.getBindings());
        assertNull(constructMapType.getSuperClass());
        assertNull(constructMapType.getSuperInterfaces());
        assertEquals(String.class, constructMapType.getKeyType());
        assertEquals(Integer.class, constructMapType.getValueType());
        assertNull(constructMapType.getValueHandler());
        assertNull(constructMapType.getTypeHandler());
        assertFalse(constructMapType.isStatic());
    }

    @Test
    public void testConstructDeprecated() {
        MapType constructMapType = MapType.construct(Map.class, String.class, Integer.class);

        assertNotNull(constructMapType);
        assertEquals(Map.class, constructMapType.getRawClass());
        assertNull(constructMapType.getBindings());
        assertEquals(_bogusSuperClass(Map.class), constructMapType.getSuperClass());
        assertNull(constructMapType.getSuperInterfaces());
        assertEquals(String.class, constructMapType.getKeyType());
        assertEquals(Integer.class, constructMapType.getValueType());
        assertNull(constructMapType.getValueHandler());
        assertNull(constructMapType.getTypeHandler());
        assertFalse(constructMapType.isStatic());
    }

    @Test
    public void test_narrow() {
        MapType narrowedMapType = mapType._narrow(HashMap.class);

        assertNotNull(narrowedMapType);
        assertEquals(HashMap.class, narrowedMapType.getRawClass());
        assertEquals(mapType.getBindings(), narrowedMapType.getBindings());
        assertEquals(mapType.getSuperClass(), narrowedMapType.getSuperClass());
        assertEquals(mapType.getSuperInterfaces(), narrowedMapType.getSuperInterfaces());
        assertEquals(mapType.getKeyType(), narrowedMapType.getKeyType());
        assertEquals(mapType.getValueType(), narrowedMapType.getValueType());
        assertEquals(mapType.getValueHandler(), narrowedMapType.getValueHandler());
        assertEquals(mapType.getTypeHandler(), narrowedMapType.getTypeHandler());
        assertEquals(mapType.isStatic(), narrowedMapType.isStatic());
    }

    @Test
    public void testWithTypeHandler() {
        Object typeHandler = new Object();
        MapType mapTypeWithHandler = mapType.withTypeHandler(typeHandler);

        assertNotNull(mapTypeWithHandler);
        assertEquals(mapType.getRawClass(), mapTypeWithHandler.getRawClass());
        assertEquals(mapType.getBindings(), mapTypeWithHandler.getBindings());
        assertEquals(mapType.getSuperClass(), mapTypeWithHandler.getSuperClass());
        assertEquals(mapType.getSuperInterfaces(), mapTypeWithHandler.getSuperInterfaces());
        assertEquals(mapType.getKeyType(), mapTypeWithHandler.getKeyType());
        assertEquals(mapType.getValueType(), mapTypeWithHandler.getValueType());
        assertEquals(mapType.getValueHandler(), mapTypeWithHandler.getValueHandler());
        assertEquals(typeHandler, mapTypeWithHandler.getTypeHandler());
        assertEquals(mapType.isStatic(), mapTypeWithHandler.isStatic());
    }

    @Test
    public void testWithContentTypeHandler() {
        Object contentHandler = new Object();
        MapType mapTypeWithContentHandler = mapType.withContentTypeHandler(contentHandler);

        assertNotNull(mapTypeWithContentHandler);
        assertEquals(mapType.getRawClass(), mapTypeWithContentHandler.getRawClass());
        assertEquals(mapType.getBindings(), mapTypeWithContentHandler.getBindings());
        assertEquals(mapType.getSuperClass(), mapTypeWithContentHandler.getSuperClass());
        assertEquals(mapType.getSuperInterfaces(), mapTypeWithContentHandler.getSuperInterfaces());
        assertEquals(mapType.getKeyType(), mapTypeWithContentHandler.getKeyType());
        assertEquals(mapType.getValueType().withTypeHandler(contentHandler), mapTypeWithContentHandler.getValueType());
        assertEquals(mapType.getValueHandler(), mapTypeWithContentHandler.getValueHandler());
        assertEquals(mapType.getTypeHandler(), mapTypeWithContentHandler.getTypeHandler());
        assertEquals(mapType.isStatic(), mapTypeWithContentHandler.isStatic());
    }

    @Test
    public void testWithValueHandler() {
        Object valueHandler = new Object();
        MapType mapTypeWithValueHandler = mapType.withValueHandler(valueHandler);

        assertNotNull(mapTypeWithValueHandler);
        assertEquals(mapType.getRawClass(), mapTypeWithValueHandler.getRawClass());
        assertEquals(mapType.getBindings(), mapTypeWithValueHandler.getBindings());
        assertEquals(mapType.getSuperClass(), mapTypeWithValueHandler.getSuperClass());
        assertEquals(mapType.getSuperInterfaces(), mapTypeWithValueHandler.getSuperInterfaces());
        assertEquals(mapType.getKeyType(), mapTypeWithValueHandler.getKeyType());
        assertEquals(mapType.getValueType(), mapTypeWithValueHandler.getValueType());
        assertEquals(valueHandler, mapTypeWithValueHandler.getValueHandler());
        assertEquals(mapType.getTypeHandler(), mapTypeWithValueHandler.getTypeHandler());
        assertEquals(mapType.isStatic(), mapTypeWithValueHandler.isStatic());
    }

    @Test
    public void testWithContentValueHandler() {
        Object contentValueHandler = new Object();
        MapType mapTypeWithContentValueHandler = mapType.withContentValueHandler(contentValueHandler);

        assertNotNull(mapTypeWithContentValueHandler);
        assertEquals(mapType.getRawClass(), mapTypeWithContentValueHandler.getRawClass());
        assertEquals(mapType.getBindings(), mapTypeWithContentValueHandler.getBindings());
        assertEquals(mapType.getSuperClass(), mapTypeWithContentValueHandler.getSuperClass());
        assertEquals(mapType.getSuperInterfaces(), mapTypeWithContentValueHandler.getSuperInterfaces());
        assertEquals(mapType.getKeyType(), mapTypeWithContentValueHandler.getKeyType());
        assertEquals(mapType.getValueType().withValueHandler(contentValueHandler), mapTypeWithContentValueHandler.getValueType());
        assertEquals(mapType.getValueHandler(), mapTypeWithContentValueHandler.getValueHandler());
        assertEquals(mapType.getTypeHandler(), mapTypeWithContentValueHandler.getTypeHandler());
        assertEquals(mapType.isStatic(), mapTypeWithContentValueHandler.isStatic());
    }

    @Test
    public void testWithStaticTyping() {
        MapType mapTypeWithStaticTyping = mapType.withStaticTyping();

        assertNotNull(mapTypeWithStaticTyping);
        assertEquals(mapType.getRawClass(), mapTypeWithStaticTyping.getRawClass());
        assertEquals(mapType.getBindings(), mapTypeWithStaticTyping.getBindings());
        assertEquals(mapType.getSuperClass(), mapTypeWithStaticTyping.getSuperClass());
        assertEquals(mapType.getSuperInterfaces(), mapTypeWithStaticTyping.getSuperInterfaces());
        assertEquals(mapType.getKeyType().withStaticTyping(), mapTypeWithStaticTyping.getKeyType());
        assertEquals(mapType.getValueType().withStaticTyping(), mapTypeWithStaticTyping.getValueType());
        assertEquals(mapType.getValueHandler(), mapTypeWithStaticTyping.getValueHandler());
        assertEquals(mapType.getTypeHandler(), mapTypeWithStaticTyping.getTypeHandler());
        assertTrue(mapTypeWithStaticTyping.isStatic());
    }

    @Test
    public void testWithContentType() {
        JavaType contentType = new JavaType();
        MapType mapTypeWithContentType = mapType.withContentType(contentType);

        assertNotNull(mapTypeWithContentType);
        assertEquals(mapType.getRawClass(), mapTypeWithContentType.getRawClass());
        assertEquals(mapType.getBindings(), mapTypeWithContentType.getBindings());
        assertEquals(mapType.getSuperClass(), mapTypeWithContentType.getSuperClass());
        assertEquals(mapType.getSuperInterfaces(), mapTypeWithContentType.getSuperInterfaces());
        assertEquals(mapType.getKeyType(), mapTypeWithContentType.getKeyType());
        assertEquals(contentType, mapTypeWithContentType.getValueType());
        assertEquals(mapType.getValueHandler(), mapTypeWithContentType.getValueHandler());
        assertEquals(mapType.getTypeHandler(), mapTypeWithContentType.getTypeHandler());
        assertEquals(mapType.isStatic(), mapTypeWithContentType.isStatic());
    }

    @Test
    public void testWithKeyType() {
        JavaType keyType = new JavaType();
        MapType mapTypeWithKeyType = mapType.withKeyType(keyType);

        assertNotNull(mapTypeWithKeyType);
        assertEquals(mapType.getRawClass(), mapTypeWithKeyType.getRawClass());
        assertEquals(mapType.getBindings(), mapTypeWithKeyType.getBindings());
        assertEquals(mapType.getSuperClass(), mapTypeWithKeyType.getSuperClass());
        assertEquals(mapType.getSuperInterfaces(), mapTypeWithKeyType.getSuperInterfaces());
        assertEquals(keyType, mapTypeWithKeyType.getKeyType());
        assertEquals(mapType.getValueType(), mapTypeWithKeyType.getValueType());
        assertEquals(mapType.getValueHandler(), mapTypeWithKeyType.getValueHandler());
        assertEquals(mapType.getTypeHandler(), mapTypeWithKeyType.getTypeHandler());
        assertEquals(mapType.isStatic(), mapTypeWithKeyType.isStatic());
    }

    @Test
    public void testRefine() {
        MapType refinedMapType = mapType.refine(Map.class, null, null, null);

        assertNotNull(refinedMapType);
        assertEquals(Map.class, refinedMapType.getRawClass());
        assertNull(refinedMapType.getBindings());
        assertNull(refinedMapType.getSuperClass());
        assertNull(refinedMapType.getSuperInterfaces());
        assertEquals(mapType.getKeyType(), refinedMapType.getKeyType());
        assertEquals(mapType.getValueType(), refinedMapType.getValueType());
        assertEquals(mapType.getValueHandler(), refinedMapType.getValueHandler());
        assertEquals(mapType.getTypeHandler(), refinedMapType.getTypeHandler());
        assertEquals(mapType.isStatic(), refinedMapType.isStatic());
    }

    @Test
    public void testWithKeyTypeHandler() {
        Object keyTypeHandler = new Object();
        MapType mapTypeWithKeyTypeHandler = mapType.withKeyTypeHandler(keyTypeHandler);

        assertNotNull(mapTypeWithKeyTypeHandler);
        assertEquals(mapType.getRawClass(), mapTypeWithKeyTypeHandler.getRawClass());
        assertEquals(mapType.getBindings(), mapTypeWithKeyTypeHandler.getBindings());
        assertEquals(mapType.getSuperClass(), mapTypeWithKeyTypeHandler.getSuperClass());
        assertEquals(mapType.getSuperInterfaces(), mapTypeWithKeyTypeHandler.getSuperInterfaces());
        assertEquals(mapType.getKeyType().withTypeHandler(keyTypeHandler), mapTypeWithKeyTypeHandler.getKeyType());
        assertEquals(mapType.getValueType(), mapTypeWithKeyTypeHandler.getValueType());
        assertEquals(mapType.getValueHandler(), mapTypeWithKeyTypeHandler.getValueHandler());
        assertEquals(mapType.getTypeHandler(), mapTypeWithKeyTypeHandler.getTypeHandler());
        assertEquals(mapType.isStatic(), mapTypeWithKeyTypeHandler.isStatic());
    }

    @Test
    public void testWithKeyValueHandler() {
        Object keyValueHandler = new Object();
        MapType mapTypeWithKeyValueHandler = mapType.withKeyValueHandler(keyValueHandler);

        assertNotNull(mapTypeWithKeyValueHandler);
        assertEquals(mapType.getRawClass(), mapTypeWithKeyValueHandler.getRawClass());
        assertEquals(mapType.getBindings(), mapTypeWithKeyValueHandler.getBindings());
        assertEquals(mapType.getSuperClass(), mapTypeWithKeyValueHandler.getSuperClass());
        assertEquals(mapType.getSuperInterfaces(), mapTypeWithKeyValueHandler.getSuperInterfaces());
        assertEquals(mapType.getKeyType().withValueHandler(keyValueHandler), mapTypeWithKeyValueHandler.getKeyType());
        assertEquals(mapType.getValueType(), mapTypeWithKeyValueHandler.getValueType());
        assertEquals(mapType.getValueHandler(), mapTypeWithKeyValueHandler.getValueHandler());
        assertEquals(mapType.getTypeHandler(), mapTypeWithKeyValueHandler.getTypeHandler());
        assertEquals(mapType.isStatic(), mapTypeWithKeyValueHandler.isStatic());
    }

    @Test
    public void testToString() {
        assertEquals("[map type; class java.util.Map, class java.lang.String -> class java.lang.Integer]", mapType.toString());
    }
}