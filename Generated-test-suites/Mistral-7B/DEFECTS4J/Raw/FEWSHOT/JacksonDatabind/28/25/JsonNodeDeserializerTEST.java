package com.fasterxml.jackson.databind.deser.std;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.node.*;
import org.junit.Test;
import static org.junit.Assert.*;

public class JsonNodeDeserializerTest {

    @Test
    public void testDeserializeNullValue() throws IOException {
        JsonNodeDeserializer deserializer = new JsonNodeDeserializer();
        DeserializationContext ctxt = new TestContext();
        JsonParser p = createMockParser(JsonToken.NULL);
        assertEquals(NullNode.getInstance(), deserializer.deserialize(p, ctxt));
    }

    @Test
    public void testDeserializeObject() throws IOException {
        JsonNodeDeserializer deserializer = new JsonNodeDeserializer();
        DeserializationContext ctxt = new TestContext();
        JsonParser p = createMockParser("{\"name\": \"John\"}");

        ObjectNode result = deserializer.deserialize(p, ctxt);

        assertEquals("John", result.get("name").asText());
    }

    @Test
    public void testDeserializeArray() throws IOException {
        JsonNodeDeserializer deserializer = new JsonNodeDeserializer();
        DeserializationContext ctxt = new TestContext();
        JsonParser p = createMockParser("[1, 2, 3]");

        ArrayNode result = (ArrayNode) deserializer.deserialize(p, ctxt);

        assertEquals(3, result.size());
        assertEquals(1, result.get(0).asInt());
        assertEquals(2, result.get(1).asInt());
        assertEquals(3, result.get(2).asInt());
    }

    @Test
    public void testDeserializeAny() throws IOException {
        JsonNodeDeserializer deserializer = new JsonNodeDeserializer();
        DeserializationContext ctxt = new TestContext();
        JsonParser p = createMockParser("{\"name\": 1}");

        JsonNode result = deserializer.deserializeAny(p, ctxt);

        assertEquals(JsonToken.VALUE_NUMBER_INT, p.getCurrentToken());
        assertEquals(1, result.asInt());
    }

    @Test
    public void testDeserializeObjectWithDuplicateField() throws IOException {
        JsonNodeDeserializer deserializer = new JsonNodeDeserializer();
        DeserializationContext ctxt = new TestContext();
        JsonParser p = createMockParser("{\"name\": \"John\", \"name\": \"Jane\"}");

        ObjectNode result = (ObjectNode) deserializer.deserialize(p, ctxt);

        assertEquals(2, result.size());
        assertEquals("John", result.get("name").asText());
        assertEquals("Jane", result.get("name").asText());
    }

    @Test(expected = JsonMappingException.class)
    public void testDeserializeInvalidJson() throws IOException {
        JsonNodeDeserializer deserializer = new JsonNodeDeserializer();
        DeserializationContext ctxt = new TestContext();
        JsonParser p = createMockParser("{invalid: JSON}");

        deserializer.deserialize(p, ctxt);
    }

    @Test
    public void testDeserializeUnknownFieldType() throws IOException {
        JsonNodeDeserializer deserializer = new JsonNodeDeserializer();
        DeserializationContext ctxt = new TestContext();
        JsonParser p = createMockParser("{\"unknown\": true}");

        JsonNode result = deserializer.deserializeAny(p, ctxt);

        assertEquals(JsonToken.VALUE_TRUE, p.getCurrentToken());
        assertEquals(true, result.asBoolean());
    }

    private JsonParser createMockParser(String json) throws IOException {
        return createMockParser(JsonToken.START_OBJECT, json);
    }

    private JsonParser createMockParser(JsonToken token, String json) throws IOException {
        MockParser parser = new MockParser();
        parser.parseModel(new JsonFactory(), new ObjectMapper().readTree(json));
        parser.nextToken();
        parser.nextToken();
        parser.setCurrentLocation(new StandardLocation(null, 0, 0));
        parser.setCurrentName(null);
        parser.setCurrentToken(token);
        parser.setParserConfig(new ParserConfig());
        return parser;
    }

    private JsonParser createMockParser(JsonToken token) throws IOException {
        return createMockParser(token, null);
    }

    private static class TestContext extends DeserializationContext {
        @Override
        public JsonNodeFactory nodeFactory() {
            return new StandardJsonNodeFactory();
        }
    }
}