import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;

public class JsonNodeDeserializerTest {
    
    @Test
    public void testGetNullValue() {
        JsonNodeDeserializer deserializer = new JsonNodeDeserializer();
        JsonNode result = deserializer.getNullValue();
        assertNotNull(result);
    }
    
    @Test
    public void testDeserialize() throws IOException {
        JsonNodeDeserializer deserializer = new JsonNodeDeserializer();
        JsonParser parser = new JsonParser();
        DeserializationContext context = new DeserializationContext();
        JsonNode result = deserializer.deserialize(parser, context);
        assertNotNull(result);
    }
    
    @Test
    public void testObjectDeserializerDeserialize() throws IOException {
        JsonNodeDeserializer.ObjectDeserializer deserializer = new JsonNodeDeserializer.ObjectDeserializer();
        JsonParser parser = new JsonParser();
        DeserializationContext context = new DeserializationContext();
        JsonNode result = deserializer.deserialize(parser, context);
        assertNotNull(result);
    }
    
    @Test
    public void testArrayDeserializerDeserialize() throws IOException {
        JsonNodeDeserializer.ArrayDeserializer deserializer = new JsonNodeDeserializer.ArrayDeserializer();
        JsonParser parser = new JsonParser();
        DeserializationContext context = new DeserializationContext();
        JsonNode result = deserializer.deserialize(parser, context);
        assertNotNull(result);
    }

}