package com.fasterxml.jackson.databind.deser.std;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import java.io.IOException;

import org.junit.Test;

import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonParser;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;

public class NullifyingDeserializerTest {
    @Test
    public void testDeserialize() throws IOException {
        JsonParser p = mock(JsonParser.class);
        DeserializationContext ctxt = mock(DeserializationContext.class);
        NullifyingDeserializer deserializer = NullifyingDeserializer.instance;
        
        assertNull(deserializer.deserialize(p, ctxt));
        verify(p).skipChildren();
    }

    @Test
    public void testDeserializeWithType() throws IOException {
        JsonParser p = mock(JsonParser.class);
        DeserializationContext ctxt = mock(DeserializationContext.class);
        TypeDeserializer typeDeserializer = mock(TypeDeserializer.class);
        NullifyingDeserializer deserializer = NullifyingDeserializer.instance;
        
        assertNull(deserializer.deserializeWithType(p, ctxt, typeDeserializer));
        
        // Make sure the correct method in TypeDeserializer is called based on the current token ID
        verify(typeDeserializer).deserializeTypedFromAny(p, ctxt);
    }
}