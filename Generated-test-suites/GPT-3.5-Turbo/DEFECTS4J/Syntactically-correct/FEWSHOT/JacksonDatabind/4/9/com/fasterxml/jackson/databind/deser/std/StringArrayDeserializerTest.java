package com.fasterxml.jackson.databind.deser.std;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.deser.ContextualDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.util.ObjectBuffer;
import org.junit.Test;

import static org.junit.Assert.*;

public class StringArrayDeserializerTest {

    @Test
    public void testDeserialize() throws Exception {
        StringArrayDeserializer deserializer = new StringArrayDeserializer();

        assertNull(deserializer.deserialize(null, null));

        JsonParser jp = null;
        DeserializationContext ctxt = null;
        String[] result = deserializer.deserialize(jp, ctxt);

        assertNull(result);
    }

    @Test
    public void test_deserialize_custom() throws Exception {
        StringArrayDeserializer deserializer = new StringArrayDeserializer();
        ObjectBuffer buffer = null;
        ContextualDeserializer ctxt = null;
        JsonParser jp = null;

        assertNull(deserializer._deserializeCustom(null, null));

        deserializer._elementDeserializer = new JsonDeserializer<String>() {
            @Override
            public String deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
                return null;
            }
        };
        assertNull(deserializer.deserialize(jp, ctxt));

        deserializer._elementDeserializer = null;
        JsonDeserializer<?> deser = null;

        String[] result = deserializer._deserializeCustom(jp, ctxt);

        assertNull(result);
    }

    @Test
    public void testDeserializeWithType() throws Exception {
        StringArrayDeserializer deserializer = new StringArrayDeserializer();
        JsonParser jp = null;
        DeserializationContext ctxt = null;
        TypeDeserializer typeDeserializer = null;

        assertNull(deserializer.deserializeWithType(jp, ctxt, typeDeserializer));
    }

    @Test
    public void test_handleNonArray() throws Exception {
        StringArrayDeserializer deserializer = new StringArrayDeserializer();
        JsonParser jp = null;
        DeserializationContext ctxt = null;

        assertNull(deserializer.handleNonArray(jp, ctxt));
    }

    @Test
    public void testCreateContextual() throws Exception {
        StringArrayDeserializer deserializer = new StringArrayDeserializer();
        DeserializationContext ctxt = null;
        BeanProperty property = null;

        assertNull(deserializer.createContextual(ctxt, property));
    }
}