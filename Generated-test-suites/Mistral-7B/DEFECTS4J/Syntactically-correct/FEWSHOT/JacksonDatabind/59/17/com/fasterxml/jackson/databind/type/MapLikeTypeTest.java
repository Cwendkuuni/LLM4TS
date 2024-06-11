package com.fasterxml.jackson.databind.type;

import static org.junit.Assert.*;

import org.junit.Test;

public class MapLikeTypeTest {

    @Test
    public void testIsContainerType() {
        JavaType mapType = MapLikeType.construct(Map.class, String.class, Integer.class);
        assertTrue(mapType.isContainerType());
    }

    @Test
    public void testIsMapLikeType() {
        JavaType mapType = MapLikeType.construct(Map.class, String.class, Integer.class);
        assertTrue(mapType.isMapLikeType());
    }

    @Test
    public void testGetKeyType() {
        JavaType mapType = MapLikeType.construct(Map.class, String.class, Integer.class);
        JavaType keyType = mapType.getKeyType();
        assertEquals(String.class, keyType.getRawClass());
    }

    @Test
    public void testGetContentType() {
        JavaType mapType = MapLikeType.construct(Map.class, String.class, Integer.class);
        JavaType valueType = mapType.getContentType();
        assertEquals(Integer.class, valueType.getRawClass());
    }

    @Test
    public void testGetContentValueHandler() {
        JavaType mapType = MapLikeType.construct(Map.class, String.class, Integer.class);
        Object valueHandler = mapType.getContentValueHandler();
        assertNull(valueHandler);
    }

    @Test
    public void testGetContentTypeHandler() {
        JavaType mapType = MapLikeType.construct(Map.class, String.class, Integer.class);
        Object typeHandler = mapType.getContentTypeHandler();
        assertNull(typeHandler);
    }

    @Test
    public void testHasHandlers() {
        JavaType mapType = MapLikeType.construct(Map.class, String.class, Integer.class);
        boolean hasHandlers = mapType.hasHandlers();
        assertFalse(hasHandlers);
    }

    @Test
    public void testIsTrueMapType() {
        JavaType mapType = MapLikeType.construct(Map.class, String.class, Integer.class);
        assertTrue(mapType.isTrueMapType());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructInvalidBaseType() {
        JavaType baseType = TypeFactory.defaultInstance().constructType(List.class);
        MapLikeType.construct(baseType, String.class, Integer.class);
    }

    @Test
    public void testUpgradeFrom() {
        JavaType listType = TypeFactory.defaultInstance().constructType(List.class);
        JavaType keyType = String.class;
        JavaType valueType = Integer.class;
        JavaType mapType = MapLikeType.upgradeFrom(listType, keyType, valueType);
        assertEquals(Map.class, mapType.getRawClass());
    }

    @Test
    public void testWithKeyType() {
        JavaType mapType = MapLikeType.construct(Map.class, String.class, Integer.class);
        JavaType newKeyType = TypeFactory.defaultInstance().constructType(Long.class);
        JavaType newMapType = mapType.withKeyType(newKeyType);
        assertEquals(newKeyType, newMapType.getKeyType());
        assertEquals(Integer.class, newMapType.getContentType().getRawClass());
    }

    @Test
    public void testWithContentType() {
        JavaType mapType = MapLikeType.construct(Map.class, String.class, Integer.class);
        JavaType newValueType = TypeFactory.defaultInstance().constructType(Long.class);
        JavaType newMapType = mapType.withContentType(newValueType);
        assertEquals(Integer.class, mapType.getContentType().getRawClass());
        assertEquals(Long.class, newMapType.getContentType().getRawClass());
    }

    @Test
    public void testWithTypeHandler() {
        JavaType mapType = MapLikeType.construct(Map.class, String.class, Integer.class);
        Object handler = new Object() {
        };
        JavaType newMapType = mapType.withTypeHandler(handler);
        assertNotEquals(handler, newMapType.getTypeHandler());
    }

    @Test
    public void testWithContentTypeHandler() {
        JavaType mapType = MapLikeType.construct(Map.class, String.class, Integer.class);
        Object handler = new Object() {
        };
        JavaType newMapType = mapType.withContentTypeHandler(handler);
        assertNotEquals(handler, newMapType.getContentTypeHandler());
    }

    @Test
    public void testWithValueHandler() {
        JavaType mapType = MapLikeType.construct(Map.class, String.class, Integer.class);
        Object handler = new Object() {
        };
        JavaType newMapType = mapType.withValueHandler(handler);
        assertNotEquals(handler, newMapType.getValueHandler());
    }

    @Test
    public void testWithContentValueHandler() {
        JavaType mapType = MapLikeType.construct(Map.class, String.class, Integer.class);
        Object handler = new Object() {
        };
        JavaType newMapType = mapType.withContentValueHandler(handler);
        assertNotEquals(handler, newMapType.getContentValueHandler());
    }

    @Test
    public void testWithStaticTyping() {
        JavaType mapType = MapLikeType.construct(Map.class, String.class, Integer.class);
        JavaType newMapType = mapType.withStaticTyping();
        assertTrue(newMapType.isStatic());
    }

    @Test
    public void testRefine() {
        JavaType rawType = Map.class;
        TypeBindings bindings = TypeBindings.emptyBindings();
        JavaType superClass = Object.class;
        JavaType[] superInterfaces = new JavaType[0];
        JavaType mapType = new MapLikeType(rawType, bindings, superClass, superInterfaces,
                String.class, Integer.class, null, null, false);
        JavaType refinedType = mapType.refine(rawType, bindings, superClass, superInterfaces);
        assertEquals(mapType, refinedType);
    }

    @Test
    public void testBuildCanonicalName() {
        JavaType mapType = MapLikeType.construct(Map.class, String.class, Integer.class);
        String canonicalName = mapType.buildCanonicalName();
        String expected = "class java.util.Map<java.lang.String, java.lang.Integer>";
        assertEquals(expected, canonicalName);
    }
}