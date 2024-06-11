package com.fasterxml.jackson.databind.type.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.TypeBindings;
import com.fasterxml.jackson.databind.TypeFactory;
import com.fasterxml.jackson.databind.type.MapLikeType;
import com.fasterxml.jackson.databind.type.TypeBase;
import com.fasterxml.jackson.databind.type.TypeDescriptor;
import com.fasterxml.jackson.databind.type.TypeModifier;

public class MapLikeTypeTest {

    @Test
    public void testMapLikeTypeCreation() {
        JavaType mapType = TypeFactory.defaultInstance().constructMapType(
                Map.class, String.class, Number.class);
        MapLikeType mapLikeType = MapLikeType.upgradeFrom(mapType, String.class,
                Number.class);
        assertEquals("java.util.Map<java.lang.String,java.lang.Number>", mapLikeType.toString());
    }

    @Test
    public void testMapLikeTypeCreationFromBase() {
        Class<Map<String, Number>> rawType = Map.class;
        JavaType keyType = String.class;
        JavaType valueType = Number.class;
        TypeBindings bindings = TypeBindings.create(rawType, keyType, valueType);
        MapLikeType mapLikeType = new MapLikeType(rawType, bindings, _bogusSuperClass(rawType),
                null, keyType, valueType, null, null, false);
        assertEquals("java.util.Map<java.lang.String,java.lang.Number>", mapLikeType.toString());
    }

    @Test
    public void testMapLikeTypeWithKeyType() {
        JavaType mapType = TypeFactory.defaultInstance().constructMapType(
                Map.class, String.class, Number.class);
        JavaType keyType = Character.class;
        MapLikeType mapLikeType = new MapLikeType(mapType.getType(), keyType, valueType)
                .withKeyType(keyType);
        assertEquals("java.util.Map<java.lang.Character,java.lang.Number>", mapLikeType.toString());
    }

    @Test
    public void testMapLikeTypeWithValueType() {
        JavaType mapType = TypeFactory.defaultInstance().constructMapType(
                Map.class, String.class, Number.class);
        JavaType valueType = Boolean.TYPE;
        MapLikeType mapLikeType = new MapLikeType(mapType.getType(), keyType, valueType)
                .withContentType(valueType);
        assertEquals("java.util.Map<java.lang.String,java.lang.Boolean>", mapLikeType.toString());
    }

    @Test
    public void testMapLikeTypeWithTypeHandler() {
        JavaType mapType = TypeFactory.defaultInstance().constructMapType(
                Map.class, String.class, Number.class);
        Object handler = new Object() {};
        MapLikeType mapLikeType = new MapLikeType(mapType.getType(), keyType, valueType)
                .withTypeHandler(handler);
        assertNotNull(mapLikeType.getTypeHandler());
    }

    @Test
    public void testMapLikeTypeWithValueHandler() {
        JavaType mapType = TypeFactory.defaultInstance().constructMapType(
                Map.class, String.class, Number.class);
        Object handler = new Object() {};
        MapLikeType mapLikeType = new MapLikeType(mapType.getType(), keyType, valueType)
                .withValueHandler(handler);
        assertNotNull(mapLikeType.getValueHandler());
    }

    @Test
    public void testMapLikeTypeWithKeyValueHandler() {
        JavaType mapType = TypeFactory.defaultInstance().constructMapType(
                Map.class, String.class, Number.class);
        Object keyHandler = new Object() {};
        Object valueHandler = new Object() {};
        MapLikeType mapLikeType = new MapLikeType(mapType.getType(), keyType, valueType)
                .withKeyValueHandler(keyHandler, valueHandler);
        assertNotNull(mapLikeType.getKeyHandler());
        assertNotNull(mapLikeType.getValueHandler());
    }

    @Test
    public void testMapLikeTypeWithContentTypeHandler() {
        JavaType mapType = TypeFactory.defaultInstance().constructMapType(
                Map.class, String.class, Number.class);
        Object handler = new Object() {};
        JavaType valueTypeWithHandler = TypeModifier.constructType(Number.class, handler);
        MapLikeType mapLikeType = new MapLikeType(mapType.getType(), keyType, valueType)
                .withContentTypeHandler(handler);
        assertEquals(valueTypeWithHandler, mapLikeType.getContentType());
    }

    @Test
    public void testMapLikeTypeWithStaticTyping() {
        JavaType mapType = TypeFactory.defaultInstance().constructMapType(
                HashMap.class, String.class, Number.class);
        MapLikeType mapLikeType = new MapLikeType(mapType, keyType, valueType)
                .withStaticTyping();
        assertTrue(mapLikeType.isStaticTyping());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testMapLikeTypeUpgradeFromInvalidBaseType() {
        JavaType baseType = Integer.TYPE;
        JavaType keyType = String.class;
        JavaType valueType = Number.class;
        MapLikeType.upgradeFrom(baseType, keyType, valueType);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testMapLikeTypeConstructWithInvalidNumberOfTypeParameters() {
        Class<Map<String, Number, Integer, Boolean>> rawType = Map.class;
        JavaType keyType = String.class;
        JavaType valueType = Number.class;
        TypeBindings bindings = TypeBindings.create(rawType, keyType, valueType);
        MapLikeType mapLikeType = MapLikeType.construct(rawType, keyType, valueType);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testMapLikeTypeConstructWithInvalidKeyType() {
        Class<Map<String, Number, Integer>> rawType = Map.class;
        JavaType keyType = Integer.class;
        JavaType valueType = Number.class;
        TypeBindings bindings = TypeBindings.create(rawType, keyType, valueType);
        MapLikeType mapLikeType = MapLikeType.construct(rawType, keyType, valueType);
    }

    @Test
    public void testMapLikeTypeRefine() {
        JavaType mapType = TypeFactory.defaultInstance().constructMapType(
                Map.class, String.class, Number.class);
        JavaType keyType = Character.class;
        JavaType valueType = Boolean.TYPE;
        TypeBindings bindings = TypeBindings.create(mapType.getType(), keyType, valueType);
        MapLikeType mapLikeType = new MapLikeType(mapType.getType(), bindings, _bogusSuperClass(mapType.getType()),
                null, keyType, valueType, null, null, false);
        JavaType refinedType = mapLikeType.refine(Map.class, bindings, null, null);
        assertEquals("java.util.Map<java.lang.Character,java.lang.Boolean>", refinedType.toString());
    }

    @Test
    public void testMapLikeTypeWithEmptyTypeBindings() {
        Class<Map<String, Number>> rawType = Map.class;
        JavaType keyType = String.class;
        JavaType valueType = Number.class;
        TypeBindings bindings = TypeBindings.emptyBindings();
        MapLikeType mapLikeType = new MapLikeType(rawType, bindings, _bogusSuperClass(rawType),
                null, keyType, valueType, null, null, false);
        assertEquals("java.util.Map<java.lang.String,java.lang.Number>", mapLikeType.toString());
    }

    @Test
    public void testMapLikeTypeIsContainerType() {
        JavaType mapType = TypeFactory.defaultInstance().constructMapType(
                Map.class, String.class, Number.class);
        MapLikeType mapLikeType = new MapLikeType(mapType, keyType, valueType);
        assertTrue(mapLikeType.isContainerType());
    }

    @Test
    public void testMapLikeTypeIsMapLikeType() {
        JavaType mapType = TypeFactory.defaultInstance().constructMapType(
                Map.class, String.class, Number.class);
        MapLikeType mapLikeType = new MapLikeType(mapType, keyType, valueType);
        assertTrue(mapLikeType.isMapLikeType());
    }

    @Test
    public void testMapLikeTypeGetKeyType() {
        JavaType mapType = TypeFactory.defaultInstance().constructMapType(
                Map.class, String.class, Number.class);
        MapLikeType mapLikeType = new MapLikeType(mapType, keyType, valueType);
        assertEquals(keyType, mapLikeType.getKeyType());
    }

    @Test
    public void testMapLikeTypeGetContentType() {
        JavaType mapType = TypeFactory.defaultInstance().constructMapType(
                Map.class, String.class, Number.class);
        MapLikeType mapLikeType = new MapLikeType(mapType, keyType, valueType);
        assertEquals(valueType, mapLikeType.getContentType());
    }

    @Test
    public void testMapLikeTypeGetContentValueHandler() {
        JavaType mapType = TypeFactory.defaultInstance().constructMapType(
                Map.class, String.class, Number.class);
        Object valueHandler = new Object() {};
        MapLikeType mapLikeType = new MapLikeType(mapType, keyType, valueType)
                .withValueHandler(valueHandler);
        assertEquals(valueHandler, mapLikeType.getContentValueHandler());
    }

    @Test
    public void testMapLikeTypeGetContentTypeHandler() {
        JavaType mapType = TypeFactory.defaultInstance().constructMapType(
                Map.class, String.class, Number.class);
        Object typeHandler = new Object() {};
        MapLikeType mapLikeType = new MapLikeType(mapType, keyType, valueType)
                .withTypeHandler(typeHandler);
        assertEquals(typeHandler, mapLikeType.getTypeHandler());
    }

    @Test
    public void testMapLikeTypeHasHandlers() {
        JavaType mapType = TypeFactory.defaultInstance().constructMapType(
                Map.class, String.class, Number.class);
        MapLikeType mapLikeType = new MapLikeType(mapType, keyType, valueType)
                .withValueHandler(new Object() {})
                .withTypeHandler(new Object() {});
        assertTrue(mapLikeType.hasHandlers());
    }

    @Test
    public void testMapLikeTypeGetErasedSignature() {
        JavaType mapType = TypeFactory.defaultInstance().constructMapType(
                Map.class, String.class, Number.class);
        MapLikeType mapLikeType = new MapLikeType(mapType, keyType, valueType);
        assertEquals("[map-like type; class java.util.Map, java.lang.String -> java.lang.Number]", mapLikeType.getErasedSignature().toString());
    }

    @Test
    public void testMapLikeTypeGetGenericSignature() {
        JavaType mapType = TypeFactory.defaultInstance().constructMapType(
                Map.class, String.class, Number.class);
        MapLikeType mapLikeType = new MapLikeType(mapType, keyType, valueType);
        assertEquals("[map-like type; class java.util.Map<java.lang.String,java.lang.Number>]", mapLikeType.getGenericSignature().toString());
    }

    private static Class<?> _bogusSuperClass(Class<?> clazz) {
        return Object.class;
    }

    private static JavaType keyType = TypeFactory.defaultInstance().constructType(String.class);
    private static JavaType valueType = TypeFactory.defaultInstance().constructType(Number.class);
}