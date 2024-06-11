package com.fasterxml.jackson.databind.deser.std;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import org.junit.Test;

public class NullifyingDeserializerTest {
    @Test
    public void testDeserialize() throws IOException {
        NullifyingDeserializer nullifyingDeserializer = new NullifyingDeserializer();
        JsonParser jsonParser = mock(JsonParser.class);
        DeserializationContext deserializationContext = mock(DeserializationContext.class);
        
        doNothing().when(jsonParser).skipChildren();
        
        assertNull(nullifyingDeserializer.deserialize(jsonParser, deserializationContext));
    }

    @Test
    public void testDeserializeWithType() throws IOException {
        NullifyingDeserializer nullifyingDeserializer = new NullifyingDeserializer();
        JsonParser jsonParser = mock(JsonParser.class);
        DeserializationContext deserializationContext = mock(DeserializationContext.class);
        TypeDeserializer typeDeserializer = mock(TypeDeserializer.class);
        
        assertEquals(null, nullifyingDeserializer.deserializeWithType(jsonParser, deserializationContext, typeDeserializer));
    }
}