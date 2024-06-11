
package com.fasterxml.jackson.databind.type;

import org.junit.Assert;
import org.junit.Test;

public class MapTypeTest {

    @Test
    public void testConstruct() {
        Class<?> rawType = Map.class;
        TypeBindings bindings = null;
        JavaType superClass = null;
        JavaType[] superInts = null;
        JavaType keyT = null;
        JavaType valueT = null;
        Object valueHandler = null;
        Object typeHandler = null;
        boolean asStatic = false;

        MapType mapType = MapType.construct(rawType, bindings, superClass,
                superInts, keyT, valueT);

        Assert.assertNotNull(mapType);
        Assert.assertEquals(rawType, mapType.getRawClass());
        Assert.assertEquals(bindings, mapType.getBindings());
        Assert.assertEquals(superClass, mapType.getSuperClass());
        Assert.assertEquals(superInts, mapType.getInterfaces());
        Assert.assertEquals(keyT, mapType.getKeyType());
        Assert.assertEquals(valueT, mapType.getContentType());
        Assert.assertEquals(valueHandler, mapType.getValueHandler());
        Assert.assertEquals(typeHandler, mapType.getTypeHandler());
        Assert.assertEquals(asStatic, mapType.isStaticTyping());
    }

    @Test
    public void testWithContentType() {
        // Setup
        JavaType contentType = null;

        // Test
        MapType mapType = new MapType(Map.class, null, null, null, null, null,
                null, null, false);
        MapType result = mapType.withContentType(contentType);

        // Verify
        Assert.assertNotNull(result);
        Assert.assertEquals(mapType.getRawClass(), result.getRawClass());
        Assert.assertEquals(mapType.getBindings(), result.getBindings());
        Assert.assertEquals(mapType.getSuperClass(), result.getSuperClass());
        Assert.assertEquals(mapType.getInterfaces(), result.getInterfaces());
        Assert.assertEquals(mapType.getKeyType(), result.getKeyType());
        Assert.assertEquals(contentType, result.getContentType());
        Assert.assertEquals(mapType.getValueHandler(), result.getValueHandler());
        Assert.assertEquals(mapType.getTypeHandler(), result.getTypeHandler());
        Assert.assertEquals(mapType.isStaticTyping(), result.isStaticTyping());
    }

    @Test
    public void testWithKeyType() {
        // Setup
        JavaType keyType = null;

        // Test
        MapType mapType = new MapType(Map.class, null, null, null, null, null,
                null, null, false);
        MapType result = mapType.withKeyType(keyType);

        // Verify
        Assert.assertNotNull(result);
        Assert.assertEquals(mapType.getRawClass(), result.getRawClass());
        Assert.assertEquals(mapType.getBindings(), result.getBindings());
        Assert.assertEquals(mapType.getSuperClass(), result.getSuperClass());
        Assert.assertEquals(mapType.getInterfaces(), result.getInterfaces());
        Assert.assertEquals(keyType, result.getKeyType());
        Assert.assertEquals(mapType.getContentType(), result.getContentType());
        Assert.assertEquals(mapType.getValueHandler(), result.getValueHandler());
        Assert.assertEquals(mapType.getTypeHandler(), result.getTypeHandler());
        Assert.assertEquals(mapType.isStaticTyping(), result.isStaticTyping());
    }

    @Test
    public void testWithKeyTypeHandler() {
        // Setup
        Object h = null;

        // Test
        MapType mapType = new MapType(Map.class, null, null, null, null, null,
                null, null, false);
        MapType result = mapType.withKeyTypeHandler(h);

        // Verify
        Assert.assertNotNull(result);
        Assert.assertEquals(mapType.getRawClass(), result.getRawClass());
        Assert.assertEquals(mapType.getBindings(), result.getBindings());
        Assert.assertEquals(mapType.getSuperClass(), result.getSuperClass());
        Assert.assertEquals(mapType.getInterfaces(), result.getInterfaces());
        Assert.assertEquals(mapType.getKeyType().withTypeHandler(h), result.getKeyType());
        Assert.assertEquals(mapType.getContentType(), result.getContentType());
        Assert.assertEquals(mapType.getValueHandler(), result.getValueHandler());
        Assert.assertEquals(mapType.getTypeHandler(), result.getTypeHandler());
        Assert.assertEquals(mapType.isStaticTyping(), result.isStaticTyping());
    }

    @Test
    public void testWithKeyValueHandler() {
        // Setup
        Object h = null;

        // Test
        MapType mapType = new MapType(Map.class, null, null, null, null, null,
                null, null, false);
        MapType result = mapType.withKeyValueHandler(h);

        // Verify
        Assert.assertNotNull(result);
        Assert.assertEquals(mapType.getRawClass(), result.getRawClass());
        Assert.assertEquals(mapType.getBindings(), result.getBindings());
        Assert.assertEquals(mapType.getSuperClass(), result.getSuperClass());
        Assert.assertEquals(mapType.getInterfaces(), result.getInterfaces());
        Assert.assertEquals(mapType.getKeyType().withValueHandler(h), result.getKeyType());
        Assert.assertEquals(mapType.getContentType(), result.getContentType());
        Assert.assertEquals(mapType.getValueHandler(), result.getValueHandler());
        Assert.assertEquals(mapType.getTypeHandler(), result.getTypeHandler());
        Assert.assertEquals(mapType.isStaticTyping(), result.isStaticTyping());
    }

    @Test
    public void testWithStaticTyping() {
        // Setup

        // Test
        MapType mapType = new MapType(Map.class, null, null, null, null, null,
                null, null, false);
        MapType result = mapType.withStaticTyping();

        // Verify
        Assert.assertNotNull(result);
        Assert.assertEquals(mapType.getRawClass(), result.getRawClass());
        Assert.assertEquals(mapType.getBindings(), result.getBindings());
        Assert.assertEquals(mapType.getSuperClass(), result.getSuperClass());
        Assert.assertEquals(mapType.getInterfaces(), result.getInterfaces());
        Assert.assertEquals(mapType.getKeyType().withStaticTyping(), result.getKeyType());
        Assert.assertEquals(mapType.getContentType().withStaticTyping(), result.getContentType());
        Assert.assertEquals(mapType.getValueHandler(), result.getValueHandler());
        Assert.assertEquals(mapType.getTypeHandler(), result.getTypeHandler());
        Assert.assertEquals(true, result.isStaticTyping());
    }

    @Test
    public void testToString() {
        // Setup
        Class<?> rawType = Map.class;
        JavaType keyT = null;
        JavaType valueT = null;

        // Test
        MapType mapType = new MapType(rawType, null, null, null, keyT, valueT,
                null, null, false);
        String result = mapType.toString();

        // Verify
        Assert.assertEquals("[map type; class java.util.Map, " + keyT + " -> " + valueT + "]", result);
    }
}
