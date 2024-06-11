package com.fasterxml.jackson.databind.type;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertEquals;

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
        Object valueHandler = new Object();
        JavaType mapType = new MapType(Map.class, null, null, null, keyType, valueType, null, valueHandler, false);
        assertEquals(valueHandler, mapType.getValueHandler());
    }

    @Test
    public void testMapTypeWithContentTypeHandler() {
        JavaType keyType = new SimpleType("java.lang.String");
        JavaType valueType = new SimpleType("java.lang.Integer");
        Object contentTypeHandler = new Object();
        JavaType mapType = new MapType(Map.class, null, null, null, keyType, valueType, null, null, false);
        JavaType newMapType = mapType.withContentTypeHandler(contentTypeHandler);
        assertEquals(contentTypeHandler, newMapType.getTypeHandler());
    }

    @Test
    public void testMapTypeWithValueHandler() {
        JavaType keyType = new SimpleType("java.lang.String");
        JavaType valueType = new SimpleType("java.lang.Integer");
        Object valueHandler = new Object();
        JavaType mapType = new MapType(Map.class, null, null, null, keyType, valueType, valueHandler, null, false);
        assertEquals(valueHandler, mapType.getValueHandler());
    }

    @Test
    public void testMapTypeWithContentValueHandler() {
        JavaType keyType = new SimpleType("java.lang.String");
        JavaType valueType = new SimpleType("java.lang.Integer");
        Object contentValueHandler = new Object();
        JavaType mapType = new MapType(Map.class, null, null, null, keyType, valueType, null, null, false);
        JavaType newMapType = mapType.withContentValueHandler(contentValueHandler);
        assertEquals(contentValueHandler, newMapType.getContentValueHandler());
    }

    @Test
    public void testMapTypeWithStaticTyping() {
        JavaType keyType = new SimpleType("java.lang.String");
        JavaType valueType = new SimpleType("java.lang.Integer");
        JavaType mapType = new MapType(Map.class, null, null, null, keyType, valueType, null, null, false);
        JavaType staticMapType = mapType.withStaticTyping();
        assertTrue(staticMapType.isStatic());
    }

    @Test
    public void testMapTypeWithContentType() {
        JavaType keyType = new SimpleType("java.lang.String");
        JavaType valueType = new SimpleType("java.lang.Integer");
        JavaType contentType = new SimpleType("java.util.List<java.lang.String>");
        JavaType mapType = new MapType(Map.class, null, null, null, keyType, valueType, null, null, false);
        JavaType newMapType = mapType.withContentType(contentType);
        assertNotSame(valueType, newMapType.getValueType());
        assertEquals(contentType, newMapType.getValueType());
    }

    @Test
    public void testMapTypeWithKeyType() {
        JavaType keyType1 = new SimpleType("java.lang.String");
        JavaType valueType = new SimpleType("java.lang.Integer");
        JavaType keyType2 = new SimpleType("java.lang.Integer");
        JavaType mapType = new MapType(Map.class, null, null, null, keyType1, valueType, null, null, false);
        JavaType newMapType = mapType.withKeyType(keyType2);
        assertNotSame(keyType1, newMapType.getKeyType());
        assertEquals(keyType2, newMapType.getKeyType());
    }

    @Test
    public void testMapTypeRefine() {
        JavaType rawType = Map.class;
        TypeBindings bindings = new TypeBindings();
        JavaType superClass = Collection.class;
        JavaType[] superInterfaces = new JavaType[]{List.class, Cloneable.class};
        JavaType keyType = new SimpleType("java.lang.String");
        JavaType valueType = new SimpleType("java.lang.Integer");
        JavaType mapType = new MapType(rawType, null, superClass, superInterfaces, keyType, valueType, null, null, false);
        JavaType newMapType = mapType.refine(HashSet.class, null, null, null);
        assertNotSame(mapType, newMapType);
        assertEquals(HashSet.class, newMapType.getRawClass());
        assertEquals(keyType, newMapType.getKeyType());
        assertEquals(valueType, newMapType.getValueType());
    }

    @Test
    public void testMapTypeWithKeyTypeHandler() {
        JavaType keyType = new SimpleType("java.lang.String");
        JavaType valueType = new SimpleType("java.lang.Integer");
        Object keyTypeHandler = new Object();
        JavaType mapType = new MapType(Map.class, null, null, null, keyType, valueType, null, null, false);
        JavaType newMapType = mapType.withKeyTypeHandler(keyTypeHandler);
        assertEquals(keyTypeHandler, newMapType.getKeyTypeHandler());
    }

    @Test
    public void testMapTypeWithKeyValueHandler() {
        JavaType keyType = new SimpleType("java.lang.String");
        JavaType valueType = new SimpleType("java.lang.Integer");
        Object keyValueHandler = new Object();
        JavaType mapType = new MapType(Map.class, null, null, null, keyType, valueType, null, null, false);
        JavaType newMapType = mapType.withKeyValueHandler(keyValueHandler);
        assertEquals(keyValueHandler, newMapType.getKeyValueHandler());
    }
}