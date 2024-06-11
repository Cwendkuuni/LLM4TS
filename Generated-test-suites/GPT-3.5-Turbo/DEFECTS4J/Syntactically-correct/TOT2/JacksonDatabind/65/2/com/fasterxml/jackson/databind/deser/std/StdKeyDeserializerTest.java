package com.fasterxml.jackson.databind.deser.std;

import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.std.StdKeyDeserializer;
import org.junit.Test;
import static org.junit.Assert.*;

public class StdKeyDeserializerTest {

    @Test
    public void testDeserializeKey_NullKey() throws Exception {
        StdKeyDeserializer stdKeyDeserializer = new StdKeyDeserializer(StdKeyDeserializer.TYPE_STRING, String.class);
        DeserializationContext ctxt = new DeserializationContext(null, null, null);
        assertNull(stdKeyDeserializer.deserializeKey(null, ctxt));
    }

    @Test
    public void testDeserializeKey_InvalidKey() throws Exception {
        StdKeyDeserializer stdKeyDeserializer = new StdKeyDeserializer(StdKeyDeserializer.TYPE_INT, Integer.class);
        DeserializationContext ctxt = new DeserializationContext(null, null, null);
        assertNull(stdKeyDeserializer.deserializeKey("invalid", ctxt));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseInt_InvalidInput() throws Exception {
        StdKeyDeserializer stdKeyDeserializer = new StdKeyDeserializer(StdKeyDeserializer.TYPE_INT, Integer.class);
        stdKeyDeserializer._parseInt("invalid");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseLong_InvalidInput() throws Exception {
        StdKeyDeserializer stdKeyDeserializer = new StdKeyDeserializer(StdKeyDeserializer.TYPE_LONG, Long.class);
        stdKeyDeserializer._parseLong("invalid");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseDouble_InvalidInput() throws Exception {
        StdKeyDeserializer stdKeyDeserializer = new StdKeyDeserializer(StdKeyDeserializer.TYPE_DOUBLE, Double.class);
        stdKeyDeserializer._parseDouble("invalid");
    }

    @Test
    public void testStringKD_DeserializeKey() throws Exception {
        StdKeyDeserializer.StringKD stringKD = new StdKeyDeserializer.StringKD(String.class);
        DeserializationContext ctxt = new DeserializationContext(null, null, null);
        assertEquals("test", stringKD.deserializeKey("test", ctxt));
    }

    @Test
    public void testEnumKD_Parse() throws Exception {
        EnumResolver enumResolver = EnumResolver.constructUnsafeUsingToString(TestEnum.class, null);
        StdKeyDeserializer.EnumKD enumKD = new StdKeyDeserializer.EnumKD(enumResolver, null);
        DeserializationContext ctxt = new DeserializationContext(null, null, null);
        assertEquals(TestEnum.ONE, enumKD._parse("ONE", ctxt));
    }

    enum TestEnum {
        ONE, TWO, THREE
    }
}