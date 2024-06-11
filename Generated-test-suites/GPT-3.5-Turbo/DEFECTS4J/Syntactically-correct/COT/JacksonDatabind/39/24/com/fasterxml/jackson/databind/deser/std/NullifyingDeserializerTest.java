
package com.fasterxml.jackson.databind.deser.std;

import static org.junit.Assert.*;
import org.junit.Test;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;

public class NullifyingDeserializerTest {
  
    // Test deserialize method with valid JsonParser and DeserializationContext
    @Test
    public void testDeserialize_ValidJsonParserAndDeserializationContext() throws Exception {
        NullifyingDeserializer deserializer = NullifyingDeserializer.instance;
        JsonParser p = new JsonParser(new JsonFactory().createParser("{}"));
        DeserializationContext ctxt = new DeserializationContext(new ObjectMapper(),
                DeserializationConfig.emptyInstance(), null);
        
        Object result = deserializer.deserialize(p, ctxt);
        
        assertNull(result);
    }
    
    // Test deserialize method with null JsonParser and DeserializationContext
    @Test
    public void testDeserialize_NullJsonParserAndDeserializationContext() throws Exception {
        NullifyingDeserializer deserializer = NullifyingDeserializer.instance;
        JsonParser p = null;
        DeserializationContext ctxt = null;
        
        Object result = deserializer.deserialize(p, ctxt);
        
        assertNull(result);
    }
    
    // Test deserializeWithType method with valid JsonParser, DeserializationContext and TypeDeserializer
    @Test
    public void testDeserializeWithType_ValidJsonParserDeserializationContextAndTypeDeserializer() throws Exception {
        NullifyingDeserializer deserializer = NullifyingDeserializer.instance;
        JsonParser p = new JsonParser(new JsonFactory().createParser("{}"));
        DeserializationContext ctxt = new DeserializationContext(new ObjectMapper(),
                DeserializationConfig.emptyInstance(), null);
        TypeDeserializer typeDeserializer = null;
        
        Object result = deserializer.deserializeWithType(p, ctxt, typeDeserializer);
        
        assertNull(result);
    }
    
    // Test deserializeWithType method with null JsonParser, DeserializationContext and TypeDeserializer
    @Test
    public void testDeserializeWithType_NullJsonParserDeserializationContextAndTypeDeserializer() throws Exception {
        NullifyingDeserializer deserializer = NullifyingDeserializer.instance;
        JsonParser p = null;
        DeserializationContext ctxt = null;
        TypeDeserializer typeDeserializer = null;
        
        Object result = deserializer.deserializeWithType(p, ctxt, typeDeserializer);
        
        assertNull(result);
    }
  
}
