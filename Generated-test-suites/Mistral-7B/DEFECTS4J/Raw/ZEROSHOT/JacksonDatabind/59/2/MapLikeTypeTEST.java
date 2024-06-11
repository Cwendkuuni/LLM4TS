package com.fasterxml.jackson.databind.type;

import static org.junit.Assert.*;

import java.lang.reflect.TypeVariable;
import java.util.*;

import org.junit.Test;

import com.fasterxml.jackson.databind.JavaType;

public class MapLikeTypeTest {

    @Test
    public void testMapLikeTypeConstruction() {
        Class<?> mapType = Map.class;
        TypeVariable<?> keyTypeVar = TypeVariable.valueOf(Map.class, "K");
        TypeVariable<?> valueTypeVar = TypeVariable.valueOf(Map.class, "V");
        JavaType keyType = JavaType.constructCollectionType(HashMap.class, String.class);
        JavaType valueType = JavaType.constructCollectionType(ArrayList.class, Integer.class);
        MapLikeType mapLikeType = new MapLikeType(mapType, TypeBindings.emptyBindings(), _bogusSuperClass(mapType), null, keyType, valueType);
        assertEquals(mapType, mapLikeType.getRawClass());
        assertEquals(keyType, mapLikeType.getKeyType());
        assertEquals(valueType, mapLikeType.getValueType());
    }

    @Test
    public void testMapLikeTypeUpgrade() {
        Class<?> listType = ArrayList.class;
        TypeVariable<?> listElementTypeVar = TypeVariable.valueOf(ArrayList.class, "E");
        JavaType javaType = JavaType.constructCollectionType(ArrayList.class, String.class);
        JavaType keyType = JavaType.constructTypeReference(String.class);
        JavaType valueType = JavaType.constructTypeReference(Integer.class);
        MapLikeType mapLikeType = new MapLikeType(javaType, keyType, valueType);
        MapLikeType upgradedMapLikeType = MapLikeType.upgradeFrom(javaType, keyType, valueType);
        assertEquals(mapLikeType.getClass(), upgradedMapLikeType.getClass());
        assertEquals(mapLikeType.getRawClass(), upgradedMapLikeType.getRawClass());
        assertEquals(mapLikeType.getKeyType(), upgradedMapLikeType.getKeyType());
        assertEquals(mapLikeType.getValueType(), upgradedMapLikeType.getValueType());
    }

    @Test
    public void testMapLikeTypeWithKeyType() {
        Class<?> mapType = Map.class;
        TypeVariable<?> keyTypeVar = TypeVariable.valueOf(Map.class, "K");
        TypeVariable<?> valueTypeVar = TypeVariable.valueOf(Map.class, "V");
        JavaType keyType = JavaType.constructCollectionType(HashMap.class, String.class);
        JavaType valueType = JavaType.constructCollectionType(ArrayList.class, Integer.class);
        MapLikeType mapLikeType = new MapLikeType(mapType, TypeBindings.emptyBindings(), _bogusSuperClass(mapType), null, keyType, valueType);
        JavaType newKeyType = JavaType.constructCollectionType(LinkedHashMap.class, String.class);
        MapLikeType newMapLikeType = mapLikeType.withKeyType(newKeyType);
        assertEquals(mapLikeType, newMapLikeType.withKeyType(keyType)); // test equals
        assertEquals(newKeyType, newMapLikeType.getKeyType());
        assertEquals(valueType, newMapLikeType.getValueType());
    }

    @Test
    public void testMapLikeTypeWithContentType() {
        Class<?> mapType = Map.class;
        TypeVariable<?> keyTypeVar = TypeVariable.valueOf(Map.class, "K");
        TypeVariable<?> valueTypeVar = TypeVariable.valueOf(Map.class, "V");
        JavaType keyType = JavaType.constructCollectionType(HashMap.class, String.class);
        JavaType valueType = JavaType.constructCollectionType(ArrayList.class, Integer.class);
        MapLikeType mapLikeType = new MapLikeType(mapType, TypeBindings.emptyBindings(), _bogusSuperClass(mapType), null, keyType, valueType);
        JavaType newValueType = JavaType.constructCollectionType(LinkedList.class, Integer.class);
        MapLikeType newMapLikeType = mapLikeType.withContentType(newValueType);
        assertEquals(mapLikeType, newMapLikeType.withContentType(valueType)); // test equals
        assertEquals(keyType, newMapLikeType.getKeyType());
        assertEquals(newValueType, newMapLikeType.getValueType());
    }

    @Test
    public void testMapLikeTypeWithTypeHandler() {
        Class<?> mapType = Map.class;
        TypeVariable<?> keyTypeVar = TypeVariable.valueOf(Map.class, "K");
        TypeVariable<?> valueTypeVar = TypeVariable.valueOf(Map.class, "V");
        JavaType keyType = JavaType.constructCollectionType(HashMap.class, String.class);
        JavaType valueType = JavaType.constructCollectionType(ArrayList.class, Integer.class);
        MapLikeType mapLikeType = new MapLikeType(mapType, TypeBindings.emptyBindings(), _bogusSuperClass(mapType), null, keyType, valueType);
        Object handler = new Object();
        MapLikeType newMapLikeType = mapLikeType.withTypeHandler(handler);
        assertEquals(mapLikeType, newMapLikeType); // test equals
        assertNotNull(newMapLikeType.getTypeHandler());
        assertSame(handler, newMapLikeType.getTypeHandler());
    }

    @Test
    public void testMapLikeTypeWithContentTypeHandler() {
        Class<?> mapType = Map.class;
        TypeVariable<?> keyTypeVar = TypeVariable.valueOf(Map.class, "K");
        TypeVariable<?> valueTypeVar = TypeVariable.valueOf(Map.class, "V");
        JavaType keyType = JavaType.constructCollectionType(HashMap.class, String.class);
        JavaType valueType = JavaType.constructCollectionType(ArrayList.class, Integer.class);
        MapLikeType mapLikeType = new MapLikeType(mapType, TypeBindings.emptyBindings(), _bogusSuperClass(mapType), null, keyType, valueType);
        Object contentHandler = new Object();
        JavaType newValueType = JavaType.constructCollectionType(LinkedList.class, Integer.class).withTypeHandler(contentHandler);
        MapLikeType newMapLikeType = mapLikeType.withContentTypeHandler(contentHandler);
        assertEquals(mapLikeType, newMapLikeType.withContentTypeHandler(valueType.getTypeHandler())); // test equals
        assertEquals(keyType, newMapLikeType.getKeyType());
        assertEquals(newValueType, newMapLikeType.getValueType());
        assertSame(contentHandler, newMapLikeType.getValueTypeHandler());
    }

    @Test
    public void testMapLikeTypeWithKeyTypeHandler() {
        Class<?> mapType = Map.class;
        TypeVariable<?> keyTypeVar = TypeVariable.valueOf(Map.class, "K");
        TypeVariable<?> valueTypeVar = TypeVariable.valueOf(Map.class, "V");
        JavaType keyType = JavaType.constructCollectionType(HashMap.class, String.class);
        JavaType valueType = JavaType.constructCollectionType(ArrayList.class, Integer.class);
        MapLikeType mapLikeType = new MapLikeType(mapType, TypeBindings.emptyBindings(), _bogusSuperClass(mapType), null, keyType, valueType);
        Object keyHandler = new Object();
        JavaType newKeyType = JavaType.constructCollectionType(LinkedHashMap.class, String.class).withTypeHandler(keyHandler);
        MapLikeType newMapLikeType = mapLikeType.withKeyTypeHandler(keyHandler);
        assertEquals(mapLikeType, newMapLikeType.withKeyTypeHandler(keyType.getTypeHandler())); // test equals
        assertEquals(newKeyType, newMapLikeType.getKeyType());
        assertEquals(valueType, newMapLikeType.getValueType());
        assertSame(keyHandler, newMapLikeType.getKeyTypeHandler());
    }

    @Test
    public void testMapLikeTypeWithKeyValueHandler() {
        Class<?> mapType = Map.class;
        TypeVariable<?> keyTypeVar = TypeVariable.valueOf(Map.class, "K");
        TypeVariable<?> valueTypeVar = TypeVariable.valueOf(Map.class, "V");
        JavaType keyType = JavaType.constructCollectionType(HashMap.class, String.class);
        JavaType valueType = JavaType.constructCollectionType(ArrayList.class, Integer.class);
        MapLikeType mapLikeType = new MapLikeType(mapType, TypeBindings.emptyBindings(), _bogusSuperClass(mapType), null, keyType, valueType);
        Object keyValueHandler = new Object();
        JavaType newKeyType = JavaType.constructCollectionType(LinkedHashMap.class, String.class).withValueHandler(keyValueHandler);
        MapLikeType newMapLikeType = mapLikeType.withKeyValueHandler(keyValueHandler);
        assertEquals(mapLikeType, newMapLikeType.withKeyValueHandler(keyType.getValueHandler())); // test equals
        assertEquals(newKeyType, newMapLikeType.getKeyType());
        assertEquals(valueType, newMapLikeType.getValueType());
        assertSame(keyValueHandler, newMapLikeType.getKeyValueHandler());
    }

    @Test
    public void testMapLikeTypeIsTrueMapType() {
        Class<?> mapType = Map.class;
        TypeVariable<?> keyTypeVar = TypeVariable.valueOf(Map.class, "K");
        TypeVariable<?> valueTypeVar = TypeVariable.valueOf(Map.class, "V");
        JavaType keyType = JavaType.constructCollectionType(HashMap.class, String.class);
        JavaType valueType = JavaType.constructCollectionType(ArrayList.class, Integer.class);
        MapLikeType mapLikeType = new MapLikeType(mapType, TypeBindings.emptyBindings(), _bogusSuperClass(mapType), null, keyType, valueType);
        assertTrue(mapLikeType.isTrueMapType());
    }

    private static Class<?> _bogusSuperClass(Class<?> c) {
        return null;
    }
}