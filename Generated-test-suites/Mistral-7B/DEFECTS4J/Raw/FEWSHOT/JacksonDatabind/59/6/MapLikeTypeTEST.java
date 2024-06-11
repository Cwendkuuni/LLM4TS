package com.fasterxml.jackson.databind.type;

import static org.junit.Assert.*;

import java.lang.reflect.TypeVariable;
import java.util.*;

import com.fasterxml.jackson.databind.JavaType;

public class MapLikeTypeTest {

    @Test
    public void testMapLikeType() {
        JavaType mapType = new MapLikeType(Map.class, TypeBindings.emptyBindings(), _bogusSuperClass(Map.class), null, String.class, Integer.class);
        assertEquals(Map.class, mapType.getRawClass());
        assertEquals(String.class, mapType.getKeyType());
        assertEquals(Integer.class, mapType.getValueType());
        assertTrue(mapType.isMapLikeType());
        assertTrue(mapType.isContainerType());
        assertFalse(mapType.isTrueMapType());
    }

    @Test
    public void testUpgradeFrom() {
        JavaType baseType = new TypeRef<LinkedHashMap<String, Integer>>() {}.getType();
        JavaType keyType = String.class;
        JavaType valueType = Integer.class;
        JavaType mapLikeType = MapLikeType.upgradeFrom(baseType, keyType, valueType);
        assertEquals(Map.class, mapLikeType.getRawClass());
        assertEquals(String.class, mapLikeType.getKeyType());
        assertEquals(Integer.class, mapLikeType.getValueType());
        assertTrue(mapLikeType.isMapLikeType());
        assertTrue(mapLikeType.isContainerType());
        assertFalse(mapLikeType.isTrueMapType());
    }

    @Test
    public void testConstruct() {
        TypeVariable<?>[] vars = HashMap.class.getTypeParameters();
        JavaType mapType = MapLikeType.construct(HashMap.class, String.class, Integer.class);
        assertEquals(HashMap.class, mapType.getRawClass());
        assertEquals(String.class, mapType.getKeyType());
        assertEquals(Integer.class, mapType.getValueType());
        assertTrue(mapType.isMapLikeType());
        assertTrue(mapType.isContainerType());
        assertFalse(mapType.isTrueMapType());
    }

    @Test
    public void testNarrow() {
        JavaType baseType = new TypeRef<HashMap<String, Integer>>() {}.getType();
        JavaType mapType = new MapLikeType(baseType.getTypeParameters()[0].getType(), String.class, Integer.class);
        JavaType narrowedType = mapType.narrow(HashMap.class);
        assertEquals(HashMap.class, narrowedType.getRawClass());
        assertEquals(String.class, narrowedType.getKeyType());
        assertEquals(Integer.class, narrowedType.getValueType());
        assertTrue(narrowedType.isMapLikeType());
        assertTrue(narrowedType.isContainerType());
        assertFalse(narrowedType.isTrueMapType());
    }

    @Test
    public void testWithKeyType() {
        JavaType baseType = new TypeRef<HashMap<String, Integer>>() {}.getType();
        JavaType mapType = new MapLikeType(baseType, String.class, Integer.class);
        JavaType newKeyType = Boolean.class;
        JavaType newMapType = mapType.withKeyType(newKeyType);
        assertEquals(HashMap.class, newMapType.getRawClass());
        assertEquals(Boolean.class, newMapType.getKeyType());
        assertEquals(Integer.class, newMapType.getValueType());
        assertTrue(newMapType.isMapLikeType());
        assertTrue(newMapType.isContainerType());
        assertFalse(newMapType.isTrueMapType());
    }

    @Test
    public void testWithContentType() {
        JavaType baseType = new TypeRef<HashMap<String, Integer>>() {}.getType();
        JavaType mapType = new MapLikeType(baseType, String.class, Integer.class);
        JavaType newValueType = Long.class;
        JavaType newMapType = mapType.withContentType(newValueType);
        assertEquals(HashMap.class, newMapType.getRawClass());
        assertEquals(String.class, newMapType.getKeyType());
        assertEquals(Long.class, newMapType.getValueType());
        assertTrue(newMapType.isMapLikeType());
        assertTrue(newMapType.isContainerType());
        assertFalse(newMapType.isTrueMapType());
    }

    @Test
    public void testWithTypeHandler() {
        JavaType mapType = new MapLikeType(Map.class, TypeBindings.emptyBindings(), _bogusSuperClass(Map.class), null, String.class, Integer.class, null, null, false);
        Object handler = new Object() {
            public void handle(Object value) {
            }
        };
        JavaType newMapType = mapType.withTypeHandler(handler);
        assertEquals(Map.class, newMapType.getRawClass());
        assertEquals(String.class, newMapType.getKeyType());
        assertEquals(Integer.class, newMapType.getValueType());
        assertNotNull(newMapType.getTypeHandler());
        assertEquals(handler, newMapType.getTypeHandler());
        assertTrue(newMapType.isMapLikeType());
        assertTrue(newMapType.isContainerType());
        assertFalse(newMapType.isTrueMapType());
    }

    @Test
    public void testWithContentTypeHandler() {
        JavaType mapType = new MapLikeType(Map.class, TypeBindings.emptyBindings(), _bogusSuperClass(Map.class), null, String.class, Integer.class, null, null, false);
        Object handler = new Object() {
            public Integer handle(Object value) {
                return (Integer) value;
            }
        };
        JavaType newMapType = mapType.withContentTypeHandler(handler);
        assertEquals(Map.class, newMapType.getRawClass());
        assertEquals(String.class, newMapType.getKeyType());
        assertEquals(Integer.class, newMapType.getValueType());
        assertNotNull(newMapType.getValueHandler());
        assertEquals(handler, newMapType.getValueHandler());
        assertTrue(newMapType.isMapLikeType());
        assertTrue(newMapType.isContainerType());
        assertFalse(newMapType.isTrueMapType());
    }

    @Test
    public void testWithValueHandler() {
        JavaType mapType = new MapLikeType(Map.class, TypeBindings.emptyBindings(), _bogusSuperClass(Map.class), null, String.class, Integer.class, null, null, false);
        Object handler = new Object() {
            public void handle(Object key, Object value) {
            }
        };
        JavaType newMapType = mapType.withValueHandler(handler);
        assertEquals(Map.class, newMapType.getRawClass());
        assertEquals(String.class, newMapType.getKeyType());
        assertEquals(Integer.class, newMapType.getValueType());
        assertNotNull(newMapType.getValueHandler());
        assertEquals(handler, newMapType.getValueHandler());
        assertTrue(newMapType.isMapLikeType());
        assertTrue(newMapType.isContainerType());
        assertFalse(newMapType.isTrueMapType());
    }

    @Test
    public void testWithContentValueHandler() {
        JavaType mapType = new MapLikeType(Map.class, TypeBindings.emptyBindings(), _bogusSuperClass(Map.class), null, String.class, Integer.class, null, null, false);
        Object handler = new Object() {
            public Integer handle(Object value) {
                return (Integer) value;
            }
        };
        JavaType newMapType = mapType.withContentValueHandler(handler);
        assertEquals(Map.class, newMapType.getRawClass());
        assertEquals(String.class, newMapType.getKeyType());
        assertEquals(Integer.class, newMapType.getValueType());
        assertNotNull(newMapType.getValueHandler());
        assertEquals(handler, newMapType.getValueHandler());
        assertTrue(newMapType.isMapLikeType());
        assertTrue(newMapType.isContainerType());
        assertFalse(newMapType.isTrueMapType());
    }

    @Test
    public void testWithKeyTypeHandler() {
        JavaType mapType = new MapLikeType(Map.class, TypeBindings.emptyBindings(), _bogusSuperClass(Map.class), null, String.class, Integer.class, null, null, false);
        Object handler = new Object() {
            public String handle(Object key) {
                return (String) key;
            }
        };
        JavaType newMapType = mapType.withKeyTypeHandler(handler);
        assertEquals(Map.class, newMapType.getRawClass());
        assertEquals(String.class, newMapType.getKeyType());
        assertEquals(Integer.class, newMapType.getValueType());
        assertNotNull(newMapType.getKeyHandler());
        assertEquals(handler, newMapType.getKeyHandler());
        assertTrue(newMapType.isMapLikeType());
        assertTrue(newMapType.isContainerType());
        assertFalse(newMapType.isTrueMapType());
    }

    @Test
    public void testWithKeyValueHandler() {
        JavaType mapType = new MapLikeType(Map.class, TypeBindings.emptyBindings(), _bogusSuperClass(Map.class), null, String.class, Integer.class, null, null, false);
        Object handler = new Object() {
            public void handle(Object key, Object value) {
            }
        };
        JavaType newMapType = mapType.withKeyValueHandler(handler);
        assertEquals(Map.class, newMapType.getRawClass());
        assertEquals(String.class, newMapType.getKeyType());
        assertEquals(Integer.class, newMapType.getValueType());
        assertNotNull(newMapType.getKeyHandler());
        assertEquals(handler, newMapType.getKeyHandler());
        assertNotNull(newMapType.getValueHandler());
        assertTrue(newMapType.isMapLikeType());
        assertTrue(newMapType.isContainerType());
        assertFalse(newMapType.isTrueMapType());
    }

    @Test
    public void testIsTrueMapType() {
        JavaType mapType = new MapLikeType(Map.class, TypeBindings.emptyBindings(), _bogusSuperClass(Map.class), null, String.class, Integer.class);
        assertTrue(mapType.isTrueMapType());

        JavaType listType = new TypeRef<ArrayList<String>>() {}.getType();
        assertFalse(listType.isTrueMapType());
    }

    private static Class<?> _bogusSuperClass(Class<?> clazz) {
        return Object.class;
    }
}