import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;

public class JsonNodeDeserializerTest {

    private final JsonFactory jsonFactory = new JsonFactory();
    private final DeserializationContext deserializationContext = new DeserializationContext(null, null, null);
    private final JsonNodeFactory jsonNodeFactory = JsonNodeFactory.instance;

    private final JsonNodeDeserializer jsonNodeDeserializer = new JsonNodeDeserializer();

    @Test
    public void testDeserialize_ObjectNode() throws IOException {
        JsonParser jsonParser = jsonFactory.createParser("{\"name\": \"John\", \"age\": 30}");
        JsonDeserializer<? extends JsonNode> deserializer = jsonNodeDeserializer.getDeserializer(ObjectNode.class);
        JsonNode result = deserializer.deserialize(jsonParser, deserializationContext);

        Assert.assertTrue(result instanceof ObjectNode);
        Assert.assertEquals("{\"name\":\"John\",\"age\":30}", result.toString());
    }

    @Test
    public void testDeserialize_ArrayNode() throws IOException {
        JsonParser jsonParser = jsonFactory.createParser("[1, 2, 3]");
        JsonDeserializer<? extends JsonNode> deserializer = jsonNodeDeserializer.getDeserializer(ArrayNode.class);
        JsonNode result = deserializer.deserialize(jsonParser, deserializationContext);

        Assert.assertTrue(result instanceof ArrayNode);
        Assert.assertEquals("[1,2,3]", result.toString());
    }

    @Test
    public void testDeserialize_NullNode() throws IOException {
        JsonParser jsonParser = jsonFactory.createParser("null");
        JsonNode result = jsonNodeDeserializer.deserialize(jsonParser, deserializationContext);

        Assert.assertTrue(result instanceof NullNode);
        Assert.assertEquals("null", result.toString());
    }

    @Test
    public void testDeserialize_AnyNode_ObjectNode() throws IOException {
        JsonParser jsonParser = jsonFactory.createParser("{\"name\": \"John\", \"age\": 30}");
        JsonNode result = jsonNodeDeserializer.deserializeAny(jsonParser, deserializationContext, jsonNodeFactory);

        Assert.assertTrue(result instanceof ObjectNode);
        Assert.assertEquals("{\"name\":\"John\",\"age\":30}", result.toString());
    }

    @Test
    public void testDeserialize_AnyNode_ArrayNode() throws IOException {
        JsonParser jsonParser = jsonFactory.createParser("[1, 2, 3]");
        JsonNode result = jsonNodeDeserializer.deserializeAny(jsonParser, deserializationContext, jsonNodeFactory);

        Assert.assertTrue(result instanceof ArrayNode);
        Assert.assertEquals("[1,2,3]", result.toString());
    }

    @Test
    public void testDeserialize_AnyNode_NullNode() throws IOException {
        JsonParser jsonParser = jsonFactory.createParser("null");
        JsonNode result = jsonNodeDeserializer.deserializeAny(jsonParser, deserializationContext, jsonNodeFactory);

        Assert.assertTrue(result instanceof NullNode);
        Assert.assertEquals("null", result.toString());
    }

    @Test(expected = IOException.class)
    public void testDeserialize_AnyNode_InvalidToken() throws IOException {
        JsonParser jsonParser = jsonFactory.createParser("123");
        jsonNodeDeserializer.deserializeAny(jsonParser, deserializationContext, jsonNodeFactory);
    }

}