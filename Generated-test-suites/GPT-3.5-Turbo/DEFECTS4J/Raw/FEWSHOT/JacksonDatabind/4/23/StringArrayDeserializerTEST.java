package com.fasterxml.jackson.databind.deser.std;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class StringArrayDeserializerTest {
    @Test
    public void testDeserialize() {
        StringArrayDeserializer deserializer = new StringArrayDeserializer();
        DeserializationContext ctxt = new DeserializationContext();

        JsonParser jp = new JsonParser();
        jp.setToken(JsonToken.START_ARRAY);

        JsonDeserializer<String> valueDeserializer = new StringDeserializer();
        deserializer._elementDeserializer = valueDeserializer;
        assertEquals(new String[]{"foo", "bar"}, deserializer.deserialize(jp, ctxt));
        
        jp.setToken(JsonToken.VALUE_STRING);
        jp.setText("foo");
        deserializer._elementDeserializer = null;
        assertEquals(new String[]{"foo"}, deserializer.deserialize(jp, ctxt));

        jp.setToken(JsonToken.VALUE_NULL);
        deserializer._elementDeserializer = null;
        assertNull(deserializer.deserialize(jp, ctxt));
    }

    @Test
    public void testDeserializeWithType() {
        StringArrayDeserializer deserializer = new StringArrayDeserializer();
        DeserializationContext ctxt = new DeserializationContext();

        JsonParser jp = new JsonParser();
        TypeDeserializer typeDeserializer = new TypeDeserializer();

        assertNull(deserializer.deserializeWithType(jp, ctxt, typeDeserializer));
    }

    @Test
    public void testDeserializeCustom() {
        StringArrayDeserializer deserializer = new StringArrayDeserializer(new StringDeserializer());
        DeserializationContext ctxt = new DeserializationContext();

        JsonParser jp = new JsonParser();
        jp.setToken(JsonToken.START_ARRAY);

        assertEquals(new String[]{"foo", "bar"}, deserializer._deserializeCustom(jp, ctxt));

        jp.setToken(JsonToken.VALUE_NULL);
        assertEquals(new String[]{null}, deserializer._deserializeCustom(jp, ctxt));
    }

    @Test
    public void testHandleNonArray() {
        StringArrayDeserializer deserializer = new StringArrayDeserializer();
        DeserializationContext ctxt = new DeserializationContext();

        JsonParser jp = new JsonParser();
        jp.setToken(JsonToken.VALUE_STRING);

        ctxt.enable(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY);
        assertEquals(new String[]{"foo"}, deserializer.handleNonArray(jp, ctxt));

        jp.setToken(JsonToken.VALUE_NULL);
        assertNull(deserializer.handleNonArray(jp, ctxt));

        ctxt.disable(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY);
        try {
            deserializer.handleNonArray(jp, ctxt);
        } catch (RuntimeException e) {
            assertEquals("Mapping Exception", e.getMessage());
        }
    }

    @Test
    public void testCreateContextual() {
        StringArrayDeserializer deserializer = new StringArrayDeserializer();
        DeserializationContext ctxt = new DeserializationContext();

        BeanProperty property = new BeanProperty();
        JsonDeserializer<?> valueDeserializer = new ValueDeserializer();
        deserializer._elementDeserializer = null;
        deserializer.createContextual(ctxt, property);
        assertNull(deserializer._elementDeserializer);

        deserializer._elementDeserializer = valueDeserializer;
        deserializer.createContextual(ctxt, property);
        assertEquals(valueDeserializer, deserializer._elementDeserializer);
    }
}