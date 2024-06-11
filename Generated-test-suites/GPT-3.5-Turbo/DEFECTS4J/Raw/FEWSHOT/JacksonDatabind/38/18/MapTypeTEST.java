package com.fasterxml.jackson.databind.type;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class MapTypeTest {

    @Test
    public void testConstructWithJavaType() {
        JavaType keyType = new SimpleType(Object.class);
        JavaType valueType = new SimpleType(String.class);
        MapType mapType = new MapType(Map.class, keyType, valueType);

        assertEquals(Map.class, mapType.getRawClass());
        assertEquals(keyType, mapType.getKeyType());
        assertEquals(valueType, mapType.getValueType());
    }

    @Test
    public void testGetTypeHandler() {
        JavaType keyType = new SimpleType(Object.class);
        JavaType valueType = new SimpleType(String.class);
        MapType mapType = new MapType(Map.class, keyType, valueType);
        Object typeHandler = mapType.getTypeHandler();

        assertNull(typeHandler);
    }

    @Test
    public void testWithTypeHandler() {
        JavaType keyType = new SimpleType(Object.class);
        JavaType valueType = new SimpleType(String.class);
        MapType mapType = new MapType(Map.class, keyType, valueType);
        mapType = mapType.withTypeHandler(new StringTypeHandler());

        assertNotNull(mapType.getTypeHandler());
    }

    @Test
    public void testWithKeyValueHandler() {
        JavaType keyType = new SimpleType(Object.class);
        JavaType valueType = new SimpleType(String.class);
        MapType mapType = new MapType(Map.class, keyType, valueType);
        mapType = mapType.withKeyValueHandler(new StringKeyValueHandler());

        assertNotNull(mapType.getTypeHandler());
    }

    @Test
    public void testRefine() {
        JavaType keyType = new SimpleType(Object.class);
        JavaType valueType = new SimpleType(String.class);
        MapType baseType = new MapType(Map.class, keyType, valueType);
        JavaType superClass = new SimpleType(Object.class);
        JavaType[] superInterfaces = new JavaType[0];

        JavaType refinedType = baseType.refine(HashMap.class, new TypeBindings(), superClass, superInterfaces);

        assertEquals(HashMap.class, refinedType.getRawClass());
        assertEquals(keyType, refinedType.getKeyType());
        assertEquals(valueType, refinedType.getValueType());
    }

    @Test
    public void testWithKeyTypeHandler() {
        JavaType keyType = new SimpleType(Object.class);
        JavaType valueType = new SimpleType(String.class);
        MapType mapType = new MapType(Map.class, keyType, valueType);
        mapType = mapType.withKeyTypeHandler(new StringTypeHandler());

        assertNotNull(mapType.getKeyType().getTypeHandler());
    }

    @Test
    public void testWithContentValueHandler() {
        JavaType keyType = new SimpleType(Object.class);
        JavaType valueType = new SimpleType(String.class);
        MapType mapType = new MapType(Map.class, keyType, valueType);
        mapType = mapType.withContentValueHandler(new StringContentValueHandler());

        assertNotNull(mapType.getValueType().getValueHandler());
    }
}