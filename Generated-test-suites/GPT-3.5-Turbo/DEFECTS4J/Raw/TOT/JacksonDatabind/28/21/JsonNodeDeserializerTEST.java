package com.fasterxml.jackson.databind.deser.std;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.io.IOException;

import org.junit.Test;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.node.TextNode;

public class JsonNodeDeserializerTest {
    
    private final ObjectMapper mapper = new ObjectMapper();

    @Test
    public void testDeserialize_NullValue_ShouldReturnNullNode() throws IOException {
        // Arrange
        JsonNodeDeserializer deserializer = JsonNodeDeserializer.getInstance();
        DeserializationContext ctxt = mapper.getDeserializationContext();
        
        // Act
        JsonNode result = deserializer.deserialize(null, ctxt);
        
        // Assert
        assertNull(result);
    }
    
    @Test
    public void testDeserialize_StartObjectToken_ShouldDeserializeObjectNode() throws IOException {
        // Arrange
        String json = "{\"key\":\"value\"}";
        JsonFactory jsonFactory = new JsonFactory();
        JsonParser p = jsonFactory.createParser(json);
        DeserializationContext ctxt = mapper.getDeserializationContext();
        ctxt.setNodeFactory(JsonNodeFactory.withExactBigDecimals(true));
        JsonDeserializer<? extends JsonNode> deserializer = JsonNodeDeserializer.getDeserializer(ObjectNode.class);
        
        // Act
        JsonNode result = deserializer.deserialize(p, ctxt);
        
        // Assert
        ObjectNode expected = JsonNodeFactory.instance.objectNode();
        expected.set("key", JsonNodeFactory.instance.textNode("value"));
        assertEquals(expected, result);
    }

    @Test
    public void testDeserialize_StartArrayToken_ShouldDeserializeArrayNode() throws IOException {
        // Arrange
        String json = "[1, 2, 3]";
        JsonFactory jsonFactory = new JsonFactory();
        JsonParser p = jsonFactory.createParser(json);
        DeserializationContext ctxt = mapper.getDeserializationContext();
        ctxt.setNodeFactory(JsonNodeFactory.withExactBigDecimals(true));
        JsonDeserializer<? extends JsonNode> deserializer = JsonNodeDeserializer.getDeserializer(ArrayNode.class);
        
        // Act
        JsonNode result = deserializer.deserialize(p, ctxt);
        
        // Assert
        ArrayNode expected = JsonNodeFactory.instance.arrayNode();
        expected.add(1);
        expected.add(2);
        expected.add(3);
        assertEquals(expected, result);
    }
}