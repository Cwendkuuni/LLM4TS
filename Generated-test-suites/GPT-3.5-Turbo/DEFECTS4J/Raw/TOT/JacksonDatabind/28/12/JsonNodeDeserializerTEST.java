import org.junit.Test;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectMapper.*;
import static org.junit.Assert.*;
import java.io.IOException;
import java.io.IOException;


public class JsonNodeDeserializerTest {
    
    @Test
    public void testGetNullValue() {
        JsonNodeDeserializer jsonNodeDeserializer = new JsonNodeDeserializer();
        JsonNode expected = NullNode.getInstance();
        JsonNode actual = jsonNodeDeserializer.getNullValue();
        assertEquals(expected, actual);
    }
    
    @Test
    public void testDeserialize_ObjectNode() {
        JsonParser p = new JsonFactory().createParser("{\"key\":\"value\"}");
        DeserializationContext ctxt = new ObjectMapper().getDeserializationConfig().getContext();
        JsonNodeFactory nodeFactory = ctxt.getNodeFactory();
        JsonNode expected = nodeFactory.objectNode().put("key", "value");
        JsonNode actual = jsonNodeDeserializer.deserialize(p, ctxt);
        assertEquals(expected, actual);
    }
    
    @Test
    public void testDeserialize_EmptyObjectNode() {
        JsonParser p = new JsonFactory().createParser("{}");
        DeserializationContext ctxt = new ObjectMapper().getDeserializationConfig().getContext();
        JsonNodeFactory nodeFactory = ctxt.getNodeFactory();
        JsonNode expected = nodeFactory.objectNode();
        JsonNode actual = jsonNodeDeserializer.deserialize(p, ctxt);
        assertEquals(expected, actual);
    }
    
    @Test
    public void testDeserialize_ArrayNode() {
        JsonParser p = new JsonFactory().createParser("[\"a\", \"b\", \"c\"]");
        DeserializationContext ctxt = new ObjectMapper().getDeserializationConfig().getContext();
        JsonNodeFactory nodeFactory = ctxt.getNodeFactory();
        JsonNode expected = nodeFactory.arrayNode().add("a").add("b").add("c");
        JsonNode actual = jsonNodeDeserializer.deserialize(p, ctxt);
        assertEquals(expected, actual);
    }
    
    @Test
    public void testDeserialize_EmptyArrayNode() {
        JsonParser p = new JsonFactory().createParser("[]");
        DeserializationContext ctxt = new ObjectMapper().getDeserializationConfig().getContext();
        JsonNodeFactory nodeFactory = ctxt.getNodeFactory();
        JsonNode expected = nodeFactory.arrayNode();
        JsonNode actual = jsonNodeDeserializer.deserialize(p, ctxt);
        assertEquals(expected, actual);
    }
    
    @Test
    public void testDeserialize_AnyNode() {
        JsonParser p = new JsonFactory().createParser("true");
        DeserializationContext ctxt = new ObjectMapper().getDeserializationConfig().getContext();
        JsonNodeFactory nodeFactory = ctxt.getNodeFactory();
        JsonNode expected = nodeFactory.booleanNode(true);
        JsonNode actual = jsonNodeDeserializer.deserialize(p, ctxt);
        assertEquals(expected, actual);
    }
    
    @Test(expected=IOException.class)
    public void testDeserialize_IOException() throws IOException{
        JsonParser p = new JsonFactory().createParser("{");
        DeserializationContext ctxt = new ObjectMapper().getDeserializationConfig().getContext();
        JsonNodeFactory nodeFactory = ctxt.getNodeFactory();
        JsonNode actual = jsonNodeDeserializer.deserialize(p, ctxt);
    }
}