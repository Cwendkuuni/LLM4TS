package com.fasterxml.jackson.databind.type.test;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.MapLikeType;
import com.fasterxml.jackson.databind.type.MapType;
import com.fasterxml.jackson.databind.type.TypeBindings;
import org.junit.Assert;
import org.junit.Test;

public class MapTypeTest {

    @Test
    public void testMapTypeConstructor() {
        Class<Map<String, Integer>> rawType = Map.class;
        TypeBindings bindings = TypeBindings.defaultBindings();
        JavaType keyType = TypeFactory.defaultInstance().constructType(String.class);
        JavaType valueType = TypeFactory.defaultInstance().constructType(Integer.class);

        MapType mapType = MapType.construct(rawType, bindings, null, null, keyType, valueType);

        Assert.assertEquals(Map.class, mapType.getRawClass());
        Assert.assertEquals(keyType, mapType.getKeyType());
        Assert.assertEquals(valueType, mapType.getValueType());
    }

    @Test
    public void testMapTypeConstructDeprecated() {
        Class<Map<String, Integer>> rawType = Map.class;
        JavaType keyType = TypeFactory.defaultInstance().constructType(String.class);
        JavaType valueType = TypeFactory.defaultInstance().constructType(Integer.class);

        MapType mapType = MapType.construct(rawType, keyType, valueType);

        Assert.assertEquals(Map.class, mapType.getRawClass());
        Assert.assertEquals(keyType, mapType.getKeyType());
        Assert.assertEquals(valueType, mapType.getValueType());
    }

    @Test
    public void testMapTypeNarrow() {
        Class<Map<String, Integer>> rawType = Map.class;
        Class<Map<String, Long>> subclass = Map.class;
        MapType mapType = MapType.construct(rawType, null, null, null, TypeFactory.defaultInstance().constructType(String.class), TypeFactory.defaultInstance().constructType(Integer.class));

        MapType narrowedType = mapType.narrow(subclass);

        Assert.assertEquals(subclass, narrowedType.getRawClass());
        Assert.assertEquals(mapType.getKeyType(), narrowedType.getKeyType());
        Assert.assertEquals(mapType.getValueType(), narrowedType.getValueType());
    }

    @Test
    public void testMapTypeWithTypeHandler() {
        Class<Map<String, Integer>> rawType = Map.class;
        JavaType keyType = TypeFactory.defaultInstance().constructType(String.class);
        JavaType valueType = TypeFactory.defaultInstance().constructType(Integer.class);
        Object handler = new Object();

        MapType mapType = new MapType(rawType, null, null, null, keyType, valueType);
        MapType withHandler = mapType.withTypeHandler(handler);

        Assert.assertNotSame(mapType, withHandler);
        Assert.assertEquals(handler, withHandler.getTypeHandler());
        Assert.assertEquals(mapType.getKeyType(), withHandler.getKeyType());
        Assert.assertEquals(mapType.getValueType(), withHandler.getValueType());
    }

    @Test
    public void testMapTypeWithContentTypeHandler() {
        Class<Map<String, Integer>> rawType = Map.class;
        JavaType keyType = TypeFactory.defaultInstance().constructType(String.class);
        JavaType valueType = TypeFactory.defaultInstance().constructType(Integer.class);
        Object contentHandler = new Object();

        MapType mapType = new MapType(rawType, null, null, null, keyType, valueType);
        MapType withContentHandler = mapType.withContentTypeHandler(contentHandler);

        Assert.assertNotSame(mapType, withContentHandler);
        Assert.assertEquals(mapType.getKeyType(), withContentHandler.getKeyType());
        Assert.assertEquals(contentHandler, withContentHandler.getContentTypeHandler());
        Assert.assertEquals(mapType.getValueType(), withContentHandler.getValueType());
    }

    @Test
    public void testMapTypeWithValueHandler() {
        Class<Map<String, Integer>> rawType = Map.class;
        JavaType keyType = TypeFactory.defaultInstance().constructType(String.class);
        JavaType valueType = TypeFactory.defaultInstance().constructType(Integer.class);
        Object handler = new Object();

        MapType mapType = new MapType(rawType, null, null, null, keyType, valueType);
        MapType withHandler = mapType.withValueHandler(handler);

        Assert.assertNotSame(mapType, withHandler);
        Assert.assertEquals(mapType.getKeyType(), withHandler.getKeyType());
        Assert.assertEquals(mapType.getValueType(), withHandler.getValueType());
        Assert.assertEquals(handler, withHandler.getValueHandler());
    }

    @Test
    public void testMapTypeWithContentValueHandler() {
        Class<Map<String, Integer>> rawType = Map.class;
        JavaType keyType = TypeFactory.defaultInstance().constructType(String.class);
        JavaType valueType = TypeFactory.defaultInstance().constructType(Integer.class);
        Object contentHandler = new Object();
        Object valueHandler = new Object();

        MapType mapType = new MapType(rawType, null, null, null, keyType, valueType);
        MapType withContentHandlerAndValueHandler = mapType.withContentValueHandler(contentHandler, valueHandler);

        Assert.assertNotSame(mapType, withContentHandlerAndValueHandler);
        Assert.assertEquals(mapType.getKeyType(), withContentHandlerAndValueHandler.getKeyType());
        Assert.assertEquals(contentHandler, withContentHandlerAndValueHandler.getContentValueHandler());
        Assert.assertEquals(mapType.getValueType(), withContentHandlerAndValueHandler.getValueType());
        Assert.assertEquals(valueHandler, withContentHandlerAndValueHandler.getValueHandler());
    }

    @Test
    public void testMapTypeWithStaticTyping() {
        Class<Map<String, Integer>> rawType = Map.class;
        JavaType keyType = TypeFactory.defaultInstance().constructType(String.class);
        JavaType valueType = TypeFactory.defaultInstance().constructType(Integer.class);

        MapType mapType = new MapType(rawType, null, null, null, keyType, valueType);
        MapType withStaticTyping = mapType.withStaticTyping();

        Assert.assertNotSame(mapType, withStaticTyping);
        Assert.assertTrue(withStaticTyping.isStatic());
        Assert.assertEquals(mapType.getKeyType(), withStaticTyping.getKeyType());
        Assert.assertEquals(mapType.getValueType(), withStaticTyping.getValueType());
    }

    @Test
    public void testMapTypeWithContentType() {
        Class<Map<String, Integer>> rawType = Map.class;
        JavaType keyType = TypeFactory.defaultInstance().constructType(String.class);
        JavaType contentType = TypeFactory.defaultInstance().constructType(String.class);
        JavaType valueType = TypeFactory.defaultInstance().constructType(Integer.class);

        MapType mapType = new MapType(rawType, null, null, null, keyType, valueType);
        MapType withContentType = mapType.withContentType(contentType);

        Assert.assertNotSame(mapType, withContentType);
        Assert.assertEquals(mapType.getKeyType(), withContentType.getKeyType());
        Assert.assertEquals(contentType, withContentType.getValueType());
        Assert.assertEquals(mapType.getValueType(), withContentType.getValueType());
    }

    @Test
    public void testMapTypeWithKeyType() {
        Class<Map<String, Integer>> rawType = Map.class;
        JavaType keyType1 = TypeFactory.defaultInstance().constructType(String.class);
        JavaType keyType2 = TypeFactory.defaultInstance().constructType(String[].class);
        JavaType valueType = TypeFactory.defaultInstance().constructType(Integer.class);

        MapType mapType = new MapType(rawType, null, null, null, keyType1, valueType);
        MapType withKeyType = mapType.withKeyType(keyType2);

        Assert.assertNotSame(mapType, withKeyType);
        Assert.assertEquals(mapType.getKeyType(), keyType1);
        Assert.assertEquals(withKeyType.getKeyType(), keyType2);
        Assert.assertEquals(mapType.getValueType(), withKeyType.getValueType());
    }

    @Test
    public void testMapTypeRefine() {
        Class<Map<String, Integer>> rawType = Map.class;
        TypeBindings bindings = TypeBindings.defaultBindings();
        JavaType keyType = TypeFactory.defaultInstance().constructType(String.class);
        JavaType valueType = TypeFactory.defaultInstance().constructType(Integer.class);

        MapType mapType = new MapType(rawType, bindings, null, null, keyType, valueType);
        MapType refinedType = mapType.refine(Map.class, null, null, null);

        Assert.assertNotSame(mapType, refinedType);
        Assert.assertEquals(Map.class, refinedType.getRawClass());
        Assert.assertEquals(keyType, refinedType.getKeyType());
        Assert.assertEquals(valueType, refinedType.getValueType());
    }

    @Test
    public void testMapTypeWithKeyTypeHandler() {
        Class<Map<String, Integer>> rawType = Map.class;
        JavaType keyType = TypeFactory.defaultInstance().constructType(String.class);
        JavaType valueType = TypeFactory.defaultInstance().constructType(Integer.class);
        Object handler = new Object();

        MapType mapType = new MapType(rawType, null, null, null, keyType, valueType);
        MapType withKeyTypeHandler = mapType.withKeyTypeHandler(handler);

        Assert.assertNotSame(mapType, withKeyTypeHandler);
        Assert.assertEquals(mapType.getKeyType(), withKeyTypeHandler.getKeyType());
        Assert.assertEquals(mapType.getValueType(), withKeyTypeHandler.getValueType());
        Assert.assertEquals(handler, withKeyTypeHandler.getKeyTypeHandler());
    }

    @Test
    public void testMapTypeWithKeyValueHandler() {
        Class<Map<String, Integer>> rawType = Map.class;
        JavaType keyType = TypeFactory.defaultInstance().constructType(String.class);
        JavaType valueType = TypeFactory.defaultInstance().constructType(Integer.class);
        Object handler = new Object();

        MapType mapType = new MapType(rawType, null, null, null, keyType, valueType);
        MapType withKeyValueHandler = mapType.withKeyValueHandler(handler);

        Assert.assertNotSame(mapType, withKeyValueHandler);
        Assert.assertEquals(mapType.getKeyType(), withKeyValueHandler.getKeyType());
        Assert.assertEquals(mapType.getValueType(), withKeyValueHandler.getValueType());
        Assert.assertEquals(handler, withKeyValueHandler.getKeyValueHandler());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testMapTypeNarrowNullSuperclass() {
        Class<Map<String, Integer>> rawType = Map.class;
        MapType mapType = new MapType(rawType, null, null, null, TypeFactory.defaultInstance().constructType(String.class), TypeFactory.defaultInstance().constructType(Integer.class));
        mapType.narrow(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testMapTypeNarrowNullBindings() {
        Class<Map<String, Integer>> rawType = Map.class;
        MapType mapType = new MapType(rawType, null, null, null, TypeFactory.defaultInstance().constructType(String.class), TypeFactory.defaultInstance().constructType(Integer.class));
        mapType.narrow(Map.class, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testMapTypeNarrowNullKeyType() {
        Class<Map<String, Integer>> rawType = Map.class;
        MapType mapType = new MapType(rawType, null, null, null, null, TypeFactory.defaultInstance().constructType(Integer.class));
        mapType.narrow(Map.class, null, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testMapTypeNarrowNullValueType() {
        Class<Map<String, Integer>> rawType = Map.class;
        MapType mapType = new MapType(rawType, null, null, null, TypeFactory.defaultInstance().constructType(String.class), null);
        mapType.narrow(Map.class, null, null);
    }
}