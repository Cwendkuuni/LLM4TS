package com.fasterxml.jackson.databind.deser.std;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonTokenId;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import org.junit.Test;

public class NullifyingDeserializerTest {
    @Test
    public void testDeserialize() throws IOException {
        // Create mock objects
        JsonParser p = mock(JsonParser.class);
        DeserializationContext ctxt = mock(DeserializationContext.class);

        // Test cases
        assertEquals(null, NullifyingDeserializer.instance.deserialize(p, ctxt));
    }

    @Test
    public void testDeserializeWithType() throws IOException {
        // Create mock objects
        JsonParser p = mock(JsonParser.class);
        DeserializationContext ctxt = mock(DeserializationContext.class);
        TypeDeserializer typeDeserializer = mock(TypeDeserializer.class);

        // Set up mock responses
        when(p.getCurrentTokenId()).thenReturn(JsonTokenId.ID_START_ARRAY, JsonTokenId.ID_START_OBJECT, JsonTokenId.ID_FIELD_NAME);
        when(typeDeserializer.deserializeTypedFromAny(p, ctxt)).thenReturn(null);

        // Test cases
        assertNull(NullifyingDeserializer.instance.deserializeWithType(p, ctxt, typeDeserializer));
        assertNull(NullifyingDeserializer.instance.deserializeWithType(p, ctxt, typeDeserializer));
        assertNull(NullifyingDeserializer.instance.deserializeWithType(p, ctxt, typeDeserializer));
    }
}