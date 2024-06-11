package com.fasterxml.jackson.databind.deser.std;

import static org.junit.Assert.*;

import java.io.IOException;

import org.junit.Test;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonTokenId;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;

public class NullifyingDeserializerTest {
    
    @Test
    public void testDeserialize() throws IOException {
        NullifyingDeserializer deserializer = new NullifyingDeserializer();
        JsonParser jsonParser = null; // initialize with the appropriate json parser
        DeserializationContext context = null; // initialize with the appropriate deserialization context
        
        Object result = deserializer.deserialize(jsonParser, context);
        
        assertNull(result);
    }
    
    @Test
    public void testDeserializeWithType() throws IOException {
        NullifyingDeserializer deserializer = new NullifyingDeserializer();
        JsonParser jsonParser = null; // initialize with the appropriate json parser
        DeserializationContext context = null; // initialize with the appropriate deserialization context
        TypeDeserializer typeDeserializer = null; // initialize with the appropriate type deserializer
        
        Object result = deserializer.deserializeWithType(jsonParser, context, typeDeserializer);
        
        assertNull(result);
    }
    
    @Test
    public void testDeserializeWithType_ArrayStart() throws IOException {
        NullifyingDeserializer deserializer = new NullifyingDeserializer();
        JsonParser jsonParser = null; // initialize with the appropriate json parser
        DeserializationContext context = null; // initialize with the appropriate deserialization context
        TypeDeserializer typeDeserializer = null; // initialize with the appropriate type deserializer
        
        jsonParser.setCurrentTokenId(JsonTokenId.ID_START_ARRAY);
        Object result = deserializer.deserializeWithType(jsonParser, context, typeDeserializer);
        
        assertNull(result);
    }
    
    @Test
    public void testDeserializeWithType_ObjectStart() throws IOException {
        NullifyingDeserializer deserializer = new NullifyingDeserializer();
        JsonParser jsonParser = null; // initialize with the appropriate json parser
        DeserializationContext context = null; // initialize with the appropriate deserialization context
        TypeDeserializer typeDeserializer = null; // initialize with the appropriate type deserializer
        
        jsonParser.setCurrentTokenId(JsonTokenId.ID_START_OBJECT);
        Object result = deserializer.deserializeWithType(jsonParser, context, typeDeserializer);
        
        assertNull(result);
    }
    
    @Test
    public void testDeserializeWithType_FieldName() throws IOException {
        NullifyingDeserializer deserializer = new NullifyingDeserializer();
        JsonParser jsonParser = null; // initialize with the appropriate json parser
        DeserializationContext context = null; // initialize with the appropriate deserialization context
        TypeDeserializer typeDeserializer = null; // initialize with the appropriate type deserializer
        
        jsonParser.setCurrentTokenId(JsonTokenId.ID_FIELD_NAME);
        Object result = deserializer.deserializeWithType(jsonParser, context, typeDeserializer);
        
        assertNull(result);
    }
    
}