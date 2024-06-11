import static org.junit.Assert.*;

import org.junit.Test;
import org.mockito.Mockito;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.deser.std.StringArrayDeserializer;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;

public class StringArrayDeserializerTest {

    @Test
    public void testDeserialize() throws Exception {
        StringArrayDeserializer deserializer = new StringArrayDeserializer();
        JsonParser mockParser = Mockito.mock(JsonParser.class);
        DeserializationContext mockContext = Mockito.mock(DeserializationContext.class);
        
        String[] result = deserializer.deserialize(mockParser, mockContext);
        
        assertNotNull(result);
        assertEquals(0, result.length);
    }

    @Test
    public void testDeserializeCustom() throws Exception {
        JsonParser mockParser = Mockito.mock(JsonParser.class);
        DeserializationContext mockContext = Mockito.mock(DeserializationContext.class);
        StdDeserializer<String> mockDeserializer = new StdDeserializer<String>(String.class) {
            @Override
            public String deserialize(JsonParser p, DeserializationContext ctxt) {
                return "mock";
            }
        };
        
        StringArrayDeserializer deserializer = new StringArrayDeserializer(mockDeserializer);
        String[] result = deserializer._deserializeCustom(mockParser, mockContext);
        
        assertNotNull(result);
        assertEquals(1, result.length);
        assertEquals("mock", result[0]);
    }

    @Test
    public void testDeserializeWithType() throws Exception {
        StringArrayDeserializer deserializer = new StringArrayDeserializer();
        JsonParser mockParser = Mockito.mock(JsonParser.class);
        DeserializationContext mockContext = Mockito.mock(DeserializationContext.class);
        JsonNode jsonNode = new ObjectMapper().createObjectNode();
        
        Object result = deserializer.deserializeWithType(mockParser, mockContext, jsonNode);
        
        assertNull(result);
    }

    @Test
    public void testHandleNonArray() throws Exception {
        StringArrayDeserializer deserializer = new StringArrayDeserializer();
        JsonParser mockParser = Mockito.mock(JsonParser.class);
        DeserializationContext mockContext = Mockito.mock(DeserializationContext.class);
        
        String[] result = deserializer.handleNonArray(mockParser, mockContext);
        
        assertNotNull(result);
        assertEquals(1, result.length);
        assertNull(result[0]);
    }

    @Test
    public void testCreateContextual() throws Exception {
        StringArrayDeserializer deserializer = new StringArrayDeserializer();
        DeserializationContext mockContext = Mockito.mock(DeserializationContext.class);
        JsonMappingException jsonMappingException = new JsonMappingException("test error");
        
        JsonDeserializer<?> result = deserializer.createContextual(mockContext, null);
        
        assertNull(result);
    }
}