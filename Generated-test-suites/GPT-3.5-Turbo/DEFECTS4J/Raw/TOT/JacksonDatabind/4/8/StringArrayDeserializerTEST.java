package com.fasterxml.jackson.databind.deser.std;

import org.junit.Test;
import static org.junit.Assert.*;

public class StringArrayDeserializerTest {

    @Test
    public void testDeserialize_standardValue() throws Exception {
        StringArrayDeserializer deserializer = StringArrayDeserializer.instance;

        JsonParser jp = new JsonParser();
        DeserializationContext ctxt = new DeserializationContext();
        jp.setExpectedToken(JsonToken.START_ARRAY);
        jp.setCurrentToken(JsonToken.START_ARRAY);

        jp.nextToken();
        jp.setCurrentToken(JsonToken.VALUE_STRING);
        jp.setText("value1");

        jp.nextToken();
        jp.setCurrentToken(JsonToken.VALUE_STRING);
        jp.setText("value2");

        jp.nextToken();
        jp.setCurrentToken(JsonToken.VALUE_STRING);
        jp.setText("value3");

        jp.nextToken();
        jp.setCurrentToken(JsonToken.END_ARRAY);

        String[] result = deserializer.deserialize(jp, ctxt);

        assertEquals(3, result.length);
        assertEquals("value1", result[0]);
        assertEquals("value2", result[1]);
        assertEquals("value3", result[2]);
    }

    @Test
    public void testDeserialize_nullValue() throws Exception {
        StringArrayDeserializer deserializer = StringArrayDeserializer.instance;

        JsonParser jp = new JsonParser();
        DeserializationContext ctxt = new DeserializationContext();
        jp.setExpectedToken(JsonToken.START_ARRAY);
        jp.setCurrentToken(JsonToken.START_ARRAY);

        jp.nextToken();
        jp.setCurrentToken(JsonToken.VALUE_NULL);

        jp.nextToken();
        jp.setCurrentToken(JsonToken.VALUE_STRING);
        jp.setText("value2");

        jp.nextToken();
        jp.setCurrentToken(JsonToken.VALUE_NULL);

        jp.nextToken();
        jp.setCurrentToken(JsonToken.END_ARRAY);

        String[] result = deserializer.deserialize(jp, ctxt);

        assertEquals(3, result.length);
        assertNull(result[0]);
        assertEquals("value2", result[1]);
        assertNull(result[2]);
    }

    @Test
    public void testDeserialize_emptyArray() throws Exception {
        StringArrayDeserializer deserializer = StringArrayDeserializer.instance;

        JsonParser jp = new JsonParser();
        DeserializationContext ctxt = new DeserializationContext();
        jp.setExpectedToken(JsonToken.VALUE_NULL);
        jp.setCurrentToken(JsonToken.VALUE_NULL);

        jp.nextToken();
        jp.setCurrentToken(JsonToken.END_ARRAY);

        String[] result = deserializer.deserialize(jp, ctxt);

        assertEquals(0, result.length);
    }

    @Test(expected = IOException.class)
    public void testDeserialize_invalidInput() throws Exception {
        StringArrayDeserializer deserializer = StringArrayDeserializer.instance;

        JsonParser jp = new JsonParser();
        DeserializationContext ctxt = new DeserializationContext();
        jp.setExpectedToken(JsonToken.START_ARRAY);
        jp.setCurrentToken(JsonToken.VALUE_STRING);

        deserializer.deserialize(jp, ctxt);
    }
}