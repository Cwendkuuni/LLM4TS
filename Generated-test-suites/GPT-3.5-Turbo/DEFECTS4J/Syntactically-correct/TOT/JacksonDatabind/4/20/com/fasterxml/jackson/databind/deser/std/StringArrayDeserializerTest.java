package com.fasterxml.jackson.databind.deser.std;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.deser.std.StringArrayDeserializer;
import com.fasterxml.jackson.databind.util.ObjectBuffer;
import org.junit.Assert;
import org.junit.Test;

public class StringArrayDeserializerTest {

    @Test
    public void testDeserialize_typicalUseCase() throws IOException {
        StringArrayDeserializer deserializer = StringArrayDeserializer.instance;
        JsonParser jsonParser = new DummyJsonParser(JsonToken.START_ARRAY, JsonToken.VALUE_STRING, "value1", JsonToken.VALUE_STRING, "value2", JsonToken.VALUE_STRING, "value3", JsonToken.END_ARRAY);
        DeserializationContext context = new DummyDeserializationContext();

        String[] result = deserializer.deserialize(jsonParser, context);

        Assert.assertEquals(3, result.length);
        Assert.assertEquals("value1", result[0]);
        Assert.assertEquals("value2", result[1]);
        Assert.assertEquals("value3", result[2]);
    }

    @Test
    public void testDeserialize_edgeCase_emptyArray() throws IOException {
        StringArrayDeserializer deserializer = StringArrayDeserializer.instance;
        JsonParser jsonParser = new DummyJsonParser(JsonToken.START_ARRAY, JsonToken.END_ARRAY);
        DeserializationContext context = new DummyDeserializationContext();

        String[] result = deserializer.deserialize(jsonParser, context);

        Assert.assertEquals(0, result.length);
    }

    @Test
    public void testDeserialize_edgeCase_singleValueArray() throws IOException {
        StringArrayDeserializer deserializer = StringArrayDeserializer.instance;
        JsonParser jsonParser = new DummyJsonParser(JsonToken.START_ARRAY, JsonToken.VALUE_STRING, "value", JsonToken.END_ARRAY);
        DeserializationContext context = new DummyDeserializationContext();

        String[] result = deserializer.deserialize(jsonParser, context);

        Assert.assertEquals(1, result.length);
        Assert.assertEquals("value", result[0]);
    }

    @Test
    public void testDeserialize_errorCase_invalidInput() throws IOException {
        StringArrayDeserializer deserializer = StringArrayDeserializer.instance;
        JsonParser jsonParser = new DummyJsonParser(JsonToken.VALUE_NULL);
        DeserializationContext context = new DummyDeserializationContext();

        try {
            deserializer.deserialize(jsonParser, context);
            Assert.fail("Expected IOException to be thrown");
        } catch (IOException e) {
            Assert.assertEquals("Invalid input for deserializing String array", e.getMessage());
        }
    }

    // Below are the dummy classes that can be used to create the DummyJsonParser and DummyDeserializationContext objects for testing

    private static class DummyJsonParser extends JsonParser {

        private JsonToken[] tokens;
        private String[] values;
        private int index;

        public DummyJsonParser(JsonToken... tokens) {
            this(tokens, null);
        }

        public DummyJsonParser(JsonToken[] tokens, String[] values) {
            this.tokens = tokens;
            this.values = values;
            this.index = 0;
        }

        @Override
        public JsonToken nextToken() throws IOException {
            if (index < tokens.length) {
                return tokens[index++];
            }
            return null;
        }

        @Override
        public String getText() throws IOException {
            return values[index / 2];
        }

        // Other overridden methods have been omitted for brevity
    }

    private static class DummyDeserializationContext extends DeserializationContext {

        @Override
        public ObjectBuffer leaseObjectBuffer() {
            return new ObjectBuffer();
        }

        @Override
        public void returnObjectBuffer(ObjectBuffer buf) {
            // Do nothing
        }

        // Other overridden methods have been omitted for brevity
    }
}