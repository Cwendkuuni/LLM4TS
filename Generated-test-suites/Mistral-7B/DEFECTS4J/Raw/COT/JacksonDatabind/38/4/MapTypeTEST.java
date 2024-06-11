package com.fasterxml.jackson.databind.type.test;

import com.fasterxml.jackson.core.type.TypeBindings;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.MapLikeType;
import com.fasterxml.jackson.databind.type.MapType;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import static org.junit.Assert.*;

@RunWith(JUnit4.class)
public class MapTypeTest {

    @Test
    public void testMapTypeConstructor() {
        Class<Map<String, Integer>> mapType = Map.class;
        TypeBindings bindings = TypeBindings.defaultBindings();
        JavaType superClass = Object.class;
        JavaType[] superInts = new JavaType[0];
        JavaType keyType = JavaType.constructType(String.class);
        JavaType valueType = JavaType.constructType(Integer.class);
        MapType mapTypeInstance = MapType.construct(mapType, bindings, superClass, superInts, keyType, valueType);
        assertEquals(mapType, mapTypeInstance.getRawClass());
        assertEquals(keyType, mapTypeInstance.getKeyType());
        assertEquals(valueType, mapTypeInstance.getValueType());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testMapTypeConstructInvalidBindings() {
        Class<Map<String, Integer>> mapType = Map.class;
        JavaType keyType = JavaType.constructType(String.class);
        JavaType valueType = JavaType.constructType(Integer.class);
        TypeBindings bindings = new TypeBindings() {
            @Override
            public JavaType findBinding(Class<?> type) {
                throw new UnsupportedOperationException();
            }
        };
        MapType mapTypeInstance = MapType.construct(mapType, bindings, null, null, keyType, valueType);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testMapTypeConstructInvalidSuperClass() {
        Class<Map<String, Integer>> mapType = Map.class;
        TypeBindings bindings = TypeBindings.defaultBindings();
        JavaType superClass = String.class;
        JavaType[] superInts = new JavaType[0];
        JavaType keyType = JavaType.constructType(String.class);
        JavaType valueType = JavaType.constructType(Integer.class);
        MapType mapTypeInstance = MapType.construct(mapType, bindings, superClass, superInts, keyType, valueType);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testMapTypeConstructInvalidKeyType() {
        Class<Map<String, Integer>> mapType = Map.class;
        TypeBindings bindings = TypeBindings.defaultBindings();
        JavaType superClass = Object.class;
        JavaType[] superInts = new JavaType[0];
        JavaType keyType = null;
        JavaType valueType = JavaType.constructType(Integer.class);
        MapType mapTypeInstance = MapType.construct(mapType, bindings, superClass, superInts, keyType, valueType);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testMapTypeConstructInvalidValueType() {
        Class<Map<String, Integer>> mapType = Map.class;
        TypeBindings bindings = TypeBindings.defaultBindings();
        JavaType superClass = Object.class;
        JavaType[] superInts = new JavaType[0];
        JavaType keyType = JavaType.constructType(String.class);
        JavaType valueType = null;
        MapType mapTypeInstance = MapType.construct(mapType, bindings, superClass, superInts, keyType, valueType);
    }

    @Test
    public void testMapTypeWithTypeHandler() {
        Class<Map<String, Integer>> mapType = Map.class;
        TypeBindings bindings = TypeBindings.defaultBindings();
        JavaType superClass = Object.class;
        JavaType[] superInts = new JavaType[0];
        JavaType keyType = JavaType.constructType(String.class);
        JavaType valueType = JavaType.constructType(Integer.class);
        Object valueHandler = new Object();
        MapType mapTypeInstance = new MapType(mapType, bindings, superClass, superInts, keyType, valueType, null, null, false);
        MapType mapTypeWithHandler = mapTypeInstance.withTypeHandler(valueHandler);
        assertNotSame(mapTypeInstance, mapTypeWithHandler);
        assertEquals(valueHandler, mapTypeWithHandler.getTypeHandler());
    }

    @Test
    public void testMapTypeWithContentTypeHandler() {
        Class<Map<String, Integer>> mapType = Map.class;
        TypeBindings bindings = TypeBindings.defaultBindings();
        JavaType superClass = Object.class;
        JavaType[] superInts = new JavaType[0];
        JavaType keyType = JavaType.constructType(String.class);
        JavaType valueType = JavaType.constructType(Integer.class);
        Object contentTypeHandler = new Object();
        MapType mapTypeInstance = new MapType(mapType, bindings, superClass, superInts, keyType, valueType, null, null, false);
        JavaType contentType = JavaType.constructType(Map.class);
        MapType mapTypeWithContentTypeHandler = mapTypeInstance.withContentTypeHandler(contentTypeHandler);
        JavaType result = mapTypeWithContentTypeHandler.withContentType(contentType);
        assertEquals(contentType, result);
    }

    @Test
    public void testMapTypeWithValueHandler() {
        Class<Map<String, Integer>> mapType = Map.class;
        TypeBindings bindings = TypeBindings.defaultBindings();
        JavaType superClass = Object.class;
        JavaType[] superInts = new JavaType[0];
        JavaType keyType = JavaType.constructType(String.class);
        JavaType valueType = JavaType.constructType(Integer.class);
        Object valueHandler = new Object();
        MapType mapTypeInstance = new MapType(mapType, bindings, superClass, superInts, keyType, valueType, null, null, false);
        MapType mapTypeWithValueHandler = mapTypeInstance.withValueHandler(valueHandler);
        assertNotSame(mapTypeInstance, mapTypeWithValueHandler);
        assertEquals(valueHandler, mapTypeWithValueHandler.getValueHandler());
    }

    @Test
    public void testMapTypeWithContentValueHandler() {
        Class<Map<String, Integer>> mapType = Map.class;
        TypeBindings bindings = TypeBindings.defaultBindings();
        JavaType superClass = Object.class;
        JavaType[] superInts = new JavaType[0];
        JavaType keyType = JavaType.constructType(String.class);
        JavaType valueType = JavaType.constructType(Integer.class);
        Object contentValueHandler = new Object();
        MapType mapTypeInstance = new MapType(mapType, bindings, superClass, superInts, keyType, valueType, null, null, false);
        JavaType valueTypeWithContentValueHandler = valueType.withContentValueHandler(contentValueHandler);
        MapType mapTypeWithContentValueHandler = mapTypeInstance.withContentValueHandler(contentValueHandler);
        JavaType result = mapTypeWithContentValueHandler.withValueType(valueTypeWithContentValueHandler);
        assertEquals(valueTypeWithContentValueHandler, result);
    }

    @Test
    public void testMapTypeWithStaticTyping() {
        Class<Map<String, Integer>> mapType = Map.class;
        TypeBindings bindings = TypeBindings.defaultBindings();
        JavaType superClass = Object.class;
        JavaType[] superInts = new JavaType[0];
        JavaType keyType = JavaType.constructType(String.class);
        JavaType valueType = JavaType.constructType(Integer.class);
        MapType mapTypeInstance = new MapType(mapType, bindings, superClass, superInts, keyType, valueType, null, null, false);
        MapType mapTypeWithStaticTyping = mapTypeInstance.withStaticTyping();
        assertTrue(mapTypeWithStaticTyping.isStatic());
        assertFalse(mapTypeInstance.isStatic());
    }

    @Test
    public void testMapTypeWithKeyType() {
        Class<Map<String, Integer>> mapType = Map.class;
        TypeBindings bindings = TypeBindings.defaultBindings();
        JavaType superClass = Object.class;
        JavaType[] superInts = new JavaType[0];
        JavaType keyType = JavaType.constructType(String.class);
        JavaType valueType = JavaType.constructType(Integer.class);
        JavaType newKeyType = JavaType.constructType(Long.class);
        MapType mapTypeInstance = new MapType(mapType, bindings, superClass, superInts, keyType, valueType, null, null, false);
        MapType mapTypeWithNewKeyType = mapTypeInstance.withKeyType(newKeyType);
        assertNotSame(mapTypeInstance, mapTypeWithNewKeyType);
        assertEquals(newKeyType, mapTypeWithNewKeyType.getKeyType());
    }

    @Test
    public void testMapTypeWithContentType() {
        Class<Map<String, Integer>> mapType = Map.class;
        TypeBindings bindings = TypeBindings.defaultBindings();
        JavaType superClass = Object.class;
        JavaType[] superInts = new JavaType[0];
        JavaType keyType = JavaType.constructType(String.class);
        JavaType valueType = JavaType.constructType(Integer.class);
        JavaType newContentType = JavaType.constructType(Map.class);
        MapType mapTypeInstance = new MapType(mapType, bindings, superClass, superInts, keyType, valueType, null, null, false);
        MapType mapTypeWithNewContentType = mapTypeInstance.withContentType(newContentType);
        assertNotSame(mapTypeInstance, mapTypeWithNewContentType);
        assertEquals(newContentType, mapTypeWithNewContentType.getValueType());
    }

    @Test
    public void testMapTypeRefine() {
        Class<Map<String, Integer>> mapType = Map.class;
        TypeBindings bindings = TypeBindings.defaultBindings();
        JavaType superClass = Object.class;
        JavaType[] superInts = new JavaType[0];
        JavaType keyType = JavaType.constructType(String.class);
        JavaType valueType = JavaType.constructType(Integer.class);
        JavaType refinedMapType = JavaType.constructType(Map.class, String.class, Integer.class);
        MapType mapTypeInstance = new MapType(mapType, bindings, superClass, superInts, keyType, valueType, null, null, false);
        MapType refinedMapTypeInstance = mapTypeInstance.refine(Map.class, bindings, superClass, superInts);
        assertNotSame(mapTypeInstance, refinedMapTypeInstance);
        assertEquals(refinedMapType, refinedMapTypeInstance.getRawClass());
    }

    @Test
    public void testMapTypeToString() {
        Class<Map<String, Integer>> mapType = Map.class;
        TypeBindings bindings = TypeBindings.defaultBindings();
        JavaType superClass = Object.class;
        JavaType[] superInts = new JavaType[0];
        JavaType keyType = JavaType.constructType(String.class);
        JavaType valueType = JavaType.constructType(Integer.class);
        MapType mapTypeInstance = new MapType(mapType, bindings, superClass, superInts, keyType, valueType, null, null, false);
        String result = mapTypeInstance.toString();
        String expected = "[map type; class " + mapType.getName() + ", " + keyType + " -> " + valueType + "]";
        assertEquals(expected, result);
    }

}