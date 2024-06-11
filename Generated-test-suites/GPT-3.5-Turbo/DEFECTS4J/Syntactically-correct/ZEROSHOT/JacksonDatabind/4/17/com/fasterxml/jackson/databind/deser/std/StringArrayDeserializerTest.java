package com.fasterxml.jackson.databind.deser.std;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class StringArrayDeserializerTest {
    
    private StringArrayDeserializer deserializer;
    
    @Before
    public void setUp() {
        deserializer = new StringArrayDeserializer();
    }
    
    @Test
    public void testDeserialize() throws Exception {
        String[] expected = {"hello", "world"};
        String json = "[\"hello\", \"world\"]";
        ObjectMapper mapper = new ObjectMapper();
        JsonParser parser = mapper.getFactory().createParser(json);
        DeserializationContext ctxt = mapper.getDeserializationContext();
        
        String[] result = deserializer.deserialize(parser, ctxt);
        
        assertArrayEquals(expected, result);
    }
    
    @Test
    public void testDeserializeWithNullValue() throws Exception {
        String[] expected = {"hello", null, "world"};
        String json = "[\"hello\", null, \"world\"]";
        ObjectMapper mapper = new ObjectMapper();
        JsonParser parser = mapper.getFactory().createParser(json);
        DeserializationContext ctxt = mapper.getDeserializationContext();
        
        String[] result = deserializer.deserialize(parser, ctxt);
        
        assertArrayEquals(expected, result);
    }
    
    @Test
    public void testDeserializeWithType() throws Exception {
        String[] expected = {"hello", "world"};
        String json = "[\"hello\", \"world\"]";
        ObjectMapper mapper = new ObjectMapper();
        JsonParser parser = mapper.getFactory().createParser(json);
        DeserializationContext ctxt = mapper.getDeserializationContext();
        TypeDeserializer typeDeserializer = null; // need to create the type deserializer
        
        Object result = deserializer.deserializeWithType(parser, ctxt, typeDeserializer);
        
        assertTrue(result instanceof String[]);
        assertArrayEquals(expected, (String[]) result);
    }
    
    @Test
    public void testHandleNonArray() throws Exception {
        String[] expected = {"hello"};
        String json = "\"hello\"";
        ObjectMapper mapper = new ObjectMapper();
        JsonParser parser = mapper.getFactory().createParser(json);
        DeserializationContext ctxt = mapper.getDeserializationContext();
        
        String[] result = deserializer.handleNonArray(parser, ctxt);
        
        assertArrayEquals(expected, result);
    }
    
    @Test(expected = Exception.class)
    public void testHandleNonArrayWithInvalidValue() throws Exception {
        String json = "123";
        ObjectMapper mapper = new ObjectMapper();
        JsonParser parser = mapper.getFactory().createParser(json);
        DeserializationContext ctxt = mapper.getDeserializationContext();
        
        deserializer.handleNonArray(parser, ctxt);
        // Exception should be thrown
    }
    
    @Test
    public void testCreateContextual() throws Exception {
        StringArrayDeserializer otherDeserializer = new StringArrayDeserializer();
        
        JsonDeserializer<?> contextualizedDeserializer = deserializer.createContextual(ctxt, null);
        JsonDeserializer<?> expected = deserializer.createContextual(ctxt, null);
        
        assertEquals(expected, contextualizedDeserializer);
        
        JsonDeserializer<?> otherContextualizedDeserializer = otherDeserializer.createContextual(ctxt, null);
        
        assertNotEquals(otherContextualizedDeserializer, contextualizedDeserializer);
    }
}