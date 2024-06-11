package com.fasterxml.jackson.databind.deser.std;

import static org.junit.Assert.*;
import org.junit.Test;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonProcessingException;

public class StdKeyDeserializerTest {

    @Test
    public void testDeserializeKey() throws Exception {
        StdKeyDeserializer deserializer = new StdKeyDeserializer(0, Object.class) {
            @Override
            public Object deserializeKey(String key, DeserializationContext ctxt)
                    throws JsonProcessingException {
                return null;
            }
        };
        assertNull(deserializer.deserializeKey(null, null));
    }

    @Test
    public void testGetKeyClass() throws Exception {
        StdKeyDeserializer deserializer = new StdKeyDeserializer(0, Object.class) {
            @Override
            public Object deserializeKey(String key, DeserializationContext ctxt)
                    throws JsonProcessingException {
                return null;
            }
        };
        assertEquals(Object.class, deserializer.getKeyClass());
    }

    @Test
    public void testParse() throws Exception {
        StdKeyDeserializer deserializer = new StdKeyDeserializer(0, Object.class) {
            @Override
            public Object _parse(String key, DeserializationContext ctxt) throws Exception {
                return null;
            }
        };
        assertNull(deserializer._parse(null, null));
    }

    @Test
    public void testParseInt() throws Exception {
        StdKeyDeserializer deserializer = new StdKeyDeserializer(0, Object.class) {
            @Override
            protected int _parseInt(String key) throws IllegalArgumentException {
                return 0;
            }
        };
        assertEquals(0, deserializer._parseInt(null));
    }

    @Test
    public void testParseLong() throws Exception {
        StdKeyDeserializer deserializer = new StdKeyDeserializer(0, Object.class) {
            @Override
            protected long _parseLong(String key) throws IllegalArgumentException {
                return 0L;
            }
        };
        assertEquals(0L, deserializer._parseLong(null));
    }

    @Test
    public void testParseDouble() throws Exception {
        StdKeyDeserializer deserializer = new StdKeyDeserializer(0, Object.class) {
            @Override
            protected double _parseDouble(String key) throws IllegalArgumentException {
                return 0.0;
            }
        };
        assertEquals(0.0, deserializer._parseDouble(null), 0.0);
    }

}