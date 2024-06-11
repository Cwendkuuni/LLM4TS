package com.fasterxml.jackson.databind.type;

import org.junit.Test;
import static org.junit.Assert.*;

public class MapTypeTest {

    @Test
    public void testConstruct() {
        MapType mapType = MapType.construct(HashMap.class, KeyType.Integer, ValueType.String);
        assertNotNull(mapType);
    }

    @Test
    public void testWithTypeHandler() {
        Object handler = new TypeHandler();
        MapType mapType = new MapType(HashMap.class, null, null, null, KeyType.Integer, ValueType.String, null, null, false);
        MapType result = mapType.withTypeHandler(handler);
        assertNotNull(result);
    }

    @Test
    public void testWithStaticTyping() {
        MapType mapType = new MapType(HashMap.class, null, null, null, KeyType.Integer, ValueType.String, null, null, false);
        MapType result = mapType.withStaticTyping();
        assertTrue(result.isStatic());
    }

    @Test
    public void testWithKeyType() {
        MapType mapType = new MapType(HashMap.class, null, null, null, KeyType.Integer, ValueType.String, null, null, false);
        MapType result = mapType.withKeyType(KeyType.String);
        assertEquals(KeyType.String, result.getKeyType());
    }

    // Add more test cases for other methods as needed

}