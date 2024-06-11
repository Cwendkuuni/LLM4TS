package com.fasterxml.jackson.databind.deser.std;

import java.io.IOException;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.JacksonStdImpl;
import com.fasterxml.jackson.databind.deser.ContextualDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.util.ObjectBuffer;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

@JacksonStdImpl
public class StringArrayDeserializerTest {
    
    private StringArrayDeserializer deserializer;
    
    @Before
    public void setup() {
        deserializer = new StringArrayDeserializer();
    }
    
    @Test
    public void testDeserialize_WithExpectedStartArrayToken_ReturnsStringArray() throws IOException {
        JsonParser jsonParser = null; // replace with valid JsonParser object
        DeserializationContext ctxt = null; // replace with valid DeserializationContext object
        
        String[] result = deserializer.deserialize(jsonParser, ctxt);
        
        assertNotNull(result);
        assertTrue(result instanceof String[]);
    }
    
    @Test
    public void testDeserialize_WithElementDeserializer_ReturnsStringArray() throws IOException {
        JsonParser jsonParser = null; // replace with valid JsonParser object
        DeserializationContext ctxt = null; // replace with valid DeserializationContext object
        
        String[] result = deserializer.deserialize(jsonParser, ctxt);
        
        assertNotNull(result);
        assertTrue(result instanceof String[]);
    }
    
    @Test
    public void testDeserializeWithType_ReturnsObject() throws IOException {
        JsonParser jsonParser = null; // replace with valid JsonParser object
        DeserializationContext ctxt = null; // replace with valid DeserializationContext object
        TypeDeserializer typeDeserializer = null; // replace with valid TypeDeserializer object
        
        Object result = deserializer.deserializeWithType(jsonParser, ctxt, typeDeserializer);
        
        assertNotNull(result);
    }
    
    @Test
    public void testHandleNonArray_WithAcceptSingleValueAsArrayDisabled_ThrowsMappingException() throws IOException {
        JsonParser jsonParser = null; // replace with valid JsonParser object
        DeserializationContext ctxt = null; // replace with valid DeserializationContext object
        String expectedMessage = "Expected [Ljava.lang.String; but found " + jsonParser.getCurrentToken().name();
        
        try {
            deserializer.handleNonArray(jsonParser, ctxt);
            fail("Expected MappingException");
        } catch (MappingException e) {
            assertEquals(expectedMessage, e.getMessage());
        }
    }
    
    @Test
    public void testHandleNonArray_WithAcceptSingleValueAsArrayEnabled_ReturnsStringArray() throws IOException {
        JsonParser jsonParser = null; // replace with valid JsonParser object
        DeserializationContext ctxt = null; // replace with valid DeserializationContext object
        
        String[] result = deserializer.handleNonArray(jsonParser, ctxt);
        
        assertNotNull(result);
        assertTrue(result instanceof String[]);
    }
    
    @Test
    public void testCreateContextual_WithDefaultDeserializer_ReturnsStringArrayDeserializer() throws JsonMappingException {
        DeserializationContext ctxt = null; // replace with valid DeserializationContext object
        BeanProperty property = null; // replace with valid BeanProperty object
        JsonDeserializer<String> deser = null; // replace with valid JsonDeserializer<String> object
        
        JsonDeserializer<?> result = deserializer.createContextual(ctxt, property);
        
        assertNotNull(result);
        assertTrue(result instanceof StringArrayDeserializer);
        assertNotEquals(deserializer, result);
    }
    
    @Test
    public void testCreateContextual_WithCustomDeserializer_ReturnsStringArrayDeserializerWithCustomDeserializer() throws JsonMappingException {
        DeserializationContext ctxt = null; // replace with valid DeserializationContext object
        BeanProperty property = null; // replace with valid BeanProperty object
        JsonDeserializer<String> deser = null; // replace with valid JsonDeserializer<String> object
        
        StringArrayDeserializer customDeserializer = new StringArrayDeserializer(deser);
        JsonDeserializer<?> result = customDeserializer.createContextual(ctxt, property);
        
        assertNotNull(result);
        assertTrue(result instanceof StringArrayDeserializer);
        assertNotEquals(customDeserializer, result);
    }
}