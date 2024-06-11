import static org.junit.Assert.*;
import org.junit.Test;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;

public class JsonNodeDeserializerTest {

    @Test
    public void testGetNullValue() {
        JsonNodeDeserializer deserializer = new JsonNodeDeserializer();
        assertNotNull(deserializer.getNullValue(null));
    }

    @Test
    public void testDeserialize_ObjectNode() throws Exception {
        JsonFactory factory = new JsonFactory();
        JsonParser parser = factory.createParser("{\"key\": \"value\"}");
        ObjectMapper mapper = new ObjectMapper();
        JsonNodeDeserializer deserializer = new JsonNodeDeserializer();
        deserializer.setObjectMapper(mapper);
        ObjectNode node = deserializer.deserialize(parser, null);
        assertNotNull(node);
        assertTrue(node.isObject());
        assertEquals("value", node.get("key").asText());
    }

    @Test
    public void testDeserialize_ArrayNode() throws Exception {
        JsonFactory factory = new JsonFactory();
        JsonParser parser = factory.createParser("[1, 2, 3]");
        ObjectMapper mapper = new ObjectMapper();
        JsonNodeDeserializer deserializer = new JsonNodeDeserializer();
        deserializer.setObjectMapper(mapper);
        ArrayNode node = deserializer.deserialize(parser, null);
        assertNotNull(node);
        assertTrue(node.isArray());
        assertEquals(3, node.size());
        assertEquals(1, node.get(0).asInt());
        assertEquals(2, node.get(1).asInt());
        assertEquals(3, node.get(2).asInt());
    }

    @Test
    public void testDeserialize_InvalidJson() throws Exception {
        JsonFactory factory = new JsonFactory();
        JsonParser parser = factory.createParser("{invalid}");
        ObjectMapper mapper = new ObjectMapper();
        JsonNodeDeserializer deserializer = new JsonNodeDeserializer();
        deserializer.setObjectMapper(mapper);
        try {
            deserializer.deserialize(parser, null);
            fail("Expected IOException to be thrown");
        } catch (IOException e) {
            assertEquals("Unexpected character ('i' (code 105)): was expecting double-quote to start field name\n" +
                    " at [Source: (String)\"{invalid}\"; line: 1, column: 2]", e.getMessage());
        }
    }

    @Test
    public void testGetDeserializer_ObjectNode() {
        Class<?> nodeClass = ObjectNode.class;
        JsonDeserializer<?> deserializer = JsonNodeDeserializer.getDeserializer(nodeClass);
        assertNotNull(deserializer);
        assertTrue(deserializer instanceof JsonNodeDeserializer.ObjectDeserializer);
    }

    @Test
    public void testGetDeserializer_ArrayNode() {
        Class<?> nodeClass = ArrayNode.class;
        JsonDeserializer<?> deserializer = JsonNodeDeserializer.getDeserializer(nodeClass);
        assertNotNull(deserializer);
        assertTrue(deserializer instanceof JsonNodeDeserializer.ArrayDeserializer);
    }

    @Test
    public void testGetDeserializer_OtherNode() {
        Class<?> nodeClass = JsonNode.class;
        JsonDeserializer<?> deserializer = JsonNodeDeserializer.getDeserializer(nodeClass);
        assertNotNull(deserializer);
        assertTrue(deserializer instanceof JsonNodeDeserializer);
    }
}