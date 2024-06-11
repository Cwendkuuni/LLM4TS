package com.fasterxml.jackson.databind.deser.std;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.io.IOException;

import org.junit.Test;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;

public class NullifyingDeserializerTest {
    
    @Test
    public void testDeserialize() throws IOException {
        NullifyingDeserializer deserializer = NullifyingDeserializer.instance;
        JsonFactory jsonFactory = new JsonFactory();
        
        JsonParser parser = jsonFactory.createParser("some json content");
        
        DeserializationContext deserializationContext = new ObjectMapper().getDeserializationContext();
        
        assertNull(deserializer.deserialize(parser, deserializationContext));
    }
    
    @Test
    public void testDeserializeWithType() throws IOException {
        NullifyingDeserializer deserializer = NullifyingDeserializer.instance;
        JsonFactory jsonFactory = new JsonFactory();
        
        JsonParser parser = jsonFactory.createParser("some json content");
        
        DeserializationContext deserializationContext = new ObjectMapper().getDeserializationContext();
        
        TypeDeserializer typeDeserializer = null; // Fill in with appropriate TypeDeserializer
        
        assertNull(deserializer.deserializeWithType(parser, deserializationContext, typeDeserializer));
    }
}