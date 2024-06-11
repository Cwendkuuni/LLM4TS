import static org.junit.Assert.*;

import java.io.IOException;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.deser.std.JsonNodeDeserializer;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

public class JsonNodeDeserializerTest {
    
    private JsonDeserializer<JsonNode> deserializer;

    @Before
    public void setup() {
        deserializer = JsonNodeDeserializer.instance;
    }
    
    @Test
    public void testGetNullValue() throws Exception {
        DeserializationContext ctxt = new ObjectMapper().getDeserializationContext();
        JsonNode nullValue = deserializer.getNullValue(ctxt);
        
        assertEquals(NullNode.getInstance(), nullValue);
    }
    
    @Test
    public void testDeserialize_ObjectNode() throws Exception {
        JsonFactory factory = new JsonFactory();
        JsonParser parser = factory.createParser("{\"name\":\"John\",\"age\":30}");
        DeserializationContext ctxt = new ObjectMapper().getDeserializationContext();
        
        ObjectNode objectNode = (ObjectNode) deserializer.deserialize(parser, ctxt);
        
        assertTrue(objectNode.get("name").isTextual());
        assertEquals("John", objectNode.get("name").asText());
        
        assertTrue(objectNode.get("age").isNumber());
        assertEquals(30, objectNode.get("age").asInt());
    }
    
    @Test
    public void testDeserialize_ArrayNode() throws Exception {
        JsonFactory factory = new JsonFactory();
        JsonParser parser = factory.createParser("[1, 2, 3]");
        DeserializationContext ctxt = new ObjectMapper().getDeserializationContext();
        
        ArrayNode arrayNode = (ArrayNode) deserializer.deserialize(parser, ctxt);
        
        assertTrue(arrayNode.get(0).isNumber());
        assertEquals(1, arrayNode.get(0).asInt());
        
        assertTrue(arrayNode.get(1).isNumber());
        assertEquals(2, arrayNode.get(1).asInt());
        
        assertTrue(arrayNode.get(2).isNumber());
        assertEquals(3, arrayNode.get(2).asInt());
    }
    
    @Test
    public void testDeserializeAny_ObjectNode() throws Exception {
        JsonFactory factory = new JsonFactory();
        JsonParser parser = factory.createParser("{\"name\":\"John\",\"age\":30}");
        DeserializationContext ctxt = new ObjectMapper().getDeserializationContext();
        JsonNodeFactory nodeFactory = new ObjectMapper().getNodeFactory();
        
        JsonNode jsonNode = deserializer.deserializeAny(parser, ctxt, nodeFactory);
        
        assertTrue(jsonNode.isObject());
        ObjectNode objectNode = (ObjectNode) jsonNode;
        
        assertTrue(objectNode.get("name").isTextual());
        assertEquals("John", objectNode.get("name").asText());
        
        assertTrue(objectNode.get("age").isNumber());
        assertEquals(30, objectNode.get("age").asInt());
    }
    
    @Test
    public void testDeserializeAny_ArrayNode() throws Exception {
        JsonFactory factory = new JsonFactory();
        JsonParser parser = factory.createParser("[1, 2, 3]");
        DeserializationContext ctxt = new ObjectMapper().getDeserializationContext();
        JsonNodeFactory nodeFactory = new ObjectMapper().getNodeFactory();
        
        JsonNode jsonNode = deserializer.deserializeAny(parser, ctxt, nodeFactory);
        
        assertTrue(jsonNode.isArray());
        ArrayNode arrayNode = (ArrayNode) jsonNode;
        
        assertTrue(arrayNode.get(0).isNumber());
        assertEquals(1, arrayNode.get(0).asInt());
        
        assertTrue(arrayNode.get(1).isNumber());
        assertEquals(2, arrayNode.get(1).asInt());
        
        assertTrue(arrayNode.get(2).isNumber());
        assertEquals(3, arrayNode.get(2).asInt());
    }
}