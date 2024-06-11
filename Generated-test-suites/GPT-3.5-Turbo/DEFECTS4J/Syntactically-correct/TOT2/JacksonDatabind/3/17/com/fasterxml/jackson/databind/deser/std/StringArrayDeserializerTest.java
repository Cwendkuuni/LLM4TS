import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import org.junit.Test;
import static org.junit.Assert.*;

public class StringArrayDeserializerTest {

    @Test
    public void testDeserialize() throws Exception {
        StringArrayDeserializer deserializer = new StringArrayDeserializer();
        
        JsonFactory jsonFactory = new JsonFactory();
        JsonParser parser = jsonFactory.createParser("[\"value1\", \"value2\"]");
        DeserializationContext ctxt = null; // Assuming DeserializationContext is properly initialized
        
        String[] result = deserializer.deserialize(parser, ctxt);
        
        assertNotNull(result);
        assertEquals(2, result.length);
        assertEquals("value1", result[0]);
        assertEquals("value2", result[1]);
    }

    @Test
    public void testDeserializeWithNulls() throws Exception {
        StringArrayDeserializer deserializer = new StringArrayDeserializer();
        
        JsonFactory jsonFactory = new JsonFactory();
        JsonParser parser = jsonFactory.createParser("[\"value1\", null, \"value2\"]");
        DeserializationContext ctxt = null; // Assuming DeserializationContext is properly initialized
        
        String[] result = deserializer.deserialize(parser, ctxt);
        
        assertNotNull(result);
        assertEquals(3, result.length);
        assertEquals("value1", result[0]);
        assertNull(result[1]);
        assertEquals("value2", result[2]);
    }

    @Test
    public void testDeserializeEmptyArray() throws Exception {
        StringArrayDeserializer deserializer = new StringArrayDeserializer();
        
        JsonFactory jsonFactory = new JsonFactory();
        JsonParser parser = jsonFactory.createParser("[]");
        DeserializationContext ctxt = null; // Assuming DeserializationContext is properly initialized
        
        String[] result = deserializer.deserialize(parser, ctxt);
        
        assertNotNull(result);
        assertEquals(0, result.length);
    }

    @Test
    public void testDeserializeWithType() throws Exception {
        StringArrayDeserializer deserializer = new StringArrayDeserializer();
        
        JsonFactory jsonFactory = new JsonFactory();
        JsonParser parser = jsonFactory.createParser("[\"value1\", \"value2\"]");
        DeserializationContext ctxt = null; // Assuming DeserializationContext is properly initialized
        // TypeDeserializer typeDeserializer = null; // Assuming TypeDeserializer is properly initialized
        
        Object result = deserializer.deserializeWithType(parser, ctxt, typeDeserializer);
        
        assertNotNull(result);
        assertTrue(result instanceof String[]);
    }

    @Test
    public void testHandleNonArray() throws Exception {
        StringArrayDeserializer deserializer = new StringArrayDeserializer();
        
        JsonFactory jsonFactory = new JsonFactory();
        JsonParser parser = jsonFactory.createParser("\"singleValue\"");
        DeserializationContext ctxt = null; // Assuming DeserializationContext is properly initialized
        
        String[] result = deserializer.handleNonArray(parser, ctxt);
        
        assertNotNull(result);
        assertEquals(1, result.length);
        assertEquals("singleValue", result[0]);
    }

    @Test
    public void testCreateContextual() throws Exception {
        StringArrayDeserializer deserializer = new StringArrayDeserializer();
        
        DeserializationContext ctxt = null; // Assuming DeserializationContext is properly initialized
        BeanProperty property = null; // Assuming BeanProperty is properly initialized
        
        JsonDeserializer<?> result = deserializer.createContextual(ctxt, property);
        
        assertNotNull(result);
        assertTrue(result instanceof StringArrayDeserializer);
    }
}