package com.fasterxml.jackson.databind.deser.std;

import static org.junit.Assert.*;

import java.io.IOException;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.deser.DeserializationContext;

public class JsonNodeDeserializerTest {

    private JsonNodeDeserializer deserializer;

    @Before
    public void setUp() {
        deserializer = new JsonNodeDeserializer();
    }

    @Test
    public void testGetNullValue() {
        DeserializationContext ctxt = new ObjectMapper().getDeserializationContext();
        JsonNode result = deserializer.getNullValue(ctxt);
        assertEquals("NullNode", result.getNodeType().name());
    }

    @Test
    public void testDeserialize() throws IOException {
        JsonFactory factory = new JsonFactory();
        JsonParser parser = factory.createParser("{}");
        DeserializationContext ctxt = new ObjectMapper().getDeserializationContext();
        JsonNode result = deserializer.deserialize(parser, ctxt);
        assertEquals("ObjectNode", result.getNodeType().name());
    }

}