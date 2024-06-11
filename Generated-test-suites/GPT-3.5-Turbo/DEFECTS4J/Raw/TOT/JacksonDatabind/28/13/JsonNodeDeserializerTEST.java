import static org.junit.Assert.*;
import org.junit.Test;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

public class JsonNodeDeserializerTest {

    @Test
    public void testDeserialize_ObjectNode() {
        String json = "{\"name\":\"John\", \"age\":30, \"city\":\"New York\"}";
        ObjectMapper mapper = new ObjectMapper();
        JsonNodeDeserializer deserializer = new JsonNodeDeserializer();
        
        try {
            JsonNode node = deserializer.deserialize(mapper.getFactory().createParser(json), mapper.getDeserializationContext());
            
            assertTrue(node instanceof ObjectNode);
            assertEquals("John", node.get("name").asText());
            assertEquals(30, node.get("age").asInt());
            assertEquals("New York", node.get("city").asText());
        } catch (Exception e) {
            fail("Exception thrown: " + e.getMessage());
        }
    }
    
    @Test
    public void testDeserialize_ArrayNode() {
        String json = "[\"apple\", \"banana\", \"cherry\"]";
        ObjectMapper mapper = new ObjectMapper();
        JsonNodeDeserializer deserializer = new JsonNodeDeserializer();
        
        try {
            JsonNode node = deserializer.deserialize(mapper.getFactory().createParser(json), mapper.getDeserializationContext());
            
            assertTrue(node instanceof ArrayNode);
            assertEquals("apple", node.get(0).asText());
            assertEquals("banana", node.get(1).asText());
            assertEquals("cherry", node.get(2).asText());
        } catch (Exception e) {
            fail("Exception thrown: " + e.getMessage());
        }
    }
    
    @Test
    public void testDeserialize_NullNode() {
        String json = "null";
        ObjectMapper mapper = new ObjectMapper();
        JsonNodeDeserializer deserializer = new JsonNodeDeserializer();
        
        try {
            JsonNode node = deserializer.deserialize(mapper.getFactory().createParser(json), mapper.getDeserializationContext());
            
            assertNull(node);
        } catch (Exception e) {
            fail("Exception thrown: " + e.getMessage());
        }
    }
    
    @Test
    public void testDeserialize_EmbeddedObjectNode() {
        String json = "{\"_embedded\":{\"name\":\"John\", \"age\":30, \"city\":\"New York\"}}";
        ObjectMapper mapper = new ObjectMapper();
        JsonNodeDeserializer deserializer = new JsonNodeDeserializer();
        
        try {
            JsonNode node = deserializer.deserialize(mapper.getFactory().createParser(json), mapper.getDeserializationContext());
            
            assertTrue(node instanceof ObjectNode);
            assertTrue(node.has("_embedded"));
            assertEquals("John", node.get("_embedded").get("name").asText());
            assertEquals(30, node.get("_embedded").get("age").asInt());
            assertEquals("New York", node.get("_embedded").get("city").asText());
        } catch (Exception e) {
            fail("Exception thrown: " + e.getMessage());
        }
    }
    
    @Test
    public void testDeserialize_InvalidJson() {
        String json = "{\"name\":\"John\", \"age\":30\"city\":\"New York\"}";
        ObjectMapper mapper = new ObjectMapper();
        JsonNodeDeserializer deserializer = new JsonNodeDeserializer();
        
        try {
            deserializer.deserialize(mapper.getFactory().createParser(json), mapper.getDeserializationContext());
            fail("Expected JsonParseException");
        } catch (JsonParseException e) {
            // Expected exception
        } catch (Exception e) {
            fail("Exception thrown: " + e.getMessage());
        }
    }
    
}