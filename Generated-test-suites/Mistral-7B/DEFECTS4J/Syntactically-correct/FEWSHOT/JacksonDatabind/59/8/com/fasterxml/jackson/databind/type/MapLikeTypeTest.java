package org.apache.commons.cli.test;

import static org.junit.Assert.*;

import org.junit.Test;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.MapLikeType;

import java.lang.reflect.TypeVariable;
import java.util.*;

public class MapLikeTypeTest {

    @Test
    public void testMapLikeType() {
        Class<?> mapType = Map.class;
        TypeVariable<?>[] mapTypeVars = mapType.getTypeParameters();
        JavaType keyType = JavaType.constructType(String.class);
        JavaType valueType = JavaType.constructType(Integer.class);
        MapLikeType mapLikeType = MapLikeType.upgradeFrom(JavaType.constructType(mapType), keyType, valueType);

        assertEquals(mapType, mapLikeType.getType());
        assertEquals(keyType, mapLikeType.getKeyType());
        assertEquals(valueType, mapLikeType.getValueType());

        Map<String, Integer> actualMap = new HashMap<>();
        actualMap.put("foo", 1);
        actualMap.put("bar", 2);

        Object mapHandler = new HashMapSerializationTest.MapHandler();
        Object keyHandler = new HashMapSerializationTest.StringKeyHandler();
        Object valueHandler = new HashMapSerializationTest.IntegerValueHandler();

        MapLikeType mapLikeTypeWithHandlers = mapLikeType.withKeyTypeHandler(keyHandler).withValueTypeHandler(valueHandler)
                .withValueHandler(valueHandler).withContentValueHandler(valueHandler).withTypeHandler(mapHandler);

        Map<String, Integer> deserializedMap = mapLikeTypeWithHandlers.deserializeFromString("{\"foo\":1,\"bar\":2}");

        assertEquals(actualMap, deserializedMap);
    }

    @Test
    public void testMapLikeTypeWithDifferentKeyType() {
        Class<?> mapType = Map.class;
        TypeVariable<?>[] mapTypeVars = mapType.getTypeParameters();
        JavaType keyType1 = JavaType.constructType(String.class);
        JavaType valueType = JavaType.constructType(Integer.class);
        MapLikeType mapLikeType = MapLikeType.upgradeFrom(JavaType.constructType(mapType), keyType1, valueType);

        JavaType keyType2 = JavaType.constructType(Integer.class);
        MapLikeType mapLikeTypeWithDifferentKeyType = mapLikeType.withKeyType(keyType2);

        assertEquals(mapType, mapLikeTypeWithDifferentKeyType.getType());
        assertEquals(keyType2, mapLikeTypeWithDifferentKeyType.getKeyType());
        assertEquals(valueType, mapLikeTypeWithDifferentKeyType.getValueType());
    }

    @Test
    public void testMapLikeTypeWithDifferentValueType() {
        Class<?> mapType = Map.class;
        TypeVariable<?>[] mapTypeVars = mapType.getTypeParameters();
        JavaType keyType = JavaType.constructType(String.class);
        JavaType valueType1 = JavaType.constructType(Integer.class);
        MapLikeType mapLikeType = MapLikeType.upgradeFrom(JavaType.constructType(mapType), keyType, valueType1);

        JavaType valueType2 = JavaType.constructType(Long.class);
        MapLikeType mapLikeTypeWithDifferentValueType = mapLikeType.withValueType(valueType2);

        assertEquals(mapType, mapLikeTypeWithDifferentValueType.getType());
        assertEquals(keyType, mapLikeTypeWithDifferentValueType.getKeyType());
        assertEquals(valueType2, mapLikeTypeWithDifferentValueType.getValueType());
    }

    @Test
    public void testMapLikeTypeWithStaticTyping() {
        Class<?> mapType = HashMap.class;
        TypeVariable<?>[] mapTypeVars = mapType.getTypeParameters();
        JavaType keyType = JavaType.constructType(String.class);
        JavaType valueType = JavaType.constructType(Integer.class);
        MapLikeType mapLikeType = MapLikeType.upgradeFrom(JavaType.constructType(mapType), keyType, valueType);

        MapLikeType mapLikeTypeWithStaticTyping = mapLikeType.withStaticTyping();

        assertTrue(mapLikeTypeWithStaticTyping.isStatic());
        assertFalse(mapLikeType.isStatic());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructFromNullType() {
        JavaType nullType = null;
        JavaType keyType = JavaType.constructType(String.class);
        JavaType valueType = JavaType.constructType(Integer.class);
        MapLikeType mapLikeType = MapLikeType.construct(null, keyType, valueType);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructFromInvalidType() {
        Class<?> invalidType = new Object() {}.getClass();
        JavaType keyType = JavaType.constructType(String.class);
        JavaType valueType = JavaType.constructType(Integer.class);
        MapLikeType mapLikeType = MapLikeType.construct(invalidType, keyType, valueType);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testUpgradeFromInvalidBaseType() {
        JavaType invalidBaseType = JavaType.constructType(int[].class);
        JavaType keyType = JavaType.constructType(String.class);
        JavaType valueType = JavaType.constructType(Integer.class);
        MapLikeType mapLikeType = MapLikeType.upgradeFrom(invalidBaseType, keyType, valueType);
    }
}