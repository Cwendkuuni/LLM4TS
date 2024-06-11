package com.fasterxml.jackson.databind.deser.std;

import org.junit.Assert;
import org.junit.Test;
import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.node.*;
import com.fasterxml.jackson.databind.util.RawValue;

public class JsonNodeDeserializerTest {

    @Test
    public void testGetNullValue() {
        JsonNodeDeserializer jsonNodeDeserializer = new JsonNodeDeserializer();
        DeserializationContext deserializationContext = null;
        JsonNode jsonNode = jsonNodeDeserializer.getNullValue(deserializationContext);
        Assert.assertTrue(jsonNode instanceof NullNode);
    }

    @Test
    public void testDeserialize() {
        JsonNodeDeserializer jsonNodeDeserializer = new JsonNodeDeserializer();
        JsonParser jsonParser = null;
        DeserializationContext deserializationContext = null;
        try {
            JsonNode jsonNode = jsonNodeDeserializer.deserialize(jsonParser, deserializationContext);
            Assert.assertNotNull(jsonNode);
        } catch (Exception ex) {
            Assert.fail("Exception should not be thrown");
        }
    }

    @Test
    public void testDeserializeObject() {
        BaseNodeDeserializer baseNodeDeserializer = new BaseNodeDeserializer<ObjectNode>(ObjectNode.class) {
            @Override
            protected void _handleDuplicateField(JsonParser p, DeserializationContext ctxt, JsonNodeFactory nodeFactory, String fieldName, ObjectNode objectNode, JsonNode oldValue, JsonNode newValue) throws JsonProcessingException {
                super._handleDuplicateField(p, ctxt, nodeFactory, fieldName, objectNode, oldValue, newValue);
            }

            @Override
            public ObjectNode deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
                return super.deserialize(p, ctxt);
            }
        };
        JsonParser jsonParser = null;
        DeserializationContext deserializationContext = null;
        JsonNodeFactory jsonNodeFactory = null;

        try {
            ObjectNode objectNode = (ObjectNode) baseNodeDeserializer.deserialize(jsonParser, deserializationContext);
            Assert.assertNotNull(objectNode);
        } catch (Exception ex) {
            Assert.fail("Exception should not be thrown");
        }
    }

    @Test
    public void testDeserializeArray() {
        BaseNodeDeserializer baseNodeDeserializer = new BaseNodeDeserializer<ArrayNode>(ArrayNode.class) {
            @Override
            protected void _handleDuplicateField(JsonParser p, DeserializationContext ctxt, JsonNodeFactory nodeFactory, String fieldName, ObjectNode objectNode, JsonNode oldValue, JsonNode newValue) throws JsonProcessingException {
                super._handleDuplicateField(p, ctxt, nodeFactory, fieldName, objectNode, oldValue, newValue);
            }

            @Override
            public ArrayNode deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
                return super.deserialize(p, ctxt);
            }
        };
        JsonParser jsonParser = null;
        DeserializationContext deserializationContext = null;
        JsonNodeFactory jsonNodeFactory = null;

        try {
            ArrayNode arrayNode = (ArrayNode) baseNodeDeserializer.deserialize(jsonParser, deserializationContext);
            Assert.assertNotNull(arrayNode);
        } catch (Exception ex) {
            Assert.fail("Exception should not be thrown");
        }
    }

    @Test
    public void testFromEmbedded() {
        BaseNodeDeserializer baseNodeDeserializer = new BaseNodeDeserializer<JsonNode>(JsonNode.class) {
            @Override
            protected void _handleDuplicateField(JsonParser p, DeserializationContext ctxt, JsonNodeFactory nodeFactory, String fieldName, ObjectNode objectNode, JsonNode oldValue, JsonNode newValue) throws JsonProcessingException {
                super._handleDuplicateField(p, ctxt, nodeFactory, fieldName, objectNode, oldValue, newValue);
            }

            @Override
            public JsonNode deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
                return super.deserialize(p, ctxt);
            }
        };
        JsonParser jsonParser = null;
        DeserializationContext deserializationContext = null;
        JsonNodeFactory jsonNodeFactory = null;

        try {
            JsonNode embeddedNode = baseNodeDeserializer._fromEmbedded(jsonParser, deserializationContext, jsonNodeFactory);
            Assert.assertNotNull(embeddedNode);
        } catch (Exception ex) {
            Assert.fail("Exception should not be thrown");
        }
    }

}