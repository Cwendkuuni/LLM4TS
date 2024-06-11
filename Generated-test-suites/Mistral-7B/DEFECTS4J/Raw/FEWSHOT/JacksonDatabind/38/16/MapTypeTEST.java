package com.fasterxml.jackson.databind.type;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class MapTypeTest {

    @Test
    public void testConstruct() {
        JavaType keyType = new SimpleType("java.lang.String");
        JavaType valueType = new SimpleType("java.lang.Integer");
        JavaType mapType = MapType.construct(Map.class, null, null, null, keyType, valueType);
        assertEquals(Map.class, mapType.getRawClass());
        assertEquals(keyType, mapType.getKeyType());
        assertEquals(valueType, mapType.getValueType());
        assertNull(mapType.getValueHandler());
        assertNull(mapType.getTypeHandler());
        assertNull(mapType.getBindings());
        assertFalse(mapType.isStatic());
    }

    @Test
    public void testConstructWithBindings() {
        JavaType keyType = new SimpleType("java.lang.String");
        JavaType valueType = new SimpleType("java.lang.Integer");
        JavaType mapType = MapType.construct(Map.class, new TypeBindings(), null, null, keyType, valueType);
        assertEquals(Map.class, mapType.getRawClass());
        assertEquals(keyType, mapType.getKeyType());
        assertEquals(valueType, mapType.getValueType());
        assertNull(mapType.getValueHandler());
        assertNull(mapType.getTypeHandler());
        assertNotNull(mapType.getBindings());
        assertFalse(mapType.isStatic());
    }

    @Test
    public void testConstructWithValueHandler() {
        JavaType keyType = new SimpleType("java.lang.String");
        JavaType valueType = new SimpleType("java.lang.Integer");
        Object valueHandler = new Object();
        JavaType mapType = MapType.construct(Map.class, null, null, null, keyType, valueType, valueHandler);
        assertEquals(Map.class, mapType.getRawClass());
        assertEquals(keyType, mapType.getKeyType());
        assertEquals(valueType, mapType.getValueType());
        assertEquals(valueHandler, mapType.getValueHandler());
        assertNull(mapType.getTypeHandler());
        assertNull(mapType.getBindings());
        assertFalse(mapType.isStatic());
    }

    @Test
    public void testConstructWithTypeHandler() {
        JavaType keyType = new SimpleType("java.lang.String");
        JavaType valueType = new SimpleType("java.lang.Integer");
        Object typeHandler = new Object();
        JavaType mapType = MapType.construct(Map.class, null, null, null, keyType, valueType, null, typeHandler);
        assertEquals(Map.class, mapType.getRawClass());
        assertEquals(keyType, mapType.getKeyType());
        assertEquals(valueType, mapType.getValueType());
        assertNull(mapType.getValueHandler());
        assertEquals(typeHandler, mapType.getTypeHandler());
        assertNull(mapType.getBindings());
        assertFalse(mapType.isStatic());
    }

    @Test
    public void testConstructWithValueHandlerAndTypeHandler() {
        JavaType keyType = new SimpleType("java.lang.String");
        JavaType valueType = new SimpleType("java.lang.Integer");
        Object valueHandler = new Object();
        Object typeHandler = new Object();
        JavaType mapType = MapType.construct(Map.class, null, null, null, keyType, valueType, valueHandler, typeHandler);
        assertEquals(Map.class, mapType.getRawClass());
        assertEquals(keyType, mapType.getKeyType());
        assertEquals(valueType, mapType.getValueType());
        assertEquals(valueHandler, mapType.getValueHandler());
        assertEquals(typeHandler, mapType.getTypeHandler());
        assertNull(mapType.getBindings());
        assertFalse(mapType.isStatic());
    }

    @Test
    public void testConstructWithStaticTyping() {
        JavaType keyType = new SimpleType("java.lang.String");
        JavaType valueType = new SimpleType("java.lang.Integer");
        JavaType mapType = MapType.construct(Map.class, null, null, null, keyType, valueType).withStaticTyping();
        assertEquals(Map.class, mapType.getRawClass());
        assertEquals(keyType, mapType.getKeyType());
        assertEquals(valueType, mapType.getValueType());
        assertNull(mapType.getValueHandler());
        assertNull(mapType.getTypeHandler());
        assertNull(mapType.getBindings());
        assertTrue(mapType.isStatic());
    }

    @Test
    public void testWithTypeHandler() {
        JavaType keyType = new SimpleType("java.lang.String");
        JavaType valueType = new SimpleType("java.lang.Integer");
        Object valueHandler = new Object();
        JavaType mapType = new MapType(Map.class, null, null, null, keyType, valueType)
                .withTypeHandler(new Object());
        assertEquals(Map.class, mapType.getRawClass());
        assertEquals(keyType, mapType.getKeyType());
        assertEquals(valueType, mapType.getValueType());
        assertNull(mapType.getValueHandler());
        assertEquals(new Object(), mapType.getTypeHandler());
        assertNull(mapType.getBindings());
        assertFalse(mapType.isStatic());
    }

    @Test
    public void testWithContentTypeHandler() {
        JavaType keyType = new SimpleType("java.lang.String");
        JavaType valueType = new SimpleType("java.lang.Integer");
        Object contentTypeHandler = new Object();
        JavaType mapType = new MapType(Map.class, null, null, null, keyType, valueType)
                .withContentTypeHandler(contentTypeHandler);
        assertEquals(Map.class, mapType.getRawClass());
        assertEquals(keyType, mapType.getKeyType());
        JavaType contentValueType = mapType.getValueType().withTypeHandler(contentTypeHandler);
        assertEquals(valueType, mapType.getValueType());
        assertEquals(contentTypeHandler, mapType.getValueType().getTypeHandler());
        assertNull(mapType.getValueHandler());
        assertNull(mapType.getTypeHandler());
        assertNull(mapType.getBindings());
        assertFalse(mapType.isStatic());
    }

    @Test
    public void testWithValueHandler() {
        JavaType keyType = new SimpleType("java.lang.String");
        JavaType valueType = new SimpleType("java.lang.Integer");
        Object valueHandler = new Object();
        JavaType mapType = new MapType(Map.class, null, null, null, keyType, valueType)
                .withValueHandler(valueHandler);
        assertEquals(Map.class, mapType.getRawClass());
        assertEquals(keyType, mapType.getKeyType());
        assertEquals(valueType, mapType.getValueType());
        assertEquals(valueHandler, mapType.getValueHandler());
        assertNull(mapType.getTypeHandler());
        assertNull(mapType.getBindings());
        assertFalse(mapType.isStatic());
    }

    @Test
    public void testWithContentValueHandler() {
        JavaType keyType = new SimpleType("java.lang.String");
        JavaType valueType = new SimpleType("java.lang.Integer");
        Object contentValueHandler = new Object();
        JavaType mapType = new MapType(Map.class, null, null, null, keyType, valueType)
                .withContentValueHandler(contentValueHandler);
        assertEquals(Map.class, mapType.getRawClass());
        assertEquals(keyType, mapType.getKeyType());
        JavaType contentValueType = mapType.getValueType();
        assertEquals(valueType, contentValueType.getValueType());
        assertEquals(contentValueHandler, contentValueType.getValueHandler());
        assertNull(mapType.getValueHandler());
        assertNull(mapType.getTypeHandler());
        assertNull(mapType.getBindings());
        assertFalse(mapType.isStatic());
    }

    @Test
    public void testWithStaticTyping() {
        JavaType keyType = new SimpleType("java.lang.String");
        JavaType valueType = new SimpleType("java.lang.Integer");
        JavaType mapType = new MapType(Map.class, null, null, null, keyType, valueType)
                .withStaticTyping();
        assertEquals(Map.class, mapType.getRawClass());
        assertEquals(keyType, mapType.getKeyType());
        assertEquals(valueType, mapType.getValueType());
        assertNull(mapType.getValueHandler());
        assertNull(mapType.getTypeHandler());
        assertNull(mapType.getBindings());
        assertTrue(mapType.isStatic());
    }

    @Test
    public void testWithContentType() {
        JavaType keyType = new SimpleType("java.lang.String");
        JavaType valueType = new SimpleType("java.lang.Integer");
        JavaType contentType = new SimpleType("java.lang.String");
        JavaType mapType = new MapType(Map.class, null, null, null, keyType, valueType)
                .withContentType(contentType);
        assertEquals(Map.class, mapType.getRawClass());
        assertEquals(keyType, mapType.getKeyType());
        assertEquals(contentType, mapType.getValueType());
        assertNull(mapType.getValueHandler());
        assertNull(mapType.getTypeHandler());
        assertNull(mapType.getBindings());
        assertFalse(mapType.isStatic());
    }

    @Test
    public void testWithKeyType() {
        JavaType keyType1 = new SimpleType("java.lang.String");
        JavaType valueType = new SimpleType("java.lang.Integer");
        JavaType mapType = new MapType(Map.class, null, null, null, keyType1, valueType);
        JavaType mapTypeWithKeyType = mapType.withKeyType(new SimpleType("java.lang.Integer"));
        assertEquals(Map.class, mapTypeWithKeyType.getRawClass());
        assertEquals(new SimpleType("java.lang.Integer"), mapTypeWithKeyType.getKeyType());
        assertEquals(valueType, mapTypeWithKeyType.getValueType());
        assertNull(mapTypeWithKeyType.getValueHandler());
        assertNull(mapTypeWithKeyType.getTypeHandler());
        assertNull(mapTypeWithKeyType.getBindings());
        assertFalse(mapTypeWithKeyType.isStatic());
    }

    @Test
    public void testRefine() {
        JavaType keyType = new SimpleType("java.lang.String");
        JavaType valueType = new SimpleType("java.lang.Integer");
        JavaType mapType = new MapType(Map.class, null, null, null, keyType, valueType);
        JavaType mapTypeRefined = mapType.refine(HashMap.class, null, null, null);
        assertEquals(HashMap.class, mapTypeRefined.getRawClass());
        assertEquals(keyType, mapTypeRefined.getKeyType());
        assertEquals(valueType, mapTypeRefined.getValueType());
        assertNull(mapTypeRefined.getValueHandler());
        assertNull(mapTypeRefined.getTypeHandler());
        assertNull(mapTypeRefined.getBindings());
        assertFalse(mapTypeRefined.isStatic());
    }

    @Test
    public void testToString() {
        JavaType keyType = new SimpleType("java.lang.String");
        JavaType valueType = new SimpleType("java.lang.Integer");
        JavaType mapType = new MapType(Map.class, null, null, null, keyType, valueType);
        assertEquals("[map type; class Map<java.lang.String,java.lang.Integer>, java.lang.String -> java.lang.Integer]", mapType.toString());
    }
}