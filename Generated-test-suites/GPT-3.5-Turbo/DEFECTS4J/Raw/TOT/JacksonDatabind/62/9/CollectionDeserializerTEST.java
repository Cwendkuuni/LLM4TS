package com.fasterxml.jackson.databind.deser.std;

import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

public class CollectionDeserializerTest {

    @Test
    public void testDeserialize() throws IOException {
        CollectionDeserializer deserializer = new CollectionDeserializer(null, null, null, null);

        ArrayList<Object> expected = new ArrayList<>(Arrays.asList("a", "b", "c", "d"));
        Collection<Object> actual = deserializer.deserialize(createJsonParser(expected), null);

        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testDeserializeWithNullValue() throws IOException {
        CollectionDeserializer deserializer = new CollectionDeserializer(null, null, null, null);

        ArrayList<Object> expected = new ArrayList<>(Arrays.asList(null, "a", null, "b"));
        Collection<Object> actual = deserializer.deserialize(createJsonParser(expected), null);

        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testDeserializeWithType() throws IOException {
        CollectionDeserializer deserializer = new CollectionDeserializer(null, null, null, null);

        ArrayList<Object> expected = new ArrayList<>(Arrays.asList("a", "b", "c"));
        Collection<Object> actual = deserializer.deserializeWithType(createJsonParser(expected), null, null);

        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testDeserializeWithTypeAndReference() throws IOException {
        CollectionDeserializer deserializer = new CollectionDeserializer(null, null, null, null);

        ArrayList<Object> expected = new ArrayList<>(Arrays.asList("a", "b", "c"));
        Collection<Object> actual = deserializer.deserializeWithType(createJsonParser(expected), null, null);

        Assert.assertEquals(expected, actual);
    }

    private JsonParser createJsonParser(Collection<Object> values) {
        // Create a mock JsonParser that returns the values as tokens
        List<JsonToken> tokens = new ArrayList<>();
        for (Object value : values) {
            tokens.add(JsonToken.VALUE_STRING);
        }
        MockJsonParser parser = new MockJsonParser(tokens, values);

        // Set the current token to the first token
        parser.setCurrentToken(tokens.get(0));

        return parser;
    }

    private static class MockJsonParser extends JsonParser {

        private List<JsonToken> tokens;
        private Collection<Object> values;
        private int currentTokenIndex = 0;
        private boolean closed = false;

        public MockJsonParser(List<JsonToken> tokens, Collection<Object> values) {
            this.tokens = tokens;
            this.values = values;
        }

        @Override
        public JsonToken nextToken() throws IOException {
            if (currentTokenIndex == tokens.size()) {
                return null;
            }
            return tokens.get(currentTokenIndex++);
        }

        @Override
        public String getText() throws IOException {
            if (currentTokenIndex > 0 && currentTokenIndex <= values.size()) {
                return values.toArray()[currentTokenIndex - 1].toString();
            }
            return null;
        }

        @Override
        public void close() throws IOException {
            closed = true;
        }

        @Override
        public boolean isClosed() {
            return closed;
        }
    }
}