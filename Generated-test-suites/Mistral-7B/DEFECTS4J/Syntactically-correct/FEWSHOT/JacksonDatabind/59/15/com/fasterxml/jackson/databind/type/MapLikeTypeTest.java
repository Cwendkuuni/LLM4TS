package org.apache.commons.cli.test; // Adjust the package name if necessary

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.TypeBindings;
import com.fasterxml.jackson.databind.type.TypeBase;
import com.fasterxml.jackson.databind.type.TypeBindings.TypeBinding;
import com.fasterxml.jackson.databind.type.TypeModifier;

public class MapLikeTypeTest {

    @Test
    public void testIsContainerType() {
        JavaType mapType = TypeModifier.constructMapType(Map.class, String.class, Integer.class);
        MapLikeType mapLikeType = (MapLikeType) mapType;
        assertTrue(mapLikeType.isContainerType());
        assertTrue(mapLikeType.isMapLikeType());
    }

    @Test
    public void testIsTrueMapType() {
        JavaType mapType = TypeModifier.constructMapType(Map.class, String.class, Integer.class);
        MapLikeType mapLikeType = (MapLikeType) mapType;
        assertTrue(mapLikeType.isTrueMapType());

        JavaType notMapType = TypeFactory.defaultInstance().constructType(List.class, String.class);
        MapLikeType notMapLikeType = (MapLikeType) notMapType;
        assertFalse(notMapLikeType.isTrueMapType());
    }

    @Test
    public void testUpgradeFrom() {
        JavaType listType = TypeFactory.defaultInstance().constructType(List.class, String.class);
        JavaType mapKeyType = TypeFactory.defaultInstance().constructType(String.class);
        JavaType mapValueType = TypeFactory.defaultInstance().constructType(Integer.class);
        JavaType mapLikeType = MapLikeType.upgradeFrom(listType, mapKeyType, mapValueType);

        assertTrue(mapLikeType instanceof MapLikeType);
        assertEquals(mapKeyType, mapLikeType.getKeyType());
        assertEquals(mapValueType, mapLikeType.getValueType());
    }

    @Test
    public void testConstruct() {
        JavaType mapType = TypeModifier.constructMapType(Map.class, String.class, Integer.class);
        MapLikeType mapLikeType = (MapLikeType) mapType;
        JavaType keyType = mapLikeType.getKeyType();
        JavaType valueType = mapLikeType.getValueType();

        TypeBindings bindings = mapLikeType.getBindings();
        TypeBinding keyBinding = bindings.findBinding(String.class);
        TypeBinding valueBinding = bindings.findBinding(Integer.class);

        JavaType constructedMapLikeType = MapLikeType.construct(mapType.getRawClass(), keyType, valueType);
        MapLikeType constructedMapLikeTypeInstance = (MapLikeType) constructedMapLikeType;

        assertEquals(mapLikeType, constructedMapLikeTypeInstance);
        assertEquals(bindings, constructedMapLikeTypeInstance.getBindings());
        assertEquals(keyBinding, constructedMapLikeTypeInstance.getKeyBinding());
        assertEquals(valueBinding, constructedMapLikeTypeInstance.getValueBinding());
    }

    @Test
    public void testNarrow() {
        JavaType listType = TypeFactory.defaultInstance().constructType(List.class, String.class);
        JavaType mapKeyType = TypeFactory.defaultInstance().constructType(String.class);
        JavaType mapValueType = TypeFactory.defaultInstance().constructType(Integer.class);
        JavaType mapLikeType = MapLikeType.upgradeFrom(listType, mapKeyType, mapValueType);

        MapLikeType narrowedMapLikeType = mapLikeType.narrow(HashMap.class);
        assertNotNull(narrowedMapLikeType);
        assertTrue(narrowedMapLikeType instanceof MapLikeType);
        assertEquals(mapKeyType, narrowedMapLikeType.getKeyType());
        assertEquals(mapValueType, narrowedMapLikeType.getValueType());
    }

    @Test
    public void testWithKeyType() {
        JavaType mapType = TypeModifier.constructMapType(Map.class, String.class, Integer.class);
        MapLikeType mapLikeType = (MapLikeType) mapType;
        JavaType keyType = TypeFactory.defaultInstance().constructType(String.class);

        MapLikeType newMapLikeType = mapLikeType.withKeyType(keyType);
        assertEquals(mapLikeType, newMapLikeType);
        assertNotEquals(keyType, newMapLikeType.getKeyType());
        assertEquals(mapLikeType.getKeyType(), newMapLikeType.getKeyType());
    }

    @Test
    public void testWithContentType() {
        JavaType mapType = TypeModifier.constructMapType(Map.class, String.class, Integer.class);
        MapLikeType mapLikeType = (MapLikeType) mapType;
        JavaType valueType = TypeFactory.defaultInstance().constructType(Long.class);

        MapLikeType newMapLikeType = mapLikeType.withContentType(valueType);
        assertEquals(mapLikeType, newMapLikeType);
        assertNotEquals(valueType, newMapLikeType.getValueType());
        assertEquals(mapLikeType.getValueType(), newMapLikeType.getValueType());
    }

    @Test
    public void testWithTypeHandler() {
        JavaType mapType = TypeModifier.constructMapType(Map.class, String.class, Integer.class);
        MapLikeType mapLikeType = (MapLikeType) mapType;
        Object handler = new Object();

        MapLikeType newMapLikeType = mapLikeType.withTypeHandler(handler);
        assertEquals(mapLikeType, newMapLikeType);
        assertNotEquals(handler, newMapLikeType.getTypeHandler());
        assertEquals(mapLikeType.getTypeHandler(), newMapLikeType.getTypeHandler());
    }

    @Test
    public void testWithContentTypeHandler() {
        JavaType mapType = TypeModifier.constructMapType(Map.class, String.class, Integer.class);
        MapLikeType mapLikeType = (MapLikeType) mapType;
        Object handler = new Object();

        MapLikeType newMapLikeType = mapLikeType.withContentTypeHandler(handler);
        assertEquals(mapLikeType, newMapLikeType);
        assertNotEquals(handler, newMapLikeType.getValueType().getTypeHandler());
        assertEquals(mapLikeType.getValueType().getTypeHandler(), newMapLikeType.getValueType().getTypeHandler());
    }

    @Test
    public void testWithValueHandler() {
        JavaType mapType = TypeModifier.constructMapType(Map.class, String.class, Integer.class);
        MapLikeType mapLikeType = (MapLikeType) mapType;
        Object handler = new Object();

        MapLikeType newMapLikeType = mapLikeType.withValueHandler(handler);
        assertEquals(mapLikeType, newMapLikeType);
        assertNotEquals(handler, newMapLikeType.getValueHandler());
        assertEquals(mapLikeType.getValueHandler(), newMapLikeType.getValueHandler());
    }

    @Test
    public void testWithContentValueHandler() {
        JavaType mapType = TypeModifier.constructMapType(Map.class, String.class, Integer.class);
        MapLikeType mapLikeType = (MapLikeType) mapType;
        Object handler = new Object();

        MapLikeType newMapLikeType = mapLikeType.withContentValueHandler(handler);
        assertEquals(mapLikeType, newMapLikeType);
        assertNotEquals(handler, newMapLikeType.getValueType().getValueHandler());
        assertEquals(mapLikeType.getValueType().getValueHandler(), newMapLikeType.getValueType().getValueHandler());
    }

    @Test
    public void testWithStaticTyping() {
        JavaType mapType = TypeModifier.constructMapType(Map.class, String.class, Integer.class);
        MapLikeType mapLikeType = (MapLikeType) mapType;

        MapLikeType newMapLikeType = mapLikeType.withStaticTyping();
        assertEquals(mapLikeType, newMapLikeType);
        assertTrue(newMapLikeType._asStatic);
    }

    @Test
    public void testRefine() {
        JavaType mapType = TypeModifier.constructMapType(Map.class, String.class, Integer.class);
        MapLikeType mapLikeType = (MapLikeType) mapType;
        Class<Map<String, Integer>> rawType = (Class<Map<String, Integer>>) mapType.getRawClass();
        JavaType refinedMapLikeType = mapLikeType.refine(rawType, null, null, null);
        assertEquals(mapLikeType, refinedMapLikeType);
    }

    @Test
    public void testBuildCanonicalName() {
        JavaType mapType = TypeModifier.constructMapType(Map.class, String.class, Integer.class);
        MapLikeType mapLikeType = (MapLikeType) mapType;
        StringBuilder expected = new StringBuilder("[map-like type; class java.util.Map, Ljava/lang/String;Ljava/lang/Integer;]");
        StringBuilder actual = new StringBuilder();
        mapLikeType.buildCanonicalName(actual);
        assertEquals(expected.toString(), actual.toString());
    }

    @Test
    public void testGetErasedSignature() {
        JavaType mapType = TypeModifier.constructMapType(Map.class, String.class, Integer.class);
        MapLikeType mapLikeType = (MapLikeType) mapType;
        StringBuilder expected = new StringBuilder("[map-like type; class java.util.Map]");
        StringBuilder actual = new StringBuilder();
        mapLikeType.getErasedSignature(actual);
        assertEquals(expected.toString(), actual.toString());
    }

    @Test
    public void testGetGenericSignature() {
        JavaType mapType = TypeModifier.constructMapType(Map.class, String.class, Integer.class);
        MapLikeType mapLikeType = (MapLikeType) mapType;
        StringBuilder expected = new StringBuilder("[map-like type; class java.util.Map<Ljava/lang/String;Ljava/lang/Integer;>]");
        StringBuilder actual = new StringBuilder();
        mapLikeType.getGenericSignature(actual);
        assertEquals(expected.toString(), actual.toString());
    }
}