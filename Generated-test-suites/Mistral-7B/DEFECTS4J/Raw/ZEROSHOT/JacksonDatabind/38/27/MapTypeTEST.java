package com.fasterxml.jackson.databind.type;

import static org.junit.Assert.*;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.MapType;

public class MapTypeTest {

    @Test
    public void testConstruct() {
        Class<?> clazz = HashMap.class;
        JavaType keyType = new TypeReference<String>() {}.getType();
        JavaType valueType = new TypeReference<Integer>() {}.getType();
        TypeBindings bindings = new TypeBindings();
        JavaType mapType = MapType.construct(clazz, bindings, null, null, keyType, valueType);
        assertEquals(clazz, mapType.getRawClass());
        assertEquals(keyType, mapType.getKeyType());
        assertEquals(valueType, mapType.getValueType());
    }

    @Test
    public void testConstructWithTypeHandler() {
        Class<?> clazz = HashMap.class;
        JavaType keyType = new TypeReference<String>() {}.getType();
        JavaType valueType = new TypeReference<Integer>() {}.getType();
        Object valueHandler = new Object() {};
        TypeBindings bindings = new TypeBindings();
        JavaType mapType = MapType.construct(clazz, bindings, null, null, keyType, valueType, valueHandler);
        assertEquals(clazz, mapType.getRawClass());
        assertEquals(keyType, mapType.getKeyType());
        assertEquals(valueType, mapType.getValueType());
        assertEquals(valueHandler, mapType.getValueHandler());
    }

    @Test
    public void testConstructWithTypeBindings() {
        Class<?> clazz = HashMap.class;
        JavaType keyType = new TypeReference<String>() {}.getType();
        JavaType valueType = new TypeReference<Integer>() {}.getType();
        TypeBindings bindings = new TypeBindings();
        JavaType mapType = MapType.construct(clazz, bindings, null, null, keyType, valueType);
        assertEquals(clazz, mapType.getRawClass());
        assertEquals(keyType, mapType.getKeyType());
        assertEquals(valueType, mapType.getValueType());
        assertNull(mapType.getValueHandler());
        assertNull(mapType.getTypeHandler());
    }

    @Test
    public void testConstructWithBogusSuperClass() {
        Class<?> clazz = HashMap.class;
        JavaType keyType = new TypeReference<String>() {}.getType();
        JavaType valueType = new TypeReference<Integer>() {}.getType();
        JavaType bogusSuperClass = new TypeReference<Object>() {}.getType();
        JavaType mapType = MapType.construct(clazz, null, bogusSuperClass, null, keyType, valueType);
        assertEquals(clazz, mapType.getRawClass());
        assertEquals(keyType, mapType.getKeyType());
        assertEquals(valueType, mapType.getValueType());
        assertNull(mapType.getValueHandler());
        assertNull(mapType.getTypeHandler());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructWithNullKeyType() {
        Class<?> clazz = HashMap.class;
        JavaType keyType = null;
        JavaType valueType = new TypeReference<Integer>() {}.getType();
        TypeBindings bindings = new TypeBindings();
        JavaType mapType = MapType.construct(clazz, bindings, null, null, keyType, valueType);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructWithNullValueType() {
        Class<?> clazz = HashMap.class;
        JavaType keyType = new TypeReference<String>() {}.getType();
        JavaType valueType = null;
        TypeBindings bindings = new TypeBindings();
        JavaType mapType = MapType.construct(clazz, bindings, null, null, keyType, valueType);
    }

    @Test
    public void testNarrow() {
        Class<?> superclass = HashMap.class;
        Class<?> subclass = LinkedHashMap.class;
        JavaType keyType = new TypeReference<String>() {}.getType();
        JavaType valueType = new TypeReference<Integer>() {}.getType();
        JavaType mapType = MapType.construct(superclass, null, null, null, keyType, valueType);
        JavaType narrowedMapType = mapType.narrow(subclass);
        assertEquals(subclass, narrowedMapType.getRawClass());
        assertEquals(keyType, narrowedMapType.getKeyType());
        assertEquals(valueType, narrowedMapType.getValueType());
    }

    @Test
    public void testWithTypeHandler() {
        Class<?> clazz = HashMap.class;
        JavaType keyType = new TypeReference<String>() {}.getType();
        JavaType valueType = new TypeReference<Integer>() {}.getType();
        Object valueHandler = new Object() {};
        JavaType mapType = new MapType(clazz, null, null, null, keyType, valueType);
        JavaType withTypeHandlerMapType = mapType.withTypeHandler(valueHandler);
        assertEquals(clazz, withTypeHandlerMapType.getRawClass());
        assertEquals(keyType, withTypeHandlerMapType.getKeyType());
        assertEquals(valueType, withTypeHandlerMapType.getValueType());
        assertEquals(valueHandler, withTypeHandlerMapType.getTypeHandler());
    }

    @Test
    public void testWithContentTypeHandler() {
        Class<?> clazz = HashMap.class;
        JavaType keyType = new TypeReference<String>() {}.getType();
        JavaType contentType = new TypeReference<String>() {}.getType();
        JavaType valueType = new TypeReference<Integer>() {}.getType();
        Object valueHandler = new Object() {};
        JavaType mapType = new MapType(clazz, null, null, null, keyType, valueType);
        JavaType withContentTypeHandlerMapType = mapType.withContentTypeHandler(valueHandler);
        assertEquals(clazz, withContentTypeHandlerMapType.getRawClass());
        assertEquals(keyType, withContentTypeHandlerMapType.getKeyType());
        assertEquals(contentType, withContentTypeHandlerMapType.getValueType());
        assertEquals(valueHandler, withContentTypeHandlerMapType.getValueHandler());
    }

    @Test
    public void testWithValueHandler() {
        Class<?> clazz = HashMap.class;
        JavaType keyType = new TypeReference<String>() {}.getType();
        JavaType valueType = new TypeReference<Integer>() {}.getType();
        Object valueHandler = new Object() {};
        JavaType mapType = new MapType(clazz, null, null, null, keyType, valueType);
        JavaType withValueHandlerMapType = mapType.withValueHandler(valueHandler);
        assertEquals(clazz, withValueHandlerMapType.getRawClass());
        assertEquals(keyType, withValueHandlerMapType.getKeyType());
        assertEquals(valueType, withValueHandlerMapType.getValueType());
        assertEquals(valueHandler, withValueHandlerMapType.getValueHandler());
    }

    @Test
    public void testWithContentValueHandler() {
        Class<?> clazz = HashMap.class;
        JavaType keyType = new TypeReference<String>() {}.getType();
        JavaType contentValueType = new TypeReference<String>() {}.getType();
        JavaType valueType = new TypeReference<Integer>() {}.getType();
        Object valueHandler = new Object() {};
        JavaType mapType = new MapType(clazz, null, null, null, keyType, valueType);
        JavaType withContentValueHandlerMapType = mapType.withContentValueHandler(valueHandler);
        assertEquals(clazz, withContentValueHandlerMapType.getRawClass());
        assertEquals(keyType, withContentValueHandlerMapType.getKeyType());
        assertEquals(contentValueType, withContentValueHandlerMapType.getValueType());
        assertEquals(valueHandler, withContentValueHandlerMapType.getValueHandler());
    }

    @Test
    public void testWithStaticTyping() {
        Class<?> clazz = HashMap.class;
        JavaType keyType = new TypeReference<String>() {}.getType();
        JavaType valueType = new TypeReference<Integer>() {}.getType();
        JavaType mapType = new MapType(clazz, null, null, null, keyType, valueType);
        JavaType withStaticTypingMapType = mapType.withStaticTyping();
        assertTrue(withStaticTypingMapType.isStatic());
    }

    @Test
    public void testWithContentType() {
        Class<?> clazz = HashMap.class;
        JavaType keyType = new TypeReference<String>() {}.getType();
        JavaType contentType = new TypeReference<String>() {}.getType();
        JavaType valueType = new TypeReference<Integer>() {}.getType();
        JavaType mapType = new MapType(clazz, null, null, null, keyType, valueType);
        JavaType withContentTypeMapType = mapType.withContentType(contentType);
        assertEquals(clazz, withContentTypeMapType.getRawClass());
        assertEquals(keyType, withContentTypeMapType.getKeyType());
        assertEquals(contentType, withContentTypeMapType.getValueType());
    }

    @Test
    public void testWithKeyType() {
        Class<?> clazz = HashMap.class;
        JavaType newKeyType = new TypeReference<StringBuilder>() {}.getType();
        JavaType keyType = new TypeReference<String>() {}.getType();
        JavaType valueType = new TypeReference<Integer>() {}.getType();
        JavaType mapType = new MapType(clazz, null, null, null, keyType, valueType);
        JavaType withKeyTypeMapType = mapType.withKeyType(newKeyType);
        assertEquals(clazz, withKeyTypeMapType.getRawClass());
        assertEquals(newKeyType, withKeyTypeMapType.getKeyType());
        assertEquals(valueType, withKeyTypeMapType.getValueType());
    }

    @Test
    public void testRefine() {
        Class<?> rawType = HashMap.class;
        JavaType keyType = new TypeReference<String>() {}.getType();
        JavaType valueType = new TypeReference<Integer>() {}.getType();
        TypeBindings bindings = new TypeBindings();
        JavaType mapType = new MapType(rawType, bindings, null, null, keyType, valueType);
        JavaType refinedMapType = mapType.refine(LinkedHashMap.class, null, null, null);
        assertEquals(LinkedHashMap.class, refinedMapType.getRawClass());
        assertEquals(keyType, refinedMapType.getKeyType());
        assertEquals(valueType, refinedMapType.getValueType());
    }

    @Test
    public void testToString() {
        Class<?> clazz = HashMap.class;
        JavaType keyType = new TypeReference<String>() {}.getType();
        JavaType valueType = new TypeReference<Integer>() {}.getType();
        JavaType mapType = new MapType(clazz, null, null, null, keyType, valueType);
        String toString = mapType.toString();
        assertTrue(toString.contains("map type"));
        assertTrue(toString.contains("class "+clazz.getName()));
        assertTrue(toString.contains(keyType.toString()));
        assertTrue(toString.contains(valueType.toString()));
    }

    @Test
    public void testWithKeyTypeHandler() {
        Class<?> clazz = HashMap.class;
        JavaType keyType = new TypeReference<String>() {}.getType();
        JavaType valueType = new TypeReference<Integer>() {}.getType();
        Object keyTypeHandler = new Object() {};
        JavaType mapType = new MapType(clazz, null, null, null, keyType, valueType);
        JavaType withKeyTypeHandlerMapType = mapType.withKeyTypeHandler(keyTypeHandler);
        assertEquals(clazz, withKeyTypeHandlerMapType.getRawClass());
        assertEquals(keyType, withKeyTypeHandlerMapType.getKeyType());
        assertEquals(valueType, withKeyTypeHandlerMapType.getValueType());
        assertEquals(keyTypeHandler, withKeyTypeHandlerMapType.getKeyTypeHandler());
    }

    @Test
    public void testWithKeyValueHandler() {
        Class<?> clazz = HashMap.class;
        JavaType keyType = new TypeReference<String>() {}.getType();
        JavaType valueType = new TypeReference<Integer>() {}.getType();
        Object keyValueHandler = new Object() {};
        JavaType mapType = new MapType(clazz, null, null, null, keyType, valueType);
        JavaType withKeyValueHandlerMapType = mapType.withKeyValueHandler(keyValueHandler);
        assertEquals(clazz, withKeyValueHandlerMapType.getRawClass());
        assertEquals(keyType, withKeyValueHandlerMapType.getKeyType());
        assertEquals(valueType, withKeyValueHandlerMapType.getValueType());
        assertEquals(keyValueHandler, withKeyValueHandlerMapType.getKeyValueHandler());
    }
}