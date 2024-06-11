package com.fasterxml.jackson.databind.type;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class MapTypeTest {
    @Test
    public void testConstruct() {
        MapType mapType = MapType.construct(String.class, null, null, null, String.class, String.class);
        assertEquals(String.class, mapType.getKeyType());
        assertEquals(String.class, mapType.getValueType());

        assertNotEquals(Integer.class, mapType.getKeyType());
        assertNotEquals(Integer.class, mapType.getValueType());
    }

    @Test
    public void testWithKeyTypeHandler() {
        MapType mapType = MapType.construct(String.class, null, null, null, String.class, String.class);
        Object handler = new Object();
        MapType withKeyTypeHandler = mapType.withKeyTypeHandler(handler);

        assertNotNull(withKeyTypeHandler.getKeyTypeHandler());
        assertEquals(handler, withKeyTypeHandler.getKeyTypeHandler());

        assertNotEquals(new Object(), withKeyTypeHandler.getKeyTypeHandler());
    }

    @Test
    public void testWithKeyValueHandler() {
        MapType mapType = MapType.construct(String.class, null, null, null, String.class, String.class);
        Object handler = new Object();
        MapType withKeyValueHandler = mapType.withKeyValueHandler(handler);

        assertNotNull(withKeyValueHandler.getKeyValueHandler());
        assertEquals(handler, withKeyValueHandler.getKeyValueHandler());

        assertNotEquals(new Object(), withKeyValueHandler.getKeyValueHandler());
    }

    @Test
    public void testWithStaticTyping() {
        MapType mapType = MapType.construct(String.class, null, null, null, String.class, String.class);
        MapType withStaticTyping = mapType.withStaticTyping();

        assertTrue(withStaticTyping.isStaticTyping());

        assertFalse(mapType.isStaticTyping());
    }

    @Test
    public void testWithContentType() {
        MapType mapType = MapType.construct(String.class, null, null, null, String.class, String.class);
        JavaType contentType = new JavaType();
        MapType withContentType = mapType.withContentType(contentType);

        assertEquals(contentType, withContentType.getContentType());

        assertNotEquals(new JavaType(), withContentType.getContentType());
    }

    @Test
    public void testWithKeyType() {
        MapType mapType = MapType.construct(String.class, null, null, null, String.class, String.class);
        JavaType keyType = new JavaType();
        MapType withKeyType = mapType.withKeyType(keyType);

        assertEquals(keyType, withKeyType.getKeyType());

        assertNotEquals(new JavaType(), withKeyType.getKeyType());
    }

    @Test
    public void testRefine() {
        MapType mapType = MapType.construct(String.class, null, null, null, String.class, String.class);
        JavaType superClass = new JavaType();
        JavaType[] superInterfaces = new JavaType[] { new JavaType() };
        MapType refinedMapType = mapType.refine(MapType.class, null, superClass, superInterfaces);

        assertEquals(MapType.class, refinedMapType.getRawClass());
        assertEquals(superClass, refinedMapType.getSuperClass());
        assertArrayEquals(superInterfaces, refinedMapType.getInterfaces());
    }

    @Test
    public void testToString() {
        MapType mapType = MapType.construct(String.class, null, null, null, String.class, String.class);
        String expectedResult = "[map type; class java.lang.String, java.lang.String -> java.lang.String]";
        assertEquals(expectedResult, mapType.toString());

        assertNotEquals("invalid string", mapType.toString());
    }
}