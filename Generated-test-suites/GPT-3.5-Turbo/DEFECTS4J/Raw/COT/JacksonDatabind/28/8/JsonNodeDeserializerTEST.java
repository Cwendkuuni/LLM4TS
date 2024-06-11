import static org.junit.Assert.*;
import org.junit.Test;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.node.*;

public class JsonNodeDeserializerTest {

    private JsonNodeDeserializer deserializer = JsonNodeDeserializer.getDeserializer(JsonNode.class);
    private DeserializationContext ctxt = new JsonMappingContext(null, null, null).getDeserializationContext();
    private JsonNodeFactory nodeFactory = JsonNodeFactory.instance;

    @Test
    public void testDeserialize_NullValue() throws Exception {
        JsonParser p = ctxt.getFactory().createParser("null");
        JsonNode result = deserializer.deserialize(p, ctxt);
        assertEquals(NullNode.getInstance(), result);
    }

    @Test
    public void testDeserialize_ObjectNode() throws Exception {
        JsonParser p = ctxt.getFactory().createParser("{\"name\": \"John\", \"age\": 30}");
        JsonNode result = deserializer.deserialize(p, ctxt);
        assertEquals(ObjectNode.class, result.getClass());
        assertEquals("{\"name\":\"John\",\"age\":30}", result.toString());
    }

    @Test
    public void testDeserialize_ArrayNode() throws Exception {
        JsonParser p = ctxt.getFactory().createParser("[1,2,3]");
        JsonNode result = deserializer.deserialize(p, ctxt);
        assertEquals(ArrayNode.class, result.getClass());
        assertEquals("[1,2,3]", result.toString());
    }

    // Edge cases and exception handling scenarios
    @Test
    public void testDeserialize_EmptyObject() throws Exception {
        JsonParser p = ctxt.getFactory().createParser("{}");
        JsonNode result = deserializer.deserialize(p, ctxt);
        assertEquals(ObjectNode.class, result.getClass());
        assertTrue(result.isEmpty());
    }

    @Test(expected = JsonMappingException.class)
    public void testDeserialize_InvalidJson() throws Exception {
        JsonParser p = ctxt.getFactory().createParser("invalid");
        deserializer.deserialize(p, ctxt);
    }

    @Test
    public void testDeserialize_EmptyArray() throws Exception {
        JsonParser p = ctxt.getFactory().createParser("[]");
        JsonNode result = deserializer.deserialize(p, ctxt);
        assertEquals(ArrayNode.class, result.getClass());
        assertTrue(result.isEmpty());
    }

    @Test
    public void testDeserialize_StringNode() throws Exception {
        JsonParser p = ctxt.getFactory().createParser("\"Hello World!\"");
        JsonNode result = deserializer.deserialize(p, ctxt);
        assertEquals(TextNode.class, result.getClass());
        assertEquals("Hello World!", result.asText());
    }

    @Test
    public void testDeserialize_IntNode() throws Exception {
        JsonParser p = ctxt.getFactory().createParser("5");
        JsonNode result = deserializer.deserialize(p, ctxt);
        assertEquals(IntNode.class, result.getClass());
        assertEquals(5, result.asInt());
    }

    @Test
    public void testDeserialize_LongNode() throws Exception {
        JsonParser p = ctxt.getFactory().createParser("10000000000");
        JsonNode result = deserializer.deserialize(p, ctxt);
        assertEquals(LongNode.class, result.getClass());
        assertEquals(10000000000L, result.asLong());
    }

    @Test
    public void testDeserialize_BooleanNode() throws Exception {
        JsonParser p = ctxt.getFactory().createParser("true");
        JsonNode result = deserializer.deserialize(p, ctxt);
        assertEquals(BooleanNode.class, result.getClass());
        assertTrue(result.asBoolean());
    }

    @Test
    public void testDeserialize_EmbeddedObject() throws Exception {
        RawValue rawValue = new RawValue("true");
        JsonParser p = ctxt.getFactory().createParser("true");
        p.setEmbeddedObject(rawValue);
        JsonNode result = deserializer.deserialize(p, ctxt);
        assertEquals(BooleanNode.class, result.getClass());
        assertTrue(result.asBoolean());
    }

}