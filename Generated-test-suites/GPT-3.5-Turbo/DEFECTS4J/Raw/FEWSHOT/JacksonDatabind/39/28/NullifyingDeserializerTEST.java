package com.fasterxml.jackson.databind.deser.std;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonTokenId;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import org.junit.Test;

public class NullifyingDeserializerTest {
    private NullifyingDeserializer nullifyingDeserializer = new NullifyingDeserializer();

    @Test
    public void testDeserialize() throws IOException {
        // Test case for ID_FIELD_NAME
        JsonParser p = mock(JsonParser.class);
        when(p.getCurrentTokenId()).thenReturn(JsonTokenId.ID_FIELD_NAME);
        Object result = nullifyingDeserializer.deserialize(p, mock(DeserializationContext.class));
        assertNull(result);

        // Test case for ID_START_ARRAY
        p = mock(JsonParser.class);
        when(p.getCurrentTokenId()).thenReturn(JsonTokenId.ID_START_ARRAY);
        result = nullifyingDeserializer.deserialize(p, mock(DeserializationContext.class));
        assertNull(result);

        // Test case for ID_START_OBJECT
        p = mock(JsonParser.class);
        when(p.getCurrentTokenId()).thenReturn(JsonTokenId.ID_START_OBJECT);
        result = nullifyingDeserializer.deserialize(p, mock(DeserializationContext.class));
        assertNull(result);
    }

    @Test
    public void testDeserializeWithType() throws IOException {
        // Test case for ID_FIELD_NAME
        JsonParser p = mock(JsonParser.class);
        when(p.getCurrentTokenId()).thenReturn(JsonTokenId.ID_FIELD_NAME);
        TypeDeserializer typeDeserializer = mock(TypeDeserializer.class);
        when(typeDeserializer.deserializeTypedFromAny(p, mock(DeserializationContext.class))).thenReturn(null);
        Object result = nullifyingDeserializer.deserializeWithType(p, mock(DeserializationContext.class), typeDeserializer);
        assertNull(result);

        // Test case for ID_START_ARRAY
        p = mock(JsonParser.class);
        when(p.getCurrentTokenId()).thenReturn(JsonTokenId.ID_START_ARRAY);
        typeDeserializer = mock(TypeDeserializer.class);
        when(typeDeserializer.deserializeTypedFromAny(p, mock(DeserializationContext.class))).thenReturn(null);
        result = nullifyingDeserializer.deserializeWithType(p, mock(DeserializationContext.class), typeDeserializer);
        assertNull(result);

        // Test case for ID_START_OBJECT
        p = mock(JsonParser.class);
        when(p.getCurrentTokenId()).thenReturn(JsonTokenId.ID_START_OBJECT);
        typeDeserializer = mock(TypeDeserializer.class);
        when(typeDeserializer.deserializeTypedFromAny(p, mock(DeserializationContext.class))).thenReturn(null);
        result = nullifyingDeserializer.deserializeWithType(p, mock(DeserializationContext.class), typeDeserializer);
        assertNull(result);
    }
}