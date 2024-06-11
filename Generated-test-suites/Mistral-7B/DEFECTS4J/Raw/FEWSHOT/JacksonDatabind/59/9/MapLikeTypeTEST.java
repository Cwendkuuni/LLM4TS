package com.example.test.jackson.databind.type;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.lang.reflect.TypeVariable;
import java.util.Arrays;

import org.junit.Test;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.TypeBase;
import com.fasterxml.jackson.databind.TypeBindings;
import com.fasterxml.jackson.databind.type.MapLikeType;

public class MapLikeTypeTest {

    @Test
    public void testMapLikeTypeConstruction() {
        JavaType keyType = new SimpleType("String");
        JavaType valueType = new SimpleType("Integer");
        JavaType mapType = new ClassType(Map.class);

        MapLikeType mapLikeType = new MapLikeType(mapType, TypeBindings.emptyBindings(),
                null, null, keyType, valueType, null, null, false);
        assertEquals(Map.class, mapLikeType.getType());
        assertEquals(keyType, mapLikeType.getKeyType());
        assertEquals(valueType, mapLikeType.getContentType());
        assertNull(mapLikeType.getValueHandler());
        assertNull(mapLikeType.getTypeHandler());
        assertFalse(mapLikeType.isStaticTyping());
    }

    @Test
    public void testMapLikeTypeUpgrade() {
        JavaType baseType = new SimpleType("java.util.HashMap");
        JavaType keyType = new SimpleType("String");
        JavaType valueType = new SimpleType("Integer");
        MapLikeType mapLikeType = MapLikeType.upgradeFrom(baseType, keyType, valueType);
        assertEquals(Map.class, mapLikeType.getType());
        assertEquals(keyType, mapLikeType.getKeyType());
        assertEquals(valueType, mapLikeType.getContentType());
        assertNull(mapLikeType.getValueHandler());
        assertNull(mapLikeType.getTypeHandler());
        assertFalse(mapLikeType.isStaticTyping());
    }

    @Test
    public void testMapLikeTypeNarrow() {
        JavaType baseType = new SimpleType("java.util.LinkedHashMap");
        JavaType keyType = new SimpleType("String");
        JavaType valueType = new SimpleType("Integer");
        MapLikeType mapLikeType = new MapLikeType(baseType, TypeBindings.emptyBindings(),
                null, null, keyType, valueType, null, null, false);
        JavaType narrowedType = mapLikeType.narrow(HashSet.class);
        assertNull(narrowedType);
        narrowedType = mapLikeType.narrow(HashMap.class);
        assertEquals(HashMap.class, narrowedType.getType());
        assertEquals(keyType, narrowedType.getKeyType());
        assertEquals(valueType, narrowedType.getContentType());
        assertNull(narrowedType.getValueHandler());
        assertNull(narrowedType.getTypeHandler());
        assertFalse(narrowedType.isStaticTyping());
    }

    @Test
    public void testMapLikeTypeWithKeyType() {
        JavaType baseType = new SimpleType("java.util.HashMap");
        JavaType keyType = new SimpleType("String");
        JavaType valueType = new SimpleType("Integer");
        MapLikeType mapLikeType = new MapLikeType(baseType, TypeBindings.emptyBindings(),
                null, null, keyType, valueType, null, null, false);
        JavaType newKeyType = new SimpleType("Integer");
        MapLikeType newMapLikeType = mapLikeType.withKeyType(newKeyType);
        assertEquals(baseType, newMapLikeType.getType());
        assertEquals(newKeyType, newMapLikeType.getKeyType());
        assertEquals(valueType, newMapLikeType.getContentType());
        assertNull(newMapLikeType.getValueHandler());
        assertNull(newMapLikeType.getTypeHandler());
        assertFalse(newMapLikeType.isStaticTyping());
    }

    @Test
    public void testMapLikeTypeWithContentType() {
        JavaType baseType = new SimpleType("java.util.HashMap");
        JavaType keyType = new SimpleType("String");
        JavaType valueType = new SimpleType("Integer");
        MapLikeType mapLikeType = new MapLikeType(baseType, TypeBindings.emptyBindings(),
                null, null, keyType, valueType, null, null, false);
        JavaType newValueType = new SimpleType("Double");
        MapLikeType newMapLikeType = mapLikeType.withContentType(newValueType);
        assertEquals(baseType, newMapLikeType.getType());
        assertEquals(keyType, newMapLikeType.getKeyType());
        assertEquals(newValueType, newMapLikeType.getContentType());
        assertNull(newMapLikeType.getValueHandler());
        assertNull(newMapLikeType.getTypeHandler());
        assertFalse(newMapLikeType.isStaticTyping());
    }

    @Test
    public void testMapLikeTypeWithTypeHandler() {
        JavaType baseType = new SimpleType("java.util.HashMap");
        JavaType keyType = new SimpleType("String");
        JavaType valueType = new SimpleType("Integer");
        MapLikeType mapLikeType = new MapLikeType(baseType, TypeBindings.emptyBindings(),
                null, null, keyType, valueType, null, null, false);
        Object handler = new Object();
        MapLikeType newMapLikeType = mapLikeType.withTypeHandler(handler);
        assertEquals(baseType, newMapLikeType.getType());
        assertEquals(keyType, newMapLikeType.getKeyType());
        assertEquals(valueType, newMapLikeType.getContentType());
        assertEquals(handler, newMapLikeType.getTypeHandler());
        assertNull(newMapLikeType.getValueHandler());
        assertFalse(newMapLikeType.isStaticTyping());
    }

    @Test
    public void testMapLikeTypeWithContentTypeHandler() {
        JavaType baseType = new SimpleType("java.util.HashMap");
        JavaType keyType = new SimpleType("String");
        JavaType valueType = new SimpleType("Integer");
        MapLikeType mapLikeType = new MapLikeType(baseType, TypeBindings.emptyBindings(),
                null, null, keyType, valueType, null, null, false);
        Object handler = new Object();
        JavaType newValueType = new SimpleType("Double");
        MapLikeType newMapLikeType = mapLikeType.withContentTypeHandler(handler);
        assertEquals(baseType, newMapLikeType.getType());
        assertEquals(keyType, newMapLikeType.getKeyType());
        assertEquals(newValueType, newMapLikeType.getContentType());
        assertEquals(handler, newMapLikeType.getContentTypeHandler());
        assertNull(newMapLikeType.getTypeHandler());
        assertNull(newMapLikeType.getValueHandler());
        assertFalse(newMapLikeType.isStaticTyping());
    }

    @Test
    public void testMapLikeTypeWithValueHandler() {
        JavaType baseType = new SimpleType("java.util.HashMap");
        JavaType keyType = new SimpleType("String");
        JavaType valueType = new SimpleType("Integer");
        MapLikeType mapLikeType = new MapLikeType(baseType, TypeBindings.emptyBindings(),
                null, null, keyType, valueType, null, null, false);
        Object handler = new Object();
        MapLikeType newMapLikeType = mapLikeType.withValueHandler(handler);
        assertEquals(baseType, newMapLikeType.getType());
        assertEquals(keyType, newMapLikeType.getKeyType());
        assertEquals(valueType, newMapLikeType.getContentType());
        assertEquals(handler, newMapLikeType.getValueHandler());
        assertNull(newMapLikeType.getTypeHandler());
        assertFalse(newMapLikeType.isStaticTyping());
    }

    @Test
    public void testMapLikeTypeWithContentValueHandler() {
        JavaType baseType = new SimpleType("java.util.HashMap");
        JavaType keyType = new SimpleType("String");
        JavaType valueType = new SimpleType("Integer");
        MapLikeType mapLikeType = new MapLikeType(baseType, TypeBindings.emptyBindings(),
                null, null, keyType, valueType, null, null, false);
        Object handler = new Object();
        JavaType newKeyType = new SimpleType("Integer");
        MapLikeType newMapLikeType = mapLikeType.withKeyTypeHandler(handler);
        assertEquals(baseType, newMapLikeType.getType());
        assertEquals(newKeyType, newMapLikeType.getKeyType());
        assertEquals(valueType, newMapLikeType.getContentType());
        assertEquals(handler, newMapLikeType.getKeyTypeHandler());
        assertNull(newMapLikeType.getValueHandler());
        assertNull(newMapLikeType.getTypeHandler());
        assertFalse(newMapLikeType.isStaticTyping());
    }

    @Test
    public void testMapLikeTypeWithStaticTyping() {
        JavaType baseType = new SimpleType("java.util.HashMap");
        JavaType keyType = new SimpleType("String");
        JavaType valueType = new SimpleType("Integer");
        MapLikeType mapLikeType = new MapLikeType(baseType, TypeBindings.emptyBindings(),
                null, null, keyType, valueType, null, null, false);
        MapLikeType newMapLikeType = mapLikeType.withStaticTyping();
        assertEquals(baseType, newMapLikeType.getType());
        assertEquals(keyType, newMapLikeType.getKeyType());
        assertEquals(valueType, newMapLikeType.getContentType());
        assertNull(newMapLikeType.getValueHandler());
        assertNull(newMapLikeType.getTypeHandler());
        assertTrue(newMapLikeType.isStaticTyping());
    }

    @Test
    public void testMapLikeTypeRefine() {
        JavaType baseType = new SimpleType("java.util.LinkedHashMap");
        JavaType keyType = new SimpleType("String");
        JavaType valueType = new SimpleType("Integer");
        MapLikeType mapLikeType = new MapLikeType(baseType, TypeBindings.emptyBindings(),
                null, null, keyType, valueType, null, null, false);
        JavaType newBaseType = new SimpleType("java.util.IdentityHashMap");
        TypeBindings newBindings = TypeBindings.create(newBaseType, keyType, valueType);
        JavaType newMapLikeType = mapLikeType.refine(newBaseType, newBindings, null, null);
        assertEquals(newBaseType, newMapLikeType.getType());
        assertEquals(keyType, newMapLikeType.getKeyType());
        assertEquals(valueType, newMapLikeType.getContentType());
        assertNull(newMapLikeType.getValueHandler());
        assertNull(newMapLikeType.getTypeHandler());
        assertFalse(newMapLikeType.isStaticTyping());
    }

    @Test
    public void testMapLikeTypeCanonicalName() {
        JavaType baseType = new SimpleType("java.util.HashMap");
        JavaType keyType = new SimpleType("String");
        JavaType valueType = new SimpleType("Integer");
        MapLikeType mapLikeType = new MapLikeType(baseType, TypeBindings.emptyBindings(),
                null, null, keyType, valueType, null, null, false);
        String canonicalName = mapLikeType.buildCanonicalName();
        assertEquals("java.util.HashMap<String, Integer>", canonicalName);
    }

    @Test
    public void testMapLikeTypeGenericSignature() {
        JavaType baseType = new SimpleType("java.util.HashMap");
        JavaType keyType = new SimpleType("String");
        JavaType valueType = new SimpleType("Integer");
        MapLikeType mapLikeType = new MapLikeType(baseType, TypeBindings.emptyBindings(),
                null, null, keyType, valueType, null, null, false);
        StringBuilder sb = new StringBuilder();
        mapLikeType.getGenericSignature(sb);
        assertEquals("[map-like type; class java.util.HashMap<String, Integer>, Ljava/lang/String; -> Ljava/lang/Object;]",
                sb.toString());
    }

    @Test
    public void testMapLikeTypeErasedSignature() {
        JavaType baseType = new SimpleType("java.util.HashMap");
        JavaType keyType = new SimpleType("String");
        JavaType valueType = new SimpleType("Integer");
        MapLikeType mapLikeType = new MapLikeType(baseType, TypeBindings.emptyBindings(),
                null, null, keyType, valueType, null, null, false);
        StringBuilder sb = new StringBuilder();
        mapLikeType.getErasedSignature(sb);
        assertEquals("[map-like type; class java.util.HashMap, java.lang.String, java.lang.Object]",
                sb.toString());
    }

    @Test
    public void testMapLikeTypeIsContainerType() {
        JavaType baseType = new SimpleType("java.util.HashMap");
        JavaType keyType = new SimpleType("String");
        JavaType valueType = new SimpleType("Integer");
        MapLikeType mapLikeType = new MapLikeType(baseType, TypeBindings.emptyBindings(),
                null, null, keyType, valueType, null, null, false);
        assertTrue(mapLikeType.isContainerType());
        assertTrue(mapLikeType.isMapLikeType());
    }

    @Test
    public void testMapLikeTypeIsTrueMapType() {
        JavaType baseType = new SimpleType("java.util.HashMap");
        JavaType keyType = new SimpleType("String");
        JavaType valueType = new SimpleType("Integer");
        MapLikeType mapLikeType = new MapLikeType(baseType, TypeBindings.emptyBindings(),
                null, null, keyType, valueType, null, null, false);
        assertTrue(mapLikeType.isTrueMapType());
    }

    @Test
    public void testMapLikeTypeToString() {
        JavaType baseType = new SimpleType("java.util.HashMap");
        JavaType keyType = new SimpleType("String");
        JavaType valueType = new SimpleType("Integer");
        MapLikeType mapLikeType = new MapLikeType(baseType, TypeBindings.emptyBindings(),
                null, null, keyType, valueType, null, null, false);
        assertEquals("[map-like type; class java.util.HashMap, java.lang.String -> java.lang.Integer]",
                mapLikeType.toString());
    }

    @Test
    public void testMapLikeTypeEquals() {
        JavaType baseType1 = new SimpleType("java.util.HashMap");
        JavaType keyType1 = new SimpleType("String");
        JavaType valueType1 = new SimpleType("Integer");
        JavaType baseType2 = new SimpleType("java.util.HashMap");
        JavaType keyType2 = new SimpleType("String");
        JavaType valueType2 = new SimpleType("Integer");
        MapLikeType mapLikeType1 = new MapLikeType(baseType1, TypeBindings.emptyBindings(),
                null, null, keyType1, valueType1, null, null, false);
        MapLikeType mapLikeType2 = new MapLikeType(baseType2, TypeBindings.emptyBindings(),
                null, null, keyType2, valueType2, null, null, false);
        MapLikeType mapLikeType3 = new MapLikeType(baseType1, TypeBindings.emptyBindings(),
                null, null, keyType1, valueType1, null, null, true);
        MapLikeType mapLikeType4 = new MapLikeType(baseType1, TypeBindings.emptyBindings(),
                null, null, keyType1, valueType1, null, null, false);
        MapLikeType mapLikeType5 = new MapLikeType(baseType2, TypeBindings.emptyBindings(),
                null, null, keyType2, valueType2, null, null, false);
        MapLikeType mapLikeType6 = new MapLikeType(baseType1, TypeBindings.emptyBindings(),
                null, null, keyType1, valueType1, null, null, true);
        MapLikeType mapLikeType7 = new MapLikeType(baseType1, TypeBindings.emptyBindings(),
                null, null, keyType1, valueType1, null, null, false);
        assertEquals(mapLikeType1, mapLikeType4);
        assertNotEquals(mapLikeType1, mapLikeType2);
        assertNotEquals(mapLikeType1, mapLikeType3);
        assertNotEquals(mapLikeType1, mapLikeType5);
        assertNotEquals(mapLikeType1, mapLikeType6);
    }
}