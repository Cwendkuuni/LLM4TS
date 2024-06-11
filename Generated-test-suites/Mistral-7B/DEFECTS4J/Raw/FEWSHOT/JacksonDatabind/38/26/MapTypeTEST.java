package com.fasterxml.jackson.databind.type;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;

public class MapTypeTest {

    @Test
    public void testMapTypeConstruction() {
        JavaType keyType = new SimpleType("java.lang.String");
        JavaType valueType = new SimpleType("java.lang.Integer");
        JavaType mapType = MapType.construct(Map.class, null, null, null, keyType, valueType);
        assertNotNull(mapType);
        assertEquals(Map.class, mapType.getRawClass());
        assertEquals(keyType, mapType.getKeyType());
        assertEquals(valueType, mapType.getValueType());
    }

    @Test
    public void testMapTypeWithTypeHandler() {
        JavaType keyType = new SimpleType("java.lang.String");
        JavaType valueType = new SimpleType("java.lang.Integer");
        Object valueHandler = new Object() { /* test implementation */ };
        MapType mapType = new MapType(Map.class, null, null, null, keyType, valueType, valueHandler, null, false);
        assertEquals(valueHandler, mapType.getValueHandler());
    }

    @Test
    public void testMapTypeWithContentTypeHandler() {
        JavaType keyType = new SimpleType("java.lang.String");
        JavaType valueType = new SimpleType("java.lang.Integer");
        JavaType contentTypeHandler = new SimpleType("java.util.List<java.lang.Integer>");
        MapType mapType = new MapType(Map.class, null, null, null, keyType, valueType, null, contentTypeHandler, false);
        assertEquals(contentTypeHandler, mapType.getTypeHandler());
    }

    @Test
    public void testMapTypeWithValueHandler() {
        JavaType keyType = new SimpleType("java.lang.String");
        JavaType valueType = new SimpleType("java.lang.Integer");
        Object valueHandler = new Object() { /* test implementation */ };
        MapType mapType = new MapType(Map.class, null, null, null, keyType, valueType, valueHandler, null, false);
        assertEquals(valueHandler, mapType.getValueHandler());
    }

    @Test
    public void testMapTypeWithContentValueHandler() {
        JavaType keyType = new SimpleType("java.lang.String");
        JavaType valueType = new SimpleType("java.lang.Integer");
        Object contentValueHandler = new Object() { /* test implementation */ };
        MapType mapType = new MapType(Map.class, null, null, null, keyType, valueType, null, null, false);
        MapType withContentValueHandler = mapType.withContentValueHandler(contentValueHandler);
        assertEquals(contentValueHandler, withContentValueHandler.getValueHandler());
    }

    @Test
    public void testMapTypeWithStaticTyping() {
        JavaType keyType = new SimpleType("java.lang.String");
        JavaType valueType = new SimpleType("java.lang.Integer");
        MapType mapType = new MapType(Map.class, null, null, null, keyType, valueType, null, null, false);
        MapType withStaticTyping = mapType.withStaticTyping();
        assertTrue(withStaticTyping.isStatic());
    }

    @Test
    public void testMapTypeWithContentType() {
        JavaType keyType = new SimpleType("java.lang.String");
        JavaType valueType = new SimpleType("java.lang.Integer");
        JavaType contentType = new SimpleType("java.util.List<java.lang.Integer>");
        MapType mapType = new MapType(Map.class, null, null, null, keyType, valueType, null, null, false);
        MapType withContentType = mapType.withContentType(contentType);
        assertEquals(contentType, withContentType.getValueType());
    }

    @Test
    public void testMapTypeWithKeyType() {
        JavaType keyType1 = new SimpleType("java.lang.String");
        JavaType valueType = new SimpleType("java.lang.Integer");
        JavaType keyType2 = new SimpleType("java.lang.Integer");
        MapType mapType = new MapType(Map.class, null, null, null, keyType1, valueType, null, null, false);
        MapType withKeyType = mapType.withKeyType(keyType2);
        assertEquals(keyType2, withKeyType.getKeyType());
    }

    @Test
    public void testMapTypeRefine() {
        JavaType rawType = Map.class;
        JavaType keyType = new SimpleType("java.lang.String");
        JavaType valueType = new SimpleType("java.lang.Integer");
        TypeBindings bindings = new TypeBindings();
        JavaType superClass = Object.class;
        JavaType[] superInterfaces = new JavaType[0];
        MapType mapType = new MapType(rawType, bindings, superClass, superInterfaces, keyType, valueType, null, null, false);
        JavaType refinedType = mapType.refine(HashMap.class, null, null, null);
        assertEquals(HashMap.class, refinedType.getRawClass());
        assertEquals(keyType, refinedType.getKeyType());
        assertEquals(valueType, refinedType.getValueType());
    }

    @Test
    public void testMapTypeWithKeyTypeHandler() {
        JavaType keyType = new SimpleType("java.lang.String");
        JavaType valueType = new SimpleType("java.lang.Integer");
        Object keyTypeHandler = new Object() { /* test implementation */ };
        MapType mapType = new MapType(Map.class, null, null, null, keyType, valueType, null, null, false);
        MapType withKeyTypeHandler = mapType.withKeyTypeHandler(keyTypeHandler);
        assertEquals(keyTypeHandler, withKeyTypeHandler.getKeyTypeHandler());
    }

    @Test
    public void testMapTypeWithKeyValueHandler() {
        JavaType keyType = new SimpleType("java.lang.String");
        JavaType valueType = new SimpleType("java.lang.Integer");
        Object keyValueHandler = new Object() { /* test implementation */ };
        MapType mapType = new MapType(Map.class, null, null, null, keyType, valueType, null, null, false);
        MapType withKeyValueHandler = mapType.withKeyValueHandler(keyValueHandler);
        assertEquals(keyValueHandler, withKeyValueHandler.getKeyValueHandler());
    }

    @Test
    public void testMapTypeToString() {
        JavaType keyType = new SimpleType("java.lang.String");
        JavaType valueType = new SimpleType("java.lang.Integer");
        MapType mapType = new MapType(Map.class, null, null, null, keyType, valueType, null, null, false);
        String toString = mapType.toString();
        assertTrue(toString.contains("map type"));
        assertTrue(toString.contains("class " + Map.class.getName()));
        assertTrue(toString.contains("-> " + valueType.getName()));
    }
}