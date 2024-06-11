package com.fasterxml.jackson.databind.deser.std;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.deser.std.StringArrayDeserializer;

public class StringArrayDeserializerTest {
    
    private StringArrayDeserializer stringArrayDeserializer;
    private JsonParser jsonParser;
    private DeserializationContext deserializationContext;
    
    @Before
    public void setup() {
        stringArrayDeserializer = new StringArrayDeserializer();
        jsonParser = new MockJsonParser();
        deserializationContext = new MockDeserializationContext();
    }
    
    @Test
    public void testDeserialize_NullInput_ReturnsNullArray() throws Exception {
        String[] result = stringArrayDeserializer.deserialize(jsonParser, deserializationContext);
        
        assertNull(result);
    }
    
    @Test
    public void testDeserialize_EmptyArray_ReturnsEmptyArray() throws Exception {
        ((MockJsonParser) jsonParser).addToken(JsonToken.START_ARRAY);
        ((MockJsonParser) jsonParser).addToken(JsonToken.END_ARRAY);
        
        String[] result = stringArrayDeserializer.deserialize(jsonParser, deserializationContext);
        
        assertNotNull(result);
        assertEquals(0, result.length);
    }
    
    @Test
    public void testDeserialize_SingleValueStringArray_ReturnsArrayWithSingleValue() throws Exception {
        ((MockJsonParser) jsonParser).addToken(JsonToken.START_ARRAY);
        ((MockJsonParser) jsonParser).addToken(JsonToken.VALUE_STRING, "value1");
        ((MockJsonParser) jsonParser).addToken(JsonToken.END_ARRAY);
        
        String[] result = stringArrayDeserializer.deserialize(jsonParser, deserializationContext);
        
        assertNotNull(result);
        assertEquals(1, result.length);
        assertEquals("value1", result[0]);
    }
    
    @Test
    public void testDeserialize_ArrayWithNullValue_ReturnsArrayWithNullValue() throws Exception {
        ((MockJsonParser) jsonParser).addToken(JsonToken.START_ARRAY);
        ((MockJsonParser) jsonParser).addToken(JsonToken.VALUE_NULL);
        ((MockJsonParser) jsonParser).addToken(JsonToken.END_ARRAY);
        
        String[] result = stringArrayDeserializer.deserialize(jsonParser, deserializationContext);
        
        assertNotNull(result);
        assertEquals(1, result.length);
        assertNull(result[0]);
    }
    
    @Test
    public void testDeserialize_ArrayWithMixedValues_ReturnsArrayWithValues() throws Exception {
        ((MockJsonParser) jsonParser).addToken(JsonToken.START_ARRAY);
        ((MockJsonParser) jsonParser).addToken(JsonToken.VALUE_STRING, "value1");
        ((MockJsonParser) jsonParser).addToken(JsonToken.VALUE_NULL);
        ((MockJsonParser) jsonParser).addToken(JsonToken.VALUE_STRING, "value2");
        ((MockJsonParser) jsonParser).addToken(JsonToken.END_ARRAY);
        
        String[] result = stringArrayDeserializer.deserialize(jsonParser, deserializationContext);
        
        assertNotNull(result);
        assertEquals(3, result.length);
        assertEquals("value1", result[0]);
        assertNull(result[1]);
        assertEquals("value2", result[2]);
    }
    
    @Test
    public void testDeserialize_NonArrayToken_ThrowsException() throws Exception {
        ((MockJsonParser) jsonParser).addToken(JsonToken.VALUE_STRING, "value");
        
        try {
            String[] result = stringArrayDeserializer.deserialize(jsonParser, deserializationContext);
            fail("Expected IOException to be thrown");
        } catch (IOException e) {
            // Expected
        }
    }
    
    @Test
    public void testDeserialize_CustomValueDeserializer_CallsCustomDeserializer() throws Exception {
        MockDeserializer customDeserializer = new MockDeserializer("custom value");
        stringArrayDeserializer = new StringArrayDeserializer(customDeserializer);
        
        ((MockJsonParser) jsonParser).addToken(JsonToken.START_ARRAY);
        ((MockJsonParser) jsonParser).addToken(JsonToken.VALUE_STRING, "value1");
        ((MockJsonParser) jsonParser).addToken(JsonToken.END_ARRAY);
        
        String[] result = stringArrayDeserializer.deserialize(jsonParser, deserializationContext);
        
        assertNotNull(result);
        assertEquals(1, result.length);
        assertEquals("custom value", result[0]);
    }
    
    @Test
    public void testDeserialize_CustomValueDeserializerWithNullValue_ReturnsArrayWithNullValue() throws Exception {
        MockDeserializer customDeserializer = new MockDeserializer(null);
        stringArrayDeserializer = new StringArrayDeserializer(customDeserializer);
        
        ((MockJsonParser) jsonParser).addToken(JsonToken.START_ARRAY);
        ((MockJsonParser) jsonParser).addToken(JsonToken.VALUE_STRING, "value");
        ((MockJsonParser) jsonParser).addToken(JsonToken.END_ARRAY);
        
        String[] result = stringArrayDeserializer.deserialize(jsonParser, deserializationContext);
        
        assertNotNull(result);
        assertEquals(1, result.length);
        assertNull(result[0]);
    }
    
    @Test
    public void testDeserialize_CustomValueDeserializer_ThrowsExceptionIfNotStringOrNull() throws Exception {
        MockDeserializer customDeserializer = new MockDeserializer("custom value");
        stringArrayDeserializer = new StringArrayDeserializer(customDeserializer);
        
        ((MockJsonParser) jsonParser).addToken(JsonToken.START_ARRAY);
        ((MockJsonParser) jsonParser).addToken(JsonToken.VALUE_NUMBER_INT, 123);
        ((MockJsonParser) jsonParser).addToken(JsonToken.END_ARRAY);
        
        try {
            String[] result = stringArrayDeserializer.deserialize(jsonParser, deserializationContext);
            fail("Expected IOException to be thrown");
        } catch (IOException e) {
            // Expected
        }
    }
    
    private class MockJsonParser extends JsonParser {
        private List<JsonToken> tokens = new ArrayList<>();
        private int index = 0;
        
        public void addToken(JsonToken token) {
            tokens.add(token);
        }
        
        public void addToken(JsonToken token, Object value) {
            tokens.add(token);
            tokens.add(value);
        }
        
        @Override
        public JsonToken nextToken() {
            return tokens.get(index++);
        }
        
        @Override
        public String getText() {
            return (String) tokens.get(index++);
        }
        
        // Implement other methods and stub them as necessary
    }
    
    private class MockDeserializationContext extends DeserializationContext {
        // Implement necessary methods and stub them as necessary
    }
    
    private class MockDeserializer extends JsonDeserializer<String> {
        private String value;
        
        public MockDeserializer(String value) {
            this.value = value;
        }
        
        @Override
        public String deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
            return value;
        }
        
        // Implement other methods and stub them as necessary
    }
}