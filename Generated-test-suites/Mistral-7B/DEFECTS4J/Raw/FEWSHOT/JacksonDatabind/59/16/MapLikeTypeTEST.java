package com.fasterxml.jackson.databind.type;

import static org.junit.Assert.*;

import org.junit.Test;

public class MapLikeTypeTest {

    @Test
    public void testIsContainerType() {
        MapLikeType mapLikeType = new MapLikeType(Map.class, TypeBindings.emptyBindings(), _bogusSuperClass(Map.class), null, String.class, Integer.class);
        assertTrue(mapLikeType.isContainerType());
    }

    @Test
    public void testIsMapLikeType() {
        MapLikeType mapLikeType = new MapLikeType(Map.class, TypeBindings.emptyBindings(), _bogusSuperClass(Map.class), null, String.class, Integer.class);
        assertTrue(mapLikeType.isMapLikeType());
    }

    @Test
    public void testGetKeyType() {
        MapLikeType mapLikeType = new MapLikeType(Map.class, TypeBindings.emptyBindings(), _bogusSuperClass(Map.class), null, String.class, Integer.class);
        assertEquals(String.class, mapLikeType.getKeyType());
    }

    @Test
    public void testGetContentType() {
        MapLikeType mapLikeType = new MapLikeType(Map.class, TypeBindings.emptyBindings(), _bogusSuperClass(Map.class), null, String.class, Integer.class);
        assertEquals(Integer.class, mapLikeType.getContentType());
    }

    @Test
    public void testGetContentValueHandler() {
        MapLikeType mapLikeType = new MapLikeType(Map.class, TypeBindings.emptyBindings(), _bogusSuperClass(Map.class), null, String.class, Integer.class);
        assertNull(mapLikeType.getContentValueHandler());
    }

    @Test
    public void testGetContentTypeHandler() {
        MapLikeType mapLikeType = new MapLikeType(Map.class, TypeBindings.emptyBindings(), _bogusSuperClass(Map.class), null, String.class, Integer.class);
        assertNull(mapLikeType.getContentTypeHandler());
    }

    @Test
    public void testHasHandlers() {
        MapLikeType mapLikeType = new MapLikeType(Map.class, TypeBindings.emptyBindings(), _bogusSuperClass(Map.class), null, String.class, Integer.class);
        assertFalse(mapLikeType.hasHandlers());
    }

    @Test
    public void testBuildCanonicalName() {
        MapLikeType mapLikeType = new MapLikeType(HashMap.class, TypeBindings.emptyBindings(), _bogusSuperClass(HashMap.class), null, String.class, Integer.class);
        assertEquals("[map-like type; class java.util.HashMap, Ljava/lang/String;=>Ljava/lang/Integer;]", mapLikeType.buildCanonicalName());
    }

    @Test
    public void testIsTrueMapType() {
        MapLikeType mapLikeType = new MapLikeType(Map.class, TypeBindings.emptyBindings(), _bogusSuperClass(Map.class), null, String.class, Integer.class);
        assertTrue(mapLikeType.isTrueMapType());
    }

    @Test
    public void testRefine() {
        TypeVariable<?>[] vars = HashMap.class.getTypeParameters();
        TypeBindings bindings = TypeBindings.create(HashMap.class, String.class, Integer.class);
        MapLikeType mapLikeType = new MapLikeType(HashMap.class, bindings, _bogusSuperClass(HashMap.class), null, String.class, Integer.class);
        MapLikeType refinedType = mapLikeType.refine(HashMap.class, bindings, _bogusSuperClass(HashMap.class), null);
        assertEquals(mapLikeType, refinedType);
    }

    @Test
    public void testUpgradeFrom() {
        JavaType javaType = Reflector.findConcreteType(List.class, ArrayList.class);
        MapLikeType mapLikeType = MapLikeType.upgradeFrom(javaType, String.class, Integer.class);
        assertTrue(mapLikeType instanceof MapLikeType);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructWithInvalidType() {
        MapLikeType mapLikeType = MapLikeType.construct(String[].class, String.class, Integer.class);
    }

    @Test
    public void testWithKeyType() {
        MapLikeType mapLikeType = new MapLikeType(Map.class, TypeBindings.emptyBindings(), _bogusSuperClass(Map.class), null, String.class, Integer.class);
        MapLikeType withKeyType = mapLikeType.withKeyType(Long.class);
        assert NotEquals(mapLikeType, withKeyType);
        assertEquals(Long.class, withKeyType.getKeyType());
    }

    @Test
    public void testWithContentType() {
        MapLikeType mapLikeType = new MapLikeType(Map.class, TypeBindings.emptyBindings(), _bogusSuperClass(Map.class), null, String.class, Integer.class);
        MapLikeType withContentType = mapLikeType.withContentType(Long.class);
        assert NotEquals(mapLikeType, withContentType);
        assertEquals(Long.class, withContentType.getContentType());
    }

    @Test
    public void testWithTypeHandler() {
        MapLikeType mapLikeType = new MapLikeType(Map.class, TypeBindings.emptyBindings(), _bogusSuperClass(Map.class), null, String.class, Integer.class);
        MapLikeType withTypeHandler = mapLikeType.withTypeHandler(new Object());
        assert NotEquals(mapLikeType, withTypeHandler);
    }

    @Test
    public void testWithContentTypeHandler() {
        MapLikeType mapLikeType = new MapLikeType(Map.class, TypeBindings.emptyBindings(), _bogusSuperClass(Map.class), null, String.class, Integer.class);
        MapLikeType withContentTypeHandler = mapLikeType.withContentTypeHandler(new Object());
        assert NotEquals(mapLikeType, withContentTypeHandler);
    }

    @Test
    public void testWithValueHandler() {
        MapLikeType mapLikeType = new MapLikeType(Map.class, TypeBindings.emptyBindings(), _bogusSuperClass(Map.class), null, String.class, Integer.class);
        MapLikeType withValueHandler = mapLikeType.withValueHandler(new Object());
        assert NotEquals(mapLikeType, withValueHandler);
    }

    @Test
    public void testWithContentValueHandler() {
        MapLikeType mapLikeType = new MapLikeType(Map.class, TypeBindings.emptyBindings(), _bogusSuperClass(Map.class), null, String.class, Integer.class);
        MapLikeType withContentValueHandler = mapLikeType.withContentValueHandler(new Object());
        assert NotEquals(mapLikeType, withContentValueHandler);
    }

    @Test
    public void testWithStaticTyping() {
        MapLikeType mapLikeType = new MapLikeType(Map.class, TypeBindings.emptyBindings(), _bogusSuperClass(Map.class), null, String.class, Integer.class);
        MapLikeType withStaticTyping = mapLikeType.withStaticTyping();
        assert NotEquals(mapLikeType, withStaticTyping);
        assertTrue(withStaticTyping.isStaticTyping());
    }
}