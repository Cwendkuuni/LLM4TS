package com.fasterxml.jackson.databind.deser.std;

import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;

import java.io.IOException;

import org.junit.Test;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;

public class NullifyingDeserializerTest {
    @Test
    public void testDeserialize() throws IOException {
        JsonParser jsonParser = mock(JsonParser.class);
        DeserializationContext deserializationContext = mock(DeserializationContext.class);

        NullifyingDeserializer nullifyingDeserializer = new NullifyingDeserializer();
        assertNull(nullifyingDeserializer.deserialize(jsonParser, deserializationContext));
    }

    @Test
    public void testDeserializeWithType() throws IOException {
        JsonParser jsonParser = mock(JsonParser.class);
        DeserializationContext deserializationContext = mock(DeserializationContext.class);
        TypeDeserializer typeDeserializer = mock(TypeDeserializer.class);

        NullifyingDeserializer nullifyingDeserializer = new NullifyingDeserializer();
        assertNull(nullifyingDeserializer.deserializeWithType(jsonParser, deserializationContext, typeDeserializer));
    }
}