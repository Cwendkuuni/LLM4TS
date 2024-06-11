package com.fasterxml.jackson.databind.deser.std;

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonTokenId;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;

public class NullifyingDeserializerTest {

    @Test
    public void testDeserialize() throws IOException {
        JsonParser jsonParser = mock(JsonParser.class);
        DeserializationContext deserializationContext = mock(DeserializationContext.class);
        
        NullifyingDeserializer nullifyingDeserializer = new NullifyingDeserializer();
        Object result = nullifyingDeserializer.deserialize(jsonParser, deserializationContext);
        
        verify(jsonParser).skipChildren();
        assertEquals(null, result);
    }

    @Test
    public void testDeserializeWithType() throws IOException {
        JsonParser jsonParser = mock(JsonParser.class);
        DeserializationContext deserializationContext = mock(DeserializationContext.class);
        TypeDeserializer typeDeserializer = mock(TypeDeserializer.class);
        
        NullifyingDeserializer nullifyingDeserializer = new NullifyingDeserializer();
        Object result = nullifyingDeserializer.deserializeWithType(jsonParser, deserializationContext, typeDeserializer);
        
        verify(jsonParser).getCurrentTokenId();
        verify(typeDeserializer).deserializeTypedFromAny(jsonParser, deserializationContext);
        assertEquals(null, result);
    }
}