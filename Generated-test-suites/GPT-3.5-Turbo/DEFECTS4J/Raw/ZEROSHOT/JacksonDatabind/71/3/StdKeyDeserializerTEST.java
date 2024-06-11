package com.fasterxml.jackson.databind.deser.std;

import org.junit.Test;
import static org.junit.Assert.*;

public class StdKeyDeserializerTest {

    @Test
    public void testDeserializeKey() throws Exception {
        StdKeyDeserializer deserializer = new StdKeyDeserializer(StdKeyDeserializer.TYPE_BOOLEAN, Boolean.class);
        assertEquals(Boolean.TRUE, deserializer.deserializeKey("true", new DeserializationContext()));
        assertEquals(Boolean.FALSE, deserializer.deserializeKey("false", new DeserializationContext()));
    }

    @Test
    public void testGetKeyClass() throws Exception {
        StdKeyDeserializer deserializer = new StdKeyDeserializer(StdKeyDeserializer.TYPE_INT, Integer.class);
        assertEquals(Integer.class, deserializer.getKeyClass());
    }

    @Test
    public void test_parse() throws Exception {
        StdKeyDeserializer deserializer = new StdKeyDeserializer(StdKeyDeserializer.TYPE_BYTE, Byte.class);
        assertEquals(Byte.valueOf((byte) 123), deserializer._parse("123", new DeserializationContext()));
    }

    @Test
    public void test_parseLong() throws Exception {
        StdKeyDeserializer deserializer = new StdKeyDeserializer(StdKeyDeserializer.TYPE_LONG, Long.class);
        assertEquals(Long.valueOf(1234567890), deserializer._parseLong("1234567890", new DeserializationContext()));
    }

    @Test
    public void test_parseDouble() throws Exception {
        StdKeyDeserializer deserializer = new StdKeyDeserializer(StdKeyDeserializer.TYPE_FLOAT, Float.class);
        assertEquals(Float.valueOf((float) 1.23), deserializer._parseDouble("1.23", new DeserializationContext()));
    }
}