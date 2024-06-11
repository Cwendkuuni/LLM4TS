import static org.junit.Assert.*;
import org.junit.Test;
import org.mockito.Mockito;
import java.io.IOException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.node.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.node.ArrayNode;

public class JsonNodeDeserializerTest {

    private final JsonNodeDeserializer jsonNodeDeserializer = new JsonNodeDeserializer();

    @Test
    public void testGetNullValue() {
        DeserializationContext ctxt = Mockito.mock(DeserializationContext.class);
        
        JsonNode nullValue = jsonNodeDeserializer.getNullValue(ctxt);
        
        assertNotNull(nullValue);
        assertTrue(nullValue.isNull());
    }

    @Test
    public void testGetDeserializer() {
        JsonDeserializer<? extends JsonNode> deserializer = JsonNodeDeserializer.getDeserializer(ObjectNode.class);
        
        assertTrue(deserializer instanceof JsonNodeDeserializer.ObjectDeserializer);
    }

    @Test
    public void testDeserialize_ObjectNode() throws IOException {
        JsonParser jsonParser = Mockito.mock(JsonParser.class);
        DeserializationContext ctxt = Mockito.mock(DeserializationContext.class);
        
        ObjectNode objectNode = jsonNodeDeserializer.deserialize(jsonParser, ctxt);
        
        assertNotNull(objectNode);
        assertTrue(objectNode.isObject());
    }

    @Test
    public void testDeserialize_ArrayNode() throws IOException {
        JsonParser jsonParser = Mockito.mock(JsonParser.class);
        DeserializationContext ctxt = Mockito.mock(DeserializationContext.class);
        
        ArrayNode arrayNode = jsonNodeDeserializer.deserialize(jsonParser, ctxt);
        
        assertNotNull(arrayNode);
        assertTrue(arrayNode.isArray());
    }

    @Test
    public void testObjectDeserializer_Deserialize() throws IOException {
        JsonParser jsonParser = Mockito.mock(JsonParser.class);
        DeserializationContext ctxt = Mockito.mock(DeserializationContext.class);
        
        JsonNode jsonNode = new ObjectNode(null);
        
        ObjectNode objectNode = jsonNodeDeserializer.new ObjectDeserializer().deserialize(jsonParser, ctxt);
        
        assertNotNull(objectNode);
        assertEquals(jsonNode, objectNode);
    }

    @Test
    public void testArrayDeserializer_Deserialize() throws IOException {
        JsonParser jsonParser = Mockito.mock(JsonParser.class);
        DeserializationContext ctxt = Mockito.mock(DeserializationContext.class);
        
        JsonNode jsonNode = new ArrayNode(null);
        
        ArrayNode arrayNode = jsonNodeDeserializer.new ArrayDeserializer().deserialize(jsonParser, ctxt);
        
        assertNotNull(arrayNode);
        assertEquals(jsonNode, arrayNode);
    }

    @Test
    public void testIsCachable() {
        assertTrue(jsonNodeDeserializer.isCachable());
    }

    @Test
    public void testFromInt() throws IOException {
        JsonParser jsonParser = Mockito.mock(JsonParser.class);
        DeserializationContext ctxt = Mockito.mock(DeserializationContext.class);
        
        JsonNode intNode = jsonNodeDeserializer.new BaseNodeDeserializer<>(JsonNode.class)._fromInt(jsonParser, ctxt, JsonNodeFactory.instance);
        
        assertNotNull(intNode);
        assertTrue(intNode.isNumber());
    }

    @Test
    public void testFromFloat() throws IOException {
        JsonParser jsonParser = Mockito.mock(JsonParser.class);
        DeserializationContext ctxt = Mockito.mock(DeserializationContext.class);
        
        JsonNode floatNode = jsonNodeDeserializer.new BaseNodeDeserializer<>(JsonNode.class)._fromFloat(jsonParser, ctxt, JsonNodeFactory.instance);
        
        assertNotNull(floatNode);
        assertTrue(floatNode.isNumber());
    }

}