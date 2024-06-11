package com.fasterxml.jackson.databind.type;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import org.junit.Test;

public class MapTypeTest {
    @Test
    public void testConstruct() {
        MapType mapType = MapType.construct(String.class, null, null, null, Integer.class, Double.class);
        assertEquals(String.class, mapType.getRawType());
        assertNull(mapType.getKeyType());
        assertNull(mapType.getValueType());
    }

    @Test
    public void testWithKeyType() {
        MapType mapType = new MapType(String.class, null, null, null, Integer.class, Double.class, null, null, false);
        MapType newMapType = mapType.withKeyType(Double.class);
        assertEquals(String.class, newMapType.getRawType());
        assertEquals(Double.class, newMapType.getKeyType());
        assertEquals(Double.class, newMapType.getValueType());
    }

    @Test
    public void testWithKeyValueHandler() {
        MapType mapType = new MapType(String.class, null, null, null, Integer.class, Double.class, null, null, false);
        MapType newMapType = mapType.withKeyValueHandler("handler");
        assertEquals(String.class, newMapType.getRawType());
        assertEquals("handler", newMapType.getKeyType().getValueHandler());
        assertEquals("handler", newMapType.getValueType().getValueHandler());
    }

    @Test
    public void testWithStaticTyping() {
        MapType mapType = new MapType(String.class, null, null, null, Integer.class, Double.class, null, null, false);
        MapType newMapType = mapType.withStaticTyping();
        assertEquals(String.class, newMapType.getRawType());
        assertEquals(Integer.class, newMapType.getKeyType().withStaticTyping());
        assertEquals(Double.class, newMapType.getValueType().withStaticTyping());
    }

    @Test
    public void testRefine() {
        MapType mapType = new MapType(String.class, null, null, null, Integer.class, Double.class,
                                      null, null, false);
        MapType newMapType = mapType.refine(Double.class, null, null, null);
        assertEquals(Double.class, newMapType.getRawType());
        assertEquals(Integer.class, newMapType.getKeyType());
        assertEquals(Double.class, newMapType.getValueType());
    }

    @Test
    public void testWithKeyTypeHandler() {
        MapType mapType = new MapType(String.class, null, null, null, Integer.class, Double.class,
                                      null, null, false);
        MapType newMapType = mapType.withKeyTypeHandler("handler");
        assertEquals(String.class, newMapType.getRawType());
        assertEquals("handler", newMapType.getKeyType().getTypeHandler());
        assertEquals(Double.class, newMapType.getValueType());
    }

    @Test
    public void testWithContentType() {
        MapType mapType = new MapType(String.class, null, null, null, Integer.class, Double.class,
                                      null, null, false);
        MapType newMapType = mapType.withContentType(Integer.class);
        assertEquals(String.class, newMapType.getRawType());
        assertEquals(Integer.class, newMapType.getValueType());
        assertEquals(Integer.class, newMapType.getKeyType());
    }

    @Test
    public void testToString() {
        MapType mapType = new MapType(String.class, null, null, null, Integer.class, Double.class,
                                      null, null, false);
        assertEquals("[map type; class java.lang.String, Integer -> Double]", mapType.toString());
    }
}