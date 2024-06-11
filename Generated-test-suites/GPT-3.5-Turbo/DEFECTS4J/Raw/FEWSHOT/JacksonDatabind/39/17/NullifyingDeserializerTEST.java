package com.fasterxml.jackson.databind.deser.std;

import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.*;

import java.io.IOException;

import org.junit.Test;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;

public class NullifyingDeserializerTest {

    @Test
    public void testDeserialize() throws IOException {
        // Mock the dependencies
        JsonParser parser = mock(JsonParser.class);
        DeserializationContext ctxt = mock(DeserializationContext.class);
        
        // Call the method under test
        Object result = NullifyingDeserializer.instance.deserialize(parser, ctxt);
        
        // Verify the results
        assertNull(result);
        
        // Verify that necessary operations were called on the mock objects
        verify(parser).skipChildren();
    }

    @Test
    public void testDeserializeWithType() throws IOException {
        // Mock the dependencies
        JsonParser parser = mock(JsonParser.class);
        DeserializationContext ctxt = mock(DeserializationContext.class);
        TypeDeserializer typeDeserializer = mock(TypeDeserializer.class);
        
        // Call the method under test
        Object result = NullifyingDeserializer.instance.deserializeWithType(parser, ctxt, typeDeserializer);
        
        // Verify the results
        assertNull(result);
        
        // Verify that necessary operations were called on the mock objects
        verify(typeDeserializer).deserializeTypedFromAny(parser, ctxt);
    }
}