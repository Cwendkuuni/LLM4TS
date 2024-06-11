package com.fasterxml.jackson.databind.deser.std;

import org.junit.Test;
import static org.junit.Assert.*;

public class StdKeyDeserializerTest {

    @Test
    public void testDeserializeKey() throws Exception {
        StdKeyDeserializer deserializer = new StdKeyDeserializer(StdKeyDeserializer.TYPE_INT, Integer.class);
        DeserializationContext ctxt = new DeserializationContext();
        assertEquals(deserializer.deserializeKey("123", ctxt), 123);
    }

    @Test
    public void testGetKeyClass() {
        StdKeyDeserializer deserializer = new StdKeyDeserializer(StdKeyDeserializer.TYPE_INT, Integer.class);
        assertEquals(deserializer.getKeyClass(), Integer.class);
    }

    @Test
    public void testParseInt() throws Exception {
        StdKeyDeserializer deserializer = new StdKeyDeserializer(StdKeyDeserializer.TYPE_INT, Integer.class);
        assertEquals(deserializer._parseInt("123"), 123);
    }

    @Test
    public void testParseLong() throws Exception {
        StdKeyDeserializer deserializer = new StdKeyDeserializer(StdKeyDeserializer.TYPE_LONG, Long.class);
        assertEquals(deserializer._parseLong("123"), 123L);
    }

    @Test
    public void testParseDouble() throws Exception {
        StdKeyDeserializer deserializer = new StdKeyDeserializer(StdKeyDeserializer.TYPE_DOUBLE, Double.class);
        assertEquals(deserializer._parseDouble("123.45"), 123.45, 0.0001);
    }

    @Test
    public void testStringKD() throws Exception {
        StdKeyDeserializer.StringKD stringKD = StdKeyDeserializer.StringKD.forType(String.class);
        DeserializationContext ctxt = new DeserializationContext();
        assertEquals(stringKD.deserializeKey("test", ctxt), "test");
    }

    @Test
    public void testDeserializeKeyDelegatingKD() throws Exception {
        DelegatingKD delegatingKD = new DelegatingKD(String.class, new JsonDeserializer<String>() {
            @Override
            public String deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JsonProcessingException {
                return p.getValueAsString();
            }
        });
        DeserializationContext ctxt = new DeserializationContext();
        assertEquals(delegatingKD.deserializeKey("test", ctxt), "test");
    }

    @Test
    public void testEnumKD() throws Exception {
        EnumKD enumKD = new EnumKD(new EnumResolver(Enum.class, null), null);
        DeserializationContext ctxt = new DeserializationContext();
        assertEquals(enumKD._parse("VALUE1", ctxt), Enum.VALUE1);
    }

    @Test
    public void testStringCtorKeyDeserializer() throws Exception {
        Constructor<?> ctor = String.class.getConstructor(String.class);
        StringCtorKeyDeserializer keyDeserializer = new StringCtorKeyDeserializer(ctor);
        assertEquals(keyDeserializer._parse("test", null), "test");
    }

    @Test
    public void testStringFactoryKeyDeserializer() throws Exception {
        Method factoryMethod = String.class.getMethod("valueOf", String.class);
        StringFactoryKeyDeserializer keyDeserializer = new StringFactoryKeyDeserializer(factoryMethod);
        assertEquals(keyDeserializer._parse("test", null), "test");
    }
}