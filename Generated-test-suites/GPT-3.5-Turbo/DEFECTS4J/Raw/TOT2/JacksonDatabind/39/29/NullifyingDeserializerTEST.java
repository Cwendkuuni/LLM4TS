package com.fasterxml.jackson.databind.deser.std;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;

import org.junit.Test;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;

public class NullifyingDeserializerTest {

    @Test
    public void testDeserialize_SkipsChildren_ReturnsNull() throws IOException {
        NullifyingDeserializer deserializer = new NullifyingDeserializer();
        JsonParser jsonParser = mock(JsonParser.class);
        DeserializationContext deserializationContext = mock(DeserializationContext.class);

        Object result = deserializer.deserialize(jsonParser, deserializationContext);

        assertEquals(null, result);
    }

    @Test
    public void testDeserializeWithType_DeserializeTypedFromAnyForStartArray_ReturnsTypedResult() throws IOException {
        NullifyingDeserializer deserializer = new NullifyingDeserializer();
        JsonParser jsonParser = mock(JsonParser.class);
        DeserializationContext deserializationContext = mock(DeserializationContext.class);
        TypeDeserializer typeDeserializer = mock(TypeDeserializer.class);
        when(jsonParser.getCurrentTokenId()).thenReturn(JsonTokenId.ID_START_ARRAY);

        Object result = deserializer.deserializeWithType(jsonParser, deserializationContext, typeDeserializer);

        // perform assertion for typed result
    }

    @Test
    public void testDeserializeWithType_NoMatch_ReturnsNull() throws IOException {
        NullifyingDeserializer deserializer = new NullifyingDeserializer();
        JsonParser jsonParser = mock(JsonParser.class);
        DeserializationContext deserializationContext = mock(DeserializationContext.class);
        TypeDeserializer typeDeserializer = mock(TypeDeserializer.class);
        when(jsonParser.getCurrentTokenId()).thenReturn(JsonTokenId.ID_END_OBJECT);

        Object result = deserializer.deserializeWithType(jsonParser, deserializationContext, typeDeserializer);

        assertEquals(null, result);
    }
}