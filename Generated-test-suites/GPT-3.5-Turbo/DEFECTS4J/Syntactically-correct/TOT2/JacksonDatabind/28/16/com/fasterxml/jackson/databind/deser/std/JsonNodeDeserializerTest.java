import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.junit.Test;

import static org.junit.Assert.*;

public class JsonNodeDeserializerTest {

    @Test
    public void testGetNullValue() {
        DeserializationContext ctxt = new DeserializationContext(new ObjectMapper(), null);
        JsonNodeDeserializer deserializer = new JsonNodeDeserializer();
        assertEquals(deserializer.getNullValue(ctxt), NullNode.getInstance());
    }

    @Test
    public void testDeserializeForObjectStart() throws Exception {
        DeserializationContext ctxt = new DeserializationContext(new ObjectMapper(), null);
        JsonNodeDeserializer deserializer = new JsonNodeDeserializer();
        ObjectMapper mapper = new ObjectMapper();
        JsonParser jsonParser = mapper.getFactory().createParser("{\"test\": \"value\"}");
        jsonParser.nextToken(); // move to the start object token
        JsonNode node = deserializer.deserialize(jsonParser, ctxt);
        assertTrue(node.isObject());
        assertEquals("value", node.get("test").asText());
    }

    @Test
    public void testDeserializeForArrayStart() throws Exception {
        DeserializationContext ctxt = new DeserializationContext(new ObjectMapper(), null);
        JsonNodeDeserializer deserializer = new JsonNodeDeserializer();
        ObjectMapper mapper = new ObjectMapper();
        JsonParser jsonParser = mapper.getFactory().createParser("[1, 2, 3]");
        jsonParser.nextToken(); // move to the start array token
        JsonNode node = deserializer.deserialize(jsonParser, ctxt);
        assertTrue(node.isArray());
        assertEquals(3, node.size());
    }

    @Test
    public void testGetDeserializerForObjectNode() {
        JsonDeserializer<? extends JsonNode> deserializer = JsonNodeDeserializer.getDeserializer(ObjectNode.class);
        assertTrue(deserializer instanceof JsonNodeDeserializer.ObjectDeserializer);
    }

    @Test
    public void testGetDeserializerForArrayNode() {
        JsonDeserializer<? extends JsonNode> deserializer = JsonNodeDeserializer.getDeserializer(ArrayNode.class);
        assertTrue(deserializer instanceof JsonNodeDeserializer.ArrayDeserializer);
    }

}