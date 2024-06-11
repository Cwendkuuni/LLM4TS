package com.yourpackage.test;

import static org.junit.Assert.*;

import org.junit.Test;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.MapLikeType;

public class MapLikeTypeTest {

    // Basic test cases for constructors

    @Test
    public void testMapLikeTypeConstructorWithClassMapType() {
        JavaType keyType = new SimpleJavaType(String.class);
        JavaType valueType = new SimpleJavaType(Integer.class);
        MapLikeType mapLikeType = new MapLikeType(Map.class, null, null, null,
                keyType, valueType);
        assertEquals(Map.class, mapLikeType.getRawClass());
        assertEquals(keyType, mapLikeType.getKeyType());
        assertEquals(valueType, mapLikeType.getValueType());
    }

    @Test
    public void testMapLikeTypeConstructorWithTypeBindings() {
        JavaType keyType = new SimpleJavaType(String.class);
        JavaType valueType = new SimpleJavaType(Integer.class);
        TypeBindings bindings = TypeBindings.emptyBindings();
        MapLikeType mapLikeType = new MapLikeType(null, bindings,
                _bogusSuperClass(Map.class), null, keyType, valueType, null, null,
                false);
        assertEquals(Map.class, mapLikeType.getRawClass());
        assertEquals(keyType, mapLikeType.getKeyType());
        assertEquals(valueType, mapLikeType.getValueType());
    }

    // Basic test cases for methods

    @Test
    public void testUpgradeFromJavaType() {
        JavaType baseType = new SimpleJavaType(HashMap.class);
        JavaType keyType = new SimpleJavaType(String.class);
        JavaType valueType = new SimpleJavaType(Integer.class);
        MapLikeType mapLikeType = MapLikeType.upgradeFrom(baseType, keyType,
                valueType);
        assertEquals(Map.class, mapLikeType.getRawClass());
        assertEquals(keyType, mapLikeType.getKeyType());
        assertEquals(valueType, mapLikeType.getValueType());
    }

    @Test
    public void testNarrowMethod() {
        JavaType subclass = new SimpleJavaType(HashMap.class);
        MapLikeType mapLikeType = new MapLikeType(HashMap.class, null,
                null, null, new SimpleJavaType(String.class),
                new SimpleJavaType(Integer.class), null, null, false);
        MapLikeType narrowedType = mapLikeType.narrow(subclass);
        assertEquals(subclass, narrowedType.getRawClass());
        assertEquals(mapLikeType.getKeyType(), narrowedType.getKeyType());
        assertEquals(mapLikeType.getValueType(), narrowedType.getValueType());
    }

    // Test cases for factories

    @Test(expected = IllegalArgumentException.class)
    public void testUpgradeFromInvalidBaseType() {
        JavaType baseType = new SimpleJavaType(String.class);
        JavaType keyType = new SimpleJavaType(String.class);
        JavaType valueType = new SimpleJavaType(Integer.class);
        MapLikeType.upgradeFrom(baseType, keyType, valueType);
    }

    // Test cases for methods with parameters

    @Test
    public void testWithKeyType() {
        JavaType keyType = new SimpleJavaType(String.class);
        JavaType valueType = new SimpleJavaType(Integer.class);
        MapLikeType mapLikeType = new MapLikeType(Map.class, null, null, null,
                keyType, valueType);
        MapLikeType withKeyType = mapLikeType.withKeyType(new SimpleJavaType(Double.class));
        assertEquals(mapLikeType.getRawClass(), withKeyType.getRawClass());
        assertNotEquals(keyType, withKeyType.getKeyType());
        assertEquals(valueType, withKeyType.getValueType());
    }

    @Test
    public void testWithContentType() {
        JavaType keyType = new SimpleJavaType(String.class);
        JavaType valueType = new SimpleJavaType(Integer.class);
        JavaType contentType = new SimpleJavaType(Long.class);
        MapLikeType mapLikeType = new MapLikeType(Map.class, null, null, null,
                keyType, valueType);
        MapLikeType withContentType = mapLikeType.withContentType(contentType);
        assertEquals(mapLikeType.getRawClass(), withContentType.getRawClass());
        assertEquals(keyType, withContentType.getKeyType());
        assertNotEquals(valueType, withContentType.getValueType());
        assertEquals(contentType, withContentType.getValueType());
    }

    @Test
    public void testWithTypeHandler() {
        JavaType keyType = new SimpleJavaType(String.class);
        JavaType valueType = new SimpleJavaType(Integer.class);
        Object handler = new Object();
        MapLikeType mapLikeType = new MapLikeType(Map.class, null, null, null,
                keyType, valueType);
        MapLikeType withTypeHandler = mapLikeType.withTypeHandler(handler);
        assertEquals(mapLikeType.getRawClass(), withTypeHandler.getRawClass());
        assertEquals(keyType, withTypeHandler.getKeyType());
        assertEquals(valueType, withTypeHandler.getValueType());
        assertEquals(handler, withTypeHandler.getTypeHandler());
    }

    @Test
    public void testWithContentTypeHandler() {
        JavaType keyType = new SimpleJavaType(String.class);
        JavaType valueType = new SimpleJavaType(Integer.class);
        Object handler = new Object();
        MapLikeType mapLikeType = new MapLikeType(Map.class, null, null, null,
                keyType, valueType);
        MapLikeType withContentTypeHandler = mapLikeType
                .withContentTypeHandler(handler);
        assertEquals(mapLikeType.getRawClass(), withContentTypeHandler.getRawClass());
        assertEquals(keyType, withContentTypeHandler.getKeyType());
        assertEquals(valueType, withContentTypeHandler.getValueType());
        assertEquals(handler, withContentTypeHandler.getContentTypeHandler());
    }

    @Test
    public void testWithValueHandler() {
        JavaType keyType = new SimpleJavaType(String.class);
        JavaType valueType = new SimpleJavaType(Integer.class);
        Object handler = new Object();
        MapLikeType mapLikeType = new MapLikeType(Map.class, null, null, null,
                keyType, valueType);
        MapLikeType withValueHandler = mapLikeType.withValueHandler(handler);
        assertEquals(mapLikeType.getRawClass(), withValueHandler.getRawClass());
        assertEquals(keyType, withValueHandler.getKeyType());
        assertEquals(valueType, withValueHandler.getValueType());
        assertEquals(handler, withValueHandler.getValueHandler());
    }

    @Test
    public void testWithContentValueHandler() {
        JavaType keyType = new SimpleJavaType(String.class);
        JavaType valueType = new SimpleJavaType(Integer.class);
        Object handler = new Object();
        MapLikeType mapLikeType = new MapLikeType(Map.class, null, null, null,
                keyType, valueType);
        MapLikeType withContentValueHandler = mapLikeType
                .withContentValueHandler(handler);
        assertEquals(mapLikeType.getRawClass(), withContentValueHandler.getRawClass());
        assertEquals(keyType, withContentValueHandler.getKeyType());
        assertEquals(valueType, withContentValueHandler.getValueType());
        assertEquals(handler, withContentValueHandler.getContentValueHandler());
    }

    @Test
    public void testWithStaticTyping() {
        JavaType keyType = new SimpleJavaType(String.class);
        JavaType valueType = new SimpleJavaType(Integer.class);
        MapLikeType mapLikeType = new MapLikeType(Map.class, null, null, null,
                keyType, valueType, false);
        MapLikeType withStaticTyping = mapLikeType.withStaticTyping();
        assertEquals(mapLikeType.getRawClass(), withStaticTyping.getRawClass());
        assertEquals(true, withStaticTyping.isStaticTyping());
    }

    // Edge cases and exception handling test cases

    @Test(expected = NullPointerException.class)
    public void testWithNullBindings() {
        JavaType keyType = new SimpleJavaType(String.class);
        JavaType valueType = new SimpleJavaType(Integer.class);
        MapLikeType mapLikeType = new MapLikeType(Map.class, null, null, null,
                keyType, valueType);
        mapLikeType.withNullBindings();
    }

    @Test(expected = IllegalArgumentException.class)
    public void testWithNegativeIntArray() {
        JavaType keyType = new SimpleJavaType(String.class);
        JavaType valueType = new SimpleJavaType(Integer.class);
        MapLikeType mapLikeType = new MapLikeType(Map.class, null, null, null,
                keyType, valueType);
        mapLikeType.withNegativeIntArray();
    }

    @Test(expected = IllegalArgumentException.class)
    public void testWithNullSuperClass() {
        JavaType keyType = new SimpleJavaType(String.class);
        JavaType valueType = new SimpleJavaType(Integer.class);
        MapLikeType mapLikeType = new MapLikeType(Map.class, null, null, null,
                keyType, valueType);
        mapLikeType.withNullSuperClass();
    }

    @Test(expected = IllegalArgumentException.class)
    public void testWithNullSuperInterfaces() {
        JavaType keyType = new SimpleJavaType(String.class);
        JavaType valueType = new SimpleJavaType(Integer.class);
        MapLikeType mapLikeType = new MapLikeType(Map.class, null, null, null,
                keyType, valueType);
        mapLikeType.withNullSuperInterfaces();
    }

    @Test(expected = NullPointerException.class)
    public void testWithNullKeyType() {
        JavaType valueType = new SimpleJavaType(Integer.class);
        MapLikeType mapLikeType = new MapLikeType(Map.class, null, null, null,
                null, valueType);
        mapLikeType.withNullKeyType();
    }

    @Test(expected = NullPointerException.class)
    public void testWithNullValueType() {
        JavaType keyType = new SimpleJavaType(String.class);
        MapLikeType mapLikeType = new MapLikeType(Map.class, null, null, null,
                keyType, null);
        mapLikeType.withNullValueType();
    }

    @Test(expected = IllegalArgumentException.class)
    public void testWithNullValueHandler() {
        JavaType keyType = new SimpleJavaType(String.class);
        JavaType valueType = new SimpleJavaType(Integer.class);
        MapLikeType mapLikeType = new MapLikeType(Map.class, null, null, null,
                keyType, valueType);
        mapLikeType.withNullValueHandler();
    }

    @Test(expected = IllegalArgumentException.class)
    public void testWithNullTypeHandler() {
        JavaType keyType = new SimpleJavaType(String.class);
        JavaType valueType = new SimpleJavaType(Integer.class);
        MapLikeType mapLikeType = new MapLikeType(Map.class, null, null, null,
                keyType, valueType);
        mapLikeType.withNullTypeHandler();
    }

    // Test case for isTrueMapType method

    @Test
    public void testIsTrueMapTypeWithHashMapClass() {
        JavaType keyType = new SimpleJavaType(String.class);
        JavaType valueType = new SimpleJavaType(Integer.class);
        MapLikeType mapLikeType = new MapLikeType(Map.class, null, null, null,
                keyType, valueType);
        assertTrue(mapLikeType.isTrueMapType());
    }

    @Test
    public void testIsTrueMapTypeWithCustomClassNotImplementingMapInterface() {
        Class<?> customClass = new SimpleClassWithNoMapImplementation();
        JavaType keyType = new SimpleJavaType(String.class);
        JavaType valueType = new SimpleJavaType(Integer.class);
        MapLikeType mapLikeType = new MapLikeType(customClass,
                null, null, null, keyType, valueType);
        assertFalse(mapLikeType.isTrueMapType());
    }
}

class SimpleClassWithNoMapImplementation {
    // A custom class that does not implement Map interface
}

class SimpleJavaType extends JavaType {
    // A helper class for creating JavaTypes with simple types
}