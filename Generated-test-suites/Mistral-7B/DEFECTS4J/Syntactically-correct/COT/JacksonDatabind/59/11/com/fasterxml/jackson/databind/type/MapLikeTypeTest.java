package com.example.mapliketype;

import static org.junit.Assert.*;

import org.junit.Test;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.MapLikeType;

public class MapLikeTypeTest {

    @Test
    public void testMapLikeTypeConstructor() {
        JavaType keyType = new JavaType(String.class, null);
        JavaType valueType = new JavaType(Integer.class, null);
        MapLikeType mapLikeType = new MapLikeType(Map.class, null, null, null, keyType, valueType);
        assertNotNull(mapLikeType);
        assertEquals(Map.class, mapLikeType.getRawClass());
        assertEquals(keyType, mapLikeType.getKeyType());
        assertEquals(valueType, mapLikeType.getValueType());
    }

    @Test
    public void testUpgradeFrom() {
        JavaType baseType = new JavaType(HashMap.class, null);
        JavaType keyType = new JavaType(String.class, null);
        JavaType valueType = new JavaType(Integer.class, null);
        MapLikeType mapLikeType = MapLikeType.upgradeFrom(baseType, keyType, valueType);
        assertInstanceOf(MapLikeType.class, mapLikeType);
        assertEquals(keyType, mapLikeType.getKeyType());
        assertEquals(valueType, mapLikeType.getValueType());
    }

    @Test
    public void testConstructDeprecated() {
        @SuppressWarnings("rawtypes")
        Class rawType = Map.class;
        JavaType keyType = new JavaType(String.class, null);
        JavaType valueType = new JavaType(Integer.class, null);
        MapLikeType mapLikeType = MapLikeType.construct(rawType, keyType, valueType);
        assertInstanceOf(MapLikeType.class, mapLikeType);
        assertEquals(keyType, mapLikeType.getKeyType());
        assertEquals(valueType, mapLikeType.getValueType());
    }

    @Test
    public void testNarrowDeprecated() {
        JavaType subclassType = HashMap.class;
        MapLikeType mapLikeType = new MapLikeType(Map.class, null, null, null,
                new JavaType(String.class, null), new JavaType(Integer.class, null));
        JavaType narrowedType = mapLikeType.narrow(subclassType);
        assertInstanceOf(MapLikeType.class, narrowedType);
        assertEquals(subclassType, narrowedType.getRawClass());
        assertEquals(mapLikeType.getKeyType(), narrowedType.getKeyType());
        assertEquals(mapLikeType.getValueType(), narrowedType.getValueType());
    }

    @Test
    public void testWithKeyType() {
        JavaType originalKeyType = new JavaType(String.class, null);
        JavaType newKeyType = new JavaType(Integer.class, null);
        MapLikeType originalMapLikeType = new MapLikeType(Map.class, null, null,
                null, originalKeyType, new JavaType(Integer.class, null));
        MapLikeType newMapLikeType = originalMapLikeType.withKeyType(newKeyType);
        assertNotSame(originalMapLikeType, newMapLikeType);
        assertEquals(newKeyType, newMapLikeType.getKeyType());
        assertEquals(originalMapLikeType.getValueType(), newMapLikeType.getValueType());
    }

    @Test
    public void testWithContentType() {
        JavaType originalValueType = new JavaType(Integer.class, null);
        JavaType newValueType = new JavaType(Double.class, null);
        MapLikeType originalMapLikeType = new MapLikeType(Map.class, null, null,
                null, new JavaType(String.class, null), originalValueType);
        MapLikeType newMapLikeType = originalMapLikeType.withContentType(newValueType);
        assertNotSame(originalMapLikeType, newMapLikeType);
        assertEquals(originalMapLikeType.getKeyType(), newMapLikeType.getKeyType());
        assertEquals(newValueType, newMapLikeType.getValueType());
    }

    @Test
    public void testWithTypeHandler() {
        Object handler = new Object() {};
        MapLikeType mapLikeType = new MapLikeType(Map.class, null, null, null,
                new JavaType(String.class, null), new JavaType(Integer.class, null));
        MapLikeType newMapLikeType = mapLikeType.withTypeHandler(handler);
        assertNotSame(mapLikeType, newMapLikeType);
        assertEquals(mapLikeType.getKeyType(), newMapLikeType.getKeyType());
        assertEquals(mapLikeType.getValueType(), newMapLikeType.getValueType());
        assertEquals(handler, newMapLikeType.getTypeHandler());
    }

    @Test
    public void testWithContentTypeHandler() {
        Object handler = new Object() {};
        MapLikeType mapLikeType = new MapLikeType(Map.class, null, null, null,
                new JavaType(String.class, null), new JavaType(Integer.class, null).withTypeHandler(handler));
        MapLikeType newMapLikeType = mapLikeType.withContentTypeHandler(handler);
        assertNotSame(mapLikeType, newMapLikeType);
        assertEquals(mapLikeType.getKeyType(), newMapLikeType.getKeyType());
        assertEquals(mapLikeType.getValueType(), newMapLikeType.getValueType());
        assertEquals(handler, newMapLikeType.getContentTypeHandler());
    }

    @Test
    public void testWithValueHandler() {
        Object handler = new Object() {};
        MapLikeType mapLikeType = new MapLikeType(Map.class, null, null, null,
                new JavaType(String.class, null), new JavaType(Integer.class, null));
        MapLikeType newMapLikeType = mapLikeType.withValueHandler(handler);
        assertNotSame(mapLikeType, newMapLikeType);
        assertEquals(mapLikeType.getKeyType(), newMapLikeType.getKeyType());
        assertEquals(mapLikeType.getValueType(), newMapLikeType.getValueType());
        assertEquals(handler, newMapLikeType.getValueHandler());
    }

    @Test
    public void testWithContentValueHandler() {
        Object handler = new Object() {};
        MapLikeType mapLikeType = new MapLikeType(Map.class, null, null, null,
                new JavaType(String.class, null), new JavaType(Integer.class, null).withValueHandler(handler));
        MapLikeType newMapLikeType = mapLikeType.withContentValueHandler(handler);
        assertNotSame(mapLikeType, newMapLikeType);
        assertEquals(mapLikeType.getKeyType(), newMapLikeType.getKeyType());
        assertEquals(mapLikeType.getValueType(), newMapLikeType.getValueType());
        assertEquals(handler, newMapLikeType.getContentValueHandler());
    }

    @Test
    public void testWithStaticTyping() {
        MapLikeType mapLikeType = new MapLikeType(Map.class, null, null, null,
                new JavaType(String.class, null), new JavaType(Integer.class, null));
        MapLikeType newMapLikeType = mapLikeType.withStaticTyping();
        assertTrue(newMapLikeType.isStaticTyping());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testUpgradeFromInvalidBaseType() {
        JavaType baseType = new JavaType(String.class, null);
        JavaType keyType = new JavaType(String.class, null);
        JavaType valueType = new JavaType(Integer.class, null);
        MapLikeType.upgradeFrom(baseType, keyType, valueType);
        fail("IllegalArgumentException expected");
    }

    @Test
    public void testRefine() {
        @SuppressWarnings("rawtypes")
        Class rawType = HashMap.class;
        TypeBindings bindings = TypeBindings.emptyBindings();
        JavaType superClass = HashMap.class;
        JavaType[] superInterfaces = new JavaType[] { Collection.class };
        JavaType keyType = new JavaType(String.class, null);
        JavaType valueType = new JavaType(Integer.class, null);
        MapLikeType mapLikeType = new MapLikeType(rawType, bindings, superClass,
                superInterfaces, keyType, valueType);
        MapLikeType refinedType = mapLikeType.refine(Map.class, null, null, null);
        assertInstanceOf(MapLikeType.class, refinedType);
        assertNotSame(mapLikeType, refinedType);
        assertEquals(Map.class, refinedType.getRawClass());
        assertEquals(keyType, refinedType.getKeyType());
        assertEquals(valueType, refinedType.getValueType());
    }

    @Test
    public void testIsTrueMapType() {
        MapLikeType mapLikeType = new MapLikeType(Map.class, null, null, null,
                new JavaType(String.class, null), new JavaType(Integer.class, null));
        assertTrue(mapLikeType.isTrueMapType());
    }

    @Test
    public void testIsContainerType() {
        MapLikeType mapLikeType = new MapLikeType(Map.class, null, null, null,
                new JavaType(String.class, null), new JavaType(Integer.class, null));
        assertTrue(mapLikeType.isContainerType());
    }

    @Test
    public void testIsMapLikeType() {
        MapLikeType mapLikeType = new MapLikeType(Map.class, null, null, null,
                new JavaType(String.class, null), new JavaType(Integer.class, null));
        assertTrue(mapLikeType.isMapLikeType());
    }

    @Test
    public void testGetKeyType() {
        JavaType keyType = new JavaType(String.class, null);
        MapLikeType mapLikeType = new MapLikeType(Map.class, null, null, null,
                keyType, new JavaType(Integer.class, null));
        assertEquals(keyType, mapLikeType.getKeyType());
    }

    @Test
    public void testGetContentType() {
        JavaType valueType = new JavaType(Integer.class, null);
        MapLikeType mapLikeType = new MapLikeType(Map.class, null, null, null,
                new JavaType(String.class, null), valueType);
        assertEquals(valueType, mapLikeType.getValueType());
    }

    @Test
    public void testGetContentValueHandler() {
        Object valueHandler = new Object() {};
        JavaType valueType = new JavaType(Integer.class, null).withValueHandler(valueHandler);
        MapLikeType mapLikeType = new MapLikeType(Map.class, null, null, null,
                new JavaType(String.class, null), valueType);
        assertEquals(valueHandler, mapLikeType.getContentValueHandler());
    }

    @Test
    public void testGetContentTypeHandler() {
        Object typeHandler = new Object() {};
        JavaType valueType = new JavaType(Integer.class, null).withTypeHandler(typeHandler);
        MapLikeType mapLikeType = new MapLikeType(Map.class, null, null, null,
                new JavaType(String.class, null), valueType);
        assertEquals(typeHandler, mapLikeType.getContentTypeHandler());
    }

    @Test
    public void testHasHandlers() {
        JavaType keyType = new JavaType(String.class, null).withTypeHandler(new Object() {});
        JavaType valueType = new JavaType(Integer.class, null).withValueHandler(new Object() {});
        MapLikeType mapLikeType = new MapLikeType(Map.class, null, null, null, keyType, valueType);
        assertTrue(mapLikeType.hasHandlers());
    }

    @Test
    public void testGetErasedSignature() {
        JavaType keyType = new JavaType(String.class, null);
        JavaType valueType = new JavaType(Integer.class, null);
        MapLikeType mapLikeType = new MapLikeType(Map.class, null, null, null, keyType, valueType);
        StringBuilder signature = new StringBuilder();
        mapLikeType.getErasedSignature(signature);
        assertEquals("java.util.Map", signature.toString());
    }

    @Test
    public void testGetGenericSignature() {
        JavaType keyType = new JavaType(String.class, null);
        JavaType valueType = new JavaType(Integer.class, null);
        MapLikeType mapLikeType = new MapLikeType(Map.class, null, null, null, keyType, valueType);
        StringBuilder signature = new StringBuilder();
        mapLikeType.getGenericSignature(signature);
        assertEquals("[Ljava.util.Map;<Ljava.lang.String;Ljava.lang.Integer;>", signature.toString());
    }
}