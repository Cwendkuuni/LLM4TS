package com.fasterxml.jackson.databind.deser.std;

import java.io.IOException;
import org.junit.Assert;
import org.junit.Test;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.deser.ContextualDeserializer;
import com.fasterxml.jackson.databind.deser.std.StringArrayDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.util.ObjectBuffer;

public class StringArrayDeserializerTest {

    @Test
    public void testDeserialize() throws IOException {
        StringArrayDeserializer deserializer = new StringArrayDeserializer();
        JsonParser jp = null; // provide your own JsonParser object
        DeserializationContext ctxt = null; // provide your own DeserializationContext object
        String[] result = deserializer.deserialize(jp, ctxt);

        Assert.assertNotNull(result);
        // Add more assertions here
    }

    @Test
    public void test_handleNonArray() throws IOException {
        StringArrayDeserializer deserializer = new StringArrayDeserializer();
        JsonParser jp = null; // provide your own JsonParser object
        DeserializationContext ctxt = null; // provide your own DeserializationContext object
        String[] result = deserializer.handleNonArray(jp, ctxt);

        Assert.assertNotNull(result);
        // Add more assertions here
    }

    @Test
    public void test_deserializeCustom() throws IOException {
        StringArrayDeserializer deserializer = new StringArrayDeserializer();
        JsonParser jp = null; // provide your own JsonParser object
        DeserializationContext ctxt = null; // provide your own DeserializationContext object
        String[] result = deserializer._deserializeCustom(jp, ctxt);

        Assert.assertNotNull(result);
        // Add more assertions here
    }

    @Test
    public void test_createContextual() throws IOException {
        StringArrayDeserializer deserializer = new StringArrayDeserializer();
        DeserializationContext ctxt = null; // provide your own DeserializationContext object
        BeanProperty property = null; // provide your own BeanProperty object
        JsonDeserializer<?> result = deserializer.createContextual(ctxt, property);

        Assert.assertNotNull(result);
        // Add more assertions here
    }

    @Test
    public void test_deserializeWithType() throws IOException {
        StringArrayDeserializer deserializer = new StringArrayDeserializer();
        JsonParser jp = null; // provide your own JsonParser object
        DeserializationContext ctxt = null; // provide your own DeserializationContext object
        TypeDeserializer typeDeserializer = null; // provide your own TypeDeserializer object
        Object result = deserializer.deserializeWithType(jp, ctxt, typeDeserializer);

        Assert.assertNotNull(result);
        // Add more assertions here
    }

}