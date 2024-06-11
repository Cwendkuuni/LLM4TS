package com.fasterxml.jackson.databind.deser.std;

import org.junit.Test;

import static org.junit.Assert.*;

public class StdKeyDeserializerTest {

    @Test
    public void testDeserializeKey() {
        StdKeyDeserializer deserializer = StdKeyDeserializer.forType(String.class);
        assertEquals("test", deserializer.deserializeKey("test", null));
    }

    @Test
    public void testGetKeyClass() {
        StdKeyDeserializer deserializer = StdKeyDeserializer.forType(String.class);
        assertEquals(String.class, deserializer.getKeyClass());
    }

    @Test
    public void test_parse() throws Exception {
        StdKeyDeserializer deserializer = new StdKeyDeserializer(StdKeyDeserializer.TYPE_BOOLEAN, Boolean.class);
        assertEquals(Boolean.TRUE, deserializer._parse("true", null));

        deserializer = new StdKeyDeserializer(StdKeyDeserializer.TYPE_BYTE, Byte.class);
        assertEquals(Byte.valueOf((byte) 1), deserializer._parse("1", null));

        deserializer = new StdKeyDeserializer(StdKeyDeserializer.TYPE_SHORT, Short.class);
        assertEquals(Short.valueOf((short) 1), deserializer._parse("1", null));

        deserializer = new StdKeyDeserializer(StdKeyDeserializer.TYPE_CHAR, Character.class);
        assertEquals(Character.valueOf('c'), deserializer._parse("c", null));

        deserializer = new StdKeyDeserializer(StdKeyDeserializer.TYPE_INT, Integer.class);
        assertEquals(Integer.valueOf(1), deserializer._parse("1", null));

        deserializer = new StdKeyDeserializer(StdKeyDeserializer.TYPE_LONG, Long.class);
        assertEquals(Long.valueOf(1L), deserializer._parse("1", null));

        deserializer = new StdKeyDeserializer(StdKeyDeserializer.TYPE_FLOAT, Float.class);
        assertEquals(Float.valueOf(1.0f), deserializer._parse("1.0", null));

        deserializer = new StdKeyDeserializer(StdKeyDeserializer.TYPE_DOUBLE, Double.class);
        assertEquals(Double.valueOf(1.0), deserializer._parse("1.0", null));
    }

    @Test
    public void test_parse_exception() {
        StdKeyDeserializer deserializer = new StdKeyDeserializer(StdKeyDeserializer.TYPE_BOOLEAN, Boolean.class);
        try {
            deserializer._parse("test", null);
            fail("Expected exception not thrown");
        } catch (Exception e) {
            assertNotNull(e);
        }
    }

    @Test
    public void test_getToStringResolver() {
        StdKeyDeserializer.EnumKD enumKD = new StdKeyDeserializer.EnumKD(null, null);
        assertNull(enumKD._byToStringResolver);
        enumKD._getToStringResolver();
        assertNotNull(enumKD._byToStringResolver);
    }

    @Test
    public void testGetStringConstructorKeyDeserializer() throws Exception {
        StdKeyDeserializer.StringCtorKeyDeserializer deserializer = new StdKeyDeserializer.StringCtorKeyDeserializer(String.class.getConstructor(String.class));
        assertEquals("test", deserializer._parse("test", null));
    }

    @Test
    public void testGetStringFactoryKeyDeserializer() throws Exception {
        StdKeyDeserializer.StringFactoryKeyDeserializer deserializer = new StdKeyDeserializer.StringFactoryKeyDeserializer(String.class.getMethod("valueOf", String.class));
        assertEquals("test", deserializer._parse("test", null));
    }

}