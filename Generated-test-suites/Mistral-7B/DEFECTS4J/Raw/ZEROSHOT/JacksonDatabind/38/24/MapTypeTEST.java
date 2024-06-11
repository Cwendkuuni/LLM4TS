package com.fasterxml.jackson.databind.type;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.Serializable;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import com.fasterxml.jackson.core.type.TypeBindings;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.MapLikeType;
import com.fasterxml.jackson.databind.type.MapType;

public class MapTypeTest {

    @Test
    public void testConstructSimpleMapType() {
        JavaType keyType = JavaType.constructType(String.class, null);
        JavaType valueType = JavaType.constructType(Integer.class, null);
        JavaType mapType = MapType.construct(Map.class, null, null, null, keyType, valueType);
        assertEquals(Map.class, mapType.getRawClass());
        assertEquals(keyType, mapType.getKeyType());
        assertEquals(valueType, mapType.getValueType());
    }

    @Test
    public void testConstructStaticMapType() {
        JavaType keyType = JavaType.constructType(String.class, null);
        JavaType valueType = JavaType.constructType(Integer.class, null);
        JavaType mapType = MapType.construct(HashMap.class, null, null, null, keyType, valueType);
        assertEquals(HashMap.class, mapType.getRawClass());
        assertEquals(keyType, mapType.getKeyType());
        assertEquals(valueType, mapType.getValueType());
    }

    @Test
    public void testConstructWithBindings() {
        JavaType keyType = JavaType.constructType(String.class, null);
        JavaType valueType = JavaType.constructType(Integer.class, null);
        TypeBindings bindings = TypeBindings.defaultBindings();
        JavaType mapType = MapType.construct(Map.class, bindings, null, null, keyType, valueType);
        assertEquals(Map.class, mapType.getRawClass());
        assertEquals(keyType, mapType.getKeyType());
        assertEquals(valueType, mapType.getValueType());
        assertEquals(bindings, mapType.getTypeBindings());
    }

    @Test
    public void testConstructWithValueHandler() {
        JavaType keyType = JavaType.constructType(String.class, null);
        JavaType valueType = JavaType.constructType(Integer.class, null);
        Object valueHandler = new Object() {
            // Empty implementation for test
        };
        JavaType mapType = MapType.construct(Map.class, null, null, null, keyType, valueType, valueHandler, null, false);
        assertEquals(Map.class, mapType.getRawClass());
        assertEquals(keyType, mapType.getKeyType());
        assertEquals(valueType, mapType.getValueType());
        assertNotNull(mapType.getValueHandler());
        assertEquals(valueHandler, mapType.getValueHandler());
    }

    @Test
    public void testConstructWithTypeHandler() {
        JavaType keyType = JavaType.constructType(String.class, null);
        JavaType valueType = JavaType.constructType(Integer.class, null);
        Object typeHandler = new Object() {
            // Empty implementation for test
        };
        JavaType mapType = MapType.construct(Map.class, null, null, null, keyType, valueType, null, typeHandler, false);
        assertEquals(Map.class, mapType.getRawClass());
        assertEquals(keyType, mapType.getKeyType());
        assertEquals(valueType, mapType.getValueType());
        assertNotNull(mapType.getTypeHandler());
        assertEquals(typeHandler, mapType.getTypeHandler());
    }

    @Test
    public void testConstructWithStaticTyping() {
        JavaType keyType = JavaType.constructType(String.class, null);
        JavaType valueType = JavaType.constructType(Integer.class, null);
        JavaType mapType = MapType.construct(Map.class, null, null, null, keyType, valueType).withStaticTyping();
        assertTrue(mapType.isStatic());
        JavaType staticMapType = MapType.construct(Map.class, null, null, null, keyType, valueType).withStaticTyping();
        assertEquals(mapType, staticMapType);
    }

    @Test
    public void testWithTypeHandler() {
        JavaType keyType = JavaType.constructType(String.class, null);
        JavaType valueType = JavaType.constructType(Integer.class, null);
        Object valueHandler = new Object() {
            // Empty implementation for test
        };
        JavaType mapType = JavaType.construct(MapType.class, null, null, null, keyType, valueType);
        JavaType newMapType = mapType.withTypeHandler(valueHandler);
        assertEquals(mapType.getRawClass(), newMapType.getRawClass());
        assertEquals(mapType.getKeyType(), newMapType.getKeyType());
        assertEquals(mapType.getValueType(), newMapType.getValueType());
        assertNotNull(newMapType.getTypeHandler());
        assertEquals(valueHandler, newMapType.getTypeHandler());
    }

    @Test
    public void testWithContentTypeHandler() {
        JavaType keyType = JavaType.constructType(String.class, null);
        JavaType valueType = JavaType.constructType(Integer.class, null);
        Object contentTypeHandler = new Object() {
            // Empty implementation for test
        };
        JavaType mapType = JavaType.construct(MapType.class, null, null, null, keyType, valueType);
        JavaType newMapType = mapType.withContentTypeHandler(contentTypeHandler);
        assertEquals(mapType.getRawClass(), newMapType.getRawClass());
        assertEquals(mapType.getKeyType(), newMapType.getKeyType());
        assertEquals(mapType.getValueType(), newMapType.getValueType());
        assertNotNull(newMapType.getContentTypeHandler());
        assertEquals(contentTypeHandler, newMapType.getContentTypeHandler());
    }

    @Test
    public void testWithValueHandler() {
        JavaType keyType = JavaType.constructType(String.class, null);
        JavaType valueType = JavaType.constructType(Integer.class, null);
        Object valueHandler = new Object() {
            // Empty implementation for test
        };
        JavaType mapType = JavaType.construct(MapType.class, null, null, null, keyType, valueType);
        JavaType newMapType = mapType.withValueHandler(valueHandler);
        assertEquals(mapType.getRawClass(), newMapType.getRawClass());
        assertEquals(mapType.getKeyType(), newMapType.getKeyType());
        assertEquals(mapType.getValueType(), newMapType.getValueType());
        assertNotNull(newMapType.getValueHandler());
        assertEquals(valueHandler, newMapType.getValueHandler());
    }

    @Test
    public void testWithContentValueHandler() {
        JavaType keyType = JavaType.constructType(String.class, null);
        JavaType valueType = JavaType.constructType(Integer.class, null);
        Object contentValueHandler = new Object() {
            // Empty implementation for test
        };
        JavaType mapType = JavaType.construct(MapType.class, null, null, null, keyType, valueType);
        JavaType newMapType = mapType.withContentValueHandler(contentValueHandler);
        assertEquals(mapType.getRawClass(), newMapType.getRawClass());
        assertEquals(mapType.getKeyType(), newMapType.getKeyType());
        assertEquals(mapType.getValueType(), newMapType.getValueType());
        assertNotNull(newMapType.getContentValueHandler());
        assertEquals(contentValueHandler, newMapType.getContentValueHandler());
    }

    @Test
    public void testWithKeyTypeHandler() {
        JavaType keyType1 = JavaType.constructType(String.class, null);
        JavaType valueType = JavaType.constructType(Integer.class, null);
        Object keyTypeHandler = new Object() {
            // Empty implementation for test
        };
        JavaType mapType = JavaType.construct(MapType.class, null, null, null, keyType1, valueType);
        JavaType newMapType = mapType.withKeyTypeHandler(keyTypeHandler);
        assertEquals(mapType.getRawClass(), newMapType.getRawClass());
        assertEquals(keyTypeHandler, newMapType.getKeyTypeHandler());
        assertEquals(mapType.getKeyType(), newMapType.getKeyType());
        assertEquals(mapType.getValueType(), newMapType.getValueType());
    }

    @Test
    public void testWithKeyValueHandler() {
        JavaType keyType = JavaType.constructType(String.class, null);
        JavaType valueType = JavaType.constructType(Integer.class, null);
        Object keyValueHandler = new Object() {
            // Empty implementation for test
        };
        JavaType mapType = JavaType.construct(MapType.class, null, null, null, keyType, valueType);
        JavaType newMapType = mapType.withKeyValueHandler(keyValueHandler);
        assertEquals(mapType.getRawClass(), newMapType.getRawClass());
        assertEquals(mapType.getKeyType(), newMapType.getKeyType());
        assertEquals(mapType.getValueType(), newMapType.getValueType());
        assertNotNull(newMapType.getKeyValueHandler());
        assertEquals(keyValueHandler, newMapType.getKeyValueHandler());
    }

    @Test
    public void testToString() {
        JavaType keyType = JavaType.constructType(String.class, null);
        JavaType valueType = JavaType.constructType(Integer.class, null);
        JavaType mapType = MapType.construct(Map.class, null, null, null, keyType, valueType);
        String expected = "[map type; class Map<java.lang.String, java.lang.Integer>, java.lang.String -> java.lang.Integer]";
        assertEquals(expected, mapType.toString());
    }

    @Test
    public void testNarrow() {
        JavaType rawType = Map.class;
        JavaType superClass = Object.class;
        JavaType keyType = JavaType.constructType(String.class, null);
        JavaType valueType = JavaType.constructType(Integer.class, null);
        JavaType mapType = new MapType(rawType, null, superClass, null, keyType, valueType, null, null, false);
        JavaType narrowedType = mapType.narrow(String.class);
        assertEquals(MapType.class, narrowedType.getRawClass());
        assertEquals(String.class, narrowedType.getKeyType().getRawClass());
        assertEquals(Integer.class, narrowedType.getValueType().getRawClass());
    }

    @Test
    public void testRefine() {
        JavaType rawType = Map.class;
        JavaType keyType = JavaType.constructType(String.class, null);
        JavaType valueType = JavaType.constructType(Integer.class, null);
        JavaType mapType = new MapType(rawType, null, null, null, keyType, valueType, null, null, false);
        JavaType refinedType = mapType.refine(HashMap.class, null, null, null);
        assertEquals(MapType.class, refinedType.getRawClass());
        assertEquals(HashMap.class, refinedType.getRawClass());
        assertEquals(String.class, refinedType.getKeyType().getRawClass());
        assertEquals(Integer.class, refinedType.getValueType().getRawClass());
    }
}