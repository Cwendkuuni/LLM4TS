package com.fasterxml.jackson.databind.deser.std;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class StdKeyDeserializerTest {
    @Test
    public void testDeserializeKey() throws Exception {
        StdKeyDeserializer deserializer = new StdKeyDeserializer(StdKeyDeserializer.TYPE_BOOLEAN, Boolean.class);
        assertEquals(true, deserializer.deserializeKey("true", null));
        assertEquals(false, deserializer.deserializeKey("false", null));
    }

    @Test
    public void testGetKeyClass() {
        StdKeyDeserializer deserializer = new StdKeyDeserializer(StdKeyDeserializer.TYPE_INT, Integer.class);
        assertEquals(Integer.class, deserializer.getKeyClass());
    }

    @Test
    public void testParseInt() throws Exception {
        StdKeyDeserializer deserializer = new StdKeyDeserializer(StdKeyDeserializer.TYPE_INT, Integer.class);
        assertEquals(123, deserializer._parseInt("123"));
    }

    @Test
    public void testParseLong() throws Exception {
        StdKeyDeserializer deserializer = new StdKeyDeserializer(StdKeyDeserializer.TYPE_LONG, Long.class);
        assertEquals(123456789000L, deserializer._parseLong("123456789000"));
    }

    @Test
    public void testParseDouble() throws Exception {
        StdKeyDeserializer deserializer = new StdKeyDeserializer(StdKeyDeserializer.TYPE_DOUBLE, Double.class);
        assertEquals(1.23, deserializer._parseDouble("1.23"), 0.001);
    }

    @Test
    public void testDeserializeUnknownEnumValue() throws Exception {
        StdKeyDeserializer deserializer = StdKeyDeserializer.forType(MyEnum.class);
        assertNull(deserializer.deserializeKey("UNKNOWN", null));
    }

    enum MyEnum {
        FOO, BAR
    }
}