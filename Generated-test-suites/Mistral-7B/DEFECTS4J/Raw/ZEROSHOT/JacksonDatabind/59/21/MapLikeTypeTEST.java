package com.example.junit;

import static org.junit.Assert.*;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.MapLikeType;

public class MapLikeTypeTest {

    @Test
    public void testMapLikeTypeConstruction() {
        Class<?> mapType = Map.class;
        TypeBindings bindings = TypeBindings.emptyBindings();
        JavaType superClass = _bogusSuperClass(mapType);
        JavaType keyType = JavaType.construct(String.class, TypeBindings.emptyBindings());
        JavaType valueType = JavaType.construct(Integer.class, TypeBindings.emptyBindings());

        MapLikeType mapLikeType = new MapLikeType(mapType, bindings, superClass, null, keyType, valueType, null, null, false);

        assertEquals(mapType, mapLikeType.getRawClass());
        assertEquals(keyType, mapLikeType.getKeyType());
        assertEquals(valueType, mapLikeType.getValueType());
    }

    @Test
    public void testUpgradeFromJavaType() {
        JavaType baseType = JavaType.construct(HashMap.class, TypeBindings.emptyBindings());
        JavaType keyType = JavaType.construct(String.class, TypeBindings.emptyBindings());
        JavaType valueType = JavaType.construct(Integer.class, TypeBindings.emptyBindings());

        MapLikeType mapLikeType = MapLikeType.upgradeFrom(baseType, keyType, valueType);

        assertEquals(HashMap.class, mapLikeType.getRawClass());
        assertEquals(keyType, mapLikeType.getKeyType());
        assertEquals(valueType, mapLikeType.getValueType());
    }

    @Test
    public void testIsContainerType() {
        Class<?> mapType = Map.class;
        TypeBindings bindings = TypeBindings.emptyBindings();
        JavaType superClass = _bogusSuperClass(mapType);
        JavaType keyType = JavaType.construct(String.class, TypeBindings.emptyBindings());
        JavaType valueType = JavaType.construct(Integer.class, TypeBindings.emptyBindings());

        MapLikeType mapLikeType = new MapLikeType(mapType, bindings, superClass, null, keyType, valueType, null, null, false);

        assertTrue(mapLikeType.isContainerType());
        assertTrue(mapLikeType.isMapLikeType());
    }

    @Test
    public void testIsTrueMapType() {
        Class<?> mapType = Map.class;
        TypeBindings bindings = TypeBindings.emptyBindings();
        JavaType superClass = _bogusSuperClass(mapType);
        JavaType keyType = JavaType.construct(String.class, TypeBindings.emptyBindings());
        JavaType valueType = JavaType.construct(Integer.class, TypeBindings.emptyBindings());

        MapLikeType mapLikeType = new MapLikeType(mapType, bindings, superClass, null, keyType, valueType, null, null, false);

        assertTrue(mapLikeType.isTrueMapType());
    }

    @Test
    public void testGetKeyType() {
        Class<?> mapType = Map.class;
        TypeBindings bindings = TypeBindings.emptyBindings();
        JavaType superClass = _bogusSuperClass(mapType);
        JavaType keyType = JavaType.construct(String.class, TypeBindings.emptyBindings());
        JavaType valueType = JavaType.construct(Integer.class, TypeBindings.emptyBindings());

        MapLikeType mapLikeType = new MapLikeType(mapType, bindings, superClass, null, keyType, valueType, null, null, false);

        assertEquals(keyType, mapLikeType.getKeyType());
    }

    @Test
    public void testGetContentType() {
        Class<?> mapType = Map.class;
        TypeBindings bindings = TypeBindings.emptyBindings();
        JavaType superClass = _bogusSuperClass(mapType);
        JavaType keyType = JavaType.construct(String.class, TypeBindings.emptyBindings());
        JavaType valueType = JavaType.construct(Integer.class, TypeBindings.emptyBindings());

        MapLikeType mapLikeType = new MapLikeType(mapType, bindings, superClass, null, keyType, valueType, null, null, false);

        assertEquals(valueType, mapLikeType.getValueType());
    }

    @Test
    public void testToString() {
        Class<?> mapType = Map.class;
        TypeBindings bindings = TypeBindings.emptyBindings();
        JavaType superClass = _bogusSuperClass(mapType);
        JavaType keyType = JavaType.construct(String.class, TypeBindings.emptyBindings());
        JavaType valueType = JavaType.construct(Integer.class, TypeBindings.emptyBindings());

        MapLikeType mapLikeType = new MapLikeType(mapType, bindings, superClass, null, keyType, valueType, null, null, false);

        assertEquals("[map-like type; class " + mapType.getName() + ", " + keyType + " -> " + valueType + "]", mapLikeType.toString());
    }

    private JavaType _bogusSuperClass(Class<?> clazz) {
        return JavaType.construct(Object.class, TypeBindings.emptyBindings());
    }
}