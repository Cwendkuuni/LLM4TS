
package com.fasterxml.jackson.databind.deser.std;

import org.junit.Test;
import org.junit.Assert;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;

public class NullifyingDeserializerTest {

    @Test
    public void testDeserialize() throws Exception {
        NullifyingDeserializer deserializer = NullifyingDeserializer.instance;
        JsonFactory jsonFactory = new JsonFactory(new ObjectMapper());
        JsonParser parser = jsonFactory.createParser("data"); // Replace "data" with valid JSON content
        
        DeserializationContext ctxt = new DeserializationContext(new ObjectMapper(), jsonFactory);
        Object result = deserializer.deserialize(parser, ctxt);
        
        Assert.assertNull(result);
    }

    @Test
    public void testDeserializeWithType() throws Exception {
        NullifyingDeserializer deserializer = NullifyingDeserializer.instance;
        JsonFactory jsonFactory = new JsonFactory(new ObjectMapper());
        JsonParser parser = jsonFactory.createParser("data"); // Replace "data" with valid JSON content
        
        DeserializationContext ctxt = new DeserializationContext(new ObjectMapper(), jsonFactory);
        TypeDeserializer typeDeserializer = null; // Replace null with the actual TypeDeserializer
        Object result = deserializer.deserializeWithType(parser, ctxt, typeDeserializer);
        
        Assert.assertNull(result);
    }
}
