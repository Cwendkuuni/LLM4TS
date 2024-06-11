package com.fasterxml.jackson.databind.deser.std;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.util.RawValue;
import org.junit.Test;
import java.io.IOException;
import java.math.BigDecimal;
import static org.junit.Assert.*;

public class JsonNodeDeserializerTest {

    @Test
    public void testGetNullValue() {
        JsonNodeDeserializer jsonNodeDeserializer = new JsonNodeDeserializer();
        DeserializationContext ctxt = null;
        JsonNode expected = jsonNodeDeserializer.getNullValue(ctxt);
        assertEquals(NullNode.getInstance(), expected);
    }

    @Test
    public void testGetNullValueDeprecated() {
        JsonNodeDeserializer jsonNodeDeserializer = new JsonNodeDeserializer();
        JsonNode expected = jsonNodeDeserializer.getNullValue();
        assertEquals(NullNode.getInstance(), expected);
    }

    @Test
    public void testDeserialize() throws IOException {
        JsonNodeDeserializer jsonNodeDeserializer = new JsonNodeDeserializer();
        DeserializationContext ctxt = null;
        JsonParser p = null;
        JsonNodeFactory nodeFactory = new JsonNodeFactory(false);
        JsonNode expected = jsonNodeDeserializer.deserialize(p, ctxt);
        assertEquals(NullNode.getInstance(), expected);
    }

    @Test
    public void testDeserializeObject() throws IOException {
        JsonNodeDeserializer jsonNodeDeserializer = new JsonNodeDeserializer();
        DeserializationContext ctxt = null;
        JsonParser p = null;
        JsonNodeFactory nodeFactory = new JsonNodeFactory(false);
        JsonNode expectedNode = new ObjectMapper().readTree("{}");
        ObjectNode expected = jsonNodeDeserializer.deserializeObject(p, ctxt, nodeFactory);
        assertEquals(expectedNode, expected);
    }

    @Test
    public void testDeserializeArray() throws IOException {
        JsonNodeDeserializer jsonNodeDeserializer = new JsonNodeDeserializer();
        DeserializationContext ctxt = null;
        JsonParser p = null;
        JsonNodeFactory nodeFactory = new JsonNodeFactory(false);
        JsonNode expectedNode = new ObjectMapper().readTree("[]");
        ArrayNode expected = jsonNodeDeserializer.deserializeArray(p, ctxt, nodeFactory);
        assertEquals(expectedNode, expected);
    }

    @Test
    public void testDeserializeAny() throws IOException {
        JsonNodeDeserializer jsonNodeDeserializer = new JsonNodeDeserializer();
        DeserializationContext ctxt = null;
        JsonParser p = null;
        JsonNodeFactory nodeFactory = new JsonNodeFactory(false);
        JsonNode expectedNode = new ObjectMapper().readTree("{}");
        JsonNode expected = jsonNodeDeserializer.deserializeAny(p, ctxt, nodeFactory);
        assertEquals(expectedNode, expected);
    }

    @Test
    public void test_handleDuplicateField() throws JsonMappingException {
        JsonNodeDeserializer jsonNodeDeserializer = new JsonNodeDeserializer();
        DeserializationContext ctxt = null;
        JsonParser p = null;
        JsonNodeFactory nodeFactory = new JsonNodeFactory(false);
        String fieldName = "fieldName";
        ObjectNode objectNode = new ObjectMapper().createObjectNode();
        JsonNode oldValue = new ObjectMapper().readTree("null");
        JsonNode newValue = new ObjectMapper().readTree("null");
        jsonNodeDeserializer._handleDuplicateField(p, ctxt, nodeFactory, fieldName, objectNode, oldValue, newValue);
        // add assertion here
    }

    @Test
    public void test_fromInt() throws IOException {
        JsonNodeDeserializer jsonNodeDeserializer = new JsonNodeDeserializer();
        DeserializationContext ctxt = null;
        JsonParser p = null;
        JsonNodeFactory nodeFactory = new JsonNodeFactory(false);
        JsonNode expected = new ObjectMapper().readTree("0");
        JsonNode result = jsonNodeDeserializer._fromInt(p, ctxt, nodeFactory);
        assertEquals(expected, result);
    }

    @Test
    public void test_fromFloat() throws IOException {
        JsonNodeDeserializer jsonNodeDeserializer = new JsonNodeDeserializer();
        DeserializationContext ctxt = null;
        JsonParser p = null;
        JsonNodeFactory nodeFactory = new JsonNodeFactory(false);
        JsonNode expected = new ObjectMapper().readTree("0.0");
        JsonNode result = jsonNodeDeserializer._fromFloat(p, ctxt, nodeFactory);
        assertEquals(expected, result);
    }

    @Test
    public void test_fromEmbedded() throws IOException {
        JsonNodeDeserializer jsonNodeDeserializer = new JsonNodeDeserializer();
        DeserializationContext ctxt = null;
        JsonParser p = null;
        JsonNodeFactory nodeFactory = new JsonNodeFactory(false);
        JsonNode expectedNode = new ObjectMapper().readTree("\"embedded\"");
        JsonNode result = jsonNodeDeserializer._fromEmbedded(p, ctxt, nodeFactory);
        assertEquals(expectedNode, result);
    }
}