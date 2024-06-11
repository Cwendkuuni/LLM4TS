package com.fasterxml.jackson.databind.type;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import java.lang.reflect.TypeVariable;

public class MapLikeTypeTest {

    @Test
    public void testConstructor() {
        TypeBindings bindings = TypeBindings.create(HashMap.class, String.class, Integer.class);
        JavaType superClass = TypeFactory.defaultInstance().constructType(Map.class);
        JavaType[] superInts = new JavaType[0];
        JavaType keyType = TypeFactory.defaultInstance().constructType(String.class);
        JavaType valueType = TypeFactory.defaultInstance().constructType(Integer.class);
        MapLikeType mapLikeType = new MapLikeType(HashMap.class, bindings, superClass, superInts, keyType, valueType, null, null, false);

        assertEquals(HashMap.class, mapLikeType.getRawClass());
        assertEquals(bindings, mapLikeType.getBindings());
        assertEquals(superClass, mapLikeType.getSuperClass());
        assertEquals(superInts, mapLikeType.getInterfaces());
        assertEquals(keyType, mapLikeType.getKeyType());
        assertEquals(valueType, mapLikeType.getValueType());
    }

    @Test
    public void testWithKeyType() {
        JavaType keyType = TypeFactory.defaultInstance().constructType(String.class);
        JavaType valueType = TypeFactory.defaultInstance().constructType(Integer.class);
        MapLikeType mapLikeType = new MapLikeType(HashMap.class, TypeBindings.emptyBindings(), null, null, keyType, valueType, null, null, false);

        MapLikeType updatedType = mapLikeType.withKeyType(TypeFactory.defaultInstance().constructType(Double.class));
        assertEquals(HashMap.class, updatedType.getRawClass());
        assertEquals(keyType, mapLikeType.getKeyType());
        assertEquals(valueType, mapLikeType.getValueType());
        assertEquals(Double.class, updatedType.getKeyType().getRawClass());
        assertEquals(valueType, updatedType.getValueType());
    }

    @Test
    public void testWithContentType() {
        JavaType keyType = TypeFactory.defaultInstance().constructType(String.class);
        JavaType valueType = TypeFactory.defaultInstance().constructType(Integer.class);
        MapLikeType mapLikeType = new MapLikeType(HashMap.class, TypeBindings.emptyBindings(), null, null, keyType, valueType, null, null, false);

        MapLikeType updatedType = mapLikeType.withContentType(TypeFactory.defaultInstance().constructType(Double.class));
        assertEquals(HashMap.class, updatedType.getRawClass());
        assertEquals(keyType, mapLikeType.getKeyType());
        assertEquals(valueType, mapLikeType.getValueType());
        assertEquals(valueType, mapLikeType.getValueType());
        assertEquals(Double.class, updatedType.getValueType().getRawClass());
    }

    @Test
    public void testWithKeyTypeHandler() {
        JavaType keyType = TypeFactory.defaultInstance().constructType(String.class).withTypeHandler(new CustomTypeHandler());
        JavaType valueType = TypeFactory.defaultInstance().constructType(Integer.class);
        MapLikeType mapLikeType = new MapLikeType(HashMap.class, TypeBindings.emptyBindings(), null, null, keyType, valueType, null, null, false);

        MapLikeType updatedType = mapLikeType.withKeyTypeHandler(new CustomTypeHandler());
        assertEquals(HashMap.class, updatedType.getRawClass());
        assertEquals(keyType, mapLikeType.getKeyType());
        assertEquals(valueType, mapLikeType.getValueType());
        assertEquals(CustomTypeHandler.class, updatedType.getKeyType().getTypeHandler().getClass());
        assertEquals(valueType, updatedType.getValueType());
    }

    @Test
    public void testWithKeyValueHandler() {
        JavaType keyType = TypeFactory.defaultInstance().constructType(String.class);
        JavaType valueType = TypeFactory.defaultInstance().constructType(Integer.class).withValueHandler(new CustomValueHandler());
        MapLikeType mapLikeType = new MapLikeType(HashMap.class, TypeBindings.emptyBindings(), null, null, keyType, valueType, null, null, false);

        MapLikeType updatedType = mapLikeType.withKeyValueHandler(new CustomValueHandler());
        assertEquals(HashMap.class, updatedType.getRawClass());
        assertEquals(keyType, mapLikeType.getKeyType());
        assertEquals(valueType, mapLikeType.getValueType());
        assertEquals(valueType, mapLikeType.getValueType());
        assertEquals(CustomValueHandler.class, updatedType.getValueType().getValueHandler().getClass());
    }

    @Test
    public void testIsTrueMapType() {
        JavaType keyType = TypeFactory.defaultInstance().constructType(String.class);
        JavaType valueType = TypeFactory.defaultInstance().constructType(Integer.class);
        MapLikeType mapLikeType = new MapLikeType(HashMap.class, TypeBindings.emptyBindings(), null, null, keyType, valueType, null, null, false);

        assertTrue(mapLikeType.isTrueMapType());

        mapLikeType = new MapLikeType(ArrayList.class, TypeBindings.emptyBindings(), null, null, keyType, valueType, null, null, false);
        assertFalse(mapLikeType.isTrueMapType());
    }

    @Test
    public void testToString() {
        JavaType keyType = TypeFactory.defaultInstance().constructType(String.class);
        JavaType valueType = TypeFactory.defaultInstance().constructType(Integer.class);
        MapLikeType mapLikeType = new MapLikeType(HashMap.class, TypeBindings.emptyBindings(), null, null, keyType, valueType, null, null, false);

        assertEquals("[map-like type; class java.util.HashMap, class java.lang.String -> class java.lang.Integer]", mapLikeType.toString());
    }
}