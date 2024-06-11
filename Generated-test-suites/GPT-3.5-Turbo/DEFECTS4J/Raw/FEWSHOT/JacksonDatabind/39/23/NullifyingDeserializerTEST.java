package com.fasterxml.jackson.databind.deser.std;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import java.io.IOException;
import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;

import org.junit.Test;

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
        
        verify(typeDeserializer).deserializeTypedFromAny(p, ctxt);
    }
}