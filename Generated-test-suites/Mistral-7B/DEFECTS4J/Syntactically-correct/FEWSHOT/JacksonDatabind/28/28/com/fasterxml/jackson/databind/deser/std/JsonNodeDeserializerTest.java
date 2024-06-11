package com.fasterxml.jackson.databind.deser.std;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.node.*;
import org.junit.Test;

public class JsonNodeDeserializerTest {

    @Test
    public void testObjectDeserialization() throws IOException {
        String json = "{\"foo\": 1, \"bar\": [1, 2, 3]}";
        JsonParser parser = new ObjectMapper().getFactory().createParser(json);
        JsonNode expected = new ObjectMapper().readTree(json);

        JsonDeserializer<JsonNode> deserializer = JsonNodeDeserializer.getDeserializer(ObjectNode.class);
        JsonNode actual = deserializer.deserialize(parser, new DeserializationContext(), new JsonNodeFactory());

        assertEqualsJsonNodes(expected, actual);
    }

    @Test
    public void testArrayDeserialization() throws IOException {
        String json = "[1, 2, 3]";
        JsonParser parser = new ObjectMapper().getFactory().createParser(json);
        JsonNode expected = new ObjectMapper().readTree(json);

        JsonDeserializer<JsonNode> deserializer = JsonNodeDeserializer.getDeserializer(ArrayNode.class);
        JsonNode actual = deserializer.deserialize(parser, new DeserializationContext(), new JsonNodeFactory());

        assertEqualsJsonNodes(expected, actual);
    }

    @Test
    public void testAnyDeserialization() throws IOException {
        String json = "{\"foo\": 1, \"bar\": [1, 2, 3], \"baz\": \"qux\"}";
        JsonParser parser = new ObjectMapper().getFactory().createParser(json);
        JsonNode expected = new ObjectMapper().readTree(json);

        JsonDeserializer<JsonNode> deserializer = JsonNodeDeserializer.instance;
        JsonNode actual = deserializer.deserialize(parser, new DeserializationContext(), new JsonNodeFactory());

        assertEqualsJsonNodes(expected, actual);
    }

    @Test
    public void testNullDeserialization() throws IOException {
        String json = "null";
        JsonParser parser = new ObjectMapper().getFactory().createParser(json);

        JsonDeserializer<JsonNode> deserializer = JsonNodeDeserializer.instance;
        JsonNode actual = deserializer.deserialize(parser, new DeserializationContext(), new JsonNodeFactory());

        assertTrue(actual.isNull());
    }

    @Test
    public void testDeserializeWithType() throws IOException {
        String json = "{\"foo\": 1, \"bar\": [1, 2, 3], \"baz\": \"qux\"}";
        JsonParser parser = new ObjectMapper().getFactory().createParser(json);

        TypeDeserializer typeDeserializer = new JsonNodeTypeDeserializer();
        JsonNode actual = JsonNodeDeserializer.instance.deserializeWithType(parser, new DeserializationContext(), typeDeserializer);

        assertEqualsJsonNodes(new ObjectMapper().readTree(json), actual);
    }

    private void assertEqualsJsonNodes(JsonNode expected, JsonNode actual) {
        if (!expected.getClass().equals(actual.getClass())) {
            throw new AssertionError("Expected and actual JsonNodes have different classes: " +
                    expected.getClass().getName() + " and " + actual.getClass().getName());
        }

        switch (expected.getClass().getName()) {
            case "com.fasterxml.jackson.databind.node.ObjectNode":
                ObjectNode expectedObject = (ObjectNode) expected;
                ObjectNode actualObject = (ObjectNode) actual;

                for (Map.Entry<String, JsonNode> entry : expectedObject.fields()) {
                    String key = entry.getKey();
                    JsonNode expectedValue = entry.getValue();
                    JsonNode actualValue = actualObject.get(key);

                    assertEqualsJsonNodes(expectedValue, actualValue);
                }

                break;
            case "com.fasterxml.jackson.databind.node.ArrayNode":
                ArrayNode expectedArray = (ArrayNode) expected;
                ArrayNode actualArray = (ArrayNode) actual;

                for (int i = 0; i < expectedArray.size(); i++) {
                    JsonNode expectedElement = expectedArray.get(i);
                    JsonNode actualElement = actualArray.get(i);

                    assertEqualsJsonNodes(expectedElement, actualElement);
                }

                break;
            case "com.fasterxml.jackson.databind.node.TextNode":
            case "com.fasterxml.jackson.databind.node.NumberNode":
            case "com.fasterxml.jackson.databind.node.BooleanNode":
            case "com.fasterxml.jackson.databind.node.NullNode":
                assertTrue(expected.equals(actual));
                break;
            default:
                throw new AssertionError("Unexpected JsonNode type: " + expected.getClass().getName());
        }
    }
}

class JsonNodeTypeDeserializer extends TypeDeserializer {
    @Override
    public JsonNode deserializeTypedFromAny(JsonParser p, DeserializationContext ctxt)
            throws IOException {
        return JsonNodeDeserializer.instance.deserialize(p, ctxt, new JsonNodeFactory());
    }
}