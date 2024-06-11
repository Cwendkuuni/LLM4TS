import org.junit.Test;
import static org.junit.Assert.*;

public class JsonNodeDeserializerTest {

    @Test
    public void testGetNullValue() {
        JsonNodeDeserializer deserializer = new JsonNodeDeserializer();
        JsonNode nullValue = deserializer.getNullValue(null);
        assertNotNull(nullValue);
        assertEquals(NullNode.getInstance(), nullValue);
    }

    @Test
    public void testDeserialize() throws IOException {
        JsonNodeDeserializer deserializer = new JsonNodeDeserializer();
        JsonParser parser = new JsonFactory().createJsonParser("{\"key\":\"value\"}");
        DeserializationContext ctxt = new DefaultDeserializationContext(null, null, null);
        JsonNode result = deserializer.deserialize(parser, ctxt);
        assertNotNull(result);
        assertTrue(result instanceof ObjectNode);
    }

    @Test
    public void testDeserializeObject() throws IOException {
        JsonNodeDeserializer deserializer = new JsonNodeDeserializer();
        JsonFactory jsonFactory = new JsonFactory();
        JsonParser parser = jsonFactory.createJsonParser("{\"key\":\"value\"}");
        DeserializationContext ctxt = new DefaultDeserializationContext(null, null, null);
        JsonNodeFactory nodeFactory = new JsonNodeFactory(null);
        JsonNode result = deserializer.deserializeObject(parser, ctxt, nodeFactory);
        assertNotNull(result);
        assertTrue(result instanceof ObjectNode);
        assertEquals("value", result.get("key").asText());
    }

    @Test
    public void testDeserializeArray() throws IOException {
        JsonNodeDeserializer deserializer = new JsonNodeDeserializer();
        JsonFactory jsonFactory = new JsonFactory();
        JsonParser parser = jsonFactory.createJsonParser("[1,2,3]");
        DeserializationContext ctxt = new DefaultDeserializationContext(null, null, null);
        JsonNodeFactory nodeFactory = new JsonNodeFactory(null);
        JsonNode result = deserializer.deserializeArray(parser, ctxt, nodeFactory);
        assertNotNull(result);
        assertTrue(result instanceof ArrayNode);
        assertEquals(3, result.size());
        assertEquals(1, result.get(0).asInt());
        assertEquals(2, result.get(1).asInt());
        assertEquals(3, result.get(2).asInt());
    }

    @Test
    public void testFromInt() throws IOException {
        JsonNodeDeserializer deserializer = new JsonNodeDeserializer();
        JsonNodeFactory nodeFactory = new JsonNodeFactory(null);
        JsonParser parser = new JsonFactory().createJsonParser("123");
        DeserializationContext ctxt = new DefaultDeserializationContext(null, null, null);
        JsonNode result = deserializer._fromInt(parser, ctxt, nodeFactory);
        assertNotNull(result);
        assertTrue(result instanceof IntNode);
        assertEquals(123, result.asInt());
    }

    @Test
    public void testFromFloat() throws IOException {
        JsonNodeDeserializer deserializer = new JsonNodeDeserializer();
        JsonNodeFactory nodeFactory = new JsonNodeFactory(null);
        JsonParser parser = new JsonFactory().createJsonParser("12.3");
        DeserializationContext ctxt = new DefaultDeserializationContext(null, null, null);
        JsonNode result = deserializer._fromFloat(parser, ctxt, nodeFactory);
        assertNotNull(result);
        assertTrue(result instanceof DoubleNode);
        assertEquals(12.3, result.asDouble(), 0.0001);
    }

    @Test
    public void testFromEmbedded() throws IOException {
        JsonNodeDeserializer deserializer = new JsonNodeDeserializer();
        JsonNodeFactory nodeFactory = new JsonNodeFactory(null);
        JsonParser parser = new JsonFactory().createJsonParser("\"text\"");
        DeserializationContext ctxt = new DefaultDeserializationContext(null, null, null);
        JsonNode result = deserializer._fromEmbedded(parser, ctxt, nodeFactory);
        assertNotNull(result);
        assertTrue(result instanceof TextNode);
        assertEquals("text", result.asText());
    }
}