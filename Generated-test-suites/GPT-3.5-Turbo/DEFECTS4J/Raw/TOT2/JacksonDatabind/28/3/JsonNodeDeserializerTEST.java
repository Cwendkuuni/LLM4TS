import static org.junit.Assert.*;

import java.io.IOException;

import org.junit.Test;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.deser.std.JsonNodeDeserializer;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.node.NullNode;

public class JsonNodeDeserializerTest {

    @Test
    public void testGetNullValue() {
        JsonNodeDeserializer deserializer = new JsonNodeDeserializer();
        JsonNode result = deserializer.getNullValue(null);
        assertNotNull(result);
        assertTrue(result instanceof NullNode);
    }

    @Test
    public void testGetNullValueDeprecated() {
        JsonNodeDeserializer deserializer = new JsonNodeDeserializer();
        JsonNode result = deserializer.getNullValue();
        assertNotNull(result);
        assertTrue(result instanceof NullNode);
    }

    @Test
    public void testDeserializeObject() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        String json = "{\"key\":\"value\"}";
        JsonNodeDeserializer deserializer = new JsonNodeDeserializer();
        JsonNode result = deserializer.deserialize(mapper.getFactory().createParser(json), mapper.getDeserializationContext());
        assertTrue(result instanceof ObjectNode);
        assertEquals("value", result.get("key").asText());
    }

    @Test
    public void testDeserializeArray() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        String json = "[1,2,3]";
        JsonNodeDeserializer deserializer = new JsonNodeDeserializer();
        JsonNode result = deserializer.deserialize(mapper.getFactory().createParser(json), mapper.getDeserializationContext());
        assertTrue(result instanceof ArrayNode);
        assertEquals(3, result.size());
    }

    @Test(expected = JsonParseException.class)
    public void testDeserializeException() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        String json = "{{}";
        JsonNodeDeserializer deserializer = new JsonNodeDeserializer();
        deserializer.deserialize(mapper.getFactory().createParser(json), mapper.getDeserializationContext());
    }

}