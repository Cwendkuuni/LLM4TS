package com.example.junit; // replace with your test package if necessary

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.ObjectBuffer;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.json.JsonFactory;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;

public class StringArrayDeserializerTest {

    @Test
    public void testDeserialize() throws IOException {
        JsonFactory jsonFactory = new JsonFactory();
        JsonParser jp = jsonFactory.createParser("[\"apple\",\"banana\",\"orange\"]");
        StringArrayDeserializer deserializer = StringArrayDeserializer.instance;
        String[] result = deserializer.deserialize(jp, new DeserializationContext(jsonFactory, Collections.emptyMap(), new ObjectMapper()));
        Assert.assertArrayEquals(new String[]{"apple", "banana", "orange"}, result);
    }

    @Test
    public void testDeserializeWithCustomElementDeserializer() throws IOException {
        JsonFactory jsonFactory = new JsonFactory();
        JsonParser jp = jsonFactory.createParser("[\"1\", \"2\", \"3\"]");
        StringArrayDeserializer deserializer = new StringArrayDeserializer(new NumberDeserializer());
        String[] result = deserializer.deserialize(jp, new DeserializationContext(jsonFactory, Collections.emptyMap(), new ObjectMapper()));
        Assert.assertArrayEquals(new String[]{"1", "2", "3"}, result);
    }

    @Test
    public void testDeserializeNullValue() throws IOException {
        JsonFactory jsonFactory = new JsonFactory();
        JsonParser jp = jsonFactory.createParser("[]");
        StringArrayDeserializer deserializer = StringArrayDeserializer.instance;
        String[] result = deserializer.deserialize(jp, new DeserializationContext(jsonFactory, Collections.emptyMap(), new ObjectMapper()));
        Assert.assertArrayEquals(new String[0], result);

        jp.nextToken(); // consume START_ARRAY token
        result = deserializer.deserialize(jp, new DeserializationContext(jsonFactory, Collections.emptyMap(), new ObjectMapper()));
        Assert.assertArrayEquals(new String[]{null}, result);
    }

    @Test
    public void testDeserializeEmptyString() throws IOException {
        JsonFactory jsonFactory = new JsonFactory();
        JsonParser jp = jsonFactory.createParser("[]");
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        StringArrayDeserializer deserializer = StringArrayDeserializer.instance;
        String[] result;
        if (objectMapper.isEnabled(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT)) {
            result = deserializer.deserialize(jp, new DeserializationContext(jsonFactory, Collections.emptyMap(), objectMapper));
            Assert.assertArrayEquals(new String[]{null}, result);
        } else {
            result = deserializer.deserialize(jp, new DeserializationContext(jsonFactory, Collections.emptyMap(), objectMapper));
            Assert.assertArrayEquals(new String[0], result);
        }
    }

    @Test
    public void testDeserializeMissingStartArrayToken() throws IOException {
        JsonFactory jsonFactory = new JsonFactory();
        JsonParser jp = jsonFactory.createParser("{\"key\": \"value\"}");
        StringArrayDeserializer deserializer = StringArrayDeserializer.instance;
        String[] result = deserializer.deserialize(jp, new DeserializationContext(jsonFactory, Collections.emptyMap(), new ObjectMapper()));
        Assert.assertArrayEquals(new String[0], result);
    }

    @Test(expected = IOException.class)
    public void testDeserializeInvalidJson() throws IOException {
        JsonFactory jsonFactory = new JsonFactory();
        JsonParser jp = jsonFactory.createParser("\"invalidJson\"");
        StringArrayDeserializer deserializer = StringArrayDeserializer.instance;
        deserializer.deserialize(jp, new DeserializationContext(jsonFactory, Collections.emptyMap(), new ObjectMapper()));
    }

    @Test
    public void testCreateContextual() {
        // This test is not possible as StringArrayDeserializer does not provide a public constructor
        // that takes a JsonDeserializer<String> parameter. This test case should be removed.
    }

    private static class NumberDeserializer extends StdDeserializer<Number> {
        public NumberDeserializer() {
            this(null);
        }

        @SuppressWarnings("unchecked")
        public NumberDeserializer(JsonDeserializer<String> deser) {
            super(Number.class);
            this._elementDeserializer = (JsonDeserializer<String>) deser;
        }

        @Override
        public Number deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
            return NumberUtils.parseNumber(p.getText(), null);
        }
    }
}