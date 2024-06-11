import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.node.*;
import org.junit.Test;

public class JsonNodeDeserializerTest {

    @Test
    public void testGetNullValue() throws JsonProcessingException {
        JsonNodeDeserializer deserializer = new JsonNodeDeserializer();
        JsonNode result = deserializer.getNullValue(new TestContext());
        assertEquals(JsonNode.Value.NULL, result.getValueAsToken());
    }

    @Test
    public void testGetNullValueDeprecated() throws JsonProcessingException {
        JsonNodeDeserializer deserializer = new JsonNodeDeserializer();
        JsonNode result = deserializer.getNullValue();
        assertEquals(JsonNode.Value.NULL, result.getValueAsToken());
    }

    @Test
    public void testDeserialize() throws IOException {
        JsonParser parser = new ObjectMapper().getFactory().createParser("{\"name\":\"John\"}");
        JsonNodeDeserializer deserializer = new JsonNodeDeserializer();
        JsonNode result = deserializer.deserialize(parser, new TestContext());
        ObjectNode expected = JsonNodeFactory.instance.objectNode().put("name", JsonNodeFactory.instance.textNode("John"));
        assertEquals(expected, result);
    }

    // Test cases for ObjectDeserializer and ArrayDeserializer can be added here or in separate test classes
}