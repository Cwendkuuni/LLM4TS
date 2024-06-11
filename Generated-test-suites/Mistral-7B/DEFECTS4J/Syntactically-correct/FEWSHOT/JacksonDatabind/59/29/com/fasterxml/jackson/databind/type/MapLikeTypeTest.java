package com.fasterxml.jackson.databind.type;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.lang.reflect.TypeVariable;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

public class MapLikeTypeTest {

    @Test
    public void testIsContainerType() {
        JavaType mapType = new MapLikeType(Map.class, TypeBindings.emptyBindings(), null, null,
                String.class, Integer.class);
        assertTrue(mapType.isContainerType());

        JavaType listType = JavaType.constructRawListType(Integer.class);
        assertFalse(listType.isContainerType());
    }

    @Test
    public void testIsMapLikeType() {
        JavaType mapType = new MapLikeType(Map.class, TypeBindings.emptyBindings(), null, null,
                String.class, Integer.class);
        assertTrue(mapType.isMapLikeType());

        JavaType listType = JavaType.constructRawListType(Integer.class);
        assertFalse(listType.isMapLikeType());
    }

    @Test
    public void testGetKeyType() {
        JavaType mapType = new MapLikeType(Map.class, TypeBindings.emptyBindings(), null, null,
                String.class, Integer.class);
        assertEquals(String.class, mapType.getKeyType());
    }

    @Test
    public void testGetContentType() {
        JavaType mapType = new MapLikeType(Map.class, TypeBindings.emptyBindings(), null, null,
                String.class, Integer.class);
        assertEquals(Integer.class, mapType.getContentType());
    }

    @Test
    public void testGetContentValueHandler() {
        JavaType mapType = new MapLikeType(Map.class, TypeBindings.emptyBindings(), null, null,
                String.class, Integer.class);
        assertNull(mapType.getContentValueHandler());
    }

    @Test
    public void testGetContentTypeHandler() {
        JavaType mapType = new MapLikeType(Map.class, TypeBindings.emptyBindings(), null, null,
                String.class, Integer.class);
        assertNull(mapType.getContentTypeHandler());
    }

    @Test
    public void testHasHandlers() {
        JavaType mapType = new MapLikeType(Map.class, TypeBindings.emptyBindings(), null, null,
                String.class, Integer.class);
        assertFalse(mapType.hasHandlers());
    }

    @Test
    public void testIsTrueMapType() {
        JavaType mapType = new MapLikeType(Map.class, TypeBindings.emptyBindings(), null, null,
                String.class, Integer.class);
        assertTrue(mapType.isTrueMapType());

        JavaType listType = JavaType.constructRawListType(Integer.class);
        assertFalse(listType.isTrueMapType());
    }

    @Test
    public void testRefine() {
        JavaType baseType = JavaType.constructRawType(HashMap.class, new TypeVariable[]{
                new TypeVariable("K") { }, new TypeVariable("V") { }});
        JavaType mapType = new MapLikeType(baseType, TypeBindings.emptyBindings(), null, null,
                String.class, Integer.class);

        JavaType refinedType = mapType.refine(HashMap.class, TypeBindings.emptyBindings(),
                HashMap.class, new Class[]{});
        assertSame(mapType, refinedType);
    }

    @Test
    public void testUpgradeFrom() {
        JavaType listType = JavaType.constructRawListType(Integer.class);
        JavaType mapType = MapLikeType.upgradeFrom(listType, String.class, Integer.class);
        assertInstanceOf(MapLikeType.class, mapType);
    }

    @Test
    public void testConstruct() {
        Class<?> rawType = HashMap.class;
        JavaType mapType = MapLikeType.construct(rawType, String.class, Integer.class);
        assertInstanceOf(MapLikeType.class, mapType);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructWithInvalidBaseType() {
        JavaType baseType = JavaType.constructRawType(String.class);
        MapLikeType.construct(String.class, String.class, Integer.class);
    }

    @Test
    public void testNarrow() {
        JavaType mapType = new MapLikeType(HashMap.class, TypeBindings.emptyBindings(), null, null,
                String.class, Integer.class);
        JavaType narrowedType = mapType._narrow(HashMap.class);
        assertSame(mapType, narrowedType);
    }

    @Test
    public void testWithKeyType() {
        JavaType mapType = new MapLikeType(Map.class, TypeBindings.emptyBindings(), null, null,
                String.class, Integer.class);
        JavaType newKeyType = JavaType.constructRawType(Long.class);
        JavaType newMapType = mapType.withKeyType(newKeyType);
        assertEquals(mapType.getClass(), newMapType.getClass());
        assertEquals(newKeyType, newMapType.getKeyType());
        assertEquals(Integer.class, newMapType.getContentType());
    }

    @Test
    public void testWithContentType() {
        JavaType mapType = new MapLikeType(Map.class, TypeBindings.emptyBindings(), null, null,
                String.class, Integer.class);
        JavaType newValueType = JavaType.constructRawType(Float.class);
        JavaType newMapType = mapType.withContentType(newValueType);
        assertEquals(mapType.getClass(), newMapType.getClass());
        assertEquals(String.class, newMapType.getKeyType());
        assertEquals(newValueType, newMapType.getContentType());
    }

    @Test
    public void testWithTypeHandler() {
        JavaType mapType = new MapLikeType(Map.class, TypeBindings.emptyBindings(), null, null,
                String.class, Integer.class);
        Object handler = new Object(){};
        JavaType newMapType = mapType.withTypeHandler(handler);
        assertEquals(mapType.getClass(), newMapType.getClass());
        assertEquals(handler, newMapType.getTypeHandler());
    }

    @Test
    public void testWithContentTypeHandler() {
        JavaType mapType = new MapLikeType(Map.class, TypeBindings.emptyBindings(), null, null,
                String.class, Integer.class);
        Object handler = new Object(){};
        JavaType newMapType = mapType.withContentTypeHandler(handler);
        assertEquals(mapType.getClass(), newMapType.getClass());
        assertEquals(handler, newMapType.getContentTypeHandler());
    }

    @Test
    public void testWithValueHandler() {
        JavaType mapType = new MapLikeType(Map.class, TypeBindings.emptyBindings(), null, null,
                String.class, Integer.class);
        Object handler = new Object(){};
        JavaType newMapType = mapType.withValueHandler(handler);
        assertEquals(mapType.getClass(), newMapType.getClass());
        assertEquals(handler, newMapType.getValueHandler());
    }

    @Test
    public void testWithContentValueHandler() {
        JavaType mapType = new MapLikeType(Map.class, TypeBindings.emptyBindings(), null, null,
                String.class, Integer.class);
        Object handler = new Object(){};
        JavaType newMapType = mapType.withContentValueHandler(handler);
        assertEquals(mapType.getClass(), newMapType.getClass());
        assertEquals(handler, newMapType.getContentValueHandler());
    }

    @Test
    public void testWithStaticTyping() {
        JavaType mapType = new MapLikeType(Map.class, TypeBindings.emptyBindings(), null, null,
                String.class, Integer.class);
        JavaType newMapType = mapType.withStaticTyping();
        assertTrue(newMapType.isStaticTyping());
        assertEquals(mapType.getClass(), newMapType.getClass());
    }

    @Test
    public void testBuildCanonicalName() {
        JavaType mapType = new MapLikeType(Map.class, TypeBindings.emptyBindings(), null, null,
                String.class, Integer.class);
        assertEquals("[map-like type; class java.util.HashMap, Ljava/lang/String;->Ljava/lang/Integer;]",
                mapType.buildCanonicalName());
    }

    @Test
    public void testGetErasedSignature() {
        JavaType mapType = new MapLikeType(Map.class, TypeBindings.emptyBindings(), null, null,
                String.class, Integer.class);
        StringBuilder sb = new StringBuilder();
        mapType.getErasedSignature(sb);
        assertEquals("[map-like type; class java.util.Map, Ljava/lang/Object;->Ljava/lang/Object;]",
                sb.toString());
    }

    @Test
    public void testGetGenericSignature() {
        JavaType mapType = new MapLikeType(Map.class, TypeBindings.emptyBindings(), null, null,
                String.class, Integer.class);
        StringBuilder sb = new StringBuilder();
        mapType.getGenericSignature(sb);
        assertEquals("[map-like type; class java.util.Map, Ljava/lang/String;->Ljava/lang/Integer;]",
                sb.toString());
    }
}