package com.fasterxml.jackson.databind.type.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.TypeBindings;
import com.fasterxml.jackson.databind.type.MapLikeType;
import com.fasterxml.jackson.databind.type.MapType;
import com.fasterxml.jackson.databind.type.TypeFactory;

public class MapTypeTest {

    private JavaType keyType;
    private JavaType valueType;
    private JavaType mapType;

    @Before
    public void setUp() {
        keyType = TypeFactory.defaultInstance().constructType(Map.class, String.class);
        valueType = TypeFactory.defaultInstance().constructType(Map.class, Integer.class);
        mapType = MapType.construct(Map.class, TypeBindings.empty(), null, null, keyType, valueType);
    }

    @Test
    public void testMapTypeConstructor() {
        MapType mapType = new MapType(Map.class, TypeBindings.empty(), null, null, keyType, valueType);
        assertEquals(Map.class, mapType.getRawClass());
        assertEquals(keyType, mapType.getKeyType());
        assertEquals(valueType, mapType.getValueType());
    }

    @Test
    public void testMapTypeConstructWithTypeBindings() {
        MapType mapType = MapType.construct(Map.class, TypeBindings.empty(), null, null, keyType, valueType);
        assertEquals(Map.class, mapType.getRawClass());
        assertEquals(keyType, mapType.getKeyType());
        assertEquals(valueType, mapType.getValueType());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testMapTypeConstructWithNullClass() {
        MapType mapType = new MapType(null, TypeBindings.empty(), null, null, keyType, valueType);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testMapTypeConstructWithInvalidSuperType() {
        JavaType invalidSuperType = TypeFactory.defaultInstance().constructType(HashMap.class, String.class, Integer.class);
        MapType mapType = new MapType(Map.class, TypeBindings.empty(), invalidSuperType, null, keyType, valueType);
    }

    @Test
    public void testMapTypeWithTypeHandler() {
        Object handler = new Object() {};
        MapType mapTypeWithHandler = mapType.withTypeHandler(handler);
        assertNotNull(mapTypeWithHandler.getTypeHandler());
        assertEquals(handler.getClass(), mapTypeWithHandler.getTypeHandler().getClass());
    }

    @Test
    public void testMapTypeWithContentTypeHandler() {
        Object handler = new Object() {};
        JavaType contentType = TypeFactory.defaultInstance().constructType(HashMap.class, String.class, Integer.class);
        MapType mapTypeWithContentTypeHandler = mapType.withContentTypeHandler(handler);
        assertNotNull(mapTypeWithContentTypeHandler.getContentTypeHandler());
        assertEquals(handler.getClass(), mapTypeWithContentTypeHandler.getContentTypeHandler().getClass());
    }

    @Test
    public void testMapTypeWithValueHandler() {
        Object handler = new Object() {};
        MapType mapTypeWithValueHandler = mapType.withValueHandler(handler);
        assertNotNull(mapTypeWithValueHandler.getValueHandler());
        assertEquals(handler.getClass(), mapTypeWithValueHandler.getValueHandler().getClass());
    }

    @Test
    public void testMapTypeWithContentValueHandler() {
        Object handler = new Object() {};
        JavaType contentValueType = TypeFactory.defaultInstance().constructType(HashMap.class, String.class, Integer.class);
        MapType mapTypeWithContentValueHandler = mapType.withContentValueHandler(handler);
        assertNotNull(mapTypeWithContentValueHandler.getContentValueHandler());
        assertEquals(handler.getClass(), mapTypeWithContentValueHandler.getContentValueHandler().getClass());
    }

    @Test
    public void testMapTypeWithStaticTyping() {
        MapType mapType = mapType.withStaticTyping();
        assertTrue(mapType.isStatic());
    }

    @Test
    public void testMapTypeWithContentType() {
        JavaType newContentType = TypeFactory.defaultInstance().constructType(HashMap.class, String.class, Double.class);
        MapType mapTypeWithContentType = mapType.withContentType(newContentType);
        assertNotSame(valueType, mapTypeWithContentType.getValueType());
        assertEquals(newContentType, mapTypeWithContentType.getValueType());
    }

    @Test
    public void testMapTypeWithKeyType() {
        JavaType newKeyType = TypeFactory.defaultInstance().constructType(HashMap.class, Integer.class, String.class);
        MapType mapTypeWithKeyType = mapType.withKeyType(newKeyType);
        assertNotSame(keyType, mapTypeWithKeyType.getKeyType());
        assertEquals(newKeyType, mapTypeWithKeyType.getKeyType());
    }

    @Test
    public void testMapTypeRefine() {
        JavaType refinedType = TypeFactory.defaultInstance().constructType(HashMap.class, String.class, String.class);
        MapType mapTypeRefined = mapType.refine(refinedType.getRawClass(), TypeBindings.empty(), null, null);
        assertNotSame(mapType, mapTypeRefined);
        assertEquals(refinedType, mapTypeRefined.getType());
    }

    @Test
    public void testMapTypeToString() {
        assertEquals("[map type; class java.util.Map, java.lang.String -> java.lang.Integer]", mapType.toString());
    }
}