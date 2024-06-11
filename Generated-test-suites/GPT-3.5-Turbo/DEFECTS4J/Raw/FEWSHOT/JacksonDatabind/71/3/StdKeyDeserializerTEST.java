package com.fasterxml.jackson.databind.deser.std;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

import com.fasterxml.jackson.databind.DeserializationContext;

public class StdKeyDeserializerTest {

    @Test
    public void testDeserializeKey() throws Exception {
        StdKeyDeserializer deserializer = new StdKeyDeserializer(StdKeyDeserializer.TYPE_BOOLEAN, Boolean.class);
        DeserializationContext ctxt = new DeserializationContext(null, null, null);
        assertEquals(Boolean.TRUE, deserializer.deserializeKey("true", ctxt));
        assertEquals(Boolean.FALSE, deserializer.deserializeKey("false", ctxt));
        assertNull(deserializer.deserializeKey("other", ctxt));
    }

    @Test
    public void testGetKeyClass() {
        StdKeyDeserializer deserializer = new StdKeyDeserializer(StdKeyDeserializer.TYPE_BOOLEAN, Boolean.class);
        assertEquals(Boolean.class, deserializer.getKeyClass());
    }

    @Test
    public void testParseInt() {
        StdKeyDeserializer deserializer = new StdKeyDeserializer(StdKeyDeserializer.TYPE_BYTE, Byte.class) {
            @Override
            protected int _parseInt(String key) {
                return Integer.parseInt(key);
            }
        };
        assertEquals(Byte.valueOf((byte) 1), deserializer._parse("1", null));
    }

    @Test
    public void testParseLong() {
        StdKeyDeserializer deserializer = new StdKeyDeserializer(StdKeyDeserializer.TYPE_LONG, Long.class) {
            @Override
            protected long _parseLong(String key) {
                return Long.parseLong(key);
            }
        };
        assertEquals(Long.valueOf(1L), deserializer._parse("1", null));
    }

    @Test
    public void testParseDouble() {
        StdKeyDeserializer deserializer = new StdKeyDeserializer(StdKeyDeserializer.TYPE_FLOAT, Float.class) {
            @Override
            protected double _parseDouble(String key) {
                return Double.parseDouble(key);
            }
        };
        assertEquals(Float.valueOf(1.2f), deserializer._parse("1.2", null));
    }
}