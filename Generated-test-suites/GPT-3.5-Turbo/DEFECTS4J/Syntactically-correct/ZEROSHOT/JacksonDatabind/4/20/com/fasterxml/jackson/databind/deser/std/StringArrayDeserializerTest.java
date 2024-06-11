import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;

public class StringArrayDeserializerTest {
    
    private StringArrayDeserializer stringArrayDeserializer;
    
    @Before
    public void setUp() {
        stringArrayDeserializer = new StringArrayDeserializer();
    }
    
    @Test
    public void testDeserialize() throws Exception {
        String json = "[\"value1\", \"value2\", \"value3\"]";
        ObjectMapper mapper = new ObjectMapper();
        JsonParser jp = mapper.getFactory().createParser(json);
        DeserializationContext ctxt = mapper.getDeserializationContext();
        
        String[] result = stringArrayDeserializer.deserialize(jp, ctxt);
        
        assertNotNull(result);
        assertEquals(3, result.length);
        assertEquals("value1", result[0]);
        assertEquals("value2", result[1]);
        assertEquals("value3", result[2]);
    }
    
    @Test
    public void testHandleNonArray() throws Exception {
        String json = "\"value\"";
        ObjectMapper mapper = new ObjectMapper();
        JsonParser jp = mapper.getFactory().createParser(json);
        DeserializationContext ctxt = mapper.getDeserializationContext();
        
        String[] result = stringArrayDeserializer.handleNonArray(jp, ctxt);
        
        assertNotNull(result);
        assertEquals(1, result.length);
        assertEquals("value", result[0]);
    }
    
    @Test
    public void testDeserializeWithType() throws Exception {
        String json = "[\"value1\", \"value2\", \"value3\"]";
        ObjectMapper mapper = new ObjectMapper();
        JsonParser jp = mapper.getFactory().createParser(json);
        DeserializationContext ctxt = mapper.getDeserializationContext();
        TypeDeserializer typeDeserializer = mapper.getTypeFactory().findTypeDeserializer(ctxt.getConfig(), ctxt.constructType(String[].class));
        
        Object result = stringArrayDeserializer.deserializeWithType(jp, ctxt, typeDeserializer);
        
        assertTrue(result instanceof String[]);
        String[] arrayResult = (String[]) result;
        assertEquals(3, arrayResult.length);
        assertEquals("value1", arrayResult[0]);
        assertEquals("value2", arrayResult[1]);
        assertEquals("value3", arrayResult[2]);
    }
    
    @Test
    public void testCreateContextual() throws Exception {
        JsonDeserializer<?> deser = stringArrayDeserializer.createContextual(null, null);
        
        assertNull(deser);
    }
}