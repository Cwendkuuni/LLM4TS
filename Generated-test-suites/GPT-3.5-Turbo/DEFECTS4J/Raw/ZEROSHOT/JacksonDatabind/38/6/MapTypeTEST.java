package com.fasterxml.jackson.databind.type;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class MapTypeTest {

    private MapType mapType;

    @Before
    public void setup() {
        mapType = MapType.construct(String.class, Integer.class);
    }

    @Test
    public void testConstruct() {
        MapType type = new MapType(String.class, null, null, null, null, null, null, null, false);
        assertNotNull(type);
    }

    @Test
    public void testConstructWithBindings() {
        MapType type = new MapType(String.class, new TypeBindings(), null, null, null, null, null, null, false);
        assertNotNull(type);
    }

    @Test
    public void testWithStaticTyping() {
        MapType type = mapType.withStaticTyping();
        assertTrue(type.isStaticTyping());
    }

    @Test
    public void testWithContentType() {
        JavaType contentType = new JavaType();
        MapType type = mapType.withContentType(contentType);
        assertEquals(contentType, type.getContentType());
    }

    @Test
    public void testWithKeyType() {
        JavaType keyType = new JavaType();
        MapType type = mapType.withKeyType(keyType);
        assertEquals(keyType, type.getKeyType());
    }

    @Test
    public void testRefine() {
        MapType type = mapType.refine(String.class, null, null, null);
        assertEquals(String.class, type.getRawClass());
    }

    @Test
    public void testWithKeyTypeHandler() {
        Object typeHandler = new Object();
        MapType type = mapType.withKeyTypeHandler(typeHandler);
        assertEquals(typeHandler, type.getKeyTypeHandler());
    }

    @Test
    public void testWithKeyValueHandler() {
        Object valueHandler = new Object();
        MapType type = mapType.withKeyValueHandler(valueHandler);
        assertEquals(valueHandler, type.getKeyValueHandler());
    }

    @Test
    public void testToString() {
        String expected = "[map type; class java.lang.String, null -> null]";
        assertEquals(expected, mapType.toString());
    }
}