package org.apache.commons.cli.test;

import static org.junit.Assert.*;

import org.junit.Test;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.MapLikeType;

public class MapLikeTypeTest {

    @Test
    public void testNewInstance() {
        JavaType keyType = TypeFactory.defaultInstance().constructType(String.class);
        JavaType valueType = TypeFactory.defaultInstance().constructType(Integer.class);

        MapLikeType mapLikeType = new MapLikeType(Map.class, TypeBindings.emptyBindings(), null, null, keyType, valueType);

        assertEquals(Map.class, mapLikeType.getRawType());
        assertEquals(keyType, mapLikeType.getKeyType());
        assertEquals(valueType, mapLikeType.getContentType());
        assertNull(mapLikeType.getValueHandler());
        assertNull(mapLikeType.getTypeHandler());
        assertFalse(mapLikeType.isStatic());
    }

    @Test
    public void testNewInstanceWithBindings() {
        JavaType keyType = TypeFactory.defaultInstance().constructType(String.class);
        JavaType valueType = TypeFactory.defaultInstance().constructType(Integer.class);

        TypeBindings bindings = TypeBindings.create(Map.class, keyType, valueType);

        MapLikeType mapLikeType = new MapLikeType(Map.class, bindings, null, null, keyType, valueType);

        assertEquals(Map.class, mapLikeType.getRawType());
        assertEquals(keyType, mapLikeType.getKeyType());
        assertEquals(valueType, mapLikeType.getContentType());
        assertNull(mapLikeType.getValueHandler());
        assertNull(mapLikeType.getTypeHandler());
        assertFalse(mapLikeType.isStatic());
        assertEquals(bindings, mapLikeType.getBindings());
    }

    @Test
    public void testUpgradeFrom() {
        JavaType baseType = TypeFactory.defaultInstance().constructType(HashMap.class);
        JavaType keyType = TypeFactory.defaultInstance().constructType(String.class);
        JavaType valueType = TypeFactory.defaultInstance().constructType(Integer.class);

        MapLikeType mapLikeType = MapLikeType.upgradeFrom(baseType, keyType, valueType);

        assertEquals(MapLikeType.class, mapLikeType.getClass());
        assertEquals(Map.class, mapLikeType.getRawType());
        assertEquals(keyType, mapLikeType.getKeyType());
        assertEquals(valueType, mapLikeType.getContentType());
        assertNull(mapLikeType.getValueHandler());
        assertNull(mapLikeType.getTypeHandler());
        assertFalse(mapLikeType.isStatic());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testUpgradeFromInvalidBaseType() {
        JavaType baseType = TypeFactory.defaultInstance().constructType(Integer.class);
        JavaType keyType = TypeFactory.defaultInstance().constructType(String.class);
        JavaType valueType = TypeFactory.defaultInstance().constructType(Integer.class);

        MapLikeType.upgradeFrom(baseType, keyType, valueType);
    }

    @Test
    public void testNewInstanceWithHandlers() {
        JavaType keyType = TypeFactory.defaultInstance().constructType(String.class);
        JavaType valueType = TypeFactory.defaultInstance().constructType(Integer.class);
        Object valueHandler = new Object() {
            @SuppressWarnings("unchecked")
            public Integer deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException {
                return jp.getValueAsInt();
            }
        };
        Object typeHandler = new Object() {
            @SuppressWarnings("unchecked")
            public Map<String, Integer> deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException {
                return jp.readValueAs(Map.class);
            }
        };

        MapLikeType mapLikeType = new MapLikeType(Map.class, null, null, null, keyType, valueType, valueHandler, typeHandler, false);

        assertEquals(Map.class, mapLikeType.getRawType());
        assertEquals(keyType, mapLikeType.getKeyType());
        assertEquals(valueType, mapLikeType.getContentType());
        assertEquals(valueHandler, mapLikeType.getValueHandler());
        assertEquals(typeHandler, mapLikeType.getTypeHandler());
        assertFalse(mapLikeType.isStatic());
    }

    @Test
    public void testNewInstanceWithStaticTyping() {
        JavaType keyType = TypeFactory.defaultInstance().constructType(String.class);
        JavaType valueType = TypeFactory.defaultInstance().constructType(Integer.class);

        MapLikeType mapLikeType = new MapLikeType(Map.class, null, null, null, keyType, valueType).withStaticTyping();

        assertTrue(mapLikeType.isStatic());
    }

    @Test
    public void testWithKeyType() {
        JavaType baseType = TypeFactory.defaultInstance().constructType(HashMap.class);
        JavaType keyType = TypeFactory.defaultInstance().constructType(String.class);
        JavaType valueType = TypeFactory.defaultInstance().constructType(Integer.class);

        MapLikeType mapLikeType = new MapLikeType(baseType, null, null, null, null, null);
        JavaType newKeyType = TypeFactory.defaultInstance().constructType(String[].class);

        MapLikeType newMapLikeType = mapLikeType.withKeyType(newKeyType);

        assertEquals(MapLikeType.class, newMapLikeType.getClass());
        assertEquals(baseType, newMapLikeType.getRawType());
        assertEquals(newKeyType, newMapLikeType.getKeyType());
        assertEquals(valueType, newMapLikeType.getContentType());
        assertNull(newMapLikeType.getValueHandler());
        assertNull(newMapLikeType.getTypeHandler());
        assertFalse(newMapLikeType.isStatic());
    }

    @Test
    public void testWithContentType() {
        JavaType baseType = TypeFactory.defaultInstance().constructType(HashMap.class);
        JavaType keyType = TypeFactory.defaultInstance().constructType(String.class);
        JavaType valueType = TypeFactory.defaultInstance().constructType(Integer.class);
        JavaType newValueType = TypeFactory.defaultInstance().constructType(Long.class);

        MapLikeType mapLikeType = new MapLikeType(baseType, null, null, null, keyType, valueType);

        MapLikeType newMapLikeType = mapLikeType.withContentType(newValueType);

        assertEquals(MapLikeType.class, newMapLikeType.getClass());
        assertEquals(baseType, newMapLikeType.getRawType());
        assertEquals(keyType, newMapLikeType.getKeyType());
        assertEquals(newValueType, newMapLikeType.getContentType());
        assertNull(newMapLikeType.getValueHandler());
        assertNull(newMapLikeType.getTypeHandler());
        assertFalse(newMapLikeType.isStatic());
    }

    @Test
    public void testWithTypeHandler() {
        JavaType baseType = TypeFactory.defaultInstance().constructType(HashMap.class);
        JavaType keyType = TypeFactory.defaultInstance().constructType(String.class);
        JavaType valueType = TypeFactory.defaultInstance().constructType(Integer.class);
        Object handler = new Object();

        MapLikeType mapLikeType = new MapLikeType(baseType, null, null, null, keyType, valueType);

        MapLikeType newMapLikeType = mapLikeType.withTypeHandler(handler);

        assertEquals(MapLikeType.class, newMapLikeType.getClass());
        assertEquals(baseType, newMapLikeType.getRawType());
        assertEquals(keyType, newMapLikeType.getKeyType());
        assertEquals(valueType, newMapLikeType.getContentType());
        assertEquals(handler, newMapLikeType.getTypeHandler());
        assertNull(newMapLikeType.getValueHandler());
        assertFalse(newMapLikeType.isStatic());
    }

    @Test
    public void testWithContentTypeHandler() {
        JavaType baseType = TypeFactory.defaultInstance().constructType(HashMap.class);
        JavaType keyType = TypeFactory.defaultInstance().constructType(String.class);
        JavaType valueType = TypeFactory.defaultInstance().constructType(Integer.class);
        Object handler = new Object();

        MapLikeType mapLikeType = new MapLikeType(baseType, null, null, null, keyType, valueType);

        MapLikeType newMapLikeType = mapLikeType.withContentTypeHandler(handler);

        assertEquals(MapLikeType.class, newMapLikeType.getClass());
        assertEquals(baseType, newMapLikeType.getRawType());
        assertEquals(keyType, newMapLikeType.getKeyType());
        assertEquals(valueType.withTypeHandler(handler), newMapLikeType.getContentType());
        assertNull(newMapLikeType.getValueHandler());
        assertEquals(handler, newMapLikeType.getTypeHandler());
        assertFalse(newMapLikeType.isStatic());
    }

    @Test
    public void testWithValueHandler() {
        JavaType baseType = TypeFactory.defaultInstance().constructType(HashMap.class);
        JavaType keyType = TypeFactory.defaultInstance().constructType(String.class);
        JavaType valueType = TypeFactory.defaultInstance().constructType(Integer.class);
        Object handler = new Object();

        MapLikeType mapLikeType = new MapLikeType(baseType, null, null, null, keyType, valueType);

        MapLikeType newMapLikeType = mapLikeType.withValueHandler(handler);

        assertEquals(MapLikeType.class, newMapLikeType.getClass());
        assertEquals(baseType, newMapLikeType.getRawType());
        assertEquals(keyType, newMapLikeType.getKeyType());
        assertEquals(valueType, newMapLikeType.getContentType());
        assertEquals(handler, newMapLikeType.getValueHandler());
        assertNull(newMapLikeType.getTypeHandler());
        assertFalse(newMapLikeType.isStatic());
    }

    @Test
    public void testWithContentValueHandler() {
        JavaType baseType = TypeFactory.defaultInstance().constructType(HashMap.class);
        JavaType keyType = TypeFactory.defaultInstance().constructType(String.class);
        JavaType valueType = TypeFactory.defaultInstance().constructType(Integer.class);
        Object handler = new Object();

        MapLikeType mapLikeType = new MapLikeType(baseType, null, null, null, keyType, valueType);

        MapLikeType newMapLikeType = mapLikeType.withContentValueHandler(handler);

        assertEquals(MapLikeType.class, newMapLikeType.getClass());
        assertEquals(baseType, newMapLikeType.getRawType());
        assertEquals(keyType, newMapLikeType.getKeyType());
        assertEquals(valueType.withValueHandler(handler), newMapLikeType.getContentType());
        assertNull(newMapLikeType.getValueHandler());
        assertNull(newMapLikeType.getTypeHandler());
        assertFalse(newMapLikeType.isStatic());
    }
}