package com.fasterxml.jackson.databind.type.test;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.MapType;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@RunWith(JUnit4.class)
public class MapTypeTest {

    @Test
    public void testMapTypeConstructor() {
        JavaType keyType = JavaType.construct(String.class);
        JavaType valueType = JavaType.construct(Integer.class);
        MapType mapType = MapType.construct(Map.class, null, null, null, keyType, valueType);
        assertNotNull(mapType);
        assertEquals(Map.class, mapType.getRawClass());
        assertEquals(keyType, mapType.getKeyType());
        assertEquals(valueType, mapType.getValueType());
    }

    @Test
    public void testMapTypeWithTypeHandler() {
        JavaType keyType = JavaType.construct(String.class);
        JavaType valueType = JavaType.construct(Integer.class);
        Object handler = new Object();
        MapType mapType = new MapType(Map.class, null, null, null, keyType, valueType);
        MapType newMapType = mapType.withTypeHandler(handler);
        assertNotNull(newMapType);
        assertEquals(mapType.getRawClass(), newMapType.getRawClass());
        assertEquals(mapType.getKeyType(), newMapType.getKeyType());
        assertEquals(mapType.getValueType(), newMapType.getValueType());
        assertEquals(handler, newMapType.getTypeHandler());
    }

    @Test
    public void testMapTypeWithValueHandler() {
        JavaType keyType = JavaType.construct(String.class);
        JavaType valueType = JavaType.construct(Integer.class);
        Object handler = new Object();
        MapType mapType = new MapType(Map.class, null, null, null, keyType, valueType);
        MapType newMapType = mapType.withValueHandler(handler);
        assertNotNull(newMapType);
        assertEquals(mapType.getRawClass(), newMapType.getRawClass());
        assertEquals(mapType.getKeyType(), newMapType.getKeyType());
        assertEquals(mapType.getValueType(), newMapType.getValueType());
        assertEquals(handler, newMapType.getValueHandler());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testMapTypeConstructWithInvalidSuperclass() {
        JavaType keyType = JavaType.construct(String.class);
        JavaType valueType = JavaType.construct(Integer.class);
        MapType mapType = MapType.construct(List.class, null, null, null, keyType, valueType);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testMapTypeConstructWithNullKeyType() {
        JavaType valueType = JavaType.construct(Integer.class);
        MapType mapType = MapType.construct(Map.class, null, null, null, null, valueType);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testMapTypeConstructWithNullValueType() {
        JavaType keyType = JavaType.construct(String.class);
        MapType mapType = MapType.construct(Map.class, null, null, null, keyType, null);
    }

    @Test
    public void testMapTypeWithStaticTyping() {
        JavaType keyType = JavaType.construct(String.class);
        JavaType valueType = JavaType.construct(Integer.class);
        MapType mapType = new MapType(Map.class, null, null, null, keyType, valueType);
        MapType staticMapType = mapType.withStaticTyping();
        assertNotNull(staticMapType);
        assertEquals(mapType.getRawClass(), staticMapType.getRawClass());
        assertEquals(mapType.getKeyType(), staticMapType.getKeyType());
        assertEquals(mapType.getValueType(), staticMapType.getValueType());
        assertTrue(staticMapType.isStatic());
    }

    @Test
    public void testMapTypeWithContentType() {
        JavaType keyType = JavaType.construct(String.class);
        JavaType valueType = JavaType.construct(Integer.class);
        JavaType contentType = JavaType.construct(String[].class);
        MapType mapType = new MapType(Map.class, null, null, null, keyType, valueType);
        MapType newMapType = mapType.withContentType(contentType);
        assertNotNull(newMapType);
        assertEquals(mapType.getRawClass(), newMapType.getRawClass());
        assertEquals(mapType.getKeyType(), newMapType.getKeyType());
        assertEquals(contentType, newMapType.getValueType());
    }

    @Test
    public void testMapTypeWithKeyType() {
        JavaType oldKeyType = JavaType.construct(String.class);
        JavaType newKeyType = JavaType.construct(Integer.class);
        JavaType valueType = JavaType.construct(Integer.class);
        MapType mapType = new MapType(Map.class, null, null, null, oldKeyType, valueType);
        MapType newMapType = mapType.withKeyType(newKeyType);
        assertNotNull(newMapType);
        assertEquals(mapType.getRawClass(), newMapType.getRawClass());
        assertEquals(newKeyType, newMapType.getKeyType());
        assertEquals(mapType.getValueType(), newMapType.getValueType());
    }
}