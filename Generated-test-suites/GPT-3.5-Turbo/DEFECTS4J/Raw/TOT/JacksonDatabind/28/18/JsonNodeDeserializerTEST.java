import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;

public class JsonNodeDeserializerTest {
    
    private JsonNodeDeserializer jsonNodeDeserializer;
    private DeserializationContext deserializationContext;
    private ObjectMapper objectMapper;
    
    @Before
    public void setup() {
        jsonNodeDeserializer = new JsonNodeDeserializer();
        deserializationContext = new DeserializationContext(null, null, null);
        objectMapper = new ObjectMapper();
    }
    
    @Test
    public void testGetNullValue() throws IOException {
        JsonNode expected = null;
        JsonNode actual = jsonNodeDeserializer.getNullValue(deserializationContext);
        
        Assert.assertEquals(expected, actual);
    }
    
    @Test
    public void testDeserializeObject() throws IOException {
        String json = "{\"name\":\"John\",\"age\":30,\"car\":null}";
        JsonParser parser = objectMapper.getFactory().createParser(json);
        
        ObjectNode expected = objectMapper.readTree(json);
        ObjectNode actual = jsonNodeDeserializer.deserializeObject(parser, deserializationContext, objectMapper.getNodeFactory());
        
        Assert.assertEquals(expected, actual);
    }
    
    @Test
    public void testDeserializeArray() throws IOException {
        String json = "[{\"name\":\"John\", \"age\":30}, {\"name\":\"Tom\", \"age\":25}]";
        JsonParser parser = objectMapper.getFactory().createParser(json);
        
        JsonNode expected = objectMapper.readTree(json);
        JsonNode actual = jsonNodeDeserializer.deserializeArray(parser, deserializationContext, objectMapper.getNodeFactory());
        
        Assert.assertEquals(expected, actual);
    }
}