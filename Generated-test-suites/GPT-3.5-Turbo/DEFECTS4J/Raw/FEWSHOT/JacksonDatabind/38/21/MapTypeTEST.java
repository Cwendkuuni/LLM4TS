
package com.fasterxml.jackson.databind.type;


import com.fasterxml.jackson.databind.JavaType;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

public class MapTypeTest {

    @Test
    public void testConstruct() {
        MapType mapType = MapType.construct(java.util.HashMap.class, new TypeBindings(), null, null, TypeFactory.defaultInstance().constructType(Integer.class), TypeFactory.defaultInstance().constructType(String.class));
        assertNotNull(mapType);
        assertEquals(java.util.HashMap.class, mapType.getRawClass());
        assertEquals(TypeFactory.defaultInstance().constructType(Integer.class), mapType.getKeyType());
        assertEquals(TypeFactory.defaultInstance().constructType(String.class), mapType.getValueType());
    }

    @Test
    public void testConstructWithNullTypes() {
        MapType mapType = MapType.construct(java.util.HashMap.class, null, null);
        assertNotNull(mapType);
        assertEquals(java.util.HashMap.class, mapType.getRawClass());
        assertNull(mapType.getKeyType());
        assertNull(mapType.getValueType());
    }

    @Test
    public void testNarrow() {
        MapType baseMapType = MapType.construct(java.util.HashMap.class, new TypeBindings(), null, null, TypeFactory.defaultInstance().constructType(Number.class), TypeFactory.defaultInstance().constructType(Object.class));
        MapType mapType = (MapType) baseMapType.narrow(java.util.Map.class);
        assertNotNull(mapType);
        assertEquals(java.util.Map.class, mapType.getRawClass());
        assertNotEquals(baseMapType, mapType);
        assertEquals(baseMapType.getKeyType(), mapType.getKeyType());
        assertEquals(baseMapType.getValueType(), mapType.getValueType());
    }

    @Test
    public void testWithTypeHandler() {
        Object handler = new Object();
        MapType baseMapType = MapType.construct(java.util.HashMap.class, new TypeBindings(), null, null, TypeFactory.defaultInstance().constructType(Integer.class), TypeFactory.defaultInstance().constructType(String.class));
        MapType mapType = baseMapType.withTypeHandler(handler);
        assertNotNull(mapType);
        assertEquals(java.util.HashMap.class, mapType.getRawClass());
        assertEquals(handler, mapType.getTypeHandler());
        assertEquals(baseMapType.getKeyType(), mapType.getKeyType());
        assertEquals(baseMapType.getValueType(), mapType.getValueType());
    }

    @Test
    public void testWithContentTypeHandler() {
        Object handler = new Object();
        MapType baseMapType = MapType.construct(java.util.HashMap.class, new TypeBindings(), null, null, TypeFactory.defaultInstance().constructType(Integer.class), TypeFactory.defaultInstance().constructType(String.class));
        MapType mapType = baseMapType.withContentTypeHandler(handler);
        assertNotNull(mapType);
        assertEquals(java.util.HashMap.class, mapType.getRawClass());
        assertEquals(handler, mapType.getContentTypeHandler());
        assertEquals(baseMapType.getKeyType(), mapType.getKeyType());
        assertEquals(baseMapType.getValueType(), mapType.getValueType());
    }

    @Test
    public void testWithValueHandler() {
        Object handler = new Object();
        MapType baseMapType = MapType.construct(java.util.HashMap.class, new TypeBindings(), null, null, TypeFactory.defaultInstance().constructType(Integer.class), TypeFactory.defaultInstance().constructType(String.class));
        MapType mapType = baseMapType.withValueHandler(handler);
        assertNotNull(mapType);
        assertEquals(java.util.HashMap.class, mapType.getRawClass());
        assertEquals(handler, mapType.getValueHandler());
        assertEquals(baseMapType.getKeyType(), mapType.getKeyType());
        assertEquals(baseMapType.getValueType(), mapType.getValueType());
    }

    @Test
    public void testWithContentValueHandler() {
        Object handler = new Object();
        MapType baseMapType = MapType.construct(java.util.HashMap.class, new TypeBindings(), null, null, TypeFactory.defaultInstance().constructType(Integer.class), TypeFactory.defaultInstance().constructType(String.class));
        MapType mapType = baseMapType.withContentValueHandler(handler);
        assertNotNull(mapType);
        assertEquals(java.util.HashMap.class, mapType.getRawClass());
        assertEquals(handler, mapType.getContentValueHandler());
        assertEquals(baseMapType.getKeyType(), mapType.getKeyType());
        assertEquals(baseMapType.getValueType(), mapType.getValueType());
    }

    @Test
    public void testWithStaticTyping() {
        MapType baseMapType = MapType.construct(java.util.HashMap.class, new TypeBindings(), null, null, TypeFactory.defaultInstance().constructType(Integer.class), TypeFactory.defaultInstance().constructType(String.class));
        MapType mapType = baseMapType.withStaticTyping();
        assertNotNull(mapType);
        assertEquals(java.util.HashMap.class, mapType.getRawClass());
        assertEquals(baseMapType.getKeyType(), mapType.getKeyType());
        assertEquals(baseMapType.getValueType(), mapType.getValueType());
        assertTrue(mapType.isStaticTyping());
    }

    @Test
    public void testWithContentType() {
        JavaType contentType = TypeFactory.defaultInstance().constructType(Object.class);
        MapType baseMapType = MapType.construct(java.util.HashMap.class, new TypeBindings(), null, null, TypeFactory.defaultInstance().constructType(Integer.class), TypeFactory.defaultInstance().constructType(String.class));
        MapType mapType = baseMapType.withContentType(contentType);
        assertNotNull(mapType);
        assertEquals(java.util.HashMap.class, mapType.getRawClass());
        assertEquals(contentType, mapType.getContentType());
        assertEquals(baseMapType.getKeyType(), mapType.getKeyType());
        assertEquals(baseMapType.getValueType(), mapType.getValueType());
    }

    @Test
    public void testWithKeyType() {
        JavaType keyType = TypeFactory.defaultInstance().constructType(String.class);
        MapType baseMapType = MapType.construct(java.util.HashMap.class, new TypeBindings(), null, null, TypeFactory.defaultInstance().constructType(Integer.class), TypeFactory.defaultInstance().constructType(String.class));
        MapType mapType = baseMapType.withKeyType(keyType);
        assertNotNull(mapType);
        assertEquals(java.util.HashMap.class, mapType.getRawClass());
        assertEquals(keyType, mapType.getKeyType());
        assertEquals(baseMapType.getValueType(), mapType.getValueType());
    }

    @Test
    public void testRefine() {
        Class<?> rawType = java.util.HashMap.class;
        JavaType keyType = TypeFactory.defaultInstance().constructType(Integer.class);
        JavaType valueType = TypeFactory.defaultInstance().constructType(String.class);
        JavaType superClass = TypeFactory.defaultInstance().constructType(java.util.Map.class);
        JavaType[] superInterfaces = new JavaType[0];

        MapType baseMapType = MapType.construct(java.util.HashMap.class, new TypeBindings(), null, null, TypeFactory.defaultInstance().constructType(Number.class), TypeFactory.defaultInstance().constructType(Object.class));
        MapType mapType = (MapType) baseMapType.refine(rawType, new TypeBindings(), superClass, superInterfaces);
        assertNotNull(mapType);
        assertEquals(rawType, mapType.getRawClass());
        assertNotEquals(baseMapType, mapType);
        assertEquals(null, mapType.getKeyType());
        assertEquals(null, mapType.getValueType());
        assertEquals(superClass, mapType.getSuperClass());
        assertEquals(superInterfaces, mapType.getInterfaces());
    }

    @Test
    public void testWithKeyTypeHandler() {
        Object handler = new Object();
        MapType baseMapType = MapType.construct(java.util.HashMap.class, new TypeBindings(), null, null, TypeFactory.defaultInstance().constructType(Number.class), TypeFactory.defaultInstance().constructType(Object.class));
        MapType mapType = baseMapType.withKeyTypeHandler(handler);
        assertNotNull(mapType);
        assertEquals(java.util.HashMap.class, mapType.getRawClass());
        assertEquals(handler, mapType.getKeyTypeHandler());
        assertEquals(baseMapType.getKeyType(), mapType.getKeyType());
        assertEquals(baseMapType.getValueType(), mapType.getValueType());
    }

    @Test
    public void testWithKeyValueHandler() {
        Object handler = new Object();
        MapType baseMapType = MapType.construct(java.util.HashMap.class, new TypeBindings(), null, null, TypeFactory.defaultInstance().constructType(Number.class), TypeFactory.defaultInstance().constructType(Object.class));
        MapType mapType = baseMapType.withKeyValueHandler(handler);
        assertNotNull(mapType);
        assertEquals(java.util.HashMap.class, mapType.getRawClass());
        assertEquals(handler, mapType.getKeyValueHandler());
        assertEquals(baseMapType.getKeyType(), mapType.getKeyType());
        assertEquals(baseMapType.getValueType(), mapType.getValueType());
    }

    @Test
    public void testToString() {
        MapType baseMapType = MapType.construct(java.util.HashMap.class, new TypeBindings(), null, null, TypeFactory.defaultInstance().constructType(Integer.class), TypeFactory.defaultInstance().constructType(String.class));
        assertEquals("[map type; class java.util.HashMap, Integer -> String]", baseMapType.toString());
    }
}
