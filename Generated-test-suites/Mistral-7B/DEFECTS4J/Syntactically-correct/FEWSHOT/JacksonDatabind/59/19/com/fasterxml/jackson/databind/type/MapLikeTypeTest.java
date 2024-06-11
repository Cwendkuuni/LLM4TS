package org.apache.commons.cli.test; // or your test package name if different than org.apache.commons.cli

import static org.junit.Assert.*;

import org.junit.Test;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.MapLikeType;

public class MapLikeTypeTest {

    @Test
    public void testMapLikeType() {
        JavaType listType = TypeFactory.defaultInstance().constructType(Map.class, String.class, Integer.class);
        MapLikeType mapLikeType = MapLikeType.upgradeFrom(listType, String.class, Integer.class);

        assertEquals(Map.class, mapLikeType.getRawClass());
        assertEquals(String.class, mapLikeType.getKeyType().getRawClass());
        assertEquals(Integer.class, mapLikeType.getValueType().getRawClass());
        assertFalse(mapLikeType.isTrueMapType());
    }

    @Test
    public void testMapLikeTypeWithKeyTypeHandler() {
        JavaType listType = TypeFactory.defaultInstance().constructType(Map.class, String.class, Integer.class);
        MapLikeType mapLikeType = MapLikeType.upgradeFrom(listType, String.class, Integer.class);

        Object keyTypeHandler = new Object() {};
        MapLikeType mapLikeTypeWithKeyTypeHandler = mapLikeType.withKeyTypeHandler(keyTypeHandler);

        assertSame(mapLikeType, mapLikeTypeWithKeyTypeHandler);
        assertEquals(keyTypeHandler, mapLikeTypeWithKeyTypeHandler.getKeyTypeHandler());
    }

    @Test
    public void testMapLikeTypeWithValueTypeHandler() {
        JavaType listType = TypeFactory.defaultInstance().constructType(Map.class, String.class, Integer.class);
        MapLikeType mapLikeType = MapLikeType.upgradeFrom(listType, String.class, Integer.class);

        Object valueTypeHandler = new Object() {};
        MapLikeType mapLikeTypeWithValueTypeHandler = mapLikeType.withValueTypeHandler(valueTypeHandler);

        assertSame(mapLikeType, mapLikeTypeWithValueTypeHandler);
        assertEquals(valueTypeHandler, mapLikeTypeWithValueTypeHandler.getValueTypeHandler());
    }

    @Test
    public void testMapLikeTypeWithKeyType() {
        JavaType listType = TypeFactory.defaultInstance().constructType(Map.class, String.class, Integer.class);
        MapLikeType mapLikeType = MapLikeType.upgradeFrom(listType, String.class, Integer.class);

        JavaType newKeyType = TypeFactory.defaultInstance().constructType(String.class);
        MapLikeType mapLikeTypeWithNewKeyType = mapLikeType.withKeyType(newKeyType);

        assertNotSame(mapLikeType, mapLikeTypeWithNewKeyType);
        assertNotSame(mapLikeType.getKeyType(), mapLikeTypeWithNewKeyType.getKeyType());
        assertEquals(newKeyType, mapLikeTypeWithNewKeyType.getKeyType());
    }

    @Test
    public void testMapLikeTypeWithValueType() {
        JavaType listType = TypeFactory.defaultInstance().constructType(Map.class, String.class, Integer.class);
        MapLikeType mapLikeType = MapLikeType.upgradeFrom(listType, String.class, Integer.class);

        JavaType newValueType = TypeFactory.defaultInstance().constructType(Integer.class, Double.class);
        MapLikeType mapLikeTypeWithNewValueType = mapLikeType.withContentType(newValueType);

        assertNotSame(mapLikeType, mapLikeTypeWithNewValueType);
        assertNotSame(mapLikeType.getValueType(), mapLikeTypeWithNewValueType.getValueType());
        assertEquals(newValueType, mapLikeTypeWithNewValueType.getValueType());
    }

    @Test
    public void testMapLikeTypeWithTypeHandler() {
        JavaType listType = TypeFactory.defaultInstance().constructType(Map.class, String.class, Integer.class);
        MapLikeType mapLikeType = MapLikeType.upgradeFrom(listType, String.class, Integer.class);

        Object typeHandler = new Object() {};
        MapLikeType mapLikeTypeWithTypeHandler = mapLikeType.withTypeHandler(typeHandler);

        assertSame(mapLikeType, mapLikeTypeWithTypeHandler);
        assertEquals(typeHandler, mapLikeTypeWithTypeHandler.getTypeHandler());
    }

    @Test
    public void testMapLikeTypeWithContentTypeHandler() {
        JavaType listType = TypeFactory.defaultInstance().constructType(Map.class, String.class, Integer.class);
        MapLikeType mapLikeType = MapLikeType.upgradeFrom(listType, String.class, Integer.class);

        Object contentTypeHandler = new Object() {};
        MapLikeType mapLikeTypeWithContentTypeHandler = mapLikeType.withContentTypeHandler(contentTypeHandler);

        assertSame(mapLikeType, mapLikeTypeWithContentTypeHandler);
        assertEquals(contentTypeHandler, mapLikeTypeWithContentTypeHandler.getContentTypeHandler());
    }

    @Test
    public void testMapLikeTypeWithValueHandler() {
        JavaType listType = TypeFactory.defaultInstance().constructType(Map.class, String.class, Integer.class);
        MapLikeType mapLikeType = MapLikeType.upgradeFrom(listType, String.class, Integer.class);

        Object valueHandler = new Object() {};
        MapLikeType mapLikeTypeWithValueHandler = mapLikeType.withValueHandler(valueHandler);

        assertSame(mapLikeType, mapLikeTypeWithValueHandler);
        assertEquals(valueHandler, mapLikeTypeWithValueHandler.getValueHandler());
    }

    @Test
    public void testMapLikeTypeWithContentValueHandler() {
        JavaType listType = TypeFactory.defaultInstance().constructType(Map.class, String.class, Integer.class);
        MapLikeType mapLikeType = MapLikeType.upgradeFrom(listType, String.class, Integer.class);

        Object contentValueHandler = new Object() {};
        MapLikeType mapLikeTypeWithContentValueHandler = mapLikeType.withContentValueHandler(contentValueHandler);

        assertSame(mapLikeType, mapLikeTypeWithContentValueHandler);
        assertEquals(contentValueHandler, mapLikeTypeWithContentValueHandler.getContentValueHandler());
    }

    @Test
    public void testMapLikeTypeWithStaticTyping() {
        JavaType listType = TypeFactory.defaultInstance().constructType(Map.class, String.class, Integer.class);
        MapLikeType mapLikeType = MapLikeType.upgradeFrom(listType, String.class, Integer.class);

        MapLikeType mapLikeTypeWithStaticTyping = mapLikeType.withStaticTyping();

        assertTrue(mapLikeTypeWithStaticTyping.isStatic());
        assertFalse(mapLikeType.isStatic());
    }
}