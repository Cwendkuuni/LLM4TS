package com.fasterxml.jackson.databind.deser.std;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class StringArrayDeserializerTest {

    private StringArrayDeserializer stringArrayDeserializer;

    @Before
    public void setup() {
        stringArrayDeserializer = new StringArrayDeserializer();
    }

    @Test
    public void testDeserialize_typicalCase() throws Exception {
        String json = "[\"apple\", \"banana\", \"orange\"]";
        JsonParser jp = createJsonParser(json);
        DeserializationContext ctxt = createDeserializationContext();
        
        String[] result = stringArrayDeserializer.deserialize(jp, ctxt);
        
        assertEquals(3, result.length);
        assertEquals("apple", result[0]);
        assertEquals("banana", result[1]);
        assertEquals("orange", result[2]);
    }
    
    @Test
    public void testDeserialize_emptyArray() throws Exception {
        String json = "[]";
        JsonParser jp = createJsonParser(json);
        DeserializationContext ctxt = createDeserializationContext();
        
        String[] result = stringArrayDeserializer.deserialize(jp, ctxt);
        
        assertEquals(0, result.length);
    }
    
    @Test
    public void testDeserialize_nullElement() throws Exception {
        String json = "[\"apple\", null, \"orange\"]";
        JsonParser jp = createJsonParser(json);
        DeserializationContext ctxt = createDeserializationContext();
        
        String[] result = stringArrayDeserializer.deserialize(jp, ctxt);
        
        assertEquals(3, result.length);
        assertEquals("apple", result[0]);
        assertNull(result[1]);
        assertEquals("orange", result[2]);
    }
    
    @Test
    public void testDeserialize_invalidJson() throws Exception {
        String json = "[\"apple\", \"banana\",";
        JsonParser jp = createJsonParser(json);
        DeserializationContext ctxt = createDeserializationContext();
        
        try {
            stringArrayDeserializer.deserialize(jp, ctxt);
            fail("Expected IOException to be thrown");
        } catch (IOException e) {
            // expected
        }
    }
    
    @Test
    public void testDeserialize_nonArrayJson() throws Exception {
        String json = "\"apple\"";
        JsonParser jp = createJsonParser(json);
        DeserializationContext ctxt = createDeserializationContext();
        
        String[] result = stringArrayDeserializer.deserialize(jp, ctxt);
        
        assertEquals(1, result.length);
        assertEquals("apple", result[0]);
    }
    
    private JsonParser createJsonParser(String json) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.getFactory().createParser(json);
    }
    
    private DeserializationContext createDeserializationContext() {
        DeserializationContext ctxt = new DeserializationContext(null, null);
        ctxt.enable(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY);
        ctxt.enable(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT);
        return ctxt;
    }
}