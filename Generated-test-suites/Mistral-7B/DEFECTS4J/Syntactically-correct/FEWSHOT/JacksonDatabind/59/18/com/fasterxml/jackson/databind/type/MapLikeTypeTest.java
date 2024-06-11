package org.apache.commons.cli.test;

import static org.junit.Assert.*;

import org.junit.Test;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.MapLikeType;

public class MapLikeTypeTest {

    @Test
    public void testIsContainerType() {
        JavaType javaType = new MapLikeType.MapLikeTypeBuilder<String, Integer>()
                .build();
        assertTrue(javaType.isContainerType());
    }

    @Test
    public void testIsMapLikeType() {
        JavaType javaType = new MapLikeType.MapLikeTypeBuilder<String, Integer>()
                .build();
        assertTrue(javaType.isMapLikeType());
    }

    @Test
    public void testGetKeyType() {
        JavaType javaType = new MapLikeType.MapLikeTypeBuilder<String, Integer>()
                .build();
        JavaType keyType = javaType.getKeyType();
        assertEquals(String.class, keyType.getRawClass());
    }

    @Test
    public void testGetContentType() {
        JavaType javaType = new MapLikeType.MapLikeTypeBuilder<String, Integer>()
                .build();
        JavaType valueType = javaType.getContentType();
        assertEquals(Integer.class, valueType.getRawClass());
    }

    @Test
    public void testGetContentValueHandler() {
        JavaType javaType = new MapLikeType.MapLikeTypeBuilder<String, Integer>()
                .build();
        Object handler = javaType.getContentValueHandler();
        assertNull(handler);
    }

    @Test
    public void testGetContentTypeHandler() {
        JavaType javaType = new MapLikeType.MapLikeTypeBuilder<String, Integer>()
                .build();
        Object handler = javaType.getContentTypeHandler();
        assertNull(handler);
    }

    @Test
    public void testHasHandlers() {
        JavaType javaType = new MapLikeType.MapLikeTypeBuilder<String, Integer>()
                .build();
        assertFalse(javaType.hasHandlers());
    }

    @Test
    public void testIsTrueMapType() {
        JavaType javaType = new MapLikeType.MapLikeTypeBuilder<>(Map.class,
                String.class, Integer.class)
                .build();
        assertTrue(javaType.isTrueMapType());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructWithNonTypeBase() {
        JavaType javaType = new MapLikeType(Object.class, null, null, null,
                null, null);
    }

    @Test
    public void testUpgradeFrom() {
        JavaType baseType = new TypeFactory().constructType(Map.class);
        JavaType keyType = new TypeFactory().constructType(String.class);
        JavaType valueType = new TypeFactory().constructType(Integer.class);
        JavaType mapLikeType = MapLikeType.upgradeFrom(baseType, keyType,
                valueType);
        assertEquals(MapLikeType.class, mapLikeType.getClass());
    }

    @Test
    public void testWithKeyType() {
        JavaType javaType = new MapLikeType.MapLikeTypeBuilder<String, Integer>()
                .build();
        JavaType newKeyType = new TypeFactory().constructType(String[].class);
        JavaType newJavaType = javaType.withKeyType(newKeyType);
        assertEquals(newKeyType, newJavaType.getKeyType());
    }

    @Test
    public void testWithContentType() {
        JavaType javaType = new MapLikeType.MapLikeTypeBuilder<String, Integer>()
                .build();
        JavaType newValueType = new TypeFactory().constructType(Double.class);
        JavaType newJavaType = javaType.withContentType(newValueType);
        assertEquals(newValueType, newJavaType.getContentType());
    }

    @Test
    public void testWithTypeHandler() {
        JavaType javaType = new MapLikeType.MapLikeTypeBuilder<String, Integer>()
                .build();
        Object handler = new Object() {
        };
        JavaType newJavaType = javaType.withTypeHandler(handler);
        assertEquals(handler, newJavaType.getTypeHandler());
    }

    @Test
    public void testWithContentTypeHandler() {
        JavaType javaType = new MapLikeType.MapLikeTypeBuilder<String, Integer>()
                .build();
        Object handler = new Object() {
        };
        JavaType newJavaType = javaType.withContentTypeHandler(handler);
        assertEquals(handler, newJavaType.getContentTypeHandler());
    }

    @Test
    public void testWithValueHandler() {
        JavaType javaType = new MapLikeType.MapLikeTypeBuilder<String, Integer>()
                .build();
        Object handler = new Object() {
        };
        JavaType newJavaType = javaType.withValueHandler(handler);
        assertEquals(handler, newJavaType.getValueHandler());
    }

    @Test
    public void testWithContentValueHandler() {
        JavaType javaType = new MapLikeType.MapLikeTypeBuilder<String, Integer>()
                .build();
        Object handler = new Object() {
        };
        JavaType newJavaType = javaType.withContentValueHandler(handler);
        assertEquals(handler, newJavaType.getContentValueHandler());
    }

    @Test
    public void testWithStaticTyping() {
        JavaType javaType = new MapLikeType.MapLikeTypeBuilder<String, Integer>()
                .build();
        JavaType newJavaType = javaType.withStaticTyping();
        assertTrue(newJavaType.isStatic());
    }

    @Test
    public void testRefine() {
        JavaType baseType = new TypeFactory().constructType(HashMap.class);
        JavaType newJavaType = baseType.refine(HashMap.class, null, null, null);
        assertEquals(baseType.getClass(), newJavaType.getClass());
    }

    @Test
    public void testBuildCanonicalName() {
        JavaType javaType = new MapLikeType.MapLikeTypeBuilder<String, Integer>()
                .build();
        String canonicalName = javaType.buildCanonicalName();
        assertTrue(canonicalName.contains("com.fasterxml.jackson.databind.type.MapLikeType"));
        assertTrue(canonicalName.contains("java.lang.String"));
        assertTrue(canonicalName.contains("java.lang.Integer"));
    }
}