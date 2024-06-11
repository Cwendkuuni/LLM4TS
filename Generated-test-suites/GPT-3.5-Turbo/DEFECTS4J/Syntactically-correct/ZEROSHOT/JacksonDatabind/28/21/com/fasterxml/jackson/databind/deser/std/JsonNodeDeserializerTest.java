package com.fasterxml.jackson.databind.deser.std;

import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.node.*;
import com.fasterxml.jackson.databind.util.RawValue;
import java.io.IOException;
import org.junit.Test;
import static org.junit.Assert.*;

public class JsonNodeDeserializerTest {

    @Test
    public void testGetNullValue_withDeserializationContext() {
        DeserializationContext ctxt = new DefaultDeserializationContext(null);
        JsonNodeDeserializer jsonNodeDeserializer = new JsonNodeDeserializer();
        JsonNode result = jsonNodeDeserializer.getNullValue(ctxt);
        assertEquals(NullNode.getInstance(), result);
    }

    @Test
    public void testGetNullValue_withoutDeserializationContext() {
        JsonNodeDeserializer jsonNodeDeserializer = new JsonNodeDeserializer();
        JsonNode result = jsonNodeDeserializer.getNullValue();
        assertEquals(NullNode.getInstance(), result);
    }

    @Test
    public void testDeserialize_withStartObjectToken() throws IOException {
        JsonParser p = new MappingJsonFactory().createParser("{\"name\":\"John\"}");
        DeserializationContext ctxt = new DefaultDeserializationContext(null);
        JsonNodeDeserializer jsonNodeDeserializer = new JsonNodeDeserializer();
        JsonNode result = jsonNodeDeserializer.deserialize(p, ctxt);
        ObjectNode expected = new ObjectMapper().createObjectNode();
        expected.put("name", "John");
        assertEquals(expected, result);
    }

    @Test
    public void testDeserialize_withStartArrayToken() throws IOException {
        JsonParser p = new MappingJsonFactory().createParser("[1,2,3]");
        DeserializationContext ctxt = new DefaultDeserializationContext(null);
        JsonNodeDeserializer jsonNodeDeserializer = new JsonNodeDeserializer();
        JsonNode result = jsonNodeDeserializer.deserialize(p, ctxt);
        ArrayNode expected = new ObjectMapper().createArrayNode();
        expected.add(1);
        expected.add(2);
        expected.add(3);
        assertEquals(expected, result);
    }

    @Test
    public void testDeserialize_withOtherToken() throws IOException {
        JsonParser p = new MappingJsonFactory().createParser("true");
        DeserializationContext ctxt = new DefaultDeserializationContext(null);
        JsonNodeDeserializer jsonNodeDeserializer = new JsonNodeDeserializer();
        JsonNode result = jsonNodeDeserializer.deserialize(p, ctxt);
        assertEquals(BooleanNode.TRUE, result);
    }
}