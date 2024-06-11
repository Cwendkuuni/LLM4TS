package com.example.junit;

import static org.junit.Assert.*;

import java.lang.reflect.TypeVariable;
import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.MapLikeType;

public class MapLikeTypeTest {

    // Basic test cases for each method

    @Test
    public void testMapLikeTypeConstructor() {
        JavaType keyType = new JavaType(String.class);
        JavaType valueType = new JavaType(Integer.class);
        MapLikeType mapLikeType = new MapLikeType(Map.class, null, null, null,
                keyType, valueType);
        assertEquals("Map<String, Integer>", mapLikeType.toString());
    }

    @Test
    public void testUpgradeFrom() {
        JavaType baseType = new JavaType(HashMap.class);
        JavaType keyType = new JavaType(String.class);
        JavaType valueType = new JavaType(Integer.class);
        MapLikeType mapLikeType = MapLikeType.upgradeFrom(baseType, keyType,
                valueType);
        assertEquals("Map<String, Integer>", mapLikeType.toString());
    }

    @Test
    public void testConstruct() {
        Class<Map<String, Integer>> rawType = Map.class;
        JavaType keyType = new JavaType(String.class);
        JavaType valueType = new JavaType(Integer.class);
        TypeBindings bindings = TypeBindings.emptyBindings();
        MapLikeType mapLikeType = new MapLikeType(rawType, bindings, null, null,
                keyType, valueType);
        assertEquals("Map<String, Integer>", mapLikeType.toString());
    }

    @Test
    public void testNarrow() {
        JavaType baseType = new JavaType(HashMap.class);
        JavaType keyType = new JavaType(String.class);
        JavaType valueType = new JavaType(Integer.class);
        MapLikeType mapLikeType = new MapLikeType(baseType, keyType, valueType);
        JavaType narrowedType = mapLikeType.narrow(HashMap.class);
        assertEquals("Map<String, Integer>", narrowedType.toString());
    }

    @Test
    public void testWithKeyType() {
        JavaType mapLikeType = new JavaType(Map.class, TypeVariable.of("K"),
                TypeVariable.of("V"));
        JavaType keyType = new JavaType(String.class);
        MapLikeType newMapLikeType = mapLikeType.withKeyType(keyType);
        assertEquals("Map<String, ? extends V>", newMapLikeType.toString());
    }

    @Test
    public void testWithContentType() {
        JavaType mapLikeType = new JavaType(Map.class, TypeVariable.of("K"),
                TypeVariable.of("V"));
        JavaType valueType = new JavaType(Integer.class);
        MapLikeType newMapLikeType = mapLikeType.withContentType(valueType);
        assertEquals("Map<K, Integer>", newMapLikeType.toString());
    }

    @Test
    public void testWithTypeHandler() {
        JavaType mapLikeType = new JavaType(Map.class, TypeVariable.of("K"),
                TypeVariable.of("V"));
        Object valueHandler = new Object() {};
        MapLikeType newMapLikeType = mapLikeType.withTypeHandler(valueHandler);
        assertEquals("Map<K, V>", newMapLikeType.toString());
    }

    @Test
    public void testWithContentTypeHandler() {
        JavaType mapLikeType = new JavaType(Map.class, TypeVariable.of("K"),
                TypeVariable.of("V"));
        Object valueHandler = new Object() {};
        JavaType valueType = new JavaType(Integer.class).withTypeHandler(valueHandler);
        MapLikeType newMapLikeType = mapLikeType.withContentTypeHandler(valueType);
        assertEquals("Map<K, Integer>", newMapLikeType.toString());
    }

    @Test
    public void testWithValueHandler() {
        JavaType mapLikeType = new JavaType(Map.class, TypeVariable.of("K"),
                TypeVariable.of("V"));
        Object valueHandler = new Object() {};
        MapLikeType newMapLikeType = mapLikeType.withValueHandler(valueHandler);
        assertEquals("Map<K, V>", newMapLikeType.toString());
    }

    @Test
    public void testWithContentValueHandler() {
        JavaType mapLikeType = new JavaType(Map.class, TypeVariable.of("K"),
                TypeVariable.of("V"));
        Object valueHandler = new Object() {};
        JavaType valueType = new JavaType(Integer.class).withValueHandler(valueHandler);
        MapLikeType newMapLikeType = mapLikeType.withContentValueHandler(valueType);
        assertEquals("Map<K, Integer>", newMapLikeType.toString());
    }

    @Test
    public void testWithStaticTyping() {
        JavaType mapLikeType = new JavaType(Map.class, TypeVariable.of("K"),
                TypeVariable.of("V"));
        MapLikeType newMapLikeType = mapLikeType.withStaticTyping();
        assertTrue(newMapLikeType.isStatic());
    }

    @Test
    public void testRefine() {
        Class<Map<String, Integer>> rawType = Map.class;
        JavaType keyType = new JavaType(String.class);
        JavaType valueType = new JavaType(Integer.class);
        TypeBindings bindings = TypeBindings.emptyBindings();
        MapLikeType mapLikeType = new MapLikeType(rawType, bindings, null, null,
                keyType, valueType);
        JavaType refinedType = mapLikeType.refine(rawType, null, null, null);
        assertEquals("Map<String, Integer>", refinedType.toString());
    }

    @Test
    public void testBuildCanonicalName() {
        JavaType mapLikeType = new JavaType(Map.class, TypeVariable.of("K"),
                TypeVariable.of("V"));
        assertEquals("[map-like type; class java.util.Map, class java.lang.Object<K>, class java.lang.Object<V>]",
                mapLikeType.buildCanonicalName().toString());
    }

    @Test
    public void testIsContainerType() {
        JavaType mapLikeType = new JavaType(Map.class, TypeVariable.of("K"),
                TypeVariable.of("V"));
        assertTrue(mapLikeType.isContainerType());
    }

    @Test
    public void testIsMapLikeType() {
        JavaType mapLikeType = new JavaType(Map.class, TypeVariable.of("K"),
                TypeVariable.of("V"));
        assertTrue(mapLikeType.isMapLikeType());
    }

    @Test
    public void testGetKeyType() {
        JavaType mapLikeType = new JavaType(Map.class, TypeVariable.of("K"),
                TypeVariable.of("V"));
        JavaType keyType = mapLikeType.getKeyType();
        assertEquals("class java.lang.Object<K>", keyType.getRawClass().getName());
    }

    @Test
    public void testGetContentType() {
        JavaType mapLikeType = new JavaType(Map.class, TypeVariable.of("K"),
                TypeVariable.of("V"));
        JavaType contentType = mapLikeType.getContentType();
        assertEquals("class java.lang.Object<V>", contentType.getRawClass().getName());
    }

    @Test
    public void testGetContentValueHandler() {
        JavaType mapLikeType = new JavaType(Map.class, TypeVariable.of("K"),
                TypeVariable.of("V"));
        Object contentValueHandler = mapLikeType.getContentValueHandler();
        assertNotNull(contentValueHandler);
    }

    @Test
    public void testGetContentTypeHandler() {
        JavaType mapLikeType = new JavaType(Map.class, TypeVariable.of("K"),
                TypeVariable.of("V"));
        Object contentTypeHandler = mapLikeType.getContentTypeHandler();
        assertNotNull(contentTypeHandler);
    }

    @Test
    public void testHasHandlers() {
        JavaType mapLikeType = new JavaType(Map.class, TypeVariable.of("K"),
                TypeVariable.of("V"));
        assertTrue(mapLikeType.hasHandlers());
    }

    @Test
    public void testGetErasedSignature() {
        JavaType mapLikeType = new JavaType(Map.class, TypeVariable.of("K"),
                TypeVariable.of("V"));
        StringBuilder sb = new StringBuilder();
        mapLikeType.getErasedSignature(sb);
        assertEquals("[map-like type; class java.util.Map]", sb.toString());
    }

    @Test
    public void testGetGenericSignature() {
        JavaType mapLikeType = new JavaType(Map.class, TypeVariable.of("K"),
                TypeVariable.of("V"));
        StringBuilder sb = new StringBuilder();
        mapLikeType.getGenericSignature(sb);
        assertEquals("[map-like type; class java.util.Map<K, V>]", sb.toString());
    }

    // Edge cases and exception handling test cases

    @Test(expected = IllegalArgumentException.class)
    public void testUpgradeFromInvalidBaseType() {
        JavaType baseType = new JavaType(String.class);
        JavaType keyType = new JavaType(String.class);
        JavaType valueType = new JavaType(Integer.class);
        MapLikeType.upgradeFrom(baseType, keyType, valueType);
        fail("should have thrown an IllegalArgumentException");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructInvalidRawType() {
        Class<Integer> rawType = Integer.class;
        JavaType keyType = new JavaType(String.class);
        JavaType valueType = new JavaType(Integer.class);
        TypeBindings bindings = TypeBindings.emptyBindings();
        MapLikeType mapLikeType = new MapLikeType(rawType, bindings, null, null,
                keyType, valueType);
        fail("should have thrown an IllegalArgumentException");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNarrowInvalidSubclass() {
        JavaType baseType = new JavaType(HashMap.class);
        JavaType keyType = new JavaType(String.class);
        JavaType valueType = new JavaType(Integer.class);
        MapLikeType mapLikeType = new MapLikeType(baseType, keyType, valueType);
        JavaType narrowedType = mapLikeType.narrow(List.class);
        fail("should have thrown an IllegalArgumentException");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testWithKeyTypeNull() {
        JavaType mapLikeType = new JavaType(Map.class, null, null, null, null,
                null);
        JavaType newKeyType = null;
        MapLikeType newMapLikeType = mapLikeType.withKeyType(newKeyType);
        fail("should have thrown an IllegalArgumentException");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testWithContentTypeNull() {
        JavaType mapLikeType = new JavaType(Map.class, null, null, null, null,
                null);
        JavaType newContentType = null;
        MapLikeType newMapLikeType = mapLikeType.withContentType(newContentType);
        fail("should have thrown an IllegalArgumentException");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testWithTypeHandlerNull() {
        JavaType mapLikeType = new JavaType(Map.class, null, null, null, null,
                null);
        Object newTypeHandler = null;
        MapLikeType newMapLikeType = mapLikeType.withTypeHandler(newTypeHandler);
        fail("should have thrown an IllegalArgumentException");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testWithContentTypeHandlerNull() {
        JavaType mapLikeType = new JavaType(Map.class, null, null, null, null,
                null);
        Object newContentTypeHandler = null;
        JavaType newContentType = new JavaType(Integer.class);
        MapLikeType newMapLikeType = mapLikeType.withContentTypeHandler(newContentTypeHandler);
        fail("should have thrown an IllegalArgumentException");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testWithValueHandlerNull() {
        JavaType mapLikeType = new JavaType(Map.class, null, null, null, null,
                null);
        Object newValueHandler = null;
        MapLikeType newMapLikeType = mapLikeType.withValueHandler(newValueHandler);
        fail("should have thrown an IllegalArgumentException");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testWithContentValueHandlerNull() {
        JavaType mapLikeType = new JavaType(Map.class, null, null, null, null,
                null);
        Object newContentValueHandler = null;
        JavaType newContentType = new JavaType(Integer.class);
        MapLikeType newMapLikeType = mapLikeType.withContentValueHandler(newContentValueHandler);
        fail("should have thrown an IllegalArgumentException");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testWithStaticTypingInvalid() {
        JavaType mapLikeType = new JavaType(Map.class, null, null, null, null,
                null);
        MapLikeType newMapLikeType = mapLikeType.withStaticTyping();
        fail("should have thrown an IllegalArgumentException");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testRefineInvalidRawType2() {
        Class<Map<String, Integer>> rawType = Map.class;
        JavaType keyType = new JavaType(String.class);
        JavaType valueType = new JavaType(Integer.class);
        TypeBindings bindings = TypeBindings.emptyBindings();
        MapLikeType mapLikeType = new MapLikeType(rawType, bindings, null, null,
                keyType, valueType);
        JavaType refinedType = mapLikeType.refine(List.class, null, null, null);
        fail("should have thrown an IllegalArgumentException");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testRefineInvalidBindings() {
        Class<Map<String, Integer>> rawType = Map.class;
        JavaType keyType = new JavaType(String.class);
        JavaType valueType = new JavaType(Integer.class);
        TypeBindings invalidBindings = new TypeBindings() {
            @Override
            public Map<TypeVariable<?>, JavaType> bindVariables(JavaType baseType) {
                throw new UnsupportedOperationException();
            }
        };
        MapLikeType mapLikeType = new MapLikeType(rawType, invalidBindings, null, null,
                keyType, valueType);
        JavaType refinedType = mapLikeType.refine(rawType, null, null, null);
        fail("should have thrown an IllegalArgumentException");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testRefineInvalidSuperInterfaces() {
        Class<Map<String, Integer>> rawType = Map.class;
        JavaType keyType = new JavaType(String.class);
        JavaType valueType = new JavaType(Integer.class);
        JavaType[] invalidSuperInterfaces = new JavaType[] {new JavaType(List.class)};
        MapLikeType mapLikeType = new MapLikeType(rawType, null, null,
                invalidSuperInterfaces, keyType, valueType);
        JavaType refinedType = mapLikeType.refine(rawType, null, null, null);
        fail("should have thrown an IllegalArgumentException");
    }
}