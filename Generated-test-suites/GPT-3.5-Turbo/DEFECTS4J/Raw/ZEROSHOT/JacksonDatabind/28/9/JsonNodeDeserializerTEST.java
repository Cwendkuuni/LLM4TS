package com.fasterxml.jackson.databind.deser.std;

import java.io.IOException;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.node.*;
import com.fasterxml.jackson.databind.util.RawValue;

/**
 * Deserializer that can build instances of {@link JsonNode} from any
 * JSON content, using appropriate {@link JsonNode} type.
 */
@SuppressWarnings("serial")
public class JsonNodeDeserializerTest {
    @Test
    public void testGetNullValue() {
        JsonNodeDeserializer jsonNodeDeserializer = new JsonNodeDeserializer();
        DeserializationContext ctxt = null;

        JsonNode result = jsonNodeDeserializer.getNullValue(ctxt);

        // assertion here
    }

    @Test
    @Deprecated // since 2.6, remove from 2.7
    public void testGetNullValue_deprecated() {
        JsonNodeDeserializer jsonNodeDeserializer = new JsonNodeDeserializer();

        JsonNode result = jsonNodeDeserializer.getNullValue();

        // assertion here
    }

    @Test
    public void testDeserialize() throws IOException {
        JsonNodeDeserializer jsonNodeDeserializer = new JsonNodeDeserializer();
        JsonParser p = null;
        DeserializationContext ctxt = null;

        JsonNode result = jsonNodeDeserializer.deserialize(p, ctxt);

        // assertion here
    }

    @Test
    public void testDeserializeObject() throws IOException {
        JsonNodeDeserializer jsonNodeDeserializer = new JsonNodeDeserializer();
        JsonParser p = null;
        DeserializationContext ctxt = null;
        JsonNodeFactory nodeFactory = null;

        ObjectNode result = jsonNodeDeserializer.deserializeObject(p, ctxt, nodeFactory);

        // assertion here
    }

    @Test
    public void testDeserializeArray() throws IOException {
        JsonNodeDeserializer jsonNodeDeserializer = new JsonNodeDeserializer();
        JsonParser p = null;
        DeserializationContext ctxt = null;
        JsonNodeFactory nodeFactory = null;

        ArrayNode result = jsonNodeDeserializer.deserializeArray(p, ctxt, nodeFactory);

        // assertion here
    }

    @Test
    public void testDeserializeAny() throws IOException {
        JsonNodeDeserializer jsonNodeDeserializer = new JsonNodeDeserializer();
        JsonParser p = null;
        DeserializationContext ctxt = null;
        JsonNodeFactory nodeFactory = null;

        JsonNode result = jsonNodeDeserializer.deserializeAny(p, ctxt, nodeFactory);

        // assertion here
    }

    @Test
    public void test_handleDuplicateField() throws JsonProcessingException {
        JsonNodeDeserializer jsonNodeDeserializer = new JsonNodeDeserializer();
        JsonParser p = null;
        DeserializationContext ctxt = null;
        JsonNodeFactory nodeFactory = null;
        String fieldName = null;
        ObjectNode objectNode = null;
        JsonNode oldValue = null;
        JsonNode newValue = null;

        jsonNodeDeserializer._handleDuplicateField(p, ctxt, nodeFactory, fieldName, objectNode, oldValue, newValue);

        // assertion here
    }

    @Test
    public void test_fromInt() throws IOException {
        JsonNodeDeserializer jsonNodeDeserializer = new JsonNodeDeserializer();
        JsonParser p = null;
        DeserializationContext ctxt = null;
        JsonNodeFactory nodeFactory = null;

        JsonNode result = jsonNodeDeserializer._fromInt(p, ctxt, nodeFactory);

        // assertion here
    }

    @Test
    public void test_fromFloat() throws IOException {
        JsonNodeDeserializer jsonNodeDeserializer = new JsonNodeDeserializer();
        JsonParser p = null;
        DeserializationContext ctxt = null;
        JsonNodeFactory nodeFactory = null;

        JsonNode result = jsonNodeDeserializer._fromFloat(p, ctxt, nodeFactory);

        // assertion here
    }

    @Test
    public void test_fromEmbedded() throws IOException {
        JsonNodeDeserializer jsonNodeDeserializer = new JsonNodeDeserializer();
        JsonParser p = null;
        DeserializationContext ctxt = null;
        JsonNodeFactory nodeFactory = null;

        JsonNode result = jsonNodeDeserializer._fromEmbedded(p, ctxt, nodeFactory);

        // assertion here
    }
}