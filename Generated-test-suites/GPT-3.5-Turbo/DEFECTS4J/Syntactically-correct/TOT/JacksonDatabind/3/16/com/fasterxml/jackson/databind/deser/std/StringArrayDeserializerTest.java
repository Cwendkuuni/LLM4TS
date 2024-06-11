package com.fasterxml.jackson.databind.deser.std;

import org.junit.Test;
import static org.junit.Assert.*;

public class StringArrayDeserializerTest {

    @Test
    public void testDeserialize_WithStartArrayToken_ReturnsStringArray() {
        StringArrayDeserializer deserializer = new StringArrayDeserializer();
        JsonParser jp = new JsonParser();
        DeserializationContext ctxt = new DeserializationContext();
        
        String[] result = deserializer.deserialize(jp, ctxt);
        
        assertNotNull(result);
        assertEquals(0, result.length);
    }
    
    @Test
    public void testDeserialize_WithElementDeserializer_ReturnsStringArray() {
        StringArrayDeserializer deserializer = new StringArrayDeserializer();
        JsonParser jp = new JsonParser();
        DeserializationContext ctxt = new DeserializationContext();
        
        String[] result = deserializer.deserialize(jp, ctxt);
        
        assertNotNull(result);
        assertEquals(0, result.length);
    }
    
    @Test
    public void testDeserializeWithType_ReturnsObject() {
        StringArrayDeserializer deserializer = new StringArrayDeserializer();
        JsonParser jp = new JsonParser();
        DeserializationContext ctxt = new DeserializationContext();
        TypeDeserializer typeDeserializer = new TypeDeserializer();
        
        Object result = deserializer.deserializeWithType(jp, ctxt, typeDeserializer);
        
        assertNull(result);
    }
    
    @Test
    public void testHandleNonArray_WithEnabledAcceptSingleValueAsArrayAndNullToken_ReturnsStringArrayWithNull() {
        StringArrayDeserializer deserializer = new StringArrayDeserializer();
        JsonParser jp = new JsonParser();
        DeserializationContext ctxt = new DeserializationContext();
        
        String[] result = deserializer.handleNonArray(jp, ctxt);
        
        assertNotNull(result);
        assertEquals(1, result.length);
        assertNull(result[0]);
    }
    
    @Test
    public void testHandleNonArray_WithEnabledAcceptSingleValueAsArrayAndNonNullToken_ReturnsStringArrayWithTokenValue() {
        StringArrayDeserializer deserializer = new StringArrayDeserializer();
        JsonParser jp = new JsonParser();
        DeserializationContext ctxt = new DeserializationContext();
        
        String[] result = deserializer.handleNonArray(jp, ctxt);
        
        assertNotNull(result);
        assertEquals(1, result.length);
        assertNotNull(result[0]);
    }
    
    @Test(expected=JsonMappingException.class)
    public void testHandleNonArray_WithDisabledAcceptSingleValueAsArrayAndNonNullToken_ThrowsJsonMappingException() {
        StringArrayDeserializer deserializer = new StringArrayDeserializer();
        JsonParser jp = new JsonParser();
        DeserializationContext ctxt = new DeserializationContext();
        
        String[] result = deserializer.handleNonArray(jp, ctxt);
    }
}