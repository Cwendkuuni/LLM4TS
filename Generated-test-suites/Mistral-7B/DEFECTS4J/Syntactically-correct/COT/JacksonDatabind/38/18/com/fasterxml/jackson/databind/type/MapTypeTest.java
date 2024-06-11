package com.fasterxml.jackson.databind.type.test;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.MapLikeType;
import com.fasterxml.jackson.databind.type.MapType;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@RunWith(JUnit4.class)
public class MapTypeTest {

    @Test
    public void testMapTypeConstructor() {
        Class<Map<String, Integer>> rawType = Map.class;
        TypeBindings bindings = TypeBindings.defaultBindings();
        JavaType superClass = Object.class;
        JavaType[] superInts = new JavaType[0];
        JavaType keyT = TypeFactory.defaultInstance().constructType(String.class);
        JavaType valueT = TypeFactory.defaultInstance().constructType(Integer.class);

        MapType mapType = MapType.construct(rawType, bindings, superClass, superInts, keyT, valueT);
        assertNotNull(mapType);
        assertEquals(Map.class, mapType.getRawClass());
        assertEquals(keyT, mapType.getKeyType());
        assertEquals(valueT, mapType.getValueType());
    }

    @Test
    public void testMapTypeWithTypeHandler() {
        Class<Map<String, Integer>> rawType = Map.class;
        TypeBindings bindings = TypeBindings.defaultBindings();
        JavaType superClass = Object.class;
        JavaType[] superInts = new JavaType[0];
        JavaType keyT = TypeFactory.defaultInstance().constructType(String.class);
        JavaType valueT = TypeFactory.defaultInstance().constructType(Integer.class);
        Object valueHandler = new Object() {};

        MapType mapType = new MapType(rawType, bindings, superClass, superInts, keyT, valueT, valueHandler, null, false);
        MapType mapTypeWithHandler = mapType.withTypeHandler(valueHandler);
        assertNotNull(mapTypeWithHandler);
        assertEquals(mapType.getRawClass(), mapTypeWithHandler.getRawClass());
        assertEquals(mapType.getKeyType(), mapTypeWithHandler.getKeyType());
        assertEquals(mapType.getValueType(), mapTypeWithHandler.getValueType());
        assertEquals(valueHandler, mapTypeWithHandler.getTypeHandler());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testMapTypeConstructWithInvalidRawType() {
        Class<?> rawType = String.class;
        TypeBindings bindings = TypeBindings.defaultBindings();
        JavaType superClass = Object.class;
        JavaType[] superInts = new JavaType[0];
        JavaType keyT = TypeFactory.defaultInstance().constructType(String.class);
        JavaType valueT = TypeFactory.defaultInstance().constructType(Integer.class);

        MapType mapType = MapType.construct(rawType, bindings, superClass, superInts, keyT, valueT);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testMapTypeConstructWithNullBindings() {
        Class<Map<String, Integer>> rawType = Map.class;
        JavaType superClass = Object.class;
        JavaType[] superInts = new JavaType[0];
        JavaType keyT = TypeFactory.defaultInstance().constructType(String.class);
        JavaType valueT = TypeFactory.defaultInstance().constructType(Integer.class);

        MapType mapType = MapType.construct(rawType, null, superClass, superInts, keyT, valueT);
    }

    @Test
    public void testMapTypeRefine() {
        Class<Map<String, Integer>> rawType = Map.class;
        TypeBindings bindings = TypeBindings.defaultBindings();
        JavaType superClass = Object.class;
        JavaType[] superInts = new JavaType[0];
        JavaType keyT = TypeFactory.defaultInstance().constructType(String.class);
        JavaType valueT = TypeFactory.defaultInstance().constructType(Integer.class);

        MapType mapType = new MapType(rawType, bindings, superClass, superInts, keyT, valueT);
        JavaType refinedType = mapType.refine(HashMap.class, bindings, superClass, superInts);
        assertNotNull(refinedType);
        assertEquals(HashMap.class, refinedType.getRawClass());
        assertEquals(keyT, refinedType.getKeyType());
        assertEquals(valueT, refinedType.getValueType());
    }
}