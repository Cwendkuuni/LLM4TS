package com.fasterxml.jackson.databind.type;

import org.junit.Test;
import static org.junit.Assert.*;

public class MapTypeTest {

    @Test
    public void testConstruct() {
        Class<?> rawType = null;
        TypeBindings bindings = null;
        JavaType superClass = null;
        JavaType[] superInts = null;
        JavaType keyT = null;
        JavaType valueT = null;

        MapType mapType = MapType.construct(rawType, bindings, superClass, superInts, keyT, valueT);
        assertEquals(rawType, mapType.getRawClass());
        assertEquals(bindings, mapType.getBindings());
        assertEquals(superClass, mapType.getSuperClass());
        assertEquals(superInts, mapType.getInterfaces());
        assertEquals(keyT, mapType.getKeyType());
        assertEquals(valueT, mapType.getContentType());
    }

    @Test
    public void testConstructDeprecated() {
        Class<?> rawType = null;
        JavaType keyT = null;
        JavaType valueT = null;

        MapType mapType = MapType.constructDeprecated(rawType, keyT, valueT);
        assertEquals(rawType, mapType.getRawClass());
        assertNull(mapType.getBindings());
        assertNotNull(mapType.getSuperClass());
        assertNull(mapType.getInterfaces());
        assertEquals(keyT, mapType.getKeyType());
        assertEquals(valueT, mapType.getContentType());
    }

    @Test
    public void testNarrow() {
        Class<?> subclass = null;

        MapType mapType = new MapType(null, null, null, null, null, null, null, null, false);
        JavaType narrowedType = mapType.narrow(subclass);
        assertEquals(subclass, narrowedType.getRawClass());
        assertEquals(mapType.getBindings(), narrowedType.getBindings());
        assertEquals(mapType.getSuperClass(), narrowedType.getSuperClass());
        assertEquals(mapType.getInterfaces(), narrowedType.getInterfaces());
        assertEquals(mapType.getKeyType(), narrowedType.getKeyType());
        assertEquals(mapType.getContentType(), narrowedType.getContentType());
        assertEquals(mapType.getValueHandler(), narrowedType.getValueHandler());
        assertEquals(mapType.getTypeHandler(), narrowedType.getTypeHandler());
        assertEquals(mapType.isStatic(), narrowedType.isStatic());
    }

    @Test
    public void testWithTypeHandler() {
        Object handler = new Object();

        MapType mapType = new MapType(null, null, null, null, null, null, null, null, false);
        MapType typeWithHandler = mapType.withTypeHandler(handler);
        assertEquals(mapType.getRawClass(), typeWithHandler.getRawClass());
        assertEquals(mapType.getBindings(), typeWithHandler.getBindings());
        assertEquals(mapType.getSuperClass(), typeWithHandler.getSuperClass());
        assertEquals(mapType.getInterfaces(), typeWithHandler.getInterfaces());
        assertEquals(mapType.getKeyType(), typeWithHandler.getKeyType());
        assertEquals(mapType.getContentType(), typeWithHandler.getContentType());
        assertEquals(mapType.getValueHandler(), typeWithHandler.getValueHandler());
        assertEquals(handler, typeWithHandler.getTypeHandler());
        assertEquals(mapType.isStatic(), typeWithHandler.isStatic());
    }

    @Test
    public void testWithContentTypeHandler() {
        Object handler = new Object();

        MapType mapType = new MapType(null, null, null, null, null, null, null, null, false);
        MapType typeWithContentHandler = mapType.withContentTypeHandler(handler);
        assertEquals(mapType.getRawClass(), typeWithContentHandler.getRawClass());
        assertEquals(mapType.getBindings(), typeWithContentHandler.getBindings());
        assertEquals(mapType.getSuperClass(), typeWithContentHandler.getSuperClass());
        assertEquals(mapType.getInterfaces(), typeWithContentHandler.getInterfaces());
        assertEquals(mapType.getKeyType(), typeWithContentHandler.getKeyType());
        assertNotEquals(mapType.getContentType(), typeWithContentHandler.getContentType());
        assertEquals(mapType.getValueHandler(), typeWithContentHandler.getValueHandler());
        assertEquals(mapType.getTypeHandler(), typeWithContentHandler.getTypeHandler());
        assertEquals(mapType.isStatic(), typeWithContentHandler.isStatic());
    }

    @Test
    public void testWithValueHandler() {
        Object handler = new Object();

        MapType mapType = new MapType(null, null, null, null, null, null, null, null, false);
        MapType typeWithValueHandler = mapType.withValueHandler(handler);
        assertEquals(mapType.getRawClass(), typeWithValueHandler.getRawClass());
        assertEquals(mapType.getBindings(), typeWithValueHandler.getBindings());
        assertEquals(mapType.getSuperClass(), typeWithValueHandler.getSuperClass());
        assertEquals(mapType.getInterfaces(), typeWithValueHandler.getInterfaces());
        assertEquals(mapType.getKeyType(), typeWithValueHandler.getKeyType());
        assertEquals(mapType.getContentType(), typeWithValueHandler.getContentType());
        assertEquals(handler, typeWithValueHandler.getValueHandler());
        assertEquals(mapType.getTypeHandler(), typeWithValueHandler.getTypeHandler());
        assertEquals(mapType.isStatic(), typeWithValueHandler.isStatic());
    }

    @Test
    public void testWithContentValueHandler() {
        Object handler = new Object();

        MapType mapType = new MapType(null, null, null, null, null, null, null, null, false);
        MapType typeWithContentValueHandler = mapType.withContentValueHandler(handler);
        assertEquals(mapType.getRawClass(), typeWithContentValueHandler.getRawClass());
        assertEquals(mapType.getBindings(), typeWithContentValueHandler.getBindings());
        assertEquals(mapType.getSuperClass(), typeWithContentValueHandler.getSuperClass());
        assertEquals(mapType.getInterfaces(), typeWithContentValueHandler.getInterfaces());
        assertEquals(mapType.getKeyType(), typeWithContentValueHandler.getKeyType());
        assertEquals(handler, typeWithContentValueHandler.getContentType().getValueHandler());
        assertEquals(mapType.getValueHandler(), typeWithContentValueHandler.getValueHandler());
        assertEquals(mapType.getTypeHandler(), typeWithContentValueHandler.getTypeHandler());
        assertEquals(mapType.isStatic(), typeWithContentValueHandler.isStatic());
    }

    @Test
    public void testWithStaticTyping() {

        MapType mapType = new MapType(null, null, null, null, null, null, null, null, false);
        MapType staticType = mapType.withStaticTyping();
        assertEquals(mapType.getRawClass(), staticType.getRawClass());
        assertEquals(mapType.getBindings(), staticType.getBindings());
        assertEquals(mapType.getSuperClass(), staticType.getSuperClass());
        assertEquals(mapType.getInterfaces(), staticType.getInterfaces());
        assertEquals(mapType.getKeyType().withStaticTyping(), staticType.getKeyType());
        assertEquals(mapType.getContentType().withStaticTyping(), staticType.getContentType());
        assertEquals(mapType.getValueHandler(), staticType.getValueHandler());
        assertEquals(mapType.getTypeHandler(), staticType.getTypeHandler());
        assertTrue(staticType.isStatic());
    }

    @Test
    public void testWithContentType() {
        JavaType contentType = null;

        MapType mapType = new MapType(null, null, null, null, null, null, null, null, false);
        MapType typeWithContentType = mapType.withContentType(contentType);
        assertEquals(mapType.getRawClass(), typeWithContentType.getRawClass());
        assertEquals(mapType.getBindings(), typeWithContentType.getBindings());
        assertEquals(mapType.getSuperClass(), typeWithContentType.getSuperClass());
        assertEquals(mapType.getInterfaces(), typeWithContentType.getInterfaces());
        assertEquals(mapType.getKeyType(), typeWithContentType.getKeyType());
        assertEquals(contentType, typeWithContentType.getContentType());
        assertEquals(mapType.getValueHandler(), typeWithContentType.getValueHandler());
        assertEquals(mapType.getTypeHandler(), typeWithContentType.getTypeHandler());
        assertEquals(mapType.isStatic(), typeWithContentType.isStatic());
    }

    @Test
    public void testWithKeyType() {
        JavaType keyType = null;

        MapType mapType = new MapType(null, null, null, null, null, null, null, null, false);
        MapType typeWithKeyType = mapType.withKeyType(keyType);
        assertEquals(mapType.getRawClass(), typeWithKeyType.getRawClass());
        assertEquals(mapType.getBindings(), typeWithKeyType.getBindings());
        assertEquals(mapType.getSuperClass(), typeWithKeyType.getSuperClass());
        assertEquals(mapType.getInterfaces(), typeWithKeyType.getInterfaces());
        assertEquals(keyType, typeWithKeyType.getKeyType());
        assertEquals(mapType.getContentType(), typeWithKeyType.getContentType());
        assertEquals(mapType.getValueHandler(), typeWithKeyType.getValueHandler());
        assertEquals(mapType.getTypeHandler(), typeWithKeyType.getTypeHandler());
        assertEquals(mapType.isStatic(), typeWithKeyType.isStatic());
    }

    @Test
    public void testRefine() {
        Class<?> rawType = null;
        TypeBindings bindings = null;
        JavaType superClass = null;
        JavaType[] superInterfaces = null;

        MapType mapType = new MapType(null, null, null, null, null, null, null, null, false);
        JavaType refinedType = mapType.refine(rawType, bindings, superClass, superInterfaces);
        assertEquals(rawType, refinedType.getRawClass());
        assertEquals(bindings, refinedType.getBindings());
        assertEquals(superClass, refinedType.getSuperClass());
        assertEquals(superInterfaces, refinedType.getInterfaces());
        assertEquals(mapType.getKeyType(), refinedType.getKeyType());
        assertEquals(mapType.getContentType(), refinedType.getContentType());
        assertEquals(mapType.getValueHandler(), refinedType.getValueHandler());
        assertEquals(mapType.getTypeHandler(), refinedType.getTypeHandler());
        assertEquals(mapType.isStatic(), refinedType.isStatic());
    }

    @Test
    public void testWithKeyTypeHandler() {
        Object handler = new Object();

        MapType mapType = new MapType(null, null, null, null, null, null, null, null, false);
        MapType typeWithKeyTypeHandler = mapType.withKeyTypeHandler(handler);
        assertEquals(mapType.getRawClass(), typeWithKeyTypeHandler.getRawClass());
        assertEquals(mapType.getBindings(), typeWithKeyTypeHandler.getBindings());
        assertEquals(mapType.getSuperClass(), typeWithKeyTypeHandler.getSuperClass());
        assertEquals(mapType.getInterfaces(), typeWithKeyTypeHandler.getInterfaces());
        assertEquals(handler, typeWithKeyTypeHandler.getKeyType().getTypeHandler());
        assertEquals(mapType.getContentType(), typeWithKeyTypeHandler.getContentType());
        assertEquals(mapType.getValueHandler(), typeWithKeyTypeHandler.getValueHandler());
        assertEquals(mapType.getTypeHandler(), typeWithKeyTypeHandler.getTypeHandler());
        assertEquals(mapType.isStatic(), typeWithKeyTypeHandler.isStatic());
    }

    @Test
    public void testWithKeyValueHandler() {
        Object handler = new Object();

        MapType mapType = new MapType(null, null, null, null, null, null, null, null, false);
        MapType typeWithKeyValueHandler = mapType.withKeyValueHandler(handler);
        assertEquals(mapType.getRawClass(), typeWithKeyValueHandler.getRawClass());
        assertEquals(mapType.getBindings(), typeWithKeyValueHandler.getBindings());
        assertEquals(mapType.getSuperClass(), typeWithKeyValueHandler.getSuperClass());
        assertEquals(mapType.getInterfaces(), typeWithKeyValueHandler.getInterfaces());
        assertEquals(handler, typeWithKeyValueHandler.getKeyType().getValueHandler());
        assertEquals(mapType.getContentType(), typeWithKeyValueHandler.getContentType());
        assertEquals(mapType.getValueHandler(), typeWithKeyValueHandler.getValueHandler());
        assertEquals(mapType.getTypeHandler(), typeWithKeyValueHandler.getTypeHandler());
        assertEquals(mapType.isStatic(), typeWithKeyValueHandler.isStatic());
    }

    @Test
    public void testToString() {
        Class<?> rawType = null;
        TypeBindings bindings = null;
        JavaType superClass = null;
        JavaType[] superInterfaces = null;
        JavaType keyT = null;
        JavaType valueT = null;

        MapType mapType = new MapType(rawType, bindings, superClass, superInterfaces, keyT, valueT, null, null, false);
        String expectedToString = "[map type; class " + rawType.getName() + ", " + keyT + " -> " + valueT + "]";
        assertEquals(expectedToString, mapType.toString());
    }
}