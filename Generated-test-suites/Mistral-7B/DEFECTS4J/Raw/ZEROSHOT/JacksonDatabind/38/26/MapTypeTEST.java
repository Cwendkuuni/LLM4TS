package com.fasterxml.jackson.databind.type;

import static org.junit.Assert.*;

import org.junit.Test;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.TypeBindings;
import com.fasterxml.jackson.databind.type.MapType;

public class MapTypeTest {

    @Test
    public void testMapTypeConstruction() {
        Class<Map<String, Integer>> rawType = Map.class;
        TypeBindings bindings = TypeBindings.getDefaultInstance();
        JavaType keyType = JavaType.constructCollectionType(Map.class, String.class);
        JavaType valueType = JavaType.of(Integer.class);
        MapType mapType = MapType.construct(rawType, bindings, null, null, keyType, valueType);
        assertEquals(Map.class, mapType.getRawClass());
        assertEquals(keyType, mapType.getKeyType());
        assertEquals(valueType, mapType.getValueType());
        assertEquals(bindings, mapType.getBindings());
    }

    @Test
    public void testMapTypeWithTypeHandlers() {
        Class<Map<String, Integer>> rawType = Map.class;
        TypeBindings bindings = TypeBindings.getDefaultInstance();
        JavaType keyType = JavaType.constructCollectionType(Map.class, String.class);
        JavaType valueType = JavaType.of(Integer.class);
        MapType mapType = new MapType(rawType, bindings, null, null, keyType, valueType);

        Object keyHandler = new Object();
        Object valueHandler = new Object();
        Object typeHandler = new Object();
        Object contentKeyHandler = new Object();
        Object contentValueHandler = new Object();

        MapType mapTypeWithHandlers = mapType.withKeyTypeHandler(keyHandler)
                .withValueHandler(valueHandler)
                .withTypeHandler(typeHandler)
                .withContentKeyHandler(contentKeyHandler)
                .withContentValueHandler(contentValueHandler);

        assertNotSame(mapType, mapTypeWithHandlers);
        assertEquals(keyHandler, mapTypeWithHandlers.getKeyTypeHandler());
        assertEquals(valueHandler, mapTypeWithHandlers.getValueHandler());
        assertEquals(typeHandler, mapTypeWithHandlers.getTypeHandler());
        assertEquals(contentKeyHandler, mapTypeWithHandlers.getContentKeyHandler());
        assertEquals(contentValueHandler, mapTypeWithHandlers.getContentValueHandler());
    }

    @Test
    public void testMapTypeWithStaticTyping() {
        Class<Map<String, Integer>> rawType = Map.class;
        TypeBindings bindings = TypeBindings.getDefaultInstance();
        JavaType keyType = JavaType.constructCollectionType(Map.class, String.class);
        JavaType valueType = JavaType.of(Integer.class);
        MapType mapType = new MapType(rawType, bindings, null, null, keyType, valueType);

        MapType staticMapType = mapType.withStaticTyping();
        assertTrue(staticMapType.isStatic());
        assertFalse(mapType.isStatic());
    }

    @Test
    public void testMapTypeWithContentType() {
        Class<Map<String, Integer>> rawType = Map.class;
        TypeBindings bindings = TypeBindings.getDefaultInstance();
        JavaType keyType = JavaType.constructCollectionType(Map.class, String.class);
        JavaType valueType = JavaType.of(Integer.class);
        JavaType contentType = JavaType.of(String.class);
        MapType mapType = new MapType(rawType, bindings, null, null, keyType, valueType);

        MapType mapTypeWithContentType = mapType.withContentType(contentType);
        assertEquals(contentType, mapTypeWithContentType.getValueType());
        assertNotSame(mapType, mapTypeWithContentType);
    }

    @Test
    public void testMapTypeWithKeyType() {
        Class<Map<String, Integer>> rawType = Map.class;
        TypeBindings bindings = TypeBindings.getDefaultInstance();
        JavaType keyType1 = JavaType.constructCollectionType(Map.class, String.class);
        JavaType valueType = JavaType.of(Integer.class);
        MapType mapType = new MapType(rawType, bindings, null, null, keyType1, valueType);

        JavaType keyType2 = JavaType.constructCollectionType(Map.class, Integer.class);
        MapType mapTypeWithKeyType = mapType.withKeyType(keyType2);
        assertEquals(keyType2, mapTypeWithKeyType.getKeyType());
        assertNotSame(mapType, mapTypeWithKeyType);
    }

    @Test
    public void testMapTypeRefine() {
        Class<Map<String, Integer>> rawType = Map.class;
        TypeBindings bindings = TypeBindings.getDefaultInstance();
        JavaType keyType = JavaType.constructCollectionType(Map.class, String.class);
        JavaType valueType = JavaType.of(Integer.class);
        MapType mapType = new MapType(rawType, bindings, null, null, keyType, valueType);

        Class<Map<Integer, String>> rawTypeRefined = Map.class;
        TypeBindings bindingsRefined = TypeBindings.getDefaultInstance();
        JavaType keyTypeRefined = JavaType.constructCollectionType(Map.class, Integer.class);
        JavaType valueTypeRefined = JavaType.of(String.class);
        MapType refinedMapType = mapType.refine(rawTypeRefined, bindingsRefined, null, null);

        assertEquals(rawTypeRefined, refinedMapType.getRawClass());
        assertEquals(keyTypeRefined, refinedMapType.getKeyType());
        assertEquals(valueTypeRefined, refinedMapType.getValueType());
        assertNotSame(mapType, refinedMapType);
    }

    @Test
    public void testMapTypeToString() {
        Class<Map<String, Integer>> rawType = Map.class;
        TypeBindings bindings = TypeBindings.getDefaultInstance();
        JavaType keyType = JavaType.constructCollectionType(Map.class, String.class);
        JavaType valueType = JavaType.of(Integer.class);
        MapType mapType = new MapType(rawType, bindings, null, null, keyType, valueType);

        String mapTypeString = mapType.toString();
        String expected = "[map type; class " + rawType.getName() + ", " + keyType + " -> " + valueType + "]";
        assertEquals(expected, mapTypeString);
    }
}