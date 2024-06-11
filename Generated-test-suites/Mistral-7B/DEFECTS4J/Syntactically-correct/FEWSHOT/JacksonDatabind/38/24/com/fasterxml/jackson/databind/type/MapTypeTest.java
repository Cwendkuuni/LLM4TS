package com.fasterxml.jackson.databind.type;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class MapTypeTest {

    @Test
    public void testConstruct() {
        JavaType intType = TypeFactory.defaultInstance().constructType(int.class);
        JavaType stringType = TypeFactory.defaultInstance().constructType(String.class);
        TypeBindings bindings = new TypeBindings();
        Class<?> rawType = Map.class;

        MapType mapType = MapType.construct(rawType, bindings, null, null, intType, stringType);
        assertEquals(Map.class, mapType.getRawClass());
        assertEquals(intType, mapType.getKeyType());
        assertEquals(stringType, mapType.getValueType());
        assertNull(mapType.getValueHandler());
        assertNull(mapType.getTypeHandler());
        assertFalse(mapType.isStatic());
    }

    @Test
    public void testConstructDeprecated() {
        JavaType intType = TypeFactory.defaultInstance().constructType(int.class);
        JavaType stringType = TypeFactory.defaultInstance().constructType(String.class);

        Class<?> rawType = Map.class;
        MapType mapType = MapType.construct(rawType, intType, stringType);

        assertEquals(Map.class, mapType.getRawClass());
        assertEquals(intType, mapType.getKeyType());
        assertEquals(stringType, mapType.getValueType());
        assertNull(mapType.getValueHandler());
        assertNull(mapType.getTypeHandler());
        assertFalse(mapType.isStatic());
    }

    @Test
    public void testNarrow() {
        JavaType intType = TypeFactory.defaultInstance().constructType(int.class);
        JavaType stringType = TypeFactory.defaultInstance().constructType(String.class);
        JavaType subclass = HashMap.class;

        MapType mapType = new MapType(Map.class, null, null, null, intType, stringType);

        MapType narrowedType = mapType.narrow(subclass);

        assertEquals(HashMap.class, narrowedType.getRawClass());
        assertEquals(intType, narrowedType.getKeyType());
        assertEquals(stringType, narrowedType.getValueType());
        assertNull(narrowedType.getValueHandler());
        assertNull(narrowedType.getTypeHandler());
        assertFalse(narrowedType.isStatic());
    }

    @Test
    public void testWithTypeHandler() {
        JavaType intType = TypeFactory.defaultInstance().constructType(int.class);
        JavaType stringType = TypeFactory.defaultInstance().constructType(String.class);
        Object handler = new Object();

        MapType mapType = new MapType(Map.class, null, null, null, intType, stringType);

        MapType withHandler = mapType.withTypeHandler(handler);

        assertEquals(mapType.getRawClass(), withHandler.getRawClass());
        assertEquals(mapType.getKeyType(), withHandler.getKeyType());
        assertEquals(mapType.getValueType(), withHandler.getValueType());
        assertEquals(handler, withHandler.getTypeHandler());
        assertNull(withHandler.getValueHandler());
        assertFalse(withHandler.isStatic());
    }

    @Test
    public void testWithContentTypeHandler() {
        JavaType intType = TypeFactory.defaultInstance().constructType(int.class);
        JavaType stringType = TypeFactory.defaultInstance().constructType(String.class);
        JavaType contentType = TypeFactory.defaultInstance().constructType(List.class);
        Object handler = new Object();

        JavaType valueType = mapTypeWithContentTypeHandler(intType, handler);

        MapType mapType = new MapType(Map.class, null, null, null, intType, valueType);

        MapType withContentHandler = mapType.withContentTypeHandler(handler);

        assertEquals(mapType.getRawClass(), withContentHandler.getRawClass());
        assertEquals(mapType.getKeyType(), withContentHandler.getKeyType());
        assertEquals(contentType, withContentHandler.getValueType());
        assertEquals(handler, withContentHandler.getTypeHandler());
        assertNull(withContentHandler.getValueHandler());
        assertFalse(withContentHandler.isStatic());
    }

    @Test
    public void testWithValueHandler() {
        JavaType intType = TypeFactory.defaultInstance().constructType(int.class);
        JavaType stringType = TypeFactory.defaultInstance().constructType(String.class);
        Object handler = new Object();

        MapType mapType = new MapType(Map.class, null, null, null, intType, stringType);

        MapType withHandler = mapType.withValueHandler(handler);

        assertEquals(mapType.getRawClass(), withHandler.getRawClass());
        assertEquals(mapType.getKeyType(), withHandler.getKeyType());
        assertEquals(mapType.getValueType(), withHandler.getValueType());
        assertEquals(handler, withHandler.getValueHandler());
        assertNull(withHandler.getTypeHandler());
        assertFalse(withHandler.isStatic());
    }

    @Test
    public void testWithContentValueHandler() {
        JavaType intType = TypeFactory.defaultInstance().constructType(int.class);
        JavaType stringType = TypeFactory.defaultInstance().constructType(String.class);
        JavaType contentValueType = TypeFactory.defaultInstance().constructType(String.class);
        Object handler = new Object();

        JavaType valueType = mapTypeWithContentValueHandler(intType, handler);

        MapType mapType = new MapType(Map.class, null, null, null, intType, valueType);

        MapType withContentValueHandler = mapType.withContentValueHandler(handler);

        assertEquals(mapType.getRawClass(), withContentValueHandler.getRawClass());
        assertEquals(mapType.getKeyType(), withContentValueHandler.getKeyType());
        assertEquals(contentValueType, withContentValueHandler.getValueType());
        assertEquals(handler, withContentValueHandler.getValueHandler());
        assertNull(withContentValueHandler.getTypeHandler());
        assertFalse(withContentValueHandler.isStatic());
    }

    @Test
    public void testWithStaticTyping() {
        JavaType intType = TypeFactory.defaultInstance().constructType(int.class);
        JavaType stringType = TypeFactory.defaultInstance().constructType(String.class);

        MapType mapType = new MapType(Map.class, null, null, null, intType, stringType);

        MapType withStaticTyping = mapType.withStaticTyping();

        assertEquals(mapType.getRawClass(), withStaticTyping.getRawClass());
        assertEquals(intType, withStaticTyping.getKeyType());
        assertEquals(stringType, withStaticTyping.getValueType());
        assertNull(withStaticTyping.getValueHandler());
        assertNull(withStaticTyping.getTypeHandler());
        assertTrue(withStaticTyping.isStatic());
    }

    @Test
    public void testWithContentType() {
        JavaType intType = TypeFactory.defaultInstance().constructType(int.class);
        JavaType stringType = TypeFactory.defaultInstance().constructType(String.class);
        JavaType contentType = TypeFactory.defaultInstance().constructType(List.class);

        MapType mapType = new MapType(Map.class, null, null, null, intType, stringType);

        MapType withContentType = mapType.withContentType(contentType);

        assertEquals(mapType.getRawClass(), withContentType.getRawClass());
        assertEquals(mapType.getKeyType(), withContentType.getKeyType());
        assertEquals(contentType, withContentType.getValueType());
        assertNull(withContentType.getValueHandler());
        assertNull(withContentType.getTypeHandler());
        assertFalse(withContentType.isStatic());
    }

    @Test
    public void testWithKeyType() {
        JavaType intType = TypeFactory.defaultInstance().constructType(int.class);
        JavaType longType = TypeFactory.defaultInstance().constructType(long.class);
        JavaType stringType = TypeFactory.defaultInstance().constructType(String.class);

        MapType mapType = new MapType(Map.class, null, null, null, intType, stringType);

        MapType withKeyType = mapType.withKeyType(longType);

        assertEquals(mapType.getRawClass(), withKeyType.getRawClass());
        assertEquals(longType, withKeyType.getKeyType());
        assertEquals(mapType.getValueType(), withKeyType.getValueType());
        assertNull(withKeyType.getValueHandler());
        assertNull(withKeyType.getTypeHandler());
        assertFalse(withKeyType.isStatic());
    }

    @Test
    public void testRefine() {
        JavaType intType = TypeFactory.defaultInstance().constructType(int.class);
        JavaType longType = TypeFactory.defaultInstance().constructType(long.class);
        JavaType stringType = TypeFactory.defaultInstance().constructType(String.class);
        TypeBindings bindings = new TypeBindings();
        Class<?> rawType = LinkedHashMap.class;

        MapType mapType = new MapType(Map.class, null, null, null, intType, stringType);

        MapType refinedType = mapType.refine(rawType, bindings, null, null);

        assertEquals(LinkedHashMap.class, refinedType.getRawClass());
        assertEquals(intType, refinedType.getKeyType());
        assertEquals(stringType, refinedType.getValueType());
        assertNull(refinedType.getValueHandler());
        assertNull(refinedType.getTypeHandler());
        assertFalse(refinedType.isStatic());
    }

    @Test
    public void testWithKeyTypeHandler() {
        JavaType intType = TypeFactory.defaultInstance().constructType(int.class);
        JavaType stringType = TypeFactory.defaultInstance().constructType(String.class);
        Object handler = new Object();

        MapType mapType = new MapType(Map.class, null, null, null, intType, stringType);

        MapType withKeyTypeHandler = mapType.withKeyTypeHandler(handler);

        assertEquals(mapType.getRawClass(), withKeyTypeHandler.getRawClass());
        assertEquals(mapType.getKeyType(), withKeyTypeHandler.getKeyType());
        assertEquals(mapType.getValueType(), withKeyTypeHandler.getValueType());
        assertEquals(handler, withKeyTypeHandler.getKeyTypeHandler());
        assertNull(withKeyTypeHandler.getValueHandler());
        assertNull(withKeyTypeHandler.getTypeHandler());
        assertFalse(withKeyTypeHandler.isStatic());
    }

    @Test
    public void testWithKeyValueHandler() {
        JavaType intType = TypeFactory.defaultInstance().constructType(int.class);
        JavaType stringType = TypeFactory.defaultInstance().constructType(String.class);
        Object handler = new Object();

        MapType mapType = new MapType(Map.class, null, null, null, intType, stringType);

        MapType withKeyValueHandler = mapType.withKeyValueHandler(handler);

        assertEquals(mapType.getRawClass(), withKeyValueHandler.getRawClass());
        assertEquals(mapType.getKeyType(), withKeyValueHandler.getKeyType());
        assertEquals(mapType.getValueType(), withKeyValueHandler.getValueType());
        assertEquals(handler, withKeyValueHandler.getKeyValueHandler());
        assertNull(withKeyValueHandler.getValueHandler());
        assertNull(withKeyValueHandler.getTypeHandler());
        assertFalse(withKeyValueHandler.isStatic());
    }

    private JavaType mapTypeWithContentTypeHandler(JavaType keyType, Object handler) {
        JavaType valueType = TypeFactory.defaultInstance().constructType(Map.Entry.class);
        JavaType mapType = MapType.construct(Map.class, null, null, null, keyType, valueType);
        return mapType.withContentTypeHandler(handler);
    }

    private JavaType mapTypeWithContentValueHandler(JavaType keyType, Object handler) {
        JavaType valueType = TypeFactory.defaultInstance().constructType(String.class);
        JavaType mapType = MapType.construct(Map.class, null, null, null, keyType, valueType);
        return mapType.withContentValueHandler(handler);
    }
}