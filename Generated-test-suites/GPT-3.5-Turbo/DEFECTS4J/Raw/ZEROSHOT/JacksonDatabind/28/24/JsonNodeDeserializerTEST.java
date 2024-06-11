package com.fasterxml.jackson.databind.deser.std;

import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.node.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@SuppressWarnings("serial")
public class JsonNodeDeserializerTest {

    private JsonNodeDeserializer jsonNodeDeserializer;

    @BeforeEach
    public void setup() {
        jsonNodeDeserializer = new JsonNodeDeserializer();
    }

    @Test
    public void testGetNullValue() {
        DeserializationContext ctxt = new DefaultDeserializationContext(null, null);
        JsonNode result = jsonNodeDeserializer.getNullValue(ctxt);
        assertEquals(NullNode.getInstance(), result);
    }

    @Test
    public void testDeserialize_Object() {
        MockJsonParser p = new MockJsonParser(JsonToken.START_OBJECT);
        DeserializationContext ctxt = new DefaultDeserializationContext(null, null);
        JsonNodeFactory nodeFactory = new JsonNodeFactory(false);
        JsonNode result = null;
        try {
            result = jsonNodeDeserializer.deserialize(p, ctxt);
        } catch (Exception e) {
            fail("Exception thrown: " + e.getMessage());
        }
        ObjectNode expectedNode = nodeFactory.objectNode();
        assertEquals(expectedNode, result);
    }

    @Test
    public void testDeserialize_Array() {
        MockJsonParser p = new MockJsonParser(JsonToken.START_ARRAY);
        DeserializationContext ctxt = new DefaultDeserializationContext(null, null);
        JsonNodeFactory nodeFactory = new JsonNodeFactory(false);
        JsonNode result = null;
        try {
            result = jsonNodeDeserializer.deserialize(p, ctxt);
        } catch (Exception e) {
            fail("Exception thrown: " + e.getMessage());
        }
        ArrayNode expectedNode = nodeFactory.arrayNode();
        assertEquals(expectedNode, result);
    }

    @Test
    public void testDeserialize_Any() {
        MockJsonParser p = new MockJsonParser(JsonToken.VALUE_TRUE);
        DeserializationContext ctxt = new DefaultDeserializationContext(null, null);
        JsonNodeFactory nodeFactory = new JsonNodeFactory(false);
        JsonNode result = null;
        try {
            result = jsonNodeDeserializer.deserialize(p, ctxt);
        } catch (Exception e) {
            fail("Exception thrown: " + e.getMessage());
        }
        JsonNode expectedNode = nodeFactory.booleanNode(true);
        assertEquals(expectedNode, result);
    }

    @Test
    public void testDeserializeWithType() {
        MockJsonParser p = new MockJsonParser(JsonToken.START_OBJECT);
        TypeDeserializer typeDeserializer = new DefaultTypeResolverBuilder(null).buildTypeDeserializer(null, null, null);
        DeserializationContext ctxt = new DefaultDeserializationContext(null, null);
        JsonNodeFactory nodeFactory = new JsonNodeFactory(false);
        Object result = null;
        try {
            result = jsonNodeDeserializer.deserializeWithType(p, ctxt, typeDeserializer);
        } catch (Exception e) {
            fail("Exception thrown: " + e.getMessage());
        }
        Object expectedNode = typeDeserializer.deserializeTypedFromAny(p, ctxt);
        assertEquals(expectedNode, result);
    }

    @Test
    public void testIsCachable() {
        boolean result = jsonNodeDeserializer.isCachable();
        assertTrue(result);
    }

    @Test
    public void testHandleDuplicateField() {
        MockJsonParser p = new MockJsonParser(JsonToken.FIELD_NAME);
        DeserializationContext ctxt = new DefaultDeserializationContext(null, null);
        JsonNodeFactory nodeFactory = new JsonNodeFactory(false);
        ObjectNode objectNode = nodeFactory.objectNode();
        JsonNode oldValue = nodeFactory.textNode("oldValue");
        JsonNode newValue = nodeFactory.textNode("newValue");
        try {
            jsonNodeDeserializer._handleDuplicateField(p, ctxt, nodeFactory, "fieldName", objectNode, oldValue, newValue);
        } catch (Exception e) {
            fail("Exception thrown: " + e.getMessage());
        }
    }

    private static class MockJsonParser extends TreeCodecJsonParser {

        private JsonToken currentToken;

        public MockJsonParser(JsonToken currentToken) {
            super(null, 0);
            this.currentToken = currentToken;
        }

        @Override
        public JsonToken getCurrentToken() {
            return currentToken;
        }

        @Override
        public String getCurrentName() {
            return null;
        }

        @Override
        public JsonToken nextToken() {
            return null;
        }
    }
}