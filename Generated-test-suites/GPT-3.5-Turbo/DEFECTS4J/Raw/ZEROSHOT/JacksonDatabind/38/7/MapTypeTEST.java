package com.fasterxml.jackson.databind.type;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

public class MapTypeTest {

    private MapType mapType;

    @Before
    public void setUp() {
        mapType = new MapType(null, null, null,
                null, null, null, null, null, false);
    }

    @Test
    public void testConstruct() {
        MapType constructedMapType = MapType
                .construct(Map.class, null, null, null, null, null);

        assertNotEquals(constructedMapType, mapType);
    }

    @Test
    public void testConstructDeprecated() {
        MapType constructedMapType = MapType
                .construct(Map.class, null, null);

        assertNotEquals(constructedMapType, mapType);
    }

    @Test
    public void test_narrow() {
        JavaType narrowType = mapType._narrow(Map.class);

        assertNotEquals(narrowType, mapType);
    }

    @Test
    public void testWithTypeHandler() {
        MapType withTypeHandlerType = mapType.withTypeHandler(null);

        assertNotEquals(withTypeHandlerType, mapType);
    }

    @Test
    public void testWithContentTypeHandler() {
        MapType withContentTypeHandlerType = mapType.withContentTypeHandler(null);

        assertNotEquals(withContentTypeHandlerType, mapType);
    }

    @Test
    public void testWithValueHandler() {
        MapType withValueHandlerType = mapType.withValueHandler(null);

        assertNotEquals(withValueHandlerType, mapType);
    }

    @Test
    public void testWithContentValueHandler() {
        MapType withContentValueHandlerType = mapType.withContentValueHandler(null);

        assertNotEquals(withContentValueHandlerType, mapType);
    }

    @Test
    public void testWithStaticTyping() {
        MapType withStaticTypingType = mapType.withStaticTyping();

        assertNotEquals(withStaticTypingType, mapType);
    }

    @Test
    public void testWithContentType() {
        MapType withContentTypeType = mapType.withContentType(null);

        assertNotEquals(withContentTypeType, mapType);
    }

    @Test
    public void testWithKeyType() {
        MapType withKeyType = mapType.withKeyType(null);

        assertNotEquals(withKeyType, mapType);
    }

    @Test
    public void testRefine() {
        JavaType refinedType = mapType.refine(Map.class, null, null, null);

        assertNotEquals(refinedType, mapType);
    }

    @Test
    public void testWithKeyTypeHandler() {
        MapType withKeyTypeHandlerType = mapType.withKeyTypeHandler(null);

        assertNotEquals(withKeyTypeHandlerType, mapType);
    }

    @Test
    public void testWithKeyValueHandler() {
        MapType withKeyValueHandlerType = mapType.withKeyValueHandler(null);

        assertNotEquals(withKeyValueHandlerType, mapType);
    }

    @Test
    public void testToString() {
        String expected = "[map type; class java.util.HashMap, null -> null]";
        String actual = mapType.toString();

        assertEquals(expected, actual);
    }
}