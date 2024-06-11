package com.yourpackagename.test;

import static org.junit.Assert.*;

import java.lang.reflect.TypeVariable;
import java.util.Arrays;

import org.junit.Test;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.MapLikeType;

public class MapLikeTypeTest {

    @Test
    public void testMapLikeTypeConstructor() {
        JavaType keyType = new BasicJavaType(String.class);
        JavaType valueType = new BasicJavaType(Integer.class);
        MapLikeType mapLikeType = new MapLikeType(Map.class, null, null, null, keyType, valueType);
        assertEquals(Map.class, mapLikeType.getType());
        assertEquals(keyType, mapLikeType.getKeyType());
        assertEquals(valueType, mapLikeType.getValueType());
    }

    @Test
    public void testMapLikeTypeUpgradeFrom() {
        JavaType baseType = new BasicJavaType(HashMap.class);
        JavaType keyType = new BasicJavaType(String.class);
        JavaType valueType = new BasicJavaType(Integer.class);
        MapLikeType mapLikeType = MapLikeType.upgradeFrom(baseType, keyType, valueType);
        assertEquals(Map.class, mapLikeType.getType());
        assertEquals(keyType, mapLikeType.getKeyType());
        assertEquals(valueType, mapLikeType.getValueType());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testMapLikeTypeUpgradeFromInvalidBaseType() {
        JavaType baseType = new BasicJavaType(Integer.class);
        JavaType keyType = new BasicJavaType(String.class);
        JavaType valueType = new BasicJavaType(Integer.class);
        MapLikeType.upgradeFrom(baseType, keyType, valueType);
    }

    @Test
    public void testMapLikeTypeFactory() {
        JavaType keyType = new BasicJavaType(String.class);
        JavaType valueType = new BasicJavaType(Integer.class);
        MapLikeType mapLikeType = new MapLikeType(null, keyType, valueType);
        assertEquals(Map.class, mapLikeType.getType());
        assertEquals(keyType, mapLikeType.getKeyType());
        assertEquals(valueType, mapLikeType.getValueType());
    }

    @Test
    public void testMapLikeTypeWithKeyType() {
        JavaType baseType = new BasicJavaType(HashMap.class);
        JavaType newKeyType = new BasicJavaType(Long.class);
        MapLikeType mapLikeType = new MapLikeType(baseType, newKeyType);
        assertEquals(HashMap.class, mapLikeType.getType());
        assertEquals(newKeyType, mapLikeType.getKeyType());
        assertEquals(baseType.getValueType(), mapLikeType.getValueType());
    }

    @Test
    public void testMapLikeTypeWithContentType() {
        JavaType baseType = new BasicJavaType(HashMap.class);
        JavaType newValueType = new BasicJavaType(Double.class);
        MapLikeType mapLikeType = new MapLikeType(baseType).withContentType(newValueType);
        assertEquals(HashMap.class, mapLikeType.getType());
        assertEquals(baseType.getKeyType(), mapLikeType.getKeyType());
        assertEquals(newValueType, mapLikeType.getValueType());
    }

    @Test
    public void testMapLikeTypeWithTypeHandler() {
        JavaType baseType = new BasicJavaType(HashMap.class);
        Object handler = new Object();
        MapLikeType mapLikeType = new MapLikeType(baseType).withTypeHandler(handler);
        assertEquals(handler, mapLikeType.getTypeHandler());
    }

    @Test
    public void testMapLikeTypeWithContentTypeHandler() {
        JavaType baseType = new BasicJavaType(HashMap.class);
        Object handler = new Object();
        MapLikeType mapLikeType = new MapLikeType(baseType).withContentTypeHandler(handler);
        assertEquals(handler, mapLikeType.getValueType().getTypeHandler());
    }

    @Test
    public void testMapLikeTypeWithKeyTypeHandler() {
        JavaType baseType = new BasicJavaType(HashMap.class);
        Object handler = new Object();
        MapLikeType mapLikeType = new MapLikeType(baseType).withKeyTypeHandler(handler);
        assertEquals(handler, mapLikeType.getKeyType().getTypeHandler());
    }

    @Test
    public void testMapLikeTypeWithValueHandler() {
        JavaType baseType = new BasicJavaType(HashMap.class);
        Object handler = new Object();
        MapLikeType mapLikeType = new MapLikeType(baseType).withValueHandler(handler);
        assertEquals(handler, mapLikeType.getValueType().getValueHandler());
    }

    @Test
    public void testMapLikeTypeWithContentValueHandler() {
        JavaType baseType = new BasicJavaType(HashMap.class);
        Object handler = new Object();
        MapLikeType mapLikeType = new MapLikeType(baseType).withContentValueHandler(handler);
        assertEquals(handler, mapLikeType.getValueType().getValueHandler());
    }

    @Test
    public void testMapLikeTypeWithStaticTyping() {
        JavaType baseType = new BasicJavaType(HashMap.class);
        MapLikeType mapLikeType = new MapLikeType(baseType).withStaticTyping();
        assertTrue(mapLikeType.isStatic());
    }

    @Test
    public void testMapLikeTypeRefine() {
        JavaType rawType = new BasicJavaType(TreeMap.class);
        TypeBindings bindings = TypeBindings.emptyBindings();
        JavaType superClass = null;
        JavaType[] superInterfaces = new JavaType[] {List.class};
        MapLikeType mapLikeType = new MapLikeType(rawType, bindings, superClass,
                superInterfaces, null, null).refine(rawType, bindings, null, superInterfaces);
        assertEquals(TreeMap.class, mapLikeType.getType());
        assertEquals(rawType.getTypeParameters()[0], mapLikeType.getKeyType());
        assertEquals(rawType.getTypeParameters()[1], mapLikeType.getValueType());
    }

    @Test
    public void testMapLikeTypeIsContainerType() {
        JavaType baseType = new BasicJavaType(HashMap.class);
        assertTrue(new MapLikeType(baseType).isContainerType());
    }

    @Test
    public void testMapLikeTypeIsMapLikeType() {
        JavaType baseType = new BasicJavaType(HashMap.class);
        assertTrue(new MapLikeType(baseType).isMapLikeType());
    }

    @Test
    public void testMapLikeTypeIsTrueMapType() {
        JavaType baseType = new BasicJavaType(HashMap.class);
        assertTrue(new MapLikeType(baseType).isTrueMapType());
    }

    @Test
    public void testMapLikeTypeToString() {
        JavaType baseType = new BasicJavaType(HashMap.class);
        MapLikeType mapLikeType = new MapLikeType(baseType);
        String expected = String.format("[map-like type; class %s, %s -> %s]",
                baseType.getName(), baseType.getKeyType().toString(),
                baseType.getValueType().toString());
        assertEquals(expected, mapLikeType.toString());
    }

    @Test
    public void testMapLikeTypeEquals() {
        JavaType baseType1 = new BasicJavaType(HashMap.class);
        JavaType baseType2 = new BasicJavaType(HashMap.class);
        JavaType keyType = new BasicJavaType(String.class);
        JavaType valueType = new BasicJavaType(Integer.class);
        MapLikeType mapLikeType1 = new MapLikeType(baseType1, keyType, valueType);
        MapLikeType mapLikeType2 = new MapLikeType(baseType2, keyType, valueType);
        MapLikeType mapLikeType3 = new MapLikeType(baseType1, keyType, valueType);
        MapLikeType mapLikeType4 = new MapLikeType(baseType1, keyType, new BasicJavaType(Integer.class));

        assertTrue(mapLikeType1.equals(mapLikeType1));
        assertTrue(mapLikeType1.equals(mapLikeType3));
        assertTrue(mapLikeType1.equals(mapLikeType2.withKeyType(keyType)));
        assertTrue(mapLikeType1.equals(mapLikeType2.withContentType(valueType)));
        assertFalse(mapLikeType1.equals(mapLikeType4));
    }
}