package com.fasterxml.jackson.databind.deser.std;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;

import org.junit.Test;
import org.mockito.Mockito;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;

public class NullifyingDeserializerTest {

    @Test
    public void testDeserialize() throws Exception {
        JsonParser p = mock(JsonParser.class);
        DeserializationContext ctxt = mock(DeserializationContext.class);
        
        NullifyingDeserializer nullDeserializer = new NullifyingDeserializer();
        assertNull(nullDeserializer.deserialize(p, ctxt));
        
        Mockito.verify(p).skipChildren();
    }

    @Test
    public void testDeserializeWithType() throws Exception {
        JsonParser p = mock(JsonParser.class);
        DeserializationContext ctxt = mock(DeserializationContext.class);
        TypeDeserializer typeDeserializer = mock(TypeDeserializer.class);

        NullifyingDeserializer nullDeserializer = new NullifyingDeserializer();
        assertNull(nullDeserializer.deserializeWithType(p, ctxt, typeDeserializer));

        Mockito.verify(typeDeserializer).deserializeTypedFromAny(p, ctxt);
    }
}