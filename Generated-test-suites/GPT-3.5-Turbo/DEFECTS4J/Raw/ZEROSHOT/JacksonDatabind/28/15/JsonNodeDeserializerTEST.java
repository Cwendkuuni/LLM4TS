package com.fasterxml.jackson.databind.deser.std;

import org.junit.Test;
import static org.junit.Assert.*;

public class JsonNodeDeserializerTest {

    @Test
    public void testGetNullValue() {
        JsonNodeDeserializer jsonNodeDeserializer = new JsonNodeDeserializer();
        JsonNode nullValue = jsonNodeDeserializer.getNullValue(null);
        assertNotNull(nullValue);
        assertTrue(nullValue instanceof NullNode);
    }

    @Test
    public void testDeserialize() throws Exception {
        JsonNodeDeserializer jsonNodeDeserializer = new JsonNodeDeserializer();

        // Test with JSON Object
        JsonParser jsonParser = new JsonFactory().createParser("{\"name\":\"John\", \"age\":30}");
        DeserializationContext deserializationContext = new DeserializationContext(null, null);
        JsonNode jsonNode = jsonNodeDeserializer.deserialize(jsonParser, deserializationContext);
        assertNotNull(jsonNode);
        assertTrue(jsonNode instanceof ObjectNode);

        // Test with JSON Array
        jsonParser = new JsonFactory().createParser("[{\"name\":\"John\", \"age\":30}, {\"name\":\"Mark\", \"age\":40}]");
        jsonNode = jsonNodeDeserializer.deserialize(jsonParser, deserializationContext);
        assertNotNull(jsonNode);
        assertTrue(jsonNode instanceof ArrayNode);

        // Test with JSON String
        jsonParser = new JsonFactory().createParser("\"Hello World\"");
        jsonNode = jsonNodeDeserializer.deserialize(jsonParser, deserializationContext);
        assertNotNull(jsonNode);
        assertTrue(jsonNode instanceof TextNode);

        // Test with JSON Number
        jsonParser = new JsonFactory().createParser("10");
        jsonNode = jsonNodeDeserializer.deserialize(jsonParser, deserializationContext);
        assertNotNull(jsonNode);
        assertTrue(jsonNode instanceof IntNode);

        // Test with JSON Boolean
        jsonParser = new JsonFactory().createParser("true");
        jsonNode = jsonNodeDeserializer.deserialize(jsonParser, deserializationContext);
        assertNotNull(jsonNode);
        assertTrue(jsonNode instanceof BooleanNode);

        // Test with JSON Null
        jsonParser = new JsonFactory().createParser("null");
        jsonNode = jsonNodeDeserializer.deserialize(jsonParser, deserializationContext);
        assertNotNull(jsonNode);
        assertTrue(jsonNode instanceof NullNode);
    }
}