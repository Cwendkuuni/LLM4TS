import static org.junit.Assert.*;

import java.io.IOException;

import org.junit.Test;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class JsonNodeDeserializerTest {

    @Test
    public void testDeserialize_NullValue() throws JsonParseException, JsonMappingException, IOException {
        String json = "null";
        ObjectMapper objectMapper = new ObjectMapper();
        
        JsonNode expected = objectMapper.readTree(json);
        JsonNode actual = JsonNodeDeserializer.instance.deserialize(objectMapper.getFactory().createParser(json), null);

        assertEquals(expected, actual);
    }
    
    @Test
    public void testDeserialize_ObjectNode() throws JsonParseException, JsonMappingException, IOException {
        String json = "{\"key\":\"value\"}";
        ObjectMapper objectMapper = new ObjectMapper();
        
        JsonNode expected = objectMapper.readTree(json);
        JsonNode actual = JsonNodeDeserializer.instance.deserialize(objectMapper.getFactory().createParser(json), null);

        assertEquals(expected, actual);
    }
    
    @Test
    public void testDeserialize_ArrayNode() throws JsonParseException, JsonMappingException, IOException {
        String json = "[1, 2, 3]";
        ObjectMapper objectMapper = new ObjectMapper();
        
        JsonNode expected = objectMapper.readTree(json);
        JsonNode actual = JsonNodeDeserializer.instance.deserialize(objectMapper.getFactory().createParser(json), null);

        assertEquals(expected, actual);
    }
    
    @Test
    public void testDeserialize_EmptyObjectNode() throws JsonParseException, JsonMappingException, IOException {
        String json = "{}";
        ObjectMapper objectMapper = new ObjectMapper();
        
        JsonNode expected = objectMapper.readTree(json);
        JsonNode actual = JsonNodeDeserializer.instance.deserialize(objectMapper.getFactory().createParser(json), null);

        assertEquals(expected, actual);
    }
    
    @Test
    public void testDeserialize_EmptyArrayNode() throws JsonParseException, JsonMappingException, IOException {
        String json = "[]";
        ObjectMapper objectMapper = new ObjectMapper();
        
        JsonNode expected = objectMapper.readTree(json);
        JsonNode actual = JsonNodeDeserializer.instance.deserialize(objectMapper.getFactory().createParser(json), null);

        assertEquals(expected, actual);
    }
    
    @Test
    public void testDeserialize_StringNode() throws JsonParseException, JsonMappingException, IOException {
        String json = "\"string\"";
        ObjectMapper objectMapper = new ObjectMapper();
        
        JsonNode expected = objectMapper.readTree(json);
        JsonNode actual = JsonNodeDeserializer.instance.deserialize(objectMapper.getFactory().createParser(json), null);

        assertEquals(expected, actual);
    }
    
    @Test
    public void testDeserialize_NumberNode() throws JsonParseException, JsonMappingException, IOException {
        String json = "123";
        ObjectMapper objectMapper = new ObjectMapper();
        
        JsonNode expected = objectMapper.readTree(json);
        JsonNode actual = JsonNodeDeserializer.instance.deserialize(objectMapper.getFactory().createParser(json), null);

        assertEquals(expected, actual);
    }
    
    @Test
    public void testDeserialize_BooleanNode() throws JsonParseException, JsonMappingException, IOException {
        String json = "true";
        ObjectMapper objectMapper = new ObjectMapper();
        
        JsonNode expected = objectMapper.readTree(json);
        JsonNode actual = JsonNodeDeserializer.instance.deserialize(objectMapper.getFactory().createParser(json), null);

        assertEquals(expected, actual);
    }
}