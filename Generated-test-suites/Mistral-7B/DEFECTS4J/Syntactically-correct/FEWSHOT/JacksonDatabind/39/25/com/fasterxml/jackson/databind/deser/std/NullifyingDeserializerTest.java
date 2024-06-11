package com.fasterxml.jackson.databind.deser.std;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.TypeDeserializer;
import org.junit.Test;
import static org.mockito.Mockito.*;
import static org.junit.Assert.assertNull;

public class NullifyingDeserializerTest {

    @Test
    public void testNullifyingDeserializerDefaultCase() throws Exception {
        JsonParser mockParser = mock(JsonParser.class);
        DeserializationContext mockCtx = mock(DeserializationContext.class);

        when(mockParser.getCurrentTokenId()).thenReturn(JsonTokenId.ID_VALUE_STRING.getId());

        Object result = new NullifyingDeserializer().deserialize(mockParser, mockCtx);

        assertNull(result);

        verify(mockParser, times(1)).skipChildren();
        verifyNoMoreInteractions(mockParser);
    }

    @Test
    public void testNullifyingDeserializerForArray() throws Exception {
        JsonParser mockParser = mock(JsonParser.class);
        DeserializationContext mockCtx = mock(DeserializationContext.class);
        TypeDeserializer typeDeserializer = mock(TypeDeserializer.class);

        when(mockParser.getCurrentTokenId()).thenReturn(JsonTokenId.ID_START_ARRAY.getId());

        new NullifyingDeserializer().deserializeWithType(mockParser, mockCtx, typeDeserializer);

        verify(mockParser, times(1)).nextToken();
        verify(mockParser, times(1)).skipChildren();
        verifyNoMoreInteractions(mockParser);
    }

    @Test
    public void testNullifyingDeserializerForObject() throws Exception {
        JsonParser mockParser = mock(JsonParser.class);
        DeserializationContext mockCtx = mock(DeserializationContext.class);
        TypeDeserializer typeDeserializer = mock(TypeDeserializer.class);

        when(mockParser.getCurrentTokenId()).thenReturn(JsonTokenId.ID_START_OBJECT.getId());

        new NullifyingDeserializer().deserializeWithType(mockParser, mockCtx, typeDeserializer);

        verify(mockParser, times(1)).nextToken();
        verify(mockParser, times(1)).nextToken(); // skip field name
        verify(mockParser, times(1)).skipChildren();
        verifyNoMoreInteractions(mockParser);
    }

    @Test
    public void testNullifyingDeserializerForFieldName() throws Exception {
        JsonParser mockParser = mock(JsonParser.class);
        DeserializationContext mockCtx = mock(DeserializationContext.class);
        TypeDeserializer typeDeserializer = mock(TypeDeserializer.class);

        when(mockParser.getCurrentTokenId()).thenReturn(JsonTokenId.ID_FIELD_NAME.getId());

        new NullifyingDeserializer().deserializeWithType(mockParser, mockCtx, typeDeserializer);

        verify(mockParser, times(1)).nextToken(); // skip field name
        verify(typeDeserializer, times(1)).deserializeTypedFromAny(mockParser, mockCtx);
        verifyNoMoreInteractions(mockParser);
    }

    @Test
    public void testNullifyingDeserializerForOtherTokens() throws Exception {
        JsonParser mockParser = mock(JsonParser.class);
        DeserializationContext mockCtx = mock(DeserializationContext.class);
        TypeDeserializer typeDeserializer = mock(TypeDeserializer.class);

        when(mockParser.getCurrentTokenId()).thenReturn(JsonTokenId.ID_VALUE_NUMBER_INT.getId());

        new NullifyingDeserializer().deserializeWithType(mockParser, mockCtx, typeDeserializer);

        verify(mockParser, times(1)).skipChildren();
        verifyNoMoreInteractions(mockParser);
    }
}