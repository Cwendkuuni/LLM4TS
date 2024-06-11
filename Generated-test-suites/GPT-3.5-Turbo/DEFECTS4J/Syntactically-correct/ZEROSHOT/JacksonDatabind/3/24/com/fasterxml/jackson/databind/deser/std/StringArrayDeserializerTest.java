import static org.junit.Assert.*;
import java.io.IOException;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.std.StringArrayDeserializer;
import org.junit.Test;

public class StringArrayDeserializerTest {
    
    @Test
    public void testDeserialize() throws IOException {
        JsonParser jp = new MappingJsonFactory().createParser("[\"one\", \"two\", \"three\"]");
        DeserializationContext ctxt = new DeserializationContext(new ObjectMapper(), jp.getDeserializationConfig());
        
        StringArrayDeserializer deserializer = new StringArrayDeserializer();
        String[] result = deserializer.deserialize(jp, ctxt);
        
        assertArrayEquals(new String[]{"one", "two", "three"}, result);
    }

    @Test
    public void testDeserializeWithNulls() throws IOException {
        JsonParser jp = new MappingJsonFactory().createParser("[\"one\", null, \"three\"]");
        DeserializationContext ctxt = new DeserializationContext(new ObjectMapper(), jp.getDeserializationConfig());
        
        StringArrayDeserializer deserializer = new StringArrayDeserializer();
        String[] result = deserializer.deserialize(jp, ctxt);
        
        assertArrayEquals(new String[]{"one", null, "three"}, result);
    }

    @Test
    public void testDeserializeEmptyArray() throws IOException {
        JsonParser jp = new MappingJsonFactory().createParser("[]");
        DeserializationContext ctxt = new DeserializationContext(new ObjectMapper(), jp.getDeserializationConfig());
        
        StringArrayDeserializer deserializer = new StringArrayDeserializer();
        String[] result = deserializer.deserialize(jp, ctxt);
        
        assertArrayEquals(new String[0], result);
    }

    @Test
    public void testDeserializeNonArray() throws IOException {
        JsonParser jp = new MappingJsonFactory().createParser("\"one,two,three\"");
        DeserializationContext ctxt = new DeserializationContext(new ObjectMapper(), jp.getDeserializationConfig());
        
        StringArrayDeserializer deserializer = new StringArrayDeserializer();
        String[] result = deserializer.deserialize(jp, ctxt);
        
        assertArrayEquals(new String[]{"one,two,three"}, result);
    }

    @Test
    public void testDeserializeWithType() throws IOException {
        JsonParser jp = new MappingJsonFactory().createParser("[\"one\", \"two\", \"three\"]");
        DeserializationContext ctxt = new DeserializationContext(new ObjectMapper(), jp.getDeserializationConfig());
        TypeDeserializer typeDeserializer = new TypeDeserializer();
        
        StringArrayDeserializer deserializer = new StringArrayDeserializer();
        Object result = deserializer.deserializeWithType(jp, ctxt, typeDeserializer);
        
        assertTrue(result instanceof String[]);
        assertArrayEquals(new String[]{"one", "two", "three"}, (String[]) result);
    }

    @Test
    public void testHandleNonArray() throws IOException {
        JsonParser jp = new MappingJsonFactory().createParser("\"one\"");
        DeserializationContext ctxt = new DeserializationContext(new ObjectMapper(), jp.getDeserializationConfig());
        
        StringArrayDeserializer deserializer = new StringArrayDeserializer();
        String[] result = deserializer.handleNonArray(jp, ctxt);
        
        assertArrayEquals(new String[]{"one"}, result);
    }

    @Test(expected = IOException.class)
    public void testHandleNonArrayNoAcceptSingleValueAsArray() throws IOException {
        JsonParser jp = new MappingJsonFactory().createParser("\"one\"");
        DeserializationContext ctxt = new DeserializationContext(new ObjectMapper(), jp.getDeserializationConfig());
        ctxt.disable(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY);
        
        StringArrayDeserializer deserializer = new StringArrayDeserializer();
        deserializer.handleNonArray(jp, ctxt);
    }

    @Test
    public void testHandleNonArrayEmptyStringAsNullObject() throws IOException {
        JsonParser jp = new MappingJsonFactory().createParser("\"\"");
        DeserializationContext ctxt = new DeserializationContext(new ObjectMapper(), jp.getDeserializationConfig());
        ctxt.enable(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT);
        
        StringArrayDeserializer deserializer = new StringArrayDeserializer();
        String[] result = deserializer.handleNonArray(jp, ctxt);
        
        assertNull(result);
    }

    @Test(expected = IOException.class)
    public void testHandleNonArrayNullObject() throws IOException {
        JsonParser jp = new MappingJsonFactory().createParser("null");
        DeserializationContext ctxt = new DeserializationContext(new ObjectMapper(), jp.getDeserializationConfig());
        
        StringArrayDeserializer deserializer = new StringArrayDeserializer();
        deserializer.handleNonArray(jp, ctxt);
    }

    @Test
    public void testCreateContextual() throws JsonMappingException {
        DeserializationContext ctxt = new DeserializationContext(new ObjectMapper(), null);
        NullValueDeserializer elementDeserializer = new NullValueDeserializer();
        
        StringArrayDeserializer deserializer = new StringArrayDeserializer();
        deserializer._elementDeserializer = elementDeserializer;
        
        JsonDeserializer<?> result = deserializer.createContextual(ctxt, null);
        
        assertSame(elementDeserializer, result);
    }

    @Test
    public void testCreateContextualWithoutElementDeserializer() throws JsonMappingException {
        DeserializationContext ctxt = new DeserializationContext(new ObjectMapper(), null);
        
        StringArrayDeserializer deserializer = new StringArrayDeserializer();
        
        JsonDeserializer<?> result = deserializer.createContextual(ctxt, null);
        
        assertSame(deserializer, result);
    }

    @Test
    public void testCreateContextualWithDefaultElementDeserializer() throws JsonMappingException {
        DeserializationContext ctxt = new DeserializationContext(new ObjectMapper(), null);
        StringArrayDeserializer deserializer = new StringArrayDeserializer();
        deserializer._elementDeserializer = new StringDeserializer();
        
        JsonDeserializer<?> result = deserializer.createContextual(ctxt, null);
        
        assertSame(deserializer, result);
    }
}