package com.example.test;

import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.std.NullifyingDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;

import org.junit.Test;

public class NullifyingDeserializerTest {

    @Test
    public void testDeserialize() throws IOException {
        JsonParser jsonParser = mock(JsonParser.class);
        DeserializationContext deserializationContext = mock(DeserializationContext.class);

        when(jsonParser.getCurrentTokenId()).thenReturn(JsonTokenId.ID_START_OBJECT.getId());

        NullifyingDeserializer deserializer = new NullifyingDeserializer();
        Object result = deserializer.deserialize(jsonParser, deserializationContext);

        assertNull(result);
    }

    @Test
    public void testDeserializeWithType() throws IOException {
        JsonParser jsonParser = mock(JsonParser.class);
        DeserializationContext deserializationContext = mock(DeserializationContext.class);
        TypeDeserializer typeDeserializer = mock(TypeDeserializer.class);

        when(jsonParser.getCurrentTokenId()).thenReturn(JsonTokenId.ID_START_ARRAY.getId());
        when(typeDeserializer.deserializeTypedFromAny(jsonParser, deserializationContext)).thenReturn("TestType");

        NullifyingDeserializer deserializer = new NullifyingDeserializer();
        Object result = deserializer.deserializeWithType(jsonParser, deserializationContext, typeDeserializer);

        assertNull(result);
    }
}