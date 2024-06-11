package com.fasterxml.jackson.databind.type;

import static org.junit.Assert.*;

import org.junit.Test;

public class MapTypeTest {
    @Test
    public void testMapTypeConstruction() {
        JavaType keyType = TypeFactory.defaultInstance().constructType(String.class);
        JavaType valueType = TypeFactory.defaultInstance().constructType(Integer.class);
        MapType mapType = MapType.construct(Map.class, null, null, null, keyType, valueType);
        assertEquals(Map.class, mapType.getRawClass());
        assertEquals(keyType, mapType.getKeyType());
        assertEquals(valueType, mapType.getValueType());
    }

    @Test
    public void testMapTypeWithTypeHandlers() {
        JavaType keyType = TypeFactory.defaultInstance().constructType(String.class);
        JavaType valueType = TypeFactory.defaultInstance().constructType(Integer.class);
        MapType mapType = new MapType(Map.class, null, null, null, keyType, valueType, null, null, false);

        mapType = mapType.withTypeHandler(new TestTypeHandler());
        assertNotNull(mapType.getTypeHandler());
        assertEquals(mapType.getTypeHandler().getClass(), TestTypeHandler.class);

        mapType = mapType.withContentTypeHandler(new TestContentTypeHandler());
        assertNotNull(mapType.getContentTypeHandler());
        assertEquals(mapType.getContentTypeHandler().getClass(), TestContentTypeHandler.class);
    }

    @Test
    public void testMapTypeWithValueHandlers() {
        JavaType keyType = TypeFactory.defaultInstance().constructType(String.class);
        JavaType valueType = TypeFactory.defaultInstance().constructType(Integer.class);
        MapType mapType = new MapType(Map.class, null, null, null, keyType, valueType, null, null, false);

        mapType = mapType.withValueHandler(new TestValueHandler());
        assertNotNull(mapType.getValueHandler());
        assertEquals(mapType.getValueHandler().getClass(), TestValueHandler.class);

        mapType = mapType.withContentValueHandler(new TestContentValueHandler());
        assertNotNull(mapType.getContentValueHandler());
        assertEquals(mapType.getContentValueHandler().getClass(), TestContentValueHandler.class);
    }

    @Test
    public void testMapTypeWithStaticTyping() {
        JavaType keyType = TypeFactory.defaultInstance().constructType(String.class);
        JavaType valueType = TypeFactory.defaultInstance().constructType(Integer.class);
        MapType mapType = new MapType(Map.class, null, null, null, keyType, valueType, null, null, false);

        MapType staticMapType = mapType.withStaticTyping();
        assertTrue(staticMapType.isStatic());
    }

    @Test
    public void testMapTypeWithContentType() {
        JavaType keyType = TypeFactory.defaultInstance().constructType(String.class);
        JavaType valueType = TypeFactory.defaultInstance().constructType(Integer.class);
        JavaType contentType = TypeFactory.defaultInstance().constructType(String[].class);
        MapType mapType = new MapType(Map.class, null, null, null, keyType, valueType, null, null, false);

        MapType newMapType = mapType.withContentType(contentType);
        assertEquals(contentType, newMapType.getValueType());
    }

    @Test
    public void testMapTypeWithKeyType() {
        JavaType keyType1 = TypeFactory.defaultInstance().constructType(String.class);
        JavaType valueType = TypeFactory.defaultInstance().constructType(Integer.class);
        JavaType keyType2 = TypeFactory.defaultInstance().constructType(Integer.class);
        MapType mapType = new MapType(Map.class, null, null, null, keyType1, valueType, null, null, false);

        MapType newMapType = mapType.withKeyType(keyType2);
        assertEquals(keyType2, newMapType.getKeyType());
    }

    @Test
    public void testMapTypeRefine() {
        JavaType keyType = TypeFactory.defaultInstance().constructType(String.class);
        JavaType valueType = TypeFactory.defaultInstance().constructType(Integer.class);
        JavaType rawType = TypeFactory.defaultInstance().constructType(HashMap.class);
        TypeBindings bindings = new TypeBindings();
        JavaType superClass = TypeFactory.defaultInstance().constructType(Map.class);
        JavaType[] superInterfaces = {List.class};

        MapType mapType = new MapType(rawType, bindings, superClass, superInterfaces, keyType, valueType, null, null, false);

        MapType refinedMapType = mapType.refine(Map.class, null, null, null);
        assertEquals(Map.class, refinedMapType.getRawClass());
        assertEquals(keyType, refinedMapType.getKeyType());
        assertEquals(valueType, refinedMapType.getValueType());
    }

    @Test
    public void testMapTypeWithKeyTypeHandler() {
        JavaType keyType = TypeFactory.defaultInstance().constructType(String.class);
        JavaType valueType = TypeFactory.defaultInstance().constructType(Integer.class);
        MapType mapType = new MapType(Map.class, null, null, null, keyType, valueType, null, null, false);

        mapType = mapType.withKeyTypeHandler(new TestKeyTypeHandler());
        assertNotNull(mapType.getKeyTypeHandler());
        assertEquals(mapType.getKeyTypeHandler().getClass(), TestKeyTypeHandler.class);
    }

    @Test
    public void testMapTypeWithKeyValueHandler() {
        JavaType keyType = TypeFactory.defaultInstance().constructType(String.class);
        JavaType valueType = TypeFactory.defaultInstance().constructType(Integer.class);
        MapType mapType = new MapType(Map.class, null, null, null, keyType, valueType, null, null, false);

        mapType = mapType.withKeyValueHandler(new TestKeyValueHandler());
        assertNotNull(mapType.getKeyValueHandler());
        assertEquals(mapType.getKeyValueHandler().getClass(), TestKeyValueHandler.class);
    }

    @Test
    public void testToString() {
        JavaType keyType = TypeFactory.defaultInstance().constructType(String.class);
        JavaType valueType = TypeFactory.defaultInstance().constructType(Integer.class);
        MapType mapType = new MapType(Map.class, null, null, null, keyType, valueType, null, null, false);

        String toString = mapType.toString();
        assertTrue(toString.contains("map type; class " + Map.class.getName()));
        assertTrue(toString.contains("-> " + valueType.toString()));
    }
}

class TestTypeHandler {}
class TestContentTypeHandler {}
class TestValueHandler {}
class TestContentValueHandler {}
class TestKeyTypeHandler {}
class TestKeyValueHandler {}