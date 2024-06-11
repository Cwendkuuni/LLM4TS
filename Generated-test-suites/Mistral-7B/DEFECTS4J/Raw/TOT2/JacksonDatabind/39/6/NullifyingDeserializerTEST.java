package com.yourpackage;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonTokenId;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.TypeDeserializer;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.*;

public class NullifyingDeserializerTest {

    @Test
    public void testNullifyingDeserializer_deserialize() throws IOException {
        JsonParser mockJsonParser = mock(JsonParser.class);
        DeserializationContext mockContext = mock(DeserializationContext.class);

        NullifyingDeserializer nullifyingDeserializer = new NullifyingDeserializer();

        when(mockJsonParser.getCurrentTokenId()).thenReturn(JsonTokenId.ID_START_ARRAY.getValue());

        Object result = nullifyingDeserializer.deserialize(mockJsonParser, mockContext);
        assertNull(result);

        verify(mockJsonParser, times(1)).skipChildren();
    }

    @Test
    public void testNullifyingDeserializer_deserializeWithType() throws IOException {
        JsonParser mockJsonParser = mock(JsonParser.class);
        DeserializationContext mockContext = mock(DeserializationContext.class);
        TypeDeserializer mockTypeDeserializer = mock(TypeDeserializer.class);

        NullifyingDeserializer nullifyingDeserializer = new NullifyingDeserializer();

        when(mockJsonParser.getCurrentTokenId()).thenReturn(JsonTokenId.ID_FIELD_NAME.getValue());
        when(mockTypeDeserializer.deserializeTypedFromAny(mockJsonParser, mockContext)).thenReturn(new Object());

        Object result = nullifyingDeserializer.deserializeWithType(mockJsonParser, mockContext, mockTypeDeserializer);
        assertNull(result);

        verify(mockJsonParser, times(1)).skipChildren();
    }
}