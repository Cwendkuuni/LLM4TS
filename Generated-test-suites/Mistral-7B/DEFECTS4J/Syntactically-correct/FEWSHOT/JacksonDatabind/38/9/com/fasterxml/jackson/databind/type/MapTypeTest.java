package com.fasterxml.jackson.databind.type;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

import java.lang.reflect.Type;
import java.util.Arrays;

import org.junit.Test;

public class MapTypeTest {

    @Test
    public void testMapTypeConstruction() {
        JavaType keyType = new SimpleType("java.lang.String");
        JavaType valueType = new SimpleType("java.lang.Integer");
        JavaType mapType = MapType.construct(Map.class, null, null, null, keyType, valueType);
        assertEquals(Map.class, mapType.getRawClass());
        assertEquals(keyType, mapType.getKeyType());
        assertEquals(valueType, mapType.getValueType());
        assertEquals(false, mapType.isStatic());
        assertNull(mapType.getTypeHandler());
        assertNull(mapType.getTypeHandlerInstantiator());
        assertNull(mapType.getValueHandler());
        assertNull(mapType.getValueHandlerInstantiator());
    }

    @Test
    public void testMapTypeWithTypeHandler() {
        JavaType keyType = new SimpleType("java.lang.String");
        JavaType valueType = new SimpleType("java.lang.Integer");
        Object valueHandler = new Object() {};
        JavaType mapType = MapType.construct(Map.class, null, null, null, keyType, valueType)
                .withTypeHandler(valueHandler);
        assertEquals(Map.class, mapType.getRawClass());
        assertEquals(keyType, mapType.getKeyType());
        assertEquals(valueType, mapType.getValueType());
        assertEquals(valueHandler, mapType.getTypeHandler());
        assertNull(mapType.getTypeHandlerInstantiator());
        assertNull(mapType.getValueHandler());
        assertNull(mapType.getValueHandlerInstantiator());
        assertFalse(mapType.isStatic());
    }

    @Test
    public void testMapTypeWithValueHandler() {
        JavaType keyType = new SimpleType("java.lang.String");
        JavaType valueType = new SimpleType("java.lang.Integer");
        Object valueHandler = new Object() {};
        JavaType mapType = MapType.construct(Map.class, null, null, null, keyType, valueType)
                .withValueHandler(valueHandler);
        assertEquals(Map.class, mapType.getRawClass());
        assertEquals(keyType, mapType.getKeyType());
        assertEquals(valueType, mapType.getValueType());
        assertNull(mapType.getTypeHandler());
        assertNull(mapType.getTypeHandlerInstantiator());
        assertEquals(valueHandler, mapType.getValueHandler());
        assertNull(mapType.getValueHandlerInstantiator());
        assertFalse(mapType.isStatic());
    }

    @Test
    public void testMapTypeWithBothHandlers() {
        JavaType keyType = new SimpleType("java.lang.String");
        JavaType valueType = new SimpleType("java.lang.Integer");
        Object valueHandler = new Object() {};
        Object typeHandler = new Object() {};
        JavaType mapType = MapType.construct(Map.class, null, null, null, keyType, valueType)
                .withTypeHandler(typeHandler)
                .withValueHandler(valueHandler);
        assertEquals(Map.class, mapType.getRawClass());
        assertEquals(keyType, mapType.getKeyType());
        assertEquals(valueType, mapType.getValueType());
        assertEquals(typeHandler, mapType.getTypeHandler());
        assertNull(mapType.getTypeHandlerInstantiator());
        assertEquals(valueHandler, mapType.getValueHandler());
        assertNull(mapType.getValueHandlerInstantiator());
        assertFalse(mapType.isStatic());
    }

    @Test
    public void testMapTypeWithStaticTyping() {
        JavaType keyType = new SimpleType("java.lang.String");
        JavaType valueType = new SimpleType("java.lang.Integer");
        JavaType mapType = MapType.construct(Map.class, null, null, null, keyType, valueType).withStaticTyping();
        assertEquals(Map.class, mapType.getRawClass());
        assertEquals(keyType, mapType.getKeyType());
        assertEquals(valueType, mapType.getValueType());
        assertSame(mapType, mapType.withStaticTyping());
        assertTrue(mapType.isStatic());
    }

    @Test
    public void testMapTypeWithContentType() {
        JavaType keyType = new SimpleType("java.lang.String");
        JavaType valueType = new SimpleType("java.lang.Integer");
        JavaType contentType = new SimpleType("java.lang.Float");
        JavaType mapType = MapType.construct(Map.class, null, null, null, keyType, valueType)
                .withContentType(contentType);
        assertEquals(Map.class, mapType.getRawClass());
        assertEquals(keyType, mapType.getKeyType());
        assertEquals(contentType, mapType.getValueType());
        assertNull(mapType.getTypeHandler());
        assertNull(mapType.getTypeHandlerInstantiator());
        assertNull(mapType.getValueHandler());
        assertNull(mapType.getValueHandlerInstantiator());
        assertFalse(mapType.isStatic());
    }

    @Test
    public void testMapTypeWithKeyType() {
        JavaType keyType1 = new SimpleType("java.lang.String");
        JavaType valueType = new SimpleType("java.lang.Integer");
        JavaType keyType2 = new SimpleType("java.lang.Integer");
        JavaType mapType1 = MapType.construct(Map.class, null, null, null, keyType1, valueType);
        JavaType mapType2 = mapType1.withKeyType(keyType2);
        assertEquals(Map.class, mapType1.getRawClass());
        assertEquals(keyType1, mapType1.getKeyType());
        assertEquals(valueType, mapType1.getValueType());
        assertEquals(Map.class, mapType2.getRawClass());
        assertEquals(keyType2, mapType2.getKeyType());
        assertEquals(valueType, mapType2.getValueType());
        assertNotEquals(mapType1, mapType2);
    }

    @Test
    public void testMapTypeWithRefining() {
        JavaType keyType = new SimpleType("java.lang.String");
        JavaType valueType = new SimpleType("java.lang.Integer");
        JavaType mapType1 = MapType.construct(Map.class, null, null, null, keyType, valueType);
        JavaType mapType2 = mapType1.refine(HashMap.class, null, null, null);
        assertEquals(HashMap.class, mapType2.getRawClass());
        assertEquals(keyType, mapType2.getKeyType());
        assertEquals(valueType, mapType2.getValueType());
        assertNotEquals(mapType1, mapType2);
    }

    @Test
    public void testMapTypeWithKeyTypeHandler() {
        JavaType keyType = new SimpleType("java.lang.String");
        JavaType valueType = new SimpleType("java.lang.Integer");
        Object keyHandler = new Object() {};
        JavaType mapType = MapType.construct(Map.class, null, null, null, keyType, valueType)
                .withKeyTypeHandler(keyHandler);
        assertEquals(Map.class, mapType.getRawClass());
        assertEquals(keyType, mapType.getKeyType());
        assertEquals(valueType, mapType.getValueType());
        assertNull(mapType.getTypeHandler());
        assertNull(mapType.getTypeHandlerInstantiator());
        assertEquals(keyHandler, mapType.getKeyTypeHandler());
        assertNull(mapType.getKeyTypeHandlerInstantiator());
        assertNull(mapType.getValueHandler());
        assertNull(mapType.getValueHandlerInstantiator());
        assertFalse(mapType.isStatic());
    }

    @Test
    public void testMapTypeWithKeyValueHandler() {
        JavaType keyType = new SimpleType("java.lang.String");
        JavaType valueType = new SimpleType("java.lang.Integer");
        Object keyValueHandler = new Object() {};
        JavaType mapType = MapType.construct(Map.class, null, null, null, keyType, valueType)
                .withKeyValueHandler(keyValueHandler);
        assertEquals(Map.class, mapType.getRawClass());
        assertEquals(keyType, mapType.getKeyType());
        assertEquals(valueType, mapType.getValueType());
        assertNull(mapType.getTypeHandler());
        assertNull(mapType.getTypeHandlerInstantiator());
        assertNull(mapType.getValueHandler());
        assertEquals(keyValueHandler, mapType.getKeyValueHandler());
        assertNull(mapType.getKeyValueHandlerInstantiator());
        assertFalse(mapType.isStatic());
    }

    @Test
    public void testMapTypeToString() {
        JavaType keyType = new SimpleType("java.lang.String");
        JavaType valueType = new SimpleType("java.lang.Integer");
        JavaType mapType = MapType.construct(Map.class, null, null, null, keyType, valueType);
        assertEquals("[map type; class Map, java.lang.String -> java.lang.Integer]", mapType.toString());
    }
}