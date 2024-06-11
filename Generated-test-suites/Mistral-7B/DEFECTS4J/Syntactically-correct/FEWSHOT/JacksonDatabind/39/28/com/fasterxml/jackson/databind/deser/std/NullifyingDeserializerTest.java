package com.fasterxml.jackson.databind.deser.std;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import org.junit.Test;
import static org.mockito.Mockito.*;
import static org.junit.Assert.*;

public class NullifyingDeserializerTest {

    @Test
    public void testDeserialize() throws IOException {
        JsonParser mockJsonParser = mock(JsonParser.class);
        DeserializationContext mockContext = mock(DeserializationContext.class);

        Object result = NullifyingDeserializer.instance.deserialize(mockJsonParser, mockContext);
        assertNull(result);

        verify(mockJsonParser, times(1)).skipChildren();
    }

    @Test
    public void testDeserializeWithType() throws IOException {
        JsonParser mockJsonParser = mock(JsonParser.class);
        DeserializationContext mockContext = mock(DeserializationContext.class);
        TypeDeserializer typeDeserializer = mock(TypeDeserializer.class);

        when(mockJsonParser.getCurrentTokenId()).thenReturn(JsonTokenId.ID_FIELD_NAME.asInt());

        Object result = NullifyingDeserializer.instance.deserializeWithType(mockJsonParser, mockContext, typeDeserializer);
        assertNull(result);

        verify(mockJsonParser, times(1)).skipChildren();
        verify(typeDeserializer, times(1)).deserializeTypedFromAny(mockJsonParser, mockContext);
    }

    @Test(expected = JsonMappingException.class)
    public void testDeserializeWithUnexpectedToken() throws IOException {
        JsonParser mockJsonParser = mock(JsonParser.class);
        DeserializationContext mockContext = mock(DeserializationContext.class);
        TypeDeserializer typeDeserializer = mock(TypeDeserializer.class);

        when(mockJsonParser.getCurrentTokenId()).thenReturn(JsonTokenId.ID_START_ARRAY.asInt());

        NullifyingDeserializer.instance.deserializeWithType(mockJsonParser, mockContext, typeDeserializer);

        verify(mockJsonParser, times(1)).skipChildren();
        verify(typeDeserializer, never()).deserializeTypedFromAny(mockJsonParser, mockContext);

        throw new JsonMappingException("Unexpected token (START_ARRAY) found: can not deserialize Object using NullifyingDeserializer[simpleType, class Object]");
    }
}