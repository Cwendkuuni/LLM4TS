package com.fasterxml.jackson.databind.type;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.lang.reflect.TypeVariable;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.databind.JavaType;

public class MapLikeTypeTest {
    private MapLikeType mapLikeType;

    @Before
    public void setUp() {
        mapLikeType = new MapLikeType(Map.class, null, null, null, null, null, null, null, false);
    }

    @Test
    public void testGetTypeParameters() {
        TypeVariable<?>[] typeParameters = mapLikeType.getTypeParameters();
        assertNull(typeParameters);
    }

    @Test
    public void testIsMapLikeType() {
        assertTrue(mapLikeType.isMapLikeType());
    }

    @Test
    public void testGetKeyType() {
        JavaType keyType = mapLikeType.getKeyType();
        assertNull(keyType);
    }

    @Test
    public void testGetContentType() {
        JavaType contentType = mapLikeType.getContentType();
        assertNull(contentType);
    }

    @Test
    public void testGetContentValueHandler() {
        Object contentValueHandler = mapLikeType.getContentValueHandler();
        assertNull(contentValueHandler);
    }

    @Test
    public void testGetContentTypeHandler() {
        Object contentTypeHandler = mapLikeType.getContentTypeHandler();
        assertNull(contentTypeHandler);
    }

    @Test
    public void testHasHandlers() {
        assertFalse(mapLikeType.hasHandlers());
    }

    @Test
    public void testGetErasedSignature() {
        String erasedSignature = mapLikeType.getErasedSignature(new StringBuilder()).toString();
        assertEquals("java.util.Map", erasedSignature);
    }

    @Test
    public void testGetGenericSignature() {
        String genericSignature = mapLikeType.getGenericSignature(new StringBuilder()).toString();
        assertEquals("java.util.Map< ; ; >;", genericSignature);
    }

    @Test
    public void testWithKeyType() {
        JavaType keyType = new JavaType();
        MapLikeType result = mapLikeType.withKeyType(keyType);
        assertEquals(keyType, result.getKeyType());
    }

    @Test
    public void testWithContentType() {
        JavaType contentType = new JavaType();
        MapLikeType result = mapLikeType.withContentType(contentType);
        assertEquals(contentType, result.getContentType());
    }

    @Test
    public void testWithKeyTypeHandler() {
        Object keyTypeHandler = new Object();
        MapLikeType result = mapLikeType.withKeyTypeHandler(keyTypeHandler);
        assertEquals(keyTypeHandler, result.getKeyType().getTypeHandler());
    }

    @Test
    public void testWithKeyValueHandler() {
        Object keyValueHandler = new Object();
        MapLikeType result = mapLikeType.withKeyValueHandler(keyValueHandler);
        assertEquals(keyValueHandler, result.getKeyType().getValueHandler());
    }

    @Test
    public void testIsTrueMapType() {
        assertTrue(mapLikeType.isTrueMapType());
    }

    @Test
    public void testToString() {
        String toString = mapLikeType.toString();
        assertEquals("[map-like type; class java.util.Map, null -> null]", toString);
    }

    @Test
    public void testEquals() {
        MapLikeType type1 = new MapLikeType(Map.class, null, null, null, null, null, null, null, false);
        MapLikeType type2 = new MapLikeType(Map.class, null, null, null, null, null, null, null, false);
        MapLikeType type3 = new MapLikeType(HashMap.class, null, null, null, null, null, null, null, false);

        assertTrue(type1.equals(type2));
        assertFalse(type1.equals(type3));
    }
}