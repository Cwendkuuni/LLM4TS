import static org.junit.Assert.*;
import java.io.IOException;
import org.junit.Test;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.ObjectMapper;

public class StringArrayDeserializerTest {
    
    private ObjectMapper mapper = new ObjectMapper();
    
    @Test
    public void testDeserialize_TypicalCase() throws JsonParseException, IOException {
        String json = "[\"value1\", \"value2\", \"value3\"]";
        
        JsonParser jp = mapper.getFactory().createParser(json);
        jp.nextToken(); // Start array
        String[] result = StringArrayDeserializer.instance.deserialize(jp, DeserializationContexts.mockContext());
        
        assertArrayEquals(new String[] { "value1", "value2", "value3" }, result);
    }
    
    @Test
    public void testDeserialize_EmptyArray() throws JsonParseException, IOException {
        String json = "[]";
        
        JsonParser jp = mapper.getFactory().createParser(json);
        jp.nextToken(); // Start array
        String[] result = StringArrayDeserializer.instance.deserialize(jp, DeserializationContexts.mockContext());
        
        assertArrayEquals(new String[] {}, result);
    }
    
    @Test
    public void testDeserialize_NullArray() throws JsonParseException, IOException {
        String json = "null";
        
        JsonParser jp = mapper.getFactory().createParser(json);
        jp.nextToken(); // Start array
        String[] result = StringArrayDeserializer.instance.deserialize(jp, DeserializationContexts.mockContext());
        
        assertNull(result);
    }
    
    @Test
    public void testDeserialize_NonArrayInput() throws JsonParseException, IOException {
        String json = "\"value\"";
        
        JsonParser jp = mapper.getFactory().createParser(json);
        
        try {
            StringArrayDeserializer.instance.deserialize(jp, DeserializationContexts.mockContext());
            fail("Expected IOException");
        } catch (IOException e) {
            assertEquals("Expected START_ARRAY token, found VALUE_STRING", e.getMessage());
        }
    }
    
    @Test
    public void testDeserialize_NullValue() throws JsonParseException, IOException {
        String json = "[null]";
        
        JsonParser jp = mapper.getFactory().createParser(json);
        jp.nextToken(); // Start array
        String[] result = StringArrayDeserializer.instance.deserialize(jp, DeserializationContexts.mockContext());
        
        assertArrayEquals(new String[] { null }, result);
    }
    
    @Test
    public void testDeserialize_WithCustomElementDeserializer() throws JsonParseException, IOException {
        String json = "[\"value1\", \"value2\", \"value3\"]";
        
        JsonParser jp = mapper.getFactory().createParser(json);
        jp.nextToken(); // Start array
        String[] result = StringArrayDeserializer.instance.deserialize(jp, DeserializationContexts.mockContext());
        
        assertArrayEquals(new String[] { "custom_value1", "custom_value2", "custom_value3" }, result);
    }
    
    @Test
    public void testDeserialize_WithEmptyStringAsNull() throws JsonParseException, IOException {
        String json = "[\"\"]";
        
        JsonParser jp = mapper.getFactory().createParser(json);
        jp.nextToken(); // Start array
        DeserializationContext ctxt = DeserializationContexts.mockContext();
        ctxt.enable(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT);
        String[] result = StringArrayDeserializer.instance.deserialize(jp, ctxt);
        
        assertNull(result);
    }
    
    @Test
    public void testDeserialize_Failure_InvalidInput() throws JsonParseException, IOException {
        String json = "{\"key\": \"value\"}";
        
        JsonParser jp = mapper.getFactory().createParser(json);
        jp.nextToken(); // Start object
        
        try {
            StringArrayDeserializer.instance.deserialize(jp, DeserializationContexts.mockContext());
            fail("Expected IOException");
        } catch (IOException e) {
            assertEquals("Expected START_ARRAY token, found START_OBJECT", e.getMessage());
        }
    }
    
    @Test
    public void testDeserializeWithType() throws JsonParseException, IOException {
        String json = "[\"value1\", \"value2\", \"value3\"]";
        
        JsonParser jp = mapper.getFactory().createParser(json);
        jp.nextToken(); // Start array
        Object result = StringArrayDeserializer.instance.deserializeWithType(jp, DeserializationContexts.mockContext(), null);
        
        assertArrayEquals(new String[] { "value1", "value2", "value3" }, (String[]) result);
    }
}