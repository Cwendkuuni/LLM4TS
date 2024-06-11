import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class JsonNodeDeserializerTest {

    private ObjectMapper mapper = new ObjectMapper();

    @Test
    public void testGetNullValue() {
        JsonNodeDeserializer deserializer = new JsonNodeDeserializer();
        JsonNode nullValue = deserializer.getNullValue();
        
        assertNotNull(nullValue);
        assertTrue(nullValue.isNull());
    }

    @Test
    public void testDeserializeObject() throws Exception {
        JsonParser parser = new JsonFactory().createParser("{\"key\":\"value\"}");
        DeserializationContext ctxt = mapper.getDeserializationContext();
        JsonNodeDeserializer deserializer = new JsonNodeDeserializer();
        
        JsonNode result = deserializer.deserialize(parser, ctxt);
        
        assertEquals("value", result.get("key").asText());
    }

    @Test
    public void testDeserializeArray() throws Exception {
        JsonParser parser = new JsonFactory().createParser("[1,2,3]");
        DeserializationContext ctxt = mapper.deserializationContext();
        JsonNodeDeserializer deserializer = new JsonNodeDeserializer();
        
        JsonNode result = deserializer.deserialize(parser, ctxt);
        
        assertEquals(3, result.size());
        assertTrue(result.isArray());
    }
    
    @Test
    public void testGetDeserializer_ObjectNode() {
        Class<?> nodeClass = ObjectNode.class;
        JsonDeserializer<? extends JsonNode> deserializer = JsonNodeDeserializer.getDeserializer(nodeClass);
        
        assertTrue(deserializer instanceof JsonNodeDeserializer.ObjectDeserializer);
    }

    @Test
    public void testGetDeserializer_ArrayNode() {
        Class<?> nodeClass = ArrayNode.class;
        JsonDeserializer<? extends JsonNode> deserializer = JsonNodeDeserializer.getDeserializer(nodeClass);
        
        assertTrue(deserializer instanceof JsonNodeDeserializer.ArrayDeserializer);
    }
}