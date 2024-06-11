import static org.junit.Assert.*;
import org.junit.Test;

public class JsonNodeDeserializerTest {
    
    @Test
    public void testGetDeserializer_ObjectNode() {
        JsonDeserializer<? extends JsonNode> deserializer = JsonNodeDeserializer.getDeserializer(ObjectNode.class);
        assertTrue(deserializer instanceof JsonNodeDeserializer.ObjectDeserializer);
    }
    
    @Test
    public void testGetDeserializer_ArrayNode() {
        JsonDeserializer<? extends JsonNode> deserializer = JsonNodeDeserializer.getDeserializer(ArrayNode.class);
        assertTrue(deserializer instanceof JsonNodeDeserializer.ArrayDeserializer);
    }
    
    @Test
    public void testGetDeserializer_OtherClass() {
        JsonDeserializer<? extends JsonNode> deserializer = JsonNodeDeserializer.getDeserializer(JsonNode.class);
        assertTrue(deserializer instanceof JsonNodeDeserializer);
    }
    
    @Test
    public void testGetNullValue() {
        JsonNodeDeserializer deserializer = new JsonNodeDeserializer();
        JsonNode nullValue = deserializer.getNullValue();
        assertTrue(nullValue instanceof NullNode);
    }
    
    @Test
    public void testDeserialize_StartObject() throws IOException {
        JsonNodeDeserializer deserializer = new JsonNodeDeserializer();
        JsonParser parser = Mockito.mock(JsonParser.class);
        DeserializationContext ctxt = Mockito.mock(DeserializationContext.class);
        JsonNodeFactory nodeFactory = Mockito.mock(JsonNodeFactory.class);
        Mockito.when(parser.getCurrentTokenId()).thenReturn(JsonTokenId.ID_START_OBJECT);
        JsonNode result = deserializer.deserialize(parser, ctxt);
        assertTrue(result instanceof ObjectNode);
    }
    
    @Test
    public void testDeserialize_StartArray() throws IOException {
        JsonNodeDeserializer deserializer = new JsonNodeDeserializer();
        JsonParser parser = Mockito.mock(JsonParser.class);
        DeserializationContext ctxt = Mockito.mock(DeserializationContext.class);
        JsonNodeFactory nodeFactory = Mockito.mock(JsonNodeFactory.class);
        Mockito.when(parser.getCurrentTokenId()).thenReturn(JsonTokenId.ID_START_ARRAY);
        JsonNode result = deserializer.deserialize(parser, ctxt);
        assertTrue(result instanceof ArrayNode);
    }
    
    @Test
    public void testDeserialize_OtherToken() throws IOException {
        JsonNodeDeserializer deserializer = new JsonNodeDeserializer();
        JsonParser parser = Mockito.mock(JsonParser.class);
        DeserializationContext ctxt = Mockito.mock(DeserializationContext.class);
        JsonNodeFactory nodeFactory = Mockito.mock(JsonNodeFactory.class);
        Mockito.when(parser.getCurrentTokenId()).thenReturn(JsonTokenId.ID_STRING);
        JsonNode result = deserializer.deserialize(parser, ctxt);
        assertTrue(result instanceof JsonNode);
    }
    
    @Test
    public void testObjectDeserializer_Deserialize() throws IOException {
        JsonNodeDeserializer.ObjectDeserializer deserializer = JsonNodeDeserializer.ObjectDeserializer.getInstance();
        JsonParser parser = Mockito.mock(JsonParser.class);
        DeserializationContext ctxt = Mockito.mock(DeserializationContext.class);
        JsonNodeFactory nodeFactory = Mockito.mock(JsonNodeFactory.class);
        Mockito.when(parser.getCurrentToken()).thenReturn(JsonToken.START_OBJECT);
        JsonNode result = deserializer.deserialize(parser, ctxt);
        assertTrue(result instanceof ObjectNode);
    }
    
    @Test
    public void testArrayDeserializer_Deserialize() throws IOException {
        JsonNodeDeserializer.ArrayDeserializer deserializer = JsonNodeDeserializer.ArrayDeserializer.getInstance();
        JsonParser parser = Mockito.mock(JsonParser.class);
        DeserializationContext ctxt = Mockito.mock(DeserializationContext.class);
        JsonNodeFactory nodeFactory = Mockito.mock(JsonNodeFactory.class);
        Mockito.when(parser.isExpectedStartArrayToken()).thenReturn(true);
        JsonNode result = deserializer.deserialize(parser, ctxt);
        assertTrue(result instanceof ArrayNode);
    }

}