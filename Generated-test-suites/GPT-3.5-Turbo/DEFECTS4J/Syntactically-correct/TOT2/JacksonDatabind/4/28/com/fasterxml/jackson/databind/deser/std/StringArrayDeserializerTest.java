import static org.junit.Assert.*;

import org.junit.Test;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.std.StringArrayDeserializer;

public class StringArrayDeserializerTest {

    @Test
    public void testDeserialize() throws Exception {
        StringArrayDeserializer deserializer = new StringArrayDeserializer();
        JsonParser jp = new JsonFactory().createParser("[\"test1\", null, \"test2\"]");
        DeserializationContext ctxt = new DefaultDeserializationContext.Impl(new ObjectMapper(), new DefaultDeserializationConfig());
        
        String[] result = deserializer.deserialize(jp, ctxt);
        
        assertNotNull(result);
        assertEquals(3, result.length);
        assertEquals("test1", result[0]);
        assertNull(result[1]);
        assertEquals("test2", result[2]);
    }
    
    @Test
    public void testDeserializeWithType() throws Exception {
        StringArrayDeserializer deserializer = new StringArrayDeserializer();
        JsonParser jp = new JsonFactory().createParser("[\"test\"]");
        DeserializationContext ctxt = new DefaultDeserializationContext.Impl(new ObjectMapper(), new DefaultDeserializationConfig());
        TypeDeserializer typeDeserializer = new AsArrayTypeDeserializer(null, null);
        
        Object result = deserializer.deserializeWithType(jp, ctxt, typeDeserializer);
        
        assertNotNull(result);
        assertTrue(result instanceof String[]);
        String[] arrayResult = (String[]) result;
        assertEquals(1, arrayResult.length);
        assertEquals("test", arrayResult[0]);
    }
    
    @Test
    public void testHandleNonArray() throws Exception {
        StringArrayDeserializer deserializer = new StringArrayDeserializer();
        JsonParser jp = new JsonFactory().createParser("\"test\"");
        DeserializationContext ctxt = new DefaultDeserializationContext.Impl(new ObjectMapper(), new DefaultDeserializationConfig());
        
        String[] result = deserializer.handleNonArray(jp, ctxt);
        
        assertNotNull(result);
        assertEquals(1, result.length);
        assertEquals("test", result[0]);
    }

    @Test
    public void testContextual() throws Exception {
        StringArrayDeserializer deserializer = new StringArrayDeserializer();
        DeserializationContext ctxt = new DefaultDeserializationContext.Impl(new ObjectMapper(), new DefaultDeserializationConfig());
        BeanProperty property = new BeanProperty.Std(null, String.class, null, DeserializationConfig.Feature.ACCEPT_SINGLE_VALUE_AS_ARRAY);
        
        JsonDeserializer<?> result = deserializer.createContextual(ctxt, property);
        
        assertNull(result);
    }
}