package com.fasterxml.jackson.databind.deser.std;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.deser.std.StringCollectionDeserializer;
import com.fasterxml.jackson.databind.deser.std.StringCollectionDeserializerTest;

public class StringCollectionDeserializerTest {

    private StringCollectionDeserializer stringCollectionDeserializer;
    
    @Before
    public void setup() {
        stringCollectionDeserializer = new StringCollectionDeserializer(null, null, null);
    }
    
    // Test deserialize method
    @Test
    public void testDeserialize() throws IOException {
        String json = "[\"value1\", \"value2\"]";
        JsonParser jsonParser = new TestJsonParser(json);
        DeserializationContext ctxt = null;
        Collection<String> result = stringCollectionDeserializer.deserialize(jsonParser, ctxt);
        
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("value1", result.toArray()[0]);
        assertEquals("value2", result.toArray()[1]);
    }

    // Test deserialize using custom method
    @Test
    public void testDeserializeUsingCustom() throws IOException {
        String json = "[\"value1\", \"value2\"]";
        JsonParser jsonParser = new TestJsonParser(json);
        DeserializationContext ctxt = null;
        Collection<String> result = new ArrayList<>();
        JsonDeserializer<String> valueDeser = new TestStringDeserializer();
        Collection<String> finalResult = stringCollectionDeserializer.deserializeUsingCustom(jsonParser, ctxt, result, valueDeser);
        
        assertNotNull(finalResult);
        assertEquals(2, finalResult.size());
        assertEquals("value1-custom", finalResult.toArray()[0]);
        assertEquals("value2-custom", finalResult.toArray()[1]);
    }

    // Test handleNonArray method
    @Test
    public void testHandleNonArray() throws IOException {
        String json = "\"value1\"";
        JsonParser jsonParser = new TestJsonParser(json);
        DeserializationContext ctxt = null;
        Collection<String> result = new ArrayList<>();
        Collection<String> finalResult = stringCollectionDeserializer.handleNonArray(jsonParser, ctxt, result);
        
        assertNotNull(finalResult);
        assertEquals(1, finalResult.size());
        assertEquals("value1", finalResult.toArray()[0]);
    }
    
    // Custom JsonParser implementation for testing purposes
    private class TestJsonParser extends JsonParser {
        
        private String json;
        
        public TestJsonParser(String json) {
            this.json = json;
        }
        
        @Override
        public String nextTextValue() throws IOException {
            return null;
        }
      
        // Other overridden methods
        
    }
    
    // Custom JsonDeserializer implementation for testing purposes
    private class TestStringDeserializer extends JsonDeserializer<String> {

        @Override
        public String deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
            return p.getValueAsString() + "-custom";
        }
      
        // Other overridden methods
        
    }

}