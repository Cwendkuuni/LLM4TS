import static org.junit.Assert.*;
import java.io.IOException;
import org.junit.Test;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.util.ObjectBuffer;

public class StringArrayDeserializerTest {

    @Test
    public void testDeserialize() throws IOException {
        StringArrayDeserializer deserializer = new StringArrayDeserializer();
        
        // Test case 1: Empty array
        JsonParser jp1 = new MockJsonParser(JsonToken.START_ARRAY, JsonToken.END_ARRAY);
        DeserializationContext ctxt1 = new DeserializationContext(null, null, null);
        String[] result1 = deserializer.deserialize(jp1, ctxt1);
        assertNotNull(result1);
        assertEquals(0, result1.length);
        
        // Test case 2: Array with strings
        JsonParser jp2 = new MockJsonParser(JsonToken.START_ARRAY, JsonToken.VALUE_STRING, JsonToken.VALUE_STRING, JsonToken.END_ARRAY);
        DeserializationContext ctxt2 = new DeserializationContext(null, null, null);
        String[] result2 = deserializer.deserialize(jp2, ctxt2);
        assertNotNull(result2);
        assertEquals(2, result2.length);
        assertEquals("value1", result2[0]);
        assertEquals("value2", result2[1]);
        
        // Test case 3: Array with null values
        JsonParser jp3 = new MockJsonParser(JsonToken.START_ARRAY, JsonToken.VALUE_NULL, JsonToken.VALUE_NULL, JsonToken.END_ARRAY);
        DeserializationContext ctxt3 = new DeserializationContext(null, null, null);
        String[] result3 = deserializer.deserialize(jp3, ctxt3);
        assertNotNull(result3);
        assertEquals(2, result3.length);
        assertNull(result3[0]);
        assertNull(result3[1]);
    }

    @Test
    public void testDeserializeWithType() throws IOException {
        StringArrayDeserializer deserializer = new StringArrayDeserializer();
        
        JsonParser jp = new MockJsonParser(JsonToken.START_ARRAY, JsonToken.VALUE_STRING, JsonToken.END_ARRAY);
        DeserializationContext ctxt = new DeserializationContext(null, null, null);
        TypeDeserializer typeDeserializer = new TypeDeserializer();
        Object result = deserializer.deserializeWithType(jp, ctxt, typeDeserializer);
        
        assertNull(result);
    }

    @Test
    public void testHandleNonArray() throws IOException {
        StringArrayDeserializer deserializer = new StringArrayDeserializer();
        
        // Test case 1: ACCEPT_SINGLE_VALUE_AS_ARRAY disabled
        DeserializationContext ctxt1 = new DeserializationContext(null, null, null);
        ctxt1.disable(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY);
        JsonParser jp1 = new MockJsonParser(JsonToken.VALUE_STRING);
        try {
            deserializer.handleNonArray(jp1, ctxt1);
            fail("Expected IOException");
        } catch (IOException e) {
            assertEquals(ctxt1.mappingException(String[].class).getMessage(), e.getMessage());
        }
        
        // Test case 2: ACCEPT_SINGLE_VALUE_AS_ARRAY enabled
        DeserializationContext ctxt2 = new DeserializationContext(null, null, null);
        ctxt2.enable(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY);
        
        // Test case 2.1: Single string value
        JsonParser jp2_1 = new MockJsonParser(JsonToken.VALUE_STRING, JsonToken.END_ARRAY);
        String[] result2_1 = deserializer.handleNonArray(jp2_1, ctxt2);
        assertNotNull(result2_1);
        assertEquals(1, result2_1.length);
        assertEquals("value", result2_1[0]);
        
        // Test case 2.2: Single null value
        JsonParser jp2_2 = new MockJsonParser(JsonToken.VALUE_NULL, JsonToken.END_ARRAY);
        String[] result2_2 = deserializer.handleNonArray(jp2_2, ctxt2);
        assertNotNull(result2_2);
        assertEquals(1, result2_2.length);
        assertNull(result2_2[0]);
    }

    @Test
    public void testCreateContextual() throws IOException {
        StringArrayDeserializer deserializer = new StringArrayDeserializer();
        
        // Test case 1: deser is null
        DeserializationContext ctxt1 = new DeserializationContext(null, null, null);
        BeanProperty property1 = new BeanProperty();
        JsonDeserializer<?> result1 = deserializer.createContextual(ctxt1, property1);
        assertNull(result1);
        
        // Test case 2: deser is not null and is the default String deserializer
        DeserializationContext ctxt2 = new DeserializationContext(null, null, null);
        BeanProperty property2 = new BeanProperty();
        JsonDeserializer<?> deser2 = new DefaultStringDeserializer();
        JsonDeserializer<?> result2 = deserializer.createContextual(ctxt2, property2);
        assertNotSame(deser2, result2);
        
        // Test case 3: deser is not null and is not the default String deserializer
        DeserializationContext ctxt3 = new DeserializationContext(null, null, null);
        BeanProperty property3 = new BeanProperty();
        JsonDeserializer<?> deser3 = new CustomStringDeserializer();
        JsonDeserializer<?> result3 = deserializer.createContextual(ctxt3, property3);
        assertNotSame(deser3, result3);
    }
    
    private class MockJsonParser extends JsonParser {
        private JsonToken[] tokens;
        private int index;
        
        public MockJsonParser(JsonToken... tokens) {
            this.tokens = tokens;
            this.index = 0;
        }
        
        @Override
        public boolean isExpectedStartArrayToken() {
            return tokens[index] == JsonToken.START_ARRAY;
        }

        @Override
        public boolean isExpectedStartObjectToken() {
            return false;
        }

        @Override
        public JsonToken nextToken() {
            if (index < tokens.length) {
                return tokens[index++];
            }
            return null;
        }

        @Override
        public String getText() {
            if (tokens[index] == JsonToken.VALUE_STRING) {
                return "value";
            }
            return null;
        }

        @Override
        public JsonToken getCurrentToken() {
            return tokens[index];
        }

        @Override
        public Object getEmbeddedObject() {
            return null;
        }

        @Override
        public JsonParser skipChildren() {
            return null;
        }

        // Other overridden methods...
    }
    
    private class TypeDeserializer {
        public Object deserializeTypedFromArray(JsonParser jp, DeserializationContext ctxt) throws IOException {
            return null;
        }
    }
    
    private class DefaultStringDeserializer extends JsonDeserializer<String> {
        @Override
        public String deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
            return null;
        }
    }
    
    private class CustomStringDeserializer extends JsonDeserializer<String> {
        @Override
        public String deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
            return null;
        }
    }
    
    private class BeanProperty { }

}