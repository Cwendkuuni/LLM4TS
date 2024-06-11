package com.example.junit;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.lang.reflect.TypeVariable;
import java.util.Arrays;

import org.junit.Test;

import com.fasterxml.jackson.databind.JavaType;

public class MapLikeTypeTest {

    @Test
    public void testMapLikeTypeConstruction() {
        JavaType keyType = JavaType.constructType(String.class, TypeVariable.emptyArray());
        JavaType valueType = JavaType.constructType(Integer.class, TypeVariable.emptyArray());
        MapLikeType mapLikeType = new MapLikeType(Map.class, null, null, null, keyType, valueType);
        assertNotNull(mapLikeType);
        assertSame(Map.class, mapLikeType.getRawClass());
        assertEquals(keyType, mapLikeType.getKeyType());
        assertEquals(valueType, mapLikeType.getValueType());
    }

    @Test
    public void testMapLikeTypeUpgradeFrom() {
        JavaType baseType = JavaType.constructType(HashMap.class, TypeVariable.emptyArray());
        JavaType keyType = JavaType.constructType(String.class, TypeVariable.emptyArray());
        JavaType valueType = JavaType.constructType(Integer.class, TypeVariable.emptyArray());
        MapLikeType mapLikeType = MapLikeType.upgradeFrom(baseType, keyType, valueType);
        assertNotNull(mapLikeType);
        assertSame(Map.class, mapLikeType.getRawClass());
        assertEquals(keyType, mapLikeType.getKeyType());
        assertEquals(valueType, mapLikeType.getValueType());
    }

    @Test
    public void testMapLikeTypeIsContainerType() {
        JavaType keyType = JavaType.constructType(String.class, TypeVariable.emptyArray());
        JavaType valueType = JavaType.constructType(Integer.class, TypeVariable.emptyArray());
        MapLikeType mapLikeType = new MapLikeType(Map.class, null, null, null, keyType, valueType);
        assertTrue(mapLikeType.isContainerType());
        assertTrue(mapLikeType.isMapLikeType());
    }

    @Test
    public void testMapLikeTypeIsTrueMapType() {
        JavaType keyType = JavaType.constructType(String.class, TypeVariable.emptyArray());
        JavaType valueType = JavaType.constructType(Integer.class, TypeVariable.emptyArray());
        MapLikeType mapLikeType = new MapLikeType(Map.class, null, null, null, keyType, valueType);
        assertTrue(mapLikeType.isTrueMapType());
    }

    @Test
    public void testMapLikeTypeGetKeyType() {
        JavaType keyType = JavaType.constructType(String.class, TypeVariable.emptyArray());
        JavaType valueType = JavaType.constructType(Integer.class, TypeVariable.emptyArray());
        MapLikeType mapLikeType = new MapLikeType(Map.class, null, null, null, keyType, valueType);
        assertEquals(keyType, mapLikeType.getKeyType());
    }

    @Test
    public void testMapLikeTypeGetValueType() {
        JavaType keyType = JavaType.constructType(String.class, TypeVariable.emptyArray());
        JavaType valueType = JavaType.constructType(Integer.class, TypeVariable.emptyArray());
        MapLikeType mapLikeType = new MapLikeType(Map.class, null, null, null, keyType, valueType);
        assertEquals(valueType, mapLikeType.getValueType());
    }

    @Test
    public void testMapLikeTypeHashCode() {
        JavaType keyType = JavaType.constructType(String.class, TypeVariable.emptyArray());
        JavaType valueType = JavaType.constructType(Integer.class, TypeVariable.emptyArray());
        MapLikeType mapLikeType1 = new MapLikeType(Map.class, null, null, null, keyType, valueType);
        MapLikeType mapLikeType2 = new MapLikeType(Map.class, null, null, null, keyType, valueType);
        assertEquals(mapLikeType1.hashCode(), mapLikeType2.hashCode());

        JavaType keyType2 = JavaType.constructType(String[].class, TypeVariable.emptyArray());
        JavaType valueType2 = JavaType.constructType(Integer[].class, TypeVariable.emptyArray());
        MapLikeType mapLikeType3 = new MapLikeType(Map.class, null, null, null, keyType2, valueType2);
        assertNotEquals(mapLikeType1.hashCode(), mapLikeType3.hashCode());
    }

    @Test
    public void testMapLikeTypeToString() {
        JavaType keyType = JavaType.constructType(String.class, TypeVariable.emptyArray());
        JavaType valueType = JavaType.constructType(Integer.class, TypeVariable.emptyArray());
        MapLikeType mapLikeType = new MapLikeType(Map.class, null, null, null, keyType, valueType);
        String toString = mapLikeType.toString();
        assertTrue(toString.startsWith("[map-like type; class"));
        assertTrue(toString.endsWith(">]"));
        String[] split = toString.split("; ");
        assertEquals(3, split.length);
        assertEquals("Map", split[0].split(" ")[1]);
        assertEquals("class <String> -> class <Integer>", split[1]);
    }

    @Test
    public void testMapLikeTypeEquals() {
        JavaType keyType = JavaType.constructType(String.class, TypeVariable.emptyArray());
        JavaType valueType = JavaType.constructType(Integer.class, TypeVariable.emptyArray());
        MapLikeType mapLikeType1 = new MapLikeType(Map.class, null, null, null, keyType, valueType);
        MapLikeType mapLikeType2 = new MapLikeType(Map.class, null, null, null, keyType, valueType);
        assertTrue(mapLikeType1.equals(mapLikeType2));

        JavaType keyType2 = JavaType.constructType(String[].class, TypeVariable.emptyArray());
        JavaType valueType2 = JavaType.constructType(Integer[].class, TypeVariable.emptyArray());
        MapLikeType mapLikeType3 = new MapLikeType(Map.class, null, null, null, keyType2, valueType2);
        assertFalse(mapLikeType1.equals(mapLikeType3));

        JavaType keyType3 = JavaType.constructType(String.class, TypeVariable.emptyArray());
        JavaType valueType3 = JavaType.constructType(Integer.class, TypeVariable.emptyArray());
        MapLikeType mapLikeType4 = new MapLikeType(List.class, null, null, null, keyType3, valueType3);
        assertFalse(mapLikeType1.equals(mapLikeType4));
    }

    @Test
    public void testMapLikeTypeWithKeyType() {
        JavaType keyType = JavaType.constructType(String.class, TypeVariable.emptyArray());
        JavaType valueType = JavaType.constructType(Integer.class, TypeVariable.emptyArray());
        MapLikeType mapLikeType = new MapLikeType(Map.class, null, null, null, keyType, valueType);
        JavaType keyType2 = JavaType.constructType(Integer.class, TypeVariable.emptyArray());
        MapLikeType mapLikeType2 = mapLikeType.withKeyType(keyType2);
        assertNotSame(mapLikeType, mapLikeType2);
        assertEquals(keyType2, mapLikeType2.getKeyType());
        assertEquals(valueType, mapLikeType2.getValueType());
    }

    @Test
    public void testMapLikeTypeWithValueType() {
        JavaType keyType = JavaType.constructType(String.class, TypeVariable.emptyArray());
        JavaType valueType = JavaType.constructType(Integer.class, TypeVariable.emptyArray());
        MapLikeType mapLikeType = new MapLikeType(Map.class, null, null, null, keyType, valueType);
        JavaType valueType2 = JavaType.constructType(Double.class, TypeVariable.emptyArray());
        MapLikeType mapLikeType2 = mapLikeType.withValueType(valueType2);
        assertNotSame(mapLikeType, mapLikeType2);
        assertEquals(keyType, mapLikeType2.getKeyType());
        assertEquals(valueType2, mapLikeType2.getValueType());
    }

    @Test
    public void testMapLikeTypeWithTypeHandler() {
        JavaType keyType = JavaType.constructType(String.class, TypeVariable.emptyArray());
        JavaType valueType = JavaType.constructType(Integer.class, TypeVariable.emptyArray());
        MapLikeType mapLikeType = new MapLikeType(Map.class, null, null, null, keyType, valueType);
        Object handler = new Object();
        MapLikeType mapLikeType2 = mapLikeType.withTypeHandler(handler);
        assertNotSame(mapLikeType, mapLikeType2);
        assertEquals(mapLikeType.getHandlers(), mapLikeType2.getHandlers());
        assertNull(mapLikeType.getTypeHandler());
        assertSame(handler, mapLikeType2.getTypeHandler());
    }

    @Test
    public void testMapLikeTypeWithContentTypeHandler() {
        JavaType keyType = JavaType.constructType(String.class, TypeVariable.emptyArray());
        JavaType valueType = JavaType.constructType(Integer[].class, TypeVariable.emptyArray());
        MapLikeType mapLikeType = new MapLikeType(Map.class, null, null, null, keyType, valueType);
        Object handler = new Object();
        JavaType contentType = JavaType.constructType(Integer.class, TypeVariable.emptyArray());
        MapLikeType mapLikeType2 = mapLikeType.withContentTypeHandler(handler);
        assertNotSame(mapLikeType, mapLikeType2);
        assertEquals(mapLikeType.getHandlers(), mapLikeType2.getHandlers());
        assertSame(mapLikeType.getValueType().getTypeHandler(), mapLikeType2.getValueType().getTypeHandler());
        assertNull(mapLikeType.getValueType().getValueHandler());
        assertSame(handler, mapLikeType2.getValueType().getValueHandler());
    }

    @Test
    public void testMapLikeTypeWithValueHandler() {
        JavaType keyType = JavaType.constructType(String.class, TypeVariable.emptyArray());
        JavaType valueType = JavaType.constructType(Integer.class, TypeVariable.emptyArray());
        MapLikeType mapLikeType = new MapLikeType(Map.class, null, null, null, keyType, valueType);
        Object handler = new Object();
        MapLikeType mapLikeType2 = mapLikeType.withValueHandler(handler);
        assertNotSame(mapLikeType, mapLikeType2);
        assertEquals(mapLikeType.getHandlers(), mapLikeType2.getHandlers());
        assertSame(mapLikeType.getValueType().getValueHandler(), mapLikeType2.getValueType().getValueHandler());
        assertNull(mapLikeType.getValueType().getTypeHandler());
        assertSame(handler, mapLikeType2.getValueType().getValueHandler());
    }

    @Test
    public void testMapLikeTypeWithContentValueHandler() {
        JavaType keyType = JavaType.constructType(String.class, TypeVariable.emptyArray());
        JavaType valueType = JavaType.constructType(Integer[].class, TypeVariable.emptyArray());
        MapLikeType mapLikeType = new MapLikeType(Map.class, null, null, null, keyType, valueType);
        Object handler = new Object();
        JavaType contentType = JavaType.constructType(Integer.class, TypeVariable.emptyArray());
        MapLikeType mapLikeType2 = mapLikeType.withContentValueHandler(handler);
        assertNotSame(mapLikeType, mapLikeType2);
        assertEquals(mapLikeType.getHandlers(), mapLikeType2.getHandlers());
        assertSame(mapLikeType.getValueType().getValueHandler(), mapLikeType2.getValueType().getValueHandler());
        assertSame(contentType, mapLikeType2.getValueType());
        assertNull(mapLikeType2.getValueType().getTypeHandler());
        assertSame(handler, mapLikeType2.getValueType().getValueHandler());
    }

    @Test
    public void testMapLikeTypeWithStaticTyping() {
        JavaType keyType = JavaType.constructType(String.class, TypeVariable.emptyArray());
        JavaType valueType = JavaType.constructType(Integer.class, TypeVariable.emptyArray());
        MapLikeType mapLikeType = new MapLikeType(Map.class, null, null, null, keyType, valueType);
        MapLikeType mapLikeType2 = mapLikeType.withStaticTyping();
        assertNotSame(mapLikeType, mapLikeType2);
        assertTrue(mapLikeType2.isStatic());
        assertFalse(mapLikeType.isStatic());
    }

    @Test
    public void testMapLikeTypeRefine() {
        JavaType keyType = JavaType.constructType(String.class, TypeVariable.emptyArray());
        JavaType valueType = JavaType.constructType(Integer.class, TypeVariable.emptyArray());
        MapLikeType mapLikeType = new MapLikeType(Map.class, null, null, null, keyType, valueType);
        JavaType rawType = JavaType.constructType(HashMap.class, TypeVariable.emptyArray());
        TypeBindings typeBindings = TypeBindings.emptyBindings();
        JavaType superClass = JavaType.constructType(Map.class, TypeVariable.emptyArray());
        JavaType[] superInterfaces = new JavaType[]{ JavaType.constructType(Collection.class, TypeVariable.emptyArray()) };
        MapLikeType mapLikeTypeRefined = mapLikeType.refine(rawType, typeBindings, superClass, superInterfaces);
        assertSame(mapLikeType, mapLikeTypeRefined);

        JavaType keyType2 = JavaType.constructType(String[].class, TypeVariable.emptyArray());
        JavaType valueType2 = JavaType.constructType(Integer[].class, TypeVariable.emptyArray());
        MapLikeType mapLikeType2Refined = mapLikeType.refine(rawType, typeBindings, superClass, new JavaType[] { JavaType.constructType(Map.class, new TypeVariable[] { TypeVariable.getUnknownTypeVariable("K"), TypeVariable.getUnknownTypeVariable("V") }) });
        assertNotSame(mapLikeType, mapLikeType2Refined);
        assertEquals(keyType2, mapLikeType2Refined.getKeyType());
        assertEquals(valueType2, mapLikeType2Refined.getValueType());
    }

    @Test
    public void testMapLikeTypeGetErasedSignature() {
        JavaType keyType = JavaType.constructType(String.class, TypeVariable.emptyArray());
        JavaType valueType = JavaType.constructType(Integer.class, TypeVariable.emptyArray());
        MapLikeType mapLikeType = new MapLikeType(Map.class, null, null, null, keyType, valueType);
        StringBuilder sb = new StringBuilder();
        mapLikeType.getErasedSignature(sb);
        String signature = sb.toString();
        assertTrue(signature.startsWith("[map => java.util.Map]"));
        String[] split = signature.split(" => ");
        assertEquals(2, split.length);
        assertEquals("java.util.Map", split[0]);
        StringBuilder sb2 = new StringBuilder();
        mapLikeType.getGenericSignature(sb2);
        String genericSignature = sb2.toString();
        assertTrue(genericSignature.startsWith("[map-like type; class <String> -> <Integer>]"));
        assertFalse(genericSignature.contains("[static]"));
    }
}